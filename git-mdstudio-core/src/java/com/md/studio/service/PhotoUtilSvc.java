package com.md.studio.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.md.studio.dto.PhotosToProcessDto;
import com.md.studio.dto.UploadPhotoDto;

public interface PhotoUtilSvc {
	
	public String findAndProcessPhotos(String category);
	
	public String FindAndProcessSlidePhotos();
	
	public int countAllPhotosToProcess(String folderType);
	
	public void uploadPhoto(List<MultipartFile> photos, Long photoUploadId);
	
	public void processUploadPhoto(UploadPhotoDto uploadPhotoDto);
	
	public void processPhotos (PhotosToProcessDto photosToProcessDto) throws IOException;
	
	
	// listeners //
	
	//remove all category by type
	// insert new category and files
	public void processPortfolio(String adminUser) throws IOException;
		
	// remove all photos and categories
	// iterate to all directories and files 
	// to insert directory and files
	public void processPreviewPhotos(String adminUser);
	
	// insert new directory and photos in record
	// if directory exist, it will update directory
	// remove photos and insert new files
	public void processPhotoArchives(String adminUser) throws IOException;
	
	
	// async
	public int cleanupPortfolio();
	public int cleanupPreviewPhotos();
	public int cleanupPhotoArchives();
	
	
	// scheduled job
	// clean up records 
	// and delete if doesn't match with the files
	public void cleanupData();
	
	public int getTotalRecordsProcessed();
}
