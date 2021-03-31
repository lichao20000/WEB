<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<title>e8-c业务查询</title>
		<%
			 /**
			 * e8-c业务查询
			 * 
			 * @author qixueqi(4174)
			 * @version 1.0
			 * @since 2010-09-08
			 * @category
			 */
		%>
		<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
			type="text/css">
		<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
			type="text/css">
		<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
		<script type="text/javascript"
			src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

		<script type="text/javascript">
	
	$(function(){
		parent.dyniframesize();
	});

</script>

	</head>

	<body>
		<table class="listtable">
			<caption>
				查询结果
			</caption>
			<thead>
		
				<tr>
					<th>
						属地名称
					</th>
					<th>
						设备厂商
					</th>
					<th>
						设备型号
					</th>
					
					<th>
						设备序列号
					</th>
					<th>
						LOID
					</th>
					<th>
						域名或者IP
					</th>
					<th>
						上报时间
					</th>
					
				    <th>
						操作
					</th>
				</tr>
			</thead>
			<tbody>
				<s:if test="deviceList!=null">
					<s:if test="deviceList.size()>0">
						<s:iterator value="deviceList">
							<tr>
								<td>
									<s:property value="city_name" />
								</td>
								<td>
									<s:property value="vendor_add" />
								</td>
								<td>
									<s:property value="device_model" />
								</td>
								<td>
									<s:property value="device_serialnumber" />
								</td>
								<td>
									<s:property value="device_id_ex" />
								</td>
								<td>
									<s:property value="loopback_ip" />
								</td>
								<td>
									<s:property value="last_time" />
								</td>
								
								<td>&nbsp;&nbsp;&nbsp;&nbsp;
							       <IMG
									SRC="/itms/images/view.gif" BORDER="0" ALT="详细信息"
									onclick="detailDevice('<s:property value="device_id" />')"
									style="cursor: hand">
								</td>
						
							</tr>
						</s:iterator>
					</s:if>
					<s:else>
						<tr>
							<td colspan=9>
								没有查询到相关设备！
							</td>
						</tr>
					</s:else>
				</s:if>
				<s:else>
					<tr>
						<td colspan=9>
							没有查询到相关设备！
						</td>
					</tr>
				</s:else>
			</tbody>
  
			<tfoot>
				<tr>
					<td colspan="9" align="right">
						<lk:pages
							url="/itms/resource/countFtthACT!queryUnbind.action"
							styleClass="" showType="" isGoTo="true" changeNum="true" />
					</td>
				</tr>
				<tr>
			<td colspan="9">
				<IMG SRC="/itms/images/excel.gif" BORDER='0' ALT='导出列表'
					style='cursor: hand'
					onclick="ToExcel()">
			</td>
		</tr>
			</tfoot>

		</table>
	</body>
<script>

function ToExcel(){
	
	    
		var mainForm = window.parent.document.getElementById("mainForm");
		mainForm.action="<s:url value='/itms/resource/countFtthACT!toExcelUnloid.action'/>"
		mainForm.submit();
		mainForm.action="<s:url value='/itms/resource/countFtthACT!queryUnloid.action'/>";
		
	}
function detailDevice(device_id)
{
      var strpage = "../../Resource/DeviceShow.jsp?device_id=" + device_id;
	window.open(strpage,"","left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
  }


</script>
</html>