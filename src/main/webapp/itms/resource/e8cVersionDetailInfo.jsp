<%@page import="com.linkage.litms.LipossGlobals"%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>e8-c�淶�汾��ϸҳ��</title>
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
	<a class="dialog_close" onclick="javascript:closeDialog()">��</a>
	  	<h2>��ϸ��Ϣ</h2>
	  	<div class="itd_main">
	    <table width="100%" border="0" cellspacing="0" cellpadding="0" class="it_table">
	  		<tbody>
	  			<s:iterator value="deviceList">
					  <tr>
					    <th width="190px;">����Э��</th>
					    <td width="160px;">
					    	<s:if test="null==server_type">
					    		��
					    	</s:if>
					    	<s:else>
					    		<s:property value="server_type" />
					    	</s:else>
					    </td>
					    <th width="150px;">�Ƿ�֧��IPV6</th>
					     <td width="160px;">
					    	<s:if test='1==ip_type'>
								��
							</s:if> 
							<s:elseif test='2==ip_type'>
								��
							</s:elseif>
							<s:else>
								��
							</s:else>
						</td>
					  </tr>
					  <tr>
					    <th width="190px;">�豸IP֧�ַ�ʽ</th>
					    <td width="160px;">
						    <s:if test='0==ip_model_type'>
								IPv4
							</s:if> 
							<s:elseif test="1==ip_model_type">
								IPv4��IPv6
							</s:elseif>
							<s:elseif test="2==ip_model_type">
								DS-Lite
							</s:elseif>
							<s:elseif test="3==ip_model_type">
								LAFT6
							</s:elseif>
							<s:elseif test="4==ip_model_type">
								��IPV6
							</s:elseif>
							<s:else>
								��
							</s:else>
						</td>
					    <th width="150px;">�汾����ʱ��</th>
					     <td width="160px;">
					     	<s:property value="versionttime" />
					     </td>
					  </tr>
					  <tr>
					    <th width="190px;">�Ƿ�֧�ְ��׿��</th>
					     <td width="160px;">
					    	<s:if test='1==mbbroadband'>
								��
							</s:if> 
							<s:elseif test='2==mbbroadband'>
								��
							</s:elseif>
							<s:else>
								��
							</s:else>
						</td>
					    <th width="150px;">�Ƿ�֧��awifi��ͨ</th>
					     <td width="160px;">
					    	<s:if test='1==is_awifi'>
								��
							</s:if>
							<s:elseif test='2==is_awifi'>
								��
							</s:elseif>
							<s:else>
								��
							</s:else>
						</td>
					  </tr>
					  <tr>
					    <th width="190px;">�Ƿ�֧�ֻ�����������</th>
					     <td width="160px;">
					    	<s:if test='1==zeroconf'>
								��
							</s:if> 
							<s:elseif test='2==zeroconf'>
								��
							</s:elseif>
							<s:else>
								��
							</s:else>
						</td>
					    <th width="150px;">�Ƿ�֧��OTT</th>
					    <td width="160px;">
					    	<s:if test='1==is_ott'>
								��
							</s:if> 
							<s:elseif test='2==is_ott'>
								��
							</s:elseif>
							<s:else>
								��
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