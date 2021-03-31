<%--
故障诊断-高级查询-ONU
--%>

<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../head.jsp"%>

<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value='/Js/jquery.js'/>"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value='/Js/CheckForm.js'/>"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">

var gw_type = '<s:property value="gw_type"/>';
var oldDns = '<s:property value="wanBussObj.dns"/>';

$(function(){
	init();
});
//get data
function init() {
	
	parent.unblock();
	document.all("operResult").style.display = ""
	$("div[@id='operResult']").html("");
	$("div[@id='operResult']").html("数据采集:");
	$("div[@id='operResult']").append("<s:property value='result'/>");
	setTimeout("clearResult()", 3000);
}

//config.
function CheckPost() {
	var deviceId = $("input[@name='deviceId']");
	var dns = $("input[@name='dns']");
	
	if(dns.val() != ""){
		var arrDns = dns.val().split(",");
		var re = /^([0-9]|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.([0-9]|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.([0-9]|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.([0-9]|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])$/;  
		for(var i=0;i<arrDns.length;i++){			
			if(!re.test(arrDns[i])){  
				alert("dns格式不正确!");  
			    return false;  
			}  
		}
	}	
	parent.block();
	var url = "<s:url value='/gwms/diagnostics/dnsModifyACT!config.action'/>";
	$.post(url,{
		deviceId:deviceId.val(),
		dns:dns.val(),
		gw_type:gw_type,
		oldDns:oldDns
	},function(ajax){
		document.all("tr002").style.display = ""
		$("td[@id='td_save_result']").html(ajax);
		parent.dyniframesize();
        parent.unblock();
	});
	setTimeout("clearResult()", 3000);
}

function clearResult() {
	$("div[@id='operResult']").html("");
	document.all("operResult").style.display = "none"
}
</SCRIPT>

<BODY>

<FORM NAME="frm" METHOD="post">
<TABLE style="width: 100%;"  border=0 cellspacing=0 cellpadding=0>
	<tr bgcolor=#ffffff>
		<td align=right>
			<div id="operResult" style='width:20%;display:none;background-color:#33CC00'></div>
		</td>
	</tr>
	<TR>
		<TD bgcolor=#999999>
			<TABLE style="width: 100%;" border=0 cellspacing=1 cellpadding=2>
				<s:if test="null == wanBussObj">
					<TR bgcolor="#FFFFFF" STYLE="display:">
						<TD width="15%" class="column" align="left" colspan="4">该设备暂无上网参数信息！</TD>					
					</TR>
				</s:if>
				<s:else>
					<TR bgcolor="#FFFFFF" >
						<TD class="column" align="right">宽带账号:</TD>
							<TD align="left">
							<s:if test="wanBussObj.username != 'null'">
							<s:property value="wanBussObj.username"/>
							</s:if>
							</TD>
						<TD class="column" align="right">密码:</TD>
							<TD align="left">******</TD>					
					</TR>
					<TR bgcolor="#FFFFFF" STYLE="display:">
						<TD class="column" align="right">业务类型:</TD>
						<s:if test="wanBussObj.servTypeId==11">
							<TD align="left">IPTV</TD>
						</s:if>
						<s:elseif test="wanBussObj.servTypeId==14">
				            <TD align="left">VOIP</TD>
				        </s:elseif>
						 <s:else>
				            <TD align="left">上网</TD>
				        </s:else>
						<TD class="column" align="right">连接类型:</TD>
						<s:if test="wanBussObj.wanType==4">
							<TD align="left">DHCP</TD>
						</s:if>
						<s:elseif test="wanBussObj.wanType==3">
				            <TD align="left">静态IP</TD>
				        </s:elseif>
				        <s:elseif test="wanBussObj.wanType==2">
				            <TD align="left">路由</TD>
				        </s:elseif>
						 <s:else>
				            <TD align="left">桥接</TD>
				        </s:else>				        					
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class="column" align="right"  width="15%">PVC/VLAN配置:</TD>
						<s:if test="wanBussObj.vlanid=='null' || wanBussObj.vlanid=='0'">
							<TD align="left" colspan="3">PVC:<s:property value="wanBussObj.vpiid"/>/<s:property value="wanBussObj.vciid"/></TD>
						</s:if>
						 <s:else>
				            <TD align="left" colspan="3"><s:property value="wanBussObj.vlanid"/></TD>
				        </s:else>				        
					</TR>					
					<TR bgcolor="#FFFFFF">						
						<TD class="column" align="right">DNS:</TD>
						<TD align="left" colspan="3">
						<input type="text" name="dns" value="<s:property value="wanBussObj.dns"/>"/>
						</TD>
					</TR>					
					<TR bgcolor="#FFFFFF">
						<TD class="column" align="right">绑定端口:</TD>
						 <TD align="left" colspan="3">
						 <s:if test="wanBussObj.bindPort != 'null,null,null,null'">
							<s:property value="wanBussObj.bindPort"/>
						</s:if>
						 </TD>							
					</TR>
					<TR bgcolor="#FFFFFF">
					<TD colspan="4" class="green_foot" align=right>
					<INPUT NAME="deviceId" TYPE="hidden"  value="<s:property value='deviceId'/>">
					<input id="button_save" type="button" value=" 保 存 " class="jianbian" onclick="javascript:CheckPost();"></TD>
				    </TR>
				</s:else>
				<TR bgcolor="#FFFFFF" id="tr002" style="display: none">
					<td align="center" colspan="2">
					操作结果
					</td>
					<td align="center" id="td_save_result" colspan="2">
					</td>
				</TR>
			</TABLE>
		</TD>
	</TR>
</TABLE>
</FORM>
</BODY>
