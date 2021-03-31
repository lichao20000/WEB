<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.webtopo.PmeeDataList"%>
<%@ page import="java.util.*"%>
<%@ page import="com.linkage.litms.performance.CommonSearch"%>
<%@ page import="com.linkage.litms.system.dbimpl.DbUserRes"%>
<%@ page import="com.linkage.litms.common.util.CommonMap"%>
<%@ page import="com.linkage.litms.performance.GeneralNetPerf"%>
<%@ page import="com.linkage.litms.common.util.StringUtils"%>
<%request.setCharacterEncoding("GBK");
			String strSQL , tmpSQL;
			Cursor cursor;
			Map fields;
			String year , month;
			//获取页面传递的参数
			PmeeDataList pmeeData = new PmeeDataList();
			Date now = new Date();
			Calendar cale = Calendar.getInstance();
			cale.setTime(now);
			long end = cale.getTimeInMillis() / 1000;
			long start = end - 3600;
			Long tempE = new Long(end);
			int iEnd = tempE.intValue();
			Long tempS = new Long(start);
			int iStart = tempS.intValue();
			String expressionid = request.getParameter("expressionid");
			String id = request.getParameter("ip");
			String[] arr_iid = pmeeData.getPM_instance_ID(expressionid, id);
			String sortcolumn = "avgvalue";
			//将变量保存到session
			Map req_data = new HashMap();
			req_data.put("start", start + "");
			req_data.put("expressionid", expressionid);
			req_data.put("id", id);
			session.setAttribute("req_data", req_data);
			//格式化实例索引
			String str_iid = null;
			for (int i = 0; i < arr_iid.length; i++)
			{
				if (str_iid == null)
					str_iid = "'" + arr_iid[i] + "'";
				else
					str_iid += ",'" + arr_iid[i] + "'";
			}
			//获取表达式名字
			Map expressionMap = CommonSearch.getExpressionMap(expressionid);
			//获取设备名称
			Map deviceMap = CommonSearch.getDeviceMap(id);
			//获取实例索引及相关信息
			Map indexMap = CommonSearch.getIndexMap(str_iid);
			DbUserRes dbUserRes = (DbUserRes) session.getAttribute("curUser");
			List gather_id = dbUserRes.getUserProcesses();
			Map ipMap = CommonMap.getDeviceIPMap(gather_id);
			//根据时间格式化小时报表表名
			String[] arr = StringUtils.secondToDateStr(iStart);
			year = arr[0];
			month = arr[1];
			String tblname = "pm_hour_stats_" + year + "_" + month;
			//获取表达式名称和单位
			String ex_name , ex_unit;
			String s = (String) expressionMap.get(expressionid);
			arr = s.split(",");
			ex_name = arr[0];
			ex_unit = arr[1];

			GeneralNetPerf netPM = new GeneralNetPerf(start, end, 1, arr_iid);
			cursor = netPM.getGeneralTxtData();
			fields = cursor.getNext();
%>


<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--

function checkForm(){
	var iStart;
	
	iStart = DateToDes(document.frm.start.value);


	if(!IsNull(document.frm.start.value,"日期")){
		document.frm.start.focus();
		document.frm.start.select();
		return false;
	}
	
	else{
		document.frm.hidstart.value = iStart;
		idLayerView.style.width = document.body.clientWidth * 0.98;
		idLayerView.style.display = "";
		idLayerView.innerHTML = "正在载入数据......";

		return true;
	}
}

function DateToDes(v){
	if(v != ""){
		pos = v.indexOf("-");
		if(pos != -1 && pos == 4){
			y = parseInt(v.substring(0,pos));
			v = v.substring(pos+1,v.length);
		}
		pos = v.indexOf("-");
		if(pos != -1){
			m = parseInt(v.substring(0,pos));
			v = v.substring(pos+1,v.length);
		}
		if(v.length>0)
			d = parseInt(v);

		dt = new Date(m+"/"+d+"/"+y);
		var s  = dt.getTime();
		return s/1000;
	}
	else
		return 0;
}

function DateToDesMonth(v){
	if(v != ""){
		pos = v.indexOf("-");
		if(pos != -1 && pos == 4){
			y = parseInt(v.substring(0,pos));
			v = v.substring(pos+1,v.length);
		}

		m = parseInt(v);

		dt = new Date(m+"/01/"+y);
		var s  = dt.getTime();
		dt = new Date((m+1)+"/01/"+y);
		document.frm.hidend.value = dt.getTime()/1000;
		return s/1000;
	}
	else
		return 0;
}

function searchType(){
	var type=0;
	for(var i=0;i<document.frm.SearchType.length;i++){
		 if(document.frm.SearchType[i].checked){
				type=document.frm.SearchType[i].value;
				break;
		  }
	}

	if(type==1){
//		document.all("title").innerHTML="〖小时报表〗";
		document.all("hour").style.display="";
	}else if(type==2){
//		document.all("title").innerHTML="〖日报表〗";
		document.all("hour").style.display="none";
		document.frm.action = "webtopo_PMReportDataDay.jsp";
	}else if(type==3){
//		document.all("title").innerHTML="〖周报表〗";
		document.all("hour").style.display="none";
		document.frm.action = "webtopo_PMReportDataWeek.jsp";
	}else if(type==4){
//		document.all("title").innerHTML="〖月报表〗";
		document.all("hour").style.display="none";
		document.frm.action = "webtopo_PMReportDataMonth.jsp";
	}

}


/****************************************
 * init the date and hour
 * @author Yanhj
 * @date 2005-12-12
 ****************************************/
function initTime(){
	var vDate = new Date();
	lms = vDate.getTime();
	vDate = new Date(lms-3600*24*1000);
	var y  = vDate.getYear();
	var m  = vDate.getMonth()+1;
	var d  = vDate.getDate();
	var h  = vDate.getHours(); 
	
	document.frm.start.value = y+"-"+m+"-"+d;

	document.frm.hour.value = h-1;
}

//-->
</SCRIPT>


<TABLE border=0 cellspacing=0 cellpadding=0 width="98%" align="center">
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<TR>
		<TD >
		<FORM METHOD=POST ACTION="webtopo_PMReportData.jsp" NAME="frm"
			onsubmit="return checkForm()" target="childFrm">
		<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
			<TR>
				<TD bgcolor=#999999>
				<TABLE class=text cellSpacing=1 cellPadding=1 width="100%"
					align=center bgColor=#999999 border=0>
					<TR>
						<TH colspan=4>设备性能查询</TH>
					</TR>
					<TR class=blue_trOut onmouseout="className='blue_trOut'"
						bgColor=#ffffff>
						<TD class="column" align='right' width='30%'>&nbsp;&nbsp;报表类型</TD>
						<TD colspan="3" class=""><INPUT TYPE="radio" NAME="SearchType"
							CLASS="bk" VALUE="1" checked onClick="javascript:searchType();">小时报表
						<INPUT TYPE="radio" NAME="SearchType" CLASS="bk" VALUE="2"
							onClick="javascript:searchType();">日报表 <INPUT TYPE="radio"
							NAME="SearchType" CLASS="bk" VALUE="3"
							onClick="javascript:searchType();">周报表 <INPUT TYPE="radio"
							NAME="SearchType" CLASS="bk" VALUE="4"
							onClick="javascript:searchType();">月报表</TD>
					</TR>
					<TR class=blue_trOut onmouseout="className='blue_trOut'"
						bgColor=#ffffff>
						<TD class="column" align='right'>&nbsp;&nbsp;选择日期</TD>
						<TD class="" colspan=3><INPUT TYPE="text" NAME="start" class=bk><INPUT
							TYPE="button" value="▼" class=jianbian
							onclick="showCalendar('day',event)"> &nbsp;<font color="#FF0000">*</font>
						<INPUT TYPE="hidden" name="hidstart"> <select name="hour" class=bk>
							<%for (int i = 0; i < 24; i++)
			{%>
							<option value="<%=i%>"><%=i%>点--<%=i + 1%>点</option>
							<%}%>
						</select> <INPUT TYPE="hidden" value=<%= expressionid%>
							name="expressionid"> <INPUT TYPE="hidden" value=<%= id%>
							name="ip"></TD>
					</TR>
					<TR class=blue_trOut onmouseout="className='blue_trOut'"
						bgColor=#ffffff>
						<TD colspan=4 class=green_foot align=right><INPUT TYPE="submit"
							name="cmdOK" value=" 查 询 " class=""></TD>
					</TR>
				</TABLE>
				</TD>
			</TR>
		</TABLE>
		</FORM>
		</TD>
	</TR>
	<TR>
		<TD align=center>
		<DIV id="idLayerView" style="overflow:auto;display:" width="100%">

		</DIV>
		</TD>
	</TR>
	<TR>
		<TD HEIGHT=20>&nbsp;<IFRAME name="childFrm" ID=childFrm SRC=""
			STYLE="display:none;width:500;height:500"></IFRAME></TD>
	</TR>
</TABLE>

<%-- Added by Yanhj at 2005-12-12 for initialize time --%>
<SCRIPT LANGUAGE="JavaScript">
<!--
initTime();
//-->
</SCRIPT>

<%@ include file="../foot.jsp"%>
