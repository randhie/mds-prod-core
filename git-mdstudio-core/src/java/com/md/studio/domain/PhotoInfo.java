package com.md.studio.domain;

import java.io.Serializable;

public class PhotoInfo implements Serializable {
	private static final long serialVersionUID = -1579451050782693634L;

	private long photoId;
	private long categoryId;
	private String fileName;
	private int photoType = 0;
	private byte[] fileBytes;
	private byte[] thumbnailBytes;
	
	public static final int PORTRAIT = 0;
	public static final int LANDSCAPE = 1;
	public static final int COVERPHOTO = 2;
	public static final int BANNER = 3;
	
	public long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}
	public long getPhotoId() {
		return photoId;
	}
	public void setPhotoId(long photoId) {
		this.photoId = photoId;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public int getPhotoType() {
		return photoType;
	}
	public void setPhotoType(int photoType) {
		this.photoType = photoType;
	}
	public byte[] getFileBytes() {
		return fileBytes;
	}
	public void setFileBytes(byte[] fileBytes) {
		this.fileBytes = fileBytes;
	}
	public byte[] getThumbnailBytes() {
		return thumbnailBytes;
	}
	public void setThumbnailBytes(byte[] thumbnailBytes) {
		this.thumbnailBytes = thumbnailBytes;
	}
}

