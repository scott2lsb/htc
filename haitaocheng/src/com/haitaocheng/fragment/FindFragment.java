package com.haitaocheng.fragment;

import java.util.ArrayList;

import com.google.common.collect.Lists;
import com.haitaocheng.www.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class FindFragment extends BaseFragment {
	private View mView;
	private ArrayList<String> tabStrs;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mView = super.onCreateView(inflater, container, savedInstanceState);
//		ViewUtils.inject(this,mView);
		tabStrs = Lists.newArrayList("热门","潮品快讯","买家推荐","更多");
		setTabText(tabStrs);  //设置Tab的文字
		setAdVisivible(false);//设置广告位隐藏
		View contentView = inflater.inflate(R.layout.column_subscription, null);
		pwMyPopWindow.setContentView(contentView);
		return mView;
	}
	

}
