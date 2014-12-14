package com.haitaocheng.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.HashMap;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.support.v4.util.LruCache;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageSwitcher;
import android.widget.ImageView;

public class ImageUtil {

	// Bitmap 软引用 缓存， 缓存目录中的小图片
	// private static HashMap<String, SoftReference<Bitmap>> sdcardBitmapCacheItem = new HashMap<String, SoftReference<Bitmap>>();
	// 缓存 打开的大图片
	private static HashMap<String, SoftReference<Bitmap>> sdcardBitmapCachePhoto = new HashMap<String, SoftReference<Bitmap>>();
	
	// LrCache 缓存， 缓存目录中的小图片
//	private static HashMap<String, SoftReference<Bitmap>> sdcardBitmapCacheItem = new HashMap<String, SoftReference<Bitmap>>();
		
	
	/**
	 * 缓存Image的类，当存储Image的大小大于LruCache设定的值，系统自动释放内存
	 */
	private static LruCache<String, Bitmap> mMemoryItemCache;
	
	
	private Activity activity = null;
	public ImageUtil(Activity activity) {
		this.activity = activity;
		//获取系统分配给每个应用程序的最大内存，假设每个应用系统分配32M
		int maxMemory = (int) Runtime.getRuntime().maxMemory();  
		System.out.println(maxMemory);
        int mCacheSize = maxMemory / 4; // 缓存占用应用的内存的1/4 = 8M
        System.out.println(mCacheSize);
        //给LruCache分配1/4 8M
		mMemoryItemCache = new LruCache<String, Bitmap>(mCacheSize){
			//必须重写此方法，来测量Bitmap的大小
			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getRowBytes() * value.getHeight();
			}
		};
	}
	
	/**
	 * 添加Bitmap到内存缓存
	 * @param key
	 * @param bitmap
	 */
	public void addBitmapToMemoryCache(String key, Bitmap bitmap) {  
	    if (getBitmapFromMemCache(key) == null && bitmap != null) {  
	        mMemoryItemCache.put(key, bitmap);  
	    }  
	}  
	
	/**
	 * 从内存缓存中获取一个Bitmap
	 * @param key
	 * @return
	 */
	public Bitmap getBitmapFromMemCache(String key) {  
	    return mMemoryItemCache.get(key);  
	} 
	
	/**
	 * 2.1 方法描述：获取缓存图片的路径
	 * 
	 * @param imagePath
	 * @return
	 */
	public String getCacheImagePath(String imagePath) {
		//  寻找 缓存文件夹
		String cachePath = new File(imagePath).getParentFile().getAbsolutePath() + "/cache/";
		
		// 如果缓存文件不存在，则创建一个缓存文件夹
		if(!new File(cachePath).exists()) {
			new File(cachePath).mkdirs(); 
		}
		String fileName = new File(imagePath).getName();
		String newImagePath = "";
		
		newImagePath = cachePath + fileName + ".cache_gridview_item";
		
//		int index = path.lastIndexOf(".");
//		String prefix = path.substring(0, index);
//		String suffix = path.substring(index);
//		String newImagePath = prefix + suffixName + suffix;
		// System.out.println("缓存图片路径newImagePath: " + newImagePath);
		return newImagePath;
	}
	
	
	
	
	/**
	 * 1 方法描述：从内存缓存中获取
	 * 加载图片: 大图-->小图-->本地缓存小图-->内存缓存-->加载
	 * 
	 * @param imagePath		图片路径
	 * @param type				图片来源类型
	 * @return
	 */
	public Bitmap loadSdcardCachePhoto(String imagePath) {
		Bitmap bitmap = null;
		if (sdcardBitmapCachePhoto.containsKey(imagePath)) {
			// 如果一个对象只具有软引用，则内存空间足够，垃圾回收器就不会回收它；
			// 如果内存空间不足了，就会回收这些对象的内存
			SoftReference<Bitmap> softReference = sdcardBitmapCachePhoto.get(imagePath);
			bitmap = softReference.get();
			if (null != bitmap) {
				return bitmap;
			}
		}
		try {
			// 获取缩小之后的图片
			bitmap =  loadBigPhoto(imagePath);
			sdcardBitmapCachePhoto.put(imagePath, new SoftReference<Bitmap>(bitmap));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return bitmap;
	}

	
	/**
	 * 1 方法描述：从内存缓存中获取
	 * 加载图片: 大图-->小图-->本地缓存小图-->内存缓存-->加载
	 * 
	 * @param imagePath		图片路径
	 * @param type				图片来源类型
	 * @return
	 */
	public Bitmap loadSdcardItemImage(String imagePath) {
		Bitmap bitmap = null;
		if(getBitmapFromMemCache(imagePath) != null){
			bitmap = getBitmapFromMemCache(imagePath);
			if (null != bitmap) {
				return bitmap;
			}
		}
		// 获取缩小之后的图片
		bitmap = loadImage(imagePath);
		addBitmapToMemoryCache(imagePath, bitmap);
		return bitmap;
	}
	

//	/**
//	 * 1 方法描述：从内存缓存中获取
//	 * 加载图片: 大图-->小图-->本地缓存小图-->内存缓存-->加载
//	 * 
//	 * @param imagePath		图片路径
//	 * @param type				图片来源类型
//	 * @return
//	 */
//	public Bitmap loadSdcardCacheImageItem(String imagePath) {
//		Bitmap bitmap = null;
//		if (sdcardBitmapCacheItem.containsKey(imagePath)) {
//			// 如果一个对象只具有软引用，当内存空间足够，垃圾回收器就不会回收它；
//			// 如果内存空间不足了，就会回收这些对象的内存
//			SoftReference<Bitmap> softReference = sdcardBitmapCacheItem.get(imagePath);
//			bitmap = softReference.get();
//			if (null != bitmap) {
//				return bitmap;
//			}
//		}
//		// 获取缩小之后的图片
//		bitmap = loadImage(imagePath);
//		sdcardBitmapCacheItem.put(imagePath, new SoftReference<Bitmap>(bitmap));
//		return bitmap;
//	}

	
	/**
	 * 2 方法描述：从本地缓存小图
	 * 加载图片: 大图-->小图-->本地缓存小图-->内存缓存-->加载
	 * 
	 * 		判断 缓存图片 是否存在
	 * 		 (1) 缓存图片 存在, 就直接读取 缓存图片	
	 * 		 (2) 缓存图片 不存在, 则重新生成一张本地缓存图片
	 * 
	 * @param activity
	 * @param imagePath
	 * @return
	 */
	public  Bitmap loadImage(String imagePath) {
		Bitmap bitmap = null;
		// 2.1 获取缓存图片路径
		String cacheImagePath = getCacheImagePath(imagePath);
		
	    File cacheFile= new File(cacheImagePath);
	    try {
	    	//  (1) 缓存图片 存在, 就直接读取 缓存图片	
		    if(!cacheFile.exists()){
		    	// 读取大图片，成小图片
		    	bitmap = loadBigImage(imagePath);
		    	// 把小图片 保存缓存小图  到 本地 
		    	saveFileByBitmap(bitmap, cacheImagePath);
		    } 
		    //  (2) 缓存图片 不存在, 则重新生成一张本地缓存图片
		    else {
		    	// bitmap = BitmapFactory.decodeFile(imagePath);  
		    	bitmap = BitmapFactory.decodeStream(new FileInputStream(new File(cacheImagePath)));
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}
	
	
	/**
	 * 3 方法描述： 加载sd卡下的大图片
	 * @param imagePath
	 * @throws Exception 
	 */
	public Bitmap loadBigImage(String imagePath) throws Exception {
		Bitmap bitmap = null;
		
		int width = 100;
		int height = 100; 
        // 1.options.inJustDecodeBounds  
        BitmapFactory.Options options = new BitmapFactory.Options();  
 		options.inJustDecodeBounds = true;
 		// 这里返回的bmp是 null, 但是 有了宽 和 高的信息，
 	 	BitmapFactory.decodeFile(imagePath, options);  
 	 	System.out.println("图片 原始宽度：" + options.outWidth + ", 高度： " + options.outHeight);
        
 	 	// 2.options.inSampleSize 
 	 	int scaleX = (int) Math.ceil(options.outWidth /(float)width); 		// 计算宽度比例
		int scaleY = (int) Math.ceil(options.outHeight /(float)height); 	// 计算高度比例
		if (scaleX > 1 && scaleY > 1) {
			options.inSampleSize =  (scaleX < scaleY) ? scaleX : scaleY;
		}
		// 3. 为了节约内存我们还可以使用下面的几个字段
//		options.inPreferredConfig = Bitmap.Config.ARGB_4444;    // 默认是Bitmap.Config.ARGB_8888 
//		/* 下面两个字段需要组合使用 */
//		options.inPurgeable = true;
//		options.inInputShareable = true;
		options.inPreferredConfig = Bitmap.Config.ARGB_8888; 
		
		// 设置为false才能真正的返回一个Bitmap，因为上面我们将其设置为true来获取图片尺寸了  
		options.inJustDecodeBounds = false; 
		// bitmap = BitmapFactory.decodeFile(imagePath,  options);  
		bitmap = BitmapFactory.decodeStream( new FileInputStream(new File(imagePath)), null,  options);  
		System.out.println("压缩后的 图片宽度：" + bitmap.getWidth() + ", 高度： " + bitmap.getHeight());
		System.out.println("=================================");

		// 调用工具类： 获取图片的旋转角度，有些系统把拍照的图片旋转了，有的没有旋转 
		int degree = readPictureDegree(imagePath);  
		
		// 调用工具类：  把图片旋转为正的方向
		bitmap = rotaingImageView(bitmap, degree); 
		return bitmap;
	}

	
	/**
	 * 3.X  方法描述： 加载sd卡下的大图片， 如果大于 长 或者 宽 大于 2个屏幕， 就压缩一下
	 * @param imagePath
	 * @throws Exception 
	 */
	public Bitmap loadBigPhoto(String imagePath) throws Exception {
		Bitmap bitmap = null;
		// 如果控件为空，则生成一个屏幕大小的图片
		// 获取屏幕大小
		Display display = activity.getWindowManager().getDefaultDisplay();  
		int screenWidth = display.getWidth();  
		int screenHeight = display.getHeight();  
   
        // 1.options.inJustDecodeBounds  
        BitmapFactory.Options options = new BitmapFactory.Options();  
 		options.inJustDecodeBounds = true;
 		// 这里返回的bmp是 null, 但是 有了宽 和 高的信息，
 	 	BitmapFactory.decodeFile(imagePath, options);  
        
 	 	options.inSampleSize = calculateInSampleSize(options, screenWidth, screenHeight);
// 	 	options.inSampleSize = 10;
		
 	 	// 3. 为了节约内存我们还可以使用下面的几个字段
// 	 	options.inPreferredConfig = Bitmap.Config.ARGB_4444; 
 	 	options.inPreferredConfig = Bitmap.Config.ARGB_8888; 
 	 	/* 下面两个字段需要组合使用 */
		options.inPurgeable = true;
		options.inInputShareable = true;
			
		// 设置为false才能真正的返回一个Bitmap，因为上面我们将其设置为true来获取图片尺寸了  
		options.inJustDecodeBounds = false; 
		 bitmap = BitmapFactory.decodeFile(imagePath,  options);  
//		bitmap = BitmapFactory.decodeStream( new FileInputStream(new File(imagePath)), null,  options);  
		// System.out.println("压缩后的 图片宽度：" + bitmap.getWidth() + ", 高度： " + bitmap.getHeight());
		// System.out.println("=================================");

		// 调用工具类： 获取图片的旋转角度，有些系统把拍照的图片旋转了，有的没有旋转 
		int degree = readPictureDegree(imagePath);  
		
		// 调用工具类：  把图片旋转为正的方向
		bitmap = rotaingImageView(bitmap, degree); 
		return bitmap;
	}
	
	
	/**
	 * 根据原图尺寸和目标区域的尺寸  计算出合适的Bitmap尺寸
	 * @param options
	 * @param d
	 * @param width
	 * @return
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options, double screenWidth, double screenHeight) {
		// 原始图片的宽高
		final float imageWidth = options.outWidth;
		final float imageHeight = options.outHeight;
		System.out.println("========屏幕宽和高：" +screenWidth + ", " +  screenHeight);
		System.out.println("========图片宽和高：" +imageWidth + ", " +  imageHeight);
		int inSampleSize = 1;

		if ( imageWidth > screenWidth || imageHeight > screenHeight ) {
			final float halfWidth = imageWidth / 2;
			final float halfHeight = imageHeight / 2;

			// 在保证解析出的bitmap宽高分别大于目标尺寸宽高的前提下，取可能的inSampleSize的最大值
			while ((halfHeight / inSampleSize) > screenHeight || (halfWidth / inSampleSize) > screenWidth) {
//				inSampleSize *= 2;
				inSampleSize++;
				System.out.println("大图片压缩：" + imageWidth/inSampleSize + "=====" +  imageHeight/inSampleSize +"=====" +  inSampleSize);
			}
		}
		if(inSampleSize>1 && (imageWidth < 1800 || imageHeight<1800)) {
			inSampleSize -= 1;
		}
		System.out.println("大图片压缩：" +"=====" + inSampleSize );
		return inSampleSize;
	}
	
	/**
	 * 4 根据 Bitmap 生成一个新图片保存到本地
	 * @param bitmap
	 * @param newImagePath
	 */
	public void saveFileByBitmap(Bitmap bitmap, String newImagePath) {
		 File file=new File(newImagePath);
	        try {
	            FileOutputStream out=new FileOutputStream(file);
	            if(bitmap.compress(Bitmap.CompressFormat.JPEG, 85, out)){
	                out.flush();
	                out.close();
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	}	
	 
	

	
    /**
     * 3.1 读取图片属性：旋转的角度
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
	public int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}
	
	
	/** 
     * 3.2 旋转图片 
     * @param degree 
     * @param bitmap 
     * @return Bitmap 
     */  
	 public Bitmap rotaingImageView(Bitmap bitmap, int degree) {  
        //旋转图片 动作  
        Matrix matrix = new Matrix();;  
        matrix.postRotate(degree);  
        // 创建新的图片  
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,  
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);  
        return resizedBitmap;  
    }
	 
	 
	/**
	 * 4 清除缓存
	 */
	public void destoryBitmaps() {
		try {
			if (mMemoryItemCache != null && mMemoryItemCache.size() > 0) {
				mMemoryItemCache = null;
			}
			if (sdcardBitmapCachePhoto != null && sdcardBitmapCachePhoto.size() > 0) {
				sdcardBitmapCachePhoto.clear();
			}
			System.out.println("清除缓存");
		} catch (Exception e) {
			System.out.println("清除缓存异常");
		}
	}
		
	
	/**
	 *   4 释放单个  Bitmap内存
	 * @param path
	 */
	public void destoryBitmap(String path) {
		if (mMemoryItemCache.get(path) != null) {
			Bitmap bitmap = mMemoryItemCache.get(path);
			this.destoryBitmap(bitmap);
			mMemoryItemCache.remove(path);
		}
		if (sdcardBitmapCachePhoto.containsKey(path)) {
			SoftReference<Bitmap> reference = sdcardBitmapCachePhoto.get(path);
			Bitmap bitmap = reference.get();
			this.destoryBitmap(bitmap);
			sdcardBitmapCachePhoto.remove(path);
		}
	}
	
	
//	/**
//	 *   4 释放单个  Bitmap内存
//	 * @param path
//	 */
//	public void destoryBitmap(String path) {
//		if (sdcardBitmapCacheItem.containsKey(path)) {
//			SoftReference<Bitmap> reference = sdcardBitmapCacheItem.get(path);
//			Bitmap bitmap = reference.get();
//			this.destoryBitmap(bitmap);
//			sdcardBitmapCacheItem.remove(path);
//		}
//		if (sdcardBitmapCachePhoto.containsKey(path)) {
//			SoftReference<Bitmap> reference = sdcardBitmapCachePhoto.get(path);
//			Bitmap bitmap = reference.get();
//			this.destoryBitmap(bitmap);
//			sdcardBitmapCachePhoto.remove(path);
//		}
//	}	
//	
//	/**
//	 *   4 释放单个  Bitmap内存
//	 * @param path
//	 */
//	public void destoryBitmap(String path) {
//		if (sdcardBitmapCacheItem.containsKey(path)) {
//			SoftReference<Bitmap> reference = sdcardBitmapCacheItem.get(path);
//			Bitmap bitmap = reference.get();
//			this.destoryBitmap(bitmap);
//			sdcardBitmapCacheItem.remove(path);
//		}
//		if (sdcardBitmapCachePhoto.containsKey(path)) {
//			SoftReference<Bitmap> reference = sdcardBitmapCachePhoto.get(path);
//			Bitmap bitmap = reference.get();
//			this.destoryBitmap(bitmap);
//			sdcardBitmapCachePhoto.remove(path);
//		}
//	}	

	 /** 
     *  4 出于内存考虑，销毁Bitmap 这个吃内存的 大老虎
     */  
    public void destoryBitmap(Bitmap bitmap) {  
        if (bitmap != null && !bitmap.isRecycled()) {  
        	bitmap.recycle();  
        	bitmap = null;  
        	System.out.println("清空bitmap");
        }  
    }
    

	/**
	 * 3 方法描述： 加载R资源下的大图片 成 View 大小的图片。 如果view为空，则生成屏幕大小的图片
	 * 使用场景： 如gridview中的 item 的大小
	 * @param resources
	 * @param resourceId
	 * @param view			如果view为空，则生成屏幕大小的图
	 * @return
	 */
	public Bitmap loadResourceImage(int resourceId, final View view)  {
		Bitmap bitmap = null;
		
		int width = 0;
		int height = 0; 
		// 如果控件为空，则生成一个屏幕大小的图片
		if(view == null) {
			// 获取屏幕大小
			Display display = activity.getWindowManager().getDefaultDisplay();  
	        width = display.getWidth();  
	        height = display.getHeight();  
		} else {
			// 获取控件大小
			ViewTreeObserver vto = view.getViewTreeObserver(); 
			vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() { 
				@Override
				public void onGlobalLayout() { 
					view.getViewTreeObserver().removeGlobalOnLayoutListener(this); 
				} 
			});
			width = view.getWidth();  
	        height = view.getHeight();  
		}
		// System.out.println("控件宽度 width:" + width + "  height:" + height);
        
        // 1.options.inJustDecodeBounds  
        BitmapFactory.Options options = new BitmapFactory.Options();  
 		options.inJustDecodeBounds = true;
 		// 这里返回的bmp是 null, 但是 有了宽 和 高的信息，
 	 	BitmapFactory.decodeResource(activity.getResources(), resourceId, options);  
        // System.out.println("图片 原始宽度：" + options.outWidth + ", 高度： " + options.outHeight);
 	 	
 	 	// 2.options.inSampleSize 
 	 	int scaleX = (int) Math.ceil(options.outWidth /(float)width); 		// 计算宽度比例
		int scaleY = (int) Math.ceil(options.outHeight /(float)height); 	// 计算高度比例
	
		if (scaleX > 1 && scaleY > 1) {
			if(view != null)  scaleX--; scaleY--;
			if (scaleX > scaleY) {
				options.inSampleSize = scaleX;
			} else {
				options.inSampleSize = scaleY;
			} 
		}
//		// 3. 为了节约内存我们还可以使用下面的几个字段
//		options.inPreferredConfig = Bitmap.Config.ARGB_4444;    // 默认是Bitmap.Config.ARGB_8888 
//		/* 下面两个字段需要组合使用 */
//		options.inPurgeable = true;
//		options.inInputShareable = true;
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		
		// 设置为false才能真正的返回一个Bitmap，因为上面我们将其设置为true来获取图片尺寸了  
		options.inJustDecodeBounds = false; 
		// bitmap = BitmapFactory. ecodeFile(imagePath,  options);  
		// bitmap = BitmapFactory.decodeStream( new FileInputStream(new File(imagePath)), null,  options);  
		bitmap = BitmapFactory.decodeResource(activity.getResources(), resourceId, options);
		// System.out.println("压缩后的 图片宽度：" + bitmap.getWidth() + ", 高度： " + bitmap.getHeight());
		return bitmap;
	}
	
	
	
	/**
	 * 创建满屏幕的 ImageView
	 * @return
	 */
	public ImageView createImageView() {
		// 获取屏幕大小
		Display display = activity.getWindowManager().getDefaultDisplay();  
        int width = display.getWidth();  
        int height = display.getHeight();  
        
        ImageView imageView = new ImageView(activity);
		imageView.setBackgroundColor(0xff000000);// 黑色背景
		// imageView.setScaleType(ImageView.ScaleType.CENTER_CROP); // 如果是ScaleType.CENTER，就看不到完整的图片
		// imageView.setLayoutParams(new ImageSwitcher.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		imageView.setLayoutParams(new ImageSwitcher.LayoutParams(width, height));
		return imageView;
	}
	
	
}
