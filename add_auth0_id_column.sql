-- Add auth0_id column to users table
ALTER TABLE users ADD COLUMN auth0_id VARCHAR(100) UNIQUE;

-- Remove password_hash column since we're using only OAuth0
ALTER TABLE users DROP COLUMN IF EXISTS password_hash;

-- Add index for better performance
CREATE INDEX idx_users_auth0_id ON users(auth0_id); 