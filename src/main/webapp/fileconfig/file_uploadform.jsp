<%@ include file="../timelater.jsp"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="ms" uri="/ailk-itms-web-tags" %>
<%@ page contentType="text/html;charset=GBK"%>
<%@page import="com.linkage.litms.LipossGlobals"%>
<jsp:useBean id="fileManage" scope="request" class="com.linkage.litms.filemanage.FileManage"/>
<%
    int fileType =1;
    Cursor cursor = (Cursor)fileManage.getCursor(fileType);
    Map fields = cursor.getNext();
    
    String instArea = LipossGlobals.getLipossProperty("InstArea.ShortName");
%>

<style type="text/css">
<!--
select {
	position:relative;
	font-size:12px;
	width:160px;
	line-height:18px;border:0px;
	color:#909993;
}
-->
</style>

<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/jquery.js"></SCRIPT>
<script type="text/javascript" src="<s:url value="../Js/CheckForm.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">

var area = '<%=instArea%>'

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
		
		/**
		if(param == "vendor_id"){
			page = "showDeviceModel.jsp?vendor_id="+document.frm.vendor_id.value;
			document.all("childFrm").src = page;		
		}
		if(param == "device_model_id"){
			page = "showSoftwareVersion.jsp?device_model_id="+document.frm.device_model_id.value;
			document.all("childFrm1").src = page;		
		}
		**/

		// 设备厂商改变时触发
		if(param == 'vendor_name'){
			queryModel(param);
		}
		
		if(param == 'device_model_2'){
			queryVersion();
			if(area == 'sx_lt'){
				queryHardware();
			}
		}
		
		if(area != 'sx_lt'){
			if(param == 'device_model'){
				queryHardware();
			}
		}
		
	}

	function checkform(){
		var filetype = 0;
		if(area != 'sx_lt')
		{
			for(var i=0;i< document.frm.filestatus.length;i++)
			{
				if(document.frm.filestatus[i].checked)
				{
					filetype=document.frm.filestatus[i].value;
					break;
				}
			}
			document.frm.file_status.value=filetype;
		}
		
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
		//if (e.name != 'filestatus' && e.name!="cityAll" && e.name!="service" && e.name!="serviceAll"  && e.name!="inORout")
		if (e.name == "city")
		   e.checked = frm.cityAll.checked;
		}
	}
	
	function ChkAllService()
	{
		for (var i=0;i<frm.elements.length;i++)
		{
		var e = frm.elements[i];
		//if (e.name != 'filestatus' && e.name!="serviceAll" && e.name!="city" && e.name!="cityAll" && e.name!="inORout")
		if (e.name == 'service')
		   e.checked = frm.serviceAll.checked;
		}
	}
	
	//function ChkAllHardversion(){
	//	var hardware = document.getElementsByName("hardwareversion");
	//	if(hardware){
	//		for (var i=0;i<hardware.length;i++){
	//			hardware[i].checked = frm.all("hardwareAll").checked;
	//		}
	//	}
	//}
	
	function ChkAllDevModel(){
		var devModel = document.getElementsByName("deviceModel");
		if(devModel){
			for (var i=0;i<devModel.length;i++){
				devModel[i].checked = frm.all("deviceModelAll").checked;
			}
		}
	}

	// 页面加载进来获取厂商
	function queryVendor(){
		var url = "<s:url value="/Resource/devVenderModelAction!getVendorList.action"/>";
		$.post(url,{
    	},function(mesg){
    		//alert(mesg);
    		document.getElementById("vendorList").innerHTML = mesg;
   		});
	}

	// 获取型号
	function queryModel(vendorname){
		var vend = document.all(vendorname).value;
		var url = "<s:url value="/Resource/devVenderModelAction!getDeviceModelList.action"/>";
		$.post(url,{
			vendorname:vend
    	},function(mesg){
    		if(area != 'sx_lt'){
	    		document.getElementById('div_device_model').innerHTML = mesg;
    		}
    		document.getElementById('div_device_model_2').innerHTML = mesg.replace(/device_model/gi,'device_model_2');
  		});
	}

	//function queryHardware(){
	//	var vend = document.all("vendor_name").value;
	//	var model = document.all("device_model").value;
	//	var url = "<s:url value="/Resource/devVenderModelAction!getHardwareversionList.action"/>";
	//	$.post(url,{
	//		vendorname:vend,
	//		deviceModel:model
    //	},function(mesg){
    //		//alert(mesg);
    //		document.getElementById("div_hardwareversion").innerHTML = mesg;
  	//	});
	//}
	
	function queryHardware(){
		var vend = document.all("vendor_name").value;
		var model = '';
		if(area == 'sx_lt'){
			model = document.all("device_model_2").value;
		}else{
			model = document.all("device_model").value;
		}
		var url = "<s:url value="/Resource/devVenderModelAction!getHardwareversionStr.action"/>";
		$.post(url,{
			vendorname:vend,
			deviceModel:model
    	},function(mesg){
    		document.getElementById("hardwareversion").value = mesg;
    		//alert(document.getElementById("hardwareversion").value);
  		});
	}
	
	function queryVersion(){
		var vend = document.all("vendor_name").value;
		var model = document.all("device_model_2").value;	
		var url = "<s:url value="/Resource/devVenderModelAction!getVersionList.action"/>";
		$.post(url,{
			vendorname:vend,
			deviceModel:model
    	},function(mesg){
    		//alert(mesg);
    		document.getElementById("div_softwareversion").innerHTML = mesg;
    	});
	}
</SCRIPT>
<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0">
<TR>
	<TD valign=top>
	<FORM NAME="frm" METHOD="post" ACTION="fileSave.jsp" onsubmit="return checkform()">
	<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD HEIGHT=20>&nbsp;</TD>
		</TR>
		<tr>
			<td>
				<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
					<tr>
						<td width="162" align="center" class="title_bigwhite">
							版本文件管理
						</td>
						<td>&nbsp;
							<img src="../images/attention_2.gif" width="15" height="12">
								&nbsp;上传版本文件到版本文件服务器。带<font color="red">*</font>的必须选择或输入.
						</td>
					</tr>
				</table>
			</td>
		</tr>		
		<TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
				<TR>
					<TH  colspan="4" align="center"><B>版本文件添加</B></TH>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" width="15%" nowrap><font color="red">*</font>&nbsp;名称</TD>
					<ms:inArea areaCode="sx_lt" notInMode="true">
						<TD width="35%">
							<INPUT TYPE="text" NAME="filename" class="bk" maxlength=30 size=30>&nbsp;&nbsp;<font color="red">名称不能包含中文、空格和特殊字符</font>
						</TD>
					</ms:inArea>
					<!-- 山西联通隐藏了状态之后，这个td要合并 -->
					<ms:inArea areaCode="sx_lt" notInMode="fasle">
						<TD width="35%" colspan="3">
							<INPUT TYPE="text" NAME="filename" class="bk" maxlength=30 size=30>&nbsp;&nbsp;<font color="red">名称不能包含中文、空格和特殊字符</font>
						</TD>
					</ms:inArea>
					<ms:inArea areaCode="sx_lt" notInMode="true">
						<TD class=column align="right" width="15%">状态</TD>
						<TD width="35%">
							<input type="radio" name="filestatus" value="1" checked>已审核
							<input type="radio" name="filestatus" value="2">未审核
							<input type="hidden" name="file_status" value="">
						</TD>
					</ms:inArea>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right"  width="15%"><font color="red">*</font>&nbsp;设备厂商</TD>	
					<TD width="35%"><div id="vendorList"></div></TD>
					<!-- 山西联通分行展示设备型号 -->
					<ms:inArea areaCode="sx_lt" notInMode="fasle">
						</TR>
						<TR bgcolor="#FFFFFF">
					</ms:inArea>
					<TD class=column align="right" width="15%"><font color="red">*</font>&nbsp;设备型号</TD>	
					<TD width="35%"><div id="div_device_model_2"></div></TD>
				</TR>
				<TR bgcolor="#FFFFFF" height="25">
					<TD class=column align="right" width="15%"><font color="red">*</font>&nbsp;目标版本</TD>	
					<ms:inArea areaCode="sx_lt" notInMode="true">
						<TD width="35%"><div id="div_softwareversion"></div></TD>
					</ms:inArea>

					<!-- 山西联通隐藏了状态之后，这个td要合并 -->
					<ms:inArea areaCode="sx_lt" notInMode="fasle">
						<TD width="35%" colspan="3"><div id="div_softwareversion"></div></TD>
					</ms:inArea>

					<ms:inArea areaCode="sx_lt" notInMode="true">
						<TD class=column align="right" width="15%"><font color="red">*</font>&nbsp;适用的设备型号</TD>	
						<TD width="35%">
						    <div id="div_device_model"></div>
						</TD>
					</ms:inArea>
					<INPUT TYPE="hidden" ID="hardwareversion" NAME="hardwareversion" value="">
				</TR>
				
				<!-- 应需求此处的"硬件版本"不需要了,将其注释 -->
				<!-- 
				<TR bgcolor="#FFFFFF" style="">
					<TD class=column align="right" width="15%" height="25">
						<div align="right"><font color="red">*</font>&nbsp;适用的硬件版本<br>
	                        <input type=checkbox name=hardwareAll onclick="javascript:ChkAllHardversion();">
	                    </div>
					</TD>
					<TD colspan="3"><div id="div_hardwareversion"></div></TD>
				</TR>
				 -->
				<TR bgcolor="#FFFFFF" >
					<TD class=column align="right" nowrap ><font color="red">*</font>&nbsp;存储路径</TD>
					<TD colspan="3" nowrap>
						<select name="fileserver" class="bk" onchange="showChild('fileserver')">
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
						
						</select>&nbsp;&nbsp; 
						<input type="radio" value="0" name="inORout" checked>用户访问URL上传&nbsp;&nbsp;
						<%
							if("sx_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){
						%>
						<input type="hidden" value="1" name="inORout">

						<%}else{ %>
						<input type="radio" value="1" name="inORout">设备访问URL上传&nbsp;&nbsp;
						<%
							}
						%>
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
					<ms:inArea areaCode="sx_lt" notInMode="true">
						<tr bgcolor="#FFFFFF">
							<td class=column>
								<div align="right">请选择属地<br>
									<input type=checkbox name=cityAll onclick="javascript:ChkAllCity();">
								</div>
							</td>
							<td>
								<%=com.linkage.litms.netcutover.CommonForm.getCityCheckBox_(request)%>
								<input type="hidden" name="cityList" value="">
							</td>
							<td class=column>
								<div align="right">请选择业务<br>
									<input type=checkbox name=serviceAll onclick="javascript:ChkAllService();">
								</div>
							</td>
							<td>
								<%=com.linkage.litms.filemanage.FileManage.getServiceCheckBox("")%>
								<input type="hidden" name="serviceList" value="">
							</td>
						</tr>
					</ms:inArea>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right">描述</TD>
					<TD colspan="3">
						<textarea name="remark" cols="80" class="bk" rows="2"></textarea>
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right"><font color="red">*</font>&nbsp;选择文件</TD>
					<TD colspan="3">
						<iframe id="uploadFrm" name="uploadFrm" FRAMEBORDER=0 src="FileUpload.jsp?type=office"  height="25" width=100%>
						<IFRAME ID="childFrm" SRC="" STYLE="display:none"></IFRAME>						
						</iframe>
						<IFRAME ID="childFrm1" SRC="" STYLE="display:none"></IFRAME>
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
<BR>
<%@ include file="../foot.jsp"%>
<script language="javascript">
	queryVendor();
</script>

