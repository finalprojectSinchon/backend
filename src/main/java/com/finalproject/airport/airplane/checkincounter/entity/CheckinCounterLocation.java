package com.finalproject.airport.airplane.checkincounter.entity;

public enum CheckinCounterLocation {
    A(1),
    B(2),
    C(3),
    D(4),
    E(5),
    F(6),
    G(7),
    H(8),
    I(9),
    J(10),
    K(11),
    L(12),
    M(13),
    N(14);

    private final int code;

    CheckinCounterLocation(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static CheckinCounterLocation fromCode(int code) {
        for (CheckinCounterLocation location : values()) {
            if (location.getCode() == code) {
                return location;
            }
        }
        throw new IllegalArgumentException("Invalid code: " + code);
    }
}
