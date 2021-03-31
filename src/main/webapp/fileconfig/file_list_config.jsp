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
	
	//����
	String cityList = deviceAct.getCityListAll(false,"","city_id",request);
	
	//ҵ��
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
		alert("��ѡ����");
		return;
	}  
 
	var status = 1;
	
	document.all("operResult").innerHTML="����ͳ��,���Ժ�......";
	var page="file_listdata.jsp?device_model_id="+ document.frm1.device_model_id.value
			+ "&filestatus=" + status 
			+ "&filename="+document.frm1.filename.value
			+ "&vendor_id="+document.frm1._vendor_id.value
			+ "&city_id=" + document.frm1.city_id.value + "&service_id=" + document.frm1.service_id.value;		
	document.all("childFrm").src = page;
}

function delWarn(){
	if(confirm("���Ҫɾ�����ļ���\n��������ɾ���Ĳ��ָܻ�������")){
		return true;
	}
	else{
		return false;
	}
}

function addFile() {
	document.frm.reset();
	$("#filename").removeClass("disable");// �ļ����ɱ༭
	$("#vendorList").removeClass("disable");// ���̿�ѡ
	$("#uploadTr").show();// �ϴ��ļ���ʾ
	$("#save").hide();// ���水ť����
	$('#uploadFrm').attr('src', $('#uploadFrm').attr('src'));
	$("#addFile").show();// ��ʾ������
	$("#fileserver").show();
	$("#filePath").hide();
	$("#editTypeText").html("��Ӱ汾�ļ�");
	// �������Զ��ر� ������ FileUploadResult.jsp
}

function editFile(param) {
	/* 	var page = "fileEdit.jsp";
	page += "?softwarefile_id=" + param;
	window.open(page,"�汾�ļ�","width=800,height=520,resizable=no,scrollbars=yes"); */
	
	param = eval("("+param+")");
	document.frm.filename.value = param.softwarefile_name;// �ļ���
	document.frm.softwarefile_id.value = param.softwarefile_id;// �汾�ļ�id
	document.frm.remark.value = param.softwarefile_description;// ��ע
	document.frm.file_status.value = param.softwarefile_status;// ״̬
	
	window.device_model = window.devModelMap[param.device_model_id];
	
	getFileMap(param.softwarefile_id);
	getDevTypeByDevTypeId(param.devicetype_id, device_model);
	
	$("#filename").addClass("disable");// ���ƽ���
	$("#vendorList").addClass("disable");// ���̽���
	$("#uploadTr").hide();// �ϴ��ļ�����
	$("#save").show();// �����ļ���ʾ
	$("#editTypeText").html("�༭�汾�ļ�");
	$("#fileserver").hide();
	$("#filePath").show();
	
	$("#addFile").show();// ��ʾ�༭��
}

function closeAdd(){
	$("#addFile").hide();
}

// ��ȡ�汾�汾��Ϣ
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
				document.frm.filePath.value = server_name + "|" + server_path;// �洢·��
			}
		},
		error:function(e){
			alert("�������쳣");
			console.info("e",e);
		}
	});
}

// ���ݰ汾id��ȡ�汾��Ϣ
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
				document.frm.vendor_name.value = vendor_name;// ����
				showChild("vendor_name");
			}
		},
		error:function(e){
			alert("�������쳣");
			console.info("e",e);
		}
	});
}

// ��ȡ������Ϣ Map<vendor_id,vendor_name> ���� Map<vendor_name,vendor_id>
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
			alert("�������쳣");
			console.info("e",e);
		}
	});
}

// ��ȡ�ͺ���Ϣ Map<device_model_id, device_model> ���� Map<device_model,device_model_id>
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
			alert("�������쳣");
			console.info("e",e);
		}
	});
}

function saveFile()
{
	
	if(!checkSave()){
		return;
	}
	
	// ����id
	var vendor_id = window.devVendorMap[document.frm.vendor_name.value];
	// �ͺ�id
	var device_model_id = document.frm.device_model_id.value;
	// Ӳ���汾
	var hardwareversion = document.frm.hardwareversion.value;
	// ����汾
	var softwareversion = document.frm.softwareversion.value;
	
	// ���ݳ���id���ͺ�id����Ӳ���汾��ȡdevicetype_id
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
			alert("�������쳣");
			console.info("e",e);
		}
	});
	
	// ����
	$.ajax({
		type:"Post",
		url: "fileSave.jsp",
		data: $("#frm").serialize(),
		dataType:'text',
		success:function(data){
			if(data){
				if(data.indexOf("�ɹ�") != -1){
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
			alert("�������쳣");
			console.info("e",e);
		}
	});
}

function checkSave() {

	// �ͺ�
	var device_model_2 = document.frm.device_model_2.value;
	
	if(device_model_2 == -1){
		alert("��ѡ���ͺ�");
		return false;
	}
	document.frm.device_model_id.value = window.devModelMap[device_model_2];
	
	// Ŀ��汾������汾��
	var softwareversion = document.frm.softwareversion.value;
	if(softwareversion == -1){
		alert("��ѡ��Ŀ��汾");
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
				if(data.indexOf("�ɹ�") != -1){
					if(document.frm1._vendor_id.value != "-1"){
						queryfile();
					}
					
				}else{
					alert(data);
				}
			}
		},
		error:function(e){
			alert("�������쳣");
			console.info("e",e);
		}
	});
	
}
</SCRIPT>


<SCRIPT LANGUAGE="JavaScript">

function IsNull(strValue,strMsg){
	if(Trim(strValue).length>0) return true;
	else{
		alert(strMsg+'����Ϊ��');
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
			alert("��ѡ���豸�ͺţ�");
			document.frm.devicetype_id.focus();
			return false;
		}
		if(!IsNull(document.frm.filename.value,"�ļ���")){
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
    			document.frm.device_model_2.value = window.device_model;// �ͺ�
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
    			document.frm.softwareversion.value = softwareversion;// ����汾
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
							�汾�ļ�����
						</td>
						<td>
							&nbsp;<img src="../images/attention_2.gif" width="15" height="12">
								&nbsp;��ѯ�汾�ļ��������ϵİ汾�ļ�����<font color="red">*</font>�ı���ѡ�������.
						</td>
					</tr>
				</table>
			</td>
		</tr>		
		<TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
				<TR>
					<TH  colspan="4" align="center"><B>�汾�ļ���ѯ</B></TH>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" width="10%" nowrap>����</TD>
					<TD width="40%" colspan="3">
						<INPUT TYPE="text" name="filename" class="bk" style="width: 225px">&nbsp;
					</TD>
				</TR>

				<TR bgcolor="#FFFFFF">
					<TD class=column align="right">
						<font color="red">*</font>&nbsp;�豸����
					</TD>
					<TD>
						<%=str_VendorList%>
					<TD class=column align="right">
						�豸�ͺ�
					</TD>
					<TD>
						<div id="div_device_model_id">
							<select name="device_model_id" class="bk" style="width: 225px">
								<option value="-1">
									--����ѡ����--
								</option>
							</select>
						</div>
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right">����</TD>
					<TD>
						<%=cityList %>
					</TD>
					<TD class=column align="right">ҵ��</TD>
					<TD>
						<%=serviceList %>
					</TD>
				</TR>
				
				<TR class=green_foot>
					<TD colspan="4" align="right" >
						<INPUT TYPE="button" value=" �� ѯ " class=btn onclick="queryfile()">&nbsp;&nbsp;
						<INPUT TYPE="button" value=" ���� " class=btn onclick="addFile()">&nbsp;&nbsp;
						<INPUT TYPE="reset" value=" �� �� " class=btn>
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
					<TH  colspan="4" align="center"><B id="editTypeText">��Ӱ汾�ļ�</B></TH>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" width="15%" nowrap><font color="red">*</font>&nbsp;����</TD>
					<TD width="35%" colspan="3">
						<INPUT TYPE="text" NAME="filename" id="filename" class="bk" maxlength=30 size=30 style="width: 225px">&nbsp;&nbsp;<font color="red">���Ʋ��ܰ������ġ��ո�������ַ�</font>
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right"  width="15%"><font color="red">*</font>&nbsp;�豸����</TD>	
					<TD width="35%"><div id="vendorList"></div></TD>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" width="15%"><font color="red">*</font>&nbsp;�豸�ͺ�</TD>	
					<TD width="35%"><div id="div_device_model_2"></div></TD>
				</TR>
				<TR bgcolor="#FFFFFF" height="25">
					<TD class=column align="right" width="15%"><font color="red">*</font>&nbsp;Ŀ��汾</TD>	
					<TD width="35%" colspan="3"><div id="div_softwareversion"></div></TD>
					<INPUT TYPE="hidden" ID="hardwareversion" NAME="hardwareversion" value="">
				</TR>
				
				<TR bgcolor="#FFFFFF" >
					<TD class=column align="right" nowrap ><font color="red">*</font>&nbsp;�洢·��</TD>
					<TD colspan="3" nowrap>
						<select name="fileserver" class="bk" id="fileserver" onchange="showChild('fileserver')" style="width: 225px">
						<%
							if(fields == null){
						%>
								<option value="-1">==����û�м�¼==</option>
						<%
							}else{
						%>
								<option value="-1">==��ѡ��==</option>
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
						<input type="radio" value="0" name="inORout" checked>�û�����URL�ϴ�&nbsp;&nbsp;
						<input type="hidden" value="1" name="inORout">
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF" style="display:none">
					<TD class=column align="right" >�û���</TD>
					<TD >
						<input type="text" name="serUser" class="bk" value="">			
					</TD>
					<TD class=column align="right" >���� </TD>	
					<TD >
						<input type="password" name="serPass" class="bk" value="">
					</TD>								
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right">����</TD>
					<TD colspan="3">
						<textarea name="remark" cols="80" class="bk" rows="2"></textarea>
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF" id="uploadTr">
					<TD class=column align="right"><font color="red">*</font>&nbsp;ѡ���ļ�</TD>
					<TD colspan="3">
						<iframe id="uploadFrm" name="uploadFrm" FRAMEBORDER=0 src="FileUpload.jsp?type=office"  height="25" width=100%></iframe>
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD  class=column colspan="2" align="right">
						<INPUT TYPE="button" value=" �� ��  " onclick="saveFile()" id="save"  class=btn/>&nbsp;&nbsp;
						<INPUT TYPE="button" value=" �� ��  " onclick="closeAdd()"  class=btn/>
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
