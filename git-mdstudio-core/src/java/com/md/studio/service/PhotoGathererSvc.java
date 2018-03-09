package com.md.studio.service;

import java.util.List;

import com.md.studio.domain.PhotoGatherer;
import com.md.studio.domain.PhotoInfo;

public interface PhotoGathererSvc {

	public void createPhoto(PhotoGatherer photo);
	
	public void createPhotoGatherer(PhotoGatherer photo);
	
	public void createPhotoInfo(PhotoInfo photoInfo);
	
	public void updatePhotoCategory(PhotoGatherer photo);
	
	public void removePhotoCategory(long categoryId);
	
	public void removeDirectory(String category, String directoryName);
	
	public void removePhotosInDirectory(long categoryId);
	
	public void removePhoto(PhotoInfo photoInfo);
	
	public void removeAllByCategoryType(int categoryType);
	
	public List<String> getAllPhotoCategory(int categoryType);
	
	public List<PhotoGatherer> getAllCovers(String category);
	
	public PhotoGatherer getAllPhotos(String category, String directory, int offset, int limit, int page, boolean isUtil);
	
	public PhotoGatherer getPhoto(String category, String album, String fileName);
	
	public PhotoInfo getPhoto(long photoId);
	
	public byte[] getPhotoByte(long photoId);
	
	public List<PhotoGatherer> getAll();
	
	public List<PhotoGatherer> getAllPreviewPhotos();
	
	public List<PhotoGatherer> getPhotoGatherer(String category, int categoryType, Integer...photoTypes);
	
	public List<PhotoGatherer> getPhotoGatherer(Integer categoryId, Integer...photoTypes);
	
//	public void addMostView(PhotoGatherer photoGatherer);
//	
//	public void addRatings(PhotoGatherer photoGather);
	
	
}
