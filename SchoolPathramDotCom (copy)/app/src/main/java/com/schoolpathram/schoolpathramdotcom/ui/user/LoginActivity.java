package com.schoolpathram.schoolpathramdotcom.ui.user;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.schoolpathram.schoolpathramdotcom.MainActivity;
import com.schoolpathram.schoolpathramdotcom.R;
import com.schoolpathram.schoolpathramdotcom.helper.CustomJSONObjectRequest;
import com.schoolpathram.schoolpathramdotcom.helper.VolleyCallback;
import com.schoolpathram.schoolpathramdotcom.helper.VolleyController;
import com.schoolpathram.schoolpathramdotcom.model.Category;
import com.schoolpathram.schoolpathramdotcom.model.Media;
import com.schoolpathram.schoolpathramdotcom.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import io.reactivex.disposables.CompositeDisposable;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();


    //    private LoginViewModel loginViewModel;
    Intent registerIntent, mainIntent;
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    ProgressBar loadingProgressBar;
    EditText usernameEditText;
    EditText passwordEditText;
    SharedPreferences sharedPreferences;

    EditText imageViewButton1;
    EditText imageViewButton2;
    EditText imageViewButton3;
    EditText imageViewButton4;

    String loginPwd;
    final String USER_SHARED_PREFERNCE_FILE_NAME = "sp_user_cred";
    Map <String,Category> categoriesMap =  new HashMap<String,Category>();
    Map <String, Media> mediaMap =  new HashMap<String,Media>();

    private ArrayList<Media> mediaArrayList;
    private ArrayList<Category> categoryArrayList;





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory())
//                .get(LoginViewModel.class);



        imageViewButton1 = findViewById(R.id.login_circle_1);
        imageViewButton2 = findViewById(R.id.login_circle_2);
        imageViewButton3 = findViewById(R.id.login_circle_3);
        imageViewButton4 = findViewById(R.id.login_circle_4);


//        imageViewButton1.getText()
//        imageViewButton1.setOnClickListener(v -> animateCircle(imageViewButton1));
        imageViewButton2.setOnClickListener(v -> openNumPad());
        imageViewButton3.setOnClickListener(v -> openNumPad());
        imageViewButton4.setOnClickListener(v -> openNumPad());

        imageViewButton4.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_DEL) {
                    imageViewButton3.requestFocus();
                }
                return false;
            }
        });

        imageViewButton3.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_DEL) {
                    imageViewButton2.requestFocus();
                }
                return false;
            }
        });
        imageViewButton2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_DEL) {
                    imageViewButton1.requestFocus();
                }
                return false;
            }
        });
        imageViewButton1.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                animateCircle(imageViewButton1);

                String pwdString = String.valueOf(imageViewButton1.getText());
                if (pwdString.isEmpty()) {
                    imageViewButton1.requestFocus();
                }
                else {
                    imageViewButton2.requestFocus();
                }

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(imageViewButton2, InputMethodManager.SHOW_IMPLICIT);

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                imageViewButton1.setTransformationMethod(PasswordTransformationMethod.getInstance());
//                loginPwd = (String) s;
            }
        });
        imageViewButton2.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                animateCircle(imageViewButton2);


                String pwdString = String.valueOf(imageViewButton2.getText());
                if (pwdString.isEmpty()) {
                    imageViewButton2.requestFocus();
                }
                else {
                    imageViewButton3.requestFocus();
                }

//                imageViewButton3.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(imageViewButton3, InputMethodManager.SHOW_IMPLICIT);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                imageViewButton2.setTransformationMethod(PasswordTransformationMethod.getInstance());

//                loginPwd = (String) s;
            }
        });
        imageViewButton3.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {

                animateCircle(imageViewButton3);

                String pwdString = String.valueOf(imageViewButton3.getText());
                if (pwdString.isEmpty()) {
                    imageViewButton3.requestFocus();
                }
                else {
                    imageViewButton4.requestFocus();
                }

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(imageViewButton4, InputMethodManager.SHOW_IMPLICIT);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                imageViewButton3.setTransformationMethod(PasswordTransformationMethod.getInstance());

//                loginPwd = (String) s;
            }
        });

        imageViewButton4.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {

                animateCircle(imageViewButton4);
                loginUser();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                imageViewButton4.setTransformationMethod(PasswordTransformationMethod.getInstance());

//                loginPwd = (String) s;
            }
        });




        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
//        final Button loginButton = findViewById(R.id.login);
        final Button registerButton = findViewById(R.id.register);

        loadingProgressBar = findViewById(R.id.loading);
        getSupportActionBar().hide();




//        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
//            @Override
//            public void onChanged(@Nullable LoginResult loginResult) {
//                if (loginResult == null) {
//                    return;
//                }
//                loadingProgressBar.setVisibility(View.GONE);
//                if (loginResult.getError() != null) {
//                    showLoginFailed(loginResult.getError());
//                }
//                if (loginResult.getSuccess() != null) {
//                    updateUiWithUser(loginResult.getSuccess());
//                }
//                setResult(Activity.RESULT_OK);
//
//
//                mainIntent = new Intent(getApplicationContext(), MainActivity.class);
//                startActivity(mainIntent);
//                finish();
//                //Complete and destroy login activity once successful
////                finish();
//            }
//        });


//        usernameEditText.addTextChangedListener(afterTextChangedListener);
//        passwordEditText.addTextChangedListener(afterTextChangedListener);
//        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_DONE) {
//                    loginViewModel.login(usernameEditText.getText().toString(),
//                            passwordEditText.getText().toString());
//                }
//                return false;
//            }
//        });

//        loginButton.setOnClickListener(v -> loginUser());



        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerIntent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(registerIntent);
                finish();
//                loadingProgressBar.setVisibility(View.VISIBLE);


            }
        });
    }

    @SuppressLint("ResourceAsColor")
    private void animateCircle(EditText imageViewButton) {
        String passwordString = String.valueOf(imageViewButton.getText());
        if (passwordString.isEmpty()) {
            GradientDrawable sd = (GradientDrawable) imageViewButton.getBackground().mutate();
            int color = ContextCompat.getColor(getApplicationContext(), R.color.white);
            imageViewButton.setTextColor(color);
            sd.setColor(color);
            sd.invalidateSelf();
        }
        else {
            GradientDrawable sd = (GradientDrawable) imageViewButton.getBackground().mutate();
            int color = ContextCompat.getColor(getApplicationContext(), R.color.login_pwd_circle_after);
            imageViewButton.setTextColor(color);
            sd.setColor(color);
            sd.invalidateSelf();
        }
    }

    private void openNumPad() {
        InputMethodManager imm = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
    }

    private void updateUiWithUser(User model) {

    }


    private void loginUser() {
        loadingProgressBar.setVisibility(View.VISIBLE);

        String pwd1 = String.valueOf(imageViewButton1.getText());
        String pwd2 = String.valueOf(imageViewButton2.getText());
        String pwd3 = String.valueOf(imageViewButton3.getText());
        String pwd4 = String.valueOf(imageViewButton4.getText());

        if(pwd1.isEmpty() || pwd2.isEmpty() || pwd3.isEmpty() || pwd4.isEmpty()) {
            loadingProgressBar.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "Login Failed... Try again", Toast.LENGTH_LONG).show();
            return;
        }
        else {
            String combinedPassword = pwd1+pwd2+pwd3+pwd4;
            checkPassword(combinedPassword);
        }

    }

    private void checkPassword(String combinedPassword) {
        SharedPreferences prefs = getSharedPreferences(USER_SHARED_PREFERNCE_FILE_NAME, Context.MODE_PRIVATE);
        String password = prefs.getString("password", null);

        if (prefs.contains("userExists")) {
            if (combinedPassword.equals(password)) {
                SharedPreferences.Editor editor = prefs.edit();
                String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

                editor.putString("dateLastLogin", date);
                editor.commit();

                String email = prefs.getString("email", null);
                String mobile = prefs.getString("mobile", null);
                String firstName = prefs.getString("firstName", null);
                String lastName = prefs.getString("lastName", null);


                User user = new User(firstName, lastName, email, mobile, password);
                loadingProgressBar.setVisibility(View.GONE);
                successLogin(user);
            }
            else {
                loadingProgressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Password error.. Check Password and Try again", Toast.LENGTH_LONG).show();
            }

        }
        else {
            loadingProgressBar.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "User doesn't exists, Please register a new account.", Toast.LENGTH_LONG).show();

        }

    }

    private void successLogin(User user) {
        if (user != null) {





            String welcome = getString(R.string.welcome) + user.getFirstName();
            // TODO : initiate successful logged in experience
            Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
            mainIntent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(mainIntent);
            finish();
        }
    }

    private void processMediaData(JSONArray mediaArray) {

        for (int i = 0; i < mediaArray.length(); i++) {
            try {
                JSONObject object = mediaArray.getJSONObject(Integer.parseInt(String.valueOf(i)));
                int mediaId = Integer.parseInt(object.getString("id"));
                String mediaUrl = object.getString("source_url");
                String mediaType = object.getString("media_type");
//                        UUID uuid = UUID.randomUUID();

                Media media = new Media(mediaId, mediaUrl, mediaType);


//                mediaMap.put(String.valueOf(media.getServerId()), media);
                        mediaArrayList.add(media);

//                        new MediaInsertAsyncTask(mediaArrayList).execute();
            } catch (JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
            }
            catch (Exception e) {
                Log.e(TAG, "Exception Json parsing error: " + e.getMessage());
            }
        }
    }

    private void processCategoryData(JSONArray categoryArray) {

        for (int i = 0; i < categoryArray.length(); i++) {
            try {
                JSONObject object = categoryArray.getJSONObject(Integer.parseInt(String.valueOf(i)));

                int catId = Integer.parseInt(object.getString("id"));
                String catName = object.getString("name");

                Category category = new Category(catId, catName);
                categoryArrayList.add(category);
//                categoriesMap.put(String.valueOf(category.getId()), category);

//                        new CategoriesInsertAsyncTask(categories).execute();

            } catch (JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
            }
        }
    }

    private void LoginFailed(String reason) {
        Toast.makeText(getApplicationContext(), reason, Toast.LENGTH_LONG).show();
    }

    public void getCategories( final String endpoint, final VolleyCallback callback) {

        CustomJSONObjectRequest rq = new CustomJSONObjectRequest(Request.Method.GET,
                endpoint, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.v("Response", response.toString());
                        try {
                            callback.onSuccess(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v("Response", error.toString());
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }

        };
        rq.setPriority(Request.Priority.IMMEDIATE);
        VolleyController.getInstance(getApplicationContext()).addToRequestQueue(rq);

    }

    public String loadJSONFromAsset(Context context, String fileName) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(fileName);

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }


}