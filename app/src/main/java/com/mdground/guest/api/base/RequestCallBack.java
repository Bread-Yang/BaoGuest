package com.mdground.guest.api.base;

import org.apache.http.Header;

public interface RequestCallBack {
	public void onStart();

	public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable);

	public void onFinish();

	public void onSuccess(com.mdground.guest.api.base.ResponseData response);
}
