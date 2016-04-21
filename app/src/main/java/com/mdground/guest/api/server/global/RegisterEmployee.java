package com.mdground.guest.api.server.global;

import android.content.Context;

import com.mdground.guest.api.base.GlobalRequest;
import com.mdground.guest.api.base.RequestCallBack;
import com.mdground.guest.api.base.RequestData;
import com.mdground.guest.bean.Employee;
import com.mdground.guest.util.DateUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author yoghourt
 */
public class RegisterEmployee extends GlobalRequest {
	private static final String FUNCTION_NAME = "RegisterEmployee";

	public RegisterEmployee(Context context) {
		super(context);
	}

	@Override
	protected String getFunctionName() {
		return FUNCTION_NAME;
	}

	public void registerEmployee(Employee employee, RequestCallBack callBack) {
		if (employee == null) {
			return;
		}
		
		setRequestCallBack(callBack);

		JSONObject obj = new JSONObject();
		try {
			obj.put("DOB", DateUtils.getDateStringBySpecificDate(employee.getDOB()));
			obj.put("EmployeeName", employee.getEmployeeName());
			obj.put("EmployeeRole", employee.getEmployeeRole());
			obj.put("EMRType", employee.getEMRType());
			obj.put("Gender", employee.getGender());
			obj.put("LoginID", employee.getLoginID());
			obj.put("LoginPwd", employee.getLoginPwd());
			obj.put("WorkPhone", employee.getWorkPhone());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		RequestData data = getData();

		data.setQueryData(obj.toString());

		pocess();
	}
}

