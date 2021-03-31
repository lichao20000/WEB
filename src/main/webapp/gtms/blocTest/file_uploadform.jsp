<%@ include file="../../timelater.jsp"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="fileManage" scope="request" class="com.linkage.litms.filemanage.FileManage"/>
<%
    int fileType =1;
    Cursor cursor = (Cursor)fileManage.getCursor(fileType);
    Map fields = cursor.getNext();
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

<%@ include file="head.jsp"%>
<%@ include file="../../toolbar.jsp"%>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
	function showChild(param){
		if(param == "fileserver"){
			var serValue = document.frm.fileserver.value;
			if(serValue == -1){
				document.frm.serUser.value="";
				document.frm.serPass.value="";
			}else{
			//	var arrServer = serValue.split("|");
				//document.frm.serUser.value=arrServer[3];
			//	document.frm.serPass.value=arrServer[4];
			}		
		}
		
		
		if(param == 'vendor_name'){
			queryModel(param);
		}
		
		if(param == 'device_model_2'){
			queryVersion();
		}
		if(param == 'device_model'){
			queryHardware();
		}
	}

	function checkform(){
		var filetype = 0;
		for(var i=0;i< document.frm.filestatus.length;i++)
		{
			if(document.frm.filestatus[i].checked)
			{
				filetype=document.frm.filestatus[i].value;
				break;
			}
		}
		document.frm.file_status.value=filetype;
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
	
	
	function ChkAllDevModel(){
		var devModel = document.getElementsByName("deviceModel");
		if(devModel){
			for (var i=0;i<devModel.length;i++){
				devModel[i].checked = frm.all("deviceModelAll").checked;
			}
		}
	}
	
	function queryVendor(){
		var url = "<s:url value="/Resource/devVenderModelAction!getVendorList.action"/>";
		$.post(url,{
    	},function(mesg){
    		//alert(mesg);
    		document.getElementById("vendorList").innerHTML = mesg;
   		});
	}

	function queryModel(vendorname){
		var vend = document.all(vendorname).value;
		var url = "<s:url value="/Resource/devVenderModelAction!getDeviceModelList.action"/>";
		$.post(url,{
			vendorname:vend
    	},function(mesg){
    		document.getElementById('div_device_model').innerHTML = mesg;
    		document.getElementById('div_device_model_2').innerHTML = mesg.replace(/device_model/gi,'device_model_2');
  		});
	}

	
	function queryHardware(){
		var vend = document.all("vendor_name").value;
		var model = document.all("device_model").value;
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
							<img src="../../images/attention_2.gif" width="15" height="12">
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
					<TD width="35%">
						<INPUT TYPE="text" NAME="filename" class="bk" maxlength=30 size=30>&nbsp;&nbsp;<font color="red">名称不能包含中文、空格和特殊字符</font>
					</TD>
					<TD class=column align="right"  width="15%"><font color="red">*</font>&nbsp;设备厂商</TD>	
					<TD width="35%"><select name="vendor_name">
					<option value="ZTE">中兴</option>
					<option value="ALCL">ALCL</option>
					<option value="DARE">大亚</option>
					<option value="DLINK">DLINK</option>
					<option value="HUAWEI">华为</option>
					<option value="ALCATEL">贝尔</option>
					<option value="BELLMANN">贝曼</option>
					</select></TD>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" width="15%"><font color="red">*</font>&nbsp;软件版本</TD>	
					<TD width="35%"><select name="softwareversion">
					<option value="H618B V1.0.00T01">H618B V1.0.00T01</option>
					<option value="ZXV10 H608BV1.2.00T03_E">ZXV10 H608BV1.2.00T03_E</option>
					<option value="V1.2.00">V1.2.00</option>
					<option value="V1.0.00T02_E">V1.0.00T02_E</option>
					<option value="H618B V1.0.00T01">H618B V1.0.00T01</option>
					<option value="ZXV10 H618BV1.2.00T11_E">ZXV10 H618BV1.2.00T11_E</option>
					<option value="HG522V100R001C02B018SP03_JSCT">HG522V100R001C02B018SP03_JSCT</option>
					</select></TD>
					<TD class=column align="right" width="15%"><font color="red">*</font>&nbsp;硬件版本</TD>	
					<TD width="35%"><select name="hardwareversion">
					<option value="V1.0">V1.0</option>
					<option value="2.0.6">2.0.6</option>
					<option value="V1.1.00">V1.1.00</option>
					<option value="H618BV1.0">H618BV1.0</option>
					<option value="D422V2">D422V2</option>
					<option value="96358M">96358M</option>
					
					</select></TD>
				</TR>
				
				
				
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
						<input type="radio" value="1" name="inORout">设备访问URL上传&nbsp;&nbsp;
					</TD>
				</TR>
							
			
				
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right"><font color="red">*</font>&nbsp;选择文件</TD>
					<TD colspan="3">
						<iframe id="uploadFrm" name="uploadFrm" FRAMEBORDER=0 SCROLLING=NO src="FileUpload.jsp?type=office"  height="25" width=600>
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
<%@ include file="../../foot.jsp"%>
<script language="javascript">
	
</script>

