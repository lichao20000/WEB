<%@ include file="../timelater.jsp"%>

<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.webtopo.common.*"%>
<%@ page import="com.linkage.commons.db.DBUtil" %>
<SCRIPT LANGUAGE="JavaScript">
<!--

//-->
</SCRIPT>
<%@ include file="../head.jsp"%>

<%
	String id = request.getParameter("id");
	String queryInstanceSQL = "select * from pm_map_instance where id='"
			+ id + "'";
	// teledb
	if (DBUtil.GetDB() == 3) {
		queryInstanceSQL = "select mintype, mindesc, minthres, mincount, minwarninglevel, minreinstatelevel, maxtype, " +
				"maxdesc, maxcount, maxthres, maxwarninglevel, maxreinstatelevel, dynatype, dynadesc, dynacount, " +
				"beforeday, dynathres, dynawarninglevel, dynareinstatelevel, mutationtype, mutationdesc, mutationcount," +
				" mutationthres, mutationwarninglevel, mutationreinstatelevel, remark1, remark2" +
				" from pm_map_instance where id='"
				+ id + "'";
	}

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

	com.linkage.commons.db.PrepareSQL psql = new com.linkage.commons.db.PrepareSQL(queryInstanceSQL);
	psql.getSQL();
	Cursor cursorinstance = DataSetBean.getCursor(queryInstanceSQL);
	Map myMap = cursorinstance.getNext();
	if (myMap != null)
	{
		mintype = (String) myMap.get("mintype");
		mindesc = (String) myMap.get("mindesc");
		minthres = (String) myMap.get("minthres");
		mincount = (String) myMap.get("mincount");
		minwarninglevel = (String) myMap.get("minwarninglevel");
		minreinstatelevel = (String) myMap.get("minreinstatelevel");

		maxtype = (String) myMap.get("maxtype");
		maxdesc = (String) myMap.get("maxdesc");
		maxcount = (String) myMap.get("maxcount");
		maxthres = (String) myMap.get("maxthres");
		maxwarninglevel = (String) myMap.get("maxwarninglevel");
		maxreinstatelevel = (String) myMap.get("maxreinstatelevel");

		dynatype = (String) myMap.get("dynatype");
		dynadesc = (String) myMap.get("dynadesc");
		dynacount = (String) myMap.get("dynacount");
		beforeday = (String) myMap.get("beforeday");
		dynathres = (String) myMap.get("dynathres");
		dynawarninglevel = (String) myMap.get("dynawarninglevel");
		dynareinstatelevel = (String) myMap.get("dynareinstatelevel");

		mutationtype = (String) myMap.get("mutationtype");
		mutationdesc = (String) myMap.get("mutationdesc");
		mutationcount = (String) myMap.get("mutationcount");
		mutationthres = (String) myMap.get("mutationthres");
		mutationwarninglevel = (String) myMap
				.get("mutationwarninglevel");
		mutationreinstatelevel = (String) myMap
				.get("mutationreinstatelevel");

		remark1 = (String) myMap.get("remark1");
		remark2 = (String) myMap.get("remark2");

		out.println("<SCRIPT LANGUAGE=\"JavaScript\">");
		//��ʵ��ID����ȥ
		out.println("parent.frm.id.value=\"" + id + "\";");
		//�̶���ֵ����
		out.println("parent.frm.compSign_1.value=\"" + mintype + "\";");
		out.println("parent.frm.fixedness_value1.value=\"" + minthres
				+ "\";");
		out.println("parent.frm.fixedness_value1desc.value=\""
				+ mindesc + "\";");
		out.println("parent.frm.seriesOverstep_value1.value=\""
				+ mincount + "\";");
		if (minwarninglevel != null && !"".equals(minwarninglevel))
		{
			out.println("parent.frm.send_warn1.value=\""
					+ minwarninglevel + "\";");
		} else
		{
			out.println("parent.frm.send_warn1.value=\"" + 0 + "\";");
		}
		if (minreinstatelevel != null && !"".equals(minreinstatelevel))
		{
			out.println("parent.frm.renew_warn1.value=\""
					+ minreinstatelevel + "\";");
		} else
		{
			out.println("parent.frm.renew_warn1.value=\"" + 0 + "\";");
		}

		out.println("parent.frm.compSign_2.value=\"" + maxtype + "\";");
		out.println("parent.frm.fixedness_value2.value=\"" + maxthres
				+ "\";");
		out.println("parent.frm.fixedness_value2desc.value=\""
				+ maxdesc + "\";");
		out.println("parent.frm.seriesOverstep_value2.value=\""
				+ maxcount + "\";");
		if (maxwarninglevel != null && !"".equals(maxwarninglevel))
		{
			out.println("parent.frm.send_warn2.value=\""
					+ maxwarninglevel + "\";");
		} else
		{
			out.println("parent.frm.send_warn2.value=\"" + 0 + "\";");
		}
		if (maxreinstatelevel != null && !"".equals(maxreinstatelevel))
		{
			out.println("parent.frm.renew_warn2.value=\""
					+ maxreinstatelevel + "\";");
		} else
		{
			out.println("parent.frm.renew_warn2.value=\"" + 0 + "\";");
		}
		//��̬��ֵ����
		out.println("parent.frm.dynamic_OperateSign.value=\""
				+ dynatype + "\";");
		out.println("parent.frm.benchmark_Value.value=\"" + beforeday
				+ "\";");
		out.println("parent.frm.valve_Percent.value=\"" + dynathres
				+ "\";");
		out.println("parent.frm.achieve_Percent2.value=\"" + dynacount
				+ "\";");
		out.println("parent.frm.dynamic_Valve_desc.value=\"" + dynadesc
				+ "\";");
		if (dynawarninglevel != null && !"".equals(dynawarninglevel))
		{
			out.println("parent.frm.sdynamic_send_warn.value=\""
					+ dynawarninglevel + "\";");
		} else
		{
			out.println("parent.frm.sdynamic_send_warn.value=\"" + 0
					+ "\";");
		}
		if (dynareinstatelevel != null
				&& !"".equals(dynareinstatelevel))
		{
			out.println("parent.frm.sdynamic_renew_warn.value=\""
					+ dynareinstatelevel + "\";");
		} else
		{
			out.println("parent.frm.sdynamic_renew_warn.value=\"" + 0
					+ "\";");
		}
		//ͻ�䷧ֵ����
		out.println("parent.frm.mutation_OperateSign.value=\""
				+ mutationtype + "\";");
		out.println("parent.frm.overstep_Percent.value=\""
				+ mutationthres + "\";");
		out.println("parent.frm.achieve_Percent3.value=\""
				+ mutationcount + "\";");
		out.println("parent.frm.mutation_Valve_desc.value=\""
				+ mutationdesc + "\";");
		if (mutationwarninglevel != null
				&& !"".equals(mutationwarninglevel))
		{
			out.println("parent.frm.send_warn3.value=\""
					+ mutationwarninglevel + "\";");
		} else
		{
			out.println("parent.frm.send_warn3.value=\"" + 0 + "\";");
		}

		out.println("</SCRIPT>");
	}
%>