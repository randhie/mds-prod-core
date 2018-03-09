package com.md.studio.domain;

public class UserContext {
	private static ThreadLocal<Boolean> hasFolderFiltered = new ThreadLocal<Boolean>();
	private static ThreadLocal<String> ipAddress = new ThreadLocal<String>();
	private static ThreadLocal<String> userId = new ThreadLocal<String>();
	private static ThreadLocal<Boolean> isAdmin = new ThreadLocal<Boolean>();
	
	public static void setHasFolderFiltered(boolean isFiltered) {
		hasFolderFiltered.set(isFiltered);
	}
	public static boolean hasFolderFiltered() {
		return hasFolderFiltered.get();
	}
	public static String getIpAddress() {
		return ipAddress.get();
	}
	public static void setIpAddress(String ipAdd) {
		ipAddress.set(ipAdd);
	}
	
	public static String getUserId() {
		return userId.get();
	}
	public static void setUserId(String userEid) {
		userId.set(userEid);
	}
	
	
	public static void setIsAdmin(Boolean isAdminUser){
		if (isAdminUser == null) {
			isAdmin.set(false);
			return;
		}
		
		isAdmin.set(isAdminUser);
	}
	public static boolean isAdmin(){
		return isAdmin.get();
	}
	
	public static void clearContext() {
		hasFolderFiltered.remove();
		ipAddress.remove();
		userId.remove();
	}
	
}
