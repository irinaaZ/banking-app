package com.app.bankingapp.domain;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bank {
    private Long id;
    private String name;
    private String phone;
    private String type;
    private boolean ableToBuyCurrencyOnline;
    private Long numberOfBranches;
    private String address;
}
