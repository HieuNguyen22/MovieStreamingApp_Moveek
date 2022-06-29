package com.example.projectmoveek;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnSignIn;
    private TextView tvForgotPassword, tvSignUp;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnSignIn = findViewById(R.id.btn_sign_in);
        tvForgotPassword = findViewById(R.id.tv_forgot_password);
        tvSignUp = findViewById(R.id.tv_sign_up);
        mAuth = FirebaseAuth.getInstance();


        // Catch event click sign up
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        // Catch event click sign in
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                catchEventSignIn();
            }
        });
        
        // Catch event click forgot password
        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                catchEventForgotPassword();
            }
        });

    }

    private void catchEventForgotPassword() {
        EditText resetMail = new EditText(this);
        resetMail.setMaxLines(1);
        AlertDialog.Builder resetPasswordDialog = new AlertDialog.Builder(this);
        resetPasswordDialog.setTitle("Reset Password?");
        resetPasswordDialog.setMessage("Enter your email to received reset link.");
        resetPasswordDialog.setView(resetMail);

        resetPasswordDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Extract the email and send reset link
                String mail = resetMail.getText().toString();
                mAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(SignInActivity.this, "Reset link was sent to your email.", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignInActivity.this, "Error!"+e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        resetPasswordDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Close the dialog

            }
        });

        resetPasswordDialog.create().show();
    }

    private void catchEventSignIn() {
        String strEmail = etEmail.getText().toString().trim();
        String strPassword = etPassword.getText().toString().trim();
        if(strEmail.isEmpty() == true) {
            etEmail.setError("Email is required!");
            return;
        }
        if(strPassword.isEmpty() == true) {
            etPassword.setError("Password is required!");
            return;
        }
        if(strPassword.length() < 6) {
            etPassword.setError("Password must be >= 6 characters");
            return;
        }
        mAuth.signInWithEmailAndPassword(strEmail, strPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(SignInActivity.this, "Sign in successfully", Toast.LENGTH_SHORT).show();

                            finishAffinity();
                            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else
                            Toast.makeText(SignInActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}