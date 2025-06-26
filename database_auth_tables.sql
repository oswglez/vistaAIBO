-- Script para crear tablas de autenticación y autorización
-- Ejecutar en la base de datos PostgreSQL

-- 1. Crear tabla de usuarios
CREATE TABLE IF NOT EXISTS users (
    user_id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP
);

-- 2. Crear tabla de roles
CREATE TABLE IF NOT EXISTS roles (
    role_id BIGSERIAL PRIMARY KEY,
    role_name VARCHAR(50) UNIQUE NOT NULL,
    description TEXT,
    permissions JSONB
);

-- 3. Crear tabla intermedia usuario-hotel-rol
CREATE TABLE IF NOT EXISTS user_hotel_roles (
    user_hotel_role_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(user_id),
    hotel_id BIGINT REFERENCES hotel(hotel_id),
    role_id BIGINT REFERENCES roles(role_id),
    assigned_by BIGINT REFERENCES users(user_id),
    assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT true,
    UNIQUE(user_id, hotel_id, role_id)
);

-- 4. Agregar columna user_id a la tabla hotel (si no existe)
ALTER TABLE hotel ADD COLUMN IF NOT EXISTS user_id BIGINT REFERENCES users(user_id);

-- 5. Crear índices para mejor performance
CREATE INDEX IF NOT EXISTS idx_users_username ON users(username);
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_users_active ON users(is_active);
CREATE INDEX IF NOT EXISTS idx_roles_name ON roles(role_name);
CREATE INDEX IF NOT EXISTS idx_user_hotel_roles_user ON user_hotel_roles(user_id);
CREATE INDEX IF NOT EXISTS idx_user_hotel_roles_hotel ON user_hotel_roles(hotel_id);
CREATE INDEX IF NOT EXISTS idx_user_hotel_roles_active ON user_hotel_roles(is_active);
CREATE INDEX IF NOT EXISTS idx_hotel_user ON hotel(user_id);

-- 6. Insertar roles básicos
INSERT INTO roles (role_name, description, permissions) VALUES
('HOTEL_ADMIN', 'Administrador del hotel', '{"can_manage_hotel": true, "can_manage_rooms": true, "can_manage_staff": true, "can_view_reports": true}'),
('HOTEL_MANAGER', 'Gerente del hotel', '{"can_manage_rooms": true, "can_view_reports": true, "can_update_availability": true}'),
('HOTEL_STAFF', 'Personal del hotel', '{"can_view_rooms": true, "can_update_availability": true, "can_view_hotel": true}'),
('HOTEL_VIEWER', 'Solo lectura', '{"can_view_hotel": true, "can_view_rooms": true}')
ON CONFLICT (role_name) DO NOTHING;

-- 7. Insertar usuario administrador por defecto (password: admin123)
INSERT INTO users (username, email, password_hash, first_name, last_name, is_active) VALUES
('admin', 'admin@expectra.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'Admin', 'User', true)
ON CONFLICT (username) DO NOTHING;

-- 8. Asignar rol de administrador al usuario admin (asumiendo que existe un hotel con ID 1)
INSERT INTO user_hotel_roles (user_id, hotel_id, role_id, assigned_by) 
SELECT u.user_id, 1, r.role_id, u.user_id
FROM users u, roles r 
WHERE u.username = 'admin' AND r.role_name = 'HOTEL_ADMIN'
ON CONFLICT (user_id, hotel_id, role_id) DO NOTHING;

-- Comentarios:
-- 1. La contraseña 'admin123' está hasheada con BCrypt
-- 2. El usuario admin se asigna al hotel con ID 1
-- 3. Los permisos están en formato JSONB para flexibilidad
-- 4. Se crean índices para optimizar consultas frecuentes 