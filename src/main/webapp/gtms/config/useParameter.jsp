<%@ page contentType="text/html; charset=GBK"%>
<%@ include file="../../head.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<title>������ʽ�޸�Ϊ·��</title>
<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="<s:url value="../../css3/c_table.css"/>" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.js"></SCRIPT>

<script LANGUAGE="JavaScript">

function doConfig()
{
	var netusername=$("input[@name='netusername']").val();
	var netusernamepwd=$("input[@name='netusernamepwd']").val();
	
	if($.trim(netusername).length==0){
		alert("���������˺ţ�");
		return false;
	}
	if($.trim(netusernamepwd).length==0){
		alert("�������������룡");
		return false;
	}
	isShowButton(false);
	$("tr[@id='trData']").show();
	$("div[@id='QueryData']").html("����ִ��...");
	
	var url = "<s:url value='/gtms/config/useParameter!doConfig.action'/>"; 
	$.post(url,{
		netusername:netusername,
		netusernamepwd:netusernamepwd
	} ,function(ajax){
          alert(ajax);
          isShowButton(true);
          $("div[@id='QueryData']").html("");
    });
}

function isShowButton(tag){
	if(tag){
		$("button[@name='doUpdate']").attr("disabled", false);
	}else{
		$("button[@name='doUpdate']").attr("disabled", true);
	}
}
</script>
</head>

<body>
<table border=0 cellspacing=0 cellpadding=0 width="98%" align="center">
	<tr>
		<td height=20>&nbsp;</td>
	</tr>
	<tr>
		<td>
			<table width="100%" height="30" border="0" cellspacing="0"
				cellpadding="0" class="green_gargtd">
				<tr>
					<td width="162" align="center" class="title_bigwhite" nowrap>
						������ʽ�޸�Ϊ·��
					</td>
					<td nowrap>
						<img src="../../images/attention_2.gif" width="15" height="12">
						&nbsp;������ʽ�޸�Ϊ·��
					</td>
				</tr>
			</table>
		</td>
	</tr>

	<tr bgcolor="#FFFFFF" >
		<td>
			<table border=0 cellspacing=1 cellpadding=2 width="100%" bgcolor=#999999>
				<tr bgcolor="#FFFFFF" >
					<td nowrap align="right" class=column width="15%">
						����˺ţ�
					</td>
					<td width="35%">
						<input type="text" name="netusername" value="">
					</td>
					<td nowrap align="right" class=column width="15%">
						�������룺
					</td>
					<td width="35%">
						<input type="text" name="netusernamepwd" value="">
					</td>
					<td class="green_foot" align="right">
						<button name="doUpdate" onclick="javascript:doConfig()">�� &nbsp;&nbsp;��</button>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
<table>
	<tr id="trData" style="display: none">
		<td class="colum">
			<div id="QueryData" style="width: 100%; z-index: 1; top: 100px">
			</div>
		</td>
	</tr>
</table>
</body>
</html>
<%@ include file="../../foot.jsp"%>
