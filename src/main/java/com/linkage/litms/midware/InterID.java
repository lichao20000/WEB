package com.linkage.litms.midware;

/**
 * 
 * @author Zhaof
 *
 */
public enum InterID {
	D1,D2,D3,B1,B2,B3,B4,B5,B6,C1,C2,C3;
	
	public String getDescription() {
		switch (this) {
			case D1: return "add device";
			case D2: return "change device info";
			case D3: return "delete device";
			case B1: return "new bussiness";
			case B2: return "lock bussiness";
			case B3: return "unlock bussiness";
			case B4: return "update bussiness parm";
			case B5: return "close bussiness";
			case B6: return "upgrade bussiness";
			case C1: return "area info update";
			case C2: return "csr update";
			case C3: return "area info update";
			default: return "未知功能";
		}
	}
		
}
