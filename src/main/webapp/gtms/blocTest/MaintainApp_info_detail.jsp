<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@ include file="../../timelater.jsp"%>
<%@ include file="../../head.jsp"%>
<%@ include file="../../toolbar.jsp"%>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckForm.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/Calendar.js"></SCRIPT>
<link rel="stylesheet" href="../../css/liulu.css" type="text/css">
<link rel="stylesheet" href="../../css/css_green.css" type="text/css">
<link rel="stylesheet" href="../../css/tab.css" type="text/css">
<link rel="stylesheet" href="../../css/listview.css" type="text/css">
<link rel="stylesheet" href="../../css/css_ico.css" type="text/css">
<link rel="stylesheet" href="../../css/user-defined.css" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/toolbars.js"></SCRIPT>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%" id="myTable">
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<TR>
		<TD>
			<TABLE width="100%" border=0 cellspacing=0 cellpadding=0
				align="center">
				<TR>
					<TD bgcolor=#000000>

						<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
							<TR style="cursor: hand; background-color: #cccccc">
								<TD colspan="4">
									<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0">
										<TR>
											<TH>��̽��APP��ϸ��Ϣ</TH>
										</TR>
									</TABLE>
								</TD>
							</TR>
							<s:iterator value="appDetailList">
								<TR bgcolor="#FFFFFF">
									<TD class=column align="right" width="20%">APPUUID��</TD>
									<TD width="30%"><s:property value="appuuid" /></TD>
									<TD class=column align="right" width="20%">APP���� ��</TD>
									<TD width="30%"><s:property value="app_name" /></TD>
								</TR>
								<TR bgcolor="#FFFFFF">
									<TD class=column align="right" width="20%">APP������</TD>
									<TD width="30%"><s:property value="app_desc" /></TD>
									<TD class=column align="right" width="20%">APP�����̣�</TD>
									<TD width="30%"><s:property value="app_vendor" /></TD>
								</TR>
								<TR bgcolor="#FFFFFF">
									<TD class=column align="right">APP�汾��</TD>
									<TD><s:property value="app_version" /></TD>
									<TD class=column align="right">APP����ʱ�䣺</TD>
									<TD><s:property value="app_publish_time" /></TD>
								</TR>
								
								<TR bgcolor="#FFFFFF">
									<TD class=column align="right">APP����״̬��</TD>
									<TD >
									<s:if test="app_publish_status==1">
										�ѷ���
									</s:if> <s:if test="app_publish_status==0">
											<font color='red'>δ����</font>
										</s:if>
									</TD>
									<TD class=column align="right">�ļ��洢·����</TD>
									<TD><s:property value="file_path" /></TD>
								</TR>
							</s:iterator>
							<TR>
								<TD colspan="4" align="right" class=foot><INPUT
									TYPE="button" value=" �� �� " class=btn
									onclick="javascript:window.close()"> &nbsp;&nbsp;</TD>
							</TR>
						</TABLE>
					</TD>
				</TR>
			</TABLE>
		</TD>
	</TR>
</TABLE>
