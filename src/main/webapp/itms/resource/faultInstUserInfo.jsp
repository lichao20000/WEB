<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page import="java.util.Map"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/faulttreatment/slide.js"/>"></script>
<link href="<s:url value="/css3/css.css"/>" rel="stylesheet" type="text/css" />
<input type="hidden" name="instArea"
		value="<s:property value='instArea'/>">
<table width="100%" border="0" cellspacing="0" cellpadding="0"
					class="it_table">
					<tr>
						<th>选择</th>
						<th>用户帐号</th>
						<th>终端类型</th>
						<th>属地</th>
						<th>绑定设备</th>
						<th>绑定设备序列号</th>
						<th></th>
					</tr>
					<s:if test="userList!=null">
						<s:iterator value="userList">
					<tr>
						<td><input type="radio" name="radioUserId" value="1"
						onclick="userOnclick('<s:property value="user_id"/>',
											 '<s:property value="username"/>',
											 '<s:property value="city_id"/>',
											 '<s:property value="device_id"/>',
											 '<s:property value="oui"/>',
											 '<s:property value="device_serialnumber"/>',
											 '<s:property value="type_name"/>')" /></td>
						<td><s:property value="username" /></td>
						<td><s:property value="type_name" /></td>
						<td><s:property value="city_name" /></td>
						<s:if test="device_id==''">
						<td></td>
						<td></td>
						</s:if>
						<s:else>
					<td >
						<s:property value="oui" />
					</td>
					<td >
						<s:property value="device_serialnumber" />
					</td>
					</s:else>
						<td><a href="javascript:GoContent('<s:property value="user_id" />',1);" class="itta_more">查看详情</a></td>
					</tr>
					</s:iterator>
					</s:if>
				</table>
				<div class="bd_yes" id="bd_btn" style="display: none">
				<FORM NAME="frm" METHOD="post" ACTION="">
				<!-- 用户部分 -->
				<input type="hidden" name="_userId" value="" />
				<input type="hidden" name="_username" value="" />
				<input type="hidden" name="_typename" value="" />
				<input type="hidden" name="_userCityId" value="" />
				<input type="hidden" name="_oldDeviceId" value="">
					<a id="btn_msbd" onclick="bindBydeviceSN()">绑定</a>
				</FORM>
				</div>