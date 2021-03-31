<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>模版修改</title>
<base target="_self"/>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<script language="JavaScript">
 $(function(){
    $("input[@name='diagtemplate']").attr("checked",false);
    var templateParam = '<s:property value="map.template_param"/>';
    var diagtemplateLt = templateParam.split(",");
    for(i = 0;i<diagtemplateLt.length;i++){
        $("input[@name='diagtemplate'][@value='"+diagtemplateLt[i]+"']").attr("checked",true);
    }
  });

function doUpdate(){
	var diagId = $("#diagId");
	var templateParam ;
	var templateName = $("#templateName");
	$("input[@name='diagtemplate'][@checked]").each(function(){
   		 templateParam  += $(this).val()+",";
    });
	if(null == templateName.val()){
		templateName.focus();
		return false;
	}
	var url = "<s:url value='/gtms/diagnostic/diagTemplate!update.action'/>";
	$.post(url,{
		diagId :diagId.val(),
		templateName : encodeURIComponent(templateName.val()),
		templateParam : templateParam
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
		<input type="hidden" id="diagId" name ="diagId" value ='<s:property value="map.id"/>'>
		<table id="EditTable" class="querytable">
			<tr  bgcolor=#ffffff>
				<td colspan="2" class=title_1>修改模版</td>
			</tr>
			<tr bgcolor=#ffffff>
				<td width="30%" class=column align=center>模版名称：</td>
				<td width="70%"  align=center>
					<input type="text" id="templateName" name=templateName
					value='<s:property value ="map.template_name"/>' />
				</td>
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
				<td colspan="2"  style="text-align:center;" >
					<input type="button" id ="update_unit" onclick="doUpdate();" value="保存" />
					&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
					<input type="button" id ="update_unit" onclick="window.close();" value="关闭" />
				</td>
			</tr>
		</table>
		</s:if>
	</form>
</body>
</html>
