package payroll.Model;

public class EmployeeBean {
	private int EMPNO;
	private int SALUTE;
	private String FNAME;
	private String MNAME;
	private String LNAME;
	private String GENDER;
	private String DOB;
	private String BPLACE;
	private String DOJ;
	private String DOL;
	private int RESNLFTCD;
	private String PANNO;
	private String PFNO;

	public String getEsicNo() {
		return EsicNo;
	}

	public void setEsicNo(String esicNo) {
		EsicNo = esicNo;
	}

	public String getUanNo() {
		return UanNo;
	}

	public void setUanNo(String uanNo) {
		UanNo = uanNo;
	}

	private String PFOPENDT;
	private String PFNOMINEE;
	private String PFNOMIREL;
	private String MARRIED;
	private String MARRIEDDT;
	private int CASTCD;
	private int CATEGORYCD;
	private int RELEGENCD;
	private String BGRP;
	private String IDENTITY;
	private double HEIGHT;
	private double WEIGHT;
	private String OTHERDTL;
	private String MLWFNO;
	private String HOBBYCD;
	private String DISABILYN;
	private String DISABILPER;
	private int RESIDSTAT;
	private String VEHICLEDES;
	private String DRVLISCNNO;
	private String PASSPORTNO;
	private String DA_SCHEME;
	private String FHNAME;
	private String STATUS;
	private String SBOND;
	private int DEPAMT;
	private int EMPTYPE;
	private String AADHAARNUM;
	private String IMAGE;
	private String EMPCODE;
	private String EsicNo;
	private String UanNo;
	private String Prj_Code;

	public String getPrj_Code() {
		return Prj_Code;
	}

	public void setPrj_Code(String Prj_Code) {
		this.Prj_Code = Prj_Code;
	}

	public int getRESNLFTCD() {
		return RESNLFTCD;
	}

	public void setRESNLFTCD(int rESNLFTCD) {
		RESNLFTCD = rESNLFTCD;
	}

	public String getIMAGE() {
		return IMAGE;
	}

	public void setIMAGE(String iMAGE) {
		IMAGE = iMAGE;
	}

	public String getAADHAARNUM() {
		return AADHAARNUM;
	}

	public void setAADHAARNUM(String aADHAARNUM) {
		AADHAARNUM = aADHAARNUM;
	}

	public int getEMPTYPE() {
		return EMPTYPE;
	}

	public void setEMPTYPE(int eMPTYPE) {
		EMPTYPE = eMPTYPE;
	}

	public int getEMPNO() {
		return EMPNO;
	}

	public void setEMPNO(int eMPNO) {
		EMPNO = eMPNO;
	}

	public int getSALUTE() {
		return SALUTE;
	}

	public void setSALUTE(int sALUTE) {
		SALUTE = sALUTE;
	}

	public String getFNAME() {
		return FNAME;
	}

	public void setFNAME(String fNAME) {
		FNAME = fNAME;
	}

	public String getMNAME() {
		return MNAME;
	}

	public void setMNAME(String mNAME) {
		MNAME = mNAME;
	}

	public String getLNAME() {
		return LNAME;
	}

	public void setLNAME(String lNAME) {
		LNAME = lNAME;
	}

	public String getGENDER() {
		return GENDER;
	}

	public void setGENDER(String gENDER) {
		GENDER = gENDER;
	}

	public String getDOB() {
		return DOB;
	}

	public void setDOB(String dOB) {
		DOB = dOB;
	}

	public String getBPLACE() {
		return BPLACE;
	}

	public void setBPLACE(String bPLACE) {
		BPLACE = bPLACE;
	}

	public String getDOJ() {
		return DOJ;
	}

	public void setDOJ(String dOJ) {
		DOJ = dOJ;
	}

	public String getDOL() {
		return DOL;
	}

	public void setDOL(String dOL) {
		DOL = dOL;
	}

	public String getPANNO() {
		return PANNO;
	}

	public void setPANNO(String pANNO) {
		PANNO = pANNO;
	}

	public String getPFNO() {
		return PFNO;
	}

	public void setPFNO(String pFNO) {
		PFNO = pFNO;
	}

	public String getPFOPENDT() {
		return PFOPENDT;
	}

	public void setPFOPENDT(String pFOPENDT) {
		PFOPENDT = pFOPENDT;
	}

	public String getPFOPENDTQUERY() {
		if (PFOPENDT == null || PFOPENDT.equals("")) {
			return "null";
		} else {
			return "'" + PFOPENDT + "'";
		}
	}

	public String getPFNOMINEE() {
		return PFNOMINEE;
	}

	public void setPFNOMINEE(String pFNOMINEE) {
		PFNOMINEE = pFNOMINEE;
	}

	public String getPFNOMIREL() {
		return PFNOMIREL;
	}

	public void setPFNOMIREL(String pFNOMIREL) {
		PFNOMIREL = pFNOMIREL;
	}

	public String getMARRIED() {
		return MARRIED;
	}

	public void setMARRIED(String mARRIED) {
		MARRIED = mARRIED;
	}

	public String getMARRIEDDT() {
		return MARRIEDDT;
	}

	public void setMARRIEDDT(String mARRIEDDT) {
		MARRIEDDT = mARRIEDDT;
	}

	public String getBGRP() {
		return BGRP;
	}

	public void setBGRP(String bGRP) {
		BGRP = bGRP;
	}

	public String getIDENTITY() {
		return IDENTITY;
	}

	public void setIDENTITY(String iDENTITY) {
		IDENTITY = iDENTITY;
	}

	public double getHEIGHT() {
		return HEIGHT;
	}

	public void setHEIGHT(double hEIGHT) {
		HEIGHT = hEIGHT;
	}

	public double getWEIGHT() {
		return WEIGHT;
	}

	public void setWEIGHT(double wEIGHT) {
		WEIGHT = wEIGHT;
	}

	public String getOTHERDTL() {
		return OTHERDTL;
	}

	public void setOTHERDTL(String oTHERDTL) {
		OTHERDTL = oTHERDTL;
	}

	public String getMLWFNO() {
		return MLWFNO;
	}

	public void setMLWFNO(String mLWFNO) {
		MLWFNO = mLWFNO;
	}

	public String getHOBBYCD() {
		return HOBBYCD;
	}

	public void setHOBBYCD(String hOBBYCD) {
		HOBBYCD = hOBBYCD;
	}

	public String getDISABILYN() {
		return DISABILYN;
	}

	public void setDISABILYN(String dISABILYN) {
		DISABILYN = dISABILYN;
	}

	public String getDISABILPER() {
		return DISABILPER;
	}

	public void setDISABILPER(String dISABILPER) {
		DISABILPER = dISABILPER;
	}

	public int getRESIDSTAT() {
		return RESIDSTAT;
	}

	public void setRESIDSTAT(int rESIDSTAT) {
		RESIDSTAT = rESIDSTAT;
	}

	public String getVEHICLEDES() {
		return VEHICLEDES;
	}

	public void setVEHICLEDES(String vEHICLEDES) {
		VEHICLEDES = vEHICLEDES;
	}

	public String getDRVLISCNNO() {
		return DRVLISCNNO;
	}

	public void setDRVLISCNNO(String dRVLISCNNO) {
		DRVLISCNNO = dRVLISCNNO;
	}

	public String getPASSPORTNO() {
		return PASSPORTNO;
	}

	public void setPASSPORTNO(String pASSPORTNO) {
		PASSPORTNO = pASSPORTNO;
	}

	public String getDA_SCHEME() {
		return DA_SCHEME;
	}

	public void setDA_SCHEME(String dA_SCHEME) {
		DA_SCHEME = dA_SCHEME;
	}

	public String getFHNAME() {
		return FHNAME;
	}

	public void setFHNAME(String fHNAME) {
		FHNAME = fHNAME;
	}

	public String getSTATUS() {
		return STATUS;
	}

	public void setSTATUS(String sTATUS) {
		STATUS = sTATUS;
	}

	public String getSBOND() {
		return SBOND;
	}

	public void setSBOND(String sBOND) {
		SBOND = sBOND;
	}

	public int getCASTCD() {
		return CASTCD;
	}

	public void setCASTCD(int cASTCD) {
		CASTCD = cASTCD;
	}

	public int getCATEGORYCD() {
		return CATEGORYCD;
	}

	public void setCATEGORYCD(int cATEGORYCD) {
		CATEGORYCD = cATEGORYCD;
	}

	public int getRELEGENCD() {
		return RELEGENCD;
	}

	public void setRELEGENCD(int rELEGENCD) {
		RELEGENCD = rELEGENCD;
	}

	public int getDEPAMT() {
		return DEPAMT;
	}

	public void setDEPAMT(int dEPAMT) {
		DEPAMT = dEPAMT;
	}

	public String getEMPCODE() {
		return EMPCODE;
	}

	public void setEMPCODE(String eMPCODE) {
		EMPCODE = eMPCODE;
	}

}
