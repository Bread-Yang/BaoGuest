package com.mdground.guest.api.server.clinic;

import android.content.Context;

import com.mdground.guest.api.base.ClinicRequest;
import com.mdground.guest.api.base.RequestCallBack;

/**
 * 费用类型列表
 * 
 * @author yoghourt
 *
 */
public class GetFeeTemplateList extends ClinicRequest {
	private static final String FUNCTION_NAME = "GetFeeTemplateList";

	public GetFeeTemplateList(Context context) {
		super(context);
	}

	@Override
	protected String getFunctionName() {
		return FUNCTION_NAME;
	}

	public void getFeeTemplateList(RequestCallBack callBack) {
		setRequestCallBack(callBack);

		pocess();
	}
}
