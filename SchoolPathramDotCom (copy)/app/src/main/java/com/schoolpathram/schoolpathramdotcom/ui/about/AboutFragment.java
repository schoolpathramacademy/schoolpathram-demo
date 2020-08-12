package com.schoolpathram.schoolpathramdotcom.ui.about;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.schoolpathram.schoolpathramdotcom.MainActivity;
import com.schoolpathram.schoolpathramdotcom.R;

public class AboutFragment extends Fragment {

    ListView listView ;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.about_fragment, container, false);

        ((MainActivity) getActivity()).setActionBarTitle("About");


        // Get ListView object from xml
        listView = (ListView) v.findViewById(R.id.aboutList);

        // Defined Array values to show in ListView
        String[] values = new String[] { "Version : 1.0 beta",
                "Contact us",
                "Like us on Facebook",
                "Follow us on Twitter",
                "Subscribe us on Youtube",
                "Rate us on Play Store",
                "Terms and Conditions",
        };

        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1, android.R.id.text1, values) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
// Get the Item from ListView
                View view = super.getView(position, convertView, parent);

                // Initialize a TextView for ListView each Item
                TextView tv = (TextView) view.findViewById(android.R.id.text1);

                // Set the text color of TextView (ListView Item)
                tv.setTextColor(Color.WHITE);

                // Generate ListView Item using TextView

                //You need to add the following line for this solution to work; thanks skayred




                return view;            }
        };


        // Assign adapter to ListView
        listView.setAdapter(adapter);

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition     = position;

                // ListView Clicked item value
                String  itemValue    = (String) listView.getItemAtPosition(position);
                if (itemValue == "Like us on Facebook") {
                    getOpenFacebookIntent(getContext());
                }
                else if (itemValue == "Subscribe us on Youtube") {
                    getOpenYoutubeIntent(getContext());
                }
                else if (itemValue == "Rate us on Play Store") {
                    getOpenPlayStoreIntent(getContext());
                }
                else if (itemValue == "Contact us") {
                    openWebPage("https://www.schoolpathramacademy.com\n");
                }

            }

        });


        return v;

    }

    public void getOpenFacebookIntent(Context context) {

        try {
            context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/%E0%B4%B8%E0%B5%8D%E0%B4%95%E0%B5%82%E0%B5%BE-%E0%B4%AA%E0%B4%A4%E0%B5%8D%E0%B4%B0%E0%B4%82%E0%B4%95%E0%B5%8B%E0%B4%82-313222356001764/")));
        } catch (Exception e) {
            openWebPage("https://www.facebook.com/%E0%B4%B8%E0%B5%8D%E0%B4%95%E0%B5%82%E0%B5%BE-%E0%B4%AA%E0%B4%A4%E0%B5%8D%E0%B4%B0%E0%B4%82%E0%B4%95%E0%B5%8B%E0%B4%82-313222356001764/");
//            return new Intent(Intent.ACTION_VIEW, Uri.parse(""));
            return ;
        }
    }
    public void getOpenYoutubeIntent(Context context) {

        try {
            context.getPackageManager().getPackageInfo("com.google.android.youtube", 0);
            startActivity( new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/UCCzWbTbsJQ4IeE968whoQpw")));
        } catch (Exception e) {
            openWebPage("https://www.youtube.com/channel/UCCzWbTbsJQ4IeE968whoQpw");
        }
    }

    public void getOpenPlayStoreIntent(Context context) {

        final String appPackageName = context.getPackageName(); // getPackageName() from Context or Activity object
        try {
            startActivity (new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity (new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }
    public void openWebPage(String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }



}