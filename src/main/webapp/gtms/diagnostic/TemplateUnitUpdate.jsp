<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>模版单元修改</title>
<base target="_self"/>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<script language="JavaScript">

function doUpdate(){
	var unitId = $("#unitId");
	var templateName = $("#templateName");
	var templateURL =  $("#templateURL");
	if(null == templateName.val()){
		templateName.focus();
		return false;
	}
	if(null == templateURL.val()){
		templateURL.focus();
		return false;
	}
	var url = "<s:url value='/gtms/diagnostic/templateUnitManage!update.action'/>"; 
	$.post(url,{
		unitId :unitId.val(),
		templateUnitName : encodeURIComponent(templateName.val()),
		templateUnitURL : templateURL.val()
	},function(ajax){
	   if(ajax =="1" ){
			alert("修改成功");
		}else{
			alert("修改失败");	
		}
		window.location.href=window.location.href;
	});
}
</script>
</head>
<body>
	<form action="">
		<s:if test="null != map">
		<input type="hidden" id="unitId" name ="unitId" value ='<s:property value="map.id"/>'>
		<table id="EditTable" class="querytable">
			<tr  bgcolor=#ffffff>
				<td colspan="2">修改模版单元</td>
			</tr>
			<tr bgcolor=#ffffff>
				<td width="50%" class=column align=center>模版单元名称：</td>
				<td width="50%"   align=center>
					<input type="text" id="templateName" name="templateName" 
					value='<s:property value ="map.unit_name"/>' />
				</td>
			</tr>
			<tr bgcolor=#ffffff>
				<td width="50%"  class=column align=center>模版单元地址</td>
				<td width="50%"   align=center>
					<input type="text" id="templateURL" name="templateURL"
					value='<s:property value ="map.unit_url"/>' />
				</td>
			</tr>
			<tr bgcolor=#ffffff>
				<td colspan="2"  style="text-align:center;" align=center>
					<input type="button" id ="update_unit" onclick="doUpdate();" value="保存" />
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<input type="button" onclick="window.close();" value="关闭" />
				</td>
			</tr>
		</table>
		</s:if>
	</form>
</body>
</html>