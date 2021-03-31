<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/prototype.js"/>"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/edittable.js"/>"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript"
	SRC="<s:url value="/Js/CheckFormForm.js"/>"></SCRIPT>
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

function deviceMemUpdate(){
	var url = "<s:url value="/gwms/sysConfig/resourceBindMemConfig!configMem.action"/>";
	var deviceId = $("input[@name='deviceId']").val().trim();
	if(""==deviceId){
		alert("请查询设备或者输入deviceId!");
		return;
	}
	$.post(url,{
		username:deviceId,
		type:"device",
		operate:"set"
    },function(mesg){
		alert(mesg);
    });
}

function getAllInfo(){
	var url = "<s:url value="/gwms/sysConfig/resourceBindMemConfig!getDeviceDetail.action"/>";
	$("div[@id='detail']").hide();
	var deviceId = $("input[@name='deviceId']").val().trim();
	if(""==deviceId){
		alert("请查询设备或者输入deviceId!");
		return;
	}
	$.post(url,{
		username:deviceId
    },function(mesg){
    	$("div[@id='detail']").show();
		$("div[@id='detail']").html(mesg);
		parent.dyniframesize();
    });
}

function checkSerialno(){
	var device_serialnumber = $.trim(document.frm.device_serialnumber.value);
	var loopbackIp = $.trim(document.frm.loopbackIp.value);
	if(device_serialnumber=="至少最后6位"){
		device_serialnumber="";
	}
	if(loopbackIp=="完整IP"){
		loopbackIp="";
	}
	if(device_serialnumber==""&&loopbackIp==""){
		alert("请至少输入设备序列号和设备IP中的一项！");
		document.frm.device_serialnumber.focus();
		return false;
	}
	if(device_serialnumber!=""){
		if(device_serialnumber.length < 6){
            alert("请输入至少最后6位设备序列号 !");
            document.frm.device_serialnumber.focus();
            return false;        
	    }
	}
	if(loopbackIp!=""){
		if(!IsIPAddr2(loopbackIp,"设备IP")){
			document.frm.loopbackIp.focus();
			return false;
		}
	}
	var url = "<s:url value='/gwms/sysConfig/itmsInstTest!getDeviceInfo.action'/>";
	$.post(url,{
		deviceNo:device_serialnumber,
		loopbackIp:loopbackIp
	},function(ajax){
		deviceClear();
	    $("div[@id='div_device']").html("");
		$("div[@id='div_device']").append(ajax);
		parent.dyniframesize();
	});
	document.all("tr_deviceinfo").style.display="";
}

//add qixueqi
//针对选中设备时需要提交的数据
function deviceOnclick(device_id,city_id,oui,device_serialnumber,IpCity_id,cpe_allocatedstatus,flag,manage,device_type){
	$("input[@name='deviceId']").val(device_id);
}

//add qixueqi
//重新查询设备时清空已选择的设备
function deviceClear(){
	$("input[@name='deviceId']").val("");
}

function DetailDevice(device_id){
	var strpage = "../../Resource/DeviceShow.jsp?device_id=" + device_id;
	window.open(strpage,"","left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
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

	<TABLE border=0 cellspacing=0 cellpadding=0 width="98%" align="center">
		<TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR bgcolor="#FFFFFF">
						<TH colspan="3">
							设备查询
						</TH>
					</TR>
					<TR bgcolor="#FFFFFF">

						<TD align="center" width="50%">
							设备序列号&nbsp;
							<input type="text" size="35" class="bk" name=device_serialnumber
								value="至少最后6位" onfocus="inputFocus(this,'至少最后6位')"
								onblur="inputBlur(this,'至少最后6位')">

						</TD>
						<TD align="center" width="50%">
							设备IP&nbsp;
							<input type="text" size="35" class="bk" name=loopbackIp
								value="完整IP" onfocus="inputFocus(this,'完整IP')"
								onblur="inputBlur(this,'完整IP')">

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
			<TD>
				<div id="div_device" style="width: 100%; z-index: 1; top: 100px">
				</div>
			</TD>
		</TR>
		<TR>
			<TD HEIGHT=30>
			</TD>
		</TR>
		<TR id="queryuser" style="display: ">
			<TD bgcolor=#999999>
				<table width="100%" border=0 cellspacing=1 cellpadding=2
					align="center">
					<TR bgcolor="#FFFFFF">
						<TH colspan="1">
							device_id:<input type="text" name="deviceId" value="" />
							<input type='button' class=jianbian name='save_btn' value='DB与MEM信息比对' onclick='getAllInfo()' />
							<input type='button' class=jianbian name='save_btn' value='MEM信息更新' onclick='deviceMemUpdate()' />
						</TH>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD align="center" width="100%" height="20">
							<div id="detail" style="display:none">
							</div>
						</TD>
					</TR>

				</table>
			</TD>
		</TR>
	</TABLE>
</FORM>
