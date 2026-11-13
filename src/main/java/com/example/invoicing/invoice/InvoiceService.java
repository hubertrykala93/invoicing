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
import com.example.invoicing.organization.OrganizationEntity;
import com.example.invoicing.organization.OrganizationRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final ContractorService contractorService;
    private final InvoiceNumberSequenceRepository invoiceNumberSequenceRepository;
    private final OrganizationRepository organizationRepository;

    public Page<InvoiceSummaryResponse> getAll(Pageable pageable, InvoiceFilter filter) {
        log.info("FILTER -> " + filter);
        return invoiceRepository
            .getAllByFilter(
                pageable, filter.getStatus(), filter.getContractorId(), filter.getIssueDateFrom(), filter.getIssueDateTo(),
                filter.getOverdue()
            )
            .map(i -> InvoiceSummaryResponse.from(i));
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

        OrganizationEntity organizationEntity = organizationRepository.findAll().getFirst();

        InvoiceNumberSequenceEntity sequence = invoiceNumberSequenceRepository.findByMonthAndYear(issueMonth, issueYear)
            .orElseGet(() -> new InvoiceNumberSequenceEntity(issueMonth, issueYear, 0, organizationEntity));

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

    @Transactional
    public InvoiceResponse createItem(UUID id, ItemCreateRequest request) {
        InvoiceEntity invoiceEntity = getById(id);

        if (!invoiceEntity.isDraft()) {
            throw new ApiException(
                ErrorCode.INVOICE_NOT_EDITABLE,
                "Items of invoice with id " + id + " cannot be modified because its status is " + invoiceEntity.getStatus()
                    + ". Only " + Status.DRAFT + " invoices can be edited."
            );
        }

        ItemEntity newItem = new ItemEntity();

        newItem.setName(request.getName());
        newItem.setQuantity(request.getQuantity());
        newItem.setUnit(request.getUnit());
        newItem.setNetUnitPrice(request.getNetUnitPrice());
        newItem.setVatRate(request.getVatRate());
        newItem.setInvoice(invoiceEntity);
        newItem.setNetValue(newItem.calculateNetto(newItem));
        newItem.setVatValue(newItem.calculateVat(newItem));
        newItem.setGrossValue(newItem.calculateGross(newItem));
        newItem.setPosition(invoiceEntity.getItems().size() + 1);

        invoiceEntity.getItems().addLast(newItem);

        invoiceRepository.save(invoiceEntity);

        return InvoiceResponse.from(invoiceEntity);
    }

    @Transactional
    public InvoiceResponse updateItem(UUID id, UUID itemId, ItemCreateRequest request) {
        InvoiceEntity invoiceEntity = getById(id);

        if (!invoiceEntity.isDraft()) {
            throw new ApiException(
                ErrorCode.INVOICE_NOT_EDITABLE,
                "Items of invoice with id " + id + " cannot be modified because its status is " + invoiceEntity.getStatus()
                    + ". Only " + Status.DRAFT + " invoices can be edited."
            );
        }

        ItemEntity itemEntity = getItemById(invoiceEntity, itemId);

        itemEntity.setName(request.getName());
        itemEntity.setQuantity(request.getQuantity());
        itemEntity.setUnit(request.getUnit());
        itemEntity.setVatRate(request.getVatRate());
        itemEntity.setNetUnitPrice(request.getNetUnitPrice());
        itemEntity.setNetValue(itemEntity.calculateNetto(itemEntity));
        itemEntity.setVatValue(itemEntity.calculateVat(itemEntity));
        itemEntity.setGrossValue(itemEntity.calculateGross(itemEntity));

        itemEntity.setInvoice(invoiceEntity);

        invoiceRepository.save(invoiceEntity);

        return InvoiceResponse.from(invoiceEntity);
    }

    @Transactional
    public void deleteItem(UUID id, UUID itemId) {
        InvoiceEntity invoiceEntity = getById(id);
        ItemEntity itemEntity = getItemById(invoiceEntity, itemId);

        if (!invoiceEntity.isDraft()) {
            throw new ApiException(
                ErrorCode.INVOICE_NOT_EDITABLE,
                "Items of invoice with id " + id + " cannot be modified because its status is " + invoiceEntity.getStatus()
                    + ". Only " + Status.DRAFT + " invoices can be edited."
            );
        }

        invoiceEntity.removeItem(itemEntity);
    }

    @Transactional
    public InvoiceResponse createPayment(UUID id, PaymentCreateRequest request) {
        InvoiceEntity invoiceEntity = getById(id);

        if (!invoiceEntity.isPayable()) {
            throw new ApiException(
                ErrorCode.INVOICE_NOT_PAYABLE,
                "Payment cannot be registered for invoice with id " + id + " because its status is " + invoiceEntity.getStatus()
                    + ". Payments are accepted only for " + Status.ISSUED + " and " + Status.PARTIALLY_PAID + " invoices."
            );
        }

        if (request.getAmount().compareTo(invoiceEntity.getRemainingAmount()) > 0) {
            throw new ApiException(
                ErrorCode.OVERPAYMENT,
                "Payment amount " + request.getAmount() + " exceeds the remaining amount " + invoiceEntity.getRemainingAmount()
                    + " of invoice with id " + id + "."
            );
        }

        if (request.getPaymentDate().isBefore(invoiceEntity.getIssueDate()) || request.getPaymentDate().isAfter(LocalDate.now())) {
            throw new ApiException(
                ErrorCode.INVALID_PAYMENT_DATE,
                "Payment date " + request.getPaymentDate() + " is invalid. It must be between the issue date "
                    + invoiceEntity.getIssueDate() + " and today (" + LocalDate.now() + ")."
            );
        }

        PaymentEntity paymentEntity = request.toEntity();

        invoiceEntity.addPayment(paymentEntity);

        invoiceRepository.save(invoiceEntity);

        return InvoiceResponse.from(invoiceEntity);
    }

    @Transactional
    public InvoiceResponse deletePayment(UUID id, UUID paymentId) {
        InvoiceEntity invoiceEntity = getById(id);

        if (invoiceEntity.isCancelled()) {
            throw new ApiException(
                ErrorCode.PAYMENT_NOT_DELETABLE,
                "Payments of invoice with id " + id + " cannot be deleted because the invoice is " + Status.CANCELLED + "."
            );
        }

        PaymentEntity paymentEntity = getPaymentById(invoiceEntity, paymentId);

        invoiceEntity.getPayments().remove(paymentEntity);

        invoiceEntity.deletePayment();

        invoiceRepository.save(invoiceEntity);

        return InvoiceResponse.from(invoiceEntity);
    }

    public InvoiceEntity getById(UUID id) {
        return invoiceRepository.findById(id)
            .orElseThrow(() -> new ApiException(ErrorCode.INVOICE_NOT_FOUND, "Invoice with id " + id + " was not found."));
    }

    private ItemEntity getItemById(InvoiceEntity invoiceEntity, UUID itemId) {
        return invoiceEntity.getItems()
            .stream()
            .filter(i -> i.getId().equals(itemId))
            .findFirst()
            .orElseThrow(
                () -> new ApiException(
                    ErrorCode.ITEM_NOT_FOUND,
                    "Item with id " + itemId + " was not found in invoice with id " + invoiceEntity.getId() + "."
                )
            );
    }

    private PaymentEntity getPaymentById(InvoiceEntity invoiceEntity, UUID paymentId) {
        return invoiceEntity.getPayments()
            .stream()
            .filter(p -> p.getId().equals(paymentId))
            .findFirst()
            .orElseThrow(
                () -> new ApiException(
                    ErrorCode.PAYMENT_NOT_FOUND,
                    "Payment with id " + paymentId + " was not found in invoice with id " + invoiceEntity.getId() + "."
                )
            );
    }
}
