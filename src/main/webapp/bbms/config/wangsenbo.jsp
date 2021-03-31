<%--
FileName	:
Desc		: 
Date		: 
--%>
<%@ page contentType="text/html;charset=utf-8"%>

<jsp:include page="../../share/selectDeviceJs.jsp">
	<jsp:param name="selectType" value="checkbox"/>
	<jsp:param name="jsFunctionName" value="selFileByDev"/>
</jsp:include>

<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
<SCRIPT LANGUAGE="JavaScript">
<%
%>
	var isOnlyOne = false;

	function showChild(param){
		var page ="";
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
	var oselect = document.all("device_id");
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
	if(num ==0){
		alert("请选择设备！");
		return false;
	}
	var obj = document.frm;	
//-------------------------------------------------------------------
	if(obj.file_path.value == -1){
		alert("请选择文件路径！");
		obj.file_path.focus();
		return false;
	}
//	if(!IsNull(obj.file_size.value,'文件大小')){
//		obj.file_size.focus();
//		obj.file_size.select();
//		return false;
//	}	
	if(!IsNull(obj.filename.value,'文件名称')){
		obj.filename.focus();
		obj.filename.select();
		return false;
	}
	if(!IsNull(obj.delay_time.value,'配置时延')){
		obj.delay_time.focus();
		obj.delay_time.select();
		return false;
	}else{
		if(!IsNumber(obj.delay_time.value,"配置时延")){
			obj.delay_time.focus();
			obj.delay_time.select();
			return false;
		}
	}			
}

//选择某一设备,过滤对应的恢复文件
function filterByDevID(devicetype_id) {
	var obj = document.frm;
	var dev_id = "";

	if(typeof(obj.device_id.length)=="undefined"){
	
		dev_id = obj.device_id.value;
		
	} else {
		for (var i = 0; i < obj.device_id.length; i++) {
			if (obj.device_id[i].checked) {
				dev_id = obj.device_id[i].value;		
			}		
		}
	}
	page = "showFilePath.jsp?device_id=" + dev_id + "&needFilter=true&isFromLocal=false&refresh="+Math.random();
	document.all("childFrm").src = page;
}

//filterByDevIDAndDevTypeID
function filterByDevIDAndDevTypeID(device_id, devicetype_id) {
	var obj = document.frm;
	var dev_id = "";

	if(typeof(obj.device_id.length)=="undefined"){
	
		dev_id = obj.device_id.value;
		
	} else {
		for (var i = 0; i < obj.device_id.length; i++) {
			if (obj.device_id[i].checked) {
				dev_id = obj.device_id[i].value;		
			}		
		}
	}
	page = "showFilePath.jsp?device_id="+dev_id + "&devicetype_id="+devicetype_id + "&needFilter=true&isFromLocal=false&refresh="+Math.random();
	document.all("childFrm").src = page;
}

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

//---------------------------------------
//根据设备ID显示相关配置文件列表
//---------------------------------------
function selFileByDev (devId) {
//	alert(devId);

	page = "showFilePath.jsp?device_id=" + devId + "&isFromLocal=false&refresh=" + Math.random();
	document.all("childFrm").src = page;
}

</SCRIPT>

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
									<td width="162" align="center" class="title_bigwhite" nowrap>
										设备配置恢复
									</td>
									<td nowrap>
										<img src="../images/attention_2.gif" width="15" height="12">
										对单个设备以配置文件方式进行恢复。
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<TR>
						<TH colspan="4" align="center">
							设备查询
						</TH>
					</TR>
					<TR bgcolor="#FFFFFF" id="tr1" STYLE="display:">
						<td colspan="4">
							<div id="selectDevice"><span>加载中....</span></div>
						</td>
					</TR>
					<TH colspan="4" align="center">
							设备配置
					</TH>

					<tr>
						<td>
							<TABLE width="100%" border=0 cellspacing=0 cellpadding=0
								align="center">
								<TR>
									<TD bgcolor=#999999>
										<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
											<TR bgcolor="#FFFFFF" STYLE="display:none">
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
												<TD align="right" width="20%" nowrap>
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
													</select>
												</TD>
											</TR> -->
											<TR bgcolor="#FFFFFF">
												<TD align="right" width="15%">
													文件路径
												</TD>
												<TD width="85%" colspan="3">
													<div id="filepath">
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
												<!-- <TD align="right" width="20%">
													文件大小
												</TD>
												<TD width="30%">
													<input type="text" name="file_size" maxlength=255 class=bk
														size=20 readOnly>
												</TD> -->
												<TD align="right" width="15%">
													文件名称
												</TD>
												<TD width="35%">
													<input type="text" name="filename" maxlength=255 class=bk
														size=20>
												</TD>
												<TD align="right" width="15%">
													配置时延
												</TD>
												<TD width="35%">
													<input type="text" name="delay_time" maxlength=255 class=bk
														size=20 value="0">&nbsp;(/秒)
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
											<TR>
												<TD colspan="4" align="right" CLASS="green_foot">
												<INPUT TYPE="reset" value=" 重 置 " class=btn>
												<INPUT TYPE="submit" value=" 配 置 " class=btn>
												<input type="hidden" name="file_size" class=bk>
												<input type="hidden" name="sucess_url" class=bk size=80>
												<input type="hidden" name="fail_url" class=bk size=80>
												<INPUT TYPE="hidden" name="action" value="add">
												<input type ="hidden" name="auto_type" value="2">
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
