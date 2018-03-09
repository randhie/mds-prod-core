package com.md.studio.dto;

import java.io.File;
import java.io.Serializable;

public class PhotosToProcessDto implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private File[] directories;
	private String fileTocopy;
	private boolean isSlideShow;
	private String category;
	
	
	public File[] getDirectories() {
		return directories;
	}
	public void setDirectories(File[] directories) {
		this.directories = directories;
	}
	public String getFileTocopy() {
		return fileTocopy;
	}
	public void setFileTocopy(String fileTocopy) {
		this.fileTocopy = fileTocopy;
	}
	public boolean isSlideShow() {
		return isSlideShow;
	}
	public void setSlideShow(boolean isSlideShow) {
		this.isSlideShow = isSlideShow;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
}
