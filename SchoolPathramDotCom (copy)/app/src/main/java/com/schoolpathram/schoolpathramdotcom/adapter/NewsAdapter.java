package com.schoolpathram.schoolpathramdotcom.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.schoolpathram.schoolpathramdotcom.R;
import com.schoolpathram.schoolpathramdotcom.model.Category;
import com.schoolpathram.schoolpathramdotcom.model.News;

import java.util.List;

import static android.content.ContentValues.TAG;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private List<News>list_data;
    private Context context;



    public NewsAdapter(List<News> list_data, Context context) {
        this.list_data = list_data;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        News post=list_data.get(position);
        String url = post.getImageUrl();

        if ((!url.isEmpty()) || post.getMediaList().size() >= 1) {

            String imagePreviewUrl = "";
            if (!url.isEmpty()){
                imagePreviewUrl = url;
            }else {
                imagePreviewUrl = post.getMediaList().get(0).getMediaUrl();
            }

            Glide.with(context).load(imagePreviewUrl)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.drawable.ic_image_not_found)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            Log.e(TAG, "Load failed", e);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            Log.e(TAG, "Load ready");

                            return false;
                        }
                    })

                    .into(holder.img);
        }

        else {
            holder.img.setVisibility(View.GONE);
        }


//        Picasso.with(context)
//                .load(listData
//                        .getImageUrl())
//                .into(holder.img);


        holder.txtname.setText(post.getTitle());

        List<Category> categories = post.getCategories();
        if (categories.size() != 0) {
            holder.txtCat1.setText(categories.get(0).getName());
        }
        if (categories.size() > 1) {
            holder.txtCat2.setText(categories.get(1).getName());
        }
        else {
            holder.txtCat2.setVisibility(View.GONE);
        }

//        if (post.getMediaList().size() >= 2){
//            holder.mediaCount.setText("1+ more");
//        }else{
//            holder.mediaCount.setVisibility(View.GONE);
//        }

//        try {
//            holder.txtCat2.setText(categories.get(1).getName());
//        }
//        catch(Exception exc) {
//
//        }

//        for (Category category: categories) {
//            holder.txtCat1.setText(category.getName());
//        }

    }

    @Override
    public int getItemCount() {
        return list_data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView img;
        private TextView txtname;
        private TextView txtCat1;
        private TextView txtCat2;
//        private TextView mediaCount;



        public ViewHolder(View itemView) {
            super(itemView);
            img=(ImageView)itemView.findViewById(R.id.postMedia);
            txtname=(TextView)itemView.findViewById(R.id.postTitle);
            txtCat1=(TextView)itemView.findViewById(R.id.postCategory1);
            txtCat2=(TextView)itemView.findViewById(R.id.postCategory2);
//            mediaCount = (TextView)itemView.findViewById(R.id.multipleImageBadge);


        }
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private NewsAdapter.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final NewsAdapter.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

}