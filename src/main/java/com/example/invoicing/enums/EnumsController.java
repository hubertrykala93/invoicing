package com.example.invoicing.enums;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequestMapping("/api/enums")
@RestController
@RequiredArgsConstructor
public class EnumsController {

    private final EnumsService enumsService;

    @GetMapping
    public EnumsResponse getAll() {
        return enumsService.getAll();
    }
}
