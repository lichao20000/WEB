<%@ include file="../../../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<script type="text/javascript" src="../../../Js/My97DatePicker/WdatePicker.js"></script>
<script language="JavaScript">
<%
	String opr = (String)request.getParameter("opr");
%>
function doQuery(){
    var mac = $.trim($("input[@name='mac']").val());           
    var userName = $.trim($("input[@name='userName']").val()); 
    var remark = $.trim($("input[@name='remark']").val());
    var opr = '<%=opr%>' ;
    
  	//2个参数至少一个
	if(mac.length == 0 || userName.length == 0)
	{
		alert("设备MAC,业务账号均不能为空");
		document.frm.mac.focus();
		return false;
	}
	
	var re = /^[0-9A-F][0-9A-F]*$/;
	if(!re.test(mac))
	{
		alert("mac不合法");
		return false;
	}
	
    var url = '<s:url value='/gtms/stb/resource/stbBindProtect!add.action'/>'; 
	$.post(url,{
		mac:mac,
		userName:userName,
		opr:opr,
		remark:encodeURI(remark)
	},function(ajax){
		if(1 == ajax)
		{
			alert("添加记录成功");
		}
		else
		{
			alert("业务账号或机顶盒MAC已经存在记录，请核对后再进行录入！");
		}
		window.location.reload();
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
						绑定保护关系增加
					</th>
					<td>
						<img src="<s:url value="/images/attention_2.gif"/>" width="15" height="12">
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
							绑定保护关系增加
						</th>
					</tr>
					<tr bgcolor=#ffffff>
						<td class="column" width='15%' align="right">业务账号：</td>
						<td width='35%' align="left">
						<input name="userName" type="text" class='bk' value="">
						<font color="red">*</font>
						</td>
						<td class="column" width='5%' align="right">设备MAC：</td>
						<td width='45%' align="left">
						<input name="mac" type="text" class='bk' value=""> 
						<font color="red">* 只能A-F字母或者数字组成,样式：112233AABBCC</font>
						</td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class="column" width='15%' align="right">备注：</td>
						<td width='35%' align="left">
						<input name="remark" type="text"  class='bk' value="">
						</td>
						<td class="column" width='15%' align="right"></td>
						<td width='35%' align="left">
							<input name="acc_oid" type="hidden" class='bk' value=""> 
						</td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=foot colspan=4 align=right>
							<button onclick="doQuery()">
								&nbsp;提交&nbsp;
							</button>
						</td>
					</tr>
				</table>
			</form>
		</td>
	</tr>

	<tr id="trData" style="display: none">
		<td class="colum">
			<div id="QueryData" style="width: 100%; z-index: 1; top: 100px">
				正在统计，请稍等....
			</div>
		</td>
	</tr>
	<tr>
		<td height="20">
		</td>
	</tr>
</TABLE>

<%@ include file="/foot.jsp"%>

