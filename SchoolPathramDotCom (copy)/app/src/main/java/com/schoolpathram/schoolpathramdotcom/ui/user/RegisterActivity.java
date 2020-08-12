package com.schoolpathram.schoolpathramdotcom.ui.user;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.schoolpathram.schoolpathramdotcom.R;
import com.schoolpathram.schoolpathramdotcom.model.User;

import java.util.UUID;

import io.reactivex.disposables.CompositeDisposable;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = RegisterActivity.class.getSimpleName();

    final String USER_SHARED_PREFERNCE_FILE_NAME = "sp_user_cred";

    User user;
    Intent loginIntent;
//    UserService userService;

    private final CompositeDisposable mDisposable = new CompositeDisposable();
    Button registerButton;
    EditText usernameEditText, passwordEditText, firstNameEditText, lastNameEditText, mobileNUmberEditText, cPasswordEditText;
    Button loginButton;
    SharedPreferences prefs;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

         usernameEditText = findViewById(R.id.username);
         passwordEditText = findViewById(R.id.password);
         firstNameEditText = findViewById(R.id.firstName);
         lastNameEditText = findViewById(R.id.lastName);
         mobileNUmberEditText = findViewById(R.id.mobileNumber);
         cPasswordEditText = findViewById(R.id.cPassword);
        registerButton =  findViewById(R.id.registerUser);
        loginButton = findViewById(R.id.loginUser);

        prefs = getSharedPreferences(USER_SHARED_PREFERNCE_FILE_NAME, Context.MODE_PRIVATE);


        final ProgressBar loadingProgressBar = findViewById(R.id.loading);
        getSupportActionBar().hide();



        registerButton.setOnClickListener(v -> registerUser());

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(loginIntent);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Subscribe to the emissions of the user name from the view model.
        // Update the user name text view, at every onNext emission.
        // In case of error, log the exception.

    }

    @Override
    protected void onStop() {
        super.onStop();
        // clear all the subscriptions
        mDisposable.clear();
    }

    private void registerUser() {


        if (prefs.contains("userExists")) {
            new AlertDialog.Builder(getApplicationContext())
                    .setTitle("Delete entry")
                    .setMessage("Are you sure you want to delete this entry?")

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Continue with delete operation
                            processRegistration();

                        }
                    })
                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getApplicationContext(), "Registration cancelled, Login now to use your account..", Toast.LENGTH_LONG).show();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }


        else
        {
            processRegistration();
        }

    }

    private void processRegistration() {

        String firstName = firstNameEditText.getText().toString();
        String lastName = lastNameEditText.getText().toString();
        String email = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String mobile = mobileNUmberEditText.getText().toString();
        String cPassword = cPasswordEditText.getText().toString();
        if (password.equals(cPassword)) {
            // Disable the update button until the user name update has been done
            registerButton.setEnabled(false);

            String userId = UUID.randomUUID().toString();

            successRegistration(userId, firstName, lastName, email, mobile, password);

            // Subscribe to updating the user name.
            // Re-enable the button once the user name has been updated
//            mDisposable.add(mViewModel.registerUser(firstName, lastName, email, mobile, password)
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(() -> successRegistration(firstName, lastName, email, mobile, password),
//                            throwable -> Log.e(TAG, "Unable to create account", throwable)));
        }
        else {
            Toast.makeText(getApplicationContext(), "Password mismatch, Please try again..", Toast.LENGTH_LONG).show();
            return;
        }

    }


    private void successRegistration(String id, String firstName, String lastName, String email, String mobile, String password) {

        SharedPreferences.Editor editor = prefs.edit();

        editor.putBoolean("userExists", true);
        editor.putString("userId", id);
        editor.putString("firstName", firstName);
        editor.putString("firstName", firstName);
        editor.putString("lastName", lastName);
        editor.putString("email", email);
        editor.putString("mobile", mobile);
        editor.putString("password", password);

        editor.commit();

        Toast.makeText(getApplicationContext(), "User account added, Login now to use your Account", Toast.LENGTH_LONG).show();
        loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }


}