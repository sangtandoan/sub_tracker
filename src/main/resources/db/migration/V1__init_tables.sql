CREATE TABLE users (
    id uuid PRIMARY KEY,
    email text UNIQUE NOT NULL,
    name text NOT NULL,
    password bytea,
    email_verified boolean DEFAULT false NOT NULL,
    created_at timestamp NOT NULL DEFAULT now(),
    updated_at timestamp NOT NULL DEFAULT now()
);

CREATE TABLE users_providers (
    id uuid PRIMARY KEY,
    user_id uuid NOT NULL,
    provider_name text NOT NULL,
    provider_user_id text NOT NULL,
    created_at timestamp DEFAULT now() NOT NULL,

    FOREIGN KEY(user_id) REFERENCES users(id)
);

CREATE INDEX IF NOT EXISTS users_providers_user_id_idx ON users_providers(user_id);
