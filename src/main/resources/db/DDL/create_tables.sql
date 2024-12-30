DROP TABLE IF EXISTS wishlist;
DROP TABLE IF EXISTS pokedex;
DROP TABLE IF EXISTS pokemon;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    id_user SERIAL PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    date_of_birth DATE NOT NULL,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(70) UNIQUE NOT NULL,
    password_hash TEXT NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE pokemon (
    national_number INT PRIMARY KEY,
    gen VARCHAR(3),
    english_name VARCHAR(255),
    primary_type VARCHAR(255),
    secondary_type VARCHAR(255),
    classification VARCHAR(255),
    percent_male DECIMAL(5,2),
    percent_female DECIMAL(5,2),
    height_m DECIMAL(4,1),
    weight_kg DECIMAL(5,1),
    capture_rate INT,
    hp INT,
    attack INT,
    defense INT,
    speed INT,
    abilities_0 VARCHAR(255),
    abilities_1 VARCHAR(255),
    abilities_special VARCHAR(255),
    is_sublegendary INT,
    is_legendary INT,
    is_mythical INT,
    evochain_0 VARCHAR(255),
    evochain_2 VARCHAR(255),
    evochain_4 VARCHAR(255),
    mega_evolution VARCHAR(255),
    description TEXT
);

CREATE TABLE wishlist (
    id_wishlist SERIAL PRIMARY KEY,
    id_user INT NOT NULL,
    national_number INT NOT NULL,
    FOREIGN KEY (id_user) REFERENCES users(id_user) ON DELETE CASCADE,
    FOREIGN KEY (national_number) REFERENCES pokemon(national_number) ON DELETE CASCADE
);

CREATE TABLE pokedex (
    id_pokedex SERIAL PRIMARY KEY,
    id_user INT NOT NULL,
    national_number INT NOT NULL,
    FOREIGN KEY (id_user) REFERENCES users(id_user) ON DELETE CASCADE,
    FOREIGN KEY (national_number) REFERENCES pokemon(national_number) ON DELETE CASCADE
);