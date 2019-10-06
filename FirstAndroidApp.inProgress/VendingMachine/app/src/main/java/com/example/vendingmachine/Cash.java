package com.example.vendingmachine;

import java.math.BigDecimal;

class Cash {
    private BigDecimal balance;

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public Cash() {
    }

    public Cash(BigDecimal balance) {
        this.balance = balance;
    }
}
