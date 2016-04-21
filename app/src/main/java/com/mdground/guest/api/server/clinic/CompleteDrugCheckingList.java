package com.mdground.guest.api.base;

import android.content.Context;

/**
 *
 * @author yoghourt
 *
 */
public class CompleteDrugCheckingList extends ClinicRequest {
	private static final String FUNCTION_NAME = "CompleteDrugCheckingList";

	public CompleteDrugCheckingList(Context context) {
		super(context);
	}

	@Override
	protected String getFunctionName() {
		return FUNCTION_NAME;
	}

	public void completeDrugCheckingList(RequestCallBack callBack) {
		setRequestCallBack(callBack);

		RequestData data = getData();

		pocess();
	}
}
