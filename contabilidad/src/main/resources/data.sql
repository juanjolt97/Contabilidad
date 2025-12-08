-- Datos iniciales para la base de datos H2

-- Insertar movimientos de ejemplo
INSERT INTO movimientos (descripcion, cantidad, tipo, fecha, categoria, notas) VALUES
('Compra de alimentos', 45.50, 'GASTO', '2024-12-01', 'Alimentación', 'Mercado semanal'),
('Gasolina para el coche', 60.00, 'GASTO', '2024-12-02', 'Transporte', 'Tanque completo'),
('Película en cine', 12.00, 'GASTO', '2024-12-03', 'Entretenimiento', 'Entrada para 2 personas'),
('Reparación del electrodoméstico', 150.00, 'GASTO', '2024-12-04', 'Hogar', 'Reparación de nevera'),
('Consulta médica', 60.00, 'GASTO', '2024-12-05', 'Salud', 'Revisión general'),
('Sueldo del mes', 2500.00, 'BENEFICIO', '2024-12-01', 'Ingresos', 'Salario mensual'),
('Venta de artículos usados', 120.00, 'BENEFICIO', '2024-12-02', 'Ingresos', 'Venta en plataforma online'),
('Comida en restaurante', 35.00, 'GASTO', '2024-12-03', 'Alimentación', 'Almuerzo con amigos'),
('Libros', 45.00, 'GASTO', '2024-12-04', 'Educación', 'Compra en librería'),
('Regalo recibido', 50.00, 'BENEFICIO', '2024-12-05', 'Otros', 'Dinero regalo');
