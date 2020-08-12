package com.schoolpathram.schoolpathramdotcom.ui.office;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.schoolpathram.schoolpathramdotcom.MainActivity;
import com.schoolpathram.schoolpathramdotcom.R;

public class OfficeFragment extends Fragment {
    GridLayout gridLayout;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.office_fragment, container, false);
        ((MainActivity) getActivity()).setActionBarTitle("Office Related");


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

                    if (cardView.getId() == R.id.links) {
//                        Toast.makeText(getContext(),"Clicked at News "+ cardView.getId(),
//                                Toast.LENGTH_SHORT).show();

                    }
                    if (cardView.getId() == R.id.userGuide) {
//                        Toast.makeText(getContext(),"Clicked at News "+ cardView.getId(),
//                                Toast.LENGTH_SHORT).show();

                    }
                    if (cardView.getId() == R.id.software) {
//                        Toast.makeText(getContext(),"Clicked at News "+ cardView.getId(),
//                                Toast.LENGTH_SHORT).show();

                    }
                    if (cardView.getId() == R.id.contacts) {
//                        Toast.makeText(getContext(),"Clicked at News "+ cardView.getId(),
//                                Toast.LENGTH_SHORT).show();

                    } if (cardView.getId() == R.id.forms) {
//                        Toast.makeText(getContext(),"Clicked at News "+ cardView.getId(),
//                                Toast.LENGTH_SHORT).show();

                    }
                    if (cardView.getId() == R.id.officeSchoolDetails) {

                        openWebPage("https://sametham.kite.kerala.gov.in/");

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