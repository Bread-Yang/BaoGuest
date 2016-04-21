package com.mdground.guest.api.server.clinic;

import android.content.Context;

import com.mdground.guest.MedicalAppliction;
import com.mdground.guest.api.base.ClinicRequest;
import com.mdground.guest.api.base.RequestCallBack;
import com.mdground.guest.api.base.RequestData;
import com.mdground.guest.bean.Employee;
import com.mdground.guest.enumobject.CheckStatusEnum;
import com.mdground.guest.enumobject.DrugOperateTypeEnum;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author yoghourt
 *
 */
public class GetDrugOperateListByAudit extends ClinicRequest {
	private static final String FUNCTION_NAME = "GetDrugOperateListByAudit";

	public GetDrugOperateListByAudit(Context context) {
		super(context);
	}

	@Override
	protected String getFunctionName() {
		return FUNCTION_NAME;
	}

	public void getDrugOperateListByAudit(int pageIndex, DrugOperateTypeEnum operateType, CheckStatusEnum checkType, RequestCallBack callBack) {
		setRequestCallBack(callBack);

		JSONObject obj = new JSONObject();
		try {
			obj.put("PageIndex", String.valueOf(pageIndex));
			obj.put("PageSize", 10);
			obj.put("OperateType", String.valueOf(operateType.getValue()));
			obj.put("CheckStatus", String.valueOf(checkType.getValue()));
			if ((MedicalAppliction.mLoginEmployee.getEmployeeRole() & Employee.AUDIT_MANAGER) == 0) {
				obj.put("OperatorID", MedicalAppliction.mLoginEmployee.getEmployeeID());
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		RequestData data = getData();
		data.setQueryData(obj.toString());

		pocess();
	}
}
