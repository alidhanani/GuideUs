package com.szabist.fyp.guideus;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    Switch switchUser;
    EditText txtUser, txtPass;
    TextView txtForget, txtHaveAccount;
    Button btnLogin;
    private static final String TAG = "EmailPassword";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        instialise();
        workingSwitch();
        btnLogin();
        ForgetPasswordFunc();
    }

    private void instialise() {
        txtUser = (EditText) findViewById(R.id.txtLoginUser);
        switchUser = (Switch) findViewById(R.id.switchUser);
        txtPass = (EditText) findViewById(R.id.txtLoginPass);
        txtForget = (TextView) findViewById(R.id.txtForgotPass);
        txtHaveAccount = (TextView) findViewById(R.id.txtNoAccount);
        btnLogin = (Button) findViewById(R.id.btnLogin);

    }

    private String getUsername() {
        return txtUser.getText().toString();
    }

    private String getPassword() {
        return txtPass.getText().toString();
    }

    private void btnLogin(){
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String user = getUsername();
                String pass = getPassword();

                if(user.equals("")) {
                    txtUser.setError("Enter Username");
                } else if(pass.equals("")) {
                    txtPass.setError("Enter Password");
                } else {
                    workingFirebase(txtUser.getText().toString(), txtPass.getText().toString());
                }
            }
        });
    }

    private void workingSwitch() {
        switchUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(switchUser.isChecked()) {
                    switchUser.setText("Business");
                } else {
                    switchUser.setText("User");
                }
            }
        });
    }

    private void ForgetPasswordFunc() {
        txtForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailAddress = getUsername();
                if(emailAddress.equals("")) {
                    txtUser.setError("Enter Email Address");
                } else {
                    FirebasePassword(emailAddress);
                }
            }
        });
    }

    private void FirebasePassword(String email) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent.");
                        }
                    }
                });
    }

    private void DontHaveAccount() {
        txtHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(i);
            }
        });
    }

    private void workingFirebase(String email, String pass) {
        System.out.print(email+" "+pass);
        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if(switchUser.isChecked()) {
                                Intent i = new Intent(LoginActivity.this, BusinessHomePageActivity.class);
                                startActivity(i);
                            } else {
                                Intent i = new Intent(LoginActivity.this, UserHomePageActivity.class);
                                startActivity(i);
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

}
