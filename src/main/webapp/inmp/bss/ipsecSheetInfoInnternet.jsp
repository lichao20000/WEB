<%@page import="com.linkage.litms.LipossGlobals"%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<table class="querytable">

	<TR>
		<th colspan="4">
			IPSEC工单详细信息
		</th>
	</tr>
	<s:if test="ipsecSheetList!=null">
		<s:if test="ipsecSheetList.size()>0">
			<s:iterator value="ipsecSheetList">
				<TR>
					<TD class=column width="15%" align='right'>
						客户宽带账号
					</TD>
					<TD width="35%">
						<s:property value="username" />
					</TD>
					<TD class=column width="15%" align='right'>
						业务类型
					</TD>
					<TD width="35%">
						<s:property value="serv_type_id" />
					</TD>
				</TR>
				<TR>
					<TD class=column width="15%" align='right'>
					业务状态
					</TD>
					<TD width="35%">
						<s:property value="serv_status" />
					</TD>
					<TD class=column width="15%" align='right'>
						是否启用IPSec
					</TD>   
					<TD width="35%">
						<s:property value="enable" />
					</TD>
				</TR>
				<TR>
					<TD class=column width="15%" align='right'>
						请求id
					</TD>
					<TD width="35%">
						<s:property value="request_id" />
					</TD>
					<TD class=column width="15%" align='right'>
						IPSec类型
					</TD>
					<TD width="35%">
						<s:property value="ipsec_type" />
					</TD>
				</TR>
				<TR>
					
					<TD class=column width="15%" align='right'>
						PC -to-Site模式下对端域名
					</TD>
					<TD width="35%">
						<s:property value="remote_domain" />
					</TD>
					<TD class=column width="15%" align='right'>
						对端子网
					</TD>
					<TD width="35%" colspan="3">
						<s:property value="remote_subnet" />
					</TD>
				</TR>
				<TR>
					
					<TD class=column width="15%" align='right'>
						本端子网
					</TD>
					<TD width="35%">
						<s:property value="local_subnet" />
					</TD>
					<TD class=column width="15%" align='right'>
						Site-to-Site模式下对端IP地址
					</TD>
					<TD width="35%" colspan="3">
						<s:property value="remote_ip" />
					</TD>
				</TR>
				<TR>
					<TD class=column width="15%" align='right'>
						IKE协商方式
					</TD>
					<TD width="35%">
							<s:property value="exchange_mode" />
					</TD>
					<TD class=column width="15%" align='right'>
					IKE验证算法
					</TD>
					<TD width="35%" colspan="3">
					<s:property value="ike_auth_algorithm" />
					</TD>
				</TR>
				<TR>
					<TD class=column width="15%" align='right'>
						IKE验证方法
					</TD>
					<TD width="35%">
							<s:property value="ike_auth__method" />
					</TD>
					<TD class=column width="15%" align='right'>
					IKE加密算法
					</TD>
					<TD width="35%" colspan="3">
					<s:property value="ike_encryption_algorithm" />
					</TD>
				</TR>
				<TR>
					<TD class=column width="15%" align='right'>
						IKE DH组
					</TD>
					<TD width="35%">
							<s:property value="ike_dhgroup" />
					</TD>
					<TD class=column width="15%" align='right'>
					IKE身份类型
					</TD>
					<TD width="35%" colspan="3">
					<s:property value="ike_idtype" />
					</TD>
				</TR>
				<TR>
					<TD class=column width="15%" align='right'>
					IKE本端名称
					</TD>
					<TD width="35%">
							<s:property value="ike_localname" />
					</TD>
					<TD class=column width="15%" align='right'>
					IKE对端名称
					</TD>
					<TD width="35%" colspan="3">
					<s:property value="ike_remotename" />
					</TD>
				</TR>
				<TR>
					<TD class=column width="15%" align='right'>
						IKE预共享密钥
					</TD>
					<TD width="35%">
							<s:property value="ike_presharekey" />
					</TD>
					<TD class=column width="15%" align='right'>
					Ipsec封装后报文的出接口
					</TD>
					<TD width="35%" colspan="3">
					<s:property value="ipsec_out_interface" />
					</TD>
				</TR>
				<TR>
					<TD class=column width="15%" align='right'>
						Ipsec封装模式
					</TD>
					<TD width="35%">
							<s:property value="ipsec_encapsulation_mode" />
					</TD>
					<TD class=column width="15%" align='right'>
					IPSec安全协议
					</TD>
					<TD width="35%" colspan="3">
					<s:property value="ipsec_transform" />
					</TD>
				</TR>
				<TR>
					<TD class=column width="15%" align='right'>
						IPsec验证算法
					</TD>
					<TD width="35%">
							<s:property value="esp_auth_algorithem" />
					</TD>
					<TD class=column width="15%" align='right'>
					IPsec加密算法
					</TD>
					<TD width="35%" colspan="3">
					<s:property value="esp_encrypt_algorithm" />
					</TD>
				</TR>
				<TR>
					<TD class=column width="15%" align='right'>
						IPSec DH组
					</TD>
					<TD width="35%">
							<s:property value="ipsec_pfs" />
					</TD>
					<TD class=column width="15%" align='right'>
					IKE SA生命周期
					</TD>
					<TD width="35%" colspan="3">
					<s:property value="ike_saperiod" />
					</TD>
				</TR>
				<TR>
					<TD class=column width="15%" align='right'>
						IPsec SA时间生命周期
					</TD>
					<TD width="35%">
							<s:property value="ipsec_satime_period" />
					</TD>
					<TD class=column width="15%" align='right'>
					IPsec SA流量生命周期
					</TD>
					<TD width="35%" colspan="3">
					<s:property value="ipsec_satraffic_period" />
					</TD>
				</TR>
				<TR>
					<TD class=column width="15%" align='right'>
						AH验证算法
					</TD>
					<TD width="35%">
							<s:property value="ah_auth_algorithm" />
					</TD>
					<TD class=column width="15%" align='right'>
					DPD使能
					</TD>
					<TD width="35%" colspan="3">
					<s:property value="dpd_enable" />
					</TD>
				</TR>
				<TR>
					<TD class=column width="15%" align='right'>
						DPD空闲时间
					</TD>
					<TD width="35%">
							<s:property value="dpd_threshold" />
					</TD>
					<TD class=column width="15%" align='right'>
					未收到DPD响应，再次尝试间
					</TD>
					<TD width="35%" colspan="3">
					<s:property value="dpd_retry" />
					</TD>
				</TR>
				<TR>
					<TD class=column width="15%" align='right'>
					开户时间
					</TD>
					<TD width="35%">
							<s:property value="open_date" />
					</TD>
					<TD class=column width="15%" align='right'>
					更新时间
					</TD>
					<TD width="35%" colspan="3">
					<s:property value="updatetime" />
					</TD>
				</TR>
				<TR>
					<TD class=column width="15%" align='right'>
					完成时间
					</TD>
					<TD width="35%">
							<s:property value="completedate" />
					</TD>
					<TD class=column width="15%" align='right'>
					开通状态
					</TD>
					<TD width="35%" colspan="3">
					<s:property value="open_status" />
					</TD>
				</TR>
				<!-- <tr>
					<TD class=column width="15%" align='right'>
						回单信息
					</TD>
					<TD width="85%" colspan="3">
						0|||00|||成功
					</TD>
				</tr> -->
			</s:iterator>
		</s:if>
		<s:else>
			<tr>
				<td colspan=4>
					工单信息不存在或错误!
				</td>
			</tr>
		</s:else>
	</s:if>
	<s:else>
		<tr>
			<td colspan=4>
				用户信息错误!
			</td>
		</tr>
	</s:else>
	<TR>
		<td colspan="4" align="right" class=foot>
			<a href="javascript:bssSheetClose()">关闭</a>
		</td>
	</TR>
</table>
