CREATE TABLE subscriptions (
    id uuid PRIMARY KEY,
    user_id uuid NOT NULL,
    name text NOT NULL,
    start_date date NOT NULL,
    end_date date NOT NULL,
    duration text NOT NULL,
    is_cancelled boolean NOT NULL,

    FOREIGN KEY (user_id) REFERENCES users(id)
)
