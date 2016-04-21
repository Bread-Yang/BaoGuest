package com.mdground.guest.activity.searchpatient;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.mdground.guest.R;
import com.mdground.guest.api.base.RequestCallBack;
import com.mdground.guest.api.base.ResponseCode;
import com.mdground.guest.api.base.ResponseData;
import com.mdground.guest.api.server.clinic.SaveAppointment;
import com.mdground.guest.api.server.clinic.SearchPatientList;
import com.mdground.guest.bean.AppointmentInfo;
import com.mdground.guest.bean.Patient;
import com.mdground.guest.constant.MemberConstant;

import org.apache.http.Header;

import java.util.List;

public class SearchPatientPresenterImpl implements SearchPatientPresenter {

	SearchPatientView mView;

	public SearchPatientPresenterImpl(SearchPatientView view) {
		this.mView = view;
	}

	@Override
	public void searchPatient(String keyword, int pageIndex) {
		SearchPatientList searchPatient = new SearchPatientList((Context) mView);
		searchPatient.searchPatient(keyword, pageIndex, new RequestCallBack() {

			@Override
			public void onStart() {
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				mView.showToast(R.string.request_error);
			}

			@Override
			public void onFinish() {
				mView.hideProgress();
			}

			@Override
			public void onSuccess(ResponseData response) {
				if (response != null && response.getCode() == ResponseCode.Normal.getValue()) {
					List<Patient> patients = response.getContent(new TypeToken<List<Patient>>() {
					});
					mView.updateResult(patients);
				} else {
					mView.requestError(response.getCode(), response.getMessage());
				}
			}
		});

	}

	@Override
	public void saveAppointment(AppointmentInfo appointmentInfo) {

		SaveAppointment saveAppointment = new SaveAppointment((Context) mView);
		saveAppointment.saveAppointment(appointmentInfo, new RequestCallBack() {

			@Override
			public void onSuccess(ResponseData response) {
				if (response.getCode() == ResponseCode.Normal.getValue()) {
					AppointmentInfo appointment = response.getContent(AppointmentInfo.class);
					mView.finishResult(MemberConstant.APPIONTMENT_RESULT_CODE, appointment);
				} else {
					mView.requestError(response.getCode(), response.getMessage());
				}
			}

			@Override
			public void onStart() {
				mView.showProgress();
			}

			@Override
			public void onFinish() {
				mView.hideProgress();
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				mView.showToast(R.string.request_error);
			}
		});

	}

}
