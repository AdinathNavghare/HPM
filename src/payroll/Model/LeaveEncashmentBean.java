package payroll.Model;

public class LeaveEncashmentBean {
	int empNo;
	int site_id;
	float leaveBal;
	float maxLimit;
	float encashApplicable;
	float monthlyGross;
	float esicAmt;
	float encashmentAmt;
	String status;
	int encashmentApplNo;
	float leaveEncashmentSanction;
	String leaveEncashmentDate;
	String fromDate;
	String toDate;
	
	
	public String getLeaveEncashmentDate() {
		return leaveEncashmentDate;
	}
	public void setLeaveEncashmentDate(String leaveEncashmentDate) {
		this.leaveEncashmentDate = leaveEncashmentDate;
	}
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	public float getLeaveEncashmentSanction() {
		return leaveEncashmentSanction;
	}
	public void setLeaveEncashmentSanction(float leaveEncashmentSanction) {
		this.leaveEncashmentSanction = leaveEncashmentSanction;
	}
	public int getEmpNo() {
		return empNo;
	}
	public void setEmpNo(int empNo) {
		this.empNo = empNo;
	}
	public int getSite_id() {
		return site_id;
	}
	public void setSite_id(int site_id) {
		this.site_id = site_id;
	}
	public float getLeaveBal() {
		return leaveBal;
	}
	public void setLeaveBal(float leaveBal) {
		this.leaveBal = leaveBal;
	}
	public float getMaxLimit() {
		return maxLimit;
	}
	public void setMaxLimit(float maxLimit) {
		this.maxLimit = maxLimit;
	}
	public float getEncashApplicable() {
		return encashApplicable;
	}
	public void setEncashApplicable(float encashApplicable) {
		this.encashApplicable = encashApplicable;
	}
	public float getMonthlyGross() {
		return monthlyGross;
	}
	public void setMonthlyGross(float monthlyGross) {
		this.monthlyGross = monthlyGross;
	}
	public float getEsicAmt() {
		return esicAmt;
	}
	public void setEsicAmt(float esicAmt) {
		this.esicAmt = esicAmt;
	}
	public float getEncashmentAmt() {
		return encashmentAmt;
	}
	public void setEncashmentAmt(float encashmentAmt) {
		this.encashmentAmt = encashmentAmt;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getEncashmentApplNo() {
		return encashmentApplNo;
	}
	public void setEncashmentApplNo(int encashmentApplNo) {
		this.encashmentApplNo = encashmentApplNo;
	}
	
}
