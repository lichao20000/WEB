<%--
FileName	: IpLineSave.jsp
Author		: liuli
Date		: 2007-3-06
Note		: 帐号增加、修改、删除操作
--%>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.fault.Eventcobe" %>
<%
request.setCharacterEncoding("GBK");
String strMsg = null;
String strSQL=null;
String strAction = request.getParameter("action");
String[]  str_hid_allwarn=null;
if(strAction.equals("delete")){
	//删除操作
	str_hid_allwarn = request.getParameterValues("Mycheckbox");

	for(int i=0;i<str_hid_allwarn.length;i++){
		String[] str_value = str_hid_allwarn[i].split("&");
		
		if(strSQL ==null){
			strSQL =" delete from fault_filter_multi where rule_id="+ str_value[0]+" ";	
		}else{
			strSQL += "  delete from fault_filter_multi  where rule_id="+ str_value[0]+" ";
		}
	}
	
}

if(strMsg==null || strSQL!=null){
	int iCode[] = DataSetBean.doBatch(strSQL);
	if(iCode != null && iCode.length> 0){		
		strMsg = "告警排重规则删除成功！";
		for(int i=0;i<str_hid_allwarn.length;i++){
			String[] str_value = str_hid_allwarn[i].split("&");			
			String[] gather_id = str_value[1].split(",");
			for (int j = 0; j < gather_id.length; j++) 
			{		              
				Eventcobe eventcobe = new Eventcobe(gather_id[j]);
				try {
					if(str_value[2].equals("1")){
						eventcobe.filterRuleDelete(str_value[0]);
						strMsg = "告警排重规则删除成功,通知后台成功！";
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					strMsg = "告警排重规则删除成功,通知后台失败！";
					eventcobe.clearService();
				}
			}
		}
	}else{
		strMsg = "告警排重规则删除失败，请返回重试或稍后再试！";
    }
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
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>
	<TABLE width="50%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD bgcolor=#000000>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR>
						<TH align="center">告警排重规则删除操作提示信息</TH>
					</TR>
						<TR height="50">
							<TD align=center valign=middle class=column><%out.println(strMsg);%></TD>
						</TR>				
					<TR>
						<TD class=foot align="right">
						<INPUT TYPE="button" NAME="cmdJump" onclick="GoList('event_paichong_from.jsp')" value=" 列 表 " class=btn>
						<INPUT TYPE="button" NAME="cmdBack" onclick="javascript:history.go(-1);" value=" 返 回 " class=btn>
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
	</TABLE>
</TD></TR>
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
</TABLE>
<%@ include file="../foot.jsp"%>