<%@ page contentType="text/html;charset=GBK"%>
<%@page import="com.linkage.litms.common.util.DateTimeUtil"%>
<%@page import="java.util.*"%>

<jsp:useBean id="StrategyBean" scope="request" class="com.linkage.litms.paramConfig.StrategyBean" />
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>

<%@ include file="../head.jsp"%>

<%
	request.setCharacterEncoding("GBK");
	
	Map fields = StrategyBean.detailStrategyInfo(request);

	Map customerFields = StrategyBean.detailCustomerInfo(request);
	
	Map<String, String> statusMap = new HashMap<String, String>();
	statusMap.put("0","�ȴ�ִ��");
	statusMap.put("1","Ԥ��PVC");
	statusMap.put("2","Ԥ���󶨶˿�");
	statusMap.put("3","Ԥ������");
	statusMap.put("4","ҵ���·�");
	statusMap.put("100","�������");
	
	Map<String, String> typeMap = new HashMap<String, String>();
	typeMap.put("0","����ִ��");
	typeMap.put("1","��һ������ϵͳ");
	typeMap.put("2","�����ϱ�");
	typeMap.put("3","��������");
	typeMap.put("4","�´�����ϵͳ");
	
	Map<String, String> businessStatusMap = new HashMap<String, String>();
	businessStatusMap.put("0","δ��");
	businessStatusMap.put("1","��ͨ�ɹ�");
	businessStatusMap.put("-1","��ͨʧ��");
	
	Map<String, String> resultIDMap = new HashMap<String, String>();
	resultIDMap.put("0","ϵͳδ֪����");
	resultIDMap.put("1","����ִ�гɹ�");
	resultIDMap.put("-1","�豸���Ӳ���");
	resultIDMap.put("-2","�豸û����Ӧ");
	resultIDMap.put("-3","ϵͳû�й���");
	resultIDMap.put("-4","ϵͳû���豸");
	resultIDMap.put("-5","ϵͳû��ģ��");
	resultIDMap.put("-6","�豸��������");
	resultIDMap.put("-7","ϵͳ��������");
	
	String time = "";
	String start_time = "";
	String end_time = "";
	String device_serialnumber = "";
	String status = "";
	String type = "";
	String resultMsg = "";
	if (null != fields) {

		DateTimeUtil dateTimeUtil = null;
		device_serialnumber = (String) fields.get("device_serialnumber");
		status = (String) fields.get("status");

		time = (String) fields.get("time");
		start_time = (String) fields.get("start_time");
		end_time = (String) fields.get("end_time");
		
		if (time != null && !time.equals("")) {
			dateTimeUtil = new DateTimeUtil(Long.parseLong(time) * 1000);
			time = dateTimeUtil.getLongDate();
			dateTimeUtil = null;
		}
		if (start_time != null && !start_time.equals("")) {
			dateTimeUtil = new DateTimeUtil(Long.parseLong(start_time) * 1000);
			start_time = dateTimeUtil.getLongDate();
			dateTimeUtil = null;
		}
		if (end_time != null && !end_time.equals("")) {
			dateTimeUtil = new DateTimeUtil(Long.parseLong(end_time) * 1000);
			end_time = dateTimeUtil.getLongDate();
			dateTimeUtil = null;
		}
		
		if ("0".equals((String)fields.get("result_id")) && "0".equals(status)) {
			resultMsg = "";
		} else {
			resultMsg = resultIDMap.get((String)fields.get("result_id"));
		}
		
		//if ("1".equals((String)fields.get("result_id")) && "100".equals(status)) {
		//	resultMsg = "ִ�гɹ�";
		//} else if ("1".equals(status)) {
		//	resultMsg = "";
		//} else {
		//	resultMsg = "ִ��ʧ��";
		//}
		
	} else {
	}
%>

<link href="../css/css_green.css" rel="stylesheet" type="text/css">
<SCRIPT LANGUAGE="JavaScript">

</SCRIPT>
<%@ include file="../toolbar.jsp"%>

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<TR>
		<TD>
		<TABLE width="95%" border=0 cellspacing=0 cellpadding=0 align="center">
			<TR>
				<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%"
					id="ssidTable">
					<TR>
						<TH colspan="10">IPTV����������Ϣ</TH>
					</TR>
					<TR bgcolor="#FFFFFF" height="20">
					<TD class=column align="right" width="20%">ҵ�����ͣ�</TD>
					<TD width="30%">IPTV�Žӿ���</TD>
					<TD class=column align="right" width="20%">�󶨶˿ڣ�</TD>
					<TD width="30%">LAN2�ں�WLAN2��</TD>
					</TR>
					<TR bgcolor="#FFFFFF" height="20">
					<TD class=column align="right" width="20%">�û�����˺ţ�</TD>
					<TD width="30%"><%=fields.get("username")%></TD>
					<TD class=column align="right" width="20%">�豸OUI��</TD>
					<TD width="30%"><%=fields.get("oui")%></TD>
					</TR>
					<TR bgcolor="#FFFFFF" height="20">
					<TD class=column align="right" width="20%">�豸���кţ�</TD>
					<TD width="30%"><%=device_serialnumber%></TD>
					<TD class=column align="right" width="20%">����״̬��</TD>
					<TD width="20%"><%=statusMap.get(status)%></TD>
					</TR>
					<TR bgcolor="#FFFFFF" height="20">
					<TD class=column align="right" width="20%">����ִ�н����</TD>
					<TD width="30%"><%=resultMsg%></TD>
					<TD class=column align="right" width="20%">����ִ�н��������</TD>
					<TD width="20%"><%=fields.get("result_desc")%></TD>
					</TR>
					<TR bgcolor="#FFFFFF" height="20">
					<TD class=column align="right" width="20%">PVC��</TD>
					<TD width="30%">PVC:<%=customerFields.get("vpiid")%>/<%=customerFields.get("vciid")%></TD>
					<TD class=column align="right" width="20%">��ͨ״̬��</TD>
					<TD width="20%"><%=businessStatusMap.get(customerFields.get("open_status"))%></TD>
					</TR>
					<TR bgcolor="#FFFFFF" height="20">
					<TD class=column align="right" width="20%">���Է�ʽ��</TD>
					<TD width="30%"><%=typeMap.get(fields.get("type"))%></TD>
					<TD class=column align="right" width="20%">����ʱ�䣺</TD>
					<TD width="30%"><%=time%></TD>
					</TR>
					<TR bgcolor="#FFFFFF" height="20">
					<TD class=column align="right" width="20%">��ʼִ��ʱ�䣺</TD>
					<TD width="30%"><%=start_time%></TD>
					<TD class=column align="right" width="20%">����ִ��ʱ�䣺</TD>
					<TD width="30%"><%=end_time%></TD>
					
					</TR>
				</TABLE>
				</TD>
			</TR>
		</TABLE>

		</TD>
	</TR>
</TABLE>

<%@ include file="../foot.jsp"%>

<%
	//DeviceAct = null;
	//CLEAR
	customerFields = null;
	fields = null;
	statusMap = null;
	typeMap = null;
	businessStatusMap = null;
%>








