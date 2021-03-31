<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import ="com.linkage.litms.resource.DeviceAct,java.util.*"%>
<jsp:useBean id="paramManage" scope="request" class="com.linkage.litms.paramConfig.ParamManage"/>
<jsp:useBean id="sheetManage" scope="request" class="com.linkage.litms.netcutover.SheetManage"/>
<jsp:useBean id="fileSevice" scope="request" class="com.linkage.litms.resource.FileSevice"/>
<%
request.setCharacterEncoding("GBK");


//ҵ��ID
String device_id = request.getParameter("device_id");
String service_id = request.getParameter("service_id");
String username = request.getParameter("username");
String passwd = request.getParameter("passwd");
String vpiid = request.getParameter("vpiid");
String vciid = request.getParameter("vciid");
String wan_type = request.getParameter("wan_type");
String oui = request.getParameter("oui");
String device_serialnumber = request.getParameter("device_serialnumber");

String pvc = vpiid + "/" + vciid;

String errorSheet="";


//�����豸 �����ݿ��в�ѯ��òɼ���
DeviceAct act = new DeviceAct();
HashMap deviceInfo= act.getDeviceInfo(device_id);
String gather_id = (String)deviceInfo.get("gather_id");
String devicetype_id = (String)deviceInfo.get("devicetype_id");

//����Ϣ��װ��map
Map map = new HashMap();
map.put("curUser", (UserRes)(request.getSession().getAttribute("curUser")));
map.put("device_id",device_id);
map.put("service_id",service_id);
map.put("devicetype_id",devicetype_id);
map.put("gather_id",gather_id);

//ȡ��WANConnection�Ľ���WANPPPConnection�Ľ��ֵ
String ior = user.getIor("ACS_" + gather_id,"ACS_Poa_" + gather_id);
String connNodeAndpppNode = fileSevice.getInternetPVCNode(device_id, gather_id, ior, pvc);
String wconnNode = connNodeAndpppNode.split(",")[0];
String wpppConnNode = connNodeAndpppNode.split(",")[1];

//�쳣�������
if (null == wconnNode || "-1".equals(wconnNode)) {
	response.sendRedirect("excuError.jsp?msg=connError&returnJsp=eGuard_Form.jsp");
	return;
} else if (null == wconnNode || "".equals(wconnNode)) {
	response.sendRedirect("excuError.jsp?msg=noNodeError&returnJsp=eGuard_Form.jsp");
	return;
}

//���ݲ�ͬ�豸���ò�ͬģ�崦��
int parasNum = 0;
String countTemplateParas = sheetManage.countTemplateParas(devicetype_id, service_id);
if (null != countTemplateParas && !"".equals(countTemplateParas)) {
	parasNum = Integer.parseInt(countTemplateParas);
}

//����ҳ�������������װ�õ�����
String[] ArrHandSheet = sheetManage.getParasArr(parasNum, wconnNode, wpppConnNode, request);
if (null == ArrHandSheet) {
	response.sendRedirect("excuError.jsp?msg=noTemplateError&returnJsp=eGuard_Form.jsp");
	return;
}
//-----------------------------------------------------------------------------------------

String strSql = "";
//int reval = sheetManage.updateEgwSheet(request);
//��⹤����Ϣ���û���ִ̨��
String sheet_id = sheetManage.getSheetReport(map, ArrHandSheet);
//���صĹ���ID ���ִ�����ת��ʧ��ҳ��
if (sheet_id == null || sheet_id.length() < 1) {
	response.sendRedirect("excuError.jsp?returnJsp=eGuard_Form.jsp");
} else {
	strSql = "select *  from tab_sheet_report where sheet_id ='" + sheet_id + "'";
}
%>

<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
function init(){
    var tmpSql = "<%=strSql%>";
    var service_id = "<%= service_id%>";
    var username = "<%= username%>";
    var passwd = "<%= passwd%>";
	var wan_type = "<%= wan_type%>";
	var vpiid = "<%= vpiid%>";
	var vciid = "<%= vciid%>";
    
	//var page = "config_sheetList.jsp?tmpSql=" + tmpSql + "&service_id=" + service_id + "&username=" + username + "&passwd=" + passwd  + "&wan_type=" + wan_type + "&vpiid=" + vpiid + "&vciid=" + vciid;
	//document.all("childFrm").src = page;
	
	location.href="resultByRunAfter.action?goback=../NetCutover/eGuard_Form.jsp&gobefore=../NetCutover/WorkSheetView_device.jsp";
	
	showMsgDlg();
}
function showMsgDlg(){
	w = document.body.clientWidth;
	h = document.body.clientHeight;

	l = (w-250)/2;
	t = (h-60)/2;
	PendingMessage.style.left = l;
	PendingMessage.style.top  = t;
	PendingMessage.style.display="";
}
function closeMsgDlg(){
	PendingMessage.style.display="none";
}
function doDbClick(o){
	parames = o.parames;
	arrParame = parames.split(",");
	sheet_id = arrParame[0];
	receive_time = arrParame[1];
	gather_id = arrParame[2];
	page = "sheet_detail.jsp?sheet_id="+ sheet_id + "&receive_time="+ receive_time;

	window.open(page,new Date().getTime(),"left=20,top=20,width=500,height=420,resizable=no,scrollbars=no");
}

//-->
</SCRIPT>
<%@ include file="../toolbar.jsp"%>
<div id="PendingMessage"
	style="position:absolute;z-index:3;top:240px;left:250px;width:250;height:60;border-width:1;border-style:ridge;background-color:#eeeeee;padding-top:10px;display:none">
<center>
<table border="0">
	<tr>
		<td valign="middle"><img src="../images/cursor_hourglas.gif"
			border="0" WIDTH="30" HEIGHT="30"></td>
		<td>&nbsp;&nbsp;</td>
		<td valign="middle">
			<span id=txtLoading style="font-size:14px;font-family: ����">���Եȡ�����������</span>
		</td>
	</tr>
</table>
</center>
</div>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<FORM name="frm" action="" >
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>
	<TABLE width="90%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR><td>
			<Div id="idList">
			</Div>
			</td>
		</TR>
	</TABLE>
</TD></TR>
	<TR>
		<TD HEIGHT=10>&nbsp;<IFRAME ID=childFrm SRC="about:blank" STYLE="display:none"></IFRAME>
		<IFRAME ID=childFrm2 SRC="about:blank" STYLE="display:none"></IFRAME>
		<IFRAME ID=childFrm3 SRC="" STYLE="display:none"></IFRAME>
		</TD>
	</TR>
</TABLE>
<SCRIPT LANGUAGE="JavaScript">
<!--
var erSheet = "<%=errorSheet%>";
if(erSheet == ""){
	alert("�ɹ��ҵ�������ӣ�����ʼ���ã�");
}else{
	alert("������<%=errorSheet%>���������豸����ִ�У�");
}
init();
//-->
</SCRIPT>
<%@ include file="../foot.jsp"%>
