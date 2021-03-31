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
		
		
		var adaccount = $("input[@name='adaccount']");
		var username = $("input[@name='username']");
		var useraddress = $("input[@name='useraddress']");
		var contactperson = $("input[@name='contactperson']");
		var phonenumber = $("input[@name='phonenumber']");
		var migration = $("select[@name='migration']");
		
		/* 业务字段 */
		var mgip = $("input[@name='mgip']");
		var mgmask = $("input[@name='mgmask']");
		var vlanid = $("input[@name='vlanid']");
		var imsdnsip = $("input[@name='imsdnsip']");
		var mggw = $("input[@name='mggw']");
		var proxyserverip = $("input[@name='proxyserverip']");
		var proxyserverport = $("input[@name='proxyserverport']");
		var registrarserverip = $("input[@name='registrarserverip']");
		var registrarserverport = $("input[@name='registrarserverport']");
		var sipuri = $("input[@name='sipuri']");
		var usertid = $("select[@name='usertid']");
		var authusername = $("input[@name='authusername']");
		var authpassword = $("input[@name='authpassword']");
		var protocoltype = $("select[@name='protocoltype']");
		
		var mgDomain =  $("input[@name='mgDomain']");
		var PhonenumberSip = $("input[@name='PhonenumberSip']");
		var ssip = $("input[@name='ssip']");
		var ssport = $("input[@name='ssport']");
		var ssbakip = $("input[@name='ssbakip']");
		var ssbakport = $("input[@name='ssbakport']");
		
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
		
		//IPTV UVLAN
		if (!IsNull(vlanid.val(), "VOIP UVLAN")) {
			vlanid.focus();
			return false;
		}
		if (!IsNull(imsdnsip.val(), "DNS服务器地址")) {
			imsdnsip.focus();
			return false;
		}
		if(!IsNull(mgip.val(), "MG IP地址")){
			mgip.focus();
			return false;
		}
		if(!IsNull(mgmask.val(), "MG 掩码")){
			mgmask.focus();
			return false;
		}
		if(!IsNull(mggw.val(), "MG网关IP")){
			mggw.focus();
			return false;
		}
		
		debugger;
		if(protocoltype.val()=="0"){
			if(!IsNull(proxyserverip.val(), "SIP服务器代理地址或域名")){
				proxyserverip.focus();
				return false;
			}
			if(!IsNull(proxyserverport.val(), "SIP服务器代理端口号")){
				proxyserverport.focus();
				return false;
			}
			if(!IsNull(registrarserverip.val(), "主用注册服务器IP地址（域名）")){
				registrarserverip.focus();
				return false;
			}
			if(!IsNull(registrarserverport.val(), "主用注册服务器端口")){
				registrarserverport.focus();
				return false;
			}
			if(""==sipuri.val()&& protocoltype.val()=="0"){
				alert("IMS SIP必填SIP URI用户电话号码");
				sipuri.focus();
				return false;
			}

			if(!IsNull(authusername.val(), "软交换用户名")){
				authusername.focus();
				return false;
			}
			if(!IsNull(authpassword.val(), "软交换密码")){
				authpassword.focus();
				return false;
			}
		}
		else{
			if(!IsNull(mgDomain.val(), "H.248 MG注册域名")){
				mgDomain.focus();
				return false;
			}
			if(!IsNull(PhonenumberSip.val(), "电话号码(H248)")){
				PhonenumberSip.focus();
				return false;
			}
			if(!IsNull(ssip.val(), "主用软交换IP地址")){
				ssip.focus();
				return false;
			}
			if(!IsNull(ssport.val(), "主用软交换端口")){
				ssport.focus();
				return false;
			}
			if(!IsNull(ssbakip.val(), "备用软交换IP地址")){
				ssbakip.focus();
				return false;
			}
			if(!IsNull(ssbakport.val(), "备用软交换端口")){
				ssbakport.focus();
				return false;
			}
			
		}
		
		document.frm.submit();
	}
	
	
	function changetype(){
		var protocoltype = $("#protocoltype").val();
		if(protocoltype=="0"){
			$("#siptab").show();
			$("#H248tab").hide();
		}
		else{
			$("#siptab").hide();
			$("#H248tab").show();
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
											value='14'> <input
											type="hidden" name="operateType"
											value='1'> 
											<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%"
												BORDER="0">
												<TR>
													<TD align="center"><font size="2"><b>VOIP开户信息</b></font></TD>
												</TR>
											</TABLE></TD>
									</TR>
									
									<TR bgcolor="#FFFFFF">
										<TD width="20%" class=column align="right" nowrap>协议类型</TD>
										<TD colspan="3"><select name="protocoltype" id="protocoltype" class="bk" onchange="changetype()">
												<option value="0">==IMS SIP==</option>
												<!-- <option value="1">==SS SIP==</option> -->
												<option value="2">==SS H.248==</option>
												<option value="3">==IMS H.248==</option>
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
										<TD width="20%" class=column align="right" nowrap>账号</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="adaccount" maxlength=50
											class=bk value="">&nbsp; <font color="#FF0000">
										</font></TD>
										<TD width="20%" class=column align="right" nowrap>用户名称</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="username" maxlength=50
											class=bk value="">&nbsp; <font color="#FF0000">
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
									
									<!-- 业务参数 -->
									<TR bgcolor="#FFFFFF">
										<TD width="20%" class=column align="right" nowrap>用户ID(逻辑ID)</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="loid" maxlength=100
											class=bk value="" onblur="checkLoid()">&nbsp; <font color="#FF0000">*
										</font></TD>
										<TD width="20%" class=column align="right" nowrap>VOIP UVLAN</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="vlanid" maxlength=10
											class=bk value="">&nbsp; <font color="#FF0000">*
										</font></TD>
									</TR>
									
									<TR bgcolor="#FFFFFF">
										<TD width="20%" class=column align="right" nowrap>DNS服务器地址</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="imsdnsip" maxlength=32
											class=bk value="">&nbsp; <font color="#FF0000">*</font></TD>
										<TD width="20%" class=column align="right" nowrap>DNS备用服务器地址</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="imsdnsbakip" maxlength=32
											class=bk value="">&nbsp; <font color="#FF0000"></font></TD>
									</TR>
									
									<TR bgcolor="#FFFFFF">
										<TD width="20%" class=column align="right" nowrap>MG IP地址</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="mgip" maxlength=32
											class=bk value="">&nbsp; <font color="#FF0000">*
										</font></TD>
										<TD width="20%" class=column align="right" nowrap>MG 掩码</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="mgmask" maxlength=32
											class=bk value="">&nbsp; <font color="#FF0000">*
										</font></TD>
									</TR>
									
									<TR bgcolor="#FFFFFF">
										<TD width="20%" class=column align="right" nowrap>MG网关IP</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="mggw" maxlength=32
											class=bk value="">&nbsp; <font color="#FF0000">*
										</font></TD>
										<TD width="20%" class=column align="right" nowrap>终端语音端口号</TD>
										<TD width="30%">
										<select name="usertid" class="bk">
											<option value="000">端口1</option>
											<option value="001">端口2</option>
											<option value="002">端口3</option>
											<option value="003">端口4</option>
											<option value="004">端口5</option>
											<option value="005">端口6</option>
											<option value="006">端口7</option>
											<option value="007">端口8</option>
											<option value="008">端口9</option>
											<option value="009">端口10</option>
											<option value="010">端口11</option>
											<option value="011">端口12</option>
											<option value="012">端口13</option>
											<option value="013">端口14</option>
											<option value="014">端口15</option>
											<option value="015">端口16</option>
											<option value="016">端口17</option>
											<option value="017">端口18</option>
											<option value="018">端口19</option>
											<option value="019">端口20</option>
											<option value="020">端口21</option>
											<option value="021">端口22</option>
											<option value="022">端口23</option>
											<option value="023">端口24</option>
											<option value="024">端口25</option>
											<option value="025">端口26</option>
											<option value="026">端口27</option>
											<option value="027">端口28</option>
											<option value="028">端口29</option>
											<option value="029">端口30</option>
											<option value="030">端口31</option>
											<option value="031">端口32</option>
										</select>
										<font color="#FF0000">*</font></TD>
									</TR>
									
									<TABLE border=0 cellspacing=1 cellpadding=2 width="100%" id="H248tab" style="display: none;">
									<!-- H.248 -->
									
									<TR bgcolor="#FFFFFF">
										<TD width="20%" class=column align="right" nowrap>H.248 MG注册域名(H.248协议)</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="mgDomain" maxlength=100
											class=bk value="">&nbsp; <font color="#FF0000">*
										</font></TD>
										<TD width="20%" class=column align="right" nowrap>电话号码</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="PhonenumberSip" maxlength=32
											class=bk value="">&nbsp; <font color="#FF0000">*</font></TD>
									</TR>
									
									<TR bgcolor="#FFFFFF">
										<TD width="20%" class=column align="right" nowrap>主用软交换IP地址</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="ssip" maxlength=32
											class=bk value="">&nbsp; <font color="#FF0000">*
										</font></TD>
										<TD width="20%" class=column align="right" nowrap>主用软交换端口</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="ssport" maxlength=6
											class=bk value="">&nbsp; <font color="#FF0000">*
										</font></TD>
									</TR>
									
									<TR bgcolor="#FFFFFF">
										<TD width="20%" class=column align="right" nowrap>备用软交换IP地址</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="ssbakip" maxlength=32
											class=bk value="">&nbsp; <font color="#FF0000">*
										</font></TD>
										<TD width="20%" class=column align="right" nowrap>备用软交换端口</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="ssbakport" maxlength=6
											class=bk value="">&nbsp; <font color="#FF0000">*
										</font></TD>
									</TR>
									
									</TABLE>
									<!-- H.248 -->
									
									<!-- sip -->
									<TABLE border=0 cellspacing=1 cellpadding=2 width="100%" id="siptab" >
									<TR bgcolor="#FFFFFF">
										<TD width="20%" class=column align="right" nowrap>SIP服务器代理地址或域名</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="proxyserverip" maxlength=32
											class=bk value="">&nbsp; <font color="#FF0000">*
											</font></TD>
										<TD width="20%" class=column align="right" nowrap>SIP服务器代理端口号</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="proxyserverport" maxlength=6
											class=bk value="">&nbsp; <font color="#FF0000">*
											</font></TD>
									</TR>
									
									<TR bgcolor="#FFFFFF">
										<TD width="20%" class=column align="right" nowrap>备用SIP服务器地址</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="proxyserverbakip" maxlength=32
											class=bk value="">&nbsp; <font color="#FF0000">
											</font></TD>
										<TD width="20%" class=column align="right" nowrap>备用SIP服务器端口</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="proxyserverbakport" maxlength=6
											class=bk value="">&nbsp; <font color="#FF0000">
											</font></TD>
									</TR>
									
									<TR bgcolor="#FFFFFF">
										<TD width="20%" class=column align="right" nowrap>主用注册服务器IP地址（域名）</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="registrarserverip" maxlength=32
											class=bk value="">&nbsp; <font color="#FF0000">*</font></TD>
										<TD width="20%" class=column align="right" nowrap>主用注册服务器端口</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="registrarserverport" maxlength=6
											class=bk value="">&nbsp; <font color="#FF0000">*</font></TD>
									</TR>
									
									<TR bgcolor="#FFFFFF">
										<TD width="20%" class=column align="right" nowrap>备用注册服务器IP地址（域名）</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="standbyregistrarserverip" maxlength=32
											class=bk value="">&nbsp; <font color="#FF0000"></font></TD>
										<TD width="20%" class=column align="right" nowrap>备用注册服务器端口</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="standbyregistrarserverport" maxlength=6
											class=bk value="">&nbsp; <font color="#FF0000"></font></TD>
									</TR>
									
									
									<TR bgcolor="#FFFFFF">
										<TD width="20%" class=column align="right" nowrap>软交换用户名</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="authusername" maxlength=100
											class=bk value="">&nbsp; <font color="#FF0000">*</font></TD>
										<TD width="20%" class=column align="right" nowrap>软交换密码</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="authpassword" maxlength=100
											class=bk value="">&nbsp; <font color="#FF0000">*</font></TD>
									</TR>
									
									<TR bgcolor="#FFFFFF">
										<TD width="20%" class=column align="right" nowrap>用户电话号码</TD>
										<TD colspan="3"><INPUT TYPE="text" NAME="sipuri" maxlength=32
											class=bk value="">&nbsp; <font color="#FF0000">IMS SIP必填</font></TD>
									</TR>
									</TABLE>
									<!-- sip -->
									
									<TABLE border=0 cellspacing=1 cellpadding=2 width="100%" >
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