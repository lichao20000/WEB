<%@ include file="../timelater.jsp"%>
<%@ page import="com.linkage.litms.common.corba.interfacecontrol.PmeeInterface" %>
<%@ page contentType="text/html;charset=GBK"%>
<SCRIPT LANGUAGE="JavaScript">
<!--

//-->
</SCRIPT>
<%@ include file="../head.jsp"%>
<%	
	String id = null;//唯一实例id
	String expressionid = null;//表达式id
	String device_id = null;//设备唯一id
	String indexid = null;//实例索引
	String descr = null;//实例描述
	String collect = null;//是否采集
	String intodb = null;//原始数据是否入库

	String mintype = null;//固定阈值一类型
	String minthres = null;//固定阈值一
	String mindesc = null;//固定阈值一描述
	String mincount = null;//连续超出阈值一次数
	String minwarninglevel = null;//固定阈值一告警级别
	String minreinstatelevel = null;//固定阈值一恢复告警级别

	String maxtype = null;//固定阈值二类型
	String maxthres = null;//固定阈值二
	String maxdesc = null;//固定阈值二描述
	String maxcount = null;//连续超出阈值二次数
	String maxwarninglevel = null;//固定阈值二告警级别
	String maxreinstatelevel = null;//固定阈值二恢复告警级别

	String dynatype = null;//是否启动动态阈值类型
	String beforeday = null;//几天前的数据为基准值
	String dynathres = null;//动态阈值(例如78%，在此为78)
	String dynacount = null;//连续超出动态阈值次数
	String dynadesc = null;//动态阈值描述
	String dynawarninglevel = null;//动态阈值告警级别
	String dynareinstatelevel = null;//动态阈值告警恢复告警级别

	String mutationtype = null;//是否启动突变阈值类型
	String mutationthres = null;//突变阈值
	String mutationcount = null;//连续超出突变阈值次数
	String mutationdesc = null;//突变阈值描述
	String mutationwarninglevel = null;//突变阈值告警级别
	String mutationreinstatelevel = null;//突变阈值告警恢复告警级别(页面上未使用)

	String remark1 = null;//保留字段1(页面上未使用)
	String remark2 = null;//保留字段2(页面上未使用)
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
			out.println("window.alert(\"性能实例更新成功,通知后台成功\");");
		} else
		{
			out.println("window.alert(\"性能实例更新成功,通知后台失败\");");
		}		
	} else
	{
		out.println("window.alert(\"性能实例更新失败,请重新操作\");");
	}
	//将所有页面信息初始化
	out.println("parent.frm.id.value=\"\"");
	//固定阀值配置
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

	//动态阀值配置
	out.println("parent.frm.dynamic_OperateSign.value=\"0\";");
	out.println("parent.frm.benchmark_Value.value=\"1\";");
	out.println("parent.frm.valve_Percent.value=\"\";");
	out.println("parent.frm.achieve_Percent2.value=\"1\";");
	out.println("parent.frm.dynamic_Valve_desc.value=\"\";");

	out.println("parent.frm.sdynamic_send_warn.value=\"0\";");

	out.println("parent.frm.sdynamic_renew_warn.value=\"0\";");

	//突变阀值配置
	out.println("parent.frm.mutation_OperateSign.value=\"0\";");
	out.println("parent.frm.overstep_Percent.value=\"\";");
	out.println("parent.frm.achieve_Percent3.value=\"1\";");
	out.println("parent.frm.mutation_Valve_desc.value=\"\";");

	out.println("parent.frm.send_warn3.value=\"0\";");

	out.println("</SCRIPT>");
%>

