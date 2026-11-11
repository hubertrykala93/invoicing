package com.example.invoicing.contractor;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/contractor")
@RequiredArgsConstructor
public class ContractorController {
    private final ContractorService contractorService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<ContractorResponse> getAll(Pageable pageable, @ModelAttribute ContractorFilter filter) {
        return contractorService.getAll(pageable, filter);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ContractorResponse get(@PathVariable UUID id) {
        return contractorService.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ContractorResponse create(@RequestBody @Valid ContractorRequest request) {
        return contractorService.create(request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        contractorService.delete(id);
    }

    @PostMapping("/{id}/deactivate")
    @ResponseStatus(HttpStatus.OK)
    public ContractorResponse deactivate(@PathVariable UUID id) {
        return contractorService.deactivate(id);
    }

    @PostMapping("/{id}/activate")
    @ResponseStatus(HttpStatus.OK)
    public ContractorResponse activate(@PathVariable UUID id) {
        return contractorService.activate(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ContractorResponse update(@PathVariable UUID id, @RequestBody @Valid ContractorRequest request) {
        return contractorService.update(id, request);
    }
}
