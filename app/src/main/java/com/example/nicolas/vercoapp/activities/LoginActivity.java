package com.example.nicolas.vercoapp.activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nicolas.vercoapp.R;
import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {

    Boolean signUpModeActive = true;
    TextView changeSignupModeTextview;
    EditText passwordEditText;
    EditText emailEditText;
    EditText usernameEditText;
    EditText fullNameEditText;
    CheckBox checkBox;
    Button signupButton;

    public void showModeSelection() {

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed()
    {
        moveTaskToBack(true);
    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN){
            signUp(view); //entonces al hacer enter despues de ingresar la pass, se va a la funcionalidad
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        if (signUpModeActive){
            signUpModeActive = false;
            signupButton.setText("INGRESAR");
            fullNameEditText.setVisibility(View.GONE);
            emailEditText.setVisibility(View.GONE);
            checkBox.setVisibility(View.GONE);
            changeSignupModeTextview.setText("Registrarse");

        } else {
            signUpModeActive = true;
            signupButton.setText("REGISTRARSE");
            fullNameEditText.setVisibility(View.VISIBLE);
            fullNameEditText.requestFocus();
            emailEditText.setVisibility(View.VISIBLE);
            checkBox.setVisibility(View.VISIBLE);
            changeSignupModeTextview.setText("Ingresar");
        }
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService((INPUT_METHOD_SERVICE));
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    public void signUp(View view) {

        usernameEditText = (EditText) findViewById(R.id.username_edit_text);
        passwordEditText = (EditText) findViewById(R.id.password_edit_text);

        if (signUpModeActive) {

            if (fullNameEditText.getText().toString().matches("") || usernameEditText.getText().toString().matches("") ||
                    emailEditText.getText().toString().matches("") || passwordEditText.getText().toString().matches("")) {

                Toast.makeText(this, "Tiene campos vacíos.", Toast.LENGTH_SHORT).show();

            } else {

                signupButton.setText("REGISTRANDO......");
                signupButton.setEnabled(false);

                ParseUser user = new ParseUser();
                user.put("name", fullNameEditText.getText().toString());
                user.put("notifications", checkBox.isChecked());
                user.setUsername(usernameEditText.getText().toString());
                user.setEmail(emailEditText.getText().toString());
                user.setPassword(passwordEditText.getText().toString());

                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {

                    if (e == null) {

                        Toast.makeText(LoginActivity.this, "Registro exitoso!", Toast.LENGTH_SHORT).show();
                        showModeSelection();
                        Log.i("Signup", "Successful");

                    } else {

                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    }
                });

            }

        } else {

            ParseUser.logInInBackground(usernameEditText.getText().toString(),
                passwordEditText.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (user != null) {
                            signupButton.setText("INGRESANDO......");
                            signupButton.setEnabled(false);

                            Log.i("Login", "Login Successful");
                            showModeSelection();

                        } else {

                            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle("Verco App");

        fullNameEditText = (EditText) findViewById(R.id.name_edit_text);
        emailEditText = (EditText) findViewById(R.id.email_edit_text);
        checkBox = (CheckBox) findViewById(R.id.checkbox);
        signupButton = (Button) findViewById(R.id.signUp_button);

        @SuppressLint("RestrictedApi") AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.myDialog));
        builder.setMessage("Al registrarse obtendrá un descuento de 5% en su primera compra!");
        builder.setCancelable(true);

        builder.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();

        changeSignupModeTextview = (TextView) findViewById(R.id.change_signup_mode_text_view);
        changeSignupModeTextview.setOnClickListener(this);
        passwordEditText = (EditText) findViewById(R.id.password_edit_text);
        passwordEditText.setOnKeyListener(this);

        //chequear si user esta logeado
        if (ParseUser.getCurrentUser() != null){
            showModeSelection();
        }

        ParseAnalytics.trackAppOpenedInBackground(getIntent());

    }

}
