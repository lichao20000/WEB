<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>���û�����һ��ͬ��</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="/Js/CheckFormForm.js"></SCRIPT>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript">

function query(){
	var loid=$.trim($("input[@name='loid']").val());
	if(""==loid)
	{
	alert("����дloid!");
	return false;
	}
	var url = "<s:url value='itms/service/itvServConfig!query.action'/>";
	$.post(url,{
		loid:loid
	},function(ajax){
	    $("div[@id='div_user']").html("");
		$("div[@id='div_user']").append(ajax);
	});
	document.all("tr_userinfo").style.display="";
}

function doServ()
{
	var loid=$.trim($("input[@name='loid']").val());
	var url = "<s:url value='itms/service/itvServConfig!config.action'/>";
	$.post(url, {
		loid:loid
	}, function(ajax) {
		if (ajax == "�ɹ�") {
			alert("�·��ɹ�!");
		} else {
			alert("�·�ʧ��!");
		}
	});
}
</script>
</head>

<body>
	<form name="frm" id="frm" method="post"
		action=""
		target="dataForm">
		<table>
			<tr>
				<td HEIGHT=20>&nbsp;</td>
			</tr>
			<tr>
				<td>
					<table class="green_gargtd">
						<tr>
							<td width="162" align="center" class="title_bigwhite">
								���û�����һ��ͬ��</td>
							<td><img src="<s:url value="/images/attention_2.gif"/>"
								width="15" height="12" /><font color="red">*</font></td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<table class="querytable">

						<TR>
							<th colspan="4">���û�����һ��ͬ��</th>
						</tr>
						<TR bgcolor="#FFFFFF" id="deviceType21" STYLE="">
							<TD align="right" class=column width="15%">LOID��</TD>
							<TD  width="35%"><INPUT TYPE="text"
								NAME="loid" maxlength=30 class=bk size=20>&nbsp; <font
								color="red"> *</font></TD>
						</TR>
						<TR>
							<td colspan="4" align="right" class="foot" width="100%">
								<input type='button' value = '��ѯ' onclick="query()"></input>
							</td>
						</TR>
			<TR style="display: none;width: 100%;" id="tr_userinfo">
			<TD colspan="4" width="100%">
				<div id="div_user"
					style="width: 100%;">
				</div>
			</TD>
			</TR>
		<TR style="width: 100%">
			<TD align="center" colspan="4" width="100%">
				<div id="div_bind" style="display: none;text-align: center;width: 100%;" >
				</div>
			</TD>
		</TR>
					</table>
				</td>
			</tr>
			<tr>
				<td height="25" id="resultStr"></td>
			</tr>
		</table>
		<br>
	</form>
</body>
</html>
