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
		alert("�������û��˺ţ�");
		$("input[@name='username']").focus();
		return false;
	}
    if(devSn.length<8){
		alert("�������������8λ�豸���кŽ��в�ѯ��");
		$("input[@name='devSn']").focus();
		return false;
	}
    $("tr[@id='trData']").show();
    $("div[@id='QueryData']").html("���ڲ�ѯ�����Ե�....");
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
		alert("�������û����룡");
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
	    $("div[@id='div_query']").html("<font color='green' size='3'>�·������"+ajax+"</font>");
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
						�û�·�ɿ�ͨ
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
							�û�·�ɿ�ͨ
					</tr>

					<tr bgcolor=#ffffff>
						<td class=column width="15%">
							�û��˺ţ�
						</td>
						<td width="35%">
							<input type="text" name="username" class='bk'
								value="<s:property value='username'/>">
								<font color="red">*</font>
						</td>
						<td class=column width="15%">
							�豸���кţ�
						</td>
						<td width="35%">
							<input type="text" name="devSn" class='bk'
								value="<s:property value='devSn'/>">
								<font color="red">* ���ٺ�8λ</font>
						</td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=foot colspan=4 align=right>
							<button onclick="doQuery()">
								&nbsp;·���·�֧�������ѯ&nbsp;
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
				���ڲ�ѯ�����Ե�....
			</div>
		</td>
	</tr>
	<tr>
		<td height="20">
		</td>
	</tr>
</TABLE>

<%@ include file="/foot.jsp"%>
