package com.md.studio.service;

import java.util.List;
import java.util.Map;

import com.md.studio.dto.Photo;

public interface PhotoInfoNewSvc {
	
	public List<Photo> getSlidePhotos();
	
	
	public List<Photo> getPreviewNewPhotos();
	
	
	/*GET ALL LIST OF PHOTO CATEGORIES*/
	/*eg. landscape, events, portraits, places*/
	public Map<String, List<Photo>> getAllPhotoCategories();
	
	
	/*GET ALL PHOTO COVERS BASED ON THE PARENT DIRECTORY*/
	/*e.g: 
	 * get all COVER photos of all landscapes 
	 * covers for harrison hotspring, canada place,
	 * deception park, gastown, etc. 
	 *  */
	public List<Photo> getAllPhotoCover(String parentDirectory);
	
	
	
	/*GET ALL PHOTOS FROM CHILD FOLDER*/
	/*e.g: all photos from landscape folder
	 * photos from canada place
	 *  
	 * */
	public List<Photo> getAllPhotosByDirectory(String parentChildFolder);
	
	
	/* Portfolio
	 * 
	 * 
	 * 
	 */
	public List<Photo> getAllPortfolio();
	

	/*
	 * Photos within 
	 * the portfolio
	 * category
	 *
	 */
	public List<Photo> getPortfolioByDirectory(String parentChildFolder);
	
}
