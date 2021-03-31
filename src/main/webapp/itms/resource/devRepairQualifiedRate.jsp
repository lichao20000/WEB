<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<script type="text/javascript">
	function doQuery(){
		$("#toExcel").show();
		var barScript = document.createElement("script");   
		barScript.type = "text/javascript"; 
		var searchType = $.trim($("input[@name='searchType']").val());
		if(searchType == 1){
			barScript.src = "../../Js/echarts/barQualifiedRateByVendor.js";
		}else{
			barScript.src = "../../Js/echarts/barQualifiedRateByCity.js";
		}
		document.body.appendChild(barScript); 
		var pieScript = document.createElement("script");   
		pieScript.type = "text/javascript";   
		pieScript.src = "../../Js/echarts/pieQualifiedRate.js";   
		document.body.appendChild(pieScript);
	}
	function goBack(){
		var form = document.getElementById("frm");
		form.action = "<s:url value='/itms/resource/devRepairTestInfo!qualifiedReload.action'/>";
		form.submit();
	}
	var searchType = $.trim($("input[@name='searchType']").val());
	var repair_vendor_id = "";
	var cityId = "";
	if(searchType == 1){
		repair_vendor_id = $.trim($("select[@name='repair_vendor_id']").val());
	}else{
		cityId = $.trim($("select[@name='cityId']").val());
	}
	function qualifiedRateToExcel(){
		var starttime = $.trim($("input[@name='starttime']").val());
	    var endtime = $.trim($("input[@name='endtime']").val());
	    var batchNum = $.trim($("select[@name='batchNum']").val());
		if(searchType == 1){
			repair_vendor_id = $.trim($("select[@name='repair_vendor_id']").val());
			page = "<s:url value='/itms/resource/devRepairTestInfo!qualifiedRateToExcel.action'/>?"
		    	+ "repair_vendor_id=" + repair_vendor_id
		    	+ "&starttime=" + starttime
				+ "&endtime=" + endtime
				+ "&batchNum=" + batchNum
				+ "&searchType=" + searchType;
		}else{
			cityId = $.trim($("select[@name='cityId']").val());
			page = "<s:url value='/itms/resource/devRepairTestInfo!qualifiedRateToExcel.action'/>?"
		    	+ "cityId=" + cityId
		    	+ "&starttime=" + starttime
				+ "&endtime=" + endtime
				+ "&batchNum=" + batchNum
				+ "&searchType=" + searchType;
		}
		document.all("childFrm").src=page;	
	}
	function noQualifiedToExcel(){
		var starttime = $.trim($("input[@name='starttime']").val());
	    var endtime = $.trim($("input[@name='endtime']").val());
	    var batchNum = $.trim($("select[@name='batchNum']").val());
		if(searchType == 1){
			repair_vendor_id = $.trim($("select[@name='repair_vendor_id']").val());
			page = "<s:url value='/itms/resource/devRepairTestInfo!noQualifiedToExcel.action'/>?"
		    	+ "repair_vendor_id=" + repair_vendor_id
		    	+ "&starttime=" + starttime
				+ "&endtime=" + endtime
				+ "&batchNum=" + batchNum
				+ "&searchType=" + searchType;
		}else{
			cityId = $.trim($("select[@name='cityId']").val());
			page = "<s:url value='/itms/resource/devRepairTestInfo!noQualifiedToExcel.action'/>?"
		    	+ "cityId=" + cityId
		    	+ "&starttime=" + starttime
				+ "&endtime=" + endtime
				+ "&batchNum=" + batchNum
				+ "&searchType=" + searchType;
		}
		document.all("childFrm").src=page;		
	}
</script>
<title>�����ն˺ϸ���ͳ��</title>
</head>
<body>
	<form name="frm" id="frm" method="post">
		<div class="it_main">
			<h1 class="it_webtt">�����ն˺ϸ���ͳ��</h1>
			<table style="width: 90%" border="0" cellspacing="0" cellpadding="0" class="mainSearch">
			  	<tr>
				    <s:if test="searchType==1">
			  			<td class="tit">ά�޳��ң�</td>
				    	<td>
				    		<input type="hidden" name="searchType" value="<s:property value='searchType' />" />
				    		<s:select list="vendorList" name="repair_vendor_id" headerKey="-1" 
								headerValue="ȫ������" listKey="repair_vendor_id" listValue="vendor_name" 
								value="repair_vendor_id"  theme="simple" cssClass="gj_select"/>
				    	</td>
			  		</s:if>
			  		<s:elseif test="searchType==2">
				    	<td class="tit">���أ�</td>
				    	<td>
				    		<s:select list="cityList" name="cityId" headerKey="-1"
								headerValue="��ѡ������" listKey="city_id" listValue="city_name"
								value="cityId" cssClass="gj_select"></s:select>
				    	</td>
				    </s:elseif>
				    <td class="tit">����ʱ�䣺</td>
				    <td width="39%">
				    	<input type="hidden" name="searchType" value="<s:property value = 'searchType' />" />
				    	<input type="text" name="starttime" readonly value="<s:property value='starttime'/>">
							<img name="shortDateimg" onClick="WdatePicker({el:document.frm.starttime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../images/dateButton.png" width="15" height="12" border="0" alt="ѡ��" /> 
								&nbsp;��&nbsp;
						<input type="text" name="endtime" readonly value="<s:property value='endtime'/>"> 
							<img name="shortDateimg" onClick="WdatePicker({el:document.frm.endtime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../images/dateButton.png" width="15" height="12" border="0" alt="ѡ��" /> 
				    </td>
				    <td class="tit">���Σ�</td>
				    <td>
			      		<s:select list="batchNumList" name="batchNum" headerKey="-1" 
							headerValue="ȫ������" listKey="batch_number" listValue="batch_number" 
							value="batch_number"  theme="simple" cssClass="gj_select"/>
				    </td>
			  	</tr>
			</table>
			<div style="width: 87%" class="mainSearch_btn">
				<a href="javascript:doQuery();">�� ѯ </a>
				<a href="javascript:goBack();" target="_self">���� </a>
			</div>
		  	<div class="content">
			    <div class="left_chat" style="width: 1000px;" id="columechat"></div>
			    <div class="right_chat" style="width: 200px;" id="qualifiedRateCharts">
			    <div class="pienum" id="qualfiedRatePie"></div>
			    <div></div>
			    </div>
			</div>
		</div>
	</form>
	<div style="display: none;">
		<iframe id="childFrm" name="childFrm" src=""></iframe>
	</div> 
</body>
<div id="toExcel" style="display: none;margin-top: 300px;margin-left: 50px;">
	<img src="<s:url value='/images/excel.gif'/>" border="0" alt="��������" style="cursor: hand" onclick="qualifiedRateToExcel()">&nbsp;&nbsp;��������&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	<img src="<s:url value='/images/excel.gif'/>" border="0" alt="����δ�ϸ��嵥" style="cursor: hand" onclick="noQualifiedToExcel()">&nbsp;&nbsp;����δ�ϸ��嵥
</div>
</html>