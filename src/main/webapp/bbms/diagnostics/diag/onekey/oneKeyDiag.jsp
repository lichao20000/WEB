<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<link href="../../css/css_green.css" rel="stylesheet" type="text/css">

<title>一键诊断</title>
<script type="text/javascript">
<!--//

//业务类型，上网
var serv_type_id = "10";
var device_id = '<s:property value="deviceId"/>';
var user_id = '<s:property value="userId"/>';
var wire_type = '1';//有线

function clearDiv()
{
	$("div[@id='dsl_result']").html("");
	$("div[@id='ping_result']").html("");
	$("div[@id='httpget_result']").html("");
	$("div[@id='tracerout_result']").html("");
	$("div[@id='dnsquery_result']").html("");
}

function doDiag()
{
	clearDiv();
	//block();
	var dslCheckbox = document.all("dslCheckbox").checked;
	var pingCheckbox = document.all("pingCheckbox").checked;
	var httpGetCheckbox = document.all("httpGetCheckbox").checked;
	var traceRouteCheckbox = document.all("traceRouteCheckbox").checked;
	var dnsQueryCheckbox = document.all("dnsQueryCheckbox").checked;
	
	if(document.all("dslCheckbox").checked == false &&
		document.all("pingCheckbox").checked == false &&
		document.all("httpGetCheckbox").checked == false &&
		document.all("traceRouteCheckbox").checked == false &&
		document.all("dnsQueryCheckbox").checked == false)
		{
		alert("请选择至少一项诊断！");
		}
	if(dslCheckbox == true)
	{
		dslDiag(pingCheckbox,httpGetCheckbox,traceRouteCheckbox,dnsQueryCheckbox);
	}else if(pingCheckbox == true)
	{
		pingDiag(httpGetCheckbox,traceRouteCheckbox,dnsQueryCheckbox);
	}else if(httpGetCheckbox == true)
	{
		httpgetDiag(traceRouteCheckbox,dnsQueryCheckbox);
	}else if(traceRouteCheckbox == true)
	{
		traceroutDiag(dnsQueryCheckbox);
	}else if(dnsQueryCheckbox == true)
	{
		dnsqueryDiag();
	}
}

// dsl
function dslDiag(pingCheckbox,httpGetCheckbox,traceRouteCheckbox,dnsQueryCheckbox)
{
		var interface = document.getElementById("dslWan");
		if(interface == undefined || interface.value=="")
		{
			alert("请获取接口！");
			document.getElementById("dslWan").focus();
			return false;
		}

		//$("div[@id='dsl_result']").html("");
		
	$("div[@id='dsl_result']").html("正在诊断...");
    var url = "<s:url value='/bbms/diag/oneKeyDiag!dslDiag.action' />";
    var dslWan = $("select[@id='dslWan']").val();

	$.post(url,{
		deviceId:deviceId,
		dslWan:dslWan
	},function(ajaxMesg){
		//alert(ajaxMesg);
		$("div[@id='dsl_result']").html(ajaxMesg);
		//parent.dyniframesize();
		//是否继续
		if(pingCheckbox == true)
	{
		pingDiag(httpGetCheckbox,traceRouteCheckbox,dnsQueryCheckbox);
	}else if(httpGetCheckbox == true)
	{
		httpgetDiag(traceRouteCheckbox,dnsQueryCheckbox);
	}else if(traceRouteCheckbox == true)
	{
		traceroutDiag(dnsQueryCheckbox);
	}else if(dnsQueryCheckbox == true)
	{
		dnsqueryDiag();
	}
	});
}

// ping
function pingDiag(httpGetCheckbox,traceRouteCheckbox,dnsQueryCheckbox)
{
	
	$("div[@id='ping_result']").html("正在诊断...");
	
	var url = "<s:url value='/bbms/diag/oneKeyDiag!pingDiag.action' />";
	
	var dataBlockSize = $("input[@name='ping_DataBlockSize']").val();
	var host = $("input[@name='ping_Host']").val();
	var numberOfRepetitions =  $("input[@name='ping_NumberOfRepetitions']").val();
	var timeout = $("input[@name='ping_Timeout']").val();
	
	$.post(url,{
		deviceId:deviceId,
		dataBlockSize:dataBlockSize,
		host:host,
		numberOfRepetitions:numberOfRepetitions,
		timeout:timeout
	},function(ajaxMesg){
		//alert(ajaxMesg);
		$("div[@id='ping_result']").html(ajaxMesg);
		//parent.dyniframesize();
		//是否继续
		if(httpGetCheckbox == true)
	{
		httpgetDiag(traceRouteCheckbox,dnsQueryCheckbox);
	}else if(traceRouteCheckbox == true)
	{
		traceroutDiag(dnsQueryCheckbox);
	}else if(dnsQueryCheckbox == true)
	{
		dnsqueryDiag();
	}
	});
}

// httpget
function httpgetDiag(traceRouteCheckbox,dnsQueryCheckbox)
{
	
	$("div[@id='httpget_result']").html("正在诊断...");
	var url = "<s:url value='/bbms/diag/oneKeyDiag!httpgetDiag.action' />";
	
	var httpVersion = $("select[@name='httpVersion']").val();
	var url_ = $("input[@name='httpget_URL']").val();
	var numberOfRepetitions = $("input[@name='httpget_NumberOfRepetitions']").val();
	var timeout = $("input[@name='httpget_Timeout']").val();
	
	$.post(url,{
		deviceId:deviceId,
		httpVersion:httpVersion,
		url:url_,
		numberOfRepetitions:numberOfRepetitions,
		timeout:timeout
	},function(ajaxMesg){
		//alert(ajaxMesg);
		$("div[@id='httpget_result']").html(ajaxMesg);
		//traceroutDiag();
		//parent.dyniframesize();
		//是否继续
		if(traceRouteCheckbox == true)
	{
		traceroutDiag(dnsQueryCheckbox);
	}else if(document.all("dnsQueryCheckbox").checked == true)
	{
		dnsqueryDiag();
	}
	});
}

// tracerout
function traceroutDiag(dnsQueryCheckbox)
{
	
	$("div[@id='tracerout_result']").html("正在诊断...");
	var url = "<s:url value='/bbms/diag/oneKeyDiag!traceroutDiag.action' />";
	
	var host = $("input[@name='tracerout_Host']").val();
	var maxHopCount = $("input[@name='tracerout_MaxHopCount']").val();
	var timeout = $("input[@name='tracerout_Timeout']").val();
	
	$.post(url,{
		deviceId:deviceId,
		host:host,
		maxHopCount:maxHopCount,
		timeout:timeout
	},function(ajaxMesg){
		//alert(ajaxMesg);
		$("div[@id='tracerout_result']").html(ajaxMesg);
		//parent.dyniframesize();
		//是否继续
		if(dnsQueryCheckbox == true)
	{
		dnsqueryDiag();
	}
	});
}

// dnsQuery
function dnsqueryDiag()
{
	
	$("div[@id='dnsquery_result']").html("正在诊断...");
	var url = "<s:url value='/bbms/diag/oneKeyDiag!dnsqueryDiag.action' />";
	
	var dnsServerIP = $("input[@name='DNSServerIP']").val();
	var domainName = $("input[@name='DomainName']").val();
	var numberOfRepetitions = $("input[@name='dnsquery_NumberOfRepetitions']").val();
	var timeout = $("input[@name='dnsquery_Timeout']").val();
	
	$.post(url,{
		deviceId:deviceId,
		dnsServerIP:dnsServerIP,
		domainName:domainName,
		numberOfRepetitions:numberOfRepetitions,
		timeout:timeout
	},function(ajaxMesg){
		//alert(ajaxMesg);
		$("div[@id='dnsquery_result']").html(ajaxMesg);
		//parent.dyniframesize();
	});
}

function getInterface()
{
	$("span[@id='interfaceSpan']").html("<font color=blue>正在获取...</font>");
	var url = "<s:url value='/bbms/diag/oneKeyDiag!getDSLWans.action' />";
	$.post(url,{
		deviceId:deviceId
	},function(ajaxMesg){
		setDslWan(ajaxMesg);
	});
	
}

function setDslWan(html)
{
	if(html == ""){
		alert("获取失败,设备正忙或者发生连接错误!");
	}else{
		var sel =  document.createElement("select");
		sel.name = "dslWan";
		sel.id = "dslWan";
		sel.className = "column";
		var wans = html.split("\6");
		for(i=0; i<wans.length; i++)
		{
			var option = document.createElement("option");
			sel.options.add(option);
			option.innerHTML = wans[i];
			option.value = wans[i];
		}
		document.getElementById("interfaceSpan").innerHTML = sel.outerHTML;
	}
}

//-->
</script>
</head>
<body>
<form action="deviceDiagnostics">
<table width="100%" border=0 cellspacing=0 cellpadding=0 align="center" height="auto">
	<tr>
		<td bgcolor=#999999>
			<table border=0 cellspacing=1 cellpadding=2 width="100%" height="auto">
				<tr>
					<th>相关工具</th>
				</tr>
				<tr bgcolor=#ffffff>
					<td>
						<button onclick="javascript:open1()">Syslog日志分析</button>
						<button onclick="javascript:open2()">企业网关重启</button>
						<button onclick="javascript:open3()">配置文件上传</button>
						<button onclick="javascript:open4()">配置文件下发</button>
						<button onclick="javascript:open5()">SNMP告警采集</button>
						<button onclick="javascript:open6()">SNMP流量性能</button>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td height="20">
		</td>
	</tr>
	<tr>
		<td bgcolor=#999999>
			<table border=0 cellspacing=1 cellpadding=2 width="100%" height="auto">
				<tr>
					<th colspan="4">一键诊断内容</th>
				</tr>
				<tr CLASS="green_foot">
					<td colspan="4">&nbsp;&nbsp;<input type="checkbox" name="dslCheckbox">&nbsp;DSL诊断</td>
				</tr>
				<tr bgcolor=#ffffff>
					<td class="column" align="right">WAN连接:</td>
					<td colspan="3" valign="top">
						<span id="interfaceSpan" >
							<input type="text" size="15"  class="bk" id="dslWan" />
						</span>&nbsp;<button onclick="getInterface()" style="padding-bottom:10px">获 取</button>
					</td>
				</tr>
				<tr>
					<td colspan="4" bgcolor=#ffffff  height="18">
						<div id="dsl_result"></div>
					</td>
				</tr>
			
				<!-- PING -->
				<tr CLASS="green_foot">
					<td colspan="8">&nbsp;&nbsp;<input type="checkbox" name="pingCheckbox">&nbsp;PING诊断</td>
				</tr>
				<tr bgcolor=#ffffff>
					<td class=column align="right" width="15%" nowrap>包大小:&nbsp;</td>
					<td width="35%">
						<input type="text" name="ping_DataBlockSize" class="bk" size="16" value="32">&nbsp;(byte)
					</td>
					<td class=column width="15%" align="right">测试IP:&nbsp;</td>
					<td width="35%" >
						<input type="text" name="ping_Host" class="bk">
					</td>
				</tr>
				<tr bgcolor=#ffffff>
					<td class=column width="15%" align="right">包数目:&nbsp;</td>
					<td width="35%" >
						<input type="text" name="ping_NumberOfRepetitions" class="bk"size="16" value="2">
					</td>
					<td class=column align="right" nowrap>超时时间(ms):&nbsp;</td>
					<td>
						<input type="text" name="ping_Timeout" class="bk" size="16" value="2000">
					</td>
				</tr>
				<tr>
					<td colspan="4" bgcolor=#ffffff  height="18">
						<div id="ping_result"></div>
					</td>
				</tr>
			
				<!-- HTTP GET -->
				<tr CLASS="green_foot">
					<td colspan="4">&nbsp;&nbsp;<input type="checkbox" name="httpGetCheckbox">&nbsp;HttpGet诊断</td>
				</tr>
				<tr bgcolor=#ffffff>
					<td class=column align="right" nowrap>HTTP版本号:&nbsp;</td>
					<td>
						<select name="httpVersion" id="httpVersion" class='bk'>
							<option selected value="HTTP/1.0">HTTP/1.0</option>
							<option value="HTTP/1.1">HTTP/1.1</option>
						</select>
					</td>
					<td class=column align="right" >测试地址:&nbsp;</td>
					<td>
						<input type="text" name="httpget_URL" class="bk">
					</td>
				</tr>
				<tr bgcolor=#ffffff>
					<td class=column align="right">测试次数:&nbsp;</td>
					<td>
						<input type="text" name="httpget_NumberOfRepetitions" class="bk" size="16" value="5">
					</td>
					<td class=column align="right" nowrap>等待时间(ms):&nbsp;</td>
					<td>
						<input type="text" name="httpget_Timeout" class="bk" size="16" value="5000">
					</td>
				</tr>
				<tr>
					<td colspan="4" bgcolor=#ffffff height="18">
						<div id="httpget_result"></div>
					</td>
				</tr>
			
				<!-- TraceRoute -->
				<tr CLASS="green_foot">
					<td colspan="4">&nbsp;&nbsp;<input type="checkbox" name="traceRouteCheckbox">&nbsp;TraceRoute诊断</td>
				</tr>
				<tr bgcolor=#ffffff>
					<td class=column align="right" nowrap>测试地址:&nbsp;</td>
					<td >
						<input type="text" name="tracerout_Host" class="bk">
					</td>
					<td class=column align="right" nowrap>最大跳转数:&nbsp;</td>
					<td>
						<input type="text" name="tracerout_MaxHopCount" class="bk" value="30" />
					</td>
				</tr>
				<tr bgcolor=#ffffff>
					<td class=column align="right" nowrap>等待时间(ms):&nbsp;</td>
					<td>
						<input type="text" name="tracerout_Timeout" class="bk" value="5000" />
					</td>
					<td colspan=2>
				</tr>
				<tr>
					<td colspan="4" bgcolor=#ffffff height="18">
						<div id="tracerout_result"></div>
					</td>
				</tr>
			
				<!-- DNS QUERY -->
				<tr CLASS="green_foot">
					<td colspan="4">&nbsp;&nbsp;<input type="checkbox" name="dnsQueryCheckbox">&nbsp;DNSQuery诊断</td>
				</tr>
				<tr bgcolor=#ffffff>
					<td class=column align="right" nowrap>诊断IP:&nbsp;</td>
					<td >
						<input type="text" name="DNSServerIP" id="DNSServerIP" class="bk">
					</td>
					<td class=column align="right" >域名:&nbsp;</td>
					<td>
						<input type="text" name="DomainName" id="DomainName"class="bk">
					</td>
				</tr>
				<tr bgcolor=#ffffff>
					<td class=column align="right">测试次数:&nbsp;</td>
					<td>
						<input type="text" name="dnsquery_NumberOfRepetitions" class="bk" size="16" value="5">
					</td>
					<td class=column align="right" nowrap>等待时间(ms):&nbsp;</td>
					<td>
						<input type="text" name="dnsquery_Timeout" class="bk" size="16" value="5000">
					</td>
				</tr>
				<tr>
					<td colspan="4" bgcolor=#ffffff height="18">
						<div id="dnsquery_result"></div>
					</td>
				</tr>
				<tr bgcolor=#ffffff>
					<td align="right" colspan="4">
						<button onclick="javascript:doDiag();" >开始诊断</button>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</form>
</body>
<script>
function open1()
{
	var strpage="<s:url value='/bbms/report/SyslogQuery.jsp'/>";
	window.open(strpage,"","left=20,top=20,width=600,height=450,resizable=yes,scrollbars=yes");
}

function open2()
{
	var strpage="<s:url value='/diagnostic/jt_device_zendan_from3.jsp?gw_type=2'/>";
	window.open(strpage,"","left=20,top=20,width=600,height=450,resizable=yes,scrollbars=yes");
}

function open3()
{
	var strpage="<s:url value='/paramConfig/uploadConfigFile.jsp?gw_type=2'/>";
	window.open(strpage,"","left=20,top=20,width=600,height=450,resizable=yes,scrollbars=yes");
}

function open4()
{
	var strpage="<s:url value='/paramConfig/device_configrecover.jsp?gw_type=2'/>";
	window.open(strpage,"","left=20,top=20,width=600,height=450,resizable=yes,scrollbars=yes");
}

function open5()
{
	var strpage="<s:url value='/Warn/NetWarnQuery.action?gw_type=2'/>";
	window.open(strpage,"","left=20,top=20,width=600,height=450,resizable=yes,scrollbars=yes");
}

function open6()
{
	var strpage="<s:url value='/Resource/AdvancedDeviceInfo.jsp?gw_type=2'/>";
	window.open(strpage,"","left=20,top=20,width=600,height=450,resizable=yes,scrollbars=yes");
}
</script>
</html>