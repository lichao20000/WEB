<%@ page contentType="text/html;charset=GBK"%>
<%@page import="com.linkage.litms.paramConfig.ScheludeJsp"%>
<%
request.setCharacterEncoding("GBK");
//String strGatherId = request.getParameter("gather_id");
String sheet_id_str = request.getParameter("sheet_id_str");
String receive_time_str = request.getParameter("receive_time_str");
//�ֽ⹤�����
String[] sheet_id_array = sheet_id_str.split(";");

//�ֽ�ʱ���
String[] receive_time_array = receive_time_str.split(";");

ScheludeJsp schelude = new ScheludeJsp();

//����sheet_id��ȡ��gather_id
String strGatherId = schelude.getGatherId(sheet_id_array[0]);

//����sheet_id��ȡЭ������prot_id
String portID = schelude.getSheetType(sheet_id_array[0]);

if ("2".equals(portID)){
	schelude.sheetInformSNMP(strGatherId,sheet_id_array,receive_time_array); 
}
else{
	schelude.sheetInform(strGatherId,sheet_id_array,receive_time_array); 
}


//clear parameter
strGatherId = null;
schelude = null;
sheet_id_array = null;
sheet_id_str = null;
%>