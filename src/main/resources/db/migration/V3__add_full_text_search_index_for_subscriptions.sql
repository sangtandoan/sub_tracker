-- Add a tsvector column for full-text search
ALTER TABLE subscriptions ADD COLUMN name_tsv tsvector;

-- Create an index on the tsvector column
CREATE INDEX subscriptions_name_tsv_idx ON subscriptions USING GIN (name_tsv);

-- Create a trigger to automatically update the tsvector column
CREATE OR REPLACE FUNCTION subscriptions_name_trigger() RETURNS trigger AS $$
BEGIN
  NEW.name_tsv := to_tsvector('english', NEW.name);
RETURN NEW;
END
$$ LANGUAGE plpgsql;

CREATE TRIGGER subscriptions_name_tsvector_update
    BEFORE INSERT OR UPDATE ON subscriptions
                         FOR EACH ROW EXECUTE FUNCTION subscriptions_name_trigger();

-- Update existing records
UPDATE subscriptions SET name_tsv = to_tsvector('english', name);
