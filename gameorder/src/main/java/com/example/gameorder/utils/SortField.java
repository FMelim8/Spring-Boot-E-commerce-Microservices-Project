package com.example.gameorder.utils;

public enum SortField {
    ORDERID("id"),
    GAMEID("gameId"),
    USERID("userId"),
    ORDERDATE("orderDate"),
    QUANTITY("quantity"),
    TOTALAMOUNT("totalAmount");

    private final String value;

    SortField(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
