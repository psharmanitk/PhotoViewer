package com.android.imageview.model;

import android.util.Log;

import org.junit.Before;
import org.junit.Test;

import java.net.MalformedURLException;

import static org.junit.Assert.*;

public class PhotoObjectTest {
    PhotoObject myPhotoObject;
    @Before
    public void setUp() throws Exception {
        myPhotoObject = new PhotoObject();
    }

    @Test
    public void testSetFarm() {
        myPhotoObject.setFarm("1");
        assertTrue("farm is set", myPhotoObject.farm == "1");
    }

    @Test
    public void testSetServer() {
        myPhotoObject.setServer("578");
        assertTrue("Server is set", myPhotoObject.server == "578");
    }

    @Test
    public void testSetId() {
        myPhotoObject.setId("23451156376");
        assertTrue("Server is set", myPhotoObject.id == "23451156376");
    }

    @Test
    public void testSetSecret() {
        myPhotoObject.setSecret("8983a8ebc7");
        assertTrue("Secret is set", myPhotoObject.secret == "8983a8ebc7");
    }
    //http://farm1.static.flickr.com/578/23451156376_8983a8ebc7.jpg
    @Test
    public void testSetPhotoUrl() throws MalformedURLException{
        myPhotoObject.setFarm("1");
        myPhotoObject.setServer("578");
        myPhotoObject.setId("23451156376");
        myPhotoObject.setSecret("8983a8ebc7");
        myPhotoObject.setPhotoUrl();
        Log.d("priya", myPhotoObject.photoUrl.toString());
        assertTrue("Url set", myPhotoObject.photoUrl.toString().equals("https://farm1.static.flickr.com/578/23451156376_8983a8ebc7.jpg"));
    }
}