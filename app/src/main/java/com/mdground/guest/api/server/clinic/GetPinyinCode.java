package com.mdground.guest.api.server.clinic;

import android.content.Context;

import com.mdground.guest.api.base.ClinicRequest;
import com.mdground.guest.api.base.RequestCallBack;
import com.mdground.guest.api.base.RequestData;

/**
 * 获取拼字简码
 * 
 * @author yoghourt
 *
 */
public class GetPinyinCode extends ClinicRequest {
	private static final String FUNCTION_NAME = "GetPinyinCode";

	public GetPinyinCode(Context context) {
		super(context);
	}

	@Override
	protected String getFunctionName() {
		return FUNCTION_NAME;
	}

	public void getPinyinCode(String drugName, RequestCallBack callBack) {
		setRequestCallBack(callBack);

		RequestData data = getData();
		data.setQueryData(String.valueOf(drugName));

		pocess();
	}
}
