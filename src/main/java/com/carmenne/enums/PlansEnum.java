package com.carmenne.enums;

/** Defines the plans */

public enum PlansEnum {

    BASIC(1, "BASIC"),
    PRO(2, "PRO");

    private final int id;
    private final String name;

    PlansEnum(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
