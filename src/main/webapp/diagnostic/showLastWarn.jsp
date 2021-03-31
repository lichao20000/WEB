<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.common.database.Cursor"%>
<%@ page import="java.util.Map"%>
<%@ page import="com.linkage.litms.common.database.DataSetBean"%>
<%@ page import="com.linkage.litms.common.util.DateTimeUtil" %>
<%@ page import="com.linkage.litms.paramConfig.warnAction" %>
<%@ page import="com.linkage.litms.common.util.CommonMap" %>
<%@ page import="com.linkage.commons.db.DBUtil" %>

<%
int total = 0;
String sql = "select * from tab_alarm where 1=1 ";

// teledb
if (DBUtil.GetDB() == 3) {
	sql = "select mlevel, type, device_id, id, occurtime, status, description from tab_alarm where 1=1 ";
}

//�澯����
String warnType = request.getParameter("warnType");
if (warnType != null && !"".equals(warnType) && !"-1".equals(warnType)){
	sql += " and type = " + warnType;
}

//�澯�ȼ�
String warnLevel = request.getParameter("warnLevel");
if (warnLevel != null && !"".equals(warnLevel) && !"-1".equals(warnLevel)){
	sql += " and mlevel = " + warnLevel;
}

//�澯״̬
String warnStatus = request.getParameter("warnStatus");
if (warnStatus != null && !"".equals(warnStatus) && !"-1".equals(warnStatus)){
	sql += " and status = " + warnStatus;
}

sql += " order by occurtime desc";

com.linkage.commons.db.PrepareSQL psql = new com.linkage.commons.db.PrepareSQL(sql);
psql.getSQL();
//��ȡ�����200����¼
Cursor cursor=DataSetBean.getCursor(sql,200);
Map fields=cursor.getNext();

warnAction warnAct = new warnAction();

Map deviceNameMap = CommonMap.getDeviceNameMap();
%>

<script language="javascript">
function confirmWarn(id, device_id, time, status){

	if (status == 2 || status ==4){
		alert('�ø澯�Ѿ�ȷ�Ϲ�!');
	}
	else if (status == 1 || status ==3){
		var page="changeStatus.jsp?id="+id+"&device_id="+device_id+"&time="+time+"&action=confirm&status="+status+"&refresh="+(new Date()).getTime();
		document.all("childFrm").src = page;
	}
	else{
		alert('δ֪�澯״̬�޷�ȷ��!');
	}
}

function cleanWarn(id, device_id, time, status){

	if (status == 3 || status == 4){
		alert('�ø澯�Ѿ������!');
	}
	else if (status == 1 || status == 2){
		var page="changeStatus.jsp?id="+id+"&device_id="+device_id+"&time="+time+"&action=clean&status="+status+"&refresh="+(new Date()).getTime();
		document.all("childFrm").src = page;
	}
	else{
		alert('δ֪�澯״̬�޷����!');
	}
}

function cancelConfirmWarn(id, device_id, time, status){
	if (status == 1 || status == 3){
		alert('�ø澯��δȷ��!');
	}
	else if (status == 2 || status == 4){
		var page="changeStatus.jsp?id="+id+"&device_id="+device_id+"&time="+time+"&action=cancelConfirm&status="+status+"&refresh="+(new Date()).getTime();
		document.all("childFrm").src = page;
	}
	else{
		alert('δ֪�澯״̬�޷�����!');
	}
}

function showDetail(id, device_id, time, status){
	window.showModalDialog("./showWarnDetail.jsp?id="+id+"&device_id="+device_id+"&time="+time+"&refresh="+(new Date()).getTime());
}

function refreshAlarm(){
	var warnType = document.frm.warnType.value;
	var warnLevel = document.frm.warnLevel.value;
	var warnStatus = document.frm.warnStatus.value;
	
	window.location.reload("./showLastWarn.jsp?warnType="+warnType+"&warnLevel="+warnLevel+"&warnStatus="+warnStatus);
}

function reloadForm(){
	window.location.reload();
}

function confirmBatch(){
	document.frm.isBatch.value="1";
	document.frm.action.value="confirm";
	
	var obj = document.frm.checkWarm;
	var tmpList = "";
	for (var i=0;i<obj.length;i++){
		if (obj[i].checked){
			if (tmpList == ""){
				tmpList = obj[i].value;
			}
			else{
				tmpList = tmpList + "#" + obj[i].value;
			}
		}
	}
	
	if (tmpList == ""){
		alert("��ѡ����Ҫȷ�ϵĸ澯");
		return false;
	}
	
	document.frm.warmList.value = tmpList;
	
	document.frm.submit();
}

function cleanBatch(){
	document.frm.isBatch.value="1";
	document.frm.action.value="clean";
	
	var obj = document.frm.checkWarm;
	var tmpList = "";
	for (var i=0;i<obj.length;i++){
		if (obj[i].checked){
			if (tmpList == ""){
				tmpList = obj[i].value;
			}
			else{
				tmpList = tmpList + "#" + obj[i].value;
			}
		}
	}
	
	if (tmpList == ""){
		alert("��ѡ����Ҫ����ĸ澯");
		return false;
	}
	
	document.frm.warmList.value = tmpList;
	
	document.frm.submit();
}

function canelConfirmBatch(){
	document.frm.isBatch.value="1";
	document.frm.action.value="cancelConfirm";
	
	var obj = document.frm.checkWarm;
	var tmpList = "";
	for (var i=0;i<obj.length;i++){
		if (obj[i].checked){
			if (tmpList == ""){
				tmpList = obj[i].value;
			}
			else{
				tmpList = tmpList + "#" + obj[i].value;
			}
		}
	}
	
	if (tmpList == ""){
		alert("��ѡ����Ҫ����ĸ澯");
		return false;
	}
	
	document.frm.warmList.value = tmpList;
	
	document.frm.submit();
}

function checkAllWarm(){
	
	var obj = document.frm.checkWarm;
	if (document.frm.checkAll.checked){
		for (var i=0;i<obj.length;i++){
			obj[i].checked = true;
		}
		document.frm.checkAll2.checked = true;
	}
	else{
		for (var i=0;i<obj.length;i++){
			obj[i].checked = false;
		}
		document.frm.checkAll2.checked = false;
	}
}
function checkAllWarm2(){
	
	var obj = document.frm.checkWarm;
	if (document.frm.checkAll2.checked){
		for (var i=0;i<obj.length;i++){
			obj[i].checked = true;
		}
		document.frm.checkAll.checked = true;
	}
	else{
		for (var i=0;i<obj.length;i++){
			obj[i].checked = false;
		}
		document.frm.checkAll.checked = false;
	}
}
</script>

<%@ include file="../head.jsp"%>
<form name="frm" method="post" action="changeStatus.jsp" target="childFrm1">
	<input type="hidden" name="isBatch" value="0">
	<input type="hidden" name="warmList" value="">
	<input type="hidden" name="action" value="">
	<table width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
		<tr>
			<td height="20"></td>
		</tr>
		<TR>
			<TD colspan="2">
				<TABLE width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
					<TR>
						<TD width="164" align="center" class="title_bigwhite" nowrap>
							�澯����
						</TD>
						<td align=right nowrap>�澯����</td>
    					<td>
    						<select name="warnType" id="warnType" class=bk onchange="refreshAlarm()">
    							<option value="-1">--��������--</option>
    							<option value="1">�豸�澯</option>
    							<option value="2">���������澯</option>
    							<option value="3">ͨ�Ÿ澯</option>
    							<option value="4">����ʧ�ܸ澯</option>
    							<option value="5">����ϵͳ�����ĸ澯</option>
    						</select>
    					</td>
						<td align=right nowrap>�澯����</td>
    					<td>
    						<select name="warnLevel" id="warnLevel" class=bk onchange="refreshAlarm()">
    							<option value="-1">--���и澯--</option>
    							<option value="1">���ظ澯</option>
    							<option value="2">��Ҫ�澯</option>
    							<option value="3">��Ҫ�澯</option>
    							<option value="4">����澯</option>
    							<option value="5">�¼��澯</option>
    							<option value="6">����澯</option>
    						</select>
    					</td>
    					<td align=right nowrap>�澯״̬</td>
    					<td>
    						<select name="warnStatus" id="warnStatus" class=bk onchange="refreshAlarm()">
    							<option value="-1">--����״̬--</option>
    							<option value="1">δȷ��δ����澯</option>
    							<option value="2">��ȷ��δ����澯</option>
    							<option value="3">δȷ�ϵ�������澯</option>
    							<option value="4">��ȷ�ϲ�������澯</option>
    						</select>
    					</td>
    					<td nowrap>
							<a href="#" onclick="window.location.reload()">ˢ��</a>
						</td>
					</TR>
				</TABLE>
			</TD>
		</TR>
		<tr>
			<td>
				<table width="100%" border=0 cellspacing=1 cellpadding=2 bgcolor=#999999>
					<TR><TH colspan="10">ʵʱ��ѯ</TH></TR>
					<tr>
						<TD class="green_title2"><input type="checkbox" name="checkAll" onclick="checkAllWarm()"></TD>
						<TD class="green_title2">���</TD>
						<TD class="green_title2">�澯Դ</TD>
						<TD class="green_title2">�澯ʱ��</TD>
						<TD class="green_title2">�澯����</TD>
						<TD class="green_title2">�澯����</TD>
						<TD class="green_title2">�澯����</TD>
						<TD class="green_title2">�澯����</TD>
						<TD class="green_title2">�澯״̬</TD>
						<TD class="green_title2">����</TD>
					</tr>
					<%
					if (cursor.getRecordSize() !=0){
					while (fields != null){ 
						total++;
						
						String level = (String)fields.get("mlevel");
						String style = "color=black";
						if ("1".equals(level)){
							style = "color=#FF0000";
						}
						else if ("2".equals(level)){
							style = "color=#FFB4B2";
						}
						else if ("3".equals(level)){
							style = "color=#FFC351";
						}
						else if ("4".equals(level)){
							style = "color=#FFEBB5";
						}
						else if ("5".equals(level)){
							style = "color=#E1ECFB";
						}
						else{
							style = "color=black";
						}
						
						String type = (String)fields.get("type");
						String warnOrigin = (String)fields.get("device_id");
						if ("1".equals(type)){
							if (deviceNameMap.get(warnOrigin) != null){
								warnOrigin = (String)deviceNameMap.get(warnOrigin);
							}
							else{
								warnOrigin = "δ֪�豸";
							}
						}
						
						String checkWarm = (String)fields.get("id")
											+ "," + (String)fields.get("device_id")
											+ "," + (String)fields.get("occurtime")
											+ "," + (String)fields.get("status");
					%>
					<tr bgcolor=#ffffff>
						<td width="5%" class=column align=left><input type="checkbox" name="checkWarm" value="<%=checkWarm %>"></td>
						<td width="10%" class=column align=left nowrap><font <%=style%>><%=fields.get("id")%></font></td>
						<td width="10%" class=column align=left><font <%=style%>><%=warnOrigin%></font></td>
						<td width="10%" class=column align=left nowrap><font <%=style%>><%=(new DateTimeUtil(Long.parseLong((String)fields.get("occurtime"))*1000)).getLongDate()%></font></td>
						<td width="10%" class=column align=left nowrap><font <%=style%>><%=warnAct.getWarnLevel(fields.get("mlevel"))%></font></td>
						<td width="10%" class=column align=left nowrap><font <%=style%>><%=warnAct.getWarnType(type)%></font></td>
						<td width="10%" class=column align=left nowrap><font <%=style%>><%=warnAct.getWarnName(fields.get("id"))%></font></td>
						<td width="20%" class=column align=left nowrap><textarea rows="3" class="input-textarea" style="width:100%"><%=fields.get("description")%></textarea></td>
						<td width="10%" class=column align=left nowrap><font <%=style%>><%=warnAct.getWarnStatus(fields.get("status"))%></font></td>
						<td width="10%" class=column align=center nowrap>
							<a href="#" onclick="confirmWarn('<%=fields.get("id")%>','<%=fields.get("device_id")%>','<%=fields.get("occurtime")%>','<%=fields.get("status")%>')">ȷ��</a><BR>
							<a href="#" onclick="cleanWarn('<%=fields.get("id")%>','<%=fields.get("device_id")%>','<%=fields.get("occurtime")%>','<%=fields.get("status")%>')">���</a><BR>
							<a href="#" onclick="cancelConfirmWarn('<%=fields.get("id")%>','<%=fields.get("device_id")%>','<%=fields.get("occurtime")%>','<%=fields.get("status")%>')">ȡ��ȷ��</a><BR>
							<a href="#" onclick="showDetail('<%=fields.get("id")%>','<%=fields.get("device_id")%>','<%=fields.get("occurtime")%>','<%=fields.get("status")%>')">�澯��Ϣ</a>
						</td>
					</tr>
					<%
						fields = cursor.getNext();
					} 
					%>
					<tr>
						<TD class="green_title2"><input type="checkbox" name="checkAll2" onclick="checkAllWarm2()"></TD>
						<TD class="green_title2">���</TD>
						<TD class="green_title2">�澯Դ</TD>
						<TD class="green_title2">�澯ʱ��</TD>
						<TD class="green_title2">�澯����</TD>
						<TD class="green_title2">�澯����</TD>
						<TD class="green_title2">�澯����</TD>
						<TD class="green_title2">�澯����</TD>
						<TD class="green_title2">�澯״̬</TD>
						<TD class="green_title2">����</TD>
					</tr>
					<%
					}
					else{%>
					<tr  class=column><td colspan="10">û�в�ѯ������</td></tr>
					<%} %>
					<tr >
						<td colspan="5"  align="left" class="green_foot"  height="6"><a href="#" onclick="confirmBatch()">����ȷ��</a><a href="#" onclick="cleanBatch()" >  �������</a><a href="#" onclick="canelConfirmBatch()">  ����ȡ��ȷ��</a></td>
						<td colspan="5" align="right" class="green_foot"  height="6">������<%=total %>������</td>
					</tr>
				</table>
			</td>
		</tr>
		<TR style="display:none">
			<TD>
				<IFRAME name="childFrm" ID="childFrm" SRC="" STYLE="display:none"></IFRAME>
			</TD>
		</TR>
		<tr><td></td></tr>
	</table>
	<iframe name="childFrm1" id="childFrm1" style="display:none"></iframe>
</form>

<script language="javascript">
var tmp1 = "<%=warnType%>";
var tmp2 = "<%=warnLevel%>";
var tmp3 = "<%=warnStatus%>";

if (tmp1 != "" && tmp1 != "null"){
	document.frm.warnType.value = tmp1;
}
if (tmp2 != "" && tmp2 != "null"){
	document.frm.warnLevel.value = tmp2;
}
if (tmp3 != "" && tmp3 != "null"){
	document.frm.warnStatus.value = tmp3;
}
</script>
<%@ include file="../foot.jsp"%>