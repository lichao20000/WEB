<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<html>
<head></head>
<SPAN ID="child1">
	<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
	<TR>
		<TD bgcolor="#999999">
			<table border=0 cellspacing=1 cellpadding=2 width="100%">
			<tr>
				<th width="90%" colspan="6">�������û�</th>
			</tr>
			<tr bgcolor="#D6E1F7">
				<td width="10%" align="center">&nbsp;ѡ��&nbsp;</td>
				<td width="15%" align="center">&nbsp;����&nbsp;</td>
				<td width="20%" align="center">&nbsp;����˺�&nbsp;</td>
				<td width="15%" align="center">&nbsp;���̱��&nbsp;</td>
				<td width="30%" align="center">&nbsp;�豸���к�&nbsp;</td>
				<td width="30%" align="center">&nbsp;����״̬&nbsp;</td>
			</tr>
			<s:iterator value="deviceNormalList" var = "deviceNormalList">
				<tr bgcolor="#FFFFFF">
					<s:if test="#deviceNormalList.open_status_code==1">
						<TD align="center">
							<input type="checkbox" id="device_id" name="device_id"
							value="<s:property value="device_id"/>|<s:property value="gather_id"/>|<s:property value="oui"/>|<s:property value="device_serialnumber"/>|<s:property value="username"/>|<s:property value="wan_type"/>|<s:property value="device_model_id"/>|<s:property value="user_id"/>|<s:property value="devicetype_id"/>|<s:property value="open_status_code"/>"/>
						</TD>
					</s:if>
					<s:else>
						<TD align="center">
							<input type="checkbox" id="device_id" name="device_id" checked
							value="<s:property value="device_id"/>|<s:property value="gather_id"/>|<s:property value="oui"/>|<s:property value="device_serialnumber"/>|<s:property value="username"/>|<s:property value="wan_type"/>|<s:property value="device_model_id"/>|<s:property value="user_id"/>|<s:property value="devicetype_id"/>|<s:property value="open_status_code"/>"/>
						</TD>

					</s:else>

					<TD align="center">
						<s:property value="city_name"/>
					</TD>
					<TD align="center">
						<s:property value="username"/>
					</TD>
					<TD align="center">
						<s:property value="oui"/>
					</TD>
					<TD align="center">
						<s:property value="device_serialnumber"/>
					</TD>
					<s:if test="#deviceNormalList.open_status_code==1">
						<TD align="center"><font color="blue">�������ѳɹ�</font></TD>
					</s:if>
					<s:elseif test="#deviceNormalList.open_status_code==2">
	            		<TD align="center"><font color="red">������δ�ɹ�</font></TD>
	        		</s:elseif>
					<s:else>
	            		<TD align="center">δ����δ�ɹ�</TD>
	        		</s:else>

				</tr>
			</s:iterator>

			<TR  bgcolor="#FFFFFF">
				<TD HEIGHT="20" colspan="6">&nbsp;</TD>
			</TR>

			<tr>
				<th width="90%" colspan="6">�쳣�û�</th>
			</tr>
			<s:if test="deviceExceptionList.size==0">
				<tr bgcolor="#FFFFFF">
					<TD align="left" colspan="6">&nbsp;&nbsp;���쳣���ݣ�</TD>
				</tr>
			</s:if>
			<s:else>
				<tr bgcolor="#D6E1F7">
				<td width="10%" align="center">&nbsp;�˺�&nbsp;</td>
				<td width="50%" colspan="5" align="center">&nbsp;�쳣ԭ��&nbsp;</td>
				</tr>
				<s:iterator value="deviceExceptionList">
					<tr bgcolor="#FFFFFF">
						<TD align="center">
							<s:property value="username"/>
						</TD>
						<TD align="center" colspan="5">
							<s:property value="reason"/>
						</TD>
					</tr>
				</s:iterator>
			</s:else>

	</table>
	</TD>
	</TR>
	</TABLE>

</SPAN>
<SPAN ID="child2">
<iframe name="loadForm" FRAMEBORDER=0 SCROLLING=NO src="ItvImportUsername.jsp" height="30" width="100%"></iframe>
</SPAN>
<SCRIPT LANGUAGE="JavaScript">
parent.document.all("div_device").innerHTML = child1.innerHTML;
parent.document.all("importUsername").innerHTML = child2.innerHTML;

//document.getElementById("deviceListTrId").style.display="";
</SCRIPT>
</html>
