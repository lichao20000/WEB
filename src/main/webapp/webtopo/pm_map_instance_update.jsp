<%@ include file="../timelater.jsp"%>
<%@ page import="com.linkage.litms.common.corba.interfacecontrol.PmeeInterface" %>
<%@ page contentType="text/html;charset=GBK"%>
<SCRIPT LANGUAGE="JavaScript">
<!--

//-->
</SCRIPT>
<%@ include file="../head.jsp"%>
<%	
	String id = null;//Ψһʵ��id
	String expressionid = null;//���ʽid
	String device_id = null;//�豸Ψһid
	String indexid = null;//ʵ������
	String descr = null;//ʵ������
	String collect = null;//�Ƿ�ɼ�
	String intodb = null;//ԭʼ�����Ƿ����

	String mintype = null;//�̶���ֵһ����
	String minthres = null;//�̶���ֵһ
	String mindesc = null;//�̶���ֵһ����
	String mincount = null;//����������ֵһ����
	String minwarninglevel = null;//�̶���ֵһ�澯����
	String minreinstatelevel = null;//�̶���ֵһ�ָ��澯����

	String maxtype = null;//�̶���ֵ������
	String maxthres = null;//�̶���ֵ��
	String maxdesc = null;//�̶���ֵ������
	String maxcount = null;//����������ֵ������
	String maxwarninglevel = null;//�̶���ֵ���澯����
	String maxreinstatelevel = null;//�̶���ֵ���ָ��澯����

	String dynatype = null;//�Ƿ�������̬��ֵ����
	String beforeday = null;//����ǰ������Ϊ��׼ֵ
	String dynathres = null;//��̬��ֵ(����78%���ڴ�Ϊ78)
	String dynacount = null;//����������̬��ֵ����
	String dynadesc = null;//��̬��ֵ����
	String dynawarninglevel = null;//��̬��ֵ�澯����
	String dynareinstatelevel = null;//��̬��ֵ�澯�ָ��澯����

	String mutationtype = null;//�Ƿ�����ͻ����ֵ����
	String mutationthres = null;//ͻ����ֵ
	String mutationcount = null;//��������ͻ����ֵ����
	String mutationdesc = null;//ͻ����ֵ����
	String mutationwarninglevel = null;//ͻ����ֵ�澯����
	String mutationreinstatelevel = null;//ͻ����ֵ�澯�ָ��澯����(ҳ����δʹ��)

	String remark1 = null;//�����ֶ�1(ҳ����δʹ��)
	String remark2 = null;//�����ֶ�2(ҳ����δʹ��)
%>
<%
	id = request.getParameter("id");
	device_id = request.getParameter("device_id");

	mintype = request.getParameter("compSign_1");
	minthres = request.getParameter("fixedness_value1");
	mindesc = request.getParameter("fixedness_value1desc");//varchar
	mincount = request.getParameter("seriesOverstep_value1");
	minwarninglevel = request.getParameter("send_warn1");
	minreinstatelevel = request.getParameter("renew_warn1");

	maxtype = request.getParameter("compSign_2");
	maxthres = request.getParameter("fixedness_value2");
	maxdesc = request.getParameter("fixedness_value2desc");//varchar
	maxcount = request.getParameter("seriesOverstep_value2");
	maxwarninglevel = request.getParameter("send_warn2");
	maxreinstatelevel = request.getParameter("renew_warn2");

	dynatype = request.getParameter("dynamic_OperateSign");
	beforeday = request.getParameter("benchmark_Value");
	dynathres = request.getParameter("valve_Percent");
	dynacount = request.getParameter("achieve_Percent2");
	dynadesc = request.getParameter("dynamic_Valve_desc");//varchar
	dynawarninglevel = request.getParameter("sdynamic_send_warn");
	dynareinstatelevel = request.getParameter("sdynamic_renew_warn");

	mutationtype = request.getParameter("mutation_OperateSign");
	mutationthres = request.getParameter("overstep_Percent");
	mutationcount = request.getParameter("achieve_Percent3");
	mutationdesc = request.getParameter("mutation_Valve_desc");//varchar
	mutationwarninglevel = request.getParameter("send_warn3");

	String updateInstanceSQL = "update pm_map_instance set mintype="
			+ mintype + ",minthres=" + minthres + ",mindesc='"
			+ mindesc + "',mincount=" + mincount + ",minwarninglevel="
			+ minwarninglevel + ",minreinstatelevel="
			+ minreinstatelevel + ",maxtype=" + maxtype + ",maxthres="
			+ maxthres + ",maxdesc='" + maxdesc + "',maxcount="
			+ maxcount + ",maxwarninglevel=" + maxwarninglevel
			+ ",maxreinstatelevel=" + maxreinstatelevel + ",dynatype="
			+ dynatype + ",beforeday=" + beforeday + ",dynathres="
			+ dynathres + ",dynacount=" + dynacount + ",dynadesc='"
			+ dynadesc + "',dynawarninglevel=" + dynawarninglevel
			+ ",dynareinstatelevel=" + dynareinstatelevel
			+ ",mutationtype=" + mutationtype + ",mutationthres="
			+ mutationthres + ",mutationcount=" + mutationcount
			+ ",mutationdesc='" + mutationdesc
			+ "',mutationwarninglevel=" + mutationwarninglevel
			+ " where id='" + id + "'";

	out.println("<SCRIPT LANGUAGE=\"JavaScript\">");
	//out.println("window.alert(\"" + updateInstanceSQL + "\");");
	int upResultArray[] = DataSetBean.doBatch(updateInstanceSQL);	
	if (null!=upResultArray)
	{
		int retflag = PmeeInterface.GetInstance().readDevices(new String[]{ device_id });
		if (retflag == 0)
		{
			out.println("window.alert(\"����ʵ�����³ɹ�,֪ͨ��̨�ɹ�\");");
		} else
		{
			out.println("window.alert(\"����ʵ�����³ɹ�,֪ͨ��̨ʧ��\");");
		}		
	} else
	{
		out.println("window.alert(\"����ʵ������ʧ��,�����²���\");");
	}
	//������ҳ����Ϣ��ʼ��
	out.println("parent.frm.id.value=\"\"");
	//�̶���ֵ����
	out.println("parent.frm.compSign_1.value=\"0\";");
	out.println("parent.frm.fixedness_value1.value=\"\";");
	out.println("parent.frm.fixedness_value1desc.value=\"\";");
	out.println("parent.frm.seriesOverstep_value1.value=\"1\";");

	out.println("parent.frm.send_warn1.value=\"0\";");

	out.println("parent.frm.renew_warn1.value=\"0\";");

	out.println("parent.frm.compSign_2.value=\"0\";");
	out.println("parent.frm.fixedness_value2.value=\"\";");
	out.println("parent.frm.fixedness_value2desc.value=\"\";");
	out.println("parent.frm.seriesOverstep_value2.value=\"1\";");

	out.println("parent.frm.send_warn2.value=\"0\";");

	out.println("parent.frm.renew_warn2.value=\"0\";");

	//��̬��ֵ����
	out.println("parent.frm.dynamic_OperateSign.value=\"0\";");
	out.println("parent.frm.benchmark_Value.value=\"1\";");
	out.println("parent.frm.valve_Percent.value=\"\";");
	out.println("parent.frm.achieve_Percent2.value=\"1\";");
	out.println("parent.frm.dynamic_Valve_desc.value=\"\";");

	out.println("parent.frm.sdynamic_send_warn.value=\"0\";");

	out.println("parent.frm.sdynamic_renew_warn.value=\"0\";");

	//ͻ�䷧ֵ����
	out.println("parent.frm.mutation_OperateSign.value=\"0\";");
	out.println("parent.frm.overstep_Percent.value=\"\";");
	out.println("parent.frm.achieve_Percent3.value=\"1\";");
	out.println("parent.frm.mutation_Valve_desc.value=\"\";");

	out.println("parent.frm.send_warn3.value=\"0\";");

	out.println("</SCRIPT>");
%>

