package com.md.studio.service.impl;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.md.studio.dao.PhotoUploadLogDao;
import com.md.studio.domain.PhotoGatherer;
import com.md.studio.domain.PhotoInfo;
import com.md.studio.domain.PhotoUploadDirectory;
import com.md.studio.domain.PhotoUploadLog;
import com.md.studio.domain.UserContext;
import com.md.studio.dto.Photo;
import com.md.studio.dto.PhotosToProcessDto;
import com.md.studio.dto.UploadPhotoDto;
import com.md.studio.service.PhotoGathererSvc;
import com.md.studio.service.PhotoUploadDirSvc;
import com.md.studio.service.PhotoUtilSvc;
import com.md.studio.service.RefreshManagerSvc;
import com.md.studio.service.ServiceException;
import com.md.studio.utils.FileTimeCreation;
import com.md.studio.utils.JmsMessageSender;
import com.md.studio.utils.PhotoShrinkUtil;

public class PhotoUtilSvcImpl implements PhotoUtilSvc {
	private static final Logger LOGGER = Logger.getLogger(PhotoUtilSvcImpl.class);
	private static final String MSG_RETURN_FOLDER_NOTFOUND = "Cannot find directory to read files.";
	private static final String MSG_RETURN_EMPTYFILES = "Empty Directory found. Nothing to process";
	private static final String MSG_RETURN_SUCCESS = "Total number of Image processed:";
	private static final String PARAM_CONTAINS_SLIDESHOW = "slideShow";
	private static final String PARAM_CONTAINS_TEMPPHOTO = "tempPhoto";
	private static final String PARAM_CONTAINS_ALBUM = "album";
	private static final String PARAM_CONTAINS_ALBUM_CAPTION = "album-caption";
	private static final String PARAM_CONTAINS_ALBUM_DESCRIPTION = "album-description";
	private static final String PARAM_CONTAINS_COVER_PHOTO = "cover";
	private static final String PHOTO_FORMAT = "jpg";
	private static final String C_SLASH = "/";
	private static final String REFRESH_PHOTOINFOSVC = "photoInfoNewSvc";
	private static final String REFRESH_PHOTOGATHERSVC = "photoGatherSvc";
	private static final String PARAM_YEAR = "Year";
	private static final String REGEX_ALLNUMBERS = "[0-9]+";
	private static final String PHOTOGATHERERSVC = "photoGathererSvc";
	private String tempPhotoLocation;
	private String permPhotoLocation;
	private String tempSlideShowLocation;
	private String permSlideShowLocation;
	private String permUserPhotoUploadLocation;
	private String permCalendarPhotoLocation;
	private String portfolioLocation;
	private String previewLocation;
	private String destinationQueueUpload;
	private String destinationQueueCleanupPortfolio;
	private String destinationQueueCleanupPreviewPhoto;
	private String destinationQueueCleanupPhotoArchive;
	private int imageSize;
	private int thumbnailSize;
	private boolean isThumbnailNeeded;
	private boolean isShrinkNeeded = false;
	private JmsMessageSender jmsMessageSender;
	private String destinationQueue;
	private RefreshManagerSvc refreshManagerSvc;
	private PhotoUploadDirSvc photoUploadDirSvc;
	private PhotoUploadLogDao photoUploadLogDao;
	private PhotoGathererSvc photoGathererSvc;
	private int recordPhotoSize = 0;
	private boolean scannedAllowed;
	
	@Override
	public String findAndProcessPhotos(String category) {
		File currentFile = new File(tempPhotoLocation);
		if (currentFile.exists()) {
			File[] directoryList = currentFile.listFiles();
			
			if (directoryList.length == 0) {
				return MSG_RETURN_EMPTYFILES + ":0";
			}
			
			int count = 0;
			for (File directoryToProcess: directoryList) {
				
				if (directoryToProcess.getName().contains("DS_Store")) {
					directoryToProcess.delete();
					continue;
				}
				
				if (directoryToProcess.isDirectory()) {
					for (File file: directoryToProcess.listFiles()) {
						if (file.getName().contains("DS_Store")){
							file.delete();
							continue;
						}
						
						if (file.isFile()) {
							count++;
						}
					}
				}
			}
			
			if (directoryList.length == 0) {
				return MSG_RETURN_EMPTYFILES + ":0";
			}
			
			PhotosToProcessDto photos = new PhotosToProcessDto();
			photos.setDirectories(directoryList);
			photos.setFileTocopy(permPhotoLocation);
			photos.setSlideShow(false);
			photos.setCategory(category);
			
			if (category.length() <= 10 && category.contains(PARAM_YEAR)) {
				if (category.substring(category.length() - 4).matches(REGEX_ALLNUMBERS)) {
					photos.setFileTocopy(permCalendarPhotoLocation);
				}
			}
			
			jmsMessageSender.sendToQueue(destinationQueue, photos);
			/*int count = processPhotos(directoryList, permPhotoLocation, false, category);*/
			return MSG_RETURN_SUCCESS + count;
		}
		else {
			return MSG_RETURN_FOLDER_NOTFOUND + " ====> " + tempPhotoLocation + ":0";
		}
	}

	@Deprecated
	@Override
	public String FindAndProcessSlidePhotos() {
		File currentFile = new File(tempSlideShowLocation);
		if (currentFile.exists()) {
			File[] directoryList = currentFile.listFiles();
			
			if (directoryList.length == 0) {
				return MSG_RETURN_EMPTYFILES;
			}
			int count = 0;
			for (File directoryToProcess: directoryList) {
				
				if (directoryToProcess.getName().contains("DS_Store")) {
					directoryToProcess.delete();
					continue;
				}
				
				if (directoryToProcess.isDirectory()) {
					for (File file: directoryToProcess.listFiles()) {
						
						if (file.getName().contains("DS_Store")){
							file.delete();
							continue;
						}
						
						if (file.isFile()) {
							count++;
						}
					}
				}
			}
			
			if (directoryList.length == 0) {
				return MSG_RETURN_EMPTYFILES + ":0";
			}
			
			PhotosToProcessDto photos = new PhotosToProcessDto();
			photos.setDirectories(directoryList);
			photos.setFileTocopy(permSlideShowLocation);
			photos.setSlideShow(true);
			photos.setCategory(null);
			jmsMessageSender.sendToQueue(destinationQueue, photos);
			/*int count = processPhotos(directoryList, permSlideShowLocation, true, null);*/
			return MSG_RETURN_SUCCESS + count;
		}
		else {
			return MSG_RETURN_FOLDER_NOTFOUND + " ====> " + tempSlideShowLocation + ":0";
		}
	}
	
	
	@Override
	public int countAllPhotosToProcess(String folderType) {
		int count = 0;
		
		if (StringUtils.isBlank(folderType)) {
			throw new ServiceException("Folder type was not provided");
		}
		File parentDirectory = null;
		if (folderType.contains(PARAM_CONTAINS_SLIDESHOW)) {
			parentDirectory = new File(tempSlideShowLocation);	
		}
		else if (folderType.contains(PARAM_CONTAINS_TEMPPHOTO)) {
			parentDirectory = new File(tempPhotoLocation);	
		}
		
		if (parentDirectory.exists()) {
			File[] folders = parentDirectory.listFiles();

			for (File directory: folders) {
				File childDirectory = directory.getAbsoluteFile();
				if (childDirectory.exists() && childDirectory.isDirectory()) {
					for (File file: childDirectory.listFiles()) {
						if (file.exists() && file.isFile()) {
							count++;
						}
					}
				}
			}
		}
		return count;
	}


	public void processPhotos (PhotosToProcessDto photosToProcessDto) throws IOException  {
		int countFiles = 0;
		for (File directory: photosToProcessDto.getDirectories()) {
			if (directory.toString().contains("DS_Store")) {
				directory.delete();
				continue;
			}
			
			if (directory.isDirectory()) {
				File fileDirectory = new File(directory.getAbsolutePath());
				
				File[] filesFromDirectory = fileDirectory.listFiles();
				countFiles += filesFromDirectory.length;
				
				File createDirectory = null;
				if (photosToProcessDto.getCategory() == null) {
					createDirectory = new File(photosToProcessDto.getFileTocopy() + C_SLASH + directory.getName());
				}
				else {
					createDirectory = new File(photosToProcessDto.getFileTocopy() + C_SLASH + photosToProcessDto.getCategory() + C_SLASH + directory.getName());
				}
				
				if (createDirectory.exists()) {
					shrinkAndTransfer(filesFromDirectory, createDirectory, directory);
				}
				else {
					if (createDirectory.mkdirs()) {
						shrinkAndTransfer(filesFromDirectory, createDirectory, directory);
					}
					else {
						LOGGER.info("Cannot create directory. It might be exist.");
					}
				}
				directory.delete();
			}
		}
		
		if (photosToProcessDto.isSlideShow()) {
			File destFile = new File(permSlideShowLocation);
			for (File directory: destFile.listFiles()) {
				if (directory.isFile()) {
					directory.delete();
				}
			}
			
			for (File directory: destFile.listFiles()) {
				if (directory.isDirectory()) {
					for (File file: directory.listFiles()) {
						try {
							File fileToTransfer = new File(permSlideShowLocation + "/" + file.getName());
							FileUtils.copyFile(file, fileToTransfer);
							file.delete();
						} catch (IOException e) {
							LOGGER.error(e.getMessage());
						}
					}
					directory.delete();
				}
			}
			
		}
		refreshManagerSvc.processRefreshRequest(REFRESH_PHOTOINFOSVC);
		LOGGER.info("Total number of Image Processed: " + countFiles);
	}
	

	@Override
	public void uploadPhoto(List<MultipartFile> photos, Long photoUploadId) {
		if (photos == null || photos.size() == 0) {
			throw new ServiceException("Image not found.", "IMAGE.MISSING");
		}
		else if (photoUploadId == null) {
			throw new ServiceException("Cannot find any valid photo upload ID", "UPLOADID.NOTFOUND");
		}
		
		PhotoUploadDirectory photoUploadDirectory = photoUploadDirSvc.getPhotoUploadDir(photoUploadId);
		if (photoUploadDirectory == null) {
			throw new ServiceException("Cannot find any valid photo upload ID", "UPLOADID.NOTFOUND");
		}
		else if (photoUploadDirectory != null && !photoUploadDirectory.isStillValid()) {
			throw new ServiceException("Upload session expired. Cannot able to upload. Please Contact support.", "UPLOAD.EXPIRED");
		}

		List<File> images = new ArrayList<File>();
		for (MultipartFile mpf: photos) {
			long sizeInMb = mpf.getSize() / (1024 * 1024);
			if (sizeInMb > 1048576.000000275) {
				throw new ServiceException("File is too big. Please upload file less than 1TB. File size uploaded is " + sizeInMb + "MB", "FILE.SIZENOTALLOWED");
			}
			
			try {
				mpf.transferTo(new File(permUserPhotoUploadLocation + C_SLASH + mpf.getOriginalFilename()));	
			}
			catch(IOException io) {
				LOGGER.error(io.getStackTrace());
			}
			
			ImageIcon image = new ImageIcon(permUserPhotoUploadLocation + C_SLASH + mpf.getOriginalFilename());
			if (image.getImage().getWidth(null) > 0) {
				images.add(new File(permUserPhotoUploadLocation + C_SLASH + mpf.getOriginalFilename()));
				continue;	
			}
			else {
				File fileToDelete = new File(permUserPhotoUploadLocation + C_SLASH + mpf.getOriginalFilename());
				if (fileToDelete.exists() && fileToDelete.isFile()) {
					fileToDelete.delete();
				}
				throw new ServiceException("Image not found.", "IMAGE.MISSING");					
			}
		}
		
		UploadPhotoDto uploadPhotoDto = new UploadPhotoDto();
		uploadPhotoDto.setImages(images);
		uploadPhotoDto.setPhotoUploadDirectory(photoUploadDirectory);
		uploadPhotoDto.setUserid(UserContext.getUserId());
		
		jmsMessageSender.sendToQueue(destinationQueueUpload, uploadPhotoDto);
	}
	
	@Transactional
	@Override
	public void processUploadPhoto(UploadPhotoDto uploadPhotoDto) {
		List<File> photos = uploadPhotoDto.getImages();
		PhotoUploadDirectory photoUploadDir = uploadPhotoDto.getPhotoUploadDirectory();

		Date currentDate = new Date();
		for (File mpf: photos) {
			try {
				if (mpf.exists() && mpf.isFile()) {
					File fileToShrink = new File("J:\\" + photoUploadDir.getBaseFolder() +
							"\\" + photoUploadDir.getParentFolder() +
							"\\" + photoUploadDir.getChildFolder() + 
							"\\" + mpf.getName());
					
					FileUtils.copyFile(mpf, fileToShrink, true);
					if (fileToShrink.exists() && fileToShrink.isFile()) {
						PhotoShrinkUtil.shrinkPhoto(fileToShrink, false);
						
						PhotoUploadLog uploadLog = new PhotoUploadLog();
						uploadLog.setBaseFolder(photoUploadDir.getBaseFolder());
						uploadLog.setParentFolder(photoUploadDir.getParentFolder());
						uploadLog.setChildFolder(photoUploadDir.getChildFolder());
						uploadLog.setFileName(mpf.getName());
						uploadLog.setPhotoUploadDirId(photoUploadDir.getUploadId());
						uploadLog.setUploadDate(currentDate);
						uploadLog.setUserId(uploadPhotoDto.getUserid());
						photoUploadLogDao.insert(uploadLog);
						
						PhotoGatherer pg = photoGathererSvc.getAllPhotos(photoUploadDir.getParentFolder(), photoUploadDir.getChildFolder(), 0, 1, 0, true);
						if (pg != null) {
							PhotoGatherer currentPg = photoGathererSvc.getPhoto(photoUploadDir.getParentFolder(), photoUploadDir.getChildFolder(), mpf.getName());
							if (currentPg != null) {
								continue;
							}
							
							PhotoInfo pi = new PhotoInfo();
							pg.setPhotoInfoList(null);
							pi.setCategoryId(pg.getCategoryId());
							pi.setFileName(mpf.getName());
							pi.setPhotoType(PhotoGatherer.CATEGORY_TYPE_PERMANENT);
							pg.addPhotoInfo(pi);
							
							
							ByteArrayOutputStream baos = new ByteArrayOutputStream();
							BufferedImage bi;
							try {
								bi = ImageIO.read(mpf.getAbsoluteFile());
								ImageIO.write(bi, "jpg", baos);
								byte[] imageInByte = baos.toByteArray();
								pi.setFileBytes(imageInByte);
								
								baos.flush();
								baos.close();
								
								
								// thumbnail
								baos = new ByteArrayOutputStream();
								bi = PhotoShrinkUtil.shrinkImage(mpf, true);
								ImageIO.write(bi, PhotoShrinkUtil.PHOTO_FORMAT, baos);
								imageInByte = baos.toByteArray();
								pi.setThumbnailBytes(imageInByte);
								
								baos.flush();
								baos.close();
								bi.flush();
								
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							photoGathererSvc.createPhotoInfo(pi);
						}
						
					}
				}
				
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		refreshManagerSvc.processRefreshRequest(REFRESH_PHOTOGATHERSVC);
		refreshManagerSvc.processRefreshRequest(REFRESH_PHOTOINFOSVC);
	}


	private void shrinkAndTransfer(File[] filesFromDirectory, File createDirectory, File fileToProcess) throws IOException{
		int imageSizeSetting = imageSize;
		int imageSizeThumbnail = thumbnailSize;
		
		PhotoGatherer photoGatherer = new PhotoGatherer();
		PhotoInfo photoInfo = null;
		for (File fileFromDir: filesFromDirectory) {
			if (fileFromDir.toString().contains("DS_Store")) {
				fileFromDir.delete();
				continue;
			}
			
			if (fileFromDir.getName().contains(PARAM_CONTAINS_ALBUM)){
				
				BufferedReader br = new BufferedReader(new FileReader(fileFromDir.getAbsolutePath()));
				StringBuilder sb = new StringBuilder();
				while (br.ready()) {
					sb.append(br.readLine());
				}
				br.close();
				
				if (fileFromDir.getName().contains("caption")) {
					photoGatherer.setAlbumCaptionName(sb.toString());
					photoGatherer.setDateCreated(FileTimeCreation.getFileTimeCreation(fileFromDir));
				}
				else if (fileFromDir.getName().contains("description")) {
					photoGatherer.setAlbumDescription(sb.toString());
				}
				
				FileUtils.copyFile(fileFromDir, new File(createDirectory + C_SLASH + fileFromDir.getName()));
				fileFromDir.delete();
				continue;
			}
			
			ImageIcon image = new ImageIcon(fileFromDir.getAbsolutePath());
			BufferedImage resizedImage = null;
			
			if (image.getImage().getWidth(null) >= 0) {
				try {
					BufferedImage originalImage = ImageIO.read(fileFromDir);
					String formatedFileName = null;
					String fileName = null;
					fileName = fileFromDir.getName();
					formatedFileName = fileName.replaceAll(" ", "_");
					
					if (isShrinkNeeded) {
						resizedImage = Scalr.resize(originalImage, Method.ULTRA_QUALITY, imageSize);
						ImageIO.write(resizedImage, PHOTO_FORMAT, new File(createDirectory + C_SLASH + formatedFileName));
						resizedImage.flush();
					}
					else {
						ImageIO.write(originalImage, PHOTO_FORMAT, new File(createDirectory + C_SLASH + formatedFileName));
					}

					imageSize = imageSizeSetting;
					if (StringUtils.isBlank(photoGatherer.getCategory())) {
						photoGatherer.setCategory(createDirectory.getParentFile().getName());
						photoGatherer.setDirectory(fileFromDir.getParentFile().getName());
					}
					
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					ImageIO.write(resizedImage, "jpg", baos);
					byte[] imageInByte = baos.toByteArray();
					
					photoInfo = new PhotoInfo();
					photoInfo.setFileName(formatedFileName);
					photoInfo.setFileBytes(imageInByte);
					
					baos.flush();
					baos.close();
					originalImage.flush();
					
					// thumbnail
					baos = new ByteArrayOutputStream();
					resizedImage = PhotoShrinkUtil.shrinkImage(fileFromDir, true);
					ImageIO.write(resizedImage, PhotoShrinkUtil.PHOTO_FORMAT, baos);
					imageInByte = baos.toByteArray();
					photoInfo.setThumbnailBytes(imageInByte);
					
					baos.flush();
					baos.close();
					originalImage.flush();
					resizedImage.flush();
					
					photoGatherer.addPhotoInfo(photoInfo);
					if (isThumbnailNeeded) {
						BufferedImage thumbnailImage = Scalr.resize(originalImage,  Method.ULTRA_QUALITY, imageSizeThumbnail);
						String prefixFileName = FilenameUtils.removeExtension(formatedFileName);
						String extensionFileName = FilenameUtils.getExtension(fileName);
						ImageIO.write(thumbnailImage, PHOTO_FORMAT, new File(createDirectory + C_SLASH + prefixFileName + "_thumbnail." + extensionFileName));
					}
					
					LOGGER.info("Processed Image: " + fileFromDir.getAbsolutePath());
					fileFromDir.delete();
				}
				catch (IOException io){
					LOGGER.error(io.getMessage());
				}
			}
		}
		
		if (createDirectory.toString().contains("CalendarLocation")) {
			photoGatherer.setCategoryType(PhotoGatherer.CATEGORY_TYPE_CALENDAR);
		}
		
		if (photoGatherer.getPhotoInfoList() != null && !photoGatherer.getPhotoInfoList().isEmpty()) {
			photoGathererSvc.createPhoto(photoGatherer);
		}
	}
	
	
	
	@Transactional(timeout = 1200)
	@Override
	public void processPortfolio(String adminUser) throws IOException {
		photoGathererSvc.removeAllByCategoryType(PhotoGatherer.CATEGORY_TYPE_PORTFOLIO);
		
		File parentDirectory = new File(portfolioLocation);
		if (parentDirectory.exists() && parentDirectory.isDirectory()) {
			if (StringUtils.isNotBlank(adminUser)) {
				LOGGER.info("================= Album will start processing by " + adminUser);
			}
			else {
				LOGGER.info("================= Album will start processing by the scheduled job");
			}
			
			for (File albums: parentDirectory.listFiles()) {
				if (albums.exists() && albums.isDirectory()) {
					LOGGER.info("================= Start processing album: " + albums.getAbsolutePath());
					PhotoGatherer photoGatherer = new PhotoGatherer();
					photoGatherer.setCategory(PhotoGatherer.CATEGORY_PORTFOLIO);
					photoGatherer.setCategoryType(PhotoGatherer.CATEGORY_TYPE_PORTFOLIO);
					
					int ctr = 0;
					for (File files: albums.listFiles()) {
						if (files.exists() && files.isFile()) {
							recordPhotoSize--;
							if (files.getName().contains("DS_Store")) {
								files.delete();
								continue;
							}
							
							photoGatherer.setDirectory(albums.getName());
							ctr++;
							if (files.getName().contains(PARAM_CONTAINS_ALBUM_CAPTION)) {
								BufferedReader br = new BufferedReader(new FileReader(files.getAbsolutePath()));
								StringBuilder sb = new StringBuilder();
								while (br.ready()) {
									sb.append(br.readLine());
								}
								br.close();
								photoGatherer.setAlbumCaptionName(sb.toString());
								photoGatherer.setDateCreated(FileTimeCreation.getFileTimeCreation(files));
								photoGathererSvc.createPhotoGatherer(photoGatherer);
								continue;
							}
							else if (files.getName().contains(PARAM_CONTAINS_ALBUM_DESCRIPTION)) {
								BufferedReader br = new BufferedReader(new FileReader(files.getAbsolutePath()));
								StringBuilder sb = new StringBuilder();
								while (br.ready()) {
									sb.append(br.readLine());
								}
								br.close();
								photoGatherer.setAlbumDescription(sb.toString());
								photoGathererSvc.createPhotoGatherer(photoGatherer);
								continue;
							}
							
							if (StringUtils.isBlank(photoGatherer.getDirectory())) {
								photoGatherer.setCategory(PhotoGatherer.CATEGORY_PORTFOLIO);
								photoGatherer.setCategoryType(PhotoGatherer.CATEGORY_TYPE_PORTFOLIO);
								photoGatherer.setDirectory(albums.getName());
								photoGathererSvc.createPhotoGatherer(photoGatherer);
							}
							
							ImageIcon image = new ImageIcon(files.getAbsolutePath());
							if (image.getImage().getWidth(null) >= 0) {
								PhotoInfo photoInfo = new PhotoInfo();
								
								if (StringUtils.isBlank(Long.toString(photoGatherer.getCategoryId())) || photoGatherer.getCategoryId() == 0) {
									photoGathererSvc.createPhotoGatherer(photoGatherer);
								}
								
								photoInfo.setCategoryId(photoGatherer.getCategoryId());
								photoInfo.setFileName(files.getName());
								
								if (image.getIconWidth() > image.getIconHeight()) {
									photoInfo.setPhotoType(PhotoInfo.LANDSCAPE);
								}
								else {
									photoInfo.setPhotoType(PhotoInfo.PORTRAIT);
								}
								
								if (files.getName().contains(PARAM_CONTAINS_COVER_PHOTO)) {
									photoInfo.setPhotoType(PhotoInfo.COVERPHOTO);
								}
								
								ByteArrayOutputStream baos = new ByteArrayOutputStream();
								BufferedImage bi;
								try {
									bi = ImageIO.read(files.getAbsoluteFile());
									ImageIO.write(bi, "jpg", baos);
									byte[] imageInByte = baos.toByteArray();
									photoInfo.setFileBytes(imageInByte);
									
									baos.flush();
									baos.close();
									
									
									// thumbnail
									baos = new ByteArrayOutputStream();
									bi = PhotoShrinkUtil.shrinkImage(files, true);
									ImageIO.write(bi, PhotoShrinkUtil.PHOTO_FORMAT, baos);
									imageInByte = baos.toByteArray();
									photoInfo.setThumbnailBytes(imageInByte);
									
									baos.flush();
									baos.close();
									bi.flush();
									
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
								photoGathererSvc.createPhotoInfo(photoInfo);
							}
							
							LOGGER.info("================= Processed Image: " + files.getAbsolutePath());
						}
						LOGGER.info("================= Total processed: " + ctr);
					}
				}
			}
			refreshPhotoGatherer();
			recordPhotoSize = 0;
		}
		
	}

	
	@Transactional
	@Override
	public void processPreviewPhotos(String adminUser) {
		photoGathererSvc.removeAllByCategoryType(PhotoGatherer.CATEGORY_TYPE_PREVIEW);
		
		File parentDirectory = new File(previewLocation);
		if (parentDirectory.exists() && parentDirectory.isDirectory()) {
			if (StringUtils.isNotBlank(adminUser)) {
				LOGGER.info("================= Album will start processing by " + adminUser);
			}
			else {
				LOGGER.info("================= Album will start processing by the scheduled job");
			}
			
			for (File category: parentDirectory.listFiles()) {
				if (category.exists() && category.isDirectory()) {
					for (File directory: category.listFiles()) {
						if (directory.exists() && directory.isDirectory()) {
							PhotoGatherer photoGatherer = new PhotoGatherer();
							photoGatherer.setCategory(PhotoGatherer.CATEGORY_PREVIEW + "|" + category.getName());
							photoGatherer.setCategoryType(PhotoGatherer.CATEGORY_TYPE_PREVIEW);
							
							String parentPath = directory.getName();
							if (parentPath.contains("/")) {
								photoGatherer.setDirectory(parentPath.substring(parentPath.lastIndexOf("/") + 1));
							}
							else {
								photoGatherer.setDirectory(parentPath.substring(parentPath.lastIndexOf("\\") + 1));
							}
							photoGatherer.setDateCreated(FileTimeCreation.getFileTimeCreation(directory));
							photoGathererSvc.createPhotoGatherer(photoGatherer);
							for (File file: directory.listFiles()) {
								if (file.exists() && file.isFile()) {
									recordPhotoSize--;
									if (file.getName().contains("DS_Store")) {
										file.delete();
										continue;
									}
									
									ImageIcon image = new ImageIcon(file.getAbsolutePath());
									if (image.getImage().getWidth(null) >= 0) {
										PhotoInfo photoInfo = new PhotoInfo();
										photoInfo.setCategoryId(photoGatherer.getCategoryId());
										photoInfo.setFileName(file.getName());
										
										if (image.getIconWidth() > image.getIconHeight()) {
											photoInfo.setPhotoType(Photo.LANDSCAPE);
										}
										else {
											photoInfo.setPhotoType(Photo.PORTRAIT);
										}
										
										ByteArrayOutputStream baos = new ByteArrayOutputStream();
										BufferedImage bi;
										try {
											bi = ImageIO.read(file.getAbsoluteFile());
											ImageIO.write(bi, "jpg", baos);
											byte[] imageInByte = baos.toByteArray();
											photoInfo.setFileBytes(imageInByte);
											
											baos.flush();
											baos.close();
											bi.flush();
											
											// thumbnail
											baos = new ByteArrayOutputStream();
											bi = PhotoShrinkUtil.shrinkImage(file, true);
											ImageIO.write(bi, PhotoShrinkUtil.PHOTO_FORMAT, baos);
											imageInByte = baos.toByteArray();
											photoInfo.setThumbnailBytes(imageInByte);
											
											baos.flush();
											baos.close();
											bi.flush();
										} catch (IOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										
										photoGathererSvc.createPhotoInfo(photoInfo);
									}
								}
							}
						}
					}
				}
			}
			
		}
		refreshPhotoGatherer();
		recordPhotoSize = 0;
	}

	@Override
	public void processPhotoArchives(String adminUser) throws IOException {
		photoGathererSvc.removeAllByCategoryType(PhotoGatherer.CATEGORY_TYPE_PERMANENT);
		photoGathererSvc.removeAllByCategoryType(PhotoGatherer.CATEGORY_TYPE_CALENDAR);
		
		File allPermCategories = new File(permPhotoLocation);
		File allCalendarCategories = new File(permCalendarPhotoLocation);
		
		List<File> directories = new ArrayList<File>();
		if (allPermCategories.exists() && allPermCategories.isDirectory()) {
			directories.add(allPermCategories);
		}
		
		if (allCalendarCategories.exists() && allCalendarCategories.isDirectory()) {
			directories.add(allCalendarCategories);
		}
		
//		if (!allDirectories.exists()) {
//			System.out.println("File not found. ==>" + permPhotoLocation);
//			return;
//		}
		
		if (directories.isEmpty()) {
			LOGGER.info("File not found. ==>" + permPhotoLocation + " AND " + permCalendarPhotoLocation);
		}
		
		for (File allDirectories: directories) {
			if (allDirectories.exists() && allDirectories.isDirectory()) {
				for (File categories: allDirectories.listFiles()) {
					if (categories.exists() && categories.isDirectory()) {
						for (File albums: categories.listFiles()) {
							if (albums.exists() && albums.isDirectory()) {
								PhotoGatherer photoGatherer = photoGathererSvc.getAllPhotos(categories.getName(), albums.getName(), 0, 0, 0, true);
								if (photoGatherer == null) {
									photoGatherer = new PhotoGatherer();
									LOGGER.info("Adding new album: " + albums.getAbsolutePath());
								}
								else {
									photoGathererSvc.removePhotosInDirectory(photoGatherer.getCategoryId());
									LOGGER.info("updating album: " + albums.getAbsolutePath());
								}
								
								photoGatherer.setCategory(categories.getName());
								photoGatherer.setDirectory(albums.getName());
								if (allDirectories.getName().contains(PhotoGatherer.CATEGORY_CALENDAR)) {
									photoGatherer.setCategoryType(PhotoGatherer.CATEGORY_TYPE_CALENDAR);
								}
								else {
									photoGatherer.setCategoryType(PhotoGatherer.CATEGORY_TYPE_PERMANENT);	
								}
								
								for (File files: albums.listFiles()) {
									if (files.exists() && files.isFile()) {
										recordPhotoSize--;
										if (files.getName().contains("DS_Store")) {
											files.delete();
											continue;
										}
										
										if (files.getName().contains(PARAM_CONTAINS_ALBUM_CAPTION)) {
											BufferedReader br = new BufferedReader(new FileReader(files.getAbsolutePath()));
											StringBuilder sb = new StringBuilder();
											while (br.ready()) {
												sb.append(br.readLine());
											}
											br.close();
											photoGatherer.setAlbumCaptionName(sb.toString());
											photoGatherer.setDateCreated(FileTimeCreation.getFileTimeCreation(files));
											
											try {
												if (photoGatherer != null && photoGatherer.getCategoryId() != 0) {
													photoGathererSvc.updatePhotoCategory(photoGatherer);
												}
												else {
													photoGathererSvc.createPhotoGatherer(photoGatherer);
												}
											}
											catch (ServiceException se) {
												LOGGER.info(se.getMessage());
											}
											continue;
										}
										else if (files.getName().contains(PARAM_CONTAINS_ALBUM_DESCRIPTION)) {
											BufferedReader br = new BufferedReader(new FileReader(files.getAbsolutePath()));
											StringBuilder sb = new StringBuilder();
											while (br.ready()) {
												sb.append(br.readLine());
											}
											br.close();
											photoGatherer.setAlbumDescription(sb.toString());
	
											try {
												if (photoGatherer != null && photoGatherer.getCategoryId() != 0) {
													photoGathererSvc.updatePhotoCategory(photoGatherer);
												}
												else {
													photoGathererSvc.createPhotoGatherer(photoGatherer);
												}
											}
											catch (ServiceException se) {
												LOGGER.info(se.getMessage());
											}
											continue;
										}
										
										ImageIcon image = new ImageIcon(files.getAbsolutePath());
										if (image.getImage().getWidth(null) >= 0) {
											try {
												if (StringUtils.isBlank(Long.toString(photoGatherer.getCategoryId())) || photoGatherer.getCategoryId() == 0) {
													photoGathererSvc.createPhotoGatherer(photoGatherer);
												}
												else {
													photoGathererSvc.updatePhotoCategory(photoGatherer);
												}
											}
											catch (ServiceException se) {
												LOGGER.info(se.getMessage());
											}
											
											PhotoInfo photoInfo = new PhotoInfo();
											photoInfo.setCategoryId(photoGatherer.getCategoryId());
											photoInfo.setFileName(files.getName());
											
											if (image.getIconWidth() > image.getIconHeight()) {
												photoInfo.setPhotoType(Photo.LANDSCAPE);
											}
											else {
												photoInfo.setPhotoType(Photo.PORTRAIT);
											}
											
											if (files.getName().contains(PARAM_CONTAINS_COVER_PHOTO)) {
												photoInfo.setPhotoType(PhotoInfo.COVERPHOTO);
											}
	
											ByteArrayOutputStream baos = new ByteArrayOutputStream();
											BufferedImage bi = ImageIO.read(files.getAbsoluteFile());
											ImageIO.write(bi, PhotoShrinkUtil.PHOTO_FORMAT, baos);
											byte[] imageInByte = baos.toByteArray();
	
											photoInfo.setFileBytes(imageInByte);
											baos.flush();
											baos.close();
											bi.flush();
											
											// thumbnail
											baos = new ByteArrayOutputStream();
											bi = PhotoShrinkUtil.shrinkImage(files, true);
											ImageIO.write(bi, PhotoShrinkUtil.PHOTO_FORMAT, baos);
											imageInByte = baos.toByteArray();
											photoInfo.setThumbnailBytes(imageInByte);
																					
											
											photoGathererSvc.createPhotoInfo(photoInfo);
											LOGGER.info("Photos added: " + files.getAbsolutePath());
											
											baos.flush();
											baos.close();
											bi.flush();
										}
									}
									
								}
							}
						}
					}
				}
			}
		}
		recordPhotoSize = 0;
	}
	

	@Override
	public int cleanupPortfolio() {
		File parentDirectory = new File(portfolioLocation);
		if (parentDirectory.exists() && parentDirectory.isDirectory()) {
			for (File albums: parentDirectory.listFiles()) {
				if (albums.exists() && albums.isDirectory()) {
					if (albums.listFiles().length > 0) {
						recordPhotoSize += albums.length();
					}
				}
			}
		}
		
		jmsMessageSender.sendToQueue(destinationQueueCleanupPortfolio, UserContext.getUserId());
		return recordPhotoSize;
	}


	@Override
	public int cleanupPreviewPhotos() {
		File parentDirectory = new File(previewLocation);
		if (parentDirectory.exists() && parentDirectory.isDirectory()) {
			for (File category: parentDirectory.listFiles()) {
				if (category.exists() && category.isDirectory()) {
					for (File directory: category.listFiles()) {
						if (directory.exists() && directory.isDirectory()) {
							if (directory.listFiles().length > 0) {
								recordPhotoSize =+ directory.listFiles().length;	
							}
						}
					}
				}
			}
		}

		jmsMessageSender.sendToQueue(destinationQueueCleanupPreviewPhoto, UserContext.getUserId());
		return recordPhotoSize;
	}

	@Override
	public int cleanupPhotoArchives() {
		if (StringUtils.isBlank(UserContext.getUserId())) {
			UserContext.setUserId("SYSTEM");
		}
		
		List<PhotoGatherer> allPhotos = photoGathererSvc.getAll();
		for (PhotoGatherer p: allPhotos) {
			recordPhotoSize += p.getPhotoInfoList().size();
		}
		
		
		jmsMessageSender.sendToQueue(destinationQueueCleanupPhotoArchive, UserContext.getUserId());
		return recordPhotoSize;
	}

	@Override
	public void cleanupData() {
		if (!scannedAllowed) {
			return;
		}
		
		List<PhotoGatherer> allPhotos = photoGathererSvc.getAll();
		if (allPhotos != null && !allPhotos.isEmpty()) {
			int ctr = 0;
			for (PhotoGatherer photo: allPhotos) {
				switch (photo.getCategoryType()) {
				case PhotoGatherer.CATEGORY_TYPE_CALENDAR:
				case PhotoGatherer.CATEGORY_TYPE_PERMANENT:
					if (photo.getPhotoInfoList() != null && !photo.getPhotoInfoList().isEmpty()) {
						for (PhotoInfo photoInfo: photo.getPhotoInfoList()) {
							if (photoInfo != null) {
								File file = new File(permPhotoLocation + C_SLASH +
										photo.getCategory() + C_SLASH + photo.getDirectory() + 
										C_SLASH + photoInfo.getFileName());
								
								if (!file.exists()) {
									file = new File(permCalendarPhotoLocation + C_SLASH +
											photo.getCategory() + C_SLASH + photo.getDirectory() + 
											C_SLASH + photoInfo.getFileName());
								}
								
								if (!file.exists()) {
									photoGathererSvc.removePhoto(photoInfo);
									LOGGER.info("Photo record just removed. ===>" + permPhotoLocation + C_SLASH +
										photo.getCategory() + C_SLASH + photo.getDirectory() + 
										C_SLASH + photoInfo.getFileName());
									ctr++;
								}	
							}
						}
					}
					break;

				default:
					break;
				}
			}
			LOGGER.info("Total Processed photos: " + ctr);
			refreshPhotoGatherer();
		}
	}
	
	
	
	
	@Override
	public int getTotalRecordsProcessed() {
		return recordPhotoSize;
	}


	private void refreshPhotoGatherer () {
		refreshManagerSvc.processRefreshRequest(PHOTOGATHERERSVC);
	}

	@SuppressWarnings("unused")
	private void initPath(){
		String[] locations = {tempPhotoLocation, permPhotoLocation, tempSlideShowLocation, permSlideShowLocation, portfolioLocation, previewLocation};
		for (String loc: locations) {
			File initLoc = new File(loc);
			if (initLoc.exists()) {
				continue;					
			}
			else {
				throw new ServiceException("Cannot find Directory === " + loc);
			}
		}
	}

	public void setTempPhotoLocation(String tempPhotoLocation) {
		this.tempPhotoLocation = tempPhotoLocation;
	}
	public void setPermPhotoLocation(String permPhotoLocation) {
		this.permPhotoLocation = permPhotoLocation;
	}
	public void setTempSlideShowLocation(String tempSlideShowLocation) {
		this.tempSlideShowLocation = tempSlideShowLocation;
	}
	public void setPermSlideShowLocation(String permSlideShowLocation) {
		this.permSlideShowLocation = permSlideShowLocation;
	}
	public void setImageSize(int imageSize) {
		this.imageSize = imageSize;
	}
	public void setJmsMessageSender(JmsMessageSender jmsMessageSender) {
		this.jmsMessageSender = jmsMessageSender;
	}
	public void setDestinationQueue(String destinationQueue) {
		this.destinationQueue = destinationQueue;
	}
	public void setRefreshManagerSvc(RefreshManagerSvc refreshManagerSvc) {
		this.refreshManagerSvc = refreshManagerSvc;
	}
	public void setPermUserPhotoUploadLocation(String permUserPhotoUploadLocation) {
		this.permUserPhotoUploadLocation = permUserPhotoUploadLocation;
	}
	public void setDestinationQueueUpload(String destinationQueueUpload) {
		this.destinationQueueUpload = destinationQueueUpload;
	}
	public void setPhotoUploadDirSvc(PhotoUploadDirSvc photoUploadDirSvc) {
		this.photoUploadDirSvc = photoUploadDirSvc;
	}
	public void setPhotoUploadLogDao(PhotoUploadLogDao photoUploadLogDao) {
		this.photoUploadLogDao = photoUploadLogDao;
	}
	public void setThumbnailSize(int thumbnailSize) {
		this.thumbnailSize = thumbnailSize;
	}
	public void setThumbnailNeeded(boolean isThumbnailNeeded) {
		this.isThumbnailNeeded = isThumbnailNeeded;
	}
	public void setPhotoGathererSvc(PhotoGathererSvc photoGathererSvc) {
		this.photoGathererSvc = photoGathererSvc;
	}
	public void setPortfolioLocation(String portfolioLocation) {
		this.portfolioLocation = portfolioLocation;
	}
	public void setPreviewLocation(String previewLocation) {
		this.previewLocation = previewLocation;
	}
	public void setDestinationQueueCleanupPortfolio(
			String destinationQueueCleanupPortfolio) {
		this.destinationQueueCleanupPortfolio = destinationQueueCleanupPortfolio;
	}
	public void setDestinationQueueCleanupPreviewPhoto(
			String destinationQueueCleanupPreviewPhoto) {
		this.destinationQueueCleanupPreviewPhoto = destinationQueueCleanupPreviewPhoto;
	}
	public void setDestinationQueueCleanupPhotoArchive(
			String destinationQueueCleanupPhotoArchive) {
		this.destinationQueueCleanupPhotoArchive = destinationQueueCleanupPhotoArchive;
	}
	public void setPermCalendarPhotoLocation(String permCalendarPhotoLocation) {
		this.permCalendarPhotoLocation = permCalendarPhotoLocation;
	}

	public void setScannedAllowed(boolean scannedAllowed) {
		this.scannedAllowed = scannedAllowed;
	}
	public void setShrinkNeeded(boolean isShrinkNeeded) {
		this.isShrinkNeeded = isShrinkNeeded;
	}
}
