<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<link href="<s:url value="/css3/css_s.css"/>" rel="stylesheet"
	type="text/css" />
<meta http-equiv="x-ua-compatible" content="IE=7" >
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/slide.js"/>"></script>
<script type="text/javascript"
	src="../../Js/My97DatePicker/WdatePicker.js"></script>
<title>e8-c规范版本查询</title>
<script language="JavaScript">
var operType = <%=request.getParameter("operType")%>;
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

function getDeviceModel(oselect){
	var url = "<s:url value='/itms/resource/E8cVersionQuery!getDevModelSelectList.action'/>";
	var vendor_id = oselect.value;
	$.post(url,{
		vendor_id:vendor_id
	},function(ajax){
		parseMessageModel(ajax);
	});
	
	//解析查询设备型号
	function parseMessageModel(ajax){
		var devModelObj = $("#selectDevModel");
		var str = '';
		devModelObj.html("");
		str = "<option value=\"-1\" selected>==请选择型号==</option>";
		str += ajax;
		devModelObj.append(str);
	}
}

function doQuery(){
    $("#data").show();
    $("#QueryData").html("正在查询，请稍等....");
	$("input[@name='button']").attr("disabled", true);
	$("input[@name='button']").css("backgroundColor","#AAAAAA");
	var form = document.getElementById("frm");
	form.action ="<s:url value='/itms/resource/E8cVersionQuery!getE8cVersionList.action'/>?operType="+operType;
	form.submit();
}

function closeDialog(){
	$("#divData").hide();
	$("#QueryData").hide();
	$("#wall").hide();
}
</script>
</head>
<body>
	<form name="frm" id="frm" method="post" target="dataForm">
		<div class="it_main">
			<table width="100%" border="0" cellspacing="10" cellpadding="0" class="mainSearch" bgcolor="#f2f5ff">
				<tr>
				    <td class="tit">设备厂商:</td>
				    <td>
			      		<s:select list="vendorList" name="vendor_id" headerKey="-1" 
								headerValue="请选择厂商" listKey="vendor_id" listValue="vendorName" 
								value="vendor_id" onchange="getDeviceModel(this);" theme="simple" cssClass="gj_select"/>
				    </td>
				    <td class="tit">设备型号:</td>
				    <td>
				    	<select id="selectDevModel" name="device_model" class="gj_select">
							<option value="-1">==先选择厂商==</option>
						</select>
				    </td>
				    <td class="tit">设备类型:</td>
				    <td>
				    	<s:select list="devTypeMap" name="device_type"
							headerKey="-1" headerValue="请选择设备类型" listKey="type_id"
							listValue="type_name" cssClass="gj_select"></s:select>
				    </td>
				  </tr>
				  <tr>
				    <td class="tit">终端规格:</td>
				    <td>
				    	<s:select list="specList" name="spec_id" headerKey="-1"
							headerValue="请选择终端规格" listKey="id" listValue="spec_name"
							value="spec_id" cssClass="bk"></s:select>
					</td>
				    <td class="tit">开始时间:</td>
				    <td>
				    	<input type="text" name="starttime" readonly value="<s:property value='starttime'/>">
							<img name="shortDateimg" onClick="WdatePicker({el:document.frm.starttime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../images/dateButton.png" width="15" height="12" border="0" alt="选择" /> 
				    </td>
				    <td class="tit">结束时间:</td>
				    <td>
				    	<input type="text" name="endtime" readonly value="<s:property value='endtime'/>"> 
							<img name="shortDateimg" onClick="WdatePicker({el:document.frm.endtime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../images/dateButton.png" width="15" height="12" border="0" alt="选择" /> 
				    </td>
				 </tr>
				 <tr>
			</table>
			 <div class="mainSearch_btn" style="background-color: #d2dff8">
				 <input name="button" type="button" onclick="doQuery();" class="it_btn" value="查询" />
			</div>
			<div id="data" style="display: none">
				<div id="QueryData">
					正在查询，请稍等....
				</div>
			</div>
			<div style="height: 20px;">
			</div>
			<div class="it_table">
				<iframe id="dataForm" name="dataForm" height="0" frameborder="0" scrolling="no" width="100%" src=""></iframe>
			</div>
			<div id="divData" style="display: none;position: absolute;z-index: 1200">
				<div id="QueryData">
				</div>
			</div>
		</div>
		<div id="wall" class="wall"></div>
	</form>
</body>
</html>