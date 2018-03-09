package com.md.studio.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Calendar;
import java.util.Date;

public class FileTimeCreation {
	private static Calendar calendar = Calendar.getInstance();
	
	public static Date getFileTimeCreation (File file) {
		Path path = FileSystems.getDefault().getPath(file.getAbsolutePath());
		BasicFileAttributes basicFileAttr = null;
		
		try {
			basicFileAttr = Files.readAttributes(path, BasicFileAttributes.class);
			calendar.setTimeInMillis(basicFileAttr.creationTime().toMillis());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return calendar.getTime();
	}
}
