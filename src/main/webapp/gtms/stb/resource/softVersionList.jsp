<%@page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<%@page import="com.linkage.litms.LipossGlobals"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet"	type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>


<SCRIPT LANGUAGE="JavaScript">
	//关联型号
	function vendorChange()
	{
		var vendorId = $("select[@name='vendorId']").val();
		var url = "<s:url value='/gtms/stb/resource/softVersion!getDeviceModel.action'/>";
		if(vendorId!="-1"){
			$("div[@id='deviceModel']").html("");
			$.post(url,{
				vendorId:vendorId
			},function(ajax){
				if(ajax!=""){
					var lineData = ajax.split("#");
					if(typeof(lineData)&&typeof(lineData.length)){
						for(var i=0;i<lineData.length;i++){
							var oneElement = lineData[i].split("$");
							var xValue = oneElement[0];
							var xText = oneElement[1];
							checkboxtxt = "<input type='checkbox' name='deviceModelId' value='"
												+xValue+"'>"+xText+"  ";
							$("div[@id='deviceModel']").append(checkboxtxt);
						}
					}else{
						$("div[@id='deviceModel']").append("该厂商没有对应型号！");
					}
				}else{
					$("div[@id='deviceModel']").append("该厂商没有对应型号！");
				}
			});
		}else{
			$("div[@id='deviceModel']").html("请选择厂商");
		}
	}
	
	//展示编辑区
	function addeditVersion(isAdd,id,vendorId,versionName,versionDesc,versionPath,
			deviceModelIds,fileSize,MD5,epg_version,net_type)
	{
		$("table[@id='addedit']").show();
		
		$("input[@name='versionPath']").val(versionPath);
		$("input[@name='isAdd']").val(isAdd);
		$("input[@name='id']").val(id);
		$("input[@name='versionName']").val(versionName);
		$("input[@name='versionDesc']").val(versionDesc);
		$("select[@name='vendorId']").val(vendorId);
		$("input[@name='fileSize']").val(fileSize);
		$("input[@name='MD5']").val(MD5);
		$("input[@name='epg_version']").val(epg_version);
		$("select[@name='net_type']").val(net_type);
		vendorChange();
		setTimeout('deviceModelIdchecked("'+deviceModelIds+'")', 1000);
	}
	
	//选择型号
	function deviceModelIdchecked(deviceModelIds)
	{
		var deviceModelId = deviceModelIds.split(",");
    	for(var i = 0;i<deviceModelId.length;i++){
	        $("input[@name='deviceModelId'][@value='"+deviceModelId[i]+"']").attr("checked",true);
    	}
	}
	
	//编辑或新增版本
	function ExecMod()
	{
		var isAdd = $("input[@name='isAdd']").val();
		var id = $("input[@name='id']").val();
		var versionDesc = $("input[@name='versionDesc']").val();
		var vendorId = $("select[@name='vendorId']").val();
		var versionPath= $("input[@name='versionPath']").val();
		var versionName = $("input[@name='versionName']").val();
		var fileSize = $("input[@name='fileSize']").val();
		var MD5 = $("input[@name='MD5']").val();
		var epg_version = $("input[@name='epg_version']").val();
		var net_type = $("select[@name='net_type']").val();
		
		if(versionName==""){
			alert("请输入版本名称!");
			$("input[@name='versionName']").focus();
			return;
		}
		
		if(vendorId=="-1"){
			alert("请选择厂商！");
			return;
		}
		if(versionPath==""){
			alert("请输入版本文件路径!");
			$("input[@name='versionPath']").focus();
			return;
		}
		if(fileSize==""){
			alert("请输入版本文件大小!");
			$("input[@name='fileSize']").focus();
			return;
		}
		if(MD5==""){
			alert("请输入MD5数据!");
			$("input[@name='MD5']").focus();
			return;
		}
		if(epg_version==""){
			alert("请输入EPG版本数据!");
			$("input[@name='epg_version']").focus();
			return;
		}
		
		var deviceModelId = "";
		$("input[@name='deviceModelId'][@checked]").each(function(){ 
	    	deviceModelId = deviceModelId + $(this).val()+",";
	    });
	    if(deviceModelId=="" || deviceModelId==null){
			alert("请选择适用型号!");
			return;
		}
	    
	    var url = "<s:url value='/gtms/stb/resource/softVersion!addedit.action'/>";
	    $.post(url,{
			isAdd:isAdd,
			versionDesc:versionDesc,
			vendorId:vendorId,
			versionPath:versionPath,
			deviceModelId:deviceModelId,
			versionName:versionName,
			id:id,
			fileSize:fileSize,
			MD5:MD5,
			epg_version:epg_version,
			net_type:net_type
		},function(ajax){
	    	var s = ajax.split(",");
	    	alert(s[1]);
	    	if(s[0]=="1"){
	    		var showType=$("input[name=showType]").val();
	    		var strpage = "<s:url value='/gtms/stb/resource/softVersion.action'/>?showType="+showType;
	    		window.location.href=strpage;
	    	}
	    });
	}
	
	//删除版本
	function deleteVersion(id)
	{
		if(!confirm("真的要删除该条记录吗？")){
			return false;
		}
		var url = "<s:url value='/gtms/stb/resource/softVersion!deleteVersion.action'/>";
	    $.post(url,{
			id:id
		},function(ajax){
	    	var s = ajax.split(",");
	    	alert(s[1]);
			if(s[0]=="1"){
				var showType=$("input[name=showType]").val();
				var strpage = "<s:url value='/gtms/stb/resource/softVersion.action'/>?showType="+showType;
				window.location.href=strpage;
			}
		});
	}
	
	function query()
	{
		var vendorId = $("select[name=queryVendorId]").val();
		var versionName = $("input[name=queryVersionName]").val();
		var showType=$("input[name=showType]").val();
		window.location.href="<s:url value='/gtms/stb/resource/softVersion.action'/>?"
								+"queryVendorId="+vendorId
								+"&queryVersionName="+versionName
								+"&showType="+showType;
	}
	
	function detailVersion(id)
	{
		window.location.href="<s:url value='/gtms/stb/resource/softVersion!getSoftVersionDetail.action'/>?id="+id;
	}
	
</SCRIPT>

</head>
<body>
<input type="hidden" name="showType" value="<s:property value='showType' />">
<TABLE width="98%" align="center" class="querytable">
	<TR>
		<TD>
			<IMG height=20 src="<s:url value='/images/bite_2.gif'/>" width=24> 
			您当前的位置：软件版本文件路径管理
		</TD>
	</TR>
</TABLE>

<br>
<table width="98%" class="querytable" align="center">
	<tr>
		<td class="title_2" align="center">厂商</td>
		<td width="30%">
			<s:select list="vendorList" name="queryVendorId" headerKey="-1" headerValue="全部" 
				listKey="vendor_id" listValue="vendor_add" value="queryVendorId" cssClass="bk"
				theme="simple">
			</s:select>
		</td>
		<td class="title_2" align="center">版本名称</td>
		<td width="30%">
			<input type="text" name="queryVersionName" maxlength="20" />
		</td>
		<td class="title_2" align="center">
			<button onclick="javascript:query();">查询</button>
		</td>
	</tr>
</table>
<br>

<table  width="98%" class="listtable" align="center">
	<thead>
		<tr bgcolor="#FFFFFF">
			<th align="center" width="8%">厂商</th>
			<th align="center" width="15%">适用型号</th>
			<th align="center" width="15%">版本名称</th>
			<th align="center" width="20%">版本描述</th>
			<th align="center" width="10%">EPG版本</th>
			<th align="center" width="10%">适用网络类型</th>
			<!-- <th align="center" width="20%">版本文件路径</th> -->
			<th align="center" width="15%">版本更新时间</th>
			<th align="center" width="8%">操作</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="versionList!=null && versionList.size()>0">
		<s:iterator value="versionList">
			<TR>
				<td align="center"><s:property value="vendor_add" /></td>
				<td align="center"><s:property value="device_model" /></td>
				<td align="center"><s:property value="version_name" /></td>
				<td align="center"><s:property value="version_desc" /></td>
				<td align="center"><s:property value="epg_version" /></td>
				<td align="center"><s:property value="net_type" /></td>
				<!-- <td align="center"><s:property value="version_path" /></td> -->
				<td align="center"><s:property value="update_time" /></td>
				<td align="center">
					<label onclick="javascript:detailVersion('<s:property value='id'/>');">
						<IMG SRC="<s:url value="/images/view.gif"/>" BORDER='0' ALT='详细信息' style='cursor:hand'>
					</label>
					
					<s:if test="showType!=1">
						<label onclick="javascript:addeditVersion('0','<s:property value='id'/>',
																	'<s:property value='vendor_id'/>',
																	'<s:property value='version_name'/>',
																	'<s:property value='version_desc'/>',
																	'<s:property value='version_path0'/>',
																	'<s:property value='device_model_id'/>',
																	'<s:property value='file_size'/>',
																	'<s:property value='md5'/>',
																	'<s:property value='epg_version'/>',
																	'<s:property value='net_type'/>');">
							<IMG SRC="<s:url value="/images/edit.gif"/>" BORDER='0' ALT='编辑' style='cursor: hand'> 
						</label>
						<s:if test="showType!=2">
							<label onclick="javascript:deleteVersion(<s:property value='id'/>);">
								<IMG SRC="<s:url value="/images/del.gif"/>" BORDER='0' ALT='删除' style='cursor: hand'> 
							</label>
						</s:if>
					</s:if>
				</td>
			</TR>
		</s:iterator>
		</s:if>
		<s:else>
			<tr>
				<td colspan="8">
					<font color="red">没有版本文件信息</font>
				</td>
			</tr>
		</s:else>
	</tbody>
	<tfoot>
		<tr bgcolor="#FFFFFF">
			<td colspan="8" align="right">
				<lk:pages url="/gtms/stb/resource/softVersion.action" styleClass="" showType=""
					isGoTo="true" changeNum="true" />
			</td>
		</tr>
		<ms:inArea areaCode="hn_lt" notInMode="true">
		<tr>
			<td colspan="8" align="left">
				<button onclick="javascript:addeditVersion('1','','-1','','','','','','','other','unknown_net');">
					添加记录</button>
			</td>
		</tr>
		</ms:inArea>
	</tfoot>
</table>
<br>
<br>

<table class="querytable" width="98%" align="center" style="display: none" id="addedit">
	<tr>
		<td class="title_1" colspan="2">
		<%if("hn_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){ %>
			编辑区
		<%}else{ %>
			添加/编辑区
		<%} %>
			
			<input type="hidden" name="isAdd" value=""> 
			<input type="hidden" name="id" value="">
		</td>
	</tr>
	<TR>
		<TD class="title_2" align="center" width="15%">版本名称</TD>
		<TD width="85%">
			<input type="text" name="versionName" id="versionName" class="bk" value="" size="40" maxlength="40">
			<font color="red">*长度小于40</font>
		</TD>
	</TR>
	<TR>
		<TD class="title_2" align="center" width="15%">版本描述</TD>
		<TD width="85%">
			<input type="text" name="versionDesc" id="versionDesc" class="bk" value="" size="40" maxlength="80">
			<font color="red">*长度小于80</font>
		</TD>
	</TR>
	<TR>
		<TD class="title_2" align="center" width="15%">厂商</TD>
		<TD width="85%">
			<s:select list="vendorList" name="vendorId"
				headerKey="-1" headerValue="请选择厂商" listKey="vendor_id"
				listValue="vendor_add" value="vendorId" cssClass="bk"
				onchange="vendorChange()" theme="simple">
			</s:select> <font color="red">*</font>
		</TD>
	</TR>
	<TR>
		<TD class="title_2" align="center" width="15%">适用型号</TD>
		<TD width="85%">
			<div id="deviceModel">请选择厂商</div>
		</TD>
	</TR>
	<TR>
		<TD class="title_2" align="center" width="15%">版本文件路径</TD>
		<TD width="85%">
			<input type="text" name="versionPath" id="versionPath" class="bk" value="" size="40"> 
			<font color="red">*</font>
		</TD>
	</TR>
	<TR>
		<TD class="title_2" align="center" width="15%">文件大小</TD>
		<TD width="85%">
			<input type="text" name="fileSize" id="fileSize" class="bk" value="" size="40"> 
			<font color="red">*</font>
		</TD>
	</TR>
	<TR>
		<TD class="title_2" align="center" width="15%">MD5</TD>
		<TD width="85%">
			<input type="text" name="MD5" id="MD5" class="bk" value="" size="40"> 
			<font color="red">*</font>
		</TD>
	</TR>
	<TR>
		<TD class="title_2" align="center" width="15%">EPG版本</TD>
		<TD width="85%">
			<input type="text" name="epg_version" id="epg_version" class="bk" value="" size="16"> 
			<font color="red">*</font>
		</TD>
	</TR>
	<TR>
		<TD class="title_2" align="center" width="15%">适用网络类型</TD>
		<TD width="85%">
			<select name="net_type" class="bk" >
				<option value="unknown_net" selected>未  知</option>
				<option value="public_net">公  网</option>
				<option value="private_net">专  网</option>
			</select>
		</TD>
	</TR>
	<tr>
		<td colspan="2" class="foot" align="right">
			<div class="right">
				<button onclick="javascript:ExecMod();">提交</button>
			</div>
		</td>
	</tr>
</table>

</body>