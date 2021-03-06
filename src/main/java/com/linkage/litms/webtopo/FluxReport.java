package com.linkage.litms.webtopo;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.util.StringUtils;

public class FluxReport {
	
	private String sql_fluxData = "select * from ? where device_id = ? and collecttime >=? and collecttime <=? and ifindex in(?) Order by ifindex";
	private String sql_ports_Number = "select count(distinct ifindex) as num from ? where device_id = ?";
	
	public int getPortsNum(HttpServletRequest request) {
		String str_type = request.getParameter("type");
		String device_id = request.getParameter("dev_id");
		String str_hidday = request.getParameter("hidday");
		int start_time = Integer.parseInt(str_hidday);
		int searchType = Integer.parseInt(str_type); 
		String tabName = this.getTableName(start_time,searchType);
		
		PrepareSQL pSQLin = null;
		pSQLin = new PrepareSQL(sql_ports_Number);
		pSQLin.setStringExt(1,tabName,false);
		pSQLin.setStringExt(2,device_id,true);
		Cursor cursor = DataSetBean.getCursor(pSQLin.getSQL());
		
		Map fields = new HashMap();
		fields = cursor.getNext();
		String num = (String)fields.get("num");
		int portNum = Integer.parseInt(num);
		
		return portNum;
	}
	public static String getTableName(int hidday,int type){
		long time = hidday;
		Long objTime = new Long(time);
		int start_time = objTime.intValue();
		
		int year =StringUtils.getYear(start_time);
		int month = StringUtils.getMonth(start_time);
		int day = StringUtils.getDate(start_time);
		
		String str_year= String.valueOf(year);
		String str_month= String.valueOf(month);
		String str_day= String.valueOf(day);
		String tableName= "";
		
		switch(type) {
		case 1:
			tableName = "flux_hour_stat_" + str_year + "_" + str_month + "_" + str_day; 
			return tableName;
		case 2:
			tableName = "flux_hour_stat_" + str_year + "_" + str_month;
			return tableName;
		case 3:
			tableName = "flux_hour_stat_" + str_year + "_" + str_month;
			return tableName;
		case 4:
			tableName = "flux_hour_stat_" + str_year;
			return tableName;
		}
		
		return null;
	}
	
	
	public Cursor getFluxReport(HttpServletRequest request){
		String str_type = request.getParameter("type");
		String device_id = request.getParameter("dev_id");
		String str_hidday = request.getParameter("hidday");
		int start_time = Integer.parseInt(str_hidday);
		int searchType = Integer.parseInt(str_type); 
		String tabName = this.getTableName(start_time,searchType);
		long start = start_time;
		long end   = start_time+24*60*60;
		String strPorts = request.getParameter("ports");
			
		PrepareSQL pSQLin = null;
		pSQLin = new PrepareSQL(sql_fluxData);
		pSQLin.setStringExt(1,tabName,false);
		pSQLin.setStringExt(2,device_id,true);
		pSQLin.setLong(3,start);
		pSQLin.setLong(4,end);
		pSQLin.setStringExt(5,strPorts,false);
		Cursor cursor = DataSetBean.getCursor(pSQLin.getSQL());
		
		return cursor;
	}
	
	public String getFluxKindName(String kind) {
		String strName = "";
		if((kind.trim()).equals("ifinoctetsbps")) {
			strName = "????????????";
			return strName;
		}
		else if((kind.trim()).equals("ifindiscardspps")){
			strName = "???????????????";
			return strName;
		}
		else if((kind.trim()).equals("ifinerrors")){
			strName = "??????????????????";
			return strName;
		}
		else if((kind.trim()).equals("ifinoctetsbpsmax")){
			strName = "????????????";
			return strName;
		}
		else if((kind.trim()).equals("ifinucastpktspps")){
			strName = "????????????????????????";
			return strName;
		}
		else if((kind.trim()).equals("u1")){
			strName = "?????????????????????";
			return strName;
		}
		else if((kind.trim()).equals("ifoutoctetsbps")){
			strName = "????????????";
			return strName;
		}
		else if((kind.trim()).equals("ifoutdiscardspps")){
			strName = "???????????????";
			return strName;
		}
		else if((kind.trim()).equals("ifouterrors")){
			strName = "??????????????????";
			return strName;
		}
		else if((kind.trim()).equals("ifoutoctetsbpsmax")){
			strName = "????????????";
			return strName;
		}
		else if((kind.trim()).equals("ifinnucastpktspps")){
			strName = "???????????????????????????";
			return strName;
		}
		else if((kind.trim()).equals("u2")){
			strName = "?????????????????????";
			return strName;
		}
		else if((kind.trim()).equals("ifinerrorspps")){
			strName = "???????????????";
			return strName;
		}
		else if((kind.trim()).equals("ifinoctets")){
			strName = "???????????????";
			return strName;
		}
		else if((kind.trim()).equals("ifindiscards")) {
			strName = "??????????????????";
		}
		else if((kind.trim()).equals("u3")){
			strName = "?????????????????????";
			return strName;
		}
		else if((kind.trim()).equals("ifoutucastpktspps")){
			strName = "????????????????????????";
			return strName;
		}
		else if((kind.trim()).equals("ifouterrorspps")){
			strName = "???????????????";
			return strName;
		}
		else if((kind.trim()).equals("ifoutoctets")){
			strName = "???????????????";
			return strName;
		}
		else if((kind.trim()).equals("ifoutdiscards")){
			strName = "??????????????????";
			return strName;
		}
		else if((kind.trim()).equals("u4")){
			strName = "?????????????????????";
			return strName;
		}
		else if((kind.trim()).equals("ifoutnucastpktspps")){
			strName = "???????????????????????????";
			return strName;
		}
		else if((kind.trim()).equals("ifinunknownprotospps")){
			strName = "??????????????????????????????";
			return strName;
		}
		else if((kind.trim()).equals("ifoutqlenpps")){
			strName = "????????????????????????";
			return strName;
		}
		return strName;
		
	}
    public static void main(String[] arg) {
    
    }
    
}

