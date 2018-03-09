package com.md.studio.dto;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import com.md.studio.domain.PhotoUploadDirectory;

public class UploadPhotoDto implements Serializable {
	private static final long serialVersionUID = -4040597020944931721L;
	private List<File> images;
	private PhotoUploadDirectory photoUploadDirectory;
	private String userid;
	
	
	public List<File> getImages() {
		return images;
	}
	public void setImages(List<File> images) {
		this.images = images;
	}
	public PhotoUploadDirectory getPhotoUploadDirectory() {
		return photoUploadDirectory;
	}
	public void setPhotoUploadDirectory(PhotoUploadDirectory photoUploadDirectory) {
		this.photoUploadDirectory = photoUploadDirectory;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
}
