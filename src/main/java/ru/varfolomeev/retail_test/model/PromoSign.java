package ru.varfolomeev.retail_test.model;

public enum PromoSign {
    REGULAR("REGULAR"), PROMO("PROMO");

    String name;

    PromoSign(String name) {
        this.name = name;
    }
}
