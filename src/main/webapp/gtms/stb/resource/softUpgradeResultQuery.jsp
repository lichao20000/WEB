<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet"
	type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">



	$(function() {
		gwShare_setGaoji();
	});
	function deviceResult(returnVal) {
		for ( var i = 0; i < returnVal[2].length; i++) {
			//$("input[@name='deviceId']").val(returnVal[0]);
			deviceId = returnVal[2][i][0];
			devicetypeId = returnVal[2][i][6];
		}
		var url = "<s:url value='/gtms/stb/resource/stbSoftUpgrade!getUpgradeResult.action'/>";
		if (deviceId != "") {
			//$("div[@id='div_goal_softwareversion']").html("");
			$.post(url, {
				deviceId : deviceId
			}, function(ajax) {

				$('#upgraderesult').html(ajax);
			});
		} else {
			//$("div[@id='div_goal_softwareversion']").html("��ѡ���豸");
		}
	}
</SCRIPT>
<TABLE width="98%" align="center" class="querytable">
	<TR>
		<TD><IMG height=20 src="<s:url value='/images/bite_2.gif'/>"
			width=24> ����ǰ��λ�ã���̨�豸������������ѯ</TD>
	</TR>
</TABLE>
<br>
<table width="98%" border="0" align="center" cellpadding="0"
	cellspacing="0">
	<TR>
		<td><%@ include file="../share/gwShareDeviceQuery.jsp"%>
		</td>
	</TR>

</TABLE>
<DIV ID="upgraderesult"></DIV>




