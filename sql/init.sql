CREATE DATABASE IF NOT EXISTS monapp
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE monapp;

-- ============================================================
-- TABLE UTILISATEURS (pour le login)
-- ============================================================
CREATE TABLE IF NOT EXISTS utilisateurs (
                                            id INT AUTO_INCREMENT PRIMARY KEY,
                                            nom VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    mot_de_passe VARCHAR(255) NOT NULL,
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

-- ============================================================
-- TABLE ÉTUDIANTS (gestion scolaire)
-- ============================================================
CREATE TABLE IF NOT EXISTS etudiants (
                                         id INT AUTO_INCREMENT PRIMARY KEY,
                                         nom VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    telephone VARCHAR(20),
    filiere VARCHAR(50),
    niveau VARCHAR(20),
    date_inscription TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

-- ============================================================
-- TABLE COURS
-- ============================================================
CREATE TABLE IF NOT EXISTS cours (
                                     id INT AUTO_INCREMENT PRIMARY KEY,
                                     code VARCHAR(20) NOT NULL UNIQUE,
    intitule VARCHAR(150) NOT NULL,
    coefficient INT NOT NULL DEFAULT 1,
    volume_heures INT NOT NULL DEFAULT 0
    );

-- ============================================================
-- TABLE NOTES
-- ============================================================
CREATE TABLE IF NOT EXISTS notes (
                                     id INT AUTO_INCREMENT PRIMARY KEY,
                                     etudiant_id INT NOT NULL,
                                     cours_id INT NOT NULL,
                                     note DECIMAL(4,2) NOT NULL,
    date_note DATE,
    FOREIGN KEY (etudiant_id) REFERENCES etudiants(id) ON DELETE CASCADE,
    FOREIGN KEY (cours_id) REFERENCES cours(id) ON DELETE CASCADE,
    UNIQUE (etudiant_id, cours_id)
    );

-- ============================================================
-- DONNÉES DE TEST
-- ============================================================

-- Utilisateur admin (mot de passe : admin123 hashé avec BCrypt)
-- ⚠️ Le hash ci-dessous est pour "admin123" — généré avec BCrypt(12)
INSERT INTO utilisateurs (nom, email, mot_de_passe) VALUES
    ('Administrateur', 'admin@mail.com', '$2a$12$VXzk2CNS0mxdNdFNnxzya.CWpJtx3t/Fu7sXpS2M4Sj4XRFxZ0juq');

-- Étudiants
INSERT INTO etudiants (nom, email, telephone, filiere, niveau) VALUES
                                                                   ('Alice Dupont', 'alice@mail.com', '0601020304', 'INFO', 'L3'),
                                                                   ('Bob Martin', 'bob@mail.com', '0705060708', 'INFO', 'L3'),
                                                                   ('Charlie Durand', 'charlie@mail.com', '0501020304', 'INFO', 'M1');

-- Cours
INSERT INTO cours (code, intitule, coefficient, volume_heures) VALUES
                                                                   ('MATH101', 'Mathématiques', 3, 60),
                                                                   ('FR101', 'Français', 2, 40),
                                                                   ('INFO101', 'Informatique', 4, 80),
                                                                   ('HIST101', 'Histoire', 1, 30);

-- Notes
INSERT INTO notes (etudiant_id, cours_id, note, date_note) VALUES
                                                               (1, 1, 15.5, '2026-10-01'),
                                                               (1, 2, 12.0, '2026-10-02'),
                                                               (1, 3, 18.0, '2026-10-03'),
                                                               (2, 1, 8.0, '2026-10-01'),
                                                               (2, 2, 14.0, '2026-10-02'),
                                                               (3, 1, 16.0, '2026-10-01'),
                                                               (3, 3, 17.5, '2026-10-03');