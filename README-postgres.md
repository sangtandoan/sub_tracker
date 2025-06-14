# PostgreSQL Docker Setup

This project includes a PostgreSQL database service configured with Docker Compose.

## Quick Start

1. **Start the PostgreSQL service:**
   ```bash
   docker-compose up -d postgres
   ```

2. **Stop the service:**
   ```bash
   docker-compose down
   ```

3. **View logs:**
   ```bash
   docker-compose logs postgres
   ```

## Configuration

### Environment Variables

You can customize the database configuration by copying `.env.example` to `.env` and modifying the values:

```bash
cp .env.example .env
```

Available variables:
- `POSTGRES_DB`: Database name (default: sub_tracker)
- `POSTGRES_USER`: Database user (default: sub_tracker_user)
- `POSTGRES_PASSWORD`: Database password (default: sub_tracker_password)

### Database Connection

- **Host:** localhost
- **Port:** 5432
- **Database:** sub_tracker (or value from POSTGRES_DB)
- **Username:** sub_tracker_user (or value from POSTGRES_USER)
- **Password:** sub_tracker_password (or value from POSTGRES_PASSWORD)

### JDBC URL for Java Applications

```
jdbc:postgresql://localhost:5432/sub_tracker
```

## Initialization Scripts

Place SQL files in the `init-scripts/` directory to run them automatically when the database is first created. Files are executed in alphabetical order.

## Data Persistence

Database data is stored in a Docker volume named `postgres_data` and will persist between container restarts.

## Health Check

The container includes a health check that verifies PostgreSQL is ready to accept connections.

## Connecting to the Database

### Using psql from the container:
```bash
docker-compose exec postgres psql -U sub_tracker_user -d sub_tracker
```

### Using external tools:
Connect using any PostgreSQL client with the connection details above.

