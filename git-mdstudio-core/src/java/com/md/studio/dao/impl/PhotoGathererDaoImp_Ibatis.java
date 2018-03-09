package com.md.studio.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.md.studio.dao.PhotoGathererDao;
import com.md.studio.domain.PhotoGatherer;
import com.md.studio.domain.PhotoInfo;

public class PhotoGathererDaoImp_Ibatis extends SqlMapClientDaoSupport implements PhotoGathererDao {
	private static final String STMT_INSERT = "PhotoInfo.insert";
	private static final String STMT_INSERT_PHOTO = "PhotoInfo.insertPhoto";
	private static final String STMT_UPDATE_PHOTOINFO = "PhotoInfo.updatePhotoInfo";
	private static final String STMT_UPDATE_GATHERER = "PhotoInfo.updateGatherer";
	private static final String STMT_DELETE_CATEGORY = "PhotoInfo.deleteCategory";
	private static final String STMT_DELETE_PHOTOINFO = "PhotoInfo.deletePhotoInfo";
	private static final String STMT_DELETE_PHOTO_BYCATEGORY = "PhotoInfo.deletePhotoByCategoryId";
	private static final String STMT_DELETE_ALL_BYCATEGORY_TYPE = "PhotoInfo.deleteAllByCategoryType";
	
	private static final String STMT_SELECT_BY_PHOTOID_BYTE = "PhotoInfo.selectByPhotoIdByte";
	private static final String STMT_SELECT = "PhotoInfo.selectAll";
	private static final String STMT_SELECT_ALLBYCATEGORY = "PhotoInfo.selectAllByCategory";
	private static final String STMT_SELECT_ALLCATEGORY = "PhotoInfo.selectAllCategory";
	private static final String STMT_SELECT_ALL_COVERS_BYCATEGORY = "PhotoInfo.selectAllCoversByCategory";
	private static final String STMT_SELECT_PHOTOS_BYALBUM = "PhotoInfo.selectPhotosByAlbum";
	private static final String STMT_SELECT_PHOTO = "PhotoInfo.selectPhoto";
	private static final String STMT_SELECT_BY_PHOTOID = "PhotoInfo.selectByPhotoId";
	private static final String STMT_SELECT_CATEGORYID = "PhotoInfo.selectCategoryId";
	private static final String STMT_SELECT_PHOTO_BYCATEGORY_ID_FILENAME = "PhotoInfo.selectPhotoByCategoryIdAndFileName";
	private static final String STMT_SELECT_ALL_BY_CATEGORY_TYPE = "PhotoInfo.selectAllByCategoryType";
	private static final String STMT_SELECT_ID_BY_CATEGORY_TYPE = "PhotoInfo.selectIdByCategoryType";
	
	private static final String PARAM_CATEGORY = "category";
	private static final String PARAM_DIRECTORY = "directory";
	private static final String PARAM_FILENAME = "fileName";
	private static final String PARAM_CATEGORYID = "categoryId";
	
	private static final String PARAM_LIMIT = "limit";
	private static final String PARAM_OFFSET = "offset";
	private static final String PARAM_CATEGORYTYPE = "categoryType";
	private static final String PARAM_PHOTOTYPE = "photoType";
	private static final String STMT_SELECT_BY_CATEGORY_TYPE = "PhotoInfo.selectByCategoryType";
	
	@Override
	public void insertCategory(PhotoGatherer photoGatherer) {
		getSqlMapClientTemplate().insert(STMT_INSERT, photoGatherer);
		
	}
	@Override
	public void insertPhotoInfo(PhotoInfo photoInfo) {
		getSqlMapClientTemplate().insert(STMT_INSERT_PHOTO, photoInfo);
		
	}
	@Override
	public void updateCategory(PhotoGatherer photoGatherer) {
		getSqlMapClientTemplate().update(STMT_UPDATE_GATHERER, photoGatherer);
		
	}
	@Override
	public void updatePhotoInfo(PhotoInfo photoInfo) {
		getSqlMapClientTemplate().update(STMT_UPDATE_PHOTOINFO, photoInfo);
		
	}
	@Override
	public void deleteCategory(PhotoGatherer photoGatherer) {
		getSqlMapClientTemplate().delete(STMT_DELETE_CATEGORY, photoGatherer);
	}
	@Override
	public void deletePhotoInfo(PhotoInfo photoInfo) {
		getSqlMapClientTemplate().delete(STMT_DELETE_PHOTOINFO, photoInfo);
		
	}
	@Override
	public void deletePhotoInfoByCategoryId(long categoryId) {
		getSqlMapClientTemplate().delete(STMT_DELETE_PHOTO_BYCATEGORY, categoryId);
	}
	
	
	@Override
	public void deleteAllByCategoryType(int categoryType) {
		getSqlMapClientTemplate().delete(STMT_DELETE_ALL_BYCATEGORY_TYPE, categoryType);
	}
	@Override
	public List<String> selectAllCategoryList(int categoryType) {
		return getSqlMapClientTemplate().queryForList(STMT_SELECT_ALLCATEGORY, categoryType);
	}


	public List<PhotoGatherer> selectAllCategories() {
		return getSqlMapClientTemplate().queryForList(STMT_SELECT);
	}
	@Override
	public List<PhotoGatherer> selectAllPhotoCovers(String category) {
		return getSqlMapClientTemplate().queryForList(STMT_SELECT_ALL_COVERS_BYCATEGORY, category);
	}
	@Override
	public List<PhotoGatherer> selectAllByCategory(String category) {
		return getSqlMapClientTemplate().queryForList(STMT_SELECT_ALLBYCATEGORY, category);
	}
	@Override
	public PhotoGatherer selectCategoryById(long categoryId) {
		return (PhotoGatherer) getSqlMapClientTemplate().queryForObject(STMT_SELECT_CATEGORYID, categoryId);
	}
	@Override
	public PhotoGatherer selectByDirectory(String category, String directory, int offset, int limit) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(PARAM_CATEGORY, category);
		params.put(PARAM_DIRECTORY, directory);
		params.put(PARAM_OFFSET, offset);
		params.put(PARAM_LIMIT, limit);
		PhotoGatherer p = (PhotoGatherer) getSqlMapClientTemplate().queryForObject(STMT_SELECT_PHOTOS_BYALBUM, params);
		return p;
	}
	
	@Override
	public PhotoInfo selectPhotoInfo(long categoryId, String fileName) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(PARAM_CATEGORYID, categoryId);
		params.put(PARAM_FILENAME, fileName);
		return (PhotoInfo) getSqlMapClientTemplate().queryForObject(STMT_SELECT_PHOTO_BYCATEGORY_ID_FILENAME, params);
	}
	@Override
	public PhotoInfo selectPhotoInfoById(long photoId) {
		return (PhotoInfo) getSqlMapClientTemplate().queryForObject(STMT_SELECT_BY_PHOTOID, photoId);
	}
	
	
	@Override
	public byte[] selectPhotoInfoByIdByte(long photoId) {
		byte[] b = (byte[]) getSqlMapClientTemplate().queryForObject(STMT_SELECT_BY_PHOTOID_BYTE, photoId);
		return b;
	}
	@Override
	public PhotoGatherer selectPhoto(String category, String directory,	String fileName) {
		Map<String, String> params = new HashMap<String, String>();
		params.put(PARAM_CATEGORY, category);
		params.put(PARAM_DIRECTORY, directory);
		params.put(PARAM_FILENAME, fileName);
		return (PhotoGatherer) getSqlMapClientTemplate().queryForList(STMT_SELECT_PHOTO, params);
	}
	@Override
	public List<PhotoGatherer> selectAllByCategoryType(int categoryType) {
		return getSqlMapClientTemplate().queryForList(STMT_SELECT_ALL_BY_CATEGORY_TYPE, categoryType);
	}

	
	@Override
	public List<PhotoGatherer> selectByCategoryType(String category, int categoryType, List<Integer> photoType) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(PARAM_CATEGORYTYPE, categoryType);
		params.put(PARAM_PHOTOTYPE, photoType);
		
		if (StringUtils.isNotBlank(category)) {
			params.put(PARAM_CATEGORY, category);
		}
		return getSqlMapClientTemplate().queryForList(STMT_SELECT_BY_CATEGORY_TYPE, params);
	}
	@Override
	public List<PhotoGatherer> selectByCategoryType(Integer categoryId,	List<Integer> photoType) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(PARAM_CATEGORYID, categoryId);
		params.put(PARAM_PHOTOTYPE, photoType);
		
		return getSqlMapClientTemplate().queryForList(STMT_SELECT_ID_BY_CATEGORY_TYPE, params);
	}
	
	
	
	
	
}
