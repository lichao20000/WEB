<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/prototype.js"/>"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/edittable.js"/>"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<!-- add by zhangchy 2011-09-08 新增CheckFormForm.js 原因是此JSP调用了IsIPAddr2(strValue,msg)脚本-->
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/CheckFormForm.js"/>"></SCRIPT>

<link rel="stylesheet" href="<s:url value="/css/css_green.css"/>"
	type="text/css">
<style type="text/css">
.bk {
	BACKGROUND-COLOR: #f1f1f1;
	BORDER-BOTTOM: #eeeeee 1px solid;
	BORDER-LEFT: #333333 1px solid;
	BORDER-RIGHT: #eeeeee 1px solid;
	BORDER-TOP: #333333 1px solid;
	COLOR: #888888;
	FONT-FAMILY: "宋体", "Arial"
}
</style>
<SCRIPT LANGUAGE="JavaScript">
<!--
	//是否判断


function checkSerialno(){
	var deviceSN = $.trim(document.frm.deviceSN.value);
	var loopbackIp = $.trim(document.frm.loopbackIp.value);
	if(deviceSN=="至少最后6位"){
		deviceSN="";
	}
	if(loopbackIp=="完整IP"){
		loopbackIp="";
	}
	if(deviceSN==""&&loopbackIp==""){
		alert("请至少输入设备序列号和设备IP中的一项！");
		document.frm.deviceSN.focus();
		return false;
	}
	if(deviceSN!=""){
		if(deviceSN.length < 6){
            alert("请输入至少最后6位设备序列号 !");
            document.frm.deviceSN.focus();
            return false;        
	    }
	}
	if(loopbackIp!=""){
		if(!IsIPAddr2(loopbackIp,"设备IP")){
			document.frm.loopbackIp.focus();
			return false;
		}
	}
		getDeviceInfo();
	
}

function getDeviceInfo(){
	
	var deviceSN = $.trim(document.frm.deviceSN.value);
	var loopbackIp = $.trim(document.frm.loopbackIp.value);
	var url = "<s:url value='/gwms/resource/updateDevCityByIP!getDeviceInfo.action'/>";
	if(deviceSN=="至少最后6位"){
		deviceSN="";
	}
	if(loopbackIp=="完整IP"){
		loopbackIp="";
	}
	$.post(url,{
		deviceSN:deviceSN,
		loopbackIp:loopbackIp
	},function(ajax){
		deviceClear();
	    $("div[@id='div_device']").html("");
		$("div[@id='div_device']").append(ajax);
	});
	document.all("tr_deviceinfo").style.display="";
}


function CheckForm(){

	var deviceId = $("input[@name='deviceId']").val();
	
	if ("" == deviceId ){
		alert('请先选择一个设备！');
		return false;
	}
	
	if (!confirm('请确认是否进行属地修正?')){
		return false;
	}
	
	document.frm.action="<s:url value='/gwms/resource/updateDevCityByIP!updateCity.action'/>";
	document.frm.submit();	
}




//add qixueqi
//针对选中设备时需要提交的数据
function deviceOnclick(device_id,city_id,IpCity_id,cpe_allocatedstatus,flag){
	$("input[@name='deviceId']").val(device_id);
	$("input[@name='deviceCityId']").val(city_id);
	$("input[@name='IpCityId']").val(IpCity_id);
	$("input[@name='isBind']").val(cpe_allocatedstatus);
	$("input[@name='flag']").val(flag);
	$("div[@id='div_update']").css("display","");
	if("1"==flag){		
		$("div[@id='div_update']").html("<input type='button' class=jianbian name='save_btn' value=' 属 地 修 正 ' onclick='CheckForm()'/>");		
	}else if("0"==flag){		
		$("div[@id='div_update']").html("<font color='red' size='3'>该设备不需要进行属地修正！</font>");				
	}else if("3"==flag){
		$("div[@id='div_update']").html("<font color='red' size='3'>该设备IP没有对应的属地！</font>");
	}else{
		$("div[@id='div_update']").html("<font color='red' size='3'>该设备未上报或上报失败！</font>");
	}
}

//add qixueqi
//重新查询设备时清空已选择的设备
function deviceClear(){
	$("input[@name='deviceId']").val("");
	$("input[@name='deviceCityId']").val("");
	$("input[@name='IpCityId']").val("");
	$("input[@name='isBind']").val("");
	$("input[@name='flag']").val("");
	$("div[@id='div_update']").css("display","none");
}



//输入框得到去焦点
function inputFocus(s,message){
	var value = $.trim(s.value);
	s.style.color='#0000cc';
	if(value==''||value==message){
		
		s.value='';
	}
}

//输入框失去焦点
function inputBlur(s,message){
	var value = $.trim(s.value);
	if(value==''||value==message){
		s.style.color='#888888';
		s.value=message;
	}
}
//-->
</SCRIPT>


<FORM NAME="frm" METHOD="post" ACTION="">

	<!-- 隐藏需要提交项 -->
	<!-- 设备部分 -->
	<input type="hidden" name="deviceId" value="" />
	<input type="hidden" name="deviceCityId" value="" />
	<input type="hidden" name="IpCityId" value="" />
	<input type="hidden" name="isBind" value="" />
	<input type="hidden" name="flag" value="" />

	<TABLE border=0 cellspacing=0 cellpadding=0 width="98%" align="center">
		<TR>
			<TD HEIGHT=15>
			</TD>
		</TR>
		<TR>
			<td>
				<table width="100%" height="30" border="0" cellspacing="0"
					cellpadding="0" class="green_gargtd">
					<tr>
						<td width="162" align="center" class="title_bigwhite">
							全网终端查询
						</td>
						<td>
							<img src="<s:url value="/images/attention_2.gif"/>" width="15"
								height="12" />
							查询所有注册到ITMS系统的终端
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR bgcolor="#FFFFFF">
						<TH colspan="2">
							设备查询
						</TH>
					</TR>
					<TR bgcolor="#FFFFFF">

						<TD align="center">
							设备序列号&nbsp;
							<input type="text" size="35" class="bk" name=deviceSN
								value="至少最后6位" onfocus="inputFocus(this,'至少最后6位')"
								onblur="inputBlur(this,'至少最后6位')">
							<font color="red">*</font>
						</TD>
						<TD align="center">
							设备IP&nbsp;
							<input type="text" size="35" class="bk" name=loopbackIp
								value="完整IP" onfocus="inputFocus(this,'完整IP')"
								onblur="inputBlur(this,'完整IP')">
							<font color="red">*</font>
						</TD>
					</TR>
					<tr bgcolor="#FFFFFF">
						<td align="right" class="column" colspan="2">
							<input type="button" class=jianbian name="sort" value=" 查  询 "
								onclick="checkSerialno()" />
						</td>
					</tr>
				</TABLE>
			</TD>
		</TR>
		<TR style="display: none" id="tr_deviceinfo">
			<td bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<tr>
						<th>
							查询结果
						</th>
					</tr>
					<TR bgcolor="#FFFFFF">
						<TD>
							<div id="div_device" style="width: 100%; z-index: 1; top: 100px">
							</div>
						</TD>
					</tr>
				</TABLE>
			</TD>
		</TR>
		<TR>
			<TD HEIGHT=30>
			</TD>
		</TR>


		<TR>
			<TD align="center">
				<div id="div_update" style="display: none"
					style="width: 100%; z-index: 1; top: 100px">
				</div>
			</TD>
		</TR>
	</TABLE>
</FORM>

<br>
<br>
<%@ include file="../foot.jsp"%>
