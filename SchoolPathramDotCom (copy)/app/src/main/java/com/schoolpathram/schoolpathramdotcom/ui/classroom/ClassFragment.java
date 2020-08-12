package com.schoolpathram.schoolpathramdotcom.ui.classroom;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.schoolpathram.schoolpathramdotcom.MainActivity;
import com.schoolpathram.schoolpathramdotcom.R;

public class ClassFragment extends Fragment {
    GridLayout gridLayout;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.class_fragment);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.class_fragment, container, false);

        ((MainActivity) getActivity()).setActionBarTitle("Class Related");



        gridLayout=(GridLayout) v.findViewById(R.id.mainGrid);

        setSingleEvent(gridLayout);
        return v;

    }

    // we are setting onClickListener for each element
    private void setSingleEvent(GridLayout gridLayout) {
        for(int i = 0; i<gridLayout.getChildCount();i++){
            CardView cardView=(CardView)gridLayout.getChildAt(i);
            final int finalI= i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (cardView.getId() == R.id.eLibrary) {
                        openWebPage("http://www.keralasahityaakademi.org/online_library/index.html");
                    }
                    else if (cardView.getId() == R.id.dictionary) {
                        openWebPage("http://www.olam.in");
                    }
                    else if (cardView.getId() == R.id.victers) {
                        openWebPage("https://victers.kite.kerala.gov.in/");
                    }
                    else if (cardView.getId() == R.id.wikipedia) {
                        openWebPage("https://www.wikipedia.org/");
                    }
                    else if (cardView.getId() == R.id.samagra) {
                        openWebPage("https://samagra.kite.kerala.gov.in/");
                    }
                            else if (cardView.getId() == R.id.downloads) {
                        Toast.makeText(getContext(),"Clicked at downloads "+ cardView.getId(),
                                Toast.LENGTH_SHORT).show();
                    }

                }
            });
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