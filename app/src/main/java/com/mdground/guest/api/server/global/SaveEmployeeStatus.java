package com.mdground.guest.api.server.global;

import android.content.Context;

import com.mdground.guest.api.base.GlobalRequest;
import com.mdground.guest.api.base.RequestCallBack;
import com.mdground.guest.api.base.RequestData;
import com.mdground.guest.bean.Employee;
import com.mdground.guest.util.DateUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class SaveEmployeeStatus extends GlobalRequest {
	private static final String FUNCTION_NAME = "SaveEmployeeStatus";

	public SaveEmployeeStatus(Context context) {
		super(context);
	}

	@Override
	protected String getFunctionName() {
		return FUNCTION_NAME;
	}

	public void saveEmployeeStatus(Employee employee, RequestCallBack listener) {
		if (employee == null) {
			return;
		}

		setRequestCallBack(listener);

		JSONObject obj = new JSONObject();
		try {
			obj.put("EmployeeID", employee.getEmployeeID());
            if (employee.getExpiredTime() != null) {
                obj.put("ExpiredTime", DateUtils.getDateStringBySpecificDate(employee.getExpiredTime()));
            }
			obj.put("Status", employee.getStatus());
		} catch (JSONException e) {
			e.printStackTrace();
		}

		RequestData data = getData();
		data.setQueryData(obj.toString());

		pocess();
	}

}
