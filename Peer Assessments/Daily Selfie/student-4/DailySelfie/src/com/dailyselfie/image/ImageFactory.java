package com.dailyselfie.image;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.dailyselfie.R;
import com.dailyselfie.adapter.ImageRecord;

public class ImageFactory {

	private Context mContext;
	private String imageFileName;
	private String mCurrentPhotoPath;
	private final String JPEG_FILE_SUFFIX = ".jpg";
	private AlbumStorageDirFactory mAlbumStorageDirFactory = new BaseAlbumDirFactory();
	
	public ImageFactory(Context mContext) {
		this.mContext = mContext;
	}
	
	private String getAlbumName() {
		return mContext.getString(R.string.selfies);
	}
	
	public ArrayList<ImageRecord> loadFiles() {
		File storageDir = mAlbumStorageDirFactory.getAlbumStorageDir(getAlbumName());
		ArrayList<ImageRecord> list = new ArrayList<ImageRecord>();
		if(storageDir.listFiles() != null ){
			for(File file :storageDir.listFiles()){
				Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
				list.add(new ImageRecord(bitmap, file.getName()));
			}
		}
		return list;
	}
	
	@SuppressLint("SimpleDateFormat")
	private File createImageFile() throws IOException {
		String imageFileName = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, getAlbumDir());
		return imageF;
	}
	
	private File getAlbumDir() {
		File storageDir = mAlbumStorageDirFactory.getAlbumStorageDir(getAlbumName());
		if (storageDir != null) {
			if (! storageDir.mkdirs()) {
				if (! storageDir.exists()){
					Log.d("DailySelfies", "failed to create directory!");
					return null;
				}
			}
		}
		return storageDir;
	}
	
	public File setUpPhotoFile() throws IOException {
		File f = createImageFile();
		mCurrentPhotoPath = f.getAbsolutePath();
		imageFileName = f.getName();
		return f;
	}
	
	
	public Bitmap getImage(String path) {
		return  BitmapFactory.decodeFile(path);
	}
	
	public ImageRecord handlePhoto() {
		return new ImageRecord(getImage(mCurrentPhotoPath), imageFileName);
	}
	
	public void removeFile(String fileName){
		File storageDir = mAlbumStorageDirFactory.getAlbumStorageDir(getAlbumName());
		for(File file :storageDir.listFiles()){
			if (file.getName().equals(fileName)){
				file.delete();
			}
		}
	}
	
	public File getFile(String fileName){
		File storageDir = mAlbumStorageDirFactory.getAlbumStorageDir(getAlbumName());
		for(File file :storageDir.listFiles()){
			if (file.getName().equals(fileName)){
				return file;
			}
		}
		return null;
		
	}
	
	public void removeAllFile(){
		File storageDir = mAlbumStorageDirFactory.getAlbumStorageDir(getAlbumName());
		for(File file :storageDir.listFiles()){
				file.delete();
		}
	}

	public void removeCurrentFile() {
		removeFile(imageFileName);
	}
}
