package com.example.library_system;

public class Student {
    private int id;
    private String name;
    private String className;
    private String number;

    public Student(int id, String name, String className, String number) {
        this.id = id;
        this.name = name;
        this.className = className;
        this.number = number;
    }
    public Student(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
