package com.app.bankingapp.domain;

import java.math.BigDecimal;

public class Currency {
    private Long id;
    private Long bankId;
    private String name;
    private String shortName;
    private BigDecimal purchaseRate;
    private BigDecimal sellingRate;

    public Currency() {
    }

    public Currency(Long id, Long bankId, String name, String shortName, BigDecimal purchaseRate, BigDecimal sellingRate) {
        this.id = id;
        this.bankId = bankId;
        this.name = name;
        this.shortName = shortName;
        this.purchaseRate = purchaseRate;
        this.sellingRate = sellingRate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBankId() {
        return bankId;
    }

    public void setBankId(Long bankId) {
        this.bankId = bankId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public BigDecimal getPurchaseRate() {
        return purchaseRate;
    }

    public void setPurchaseRate(BigDecimal purchaseRate) {
        this.purchaseRate = purchaseRate;
    }

    public BigDecimal getSellingRate() {
        return sellingRate;
    }

    public void setSellingRate(BigDecimal sellingRate) {
        this.sellingRate = sellingRate;
    }
}
