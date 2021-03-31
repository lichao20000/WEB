<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@ include file="../head.jsp"%>
<%@ include file="../../toolbar.jsp"%>

<link rel="stylesheet" href="<s:url value="/css/css_blue.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css/css_green.css"/>" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>

<script type="text/javascript">

var _gw_type = "<%=request.getParameter("gw_type")%>";

$(function(){
	$("input[@name='gwShare_jiadan']").css("display","none");
	$("input[@name='gwShare_gaoji']").css("display","none");
	$("input[@name='gwShare_import']").css("display","none");
	$("input[@name='gwShare_up_import']").css("display","none");
	
	gwShare_setGaoji();
});

function deviceResult(returnVal){
	
	var _deviceId = returnVal[2][0][0]; 
	$("input[@name='deviceId']").val(returnVal[2][0][0]);
	
	$("tr[@id='tr_deviceInfo']").show();
    $("div[@id='div_deviceInfo']").html("正在查询，请稍等....");
    var url = "<s:url value='/gwms/resource/queryDevice!deviceOperateByDeviceId.action'/>"; 
	$.post(url,{
		deviceId:_deviceId,
		gw_type:_gw_type
	},function(ajax){	
	    $("div[@id='div_deviceInfo']").html("");
		$("div[@id='div_deviceInfo']").append(ajax);
	});
}
</script>

<table width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
	<tr>
		<td height="20"></td>
	</tr>
	<TR>
		<TD>
		<TABLE width="100%" height="30" border="0" cellspacing="0"
			cellpadding="0" class="green_gargtd">
			<TR>
				<TD width="164" align="center" class="title_bigwhite">设备资源</TD>
				<td>&nbsp; <img src="<s:url value="/images/attention_2.gif"/>"
					width="15" height="12"> &nbsp; 设备操作列表,选择时间类型确定所要查询的时间。</td>
			</TR>
		</TABLE>
		</TD>
	</TR>
	<tr>
		<td colspan="4">
			<%@ include file="/gwms/share/gwShareDeviceQuery.jsp"%>
		</td>
	</TR>
	<tr id="tr_deviceInfo" style="display: none">
		<TD>
			<div id="div_deviceInfo" style="width: 100%; z-index: 1; top: 100px">
				正在查询，请稍等....
			</div>
		</td>
	</tr>
</table>

<%@ include file="../foot.jsp"%>
