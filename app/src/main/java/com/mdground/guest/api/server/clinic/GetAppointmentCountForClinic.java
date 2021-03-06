package com.mdground.guest.api.server.clinic;

import android.content.Context;

import com.mdground.guest.api.base.ClinicRequest;
import com.mdground.guest.api.base.RequestCallBack;
import com.mdground.guest.api.base.RequestData;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author yoghourt
 *
 */
public class GetAppointmentCountForClinic extends ClinicRequest {
	private static final String FUNCTION_NAME = "GetAppointmentCountForClinic";

	public GetAppointmentCountForClinic(Context context) {
		super(context);
	}

	@Override
	protected String getFunctionName() {
		return FUNCTION_NAME;
	}

	public void getAppointmentCountForClinic(Date beginDate, Date endDate, RequestCallBack callBack) {
		setRequestCallBack(callBack);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00", Locale.CHINA);

		JSONObject obj = new JSONObject();
		try {
			obj.put("BeginDate", sdf.format(beginDate));
			obj.put("EndDate", sdf.format(endDate));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		RequestData data = getData();
		data.setQueryData(obj.toString());
		pocess();
	}
}
