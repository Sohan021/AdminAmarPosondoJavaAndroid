package com.amarposondo.windows10.adminamarposondo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.amarposondo.windows10.adminamarposondo.Model.phonenumber;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class RegistrationActivityAdmin extends AppCompatActivity {

    EditText txtphone, code, txtpassword;

    FirebaseAuth firebaseAuth;

    String Codesent;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_admin);
        firebaseAuth = FirebaseAuth.getInstance();


        databaseReference = FirebaseDatabase.getInstance().getReference("AdminInfo");

        txtphone = findViewById(R.id.editTextPhone);
        txtpassword = findViewById(R.id.editpassword);
        code = findViewById(R.id.editCode);



        findViewById(R.id.buttonverificationcode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                sendVerificationCode();
            }
        });

        findViewById(R.id.Sign_Up).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String pass = txtpassword.getText().toString().trim();
                if(pass.isEmpty())
                {
                    Toast.makeText(RegistrationActivityAdmin.this,"Error",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String codea = code.getText().toString();

                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(Codesent, codea);

                    signInWithPhoneAuthCredential(credential);

                }


            }
        });

        findViewById(R.id.Sign_In).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrationActivityAdmin.this, LoginActivityAdmin.class));
            }
        });


    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(RegistrationActivityAdmin.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {

                            String pho  = txtphone.getText().toString().trim();
                            String pass = txtpassword.getText().toString().trim();


                            phonenumber info = new phonenumber(

                                    pho,
                                    pass

                            );

                            FirebaseDatabase.getInstance().getReference("AdminInfo")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber())
                                    .setValue(info).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(RegistrationActivityAdmin.this,"Task Complete",Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(RegistrationActivityAdmin.this, LoginActivityAdmin.class));
                                }
                            });

                        }
                        else
                        {

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(getApplicationContext(), "Incorrect Code", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                });
    }

    private void sendVerificationCode() {

        final String phoneno  = txtphone.getText().toString().trim();
        final String password  = txtphone.getText().toString().trim();


        if(phoneno.isEmpty())
        {
            txtphone.setError("Phone Number is required");
            txtphone.requestFocus();
            return;
        }
        if(txtphone.length() < 11)
        {
            txtphone.setError("Phone Number is required");
            txtphone.requestFocus();
            return;
        }

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+880" + phoneno,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks); // OnVerificationStateChangedCallbacks





    }


    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            Codesent = s;
        }
    };
}
