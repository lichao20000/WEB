<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page import="java.util.Map"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/faulttreatment/slide.js"/>"></script>
<link href="<s:url value="/css3/css.css"/>" rel="stylesheet" type="text/css" />
<input type="hidden" name="instArea" value="<s:property value='instArea'/>">
<table width="100%" border="0" cellspacing="0" cellpadding="0"
					class="it_table">
					<tr>
						<th>
				选择
			</th>
			<th>
				设备厂商
			</th>
			<th>
				型号
			</th>
			<th>
				软件版本
			</th>
			<th>
				设备序列号
			</th>
			<th>
				域名或IP
			</th>
			<th>
				终端类型
			</th>
			<th>
				属地
			</th>
			<th>
				绑定状态
			</th>
			<th>
				操作
			</th>
		</tr>
					<s:if test="deviceList!=null">
						<s:iterator value="deviceList">
					<tr>
						<td>
					<input type=radio name="radioDeviceId" value="1"
						onclick="deviceOnclick('<s:property value="device_id"/>',
											'<s:property value="city_id"/>',
											'<s:property value="oui"/>',
											'<s:property value="device_serialnumber"/>',
											'<s:property value="IpCity_id"/>',
											'<s:property value="cpe_allocatedstatus"/>',
											'<s:property value="flag"/>',
											'<s:property value="manage"/>',
											'<s:property value="device_type"/>')" />
				</td>
				<td>
					<s:property value="vendor_add" />
				</td>
				<td>
					<s:property value="device_model" />
				</td>
				<td>
					<s:property value="softwareversion" />
				</td>
				<td>
					<s:property value="oui" />
					-
					<s:property value="device_serialnumber" />
				</td>
				<td>
					<s:property value="loopback_ip" />
				</td>
				<td>
					<s:property value="device_type" />
				</td>
				<td>
					<s:property value="city_name" />
				</td>
				<TD>
					<s:if test='cpe_allocatedstatus=="1"'>
					已绑定
					</s:if>
					<s:else>
					未绑定
					</s:else>
				</TD>
				<TD>
					<IMG SRC="<s:url value="/images/view.gif"/>" BORDER="0" ALT="详细信息"
						onclick="javascript:DetailDevice('<s:property value="device_id" />');"
						style="cursor: hand">
				</TD>
					</tr>
					</s:iterator>
					</s:if>
					<s:else>
				<tr bgcolor="#FFFFFF">
					<td colspan="7" align="center">
						<font color="red">你所查询的终端未注册到系统，请按照"设备未注册的处理建议"进行排障！</font>
					</td>
				</tr>
	</s:else>
				</table>
				<div class="bd_yes" id="bd_btn" style="display: none">
				<FORM NAME="frm" METHOD="post" ACTION="">
				<input type="hidden" name="_deviceId" value="" />
				<input type="hidden" name="_deviceCityId" value="" />
				<input type="hidden" name="_oui" value="" />
				<input type="hidden" name="_deviceSN" value="" />
				<input type="hidden" name="_deviceType" value="" />
				<!-- 属地修正 -->
				<input type="hidden" name="IpCityId" value="" />
				<input type="hidden" name="isBind" value="" />
				<input type="hidden" name="flag" value="" />
					<a id="btn_msbd" onclick="bindByUser()">绑定</a>
				</FORM>
				</div>