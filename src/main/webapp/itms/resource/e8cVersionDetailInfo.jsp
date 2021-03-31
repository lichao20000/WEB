<%@page import="com.linkage.litms.LipossGlobals"%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>e8-c规范版本详细页面</title>
<link href="<s:url value="/css3/css_s.css"/>" rel="stylesheet"
	type="text/css" />
<script type="text/javascript"
	src="../../Js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript">
</script>
</head>
<body style="font-size: 14px;">
	<div class="dialog dialog_table" style="margin: -140 0 0 -270;">
	<a class="dialog_close" onclick="javascript:closeDialog()">×</a>
	  	<h2>详细信息</h2>
	  	<div class="itd_main">
	    <table width="100%" border="0" cellspacing="0" cellpadding="0" class="it_table">
	  		<tbody>
	  			<s:iterator value="deviceList">
					  <tr>
					    <th width="190px;">语音协议</th>
					    <td width="160px;">
					    	<s:if test="null==server_type">
					    		无
					    	</s:if>
					    	<s:else>
					    		<s:property value="server_type" />
					    	</s:else>
					    </td>
					    <th width="150px;">是否支持IPV6</th>
					     <td width="160px;">
					    	<s:if test='1==ip_type'>
								是
							</s:if> 
							<s:elseif test='2==ip_type'>
								否
							</s:elseif>
							<s:else>
								无
							</s:else>
						</td>
					  </tr>
					  <tr>
					    <th width="190px;">设备IP支持方式</th>
					    <td width="160px;">
						    <s:if test='0==ip_model_type'>
								IPv4
							</s:if> 
							<s:elseif test="1==ip_model_type">
								IPv4和IPv6
							</s:elseif>
							<s:elseif test="2==ip_model_type">
								DS-Lite
							</s:elseif>
							<s:elseif test="3==ip_model_type">
								LAFT6
							</s:elseif>
							<s:elseif test="4==ip_model_type">
								纯IPV6
							</s:elseif>
							<s:else>
								无
							</s:else>
						</td>
					    <th width="150px;">版本定版时间</th>
					     <td width="160px;">
					     	<s:property value="versionttime" />
					     </td>
					  </tr>
					  <tr>
					    <th width="190px;">是否支持百兆宽带</th>
					     <td width="160px;">
					    	<s:if test='1==mbbroadband'>
								是
							</s:if> 
							<s:elseif test='2==mbbroadband'>
								否
							</s:elseif>
							<s:else>
								无
							</s:else>
						</td>
					    <th width="150px;">是否支持awifi开通</th>
					     <td width="160px;">
					    	<s:if test='1==is_awifi'>
								是
							</s:if>
							<s:elseif test='2==is_awifi'>
								否
							</s:elseif>
							<s:else>
								无
							</s:else>
						</td>
					  </tr>
					  <tr>
					    <th width="190px;">是否支持机顶盒零配置</th>
					     <td width="160px;">
					    	<s:if test='1==zeroconf'>
								是
							</s:if> 
							<s:elseif test='2==zeroconf'>
								否
							</s:elseif>
							<s:else>
								无
							</s:else>
						</td>
					    <th width="150px;">是否支持OTT</th>
					    <td width="160px;">
					    	<s:if test='1==is_ott'>
								是
							</s:if> 
							<s:elseif test='2==is_ott'>
								否
							</s:elseif>
							<s:else>
								无
							</s:else>
					    </td>
					  </tr>
				  </s:iterator>
	  		</tbody>
		</table>
	</div>
</div>
</body>
</html>