<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<link rel="stylesheet" href="<s:url value='/css/css_green.css'/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>

<SCRIPT LANGUAGE="JavaScript">
	
</SCRIPT>

<TABLE align="right" border=0 cellspacing=0 cellpadding=0 width="100%" >
	<tr >
	  <td colspan="4"  bgcolor="#999999">
		<table width="100%" border="0" cellpadding="2" cellspacing="1"   >
						<tr align="left" >
							<td  align="left" class="green_title_left" colspan="4">
								网关基本信息
							</td>
						</tr>
						<tr bgcolor="#FFFFFF">
							<TD class=column align="right" width="15%">
								设备序列号:
							</TD>
							<TD width="35%" >
								<s:property value="gatewayInfo.device_serialnumber" />
							</TD>
							<td class=column align="right" width="15%">
								设备MAC地址:
							</td>
							<td width="35%" >
								<s:property value="gatewayInfo.cpe_mac"/>
							</td>
						</tr>
						<tr bgcolor="#FFFFFF">
							<TD class=column align="right" width="15%">
								属地:
							</TD>
							<TD width="35%" >
								<s:property value="gatewayInfo.city_name" />
							</TD>
							<td class=column align="right" width="15%">
								宽带帐号:
							</td>
							<td width="35%" >
								<s:property value="gatewayInfo.oui"/>
							</td>
						</tr>
						<tr bgcolor="#FFFFFF">
							<TD class=column align="right" width="15%">
								IP地址:
							</TD>
							<TD width="35%" >
								<s:property value="gatewayInfo.loopback_ip" />
							</TD>
							<td class=column align="right" width="15%">
								厂商:
							</td>
							<td width="35%" >
								<s:property value="gatewayInfo.vendor_name"/>
							</td>
						</tr>
						<tr bgcolor="#FFFFFF">
							<TD class=column align="right" width="15%">
								设备型号:
							</TD>
							<TD width="35%" >
								<s:property value="gatewayInfo.device_model" />
							</TD>
							<td class=column align="right" width="15%">
								设备类型:
							</td>
							<td width="35%" >
								<s:property value="gatewayInfo.device_type"/>
							</td>
						</tr>
						<tr bgcolor="#FFFFFF">
							<TD class=column align="right" width="15%">
								软件版本号:
							</TD>
							<TD width="35%" >
								<s:property value="gatewayInfo.softwareversion" />
							</TD>
							<TD class="column" align="right" width="15%">
							</TD>
							<TD width="35%" >
							</TD>
						</tr>
			</table>
		</td>
	</tr>
	<tr >
		<td colspan="5" bgcolor="#999999">
			<table width="100%" border="0" cellpadding="2"
						cellspacing="1" >
				<tr align="left" >
					<td  align="left" class="green_title_left" colspan="5">
						网关业务信息
					</td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td   class=column align="center" width="25%">
						业务名称
					</td>
					<td   class=column align="center" width="25%">
						业务帐号
					</td>
					<td   class=column align="center" width="25%">
						受理时间
					</td>
					<td   class=column align="center" width="25%">
						业务状态
					</td>
				</tr>
				<s:if test="servInfo.size()>0">
					<s:iterator value="servInfo">
					<tr bgcolor="#FFFFFF">
						<TD align="center" width="25%" >
							<s:property value="serv_type_name" />
						</TD>
						<TD align="center" width="25%">
							<s:property value="username" />
						</TD>
						<TD align="center" width="25%">
							<s:property value="dealdate" />
						</TD>
						<TD align="center" width="25%">
							<s:if test='"1"==open_status'>成功</s:if>
							<s:if test='"0"==open_status'>未做</s:if>
							<s:if test='"-1"==open_status'>失败</s:if>
						</TD>
					</tr>
					</s:iterator>
				</s:if>
				<s:else>
					<tr  bgcolor="#FFFFFF">
						<td colspan="5" align="center">
							暂时没有数据
						</td>
					</tr>
				</s:else>
			</table>
		</td>
	</tr> 
	<tr>
		<td colspan="5" bgcolor="#999999">
			<table width="100%" border="0" cellpadding="2"
						cellspacing="1"  >
				<tr align="left">
					<td   class="green_title_left" colspan="5">  <!--LAN口需要遍历 界面，行数待定 -->
					   	网关流量信息
					</td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td class=column  align="center" width="20%">
						 最大上行流量
					</td>
					<td class=column align="center"  width="20%">
						最大下行流量
					</td>
					<td class=column align="center"  width="20%">
						平均上行流量
					</td>
					<td class=column align="center"  width="20%">
						平均下行流量
					</td>
					<td class=column align="center"  width="20%">
						平均上下行流量比 
					</td>
				</tr>
				<s:if test="gatewayFluxInfo.size()>0">
						<s:iterator value="gatewayFluxInfo">
						<tr bgcolor="#FFFFFF">
							<td align="center" width="20%">
								<s:property value="ifinoctetsbpsmax"/>
							</td>
							<td align="center" width="20%">
								<s:property value="ifoutoctetsbpsmax"/>
							</td>
							<td align="center" width="20%">
								<s:property value="ifinoctbps_avgmax"/>
							</td>
							<td align="center" width="20%">
								<s:property value="ifoutoctbps_avgmax"/>
							</td>
							<td align="center" width="20%">
								<s:property value="avgmax_rate"/>
							</td>
						</tr>
						</s:iterator>
				</s:if>
				<s:else>
					<tr  bgcolor="#FFFFFF">
						<td colspan="5" align="center">
							暂时没有数据
						</td>
					</tr>
				</s:else>
			</table>
		</td>
	</tr>
	<tr>
		<td colspan="6" bgcolor="#999999">
			<table width="100%" border="0" cellpadding="2"
						cellspacing="1" border="0" >
				<tr align="left">
					<td   class="green_title_left" colspan="7">  
					   	网关告警信息
					</td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td class=column align="center"  width="15%">
						告警类型
					</td>
					<td class=column align="center"  width="15%">
						告警级别
					</td>
					<td class=column align="center"  width="15%">
						告警状态
					</td>
					<td class=column align="center"  width="15%">
						告警原因
					</td>
					<td class=column align="center"  width="15%">
						发生时间
					</td>
					<td class=column align="center"  width="15%">
						修复时间
					</td>
					<td class=column align="center"  width="15%">
						修复建议
					</td>
				</tr>
				<s:if test="gatewayWarnLt.size()>0">
					<s:iterator value ="gatewayWarnLt">
						<tr bgcolor="#FFFFFF">
							<td align="center" width="15%">
								<s:if test='"1"==type'>设备告警</s:if>
								<s:if test='"2"==type'>服务质量告警</s:if>
								<s:if test='"3"==type'>通信告警</s:if>
								<s:if test='"4"==type'>处理失败告警></s:if>
								<s:if test='"5"==type'>网管系统产生的告警</s:if>
							</td>
							<td align="center" width="15%">
								<s:if test='"1"==mlevel'>紧急告警</s:if>
								<s:if test='"2"==mlevel'>主要告警</s:if>
								<s:if test='"3"==mlevel'>次要告警</s:if>
								<s:if test='"4"==mlevel'>警告告警</s:if>
								<s:if test='"5"==mlevel'>不确定告警</s:if>
								<s:if test='"6"==mlevel'>已清除告警</s:if>
							</td>
							<td align="center" width="15%">
								<s:if test='"1"==status'>未确认未清除告警</s:if>
								<s:if test='"2"==status'>已确认未清除告警</s:if>
								<s:if test='"3"==status'>未确认但已清除告警</s:if>
								<s:if test='"4"==status'>已确认并已清除告警</s:if>
							</td>
							<td align="center" width="15%">
								<s:property value="possiblereason"/>
							</td>
							<td align="center" width="15%">
								<s:property value="occurtime"/>
							</td>
							<td align="center" width="15%">
								<s:property value="cleartime"/>
							</td>
							<td align="center" width="15%">
								<s:property value="clearsuggestion"/>
							</td>
						</tr>
					</s:iterator>
				</s:if>
				<s:else>
						<tr bgcolor="#FFFFFF">
							<td colspan="7" align="center">
								暂时没有数据
							</td>
						</tr>
				</s:else>
			</table>
	</tr>
</TABLE>