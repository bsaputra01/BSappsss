package com.example.firebasecrud.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Lecturer implements Parcelable {
    private String id, name, gender, expertise;

    public Lecturer() {
    }

    public Lecturer(String id, String name, String gender, String expertise) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.expertise = expertise;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getExpertise() {
        return expertise;
    }

    public void setExpertise(String expertise) {
        this.expertise = expertise;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.gender);
        dest.writeString(this.expertise);
    }

    protected Lecturer(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.gender = in.readString();
        this.expertise = in.readString();
    }

    public static final Creator<Lecturer> CREATOR = new Creator<Lecturer>() {
        @Override
        public Lecturer createFromParcel(Parcel source) {
            return new Lecturer(source);
        }

        @Override
        public Lecturer[] newArray(int size) {
            return new Lecturer[size];
        }
    };
}
