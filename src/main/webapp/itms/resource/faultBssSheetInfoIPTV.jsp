<%@page import="com.linkage.litms.LipossGlobals"%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<head>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/faulttreatment/slide.js"/>"></script>
<link href="<s:url value="/css3/css.css"/>" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="<s:url value="/css/inmp/css3/c_table.css"/>" type="text/css">
<style type="text/css">
table.querytable{
	width:100%;
}

table.querytable td div.left{
	float: left;
}
table.querytable td div.right{
	float: right;
}

table.querytable,table.querytable tr,table.querytable td
{
    border:1px solid #ccc;
    /*background-color:#ffffff;*/
    padding:1px 2px 1px 2px;
	text-align: left;
    font-size: 12px;
	font-family: Arial, "Arial Black",  "??", "??";  
}

table.querytable TH
{
	font-size: 12px;
	color: #04143a;
	text-decoration: none;
	font-weight: bold;
	line-height: 22px;
	text-align: center;
	background:#d2dff8;height:30px;
}

table.querytable td.column
{
	background-color: #F2F5FF;
	color:#000000;
	text-align: right;height:30px
}

table.querytable .title_1 
{
    border:1px solid #ACA899;
	font-weight: bold;
	text-align: center;
	background-color: #D1D1D1;
	line-height: 20px;

}

table.querytable .title_2
{
	background-color: #F8E2B9;
	text-align: center;
}
table.querytable .title_3 
{
    background-color:#E0FCA8;
	text-align: center;
}
table.querytable .title_4
{
	background-color: #d1ddf2;
	text-align: center;
}
table.listtable tfoot,table.querytable tfoot{
	text-align: right;
	background-color: #E8E8FF;
	color:#000000;
	height: 25px;
}
table.querytable td.foot{
	text-align: right;
	background-color: #f2f5ff;
	color:#04143a;
	height: 40px;
}
</style>
</head>
<a class="sta_close" href="javascript:configInfoClose()"></a><div class="sta_tit" style="height: 20px"></div>
<table class="querytable">

	<TR>
		<th colspan="4">
			BSS工单详细信息
		</th>
	</tr>
	<s:if test="bssSheetList!=null">
		<s:if test="bssSheetList.size()==1">
			<s:iterator value="bssSheetList">
				<TR>
					<TD class=column width="15%" align='right'>
						业务类型
					</TD>
					<TD width="35%">
						<s:property value="serv_type" />
					</TD>
					<TD class=column width="15%" align='right'>
						操作类型
					</TD>
					<TD width="35%">
						<s:property value="serv_status" />
					</TD>
				</TR>
				<TR>
					<%if (LipossGlobals.inArea("sd_lt")) { %>
					<TD class=column width="15%" align='right'>
					RMS接收时间
					</TD>
					<%}else{ %>
					<TD class=column width="15%" align='right'>
					ITMS接收时间
					</TD>
					<%} %>
					<TD width="35%">
						<s:property value="opendate" />
					</TD>
					<TD class=column width="15%" align='right'>
						设备类型
					</TD>
					<TD width="35%">
						<s:property value="type_name" />
					</TD>
				</TR>
				<TR>
					<TD class=column width="15%" align='right'>
						LOID
					</TD>
					<TD width="35%">
						<s:property value="username" />
					</TD>
					<TD class=column width="15%" align='right'>
						IPTV宽带接入账号
					</TD>
					<TD width="35%">
						<s:property value="account" />
					</TD>
				</TR>
				<TR>
					<TD class=column width="15%" align='right'>
						属地
					</TD>
					<TD width="35%">
						<s:property value="city_name" />
					</TD>
					<TD class=column width="15%" align='right'>
						IPTV个数
					</TD>
					<TD width="35%">
						<s:property value="serv_num" />
					</TD>
				</TR>
				<TR>
					<TD class=column width="15%" align='right'>
						VLANID
					</TD>
					<TD width="85%" colspan="3" >
						<s:property value="vlanid" />
					</TD>
				</TR>
				<TR>
					<TD class=column width="15%" align='right'>
						开通端口
					</TD>
					<TD width="85%" colspan="3" >
						<s:property value="bind_port" />
					</TD>
				</TR>
				<!-- <s:if test="bssParaList!=null">
					<s:if test="bssParaList.size()>0">
						<s:iterator value="bssParaList">
							<TR>
								<TD class=column width="15%" align='right'>
									原始工单信息
								</TD>
								<TD width="85%" colspan="3">
									<s:property value="sheet_para_desc" />
								</TD>
							</TR>
						</s:iterator>
					</s:if>
					<s:else>
						<TR>
							<TD class=column width="15%" align='right'>
								原始工单信息
							</TD>
							<TD width="85%" colspan="3">
							</TD>
						</TR>
					</s:else>
				</s:if>
				<s:else>
					<TR>
						<TD class=column width="15%" align='right'>
							原始工单信息
						</TD>
						<TD width="85%" colspan="3">
						</TD>
					</TR>
				</s:else>-->
				<TR>
					<TD class=column width="15%" align='right'>
						原始工单信息
					</TD>
					<TD width="85%" colspan="3">
					</TD>
				</TR>
				<tr>
					<TD class=column width="15%" align='right'>
						回单信息
					</TD>
					<TD width="85%" colspan="3">
						0|||00|||成功
					</TD>
				</tr>
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
</table>
