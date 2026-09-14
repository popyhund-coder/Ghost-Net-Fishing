CREATE DATABASE IF NOT EXISTS ghost_net CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER IF NOT EXISTS 'ghostnet'@'localhost' IDENTIFIED BY 'ghostnet';
GRANT ALL PRIVILEGES ON ghost_net.* TO 'ghostnet'@'localhost';
FLUSH PRIVILEGES;
