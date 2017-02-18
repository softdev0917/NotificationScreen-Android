/*
 * Copyright (c) 2016 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.example.ab.notificationscreen;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ab.notificationscreen.notificationmodels.BaseNotificationModel;
import com.example.ab.notificationscreen.notificationmodels.CommentsModel;
import com.example.ab.notificationscreen.notificationmodels.FriendRequestOrFollowModel;
import com.example.ab.notificationscreen.notificationmodels.LikedPostModel;
import com.example.ab.notificationscreen.notificationmodels.LikesModel;
import com.example.ab.notificationscreen.notificationmodels.MentionsOrTaggingModel;
import com.example.ab.notificationscreen.notificationmodels.ModelType;
import com.example.ab.notificationscreen.notificationmodels.ScreenshotsModel;
import com.example.ab.notificationscreen.notificationmodels.SectionHeaderModel;
import com.example.ab.notificationscreen.notificationmodels.TextPostModel;
import com.example.ab.notificationscreen.notificationmodels.TookScreenshotModel;
import com.example.ab.notificationscreen.viewholders.CommentsItemViewHolder;
import com.example.ab.notificationscreen.viewholders.FriendRequestOrFollowItemViewHolder;
import com.example.ab.notificationscreen.viewholders.LikedyourpostItemViewHolder;
import com.example.ab.notificationscreen.viewholders.LikesItemViewHolder;
import com.example.ab.notificationscreen.viewholders.MentionsOrTaggingItemViewHolder;
import com.example.ab.notificationscreen.viewholders.ScreenshotsItemViewHolder;
import com.example.ab.notificationscreen.viewholders.SectionHeaderViewHolder;
import com.example.ab.notificationscreen.viewholders.TextPostItemViewHolder;
import com.example.ab.notificationscreen.viewholders.TookScreenshotItemViewHolder;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class NotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private ArrayList<BaseNotificationModel> list;
  private Context context;

  public NotificationAdapter(Context context, ArrayList<BaseNotificationModel> list){
    this.list = list;
    this.context = context;
  }

  @Override
  public int getItemViewType(int position){
    BaseNotificationModel model = list.get(position);
    return model.modelType.value();
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
    View v = null;
    RecyclerView.ViewHolder holder = null;

    ModelType type = ModelType.fromValue(viewType);
    switch (type)
    {
      case NEW_NOTIFICATION_SECTION:
      case PREVIOUS_NOTIFICATION_SECTION:
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_section_header, parent, false);
        holder = new SectionHeaderViewHolder(v);
        break;
      case LIKES_NOTIFICATION:
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification_likes,
            parent, false);
        holder = new LikesItemViewHolder(v);
        break;
      case FRIEND_REQUEST_NOTIFICATION:
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification_friendrequest,
            parent, false);
        holder = new FriendRequestOrFollowItemViewHolder(v);
        break;
      case COMMENTS_NOTIFICATION:
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification_comments,
            parent, false);
        holder = new CommentsItemViewHolder(v);
        break;
      case FOLLOW_NOTIFICATION:
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification_follow,
            parent, false);
        holder = new FriendRequestOrFollowItemViewHolder(v);
        break;
      case MENTIONS_NOTIFICATION:
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification_mentions,
            parent, false);
        holder = new MentionsOrTaggingItemViewHolder(v);
        break;
      case TAGGING_NOTIFICATION:
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification_tagging,
            parent, false);
        holder = new MentionsOrTaggingItemViewHolder(v);
        break;
      case SCREENSHOTS_NOTIFICATION:
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification_screenshots,
            parent, false);
        holder = new ScreenshotsItemViewHolder(v);
        break;
      case TOOK_SCREENSHOT_NOTIFICATION:
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout
            .item_notification_took_screenshot, parent, false);
        holder = new TookScreenshotItemViewHolder(v);
        break;
      case LIKED_POST_NOTIFICATION:
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout
            .item_notification_likedyourpost, parent, false);
        holder = new LikedyourpostItemViewHolder(v);
        break;
      case TEXTPOST_NOTIFICATION:
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification_textpost,
            parent, false);
        holder = new TextPostItemViewHolder(v);
        break;
      default:
        break;
    }
    return holder;
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    BaseNotificationModel model = list.get(position);
    switch (model.modelType){
      case NEW_NOTIFICATION_SECTION:
        this.setNewSectionContent((SectionHeaderViewHolder) holder, (SectionHeaderModel) model);
        break;
      case PREVIOUS_NOTIFICATION_SECTION:
        this.setPreviousSectionContent((SectionHeaderViewHolder) holder, (SectionHeaderModel) model);
        break;
      case LIKES_NOTIFICATION:
        this.setLikesItemContent((LikesItemViewHolder) holder, (LikesModel) model);
        break;
      case FRIEND_REQUEST_NOTIFICATION:
        this.setFriendRequestOrFollowItemContent((FriendRequestOrFollowItemViewHolder) holder, (FriendRequestOrFollowModel) model);
        break;
      case COMMENTS_NOTIFICATION:
        this.setCommentsItemContent((CommentsItemViewHolder) holder, (CommentsModel) model);
        break;
      case FOLLOW_NOTIFICATION:
        this.setFriendRequestOrFollowItemContent((FriendRequestOrFollowItemViewHolder) holder,
            (FriendRequestOrFollowModel) model);
        break;
      case MENTIONS_NOTIFICATION:
        this.setMentionsOrTaggingItemContent((MentionsOrTaggingItemViewHolder)holder, (MentionsOrTaggingModel)model);
        break;
      case TAGGING_NOTIFICATION:
        this.setMentionsOrTaggingItemContent((MentionsOrTaggingItemViewHolder) holder, (MentionsOrTaggingModel) model);
        break;
      case SCREENSHOTS_NOTIFICATION:
        this.setScreenshotsItemContent((ScreenshotsItemViewHolder) holder, (ScreenshotsModel) model);
        break;
      case TOOK_SCREENSHOT_NOTIFICATION:
        this.setTookScreenshotItemContent((TookScreenshotItemViewHolder)holder, (TookScreenshotModel)model);
        break;
      case LIKED_POST_NOTIFICATION:
        this.setLikedyourpostItemContent((LikedyourpostItemViewHolder)holder, (LikedPostModel)model);
        break;
      case TEXTPOST_NOTIFICATION:
        this.setTextpostItemContent((TextPostItemViewHolder)holder, (TextPostModel)model);
        break;
      default:
        break;
    }
  }

  private void setNewSectionContent(SectionHeaderViewHolder holder, SectionHeaderModel item_data){
    holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.header_background_color));
    holder.txtTitle.setText(item_data.title);
    holder.txtTitle.setTextColor(context.getResources().getColor(R.color.new_header_title_color));
    if (item_data.notificationCount > 1){
      holder.txtNotificationCount.setText(String.valueOf(item_data.notificationCount));
    } else {
      holder.txtNotificationCount.setText("");
    }
  }

  private void setPreviousSectionContent(SectionHeaderViewHolder holder, SectionHeaderModel
      item_data){
    holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.header_background_color));
    holder.txtTitle.setTextColor(context.getResources().getColor(R.color.previous_header_title_color));
    holder.txtTitle.setText(item_data.title);
    holder.txtNotificationCount.setVisibility(View.GONE);
  }

  private void setLikesItemContent(LikesItemViewHolder holder, LikesModel item_data){
    String picName = item_data.thumbImage;
    int picId = context.getResources().getIdentifier(picName, "drawable", context.getPackageName());
    holder.thumbImage.setImageResource(picId);
    holder.thumbImage.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Log.i("touched item", "touched thumb image on likes cell.");
      }
    });

    String strTitle = String.valueOf(item_data.likesCount) + " Likes";
    holder.txtTitle.setText(strTitle);

    String strAgo = this.getAgoString(item_data.lastModificationDate);
    holder.txtAgo.setText(strAgo);
    boolean visited = item_data.visited;
    this.setAgoTextColor(holder.txtAgo, visited);

    ArrayList<HashMap<String, String>> arrLikePhotoNames = item_data.arrLikesPeople;
    holder.setUsersIconData(context, arrLikePhotoNames, item_data.likesCount);
  }

  private void setFriendRequestOrFollowItemContent(FriendRequestOrFollowItemViewHolder holder,
                                                   final FriendRequestOrFollowModel item_data){

    String profileImageName = item_data.profileImage;
    int picId = context.getResources().getIdentifier(profileImageName, "drawable", context.getPackageName());
    holder.profileImage.setImageResource(picId);

    holder.profileImage.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        switch (item_data.modelType) {
          case FRIEND_REQUEST_NOTIFICATION:
            Log.i("touched item", "touched profile image on friend request cell");
            break;
          case FOLLOW_NOTIFICATION:
            Log.i("touched item", "touched profile image on following cell");
            break;
          default:
            break;
        }
      }
    });
    holder.txtTitle.setText(item_data.username);
    holder.txtDescription.setText(item_data.comment);

    String strAgo = this.getAgoString(item_data.lastModificationDate);
    holder.txtAgo.setText(strAgo);
    this.setAgoTextColor(holder.txtAgo, item_data.visited);
  }

  private void setCommentsItemContent(CommentsItemViewHolder holder, CommentsModel item_data){
    String picName = item_data.thumbImage;
    int picId = context.getResources().getIdentifier(picName, "drawable", context.getPackageName());
    holder.thumbImage.setImageResource(picId);
    holder.thumbImage.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Log.i("touched item", "touched thumb image on comments cell");
      }
    });

    String strTitle = String.valueOf(item_data.commentCount) + " Comments";
    holder.txtTitle.setText(strTitle);

    String strAgo = this.getAgoString(item_data.lastModificationDate);
    holder.txtAgo.setText(strAgo);
    this.setAgoTextColor(holder.txtAgo, item_data.visited);

    int comment_count = item_data.commentCount;
    holder.setUsersIconData(context, item_data.arrCommentDatas, comment_count);
  }

  private void setMentionsOrTaggingItemContent(MentionsOrTaggingItemViewHolder holder,
                                               final MentionsOrTaggingModel item_data){
    String thumbPicName = item_data.thumbImage;
    int thumbPicId = context.getResources().getIdentifier(thumbPicName, "drawable", context.getPackageName());
    holder.thumbImage.setImageResource(thumbPicId);
    holder.thumbImage.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        switch (item_data.modelType){
          case MENTIONS_NOTIFICATION:
            Log.i("touched item", "touched thumb image on " + item_data.description + " cell");
            break;
          case TAGGING_NOTIFICATION:
            Log.i("touched item", "touched thumb image on " + item_data.description + " cell");
            break;
          default:
            break;
        }
      }
    });

    String picName = item_data.profileImage;
    int picId = context.getResources().getIdentifier(picName, "drawable", context.getPackageName());
    holder.profileImage.setImageResource(picId);
    holder.profileImage.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        switch (item_data.modelType){
          case MENTIONS_NOTIFICATION:
            Log.i("touched item", "touched profile image on " + item_data.description + " cell");
            break;
          case TAGGING_NOTIFICATION:
            Log.i("touched item", "touched profile image on " + item_data.description + " cell");
            break;
          default:
            break;
        }
      }
    });

    holder.txtTitle.setText(item_data.username);
    holder.txtSubTitle.setText(item_data.description);

    SpannableString strDescription = item_data.comment;
    if (strDescription.equals(new SpannableString(""))) {
      holder.txtDescription.setVisibility(View.GONE);
    } else {
      holder.txtDescription.setVisibility(View.VISIBLE);
      holder.txtDescription.setText(item_data.comment);
    }

    String strAgo = this.getAgoString(item_data.lastModificationDate);
    holder.txtAgo.setText(strAgo);
    this.setAgoTextColor(holder.txtAgo, item_data.visited);
  }

  private void setScreenshotsItemContent(ScreenshotsItemViewHolder holder, ScreenshotsModel item_data) {
    String picName = item_data.thumbImage;
    int picId = context.getResources().getIdentifier(picName, "drawable", context.getPackageName());
    holder.thumbImage.setImageResource(picId);
    holder.thumbImage.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Log.i("touched item", "touched thumb image on Screenshots cell");
      }
    });

    String strTitle = String.valueOf(item_data.screenshotsCount) + " Screenshots";
    holder.txtTitle.setText(strTitle);

    String strAgo = this.getAgoString(item_data.lastModificationDate);
    holder.txtAgo.setText(strAgo);
    this.setAgoTextColor(holder.txtAgo, item_data.visited);

    int screenshots_count = item_data.screenshotsCount;
    holder.setUsersIconData(context, item_data.arrScreenshotsData, screenshots_count);
  }

  private void setTookScreenshotItemContent(TookScreenshotItemViewHolder holder,
                                            TookScreenshotModel item_data){
    int thumbPicId = context.getResources().getIdentifier(item_data.thumbImage, "drawable",
        context.getPackageName());
    holder.thumbImage.setImageResource(thumbPicId);
    holder.thumbImage.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Log.i("touched item", "touched thumb image on Took a screenshot... cell");
      }
    });

    int profilePicId = context.getResources().getIdentifier(item_data.profileImage, "drawable",
        context.getPackageName());
    holder.profileImage.setImageResource(profilePicId);
    holder.profileImage.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Log.i("touched item", "touched profile image on Took a screenshots... cell");
      }
    });

    holder.txtTitle.setText(item_data.username);
    holder.txtSubTitle.setText(item_data.description);

    String strAgo = this.getAgoString(item_data.lastModificationDate);
    holder.txtAgo.setText(strAgo);
    this.setAgoTextColor(holder.txtAgo, item_data.visited);
  }

  private void setLikedyourpostItemContent(LikedyourpostItemViewHolder holder, LikedPostModel
      item_data){

    int thumbPicId = context.getResources().getIdentifier(item_data.thumbImage, "drawable",
        context.getPackageName());
    holder.thumbImage.setImageResource(thumbPicId);
    holder.thumbImage.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Log.i("touched item", "touched thumb image on liked your post cell");
      }
    });

    int profilePicId = context.getResources().getIdentifier(item_data.profileImage, "drawable", context
        .getPackageName());
    holder.profileImage.setImageResource(profilePicId);
    holder.profileImage.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Log.i("touched item", "toched profile image on liked your post cell");
      }
    });

    holder.txtTitle.setText(item_data.username);
    holder.txtSubTitle.setText(item_data.comment);

    String strAgo = this.getAgoString(item_data.lastModificationDate);
    holder.txtAgo.setText(strAgo);
    this.setAgoTextColor(holder.txtAgo, item_data.visited);
  }

  private void setTextpostItemContent(TextPostItemViewHolder holder, TextPostModel item_data){
    String picName = item_data.profileImage;
    int picId = context.getResources().getIdentifier(picName, "drawable", context.getPackageName());
    holder.profileImage.setImageResource(picId);
    holder.profileImage.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Log.i("touched item", "touched profile image on text post cell");
      }
    });

    holder.txtTitle.setText(item_data.username);
    holder.txtSubTitle.setText(item_data.comment);

    holder.txtDescription.setText(item_data.description);
    holder.txtDescription.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Log.i("touched item", "touched text on text post cell");
      }
    });

    String strAgo = this.getAgoString(item_data.lastModificationDate);
    holder.txtAgo.setText(strAgo);
    this.setAgoTextColor(holder.txtAgo, item_data.visited);
  }

  // GET AGO STRING: for example, 5m, 1h, 1d...
  private String getAgoString(Date lastModificationDate){
    String strAgo = "";
    Date now = new Date();
    long diff = now.getTime() - lastModificationDate.getTime();
    int diffDays = (int) (diff / (24* 1000 * 60 * 60));
    int diffHours = (int) (diff / (1000 * 60 * 60));
    int diffMinutes = (int) (diff / (1000 * 60));

    if (diffDays > 0) {
      strAgo = String.format("%dd", diffDays);
    } else if (diffHours > 0){
      strAgo = String.format("%dh", diffHours);
    } else if (diffMinutes > 0) {
      strAgo = String.format("%dm", diffMinutes);
    }
    return strAgo;
  }
  // Ago label color according to new or previous notification
  private void setAgoTextColor(TextView txtAgo, boolean visited){
    if(visited) {
      txtAgo.setTextColor(context.getResources().getColor(R.color.previous_ago_color));
    } else {
      txtAgo.setTextColor(context.getResources().getColor(R.color.new_ago_color));
    }
  }

  @Override
  public int getItemCount() {
    //returns the number of elements the RecyclerView will display
    return list.size();
  }

  @Override
  public void onAttachedToRecyclerView(RecyclerView recyclerView) {
    super.onAttachedToRecyclerView(recyclerView);
  }

  // Insert a new item to the RecyclerView on a predefined position
  public void insert(int position, BaseNotificationModel data) {
    list.add(position, data);
    notifyItemInserted(position);
  }

  // Remove a RecyclerView item containing a specified Data object
  public void remove(BaseNotificationModel data) {
    int position = list.indexOf(data);
    list.remove(position);
    notifyItemRemoved(position);
  }
}