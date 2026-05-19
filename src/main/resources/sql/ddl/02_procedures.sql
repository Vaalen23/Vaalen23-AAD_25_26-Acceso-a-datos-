DROP FUNCTION IF EXISTS count_enrollments(INT);

CREATE OR REPLACE FUNCTION count_enrollments(student_id INT)
RETURNS INT
LANGUAGE sql
AS 'SELECT COUNT(*)::INT FROM matricula WHERE id_alumno = student_id';
