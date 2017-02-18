# android_notification_1

## Code review

_Author Mikael_
_Date 11th of April 2016_

### General notes

The results looks fine but now we would like you to take more time to really match the design in detail and improve interactions.
Each user actions should display a console message indicating what interaction has been made.

* The user circle icons that are on the right side of a notification are always the size in the designs, either they are within the notification cell title or in the user list of a notification cell.
* One way we are using to compare design is by taking a screenshot of the app on a phone with similar screen ratio as the screenshots and we scale them to the same size to finally compare both of them in 2 differents layers using photoshop. You can use the same approach to ensure that your final result matches the proposed design.
* We are focusing on quality more than speed and we have no problem with you taking your time to refine the current version.
* Overall: very good job for this first version.


### colors.xml

In the spec, we recommended to store all constants, including colors in a Contants class but in Android, it's better to simply have all colors in the dedicated location:

* Move all color constants to colors.xml

### Explicit class names and methods

* Change CustomImageview to HeadImageView
* Change sortArrayModel() to initDemoData(). It should return an ArrayList of your Notification model superclass . (see next part about Data model)

### Data model

* Now, you are using an array list of HashMap <String, Object>. We would like you to write a class for each notification model that inherit a BaseNotificationModel class so you can build an array list of BaseNotificationModel.

We don't like to use Object in our Android code as it leads to many bugs. Having explicit data structure to define each type of notification helps us for debugging. Also we don't use strings to manipulate data as It can't be checked by the compiler.

### Data and Views abstraction

Right now, you display data that you already preprared to be displayed:

    comments_template.put("description", "Tim Neuner: Wow I really need to ski! Where did you...");

The approach we prefer is in 3 phases

- Receive raw data (json), in your case, you don't need to do that but you have to make a set of raw data
- Parse and prepare the raw data in order to build local clean model objects
- Use the model objects to feed the layouts

*  We would like that the code doing the layout takes care of preparing the data to be displayed. Meaning that the data that you provide to your adapter should be more like:

Using an example with _Mention_ notifications

    MentionModel mentionModel = MentionModel();
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    Date modifDate = sdf.parse("21/12/2016");
    mentionModel.lastModificationDate = modifDate;
    mentionModel.comment = "Yeah Joe Smith I totally agree with you! Let's talk more when you get back in town. I'm currently eating a burger in Las vegas" //You can use SpannableString here to format it properly
    mentionModel.username = "Yuki Hayashi";
    mentionModel.likeCount = 18

* dataModel should only contain notification related data and not layout related data.

When we parse the json data, we know that it's a Mention notification or another type. So you, when we parse, we directly build the proper objects: MentionModel() for example. And we don't store any layout related texts withing the model.

    mentions_template.put("subtitle", "mentioned you"); //This should be in the code creating the layout from the data or in string constants if it's a static string.

    //same for
    follow_template.put("description", "started following you");

* From there, your adapter should format the data in order to be displayed the same was as it is in the design.

For example, from the Date object, you will have to process it to display 9h, 15m, 5d, 1y...
For the comment, if it's too long, it should automatically cut the text to display the "..." at the end of the string.

Finally, instead of matching on the class name, you can reuse the enum for notification types in the BaseNotificationModel class as an object variable.

in getView() of your NotificationAdapter, you can then still switch on this variable to know what model you are manipulating.

### Memory management

Right now, you are using ListView. It works but we need RecyclerViews to ensure that we use the memory properly.

http://developer.android.com/training/material/lists-cards.html

* Please rewrite your layout view using Recycler views 


### Matching the design

_Note_ Matching the design means here: reaching the same look and feel with the same colors, sizes, spaces and proportions proposed in the 4 screenshots.

* Sections titles
    - reduce the size of the titles ("new notifications", etc...) it's clearly too big compared to the design
    - The section view height is too small, need to increase it to match the design.
    - Background color should be gray
    - The counter font also has to match the new title size.

* Likes Cell
    - Bold names have to be part of the cell content, you might want to use SpannableString
    - Space between "18 Likes" and "Yuki Hayashi, ... " is too big compared to the design.
    - The people head images are slightly too small compared to the post image.
    - People head images have too much white space between each other.
    - Should have a more button next to the 4th head image
    - Shouldn't see the scrollbar indicator when scrolling (shouldn't scroll until user presses the more button)
    - The "18 Likes" is a bit too big compared to the icon, same for other texts like "3 Comments, etc..."
    
* Follow cell
    - The star is a bit too big proportionaly to the head circle icon
    - The left of the star should be aligned to the left of the user name.
    - The user name should be placed lower, the bottom should almost be at the center Y of the user Icon

* Mention
    - Reduce the size of the comment (green bubble) icon to match the design
    - The bold text for user name can be applied here using SpannableString ("Joe Smith")
    - name of the user "Yuki Hayashi" should be placed a little bit lower and have the bottom of the letter aligned with the center Y of the user icon.

* Tagged
    - Same notes as _Mention_

* Screenshots
    - User head images are too big but this time, there is a more button
    - name Y coordinate too high

* Commented
    - green bubble icon is too big
    - name Y coordinate too high

* Liked post
    - heart might be a bit too big, that might explain why "liked your post" doesn't fit the screen width on a HTC One M9


## Code review #2

_Author Mikael_
_Date 14th of April 2016_

* Very good layouts, very clear
* Much better code now, more efficient and easier to read
* Please re-read spec + previous code review next time or at least ask questions if something is not clear.
* Added HTC One M9 screenshots in screenshots folder
* Section title background is still white, should be gray
* on HTC One M9 (test phone here), the users' icon list is still scrollable (maybe first, hide the scrolling bar in this area) and we should see the more button. (for example in the likes notification, should show 3 images and 1 more icon, should not display other users'icons)
* "accepted your friend request" label is not aligned to the center Y of the 4 circles icon. However the circles icon position is good.
* "accepted your friend request" cell circles icon is a little bit too small compared to the design.
* Reduce section title slightly, it's a little bit too big compared to the cell size and design.
* the "started following you" cell's star is too small and not aligned vertically with the text.
* Names next to the circle head images are still too high. They should be a bit lower, please take time to compare with jpg design files.
* String should be in Strings.xml -> 

This is in FriendRequestOrFollowModel, please check for other magic strings in the code.
    
    comment = "accepted your friend request";

* in **LikesItemViewHolder** If you set a limit to the number of ImageView that a view can contain, declare a constant at the top of the class so it becomes easy to change the number of images we want to display in a notification. Ideally, If you could adjust dynamically the number of Images displayed depending on the available space, that would be great.
* same for other classes like: **ScreenshotsItemViewHolder**
* Touch event are not handled and are not displaying any console message, please display messages in console for every Touch event described in the specification. (ex: Go to post focus, Go to user profile, Go to like list... etc)

### Demo app

* Please add the took a screenshot of your flash post cell, it's seems it has been replaced by a second "liked your post"


## Code review #3

_Author Mikael_
_Date 19th of April 2016_

* Touch events now display console messages but they are either not correct or the different areas of a cell are not really handled. Please check the specs for all different touch events on each cells (be careful, they can be different from each other)
* The text next to the small yellow star and the circle icon is still not aligned on the vertical center, It's very small difference but I can see it comparing design and the app running on the phone.
* Setting text to bold algorithm as to be done on the client side for now.

Right now, it's written directly in your code

        
      likesModel.comment = new SpannableString("Yuki Hayashi, Chief Yin, Tim Neuner, Mary Hikita," +
          " ...");
      likesModel.comment.setSpan(new StyleSpan(Typeface.BOLD), 0, 23, 0);

It should be a list of User models who liked the post, each user having a name and a picture. From this array of user models, you can generate the text within the cell.

* A commentsModel shouldn't contain a troncated text like you set 

not:

        commentsModel.comment = new SpannableString("Tim Neuner: Wow I really need to ski! Where " +
          "did you...");

but more like this (normal comment) and when you layout, it should be truncated automatically. (It seems it's already working properly though)

        commentsModel.comment = new SpannableString("Tim Neuner: Wow I really need to ski! Where did you go this year? Did you stay in a Hotel?");


