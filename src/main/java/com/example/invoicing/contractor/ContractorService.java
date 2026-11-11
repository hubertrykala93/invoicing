package com.example.invoicing.contractor;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.invoicing.common.exception.ApiException;
import com.example.invoicing.common.exception.ErrorCode;
import com.example.invoicing.utils.StringUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContractorService {
    private final ContractorRepository contractorRepository;

    public Page<ContractorResponse> getAll(Pageable pageable, ContractorFilter filter) {
        String search = StringUtils.normalize(filter.getSearch());

        return contractorRepository.getAllByFilter(pageable, filter.getActive(), search)
            .map(c -> ContractorResponse.from(c));
    }

    public ContractorResponse get(UUID id) {
        return ContractorResponse.from(getById(id));
    }

    public ContractorResponse create(ContractorRequest request) {
        if (contractorRepository.existsByNip(request.getNip())) {
            throw new ApiException(ErrorCode.DUPLICATE_NIP, "Contractor with NIP " + request.getNip() + " already exists.");
        }

        ContractorEntity contractorEntity = new ContractorEntity();

        contractorEntity.setName(request.getName());
        contractorEntity.setNip(request.getNip());
        contractorEntity.setAddress(request.getAddress());
        contractorEntity.setEmail(request.getEmail());
        contractorEntity.setDefaultPaymentTermDays(request.getDefaultPaymentTermDays());

        contractorRepository.save(contractorEntity);

        return ContractorResponse.from(contractorEntity);
    }

    public void delete(UUID id) {
        contractorRepository.delete(getById(id));
    }

    public ContractorResponse deactivate(UUID id) {
        ContractorEntity contractorEntity = getById(id);

        if (contractorEntity.getActive() == false) {
            throw new ApiException(ErrorCode.CONTRACTOR_ALREADY_DEACTIVATED, null);
        }

        contractorEntity.deactivate();
        contractorRepository.save(contractorEntity);

        return ContractorResponse.from(contractorEntity);
    }

    public ContractorResponse activate(UUID id) {
        ContractorEntity contractorEntity = getById(id);

        if (contractorEntity.getActive() == true) {
            throw new ApiException(ErrorCode.CONTRACTOR_ALREADY_ACTIVATED, null);
        }

        contractorEntity.activate();
        contractorRepository.save(contractorEntity);

        return ContractorResponse.from(contractorEntity);
    }

    public ContractorResponse update(UUID id, ContractorRequest request) {
        ContractorEntity contractorEntity = getById(id);

        if (!contractorEntity.getNip().equals(request.getNip()) && contractorRepository.existsByNip(request.getNip())) {
            throw new ApiException(ErrorCode.DUPLICATE_NIP, "Contractor with NIP " + request.getNip() + " already exists.");
        }

        contractorEntity.setName(request.getName());
        contractorEntity.setNip(request.getNip());
        contractorEntity.setAddress(request.getAddress());
        contractorEntity.setEmail(request.getEmail());
        contractorEntity.setDefaultPaymentTermDays(request.getDefaultPaymentTermDays());

        contractorRepository.save(contractorEntity);

        return ContractorResponse.from(contractorEntity);
    }

    private ContractorEntity getById(UUID id) {
        return contractorRepository.findById(id)
            .orElseThrow(() -> new ApiException(ErrorCode.CONTRACTOR_NOT_FOUND, "Contractor with id " + id + " was not found."));
    }
}
