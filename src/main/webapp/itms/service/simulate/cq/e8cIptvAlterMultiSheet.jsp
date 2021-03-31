<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckFormForm.js"></SCRIPT>
<script type="text/javascript"
	src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<title>调整IPTV组播参数</title>
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
				$("select[@name='usertype']").val("0");
				$("select[@name='devicewan']").val('4');
			}
			else
			{
				var relt = ajax.split("|");
				var usertype = $("select[@name='usertype']");
				var devicewan = $("select[@name='devicewan']");
				devicewan.val(relt[0]);
				usertype.val(relt[2]);
			}
		}); 

	}
	
	
	function CheckForm() {
		var loid = $("input[@name='loid']");
		var vlanid = $("input[@name='vlanid']");
		//loid
		if (!IsNull(loid.val(), "用户ID(逻辑ID)")) {
			loid.focus();
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
											value='21'> <input
											type="hidden" name="operateType"
											value='16'> 
											<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%"
												BORDER="0">
												<TR>
													<TD align="center"><font size="2"><b>IPTV信息</b></font></TD>
												</TR>
											</TABLE></TD>
									</TR>
									
									
									<TR bgcolor="#FFFFFF">
										<TD width="20%" class=column align="right" nowrap>用户ID(逻辑ID)</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="loid" maxlength=100
											class=bk value="" onblur="checkLoid()">&nbsp; <font color="#FF0000">*
										</font></TD>
										<TD width="20%" class=column align="right" width="20%">受理时间</TD>
										<TD width="30%"><input type="text" name="ordertime"
											value='<s:property value="dealdate" />' readonly class=bk>
											<img name="shortDateimg"
											onClick="WdatePicker({el:document.frm.dealdate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
											src="../../images/dateButton.png" width="15" height="12"
											border="0" alt="选择">&nbsp; <font color="#FF0000">*</font></TD>
									</TR>

									<TR bgcolor="#FFFFFF">
										<TD width="20%" class=column align="right" nowrap>上门方式</TD>
										<TD width="30%"><select name="orderself" class="bk">
												<option value="0">==用户自助安装==</option>
												<option value="1">==外线人员上门==</option>
										</select> &nbsp; <font color="#FF0000">*</font></TD>
										<TD width="20%" class=column align="right" nowrap>设备接入类型</TD>
										<TD width="30%"><select name="devicewan" class="bk" >
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
										<TD width="20%" class=column align="right" nowrap>iptv账号</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="adaccount" maxlength=50
											class=bk value="">&nbsp; <font color="#FF0000">
										</font></TD>
										<TD width="20%" class=column align="right" nowrap>IPTV UVLAN</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="vlanid" maxlength=10
											class=bk value="">&nbsp; <font color="#FF0000">
										</font></TD>
									</TR>
									
									<TR bgcolor="#FFFFFF">
										<TD width="20%" class=column align="right" nowrap>用户类型</TD>
										<TD colspan="3"><select name="usertype" class="bk">
												<option value="0">==公众用户==</option>
												<option value="1">==政企用户==</option>
												<option value="3">==机顶盒STB==</option>
										</select> &nbsp; <font color="#FF0000">*</font></TD>
									</TR>
									
									<!-- 业务参数 -->
									
									<TR bgcolor="#FFFFFF">
										<TD width="20%" class=column align="right" nowrap>组播vlan</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="multivlanmode" maxlength=10
											class=bk value="">&nbsp; <font color="#FF0000">
										</font></TD>
										<TD width="20%" class=column align="right" nowrap>启用snooping使能</TD>
										<TD width="30%"><select name="snoopingEnable" class="bk">
												<option value="">==未选择==</option>
												<option value="true">==启用==</option>
												<option value="false">==不启用==</option>
										</select> &nbsp; <font color="#FF0000"></font></TD>
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