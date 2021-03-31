<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
	<%@page import="java.util.Map"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%
	String gw_type = request.getParameter("gw_type");
	String netServUp = request.getParameter("netServUp");
	String isRealtimeQuery = request.getParameter("isRealtimeQuery");
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>故障处理</title>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/faulttreatment/slide.js"/>"></script>
<link href="<s:url value="/css3/css.css"/>" rel="stylesheet"
	type="text/css" />
	<style type="text/css">　　
　　    .ittop_searchnew{margin:10px auto;width:1300px;}
				#titleName {
  				position: relative;
 					left: 125px;
 					}
  				#searchBox {
  				position: relative;
 					left: 260px;
 					}
 					#searchCondition {
  				position: relative;
 					left: 382px;
 					}

　　</style>　
<script type="text/javascript">
	function doQuery() {
		var queryParam = $.trim($("input[@name='queryParam']").val());
		var queryType = $("input[@name='queryType']:checked").val();
		if (queryParam == "") {
			alert("请输入相关信息");
			return;
		}
		if(queryType == 'devicesn'){
			if(queryParam.length < 6){
				alert("设备序列号至少输入后6位");
				return;
			}
		}
		$("div[@id='temp']").show();
		$("div[@id='temp']").html("正在查询，请稍后...");
		$("div[@id='nouser']").hide();
		var url = "<s:url value='/itms/resource/faultTreadtMent!queryUserAndDeviceInfo.action'/>"; 
		$.post(url, {
			queryParam : queryParam,
			queryType : queryType
		}, function(ajax) {
				$("div[@id='titleName']").text("");
			$("div[@id='temp']").hide();
			$("div[@id='QueryData']").html("");
			$("div[@id='QueryData']").html(ajax);
		});
	}
</script>
</head>

<body>
<div class="ittop_searchnew">
	<div style="float:center;width:630px;">
			<input type="hidden" name="selectType" value="0" />
			<input type="hidden" name="gw_type" value='<%=gw_type %>' />
			<input type="hidden" name="netServUp" value='<%=netServUp %>' />
			<input type="hidden" name="isRealtimeQuery" value='<%=isRealtimeQuery %>' /> 
		
  <form action="" class="radio_list" id="searchCondition">
    <label><input name="queryType" type="radio" value="username" />LOID(用户账号) </label> 
    <label><input name="queryType" type="radio" value="devicesn" />设备序列号 </label> 
    <label><input name="queryType" checked="checked" type="radio" value="netusername" />宽带账号 </label> 
    <label><input name="queryType" type="radio" value="telephone" />VOIP账号 </label> 
    <label><input name="queryType" type="radio" value="iptvsn" />IPTV账号</label>
  </form>
  </div>
  <div id="titleName" class="input_line" style="font-size:28px;float:left;width:138px;color:#0066FF;" >
  	终 端 管 理
  </div>
   <div class="input_line" id="searchBox" style="float:left;">
    <input type="text" class="it_ipt" name="queryParam" /><input type="button" onclick="doQuery()" class="it_btn" value="查询" />
  </div>
</div>
	<div id="temp" class="it_main" style="display: none; text-align: center;color:#0066FF;font-size:20px;"></div>
	<div class="it_main">
		<div id="QueryData" style="width: 100%; height:auto; z-index: 1; top: 100px; overflow:auto;">
		</div>
    </div>
</body>
</html>
