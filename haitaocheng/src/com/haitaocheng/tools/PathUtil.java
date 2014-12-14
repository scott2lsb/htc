package com.haitaocheng.tools;

import java.lang.reflect.Method;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.os.storage.StorageManager;

public class PathUtil {

	/**
	 * 获取内置存储卡 或者 外置存储卡的位置，如果有多张存储卡，就获取外置存储卡的位置
	 * @return
	 */
	public static String getExternalStorageDirectory(Activity activity) {
		// 使用2.2 ，2.3的版本 用于获取内置存储卡路径
		String outStoragePath = null;
		
		try {
			// 使用4.0以后的版本 ，  反射获取多个存储卡的路径，返回外资存储卡的路径
			StorageManager storageManager = (StorageManager) activity.getSystemService(Context.STORAGE_SERVICE);  
		    Class<?>[] paramClasses = {};  
		    Method getVolumePathsMethod = StorageManager.class.getMethod("getVolumePaths", paramClasses);  
		    getVolumePathsMethod.setAccessible(true);  
		    Object[] params = {};  
		    Object invoke = getVolumePathsMethod.invoke(storageManager, params);  
		    if(((String[])invoke).length > 1){
		    	for (int i = 0; i < ((String[])invoke).length; i++) {  
			        outStoragePath = ((String[])invoke)[i];
			    }
		    } else {
		    	outStoragePath = Environment.getExternalStorageDirectory().getAbsolutePath();
		    }
		} catch (Exception e) {
			outStoragePath = Environment.getExternalStorageDirectory().getAbsolutePath();
		}
		return outStoragePath;
	}
	
	
}
