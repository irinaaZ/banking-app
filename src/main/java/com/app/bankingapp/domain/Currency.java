package com.app.bankingapp.domain;

import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Currency {
    private Long id;
    private Long bankId;
    private String name;
    private String shortName;
    private BigDecimal purchaseRate;
    private BigDecimal sellingRate;
}
