CREATE TABLE conductores (
    id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nombre        VARCHAR(200) NOT NULL,
    tipo_vehiculo VARCHAR(100) NOT NULL,
    estado        VARCHAR(50)  NOT NULL
);


-- 2. CREAR LOS DATOS (INSERTS)
INSERT INTO conductores (id, nombre, tipo_vehiculo, estado) VALUES
  ('11111111-1111-1111-1111-111111111111', 'Juan Pérez', 'CAMION', 'DISPONIBLE'),
  ('22222222-2222-2222-2222-222222222222', 'María Gómez', 'CAMION', 'DISPONIBLE'),
  ('33333333-3333-3333-3333-333333333333', 'Carlos Sánchez', 'CAMION', 'DISPONIBLE');