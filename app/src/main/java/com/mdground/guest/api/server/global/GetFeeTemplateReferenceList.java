package com.mdground.guest.api.server.global;

import android.content.Context;

import com.mdground.guest.api.base.GlobalRequest;
import com.mdground.guest.api.base.RequestCallBack;

public class GetFeeTemplateReferenceList extends GlobalRequest {

	private static final String FUNCTION_NAME = "GetFeeTemplateReferenceList";

	public GetFeeTemplateReferenceList(Context context) {
		super(context);
	}

	@Override
	protected String getFunctionName() {
		return FUNCTION_NAME;
	}

	public void getFeeTemplateReferenceList(RequestCallBack requestCallBack) {
		setRequestCallBack(requestCallBack);
		pocess();
	}

}
