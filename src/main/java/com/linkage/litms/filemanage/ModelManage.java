package com.linkage.litms.filemanage;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.linkage.commons.db.DBUtil;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.module.gwms.Global;

/**
 * 
 * 
 * @author lizhaojun
 * @version 1.00, 4/16/2007
 * @since HGW 1.0
 */
public class ModelManage {
	/**
	 * 对文件数据表进行操作
	 * 
	 * add by lizhaojun
	 * 
	 * @param request
	 *           
	 * @return strMsg 将操作结果以字符窗的形式返回
	 */
	public String modelAct(HttpServletRequest request) {
		String strSQL = "";
		String strMsg = "";

		String strAction = request.getParameter("action");
		String template_id = request.getParameter("template_id");
		if (strAction.equals("delete")) {
			strSQL = "delete from tab_template where  template_id=" + template_id;
		}
		PrepareSQL psql = new PrepareSQL(strSQL);
        psql.getSQL();
		if (!strSQL.equals("")) {
			int iCode = DataSetBean.executeUpdate(strSQL);
			if (iCode > 0) {
				strMsg = "配置模板操作成功！";
			} else {
				strMsg = "配置模板操作失败，请返回重试或稍后再试！";
			}

		}
		return strMsg;
	}
    
	/**
	 * 查询模板列表
	 * 
	 * add by lizhaojun
	 * 
	 * @param request
	 *            
	 * @return String 将操作结果以html的形式返回
	 * 
	 * @throws UnsupportedEncodingException 
	 */
	
	public String getModelHtml(HttpServletRequest request) throws UnsupportedEncodingException {
		
        String template_id = request.getParameter("template_id");

        String template_name = new String(request.getParameter("template_name").getBytes("ISO-8859-1"),"GBK"); 

        String devicetype_id = request.getParameter("devicetype_id");
        
        String Sql = "select * from tab_template a,tab_devicetype_info b";

		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			Sql = "select a.template_id, a.template_name, a.template_desc from tab_template a,tab_devicetype_info b";
		}

		Sql += " where 1=1 and a.devicetype_id = b.devicetype_id ";
        if(template_id !=null && !template_id.equals("")){
        	Sql +=" and a.template_id =" + template_id;       	
        }

        if(template_name !=null && !template_name.equals("")){
        	Sql +=" and a.template_name like '%"+ template_name + "%' ";
        }
        
        if(!devicetype_id.equals("-1")){
        	Sql +=" and a.devicetype_id =" + devicetype_id + "";
        }
        
        Sql +=" order by template_id";


        PrepareSQL psql = new PrepareSQL(Sql);
    	psql.getSQL();
        String strData = "<TABLE border=0 cellspacing=1 bgcolor=#999999 cellpadding=2 width='100%'>"
        				+"<TR><TH>模板ID</TH><TH>模板名称</TH><TH>设备型号</TH><TH>描述</TH><TH>操作</TH></TR>";

        Cursor cursor = null;
        Map fields = null;
        cursor = DataSetBean.getCursor(Sql);
        fields = cursor.getNext();
        if(fields == null){
        	strData+="<TR><TD class=column1 align=left colspan=5>查询没有记录！</TD></TR>";
        }else{
        	while(fields != null){
        		strData +="<TR>"
        					+ "<TD class=column1 align=center>"+ fields.get("template_id")+"</TD>" 
	        				+ "<TD class=column1 align=center>"+ fields.get("template_name")+"</TD>" 
	        				+ "<TD class=column1 align=center>"+ fields.get("device_model")+"</TD>"
	        				+ "<TD class=column1 align=center>"+ fields.get("template_desc")+"</TD>" 
							+ "<TD class=column1 align=center><a href=modelSave.jsp?action=delete&template_id="+fields.get("template_id")+" onclick='return delWarn();'>删除</a></TD>"
        				+ "</TR>";
        		fields = cursor.getNext();
        	}
        	
        }
        strData += "</TABLE>";
        
        strData = "parent.document.all(\"userTable\").innerHTML=\"" + strData + "\";";
        strData = strData + "parent.document.all(\"dispTr\").style.display=\"\";";
        return strData;
    }
	
//	/**
//	 * 获取设备型号下拉列表框
//	 * 
//	 * add by lizhaojun
//	 * 
//	 * @param null
//	 *            
//	 * @return String 将操作结果以String的形式返回
//	 */
//	public String getDeviceTypeList(String cast){
//		
//		String tmpSql = "select * from tab_devicetype_info  where 1=1 order by devicetype_id";
//		Cursor cursor = DataSetBean.getCursor(tmpSql);
//		String DeviceTypeList  = FormUtil.createListBox(cursor,"devicetype_id","device_model",true,cast,"");
//		return DeviceTypeList;	
//	}
	/**
	 * 获取设备型号下拉列表框
	 * 
	 * add by lizhaojun
	 * 
	 * @param null
	 *            
	 * @return String 将操作结果以String的形式返回
	 */
	public Map  getDeviceTypeMap(){
		
		Map _map = new HashMap();
		String tmpSql = "select a.devicetype_id,b.device_model from tab_devicetype_info a,gw_device_model b where a.device_model_id=b.device_model_id";
		PrepareSQL psql = new PrepareSQL(tmpSql);
    	psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(tmpSql);
		//String DeviceTypeList  = FormUtil.createListBox(cursor,"devicetype_id","device_model",false,cast,"");
		Map fields = cursor.getNext();
		if(fields != null){
			String devicetype_id = "";
			String device_model= "";
			while(fields != null){
				devicetype_id = (String) fields.get("devicetype_id");
				device_model = (String) fields.get("device_model");
				_map.put(devicetype_id, device_model);
				fields = cursor.getNext();
			}
					
		}
		return _map;	
	}
}
