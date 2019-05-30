package com.kimjio.mealviewer.model;

import androidx.annotation.NonNull;

public class School {
    public enum Type {
        KINDER(1),
        ELEMENT(2),
        MIDDLE(3),
        HIGH(4);

        private int id;

        Type(int id) {
            this.id = id;
        }

        public int toInteger() {
            return id;
        }

        @Override
        public String toString() {
            return Integer.toString(id);
        }
    }

    private String schoolId;
    private String name;
    private String address;
    private Type type;


    public School(String schoolId, String name, String address, int type) {
        this.schoolId = schoolId;
        this.name = name;
        this.address = address;
        switch (type) {
            case 1:
                this.type = Type.KINDER;
                break;
            case 2:
                this.type = Type.ELEMENT;
                break;
            case 3:
                this.type = Type.MIDDLE;
                break;
            case 4:
                this.type = Type.HIGH;
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    public School(String schoolId, String name, String address, Type type) {
        this.schoolId = schoolId;
        this.name = name;
        this.address = address;
        this.type = type;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @NonNull
    @Override
    public String toString() {
        return "School{" +
                "schoolId='" + schoolId + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", type=" + type +
                '}';
    }
}
