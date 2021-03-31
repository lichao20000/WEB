<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<script type="text/javascript"
	src="<s:url value="/Js/jsDate/WdatePicker.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/edittable.js"/>"></SCRIPT>

<script type="text/javascript">

function ListToExcel(cityId,status,type,starttime,endtime) {
	
	var page="<s:url value='/gtms/config/voipChangeCount!getDevExcel.action'/>?"
		+ "cityId=" + cityId 		
		+ "&status=" +status;
	document.all("childFrm").src=page;
}

//查看itms用户相关的信息
function devInfo(device_id){
	var strpage="<s:url value='/Resource/DeviceShow.jsp'/>?device_id=" + device_id;
	window.open(strpage,"","left=20,top=20,width=600,height=450,resizable=yes,scrollbars=yes");
}
</script>

<table class="listtable">
	<caption>
		业务列表
	</caption>
	<thead>
		<tr>
			<th>
				LOID
			</th>
			<th>
				属地
			</th>
			<th>
				终端标识类型
			</th>
			<th>
				主用MGC地址
			</th>
			<th>
				主用MGC端口
			</th>
			<th>
				备用MGC地址
			</th>
			<th>
				备用MGC端口
			</th>
			<th>
				新IMS侧终端物理标识
			</th>	
			<th>
				临时终结点标识前缀
			</th>		
			<th>
				操作
			</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="data.size()>0">
			<s:iterator value="data">
				<tr>
					<td>
						<s:property value="username" />
					</td>
					<td>
						<s:property value="city_name" />
					</td>
					<td>
						<s:property value="reg_id_type_itms" />
					</td>
					<td>
						<s:property value="prox_serv" />
					</td>
					<td>
						<s:property value="prox_port" />
					</td>
					<td>
						<s:property value="stand_prox_serv" />
					</td>
					<td>
						<s:property value="stand_prox_port" />
					</td>
					<td>
						<s:property value="voip_port" />
					</td>
					<td>
						<s:property value="rtp_prefix" />
					</td>
					<td>
						<IMG SRC="<s:url value="/images/view.gif"/>" BORDER="0" ALT="详细信息"
							onclick="devInfo('<s:property value="device_id"/>')"
							style="cursor: hand">
					</td>
				</tr>
			</s:iterator>
		</s:if>
		<s:else>
			<tr>
				<td colspan=10>
					系统没有相关的用户信息!
				</td>
			</tr>
		</s:else>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="10">
				<span style="float: right;"> <lk:pages
						url="/gtms/config/voipChangeCount!getDev.action" styleClass=""
						showType="" isGoTo="true" changeNum="true" /> </span>
				<s:if test='#session.isReport=="1"'>
					<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
						style='cursor: hand'
						onclick="ListToExcel('<s:property value="cityId"/>','<s:property value="status"/>','<s:property value="type"/>','<s:property value="starttime"/>','<s:property value="endtime"/>')">
				</s:if>
			</td>
		</tr>


		<TR>
			<TD align="center" colspan="10">
				<button onclick="javascript:window.close();">
					&nbsp;关 闭&nbsp;
				</button>
			</TD>
		</TR>
	</tfoot>

	<tr STYLE="display: none">
		<td colspan="5">
			<iframe id="childFrm" src=""></iframe>
		</td>
	</tr>
</table>

<%@ include file="/foot.jsp"%>
