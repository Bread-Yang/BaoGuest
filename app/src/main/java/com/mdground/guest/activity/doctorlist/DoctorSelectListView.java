package com.mdground.guest.activity.doctorlist;


import com.mdground.guest.activity.base.BaseView;
import com.mdground.guest.api.base.DoctorWaittingCount;
import com.mdground.guest.bean.AppointmentInfo;
import com.mdground.guest.bean.Doctor;

import java.util.List;

public interface DoctorSelectListView extends BaseView {

	public void finishResult(int resultCode, AppointmentInfo appointmentInfo);

	public void updateDoctorList(List<Doctor> doctorsList);

	public void updateWaitingCount(List<DoctorWaittingCount> waittingCount);
}
