<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>设备版本查询</title>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value='../../Js/inmp/jquery.js'/>"></SCRIPT>
<link rel="stylesheet" href="<s:url value="../../css/inmp/css/css_green.css"/>" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/inmp/jQeuryExtend-linkage.js"/>"></script>
<script type="text/javascript">
	$(function(){
		parent.dyniframesize();
	});

var returnMap = "<s:property value='returnMap' />";
var return_type = "<s:property value='return_type' />";
function refresh(){
	var refreshUrl = "<s:url value='/gtms/config/qoeFunctionAct!refrsehMsg.action'/>";
	$("#refreshBtn").attr("disabled",true);
	var device_Id = $("input[@name='device_Id']").val();
	$.post(
	         refreshUrl,{
	         deviceIds : device_Id
	        } ,function(ajax){
	        	var TestDownloadStatus = ajax.split(",")[0];
	        	if("Success"==TestDownloadStatus){
							TestDownloadStatus="成功";
						}else if("Fail"==TestDownloadStatus){
							TestDownloadStatus="失败";
						}else if("Stop"==TestDownloadStatus){
							TestDownloadStatus="停止";
						}else if("Running"==TestDownloadStatus){
							TestDownloadStatus="运行中";
						}
						var MonitorStatus = ajax.split(",")[1];
						if(""==MonitorStatus){
							MonitorStatus=" ";
						}else if("Success"==MonitorStatus){
							MonitorStatus="成功";
						}else if("Fail"==MonitorStatus){
							MonitorStatus="失败";
						}else if("Stop"==MonitorStatus){
							MonitorStatus="停止";
						}else if("Running"==MonitorStatus){
							MonitorStatus="运行中";
						}
	         		$("#TestDownloadStatus").text(TestDownloadStatus);
							$("#MonitorStatus").text(MonitorStatus);
							$("#refreshBtn").attr("disabled",false);
	       });
}

$(function() {
	window.parent. $("tr[@id='trData']").hide();
	if("single_on" != return_type){
		if("success"==return_type){
			alert("执行成功");
		}else if("1"==return_type){
			alert("设备未绑定用户");
		}else if("2"==return_type){
			alert("终端版本不支持QOE功能");
		}else if("-5"==return_type){
			alert("调用后台预读模块失败！");
		}else {
			alert("提示节点获取失败，请确认版本是否支持QOE功能！");
		}
		$("#showTable").hide();
	}else{
		$("#showTable").show();
		var sn = "<s:property value='returnMap["sn"]' />";
		var loid = "<s:property value='returnMap["loid"]' />";
		var TestDownloadStatus = "<s:property value='returnMap["TestDownloadStatus"]' />";
		var MonitorStatus = "<s:property value='returnMap["MonitorStatus"]' />";
		var device_Id = "<s:property value='returnMap["deviceId"]' />";
		$("input[@name='device_Id']").val(device_Id);
		if(-1==TestDownloadStatus){
			TestDownloadStatus="设备连接不上！";
		}else if(-2==TestDownloadStatus){
			TestDownloadStatus="设备参数为空！";
		}else if(-3==TestDownloadStatus){
			TestDownloadStatus="设备正被操作！";
		}else if(-4==TestDownloadStatus){
			TestDownloadStatus="未知错误原因！";
		}else if(-6==TestDownloadStatus){
			TestDownloadStatus="正在配置QOE,请稍候点击刷新";
		}
		if("Success"==TestDownloadStatus){
			TestDownloadStatus="成功";
		}else if("Fail"==TestDownloadStatus){
			TestDownloadStatus="失败";
		}else if("Stop"==TestDownloadStatus){
			TestDownloadStatus="停止";
		}else if("Running"==TestDownloadStatus){
			TestDownloadStatus="运行中";
		}
		if(""==MonitorStatus){
			MonitorStatus=" ";
		}else if("Success"==MonitorStatus){
			MonitorStatus="成功";
		}else if("Fail"==MonitorStatus){
			MonitorStatus="失败";
		}else if("Stop"==MonitorStatus){
			MonitorStatus="停止";
		}else if("Running"==MonitorStatus){
			MonitorStatus="运行中";
		}
		
		$("#sn").html("");
		$("#loid").html("");
		$("#TestDownloadStatus").html("");
		$("#MonitorStatus").html("");
		$("#sn").text(sn);
		$("#loid").text(loid);
		$("#TestDownloadStatus").text(TestDownloadStatus);
		$("#MonitorStatus").text(MonitorStatus);
	}
	parent.dyniframesize();
});
</script>
</head>

<body>
	
	<table border=1 cellspacing=0 cellpadding=2 width="100%" id="showTable"
						align="center" style="display:none">
			<tr>
			<th colspan="5" >QOE功能测试结果</th><input type="hidden" name="device_Id" value="" />
			</tr>
			<tr>
				<th>设备序列号</th>
				<th>LOID</th>
				<th>QOE插件程序下载状态结果</th>
				<th>QOE插件运行状态</th>
				<th>操作</th>
			</tr>
			<tr>
				<td id="sn" align="center"></td>
				<td id="loid" align="center"></td>
				<td id="TestDownloadStatus" align="center"></td>
				<td id="MonitorStatus" align="center"></td>
				<td  align="center"> <INPUT TYPE="button" id="refreshBtn" value="刷新" class=btn onclick="refresh()"></td>
			</tr>
	</table>
</body>

</html>