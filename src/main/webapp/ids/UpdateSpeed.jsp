
<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>测速速率修改</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript"
	SRC="<s:url value="/Js/CheckFormForm.js"/>"></SCRIPT>
<script type="text/javascript">

		function updateAcc(){
			var devSn=$.trim($("#devSn").val());
			var divValue=$.trim($("#divId").html());
			var netSpeed=$.trim($("#netSpeed").val());
			
			if(devSn==''){
				alert('请先输入设备序列号进行查询，再进行修改');
				return false;
			}
			if(divValue=='没有查询结果'){
				alert('该设备在速率表中没有查询结果，不进行修改');
				return false;
			}
			if(divValue==''){
				alert('请先查询该设备是否在速率表中存在，再进行修改');
				return false;
			}
			if(netSpeed=='-1'){
				alert('请先选择速率，再进行修改');
				return false;
			}
			
			var url = "<s:url value='/ids/addSimSpeedAcc!updateRate.action'/>";
			$.post(url, {
             	devSn:devSn,
             	netRate:netSpeed
             }, function(ajax) {
            	$("#divId").html('');
             	if(ajax=='-1'){
            		alert("修改速率失败");
            	}else{
            		alert("修改速率成功");
            	}
             });
        }
        
        function searchAcc(){
        	 var devSn=$.trim($("#devSn").val());
        	 if(devSn=='' || $.trim(devSn).length<6){
        		 alert('请输入不小于六位的设备序列号');
        		 return false;
        	 }
        	 var url = "<s:url value='/ids/addSimSpeedAcc!searchAcc.action'/>";
             $.post(url, {
             	devSn:devSn
             }, function(ajax) {
             	if(ajax==''){
            		alert("没有查询结果");
            		$("#divId").html('没有查询结果')
            		return false;
            	}else if(ajax=='toManyResults'){
            		alert("查询到多条信息,请输入完整设备序列号进行查询");
        			$("#divId").html('')
        		return false;
        		}
             	var resArr = ajax.split("##");
             	$("#divId").html("当前速率："+resArr[0]);
             	$("#devSn").val(resArr[1]);
             });
        }       
        
        function clearDivValue(){
        	$("#divId").html('');
        }
        </script>
</head>

<body>
	<table>
		<tr>
			<td HEIGHT=20>&nbsp;</td>
		</tr>
		<tr>
			<td>
				<table width="100%" height="30" border="0" cellspacing="0"
					cellpadding="0" class="green_gargtd">
					<tr>
						<td width="162" align="center" class="title_bigwhite">
							测速速率修改</td>
						<td><img src="<s:url value="/images/attention_2.gif"/>"
							width="15" height="12" /> 测速速率修改</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td>
				<table class="querytable">
					<TR>
						<th colspan="4">测速速率修改</th>
					</tr>
					<TR>
						<td class="column" width='15%' align="right">设备序列号</td>
						<TD width="35%">
						<input id="devSn" value="" maxlength="40" onfocus="clearDivValue()"/>
						<div id='divId'></div>
						</TD>
						<td class="column" width='15%' align="right">带宽速率</td>
						<TD width="35%"><s:select list="netRateList" id="netSpeed"
								listKey="rate" listValue="rate" headerKey="-1" headerValue="请选择"
								cssClass="bk" onchange="queryAccount()"></s:select> (M)</TD>
					</tr>
					<TR>
						<td colspan="4" align="right" class=foot>
							<button onclick="searchAcc()">&nbsp;查 询&nbsp;</button>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<button onclick="updateAcc()">&nbsp;提 交&nbsp;</button>
						</td>
					</TR>
				</table>
			</td>
		</tr>
	</table>
</body>
</html>
<%@ include file="../foot.jsp"%>