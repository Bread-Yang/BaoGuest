package com.mdground.guest.api.server.clinic;

import android.content.Context;

import com.mdground.guest.api.base.ClinicRequest;
import com.mdground.guest.api.base.RequestCallBack;
import com.mdground.guest.api.base.RequestData;

/**
 * 获取诊所信息
 *
 * @author yoghourt
 *
 */
public class GetClinic extends ClinicRequest {
	private static final String FUNCTION_NAME = "GetClinic";

	public GetClinic(Context context) {
		super(context);
	}

	@Override
	protected String getFunctionName() {
		return FUNCTION_NAME;
	}

	public void getClinic(RequestCallBack callBack) {
		setRequestCallBack(callBack);

		RequestData data = getData();

		pocess();
	}
}
