package com.mdground.guest.api.server.global;

import android.content.Context;
import android.util.Log;

import com.mdground.guest.api.base.GlobalRequest;
import com.mdground.guest.api.base.RequestCallBack;
import com.mdground.guest.api.base.RequestData;

public class CheckEmployeePassword extends GlobalRequest {

	private static final String FUNCTION_NAME = "CheckEmployeePassword";

	public CheckEmployeePassword(Context context) {
		super(context);
	}

	@Override
	protected String getFunctionName() {
		return FUNCTION_NAME;
	}

	public void checkEmployeePwd(String password, RequestCallBack listener) {
		if (password == null || password.equals("")) {
			Log.e(TAG, "password is null");
			return;
		}
		setRequestCallBack(listener);

		RequestData data = getData();
		data.setQueryData(password);

		pocess();
	}

}
