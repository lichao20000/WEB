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
	function CheckForm() {
		var loid = $("input[@name='loid']");
		var ordertime = $("input[@name='ordertime']");
		var cityId = $("select[@name='cityId']");
		var subareacode = $("input[@name='subareacode']");
		var orderself = $("select[@name='orderself']");
		var devicewan = $("select[@name='devicewan']");
		var usertype = $("select[@name='usertype']");
		
		
		var adaccount = $("input[@name='adaccount']");
		var username = $("input[@name='username']");
		var useraddress = $("input[@name='useraddress']");
		var contactperson = $("input[@name='contactperson']");
		var phonenumber = $("input[@name='phonenumber']");
		
		/* 业务字段 */
		
		//loid
		if (!IsNull(loid.val(), "机顶盒STBID")) {
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
		if (!IsNull(phonenumber.val(), "FTTH光猫的LOID号")) {
			phonenumber.focus();
			return false;
		}
		
		
		document.frm.submit();
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
											value='26'> <input
											type="hidden" name="operateType"
											value='3ims'> 
											<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%"
												BORDER="0">
												<TR>
													<TD align="center"><font size="2"><b>stb_ims销户信息</b></font></TD>
												</TR>
											</TABLE></TD>
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
											&nbsp; <font color="#FF0000"></font></TD>
									</TR>

									<TR bgcolor="#FFFFFF">
										<TD width="20%" class=column align="right" nowrap>上门方式</TD>
										<TD colspan="3"><select name="orderself" class="bk">
												<option value="0">==用户自助安装==</option>
												<option value="1">==外线人员上门==</option>
										</select> &nbsp; <font color="#FF0000">*</font></TD>
										<%-- <TD width="20%" class=column align="right" nowrap>设备接入类型</TD>
										<TD width="30%"><select name="devicewan" class="bk">
											<!-- onchange="change_select()"> -->
												<option value="3">==EPON==</option>
												<option value="4">==GPON==</option>
												<option value="1">==ADSL2+==</option>
												<option value="2">==LAN==</option>
												<option value="5">==VDSL2==</option>
												<option value="6">==EPONE==</option>
												<option value="7">==GPONE==</option>
										</select> &nbsp; <font color="#FF0000">*</font></TD> --%>
									</TR>
									
									<TR bgcolor="#FFFFFF">
										<TD width="20%" class=column align="right" nowrap>用户类型</TD>
										<TD width="30%"><select name="usertype" class="bk">
												<option value="3">==机顶盒STB==</option>
										</select> &nbsp; <font color="#FF0000">*</font></TD>
										<TD width="20%" class=column align="right" nowrap>分局</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="subareacode" maxlength=10
											class=bk value="">&nbsp; <font color="#FF0000">
										</font></TD>
									</TR>
									
									<TR bgcolor="#FFFFFF">
										
										<TD width="20%" class=column align="right" nowrap>设备ID号</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="deviceid" maxlength=200
											class=bk value="">&nbsp; <font color="#FF0000">
										</font></TD>
										<TD width="20%" class=column align="right" nowrap>用户地址</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="useraddress" maxlength=200
											class=bk value="">&nbsp; <font color="#FF0000">
										</font></TD>
									</TR>
									
									<TR bgcolor="#FFFFFF">
										<TD width="20%" class=column align="right" nowrap>itv账号</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="adaccount" maxlength=50
											class=bk value="">&nbsp; <font color="#FF0000">
										</font></TD>
										<TD width="20%" class=column align="right" nowrap>用户名称</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="username" maxlength=50
											class=bk value="">&nbsp; <font color="#FF0000">
										</font></TD>
									</TR>
									
									
									<TR bgcolor="#FFFFFF">
										<TD width="20%" class=column align="right" nowrap>联系人</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="contactperson" maxlength=20
											class=bk value="">&nbsp; <font color="#FF0000">
										</font></TD>
										<TD width="20%" class=column align="right" nowrap>FTTH光猫的LOID号</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="phonenumber" maxlength=20
											class=bk value="">&nbsp; <font color="#FF0000">*
										</font></TD>
									</TR>
									
									
									<TR bgcolor="#FFFFFF">
										<TD width="20%" class=column align="right" nowrap>机顶盒STBID</TD>
										<TD colspan="3"><INPUT TYPE="text" NAME="loid" maxlength=100
											class=bk value="">&nbsp; <font color="#FF0000">*
										</font></TD>
									</TR>
									<!-- 业务参数 -->
									
									
									
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