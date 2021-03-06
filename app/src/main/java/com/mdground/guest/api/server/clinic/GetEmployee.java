package com.mdground.guest.api.server.clinic;

import android.content.Context;

import com.mdground.guest.api.base.ClinicRequest;
import com.mdground.guest.api.base.RequestCallBack;
import com.mdground.guest.api.base.RequestData;

import org.json.JSONException;
import org.json.JSONObject;

public class GetEmployee extends ClinicRequest {
	private static final String FUNCTION_NAME = "GetEmployee";

	public GetEmployee(Context context) {
		super(context);
	}

	@Override
	protected String getFunctionName() {
		return FUNCTION_NAME;
	}

	public void getEmployee(int employeeID, RequestCallBack callBack) {
		setRequestCallBack(callBack);
		JSONObject obj = new JSONObject();
		try {
			obj.put("EmployeeID", String.valueOf(employeeID));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		RequestData data = getData();
		data.setQueryData(obj.toString());

		pocess();
	}
}
