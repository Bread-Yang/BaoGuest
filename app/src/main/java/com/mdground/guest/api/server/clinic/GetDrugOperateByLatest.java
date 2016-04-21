package com.mdground.guest.api.server.clinic;

import android.content.Context;

import com.mdground.guest.api.base.ClinicRequest;
import com.mdground.guest.api.base.RequestCallBack;
import com.mdground.guest.api.base.RequestData;
import com.mdground.guest.enumobject.DrugOperateTypeEnum;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author yoghourt
 *
 */
public class GetDrugOperateByLatest extends ClinicRequest {
	private static final String FUNCTION_NAME = "GetDrugOperateByLatest";

	public GetDrugOperateByLatest(Context context) {
		super(context);
	}

	@Override
	protected String getFunctionName() {
		return FUNCTION_NAME;
	}

	public void getDrugOperateByLatest(int drugID, DrugOperateTypeEnum operateType, RequestCallBack callBack) {
		setRequestCallBack(callBack);

		JSONObject obj = new JSONObject();
		try {
			obj.put("DrugID", String.valueOf(drugID));
			obj.put("OperateType", String.valueOf(operateType.getValue()));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		RequestData data = getData();
		data.setQueryData(obj.toString());

		pocess();
	}
}
