package com.mdground.guest.api.server.global;

import android.content.Context;

import com.mdground.guest.api.base.GlobalRequest;
import com.mdground.guest.api.base.RequestCallBack;

public class GetDoctorList extends GlobalRequest {
	private static final String FUNCTION_NAME = "GetDoctorList";

	public GetDoctorList(Context context) {
		super(context);
	}

	@Override
	protected String getFunctionName() {
		return FUNCTION_NAME;
	}


	public void getDoctorList(RequestCallBack requestCallBack) {
		setRequestCallBack(requestCallBack);
		pocess();
	}

}
