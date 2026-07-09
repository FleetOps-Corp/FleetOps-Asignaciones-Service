CREATE TABLE conductores (
    id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nombre        VARCHAR(200) NOT NULL,
    tipo_vehiculo VARCHAR(100) NOT NULL,
    estado        VARCHAR(50)  NOT NULL
);


-- 2. CREAR LOS DATOS (INSERTS)
INSERT INTO conductores (id, nombre, tipo_vehiculo, estado) VALUES
  ('11111111-1111-1111-4111-811111111111', 'Juan Pérez', 'CAMION', 'DISPONIBLE'),
  ('22222222-2222-2222-4222-822222222222', 'María Gómez', 'CAMION', 'DISPONIBLE'),
  ('33333333-3333-3333-4333-833333333333', 'Carlos Sánchez', 'CAMION', 'DISPONIBLE');