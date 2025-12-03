package com.Valentin.aad.Repository;

import com.Valentin.aad.Model.Student;
import com.Valentin.aad.Util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.RandomAccessFile;

@Slf4j
@Repository
public class StudentRepository {

    // añade el alumno al final del fichero
    public void insertStudent(Student student) {
        try (RandomAccessFile file = new RandomAccessFile(Constants.FILE_NAME, "rw")) {
            file.seek(file.length());
            writeStudent(file, student);
            log.info("Alumno insertado: {}", student);
        } catch (IOException e) {
            log.error("Error insertando alumno", e);
        }
    }

    // lee un alumno por su posición
    public Student readStudent(int position) {
        try (RandomAccessFile file = new RandomAccessFile(Constants.FILE_NAME, "r")) {
            long offset = (long) position * Constants.RECORD_SIZE;

            if (offset >= file.length()) {
                return null;
            }

            file.seek(offset);
            return readStudentData(file);

        } catch (IOException e) {
            log.error("Error leyendo alumno", e);
            return null;
        }
    }

    // actualiza solo la nota
    public void updateGrade(int position, float newGrade) {
        try (RandomAccessFile file = new RandomAccessFile(Constants.FILE_NAME, "rw")) {
            long offset = (long) position * Constants.RECORD_SIZE + 44;

            if (offset >= file.length()) {
                return;
            }

            file.seek(offset);
            file.writeFloat(newGrade);

        } catch (IOException e) {
            log.error("Error actualizando nota", e);
        }
    }

    private void writeStudent(RandomAccessFile file, Student student) throws IOException {
        file.writeInt(student.getId());
        writeFixedString(file, student.getName(), Constants.NAME_LENGTH);
        file.writeFloat(student.getGrade());
    }

    private Student readStudentData(RandomAccessFile file) throws IOException {
        int id = file.readInt();
        String name = readFixedString(file, Constants.NAME_LENGTH);
        float grade = file.readFloat();
        return new Student(id, name.trim(), grade);
    }

    private void writeFixedString(RandomAccessFile file, String text, int length) throws IOException {
        StringBuilder b = new StringBuilder(text != null ? text : "");
        if (b.length() > length) b.setLength(length);
        while (b.length() < length) b.append(' ');
        file.writeChars(b.toString());
    }

    private String readFixedString(RandomAccessFile file, int length) throws IOException {
        char[] chars = new char[length];
        for (int i = 0; i < length; i++) chars[i] = file.readChar();
        return new String(chars);
    }
}
