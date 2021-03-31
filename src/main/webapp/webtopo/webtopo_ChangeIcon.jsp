<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.common.util.FormUtil" %>
<%
request.setCharacterEncoding("GBK");
String serial = request.getParameter("serial");
String sql = "select device_model from gw_device_model where device_model_id='" + serial + "'";
Map record = DataSetBean.getRecord(sql);
String device_model = record == null ? "" : (String)record.get("device_model");
String icon_name = request.getParameter("icon");
icon_name = icon_name == null ? "" : icon_name;
%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
	var PIC = [];
	PIC[PIC.length]="egw.png";
	PIC[PIC.length]="laptop.png";
	PIC[PIC.length]="ap_icon.png";

	var rootPath = "images/";
	var devicePic = "<%=icon_name%>";
	function showChild(objName){
		var obj = document.all(objName);
		if(obj.value == "-1"){
			return ;
		}
		document.all("imgPic").src = rootPath + obj.value;
	}
	window.onload=function(){
		var selObj = document.all("iconSelect");
		var optionObj = null;
		var len = 0;
		for(var i = 0;i<PIC.length; i++){
			len = selObj.length;
			optionObj = new Option(PIC[i],PIC[i]);
			selObj.options[len] = optionObj;
		}
		selObj.value = devicePic;
		document.all("imgPic").src = rootPath + devicePic;
	};
	function success(flag){
		if(flag) alert("修改成功!");
		else alert("修改失败!");
		if(opener.refreshTopo){opener.refreshTopo();}
		window.close();
	}
	function submitForm(){
		var obj = document.all("iconSelect");
		if(obj.value == "-1"){
			alert("请选择设备图标!");
			return ;
		}
	}
//-->
</SCRIPT>
<%@ include file="../toolbar.jsp"%>
<br>
	<form action="webtopo_ChangeSave.jsp" onsubmit="return submitForm()" method="post" target="childFrm">
	<input name="serial" value="<%=serial%>" type="hidden">
	<TABLE width="98%" border=0 cellspacing=1 cellpadding=2 align="center" align="center" bgcolor="#000000">
		<tr bgcolor=#ffffff align="center">
			<th colspan=2>修改设备图标</th>
		</tr>
		<tr bgcolor=#ffffff>
			<td align="right">设备型号</td>
			<td><%=device_model%></td>
		</tr>
		<tr bgcolor=#ffffff valign=middle>
			<td align="right">设备图标</td>
			<td  nowrap>
				<select id="iconSelect" name="iconSelect" onchange="showChild('iconSelect')">
					
				</select>	
				<img src="" id="imgPic"/>
			</td>
		</tr>
		<tr>
			<td colspan=2 align="center" bgcolor=#ffffff>
				<input name="btn" value="保存" type="submit">
			</td>
		</tr>
	</table>
	</form>
	<iframe name="childFrm" style="display:none"></iframe>