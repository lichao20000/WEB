package com.linkage.litms.common.database;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExportData {
	/**
	 * @param fieldList 读取的字段列
	 * @param fieldscn 读取字段的名称
	 * @param sql 读取的sql
	 * @param changeMap 读取的字段的转换
	 * @return
	 */
	public String[][] getexportdata(List fieldList,List fieldscn,String sql,HashMap changeMap){
		String[][] mydata=null;
		Cursor cursor=null;
		cursor=DataSetBean.getCursor(sql);
		int RecordNum=cursor.getRecordSize();
		mydata=new String[RecordNum+1][fieldscn.size()+1];
		Map fields = cursor.getNext();
		for(int i=0;i<fieldscn.size();i++){
			String fieldcn=(String)fieldscn.get(i);
			mydata[0][i]=fieldcn;
		}
		int k=1;
		while(fields!=null){
		
			for(int j=1;j<fieldList.size()+1;j++){
				String field=(String)fieldList.get(j-1);
				String fieldvalue=(String)fields.get(field.toLowerCase());
				if(changeMap.containsKey(field)){
					HashMap map=new HashMap();
					fieldvalue=(String)map.get(fieldvalue);
				}
				
				mydata[k][j-1]=fieldvalue;
			}
			k++;
			fields = cursor.getNext();
		}
		return mydata;
	}
	
	public String[][] exportdata1(String type,String sql,HashMap city){
		if(type.equals("onceexplorer"))
			return getOnceExport1(sql,city);
		else if(type.equals("noaccess_portspeed"))
				return getOnceExport2(sql,city);
		else 
			return null;
	}
	
	public String[][] exportdata(String type,String sql){
		if(type.equals("onceexplorer")){
			return getOnceExport(sql);
		}else {
			return null;
		}
	}
	/**
	 * @param fieldList 读取的字段列
	 * @param fieldscn 读取字段的名称
	 * @param sql 读取的sql
	 * @param changeMap 读取的字段的转换 没有转换则可以设为null
	 * @return
	 */
	public String[][] getexportdata(String[] fieldList,String[] fieldscn,String sql,HashMap changeMap){
		String[][] mydata=null;
		Cursor cursor=null;
		cursor=DataSetBean.getCursor(sql);
		int RecordNum=cursor.getRecordSize();
		mydata=new String[RecordNum+1][fieldscn.length];
		Map fields = cursor.getNext();
		for(int i=0;i<fieldscn.length;i++){
			String fieldcn=fieldscn[i];
			mydata[0][i]=fieldcn;
		}
		int k=1;
		while(fields!=null){

			for(int j=1;j<fieldList.length+1;j++){
				String field=fieldList[j-1];
				String fieldvalue=(String)fields.get(field.toLowerCase());
				if(changeMap!=null&&changeMap.containsKey(field)){
					HashMap map=(HashMap)changeMap.get(field);
					fieldvalue=(String)map.get(fieldvalue);
				}
				
				mydata[k][j-1]=fieldvalue;
			}
			k++;
			fields = cursor.getNext();
		}
		return mydata;
	}
	
	/**
	 * 一次性开通的的数据导出
	 * @param sql
	 * @return
	 */
	private String[][] getOnceExport2(String sql,HashMap city) {              
	
		String[] columns ={"属地","用户帐号","达标次数","轮询到次数","设备ip","设备编码","设备类型","端口索引","平均下行衰减","平均上行衰减","下行最大可达速率","上行最大可达速率","下行实际可达均速率","上行实际可达均速率","下行配置速率","上行配置速率"};
		String[] files ={"city_id","username","passtimes","onlinetimes","device_ip","device_id","device_model","portindex","avgdnattenuation","avgupattenuation","maxdnrate","maxuprate","avgdnrate","avguprate","dnspeed","upspeed"};
		HashMap changecolumn=new HashMap();
		changecolumn.put("city_id", city);
		return getexportdata(files,columns,sql,changecolumn);
	}
	/**
	 * 一次性开通的的数据导出
	 * @param sql
	 * @return
	 */
	private String[][] getOnceExport1(String sql,HashMap city) {
	
		String[] columns ={"市、县","用户帐号","工单编号","开通时间","建档员工号","下行建档速率","下行承诺速率","上行建档速率","上行承诺速率","下行建档衰减","上行建档衰减"};
		String[] files ={"system_id","username","worksheet_id","worksheet_receive_time","empno","adlbas0dnrate","bandwidth","adlbls0uprate","upwidth","adlbdnattenuation","adlbupattenuation"};
		HashMap changecolumn=new HashMap();
		changecolumn.put("system_id", city);
		return getexportdata(files,columns,sql,changecolumn);
	}
	
	
	/**
	 * 一次性开通的的数据导出
	 * @param sql
	 * @return
	 */
	private String[][] getOnceExport(String sql) {
		String[] columns ={"员工号","所在设备IP","端口索引","用户账户","流水号","安装时间","修障时间","达标情况"};
		String[] files ={"empno","deviceip","portindex","username","sheet_id","inst_time","update_time","increment"};
		HashMap increment=new HashMap();//达标情况需要转换
		increment.put("1", "上下行都达标");increment.put("2", "上行速率不达标");increment.put("3", "下行速率不达标");
		increment.put("4", "上下行速率都不达标");increment.put("5", "没有获取到上下行速率"); //转换关系
		HashMap changecolumn=new HashMap();
		changecolumn.put("increment", increment);
		return getexportdata(files,columns,sql,changecolumn);
	}
	
}
