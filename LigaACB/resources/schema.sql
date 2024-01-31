-- crear tablas

CREATE TABLE series (
    idSerie SERIAL PRIMARY KEY,
    nombre VARCHAR(255)
);


CREATE TABLE estados (
    idEstado SERIAL PRIMARY KEY,
    nombre VARCHAR(255)
);


CREATE TABLE elementos (
    id SERIAL PRIMARY KEY,
    numero INT,
    nombre VARCHAR(255),
    simbolo VARCHAR(255),
    peso DECIMAL(12,6),
    idSerie SERIAL,
    idEstado SERIAL,
    energia VARCHAR(255),
    EN DECIMAL(12,6),
    fusion DECIMAL(12,6),
    ebullicion DECIMAL(12,6),
    EA DECIMAL(12,6),
    ionizacion DECIMAL(12,6),
    radio INT,
    dureza DECIMAL(12,6),
    modulo DECIMAL(12,6),
    densidad DECIMAL(12,6),
    Cond DECIMAL(12,6),
    calor DECIMAL(12,6),
    abundancia DECIMAL(12,6),
    Dto INT,
    FOREIGN KEY (idSerie) REFERENCES series(idSerie),
    FOREIGN KEY (idEstado) REFERENCES estados(idEstado)
);



