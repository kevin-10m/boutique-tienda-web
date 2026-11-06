-- Crea la base y el usuario local del proyecto. Ejecutar una vez como root.
-- Hibernate crea las tablas y data.sql carga los datos al iniciar la aplicación.
CREATE DATABASE IF NOT EXISTS boutique_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER IF NOT EXISTS 'boutique_user'@'localhost' IDENTIFIED BY 'Boutique2026!';
GRANT ALL PRIVILEGES ON boutique_db.* TO 'boutique_user'@'localhost';
FLUSH PRIVILEGES;
