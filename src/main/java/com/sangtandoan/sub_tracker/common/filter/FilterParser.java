package com.sangtandoan.sub_tracker.common.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FilterParser {

  // Pattern to match: field[operation]=value
  private static final Pattern FILTER_PATTERN =
      Pattern.compile("([a-zA-Z_][a-zA-Z0-9_.]*)(\\[([a-z]+)\\])?");

  public static List<FilterCriteria> parseFilters(Map<String, String[]> queryParams) {
    List<FilterCriteria> criteria = new ArrayList<>();

    for (Map.Entry<String, String[]> entry : queryParams.entrySet()) {
      String key = entry.getKey();
      String[] values = entry.getValue();

      // Skip non-filter parameters
      if (isSystemParameter(key)) {
        continue;
      }

      try {
        FilterCriteria criterion = parseFilterParameter(key, values);
        if (criterion != null) {
          criteria.add(criterion);
        }
      } catch (IllegalArgumentException e) {
        throw new FilterValidationException(
            "Invalid filter parameter: " + key + ". " + e.getMessage(), e);
      }
    }

    return criteria;
  }

  private static FilterCriteria parseFilterParameter(String key, String[] values) {
    Matcher matcher = FILTER_PATTERN.matcher(key);

    if (!matcher.matches()) {
      return null; // Invalid format
    }

    String field = matcher.group(1);
    String operationStr = matcher.group(3);

    // Default operation is EQUALS if no operation specified
    FilterOperation operation =
        operationStr != null ? FilterOperation.fromValue(operationStr) : FilterOperation.EQUALS;

    // Handle multiple values for IN operation
    if (values.length > 1) {
      List<Object> valueList = Arrays.asList((Object[]) values);
      return new FilterCriteria(field, operation, valueList);

    } else if (values.length == 1) {

      // Handle comma-separated values for IN operation
      if (operation == FilterOperation.IN && values[0].contains(",")) {
        List<Object> valueList =
            Arrays.stream(values[0].split(",")).map(String::trim).map(s -> (Object) s).toList();
        return new FilterCriteria(field, operation, valueList);
      }

      return new FilterCriteria(field, operation, values[0]);
    }

    return null;
  }

  private static boolean isSystemParameter(String key) {
    // Skip common system parameters
    return key.equals("page")
        || key.equals("size")
        || key.equals("sort")
        || key.equals("search")
        || key.equals("searchTerm")
        || key.equals("isCancelled");
  }
}
