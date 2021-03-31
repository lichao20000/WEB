<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/prototype.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/edittable.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.blockUI.js"></SCRIPT>
<link rel="stylesheet" href="../../css/listview.css" type="text/css">
<link rel="stylesheet" href="../../css/css_green.css" type="text/css">



<%
	String gw_type = request.getParameter("gw_type");
%>
<style type="text/css">

.bk {
	BACKGROUND-COLOR: #f1f1f1; BORDER-BOTTOM: #eeeeee 1px solid; BORDER-LEFT: #333333 1px solid; BORDER-RIGHT: #eeeeee 1px solid; BORDER-TOP: #333333 1px solid; COLOR: #888888; FONT-FAMILY: "宋体","Arial"
}
</style>
<SCRIPT LANGUAGE="JavaScript">
var gw_type=<%=gw_type%>;
function nextStep(){
	var device_serialnumber = $.trim(document.frm.device_serialnumber.value); 
	var userName = $.trim(document.frm.userName.value); 
	if(device_serialnumber=="至少最后6位"){
		device_serialnumber="";
	}
	if(userName=="完整账号" || userName=="LOID"){
		userName="";
	}
	if(device_serialnumber==""&&userName==""){
		alert("请至少输入设备序列号和用户账号中的一项！");
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
	var url = "<s:url value='/gwms/resource/itmsInst!getReleaseUserInfo.action'/>";
	$.post(url,{
		device_serialnumber:device_serialnumber,
		userName:userName,
		gw_type:gw_type
	},function(ajax){
		userClear();	
	    $("div[@id='div_user']").html("");
		$("div[@id='div_user']").append(ajax);
	});	
	document.all("tr_userinfo").style.display="";
}

//针对选中用户时需要提交的数据
function userOnclick(user_id,device_id,city_id){
	$("input[@name='userId']").val(user_id);
	$("input[@name='deviceId']").val(device_id);
	$("input[@name='userCityId']").val(city_id);
}


//重新查询用户时时清空已选择的用户
function userClear(){
	$("input[@name='userId']").val("");
	$("input[@name='deviceId']").val("");
	$("input[@name='userCityId']").val("");
}

//查看用户相关的信息
function GoContent(user_id){
	var strpage="../../Resource/HGWUserRelatedInfo.jsp?user_id=" + user_id;
	window.open(strpage,"","left=20,top=20,width=600,height=450,resizable=yes,scrollbars=yes");
}


//针对选中用户时需要提交的数据
function userOnclick(user_id,device_id,city_id,deviceNo,username){
	$("input[@name='userId']").val(user_id);
	$("input[@name='deviceId']").val(device_id);
	$("input[@name='userCityId']").val(city_id);
	$("input[@name='deviceNo']").val(deviceNo);
	$("input[@name='username']").val(username);
	$("div[@id='div_bind']").css("display","");
	$("div[@id='div_bind']").html("<input type='button' class=jianbian name='save_btn' value=' 开 始 解 绑 ' onclick='CheckForm()'/>");	
}


//重新查询用户时时清空已选择的用户
function userClear(){
	$("input[@name='userId']").val("");
	$("input[@name='deviceId']").val("");
	$("input[@name='userCityId']").val("");
}

function CheckForm(){

	var userId = $("input[@name='userId']").val();
	
	if ("" == userId ){
		alert('请先选择一个用户！');
		return false;
	}
	
	var message = "请确认！用户帐号："+$("input[@name='username']").val()+"，设备序列号："+$("input[@name='deviceNo']").val();
	if (!confirm(message+'！是否继续保存?')){
		return false;
	}
	
	document.frm.action="itmsInst!release.action";
	document.frm.submit();	
}

//输入框得到去焦点
function inputFocus(s,message){
	var value = $.trim(s.value);
	if(value==''||value==message){
		s.style.color='#0000cc';
		s.value='';
	}
}

//输入框失去焦点
function inputBlur(s,message){
	var value = $.trim(s.value);
	s.style.color='#888888';
	if(value==''||value==message){
		
		s.value=message;
	}
}

</SCRIPT>

<FORM name="frm" action="itmsInst!release.action" method="post">
	<!-- 提示信息 -->
	<input type="hidden" name="deviceNo" value="" />
	<input type="hidden" name="username" value="" />
	<!-- 隐藏需要提交项 -->
	<input type="hidden" value="" name="userId">
	<input type="hidden" value="" name="deviceId">
	<input type="hidden" value="" name="userCityId">
	<TABLE border=0 cellspacing=0 cellpadding=0 width="98%" align="center">
		<TR>
			<TD HEIGHT="15">
				&nbsp;
			</TD>
		</TR>

		<TR>
			<TD>
				<table width="100%" height="30" border="0" cellspacing="0"
					cellpadding="0" class="green_gargtd">
					<tr>
						<td width="162">
							<div align="center" class="title_bigwhite">
								手工解绑
							</div>
						</td>
						<td>
							<img src="<s:url value="/images/attention_2.gif"/>" width="15"
								height="12" />
							请输入至少最后6位设备序列号或者完整的用户账号进行查询！
						</td>
					</tr>
				</table>
			</TD>
		</TR>
		<TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR>
						<TH colspan="5">
							待操作设备信息
						</TH>
					</TR>
					<TR bgcolor="#ffffff">
					<ms:inArea areaCode="js_dx" notInMode="true">
						<TD align="right" width="12%" class="column" >
							设备序列号:
						</td>
						<td width="38%" >
							<input type="text" name="device_serialnumber" class="bk" size="35" value="至少最后6位"
								onfocus="inputFocus(this,'至少最后6位')"
								onblur="inputBlur(this,'至少最后6位')">
							
						</td>	
					</ms:inArea>
					<ms:inArea areaCode="js_dx" notInMode="false">
						<TD align="right" width="12%" class="column" style="display: none">
							设备序列号:
						</td>
						<td width="38%"  style="display: none">
							<input type="text" name="device_serialnumber" class="bk" size="35" value="至少最后6位"
								onfocus="inputFocus(this,'至少最后6位')"
								onblur="inputBlur(this,'至少最后6位')">
							
						</td>	
					</ms:inArea>
					<TD align="right" width="12%" class="column">
						<ms:inArea areaCode="sx_lt">
							唯一标识:
						</ms:inArea>
						<ms:inArea areaCode="sx_lt" notInMode="true">
							LOID:
						</ms:inArea>
						</td>
						<td  width="38%">
							<input type="text" name="userName" class="bk" size="35" value="LOID"
								onfocus="inputFocus(this,'LOID')"
								onblur="inputBlur(this,'LOID')">
							
						</td>
					</TR>
					<tr  bgcolor="#ffffff">
						<td colspan="4" align="right" class="column">

							<input type="button" class="jianbian" name="button" value=" 下一步 "
								style="display: " onclick="nextStep();">					
						</td>
					</tr>

				</TABLE>
			</TD>
		</TR>

		<TR style="display: none" id="tr_userinfo">
			<TD>
				<div id="div_user"
					style="width: 100%; z-index: 1; top: 100px">
				</div>
			</TD>
		</TR>
		<TR>
			<TD HEIGHT=30>
			</TD>
		</TR>
		<TR>
			<TD align="center">
				<div id="div_bind" style="display: none"
					style="width: 100%; z-index: 1; top: 100px">
				</div>
			</TD>
		</TR>
	</TABLE>
</FORM>
<br>
<br>
<%@ include file="../foot.jsp"%>
