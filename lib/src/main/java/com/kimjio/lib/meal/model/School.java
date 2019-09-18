package com.kimjio.lib.meal.model;

import androidx.annotation.NonNull;

public class School {
    private String schoolId;
    private String name;
    private String address;
    private String localDomain;
    private Type type;

    public School(String schoolId, String name, String address, int type, String localDomain) {
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
            case 5:
                this.type = Type.MIDDLE_HIGH;
                break;
            default:
                throw new IllegalArgumentException();
        }
        this.localDomain = localDomain;
    }


    public School(String schoolId, String name, String address, Type type, String localDomain) {
        this.schoolId = schoolId;
        this.name = name;
        this.address = address;
        this.type = type;
        this.localDomain = localDomain;
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

    public String getLocalDomain() {
        return localDomain;
    }

    public void setLocalDomain(String localDomain) {
        this.localDomain = localDomain;
    }

    @NonNull
    @Override
    public String toString() {
        return "School{" +
                "schoolId='" + schoolId + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", localDomain='" + localDomain + '\'' +
                ", type=" + type +
                '}';
    }

    public enum Type {
        KINDER(1),
        ELEMENT(2),
        MIDDLE(3),
        HIGH(4),
        MIDDLE_HIGH(5);

        private final int id;

        Type(int id) {
            this.id = id;
        }

        public int toInteger() {
            return id;
        }

        @NonNull
        @Override
        public String toString() {
            String string;
            switch (id) {
                case 1:
                    string = "유치원";
                    break;
                case 2:
                    string = "초등학교";
                    break;
                case 3:
                    string = "중학교";
                    break;
                case 4:
                    string = "고등학교";
                    break;
                case 5:
                    string = "중·고등학교";
                    break;
                default:
                    throw new IllegalArgumentException();
            }

            return string;
        }
    }
}
