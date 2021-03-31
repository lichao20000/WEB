<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page import="com.linkage.litms.LipossGlobals"%>
<%@page import="java.util.Map"%>
<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>配置信息</title>
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
<a class="sta_close" href="javascript:configInfoClose()" ></a><div class="sta_tit" style="height: 20px"></div>
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
						宽带帐号或专线接入号
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
						上网方式
					</TD>
					<TD width="85%" colspan="3">
						<s:property value="wan_type" />
					</TD>
				</TR>
				<TR>
					
					<TD class=column width="15%" align='right'>
						VLANID
					</TD>
					<TD width="35%">
						<s:property value="vlanid" />
					</TD>
					<TD class=column width="15%" align='right'>
						
					</TD>
					<TD width="85%" colspan="3">
						
					</TD>
				</TR>
				<TR>
					
					<!-- <TD class=column width="15%" align='right'>
						地址形式
					</TD> -->
					<TD class=column width="15%" align='right'>
						用户IP类型
					</TD>
					<TD width="35%">
						<s:if test='%{dslite_enable=="是"}'>
							Ds-Lite
						</s:if>
						<s:else>
							<s:property value="ip_type" />
						</s:else>
					</TD>
					<%-- <TD class=column width="15%" align='right'>
						是否支持ds-lite
					</TD>
					<TD width="85%" colspan="3">
						<s:property value="dslite_enable" />
					</TD> --%>
					<TD class=column width="15%" align='right'>
					</TD>
					<TD width="85%" colspan="3">
					</TD>
				</TR>
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
</html>
