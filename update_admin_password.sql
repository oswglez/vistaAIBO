-- Update admin user password with the newly generated hash
UPDATE users 
SET password_hash = '$2a$10$Rh4oLa08c4t6oZutMVCU9OMITgK9RNabYDzgEWaGi6U/v8Ni0SDsW'
WHERE username = 'admin';

-- Verify the update
SELECT username, password_hash, is_active 
FROM users 
WHERE username = 'admin'; 