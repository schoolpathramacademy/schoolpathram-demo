package com.schoolpathram.schoolpathramdotcom.ui.news;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.schoolpathram.schoolpathramdotcom.R;
import com.schoolpathram.schoolpathramdotcom.adapter.NewsAdapter;
import com.schoolpathram.schoolpathramdotcom.model.News;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewsFragmentTest#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsFragmentTest extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private int mColumnCount = 1;

    private ArrayList<News> mNewsData;
    private NewsAdapter mAdapter;



    public NewsFragmentTest() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewsFragmentTest.
     */
    // TODO: Rename and change types and number of parameters
    public static NewsFragmentTest newInstance(String param1, String param2) {
        NewsFragmentTest fragment = new NewsFragmentTest();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
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
        View v =  inflater.inflate(R.layout.fragment_news_test, container, false);



        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.Testlist);

        Context context = getContext();
//        RecyclerView recyclerView = (RecyclerView) view;
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
//            recyclerView.setAdapter(new NewsAdapter(DummyContent.ITEMS));

        mNewsData = new ArrayList<>();

        //Initialize the adapter and set it ot the RecyclerView
        mAdapter = new NewsAdapter(mNewsData, getContext());
        recyclerView.setAdapter(mAdapter);

        //Get the data
        initializeData();

        //Helper class for creating swipe to dismiss and drag and drop functionality
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback
                (ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.DOWN
                        | ItemTouchHelper.UP, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            /**
             * Method that defines the drag and drop functionality
             *
             * @param recyclerView The RecyclerView that contains the list items
             * @param viewHolder   The SportsViewHolder that is being moved
             * @param target       The SportsViewHolder that you are switching the original one with.
             * @return returns true if the item was moved, false otherwise
             */
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {

                //Get the from and to position
                int from = viewHolder.getAdapterPosition();
                int to = target.getAdapterPosition();

                //Swap the items and notify the adapter
                Collections.swap(mNewsData, from, to);
                mAdapter.notifyItemMoved(from, to);
                return true;
            }

            /**
             * Method that defines the swipe to dismiss functionality
             *
             * @param viewHolder The viewholder being swiped
             * @param direction  The direction it is swiped in
             */
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                //Remove the item from the dataset
                mNewsData.remove(viewHolder.getAdapterPosition());

                //Notify the adapter
                mAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }
        });

        //Attach the helper to the RecyclerView
        helper.attachToRecyclerView(recyclerView);

        return v;

    }

    /**
     * Method for initializing the sports data from resources.
     */
    private void initializeData() {
        //Get the resources from the XML file
        String[] newsList = getResources().getStringArray(R.array.news_titles);
        String[] newsInfo = getResources().getStringArray(R.array.news_info);
        TypedArray newsImageResources = getResources().obtainTypedArray(R.array.news_images);
        //Clear the existing data (to avoid duplication)
        mNewsData.clear();
//        User user = new User("test", "test_l", "test@gmail.com", "7736128108");


        //Create the ArrayList of News objects with the titles, images
        // and information about each sport
//        for(int i=0; i<newsList.length; i++){
//            mNewsData.add(new News(newsList[i], newsInfo[i], true, user,
//                    newsImageResources.getResourceId(i,0)));
//        }

        //Recycle the typed array
        newsImageResources.recycle();

        //Notify the adapter of the change
        mAdapter.notifyDataSetChanged();
    }
}