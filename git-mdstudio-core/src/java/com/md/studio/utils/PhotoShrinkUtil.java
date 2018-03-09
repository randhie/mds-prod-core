package com.md.studio.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.apache.log4j.Logger;
import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;

public class PhotoShrinkUtil {
	private static final Logger LOGGER = Logger.getLogger(PhotoShrinkUtil.class);
	public static final String PHOTO_FORMAT = "jpg";
	private static int THUMBNAIL_SIZE = 600;
	
	public static void shrinkPhoto(File file, boolean isThumbnail) {
		
		if (file == null) {
			return;
		}
		
		if (file.exists() && file.isFile()) {
			ImageIcon image = new ImageIcon(file.getAbsolutePath());
			
			if (image.getImage().getWidth(null) >= 0) {
				try {
					BufferedImage resizedImage = null;
					BufferedImage originalImage = ImageIO.read(file);
					
					if (isThumbnail) {
						resizedImage = Scalr.resize(originalImage, Method.ULTRA_QUALITY, THUMBNAIL_SIZE);	
					}
					else {
						resizedImage = Scalr.resize(originalImage, Method.ULTRA_QUALITY, 1920);
					}
					ImageIO.write(resizedImage, PHOTO_FORMAT, file);
				}
				catch (IOException e) {
					LOGGER.error(e.getMessage());
				}
			}
		}
		
	}
	
	
	public static BufferedImage shrinkImage(File file, boolean isThumbnail) {
		
		if (file == null) {
			return null;
		}
		
		if (file.exists() && file.isFile()) {
			ImageIcon image = new ImageIcon(file.getAbsolutePath());
			
			if (image.getImage().getWidth(null) >= 0) {
				try {
					BufferedImage resizedImage = null;
					BufferedImage originalImage = ImageIO.read(file);
					
					if (isThumbnail) {
						resizedImage = Scalr.resize(originalImage, Method.ULTRA_QUALITY, THUMBNAIL_SIZE);	
					}
					else {
						resizedImage = Scalr.resize(originalImage, Method.ULTRA_QUALITY, 1920);
					}
					
					return resizedImage;
				}
				catch (IOException e) {
					LOGGER.error(e.getMessage());
				}
			}
		}
		return null;
	}

}
