package com.schoolpathram.schoolpathramdotcom.ui.news;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.schoolpathram.schoolpathramdotcom.MainActivity;
import com.schoolpathram.schoolpathramdotcom.R;
import com.schoolpathram.schoolpathramdotcom.adapter.NewsAdapter;
import com.schoolpathram.schoolpathramdotcom.db.AppDatabase;
import com.schoolpathram.schoolpathramdotcom.helper.CustomJSONObjectRequest;
import com.schoolpathram.schoolpathramdotcom.helper.EndlessRecyclerViewScrollListener;
import com.schoolpathram.schoolpathramdotcom.helper.VolleyCallback;
import com.schoolpathram.schoolpathramdotcom.helper.VolleyController;
import com.schoolpathram.schoolpathramdotcom.model.Category;
import com.schoolpathram.schoolpathramdotcom.model.GalleryImage;
import com.schoolpathram.schoolpathramdotcom.model.Media;
import com.schoolpathram.schoolpathramdotcom.model.News;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

import io.reactivex.disposables.CompositeDisposable;

import static android.content.ContentValues.TAG;

/**
 * A fragment representing a list of Items.
 */
public class NewsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private int mColumnCount = 1;
    private ProgressDialog pDialog;


    private ArrayList<News> mNewsData;
    private ArrayList<Category> categoryArrayList;
    private ArrayList<Media> mediaArrayList;

    private final CompositeDisposable mDisposable = new CompositeDisposable();

    private NewsAdapter mAdapter;
    private ArrayList<News> posts;
    private List<News> postList;
    private RecyclerView.LayoutManager mLayoutManager;
    SwipeRefreshLayout mSwipeRefreshLayout;
    private EndlessRecyclerViewScrollListener scrollListener;
    LinearLayoutManager linearLayoutManager;

    private ArrayList<GalleryImage> images;

//    static int currentPage = 1;

    private static final String HI = "https://uniqueandrocode.000webhostapp.com/hiren/androidweb.php";
    private RecyclerView rv;
    private static AppDatabase mDb;

    Map <String,Category> categoriesMap =  new HashMap<String,Category>();
    Map <String,Media> mediaMap =  new HashMap<String,Media>();

    Media mediaTemp;

    static Media mediaObj;
    static Category categoryObj;

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

        ((MainActivity) getActivity()).setActionBarTitle("News Updates");
//        v.setBackgroundColor(getResources().getColor(R.color.design_default_color_background));

        rv=(RecyclerView) v.findViewById(R.id.Testlist);
//        currentPage = 1;

        postList=new ArrayList<>();
        mNewsData = new ArrayList<News>();
        categoryArrayList = new ArrayList<Category>();
        mediaArrayList = new ArrayList<Media>();



        try {
            JSONArray catObj = new JSONArray(loadJSONFromAsset(getContext(), "categories.json"));
            JSONArray mediaObj = new JSONArray(loadJSONFromAsset(getContext(), "media.json"));
            mediaArrayList = new ArrayList<Media>();
            categoryArrayList = new ArrayList<Category>();

            processCategoryData(catObj);
            processMediaData(mediaObj);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.v("Cate size", String.valueOf(categoryArrayList.size()));
        Log.v("media Size", String.valueOf(mediaArrayList.size()));

        mAdapter=new NewsAdapter(postList, getContext());
        pDialog = new ProgressDialog(getContext());
        rv.setAdapter(mAdapter);


        rv.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getContext());
        rv.setLayoutManager(linearLayoutManager);

        mDb = AppDatabase.getInstance(getContext());

        rv.addOnItemTouchListener(new NewsAdapter.RecyclerTouchListener(getContext(), rv, new NewsAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                News news= postList.get(position);

                Intent newsDetailIntent = new Intent(getContext(), NewsDetailActivity.class);
                newsDetailIntent.putExtra("post_data", news); //you can name the keys whatever you like
                startActivity(newsDetailIntent);

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        // Retain an instance so that you can call `resetState()` for fresh searches
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
//                currentPage = page+1;
                getDataFromServer(page+1);

            }
        };



        rv.addOnScrollListener(scrollListener);


//        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                if (dy > 0) {
//                    // Scrolling up
//                    currentPage = currentPage-1;
//                } else {
//                    // Scrolling down
//                    currentPage = currentPage+1;
//                    getDataFromServer(currentPage);
//                }
//            }
//        });









        // SwipeRefreshLayout
        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                pDialog.setMessage("Fetching Latest News..");
                mSwipeRefreshLayout.setRefreshing(true);
                postList.clear();
//currentPage = 1;
                getDataFromServer(1);
            }

        });
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

//        /**
//         * Showing Swipe Refresh animation on activity create
//         * As animation won't start on onCreate, post runnable is used
//         */
//        mSwipeRefreshLayout.post(new Runnable() {
//
//            @Override
//            public void run() {
//
//                mSwipeRefreshLayout.setRefreshing(true);
//                postList.clear();
////                getDataFromServer(1);
//
//                // Fetching data from server
////                loadRecyclerViewData(currentPage);
//            }
//        });

        mSwipeRefreshLayout.setRefreshing(true);
        postList.clear();

        getDataFromServer(1);

        return v;

    }


    public void getPosts( final String endpoint, int page, final VolleyCallback callback) {

        String finalEndpoint = "";
        if (page == 0) {
            finalEndpoint = endpoint;
        }
        else {
            page = page + 1;
            finalEndpoint = endpoint + "?page=" + page;
        }

        CustomJSONObjectRequest rq = new CustomJSONObjectRequest(Request.Method.GET,
                finalEndpoint, null,
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
        VolleyController.getInstance(getContext()).addToRequestQueue(rq);

    }


//    private void loadRecyclerViewData(int page) {
//        // Showing refresh animation before making http call
//
//        getPosts(endpoint, page, new VolleyCallback() {
//            @Override
//            public void onSuccess(JSONArray result) throws JSONException {
//
//                for (int i = 0; i < result.length(); i++) {
//                    try {
//                        JSONObject object = result.getJSONObject(Integer.parseInt(String.valueOf(i)));
//                        News post = mapJsonObjectToModel(object);
//                        postList.add(post);
//                        mNewsData.add(post);
//                    } catch (JSONException e) {
//                        Log.e(TAG, "Json parsing error: " + e.getMessage());
//                    }
//                }
//
//                mAdapter.notifyDataSetChanged();
//                mSwipeRefreshLayout.setRefreshing(false);
//            }
//
//            @Override
//            public void onError(String result) throws Exception {
//                Toast.makeText(getContext(), "Oops!!",
//                        Toast.LENGTH_LONG).show();
//            }
//        });
//    }


private News mapJsonObjectToModel(JSONObject obj) {
        String mediaBaseUrl = "https://www.schoolpathramacademy.com/wp-json/wp/v2/media/";
        int id = 0;
        String title = null;
        Date dateAdded = null;
        Date dateUpdated = null;
        String imageUrl = null;
        String content = null;
        String catId = "";
        String videoUrl = null;
    News post = new News();


    try {
        id = obj.getInt("id");

        String addedDateString = obj.getString("date");
        String modifiedDateString = obj.getString("modified");

        SimpleDateFormat format = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));

        try {
            dateAdded = format.parse(addedDateString);
            dateUpdated = format.parse(modifiedDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        JSONObject renderedTitleObj = obj.getJSONObject("title");
        title = renderedTitleObj.getString("rendered");

        JSONObject renderedContentObj = obj.getJSONObject("content");
        content = renderedContentObj.getString("rendered");


        String linkString = obj.getString("link");

        boolean isVideo = linkString.indexOf("https-youtu-be") != -1 ? true : false;
        if (isVideo) {
            String videoLink   = parseContentForVideoUrl(content);
            videoUrl = "https://www.youtube.com/watch?v=1SaKMfUwuc4";
        }
        else
        {
            videoUrl = "";

        }

        String mediaId = obj.getString("featured_media");

            String finalMediaUrl = mediaBaseUrl + mediaId;
            retrieveImageUrl(finalMediaUrl, new VolleyCallback() {
                @Override
                public void onSuccess(JSONArray result) throws JSONException {
                    Log.v("Response", result.toString());

                    for (int i = 0; i < result.length(); i++) {
                        try {
                            String s = result.getString(Integer.parseInt(String.valueOf(i)));
                            JSONObject obj = new JSONObject(s);
                            post.setImageUrl(obj.getString("source_url"));
                        } catch (JSONException e) {
                            Log.e(TAG, "Json parsing error: " + e.getMessage());
                        }
                    }

                }

                @Override
                public void onError(String result) throws Exception {
                    Toast.makeText(getContext(), "Oops!!",
                            Toast.LENGTH_LONG).show();
//
                }
            });





        JSONArray catJsonArray = obj.getJSONArray("categories");
        catId = catJsonArray.get(0).toString();


    } catch (JSONException e) {
        e.printStackTrace();
    }

        post.setId(id);
        post.setTitle(title);
        post.setDateUpdated(dateUpdated);

        String beautifulContent = android.text.Html.fromHtml(content).toString();

        post.setDescription(beautifulContent);
        post.setDateAdded(dateAdded);
//        post.setImageUrl(imageUrl);
        post.setVideoUrl(videoUrl);


    String catLink = "https://www.schoolpathramacademy.com/wp-json/wp/v2/categories/" + catId;

    retreiveCategory(catLink, new VolleyCallback() {
        @Override
        public void onSuccess(JSONArray result) throws JSONException {
            Log.v("Response", result.toString());

            for (int i = 0; i < result.length(); i++) {
                try {
                    String s = result.getString(Integer.parseInt(String.valueOf(i)));
                    JSONObject obj = new JSONObject(s);
                    post.setCategory(obj.getString("name"));
                } catch (JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                }
            }
        }

        @Override
        public void onError(String result) throws Exception {
            Toast.makeText(getContext(), "Oops!!",
                    Toast.LENGTH_LONG).show();
//
        }
    });

    Log.d(TAG, post.toString());

        return post;
}




    public String parseContentForVideoUrl(String content) {

        String []splitterString=content.split("\"");

        String finalUrl= "";

                for (String s : splitterString) {
            if ((s.startsWith("https://www.youtube.com"))) {
                                finalUrl = s;
            }
            }


        return finalUrl;
    }

    public String removeLastChar(String str) {
        if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == 'x') {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }


    public void retreiveCategory(final String url, final VolleyCallback callback) {
    CustomJSONObjectRequest rq = new CustomJSONObjectRequest(Request.Method.GET,
            url, null,
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
    rq.setPriority(Request.Priority.HIGH);
    VolleyController.getInstance(getContext()).addToRequestQueue(rq);

}
    public void retrieveImageUrl(final String url, final VolleyCallback callback) {
        CustomJSONObjectRequest rq = new CustomJSONObjectRequest(Request.Method.GET,
                url, null,
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
        rq.setPriority(Request.Priority.HIGH);
        VolleyController.getInstance(getContext()).addToRequestQueue(rq);

    }


    public void retrieveMedia(final String url, final VolleyCallback callback) {
        CustomJSONObjectRequest rq = new CustomJSONObjectRequest(Request.Method.GET,
                url, null,
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
        rq.setPriority(Request.Priority.HIGH);
        VolleyController.getInstance(getContext()).addToRequestQueue(rq);

    }

    public void getDataFromServer(int page) {
        String categoriesUrl;
        String mediaUrl;
        String endpoint;

//        categoriesUrl  = "https://www.schoolpathramacademy.com/wp-json/wp/v2/categories/";
//        mediaUrl = "https://www.schoolpathramacademy.com/wp-json/wp/v2/media/";
        endpoint = "https://www.schoolpathramacademy.com/wp-json/wp/v2/posts?per_page=10&page=" +page;


        getAllPosts(endpoint, new VolleyCallback() {
            @Override
            public void onSuccess(JSONArray result) throws JSONException {

                for (int i = 0; i < result.length(); i++) {
                    try {
                        JSONObject object = result.getJSONObject(Integer.parseInt(String.valueOf(i)));
                        News news = new News();

                        int postId = object.getInt("id");


                        Date dateAdded = null, dateUpdated = null;
                        String title, content = "";

                        String addedDateString = object.getString("date");
                        String modifiedDateString = object.getString("modified");

                        SimpleDateFormat format = new SimpleDateFormat(
                                "yyyy-MM-dd'T'HH:mm:ss", Locale.US);
                        format.setTimeZone(TimeZone.getTimeZone("UTC"));

                        try {
                            dateAdded  = format.parse(addedDateString);
                            dateUpdated = format.parse(modifiedDateString);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        JSONObject renderedTitleObj = object.getJSONObject("title");
                        title = renderedTitleObj.getString("rendered");

                        JSONObject renderedContentObj = object.getJSONObject("content");
                        content = renderedContentObj.getString("rendered");

                        news.setId(postId);
                        news.setTitle(title);
                        news.setDateUpdated(dateUpdated);

                        String beautifulContent = android.text.Html.fromHtml(content).toString();

                        Log.v("beautifulContent 1", beautifulContent.toString());

                        if (beautifulContent.isEmpty()) {
                            JSONObject renderedExcerptObj = object.getJSONObject("excerpt");
                            String renderedExcerpt = renderedExcerptObj.getString("rendered");
                            beautifulContent = android.text.Html.fromHtml(renderedExcerpt).toString();
                            Log.v("beautifulContent 2", beautifulContent.toString());
                        }

                        news.setDescription(beautifulContent);
                        news.setDateAdded(dateAdded);
                        String mediaId = object.getString("featured_media");


                        List<Media> mediaList  = parseContentForMediaUrls(content, mediaId);

                        news.setMediaList(mediaList);


                        Media mediaMapObj = mediaMap.get(mediaId);

                        if (mediaMapObj==null && !(mediaId.isEmpty())) {
                            String mediaUrl = "https://schoolpathramacademy.com/wp-json/wp/v2/media/" + mediaId;

                            retrieveMedia(mediaUrl, new VolleyCallback() {
                                @Override
                                public void onSuccess(JSONArray result) throws JSONException {
                                    for (int i = 0; i < result.length(); i++) {
                                        try {

                                            JSONObject object = new JSONObject(result.getString(i));


//                                                            JSONObject object = (JSONObject) result.getJSONObject(Integer.parseInt(String.valueOf(i)));
                                            int mediaId = Integer.parseInt(object.getString("id"));
                                            String mediaUrl = object.getString("source_url");
                                            String mediaType = object.getString("media_type");
//                                                            UUID uuid = UUID.randomUUID();
                                            Media media = new Media(mediaId, mediaUrl, mediaType);
                                            mediaMap.put(String.valueOf(media.getId()), media);

                                        } catch (JSONException e) {
                                            Log.e(TAG, "Json parsing error: " + e.getMessage());
                                        }
                                    }
                                }

                                @Override
                                public void onError(String result) throws Exception {

                                }
                            });
                        }

                        Media media = mediaMap.get(mediaId);

                        if (media != null) {
                            if (media.getType().equals("video")) {
                                news.setVideoUrl(media.getMediaUrl());
                                news.setImageUrl("");
                            }
                            else if(media.getType().equals("image")) {
                                news.setVideoUrl("");
                                news.setImageUrl(media.getMediaUrl());
                            }
                        }
                        else {
                            news.setVideoUrl("");
                            news.setImageUrl("");
                        }



                        JSONArray jsonArray  = object.getJSONArray("categories");
                        ArrayList<String> categoryIds = new ArrayList<String>();
                        JSONArray jArray = (JSONArray)jsonArray;
                        if (jArray != null) {
                            for (int k=0; k<jArray.length(); k++){
                                categoryIds.add(jArray.getString(k));
                            }
                        }
                        List<Category> categoriesTemp = new ArrayList<Category>();
                        for (String catId: categoryIds) {
                            Category category = categoriesMap.get(catId);
                            categoriesTemp.add(category);
                        }
                        news.setCategories(categoriesTemp);



                        if (!postList.contains(news)) {
                            postList.add(news);
                        }
                        mNewsData.add(news);

                    } catch (JSONException e) {
                        Log.e(TAG, "Json parsing error: " + e.getMessage());
                    }
                }
                mAdapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(String result) throws Exception {
                Toast.makeText(getContext(), "Oops!!",
                        Toast.LENGTH_LONG).show();
            }
        });





//        getCategories(categoriesUrl, new VolleyCallback() {
//            @Override
//            public void onSuccess(JSONArray result) throws JSONException {
////
//                for (int i = 0; i < result.length(); i++) {
//                    try {
//                        JSONObject object = result.getJSONObject(Integer.parseInt(String.valueOf(i)));
//
//                        int catId = Integer.parseInt(object.getString("id"));
//                        String catName = object.getString("name");
//
//                        Category category = new Category(catId, catName);
////                        categories.add(category);
//                        categoriesMap.put(String.valueOf(category.getId()), category);
//
////                        new CategoriesInsertAsyncTask(categories).execute();
//
//                    } catch (JSONException e) {
//                        Log.e(TAG, "Json parsing error: " + e.getMessage());
//                    }
//                }
//
//
//                //media url
//
//                getMediaUrls(mediaUrl, new VolleyCallback() {
//                    @Override
//                    public void onSuccess(JSONArray result) throws JSONException {
//
//
//                for (int i = 0; i < result.length(); i++) {
//                    try {
//                        JSONObject object = result.getJSONObject(Integer.parseInt(String.valueOf(i)));
//                        int mediaId = Integer.parseInt(object.getString("id"));
//                        String mediaUrl = object.getString("source_url");
//                        String mediaType = object.getString("media_type");
////                        UUID uuid = UUID.randomUUID();
//
//                        Media media = new Media(mediaId, mediaUrl, mediaType);
//
//
//                        mediaMap.put(String.valueOf(media.getServerId()), media);
////                        mediaArrayList.add(media);
//
////                        new MediaInsertAsyncTask(mediaArrayList).execute();
//                    } catch (JSONException e) {
//                        Log.e(TAG, "Json parsing error: " + e.getMessage());
//                    }
//                }
//                        //final posts
//                        getAllPosts(endpoint, new VolleyCallback() {
//                            @Override
//                            public void onSuccess(JSONArray result) throws JSONException {
//
//                                for (int i = 0; i < result.length(); i++) {
//                                    try {
//                                        JSONObject object = result.getJSONObject(Integer.parseInt(String.valueOf(i)));
//                                        News news = new News();
//
//                                        int postId = object.getInt("id");
//
//
//                                        Date dateAdded = null, dateUpdated = null;
//                                        String title, content = "";
//
//                                        String addedDateString = object.getString("date");
//                                        String modifiedDateString = object.getString("modified");
//
//                                        SimpleDateFormat format = new SimpleDateFormat(
//                                                "yyyy-MM-dd'T'HH:mm:ss", Locale.US);
//                                        format.setTimeZone(TimeZone.getTimeZone("UTC"));
//
//                                        try {
//                                            dateAdded  = format.parse(addedDateString);
//                                            dateUpdated = format.parse(modifiedDateString);
//                                        } catch (ParseException e) {
//                                            e.printStackTrace();
//                                        }
//
//                                        JSONObject renderedTitleObj = object.getJSONObject("title");
//                                        title = renderedTitleObj.getString("rendered");
//
//                                        JSONObject renderedContentObj = object.getJSONObject("content");
//                                        content = renderedContentObj.getString("rendered");
//
//                                        news.setId(postId);
//                                        news.setTitle(title);
//                                        news.setDateUpdated(dateUpdated);
//
//                                        String beautifulContent = android.text.Html.fromHtml(content).toString();
//
//                                        Log.v("beautifulContent 1", beautifulContent.toString());
//
//                                        if (beautifulContent.isEmpty()) {
//                                            JSONObject renderedExcerptObj = object.getJSONObject("excerpt");
//                                            String renderedExcerpt = renderedExcerptObj.getString("rendered");
//                                            beautifulContent = android.text.Html.fromHtml(renderedExcerpt).toString();
//                                            Log.v("beautifulContent 2", beautifulContent.toString());
//                                        }
//
//                                        news.setDescription(beautifulContent);
//                                        news.setDateAdded(dateAdded);
//                                        String mediaId = object.getString("featured_media");
//
//
//                                        List<Media> mediaList  = parseContentForMediaUrls(content, mediaId);
//
//                                        news.setMediaList(mediaList);
//
//
//                                        Media mediaMapObj = mediaMap.get(mediaId);
//
//                                        if (mediaMapObj==null && !(mediaId.isEmpty())) {
//                                          String mediaUrl = "https://schoolpathramacademy.com/wp-json/wp/v2/media/" + mediaId;
//
//                                            retrieveMedia(mediaUrl, new VolleyCallback() {
//                                                @Override
//                                                public void onSuccess(JSONArray result) throws JSONException {
//                                                    for (int i = 0; i < result.length(); i++) {
//                                                        try {
//
//                                                            JSONObject object = new JSONObject(result.getString(i));
//
//
////                                                            JSONObject object = (JSONObject) result.getJSONObject(Integer.parseInt(String.valueOf(i)));
//                                                            int mediaId = Integer.parseInt(object.getString("id"));
//                                                            String mediaUrl = object.getString("source_url");
//                                                            String mediaType = object.getString("media_type");
////                                                            UUID uuid = UUID.randomUUID();
//                                                            Media media = new Media(mediaId, mediaUrl, mediaType);
//                                                            mediaMap.put(String.valueOf(media.getId()), media);
//
//                                                        } catch (JSONException e) {
//                                                            Log.e(TAG, "Json parsing error: " + e.getMessage());
//                                                        }
//                                                    }
//                                                }
//
//                                                @Override
//                                                public void onError(String result) throws Exception {
//
//                                                }
//                                            });
//                                        }
//
//                                        Media media = mediaMap.get(mediaId);
//
//                                        if (media != null) {
//                                            if (media.getType().equals("video")) {
//                                                news.setVideoUrl(media.getMediaUrl());
//                                                news.setImageUrl("");
//                                            }
//                                            else if(media.getType().equals("image")) {
//                                                news.setVideoUrl("");
//                                                news.setImageUrl(media.getMediaUrl());
//                                            }
//                                                }
//                                                else {
//                                                    news.setVideoUrl("");
//                                                    news.setImageUrl("");
//                                                }
//
//
//
//
////                                        int mediaId = object.getInt("featured_media");
////                                        AppExecutors.getInstance().diskIO().execute(new Runnable() {
////                                            @Override
////                                            public void run() {
////                                                Media media =  mDb.mediaDao().getMedia(mediaId);
////                                                if (media != null) {
////                                                    if (media.getType().equals("video")) {
////                                                        news.setVideoUrl(media.getMediaUrl());
////                                                        news.setImageUrl("");
////                                                    }
////                                                    else if(media.getType().equals("image")) {
////                                                        news.setVideoUrl("");
////                                                        news.setImageUrl(media.getMediaUrl());
////                                                    }
////                                                }
////                                                else {
////                                                    news.setVideoUrl("");
////                                                    news.setImageUrl("");
////                                                }
////
////                                            }
////                                        });
//
//                                        JSONArray jsonArray  = object.getJSONArray("categories");
//                                        ArrayList<String> categoryIds = new ArrayList<String>();
//                                        JSONArray jArray = (JSONArray)jsonArray;
//                                        if (jArray != null) {
//                                            for (int k=0; k<jArray.length(); k++){
//                                                categoryIds.add(jArray.getString(k));
//                                            }
//                                        }
//                                        List<Category> categoriesTemp = new ArrayList<Category>();
//                                        for (String catId: categoryIds) {
//                                            Category category = categoriesMap.get(catId);
//                                            categoriesTemp.add(category);
//                                        }
//                                        news.setCategories(categoriesTemp);
//
////                                        AppExecutors.getInstance().diskIO().execute(new Runnable() {
////                                            @Override
////                                            public void run() {
////                                                List<Category> categories = new ArrayList<Category>();
////                                                for (String catId: categoryIds) {
////                                                    Category category =  mDb.categoryDao().getCategory(catId);
////                                                    categories.add(category);
////                                                }
////                                                news.setCategories(categories);
////                                            }
////                                        });
//
//                                        if (!postList.contains(news)) {
//                                            postList.add(news);
//                                        }
//                                        mNewsData.add(news);
//
//                                    } catch (JSONException e) {
//                                        Log.e(TAG, "Json parsing error: " + e.getMessage());
//                                    }
//                                }
//                                mAdapter.notifyDataSetChanged();
//                                mSwipeRefreshLayout.setRefreshing(false);
//                            }
//
//                            @Override
//                            public void onError(String result) throws Exception {
//                                Toast.makeText(getContext(), "Oops!!",
//                                        Toast.LENGTH_LONG).show();
//                            }
//                        });
//
//                    }
//
//                    @Override
//                    public void onError(String result) throws Exception {
//                        Toast.makeText(getContext(), "Oops!!",
//                                Toast.LENGTH_LONG).show();
//                    }
//                });
//
//
//
//            }
//
//            @Override
//            public void onError(String result) throws Exception {
//                Toast.makeText(getContext(), "Oops!!",
//                        Toast.LENGTH_LONG).show();
//            }
//        });

    }

    private ArrayList<Media> parseContentForMediaUrls(String content, String mediaId) {
        ArrayList<Media> mediaArrayList = new ArrayList();
        String []splitterString = content.split("\"");
        ArrayList<String> addedUrls = new ArrayList();
        String url = "";
        int counter = 1;

        for (String s : splitterString) {
            if (s.startsWith("http") && s.endsWith(".jpg")) {
                url = s;

            }
            else if(s.startsWith("http") && s.endsWith(".png")) {
                url = s;
            }
            else if(s.startsWith("http") && s.endsWith(".JPEG")) {
                url = s;
            }

            else if(s.startsWith("http") && s.endsWith(".jpeg")) {
                url = s;
            }


            if (!url.isEmpty()){
                if (!(addedUrls).contains(url)) {
                    UUID uuid = UUID.randomUUID();
                    Media media = new Media(uuid.toString(), url, "image");
                    mediaArrayList.add(media);
                    addedUrls.add(url);
                }
                counter = counter+1;

            }
        }


        return mediaArrayList;
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
        VolleyController.getInstance(getContext()).addToRequestQueue(rq);

    }
    public void getMediaUrls( final String endpoint, final VolleyCallback callback) {
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
        VolleyController.getInstance(getContext()).addToRequestQueue(rq);

    }
    public void getAllPosts(final String endpoint, final VolleyCallback callback) {

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
        VolleyController.getInstance(getContext()).addToRequestQueue(rq);

    }




    private static class CategoriesInsertAsyncTask extends android.os.AsyncTask<Void, Void, Void> {
       Category category;

        public CategoriesInsertAsyncTask(Category category) {
            this.category=category;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mDb.categoryDao().insertCategory(category);
            return null;
        }

    }
    private static class MediaInsertAsyncTask extends android.os.AsyncTask<Void, Void, Void> {
        Media media;

        public MediaInsertAsyncTask(Media media) {
            this.media = media;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mDb.mediaDao().insertMedia(media);
            return null;
        }
    }

    private static class MediaRetriveAsyncTask extends android.os.AsyncTask<Void, Void, Media> {
        int mediaId;

        public MediaRetriveAsyncTask(int mediaId) {
            this.mediaId = mediaId;
        }

        @Override
        protected Media doInBackground(Void... voids) {
            Media media = mDb.mediaDao().getMedia(mediaId);
            return media;
        }

        @Override
        protected void onPostExecute(Media media) {
            processMediaValue(media);
        }
    }

    static void processMediaValue(Media media)
    {
        mediaObj = media;
    }

    private static class CategoryRetriveAsyncTask extends android.os.AsyncTask<Void, Void, Category> {
        String catId;

        public CategoryRetriveAsyncTask(String catId) {
            this.catId = catId;
        }

        @Override
        protected Category doInBackground(Void... voids) {
            Category category = mDb.categoryDao().getCategory(catId);
            return category;
        }

        @Override
        protected void onPostExecute(Category category) {
            processCategoryValue(category);
        }
    }

    static void processCategoryValue(Category category)
    {
        categoryObj = category;
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


                Media mediaMapObj = mediaMap.get(mediaId);
                if (mediaMapObj != null) {
                    mediaMap.put(String.valueOf(media.getServerId()), media);
                    mediaArrayList.add(media);
                }


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
                categoriesMap.put(String.valueOf(category.getId()), category);

//                        new CategoriesInsertAsyncTask(categories).execute();

            } catch (JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
            }
        }
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