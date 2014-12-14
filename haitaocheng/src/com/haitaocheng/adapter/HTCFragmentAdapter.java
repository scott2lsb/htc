package com.haitaocheng.adapter;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class HTCFragmentAdapter extends FragmentPagerAdapter{
	ArrayList<Fragment> list;
	public HTCFragmentAdapter(FragmentManager fm, ArrayList<Fragment> list) {
		// TODO Auto-generated constructor stub
		super(fm);		// 必须调用
		this.list = list;
	}


	@Override
	public Fragment getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public int getCount() {
		return list.size();
	}
}
