<%@page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<%@page import="com.linkage.litms.LipossGlobals"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK" />

<title>用户业务查询</title>
<script type="text/javascript"
	src="<s:url value="/Js/CheckFormForm.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<lk:res />
<script type="text/javascript">
	$(function() {
		parent.dyniframesize();
	});
	function change(obj){
var value = obj.value;
	if(value==1){
		$("input[@name='connAccount']").removeAttr("disabled");
		$("input[@name='connAccount']").val("");
		$("input[@name='connPwd']").removeAttr("disabled");
		$("input[@name='connPwd']").val("");
		$("input[@name='ipAddr']").val("请先选择用户类型!");
		$("input[@name='ipAddr']").attr("disabled","disabled");	
		$("input[@name='hideNode']").val("请先选择用户类型!");
		$("input[@name='hideNode']").attr("disabled","disabled");	
		$("input[@name='netCheck']").val("请先选择用户类型!");
		$("input[@name='netCheck']").attr("disabled","disabled");	
		$("input[@name='dnsMsg']").val("请先选择用户类型!");
		$("input[@name='dnsMsg']").attr("disabled","disabled");	
	}else if(value==3){
		$("input[@name='ipAddr']").removeAttr("disabled");
		$("input[@name='ipAddr']").val("");
		$("input[@name='hideNode']").removeAttr("disabled");
		$("input[@name='hideNode']").val("");
		$("input[@name='netCheck']").removeAttr("disabled");
		$("input[@name='netCheck']").val("");
		$("input[@name='dnsMsg']").removeAttr("disabled");
		$("input[@name='dnsMsg']").val("");
		$("input[@name='connAccount']").val("请先选择上网方式!");
		$("input[@name='connAccount']").attr("disabled","disabled");	
		$("input[@name='connPwd']").val("请先选择上网方式!");
		$("input[@name='connPwd']").attr("disabled","disabled");	
	}else{
		$("input[@name='connAccount']").val("请先选择上网方式!");
		$("input[@name='connAccount']").attr("disabled","disabled");
		$("input[@name='connPwd']").val("请先选择上网方式!");
		$("input[@name='connPwd']").attr("disabled","disabled");	
		$("input[@name='ipAddr']").val("请先选择上网方式!");
		$("input[@name='ipAddr']").attr("disabled","disabled");	
		$("input[@name='hideNode']").val("请先选择上网方式!");
		$("input[@name='hideNode']").attr("disabled","disabled");	
		$("input[@name='netCheck']").val("请先选择上网方式!");
		$("input[@name='netCheck']").attr("disabled","disabled");	
		$("input[@name='dnsMsg']").val("请先选择上网方式!");
		$("input[@name='dnsMsg']").attr("disabled","disabled");	
	}
}
	
	function checkMAC(mac){
	  	var macs = new Array();
	  	macs = mac.split(":");
	  	if(macs.length != 6){
				alert("输入的mac地址格式不正确，请以xx:xx:xx:xx:xx:xx的形式输入（xx为16进制数字）!");
				return false;
			}
	   for (var s=0; s<6; s++) {
	   	var num = macs[s];
	   	if(num.length>2){
	   		alert("输入的mac地址格式不正确，请以xx:xx:xx:xx:xx:xx的形式输入（xx为16进制数字）!");
	   		 return false;  
	   	}
	   	var temp = parseInt(macs[s],16);
	   	if(isNaN(temp)){
	   		alert("输入的mac地址格式不正确，请以xx:xx:xx:xx:xx:xx的形式输入（xx为16进制数字）!"); 
	   	  return false; 
	   	}
	   	if(temp < 0 || temp > 255){
	   		alert("输入的mac地址格式不正确，请以xx:xx:xx:xx:xx:xx的形式输入（xx为16进制数字）!"); 
	   		return false;
	   	}
	  }
	  return true;
	}
	
	function CheckForm(){
		var _dealdate = $.trim($("input[@name='dealdate']").val());
		var _userType = $.trim($("select[@name='userType']").val());
		var _loidMsg = $.trim($("input[@name='loidMsg']").val());
		var _netUsername = $.trim($("input[@name='netUsername']").val());
		var _cityId = $.trim($("select[@name='cityId']").val());
		var _bussAccount = $.trim($("input[@name='bussAccount']").val());
		var _bussPwd = $.trim($("input[@name='bussPwd']").val());
		var _wlantype = $.trim($("select[@name='wlantype']").val());
		var _connAccount = $.trim($("input[@name='connAccount']").val());
		var _connPwd = $.trim($("input[@name='connPwd']").val());
		var _ipAddr = $.trim($("input[@name='ipAddr']").val());
		var _hideNode = $.trim($("input[@name='hideNode']").val());
		var _netCheck = $.trim($("input[@name='netCheck']").val());
		var _dnsMsg =  $.trim($("input[@name='dnsMsg']").val());
		var _stbMacMsg = $.trim($("input[@name='stbMacMsg']").val());

		//业务受理时间
		if (!IsNull(_dealdate, "业务受理时间")) {
			$("input[@name='dealdate']").focus();
			return false;
		}
		//客户类型
		if('' == _userType || '-1' == _userType){
		alert("请选择类型");
		$("select[@name='userType']").focus();
		return false;
	}
		
		
		
		//属地
		if('' == _cityId || '-1' == _cityId){
		alert("请选择属地");
		$("select[@name='cityId']").focus();
		return false;
	}
		//业务账号
		if (!IsNull(_bussAccount, "业务账号")) {
			$("input[@name='bussAccount']").focus();
			return false;
		}
		//业务密码
		if (!IsNull(_bussPwd, "业务密码")) {
			$("input[@name='bussPwd']").focus();
			return false;
		}
		//上网方式
		
	
	if(_wlantype == 1){
		//接入账号
		if (!IsNull(_connAccount, "接入账号")) {
			$("input[@name='connAccount']").focus();
			return false;
		}
		//接入密码
		if (!IsNull(_connPwd, "接入密码")) {
			$("input[@name='connPwd']").focus();
			return false;
		}
	}
	if(_wlantype == 3){
		var myReg = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;
		//Ip地址
		if(_ipAddr == ""){
			alert("请输入Ip地址");
			$("input[@name='ipAddr']").focus();
			return false;
		}else{
			if(!myReg.test(_ipAddr)){
				alert('请输入有效的IP地址!');
				$("input[@name='ipAddr']").focus();
				return false;
			}
		}
		//掩码
		if(_hideNode == ""){
			alert("请输入掩码");
			$("input[@name='hideNode']").focus();
			return false;
		}else{
			if(!myReg.test(_hideNode)){
				alert('请输入有效的掩码!');
				$("input[@name='hideNode']").focus();
				return false;
			}
		}
		//网关
		if(_netCheck == ""){
			alert("请输入网关");
			$("input[@name='netCheck']").focus();
			return false;
		}else{
			if(!myReg.test(_netCheck)){
				alert('请输入有效的网关!');
				$("input[@name='netCheck']").focus();
				return false;
			}
		}
		//DNS
		if(_dnsMsg == ""){
			alert("请输入DNS");
			$("input[@name='dnsMsg']").focus();
			return false;
		}else{
			if(!myReg.test(_dnsMsg)){
				alert('请输入有效的DNS!');
				$("input[@name='dnsMsg']").focus();
				return false;
			}
		}	
	}
	
		
		
		//机顶盒MAC
		if (!IsNull(_stbMacMsg, "机顶盒MAC")) {
			$("input[@name='stbMacMsg']").focus();
			return false;
		}
	if(!checkMAC(_stbMacMsg)){
		$("input[@name='stbMacMsg']").focus();
			return false;
	}
	// 将小写转成大写
	
	document.queryStbForm.submit();
	}
</script>
</head>
<body>
	<form id="queryStbForm" name="queryStbForm" method="POST"
		action="<s:url value='/gtms/stb/resource/stbSimulateSheet!sendSheet.action'/>">
		<table width="100%" border="0" align="center" cellpadding="0"
			cellspacing="0">
			
			<TR>
				<TD>
					<table id="tblQuery" width="100%" class="querytable">
						<TR>
							<TD bgcolor=#999999>
						<thead>
							<tr>
								<input type="hidden" name="servTypeId"
							value='<s:property value="servTypeId" />'> <input
							type="hidden" name="operateType"
							value='<s:property value="operateType" />'>
								<td colspan="4" class="title_1">新装工单</td>
							</tr>
						</thead>
						<TR>
							<TD class="title_2">业务受理时间</TD>
							<TD width="30%"><input type="text" name="dealdate"
								value='<s:property value="dealdate" />' readonly class=bk>
								<img name="shortDateimg"
								onClick="WdatePicker({el:document.queryStbForm.dealdate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../../images/dateButton.png" width="13" height="12"
								border="0" alt="选择">&nbsp; <font color="#FF0000">*</font></TD>
							<TD class="title_2">客户类型</TD>
							<TD width="30%"><select name="userType" class="bk">
									<option selected value="3">==机顶盒==</option>
							</select>&nbsp; <font color="#FF0000">*</font></TD>
						</tr>

						<tr>
							<TD class="title_2">LOID</TD>
							<TD><INPUT TYPE='text' name='loidMsg' class=bk value=""></TD>
							<TD class="title_2" align="right" nowrap>用户宽带账号</TD>
							<TD><INPUT TYPE="text" name="netUsername" maxlength=20
								class=bk value=""></TD>
						</tr>
						<tr>
							<TD class=column align="right" width="20%">属地</TD>
							<TD width="30%"><s:select list="cityList" name="cityId"
									headerKey="-1" headerValue="请选择属地" listKey="city_id"
									listValue="city_name" value="cityId" cssClass="bk"></s:select>
								&nbsp; <font color="#FF0000">*</font></TD>
							<TD class="title_2" align="right" nowrap>上网方式</TD>
							<TD><select name="wlantype" class=bk onchange="change(this)">
								<%String InstArea=LipossGlobals.getLipossProperty("InstArea.ShortName");			
									if("nx_dx".equals(InstArea)){%>
									<option value="1">==桥接==</option>
								<%} else{%>
									<option value="1">==桥接==</option>
									<option value="2">==路由==</option>
									<option value="3">==静态IP==</option>
									<option value="4">==DHCP==</option>
								<%}%>
							</select> &nbsp; <font color="#FF0000">*</font>
						</tr>
						<tr>
							<TD class="title_2" align="right">业务密码</TD>
							<TD><INPUT TYPE="password" NAME="bussPwd" class=bk
								maxlength=20 value="">&nbsp; <font color="#FF0000">*</font></TD>
							<TD class="title_2" align="right" nowrap>业务账号</TD>
							<TD><INPUT TYPE="text" NAME="bussAccount" maxlength=40
								class=bk value="">&nbsp; <font color="#FF0000">*</font></TD>
						</tr>
						<tr>
							<TD class="title_2" align="right" nowrap>接入账号</TD>
							<TD><INPUT TYPE="text" NAME="connAccount" maxlength=45
								class=bk  value="">&nbsp; <font color="#FF0000">*</font></TD>
							<TD class="title_2" align="right">接入密码</TD>
							<TD><INPUT TYPE="password" NAME="connPwd" class=bk
								maxlength=25  value=""></TD>
						</tr>
						<tr>
							<TD class="title_2" align="right">Ip地址</TD>
							<TD><INPUT TYPE="text" NAME="ipAddr" class=bk
								maxlength=20 disabled="disabled"  value="请先选择上网方式!">&nbsp; <font color="#FF0000">*</font></TD>
							<TD class="title_2" align="right">掩码</TD>
							<TD><INPUT TYPE="text" NAME="hideNode" class=bk
								maxlength=20 disabled="disabled"  value="请先选择上网方式!">&nbsp; <font color="#FF0000">*</font></TD>
						</tr>
						<tr>
							<TD class="title_2" align="right">网关</TD>
							<TD><INPUT TYPE="text" NAME="netCheck" class=bk
								maxlength=20 disabled="disabled"  value="请先选择上网方式!">&nbsp; <font color="#FF0000">*</font></TD>
							<TD class="title_2" align="right">DNS</TD>
							<TD><INPUT TYPE="text" NAME="dnsMsg" class=bk
								maxlength=20 disabled="disabled"  value="请先选择上网方式!">&nbsp; <font color="#FF0000">*</font></TD>
						</tr>
						<tr>
							<TD class="title_2" align="right">机顶盒MAC</TD>
							<TD><INPUT TYPE="text" NAME="stbMacMsg" class=bk style="text-transform: uppercase;"
								maxlength=20 value="">&nbsp; <font color="#FF0000">*</font></TD>
								
							</tr>
						<TR>
							<TD colspan="4" height="35px" align="right" class="foot"><button
									onclick="CheckForm()">&nbsp;发送工单&nbsp;</button></TD>
						</TR>
						</TD>
						</TR>
					</table>
				</TD>
			</TR>
		</table>
	</form>
</body>
</html>
