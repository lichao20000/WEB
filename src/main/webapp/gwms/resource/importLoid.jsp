<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%--
	/**
	 * 该jsp的描述信息
	 *
	 * @author wuchao(工号) tel：
	 * @version 1.0
	 * @since 2011-9-21 下午02:42:14
	 * 
	 * <br>版权：南京联创科技 网管科技部
	 * 
	 */
 --%>
<html>
<head>
<title>数图配置批量下发</title>
<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
<%@ include file="../../head.jsp"%>
<%@ include file="../../toolbar.jsp"%>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
$(function(){
	var isfirst = "<s:property value="isFword" />";
	if(isfirst=='1'){
		$("tr[@name='tr_result']").show();
	}else{
		$("tr[@name='tr_result']").hide();
	}
});





function check(){

	
	var task_name =document.all("task_name").value;
	var map_id=	$("select[@name='map_id']").val();
	 if(map_id=="-1")
	   {
	     alert("数图名称不能为空");
	     return false;
	   }
	   
	
	   if(task_name=="-1" || task_name=="")
	   {
	     alert("请填写任务名称！");
	     return false;
	   }
	   return true;
	   
}

function forward(){
	if(!check()){
		return
	}
	var map_id=	$("select[@name='map_id']").val();
	var device_idNormalStr=document.getElementById("device_idNormalStr").value;
	var name=document.getElementById("task_name").value;
	var task_name=encodeURIComponent(name);
	var url = "<s:url value="/gwms/resource/importLoidACT!forward.action"/>";
	$.post(url,{
			map_id:map_id,
			device_idNormalStr:device_idNormalStr,
			task_name:task_name
	},function(ajax){
	  
		alert("下发成功");	
		
		});
$("input[@name='task_name']").val("");

}

function doUpload(){
	
	var username=document.importFrm.file.value;
	if(username.replace(/(^\s*)|(\s*$)/g, "") == ""){
		alert("请先选择文件！");
		return false;
	}
	//按钮按下的时候把按钮置灰 add by zhangcong 2011-06-07
	document.importFrm.upload.disabled = true;
	//parent.idWait.innerHTML = "正在上传解析......";
	//idWait.innerHTML = "正在上传解析......";
	document.importFrm.submit();
	
}

</SCRIPT>
</head>
<body>

<!-- <form name="frm" action="itvConfig!doStatery.action" method="post"
	onsubmit="return checkForm();"> -->
<!--  	<input type="text" size=30 value="<s:property value="normalLoid"/>"></input>-->
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<TR>
		<TD>
		<table width="98%" border="0" align="center" cellpadding="0"
			cellspacing="0" class="text">
			<tr>
				<td>
				<table width="100%" height="30" border="0" cellspacing="0"
					cellpadding="0" class="green_gargtd">
					<tr>
						<td width="162" align="center" class="title_bigwhite" nowrap>
						数图配置批量下发</td>
						<td nowrap><img src="../../images/attention_2.gif" width="15"
							height="12"> &nbsp;导入LOID配置下发</td>
					</tr>
				</table>
				</td>
			</tr>
			<tr>
				<td>
				<TABLE width="100%" border=0 cellspacing=0 cellpadding=0
					align="center">
					<TR>
						<TD bgcolor=#999999>
						<table border=0 cellspacing=1 cellpadding=2 width="100%">
							<TR>
								<TH colspan="4" align="center">数图配置批量下发</TH>
							</TR>
							<tr id="importUserQuery" bgcolor="#FFFFFF" style="display: ">
								<td align="right">提交文件：</td>
								<td colspan="3">

								<form name="importFrm"
									action="importLoidACT!getDeviceByImportLoid.action"
									method="POST" enctype="multipart/form-data">
								<table>
									<tr>
										<td><input type="file" size="60" name="file" /> <!-- <input type="button" value="说 明" name="filedesc" onclick="parent.showDesc()" /> -->
										<input type="button" value=" 提 交  " class=jianbian
											name="upload" onclick="doUpload()" /></td>
									</tr>
								</table>
								</form>
                              </td>
							</tr>
							<%
							if ("nx_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName")))
							{
							%>
                            <tr id="importUserQuery2" style="display: " bgcolor="#FFFFFF">
								<td align="right">注意事项：
								<td colspan="3"><font color="#7f9db9">
								1、需要导入的文件格式为Excel。 <br>
								2、文件的第一行第一列为Loid。 <br>
								3、文件的行数不超过100行,如超过100行，只解析前100行数据。 <br>
								4、请确保文件内设备下发的数图配置相同 </font></td>
							</tr>
							<%
							}
							else
							{
							%>
							<tr id="importUserQuery2" style="display: " bgcolor="#FFFFFF">
								<td align="right">注意事项：
								<td colspan="3"><font color="#7f9db9">
								1、需要导入的文件格式为Excel。 <br>
								2、文件的第一行为标题行，即Loid。 <br>
								3、文件只有一列。 <br>
								4、文件的行数不超过100行,如超过100行，只解析前100行数据。 <br>
								5、请确保文件内设备下发的数图配置相同 </font></td>
							</tr>
							<%
							}
							%>
							<!-- 添加一个隐藏的占位栏，保持界面风格一致 -->
							<tr id="space" style="display: " bgcolor="#FFFFFF">
								<td colspan="4" align="right" CLASS="green_foot">&nbsp;</td>
							</tr>
							<tr style="display: " bgcolor="#FFFFFF">
								<td colspan="4" height="20">&nbsp;</td>
							</tr>
							<tr bgcolor="#FFFFFF" name="tr_result" style="display: ">
								<th colspan="4" align="center" id="1">文件解析结果</th>
							</tr>

							<tr bgcolor="#FFFFFF" name="tr_result" style="display: ">
								<td align="right">导入的Loid个数</td>
                                <td colspan="3"><s:property value="allLoid" /></td>
							</tr>
                           	<tr bgcolor="#FFFFFF" name="tr_result" style="display: ">
								<td align="right">已查询到的设备个数</td>
								<!--  		<td colspan="3"><input type="text" size=50
									value="<s:property value="normalLoid"/>"></input></td>-->
								<td colspan="3"><s:property value="normalLoid" /></td>
							</tr>
                         	<tr bgcolor="#FFFFFF" name="tr_result" style="display: ">
								<td align="right">未查到的LOID</td>
								<td colspan="3"><s:property value="deviceExceptionStr" /></td>
							</tr>
                            <tr bgcolor="#FFFFFF" name="tr_result" style="display: ">
								<td align="right">数图模版名称</td>

								<td><s:select list="digitMapList" name="map_id"
									headerKey="-1" headerValue="请选择数图名称" listKey="map_id"
									listValue="map_name" value="map_id" cssClass="bk"></s:select></td>
							</tr>

							<tr bgcolor="#FFFFFF" name="tr_result" style="display: ">
								<td align="right">任务名称</td>
								<td colspan="3"><input id="task_name" type="text"
									name="task_name" size=50></input> <font color="#FF0000">*</font>
								配置下发时必须要填</td>

							</tr>
                           <tr bgcolor="#FFFFFF" name="tr_result" style="display: ">
								<td colspan="4" align="right" CLASS="green_foot"><input
									type="button" class=jianbian name=queryBtn value=" 配置下发 "
									onclick="forward();"> &nbsp;</td>
							</tr>
                           	<tr bgcolor="#FFFFFF" style="display: none">
								<td align="right">设备id正常</td>
								<td colspan="3"><input id="device_idNormalStr" type="text"
									name="device_idNormalStr" size=50
									value="<s:property value="device_idNormalStr"/>"></input></td>
							</tr>

						</table>
						</td>
					</TR>
				</table>
				</td>
				<tr>
		</table>
		</TD>
	</TR>
	<tr>
		<td height=20></td>
	</tr>
</TABLE>
<!--  </form>-->
</body>
<%@ include file="../../foot.jsp"%>
</html>