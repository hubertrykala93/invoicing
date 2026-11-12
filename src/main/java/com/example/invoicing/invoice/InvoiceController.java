package com.example.invoicing.invoice;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
@RequestMapping("/api/invoice")
@RequiredArgsConstructor
public class InvoiceController {
    private static final int DEFAULT_PAGE_SIZE = 20;

    private final InvoiceService invoiceService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<InvoiceMinimalResponse> getAll(
        @PageableDefault(size = DEFAULT_PAGE_SIZE, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
        @ModelAttribute InvoiceFilter filter
    ) {
        return invoiceService.getAll(pageable, filter);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InvoiceResponse create(@RequestBody @Valid InvoiceCreateRequest request) {
        return invoiceService.create(request);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public InvoiceResponse get(@PathVariable UUID id) {
        return invoiceService.get(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public InvoiceResponse update(@PathVariable UUID id, @RequestBody @Valid InvoiceUpdateRequest request) {
        return invoiceService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        invoiceService.delete(id);
    }

    @PostMapping("/{id}/issue")
    @ResponseStatus(HttpStatus.OK)
    public InvoiceResponse issue(@PathVariable UUID id) {
        return invoiceService.issue(id);
    }

    @PostMapping("/{id}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public InvoiceResponse cancel(@PathVariable UUID id, @RequestBody @Valid InvoiceCancelRequest request) {
        return invoiceService.cancel(id, request);
    }

    @PostMapping("/{id}/duplicate")
    @ResponseStatus(HttpStatus.CREATED)
    public InvoiceResponse duplicate(@PathVariable UUID id) {
        return invoiceService.duplicate(id);
    }

    @PostMapping("/{id}/items")
    @ResponseStatus(HttpStatus.CREATED)
    public InvoiceResponse createItem(@PathVariable UUID id, @RequestBody @Valid ItemCreateRequest request) {
        return invoiceService.createItem(id, request);
    }

    @PutMapping("/{id}/items/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public InvoiceResponse updateItem(@PathVariable UUID id, @PathVariable UUID itemId, @RequestBody @Valid ItemCreateRequest request) {
        return invoiceService.updateItem(id, itemId, request);
    }

    @DeleteMapping("/{id}/items/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteItem(@PathVariable UUID id, @PathVariable UUID itemId) {
        invoiceService.deleteItem(id, itemId);
    }

    @PostMapping("/{id}/payments")
    @ResponseStatus(HttpStatus.CREATED)
    public InvoiceResponse createPayment(@PathVariable UUID id, @RequestBody @Valid PaymentCreateRequest request) {
        return invoiceService.createPayment(id, request);
    }

    @DeleteMapping("/{id}/payments/{paymentId}")
    @ResponseStatus(HttpStatus.OK)
    public InvoiceResponse deletePayment(@PathVariable UUID id, @PathVariable UUID paymentId) {
        return invoiceService.deletePayment(id, paymentId);
    }
}
