package com.acg.equipt;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {
    private static final int REQUEST_SIGNUP = 0;
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.et_passwd)
    EditText etPasswd;
    @BindView(R.id.btn_login)
    AppCompatButton btnLogin;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.link_signup)
    TextView _signupLink;

    Firebase mRef,mPass;
    private String TAG = LoginActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Firebase.setAndroidContext(this);

        ButterKnife.bind(this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });


    }

    public void login() {
        if (!validate()) {
            onLoginFailed();
            return;
        }

        btnLogin.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
//                        onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 2000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        btnLogin.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        btnLogin.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;
        mRef = new Firebase("https://pointed-ads.firebaseio.com/Email");
        mPass = new Firebase("https://pointed-ads.firebaseio.com/Password");
        String superUser = mRef.toString();
        String superPass = mPass.toString();
        String email = String.valueOf(etEmail.getText());
        String passwd = String.valueOf(etPasswd.getText());

        Log.d(TAG, "EMAIL: " + superUser + "\nPASS: " + superPass);

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Enter a valid email address");
            valid = false;
        }

//        else if(!email.contentEquals(superUser)){
//            etEmail.setError("Invalid email address");
//        }

        else {
            etEmail.setError(null);
        }

        if (passwd.isEmpty() || passwd.length() < 4 || passwd.length() > 10) {
            etPasswd.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        }

//        else if(!passwd.contentEquals(superPass)){
//            etPasswd.setError("Invalid password");
//        }
        else {
            etPasswd.setError(null);
        }

        return valid;
    }




}
