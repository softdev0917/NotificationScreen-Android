package com.example.ab.notificationscreen.viewholders;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.ab.notificationscreen.HeadingImageView;
import com.example.ab.notificationscreen.R;

public class LikedyourpostItemViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener {

  public ImageView thumbImage;
  public HeadingImageView profileImage;
  public TextView txtTitle;
  public TextView txtSubTitle;
  public TextView txtAgo;

  public LikedyourpostItemViewHolder(View itemView){
    super(itemView);

    thumbImage = (ImageView)itemView.findViewById(R.id.thumb_pic);
    profileImage = (HeadingImageView)itemView.findViewById(R.id.profile_pic);
    txtTitle = (TextView)itemView.findViewById(R.id.txt_title);
    txtSubTitle = (TextView)itemView.findViewById(R.id.txt_subtitle);
    txtAgo = (TextView)itemView.findViewById(R.id.txt_ago);

    ViewTreeObserver vto = profileImage.getViewTreeObserver();
    vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
      @Override
      public void onGlobalLayout() {
        profileImage.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        int width = profileImage.getMeasuredWidth();
        profileImage.mRadius = width / 2.0f;
      }
    });

    itemView.setOnTouchListener(this);
  }

  @Override
  public boolean onTouch(View view, MotionEvent event){
    if (event.getAction() == MotionEvent.ACTION_UP){
      Log.i("Touched cell", "Go to Liked your post");
    }
    return true;
  }
}
