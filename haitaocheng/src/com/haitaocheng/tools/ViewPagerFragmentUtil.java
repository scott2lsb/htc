package com.haitaocheng.tools;

import java.util.ArrayList;

import com.haitaocheng.constants.Cons;
import com.haitaocheng.www.R;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;

public class ViewPagerFragmentUtil {
	private ViewPager viewPager;
	private int one;  //一个叶卡
	private PagerAdapter pagerAdapter;
	private int limit;
	protected int currentIndex;
	private FragmentActivity activity;
	private ArrayList<Button> buttons;
	private ImageView roller;
	private ArrayList<Fragment> fragmentList;
	
	public ViewPagerFragmentUtil() {
		
	}
	
	public ViewPagerFragmentUtil(FragmentActivity activity,ViewPager viewPager,final ArrayList<Button> buttons,ArrayList<Fragment> fragmentList,ImageView roller) {
		this.activity = activity;
		this.viewPager = viewPager;
		this.buttons = buttons;
		this.roller = roller;
		this.fragmentList = fragmentList;
	}
	
	public void onViewPager() {
		// TODO Auto-generated method stub
				//1.初始化滚动条，获取一个页卡距离
				one = ScreenUtil.initRoller(activity, roller, R.drawable.roller, fragmentList.size());
				//2.ViewPager设置适配器
				viewPager.setAdapter(pagerAdapter);
				// 设置缓存页面，当前页面的相邻N个页面都会被缓存
				viewPager.setOffscreenPageLimit(limit);
				// 设置默认的当前页
				viewPager.setCurrentItem(0); 
				 //为Button设置点击监听器
		  		for (int i = 0; i < buttons.size(); i++) {
		  			buttons.get(i).setOnClickListener(new TabClickListener(i));
		  		}
			    // ============ 2 设置页面切换监听器===========
		        viewPager.setOnPageChangeListener(new OnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						int size = buttons.size();
						for (int i = 0; i < size; i++) {
							buttons.get(i).setTextColor(Cons.BLACK);
							if (i==position) {
								buttons.get(i).setTextColor(Cons.YELLOW);
							}
						}
						
						Animation animation = new TranslateAnimation(one*currentIndex, one*position, 0, 0);
						currentIndex = position;
						animation.setFillAfter(true);
						animation.setDuration(300);
						roller.startAnimation(animation);
					}
					
					@Override
					public void onPageScrolled(int arg0, float arg1, int arg2) {
						// 当前页面被滑动时调用
						
					}
					
					@Override
					public void onPageScrollStateChanged(int arg0) {
						// 当前状态改变时调用
					}
				});
	}

	public void setPagerAdapter(PagerAdapter pagerAdapter) {
		this.pagerAdapter = pagerAdapter;
	}
	
	public void setOffscreenPageLimit(int limit) {
		this.limit = limit;
	}
	
	//按钮点击事件
	class TabClickListener implements OnClickListener{
		int index = 0;
		   
		public TabClickListener(int i) {
			this.index = i;
		}
		
		@Override
		public void onClick(View v) {
			// 设置为当前的选项卡，  会引起 页面切换监听
			viewPager.setCurrentItem(index);
		}
		
	}
}
