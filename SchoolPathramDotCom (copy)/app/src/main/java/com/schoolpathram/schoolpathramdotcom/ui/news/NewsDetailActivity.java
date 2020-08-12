package com.schoolpathram.schoolpathramdotcom.ui.news;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.schoolpathram.schoolpathramdotcom.R;
import com.schoolpathram.schoolpathramdotcom.model.Category;
import com.schoolpathram.schoolpathramdotcom.model.Media;
import com.schoolpathram.schoolpathramdotcom.model.News;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NewsDetailActivity extends AppCompatActivity  {

    private TextView lblCount, lblTitle, lblDate, lblContent, lblCat1, lblCat2;
    private String videoId;

    private List<Media> mediaList;



    /**
     * Initializes the activity, filling in the data from the Intent.
     * @param savedInstanceState Contains information about the saved state of the activity
     */
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        News post = (News) getIntent().getSerializableExtra("post_data");

        String mediaUrl = "";


        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.app_bar)));

        //Initialize the views
//        lblCount = (TextView) findViewById(R.id.news_lbl_count);
        lblCat1 = (TextView) findViewById(R.id.news_lbl_cat1);
        lblCat2 = (TextView) findViewById(R.id.news_lbl_cat2);
        lblTitle = (TextView) findViewById(R.id.news_title);
        lblContent = (TextView) findViewById(R.id.postDescription);
        lblDate = (TextView) findViewById(R.id.news_date);

//        mediaCount = (TextView) findViewById(R.id.multipleImageBadgeDetail);


        ImageView imageViewPreview = (ImageView) findViewById(R.id.image_preview);
        ImageButton imageButton = (ImageButton) findViewById(R.id.imageButton);
//        YouTubePlayerView youTubePlayerView = findViewById(R.id.youtube_player_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        List<Category> categories = post.getCategories();
        lblCat1.setText(categories.get(0).getName());
        if (categories.size() > 1) {
            lblCat2.setText(categories.get(1).getName());
        }
        else {
            lblCat2.setVisibility(View.GONE);
        }


        lblContent.setText(post.getDescription());
//
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date date = new Date(String.valueOf(post.getDateAdded()));
        String dateTime = dateFormat.format(date);
        System.out.println("Current Date Time : " + dateTime);

        lblDate.setText(dateTime);
        lblTitle.setText(post.getTitle());
        mediaList = post.getMediaList();


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOpenYoutubeIntent(getApplicationContext(), post.getVideoUrl());
            }
        });


        if (!post.getImageUrl().isEmpty()) {
            mediaUrl = post.getImageUrl();
        }
        else if(mediaList.size() > 0) {
            mediaUrl = mediaList.get(0).getMediaUrl();
        }
        else {
            mediaUrl = "";
        }


        if (post.getVideoUrl() != "") {
            imageViewPreview.setVisibility(View.GONE);
        }
        else {
            imageButton.setVisibility(View.GONE);
            if (!mediaUrl.isEmpty()) {
                Glide.with(this).load(mediaUrl)
                        .thumbnail(0.5f)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .error(R.drawable.ic_image_not_found)
                        .into(imageViewPreview);
            }
            else{
                imageViewPreview.setVisibility(View.GONE);
            }
            
        }

//        if (post.getMediaList().size() >= 2){
//            mediaCount.setText("1+ more");
//        }else{
//            mediaCount.setVisibility(View.GONE);
//        }



        imageViewPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("images", (Serializable) mediaList);
                bundle.putInt("position", 0);

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                NewsImageSlidePagerActivity newFragment = NewsImageSlidePagerActivity.newInstance();
                newFragment.setArguments(bundle);
                newFragment.show(ft, "slideshow");
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return true;
    }

    public void getOpenYoutubeIntent(Context context, String url) {
//        url = "https://www.youtube.com/embed/1SaKMfUwuc4?feature=oembed";

        try {
            context.getPackageManager().getPackageInfo("com.google.android.youtube", 0);
            startActivity( new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        } catch (Exception e) {
             openWebPage(url, context);
        }
    }

//
    public void openWebPage(String url, Context context) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}