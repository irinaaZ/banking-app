package com.app.bankingapp.domain;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@ToString(exclude = "currencies")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "banks")
@Entity
public class Bank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String phone;
    private String type;
    private boolean ableToBuyCurrencyOnline;
    private Long numberOfBranches;
    private String address;

    @OneToMany(mappedBy = "bank", fetch = FetchType.LAZY)
    private List<Currency> currencies;
}
