package com.coursera.dailyselfie.entity;

import android.graphics.Bitmap;
import android.util.Log;

public class PhotoInfo {
    private String photoLabel = "";
    private Bitmap icon;
    private String path;
   
    public void prettyPrint() {
        Log.v(photoLabel, "");
    }

	public String getPhotoLabel() {
		return photoLabel;
	}

	public void setPhotoLabel(String photoLabel) {
		this.photoLabel = photoLabel;
	}

	public Bitmap getIcon() {
		return icon;
	}

	public void setIcon(Bitmap icon) {
		this.icon = icon;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
