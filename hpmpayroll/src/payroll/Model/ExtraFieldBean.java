package payroll.Model;

public class ExtraFieldBean 
{
int FieldNo;
String FieldName;
String FieldDesc;
int type;
String ColumnName;
String Code;
String EmpName;
int empno;
String date;
public int getEmpno() {
	return empno;
}
public void setEmpno(int empno) {
	this.empno = empno;
}
public String getDate() {
	return date;
}
public void setDate(String date) {
	this.date = date;
}
float TotalIncome;
float Totaldec;
float NetPay;

	public String getCode() {
	return Code;
}
public void setCode(String code) {
	Code = code;
}
public String getEmpName() {
	return EmpName;
}
public void setEmpName(String empName) {
	EmpName = empName;
}
public float getTotalIncome() {
	return TotalIncome;
}
public void setTotalIncome(float totalIncome) {
	TotalIncome = totalIncome;
}
public float getTotaldec() {
	return Totaldec;
}
public void setTotaldec(float totaldec) {
	Totaldec = totaldec;
}
public float getNetPay() {
	return NetPay;
}
public void setNetPay(float netPay) {
	NetPay = netPay;
}
	public int getFieldNo() {
		return FieldNo;
	}
	public void setFieldNo(int fieldNo) {
		FieldNo = fieldNo;
	}
	public String getFieldName() {
		return FieldName;
	}
	public void setFieldName(String fieldName) {
		FieldName = fieldName;
	}
	public String getFieldDesc() {
		return FieldDesc;
	}
	public void setFieldDesc(String fieldDesc) {
		FieldDesc = fieldDesc;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getColumnName() {
		return ColumnName;
	}
	public void setColumnName(String columnName) {
		ColumnName = columnName;
	}

}
