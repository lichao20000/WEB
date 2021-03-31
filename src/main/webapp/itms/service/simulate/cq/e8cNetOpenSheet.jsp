<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckFormForm.js"></SCRIPT>
<script type="text/javascript"
	src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<title>BSS模拟工单</title>
<script type="text/javascript">
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
	$(function() {
		parent.dyniframesize();
	});
	
	
	//检查LOID,回填
	function checkLoid()
	{
		var loid = $("input[@name='loid']").val().trim();
		if("" == loid){
			return false;
		}
		
		var url = "<s:url value='/itms/service/simulateSheet4cq!checkLoid4CQ.action'/>";
		$.post(url,{
			"loid":loid
		},function(ajax){
			if("0" == ajax)
			{
				$("select[@name='cityId']").val("-1");
				$("select[@name='usertype']").val("0");
				$("select[@name='devicewan']").val('4');
			}
			else
			{
				var relt = ajax.split("|");
				var cityId = $("select[@name='cityId']");
				var usertype = $("select[@name='usertype']");
				var devicewan = $("select[@name='devicewan']");
				devicewan.val(relt[0]);
				cityId.val(relt[1]);
				usertype.val(relt[2]);
			}
		}); 

	}
	
	
	function CheckForm() {
		var loid = $("input[@name='loid']");
		var ordertime = $("input[@name='ordertime']");
		var cityId = $("select[@name='cityId']");
		var orderself = $("select[@name='orderself']");
		var devicewan = $("select[@name='devicewan']");
		var usertype = $("select[@name='usertype']");
		var subareacode = $("input[@name='subareacode']");
		var olduserid = $("input[@name='olduserid']");
		var deviceid = $("input[@name='deviceid']");
		
		var adaccount = $("input[@name='adaccount']");
		var username = $("input[@name='username']");
		var useraddress = $("input[@name='useraddress']");
		var contactperson = $("input[@name='contactperson']");
		var phonenumber = $("input[@name='phonenumber']");
		var migration = $("select[@name='migration']");
		
		/* 业务字段 */
		var rgmode = $("select[@name='rgmode']");
		var adpassword = $("input[@name='adpassword']");
		var checkBoxNet = document.frm.netPort;
		var rgportid = $("input[@name='rgportid']");
		var vlanid = $("input[@name='vlanid']");
		var AFTRAddress = $("input[@name='AFTRAddress']");
		var IPV6Enable = $("select[@name='IPV6Enable']");
		//loid
		if (!IsNull(loid.val(), "用户ID(逻辑ID)")) {
			loid.focus();
			return false;
		}
		//受理时间
		if (!IsNull(ordertime.val(), "开工单日期")) {
			ordertime.focus();
			return false;
		}
		//属地
		if ('' == cityId.val() || '-1' == cityId.val()) {
			alert("请选择区局");
			cityId.focus();
			return false;
		}
		//宽带账号
		if (!IsNull(adaccount.val(), "宽带账号")) {
			adaccount.focus();
			return false;
		}
		
		var portflag = false;
		var netPortStr = "";
		for(var i=0; i<checkBoxNet.length; i++){
			if(checkBoxNet[i].checked){
				if(portflag){
					netPortStr = netPortStr+",";
				}
				netPortStr = netPortStr + checkBoxNet[i].value;
				portflag = true;	
			}
		}
		//宽带上网业务绑定端口编号
		if(!portflag)
		{
			alert("宽带上网业务绑定端口编号不可为空。");
			return false;
		}
		rgportid.val(netPortStr);
		
		//宽带 UVLAN
		if (!IsNull(vlanid.val(), "宽带 VLAN")) {
			vlanid.focus();
			return false;
		}
		if("1"==IPV6Enable.val() && ""==AFTRAddress.val()){
			alert("IPV6为DSLITE模式时,需要填写AFTR地址");
			AFTRAddress.focus();
			return false;
		}
		if("1"==rgmode.val() && ""==adpassword.val()){
			alert("连接类型为路由模式时,需要填写Pppoe密码");
			AFTRAddress.focus();
			return false;
		}
		
		document.frm.submit();
	}

	function change_select() {
		var wlantype = $("select[@name='wlantype']");
		if ('3' == wlantype.val()) {
			$("input[@name='ipaddress']").val("");
			$("input[@name='code']").val("");
			$("input[@name='dns']").val("");
			$("input[@name='netway']").val("");
			$("tr[@id='dis1']").css("display", "");
			$("tr[@id='dis2']").css("display", "");
		} else {
			$("input[@name='ipaddress']").val("");
			$("input[@name='code']").val("");
			$("input[@name='dns']").val("");
			$("input[@name='netway']").val("");
			$("tr[@id='dis1']").css("display", "none");
			$("tr[@id='dis2']").css("display", "none");
		}
	}
</script>
</head>
<body>
	<form name="frm"
		action="<s:url value='/itms/service/simulateSheet4cq!sendSheet4CQ.action'/>"
		method="post">
		<TABLE border=0 cellspacing=0 cellpadding=0 width="100%" id="myTable">
			<TR>
				<TD>
					<TABLE width="100%" border=0 cellspacing=0 cellpadding=0
						align="center">
						<TR>
							<TD bgcolor=#999999>
								<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
									<TR class="green_title">
										<TD colspan="4"><input type="hidden" name="servTypeId"
											value='22'> <input
											type="hidden" name="operateType"
											value='1'> 
											<input type="hidden" name="rgportid" value=''> 
											<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%"
												BORDER="0">
												<TR>
													<TD align="center"><font size="2"><b>宽带开户信息</b></font></TD>
												</TR>
											</TABLE></TD>
									</TR>
									
									<TR bgcolor="#FFFFFF">
										<TD width="20%" class=column align="right" nowrap>用户ID(逻辑ID)</TD>
										<TD colspan="3"><INPUT TYPE="text" NAME="loid" maxlength=100
											class=bk value="" onblur="checkLoid()" >&nbsp; <font color="#FF0000">*
										</font></TD>
									</TR>
									
									
									<TR bgcolor="#FFFFFF">
										<TD width="20%" class=column align="right" width="20%">受理时间</TD>
										<TD width="30%"><input type="text" name="ordertime"
											value='<s:property value="dealdate" />' readonly class=bk>
											<img name="shortDateimg"
											onClick="WdatePicker({el:document.frm.dealdate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
											src="../../images/dateButton.png" width="15" height="12"
											border="0" alt="选择">&nbsp; <font color="#FF0000">*</font></TD>
										<TD width="20%" class=column align="right" width="20%">区局</TD>
										<TD width="30%"><s:select list="cityList" name="cityId"
												headerKey="-1" headerValue="请选择区局" listKey="city_id"
												listValue="city_name" value="cityId" cssClass="bk"></s:select>
											&nbsp; <font color="#FF0000">*</font></TD>
									</TR>




									<TR bgcolor="#FFFFFF">
										<TD width="20%" class=column align="right" nowrap>上门方式</TD>
										<TD width="30%"><select name="orderself" class="bk">
												<option value="0">==用户自助安装==</option>
												<option value="1">==外线人员上门==</option>
										</select> &nbsp; <font color="#FF0000">*</font></TD>
										<TD width="20%" class=column align="right" nowrap>设备接入类型</TD>
										<TD width="30%"><select name="devicewan" class="bk">
											<!-- onchange="change_select()"> -->
												<option value="4">==GPON==</option>
												<option value="3">==EPON==</option>
												<option value="1">==ADSL2+==</option>
												<option value="2">==LAN==</option>
												<option value="5">==VDSL2==</option>
												<option value="6">==EPONE==</option>
												<option value="7">==GPONE==</option>
										</select> &nbsp; <font color="#FF0000">*</font></TD>
									</TR>
									
									<TR bgcolor="#FFFFFF">
										<TD width="20%" class=column align="right" nowrap>用户类型</TD>
										<TD width="30%"><select name="usertype" class="bk">
												<option value="0">==公众用户==</option>
												<option value="1">==政企用户==</option>
												<!-- <option value="3">==机顶盒STB==</option> -->
										</select> &nbsp; <font color="#FF0000">*</font></TD>
										<TD width="20%" class=column align="right" nowrap>分局</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="subareacode" maxlength=10
											class=bk value="">&nbsp; <font color="#FF0000">
										</font></TD>
									</TR>
									
									<TR bgcolor="#FFFFFF">
										<TD width="20%" class=column align="right" nowrap>用户旧的ID</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="olduserid" maxlength=100
											class=bk value="">&nbsp; <font color="#FF0000">
										</font></TD>
										<TD width="20%" class=column align="right" nowrap>设备ID号</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="deviceid" maxlength=200
											class=bk value="">&nbsp; <font color="#FF0000">
										</font></TD>
									</TR>
									
									<TR bgcolor="#FFFFFF">
										<TD width="20%" class=column align="right" nowrap>宽带账号</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="adaccount" maxlength=50
											class=bk value="">&nbsp; <font color="#FF0000">*
										</font></TD>
										<TD width="20%" class=column align="right" nowrap>宽带密码</TD>
										<TD width="30%"><INPUT TYPE="password" NAME="adpassword" maxlength=50
											class=bk value="">&nbsp; <font color="#FF0000">路由模式下必填
										</font></TD>
									</TR>
									
									<TR bgcolor="#FFFFFF">
										<TD width="20%" class=column align="right" nowrap>用户地址</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="useraddress" maxlength=200
											class=bk value="">&nbsp; <font color="#FF0000">
										</font></TD>
										<TD width="20%" class=column align="right" nowrap>移机工单标记</TD>
										<TD width="30%"><select name="migration" class="bk">
												<option value="0">==非移机==</option>
												<option value="1">==移机==</option>
												<option value="2">==移机删除==</option>
												<option value="3">==移机撤销==</option>
										</select> &nbsp; <font color="#FF0000">*</font></TD>
									</TR>
									
									<TR bgcolor="#FFFFFF">
										<TD width="20%" class=column align="right" nowrap>联系人</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="contactperson" maxlength=20
											class=bk value="">&nbsp; <font color="#FF0000">
										</font></TD>
										<TD width="20%" class=column align="right" nowrap>联系电话</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="phonenumber" maxlength=20
											class=bk value="">&nbsp; <font color="#FF0000">
										</font></TD>
									</TR>
									
									
									<%-- <TR class="green_title">
										<TD colspan="4"><input type="hidden" name="servTypeId"
											value='22'> <input
											type="hidden" name="operateType"
											value='1'> <input
											type="hidden" name="username"
											value='<s:property value="loid" />'>
											<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%"
												BORDER="0">
												<TR>
													<TD align="center"><font size="2">宽带参数</font></TD>
												</TR>
											</TABLE></TD>
									</TR> --%>
									<!-- 业务参数 -->
									<TR bgcolor="#FFFFFF">
										<TD width="20%" class=column align="right" nowrap>连接类型</TD>
										<TD width="30%"><select name="rgmode" class="bk">
												<option value="2">==桥接模式==</option>
												<option value="1">==路由模式==</option>
												<!-- <option value="3">==静态IP模式==</option>
												<option value="4">==DHCP==</option> -->
										</select> &nbsp; <font color="#FF0000">*</font></TD>
										<TD width="20%" class=column align="right" nowrap>用户名称</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="username" maxlength=50
											class=bk value="">&nbsp; <font color="#FF0000">
										</font></TD>
									</TR>
									
									<TR bgcolor="#FFFFFF">
										<TD width="20%" class=column align="right" nowrap>业务绑定端口编号</TD>
										<TD id="netTD1" width="30%">
											<input type="checkbox" name="netPort" value="LAN1" class=bk />L1&nbsp;
											<input type="checkbox" name="netPort" value="LAN2" class=bk />L2&nbsp;
											<input type="checkbox" name="netPort" value="LAN3" class=bk />L3&nbsp;
											<input type="checkbox" name="netPort" value="LAN4" class=bk />L4&nbsp;
											<input type="checkbox" name="netPort" value="SSID1" class=bk />SSID1&nbsp;
											<input type="checkbox" name="netPort" value="SSID2" class=bk />SSID2&nbsp;
											<input type="checkbox" name="netPort" value="SSID3" class=bk />SSID3&nbsp;
											<input type="checkbox" name="netPort" value="SSID4" class=bk />SSID4&nbsp;<br/>
											<input type="checkbox" name="netPort" value="LAN5" class=bk />L5&nbsp;
											<input type="checkbox" name="netPort" value="LAN6" class=bk />L6&nbsp;
											<input type="checkbox" name="netPort" value="LAN7" class=bk />L7&nbsp;
											<input type="checkbox" name="netPort" value="LAN8" class=bk />L8&nbsp;
										&nbsp;<font color="#FF0000">*
										</TD>
										<TD width="20%" class=column align="right" nowrap>宽带 VLAN</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="vlanid" maxlength=10
											class=bk value="">&nbsp; <font color="#FF0000">*
										</font></TD>
									</TR>
									
									<TR bgcolor="#FFFFFF">
										<TD width="20%" class=column align="right" nowrap>vpi具体数值</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="pvcvpi" maxlength=10
											class=bk value="">&nbsp; <font color="#FF0000">
										</font></TD>
										<TD width="20%" class=column align="right" nowrap>vci具体数值</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="pvcvci" maxlength=10
											class=bk value="">&nbsp; <font color="#FF0000">
										</font></TD>
									</TR>
									
									<TR bgcolor="#FFFFFF">
										<TD width="20%" class=column align="right" nowrap>多终端限制模式</TD>
										<TD width="30%"><select name="multidevicemode" class="bk">
												<option value="0">==不启用==</option>
												<option value="1">==模式==</option>
												<option value="2">==模式2==</option>
												<option value="4">==DHCP==</option>
										</select> &nbsp; <font color="#FF0000">*</font></TD>
										<TD width="20%" class=column align="right" nowrap>路由模式下同时上网PC数</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="pcnumbers" maxlength=20
											class=bk value="">&nbsp; <font color="#FF0000">
										</font></TD>
									</TR>
									
									<TR bgcolor="#FFFFFF">
										<TD width="20%" class=column align="right" nowrap>删用户信息标签</TD>
										<TD width="30%"><select name="keepuserinfo" class="bk">
												<option value="1">保留用户信息</option>
												<option value="0">删除用户信息</option>
										</select> &nbsp; <font color="#FF0000">*</font></TD>
										<TD width="20%" class=column align="right" nowrap>是否启动IPV6</TD>
										<TD width="30%"><select name="IPV6Enable" class="bk">
												<option value="0">IPV6为不启用</option>
												<option value="1">DSLITE模式</option>
												<option value="2">启动双栈模块</option>
										</select> &nbsp; <font color="#FF0000">*</font></TD>
									</TR>
									
									<TR bgcolor="#FFFFFF">
										<TD width="20%" class=column align="right" nowrap>AFTR地址</TD>
										<TD width="30%" ><INPUT TYPE="text" NAME="AFTRAddress" maxlength=64
											class=bk value="">&nbsp; <font color="#FF0000">IPV6为DSLITE模式时,需要填该值
										</font></TD>
										<TD width="20%" class=column align="right" nowrap>WAN连接DHCP功能</TD>
										<TD width="30%"><select name="dhcpenable" class="bk">
												<option value="-1">保持原有配置</option>
												<option value="0">DHCP为不启用</option>
												<option value="1">对应wan的DHCP启用</option>
										</select> &nbsp; <font color="#FF0000">*</font></TD>
									</TR>
									
									<TR bgcolor="#FFFFFF">
										<TD width="20%" class=column align="right" nowrap>Awifi的端口编号</TD>
										<TD colspan="3"><INPUT TYPE="text" NAME="awifiportid" maxlength=20
											class=bk value="">&nbsp; <font color="#FF0000">多个中间用英文逗号隔开,awifi的端口号不能和宽带业务的端口号有重复
										</font></TD>
									</TR>
									
									
									
									<TR>
										<TD colspan="4" align="right" class="green_foot"><button
												onclick="CheckForm()">&nbsp;发送工单&nbsp;</button></TD>
									</TR>
								</TABLE>
							</TD>
						</TR>
					</TABLE>
				</TD>
			</TR>
		</TABLE>
	</form>
</body>
</html>