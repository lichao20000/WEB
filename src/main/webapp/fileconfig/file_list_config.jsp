<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="ms" uri="/ailk-itms-web-tags" %>
<%@ page import="com.linkage.litms.filemanage.FileManage" %>
<%@page import="com.linkage.litms.LipossGlobals"%>
<jsp:useBean id="deviceAct" scope="request" class="com.linkage.litms.resource.DeviceAct" />
<jsp:useBean id="fileManage" scope="request" class="com.linkage.litms.filemanage.FileManage"/>
<%
	//String deviceTypeList = versionManage.getDeviceTypeList("",false);
	
	String str_VendorList = deviceAct.getVendorList(true, "", "_vendor_id");
	
	//属地
	String cityList = deviceAct.getCityListAll(false,"","city_id",request);
	
	//业务
	String serviceList = FileManage.getServiceList(false,"","service_id",request);
	
	String area = LipossGlobals.getLipossProperty("InstArea.ShortName");
	
	int fileType =1;
    Cursor cursor = (Cursor)fileManage.getCursor(fileType);
    Map fields = cursor.getNext();
%>

<script type="text/javascript" src="../Js/jquery.js"></script>
<script type="text/javascript" src="../Js/CheckForm.js"></script>
<SCRIPT LANGUAGE="JavaScript">

var area = '<%=area%>'

function queryfile()
{

	if (document.frm1._vendor_id.value == "-1")
	{
		alert("请选择厂商");
		return;
	}  
 
	var status = 1;
	
	document.all("operResult").innerHTML="正在统计,请稍后......";
	var page="file_listdata.jsp?device_model_id="+ document.frm1.device_model_id.value
			+ "&filestatus=" + status 
			+ "&filename="+document.frm1.filename.value
			+ "&vendor_id="+document.frm1._vendor_id.value
			+ "&city_id=" + document.frm1.city_id.value + "&service_id=" + document.frm1.service_id.value;		
	document.all("childFrm").src = page;
}

function delWarn(){
	if(confirm("真的要删除该文件吗？\n本操作所删除的不能恢复！！！")){
		return true;
	}
	else{
		return false;
	}
}

function addFile() {
	document.frm.reset();
	$("#filename").removeClass("disable");// 文件名可编辑
	$("#vendorList").removeClass("disable");// 厂商可选
	$("#uploadTr").show();// 上传文件显示
	$("#save").hide();// 保存按钮隐藏
	$('#uploadFrm').attr('src', $('#uploadFrm').attr('src'));
	$("#addFile").show();// 显示新增框
	$("#fileserver").show();
	$("#filePath").hide();
	$("#editTypeText").html("添加版本文件");
	// 添加完成自动关闭 代码在 FileUploadResult.jsp
}

function editFile(param) {
	/* 	var page = "fileEdit.jsp";
	page += "?softwarefile_id=" + param;
	window.open(page,"版本文件","width=800,height=520,resizable=no,scrollbars=yes"); */
	
	param = eval("("+param+")");
	document.frm.filename.value = param.softwarefile_name;// 文件名
	document.frm.softwarefile_id.value = param.softwarefile_id;// 版本文件id
	document.frm.remark.value = param.softwarefile_description;// 备注
	document.frm.file_status.value = param.softwarefile_status;// 状态
	
	window.device_model = window.devModelMap[param.device_model_id];
	
	getFileMap(param.softwarefile_id);
	getDevTypeByDevTypeId(param.devicetype_id, device_model);
	
	$("#filename").addClass("disable");// 名称禁用
	$("#vendorList").addClass("disable");// 厂商禁用
	$("#uploadTr").hide();// 上传文件隐藏
	$("#save").show();// 保存文件显示
	$("#editTypeText").html("编辑版本文件");
	$("#fileserver").hide();
	$("#filePath").show();
	
	$("#addFile").show();// 显示编辑框
}

function closeAdd(){
	$("#addFile").hide();
}

// 获取版本版本信息
function getFileMap(softwarefile_id){
	$.ajax({
		type:"Post",
		url: "fileAct.jsp",
		data: {
			action: "getFileMap",
			softwarefile_id: softwarefile_id
		},
		async: false,
		dataType:'text',
		success:function(data){
			if(data.indexOf("html") == -1){
				var fileMap = eval("("+data+")"); 
				var server_name = fileMap.server_name;
				var server_path = fileMap.server_dir;
				document.frm.filePath.value = server_name + "|" + server_path;// 存储路径
			}
		},
		error:function(e){
			alert("服务器异常");
			console.info("e",e);
		}
	});
}

// 根据版本id获取版本信息
function getDevTypeByDevTypeId(devicetype_id, device_model){
	$.ajax({
		type:"Post",
		url: "fileAct.jsp",
		data: {
			action: "getDevTypeByDevTypeId",
			devicetype_id: devicetype_id
		},
		async: false,
		dataType:'text',
		success:function(data){
			if(data.indexOf("html") == -1){
				var devTypeMap = eval("("+data+")"); 
				
				var vendor_id = devTypeMap.vendor_id;
				window.softwareversion = devTypeMap.softwareversion;
				
				var vendor_name = window.devVendorMap[vendor_id];
				document.frm.vendor_name.value = vendor_name;// 厂商
				showChild("vendor_name");
			}
		},
		error:function(e){
			alert("服务器异常");
			console.info("e",e);
		}
	});
}

// 获取厂商信息 Map<vendor_id,vendor_name> 包含 Map<vendor_name,vendor_id>
function getVendorInfo(){
	$.ajax({
		type:"Post",
		url: "fileAct.jsp",
		data: {
			action: "getVendorInfo"
		},
		async: false,
		dataType:'text',
		success:function(data){
			if(data.indexOf("html") == -1){
				window.devVendorMap = eval("("+data+")"); 
			}
		},
		error:function(e){
			alert("服务器异常");
			console.info("e",e);
		}
	});
}

// 获取型号信息 Map<device_model_id, device_model> 包含 Map<device_model,device_model_id>
function getModelInfo(){
	$.ajax({
		type:"Post",
		url: "fileAct.jsp",
		data: {
			action: "getModelInfo"
		},
		async: false,
		dataType:'text',
		success:function(data){
			if(data.indexOf("html") == -1){
				window.devModelMap = eval("("+data+")"); 
			}
		},
		error:function(e){
			alert("服务器异常");
			console.info("e",e);
		}
	});
}

function saveFile()
{
	
	if(!checkSave()){
		return;
	}
	
	// 厂商id
	var vendor_id = window.devVendorMap[document.frm.vendor_name.value];
	// 型号id
	var device_model_id = document.frm.device_model_id.value;
	// 硬件版本
	var hardwareversion = document.frm.hardwareversion.value;
	// 软件版本
	var softwareversion = document.frm.softwareversion.value;
	
	// 根据厂商id，型号id，软硬件版本获取devicetype_id
	$.ajax({
		type:"Post",
		url: "fileAct.jsp",
		data: {
			action: "getDevTypeId",
			vendor_id: vendor_id,
			device_model_id: device_model_id,
			hardwareversion: hardwareversion,
			softwareversion: softwareversion
		},
		dataType:'text',
		async: false,
		success:function(data){
			var devType = eval("("+data+")"); 
			if(devType){
				document.frm.devicetype_id.value = devType.devicetype_id;
			}
		},
		error:function(e){
			alert("服务器异常");
			console.info("e",e);
		}
	});
	
	// 保存
	$.ajax({
		type:"Post",
		url: "fileSave.jsp",
		data: $("#frm").serialize(),
		dataType:'text',
		success:function(data){
			if(data){
				if(data.indexOf("成功") != -1){
					if(document.frm1._vendor_id.value != "-1"){
						queryfile();
					}
					closeAdd();
				}else{
					alert(data);
				}
			}
		},
		error:function(e){
			alert("服务器异常");
			console.info("e",e);
		}
	});
}

function checkSave() {

	// 型号
	var device_model_2 = document.frm.device_model_2.value;
	
	if(device_model_2 == -1){
		alert("请选择型号");
		return false;
	}
	document.frm.device_model_id.value = window.devModelMap[device_model_2];
	
	// 目标版本（软件版本）
	var softwareversion = document.frm.softwareversion.value;
	if(softwareversion == -1){
		alert("请选择目标版本");
		return false;
	}
	
	return true;
}

function deleteFile(param) {

	if (delWarn() == false)
	{
		return;
	}

	$.ajax({
		type:"Post",
		url: "fileSave.jsp",
		data: {
			"action": "delete",
			"softwarefile_id": param
		},
		dataType:'text',
		success:function(data){
			if(data){
				if(data.indexOf("成功") != -1){
					if(document.frm1._vendor_id.value != "-1"){
						queryfile();
					}
					
				}else{
					alert(data);
				}
			}
		},
		error:function(e){
			alert("服务器异常");
			console.info("e",e);
		}
	});
	
}
</SCRIPT>


<SCRIPT LANGUAGE="JavaScript">

function IsNull(strValue,strMsg){
	if(Trim(strValue).length>0) return true;
	else{
		alert(strMsg+'不能为空');
		return false;
	}
}
function Trim(strValue){
	var v = strValue;
	var i = 0;
	while(i<v.length){
	  if(v.substring(i,i+1)!=' '){
		v = v.substring(i,v.length) 
		break;
	  }
	  i = i + 1;
	  if(i==v.length){
        v="";
      }
	}

	i = v.length;
	while(i>0){
	  if(v.substring(i-1,i)!=' '){
	    v = v.substring(0,i);
		break;
	  }	
	  i = i - 1;
	}

	return v;
}
	function showChild(param){
		if(param == "fileserver"){
			var serValue = document.frm.fileserver.value;
			if(serValue == -1){
				document.frm.serUser.value="";
				document.frm.serPass.value="";
			}else{
				var arrServer = serValue.split("|");
				document.frm.serUser.value=arrServer[3];
				document.frm.serPass.value=arrServer[4];
			}		
		}
		
		if(param == "_vendor_id"){
			page = "showDeviceModel.jsp?vendor_id="+document.frm1._vendor_id.value + "&flag=1";
			document.all("childFrm").src = page;
		}
		
		if(param == 'vendor_name'){
			queryModel(param);
		}
		
		if(param == 'device_model_2'){
			queryVersion();
			queryHardware();
		}
	}

	function checkform(){
		if(document.frm.devicetype_id.value == -1){
			alert("请选择设备型号！");
			document.frm.devicetype_id.focus();
			return false;
		}
		if(!IsNull(document.frm.filename.value,"文件名")){
			document.frm.filename.focus();
			document.frm.filename.select();
			return false;
		}
	}
	
	function GoList(page){
		this.location = page;
	}
	
	function ChkAllCity()
	{
		for (var i=0;i<frm.elements.length;i++)
		{
		var e = frm.elements[i];
		if (e.name == "city")
		   e.checked = frm.cityAll.checked;
		}
	}
	
	function ChkAllService()
	{
		for (var i=0;i<frm.elements.length;i++)
		{
		var e = frm.elements[i];
		if (e.name == 'service')
		   e.checked = frm.serviceAll.checked;
		}
	}
	
	function ChkAllDevModel(){
		var devModel = document.getElementsByName("deviceModel");
		if(devModel){
			for (var i=0;i<devModel.length;i++){
				devModel[i].checked = frm.all("deviceModelAll").checked;
			}
		}
	}
	
	function queryVendor(){
		var url = '../Resource/devVenderModelAction!getVendorList.action';
		$.post(url,{
    	},function(mesg){
    		document.getElementById("vendorList").innerHTML = mesg;
   		});
	}

	function queryModel(vendorname){
		var vend = document.all(vendorname).value;
		var url = '../Resource/devVenderModelAction!getDeviceModelList.action';
		$.post(url,{
			vendorname:vend
    	},function(mesg){
    		document.getElementById('div_device_model_2').innerHTML = mesg.replace(/device_model/gi,'device_model_2');

    		if(window.device_model){
    			document.frm.device_model_2.value = window.device_model;// 型号
    			showChild("device_model_2");
    		}
  		});
	}

	function queryHardware(){
		var vend = document.all("vendor_name").value;
		var model = document.all("device_model_2").value;
		var url = '../Resource/devVenderModelAction!getHardwareversionStr.action';
		$.post(url,{
			vendorname:vend,
			deviceModel:model
    	},function(mesg){
    		document.getElementById("hardwareversion").value = mesg;
  		});
	}
	
	function queryVersion(){
		var vend = document.all("vendor_name").value;
		var model = document.all("device_model_2").value;	
		var url = '../Resource/devVenderModelAction!getVersionList.action';
		$.post(url,{
			vendorname:vend,
			deviceModel:model
    	},function(mesg){
    		//alert(mesg);
    		document.getElementById("div_softwareversion").innerHTML = mesg;
    		
    		if(window.softwareversion){
    			document.frm.softwareversion.value = softwareversion;// 软件版本
    		}
    	});
	}
</SCRIPT>


<style type="text/css">
select {
	position:relative;
	font-size:12px;
	width:160px;
	line-height:18px;border:0px;
	color:#909993;
}
.disable{
	pointer-events: none;
}
</style>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<FORM NAME="frm1" METHOD="post" ACTION="" onsubmit="return CheckForm()">

<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="98%" BORDER="0" align="center">
<TR>
	<TD valign=top>
	<TABLE width="100%" border=0 cellspacing=0 cellpadding=0>
		<TR>
			<TD HEIGHT=20><div id="operResult" style='width:20%;display:none;background-color:#33CC00'></div></TD>
		</TR>
		<tr>
			<td>
				<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
					<tr>
						<td width="162" align="center" class="title_bigwhite">
							版本文件管理
						</td>
						<td>
							&nbsp;<img src="../images/attention_2.gif" width="15" height="12">
								&nbsp;查询版本文件服务器上的版本文件。带<font color="red">*</font>的必须选择或输入.
						</td>
					</tr>
				</table>
			</td>
		</tr>		
		<TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
				<TR>
					<TH  colspan="4" align="center"><B>版本文件查询</B></TH>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" width="10%" nowrap>名称</TD>
					<TD width="40%" colspan="3">
						<INPUT TYPE="text" name="filename" class="bk" style="width: 225px">&nbsp;
					</TD>
				</TR>

				<TR bgcolor="#FFFFFF">
					<TD class=column align="right">
						<font color="red">*</font>&nbsp;设备厂商
					</TD>
					<TD>
						<%=str_VendorList%>
					<TD class=column align="right">
						设备型号
					</TD>
					<TD>
						<div id="div_device_model_id">
							<select name="device_model_id" class="bk" style="width: 225px">
								<option value="-1">
									--请先选择厂商--
								</option>
							</select>
						</div>
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right">属地</TD>
					<TD>
						<%=cityList %>
					</TD>
					<TD class=column align="right">业务</TD>
					<TD>
						<%=serviceList %>
					</TD>
				</TR>
				
				<TR class=green_foot>
					<TD colspan="4" align="right" >
						<INPUT TYPE="button" value=" 查 询 " class=btn onclick="queryfile()">&nbsp;&nbsp;
						<INPUT TYPE="button" value=" 新增 " class=btn onclick="addFile()">&nbsp;&nbsp;
						<INPUT TYPE="reset" value=" 重 置 " class=btn>
					</TD>
				</TR>
			</TABLE>
			</TD>
		</TR>
		<TR height="23" nowrap><TD></TD></TR>
		<TR style="display:none" id="dispTr">
			<TD><span id="userTable"></span></TD>
		</TR>
		<TR>
			<TD HEIGHT=20>&nbsp; 
				<IFRAME ID="childFrm" SRC="" STYLE="display:none">
				</IFRAME>
			</TD>
		</TR>
	</TABLE>
	</TD>
</TR>
</TABLE>
</FORM>

<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0" id="addFile" style="display: none">
<TR>
	<TD valign=top>
	<FORM NAME="frm" METHOD="post" ACTION="fileSave.jsp" id="frm" onsubmit="return checkform()">
	<INPUT TYPE="hidden" name="action" value="update">
	<input type="hidden" name="file_status" value="1">
	<INPUT TYPE="hidden" NAME="device_model_id" value="">
	<input type="hidden" name="softwarefile_id" value="">
	<input type="hidden" name="devicetype_id" value="">
	<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
				<TR>
					<TH  colspan="4" align="center"><B id="editTypeText">添加版本文件</B></TH>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" width="15%" nowrap><font color="red">*</font>&nbsp;名称</TD>
					<TD width="35%" colspan="3">
						<INPUT TYPE="text" NAME="filename" id="filename" class="bk" maxlength=30 size=30 style="width: 225px">&nbsp;&nbsp;<font color="red">名称不能包含中文、空格和特殊字符</font>
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right"  width="15%"><font color="red">*</font>&nbsp;设备厂商</TD>	
					<TD width="35%"><div id="vendorList"></div></TD>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" width="15%"><font color="red">*</font>&nbsp;设备型号</TD>	
					<TD width="35%"><div id="div_device_model_2"></div></TD>
				</TR>
				<TR bgcolor="#FFFFFF" height="25">
					<TD class=column align="right" width="15%"><font color="red">*</font>&nbsp;目标版本</TD>	
					<TD width="35%" colspan="3"><div id="div_softwareversion"></div></TD>
					<INPUT TYPE="hidden" ID="hardwareversion" NAME="hardwareversion" value="">
				</TR>
				
				<TR bgcolor="#FFFFFF" >
					<TD class=column align="right" nowrap ><font color="red">*</font>&nbsp;存储路径</TD>
					<TD colspan="3" nowrap>
						<select name="fileserver" class="bk" id="fileserver" onchange="showChild('fileserver')" style="width: 225px">
						<%
							if(fields == null){
						%>
								<option value="-1">==此项没有记录==</option>
						<%
							}else{
						%>
								<option value="-1">==请选择==</option>
						<%
								while(fields != null){
						%>
									<option value="<%=fields.get("inner_url")%>|<%=fields.get("server_dir")%>|<%=fields.get("dir_id")%>|<%=fields.get("access_user")%>|<%=fields.get("access_passwd")%>|<%=fields.get("outter_url")%>">
										<%=fields.get("server_name")%>
									</option>
						<%			
									fields = cursor.getNext();
								}
							}
						%>
						</select>
						<input type="text" size="80" name="filePath" id="filePath" class="bk" value="" readOnly  style="width: 225px">&nbsp;&nbsp; 
						<input type="radio" value="0" name="inORout" checked>用户访问URL上传&nbsp;&nbsp;
						<input type="hidden" value="1" name="inORout">
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF" style="display:none">
					<TD class=column align="right" >用户名</TD>
					<TD >
						<input type="text" name="serUser" class="bk" value="">			
					</TD>
					<TD class=column align="right" >密码 </TD>	
					<TD >
						<input type="password" name="serPass" class="bk" value="">
					</TD>								
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right">描述</TD>
					<TD colspan="3">
						<textarea name="remark" cols="80" class="bk" rows="2"></textarea>
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF" id="uploadTr">
					<TD class=column align="right"><font color="red">*</font>&nbsp;选择文件</TD>
					<TD colspan="3">
						<iframe id="uploadFrm" name="uploadFrm" FRAMEBORDER=0 src="FileUpload.jsp?type=office"  height="25" width=100%></iframe>
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD  class=column colspan="2" align="right">
						<INPUT TYPE="button" value=" 保 存  " onclick="saveFile()" id="save"  class=btn/>&nbsp;&nbsp;
						<INPUT TYPE="button" value=" 关 闭  " onclick="closeAdd()"  class=btn/>
					</TD> 
				</TR>
			</TABLE>
			</TD>
		</TR>
		<TR><TD></TD></TR>
	</TABLE>
	</FORM>
	</TD>
</TR>
</TABLE>

<span ID="editSpan"></span>

<script>
queryVendor();
getVendorInfo();
getModelInfo();
</script>
