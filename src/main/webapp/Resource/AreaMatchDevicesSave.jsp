<%@ include file="../timelater.jsp"%>
<%@ page import= "com.linkage.litms.webtopo.MCControlManager,com.linkage.litms.midware.*,java.util.Map,java.util.HashMap" %>

<jsp:useBean id="areaManage" scope="request" class="com.linkage.litms.system.dbimpl.AreaManageSyb"/>
<%@ page contentType="text/html;charset=GBK"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
//֪ͨ��̨
function infoMc(flag){
	if(parseInt(flag) == 0){
		alert("֪ͨ��̨�ɹ�");
	}else{
		alert("֪ͨ��̨ʧ�ܣ������³��Ի���ϵ������Ա!");
	}
}
//-->
</SCRIPT>
<%
request.setCharacterEncoding("GBK");

String[] target = request.getParameterValues("target");
String area_id = request.getParameter("area_id");

String add_string = request.getParameter("add_string").trim();
String rmv_string = request.getParameter("rmv_string").trim();

if (LipossGlobals.getMidWare()) {
	
	MidWareManager midWareManager = new MidWareManager(request);
	
	String area_msg = midWareManager.getDevArea(area_id,add_string,rmv_string);

	%>
	<SCRIPT LANGUAGE="JavaScript">
	<!--
	alert('������Ϣ��' + '<%=area_msg%>');
	//-->
	</SCRIPT>
	<%
}

out.println("<script language=javascript>");

boolean flag = true;

if(null!=target){
	areaManage.refAreaInfo(area_id,target);
}
/*
ArrayList m_AreaPidList = new ArrayList();
ArrayList m_AreaIdList = new ArrayList();

m_AreaPidList.clear();
m_AreaIdList.clear();

m_AreaPidList.add(area_id);
m_AreaIdList = areaManage.getAreaIdsByAreaPid(m_AreaPidList,m_AreaIdList);
flag = areaManage.delDevicesOfAreaOther(m_AreaIdList,target);
*/
//���˲����õ�sql������ĺϲ�,ʹ��doBath����һ��ִ��
//flag = areaManage.delDeviceOfAreaOther(area_id);

//if(flag){
//	out.println("refreshAreaDevices("+ flag +");");
//}
if(flag) {
	out.println("alert(\"�������豸ˢ�³ɹ�\")");
	if (LipossGlobals.IsITMS() == false) {
		MCControlManager mc=new MCControlManager(user.getAccount(),user.getPasswd());
		int flagMC = mc.CallRegionChange(area_id,"1");
		out.println("infoMc(\""+ flagMC +"\");");
	}
} else {
	out.println("alert('������ˢ���豸ʧ�ܣ������³���')");
}


target = null;
out.println("</script>");
%>


