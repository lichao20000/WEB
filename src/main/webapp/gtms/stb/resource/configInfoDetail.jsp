<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<%--
	/**
	 * 
	 *
	 * @author wuchao
	 * @version 1.0
	 * @since 2011-3-23 15:01:27
	 * 
	 * <br>��Ȩ���Ͼ������Ƽ� ���ܿ�����
	 * 
	 */
 --%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>��������ʷ������Ϣ��ѯ</title>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">

</SCRIPT>

</head>
<body>
<TABLE width="98%" class="querytable" align="center">
	<TR>
		<TH colspan="4" class="title_1">��������ʷ������Ϣ��ѯ</TH>
	</TR>
	<TR>
		<TD class="title_2" width="15%">�豸���к�</TD> 
		<TD width="85%" colspan="3"><s:property value="dataDetail.device_serialnumber"/></TD>
	</TR>
	<TR>
		<TD class="title_2" width="15%">�豸�ͺ�</TD> 
		<TD width="35%"><s:property value="dataDetail.device_model"/></TD>
		<TD class="title_2" width="15%">����</TD>
		<TD width="35%"><s:property value="dataDetail.city_name"/></TD>
	</TR>
     <TR >
		<TD class="title_2" >�豸ip</TD>
		<TD><s:property value="dataDetail.ip"/></TD>
		<TD class="title_2" >��������</TD>
		<TD><s:property value="dataDetail.mask"/></TD>
	</TR>
	<TR >
		<TD class="title_2" >����</TD>
		<TD><s:property value="dataDetail.gateway"/></TD>
		<TD class="title_2" >DNS������</TD>
		<TD><s:property value="dataDetail.dns"/></TD>
	</TR>
	<TR >
		<TD class="title_2" >MAC</TD>
		<TD><s:property value="dataDetail.mac"/></TD>
		<TD class="title_2" >���뷽ʽ</TD>
		<TD><s:property value="dataDetail.address_type"/></TD>
	</TR>
	<TR >
		<TD class="title_2" >PPPOE�˺�</TD>
		<TD width=140><s:property value="dataDetail.pppoe_id"/></TD>
		<TD class="title_2" >PPPOE����</TD>
		<TD><s:property value="dataDetail.pppoe_pwd"/></TD>
	</TR>
	<TR >
		<TD class="title_2" >DHCP�˺�</TD>
		<TD><s:property value="dataDetail.iptv_dhcp_username"/></TD>
		<TD class="title_2" >DHCP����</TD>
		<TD><s:property value="dataDetail.iptv_dhcp_password"/></TD>
	</TR>
	<TR >
		<TD class="title_2" >ҵ���˺�</TD>
		<TD><s:property value="dataDetail.user_id"/></TD>
		<TD class="title_2" >ҵ������</TD>
		<TD><s:property value="dataDetail.user_pwd"/></TD>
	</TR>
	<TR >
		<TD class="title_2" >��֤��������ַ</TD>
		<TD ><s:property value="dataDetail.auth_url"/></TD>
		<TD class="title_2" >���·�������ַ</TD>
		<TD><s:property value="dataDetail.auto_update_serv"/></TD>
	</TR>
     <TR >
		<TD class="title_2" >���ܵ�ַ</TD>
		<TD><s:property value="dataDetail.networkAddress"/></TD>
		<TD class="title_2" >����ģʽ</TD>
		<TD><s:property value="dataDetail.aspect_ratio"/></TD>
	</TR>
	<TR >
		<TD class="title_2" >��Ƶ�����ʽ</TD>
		<TD><s:property value="dataDetail.composite_video_standard"/></TD>
		<TD class="title_2" >����汾</TD>
		<TD><s:property value="dataDetail.software_version"/></TD>
	</TR>
	<TR>
		<TD colspan="4" class=foot>
			<div align="center">
				<button onclick="javascript:window.close();"> �� �� </button>
			</div>
		</TD>
	</TR>
</TABLE>

</body>