<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>新增模版</title>
<base target="_self"/>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<script language="JavaScript">
function doAdd(){
	var templateName = $("#templateName");
	var templateParam =  "";
	$("input[@name='diagtemplate'][@checked]").each(function(){
   		 templateParam  += $(this).val()+",";
    });
	if(null == templateName.val()){
		templateName.focus();
		return false;
	}
	var url = "<s:url value='/gtms/diagnostic/diagTemplate!add.action'/>";
	$.post(url,{
		templateName :  encodeURIComponent(templateName.val()),
		templateParam : templateParam
	},function(ajax){
	    if(ajax =="1" ){
			alert("增加成功");
		}else{
			alert("增加失败");
		}
		window.location.href=window.location.href;
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
				<td colspan="2" class=title_1>新增模版	</td>
			</tr>
			<tr bgcolor=#ffffff>
				<td width="50%" class=column align=center>模版名称：</td>
				<td width="50%"><input type="text" id="templateName" name="templateName" /></td>
			</tr>
			<tr bgcolor=#ffffff>
				<td colspan="unitList.size()"  align="left" class=column >配置单元项：</td>
				<td class=title_4 align="right" colspan="2">
						<table>
							<s:if test="unitList!=null">
								 <s:iterator value="unitList" var="map" status="var">
								<tr>
									<td >
										<input type="checkbox" id="cid" value="<s:property value="id"/>" name="diagtemplate">
										<s:property value="unit_name"/>
										</input>
									</td>
								</tr>
								</s:iterator>
							</s:if>
						</table>
				 </td>

			</tr>
			<tr bgcolor=#ffffff>
				<td colspan="2"  align="center" style="text-align:center;">
					<button id ="add_newUnit" onclick="doAdd();"  />&nbsp;保&nbsp;存&nbsp;</button>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<button id ="add_newUnit" onclick="doClose();"  />&nbsp;关&nbsp;闭&nbsp;</button>
				</td>
			</tr>
		</table>
	</form>
</body>
