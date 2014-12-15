package com.dailyselfie.adapter;

import java.io.Serializable;

import android.graphics.Bitmap;

public class ImageRecord implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String imageName;
	private Bitmap image;

	public ImageRecord(Bitmap bitmap, String imageName) {
		image = bitmap;
		this.imageName = imageName;
	}

	public ImageRecord() {	
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public Bitmap getImage() {
		return image;
	}

	public void setImage(Bitmap image) {
		this.image = image;
	}
}
