<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<script type="text/javascript"
	src="<s:url value="/Js/jsDate/WdatePicker.js"/>"></script>

<script language="JavaScript">


function doQuery(){
    var username = $.trim($("input[@name='username']").val());
    var devSn = $.trim($("input[@name='devSn']").val());
    if(username.length<1){
		alert("请输入用户账号！");
		$("input[@name='username']").focus();
		return false;
	}
    if(devSn.length<8){
		alert("请至少输入最后8位设备序列号进行查询！");
		$("input[@name='devSn']").focus();
		return false;
	}
    $("tr[@id='trData']").show();
    $("div[@id='QueryData']").html("正在查询，请稍等....");
    var url = '<s:url value='/gwms/config/bridge2Route.action'/>'; 
	$.post(url,{
		username:username,
		devSn:devSn
	},function(ajax){	
	    $("div[@id='QueryData']").html("");
		$("div[@id='QueryData']").append(ajax);
	});
}

function edit(devSn,username){
    var password = $.trim($("input[@name='password']").val());
    if(password.length<1){
		alert("请输入用户密码！");
		$("input[@name='password']").focus();
		return false;
	}
	$("button[@name='edit']").attr("disabled", true); 
    var url = '<s:url value='/gwms/config/bridge2Route!edit.action'/>'; 
	$.post(url,{
		username:username,
		devSn:devSn,
		password:password
	},function(ajax){	
	    alert(ajax);
	});
}

function RoutedQuery(devSn,username){
	$("div[@id='div_query']").html("");
	$("div[@id='div_query']").css("display","");
    var url = '<s:url value='/gwms/config/bridge2Route!routeQuery.action'/>'; 
	$.post(url,{
		username:username,
		devSn:devSn
	},function(ajax){	
	    $("div[@id='div_query']").html("<font color='green' size='3'>下发情况："+ajax+"</font>");
	});
}

</script>

<br>
<TABLE>
	<tr>
		<td>
			<table class="green_gargtd">
				<tr>
					<th>
						用户路由开通
					</th>
					<td>
						<img src="<s:url value="/images/attention_2.gif"/>" width="15"
							height="12">

					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<form name=frm>
				<table class="querytable">
					<tr>
						<th colspan=4>
							用户路由开通
					</tr>

					<tr bgcolor=#ffffff>
						<td class=column width="15%">
							用户账号：
						</td>
						<td width="35%">
							<input type="text" name="username" class='bk'
								value="<s:property value='username'/>">
								<font color="red">*</font>
						</td>
						<td class=column width="15%">
							设备序列号：
						</td>
						<td width="35%">
							<input type="text" name="devSn" class='bk'
								value="<s:property value='devSn'/>">
								<font color="red">* 最少后8位</font>
						</td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=foot colspan=4 align=right>
							<button onclick="doQuery()">
								&nbsp;路由下发支持情况查询&nbsp;
							</button>
							&nbsp;&nbsp;&nbsp;
						</td>
					</tr>
				</table>
			</form>
		</td>
	</tr>

	<tr id="trData" style="display: none">
		<td class="colum">
			<div id="QueryData" style="width: 100%; z-index: 1; top: 100px">
				正在查询，请稍等....
			</div>
		</td>
	</tr>
	<tr>
		<td height="20">
		</td>
	</tr>
</TABLE>

<%@ include file="/foot.jsp"%>
