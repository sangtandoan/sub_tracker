package com.sangtandoan.sub_tracker.common.filter;

import java.util.List;

public record FilterCriteria(
    String field,
    FilterOperation operation,
    Object value,
    List<Object> values // For IN operation
) {
    
    public FilterCriteria(String field, FilterOperation operation, Object value) {
        this(field, operation, value, null);
    }
    
    public FilterCriteria(String field, FilterOperation operation, List<Object> values) {
        this(field, operation, null, values);
    }
}

