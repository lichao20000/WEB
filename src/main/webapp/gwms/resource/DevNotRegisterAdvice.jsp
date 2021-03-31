<%--
设备未注册的处理建议
Author: 王森博
Version: 1.0.0
Date: 2010-4-14
--%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<link rel="stylesheet" href="<s:url value="/css/css_green.css"/>"
	type="text/css">
<table border=0 cellspacing=1 cellpadding=1 width="95%" align="center"
	bgcolor="#999999">
	<tr>
		<td class="green_title">
			<font size="4">设备未注册的处理建议</font>
		</td>
	</tr>

	<tr bgcolor="#FFFFFF">
		<td>
			<s:if test='instArea == "js_dx"'>
				<table border=0 cellspacing=0 cellpadding=0 width="100%"
					align="center" bgcolor="#999999">

					<tr bgcolor="#FFFFFF" valign="top">
						<td>
							<font size="2"> 1. </font>
						</td>
						<td>
							<font size="2">
								通过超级管理员账号登陆设备，看使用原始密码(nE7jA%5m)是否能够登录；如果无法登录，说明终端已正常注册到的ITMS系统；如果未被修改则进入下一步。</font>
						</td>
					</tr>
					<tr bgcolor="#FFFFFF" height="10">
						<td colspan="2"></td>
					</tr>
					<tr bgcolor="#FFFFFF" valign="top">
						<td valign="top">
							<font size="2"> 2.</font>
						</td>
						<td>
							<font size="2">
								查看远程管理的ITMS的URL：http://devacs.edatahome.com:9090/ACS-server/ACS配置是否正确；如果正确进入下一步。</font>
						</td>
					</tr>
					<tr bgcolor="#FFFFFF" height="10">
						<td colspan="2"></td>
					</tr>
					<tr bgcolor="#FFFFFF" valign="top">
						<td valign="top">
							<font size="2"> 3.</font>
						</td>
						<td>
							<font size="2">
								登录终端通过【网络】-【宽带设置】查看设备上是否存在8/46的PVC通道，业务类型：TR069；DHCP路由接入方式。如果正确进入下一步。
							</font>
						</td>
					</tr>
					<tr bgcolor="#FFFFFF" height="10">
						<td colspan="2"></td>
					</tr>
					<tr bgcolor="#FFFFFF" valign="top">
						<td valign="top">
							<font size="2"> 4.</font>
						</td>
						<td>
							<font size="2">
								设备上的网络连接状态查看(【状态】-【网络侧信息】菜单)，TR069的通道是否已经获取到10.的私网地址，DNS是否获取到；如果正确进入下一步。
							</font>
						</td>
					</tr>
					<tr bgcolor="#FFFFFF" height="10">
						<td colspan="2"></td>
					</tr>
					<tr bgcolor="#FFFFFF" valign="top">
						<td valign="top">
							<font size="2"> 5.</font>
						</td>
						<td>
							<font size="2"> 在终端上
								通过【管理】-【维护】菜单pingITMS系统的ACS服务器DNS的地址：192.168.0.3，查看设备到系统之间的网络是否可达；如果正确进入下一步。
							</font>
						</td>
					</tr>
					<tr bgcolor="#FFFFFF" height="10">
						<td colspan="2"></td>
					</tr>
					<tr bgcolor="#FFFFFF" valign="top">
						<td valign="top">
							<font size="2">6.</font>
						</td>
						<td>
							<font size="2"> 网络配置：DSLAM是否进行多PVC改造、BAS是否配置ITMS域、地址池，DNS：
								192.168.0.3是否配置；如果正确进入下一步。</font>
						</td>
					</tr>
					<tr bgcolor="#FFFFFF" height="10">
						<td colspan="2"></td>
					</tr>
					<tr bgcolor="#FFFFFF" valign="top">
						<td>
							<font size="2"> 7.</font>
						</td>
						<td>
							<font size="2"> 进行家庭网关终端的重启，尝试上报；如果还未能正常上报进入下一步。 </font>
						</td>
					</tr>
					<tr bgcolor="#FFFFFF" height="10">
						<td colspan="2"></td>
					</tr>
					<tr bgcolor="#FFFFFF" valign="top">
						<td>
							<font size="2">8.</font>
						</td>
						<td>
							<font size="2"> 否则需要联系厂家查看设备问题。</font>
						</td>
					</tr>
				</table>
			</s:if>
			<s:elseif test='instArea == "xj_dx"'>
				<table border=0 cellspacing=0 cellpadding=0 width="100%"
					align="center" bgcolor="#999999">


					<tr bgcolor="#FFFFFF" valign="top">
						<td>
							<font size="2"> 1. </font>
						</td>
						<td>
							<font size="2">
								通过超级管理员账号登陆设备，看使用原始密码(nE7jA%5m)是否能够登录；如果无法登录，说明终端已正常注册到的ITMS系统；如果未被修改则进入下一步。</font>
						</td>
					</tr>
					<tr bgcolor="#FFFFFF" height="10">
						<td colspan="2"></td>
					</tr>
					<tr bgcolor="#FFFFFF" valign="top">
						<td valign="top">
							<font size="2"> 2.</font>
						</td>
						<td>
							<font size="2">
								查看远程管理的ITMS的URL：http://devacs.edatahome.com:9090/ACS-server/ACS配置是否正确；如果正确进入下一步。</font>
						</td>
					</tr>
					<tr bgcolor="#FFFFFF" height="10">
						<td colspan="2"></td>
					</tr>
					<tr bgcolor="#FFFFFF" valign="top">
						<td valign="top">
							<font size="2"> 3.</font>
						</td>
						<td>
							<font size="2">
								登录终端通过【网络】-【宽带设置】查看设备上是否存在8/46的PVC通道，业务类型：TR069；DHCP路由接入方式；(新疆：独立VLAN：3960)。如果正确进入下一步。
							</font>
						</td>
					</tr>
					<tr bgcolor="#FFFFFF" height="10">
						<td colspan="2"></td>
					</tr>
					<tr bgcolor="#FFFFFF" valign="top">
						<td valign="top">
							<font size="2"> 4.</font>
						</td>
						<td>
							<font size="2">
								设备上的网络连接状态查看(【状态】-【网络侧信息】菜单)，TR069的通道是否已经获取到10.的私网地址，DNS是否获取到；如果正确进入下一步。
							</font>
						</td>
					</tr>
					<tr bgcolor="#FFFFFF" height="10">
						<td colspan="2"></td>
					</tr>
					<tr bgcolor="#FFFFFF" valign="top">
						<td valign="top">
							<font size="2"> 5.</font>
						</td>
						<td>
							<font size="2"> 在终端上
								通过【管理】-【维护】菜单pingITMS系统的ACS服务器DNS的地址：10.0.1.16，查看设备到系统之间的网络是否可达；如果正确进入下一步。
							</font>
						</td>
					</tr>
					<tr bgcolor="#FFFFFF" height="10">
						<td colspan="2"></td>
					</tr>
					<tr bgcolor="#FFFFFF" valign="top">
						<td valign="top">
							<font size="2">6.</font>
						</td>
						<td>
							<font size="2"> 网络配置：DSLAM是否进行多PVC改造、BAS是否配置ITMS域、地址池，DNS：
								10.0.1.16是否配置；如果正确进入下一步。</font>
						</td>
					</tr>
					<tr bgcolor="#FFFFFF" height="10">
						<td colspan="2"></td>
					</tr>
					<tr bgcolor="#FFFFFF" valign="top">
						<td>
							<font size="2"> 7.</font>
						</td>
						<td>
							<font size="2"> 进行家庭网关终端的重启，尝试上报；如果还未能正常上报进入下一步。 </font>
						</td>
					</tr>
					<tr bgcolor="#FFFFFF" height="10">
						<td colspan="2"></td>
					</tr>
					<tr bgcolor="#FFFFFF" valign="top">
						<td>
							<font size="2">8.</font>
						</td>
						<td>
							<font size="2"> 否则需要联系厂家查看设备问题。</font>
						</td>
					</tr>
				</table>

			</s:elseif>
		</td>
	</tr>
</table>
