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
	var con = $.trim($("select[@name='con']").val());
    var condition = $.trim($("input[@name='condition']").val());
//	var starttime = $.trim($("input[@name='starttime']").val());
//	var endtime = $.trim($("input[@name='endtime']").val());
//	var openState = $.trim($("select[@name='openState']").val());
//	var type = $.trim($("input[@name='type']").val());
	var awifi_type ='<s:property value="awifi_type" />';
	if(condition == null || condition == ""){
    	alert("带星号的为必填项！");
   	    return false;
	}
	if(con=="1" && condition.length < 6){
		 alert("设备序列号的有效字符长度至少为6！");
    	 return false;
	}
    $("tr[@id='trData']").show();
    $("div[@id='QueryData']").html("");
	$("button[@name='button']").attr("disabled", true);
	var url="<s:url value='/itms/report/strategyQuery!getCountAll.action'/>";
	document.frm.submit();
	
}

</script>

<br>
<table>
	<tr>
		<td>
			<table class="green_gargtd">
				<tr>
					<s:if test="awifi_type==1">
						<th>awifi业务策略查询</th>
					</s:if>
					<s:elseif test="awifi_type==2">
						<th>校园网无线业务策略查询</th>
					</s:elseif>
					<s:elseif test="awifi_type==3">
						<th>无线专线业务策略查询</th>
					</s:elseif>
					<td><img src="<s:url value="/images/attention_2.gif"/>"
						width="15" height="12"></td>
					<td>查询无线业务开通、关闭策略情况。开始时间、结束时间为定制时间。</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<form name="frm" id="frm" method="post" action="<s:url value='/itms/report/strategyQuery!getCountAll.action'/>" target="dataForm">
				<input type='hidden' name="awifi_type" value="<s:property value='awifi_type'/>" />
				<table class="querytable">
					<tr>
						<s:if test="awifi_type==1">
							<th colspan=4>awifi业务策略查询</th>
						</s:if>
						<s:elseif test="awifi_type==2">
							<th colspan=4>校园网无线业务策略查询</th>
						</s:elseif>
						<s:elseif test="awifi_type==3">
							<th colspan=4>无线专线业务策略查询</th>
						</s:elseif>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=column align=center width="25%">
							<select name="con" class=column>
								<option value="1">设备序列号</option>
								<option value="0">LOID</option>
								<option value="-1">宽带账号</option>
							</select>
						</td>
						<td align=center width="25%">
							<input type="text" name="condition" class='bk' /> 
							<font color="red">*</font>
						</td>
						<td class=column  width="25%">
							开通状态
						</td>
						<td bgcolor=#eeeeee>
							<select name="openState" class=column>
								<option value="2">请选择</option>
								<option value="1">成功</option>
								<option value="0">失败</option>
								<option value="3">未做</option>
							</select>
						</td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=column align=center width="25%">
							开通时间
						</td>
						<td>
							<input type="text" name="starttime" class='bk' readonly value="<s:property value='starttime'/>">
							<img name="shortDateimg" onClick="WdatePicker({el:document.frm.starttime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../images/dateButton.png" width="15" height="12" border="0" alt="选择" /> 
							<font color="red">*</font>
						</td>
						<td class=column align=center width="15%">
							结束时间
						</td>
						<td><input type="text" name="endtime" class='bk' readonly value="<s:property value='endtime'/>"> 
							<img name="shortDateimg" onClick="WdatePicker({el:document.frm.endtime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../images/dateButton.png" width="15" height="12" border="0" alt="选择" /> 
							<font color="red">*</font>
						</td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=foot colspan=4>
							<button onclick="doQuery()" name="button" id="button">&nbsp;查询&nbsp;</button>
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

