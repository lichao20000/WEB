<%@ include file="../timelater.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page contentType="text/html;charset=GBK"%>

<link rel="stylesheet" href="../css/listview.css" type="text/css">

<div id="idBody">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD>
	<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center" >
	  <tr><th>�������<th></tr>
	  
	  <TR><TD bgcolor=#999999>
		  <TABLE border=0 cellspacing=1 cellpadding=2 width="100%" id="idTable">
			<s:if test="resultNumMore==2">
				<TR bgcolor="#ffffff">
				  <td align="left" width="50%">��ѯ���豸����һ̨�������������ϸ�Ĳ�ѯ������</td>
				</TR>
			</s:if>
			<s:if test="resultNumMore==1">
				<TR bgcolor="#ffffff">
				  <TH align="right" width="25%">�豸���к�</TH>
				  <TH align="right" width="25%">�Ѱ��û�</TH>
				  <TH align="right" width="25%">����</TH>
				  <TH align="right" width="25%">����ά������</TH>
				</TR>
				<s:iterator value="deviceList">
					<tr bgcolor="#FFFFFF">
						<td class=column1 width="25%" align="center"><s:property value="device_serialnumber"/></td>
						<td class=column1 width="25%" align="center"><s:property value="username"/></td>
						<td class=column1 width="25%" align="center"><s:property value="city_name"/></td>
						<td class=column1 width="25%" align="center"><s:property value="x_com_passwd"/></td>
					</tr>
				</s:iterator>
			</s:if>
			<s:if test="resultNumMore==0">
				<TR bgcolor="#ffffff">
				  <td align="left" width="50%">��ѯ���豸��ϵͳ�в����ڣ�</td>
				</TR>
			</s:if>
		  </TABLE>
	  </TD></TR>
	</TABLE>
</TD></TR>
</TABLE>
</div>