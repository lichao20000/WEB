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
<%
            request.setCharacterEncoding("GBK");
			String strMsg = null;
			String strSQL = null;
			String Str_kinds = "";
			String[] Str_kind = null;
			String strAction = request.getParameter("action");
			String str_radiobutton="";
			String str_is_active = request.getParameter("is_active");
			String str_rule_id = request.getParameter("rule_id");
			if (strAction.equals("delete")) {
				//删除操作
				str_rule_id = request.getParameter("rule_id");
				strSQL = "delete from fault_filter_multi where rule_id="
						+ str_rule_id + "";
			} else {
				str_rule_id = request.getParameter("rule_id");
				String  str_event_name = request.getParameter("event_name");
				String str_source_ip = request.getParameter("source_ip");
				String str_event_lei = request.getParameter("event_lei");
				String str_paichong_time = request.getParameter("paichong_time");
				String str_gao_count = request.getParameter("gao_count");
				String str_ti_level = request.getParameter("ti_level");
			    str_radiobutton = request.getParameter("radiobutton");
				Str_kind = request.getParameterValues("Mycheckbox1");
				for (int i = 0; i < Str_kind.length; i++) 
				{						
					Str_kinds += ";" + Str_kind[i];
				}
				Str_kinds += ";";
				String str_textarea = request.getParameter("textarea");
				  String str_creatorname = request.getParameter("creat_name");
				    if(str_creatorname.equals("-1")){
				    	str_creatorname="";
				    }
				//增加操作radiobutton1
				if (strAction.equals("add")) {
					//判断是否重复
					String tmpSql = "select * from fault_filter_multi where rule_id="+ str_rule_id + " and rule_name='"+str_event_name +"' ";
					// teledb
					if (DBUtil.GetDB() == 3) {
						tmpSql = "select rule_id from fault_filter_multi where rule_id="+ str_rule_id + " and rule_name='"+str_event_name +"' ";
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
						strSQL = "insert into fault_filter_multi (rule_id,rule_name,gather_id,event_level_id,Device_ip,event_oid,repeat_count,elapse_time,is_active,fault_desc) values ("+rule_id1+",'"
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
								+ ",'"
								+ str_radiobutton
								+ "','"
								+ str_textarea
								+ "')";
						strSQL = StringUtils.replace(strSQL, ",,", ",null,");
						strSQL = StringUtils.replace(strSQL, ",,", ",null,");
						strSQL = StringUtils.replace(strSQL, ",)", ",null)");
					}
				} else {
						strSQL = "update fault_filter_multi set gather_id='"+Str_kinds+"',event_level_id="+str_ti_level+",device_ip='"+str_source_ip+"',event_oid='"+str_event_lei+"',repeat_count="+str_gao_count+",elapse_time="+str_paichong_time+",is_active="+str_radiobutton+",fault_desc='"+str_textarea+"',creatorname='"+str_creatorname+"' where  rule_id="+ str_rule_id + "";
						strSQL = StringUtils.replace(strSQL, "=,", "=null,");
						strSQL = StringUtils.replace(strSQL, "= where","=null where");
						com.linkage.commons.db.PrepareSQL psql2 = new com.linkage.commons.db.PrepareSQL(strSQL);
						psql2.getSQL();
					}
				}
			
	
			try {
				if (strMsg == null || !strSQL.equals("")) {
					com.linkage.commons.db.PrepareSQL psql2 = new com.linkage.commons.db.PrepareSQL(strSQL);
					psql2.getSQL();
					int iCode = DataSetBean.executeUpdate(strSQL);
					if (iCode > 0) {
						strMsg = "编辑告警排重规则操作成功！";
						for (int i = 0; i < Str_kind.length; i++) 
						{
							String gather_id = Str_kind[i];
							Eventcobe eventcobe = new Eventcobe(gather_id);
							try {
								if(str_is_active.equals("1") && str_radiobutton.equals("1")){
									eventcobe.filterRuleModify(str_rule_id);
									strMsg = "编辑告警排重规则操作成功,通知后台成功！";
								}else if(str_is_active.equals("1") && str_radiobutton.equals("0")){
									eventcobe.filterRuleDelete(str_rule_id);
									strMsg = "编辑告警排重规则操作成功,通知后台成功！";
								}else if(str_is_active.equals("0") && str_radiobutton.equals("1")){																
									long rule_id1 = DataSetBean.getMaxId("fault_filter_multi", "rule_id")-1;									
									eventcobe.filterRuleAdd(String.valueOf(rule_id1));
									strMsg = "编辑告警排重规则操作成功,通知后台成功！";
								}
							} catch (Exception ex) {
								ex.printStackTrace();
								strMsg = "编辑告警排重规则操作成功,通知后台失败！";
								eventcobe.clearService();
							}
						}
					} else {
						strMsg = "编辑告警排重规则操作失败，请返回重试或稍后再试！";
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
						<TH align="center">编辑告警排重规则操作提示信息</TH>
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
