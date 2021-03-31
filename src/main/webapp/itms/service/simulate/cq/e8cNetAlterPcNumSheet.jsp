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
		var multidevicemode = $("select[@name='multidevicemode']");
		var pcnumbers = $("input[@name='pcnumbers']");
		/* 业务字段 */
		
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
		
		if("0"!=multidevicemode.val() && ""==pcnumbers.val()){
			alert("多终端限制模式启用时上网PC数必填");
			AFTRAddress.focus();
			return false;
		}
		
		// 终端不启用，设置pcnumbers为空
		if("0" == multidevicemode.val()){
			pcnumbers.val("");
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
											value='17'> 
											<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%"
												BORDER="0">
												<TR>
													<TD align="center"><font size="2"><b>修改终端上网数</b></font></TD>
												</TR>
											</TABLE></TD>
									</TR>
									
									<TR bgcolor="#FFFFFF">
										<TD width="20%" class=column align="right" nowrap>用户ID(逻辑ID)</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="loid" maxlength=100
											class=bk value="" onblur="checkLoid()">&nbsp; <font color="#FF0000">*
										</font></TD>
										<TD width="20%" class=column align="right" nowrap>用户类型</TD>
										<TD width="30%"><select name="usertype" class="bk">
												<option value="0">==公众用户==</option>
												<option value="1">==政企用户==</option>
												<!-- <option value="3">==机顶盒STB==</option> -->
										</select> &nbsp; <font color="#FF0000">*</font></TD>
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
										<TD width="20%" class=column align="right" nowrap>多终端限制模式</TD>
										<TD width="30%"><select name="multidevicemode" class="bk">
												<option value="0">==不启用==</option>
												<option value="1">==模式==</option>
												<option value="2">==模式2==</option>
												<option value="4">==DHCP==</option>
										</select> &nbsp; <font color="#FF0000">*</font></TD>
										<TD width="20%" class=column align="right" nowrap>路由模式下同时上网PC数</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="pcnumbers" maxlength=20
											class=bk value="">&nbsp; <font color="#FF0000">多终端限制模式启用时必填
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