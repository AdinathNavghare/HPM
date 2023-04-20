package payroll.Model;

public class LeaveMassBean
{
	
	String EFFDATE;
	int LEAVECD;
	String FREQUENCY;
	float CRLIM;
	float MAXCUMLIM;
	float  MAXCF;
	float MINLIM;
	String FBEGINDATE;
	String FENDDATE;
	String LEAVEDES;
	String CONS_HOLIDAYS;
	String LEAVEINCASH;
	String WEEKOFF;
	
	public String getWEEKOFF() {
		return WEEKOFF;
	}
	public void setWEEKOFF(String wEEKOFF) {
		WEEKOFF = wEEKOFF;
	}
	int SRNO;
	
	public String getEFFDATE() {
		return EFFDATE;
	}
	public void setEFFDATE(String eFFDATE) {
		EFFDATE = eFFDATE;
	}
	
	public int getLEAVECD() {
		return LEAVECD;
	}
	public void setLEAVECD(int lEAVECD) {
		LEAVECD = lEAVECD;
	}
	public String getFREQUENCY() {
		return FREQUENCY;
	}
	public void setFREQUENCY(String fREQUENCY) {
		FREQUENCY = fREQUENCY;
	}

	public float getCRLIM() {
		return CRLIM;
	}
	public void setCRLIM(float cRLIM) {
		CRLIM = cRLIM;
	}
	
	
	public float getMAXCUMLIM() {
		return MAXCUMLIM;
	}
	public void setMAXCUMLIM(float mAXCUMLIM) {
		MAXCUMLIM = mAXCUMLIM;
	}
	public float getMAXCF() {
		return MAXCF;
	}
	public void setMAXCF(float mAXCF) {
		MAXCF = mAXCF;
	}
	public float getMINLIM() {
		return MINLIM;
	}
	public void setMINLIM(float mINLIM) {
		MINLIM = mINLIM;
	}
	public String getFBEGINDATE() {
		return FBEGINDATE;
	}
	public void setFBEGINDATE(String fBEGINDATE) {
		FBEGINDATE = fBEGINDATE;
	}
	public String getFENDDATE() {
		return FENDDATE;
	}
	public void setFENDDATE(String fENDDATE) {
		FENDDATE = fENDDATE;
	}
	public String getLEAVEDES() {
		return LEAVEDES;
	}
	public void setLEAVEDES(String lEAVEDES) {
		LEAVEDES = lEAVEDES;
	}
	public String getCONS_HOLIDAYS() {
		return CONS_HOLIDAYS;
	}
	public void setCONS_HOLIDAYS(String cONS_HOLIDAYS) {
		CONS_HOLIDAYS = cONS_HOLIDAYS;
	}
	public String getLEAVEINCASH() {
		return LEAVEINCASH;
	}
	public void setLEAVEINCASH(String lEAVEINCASH) {
		LEAVEINCASH = lEAVEINCASH;
	}
	public int getSRNO() {
		return SRNO;
	}
	public void setSRNO(int sRNO) {
		SRNO = sRNO;
	}
	
}
