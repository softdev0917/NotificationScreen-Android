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

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NotificationActivity extends Activity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_noticiation);

    ArrayList<BaseNotificationModel> modelList = readDemoJsonData();
    ArrayList<BaseNotificationModel> list = this.initDemoData(modelList);

    RecyclerView recyclerView = (RecyclerView)findViewById(R.id.notification_recycler_view);
    RecyclerView.Adapter adapter = new NotificationAdapter(getApplicationContext(), list);
    recyclerView.setAdapter(adapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));
  }

  private ArrayList<BaseNotificationModel> initDemoData(ArrayList<BaseNotificationModel> modelList){
    ArrayList<BaseNotificationModel> list = new ArrayList<BaseNotificationModel>();

    int newNotificationCount = 0;
    int previousNotificationCount = 0;
    for (int i = 0; i < modelList.size(); i++){
      BaseNotificationModel model = modelList.get(i);
      if (model.visited) {
        previousNotificationCount += 1;
      } else {
        newNotificationCount += 1;
      }
    }

    if (newNotificationCount > 0){
      // new notification header model
      SectionHeaderModel newHeaderModel = new SectionHeaderModel(getApplicationContext(),
          ModelType.NEW_NOTIFICATION_SECTION);
      String strNewHeaderTitle = getResources().getString(R.string.title_new_notifications);
      if (newNotificationCount == 1) {
        strNewHeaderTitle = getResources().getString(R.string.title_new_notification);
      }
      newHeaderModel.title = strNewHeaderTitle;
      newHeaderModel.notificationCount = newNotificationCount;
      list.add(newHeaderModel);

      for (int i = 0; i < modelList.size(); i++){
        BaseNotificationModel model = modelList.get(i);
        if (model.visited == false){
          list.add(model);
        }
      }
    }

    if (previousNotificationCount > 0){
      if (newNotificationCount > 0){
        // previous notification header model
        SectionHeaderModel previousHeaderModel = new SectionHeaderModel(getApplicationContext(),
            ModelType.PREVIOUS_NOTIFICATION_SECTION);
        String strPreviousHeaderTitle = getResources().getString(R.string
            .title_previous_notifications);
        if (previousNotificationCount == 1){
          strPreviousHeaderTitle = getResources().getString(R.string.title_previous_notification);
        }
        previousHeaderModel.title = strPreviousHeaderTitle;
        list.add(previousHeaderModel);
      }

      for (int i = 0; i < modelList.size(); i++){
        BaseNotificationModel model = modelList.get(i);
        if (model.visited){
          list.add(model);
        }
      }
    }
    return list;
  }

  // read json file to string.
  private String loadJsonFromAsset(){
    String json = null;
    try {
      InputStream is = getAssets().open("demo.json");
      int size = is.available();
      byte[] buffer = new byte[size];
      is.read(buffer);
      is.close();
      json = new String(buffer, "UTF-8");
    } catch (IOException ex){
      ex.printStackTrace();
      return null;
    }
    return json;
  }

  private ArrayList<BaseNotificationModel> readDemoJsonData() {
    ArrayList<BaseNotificationModel> list = new ArrayList<BaseNotificationModel>();
    try {
      JSONObject obj = new JSONObject(loadJsonFromAsset());
      JSONArray arrNotifications = obj.getJSONArray("notifications");

      for (int i = 0; i < arrNotifications.length(); i++) {
        JSONObject jsonObject = arrNotifications.getJSONObject(i);
        Log.d("Detail-->", jsonObject.getString("type"));

        BaseNotificationModel notificationModel = getModelFromJsonObject(jsonObject);
        if (notificationModel != null){
          list.add(notificationModel);
        }
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return list;
  }

  private BaseNotificationModel getModelFromJsonObject(JSONObject object){
    BaseNotificationModel model = null;
    try {
      boolean visited = object.has("visited") == true ? object.getBoolean("visited") : true;
      String type = object.getString("type");

      if (type.equals("like")){
        model = getLikesModel(object, visited);
      } else if (type.equals("friendrequest")){
        model = getFriendRequestModel(object, visited);
      } else if (type.equals("comments")){
        model = getCommentsModel(object, visited);
      } else if (type.equals("follow")){
        model = getFollowModel(object, visited);
      } else if (type.equals("mentions")){
        model = getMentionsModel(object, visited);
      } else if (type.equals("tagging")){
        model = getTaggingModel(object, visited);
      } else if (type.equals("screenshots")){
        model = getScreenshotsModel(object, visited);
      } else if (type.equals("tookscreenshot")){
        model = getTookscreenshotModel(object, visited);
      } else if (type.equals("likedpost")){
        model = getLikedpostModel(object, visited);
      } else if (type.equals("textpost")) {
        model = getTextpostModel(object, visited);
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return model;
  }

  private LikesModel getLikesModel(JSONObject object, boolean visited){
    LikesModel likesModel = new LikesModel(getApplicationContext(), visited);
    try{
      likesModel.thumbImage = object.has("thumbImage") == true ? object.getString("thumbImage")
            : "";
      likesModel.likesCount = object.has("likescount") == true ? object.getInt("likescount") : 0;

      if (object.has("lastModificationDate")){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy:HH:mm:SS");
        String strDate = object.getString("lastModificationDate");
        try {
          likesModel.lastModificationDate = dateFormat.parse(strDate);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }

      JSONArray arrLikepeoples = new JSONArray();
      if (object.has("peoples")){
        arrLikepeoples = object.getJSONArray("peoples");
      }
      for (int i = 0; i < arrLikepeoples.length(); i++){
        JSONObject obj = arrLikepeoples.getJSONObject(i);

        HashMap<String, String> likePeople = new HashMap<String, String>();
        String name = obj.has("name") == true ? obj.getString("name") : "";
        String photo = obj.has("photo") == true ? obj.getString("photo") : "";
        likePeople.put("name", name);
        likePeople.put("photo", photo);
        likesModel.arrLikesPeople.add(likePeople);
      }
    } catch (JSONException e){
      e.printStackTrace();
    }
    return likesModel;
  }

  private FriendRequestOrFollowModel getFriendRequestModel(JSONObject object, boolean visited){

    FriendRequestOrFollowModel friendRequestModel = new FriendRequestOrFollowModel(getApplicationContext(),
        ModelType.FRIEND_REQUEST_NOTIFICATION,
        visited);
    try{
      friendRequestModel.profileImage = object.has("profileImage") == true ? object.getString
          ("profileImage") : "";
      friendRequestModel.username = object.has("username") == true ? object.getString("username")
          : "";

      if (object.has("lastModificationDate")){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy:HH:mm:SS");
        String strDate = object.getString("lastModificationDate");
        try{
          friendRequestModel.lastModificationDate = dateFormat.parse(strDate);
        } catch (Exception e){
          e.printStackTrace();
        }
      }
    } catch (JSONException e){
      e.printStackTrace();
    }
    return friendRequestModel;
  }

  private CommentsModel getCommentsModel(JSONObject object, boolean visited){

    CommentsModel commentsModel = new CommentsModel(getApplicationContext(), visited);
    try{
      commentsModel.thumbImage = object.has("thumbImage") == true ? object.getString
          ("thumbImage") : "";
      commentsModel.commentCount = object.has("commentCount") == true ? object.getInt
          ("commentCount") : 0;

      if (object.has("lastModificationDate")){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy:HH:mm:SS");
        String strDate = object.getString("lastModificationDate");
        try {
          commentsModel.lastModificationDate = dateFormat.parse(strDate);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }

      JSONArray arrCommentpeoples = new JSONArray();
      if (object.has("peoples")){
        arrCommentpeoples = object.getJSONArray("peoples");
      }
      for (int i = 0; i < arrCommentpeoples.length(); i++){
        JSONObject obj = arrCommentpeoples.getJSONObject(i);
        HashMap<String, String> commentPeople = new HashMap<String, String>();
        String name = obj.has("name") == true ? obj.getString("name") : "";
        String photo = obj.has("photo") == true ? obj.getString("photo") : "";
        String comment = obj.has("comment") == true ? obj.getString("comment") : "";
        commentPeople.put("name", name);
        commentPeople.put("photo", photo);
        commentPeople.put("comment", comment);
        commentsModel.arrCommentDatas.add(commentPeople);
      }
    } catch (JSONException e){
      e.printStackTrace();
    }
    return commentsModel;
  }

  private FriendRequestOrFollowModel getFollowModel(JSONObject object, boolean visited){

    FriendRequestOrFollowModel followModel = new FriendRequestOrFollowModel
        (getApplicationContext(), ModelType.FOLLOW_NOTIFICATION, true);
    try{
      followModel.profileImage = object.has("profileImage") == true ? object.getString
          ("profileImage") : "";
      followModel.username = object.has("username") == true ? object.getString("username") : "";
      followModel.comment = getResources().getString(R.string.started_following_you);

      if (object.has("lastModificationDate")){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy:HH:mm:SS");
        String strDate = object.getString("lastModificationDate");
        try{
          followModel.lastModificationDate = dateFormat.parse(strDate);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }catch (JSONException e){
      e.printStackTrace();
    }
    return followModel;
  }

  private MentionsOrTaggingModel getMentionsModel(JSONObject object, boolean visited){

    MentionsOrTaggingModel mentionsModel = new MentionsOrTaggingModel(getApplicationContext(),
        ModelType.MENTIONS_NOTIFICATION, visited);
    try{
      mentionsModel.thumbImage = object.has("thumbImage") == true ? object.getString
          ("thumbImage") : "";
      mentionsModel.profileImage = object.has("profileImage") == true ? object.getString
          ("profileImage") : "";
      mentionsModel.description = object.has("description") == true ? object.getString
          ("description") : "";

      mentionsModel.username = object.has("username") == true ? object.getString("username") : "";
      String comment = object.has("comment") == true ? object.getString("comment") : "";
      mentionsModel.comment = new SpannableString(comment);
      //mentionsModel.comment.setSpan(new StyleSpan(Typeface.BOLD), 5, 14, 0); // change this
      // part...

      if (object.has("lastModificationDate")){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy:HH:mm:SS");
        String strDate = object.getString("lastModificationDate");
        try {
          mentionsModel.lastModificationDate = dateFormat.parse(strDate);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    } catch (JSONException e){
      e.printStackTrace();
    }
    return mentionsModel;
  }

  private MentionsOrTaggingModel getTaggingModel(JSONObject object, boolean visited){
    MentionsOrTaggingModel taggingModel = new MentionsOrTaggingModel(getApplicationContext(),
        ModelType.TAGGING_NOTIFICATION, visited);
    try{
      taggingModel.thumbImage = object.has("thumbImage") == true ? object.getString("thumbImage")
          : "";
      taggingModel.profileImage = object.has("profileImage") == true ? object.getString
          ("profileImage") : "";
      taggingModel.description = getResources().getString(R.string.tagged_you);
      taggingModel.username = object.has("username") == true ? object.getString("username") : "";
      String strComment = object.has("comment") == true ? object.getString("comment") : "";
      taggingModel.comment = new SpannableString(strComment);
      // change this part ///////////////
      if (strComment.length() > 72){
        taggingModel.comment.setSpan(new StyleSpan(Typeface.BOLD), 5, 35, 0);
        taggingModel.comment.setSpan(new StyleSpan(Typeface.BOLD), 40, 52, 0);
        taggingModel.comment.setSpan(new StyleSpan(Typeface.BOLD), 56, 72, 0);
      }
      ////////////////////////////////////

      if (object.has("lastModificationDate")){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy:HH:mm:SS");
        String strDate = object.getString("lastModificationDate");
        try {
          taggingModel.lastModificationDate = dateFormat.parse(strDate);
        } catch (Exception e){
          e.printStackTrace();
        }
      }
    } catch (JSONException e){
      e.printStackTrace();
    }

    return taggingModel;
  }

  private ScreenshotsModel getScreenshotsModel(JSONObject object, boolean visited){
    ScreenshotsModel screenshotsModel = new ScreenshotsModel(getApplicationContext(), visited);
    try{
      screenshotsModel.thumbImage = object.has("thumbImage") == true ? object.getString
          ("thumbImage") : "";
      screenshotsModel.screenshotsCount = object.has("screenshotsCount") == true ? object.getInt
          ("screenshotsCount") : 0;

      if (object.has("lastModificationDate")){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy:HH:mm:SS");
        String strDate = object.getString("lastModificationDate");
        try {
          screenshotsModel.lastModificationDate = dateFormat.parse(strDate);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }

      JSONArray arrLikepeoples = new JSONArray();
      if (object.has("peoples")){
        arrLikepeoples = object.getJSONArray("peoples");
      }
      for (int i = 0; i < arrLikepeoples.length(); i++){
        JSONObject obj = arrLikepeoples.getJSONObject(i);

        HashMap<String, String> people = new HashMap<String, String>();
        String name = obj.has("name") == true ? obj.getString("name") : "";
        String photo = obj.has("photo") == true ? obj.getString("photo") : "";
        people.put("name", name);
        people.put("photo", photo);
        screenshotsModel.arrScreenshotsData.add(people);
      }
    } catch (JSONException e){
      e.printStackTrace();
    }
    return screenshotsModel;
  }

  private LikedPostModel getLikedpostModel(JSONObject object, boolean visited){
    LikedPostModel likedyourpostModel = new LikedPostModel(getApplicationContext(), visited);
    try{
      likedyourpostModel.thumbImage = object.has("thumbImage") == true ? object.getString
          ("thumbImage") : "";
      likedyourpostModel.profileImage = object.has("profileImage") == true ? object.getString
          ("profileImage") : "";
      likedyourpostModel.username = object.has("username") == true ? object.getString("username")
          : "";
      likedyourpostModel.comment = getResources().getString(R.string.liked_your_post);

      if (object.has("lastModificationDate")){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy:HH:mm:SS");
        String strDate = object.getString("lastModificationDate");
        try {
          likedyourpostModel.lastModificationDate = dateFormat.parse(strDate);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    } catch (JSONException e){
      e.printStackTrace();
    }
    return likedyourpostModel;
  }

  private TookScreenshotModel getTookscreenshotModel(JSONObject object, boolean visited){
    TookScreenshotModel tookScreenshotModel = new TookScreenshotModel(getApplicationContext(), visited);
    try{
      tookScreenshotModel.thumbImage = object.has("thumbImage") == true ? object.getString
          ("thumbImage") : "";
      tookScreenshotModel.profileImage = object.has("profileImage") == true ? object.getString
          ("profileImage") : "";
      tookScreenshotModel.username = object.has("username") == true ? object.getString
          ("username") : "";
      tookScreenshotModel.description = getResources().getString(R.string.took_screenshot);

      if (object.has("lastModificationDate")){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy:HH:mm:SS");
        String strDate = object.getString("lastModificationDate");
        try {
          tookScreenshotModel.lastModificationDate = dateFormat.parse(strDate);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    } catch (JSONException e){
      e.printStackTrace();
    }
    return tookScreenshotModel;
  }

  private TextPostModel getTextpostModel(JSONObject object, boolean visited){
    TextPostModel textpostModel = new TextPostModel(getApplicationContext(), visited);
    try{
      textpostModel.profileImage = object.has("profileImage") == true ? object.getString
          ("profileImage") : "";
      textpostModel.username = object.has("username") == true ? object.getString("username") : "";
      textpostModel.comment = getResources().getString(R.string.liked_your_post);
      textpostModel.description = object.has("description") == true ? object.getString
          ("description") : "";

      if (object.has("lastModificationDate")){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy:HH:mm:SS");
        String strDate = object.getString("lastModificationDate");
        try{
          textpostModel.lastModificationDate = dateFormat.parse(strDate);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    } catch (JSONException e){
      e.printStackTrace();
    }
    return textpostModel;
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_notification, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
        return true;
    }

    return super.onOptionsItemSelected(item);
  }
}
