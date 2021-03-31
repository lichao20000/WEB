<%@ include file="../../../timelater.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<script type="text/javascript" src="../../../Js/My97DatePicker/WdatePicker.js"></script>
<script language="JavaScript">
<%
	String userName = (String)request.getParameter("userName");
	String mac = (String)request.getParameter("mac");
	String remark = (String)request.getParameter("remark");
	String opr = (String)request.getParameter("opr");
%>
function doQuery(){
    var macEdit = $.trim($("input[@name='mac']").val());          
    var userNameEdit = $.trim($("input[@name='userName']").val());  
    var remarkEdit = $.trim($("input[@name='remark']").val()); 
    var userName = '<%=userName%>';
    var mac = '<%=mac%>';
    var opr = '<%=opr%>';
    if(userNameEdit == userName)
    {
    	userNameEdit = '';
    }
    if(macEdit == mac)
    {
    	macEdit = '';
    }
    
    var re = /^[0-9A-F][0-9A-F]*$/;
	if(macEdit != '' && !re.test(macEdit))
	{
		alert("mac不合法");
		return false;
	}
    /* $("tr[@id='trData']").show();
    $("div[@id='QueryData']").html("正在操作，请稍等...."); */
    var url = '<s:url value='/gtms/stb/resource/stbBindProtect!update.action'/>'; 
	$.post(url,{
		mac:mac,
		userName:userName,
		macEdit:macEdit,
		userNameEdit:userNameEdit,
		remarkEdit:encodeURI(remarkEdit),
		opr:opr
	},function(ajax){
		if(1 == ajax)
		{
			alert("编辑成功");
			window.opener=null;
			window.open('','_self');
			window.close();
		}
		else
		{
			alert("业务账号或机顶盒MAC已经存在记录，请核对后再进行录入！");
		}
	    /* $("div[@id='QueryData']").html("");
		$("div[@id='QueryData']").append(ajax);  */
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
						账号与机顶盒绑定关系保护编辑
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
							账号与机顶盒绑定关系保护编辑
						</th>
					</tr>
					<tr bgcolor=#ffffff>
						<td class="column" width='15%' align="right">业务账号：</td>
						<td width='35%' align="left">
						<input name="userName" type="text" class='bk' value="<%=userName%>">
						</td>
						<td class="column" width='15%' align="right">设备MAC：</td>
						<td width='35%' align="left">
						<input name="mac" type="text" class='bk' value="<%=mac%>"> 
						<font color="red">只能A-F字母或者数字组成,样式：112233AABBCC</font>
						</td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class="column" width='15%' align="right">备注：</td>
						<td width='35%' align="left">
						<input name="remark" type="text" class='bk' value="<%=remark%>">
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

