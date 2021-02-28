package com.app.bankingapp.domain;

public class Bank {
    private Long id;
    private String name;
    private String phone;
    private String type;
    private boolean ableToBuyCurrencyOnline;
    private Long numberOfBranches;
    private String address;

    public Bank() {
    }

    public Bank(Long id, String name, String phone, String type, boolean ableToBuyCurrencyOnline, Long numberOfBranches, String address) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.type = type;
        this.ableToBuyCurrencyOnline = ableToBuyCurrencyOnline;
        this.numberOfBranches = numberOfBranches;
        this.address = address;
    }

    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isAbleToBuyCurrencyOnline() {
        return ableToBuyCurrencyOnline;
    }

    public void setAbleToBuyCurrencyOnline(boolean ableToBuyCurrencyOnline) {
        this.ableToBuyCurrencyOnline = ableToBuyCurrencyOnline;
    }

    public Long getNumberOfBranches() {
        return numberOfBranches;
    }

    public void setNumberOfBranches(Long numberOfBranches) {
        this.numberOfBranches = numberOfBranches;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
