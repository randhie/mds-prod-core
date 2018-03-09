package com.md.studio.service.impl;
import static com.md.studio.utils.WebConstants.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.md.studio.dto.Photo;
import com.md.studio.service.PhotoInfoNewSvc;
import com.md.studio.service.Refreshable;
import com.md.studio.service.ServiceException;

public class PhotoInfoNewSvcImpl implements PhotoInfoNewSvc, Refreshable {
	private static final Logger LOGGER = Logger.getLogger(PhotoInfoSvcImpl.class);
	private static final String FILENAME_CONTAINS_COVER = "cover";
	private static final String C_SLASH = "/";
	private static final String ALBUM_DESCRIPTION = "album-description";
	private static final String ALBUM_NAMECAPTION = "album-caption";
	private static final String TEXT_EXTFILE = "txt";
	
	private String permPhotoLocation;
	private String permSlideShowLocation;
	private String permPreviewLocation;
	private String permSpecialPhotoLocation;
	private String permPortfolio;
	
	private List<Photo> cachedSlidePhotos = new ArrayList<Photo>();
	private List<Photo> cachedPreviewNewPhotos = new ArrayList<Photo>();
	private Map<String, List<Photo>> cachedAllPhotoCategories = new HashMap<String, List<Photo>>();
	private Map<String, List<Photo>> cachedCoverPhotos = new HashMap<String, List<Photo>>();
	private Map<String, List<Photo>> cachedSpecialPhotos = new HashMap<String, List<Photo>>();
	private Map<String, List<Photo>> cachedAllPermPhotos = new HashMap<String, List<Photo>>();
	private List<Photo> cachedAllPortolioCategories = new ArrayList<Photo>();
	private Map<String, List<Photo>> cachedAllPortfolio = new HashMap<String, List<Photo>>();
	
	@Override
	public List<Photo> getSlidePhotos() {
		if (cachedSlidePhotos.isEmpty()) {
			File slideDirectory = new File(permSlideShowLocation);
			
			if (!slideDirectory.exists()) {
				throw new ServiceException(ERRORMSG_DIRECTORY, ERRORCODE_DIRECTORY);
			}
			
			List<Photo> photoList = new ArrayList<Photo>();
			if (slideDirectory.listFiles().length > 0) {
				for (File file: slideDirectory.listFiles()) {
					ImageIcon image = new ImageIcon(file.getAbsolutePath());
					Photo photo = new Photo();
					if (image.getImage().getWidth(null) >=0) {
						photo.setDirectoryName(slideDirectory.getName());
						photo.setFileName(file.getName());
						photoList.add(photo);
					}
				}
			}
			cachedSlidePhotos.addAll(photoList);
		}
		return cachedSlidePhotos;
//		File slideDirectory = new File(permSlideShowLocation);
//		
//		if (!slideDirectory.exists()) {
//			throw new ServiceException(ERRORMSG_DIRECTORY, ERRORCODE_DIRECTORY);
//		}
//		
//		List<Photo> photoList = new ArrayList<Photo>();
//		if (slideDirectory.listFiles().length > 0) {
//			for (File file: slideDirectory.listFiles()) {
//				ImageIcon image = new ImageIcon(file.getAbsolutePath());
//				Photo photo = new Photo();
//				if (image.getImage().getWidth(null) >=0) {
//					photo.setDirectoryName(slideDirectory.getName());
//					photo.setFileName(file.getName());
//					photoList.add(photo);
//				}
//			}
//		}
//		return photoList;
	}

	@Override
	public List<Photo> getPreviewNewPhotos() {
		List<Photo> parentFolderList = new ArrayList<Photo>();
			File baseFolder = new File(permPreviewLocation);
			
			// BASE FOLDER eg: mdstudio-Permanent_PhotoLocation
			if (baseFolder.exists() && baseFolder.isDirectory() && baseFolder.listFiles().length > 0) {

				// PARENT FOLDER eg: Events
				
				for (File parentFolder: baseFolder.listFiles()) {
					if (parentFolder.exists() && parentFolder.isDirectory() && parentFolder.listFiles().length > 0) {
							
						// CHILD FOLDER eg: Aladdin_show
						for (File childFolder: parentFolder.listFiles()) {
							
							if (childFolder.exists() && childFolder.isDirectory() && childFolder.listFiles().length > 0) {
									
								// FILES
								for (File file: childFolder.listFiles()) {
									if (file.exists()) {
										Photo photo = new Photo();
										photo.setDirectoryName(childFolder.getName());
										photo.setParentDirectory(parentFolder.getName());
										photo.setFileName(file.getName());
										
										Path path = FileSystems.getDefault().getPath(file.getAbsolutePath());
										try {
											BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);
											photo.setDateCreated(new Date(attr.creationTime().toMillis()));
										} catch (IOException e) {
											e.printStackTrace();
										}
										
										parentFolderList.add(photo);
									}
								} 
							} 
						}
					} 
				}	
				sortPhotosDescendingOrder(parentFolderList);
			}	
			else {
				throw new ServiceException("Cannot find folders inside the base directory", "FOLDERS.NOTFOUND");
			}
			
			baseFolder = null;
		return parentFolderList;
	}

	@Override
	public Map<String, List<Photo>> getAllPhotoCategories() {
		if (cachedAllPhotoCategories.isEmpty()) {
			File baseFolder = new File(permPhotoLocation);
			
			// BASE FOLDER eg: mdstudio-Permanent_PhotoLocation
			if (baseFolder.exists() && baseFolder.isDirectory()) {
				if (baseFolder.listFiles().length > 0) {
					
					// PARENT FOLDER eg: Events
					for (File parentFolder: baseFolder.listFiles()) {
						List<Photo> parentFolderList = new ArrayList<Photo>();
						
						if (parentFolder.exists() && parentFolder.isDirectory()) {
							if (parentFolder.listFiles().length > 0) {
								
								// CHILD FOLDER eg: Aladdin_show
								for (File childFolder: parentFolder.listFiles()) {
									Photo photo = new Photo();
									if (childFolder.exists() && childFolder.isDirectory()) {
										if (childFolder.listFiles().length > 0) {
											
											// FILES
											int ctr = 0;
											for (File file: childFolder.listFiles()) {
												if (file.exists()) {
													ctr++;
													
													// find album caption text file
													if (file.getName().toLowerCase().contains(ALBUM_NAMECAPTION)) {
														try {
															BufferedReader br = new BufferedReader(new FileReader(file.getAbsolutePath()));
															StringBuilder sb = new StringBuilder();
															while (br.ready()) {
																sb.append(br.readLine());
															}
															br.close();
															photo.setAlbumCaptionName(sb.toString());
															continue;
														}
														catch (Exception e) {
															LOGGER.warn(e.getMessage());
														}
													}
													
													if (file.getName().toLowerCase().contains(FILENAME_CONTAINS_COVER)) {
														Path path = FileSystems.getDefault().getPath(file.getAbsolutePath());
														try {
															BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);
															photo.setDateCreated(new Date(attr.creationTime().toMillis()));
														} catch (IOException e) {
															e.printStackTrace();
														}
													}
													
													if (childFolder.listFiles().length == ctr) {
														photo.setDirectoryName(childFolder.getName());
														photo.setParentDirectory(parentFolder.getName());
														
														parentFolderList.add(photo);
													}
												} // OUT OF IF FILE EXIST
											} // OUT FOR FILES
										} // OUT IF CHILDFOLDER LIST
									} // OUT OF IF CHILDFOLDER EXIST
								} // OUT OF FOR CHILDFOLDER
							} // OUT IF PARENT FOLDER LISTFILE
						} // OUT IF PARENT FOLDER EXIST
						sortPhotosDescendingOrder(parentFolderList);
						cachedAllPhotoCategories.put(parentFolder.getName(), parentFolderList);
					}	// OUT FOR PARENT FOLDER 
				}	// OUT IF BASEFOLDER LIST FILES
				else {
					throw new ServiceException("Cannot find folders inside the base directory", "FOLDERS.NOTFOUND");
				}
			} // OUT IF BASE FOLDER EXIST
			baseFolder = null;
		} // OUT IF CACHED EMPTY
		
		return cachedAllPhotoCategories;
	}

	@Override
	public List<Photo> getAllPhotoCover(String parentDirectory) {
		if (StringUtils.isBlank(parentDirectory)) {
			throw new ServiceException("Parent Folder not found.", "PARENTFOLDER.NOTFOUND");
		}
		
		if (cachedCoverPhotos.containsKey(parentDirectory) && !cachedCoverPhotos.get(parentDirectory).isEmpty()) {
			return cachedCoverPhotos.get(parentDirectory);
		}
		
		List<Photo> childFolderList = new ArrayList<Photo>();
		// PARENT FOLDER
		File parentFolder = new File(permPhotoLocation + C_SLASH + parentDirectory);
		if (parentFolder.exists() && parentFolder.isDirectory() && parentFolder.listFiles().length > 0) {
			
			
			for (File childFolder: parentFolder.listFiles()) {

				// CHILD FOLDER
				if (childFolder.exists() && childFolder.isDirectory() && childFolder.listFiles().length > 0) {
					Photo photo = new Photo();
					int ctr = 0;
					
					for (File file: childFolder.listFiles()) {
						
						// FILES
						if (file.exists() && file.isFile()) {
							ctr++;
							if (file.getName().toLowerCase().contains(FILENAME_CONTAINS_COVER)) {
								photo.setDirectoryName(childFolder.getName());
								photo.setFileName(file.getName());
								photo.setParentDirectory(parentDirectory);
								
								Path path = FileSystems.getDefault().getPath(file.getAbsolutePath());
								try {
									BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);
									photo.setDateCreated(new Date(attr.creationTime().toMillis()));
								} catch (IOException e) {
									e.printStackTrace();
								}
								
								continue;
							}
							else if (file.getName().toLowerCase().contains(ALBUM_NAMECAPTION)) {
								try {
									BufferedReader br = new BufferedReader(new FileReader(file.getAbsolutePath()));
									StringBuilder sb = new StringBuilder();
									while (br.ready()) {
										sb.append(br.readLine());
									}
									br.close();
									photo.setAlbumCaptionName(sb.toString());
									continue;
								}
								catch (Exception e) {
									LOGGER.warn(e.getMessage());
								}
							}
							
							
							if (!StringUtils.isBlank(photo.getFileName()) &&
								!StringUtils.isBlank(photo.getAlbumCaptionName()) && 
								!StringUtils.isBlank(photo.getAlbumDescription())) {
								break;
							}
							else if (childFolder.listFiles().length == ctr) {
								break;
							}
						} // OUT IF FILE EXIST
					} // OUT FOR FILE LOOP
					childFolderList.add(photo);
				}
			}
			sortPhotosDescendingOrder(childFolderList);
			cachedCoverPhotos.put(parentDirectory, childFolderList);
		}
		parentFolder = null;
		return childFolderList;
	}
	
	
	private void sortPhotosDescendingOrder(List<Photo> photos) {
		Photo[] photoArray = new Photo[photos.size()] ;
		for (int x=0; x < photos.size(); x++) {
			photoArray[x] = photos.get(x);
		}
		
		Photo x = null;
		Photo y = null;
		for (int a=0; a < photoArray.length; a++) {
			for (int b=0; b< photoArray.length; b++) {
				if (photoArray[b].getDateCreated().before(photoArray[a].getDateCreated())) {
					x = photoArray[b];
					y = photoArray[a];
					photoArray[b] = y;
					photoArray[a] = x;
				}
			}
		}
		
		photos.clear();
		for (Photo photo: photoArray) {
			photos.add(photo);
		}
		photoArray = null;
	}

	@Override
	public List<Photo> getAllPhotosByDirectory(String parentChildFolder) {
		if (StringUtils.isBlank(parentChildFolder)) {
			throw new ServiceException("Parent Child Folder not found.", "PARENTCHILD_NOTFOUND");
		}
		
		List<Photo> photoList = new ArrayList<Photo>();
		if (cachedAllPermPhotos.containsKey(parentChildFolder) && !cachedAllPermPhotos.get(parentChildFolder).isEmpty()) {
			photoList.clear();
			return cachedAllPermPhotos.get(parentChildFolder);
		}
		else if (cachedSpecialPhotos.containsKey(parentChildFolder) && !cachedSpecialPhotos.get(parentChildFolder).isEmpty()){
			photoList.clear();
			return cachedSpecialPhotos.get(parentChildFolder);
		}
		
		// PERMANENT PHOTO LOCATION
		File childFolder = new File(permPhotoLocation + C_SLASH + parentChildFolder);
		if (childFolder.exists() && childFolder.isDirectory() && childFolder.listFiles().length > 0) {
			assemblePermPhotos(parentChildFolder, childFolder, photoList);
//			photoList.addAll(cachedAllPermPhotos.get(parentChildFolder));
			cachedAllPermPhotos.put(parentChildFolder, photoList);
			return cachedAllPermPhotos.get(parentChildFolder);
		} 
		
		// PERMANENT SPECIAL PHOTO LOCATION IF ANY
		File childSpecialFolder = new File(permSpecialPhotoLocation + C_SLASH + parentChildFolder);
		if (childSpecialFolder.exists()) {
			assemblePermPhotos(parentChildFolder, childSpecialFolder, photoList);
			cachedSpecialPhotos.put(parentChildFolder, photoList);
			return cachedSpecialPhotos.get(parentChildFolder);
			/*photoList.addAll(cachedSpecialPhotos.get(parentChildFolder));*/
		}
	
		return photoList;
	}
	
	
	@Override
	public List<Photo> getAllPortfolio() {
		if (cachedAllPortolioCategories.isEmpty()) {
			File parentDirectory = new File(permPortfolio);
			if (parentDirectory.exists() && parentDirectory.isDirectory()) {
				List<Photo> portfolioList = new ArrayList<Photo>();				
				Photo photo = new Photo();
				for (File childFolder: parentDirectory.listFiles()) {
					if (childFolder.exists() && childFolder.isDirectory()) {
						for (File file: childFolder.listFiles()) {
							if (file.exists() && file.isFile()) {
								if (file.getName().toLowerCase().contains(FILENAME_CONTAINS_COVER)) {
									photo.setDirectoryName(childFolder.getName());
									photo.setFileName(file.getName());
									photo.setParentDirectory(parentDirectory.getName());
									
									Path path = FileSystems.getDefault().getPath(file.getAbsolutePath());
									try {
										BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);
										photo.setDateCreated(new Date(attr.creationTime().toMillis()));
									} catch (IOException e) {
										e.printStackTrace();
									}
									continue;
								}
								else if (file.getName().toLowerCase().contains(ALBUM_NAMECAPTION)) {
									try {
										BufferedReader br = new BufferedReader(new FileReader(file.getAbsolutePath()));
										StringBuilder sb = new StringBuilder();
										while (br.ready()) {
											sb.append(br.readLine());
										}
										br.close();
										photo.setAlbumCaptionName(sb.toString());
										continue;
									}
									catch (Exception e) {
										LOGGER.warn(e.getMessage());
									}
								}
							}
						}
						portfolioList.add(photo);
						photo = new Photo();
					}
				}
				cachedAllPortolioCategories.addAll(portfolioList);
				return cachedAllPortolioCategories;
			}
		}
		return cachedAllPortolioCategories;
	}

	
	@Override
	public List<Photo> getPortfolioByDirectory(String parentChildFolder) {
		if (StringUtils.isBlank(parentChildFolder)) {
			throw new ServiceException("Parent Child Folder not found.", "PARENTCHILD_NOTFOUND");
		}
		
		if (cachedAllPortfolio.containsKey(parentChildFolder) && !cachedAllPortfolio.get(parentChildFolder).isEmpty()) {
			return cachedAllPortfolio.get(parentChildFolder);
		}
		
		List<Photo> photoList = new ArrayList<Photo>();
		
		File childFolder = new File(permPortfolio + C_SLASH + parentChildFolder);
		if (childFolder.exists() && childFolder.isDirectory() && childFolder.listFiles().length > 0) {
			assemblePermPhotos(parentChildFolder, childFolder, photoList);
			/*sortPhotosDescendingOrder(photoList);*/
			cachedAllPortfolio.put(parentChildFolder, photoList);
		} 
		return photoList;
		
	}

	private void assemblePermPhotos(String parentChildFolder, File childFolder, List<Photo> photoList) {
		/*List<Photo> photoList = new ArrayList<Photo>();*/
		
		List<Photo> landScapePhotos = new ArrayList<Photo>();
		List<Photo> portraitPhotos = new ArrayList<Photo>();
		
		for (File file: childFolder.listFiles()) {
			Photo photo = new Photo();
			photo.setDirectoryName(childFolder.getName());
			photo.setFileName(file.getName());
			photo.setParentDirectory(parentChildFolder);
			
			Path path = FileSystems.getDefault().getPath(file.getAbsolutePath());
			try {
				BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);
				photo.setDateCreated(new Date(attr.creationTime().toMillis()));
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			if (!FilenameUtils.getExtension(file.getName()).contains(TEXT_EXTFILE)) {
				ImageIcon image = new ImageIcon(file.getAbsolutePath());
				if (image.getImage().getWidth(null) >=0) {
					if (image.getIconWidth() > image.getIconHeight()) {
						photo.setPhotoType(Photo.LANDSCAPE);
					}
					else {
						photo.setPhotoType(Photo.PORTRAIT);
					}
				}
			}
			
			if (file.getName().toLowerCase().contains(ALBUM_DESCRIPTION)) {
				try {
					BufferedReader br = new BufferedReader(new FileReader(file.getAbsolutePath()));
					String desc;
					StringBuilder sb = new StringBuilder();
					while((desc = br.readLine()) != null) {
						sb.append(desc);
					}
					br.close();
					photo.setAlbumDescription(sb.toString());
					photoList.add(photo);
					continue;
				}
				catch (Exception e) {
					LOGGER.warn(e.getMessage());
				}
			}
			else if (file.getName().toLowerCase().contains(ALBUM_NAMECAPTION)) {
				try {
					BufferedReader br = new BufferedReader(new FileReader(file.getAbsolutePath()));
					StringBuilder sb = new StringBuilder();
					while (br.ready()) {
						sb.append(br.readLine());
					}
					br.close();
					photo.setAlbumCaptionName(sb.toString());
					photoList.add(photo);
					continue;
				}
				catch (Exception e) {
					LOGGER.warn(e.getMessage());
				}
			}
			
			if (Photo.LANDSCAPE == photo.getPhotoType()) {
				landScapePhotos.add(photo);
			}
			else {
				portraitPhotos.add(photo);
			}
			
			/*photoList.add(photo);*/
		
		} // END FOR FILE
		
		/*if (isSpecialFolder) {
			cachedSpecialPhotos.put(parentChildFolder, photoList);
			return;
		}
		cachedAllPermPhotos.put(parentChildFolder, photoList);*/
		
		photoList.addAll(landScapePhotos);
		photoList.addAll(portraitPhotos);
		
		landScapePhotos.clear();
		portraitPhotos.clear();
	}
	
	@SuppressWarnings("unused")
	private void init() throws IOException{
		String[] folderLocations = {permSlideShowLocation, permPreviewLocation, permPhotoLocation, permSlideShowLocation, permSpecialPhotoLocation, permPortfolio};
		for (String folderLoc: folderLocations) {
			File folder = new File(folderLoc);
			if (folder.exists()) {
				continue;
			}
			throw new ServiceException("Cannot find folder ==== " + folderLoc);
		}
	}
	
	
	@Override
	public void refresh(String arg) {
	 // MAPS
	 cachedAllPhotoCategories.clear();
	 cachedCoverPhotos.clear();
	 cachedSpecialPhotos.clear();
     cachedAllPermPhotos.clear();
     cachedAllPortfolio.clear();
     
     // LIST
     cachedSlidePhotos.clear();
 	 cachedPreviewNewPhotos.clear();
 	 cachedAllPortolioCategories.clear();
		
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
	public void setPermPortfolio(String permPortfolio) {
		this.permPortfolio = permPortfolio;
	}
}
