package com.example.projectmoveek;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectmoveek.controller.DatabaseHelper;
import com.example.projectmoveek.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    private EditText etFullname, etEmail, etPassword, etConfirmPassword, etPhoneNum;
    private TextView tvHaveAcc;
    private Button btnSignUp;
    private FirebaseAuth mAuth;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etFullname = findViewById(R.id.et_fullname);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        etPhoneNum = findViewById(R.id.et_phone_num);
        tvHaveAcc = findViewById(R.id.tv_have_acc);
        btnSignUp = findViewById(R.id.btn_sign_up);

        mAuth = FirebaseAuth.getInstance();
        databaseHelper = new DatabaseHelper(this);

        // Catch event sign up
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                catchEventSignUp();
            }
        });

        // Catch event already have an account
        tvHaveAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



    }

    private void catchEventSignUp() {

        String strFullname = etFullname.getText().toString().trim();
        String strEmail = etEmail.getText().toString().trim();
        String strPassword = etPassword.getText().toString().trim();
        String strConfirmPassword = etConfirmPassword.getText().toString().trim();
        String strPhoneNum = etPhoneNum.getText().toString().trim();

        // CHECK FULLNAME
        if(strFullname.isEmpty() == true) {
            etFullname.setError("Fullname is required!");
            return;
        }
        // CHECK MAIL
        if(strEmail.isEmpty() == true) {
            etEmail.setError("Email is required!");
            return;
        }
        // CHECK PASSWORD
        if(strPassword.isEmpty() == true) {
            etPassword.setError("Password is required!");
            return;
        }
        if(strPassword.length() < 6) {
            etPassword.setError("Password must be >= 6 characters");
            return;
        }
        // CHECK CONFIRM PASSWORD
        if(strConfirmPassword.isEmpty() == true) {
            etConfirmPassword.setError("Confirm Password is required!");
            return;
        }

        if(strConfirmPassword.equals(strPassword) == false){
            etConfirmPassword.setError("Confirm password is different from the password!");
            return;
        }
        // CHECK PHONE
        if(strPhoneNum.isEmpty()) {
            etPhoneNum.setError("Phone number is required!");
            return;
        }

        if(strPhoneNum.isEmpty()) {
            etPhoneNum.setError("Phone number is required!");
            return;
        }

        // Create user on Firebase
        mAuth.createUserWithEmailAndPassword(strEmail, strPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            // Add data to database
                            databaseHelper.addUserData(new UserModel(-1, mAuth.getCurrentUser().getUid()
                                    , strEmail, strPassword, strFullname, strPhoneNum
                                    , "https://firebasestorage.googleapis.com/v0/b/projectmoveekapp.appspot.com/o/" +
                                    "default_avatar.jpg?alt=media&token=0300ab43-f88d-4a48-af95-c6bd4f5d73a7", "I like movie"));

                            Toast.makeText(SignUpActivity.this, "Sign up successfully", Toast.LENGTH_SHORT).show();

                            // Check if already signed in, sign out
                            if(mAuth.getCurrentUser() != null){
                                mAuth.signOut();
                            }

                            // Return sign in activity
                            finish();
                        }
                        else
                            Toast.makeText(SignUpActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                });


    }
}