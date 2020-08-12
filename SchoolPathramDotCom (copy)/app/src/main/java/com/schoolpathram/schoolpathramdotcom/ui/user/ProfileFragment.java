package com.schoolpathram.schoolpathramdotcom.ui.user;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.schoolpathram.schoolpathramdotcom.R;
import com.schoolpathram.schoolpathramdotcom.model.User;

import io.reactivex.disposables.CompositeDisposable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
//9496025065 58
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private static final String TAG = RegisterActivity.class.getSimpleName();

    final String USER_SHARED_PREFERNCE_FILE_NAME = "sp_user_cred";

    User user;
    Intent loginIntent;
    //    UserService userService;

    private final CompositeDisposable mDisposable = new CompositeDisposable();
    Button updateButton;
    EditText usernameEditText, passwordEditText, firstNameEditText, lastNameEditText, mobileNUmberEditText, cPasswordEditText;
    Button loginButton;
    SharedPreferences prefs;
    ImageView profileImageView;
;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v =  inflater.inflate(R.layout.fragment_profile, container, false);

        usernameEditText = v.findViewById(R.id.emailProfile);
//        passwordEditText = v.findViewById(R.id.password);
        firstNameEditText = v.findViewById(R.id.firstNameProfile);
        lastNameEditText = v.findViewById(R.id.lastNameProfile);
        mobileNUmberEditText = v.findViewById(R.id.mobileNumberProfile);
//        cPasswordEditText = v.findViewById(R.id.cPassword);
        updateButton =  v.findViewById(R.id.profileUpdate);

        profileImageView = (ImageView) v.findViewById(R.id.profileImageViewProfile);



        prefs = getActivity().getSharedPreferences(USER_SHARED_PREFERNCE_FILE_NAME, Context.MODE_PRIVATE);
        persistProfileData();
        final ProgressBar loadingProgressBar = v.findViewById(R.id.loading);


        updateButton.setOnClickListener(view -> updateUser());

        return v;
    }
    private void persistProfileData() {

        if (prefs.contains("userExists")) {

            String email = prefs.getString("email", null);
            String mobile = prefs.getString("mobile", null);
            String firstName = prefs.getString("firstName", null);
            String lastName = prefs.getString("lastName", null);

            usernameEditText.setText(email);
            mobileNUmberEditText.setText(mobile);
            firstNameEditText.setText(firstName);
            lastNameEditText.setText(lastName);
            if (prefs.contains("profilePicturePath")) {
                String profilePicturePath = prefs.getString("profilePicturePath", null);
                displayProfilePicture(profilePicturePath);
            }
//            profileImageViewProfile

        }
    }

    private void updateUser() {
        if (prefs.contains("userExists")) {
            new AlertDialog.Builder(getContext())
                    .setTitle("Are you Sure?")
                    .setMessage("Are you sure. you want to update existing details?")

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Continue with delete operation
                            processUpdating();
                        }
                    })
                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getContext(), "Update cancelled", Toast.LENGTH_LONG).show();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

        else
        {
            Toast.makeText(getContext(), "User account not detected..logging out..", Toast.LENGTH_LONG).show();

            Intent loginIntent = new Intent(getContext(), LoginActivity.class);
            startActivity(loginIntent);
            getActivity().finish();
        }
    }
    private void processUpdating() {

        String firstName = firstNameEditText.getText().toString();
        String lastName = lastNameEditText.getText().toString();
        String email = usernameEditText.getText().toString();
        String mobile = mobileNUmberEditText.getText().toString();
        updateButton.setEnabled(false);
        successUpdating(firstName, lastName, email, mobile);

//        mDisposable.add(mViewModel.updateUser(firstName, lastName, email, mobile)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(() -> successUpdating(firstName, lastName, email, mobile),
//                        throwable -> Log.e(TAG, "Unable to update details", throwable)));

    }
    private void successUpdating(String firstName, String lastName, String email, String mobile) {

        SharedPreferences.Editor editor = prefs.edit();
        String userId = prefs.getString("userId", null);
        String password = prefs.getString("password", null);
        String profilePicturePath = prefs.getString("profilePicturePath", null);


        editor.clear();
        editor.commit();


        editor.putBoolean("userExists", true);
        editor.putString("userId", userId);
        editor.putString("firstName", firstName);
        editor.putString("lastName", lastName);
        editor.putString("email", email);
        editor.putString("mobile", mobile);
        editor.putString("password", password);
        editor.putString("profilePicturePath", profilePicturePath);

        editor.commit();

        Toast.makeText(getContext(), "User account updated..", Toast.LENGTH_LONG).show();
    }

    public  void displayProfilePicture(String picturePath) {
        Glide.with(getContext()).load(picturePath)
                .thumbnail(0.5f)
//                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(profileImageView);
    }

}