package com.haitaocheng.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
/**
 * 控制是否滑动的ViewPager
 * @author admin
 *
 */
public class ControlScrollViewPager extends ViewPager {
	private boolean scrollable;

	public ControlScrollViewPager(Context context) {  
        super(context);  
    }  
  
    public ControlScrollViewPager(Context context, AttributeSet attrs) {  
        super(context, attrs);  
    }  
  
    public void setScrollable(boolean enable) {  
        scrollable = enable;  
    }  
  
    @Override  
    public boolean onInterceptTouchEvent(MotionEvent event) {  
        if (scrollable) {  
            return super.onInterceptTouchEvent(event);  
        } else {  
            return false;  
        }  
    }  
}
