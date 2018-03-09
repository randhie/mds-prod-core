package com.md.studio.service;

import java.util.List;
import java.util.Map;

import com.md.studio.dto.Photo;



public interface PhotoInfoSvc {
	
	public List<Photo> getSlidePhotos();
	
	public List<Photo> getPreviewNewPhotos();
	
	public List<Photo> getAllPhotos(String absoluteDirectory);
	
	public List<Photo> getChildDirectoriesByParent(String parentFolder);
	
	public Map<String, List<Photo>> getAllPhotoCategories();
	
	public Map<String,List<Photo>> getAllCoverPhotos();
}
