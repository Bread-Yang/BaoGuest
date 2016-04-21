package com.mdground.guest.api.server.clinic;

import android.content.Context;

import com.mdground.guest.api.base.ClinicRequest;
import com.mdground.guest.api.base.RequestCallBack;
import com.mdground.guest.api.base.RequestData;

/**
 * 获取药品详情
 * 
 * @author yoghourt
 *
 */
public class GetDrugInfoByID extends ClinicRequest {
	private static final String FUNCTION_NAME = "GetDrugInfoByID";

	public GetDrugInfoByID(Context context) {
		super(context);
	}

	@Override
	protected String getFunctionName() {
		return FUNCTION_NAME;
	}

	public void getDrugInfoByID(int drugID, RequestCallBack callBack) {
		setRequestCallBack(callBack);

		RequestData data = getData();
		data.setQueryData(String.valueOf(drugID));

		pocess();
	}
}
