<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%><html>
<link rel="stylesheet" href="<s:url value='/css/css_green.css'/>" type="text/css">
<head>
<title>Lan侧下联设备信息</title>
</head>
<body>
<TABLE border=0 cellspacing=1 cellpadding=2 width="100%" bgcolor=#999999>
	<tr align="left">
		<td colspan="4" class="green_title_left">
			查询结果
		</td>
	</tr>

	<s:if test="diagResult.pass == '-2'">
		<tr>
			<td colspan="4" class="column" >
				<s:property value="diagResult.failture" escapeHtml="false" default=""/>
			</td>
		</tr>
	</s:if>

	<s:else>
		<s:if test="diagResult.pass == '-1'">
			<tr>
				<td colspan="" class="column" >
					错误描述:
				</td>
				<td>
					<FONT color="red"><s:property value="diagResult.faultDesc" escapeHtml="false" default=""/></FONT>
				</td>
				<td colspan="" class="column" >
					错误建议:
				</td>
				<td>
					<FONT color="green"><s:property value="diagResult.suggest" escapeHtml="false" default=""/></FONT>
				</td>
			</tr>
		</s:if>
		
		<s:else>
			<s:iterator value="diagResult.rList">
			
				<TR bgcolor="#FFFFFF">
					<TD class=column align="center" colspan="4" height="20" >
						SSID名称:
						<s:if test="'N/A' == ssid || 'null' == ssid">
							-
						</s:if>
						<s:else>
							<s:property value="ssid" escapeHtml="false"/>
						</s:else>
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" nowrap width="15%">
						wlan的开关:
					</TD>
					<s:if test="'N/A' == ap_enable || 'null' == ap_enable">
						<td align="center">-</td>
					</s:if>
					<s:else>
						<td align="center">
							<s:if test="ap_enable == 1">
								开启
							</s:if>
							<s:elseif test="ap_enable == 0">
								关闭
							</s:elseif>
							<s:else>
								
							</s:else>
						</td>
					</s:else>

					<TD class=column align="right" width="15%">
						wlan的状态:
					</TD>
						<s:if test="'N/A' == status || 'null' == status">
						<td align="center">-</td>
					</s:if>
					<s:else>
						<td align="center">
							<s:property value="status" escapeHtml="false"/>
						</td>
					</s:else>
				</TR>

				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" nowrap width="15%">
						BeaconAdvertisementEnabled:
					</TD>
					<s:if test="'N/A' == beacon_advertisement_enabled || 'null' == beacon_advertisement_enabled">
						<td align="center">-</td>
					</s:if>
					<s:else>
						<td align="center">
							<s:if test="beacon_advertisement_enabled == 1">
								开启
							</s:if>
							<s:elseif test="beacon_advertisement_enabled == 0">
								关闭
							</s:elseif>
							<s:else>
								
							</s:else>
						</td>
					</s:else>

					<TD class=column align="right" width="15%">
						信道:
					</TD>
					<s:if test="'N/A' == channel || 'null' == channel">
						<td align="center">-</td>
					</s:if>
					<s:else>
						<td align="center">
							<s:property value="channel" escapeHtml="false"/>
						</td>
					</s:else>
				</TR>

				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" nowrap width="15%">
						BeaconType:
					</TD>
					<s:if test="'N/A' == beacontype || 'null' == beacontype">
						<td align="center">-</td>
					</s:if>
					<s:else>
						<td align="center">
							<s:property value="beacontype" escapeHtml="false"/>
						</td>
					</s:else>

					<TD class=column align="right" width="15%">
						WEPEncryptionLevel:
					</TD>
					<s:if test="'N/A' == wep_encr_level || 'null' == wep_encr_level">
						<td align="center">-</td>
					</s:if>
					<s:else>
						<td align="center">
							<s:property value="wep_encr_level" escapeHtml="false"/>
						</td>
					</s:else>
				</TR>

				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" nowrap width="15%">
						BasicEncryptionModes:
					</TD>
					<s:if test="'N/A' == basic_encryption_modes || 'null' == basic_encryption_modes">
						<td align="center">-</td>
					</s:if>
					<s:else>
						<td align="center">
							<s:property value="basic_encryption_modes" escapeHtml="false"/>
						</td>
					</s:else>

					<TD class=column align="right" width="15%">
						BasicAuthenticatonMode:
					</TD>
					<s:if test="'N/A' == basic_auth_mode || 'null' == basic_auth_mode">
						<td align="center">-</td>
					</s:if>
					<s:else>
						<td align="center">
							<s:property value="basic_auth_mode" escapeHtml="false"/>
						</td>
					</s:else>
				</TR>

				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" nowrap width="15%">
						WPAEncryptionModes:
					</TD>
					<s:if test="'N/A' == wpa_encr_mode || 'null' == wpa_encr_mode">
						<td align="center">-</td>
					</s:if>
					<s:else>
						<td align="center">
							<s:property value="wpa_encr_mode" escapeHtml="false"/>
						</td>
					</s:else>

					<TD class=column align="right" width="15%">
						WPAAuthenticationMode:
					</TD>
					<s:if test="'N/A' == wpa_auth_mode || 'null' == wpa_auth_mode">
						<td align="center">-</td>
					</s:if>
					<s:else>
						<td align="center">
							<s:property value="wpa_auth_mode" escapeHtml="false"/>
						</td>
					</s:else>
				</TR>

				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" nowrap width="15%">
						IEEE11iEncryptionModes:
					</TD>
					<s:if test="'N/A' == ieee_encr_mode || 'null' == ieee_encr_mode">
						<td align="center">-</td>
					</s:if>
					<s:else>
						<td align="center">
							<s:property value="ieee_encr_mode" escapeHtml="false"/>
						</td>
					</s:else>

					<TD class=column align="right" width="15%">
						IEEE11iAuthenticationMode:
					</TD>
					<s:if test="'N/A' == ieee_auth_mode || 'null' == ieee_auth_mode">
						<td align="center">-</td>
					</s:if>
					<s:else>
						<td align="center">
							<s:property value="ieee_auth_mode" escapeHtml="false"/>
						</td>
					</s:else>
				</TR>

				<TR bgcolor="#FFFFFF">
					
					<TD class=column align="right" width="15%">
						PC的IP地址:
					</TD>
					<s:if test="'N/A' == ipaddress || 'null' == ipaddress">
						<td align="center" colspan="3">-</td>
					</s:if>
					<s:else>
						<td align="center" colspan="3">
							<s:property value="ipaddress" escapeHtml="false"/>
						</td>
					</s:else>
				</TR>
			</s:iterator>
		</s:else>
	</s:else>
</TABLE>
</body>
</html>