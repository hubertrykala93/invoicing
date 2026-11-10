package com.example.invoicing.seller;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.invoicing.common.exception.ApiException;
import com.example.invoicing.common.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SellerService {
    private final SellerRepository sellerRepository;

    public List<SellerResponse> getAll() {
        return sellerRepository.findAll().stream().map(s -> SellerResponse.from(s)).toList();
    }

    public SellerResponse update(UUID id, SellerUpdateRequest request) {
        SellerEntity sellerEntity = getById(id);

        sellerEntity.setAddress(request.getAddress());
        sellerEntity.setBankAccount(request.getBankAccount());
        sellerEntity.setName(request.getName());
        sellerEntity.setNip(request.getNip());

        sellerRepository.save(sellerEntity);

        return SellerResponse.from(sellerEntity);
    }

    private SellerEntity getById(UUID id) {
        return sellerRepository.findById(id)
            .orElseThrow(() -> new ApiException(ErrorCode.SELLER_NOT_FOUND, "Seller with id " + id + " was not found"));
    }
}
