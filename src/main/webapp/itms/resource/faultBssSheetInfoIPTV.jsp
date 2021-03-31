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
			BSS������ϸ��Ϣ
		</th>
	</tr>
	<s:if test="bssSheetList!=null">
		<s:if test="bssSheetList.size()==1">
			<s:iterator value="bssSheetList">
				<TR>
					<TD class=column width="15%" align='right'>
						ҵ������
					</TD>
					<TD width="35%">
						<s:property value="serv_type" />
					</TD>
					<TD class=column width="15%" align='right'>
						��������
					</TD>
					<TD width="35%">
						<s:property value="serv_status" />
					</TD>
				</TR>
				<TR>
					<%if (LipossGlobals.inArea("sd_lt")) { %>
					<TD class=column width="15%" align='right'>
					RMS����ʱ��
					</TD>
					<%}else{ %>
					<TD class=column width="15%" align='right'>
					ITMS����ʱ��
					</TD>
					<%} %>
					<TD width="35%">
						<s:property value="opendate" />
					</TD>
					<TD class=column width="15%" align='right'>
						�豸����
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
						IPTV��������˺�
					</TD>
					<TD width="35%">
						<s:property value="account" />
					</TD>
				</TR>
				<TR>
					<TD class=column width="15%" align='right'>
						����
					</TD>
					<TD width="35%">
						<s:property value="city_name" />
					</TD>
					<TD class=column width="15%" align='right'>
						IPTV����
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
						��ͨ�˿�
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
									ԭʼ������Ϣ
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
								ԭʼ������Ϣ
							</TD>
							<TD width="85%" colspan="3">
							</TD>
						</TR>
					</s:else>
				</s:if>
				<s:else>
					<TR>
						<TD class=column width="15%" align='right'>
							ԭʼ������Ϣ
						</TD>
						<TD width="85%" colspan="3">
						</TD>
					</TR>
				</s:else>-->
				<TR>
					<TD class=column width="15%" align='right'>
						ԭʼ������Ϣ
					</TD>
					<TD width="85%" colspan="3">
					</TD>
				</TR>
				<tr>
					<TD class=column width="15%" align='right'>
						�ص���Ϣ
					</TD>
					<TD width="85%" colspan="3">
						0|||00|||�ɹ�
					</TD>
				</tr>
			</s:iterator>
		</s:if>
		<s:else>
			<tr>
				<td colspan=4>
					������Ϣ�����ڻ����!
				</td>
			</tr>
		</s:else>
	</s:if>
	<s:else>
		<tr>
			<td colspan=4>
				�û���Ϣ����!
			</td>
		</tr>
	</s:else>
</table>
