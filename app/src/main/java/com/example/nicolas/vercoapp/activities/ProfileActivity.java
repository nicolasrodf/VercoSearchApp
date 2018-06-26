package com.example.nicolas.vercoapp.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nicolas.vercoapp.R;
import com.parse.DeleteCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class ProfileActivity extends AppCompatActivity {

    //crear menu de opciones
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.options_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.deleteAccount) {

            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.myDialog));
            builder.setMessage("Está seguro que desea Eliminar su cuenta?");
            builder.setCancelable(true);
            builder.setPositiveButton(
                "SI",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ParseQuery query = ParseUser.getQuery();
                        query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
                        query.getFirstInBackground(new GetCallback<ParseObject>() {
                            @Override
                            public void done(ParseObject object, ParseException e) {
                            if (object == null) {
                                Log.d("Error", "The getFirst request failed.");
                            } else {
                                object.deleteInBackground(new DeleteCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e==null) {
                                            Toast.makeText(getApplicationContext(), "Cuenta eliminada.", Toast.LENGTH_SHORT).show();
                                            ParseUser.logOut();
                                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(ProfileActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                            }
                        });
                    }
                });

            builder.setNegativeButton(
                "NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

            AlertDialog alert11 = builder.create();
            alert11.show();

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Perfil");

        TextView nameTextView = (TextView) findViewById(R.id.nameTextView);
        TextView usernameTextView = (TextView) findViewById(R.id.usernameTextView);
        final EditText emailEditText = (EditText) findViewById(R.id.emailEditText);
        final EditText addressEditText = (EditText) findViewById(R.id.addressEditText);
        final EditText phoneEditText = (EditText) findViewById(R.id.phoneEditText);
        final CheckBox checkBox = (CheckBox) findViewById(R.id.checkbox);
        final Button button = (Button) findViewById(R.id.button);

        String name = ParseUser.getCurrentUser().getString("name");
        String username = ParseUser.getCurrentUser().getUsername();
        String email = ParseUser.getCurrentUser().getEmail();
        String address = ParseUser.getCurrentUser().getString("address");
        String phone = ParseUser.getCurrentUser().getString("phone");
        boolean check = ParseUser.getCurrentUser().getBoolean("notifications");

        nameTextView.setText(name);
        usernameTextView.setText(username);
        emailEditText.setText(email);
        addressEditText.setText(address);
        phoneEditText.setText(phone);
        checkBox.setChecked(check);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button.setText("APLICANDO...");
                button.setEnabled(false);
            String email = emailEditText.getText().toString();
            String address = addressEditText.getText().toString();
            String phone = phoneEditText.getText().toString();
            boolean newCheck = checkBox.isChecked();
            ParseUser.getCurrentUser().put("email", email);
            ParseUser.getCurrentUser().put("address", address);
            ParseUser.getCurrentUser().put("phone", phone);
            ParseUser.getCurrentUser().put("notifications", newCheck);
            ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e==null){
                        Toast.makeText(ProfileActivity.this, "Info actualizada.", Toast.LENGTH_SHORT).show();
                    } else{
                        Toast.makeText(ProfileActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
            }
        });
    }

}
