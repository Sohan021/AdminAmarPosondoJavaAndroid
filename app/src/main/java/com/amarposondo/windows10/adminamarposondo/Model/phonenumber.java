package com.amarposondo.windows10.adminamarposondo.Model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class phonenumber {
    private String phone, password;
    private DatabaseReference databaseReference;

    public phonenumber()
    {

    }

    public phonenumber(String phone, String password) {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        this.phone = phone;
        this.password = password;
    }






    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
