package com.mdground.guest.api.server.clinic;

import android.content.Context;

import com.mdground.guest.api.base.ClinicRequest;
import com.mdground.guest.api.base.RequestCallBack;

/**
 * 药品类型列表
 * 
 * @author yoghourt
 *
 */
public class GetDrugTypeList extends ClinicRequest {
	private static final String FUNCTION_NAME = "GetDrugTypeList";

	public GetDrugTypeList(Context context) {
		super(context);
	}

	@Override
	protected String getFunctionName() {
		return FUNCTION_NAME;
	}

	public void getDrugTypeList(RequestCallBack callBack) {
		setRequestCallBack(callBack);

		pocess();
	}
}
