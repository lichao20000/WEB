<%@ include file="../timelater.jsp"%>

<%@ page contentType="text/html;charset=GBK"%>
<%@ page import= "com.linkage.litms.webtopo.common.*" %>
<SCRIPT LANGUAGE="JavaScript">
<!--
//V1�汾
function ifupdatapmV1() {
	if (confirm('���ʽ�Ѿ��������ȷ��Ҫ���¶��壿'))
	{
		parent.frm.action="testDevPerDef_v1.jsp";
		parent.frm.submit();
	}
	else
	{
		return;
	}
}
//V2�汾
function ifupdatapmV2() {
	if (confirm('���ʽ�Ѿ��������ȷ��Ҫ���¶��壿'))
	{
		parent.frm.action="testDevPerDef_v2.jsp";
		parent.frm.submit();
	}
	else
	{
		return;
	}
}
//V3�汾
function ifupdatapmV3() {
	if (confirm('���ʽ�Ѿ��������ȷ��Ҫ���¶��壿'))
	{
		parent.frm.action="testDevPerDef_v3.jsp";
		parent.frm.submit();
	}
	else
	{
		return;
	}
}
//��֧��SNMP
function IsConfig(){
	alert("���豸δ����֤,��֧��SNMP!");
	return;
}
//-->
</SCRIPT>
<%@ include file="../head.jsp"%>

	<%
		String device_id=request.getParameter("device_id");
		String isconfig=request.getParameter("isconfig");
		DevPerDef dpd = new DevPerDef(request);
		//������֤���豸
		if(isconfig!=null && "true".equals(isconfig)){
			boolean b = dpd.is_Pmbeing(device_id);
			int V=dpd.QueryVersion(device_id);//��ð汾��Ϣ
			if (b) {//�Ƿ��Ѿ����ù�
				out.println("<SCRIPT LANGUAGE=\"JavaScript\">");
				if(V==1){
					out.println("ifupdatapmV1();");		
				}else if(V==2){
					out.println("ifupdatapmV2();");
				}else{
					out.println("ifupdatapmV3();");
				}
				out.println("</SCRIPT>");
			}else {
				out.println("<SCRIPT LANGUAGE=\"JavaScript\">");
				if(V==1){//V1�汾
					out.println("parent.frm.action=\"testDevPerDef_v1.jsp\";");
				}else if(V==2){//V2�汾
					out.println("parent.frm.action=\"testDevPerDef_v2.jsp\";");
				}else{//V3�汾
					out.println("parent.frm.action=\"testDevPerDef_v3.jsp\";");
				}	
				out.println("parent.frm.submit();");
				out.println("</SCRIPT>");
			}
		}else{//�ж��Ƿ񾭹���֤,����֤������������!
			boolean flg=dpd.isConfigV3(device_id);
			if(!flg){
				out.println("<SCRIPT LANGUAGE=\"JavaScript\">");
				out.println("IsConfig();");
				out.println("</SCRIPT>");
			}else{
				out.println("<SCRIPT LANGUAGE=\"JavaScript\">");
				out.println("parent.frm.action=\"testPmIsBeing_v3.jsp?isconfig=true\";");
				out.println("parent.frm.submit();");
				out.println("</SCRIPT>");
			}
		}
	%>
