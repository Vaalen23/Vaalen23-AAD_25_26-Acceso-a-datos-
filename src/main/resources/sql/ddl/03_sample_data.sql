-- Datos de ejemplo para pruebas

-- Alumnos
INSERT INTO alumno (nif, nombre, email) VALUES
    ('11111111A', 'Carlos Ruiz',    'carlos@example.com'),
    ('22222222B', 'Laura Jimenez',  'laura@example.com'),
    ('33333333C', 'Pedro Moreno',   'pedro@example.com'),
    ('44444444D', 'Elena Castillo', 'elena@example.com')
ON CONFLICT (nif) DO NOTHING;

-- Modulos
INSERT INTO modulo (codigo, nombre, horas) VALUES
    ('AAD',  'Acceso a Datos',                       120),
    ('PSP',  'Programacion de Servicios y Procesos', 180),
    ('BBDD', 'Bases de Datos',                       200),
    ('DI',   'Desarrollo de Interfaces',             120)
ON CONFLICT (codigo) DO NOTHING;

-- Matriculas
INSERT INTO matricula (id_alumno, id_modulo, fecha) VALUES
    ((SELECT id_alumno FROM alumno WHERE nif = '11111111A'),
     (SELECT id_modulo FROM modulo WHERE codigo = 'AAD'),
     '2024-09-10')
ON CONFLICT DO NOTHING;

INSERT INTO matricula (id_alumno, id_modulo, fecha) VALUES
    ((SELECT id_alumno FROM alumno WHERE nif = '11111111A'),
     (SELECT id_modulo FROM modulo WHERE codigo = 'PSP'),
     '2024-09-10')
ON CONFLICT DO NOTHING;

INSERT INTO matricula (id_alumno, id_modulo, fecha) VALUES
    ((SELECT id_alumno FROM alumno WHERE nif = '22222222B'),
     (SELECT id_modulo FROM modulo WHERE codigo = 'BBDD'),
     '2024-09-12')
ON CONFLICT DO NOTHING;

INSERT INTO matricula (id_alumno, id_modulo, fecha) VALUES
    ((SELECT id_alumno FROM alumno WHERE nif = '33333333C'),
     (SELECT id_modulo FROM modulo WHERE codigo = 'DI'),
     '2024-09-15')
ON CONFLICT DO NOTHING;
