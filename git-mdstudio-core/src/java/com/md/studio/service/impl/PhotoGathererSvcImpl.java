package com.md.studio.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.md.studio.dao.PhotoGathererDao;
import com.md.studio.domain.PhotoGatherer;
import com.md.studio.domain.PhotoInfo;
import com.md.studio.dto.Photo;
import com.md.studio.service.PhotoGathererSvc;
import com.md.studio.service.Refreshable;
import com.md.studio.service.ServiceException;

public class PhotoGathererSvcImpl implements PhotoGathererSvc, Refreshable {
	private static final Logger LOGGER = Logger.getLogger(PhotoGathererSvcImpl.class);
	private static final String C_COVER_UPPERCASE = "Cover";
	private static final String C_COVER_LOWERCASE = "cover";
	private static final String C_BANNER_LOWERCASE = "banner";
	private static final String C_BANNER_UPPERCASE = "BANNER";
	private static final int DEFAULT_OFFSET = 0;
	private static final int DEFAULT_LIMIT = 1000;
	
	private PhotoGathererDao photoGathererDao;
	private String slash;
	private Map<Integer, String> mapOfDirectory;
	private Map<String, PhotoGatherer> photoGathererCache = new HashMap<String, PhotoGatherer>();
	private Map<Integer, List<PhotoGatherer>> photoGathereCacheById = new HashMap<Integer, List<PhotoGatherer>>();
	
	@Override
	public void createPhoto(PhotoGatherer photo) {
		if (photo == null) {
			throw new ServiceException("Photo not found.", "PHOTOGATHER.NOTFOUND");
		}
		
		PhotoGatherer currentPhotoCategory = photoGathererDao.selectByDirectory(photo.getCategory(), photo.getDirectory(), DEFAULT_OFFSET, DEFAULT_LIMIT);
		if (currentPhotoCategory != null) {
			photo.setCategoryId(currentPhotoCategory.getCategoryId());
			updatePhotoCategory(photo);
			removePhotosInDirectory(currentPhotoCategory.getCategoryId());
		}
		else {
			photoGathererDao.insertCategory(photo);	
		}
		
		if  (photo.getPhotoInfoList() == null || photo.getPhotoInfoList().isEmpty()) {
			throw new ServiceException("Nothing to record. Files are not  found.", "FILES.EMPTY");
		}

		
		int ctr = 0;
		String fileAbsolutePath = null;
		ImageIcon image = null;
		for (PhotoInfo photoInfo: photo.getPhotoInfoList()) {
			if (photoInfo == null) {
				return;
			}
			
			if (photoInfo.getFileName().contains("DS_Store")) {
				return;
			}
			
			if (StringUtils.isNotBlank(photoInfo.getFileName())) {
				File file = new File(mapOfDirectory.get(photo.getCategoryType()) + slash + 
						  photo.getCategory() + slash + 
						  photo.getDirectory() + slash + 
						  photoInfo.getFileName());
				
				if (file.exists() && file.isFile()) {
					if (file.getName().contains("DS_Store")) {
						file.delete();
						continue;
					}
					
					if (photo.getDateCreated() == null) {
						Path path = FileSystems.getDefault().getPath(file.getAbsolutePath());
						try {
							BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);
							photo.setDateCreated(new Date(attr.creationTime().toMillis()));
						} catch(IOException e) {
							e.printStackTrace();
						}	
					}
					
					fileAbsolutePath = file.getAbsolutePath();
					if (file.getName().contains(C_COVER_LOWERCASE) || file.getName().contains(C_COVER_UPPERCASE)) {
						photoInfo.setPhotoType(PhotoInfo.COVERPHOTO);
					}
					else {
						image = new ImageIcon(file.getAbsolutePath());
						if (image.getImage().getWidth(null) >=0) {
							if (image.getIconWidth() > image.getIconHeight()) {
								photoInfo.setPhotoType(Photo.LANDSCAPE);
							}
							else {
								photoInfo.setPhotoType(Photo.PORTRAIT);
							}
							
							if (file.getName().contains(C_BANNER_LOWERCASE) || file.getName().contains(C_BANNER_UPPERCASE)) {
								photoInfo.setPhotoType(PhotoInfo.BANNER);
							}
						}
					}
					
					file = null;
					
					PhotoInfo currentPhotoInfo = photoGathererDao.selectPhotoInfo(photo.getCategoryId(), photoInfo.getFileName()); 
					if (currentPhotoInfo == null) {
						if (photo.getCategoryId() == 0) {
							PhotoGatherer photoCategory = photoGathererDao.selectByDirectory(photo.getCategory(), photo.getDirectory(), DEFAULT_OFFSET, 1);
							if (photoCategory != null && photoCategory.getCategoryId() != 0) {
								photoInfo.setCategoryId(photoCategory.getCategoryId());
							}
						}
						else {
							photoInfo.setCategoryId(photo.getCategoryId());	
						}
						
						try {
							photoGathererDao.insertPhotoInfo(photoInfo);
							LOGGER.info("Successfully inserted file: " + fileAbsolutePath);
						}
						catch (Exception e) {
							LOGGER.error("Unable to insert file to record. Filename ==>" + photoInfo.getFileName(), e);
						}
						
						
					}
					else {
						LOGGER.info("Cannot insert file because the file already exist: " + fileAbsolutePath);
					}
					
					ctr++;
				}
				else {
					LOGGER.info("File doesn't exist. Filename: " + photoInfo.getFileName());
				}
			}
			else {
				LOGGER.info("Tried to insert a record, but its empty");
			}
		}
		LOGGER.info("Processed total: " + ctr);
	}

	
	
	
	
	@Override
	public void createPhotoGatherer(PhotoGatherer photo) {
		if (photo == null) {
			throw new ServiceException("Photo Gatherer not found.", "PHOTOGATHERER.NOTFOUND");
		}
		
		PhotoGatherer currentPhotoCategory = photoGathererDao.selectByDirectory(photo.getCategory(), photo.getDirectory(), DEFAULT_OFFSET, DEFAULT_LIMIT);
		if (currentPhotoCategory != null) {
			photo.setCategoryId(currentPhotoCategory.getCategoryId());
			updatePhotoCategory(photo);
		}
		else {
			if (photo.getDateCreated() == null) {
				photo.setDateCreated(new Date());				
			}
			photoGathererDao.insertCategory(photo);	
		}
	}

	@Override
	public void createPhotoInfo(PhotoInfo photoInfo) {
		if (photoInfo == null) {
			throw new ServiceException("Photo Info not found.", "PHOTOINFO.NOTFOUND");
		}
		
		PhotoInfo currentPhotoInfo = photoGathererDao.selectPhotoInfo(photoInfo.getCategoryId(), photoInfo.getFileName()); 
		if (currentPhotoInfo == null) {
			photoGathererDao.insertPhotoInfo(photoInfo);
			LOGGER.info("Successfully inserted file: " + photoInfo.getFileName());
		}
		else {
			currentPhotoInfo.setPhotoType(photoInfo.getPhotoType());
			currentPhotoInfo.setFileBytes(photoInfo.getFileBytes());
			currentPhotoInfo.setThumbnailBytes(photoInfo.getThumbnailBytes());
			photoGathererDao.updatePhotoInfo(currentPhotoInfo);
			LOGGER.info("Cannot insert file because the file already exist: " + photoInfo.getFileName() + ". File Updated!");
		}
	}


	@Override
	public void updatePhotoCategory(PhotoGatherer photo) {
		if (photo == null) {
			throw new ServiceException("Photo not found.", "PHOTOGATHER.NOTFOUND");
		}
		
		PhotoGatherer currentPhotoGatherer = photoGathererDao.selectByDirectory(photo.getCategory(), photo.getDirectory(), DEFAULT_OFFSET, DEFAULT_LIMIT);
		if (currentPhotoGatherer == null) {
			throw new ServiceException("Photo not found. Nothing to update.", "CURRENTPHOTO.NOTFOUND");
		}
		
		photoGathererDao.updateCategory(photo);
		
	}

	@Override
	public void removePhotoCategory(long categoryId) {
		if (categoryId == 0) {
			throw new ServiceException("Photo not found.", "PHOTOGATHER.NOTFOUND");
		}

		PhotoGatherer currentPhoto = photoGathererDao.selectCategoryById(categoryId);
		if (currentPhoto != null) {
			if (currentPhoto.getPhotoInfoList() != null && !currentPhoto.getPhotoInfoList().isEmpty()) {
				for (PhotoInfo photoInfo: currentPhoto.getPhotoInfoList()) {
					photoGathererDao.deletePhotoInfo(photoInfo);
				}	
			}
			photoGathererDao.deleteCategory(currentPhoto);
		}
	}

	@Transactional
	@Override
	public void removeDirectory(String category, String directoryName) {
		if (StringUtils.isBlank(category) || StringUtils.isBlank(directoryName)) {
			throw new ServiceException("Parameter(s) Missing.", "PARAMETERS.REQUIRED");
		}
		
		PhotoGatherer currentPhotoGatherer = photoGathererDao.selectByDirectory(category, directoryName, DEFAULT_OFFSET, DEFAULT_LIMIT);
		if (currentPhotoGatherer != null) {
			photoGathererDao.deletePhotoInfoByCategoryId(currentPhotoGatherer.getCategoryId());
			photoGathererDao.deleteCategory(currentPhotoGatherer);
		}
	}

	@Override
	public void removePhotosInDirectory(long categoryId) {
		if (categoryId == 0) {
			throw new ServiceException("Parameter Missing.", "PARAMETERS.REQUIRED");
		}
		
		photoGathererDao.deletePhotoInfoByCategoryId(categoryId);
	}
	
	
	@Override
	public void removePhoto(PhotoInfo photoInfo) {
		photoGathererDao.deletePhotoInfo(photoInfo);
	}
	
	

	@Override
	public void removeAllByCategoryType(int categoryType) {
		List<PhotoGatherer> photoGathererList = photoGathererDao.selectAllByCategoryType(categoryType);
		
		if (photoGathererList != null && !photoGathererList.isEmpty()) {
			for (PhotoGatherer pg: photoGathererList) {
				photoGathererDao.deletePhotoInfoByCategoryId(pg.getCategoryId());
				photoGathererDao.deleteCategory(pg);
			}
		}
		
	}

	@Override
	public List<String> getAllPhotoCategory(int categoryType) {
		if (StringUtils.isBlank(Integer.toString(categoryType))) {
			throw new ServiceException("Parameter Missing", "CATEGORYTYPE.NOTFOUND");
		}
		return photoGathererDao.selectAllCategoryList(categoryType);
	}

	@Override
	public List<PhotoGatherer> getAllCovers(String category) {
		if (StringUtils.isBlank(category)) {
			throw new ServiceException("Parameter Missing.", "PARAMETERS.REQUIRED");
		}
		return photoGathererDao.selectAllPhotoCovers(category);
	}

	@Override
	public PhotoGatherer getAllPhotos(String category, String directory, int offset, int limit, int page, boolean isUtil) {
		if (StringUtils.isBlank(category) || StringUtils.isBlank(directory)) {
			throw new ServiceException("Parameter(s) Missing.", "PARAMETERS.REQUIRED");
		}
		
		if (StringUtils.isBlank(Integer.toString(offset)) || offset == 0) {
			offset = DEFAULT_OFFSET;
		}
		
		if (StringUtils.isBlank(Integer.toString(limit)) || limit <=1) {
			limit = DEFAULT_LIMIT;
		}
		
		if (isUtil) {
			return photoGathererDao.selectByDirectory(category, directory, offset, limit);
		}
		
		PhotoGatherer photoGatherer;
		if (photoGathererCache.containsKey(category + directory + page)) {
			photoGatherer =  photoGathererCache.get(category + directory + page);
		}
		else {
			photoGatherer = photoGathererDao.selectByDirectory(category, directory, offset, limit);
			photoGathererCache.put(category + directory + page, photoGatherer);
		}
		
		PhotoGatherer pg = new PhotoGatherer();
		pg.setCategory(photoGatherer.getCategory());
		pg.setCategoryId(photoGatherer.getCategoryId());
		pg.setDirectory(photoGatherer.getDirectory());
		pg.setDateCreated(photoGatherer.getDateCreated());
		pg.setAlbumDescription(photoGatherer.getAlbumDescription());
		pg.setAlbumCaptionName(photoGatherer.getAlbumCaptionName());
		pg.addPhotoList(photoGatherer.getPhotoInfoList());
		
		return pg;
	}

	@Override
	public PhotoGatherer getPhoto(String category, String album, String fileName) {
		if (StringUtils.isBlank(category) || StringUtils.isBlank(album) || StringUtils.isBlank(fileName)) {
			throw new ServiceException("Parameter(s) Missing.", "PARAMETERS.REQUIRED");
		}
		
		return photoGathererDao.selectPhoto(category, album, fileName);
	}
	
	
	

	@Override
	public byte[] getPhotoByte(long photoId) {
		if (photoId == 0) {
			throw new ServiceException("Parameter Missing.", "PARAMETERS.REQUIRED");
		}
		
		byte[] b = photoGathererDao.selectPhotoInfoByIdByte(photoId);
		return b;
	}





	@Override
	public PhotoInfo getPhoto(long photoId) {
		if (photoId == 0) {
			throw new ServiceException("Parameter Missing.", "PARAMETERS.REQUIRED");
		}
		
		return photoGathererDao.selectPhotoInfoById(photoId);
	}
	
	
	@Override
	public List<PhotoGatherer> getAll() {
		return photoGathererDao.selectAllCategories();
	}
	

	@Override
	public List<PhotoGatherer> getAllPreviewPhotos() {
		return photoGathererDao.selectAllByCategoryType(PhotoGatherer.CATEGORY_TYPE_PREVIEW);
	}
	
	

	@Override
	public List<PhotoGatherer> getPhotoGatherer(String category, int categoryType, Integer... photoTypes) {
		if (photoTypes == null) {
			throw new ServiceException("Please provide photo type.", "PHOTOTYPE.NOTFOUND");
		}
		
		if (StringUtils.isBlank(Integer.toString(categoryType))) {
			throw new ServiceException("Please provide photo category", "CATEGORYTYPE.NOTFOUND");
		}
		
		List<Integer> photoType = Arrays.asList(photoTypes);
		return photoGathererDao.selectByCategoryType(category, categoryType, photoType);
	}
	
	

	
	@Override
	public List<PhotoGatherer> getPhotoGatherer(Integer categoryId, Integer... photoTypes) {
		if (categoryId == null) {
			throw new ServiceException("Please provide photo type.", "PHOTOTYPE.NOTFOUND");
		}
		
		
		if (photoGathereCacheById.containsKey(categoryId)) {
			return photoGathereCacheById.get(categoryId);
		}

		List<Integer> photoType = Arrays.asList(photoTypes);
		List<PhotoGatherer> photoGathererList = photoGathererDao.selectByCategoryType(categoryId, photoType);
		if (photoGathererList != null && !photoGathererList.isEmpty()) {
			photoGathereCacheById.put(categoryId, photoGathererList);
		}
		
		return photoGathererList;
	}





	@Override
	public void refresh(String arg) {
		if (StringUtils.isBlank(arg)) {
			photoGathererCache.clear();
			photoGathereCacheById.clear();
		}
		else {
			if (photoGathererCache.containsKey(arg)) {
				photoGathererCache.remove(arg);
			}
			else if (photoGathereCacheById.containsKey(arg)) {
				photoGathereCacheById.remove(arg);
			}
		}
		
	}
	

	public void setPhotoGathererDao(PhotoGathererDao photoGathererDao) {
		this.photoGathererDao = photoGathererDao;
	}
	public void setSlash(String slash) {
		this.slash = slash;
	}
	public void setMapOfDirectory(Map<Integer, String> mapOfDirectory) {
		this.mapOfDirectory = mapOfDirectory;
	}
}
