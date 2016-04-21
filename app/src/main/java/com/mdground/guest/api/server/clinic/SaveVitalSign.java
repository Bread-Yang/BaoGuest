package com.mdground.guest.api.server.clinic;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.mdground.guest.api.base.ClinicRequest;
import com.mdground.guest.api.base.RequestCallBack;
import com.mdground.guest.api.base.RequestData;
import com.mdground.guest.bean.VitalSigns;

import android.content.Context;

/**
 * 保存体征
 * 
 * @author yoghourt
 *
 */
public class SaveVitalSign extends ClinicRequest {
	private static final String FUNCTION_NAME = "SaveVitalSign";

	public SaveVitalSign(Context context) {
		super(context);
	}

	@Override
	protected String getFunctionName() {
		return FUNCTION_NAME;
	}

	public void saveVitalSign(VitalSigns vitalSigns, RequestCallBack callBack) {
		if (vitalSigns == null) {
			return;
		}

		setRequestCallBack(callBack);

		JSONObject obj = new JSONObject();
		try {
			obj.put("VitalSign", new JSONObject(new Gson().toJson(vitalSigns)));

		} catch (JSONException e) {
			e.printStackTrace();
		}

		RequestData data = getData();
		data.setQueryData(obj.toString());

		pocess();
	}
}
