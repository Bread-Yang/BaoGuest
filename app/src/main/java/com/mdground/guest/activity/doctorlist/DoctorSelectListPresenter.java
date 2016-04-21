package com.mdground.guest.activity.doctorlist;


import com.mdground.guest.bean.AppointmentInfo;
import com.mdground.guest.bean.Doctor;

import java.util.List;

public interface DoctorSelectListPresenter {

	public void saveAppointment(AppointmentInfo appointment);
	
	//获取医生列表
	public void getDoctorList();

	public void assignAppointment(AppointmentInfo appointmentInfo, int doctorId);
	
	//获取医生候诊人数
	public void getWaitingCountForDoctorList(List<Doctor> doctors);
}
