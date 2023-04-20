package payroll.Model;

public class EncashmentBean {

	private int empno;
	public int getEmpno() {
		return empno;
	}
	public void setEmpno(int empno) {
		this.empno = empno;
	}
	public int getNoOfDays() {
		return NoOfDays;
	}
	public void setNoOfDays(int noOfDays) {
		NoOfDays = noOfDays;
	}
	public float getEncashment() {
		return Encashment;
	}
	public void setEncashment(float encashment) {
		Encashment = encashment;
	}
	private int NoOfDays;
	private float Encashment;
	
	private int LeaveOnHO;
	public int getLeaveOnHO() {
		return LeaveOnHO;
	}
	public void setLeaveOnHO(int leaveOnHO) {
		LeaveOnHO = leaveOnHO;
	}
	public int getLeaveOnOS() {
		return LeaveOnOS;
	}
	public void setLeaveOnOS(int leaveOnOS) {
		LeaveOnOS = leaveOnOS;
	}
	public float getBasic() {
		return basic;
	}
	public void setBasic(float basic) {
		this.basic = basic;
	}
	private int LeaveOnOS;
	private float basic;
	
	private int EmpCode;
	public int getEmpCode() {
		return EmpCode;
	}
	public void setEmpCode(int empCode) {
		EmpCode = empCode;
	}
	
	private int OnHo;
	public int getOnHo() {
		return OnHo;
	}
	public void setOnHo(int onHo) {
		OnHo = onHo;
	}
	public int getOnOsite() {
		return OnOsite;
	}
	public void setOnOsite(int onOsite) {
		OnOsite = onOsite;
	}
	private int OnOsite;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	private String name;
	
	private String Year;
	public String getYear() {
		return Year;
	}
	public void setYear(String year) {
		Year = year;
	}
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
	private String Status;
	
	
	
private int Prj_srno;
public int getPrj_srno() {
	return Prj_srno;
}
public void setPrj_srno(int prj_srno) {
	Prj_srno = prj_srno;
}
public String getPrj_code() {
	return Prj_code;
}
public void setPrj_code(String prj_code) {
	Prj_code = prj_code;
}
public String getPrj_name() {
	return Prj_name;
}
public void setPrj_name(String prj_name) {
	Prj_name = prj_name;
}
public String getSite_name() {
	return Site_name;
}
public void setSite_name(String site_name) {
	Site_name = site_name;
}
public String getSite_feild3() {
	return Site_feild3;
}
public void setSite_feild3(String site_feild3) {
	Site_feild3 = site_feild3;
}
private String Prj_code;
private String Prj_name;
private String Site_name;
private String Site_feild3;
	

	}

