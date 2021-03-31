<%@ include file="../../../timelater.jsp"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ page import="com.linkage.litms.common.database.DataSetBean"%>
<%@ page import="com.linkage.litms.common.database.Cursor"%>
<%@ page import="java.util.Map"%>
<%@ page import="com.linkage.commons.db.DBUtil" %>
<%request.setCharacterEncoding("gb2312");
			String strSQL = "select * from tab_role where role_id<>1";
			// teledb
			if (DBUtil.GetDB() == 3) {
				strSQL = "select role_id, role_name from tab_role where role_id<>1";
			}
			com.linkage.commons.db.PrepareSQL psql = new com.linkage.commons.db.PrepareSQL(strSQL);
			psql.getSQL();
			Cursor cursor = DataSetBean.getCursor(strSQL);
			Map fields = cursor.getNext();
			String roleStr = "";
			int i = 1;
			while (fields != null) {
				roleStr += "<input type=checkbox name=role_id value="
						+ fields.get("role_id") + ">&nbsp;"
						+ fields.get("role_name");
				if (i % 4 == 0)
					roleStr += "<br>";
				i++;
				fields = cursor.getNext();
			}
			String rp_id = request.getParameter("id");
			String rp_name = request.getParameter("report_name");
			cursor = null;
			fields = null;
%>
<HTML>
<HEAD>
<TITLE>综合网管 - 报表审核</TITLE>
<link rel="stylesheet" href="../../../css/css_green.css" type="text/css">
<script language="JavaScript" src="js/tree_maker.js"></script>
<script language="JavaScript" src="js/tree_res.js"></script>
<SCRIPT LANGUAGE="JavaScript">
<!--
function prt(msg){
	alert(msg);
}

function CheckForm(){
	document.frm.hidpathid.value = getCurrentPathID();

	document.frm.hidpath.value = getCurrentPath();

	document.frm.hidlayer.value = getCurrentLayer()*1+1;
	

	if(document.frm.hidpathid.value == ""){
		alert("请选择节点或不能选择根节点");
		return false;
	}
	else{
		var tmp = document.frm.hidpath.value;
		pos = tmp.indexOf(">>");
		tmp = tmp.substring(pos+3,tmp.length);
		document.frm.hidpath.value = tmp;
		tmp = document.frm.hidpathid.value;
		arr = tmp.split(",");
		document.frm.hidparentid.value = arr[0];
		document.frm.rootid.value = arr[arr.length-1];
		return true;
	}
}
 
function doString(flag){
	if(flag == 1)
		prt("审核报表成功!");
	else if(flag==-1)
		prt("审核报表失败!");

	window.close();
}
//-->
</SCRIPT>
</HEAD>
<BODY>

<FORM METHOD=POST ACTION="rp_nodesave.jsp" name="frm"
	onsubmit="return CheckForm();" target="childFrm">
<table width="98%" border="0" align="center" cellpadding="0"
	cellspacing="5">
	<tr>
		<td></td>
	</tr>
	<tr>
		<td height="44" valign="top">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td width="13%"><img src="images/shenhe_tu1.gif" width="85"
					height="78"></td>
				<td width="2%"><img src="images/shenhe_tu2.gif" width="3"
					height="69"></td>
				<td width="85%" valign="top">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td class="html_title"><strong>说明</strong></td>
					</tr>
					<tr>
						<td height="55" bgcolor="#EFEBFF">&nbsp;</td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td height="1" background="images/dian.gif"></td>
	</tr>
	<tr>
		<td height="13">
		<table width="95%" border="0" cellpadding="6" cellspacing="1"
			bgcolor="#999999">
			<tr>
				<td width="25%" bgcolor="#FFFFFF">
				<table width="95%" border="0" align="center" cellpadding="0"
					cellspacing="0" class="text">
					<tr>
						<td width="155">
						<table width="143" height="29" border="0" cellpadding="0"
							cellspacing="0">
							<tr>
								<td background="images/shenhe_tu3.gif">
								<div align="center">审核意见</div>
								</td>
							</tr>
						</table>
						</td>
						<td><input type="radio" name="report_stat" value="1" checked> 同意 <input
							type="radio" name="report_stat" value="-1"> 不同意</td>
					</tr>
				</table>
				</td>
			</tr>
			<tr>
				<td bgcolor="#FFFFFF">
				<table width="95%" border="0" align="center" cellpadding="0"
					cellspacing="0" class="text">
					<tr>
						<td width="155">
						<table width="143" height="29" border="0" cellpadding="0"
							cellspacing="0">
							<tr>
								<td background="images/shenhe_tu3.gif">
								<div align="center">报表名称<br>
								</div>
								</td>
							</tr>
						</table>
						</td>
						<td><input name="report_name" type="text" class="form_yellowkuang"
							size="30" value="<%=rp_name%>"></td>
					</tr>
				</table>
				</td>
			</tr>
			<tr>
				<td bgcolor="#FFFFFF">
				<table width="95%" border="0" align="center" cellpadding="0"
					cellspacing="0" class="text">
					<tr>
						<td width="155">
						<table width="143" height="29" border="0" cellpadding="0"
							cellspacing="0">
							<tr>
								<td background="images/shenhe_tu3.gif">
								<div align="center">浏览权限</div>
								</td>
							</tr>
						</table>
						</td>
						<td><%=roleStr%></td>
					</tr>
				</table>
				</td>
			</tr>
			<tr>
				<td bgcolor="#FFFFFF">
				<table width="95%" border="0" align="center" cellpadding="0"
					cellspacing="0" class="text">
					<tr>
						<td align=center><INPUT TYPE="submit" NAME="cmdSave" class="btn"
							value=" 保 存 "> &nbsp;&nbsp; <INPUT TYPE="button" NAME="cmdClose"
							class="btn" value=" 关 闭 " onclick="close();"> <INPUT
							TYPE="hidden" name="hidpathid"> <INPUT TYPE="hidden"
							name="rootid"> <INPUT TYPE="hidden" name="id" value="<%=rp_id%>">
						<INPUT TYPE="hidden" name="hidpath"> <INPUT TYPE="hidden"
							name="hidparentid"> <INPUT TYPE="hidden" name="hidlayer"></td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
</td>
</tr>
</table></FORM>
<iframe id="childFrm" name="childFrm" style="display:none"></iframe>
</BODY>
</HTML>
