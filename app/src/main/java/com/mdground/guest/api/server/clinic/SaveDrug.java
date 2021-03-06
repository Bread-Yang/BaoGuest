package com.mdground.guest.api.server.clinic;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mdground.guest.api.base.ClinicRequest;
import com.mdground.guest.api.base.RequestCallBack;
import com.mdground.guest.api.base.RequestData;
import com.mdground.guest.bean.Drug;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 保存药名
 *
 * @author yoghourt
 */
public class SaveDrug extends ClinicRequest {
    private static final String FUNCTION_NAME = "SaveDrug";

    public SaveDrug(Context context) {
        super(context);
    }

    @Override
    protected String getFunctionName() {
        return FUNCTION_NAME;
    }


    public void saveDrug(Drug drug, RequestCallBack callBack) {
        if (drug == null) {
            return;
        }

        setRequestCallBack(callBack);

        JSONObject obj = new JSONObject();
        try {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
            String drugString = gson.toJson(drug);

            obj.put("Drug", new JSONObject(drugString));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestData data = getData();
        data.setQueryData(obj.toString());

        pocess();
    }
}
