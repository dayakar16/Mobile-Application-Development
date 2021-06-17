//Assignment InClass07
//File name Contact
// Dayakar Ravuri and Anoosh Hari Group 29 A


package com.todo.contacts;

import java.io.IOException;
import java.io.Serializable;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Contact implements Serializable {

    String id, name, email, phone, type;
    private Response response;

    public Contact() {
    }

    public Contact(String id, String name, String email, String phone, String type) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.type = type;
    }

    public Contact(String user) {
        if (user != null) {
            String[] data = user.split(",");
            this.id = data[0];
            this.name = data[1];
            this.email = data[2];
            this.phone = data[3];
            this.type = data[4];
        }
    }

    @Override
    public String toString() {
        return id + "," + name + "," + email + "," + phone + "," + type;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
