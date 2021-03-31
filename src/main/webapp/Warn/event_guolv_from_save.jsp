<%--
FileName	: IpLineSave.jsp
Author		: liuli
Date		: 2007-3-06
Note		: 帐号增加、修改、删除操作
--%>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.common.corba.WebCorba" %>
<%@ page import="com.linkage.litms.resource.DeviceAct" %>
<%@ page import="com.linkage.commons.db.DBUtil" %>
<%
			request.setCharacterEncoding("GBK");
			String strMsg = null;
			String strSQL = null;
			String Str_kinds = "";
			String[] Str_kind = null;
			ArrayList Str_sql = new ArrayList();			
			String str_ruler_name = "";
			String str_event_oid = "";
			String str_crorate = "";
			String str_device_model = "";
			String str_EditTime = "";
			String str_start_ip = "";
			String str_over_ip = "";
			String str_ruler_desc = "";
			String str_radiobutton = "";
			String str_dengji = "";
			String str_dengjixi = "";
			String str_radiobutton1 = "";
			String str_gather_id = "";
			String str_vendor_id = "";
			String device_model_id = "";
			//过滤方式
			String attr_flag = "";
			//保存周期
			String savetime = "";
			
			String str_ruler_id = request.getParameter("rule_id");
			String strAction = request.getParameter("action");
			String rule_user=(String)user.getAccount();
			java.text.SimpleDateFormat  formatter =new java.text.SimpleDateFormat("yyyy-MM-dd");   				    
			java.util.Date  nowtime  =new java.util.Date();   				    
		    String  strDate =(String)formatter.format(nowtime);

			if (strAction.equals("delete")) {
				//删除操作
				str_gather_id = request.getParameter("gather_id");
				
				strSQL = "delete from fault_filter_ruler where rule_id="
						+ str_ruler_id + "";
				Str_sql.add(strSQL);
				
				strSQL = "delete from fault_filter_attr_info where rule_id="
						+ str_ruler_id + "";
				Str_sql.add(strSQL);
			} else {
				
				str_ruler_name = request.getParameter("ruler_name");
					
				str_event_oid = request.getParameter("event_oid");
				
				str_crorate = request.getParameter("creat_name");
				//设备型号
				str_device_model = request.getParameter("devicetype_id");
				//设备厂商
				str_vendor_id = request.getParameter("vendor_id");
				//设备型号id
				device_model_id = DeviceAct.getDeviceModelID(str_vendor_id, str_device_model);
				
				Str_kind = request.getParameterValues("Mycheckbox1");
				for (int i = 0; i < Str_kind.length; i++) {
					
						Str_kinds += ";" +Str_kind[i] ;

				}if(!"".equals(Str_kinds)) Str_kinds = Str_kinds + ";"; 
								

				str_EditTime = request.getParameter("ruler_time");
				
				str_start_ip = request.getParameter("start_ip");

				str_over_ip = request.getParameter("over_ip");

				str_ruler_desc = request.getParameter("ruler_desc");
				
				str_radiobutton = request.getParameter("radiobutton");
				
				str_dengji = request.getParameter("dengji");

				str_dengjixi = request.getParameter("event_level_id");
				
				str_radiobutton1 = request.getParameter("radiobutton1");
				
				String[] str_ipport = request.getParameterValues("ipport");
				
				//过滤方式
				attr_flag = request.getParameter("attr_flag");
				//保存周期
				savetime = request.getParameter("savetime");
				long timeNum = 0;
				if (savetime != null && !"".equals(savetime)){
					timeNum = Long.parseLong(savetime)*24*60*60;
				}

				//增加操作radiobutton1
					//判断是否重复
					
					long rule_id = DataSetBean.getMaxId("fault_filter_ruler","rule_id");
					
					String tmpSql = "select * from fault_filter_ruler where event_rule_name='"+ str_ruler_name + "'";
					// teledb
					if (DBUtil.GetDB() == 3) {
						tmpSql = "select event_rule_name from fault_filter_ruler where event_rule_name='"+ str_ruler_name + "'";
					}
					com.linkage.commons.db.PrepareSQL psql = new com.linkage.commons.db.PrepareSQL(tmpSql);
					psql.getSQL();
					HashMap map = null;
					map = DataSetBean.getRecord(tmpSql);
					if (map != null) {
						strMsg = "规则名称\"" + str_ruler_name + "\"已经存在，请换一个规则名称。";
					} else {
						strSQL = "insert into fault_filter_ruler (rule_id,event_rule_name,event_oid,device_model,creator_name,device_ip_min,device_ip_max,gather_id,event_level_oper,event_level_id,rule_time,attr_logic,is_active,fault_desc,rule_user,def_time,attr_flag,savetime) values ("
								+ rule_id 
								+",'"
								+ str_ruler_name
								+ "','"
								+ str_event_oid
								+ "','"
								+ device_model_id
								+ "','"
								+ str_crorate
								+ "','"
								+ str_start_ip
								+ "','"
								+ str_over_ip
								+ "','"
								+ Str_kinds
								+ "',"
								+ str_dengji
								+ ","
								+ str_dengjixi
								+ ",'"
								+ str_EditTime
								+ "',"
								+ str_radiobutton1
								+ ","
								+ str_radiobutton
								+ ",'" + str_ruler_desc 
								+ "','"+rule_user+"','"
								+strDate+"',"
								+attr_flag+"," 
								+ timeNum + ")";

						strSQL = StringUtils.replace(strSQL, ",'-1',",",'',");
						strSQL = StringUtils.replace(strSQL, ",'null',",",'',");
						strSQL = StringUtils.replace(strSQL, ",-1,", ",NULL,");
						strSQL = StringUtils.replace(strSQL, ",null,", ",NULL,");
						strSQL = StringUtils.replace(strSQL, ",,", ",NULL,");
						Str_sql.add(strSQL);
						if (str_ipport != null) {
							
							for (int i = 0; i < str_ipport.length; i++) {
								String tmpipport = (String) str_ipport[i];
								String[] str = tmpipport.split("\\|");
							
								strSQL = "insert into fault_filter_attr_info (rule_id,child_rule_id,event_attr_oid,event_attr_value,attr_oper) values("+rule_id+","+i+",'"+str[0]+"','" + str[2] + "'," + str[1] + ")";
								strSQL = StringUtils.replace(strSQL, ",'-1',",",'',");
								strSQL = StringUtils.replace(strSQL, ",'null',",",'',");
								strSQL = StringUtils.replace(strSQL, ",-1,", ",NULL,");
								strSQL = StringUtils.replace(strSQL, ",null,", ",NULL,");
								strSQL = StringUtils.replace(strSQL, ",,", ",NULL,");
								Str_sql.add(strSQL);
								
							}
						}
					}
			}

			try {
				if (strMsg == null || strSQL != null) {
					int iCode[] = DataSetBean.doBatch(Str_sql);

					if (iCode != null && iCode.length > 0) {
						strMsg = "告警过滤规则添加成功！";

						WebCorba corba = null;
			
						AlarmFilter.O_AlarmFilter AlarmFilter = null;
						   
							String _s = "";
							List _tem = curUser.getUserProcesses();
							for (int i = 0; i < _tem.size(); i++){
						
								_s = (String) _tem.get(i);

								try{
									corba = new WebCorba("EventCorrelation", _s, "1");
		
									AlarmFilter = (AlarmFilter.O_AlarmFilter)corba.getIDLCorba("EventCorrelation");
		
									if(null!=AlarmFilter){
										try{
											AlarmFilter.I_NotifyRuler("xiaoxf>>>event_rule_name=="+Encoder.ChineseStringToAscii(str_ruler_name));
										}catch(Exception e){
											try{	
												corba.refreshCorba("c");
												AlarmFilter = (AlarmFilter.O_AlarmFilter)corba.getIDLCorba("EventCorrelation");
												AlarmFilter.I_NotifyRuler("xiaoxf>>>event_rule_name=="+Encoder.ChineseStringToAscii(str_ruler_name));
											}catch(Exception e1){
												e1.printStackTrace();
											}
										}
									  }
							  }catch(Exception e1){
										e1.printStackTrace();
							  	}
							}
					} else strMsg = "告警过滤规则添加操作失败，请返回重试或稍后再试！";
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
function GoList(page){
	this.location = page;
}
//-->
</SCRIPT>
<%@ include file="../toolbar.jsp"%>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<TR>
		<TD>
		<TABLE width="50%" border=0 cellspacing=0 cellpadding=0 align="center">
			<TR>
				<TD bgcolor=#000000>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR>
						<TH align="center">新增告警过滤规则操作提示信息</TH>
					</TR>
					<TR height="50">
						<TD align=center valign=middle class=column><%out.println(strMsg);%></TD>
					</TR>
					<TR>
						<TD class=foot align="right"><INPUT TYPE="button" NAME="cmdJump"
							onclick="GoList('event_guolv_from.jsp')" value=" 列 表 " class=btn>
						<INPUT TYPE="button" NAME="cmdJump"
							onclick="javascript:history.go(-1);" value=" 返 回 "
							class=btn></TD>
					</TR>
				</TABLE>
				</TD>
			</TR>
		</TABLE>
		</TD>
	</TR>
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
</TABLE>
<%@ include file="../foot.jsp"%>
