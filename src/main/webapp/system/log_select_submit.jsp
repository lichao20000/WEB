<%@ include file="../timelater.jsp"%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.linkage.litms.common.util.DateTimeUtil"%>
<%@page import="com.linkage.litms.common.database.Cursor"%>
<%@page import="com.linkage.litms.resource.DeviceAct"%>
<%@ page contentType="text/html;charset=GBK"%>
<%ArrayList list = LogItem.getInstance().getLogs(request);
			
			String strBar = (String) list.get(0);
			Cursor cursor = (Cursor) list.get(1);
			Map fields = null;
			fields = cursor.getNext();
			DeviceAct act = new DeviceAct();
			%>


<SCRIPT LANGUAGE="JavaScript" src="../Js/edittable.js"></SCRIPT>
<table width="98%" border=0 align="center" cellpadding="0" cellspacing="0" class="text">
		<TR>
			<TD HEIGHT=20>&nbsp;</TD>
		</TR>
					<tr>
						<td>
						<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
							<tr>
								<td width="162" align="center" class="title_bigwhite">
									日志查询
								</td>
								<td>
									<img src="../images/attention_2.gif" width="15" height="12">
									&nbsp;&nbsp;系统日志查询结果
								</td>
							</tr>
						</table>
						</td>
					</tr>
	<TR>
		<TD>
		<TABLE border=0 cellspacing=1 cellpadding=2 bgcolor="#999999" width="100%" id="outTable">
			<tr bgcolor="#FFFFFF" class="blue_title">
				<TH width="8%">操作人</TH>
				<TH width="12%">时间</TH>
				<TH width="12%">日志类型</TH>
				<TH width="15%">操作名称</TH>
				<TH width="15%">操作对象</TH>
				<TH width="15%">操作内容</TH>
				<TH width="15%">操作终端</TH>
				<TH width="8%">结果</TH>
			</tr>
			<%if (null != fields) {
				int logType;
				String logStr = "";
				while (null != fields) {
					out.println("<TR bgcolor='#FFFFFF' align=right >");
					out
							.println("<TD >" + fields.get("acc_loginname")
									+ "</TD>");
					out.println("<TD >"
							+ new DateTimeUtil(Long.parseLong((String) fields
									.get("operation_time")) * 1000)
									.getLongDate() + "</TD>");
					logType = Integer.parseInt((String) fields
							.get("operationlog_type"));
					if (logType == 2) {
						logStr = "设备操作日志";
					} else if (logType == 3) {
						logStr = "工单日志";
					} else if (logType == 3) {
						logStr = "接口日志";
					} else {
						logStr = "系统操作日志";
					}
					out.println("<TD >" + logStr + "</TD>");
					String operation_name = "";
					String operation_type = String.valueOf(fields.get("operation_name")).toString();
					if("1".equals(operation_type)){
						operation_name = "查询";
					}else if("2".equals(operation_type)){
						operation_name = "配置";
					}else if("3".equals(operation_type)){
						operation_name = "诊断";
					}else if("4".equals(operation_type)){
						operation_name = "登录";
					}else{
						operation_name = "其他";
					}
					out.println("<TD >" + operation_name + "</TD>");
					out.println("<TD >" + fields.get("operation_object")
							+ "</TD>");
					out.println("<TD align='left'>" + fields.get("operation_content")
							+ "</TD>");
					
					if(null==fields.get("oui")||null==fields.get("device_serialnumber"))
					{						
						if(!"Web".equals(String.valueOf(fields.get("operation_device")).toString()))
						{							
							String[] device_ids = new String[1];
							device_ids[0] = (String)fields.get("operation_device");
							Cursor deviceCursor = act.getDevCursorByIDs(device_ids);
							Map deviceField= deviceCursor.getNext();
							if(null!=deviceField)
							{
								out.println("<TD >" +deviceField.get("oui")+"|"+deviceField.get("device_serialnumber")+"</TD>");
							}
							else
							{
								out.println("<TD>Web</TD>");
							}
							//clear
							device_ids = null;
							deviceCursor = null;
						}
						else
						{
							out.println("<TD>Web</TD>");
						}
					}
					else
					{
						out.println("<TD >" +fields.get("oui")+"|"+fields.get("device_serialnumber")+"</TD>");
					}
					
					if("0".equals(fields.get("result_id"))){
						out.println("<TD >失败</TD>");
					}else{
						out.println("<TD >成功</TD>");
					}
					
					fields = cursor.getNext();
				}
				out.println("<TR bgcolor='#FFFFFF'><TD colspan=8 align=right>"
						+ strBar + "</td></tr>");
			} else {
				out.println("<TR bgcolor='#FFFFFF'>");
				out.println("<TD colspan=8>查询无数据</TD>");
				out.println("</TR>");
			}

		%>
		 			<tr bgcolor="#FFFFFF">
				<td colspan=8 class=green_foot align=right>
				<input TYPE="button" value="导出日志" onclick="initialize(outTable,0,1)">&nbsp;&nbsp;
				<input TYPE="button"	name="cmdOK" onclick="back()" value=" 返 回 ">
                </td>
				</tr>
		</TABLE>

   		</TD>
	</TR>
</TABLE>
<script language=javascript>
function back()
{
 this.location.href="log_select.jsp";
}
</script>
<BR><BR><BR>
<%@ include file="../foot.jsp"%>