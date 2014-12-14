package com.haitaocheng.tools;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.Window;
import android.widget.ImageView;

public class ScreenUtil {
	
	/**
	 * 获取屏幕可见范围的宽度 和高度，必须使用 XXView.post(new Runnable(){....}) 才可以获取到真实数据
	 * @param activity
	 * @return
	 */
	public static int[]  getScreenVisiable(Activity activity) {
		 // 获取状况栏高度
		Rect frame = new Rect();
		Window window = activity.getWindow();  
		window.getDecorView().getWindowVisibleDisplayFrame(frame);
		int statusBarHeight = frame.top; 
		System.out.println(" 状态栏的高度: " + statusBarHeight);
		
		// 获取标题栏高度
		// 获取到的view就是程序不包括标题栏的部分
		int contentViewTop = window.findViewById(Window.ID_ANDROID_CONTENT) .getTop();  
	    int titleBarHeight = contentViewTop - statusBarHeight;  
	    System.out.println(" 标题栏的高度: " + titleBarHeight);
 
	    // 获取屏幕 宽度 和 高度
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        int screenHeight  = dm.heightPixels - statusBarHeight - titleBarHeight;
        System.out.println(" 可用屏幕部分的高度： " + screenWidth + ", " + screenHeight);
        return  new int[]{screenWidth, screenHeight };
	}
	
	
	
	/**
	 * 获取屏幕可见范围的宽度 和高度，必须使用 XXView.post(new Runnable(){....}) 才可以获取到真实数据
	 * @param activity
	 * @return
	 */
	public static int[]  getScreenDisplay(Activity activity) {
	    // 获取屏幕 宽度 和 高度
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        int screenHeight  = dm.heightPixels;
        System.out.println(" 屏幕的高度： " + screenWidth + ", " + screenHeight);
        return  new int[]{screenWidth, screenHeight };
	}
	/**
	 * 
	 * 
	 * @param activity 
	 * @param v  滚动条imageview
	 * @param imageId 滚动图片id
	 * @param sum  滚动页卡数目
	 * @return
	 */
	public static int initRoller(Activity activity,ImageView v,int imageId,int sum) {
		// 获取滚动条宽度
        int imageWidth = BitmapFactory.decodeResource(activity.getResources(), imageId).getWidth();
		//获取屏幕高宽
        int screenSize[] = ScreenUtil.getScreenDisplay(activity);
        int screenWidth = screenSize[0];
        //一个页卡距离
        int one = screenWidth/sum;
        // 计算偏移量
        int offset = (one -imageWidth) /2 ; 
        // 使用Matrix 去移动图片
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        v.setImageMatrix(matrix);
		return one;
	}
	
	
}
