CREATE TABLE IF NOT EXISTS agendamentos (
    id BINARY(16) PRIMARY KEY,
    data_hora DATETIME(6) NOT NULL,
    user_id BINARY(16) NOT NULL,
    data_criacao DATETIME(6) NOT NULL,
    CONSTRAINT fk_user_agendamento FOREIGN KEY (user_id) REFERENCES usuarios (id)
);