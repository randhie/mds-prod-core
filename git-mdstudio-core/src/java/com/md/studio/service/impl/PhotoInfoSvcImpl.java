package com.md.studio.service.impl;
import static com.md.studio.utils.WebConstants.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.ImageIcon;


import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.md.studio.dto.Photo;
import com.md.studio.service.PhotoInfoSvc;
import com.md.studio.service.Refreshable;
import com.md.studio.service.ServiceException;

public class PhotoInfoSvcImpl implements PhotoInfoSvc, Refreshable {
	private static final Logger LOGGER = Logger.getLogger(PhotoInfoSvcImpl.class);
	private static final String FILENAME_CONTAINS_COVER = "cover";
	private static final String C_SLASH = "/";
	private static final String ALBUM_DESCRIPTION = "album-description";
	private static final String ALBUM_NAMECAPTION = "album-caption";
	
	private String permPhotoLocation;
	private String permSlideShowLocation;
	private String permPreviewLocation;
	private String permSpecialPhotoLocation;

	private Map<String, List<Photo>> cachedAllPhotoCategories = new HashMap<String, List<Photo>>();
	private Map<String, List<Photo>> cachedCoverPhotos = new HashMap<String, List<Photo>>();
	private Map<String, List<Photo>> cachedSpecialPhotos = new HashMap<String, List<Photo>>();
	private List<Photo> cachedSlidePhotos = new ArrayList<Photo>();
	private List<Photo> cachedPreviewNewPhotos = new ArrayList<Photo>();
	private Map<String, List<Photo>> cachedAllPermPhotos = new HashMap<String, List<Photo>>();

	
	private int index = 1;
	private int folders = 0;
	
	@Override
	public Map<String, List<Photo>> getAllPhotoCategories() {
		
		if (!cachedAllPhotoCategories.isEmpty()) { 
			return cachedAllPhotoCategories;
		}
		
		File baseFolder = new File(permPhotoLocation);
		
		if (baseFolder.exists() && baseFolder.isDirectory()) {
			folders = baseFolder.listFiles().length;
			
			Map<String, File> folderMap = getFolder(baseFolder, new HashMap<String, File>());
			
			Map<String, List<Photo>> photoList = new HashMap<String, List<Photo>>(); 
			List<Photo> photos = new ArrayList<Photo>();
			Photo photo = new Photo();
			for (String key: folderMap.keySet()) {
				File file = folderMap.get(key);
				
				int idx = file.getAbsolutePath().lastIndexOf("\\");
				if (idx < 0) {
					idx = file.getAbsolutePath().lastIndexOf("/");
				}
				String parentFolderPath =  file.getAbsolutePath().substring(0, idx);
				String parentFolderName = StringUtils.substringAfterLast(parentFolderPath, "\\");
				
				if (StringUtils.isEmpty(parentFolderName)) {
					parentFolderName = StringUtils.substringAfterLast(parentFolderPath, "/");	
				}
				
				photo.setAlbumCaptionName(StringUtils.replaceChars(file.getName(), "_", " "));
				photo.setDirectoryName(parentFolderName);
				photo.setFileName(file.getName());
				photo.setDateCreated(new Date(file.lastModified()));
				
				photos.add(photo);
				
				if (photoList.containsKey(parentFolderName)) {
					List<Photo> photoFromMap = photoList.get(parentFolderName);
					photoFromMap.add(photo);
				}
				else {
					photoList.put(parentFolderName, photos);	
				}
				
				photos = new ArrayList<Photo>();
				photo = new Photo();
			}
			
			cachedAllPhotoCategories.putAll(photoList);
			return photoList;
		}
		return null;
	}
	
	
	private Map<String, File> getFolder(File baseFolder, Map<String, File> folderMap) {
		int ctr = 1;
		for (File parentFolder: baseFolder.listFiles()) {
			ctr ++;
			if (parentFolder.exists() && parentFolder.isDirectory()) {
				
				if (folderMap.containsKey(parentFolder.getName())) {
					folderMap.remove(parentFolder.getParentFile().getName());
					continue;
				}
				else {
					folderMap.put(parentFolder.getName(), parentFolder);	
				}
				
				if (parentFolder.listFiles().length > 0) {
					getFolder(parentFolder, folderMap);
				}
			}
			else {
				getFolder(baseFolder.getParentFile(), folderMap);
			}
		}
		if (ctr >= baseFolder.listFiles().length) {
			index++;
			if (index <= folders) {
				getFolder(new File(permPhotoLocation), folderMap);	
			}
		}
	return folderMap;
	}
	
	

	@Override
	public List<Photo> getSlidePhotos() {
		File directory = new File(permSlideShowLocation);
		if (cachedSlidePhotos.isEmpty() || cachedSlidePhotos.size() == 0) {
			cachedSlidePhotos.addAll(populatePhotos(directory));
		}
		return cachedSlidePhotos;
	}

	@Override
	public List<Photo> getPreviewNewPhotos() {
		File directory = new File(permPreviewLocation);
		if (cachedPreviewNewPhotos.isEmpty()) {
			cachedPreviewNewPhotos.addAll(populatePhotos(directory));
		}
		return cachedPreviewNewPhotos;
	}
	
	
	private List<Photo> populatePhotos(File directory){
		if (!directory.exists()) {
			throw new ServiceException(ERRORMSG_DIRECTORY, ERRORCODE_DIRECTORY);
		}
		
		List<Photo> photoList = new ArrayList<Photo>();
		if (directory.listFiles().length > 0) {
			for (File file: directory.listFiles()) {
				ImageIcon image = new ImageIcon(file.getAbsolutePath());
				Photo photo = new Photo();
				if (image.getImage().getWidth(null) >=0) {
					photo.setDirectoryName(directory.getName());
					photo.setFileName(file.getName());
					photo.setDateCreated(new Date(file.lastModified()));
					photoList.add(photo);
				}
			}
		}
		return photoList;
	}

	
	
	@Override
	public Map<String, List<Photo>> getAllCoverPhotos() {
		return cachedCoverPhotos;
	}

	private void getAllCoverPhotos(String absolutePath) {
		if (cachedCoverPhotos.containsKey(absolutePath)) {
			return;
		}
		
		File parentDirectory = new File(permPhotoLocation + C_SLASH +  absolutePath);
		
		if (!parentDirectory.exists()) {
			throw new ServiceException(ERRORMSG_DIRECTORY + " - " + parentDirectory.getAbsolutePath(), ERRORCODE_DIRECTORY);
		}
		
		if (parentDirectory.listFiles().length == 0) {
			LOGGER.info("******************* Folder  " + parentDirectory + " is empty!");
		}
		
		List<Photo> photoListToBeCache = new ArrayList<Photo>();
		LOGGER.info("Starting to populate Cover photos to be cache ...");
		String albumCaption;
		String albumDescription;
		
		for (File childDirectory: parentDirectory.listFiles() ) {
			if (childDirectory.exists() && childDirectory.isDirectory() && childDirectory.list().length > 0) {
				Photo photo = new Photo();
				int ctr = 0;
				
				for (File file: childDirectory.listFiles()) {
					if (file.exists() && file.isFile()) {
						ctr++;
						if (file.getName().toLowerCase().contains(FILENAME_CONTAINS_COVER)) {
							photo.setDirectoryName(childDirectory.getName());
							photo.setFileName(file.getName());
							photo.setParentDirectory(absolutePath);
							photo.setDateCreated(new Date(file.lastModified()));
						}
						
						if (file.getName().toLowerCase().contains(ALBUM_DESCRIPTION)) {
							try {
								BufferedReader br = new BufferedReader(new FileReader(file.getAbsolutePath()));
								String desc;
								StringBuilder sb = new StringBuilder();
								while((desc = br.readLine()) != null) {
									sb.append(desc);
								}
								albumDescription = sb.toString();
								br.close();
								photo.setAlbumDescription(albumDescription);
							}
							catch (Exception e) {
								LOGGER.warn(e.getMessage());
							}
						}
						
						
						if (file.getName().toLowerCase().contains(ALBUM_NAMECAPTION)) {
							try {
								BufferedReader br = new BufferedReader(new FileReader(file.getAbsolutePath()));
								StringBuilder sb = new StringBuilder();
								while (br.ready()) {
									sb.append(br.readLine());
								}
								br.close();
								albumCaption = sb.toString();
								photo.setAlbumCaptionName(albumCaption);
							}
							catch (Exception e) {
								LOGGER.warn(e.getMessage());
							}
						}
						
						if (ctr == childDirectory.list().length) {
							photoListToBeCache.add(photo);
						}
					}
				
					// if there are no files within the child folder
					else {
						LOGGER.info("There are no files to process at folder --->"  + childDirectory.getAbsolutePath());
					}
				}
			}

			// if there are no folders inside the base folder
			else {
				LOGGER.info("Cannot process child folder. It could be consist of files or empty directory. --->" + parentDirectory.getAbsolutePath());
			}
		}
		
		cachedCoverPhotos.put(absolutePath, photoListToBeCache);
		LOGGER.info("Process Complete. --- " + cachedCoverPhotos.size() + " Processed");
	}
	
	
	@Override
	public List<Photo> getChildDirectoriesByParent(String parentFolder) {
		if (StringUtils.isBlank(parentFolder)) {
			throw new ServiceException("parentFolder param" + PARAM_MSG_NOTFOUND, PARAM_CODE_NOTFOUND);
		}
		
		if (!cachedCoverPhotos.containsKey(parentFolder)) {
			throw new ServiceException(parentFolder + PARENTDIR_MSG_NOTFOUND, PARENTDIR_CODE_NOTFOUND);
		}
		
		return cachedCoverPhotos.get(parentFolder);
	}


	@Override
	public List<Photo> getAllPhotos(String absoluteDirectory) {
		if (cachedAllPermPhotos.containsKey(absoluteDirectory)) {
			return cachedAllPermPhotos.get(absoluteDirectory);
		}
		
		else if (cachedSpecialPhotos.containsKey(absoluteDirectory)) {
			return cachedSpecialPhotos.get(absoluteDirectory);
		}
		
		List<Photo> photoList = new ArrayList<Photo>();
		assemblePhotos(permPhotoLocation, absoluteDirectory, photoList);
		
		if (!photoList.isEmpty()) { 
			cachedAllPermPhotos.put(absoluteDirectory, photoList);
			return cachedAllPermPhotos.get(absoluteDirectory);	
		}
		else if (photoList.isEmpty()){
			assemblePhotos(permSpecialPhotoLocation, absoluteDirectory, photoList);
			cachedSpecialPhotos.put(absoluteDirectory, photoList);
			return cachedSpecialPhotos.get(absoluteDirectory);
		}
		return cachedAllPermPhotos.get(absoluteDirectory);
	}
	
	
	private void assemblePhotos(String permLocation, String absoluteDirectory, List<Photo> photoList) {
		if (StringUtils.isEmpty(absoluteDirectory)) {
			return;
		}
		
		File directory = new File(permLocation + C_SLASH + absoluteDirectory);
		if (directory.exists()) {
			Photo photo = new Photo();
			String albumDescription = null;
			String albumCaption = null;
			for (File file: directory.listFiles()) {
				if (file.getName().toLowerCase().contains(ALBUM_DESCRIPTION)) {
					try {
						BufferedReader br = new BufferedReader(new FileReader(file.getAbsolutePath()));
						String desc;
						StringBuilder sb = new StringBuilder();
						while ((desc = br.readLine()) != null) {
							sb.append(desc);
						}
						br.close();
						albumDescription = sb.toString();
						
					} catch (FileNotFoundException e) {
						LOGGER.warn(e.getMessage());
					} catch (IOException e) {
						LOGGER.warn(e.getMessage());
					}
				}
				
				if (file.getName().toLowerCase().contains(ALBUM_NAMECAPTION)) {
					try {
						BufferedReader br = new BufferedReader(new FileReader(file.getAbsolutePath()));
						String desc;
						StringBuilder sb = new StringBuilder();
						while ((desc = br.readLine()) != null) {
							sb.append(desc);
						}
						br.close();
						albumCaption =sb.toString();
						
					} catch (FileNotFoundException e) {
						LOGGER.warn(e.getMessage());
					} catch (IOException e) {
						LOGGER.warn(e.getMessage());
					}
				}
				
				ImageIcon image = new ImageIcon(file.getAbsolutePath());
				if (image.getImage().getWidth(null) >=0) {
					photo.setDirectoryName(directory.getName());
					photo.setFileName(file.getName());
					photo.setDateCreated(new Date(file.lastModified()));
					photo.setAlbumCaptionName(albumCaption);
					photo.setAlbumDescription(albumDescription);
					photo.setParentDirectory(absoluteDirectory);
					photoList.add(photo);
					photo = new Photo();	
				}
			}
		} 
		else {
			LOGGER.info("Photo Directory doesn't exist. --- " + permPhotoLocation + C_SLASH + absoluteDirectory);
		}
	}
	
	
	
	
	@SuppressWarnings("unused")
	private void init() throws IOException{
		String[] folderLocations = {permSlideShowLocation, permPreviewLocation, permPhotoLocation, permSlideShowLocation};
		for (String folderLoc: folderLocations) {
			File folder = new File(folderLoc);
			if (folder.exists()) {
				continue;
			}
			throw new ServiceException("Cannot find folder ==== " + folderLoc);
		}
		
		LOGGER.info("Cover Photos Initiated ....");
		getAllPhotoCategories();
		getPreviewNewPhotos();
		getSlidePhotos();
		
		prepareCacheFiles();
		LOGGER.info("Preview Photos: " + cachedPreviewNewPhotos.size());
		LOGGER.info("Slide Photos: " + cachedSlidePhotos.size());
		LOGGER.info("Cover Photos: " + cachedCoverPhotos.size());
	}
	
	private void prepareCacheFiles(){
		Set<String> keys = cachedAllPhotoCategories.keySet();
		for (String key: keys) {
			getAllCoverPhotos(key);
		}
		
		for (String key: keys) {
			for (Photo photo: cachedAllPhotoCategories.get(key)) {
				getAllPhotos(photo.getAbsolutePath());
			}
		}
		
	}
	

	@Override
	public void refresh(String arg) {
		cachedCoverPhotos.clear();
		cachedPreviewNewPhotos.clear();
		cachedSlidePhotos.clear();
		cachedAllPermPhotos.clear();
		cachedAllPhotoCategories.clear();
		cachedSpecialPhotos.clear();
		
		LOGGER.info("Cover photo cached cleared ==>" + cachedCoverPhotos.size());
		LOGGER.info("Preview new photo cached cleared ==> " + cachedPreviewNewPhotos.size());
		LOGGER.info("Slide show photo cached cleared ==> " + cachedSlidePhotos.size());
		LOGGER.info("All permanent photos cached cleared ==>" + cachedAllPermPhotos.size());
		LOGGER.info("All category photos cached cleared ==> " + cachedAllPhotoCategories.size());
		LOGGER.info("=============================================");
		
		getAllPhotoCategories();
		getPreviewNewPhotos();
		getSlidePhotos();
		prepareCacheFiles();
		
		LOGGER.info("=============================================");
		LOGGER.info("Cover photo cached cleared... New cached: " + cachedCoverPhotos.size());
		LOGGER.info("Preview new photo cached cleared... New cached: " + cachedPreviewNewPhotos.size());
		LOGGER.info("Slide show photo cached cleared... New cached: " + cachedSlidePhotos.size());
		LOGGER.info("All permanent photos cached cleared... Result cached: " + cachedAllPermPhotos.size());
		LOGGER.info("All category photos cached cleared... Result cached: " + cachedAllPhotoCategories.size());
	}
	
	public void setPermPhotoLocation(String permPhotoLocation) {
		this.permPhotoLocation = permPhotoLocation;
	}
	public void setPermSlideShowLocation(String permSlideShowLocation) {
		this.permSlideShowLocation = permSlideShowLocation;
	}
	public void setPermPreviewLocation(String permPreviewLocation) {
		this.permPreviewLocation = permPreviewLocation;
	}
	public void setPermSpecialPhotoLocation(String permSpecialPhotoLocation) {
		this.permSpecialPhotoLocation = permSpecialPhotoLocation;
	}
}
