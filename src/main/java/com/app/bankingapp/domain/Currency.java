package com.app.bankingapp.domain;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@EqualsAndHashCode
@ToString(exclude = "bank")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "currencies")
@Entity
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bank_id")
    private Long bankId;
    private String name;
    private String shortName;
    private BigDecimal purchaseRate;
    private BigDecimal sellingRate;

    @ManyToOne
    @JoinColumn(name = "bank_id", insertable = false, updatable = false)
    private Bank bank;
}
