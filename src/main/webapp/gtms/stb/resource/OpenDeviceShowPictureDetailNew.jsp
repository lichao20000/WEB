<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<%@page import="com.linkage.litms.LipossGlobals"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>

<script type="text/javascript">
	function resultConvert(res)
	{
		switch(res)
		{
			case 0:
				document.write("否");
				break;
			case 1:
				document.write("是");
				break;
			default:
				document.write("");
		}
	}
	
	function downloadHotel(taskId)
	{
		var url =  "<s:url value='/gtms/stb/resource/openDeviceShowPic!exportHotelUser.action'/>?taskId="+taskId;
		document.all("childFrm").src=url;
	}
		
	function checkPic()
	{
		var bootPic = $("input[@name='bootFile']").val();
		if(""!=bootPic){
			var bootfilet = bootPic.split(".");
	    	if(bootfilet.length<2){
	    		alert("开机图片必须上传!");
	    		return false;
	    	}
		}
			
		var startPic = $("input[@name='startFile']").val();
		if(""!=startPic){
			var startfilet = startPic.split(".");
	    	if(startfilet.length<2){
	    		alert("启动图片必须上传!");
	    		return false;
	    	}
		}
			
		var authPic = $("input[@name='authFile']").val();
		if(""!=authPic){
			var authfilet = authPic.split(".");
	    	if(authfilet.length<2){
	    		alert("认证图片必须上传!");
	    		return false;
	    	}
		}
			
		//新的修改，三者不能同时为空
		if(""==bootPic&&""==startPic&&""==authPic){
			alert("启动，开机，认证三张图片不能同时为空！");
			return false;
		}
		return true;
	}
		
	function ExecMod(task_id,pic1,pic2,pic3) 
	{
		var startPic = $("input[@id='startFile']").val();
		var bootPic = $("input[@id='bootFile']").val();
		var authPic = $("input[@id='authFile']").val();
		if(checkChinese(startPic)){
			alert("启动图片名称不可含有中文");
			return false;
		}

		if(checkChinese(bootPic)){
			alert("开机图片名称不可含有中文");
			return false;
		}

		if(checkChinese(authPic)){
			alert("认证图片名称不可含有中文");
			return false;
		}
			
		var isPicPass  =  checkPic();
		if(isPicPass){
			$("#doButton").attr("disabled",true);
			 var form = document.getElementById("form"); 
			$("form[@name='batchexform']").attr("action","OpenDeviceShowPictureNew!modPic.action"
															+"?bootFileName="+pic2
															+"&&startFileName="+pic1
															+"&&authFileName="+pic3
															+"&&taskId="+task_id);
			$("form[@name='batchexform']").submit();
		}else{
			return false;
		}
	}
		
		
	//中文校验
	function checkChinese(str)
	{
		var regTest = /^[\u4e00-\u9fa5]+$/;
		var flag = false;
		if(str != null && $.trim(str) != ""){
			if(str.indexOf("\\") != -1){
				var strArr = str.split("\\");
				str = strArr[strArr.length-1];
			}
			//alert(str);
	    	for(var i=0 ; i<str.length ; i++){
				var word = str.substring(i,i+1);
				if(regTest.test(word)){
					flag = true ; 
					break;
				}
			}
	    }
		return flag;
	}
</script>
</head>

<body>
<s:form action="OpenDeviceShowPictureNew!modPic.action" method="post" enctype="multipart/form-data" 
	name="batchexform" onsubmit="">
	<TABLE width="98%" align="center" class="querytable">
		<TR>
			<TD>
				<IMG height=20 src="<s:url value='/images/bite_2.gif'/>" width=24>
				您当前的位置：开机画面配置详细信息
			</TD>
		</TR>
	</TABLE>
	<br>
	<table class="querytable" width="98%" align="center">
		<tr>
			<td colspan="4" class="title_1">开机画面配置详细信息</td>
		</tr>
		<s:if test="taskDetailMap!=null && taskDetailMap.size()>0">
			<s:iterator value="taskDetailMap">
			<tr>
				<TD class="title_2" align="center" width="15%">任务名称</td>
				<TD width="85%"><s:property value="task_name" /></td>
			</tr>	
			<tr>
				<TD class="title_2" align="center" width="15%">属地</td>
				<TD width="85%"><s:property value="cityName" /></td>
			</tr>	
			<tr>
				<TD class="title_2" align="center" width="15%">分组</td>
				<TD width="85%"><s:property value="group_name" /></td>
			</tr>	
			<tr>
				<TD class="title_2" align="center" width="15%">优先级</td>
				<TD width="85%"><s:property value="priority" /></td>
			</tr>	
			<tr>
				<TD class="title_2" align="center" width="15%">厂商</td>
				<TD width="85%"><s:property value="vendorName" /></td>
			</tr>
			<tr>
				<TD class="title_2" align="center" width="15%">已选择型号</td>
				<TD width="85%"><s:property value="device_model" /></td>
			</tr>
			<tr>
				<TD class="title_2" align="center" width="15%">已选择版本</td>
				<TD width="85%"><s:property value="softwareversion" /></td>
			</tr>
			<tr>
				<TD class="title_2" align="center" width="15%">定制人</td>
				<TD width="85%"><s:property value="acc_loginname" /></td>
			</tr>
			<tr>
				<TD class="title_2" align="center" width="15%">定制时间</td>
				<TD width="85%"><s:property value="add_time" /></td>
			</tr>
			<tr>
				<TD class="title_2" align="center" width="15%">失效时间</td>
				<TD width="85%"  ><s:property value="Invalid_time" /></td>
			</tr>
            <tr>
	            <TD class="title_2" align="center" width="15%">启用业务帐号列表</td>
				 <%if("cq_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){ %>
					<TD width="85%"  >
						<s:if test='file_path=="否"'>
							否
						</s:if>
						<s:else>
							<a target='_blank' href='<s:property value="file_path_view" />'>业务帐号列表 </a>
						</s:else>
					</td>
					<%}else{ %>
					<TD width="85%"  >
						<s:if test='file_path=="否"'>
							否
						</s:if><s:else>
							<a target='_blank' href='<s:property value="file_path" />'>业务帐号列表 </a>
						</s:else>
					</td>
					<%} %>
            </tr>
            <%if("cq_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){ %>
            <tr>
				<TD class="title_2" align="center" width="15%">开机图片</td>
				<TD width="85%" >
					 <a target='_blank' href='<s:property value="sd_kj_pic_url_view" />'><s:property value="sd_kj_pic_url" /></a>
				</td>
			</tr>
			<tr>
				<TD class="title_2" align="center" width="15%" >修改开机图片</td>
				<TD width="85%" >
					<s:file label="上传" theme="simple" size="50"  id="startFile" name="startFile" ></s:file>&nbsp;&nbsp;&nbsp; 
				</td>
			</tr>
			<tr>
				<TD class="title_2" align="center" width="15%">开机动画</td>
				<TD width="85%"  >
					<a target='_blank' href='<s:property value="sd_qd_pic_url_view" />'><s:property value="sd_qd_pic_url" /></a>
				</td>
			</tr>
			<tr>
				<TD class="title_2" align="center" width="15%">修改开机动画</td>
				<TD width="85%"  >
					<s:file label="上传" theme="simple" size="50" id="bootFile" name="bootFile" ></s:file>&nbsp;&nbsp;&nbsp;
				</td>
			</tr>
			<tr>
				<TD class="title_2" align="center" width="15%">认证图片</td>
				<TD width="85%"  >
					<a target='_blank' href='<s:property value="sd_rz_pic_url_view" />'><s:property value="sd_rz_pic_url" /></a>
				</td>
			</tr>
			<tr>
				<TD class="title_2" align="center" width="15%">修改认证图片</td>
				<TD width="85%">
					<s:file label="上传" theme="simple" size="50" id="authFile" name="authFile" ></s:file>&nbsp;&nbsp;&nbsp;
				</td>
			</tr>
			<%} else{%>
			<tr>
				<TD class="title_2" align="center" width="15%">启动图片</td>
				<TD width="85%"><s:property value="sd_kj_pic_url" /></td>
			</tr>
			<tr>
				<TD class="title_2" align="center" width="15%">开机图片</td>
				<TD width="85%"><s:property value="sd_qd_pic_url" /></td>
			</tr>
			<tr>
				<TD class="title_2" align="center" width="15%">认证图片</td>
				<TD width="85%"><s:property value="sd_rz_pic_url" /></td>
			</tr>
			<%} %>
			<tr STYLE="display: none">
				<td colspan="4">
					<iframe id="childFrm" src=""></iframe>
				</td>
		   </tr>
		   <%if(!"jl_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){ %>
		   <tr bgcolor="#FFFFFF">
				<td colspan="2" align="right" class="foot" width="100%">
					<div align="right">
						<button onclick="javascript:ExecMod('<s:property value="task_id" />','<s:property value="sd_kj_pic_url" />','<s:property value="sd_qd_pic_url" />','<s:property value="sd_rz_pic_url" />');" name="gwShare_queryButton" id="doButton"
							style="CURSOR: hand" style="display:">提交更新</button>
					</div>
				</td>
			</tr>
			<%} %>
			</s:iterator>
		</s:if>
	</table>
</s:form>
<br>
<br>
</body>