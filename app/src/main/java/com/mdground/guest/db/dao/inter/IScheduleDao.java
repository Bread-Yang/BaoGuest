package com.mdground.guest.db.dao.inter;

import java.util.Date;
import java.util.List;

import com.mdground.guest.bean.Schedule;

public interface IScheduleDao {

	// 根据日期和员工获取对应的排班表数据
	public List<Schedule> getSchedules(Date workDate, int employeeId);

	public void saveSchedules(List<Schedule> schedules);
}
