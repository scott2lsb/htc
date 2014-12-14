package com.haitaocheng.fragment;


import java.util.ArrayList;

import com.google.common.collect.Lists;
import com.haitaocheng.www.R;
import com.lidroid.xutils.ViewUtils;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ViewPagerFragment1 extends BaseViewPagerFragment {
	private View mView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mView = super.onCreateView(inflater, container, savedInstanceState);
//		setAdVisivible(false);//设置广告位隐藏
		return mView;
	}
}
