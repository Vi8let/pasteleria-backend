-- Script para insertar productos iniciales en la base de datos
-- Ejecutar desde psql: \i insert-products.sql
-- O desde terminal: psql -d pasteleria -f insert-products.sql

-- Torta Cuadrada de Chocolate
INSERT INTO product (name, description, price, image_url, category, stock) VALUES
('Torta Cuadrada de Chocolate', 'Deliciosa torta de chocolate con capas de ganache y decoración artesanal.', 45000, '/assets/img/TC001-Torta Cuadrada de Chocolate.png', 'Tortas Cuadradas', 10);

-- Torta Cuadrada de Frutas
INSERT INTO product (name, description, price, image_url, category, stock) VALUES
('Torta Cuadrada de Frutas', 'Mezcla de frutas frescas de temporada con crema chantilly y base de bizcocho.', 50000, '/assets/img/TC002-Torta Cuadrada de Frutas.png', 'Tortas Cuadradas', 8);

-- Torta Circular de Vainilla
INSERT INTO product (name, description, price, image_url, category, stock) VALUES
('Torta Circular de Vainilla', 'Bizcocho de vainilla clásico con crema pastelera y decoración elegante.', 40000, '/assets/img/TT001-Torta Circular de Vainilla.png', 'Tortas Circulares', 15);

-- Torta Circular de Manjar
INSERT INTO product (name, description, price, image_url, category, stock) VALUES
('Torta Circular de Manjar', 'Torta tradicional con manjar, crema y nueces, sabor auténtico chileno.', 42000, '/assets/img/TT002-Torta Circular de Manjar.png', 'Tortas Circulares', 12);

-- Torta Especial de Cumpleaños
INSERT INTO product (name, description, price, image_url, category, stock) VALUES
('Torta Especial de Cumpleaños', 'Torta personalizada para celebraciones especiales con decoración temática.', 55000, '/assets/img/TE001-Torta Especial de Cumpleaños.png', 'Tortas Especiales', 6);

-- Torta Especial de Boda
INSERT INTO product (name, description, price, image_url, category, stock) VALUES
('Torta Especial de Boda', 'Torta elegante para ocasiones especiales con decoración refinada.', 80000, '/assets/img/TE002-Torta Especial de Boda.png', 'Tortas Especiales', 4);

-- Mousse de Chocolate
INSERT INTO product (name, description, price, image_url, category, stock) VALUES
('Mousse de Chocolate', 'Postre individual cremoso y suave de chocolate belga, perfecto para cualquier ocasión.', 5000, '/assets/img/PI001-Mousse de Chocolate.png', 'Postres Individuales', 20);

-- Tiramisú Clásico
INSERT INTO product (name, description, price, image_url, category, stock) VALUES
('Tiramisú Clásico', 'Postre italiano tradicional con café, cacao y mascarpone, porción individual.', 5500, '/assets/img/PI002-Tiramisú Clásico.png', 'Postres Individuales', 18);

-- Brownie Sin Gluten
INSERT INTO product (name, description, price, image_url, category, stock) VALUES
('Brownie Sin Gluten', 'Brownie rico y esponjoso sin gluten, ideal para personas con restricciones alimentarias.', 4500, '/assets/img/PG001-Brownie Sin Gluten.png', 'Sin Gluten', 25);

-- Pan Sin Gluten
INSERT INTO product (name, description, price, image_url, category, stock) VALUES
('Pan Sin Gluten', 'Pan artesanal sin gluten, perfecto para desayunos y meriendas saludables.', 3500, '/assets/img/PG002-Pan Sin Gluten.png', 'Sin Gluten', 30);

-- Torta Sin Azúcar de Naranja
INSERT INTO product (name, description, price, image_url, category, stock) VALUES
('Torta Sin Azúcar de Naranja', 'Torta refrescante de naranja endulzada naturalmente, sin azúcar refinada.', 48000, '/assets/img/PSA001-Torta Sin Azúcar de Naranja.png', 'Sin Azúcar', 7);

-- Cheesecake Sin Azúcar
INSERT INTO product (name, description, price, image_url, category, stock) VALUES
('Cheesecake Sin Azúcar', 'Cheesecake cremoso sin azúcar, endulzado con edulcorantes naturales.', 52000, '/assets/img/PSA002-Cheesecake Sin Azúcar.png', 'Sin Azúcar', 6);

-- Torta Vegana de Chocolate
INSERT INTO product (name, description, price, image_url, category, stock) VALUES
('Torta Vegana de Chocolate', 'Torta de chocolate 100% vegana, sin ingredientes de origen animal.', 46000, '/assets/img/PV001-Torta Vegana de Chocolate.png', 'Veganos', 9);

-- Galletas Veganas de Avena
INSERT INTO product (name, description, price, image_url, category, stock) VALUES
('Galletas Veganas de Avena', 'Galletas saludables de avena, veganas y deliciosas, paquete de 12 unidades.', 6000, '/assets/img/PV002-Galletas Veganas de Avena.png', 'Veganos', 22);

-- Empanada de Manzana
INSERT INTO product (name, description, price, image_url, category, stock) VALUES
('Empanada de Manzana', 'Empanada dulce rellena de manzana y canela, horneada al momento.', 2500, '/assets/img/PT001-Empanada de Manzana.png', 'Tradicionales', 35);

-- Tarta de Santiago
INSERT INTO product (name, description, price, image_url, category, stock) VALUES
('Tarta de Santiago', 'Tarta tradicional española de almendras, sabor auténtico y delicioso.', 12000, '/assets/img/PT002-Tarta de Santiago.png', 'Tradicionales', 15);

