package com.schoolpathram.schoolpathramdotcom.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.schoolpathram.schoolpathramdotcom.R;
import com.schoolpathram.schoolpathramdotcom.ui.classroom.ClassFragment;
import com.schoolpathram.schoolpathramdotcom.ui.gallery.GalleryFragment;
import com.schoolpathram.schoolpathramdotcom.ui.news.NewsFragment;
import com.schoolpathram.schoolpathramdotcom.ui.resource.ResourceFragment;
import com.schoolpathram.schoolpathramdotcom.ui.results.ResultsFragment;
import com.schoolpathram.schoolpathramdotcom.ui.study.StudyMaterialActivity;

public class HomeFragment extends Fragment {
    GridLayout gridLayout;
    View v;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v =  inflater.inflate(R.layout.fragment_home, container, false);

        gridLayout=(GridLayout) v.findViewById(R.id.mainGrid);

        setSingleEvent(v, gridLayout);

        return v;

    }


    // we are setting onClickListener for each element
    private void setSingleEvent(final View v, GridLayout gridLayout) {
        for(int i = 0; i<gridLayout.getChildCount();i++){
            final CardView cardView=(CardView)gridLayout.getChildAt(i);
            final int finalI= i;

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (cardView.getId() == R.id.newsCard) {
//                        Toast.makeText(getContext(),"Clicked at News "+ cardView.getId(),
//                                Toast.LENGTH_SHORT).show();
                        Fragment newsFragment = new NewsFragment();
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.homeFragment, newsFragment, "findThisFragment")
                                .addToBackStack(null)
                                .commit();
                    }
                    else if (cardView.getId() == R.id.studyMaterial) {
//                        Toast.makeText(getContext(),"Clicked at News "+ cardView.getId(),
//                                Toast.LENGTH_SHORT).show();


                        Intent intent = new Intent(getContext(), StudyMaterialActivity.class);
                        startActivity(intent);
                        getActivity().finish();


//                        Fragment studyMaterialFragment = new StudyMaterialFragment();
//                        getActivity().getSupportFragmentManager().beginTransaction()
//                                .replace(R.id.homeFragment, studyMaterialFragment, "findThisFragment")
//                                .addToBackStack(null)
//                                .commit();
                    }
                    else if (cardView.getId() == R.id.resultsCard) {
//                        Toast.makeText(getContext(),"Clicked at News "+ cardView.getId(),
//                                Toast.LENGTH_SHORT).show();
                        Fragment resultsFragment = new ResultsFragment();
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.homeFragment, resultsFragment, "findThisFragment")
                                .addToBackStack(null)
                                .commit();
                    }
                    else if (cardView.getId() == R.id.resourcesCard) {
//                        Toast.makeText(getContext(),"Clicked at News "+ cardView.getId(),
//                                Toast.LENGTH_SHORT).show();
                        Fragment resourceFragment = new ResourceFragment();
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.homeFragment, resourceFragment, "findThisFragment")
                                .addToBackStack(null)
                                .commit();
                    }
                    else if (cardView.getId() == R.id.galleryCard) {
//                        Toast.makeText(getContext(),"Clicked at News "+ cardView.getId(),
//                                Toast.LENGTH_SHORT).show();
                        Fragment galleryFragment = new GalleryFragment();
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.homeFragment, galleryFragment, "findThisFragment")
                                .addToBackStack(null)
                                .commit();
                    }
                    else if (cardView.getId() == R.id.classCard) {
//                        Toast.makeText(getContext(),"Clicked at News "+ cardView.getId(),
//                                Toast.LENGTH_SHORT).show();
                        Fragment classFragment = new ClassFragment();
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.homeFragment, classFragment, "findThisFragment")
                                .addToBackStack(null)
                                .commit();
                    }

                 }
            });
        }
    }

}