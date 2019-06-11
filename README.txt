CONTENTS OF THIS FILE
---------------------
   
 * Introduction
 * Requirements
 * Modules
 * Maintainers

INTRODUCTION
------------

This project shows the list of Images loaded from the web for the content user queries.
User needs to mention the text in the Edit text provided in the Main Activity for which u want to see the results.
The result is displayed in # column infinite Grid View and when user scroll down more images are loaded.



REQUIREMENTS
------------

The project will run on Android Studio.
Pull the project and run on Android Studio.
Internet should be connected on device being tested to show the result from Web.


MODULES
-------------------

ImageViewApp\app\src\main\java\com\android\imageview\model

This module contains the model which represent the files that interact with the Main View , fetch the result from Web, Display Result on View, Notify the View of Data change so that the view can be updated.
 * PhotoListBuilder.java: This file is to build the list of URLs which need to be displayed in the grid view.
 * PhotoObject.java: This is the object class for the details of images which will be displayed like its, farm, id, secret, server to build the URL(http://farm{farm}.static.flickr.com/{server}/{id}_{secret}.jpg) of image
 * Interface LoadDataCallback.java: This is the Callback to Main View that List of URLs has been fetched from Web.
 * BitmapCache.java: This the memory cache implementation that loads and caches the image from list of URL. When download is complete it sets the image view to View Holder and notifies the View to display it.
   Image already available in cache is set from memory cache.
 ImageViewApp\app\src\main\java\com\android\imageview\view
 * MainActivity.java: This is the File for Main View of the applications that contains and EditText View to enter user query to search object. After Images are loaded they are displayed in view below.
 * ImageAdapter: To set View Holder for RecyclerView and set the image from list of URls.


MAINTAINERS
-----------
priyankasharma.nitk@gmail.com

