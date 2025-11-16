-- data.sql
insert into camion (id, patente, cap_peso_kg, cap_volm3, disponible) values
  (1, 'AA123BB', 10000, 50, true),
  (2, 'AB456CD', 8000,  40, true);

insert into deposito (id, nombre, lat, lng, capacidad_contenedores) values
  (1, 'Depósito Córdoba', -31.41, -64.18, 200),
  (2, 'Depósito Rosario', -32.95, -60.65, 150);

insert into tarifa_regla (id, precio_km, factor_peso, factor_volumen, recargo_estadia) values
  (1, 1000, 50, 30, 500);
