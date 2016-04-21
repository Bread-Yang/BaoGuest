package com.mdground.guest.api.server.clinic;

import android.content.Context;

import com.mdground.guest.api.base.ClinicRequest;
import com.mdground.guest.api.base.RequestCallBack;
import com.mdground.guest.api.base.RequestData;

/**
 * 根据二维码获取药物详情
 * 
 * @author yoghourt
 *
 */
public class GetDrugInfoByBarcode extends ClinicRequest {
	private static final String FUNCTION_NAME = "GetDrugInfoByBarcode";

	public GetDrugInfoByBarcode(Context context) {
		super(context);
	}

	@Override
	protected String getFunctionName() {
		return FUNCTION_NAME;
	}

	public void getDurgInfoByBarcode(String barCode, RequestCallBack callBack) {
		setRequestCallBack(callBack);

		RequestData data = getData();
		data.setQueryData(barCode);

		pocess();
	}
}
