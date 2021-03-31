<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<script type="text/javascript"
	src="../../Js/My97DatePicker/WdatePicker.js"></script>

<script language="JavaScript">

//** iframe自动适应页面 **//
//输入你希望根据页面高度自动调整高度的iframe的名称的列表
//用逗号把每个iframe的ID分隔. 例如: ["myframe1", "myframe2"]，可以只有一个窗体，则不用逗号。
//定义iframe的ID
var iframeids=["dataForm"]

//如果用户的浏览器不支持iframe是否将iframe隐藏 yes 表示隐藏，no表示不隐藏
var iframehide="yes"

function dyniframesize() 
{
	var dyniframe=new Array()
	for (i=0; i<iframeids.length; i++)
	{
		if (document.getElementById)
		{
			//自动调整iframe高度
			dyniframe[dyniframe.length] = document.getElementById(iframeids[i]);
			if (dyniframe[i] && !window.opera)
			{
   			dyniframe[i].style.display="block"
   			//如果用户的浏览器是NetScape
   			if (dyniframe[i].contentDocument && dyniframe[i].contentDocument.body.offsetHeight)
    				dyniframe[i].height = dyniframe[i].contentDocument.body.offsetHeight; 
    			//如果用户的浏览器是IE
   			else if (dyniframe[i].Document && dyniframe[i].Document.body.scrollHeight) 
    				dyniframe[i].height = dyniframe[i].Document.body.scrollHeight;
 			 }
 		}
		//根据设定的参数来处理不支持iframe的浏览器的显示问题
		if ((document.all || document.getElementById) && iframehide=="no")
		{
			var tempobj=document.all? document.all[iframeids[i]] : document.getElementById(iframeids[i])
  		tempobj.style.display="block"
		}
	}
}


$(function(){
	dyniframesize();
});

$(window).resize(function(){
	dyniframesize();
}); 

function doQuery(){
	$("button[@name='button']").attr("disabled", true);
	var deviceSerialnumber = $.trim(document.frm.deviceSerialnumber.value);
	if (deviceSerialnumber != "") {
		if (deviceSerialnumber.length < 6) {
			alert("请输入至少最后6位设备序列号 !");
			document.frm.deviceSerialnumber.focus();
			return false;
		}
	}
	var frm = document.getElementById("frm");
	frm.action="<s:url value='/itms/resource/queryVlanConfig!queryVlanConfigList.action'/>";
	document.frm.submit();
	}
	
function exportList() {
	var frm = document.getElementById("frm");
	frm.action = "<s:url value='/itms/resource/queryVlanConfig!exportVlanConfigList.action'/>";
	frm.submit();
}

</script>

<br>
<table>
	<tr>
		<td>
			<table class="green_gargtd">
				<tr>
					<th>家庭网关配置查询</th>
					<td align="left"><img src="<s:url value="/images/attention_2.gif"/>"
						width="15" height="12">家庭网关端口VLAN配置情况查询。</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<form name="frm" id="frm" method="post"  target="dataForm">
				<table class="querytable">
					<tr>
						<th colspan=4>家庭网关端口VLAN配置情况查询</th>
					</tr>
					<tr bgcolor="#ffffff">
							<td class="column" align="center" width="15%">
								<select name="selectType">
									<option value="0">逻辑ID</option>
									<option value="1">宽带账号</option>
								</select>
							</td>
							<td>
								<input type="text" name="username" />
							</td>
							<td class="column" align="center" width="15%">
								设备序列号
							</td>
							<td>
								<input type="text" name="deviceSerialnumber" />
							</td>
						</tr>
						
						<tr bgcolor="#ffffff">
						<td class="column" align="center" width="15%">
								属地
							</td>
							<td>
								<s:select list="cityList" name="cityId" headerKey="-1"
										headerValue="请选择属地" listKey="city_id" listValue="city_name"
										value="cityId" cssClass="bk"></s:select>
							</td>
							<td class="column" align="center" width="15%">
								是否查询异常接口
							</td>
							<td>
								<select name="IsErrPort">
									<option value="0">全部</option>
									<option value="1">是</option>
									<option value="-1">否</option>
								</select>
							</td>
						</tr>
						<tr bgcolor=#ffffff>
							<td class="foot" colspan="4" align="right">
							<button onclick="exportList();" name="button">&nbsp;导出&nbsp;</button>&nbsp;&nbsp;
								<button onclick="doQuery();" name="button">&nbsp;查询&nbsp;</button>
							</td>
						</tr>
				</table>
			</form>
		</td>
	</tr>
	<tr>
		<td bgcolor=#999999 id="idData">
			<iframe id="dataForm" name="dataForm" height="0" frameborder="0" scrolling="no" width="100%" src=""/>
		</td>
	</tr>
	
</table>

<%@ include file="/foot.jsp"%>

