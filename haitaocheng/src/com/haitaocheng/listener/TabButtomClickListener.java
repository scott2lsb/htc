package com.haitaocheng.listener;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import android.view.View;
import android.view.View.OnClickListener;

public class TabButtomClickListener implements OnClickListener {
	
	private String url; //请求地址
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		doGet(v);
	}
	
	
	
	public void doGet(View view) {
	        //RequestParams params = new RequestParams();
	        //params.addHeader("name", "value");
	        //params.addQueryStringParameter("name", "value");
	        HttpUtils http = new HttpUtils();
	        //设置超时请求时间
	        http.configCurrentHttpCacheExpiry(1000 * 10);
	        http.send(HttpRequest.HttpMethod.GET,url,
	        		//请求回调
	                new RequestCallBack<String>() {

	                    @Override
	                    public void onStart() {
	                    	//开始
	                    }

	                    @Override
	                    public void onLoading(long total, long current, boolean isUploading) {
	                        //加载
	                    }

	                    @Override
	                    public void onSuccess(ResponseInfo<String> responseInfo) {
//	                        resultText.setText("response:" + responseInfo.result);
	                    	//加载完后调用
	                    }


	                    @Override
	                    public void onFailure(HttpException error, String msg) {
	                    	//加载失败
	                    }
	                });
	    }
}
