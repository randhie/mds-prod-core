package com.md.studio.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PhotoGatherer implements Serializable {
	private static final long serialVersionUID = 5011990101841918783L;
	public static final int CATEGORY_TYPE_PERMANENT = 0;
	public static final int CATEGORY_TYPE_SLIDESHOW = 1;
	public static final int CATEGORY_TYPE_PORTFOLIO = 2;
	public static final int CATEGORY_TYPE_PREVIEW = 3;
	public static final int CATEGORY_TYPE_CALENDAR = 4;
	
	public static final String CATEGORY_PORTFOLIO = "Portfolio";
	public static final String CATEGORY_PREVIEW = "Preview";
	public static final String CATEGORY_CALENDAR = "Calendar";
	
	private long categoryId = 0;
	private String category;
	private String directory;
	private Date dateCreated;
	private String albumDescription;
	private String albumCaptionName;
	private List<PhotoInfo> photoInfoList;
	private int categoryType = CATEGORY_TYPE_PERMANENT;
	
	public long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getDirectory() {
		return directory;
	}
	public void setDirectory(String directory) {
		this.directory = directory;
	}
	public Date getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
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
	public List<PhotoInfo> getPhotoInfoList() {
		return photoInfoList;
	}
	public void setPhotoInfoList(List<PhotoInfo> photoInfoList) {
		if (this.photoInfoList == null) {
			this.photoInfoList = new ArrayList<PhotoInfo>();
		}
		
		this.photoInfoList = photoInfoList;
	}
	
	public void addPhotoList(List<PhotoInfo> photoInfo) {
		if (this.photoInfoList == null) {
			this.photoInfoList = new ArrayList<PhotoInfo>();
		}
		
		this.photoInfoList.addAll(photoInfo);
	}
	public int getCategoryType() {
		return categoryType;
	}
	public void setCategoryType(int categoryType) {
		this.categoryType = categoryType;
	}
	
	public void addPhotoInfo(PhotoInfo photoInfo) {
		if (photoInfo != null) {
			if (photoInfoList == null) {
				this.photoInfoList = new ArrayList<PhotoInfo>();
			}
			
			photoInfoList.add(photoInfo);
		}
	}
}
