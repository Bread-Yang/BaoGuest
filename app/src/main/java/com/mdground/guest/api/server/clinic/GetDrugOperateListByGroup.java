package com.mdground.guest.api.server.clinic;

import android.content.Context;

import com.mdground.guest.api.base.ClinicRequest;
import com.mdground.guest.api.base.RequestCallBack;
import com.mdground.guest.api.base.RequestData;
import com.mdground.guest.enumobject.CheckStatusEnum;
import com.mdground.guest.enumobject.DrugOperateTypeEnum;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author yoghourt
 *
 */
public class GetDrugOperateListByGroup extends ClinicRequest {
	private static final String FUNCTION_NAME = "GetDrugOperateListByGroup";

	public GetDrugOperateListByGroup(Context context) {
		super(context);
	}

	@Override
	protected String getFunctionName() {
		return FUNCTION_NAME;
	}

	public void getDrugOperateListByGroup(int operateGroup, DrugOperateTypeEnum operateType, CheckStatusEnum checkType, RequestCallBack callBack) {
		setRequestCallBack(callBack);

		JSONObject obj = new JSONObject();
		try {
			obj.put("OperateGroup", Integer.valueOf(operateGroup));
			obj.put("OperateType", Integer.valueOf(operateType.getValue()));
			obj.put("CheckStatus", Integer.valueOf(checkType.getValue()));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		RequestData data = getData();
		data.setQueryData(obj.toString());

		pocess();
	}
}
