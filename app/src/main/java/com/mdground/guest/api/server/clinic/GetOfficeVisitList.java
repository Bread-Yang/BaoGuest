package com.mdground.guest.api.server.clinic;

import android.content.Context;

import com.mdground.guest.api.base.ClinicRequest;
import com.mdground.guest.api.base.RequestCallBack;
import com.mdground.guest.api.base.RequestData;

/**
 * 既往史
 * 
 * @author yoghourt
 *
 */
public class GetOfficeVisitList extends ClinicRequest {
	private static final String FUNCTION_NAME = "GetOfficeVisitList";

	public GetOfficeVisitList(Context context) {
		super(context);
	}

	@Override
	protected String getFunctionName() {
		return FUNCTION_NAME;
	}

	public void getOfficeVisitList(int patientId, RequestCallBack callBack) {
		setRequestCallBack(callBack);

		RequestData data = getData();
		data.setQueryData(String.valueOf(patientId));

		pocess();
	}
}
