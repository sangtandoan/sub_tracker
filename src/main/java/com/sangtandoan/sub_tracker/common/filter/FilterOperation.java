package com.sangtandoan.sub_tracker.common.filter;

public enum FilterOperation {
    EQUALS("eq"),
    NOT_EQUALS("ne"),
    GREATER_THAN("gt"),
    GREATER_THAN_OR_EQUAL("gte"),
    LESS_THAN("lt"),
    LESS_THAN_OR_EQUAL("lte"),
    LIKE("like"),
    IN("in");

    private final String value;

    FilterOperation(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static FilterOperation fromValue(String value) {
        for (FilterOperation operation : values()) {
            if (operation.value.equals(value)) {
                return operation;
            }
        }
        throw new IllegalArgumentException("Invalid filter operation: " + value);
    }
}

