<%--
FileName	: IpLineSave.jsp
Author		: liuli
Date		: 2007-3-06
Note		: �ʺ����ӡ��޸ġ�ɾ������
--%>
<html>
<%@ page import="java.util.*,com.linkage.litms.common.util.Encoder,com.linkage.litms.system.dbimpl.LogItem"%>
<jsp:useBean id="configMgr" scope="request" class="com.linkage.litms.paramConfig.ConfigureManager"/>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%
	request.setCharacterEncoding("GBK");
	//�������豸ID
	String strDevice = request.getParameter("device_id");
	String[] device_list = strDevice.split(",");
	String device_id = "";
	String device_name = "";
	// ��NTP��������ַ������
	String main_ntp_server = request.getParameter("main_ntp_server");
	// ��NTP��������ַ������
	String second_ntp_server = request.getParameter("second_ntp_server");
	// ����״̬
	String status = request.getParameter("status");
	// ���÷�ʽ
	String type = request.getParameter("type");
	// ���÷�ʽ������
	String typeDesc = "1".equals(type) ? "tr069" : "snmp";
	// �ɹ�����ֵ
	String successpercentStr = request.getParameter("successpercent");
	int successpercent = 0;
	if("".equals(successpercentStr))
		successpercent = 0;
	else
		successpercent = Integer.parseInt(successpercentStr);
	// ���Դ���(Ŀǰû����)
	String repeatnum = request.getParameter("repeatnum");
	
	// ����ͳ�Ƴɹ�����
	int successNum = 0;
	// �ɼ������ĳɹ���
	int curSuccessPercent = 0;
	
	String strlist = "";
	String tempStr = "";
	Map map = null;
	
	
	// �������õ��豸����
	int deviceNum = device_list.length;
	//------------�Ȳɼ��ɹ���------------
	
	for (int i=0;i < deviceNum;i++){
		device_id = device_list[i];
		Cursor cursor = DataSetBean.getCursor("select device_name from tab_gw_device where device_id='" + device_id + "'");
		Map fields = cursor.getNext();
		if (fields != null){
			device_name = (String)fields.get("device_name");
			map = configMgr.getNTPStatus(device_id,type);
			String main_server = "";
			String second_server = "";
			if (null != map && 0 < map.size()) {
				successNum ++;
			}
		}
	}
	// ��ǰ�ɼ����ĳɹ���
	curSuccessPercent = ( successNum * 100 ) / deviceNum;
	
	//------------------------
	// �ɼ����ĳɹ��ʵ�����ֵ�򲻽�������,���ڲ�����������
	if(curSuccessPercent >= successpercent){
		
		
		String msg_ = "���÷�ʽ��" + typeDesc + "��" + ("1".equals(status) ? ("����NTP״̬��������������NTP��������ַ��������" + main_ntp_server + "�����ñ�NTP��������ַ��������" + second_ntp_server) : "����NTP״̬������");
		msg_ = Encoder.toISO(msg_);
		
		strlist = "<table border='0' align='center' width='100%' cellpadding='0' cellspacing='1' bgcolor='#999999'>"
					+ "<tr>"
					+ "<td bgcolor='#FFFFFF' width='30%' nowrap>"
					+ "�豸����"
					+ "</td><td bgcolor='#FFFFFF' width='70%' nowrap>"
					+ "���ؽ��"
					+ "</td></tr>";
		
		
		
		for (int i=0;i < deviceNum;i++){
			device_id = device_list[i];
			Cursor cursor = DataSetBean.getCursor("select device_name from tab_gw_device where device_id='" + device_id + "'");
			Map fields = cursor.getNext();
			
			if (fields != null){
				device_name = (String)fields.get("device_name");
				String result_ = "ʧ��";
				map = configMgr.NTPConfigure(device_id,status,type,main_ntp_server,second_ntp_server);
				//Map map = null;
				//192.168.232.23
				//configMgr.testCreate("145","1.3.6.1.4.1.25506.8.22.2.1.1.1.36.172.32.42.6.3",2,"6");
				
				if(map==null || 0 == map.size()) {
					strlist += "<tr class='blue_foot'>";
					strlist += "<td bgcolor='#FFFFFF' width='30%' nowrap>";
					strlist += device_name;
					strlist += "</td>";
					strlist += "<td bgcolor='#FFFFFF' width='70%' nowrap>";
					strlist += "����ʧ��";
					strlist += "</td></tr>";
				}else{
					Set set = map.keySet();
					Iterator iterator = set.iterator();
					String name = null;
					String value = null;	
					while(iterator.hasNext()){
						name = (String)iterator.next();
						value = (String)map.get(name);
						if ((value != null && value.indexOf("���óɹ�") != -1) || "���óɹ�".equals(value) || "���óɹ�".equals(value)) {
							result_ = "�ɹ�";
						}
						strlist += "<tr class='blue_foot'>";
						strlist += "<td bgcolor='#FFFFFF' width='30%' nowrap>";
						strlist += name;
						strlist += "</td><td bgcolor='#FFFFFF' width='70%'>";
						strlist += value;
						strlist += "</td></tr>";
						
					}
				}
				
				LogItem.getInstance().writeItemLog_other(request, 1, device_id, msg_, Encoder.toISO(result_), "NTP����");
			
			}
		}
		
		strlist += "</table>";
	}else{
		strlist = "��ǰ�ɹ��� "+curSuccessPercent+"% С����ֵ "+successpercent+"% ,����������";
	}
	

%>

<body>
<SPAN ID="child"><%=strlist%></SPAN>
<SCRIPT LANGUAGE="JavaScript">
	parent.document.all("div_ping").innerHTML = child.innerHTML;
</SCRIPT>
</body>
</html>
