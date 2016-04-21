package com.mdground.guest.db.dao.inter;

import java.util.List;

import com.mdground.guest.bean.Employee;

public interface IEmployeeDao {

	public void saveEmployeess(List<Employee> employees);

	public void deleteAll();
}
