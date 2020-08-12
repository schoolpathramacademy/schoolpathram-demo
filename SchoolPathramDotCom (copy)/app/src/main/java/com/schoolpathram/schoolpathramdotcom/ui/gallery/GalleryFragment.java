package com.schoolpathram.schoolpathramdotcom.ui.gallery;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.schoolpathram.schoolpathramdotcom.AppController;
import com.schoolpathram.schoolpathramdotcom.MainActivity;
import com.schoolpathram.schoolpathramdotcom.R;
import com.schoolpathram.schoolpathramdotcom.adapter.GalleryAdapter;
import com.schoolpathram.schoolpathramdotcom.helper.EndlessRecyclerViewScrollListener;
import com.schoolpathram.schoolpathramdotcom.model.GalleryImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * A fragment representing a list of Items.
 */
public class GalleryFragment extends Fragment {



    private String TAG = GalleryFragment.class.getSimpleName();
    private static final String endpoint = "https://www.schoolpathramacademy.com/wp-json/wp/v2/media";
    private ArrayList<GalleryImage> images;
    private ProgressDialog pDialog;
    private GalleryAdapter mAdapter;
    private RecyclerView recyclerView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    private EndlessRecyclerViewScrollListener scrollListener;


    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);


    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_gallery_list, container, false);

        ((MainActivity) getActivity()).setActionBarTitle("Gallery");


        recyclerView = (RecyclerView) v.findViewById(R.id.rvList);

        pDialog = new ProgressDialog(getContext());
        images = new ArrayList<>();
        mAdapter = new GalleryAdapter(getContext(), images);

//        GridLayoutManager mLayoutManager;
//        mLayoutManager = new GridLayoutManager(getActivity(), spanCount);
//        mRecyclerView.setLayoutManager(mLayoutManager);

        GridLayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new GalleryAdapter.RecyclerTouchListener(getContext(), recyclerView, new GalleryAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("images", images);
                bundle.putInt("position", position);

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
                newFragment.setArguments(bundle);
                newFragment.show(ft, "slideshow");
//                Intent intent = new Intent(getApplicationContext(), ScreenSlidePagerActivity.class);
//                startActivity(intent);

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        // Retain an instance so that you can call `resetState()` for fresh searches
        scrollListener = new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadRecyclerViewData(page);
            }
        };
        recyclerView.addOnScrollListener(scrollListener);



        // SwipeRefreshLayout
        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.gallery_swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                pDialog.setMessage("Fetching Latest News..");
                images.clear();
                loadRecyclerViewData(0);
            }

        });
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        mSwipeRefreshLayout.post(new Runnable() {

            @Override
            public void run() {

                mSwipeRefreshLayout.setRefreshing(true);
                images.clear();

                // Fetching data from server
                loadRecyclerViewData(0);
            }
        });
        images.clear();

        fetchImages(0);

        return v;

    }
    private void loadRecyclerViewData(int page) {
        // Showing refresh animation before making http call
        fetchImages(page);
    }

    private void fetchImages(int page) {

        String finalEndpoint = "";
        if (page == 0) {
            finalEndpoint = endpoint;
        }
        else {
            page = page + 1;
            finalEndpoint = endpoint + "?page=" + page;
        }


        JsonArrayRequest req = new JsonArrayRequest(finalEndpoint,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
//                        pDialog.hide();

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject object = response.getJSONObject(i);
                                GalleryImage image = new GalleryImage();
                                JSONObject renderedTitleObj = object.getJSONObject("title");
                                String title = renderedTitleObj.getString("rendered");
                                image.setName(title);

//                                image.setTitle(object.getString("caption"));

                                JSONObject media_details = object.getJSONObject("media_details");
                                JSONObject media_by_size = media_details.getJSONObject("sizes");


                                image.setSmall(media_by_size.getString("thumbnail"));
                                image.setMedium(media_by_size.getString("medium"));
//                                image.setLarge(media_by_size.getString("large"));
                                image.setUrl(object.getString("source_url"));
//                                image.setPostId(object.getInt("post"));

                                String dateString = object.getString("date");
                                SimpleDateFormat format = new SimpleDateFormat(
                                        "yyyy-MM-dd'T'HH:mm:ss", Locale.US);
                                format.setTimeZone(TimeZone.getTimeZone("UTC"));

                                try {
                                    Date timestamp = format.parse(dateString);
                                    image.setTimeStamp(timestamp);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                images.add(image);

                            } catch (JSONException e) {
                                Log.e(TAG, "Json parsing error: " + e.getMessage());
                            }
                        }

                        mAdapter.notifyDataSetChanged();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
//                pDialog.hide();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);
    }

//    @Override
//    public void onBackPressed() {
//    if (pDialog != null) { pDialog.dismiss(); pDialog = null; }
//    Intent intent = new Intent(getContext(), MainActivity.class);
//    startActivity(intent);
////    super.onBackPressed();
//    }
}



//    // TODO: Customize parameter argument names
//    private static final String ARG_COLUMN_COUNT = "column-count";
//    // TODO: Customize parameters
//    private int mColumnCount = 1;
//
//
//    private ImageView imageView;
//    RecyclerView recyclerView;
//    GridLayoutManager gridLayoutManager;
//
//    private ArrayList<GalleryImage> mGalleryImagesData;
//    private GalleryImageAdapter galleryImageAdapter;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_gallery_list, container, false);
//
////        imageView = (ImageView) view.findViewById(R.id.galleryImageView);
//        recyclerView = (RecyclerView) view.findViewById(R.id.rvList);
//        gridLayoutManager = new GridLayoutManager(getContext(), 2);
//        recyclerView.setLayoutManager(gridLayoutManager);
//
////        ArrayList imageUrlList = prepareData();
////        GalleryImageAdapter dataAdapter = new GalleryImageAdapter(getContext(), imageUrlList);
////        recyclerView.setAdapter(dataAdapter);
//
//        // Set the adapter
//        if (view instanceof RecyclerView) {
//            Context context = view.getContext();
//            RecyclerView recyclerView = (RecyclerView) view;
//            if (mColumnCount <= 1) {
//                recyclerView.setLayoutManager(new LinearLayoutManager(context));
//            } else {
//                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
//            }
////            recyclerView.setAdapter(new NewsAdapter(DummyContent.ITEMS));
//
//            mGalleryImagesData = new ArrayList<>();
//
//            //Initialize the adapter and set it ot the RecyclerView
//            galleryImageAdapter = new GalleryImageAdapter(getContext(), mGalleryImagesData);
//            recyclerView.setAdapter(galleryImageAdapter);
//
//            //Get the data
//            initializeData();
//
//            //Helper class for creating swipe to dismiss and drag and drop functionality
//            ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback
//                    (ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.DOWN
//                            | ItemTouchHelper.UP, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
//
//                /**
//                 * Method that defines the drag and drop functionality
//                 *
//                 * @param recyclerView The RecyclerView that contains the list items
//                 * @param viewHolder   The SportsViewHolder that is being moved
//                 * @param target       The SportsViewHolder that you are switching the original one with.
//                 * @return returns true if the item was moved, false otherwise
//                 */
//                @Override
//                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
//                                      RecyclerView.ViewHolder target) {
//
//                    //Get the from and to position
//                    int from = viewHolder.getAdapterPosition();
//                    int to = target.getAdapterPosition();
//
//                    //Swap the items and notify the adapter
//                    Collections.swap(mGalleryImagesData, from, to);
//                    galleryImageAdapter.notifyItemMoved(from, to);
//                    return true;
//                }
//
//                /**
//                 * Method that defines the swipe to dismiss functionality
//                 *
//                 * @param viewHolder The viewholder being swiped
//                 * @param direction  The direction it is swiped in
//                 */
//                @Override
//                public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//
//                    //Remove the item from the dataset
//                    mGalleryImagesData.remove(viewHolder.getAdapterPosition());
//
//                    //Notify the adapter
//                    galleryImageAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
//                }
//            });
//
//            //Attach the helper to the RecyclerView
//            helper.attachToRecyclerView(recyclerView);
//
//        }
//        return view;
//    }
//
//    /**
//     * Method for initializing the sports data from resources.
//     */
//    private void initializeData() {
//        //Get the resources from the XML file
//        String[] imageUrls = getResources().getStringArray(R.array.image_urls);
//        //Clear the existing data (to avoid duplication)
//        mGalleryImagesData.clear();
////        User user = new User("test", "test_l", "test@gmail.com", "7736128108");
//
//
//        //Create the ArrayList of News objects with the titles, images
//        // and information about each sport
//        for(int i=0; i<imageUrls.length; i++){
//            mGalleryImagesData.add(new GalleryImage(imageUrls[i]));
//        }
//
//        //Recycle the typed array
////        newsImageResources.recycle();
//
//        //Notify the adapter of the change
//        galleryImageAdapter.notifyDataSetChanged();
//    }
//}