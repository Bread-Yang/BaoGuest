package com.mdground.guest.activity.searchpatient;


import com.mdground.guest.bean.AppointmentInfo;

public interface SearchPatientPresenter {
	void searchPatient(String keyword, int pageIndex);

	void saveAppointment(AppointmentInfo appointmentInfo);
}
