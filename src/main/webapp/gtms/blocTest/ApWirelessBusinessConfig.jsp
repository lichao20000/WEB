<%@ page contentType="text/html; charset=GBK"%>
<%@ include file="/timelater.jsp"%>
<%@ include file="/head.jsp"%>
<%@ include file="/toolbar.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<%--
	/**
	 * AP无线业务下发
	 *
	 * @author 姓名(工号) Tel:电话
	 * @version 1.0
	 * @since ${date} ${time}
	 * 
	 * <br>版权：南京联创科技 网管科技部
	 * 
	 */
 --%>

<html>
  <head>
  	<meta http-equiv="Content-Type" content="text/html; charset=GBK">
    <title>应用终端业务下发</title>
    <script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
    <link href="/css/css_green.css" rel="stylesheet" type="text/css">
    
	<script type="text/javascript">
		$(function(){
			gwShare_setGaoji();
		});
		
		var gw_type = "<%=request.getParameter("gw_type")%>";
		
		function doBusiness(){
		    var _device_id = $("input[@name='device_id']").val();
		    //var servTypeId = $("select[@name='queryServTypeId']").val();
		    var _ssid = $("input[@name='ssid']").val();
		    var _matchExp = document.getElementById('matchExpression').value
		    var url = "<s:url value='/gtms/blocTest/apDeviceBusinessConfig!doBusiness.action'/>"; 
		    
			if(null == _device_id || "" == _device_id){
				alert("请先查询设备!");
				return false;
			}
			if(null == _ssid || "" == _ssid){
				alert("请输入SSID！");
				return false;
			}
			if(null == _matchExp || "" == _matchExp){
				alert("请添加模板匹配表达式！");
				return false ;
			}
			
			// 为了防止在业务下发期间重复点击[业务下发]按钮，此处将[业务下发]按钮置灰,待业务下发，返回成功或失败后，将按钮置为可点击
			$("input[id='doButton']").attr("disabled", true); 
			
			$.post(url,{
				deviceId:_device_id,
				gw_type:gw_type,
				ssid:_ssid,
				//servTypeId:servTypeId,
				templatePara:encodeURIComponent(_matchExp)
			},function(ajax){
				alert(ajax);
			});
			// 将[业务下发]按钮置为可点击的
			$("input[id='doButton']").attr("disabled", false);
		}
		
		function deviceResult(returnVal){
				
			$("tr[@id='tr02']").css("display","");
		
			$("td[@id='tdDeviceSn']").html("");
			$("td[@id='tdDeviceCityName']").html("");
			
			$("input[@name='device_id']").val(returnVal[2][0][0]);
			$("td[@id='tdDeviceSn']").append(returnVal[2][0][1]+" -"+returnVal[2][0][2]);
			$("td[@id='tdDeviceCityName']").append(returnVal[2][0][5]);	
		}
	</script>
</head>
<body>
	<TABLE border=0 cellspacing=0 cellpadding=0 width="98%" align="center">
		<TR> <TD HEIGHT=20> &nbsp; </TD> </TR>
		<TR> 
		<TD>
			<table width="100%" border="0" cellpadding="0" cellspacing="0"	class="text">
				<tr>
				<td>
					<table width="100%" height="30" border="0" cellspacing="0"
						cellpadding="0" class="green_gargtd">
						<tr>
							<td width="162" align="center" class="title_bigwhite" nowrap>
								AP无线业务下发
							</td>
							<td nowrap>
								<img src="<s:url value="/images/attention_2.gif"/>" width="15" height="12">
								&nbsp;触发无线业务下发到AP终端
							</td>
						</tr>
					</table>
				</td>
				</tr>

				<TR bgcolor="#FFFFFF">
					<td colspan="4">
						<%@ include file="/gwms/share/gwShareDeviceQuery.jsp"%>
					</td>
				</TR>

				<TR  bgcolor="#FFFFFF">
					<TD colspan="4">
						<FORM NAME="frm">
							<TABLE id="tb1" border=0 cellspacing=1 cellpadding=2 width="100%" bgcolor=#999999>
								<TR>
									<TH colspan="4"> 业务参数 </TH>
								</TR>
								<TR bgcolor="#FFFFFF" id="tr01" >
									<td nowrap align="right" class=column width="15%">
										终端
										<input type="hidden" name="device_id" value="">
									</td>
									<td id="tdDeviceSn" width="35%">
									</td>
									<td nowrap align="right" class=column width="15%">
										属地
									</td>
									<td id="tdDeviceCityName" width="35%">
									</td>
								</TR>
								
								<!-- 
								<TR id="bizshow" bgcolor="#FFFFFF" STYLE="display:none">
									<TD class=column align="right" nowrap width="15%">业务类型:</TD>
									<TD width="35%" >
										<SELECT name="queryServTypeId" class="bk">
										<OPTION value="0">所有业务</OPTION>
										<OPTION value="10">上网业务</OPTION>
										<OPTION value="11">IPTV业务</OPTION>
										<OPTION value="14">VOIP业务</OPTION>
										</SELECT>
									</TD>
									<TD width="15%" class=column align="right">操作类型:</TD>
									<TD width="35%" id="operationTypeTd">开户</TD>
								</TR>
								 -->
								 
								<TR id="bizshow" bgcolor="#FFFFFF" >
									<TD class=column align="right" nowrap width="15%">
										<font color="red">*</font>&nbsp;SSID:
									</TD>
									<TD width="35%" colspan="3">
										<input type="text" name="ssid" maxlength=255 class=bk size=20>
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF" id="tr02" style="display: none">
									<TD width="" colspan="4">
										<%@ include file="/share/apMatchTemplate.jsp"%>
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF">
									<TD colspan="4" align="right" CLASS="green_foot">
										<INPUT type="button" value="业务下发" id="doButton" onClick="javascript:doBusiness()" class=jianbian>
									</TD>
								</TR>
							</TABLE>
						</FORM>
					</TD>
				</TR>
			</table>
		</TD>
		</TR>
		<tr> <td height="20"></td> </tr>
	</TABLE>
</body>
<%@ include file="/foot.jsp"%>
</html>
