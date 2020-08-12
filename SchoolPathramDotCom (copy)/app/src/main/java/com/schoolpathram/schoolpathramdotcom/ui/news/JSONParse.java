package com.schoolpathram.schoolpathramdotcom.ui.news;

import com.schoolpathram.schoolpathramdotcom.model.News;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class JSONParse {
    //Declare the arrays of fields you require
    public static String[] titles;
    public static String[] urls;
    private JSONArray postsArray = null;
    List<News> posts ;
    private String json;

    public JSONParse(String json){
        this.json = json;
    }

    protected void parseJSON() {

        JSONObject jsonObject = null;

        try {
            postsArray = new JSONArray(json);
            titles = new String[postsArray.length()];
            urls = new String[postsArray.length()];
            posts = new ArrayList<News>();

            for(int i=0;i< postsArray.length();i++) {
                jsonObject = postsArray.getJSONObject(i);
                posts.add(mapJsonObjectToModel(jsonObject));
            }
            } catch (JSONException e) {
            e.printStackTrace();
        }
    }
        public List<News> getPosts() {
        //function to return the final populated list
        return posts;
    }


    private News mapJsonObjectToModel(JSONObject obj) {
        String mediaBaseUrl = "https://example.com/wp-json/wp/v2/media/";
        int id = 0;
        String title = null;
        Date dateAdded = null;
        Date dateUpdated = null;
        String imageUrl = null;
        String content = null;

        try {
            id = obj.getInt("id");

            String addedDateString = obj.getString("date");
            String modifiedDateString = obj.getString("modified");

//        DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);

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

            try {
                String mediaId = renderedContentObj.getString("featured_media");
                imageUrl = mediaBaseUrl + mediaId;
            } catch (JSONException e) {
                String bckup_media = "http://www.schoolpathramacademy.com/wp-content/uploads/2020/06/WhatsApp-Image-2020-06-22-at-18.04.39-300x258.jpeg";
                imageUrl = bckup_media;
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        News post = new News();
        post.setId(id);
        post.setTitle(title);
        post.setDateUpdated(dateUpdated);
        post.setDescription(content);
        post.setDateAdded(dateAdded);
        post.setImageUrl(imageUrl);

        return post;
    }

}
