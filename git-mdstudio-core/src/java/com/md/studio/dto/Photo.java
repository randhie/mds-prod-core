package com.md.studio.dto;

import java.io.Serializable;
import java.util.Date;

public class Photo implements Serializable {
	public static final int PORTRAIT = 0;
	public static final int LANDSCAPE = 1;
	private static final String C_SLASH = "/";
	private static final long serialVersionUID = 6485316954483647892L;
	private String directoryName;
	private String fileName;
	private Date dateCreated;
	private String albumDescription;
	private String albumCaptionName;
	private String parentDirectory;
	private int photoType = 0;
	
	public String getDirectoryName() {
		return directoryName;
	}
	public void setDirectoryName(String directoryName) {
		this.directoryName = directoryName;
	}
	public String getFileName() {
		return fileName;
	}
	public Date getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getAbsolutePath() {
		String absolutePath = C_SLASH + getDirectoryName() + C_SLASH + getFileName();
		return absolutePath;
	}
	public String getAlbumDescription() {
		return albumDescription;
	}
	public void setAlbumDescription(String albumDescription) {
		this.albumDescription = albumDescription;
	}
	public String getAlbumCaptionName() {
		return albumCaptionName;
	}
	public void setAlbumCaptionName(String albumCaptionName) {
		this.albumCaptionName = albumCaptionName;
	}
	public String getParentDirectory() {
		return parentDirectory;
	}
	public void setParentDirectory(String parentDirectory) {
		this.parentDirectory = parentDirectory;
	}
	public int getPhotoType() {
		return photoType;
	}
	public void setPhotoType(int photoType) {
		this.photoType = photoType;
	}
}
