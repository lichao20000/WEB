<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>

<%
    /**
     *因为点击下一页或者跳转的时候，页面会刷新，所以上一次从主界面传入的参数将会失效，因为我们在 lk:pages
     *标签中设置的Action没有带入我们的参数!
     **/
	String city_id;
	String type;
    city_id = (String) request.getAttribute("city_id");
	type = (String) request.getAttribute("dataType");
	if (city_id == null || type == null) {
		 city_id = (String) request.getParameter("city_id");
		 type = (String) request.getParameter("dataType");
	}else{
		
	}
	request.setAttribute("city_id", city_id);
	request.setAttribute("dataType", type);
	System.out.println(city_id);
	System.out.println(type);
%>


<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<script type="text/javascript"
	src="<s:url value="/Js/jsDate/WdatePicker.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/edittable.js"/>"></SCRIPT>
<script type="text/javascript">
    var city_id=<%=city_id%>;
    var type=<%=type%>;
	function DetailListToExcel(cityId, startTime, endTime) {
		if (city_id == "0" || city_id == 0) {
			city_id = "00";
		}
		var page = "<s:url value='/itms/report/queryOpenBusiness!getDetailExcel.action'/>?"
				+ "&starttime="
				+ startTime
				+ "&city_id="
				+ city_id
				+ "&endtime=" + endTime
				/* 等于号不能省略，否则页面纳入发显示 */
				+ "&dataType=" + type;
		document.all("childFrm").src = page;
	}
</script>
<table class="listtable">
	<caption>用户列表</caption>
	<thead>
		<tr>
			<th>属地</th>
			<th>逻辑SN</th>
			<th>设备序列号</th>
			<th>绑定时间</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="listDetail.size()>0">
			<s:iterator value="listDetail">
				<tr>
					<td><s:property value="city_name" /></td>
					<td><s:property value="username" /></td>
					<td><s:property value="device_serialnumber" /></td>
					<td><s:property value="binddate" /></td>
				</tr>
			</s:iterator>
		</s:if>
		<s:else>
			<tr>
				<td colspan=<s:property value='isJSITMS=="1"?9:8' />>
					系统没有相关的设备信息!</td>
			</tr>
		</s:else>
	</tbody>
	<!-- 表格的底端 -->
	<tfoot>
		<tr>
			<td colspan=<s:property value='isJSITMS=="1"?9:8' />>
				<!-- 分页 --> <span style="float: right;"> <lk:pages
						url="/itms/report/queryOpenBusiness!gopageUserList.action"
						styleClass="" showType="" isGoTo="true" changeNum="true" />
			</span> <!-- 根据当前会话来决定是不是报表 --> <s:if test='#session.isReport=="1"'>
					<IMG SRC="<s:url value="../../images/excel.gif"/>" BORDER='0'
						ALT='导出列表' style='cursor: hand'
						onclick="DetailListToExcel('<s:property value="city_id"/>','<s:property value="starttime"/>','<s:property value="endtime"/>')">
				</s:if>
			</td>
		</tr>


		<TR>
			<TD align="center" colspan=<s:property value='isJSITMS=="1"?9:8' />>
				<button onclick="javascript:window.close();">&nbsp;关
					闭&nbsp;</button>
			</TD>
		</TR>
	</tfoot>

	<tr STYLE="display: none">
		<td colspan="5"><iframe id="childFrm" src=""></iframe></td>
	</tr>
</table>

<%@ include file="/foot.jsp"%>