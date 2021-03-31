<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>模版单元增加</title>
<base target="_self"/>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<script language="JavaScript">
function doAdd(){
	var templateUnitName = $("#templateUnitName");
	var templateURL =  $("#templateURL");
	if(null == templateUnitName.val()){
		templateUnitName.focus();
		return false;
	}
	if(null == templateURL.val()){
		templateURL.focus();
		return false;
	}
	var url = "<s:url value='/gtms/diagnostic/templateUnitManage!add.action'/>"; 
	$.post(url,{
		templateUnitName : encodeURIComponent(templateUnitName.val()),
		templateUnitURL : templateURL.val()
	},function(ajax){
	    if(ajax =="1" ){
			alert("增加成功");
		}else{
			alert("增加失败");	
		}
		window.location.reload();
	});
}
function doClose(){
	window.close();
	
} 
</script>
</head>
<body>
	<form action="">
		<table id="addTable" class="querytable">
			<tr>
				<td colspan="2" class=title_1>新增模版单元</td>
			</tr>
			<tr bgcolor=#ffffff>
				<td width="30%" class=column align=center>模版单元名称：</td>
				<td width="70%" ><input type="text" id="templateUnitName" name="templateUnitName" /></td>
			</tr>
			<tr bgcolor=#ffffff>
				<td width="30%" class=column align=center>模版单元地址：</td>
				<td width="70%" ><input type="text" id="templateURL" name="templateURL" /></td>
			</tr>
			<tr bgcolor=#ffffff>
			</tr>
			<tr bgcolor=#ffffff>
				<td colspan="2"  align="center" style="text-align:center;">
					<button  id ="add_newUnit" onclick="doAdd();" >&nbsp;保&nbsp;存&nbsp;</button>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<button type="button" id ="add_newUnit" onclick="doClose();" >&nbsp;关&nbsp;闭&nbsp;</button>
				</td>
			</tr>
		</table>
	</form>
</body>