<%--
FileName	: softUpgrade.jsp
Date		: 2007年5月10日
Desc		: 软件升级.
--%>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<jsp:useBean id="versionManage" scope="request"
	class="com.linkage.litms.software.VersionManage" />
<%
	request.setCharacterEncoding("gbk");
	com.linkage.litms.resource.DeviceAct deviceAct = new com.linkage.litms.resource.DeviceAct();
	String gatherList = deviceAct.getGatherList(session, "", "", true);

	//设备型号下拉框
	//String deviceModel = versionManage.getDeviceTypeList("", true);

	/* String file_path = versionManage.getFilePath_3("file_path","","",true); */

	//设备厂商
	String strVendorList = deviceAct.getVendorList(true, "", "");
	
	//操作结果
	String opeResult = request.getParameter("operResult");
	
	//网关类型
	String gw_type = request.getParameter("gw_type");
	
	String file_path = versionManage.getFilePath_3("file_path","","",true,gw_type);
%>
<%@ include file="../head.jsp"%>
<link href="../css/css_green.css" rel="stylesheet" type="text/css">
<SCRIPT LANGUAGE="JavaScript">
<%if(null!=opeResult)
    {  
    	if("true".equals(opeResult))
    	{
    		out.println("alert('策略定制成功！');");
    	}
    	else if("false".equals(opeResult))
    	{
    		out.println("alert('策略定制失败！');");
    	}
    	else if("notAllow".equals(opeResult))
    	{
    		out.println("alert('一个设备不能定义两种配置升级策略，请重新选择设备！')");
    	}
    }
%>
	var isOnlyOne = false;
	
	function selectAll(elmID){
		//var needFilter=false;
		t_obj = document.all(elmID);
		if(!t_obj) return;
		obj = event.srcElement;
		if(obj.checked){
			if(typeof(t_obj) == "object" ) {
				if(typeof(t_obj.length) != "undefined") {
					for(var i=0; i<t_obj.length; i++){
						t_obj[i].checked = true;
					}
				} else {
					t_obj.checked = true;
					//needFilter = true;
				}
			}
		
		}else{
			if(typeof(t_obj) == "object" ) {
				if(typeof(t_obj.length) != "undefined") {
					for(var i=0; i<t_obj.length; i++){
						t_obj[i].checked = false;
					}
				} else {
					t_obj.checked = false;
				}
			}
		}
		
//		page = "showFilePath.jsp?device_id=" + "&device_model_id=" +document.all.softwareversion.value+"&needFilter="+needFilter;
//		document.all("childFrm2").src = page;
		
	}
	function showChild(param){
		var page ="";
		if(param == "gather_id"){
			document.all("div_vendor").innerHTML = "<%=strVendorList%>";
		}
		if(param == "softwareversion"){
			var gw_type = '<%= gw_type %>';
			document.frm.device.checked = false;
			page = "showDeviceList.jsp?gather_id="+document.frm.gather_id.value +"&vendor_id="+document.frm.vendor_id.value+ "&devicetype_id="+document.frm.softwareversion.value+"&gw_type="+gw_type;
			document.all("childFrm").src = page;
		}
		if(param == "vendor_id"){
			page = "showDevicetype.jsp?vendor_id="+document.frm.vendor_id.value;
			document.all("childFrm1").src = page;		
			
		}if(param == "device_model_id"){
		    page = "showDeviceVersion.jsp?vendor_id="+document.frm.vendor_id.value + "&device_model_id="+ encodeURIComponent(document.frm.device_model_id.value);
			document.all("childFrm2").src = page;	
		}
		if(param == "file_path"){
			if(document.frm.file_path.value != -1){
				var file = document.frm.file_path.value;
				var arrFile = file.split("\|");
				document.frm.file_size.value=arrFile[2];
				document.frm.filename.value=arrFile[1];
			}else{
				document.frm.file_size.value="";
				document.frm.filename.value="";			
			}
		}
	}
	
	
	function CheckForm(){
	/**
		var hguser=document.all("hguser").value;
	    var telephone= document.all("telephone").value;
	    var checkradios = document.all("checkType");
	    var checkType = "";
	    for(var i=0;i<checkradios.length;i++)
	    {
	      if(checkradios[i].checked)
		  {
		    checkType = checkradios[i].value;
		    break;
		  }
	    }
    */
    
	    var excuteRadios = document.all("excute_type");
	    var excute_type = "";
	    for(var i=0;i<excuteRadios.length;i++)
	    {
	       if(excuteRadios[i].checked)
	       {
	          excute_type = excuteRadios[i].value;
	          break;
	       }
	    }
    
	    if(excute_type==2&&document.all("task_name").value=="")
	    {
	       alert("请填写策略名称！");
	       document.frm.task_name.focus();
	       return false;
	    }  
    
    
		if(document.frm.gather_id.value == -1){
			alert("请选择采集点！");
			document.frm.gather_id.focus();
			return false;
	  	}
	  	if(document.frm.vendor_id.value == -1){
			alert("请选择厂商！");
			document.frm.vendor_id.focus();
			return false;
	 	}
	 	if(document.frm.device_model_id.value == -1){
			alert("请选择设备型号！");
			document.frm.device_model_id.focus();
			return false;
	 	}
	 	if(document.frm.softwareversion.value == -1){
			alert("请选择设备版本！");
			document.frm.softwareversion.focus();
			return false;
		}
		/**	
		if(""==hguser && ""==telephone)
		{
			alert("请填写用户名或电话号码！");
			document.all("hguser").focus();
			return false;
		}
		*/
		var oselect = document.all("device_id");
		//alert(oselect);
		if(oselect == null){
			alert("请选择设备！");
			return false;
		}
		var num = 0;
		if(typeof(oselect.length)=="undefined"){
			if(oselect.checked){
				num = 1;
			}
		}else{
			for(var i=0;i<oselect.length;i++){
				if(oselect[i].checked){
					num++;
				}
			}

		}
		if(num == 0){
			alert("请选择设备！");
			return false;
		}
		var obj = document.frm;	
//-------------------------------------------------------------------
		if(obj.filetype.value == -1){
			alert("请选择文件路径！");
			bj.filetype.focus();
			return false;
		}
		if(isEmpty(obj.file_size.value)){//文件大小
			alert("文件大小不能为空");
			obj.file_size.focus();
			obj.file_size.select();
			return false;
		}	
		if(isEmpty(obj.filename.value)){//文件名
			alert("文件名不能为空");
			obj.filename.focus();
			obj.filename.select();
			return false;
		}
		if(isEmpty(obj.delay_time.value)){
			alert("时延不能为空");
			obj.delay_time.focus();
			obj.delay_time.select();
			return false;
		}else{
			if(!IsNumber(obj.delay_time.value,"时延")){
				obj.delay_time.focus();
				obj.delay_time.select();
				return false;
			}
		}			
	}
/**	
//根据用户名或电话号码查询设备
function relateDevice()
{
   var hguser=document.all("hguser").value;
   var telephone= document.all("telephone").value;
   var checkradios = document.all("checkType");
   var checkType = "";
   for(var i=0;i<checkradios.length;i++)
   {
     if(checkradios[i].checked)
	 {
	   checkType = checkradios[i].value;
	   break;
	 }
   }
   //alert("hguser:"+hguser+"   telephone:"+telephone+"   checkType:"+checkType);
   if(1==checkType&&""==hguser&&""==telephone)
   {
      alert("请填写用户名或电话号码！");
      document.all("hguser").focus();
   }
   else if(1==checkType)
   {
      var page="";
      page="showDeviceList.jsp?hguser="+hguser+"&telephone="+telephone+"&needFilter=true&refresh="+Math.random();
      document.all("childFrm1").src = page;
   }
   
}
*/

/**
function ShowDialog(param)
{
  //根据用户来查询
  if(param==1)
  {
     tr1.style.display="none";
     tr2.style.display="none";
     tr3.style.display="";
  }
  
  //根据设备版本来查询
  if(param==0)
  {
     tr1.style.display="";
     tr2.style.display="";
     tr3.style.display="none";
  }
}
*/
function displayType(param)
{
   //如果选择立即执行或计划执行升级
   if(1==param||0==param)
   {
      tr0.style.display="none";
   }
   
   //选择自动执行升级
   if(2==param)
   {
      tr0.style.display="";
   }
}


function isEmpty(obj){
    if(typeof obj == "undefined" || obj == null || obj == ""){
        return true;
    }else{
        return false;
    }
}

</SCRIPT>
<%@ include file="../toolbar.jsp"%>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>
			&nbsp;
		</TD>
	</TR>
	<TR>
		<TD>
			<FORM NAME="frm" METHOD="post" ACTION="recoverSend.jsp"
				onsubmit="return CheckForm()">
				<table width="98%" border="0" align="center" cellpadding="0"
					cellspacing="0" class="text">
					<tr>
						<td>
							<table width="100%" height="30" border="0" cellspacing="0"
								cellpadding="0" class="green_gargtd">
								<tr>
									<td width="162" align="center" class="title_bigwhite">
										参数实例管理
									</td>
									<td>
										<img src="../images/attention_2.gif" width="15" height="12">
										对选择的设备进行配置恢复。
										
									</td>
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
										<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
											<TR>
												<TH colspan="4" align="center">
													批量下发配置
												</TH>
											</TR>
											<TR bgcolor="#FFFFFF">
												<!-- <TD align="right" width="20%">
													业务类型:
												</TD>
												<TD width="30%">
													<select name="service_id" class="bk">
														<option value="1">
															配置恢复
														</option>
													</select>
												</TD> -->
												<TD align="right" width="20%">
													执行方式:
												</TD>
												<TD colspan="3">
													<input type="radio" name="excute_type" value="0" onclick="displayType(this.value)" checked>
													立即执行
													<input type="radio" name="excute_type" value="1" onclick="displayType(this.value)">
													计划执行
													<input type="radio" name="excute_type" value="2" onclick="displayType(this.value)">
													定制策略
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF" id="tr0" STYLE="display:none">
											    <TD align="right" width="15%">
											    策略名称：
											    </TD>
											    <TD width="35%">
											    <input type=text name="task_name" value="" class="bk">
											    </TD>
											    <TD align="right" width="15%">
													策略执行时间:
												</TD>
												<TD width="35%">
													<select name="auto_excutetime_type" class="bk">
														<option value="1">
															设备初始安装第一次启动时自动升级
														</option>
														<option value="2">
															设备Periodic Inform自动升级
														</option>
														<option value="3">
															设备重新启动时自动升级
														</option>
														<option value="4">
															设备下次连接到ITMS时自动升级
														</option>
													</select>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF" id="tr1" STYLE="display:">
												<TD align="right" width="20%">
													采集点:
												</TD>
												<TD width="30%">
													<%=gatherList%>
												</TD>
												<TD align="right" width="20%">
													厂商:
												</TD>
												<TD width="30%">
													<div id="div_vendor">
														<select name="vendor" class="bk">
															<option value="-1">
																--请先选择采集点--
															</option>
														</select>
													</div>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF" id="tr2" STYLE="display:">
												
												<TD align="right" width="20%">
													设备型号:
												</TD>
												<TD width="30%">
													<div id="div_devicetype">
														<select name="device_model_id" class="bk">
															<option value="-1">
																--请先选择厂商--
															</option>
														</select>
													</div>
												</TD>
												<TD align="right" width="20%">
													设备版本:
												</TD>
												<TD width="30%">
													<div id="div_deviceversion">
														<select name="device_version" class="bk">
															<option value="-1">
																--请选择设备--
															</option>
														</select>													
													</div>
												</TD>
											</TR>
											<!--  
											<TR bgcolor="#FFFFFF" id="tr3" STYLE="display:none">
											    <TD align="right" width="20%">
													用户名:
												</TD>
												<TD width="30%">
													<input type="text" name="hguser" value="" class="bk">
												</TD>
												<TD align="right" width="20%">
													用户电话号码:
												</TD>
												<TD width="30%">
													<input type="text" name="telephone" value="" class="bk">
													<input type="button" class=btn value="查询" onclick="relateDevice()">
												</TD>
											</TR>
											-->
											<TR bgcolor="#FFFFFF">
												<TD align="right">
													设备列表:
													<br>
													<INPUT TYPE="checkbox" onclick="selectAll('device_id')"
														name="device">
													全选
												</TD>
												<TD colspan="3">
													<div id="idLayer"
														style="overflow:scroll;width:95%;height:'150px'">
														<span id="div_device">请先选择设备版本！</span>
													</div>
												</TD>
											</TR>
											<!-- <TR bgcolor="#FFFFFF">
												<TD align="right" width="20%">
													关键字:
												</TD>
												<TD width="30%">
													<input type="text" name="keyword" maxlength=255 class=bk
														size=20>
												</TD>
												<TD align="right" width="20%">
													文件类型:
												</TD>
												<TD width="30%">
													<select name="filetype" class="bk" readOnly>
														<option value="3 Vendor Configuration File">
															3 Vendor Configuration File
														</option>
												</TD>
											</TR> -->
											<TR bgcolor="#FFFFFF">
												<TD align="right" width="20%">
													文件路径:
												</TD>
												<TD width="" colspan="3">
													<div id="filepath">
														<%=file_path%>
													</div>
												</TD>
											</TR>
											<!-- 
											<TR bgcolor="#FFFFFF">
												<TD align="right" width="20%">
													用户名:
												</TD>
												<TD width="30%">
													<input type="text" name="username" maxlength=255 class=bk
														size=20>
												</TD>
												<TD align="right" width="20%">
													密码:
												</TD>
												<TD width="30%">
													<input type="text" name="passwd" maxlength=255 class=bk
														size=20>
												</TD>
											</TR>											
											 -->
											 <input type="hidden" name="username" maxlength=255 class=bk size=20>
											 <input type="hidden" name="passwd" maxlength=255 class=bk size=20>
											<TR bgcolor="#FFFFFF">
												<TD align="right" width="20%">
													文件大小:
												</TD>
												<TD width="30%">
													<input type="text" name="file_size" maxlength=255 class=bk
														size=20 readOnly>
												</TD>
												<TD align="right" width="20%">
													文件名:
												</TD>
												<TD width="30%">
													<input type="text" name="filename" maxlength=255 class=bk
														size=20>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD align="right" width="20%">
													时延:
												</TD>
												<TD colspan="3">
													<input type="text" name="delay_time" maxlength=255 class=bk
														size=20 value="0">
													&nbsp;&nbsp;(单位:s)
												</TD>

											</TR>
											<!-- 
											<TR bgcolor="#FFFFFF">
												<TD align="right" width="20%">
													成功URL:
												</TD>
												<TD colspan="3">
													<input type="text" name="sucess_url" class=bk size=80>
												</TD>

											</TR>
											<TR bgcolor="#FFFFFF">
												<TD align="right" width="20%">
													失败URL:
												</TD>
												<TD colspan="3">
													<input type="text" name="fail_url" class=bk size=80>
												</TD>

											</TR>
											 -->
											 <input type="hidden" name="sucess_url" class=bk size=80>
											 <input type="hidden" name="fail_url" class=bk size=80>
											<TR>
												<TD colspan="4" align="right" CLASS="green_foot">

													<INPUT TYPE="reset" value=" 重 置 " class=btn>
													&nbsp;
													<INPUT TYPE="hidden" name="action" value="add">
													<input type ="hidden" name="auto_type" value="2">
													&nbsp;
													<INPUT TYPE="submit" value=" 发 送 " class=btn>
													<input type='hidden' name='service_id' value="1">		
													<input type='hidden' name='keyword' value="download_config">
													<input type='hidden' name='filetype' value="3 Vendor Configuration File">
												</TD>
											</TR>
										</TABLE>
									</TD>
								</TR>
							</TABLE>
						</td>
					</tr>
				</table>
			</FORM>
		</TD>
	</TR>
	<TR>
		<TD HEIGHT=20>
			&nbsp;
			<IFRAME ID=childFrm SRC="" STYLE="display:none"></IFRAME>
			<IFRAME ID=childFrm1 SRC="" STYLE="display:none"></IFRAME>
			<IFRAME ID=childFrm2 SRC="" STYLE="display:none"></IFRAME>
			<IFRAME ID=childFrm3 SRC="" STYLE="display:none"></IFRAME>
		</TD>
	</TR>
</TABLE>
<%@ include file="../foot.jsp"%>
<%
deviceAct = null;
%>
