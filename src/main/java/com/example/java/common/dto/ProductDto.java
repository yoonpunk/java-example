package com.example.java.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor(staticName = "of")
@ToString
public class ProductDto {

    private final Long id;
    private final String productName;
}
