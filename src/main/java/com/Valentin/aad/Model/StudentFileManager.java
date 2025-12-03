package com.Valentin.aad.Model;

import java.io.IOException;
import java.io.RandomAccessFile;

public class StudentFileManager {

    private static final int NAME_LENGTH = 20;
    public static final int RECORD_SIZE = 4 + (2 * NAME_LENGTH) + 4;

    private RandomAccessFile raf;

    public StudentFileManager(String path) throws IOException {
        raf = new RandomAccessFile(path, "rw");
    }

    public void close() throws IOException {
        if (raf != null) {
            raf.close();
        }
    }

    private String fixName(String name) {
        if (name == null) name = "";
        StringBuilder sb = new StringBuilder(name);

        if (sb.length() > NAME_LENGTH) {
            return sb.substring(0, NAME_LENGTH);
        }

        while (sb.length() < NAME_LENGTH) {
            sb.append(" ");
        }

        return sb.toString();
    }

    public void append(Student s) throws IOException {
        raf.seek(raf.length());
        writeRaw(s);
    }

    public void writeAtIndex(int index, Student s) throws IOException {
        long pos = (long) index * RECORD_SIZE;
        raf.seek(pos);
        writeRaw(s);
    }

    public Student readAtIndex(int index) throws IOException {
        long pos = (long) index * RECORD_SIZE;

        if (pos >= raf.length()) return null;

        raf.seek(pos);
        return readRaw();
    }

    public int getStudentCount() throws IOException {
        return (int) (raf.length() / RECORD_SIZE);
    }

    private void writeRaw(Student s) throws IOException {
        raf.writeInt(s.getId());

        String fixedName = fixName(s.getName());
        raf.writeChars(fixedName);

        raf.writeFloat(s.getGrade());
    }

    private Student readRaw() throws IOException {
        int id = raf.readInt();

        char[] chars = new char[NAME_LENGTH];
        for (int i = 0; i < NAME_LENGTH; i++) {
            chars[i] = raf.readChar();
        }
        String name = new String(chars).trim();

        float grade = raf.readFloat();

        return new Student(id, name, grade);
    }
}
