<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<link rel="stylesheet" href="<s:url value='/css/css_green.css'/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>

<SCRIPT LANGUAGE="JavaScript">
	
</SCRIPT>

<TABLE align="right" border=0 cellspacing=0 cellpadding=0 width="100%" >
<s:if test="null!=devMap ">
	<tr >
	  <td colspan="4"  bgcolor="#999999">
		<table width="100%" border="0" cellpadding="2" cellspacing="1"   >
						<tr align="left" >
							<td  align="left" class="green_title_left" colspan="4">
								�豸������Ϣ
							</td>
						</tr>
						<tr bgcolor="#FFFFFF">
							<TD class=column align="right" width="15%">
								�û��ʺ�:
							</TD>
							<TD width="35%" >
								<s:property value="devMap.userAccount" />
							</TD>
							<td class=column align="right" width="15%">
								�û�ID:
							</td>
							<td width="35%" >
								<s:property value="devMap.userId"/>
							</td>
						</tr>
						<tr bgcolor="#FFFFFF">
							<TD class=column align="right" width="15%">
								�豸���к�:
							</TD>
							<TD width="35%" >
								<s:property value="devMap.deviceSn" />
							</TD>
							<td class=column align="right" width="15%">
								�豸ID:
							</td>
							<td width="35%" >
								<s:property value="devMap.deviceId"/>
							</td>
						</tr>
						<tr bgcolor="#FFFFFF">
							<TD class=column align="right" width="15%">
								���뷽ʽ:
							</TD>
							<TD width="35%" >
								<s:property value="devMap.accessType" />
							</TD>
							<td class=column align="right" width="15%">
								�û�״̬:
							</td>
							<td width="35%" >
								<s:property value="devMap.openStatus"/>
							</td>
						</tr>
			</table>
		</td>
	</tr>
	</s:if>
</TABLE>