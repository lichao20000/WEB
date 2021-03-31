<%@ page contentType="text/html; charset=GBK"%>
<%@ include file="/timelater.jsp"%>
<%@ include file="/head.jsp"%>
<%@ include file="/toolbar.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<%--
	/**
	 * 应用终端软件升级
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
	<title>应用终端软件升级</title>
	<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
    <link href="/css/css_green.css" rel="stylesheet" type="text/css">
	
	<script type="text/javascript">
		$(function(){
			gwShare_setGaoji();
		});
		
		var gw_type = "<%=request.getParameter("gw_type")%>";
		
		function upGrade(){
		    var _device_id = $("input[@name='device_id']").val();
		    var _filePath = $("select[@name='filePath']").val();
		    var _matchExp = document.getElementById('matchExpression').value
		    var url = "<s:url value='/gtms/blocTest/apDeviceSoftUpGrade!doConfig.action'/>"; 
		    
			if(null == _device_id || "" == _device_id){
				alert("请先查询设备!");
				return false;
			}
			if(null == _filePath || "" == _filePath || "-1" == _filePath){
				alert("请选择目标版本！");
				return false;
			}
			if(null == _matchExp || "" == _matchExp){
				alert("请添加模板匹配表达式！");
				return false ;
			}
			
			// 为了防止在业务下发期间重复点击[升级]按钮，此处将[升级]按钮置灰,待业务下发，返回成功或失败后，将按钮置为可点击
			$("input[id='doButton']").attr("disabled", true);  
			
			$.post(url,{
				deviceId:_device_id,
				filePath:_filePath,
				gw_type:gw_type,
				templatePara:encodeURIComponent(_matchExp)
			},function(ajax){
				alert(ajax);
				window.location.reload();
			});
			
			// 将[升级]按钮置为可点击的
			//$("input[id='doButton']").attr("disabled", false);
		}
		
		function deviceResult(returnVal){
			
			seleFilePath();
			
			$("tr[@id='tr01']").css("display","");
			$("tr[@id='tr02']").css("display","");
			$("tr[@id='tr03']").css("display","");
		
			$("td[@id='tdDeviceSn']").html("");
			$("td[@id='tdDeviceCityName']").html("");
			
			$("input[@name='device_id']").val(returnVal[2][0][0]);
			$("td[@id='tdDeviceSn']").append(returnVal[2][0][1]+" -"+returnVal[2][0][2]);
			$("td[@id='tdDeviceCityName']").append(returnVal[2][0][5]);	
		}
		
		
		function seleFilePath(){
			var url = "<s:url value='/gtms/blocTest/apDeviceSoftUpGrade!getSelectListBox.action'/>"; 
			$.post(url,{
			},function(ajax){
				document.getElementById("div_file_path").innerHTML = ajax;
			});
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
								应用终端软件升级
							</td>
							<td nowrap>
								<img src="<s:url value="/images/attention_2.gif"/>" width="15" height="12">
								&nbsp;应用终端软件升级
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
									<TH colspan="4"> 升级配置 </TH>
								</TR>
								<TR bgcolor="#FFFFFF" id="tr01" style="display: none">
									<td nowrap align="right" class=column width="15%">
										设备属地
									</td>
									<td id="tdDeviceCityName" width="35%">
									</td>
									<td nowrap align="right" class=column width="15%">
										设备序列号
										<input type="hidden" name="device_id" value="">
									</td>
									<td id="tdDeviceSn" width="35%">
									</td>
								</TR>
								<TR bgcolor="#FFFFFF" id="tr02" style="display:none">
									<td nowrap align="right" class=column width="15%">
										<font color="red">*</font>&nbsp;目标版本
									</td>
									<td width="35%" colspan="3">
										<div id="div_file_path" name="div_file_path">
											
										</div>
									</td>
									
								</TR>
								<TR bgcolor="#FFFFFF" id="tr03" style="display: none">
									<TD width="" colspan="4">
										<%@ include file="/share/apMatchTemplate.jsp"%>
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF">
									<TD colspan="4" align="right" CLASS="green_foot">
										<INPUT type="button" value=" 升 级 " id="doButton"  onClick="javascript:upGrade()" class=jianbian>
									</TD>
								</TR>
							</TABLE>
						</FORM>
					</TD>
				</TR>
			</table>
		</TD>
		</TR>
		<tr> <td height="20"></td></tr>
	</TABLE>
</body>
<%@ include file="/foot.jsp"%>
</html>