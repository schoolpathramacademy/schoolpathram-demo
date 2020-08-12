package com.schoolpathram.schoolpathramdotcom.ui.results;

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

public class ResultsFragment extends Fragment {
    GridLayout gridLayout;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.results_fragment, container, false);
        ((MainActivity) getActivity()).setActionBarTitle("Results Related");


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
                    if (cardView.getId() == R.id.resultExam) {
                        openWebPage("http://keralaresults.nic.in/");
                    }
                    else if (cardView.getId() == R.id.resultArts) {
                        openWebPage("http://state.schoolkalolsavam.in/state/index.php/publishresult/Public_result");
                    }
                    else if (cardView.getId() == R.id.resultSports) {
                        openWebPage("http://schoolsports.in/");
                    }
                    else if (cardView.getId() == R.id.test_1) {
                        openWebPage("https://www.wikipedia.org/");
                    }
                    else if (cardView.getId() == R.id.test_2) {
                        openWebPage("https://samagra.kite.kerala.gov.in/");
                    }
                    else if (cardView.getId() == R.id.test_3) {
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