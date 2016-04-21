package com.mdground.guest.activity.searchpatient;

import com.mdground.guest.activity.base.BaseView;
import com.mdground.guest.bean.AppointmentInfo;
import com.mdground.guest.bean.Patient;

import java.util.List;

public interface SearchPatientView extends BaseView {

	void updateResult(List<Patient> patients);

	void finishResult(int appiontmentResultCode, AppointmentInfo appointment);

}
