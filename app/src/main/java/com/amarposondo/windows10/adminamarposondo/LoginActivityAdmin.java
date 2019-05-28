package com.amarposondo.windows10.adminamarposondo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.amarposondo.windows10.adminamarposondo.Model.phonenumber;
import com.amarposondo.windows10.adminamarposondo.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class LoginActivityAdmin extends AppCompatActivity {

    private EditText txtphn, txtpass;
    private Button lgn;
    private CheckBox checkBox;


    private String parentDbName = "AdminInfo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_two);

        txtphn = findViewById(R.id.editTextPhonelogin);
        txtpass = findViewById(R.id.editpasswordlogin);
        checkBox = findViewById(R.id.rememberme);

        lgn = findViewById(R.id.loginid);

        Paper.init(this);

        lgn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginUser();
            }
        });

    }
    private void loginUser() {


        String phoneno = txtphn.getText().toString().trim();
        String password = txtpass.getText().toString().trim();



        if(TextUtils.isEmpty(phoneno) )
        {
            Toast.makeText(LoginActivityAdmin.this,"Please Enter Phone Number",Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(password) )
        {
            Toast.makeText(LoginActivityAdmin.this,"Please Enter Password",Toast.LENGTH_SHORT).show();
            return;
        }

        else
        {
            AllowAccessToAccount(phoneno, password);
        }

    }

    private void AllowAccessToAccount(final String phone, final String password) {
        if(checkBox.isChecked())
        {
            Paper.book().write(Prevalent.UserPhonekey, phone);
            Paper.book().write(Prevalent.UserPasswordkey, password);
        }
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference("AdminInfo");

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    phonenumber userData = ds.getValue(phonenumber.class);
                    if(userData.getPhone().equals(phone))
                    {
                        if(userData.getPassword().equals(password))
                        {
                            if(parentDbName.equals("AdminInfo")) {
                                Toast.makeText(LoginActivityAdmin.this, "Welcome, Login SuccessFully", Toast.LENGTH_SHORT).show();
                                //loadingBar.dismiss();
                                Intent intent = new Intent(LoginActivityAdmin.this, AdminCategoryActivity.class);
                                Prevalent.currentOnlineUsers = userData;
                                startActivity(intent);
                            }
                        }
                        else
                        {
                            //loadingBar.dismiss();
                            Toast.makeText(LoginActivityAdmin.this,"Incorrect " +phone+ " Number",Toast.LENGTH_SHORT).show();
                        }

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }




}


