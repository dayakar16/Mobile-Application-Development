package com.inclass03;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.IntegerRes;

import java.io.Serializable;

/*
Assignement # InClass 03
FileName: Profile.java
FullName of Students: Anoosh Hari and Dayakar Ravuri
*/

public class Profile implements Parcelable {
    String name;
    String email;
    String studentId;
    String department;

    public Profile() {
    }

    public Profile(String name, String email, String studentId, String department) {
        this.name = name;
        this.email = email;
        this.studentId = studentId;
        this.department = department;
    }

    protected Profile(Parcel in) {
        name = in.readString();
        email = in.readString();
        studentId = in.readString();
        department = in.readString();
    }

    public static final Creator<Profile> CREATOR = new Creator<Profile>() {
        @Override
        public Profile createFromParcel(Parcel in) {
            return new Profile(in);
        }

        @Override
        public Profile[] newArray(int size) {
            return new Profile[size];
        }
    };

    @Override
    public String toString() {
        return "Profile{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", studentId=" + studentId +
                ", department='" + department + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.email);
        dest.writeString(this.studentId+"");
        dest.writeString(this.department);
    }
}
