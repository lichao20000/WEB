<%--
FileName	: IpLineSave.jsp
Author		: liuli
Date		: 2007-3-06
Note		: 帐号增加、修改、删除操作
--%>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.fault.Eventcobe" %>
<%@ page import="com.linkage.commons.db.DBUtil" %>
<%         request.setCharacterEncoding("GBK");
			String strMsg = null;
			String strSQL = null;
			String Str_kinds = "";
			String[] Str_kind = null;
			String strAction = request.getParameter("action");
			String str_radiobutton="";
			String str_gather_id = "";
			String str_is_active="";
			String str_rule_id="";
			String rule_user=(String)user.getAccount();
			int rule_type;
			if(rule_user.equals("admin")){
				rule_type=1;
			}else{
				rule_type=2;
			}
			java.text.SimpleDateFormat  formatter =new java.text.SimpleDateFormat("yyyy-MM-dd");   				    
			java.util.Date  nowtime  =new java.util.Date();   				    
		    String  strDate =(String)formatter.format(nowtime);
			if (strAction.equals("delete")) {
			//删除操作
            str_rule_id = request.getParameter("rule_id");
            str_gather_id = request.getParameter("gather_id");
            str_is_active = request.getParameter("is_active");
				strSQL = "delete from fault_filter_multi where rule_id="+ str_rule_id + "";
			} else {
			    str_rule_id = request.getParameter("rule_id");
				String str_event_name = request.getParameter("event_name");
				String str_source_ip = request.getParameter("source_ip");
				String str_event_lei = request.getParameter("event_oid");
				if(str_event_lei.equals("-1")){
					str_event_lei="";
				}
				String str_paichong_time = request.getParameter("paichong_time");
				String str_gao_count = request.getParameter("gao_count");
				if(str_gao_count.equals("")){
					str_gao_count="0";
				}
				String str_ti_level = request.getParameter("event_level_id");
			    str_radiobutton = request.getParameter("radiobutton");			    
			    String str_creatorname = request.getParameter("creat_name");
			    if(str_creatorname.equals("-1")){
			    	str_creatorname="";
			    }
				Str_kind = request.getParameterValues("Mycheckbox1");
				for (int i = 0; i < Str_kind.length; i++) 
				{
				   Str_kinds += ";" + Str_kind[i];
				}
				Str_kinds += ";";
				String str_textarea = request.getParameter("textarea");

				//增加操作radiobutton1
				if (strAction.equals("add")) {
					//判断是否重复
					String tmpSql = "select * from fault_filter_multi where rule_id="
							+ str_rule_id
							+ " and rule_name='"
							+ str_event_name
							+ "' ";
					// teledb
					if (DBUtil.GetDB() == 3) {
						tmpSql = "select rule_id from fault_filter_multi where rule_id="
								+ str_rule_id
								+ " and rule_name='"
								+ str_event_name
								+ "' ";
					}
					HashMap map = null;
					com.linkage.commons.db.PrepareSQL psql = new com.linkage.commons.db.PrepareSQL(tmpSql);
					psql.getSQL();
					map = DataSetBean.getRecord(tmpSql);
					if (map != null) {
						strMsg = "告警排重规则\"'" + str_event_name
								+ "'\"已经存在，请换一个告警排重规则名称。";
					} else {
						long rule_id1 = DataSetBean.getMaxId("fault_filter_multi", "rule_id");
						strSQL ="insert into fault_filter_multi (rule_id,rule_name,gather_id,event_level_id,device_ip,event_oid,repeat_count,elapse_time,is_active,fault_desc,creatorname,rule_user,def_time,rule_type) values ("
								+ rule_id1
								+ ",'"
								+ str_event_name
								+ "','"
								+ Str_kinds
								+ "',"
								+ str_ti_level
								+ ",'"
								+ str_source_ip
								+ "','"
								+ str_event_lei
								+ "',"
								+ str_gao_count
								+ ","
								+ str_paichong_time
								+ ","
								+ str_radiobutton
								+ ",'"
								+ str_textarea
								+ "','"+str_creatorname+"','"+rule_user+"','"+strDate+"',"+rule_type+")";
						strSQL = StringUtils.replace(strSQL, ",,", ",null,");
						strSQL = StringUtils.replace(strSQL, ",,", ",null,");
						strSQL = StringUtils.replace(strSQL, ",)", ",null)");
					}
				}
			}

			try {
				if (strMsg == null || strSQL != null) {
					int iCode = DataSetBean.executeUpdate(strSQL);

					if (strAction.equals("add")) {
						if (iCode > 0) {
							strMsg = "告警排重规则操作成功！";		
							for (int i = 0; i < Str_kind.length; i++) 
							{
								String gather_id = Str_kind[i];
								Eventcobe eventcobe = new Eventcobe(gather_id);
								try {
									if(str_radiobutton.equals("1")){
									long rule_id1 = DataSetBean.getMaxId("fault_filter_multi", "rule_id")-1;
									eventcobe.filterRuleAdd(String.valueOf(rule_id1));
									strMsg = "告警排重规则操作成功,此告警是提升告警，同时通知后台成功！";
									}
								} catch (Exception ex) {
									ex.printStackTrace();
									strMsg = "告警排重规则操作成功,通知后台失败！";
									//eventcobe.clearService();
								}
							}
						} else {
							strMsg = "告警排重规则操作失败，请返回重试或稍后再试！";
						}
					} else if (strAction.equals("delete")) {
						if (iCode > 0) {
							strMsg = "告警排重规则删除成功！";
							String arrGather[] = str_gather_id.split(";");
							for (int i = 1; i < arrGather.length; i++) 
							{                                 
								String gather_id = arrGather[i];
								Eventcobe eventcobe = new Eventcobe(gather_id);
								try {
									if(str_is_active.equals("1")){
									eventcobe.filterRuleDelete(str_rule_id);
									strMsg = "告警排重规则删除成功,同时通知后台成功！";
									}
								} catch (Exception ex) {
									ex.printStackTrace();
									strMsg = "告警排重规则删除成功,通知后台失败！";
									//eventcobe.clearService();
								}
							}
						} else {
							strMsg = "告警排重规则删除操作失败，请返回重试或稍后再试！";
						}
					}
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
						<TH align="center">新增告警排重规则操作提示信息</TH>
					</TR>
					<TR height="50">
						<TD align=center valign=middle class=column><%out.println(strMsg);%></TD>
					</TR>
					<TR>
						<TD class=foot align="right"><INPUT TYPE="button" NAME="cmdJump"
							onclick="GoList('event_paichong_from.jsp')" value=" 列 表 "
							class=btn> <INPUT TYPE="button" NAME="cmdBack"
							onclick="javascript:history.go(-1);" value=" 返 回 " class=btn></TD>
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
