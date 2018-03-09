package com.md.studio.dao;

import java.util.List;

import com.md.studio.domain.PhotoGatherer;
import com.md.studio.domain.PhotoInfo;
import com.md.studio.dto.Photo;

public interface PhotoGathererDao {
	
	public void insertCategory(PhotoGatherer photoGatherer);
	
	public void insertPhotoInfo(PhotoInfo photoInfo);
	
	public void updateCategory(PhotoGatherer photoGatherer);
	
	public void updatePhotoInfo(PhotoInfo photoInfo);
	
	public void deleteCategory(PhotoGatherer photoGatherer);
	
	public void deletePhotoInfo(PhotoInfo photoInfo);
	
	public void deletePhotoInfoByCategoryId(long categoryId);
	
	public void deleteAllByCategoryType(int categoryType);
	
	public List<String> selectAllCategoryList(int categoryType);
	
	public List<PhotoGatherer> selectAllCategories();
	
	public List<PhotoGatherer> selectAllPhotoCovers(String category);
	
	public List<PhotoGatherer> selectAllByCategory(String category);
	
	public PhotoGatherer selectCategoryById(long categoryId);
	
	public PhotoGatherer selectByDirectory(String category, String directory, int offset, int limit);
	
	public PhotoInfo selectPhotoInfo(long categoryId, String fileName);
	
	public PhotoInfo selectPhotoInfoById(long photoId);
	
	public byte[] selectPhotoInfoByIdByte(long photoId);
	
	public PhotoGatherer selectPhoto(String category, String directory, String fileName);
	
	public List<PhotoGatherer> selectAllByCategoryType(int categoryType);
	
	public List<PhotoGatherer> selectByCategoryType(String category, int categoryType, List<Integer> photoType);
	
	public List<PhotoGatherer> selectByCategoryType(Integer categoryId, List<Integer> photoType);
	
}
