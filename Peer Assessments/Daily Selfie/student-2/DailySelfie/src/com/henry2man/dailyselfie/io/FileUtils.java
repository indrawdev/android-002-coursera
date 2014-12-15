package com.henry2man.dailyselfie.io;

import java.io.File;

import com.henry2man.dailyselfie.model.Selfie;

public class FileUtils {

	public static boolean selfieExists(Selfie selfie) {
		if (selfie != null) {
			if (selfie.getPath() != null) {
				return new File(selfie.getPath()).exists();
			}
		}
		return false;
	}

}
