<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>����ģ��</title>
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
			alert("���ӳɹ�");
		}else{
			alert("����ʧ��");
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
				<td colspan="2" class=title_1>����ģ��	</td>
			</tr>
			<tr bgcolor=#ffffff>
				<td width="50%" class=column align=center>ģ�����ƣ�</td>
				<td width="50%"><input type="text" id="templateName" name="templateName" /></td>
			</tr>
			<tr bgcolor=#ffffff>
				<td colspan="unitList.size()"  align="left" class=column >���õ�Ԫ�</td>
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
					<button id ="add_newUnit" onclick="doAdd();"  />&nbsp;��&nbsp;��&nbsp;</button>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<button id ="add_newUnit" onclick="doClose();"  />&nbsp;��&nbsp;��&nbsp;</button>
				</td>
			</tr>
		</table>
	</form>
</body>
