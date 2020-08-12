package com.schoolpathram.schoolpathramdotcom;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.schoolpathram.schoolpathramdotcom.db.AppDatabase;
import com.schoolpathram.schoolpathramdotcom.helper.CustomJSONObjectRequest;
import com.schoolpathram.schoolpathramdotcom.helper.VolleyCallback;
import com.schoolpathram.schoolpathramdotcom.helper.VolleyController;
import com.schoolpathram.schoolpathramdotcom.model.Category;
import com.schoolpathram.schoolpathramdotcom.model.Media;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    protected DrawerLayout drawer;
    FloatingActionButton fab;
    protected NavigationView navigationView;
    boolean doubleBackToExitPressedOnce = false;

    ActionBarDrawerToggle mDrawerToggle;
    private AppBarConfiguration mAppBarConfiguration;
    final String USER_SHARED_PREFERNCE_FILE_NAME = "sp_user_cred";
    private static int RESULT_LOAD_IMAGE = 2;
    ImageView profileImageView;
    SharedPreferences prefs;
    Map <String,Category> categoriesMap =  new HashMap<String,Category>();
    Map <String,Media> mediaMap =  new HashMap<String,Media>();
    private static AppDatabase mDb;
    private ArrayList<Category> categories;
    private ArrayList<Media> mediaArrayList;
    private boolean isCategoryReady = false;
    private boolean isMediaReady = false;
    SharedPreferences.Editor editor;
    private boolean catFetchComplete = false;
    private boolean mediaFetchComplete = false;
    private int catPageCount=1;
    private int mediaPageCount=1;


    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefs = getSharedPreferences(USER_SHARED_PREFERNCE_FILE_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();

        mDb = AppDatabase.getInstance(getApplicationContext());

        categories = new ArrayList<Category>();
        mediaArrayList = new ArrayList<Media>();

//        app data api
//        boolean dataReady = updateApplicationDependencyDataFromServer();
//
//        if (!dataReady) {
//            Log.e(TAG, "Data retrieval not completed successfully");
//        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//                getSupportActionBar().setBackgroundDrawable(R.drawable.bg_gradient_1);
//                FloatingActionButton fab = findViewById(R.id.fab);
//                fab.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                                .setAction("Action", null).show();
//                    }
//                });
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);


        View headerview = navigationView.getHeaderView(0);
        TextView profilename = (TextView) headerview.findViewById(R.id.navHeaderUserName);
        String firstName = prefs.getString("firstName", null);
        profilename.setText(firstName);

        profileImageView = (ImageView) headerview.findViewById(R.id.profileImageView);


        if (prefs.contains("profilePicturePath")) {
            String profilePicturePath = prefs.getString("profilePicturePath", null);
            displayProfilePicture(profilePicturePath);
        }


        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_news, R.id.nav_results, R.id.nav_resources, R.id.nav_office, R.id.nav_class, R.id.nav_about)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

//    private boolean updateApplicationDependencyDataFromServer() {
//
//
//        do {
//            String catUrl = "http://www.schoolpathramacademy.com/wp-json/wp/v2/categories?per_page=100&page="+catPageCount;
//            getAllCategoriesFromServer(catUrl, new VolleyCallback() {
//
//                @Override
//                public void onSuccess(JSONArray result) throws JSONException {
//                    if (result.length() == 0) {
//                        catFetchComplete = true;
//                    }
//
//                    for (int i = 0; i < result.length(); i++) {
//                        try {
//                            JSONObject object = result.getJSONObject(Integer.parseInt(String.valueOf(i)));
//
//                            int catId = Integer.parseInt(object.getString("id"));
//                            String catName = object.getString("name");
//
//                            Category category = new Category(catId, catName);
//                            categories.add(category);
////                            categoriesMap.put(String.valueOf(category.getId()), category);
//
//                        } catch (JSONException e) {
//                            Log.e(TAG, "Json parsing error: " + e.getMessage());
//                        }
//                    }
//                    new CategoriesInsertAsyncTask(categories).execute();
//                    isCategoryReady = true;
//                    editor.putBoolean("isCategoryReady", true);
//                    catPageCount = catPageCount+1;
//                }
//
//                @Override
//                public void onError(String result) throws Exception {
//
//                }
//            });
//        } while (catFetchComplete);
//
//        do {
//            String mediaUrl = "http://www.schoolpathramacademy.com/wp-json/wp/v2/media?per_page=100&page=" + mediaPageCount;
//
//            getAllMediaFromServer(mediaUrl, new VolleyCallback() {
//
//                @Override
//                public void onSuccess(JSONArray result) throws JSONException {
//                    if (result.length() == 0) {
//                        mediaFetchComplete = true;
//                    }
//                    for (int i = 0; i < result.length(); i++) {
//                        try {
//                            JSONObject object = result.getJSONObject(Integer.parseInt(String.valueOf(i)));
//                            int mediaId = Integer.parseInt(object.getString("id"));
//                            String mediaUrl = object.getString("source_url");
//                            String mediaType = object.getString("media_type");
//                            Media media = new Media(mediaId, mediaUrl, mediaType);
////                            mediaMap.put(String.valueOf(media.getId()), media);
//                            mediaArrayList.add(media);
//                        } catch (JSONException e) {
//                            Log.e(TAG, "Json parsing error: " + e.getMessage());
//                        }
//
//                    }
//                    new MediaInsertAsyncTask(mediaArrayList).execute();
//                    isMediaReady = true;
//                    editor.putBoolean("isMediaReady", true);
//                    mediaPageCount = mediaPageCount+1;
//                }
//
//                @Override
//                public void onError(String result) throws Exception {
//
//                }
//            });
//        } while (mediaFetchComplete);
//
//        return (isMediaReady && isCategoryReady);
//
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            this.finishAffinity();
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            displayProfilePicture(picturePath);
            SharedPreferences.Editor editor = prefs.edit();

            editor.putString("profilePicturePath", picturePath);
            editor.commit();

//            if (checkSelfPermission(
//                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                requestPermissions(arr(Manifest.permission.READ_EXTERNAL_STORAGE), MY_READ_EXTERNAL_REQUEST)
//            }

//            Glide.with(getApplicationContext()).load(picturePath)
//                    .thumbnail(0.5f)
////                .crossFade()
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .into(profileImageView);

//            try ( InputStream is = new URL( picturePath ).openStream() ) {
//                Bitmap bitmap = BitmapFactory.decodeStream( is );
//                profileImageView.setImageBitmap(bitmap);
//
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }



//            .setImageURI(Uri.parse(picturePath));

            cursor.close();
            // String picturePath contains the path of selected Image
        }
    }
    public  void displayProfilePicture(String picturePath) {
        Glide.with(getApplicationContext()).load(picturePath)
                .thumbnail(0.5f)
//                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(profileImageView);
    }




    public void getAllCategoriesFromServer( final String endpoint, final VolleyCallback callback) {

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
    public void getAllMediaFromServer( final String endpoint, final VolleyCallback callback) {
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

    private static class CategoriesInsertAsyncTask extends android.os.AsyncTask<Void, Void, Void> {
        ArrayList<Category> categories;

        public CategoriesInsertAsyncTask(ArrayList<Category> categories) {
            this.categories=categories;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mDb.categoryDao().insertAllCategories(categories);
            return null;
        }

    }
    private static class MediaInsertAsyncTask extends android.os.AsyncTask<Void, Void, Void> {
        ArrayList<Media> mediaList;

        public MediaInsertAsyncTask(ArrayList<Media> mediaList) {
            this.mediaList = mediaList;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mDb.mediaDao().insertAllMedia(mediaList);
            return null;
        }

    }


}
