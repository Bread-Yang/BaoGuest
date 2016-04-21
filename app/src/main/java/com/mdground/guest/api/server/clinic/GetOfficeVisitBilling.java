package com.mdground.guest.api.server.clinic;

import android.content.Context;

import com.mdground.guest.api.base.ClinicRequest;
import com.mdground.guest.api.base.RequestCallBack;
import com.mdground.guest.api.base.RequestData;

/**
 *
 * @author yoghourt
 *
 */
public class GetOfficeVisitBilling extends ClinicRequest {
	private static final String FUNCTION_NAME = "GetOfficeVisitBilling";

	public GetOfficeVisitBilling(Context context) {
		super(context);
	}

	@Override
	protected String getFunctionName() {
		return FUNCTION_NAME;
	}

	public void getOfficeVisitBilling(int visitId, RequestCallBack callBack) {
		setRequestCallBack(callBack);

		RequestData data = getData();
		data.setQueryData(String.valueOf(visitId));

		pocess();
	}
}
