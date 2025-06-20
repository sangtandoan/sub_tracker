# Dynamic Filtering System

## Overview

This filtering system provides a flexible way to filter data using query parameters with various operations like equality, comparison, and range filtering. It's designed following industry best practices used by companies like GitHub, Stripe, and other major platforms.

## Query Parameter Format

The filtering system supports the following format:

```
field[operation]=value
```

If no operation is specified, it defaults to equality:

```
field=value  // equivalent to field[eq]=value
```

## Supported Operations

| Operation | Description               | Example                       |
| --------- | ------------------------- | ----------------------------- |
| `eq`      | Equals (default)          | `name[eq]=Netflix`            |
| `ne`      | Not equals                | `name[ne]=Spotify`            |
| `gt`      | Greater than              | `startDate[gt]=2024-01-01`    |
| `gte`     | Greater than or equal     | `startDate[gte]=2024-01-01`   |
| `lt`      | Less than                 | `endDate[lt]=2024-12-31`      |
| `lte`     | Less than or equal        | `endDate[lte]=2024-12-31`     |
| `like`    | Contains (case-sensitive) | `name[like]=Net`              |
| `in`      | In list                   | `duration[in]=MONTHLY,YEARLY` |

## Usage Examples

### Basic Filtering

**Filter by subscription name:**

```
GET /api/v1/subscriptions?name=Netflix
GET /api/v1/subscriptions?name[eq]=Netflix
```

**Filter by cancellation status:**

```
GET /api/v1/subscriptions?isCancelled=false
```

### Range Filtering (Numbers and Dates)

**Filter subscriptions starting after a specific date:**

```
GET /api/v1/subscriptions?startDate[gte]=2024-01-01
```

**Filter subscriptions ending before a specific date:**

```
GET /api/v1/subscriptions?endDate[lt]=2024-12-31
```

**Combine date range filters:**

```
GET /api/v1/subscriptions?startDate[gte]=2024-01-01&endDate[lte]=2024-12-31
```

### Text Filtering

**Search for subscriptions containing text:**

```
GET /api/v1/subscriptions?name[like]=Net
```

### List Filtering

**Filter by multiple subscription durations:**

```
GET /api/v1/subscriptions?duration[in]=MONTHLY,YEARLY
```

**Multiple values using separate parameters:**

```
GET /api/v1/subscriptions?duration[in]=MONTHLY&duration[in]=YEARLY
```

### Complex Combinations

**Multiple filters combined:**

```
GET /api/v1/subscriptions?startDate[gte]=2024-01-01&isCancelled=false&name[like]=Net&page=0&size=10
```

## Supported Data Types

The system automatically converts values to the appropriate data types:

- **String**: `name[eq]=Netflix`
- **Boolean**: `isCancelled=true`
- **Date**: `startDate[gte]=2024-01-01` (format: yyyy-MM-dd)
- **DateTime**: `createdAt[gte]=2024-01-01T10:00:00`
- **Numbers**: `amount[gte]=10.99`
- **Enums**: `duration[eq]=MONTHLY`
- **UUID**: `id[eq]=123e4567-e89b-12d3-a456-426614174000`

## Error Handling

The system provides clear error messages for invalid filters:

**Invalid operation:**

```json
{
    "timestamp": "2024-01-01T10:00:00",
    "status": 400,
    "error": "Bad Request",
    "message": "Invalid filter parameter: startDate[invalid]. Invalid filter operation: invalid",
    "type": "Filter Validation Error"
}
```

**Invalid date format:**

```json
{
    "timestamp": "2024-01-01T10:00:00",
    "status": 400,
    "error": "Bad Request",
    "message": "Invalid date format. Expected format: yyyy-MM-dd",
    "type": "Invalid Filter Value"
}
```

## System Parameters

The following parameters are reserved for system use and won't be treated as filters:

- `page` - pagination
- `size` - page size
- `sort` - sorting
- `search` - full-text search
- `searchTerm` - search term
- `isCancelled` - special business logic filter

## Best Practices

1. **Use descriptive field names**: Use the exact field names from your entity
2. **Validate date formats**: Always use ISO date format (yyyy-MM-dd)
3. **Handle case sensitivity**: The `like` operation is case-sensitive
4. **Combine with pagination**: Always use with `page` and `size` parameters
5. **Use appropriate operations**: Choose the right operation for your data type

## Implementation Notes

- Built using JPA Criteria API for type-safe queries
- Supports nested properties (e.g., `user.name`)
- Automatically handles type conversion
- Thread-safe and stateless
- Integrates with Spring Security for user-based filtering
