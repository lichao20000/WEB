<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>������������Ϣ</title>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">

</SCRIPT>

</head>
<body>
<TABLE width="98%" class="querytable" align="center">
	<TR>
		<TH colspan="4" class="title_1">�豸��<s:property value="deviceDetailMap.device_serialnumber"/>��������Ϣ</TH>
	</TR>
	<TR >
		<TD class="title_3"  colspan=4>����������Ϣ
		<font color="red"><s:property value="deviceNetMap.error"/></font>
		</TD>
	</TR>
	<TR >
		<TD class="title_2" >�յ��İ�</TD>
		<TD width="25%"><s:property value="deviceNetMap.packets_received"/>��</TD>
		<TD class="title_2" >������</TD>
		<TD width="40%"><s:property value="deviceNetMap.packets_lost"/>��</TD>
	</TR>
	<TR >
		<TD class="title_2" >������</TD>
		<TD width="25%"><s:property value="deviceNetMap.fraction_lost"/></TD>
		<TD class="title_2" >ý�嶪����</TD>
		<TD width="40%"><s:property value="deviceNetMap.pm_lost_rate"/></TD>
	</TR>
	<TR >
		<TD class="title_2" >��СDF</TD>
		<TD width="25%"><s:property value="deviceNetMap.min_df"/>s</TD>
		<TD class="title_2" >���DF</TD>
		<TD width="40%"><s:property value="deviceNetMap.max_df"/>s</TD>
	</TR>
	<TR >
		<TD class="title_2" >ƽ��DF</TD>
		<TD width="25%"><s:property value="deviceNetMap.avg_df"/>s</TD>
		<TD class="title_2" >����</TD>
		<TD width="40%"><s:property value="deviceNetMap.dithering"/>��s</TD>
	</TR>
	<TR >
		<TD class="title_2" >��RAM</TD>
		<TD width="25%"><s:property value="deviceNetMap.phy_mem_size"/></TD>
		<TD class="title_2" >�洢</TD>
		<TD width="40%"><s:property value="deviceNetMap.storage_size"/></TD>
	</TR>
	<TR >
		<TD class="title_2" >����</TD>
		<TD width="25%"><s:property value="deviceNetMap.bitrate"/>bps</TD>
		<TD class="title_2" ></TD>
		<TD width="40%"></TD>
	</TR>
	
	
	
	
	<TR >
		<TD class="title_3"  colspan=4>��Ƶ����Ϣ
		<font color="red"><s:property value="deviceEpgMap.valuation"/></font>
		</TD>
	</TR>
	<TR >
		<TD class="title_2" >��ǰMOSֵ</TD>
		<TD width="25%"><s:property value="deviceEpgMap.mos"/></TD>
		<TD class="title_2" >MDI�ӳ�����</TD>
		<TD width="40%"><s:property value="deviceEpgMap.mdi_df"/></TD>
	</TR>
	<TR >
		<TD class="title_2" >MDI��������</TD>
		<TD width="25%"><s:property value="deviceEpgMap.mdi_mlr"/></TD>
		<TD class="title_2" ></TD>
		<TD width="40%"></TD>
	</TR>
	<TR >
		<TD class="title_3"  colspan=4>EPG������Ϣ
		<font color="red"><s:property value="deviceEpgMap.valuation"/></font>
		</TD>
	</TR>
	<TR >
		<TD class="title_2" >Ƶ���л�ƽ����ʱ</TD>
		<TD width="25%"><s:property value="deviceEpgMap.avgChannelChangeCost"/>ms</TD>
		<TD class="title_2" >�鲥����ƽ����ʱ</TD>
		<TD width="40%"><s:property value="deviceEpgMap.avgMulticastRequestTime"/>ms</TD>
	</TR>
	<TR >
		<TD class="title_2" >ֱ��ҵ���л�ƽ��ʱ��</TD>
		<TD width="25%"><s:property value="deviceEpgMap.avgDirectSeedTime"/>ms</TD>
		<TD class="title_2" >ֱ��ҵ���л�ʧ�ܴ���/�������</TD>
		<TD width="40%"><s:property value="deviceEpgMap.directSeedFailTimes"/>/<s:property value="deviceEpgMap.directSeedReqTimes"/></TD>
	</TR>
	<TR >
		<TD class="title_2" >�㲥ҵ���л�ƽ��ʱ��</TD>
		<TD width="25%"><s:property value="deviceEpgMap.avgOrderProgTime"/>ms</TD>
		<TD class="title_2" >�㲥ҵ���л�ʧ�ܴ���/�������</TD>
		<TD width="40%"><s:property value="deviceEpgMap.orderProgFailTimes"/>/<s:property value="deviceEpgMap.orderProgReqTimes"/></TD>
	</TR>
	<TR >
		<TD class="title_2" >�ؿ�ҵ���л�ƽ��ʱ��</TD>
		<TD width="25%"><s:property value="deviceEpgMap.avgLookBackTime"/>ms</TD>
		<TD class="title_2" >�ؿ�ҵ���л�ʧ�ܴ���/�������</TD>
		<TD width="40%"><s:property value="deviceEpgMap.lookBackFailTimes"/>/<s:property value="deviceEpgMap.lookBackReqTimes"/></TD>
	</TR>
	<TR >
		<TD class="title_2" >EPG��������ַ</TD>
		<TD width="25%"><s:property value="deviceEpgMap.epgServerAddr"/></TD>
		<TD class="title_2" ></TD>
		<TD width="40%"></TD>
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