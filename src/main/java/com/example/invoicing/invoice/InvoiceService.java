package com.example.invoicing.invoice;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.invoicing.common.exception.ApiException;
import com.example.invoicing.common.exception.ErrorCode;
import com.example.invoicing.contractor.ContractorEntity;
import com.example.invoicing.contractor.ContractorService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final ContractorService contractorService;
    private final InvoiceNumberSequenceRepository invoiceNumberSequenceRepository;

    public Page<InvoiceMinimalResponse> getAll(Pageable pageable, InvoiceFilter filter) {
        return invoiceRepository.getAllByFilter(pageable, filter.getStatus(), filter.getContractorId())
            .map(i -> InvoiceMinimalResponse.from(i));
    }

    public InvoiceResponse create(InvoiceCreateRequest request) {
        ContractorEntity contractorEntity = contractorService.getById(request.getContractorId());

        if (!contractorEntity.isActive()) {
            throw new ApiException(
                ErrorCode.CONTRACTOR_INACTIVE,
                "Contractor with id " + request.getContractorId() + " is inactive. Activate the contractor before creating an invoice."
            );
        }

        InvoiceEntity invoiceEntity = new InvoiceEntity();

        invoiceEntity.setContractor(contractorEntity);
        invoiceEntity.setSaleDate(request.getSaleDate());
        invoiceEntity.setNotes(request.getNotes());

        invoiceEntity.setPaymentTermDays(
            Objects.requireNonNullElse(request.getPaymentTermDays(), contractorEntity.getDefaultPaymentTermDays())
        );

        if (request.getItems() != null) {
            request.getItems().forEach(i -> invoiceEntity.addItem(i.toEntity()));
        }

        invoiceRepository.save(invoiceEntity);

        return InvoiceResponse.from(invoiceEntity);
    }

    public InvoiceResponse get(UUID id) {
        InvoiceEntity invoiceEntity = getById(id);

        return InvoiceResponse.from(invoiceEntity);
    }

    public InvoiceResponse update(UUID id, InvoiceUpdateRequest request) {
        InvoiceEntity invoiceEntity = getById(id);

        if (invoiceEntity.getStatus() != Status.DRAFT) {
            throw new ApiException(
                ErrorCode.INVOICE_NOT_EDITABLE,
                "Invoice with id " + id + " cannot be edited because its status is " + invoiceEntity.getStatus() + ". Only " + Status.DRAFT
                    + " invoices can be edited."
            );
        }

        ContractorEntity contractorEntity = contractorService.getById(request.getContractorId());

        if (!contractorEntity.isActive()) {
            throw new ApiException(
                ErrorCode.CONTRACTOR_INACTIVE,
                "Contractor with id " + request.getContractorId()
                    + " is inactive. Activate the contractor before assigning it to an invoice."
            );
        }

        invoiceEntity.setContractor(contractorEntity);
        invoiceEntity.setSaleDate(request.getSaleDate());
        invoiceEntity.setPaymentTermDays(request.getPaymentTermDays());
        invoiceEntity.setNotes(request.getNotes());

        invoiceRepository.save(invoiceEntity);

        return InvoiceResponse.from(invoiceEntity);
    }

    public void delete(UUID id) {
        InvoiceEntity invoiceEntity = getById(id);

        if (invoiceEntity.getStatus() != Status.DRAFT) {
            throw new ApiException(
                ErrorCode.INVOICE_NOT_DELETABLE,
                "Invoice with id " + id + " cannot be deleted because its status is " + invoiceEntity.getStatus()
                    + ". Only " + Status.DRAFT + " invoices can be deleted."
            );
        }

        invoiceRepository.deleteById(id);
    }

    @Transactional
    public InvoiceResponse issue(UUID id) {
        InvoiceEntity invoiceEntity = getById(id);

        if (!invoiceEntity.getStatus().canTransitionTo(Status.ISSUED)) {
            throw new ApiException(
                ErrorCode.INVALID_STATUS_TRANSITION,
                "Cannot change status of invoice with id " + id + " from " + invoiceEntity.getStatus() + " to " + Status.ISSUED + "."
            );
        }

        if (invoiceEntity.getItems().size() == 0) {
            throw new ApiException(
                ErrorCode.INVOICE_EMPTY,
                "Invoice with id " + id + " has no items. Add at least one item before issuing the invoice."
            );
        }

        int nextMonth = invoiceEntity.getSaleDate().plusMonths(1).getMonthValue();
        int nextYear = invoiceEntity.getSaleDate().plusMonths(1).getYear();

        LocalDate issueDeadline = LocalDate.of(nextYear, nextMonth, 15);
        LocalDate now = LocalDate.now();

        if (issueDeadline.isBefore(now)) {
            throw new ApiException(
                ErrorCode.ISSUE_DEADLINE_EXCEEDED,
                "Invoice with sale date " + invoiceEntity.getSaleDate() + " had to be issued by " + issueDeadline + "."
            );
        }

        if (invoiceEntity.getSaleDate().isAfter(now)) {
            throw new ApiException(
                ErrorCode.SALE_DATE_AFTER_ISSUE_DATE,
                "Sale date " + invoiceEntity.getSaleDate() + " cannot be later than issue date " + now + "."
            );
        }

        invoiceEntity.issue(now);

        int issueMonth = invoiceEntity.getIssueDate().getMonthValue();
        int issueYear = invoiceEntity.getIssueDate().getYear();

        InvoiceNumberSequenceEntity sequence = invoiceNumberSequenceRepository.findByMonthAndYear(issueMonth, issueYear)
            .orElseGet(() -> new InvoiceNumberSequenceEntity(issueMonth, issueYear, 0));

        sequence.setLastNumber(sequence.getLastNumber() + 1);

        invoiceEntity.setNumber(
            "FV/" + sequence.getLastNumber() + "/" + (sequence.getMonth() >= 10 ? sequence.getMonth() : "0" + sequence.getMonth()) + "/"
                + sequence.getYear()
        );

        ContractorEntity contractorEntity = contractorService.getById(invoiceEntity.getContractor().getId());

        BuyerSnapshot buyerSnapshot = new BuyerSnapshot(
            contractorEntity.getName(), contractorEntity.getNip(), contractorEntity.getAddress()
        );

        invoiceEntity.setBuyerSnapshot(buyerSnapshot);

        invoiceNumberSequenceRepository.save(sequence);

        return InvoiceResponse.from(invoiceEntity);
    }

    public InvoiceResponse cancel(UUID id, InvoiceCancelRequest request) {
        InvoiceEntity invoiceEntity = getById(id);

        int numberOfPayments = invoiceEntity.getPayments().size();

        if (!invoiceEntity.getStatus().canTransitionTo(Status.CANCELLED)) {
            throw new ApiException(
                ErrorCode.INVALID_STATUS_TRANSITION,
                "Cannot change status of invoice with id " + id + " from " + invoiceEntity.getStatus() + " to " + Status.CANCELLED + "."
            );
        }

        if (numberOfPayments > 0) {
            throw new ApiException(
                ErrorCode.INVOICE_HAS_PAYMENTS,
                "Invoice with id " + id + " has " + numberOfPayments + " payment(s). Remove all payments before cancelling the invoice."
            );
        }

        invoiceEntity.setCancelReason(request.getCancelReason());
        invoiceEntity.cancel();
        invoiceRepository.save(invoiceEntity);

        return InvoiceResponse.from(invoiceEntity);
    }

    public InvoiceResponse duplicate(UUID id) {
        InvoiceEntity invoiceEntity = getById(id);
        boolean isActiveContractor = invoiceEntity.getContractor().isActive();

        if (!isActiveContractor) {
            throw new ApiException(
                ErrorCode.CONTRACTOR_INACTIVE,
                "Contractor with id " + invoiceEntity.getContractor().getId()
                    + " is inactive. Activate the contractor before duplicating the invoice."
            );
        }

        InvoiceEntity duplicatedInvoice = new InvoiceEntity();

        duplicatedInvoice.setContractor(invoiceEntity.getContractor());
        duplicatedInvoice.setNotes(invoiceEntity.getNotes());
        duplicatedInvoice.setPaymentTermDays(invoiceEntity.getPaymentTermDays());
        duplicatedInvoice.setSaleDate(LocalDate.now());

        invoiceEntity.getItems().stream().map(ItemEntity::copy).forEach(duplicatedInvoice::addItem);

        invoiceRepository.save(duplicatedInvoice);

        return InvoiceResponse.from(duplicatedInvoice);
    }

    public InvoiceEntity getById(UUID id) {
        return invoiceRepository.findById(id)
            .orElseThrow(() -> new ApiException(ErrorCode.INVOICE_NOT_FOUND, "Invoice with id " + id + " was not found."));
    }
}
