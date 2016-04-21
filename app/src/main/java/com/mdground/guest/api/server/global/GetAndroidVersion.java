package com.mdground.guest.api.server.global;

import android.content.Context;

import com.mdground.guest.api.base.GlobalRequest;
import com.mdground.guest.api.base.RequestCallBack;
import com.mdground.guest.api.base.RequestData;

/**
 *
 * @author yoghourt
 *
 */
public class GetAndroidVersion extends GlobalRequest {
	private static final String FUNCTION_NAME = "GetAndroidVersion";

	public GetAndroidVersion(Context context) {
		super(context);
	}

	@Override
	protected String getFunctionName() {
		return FUNCTION_NAME;
	}

	public void getAndroidVersion(String version, RequestCallBack callBack) {
		setRequestCallBack(callBack);

        RequestData data = getData();
		data.setQueryData(version);

		pocess();
	}
}
