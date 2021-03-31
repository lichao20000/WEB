<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"	type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<script type="text/javascript" src="<s:url value="/Js/jsDate/WdatePicker.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/edittable.js"/>"></SCRIPT>
<script type="text/javascript">
function ToExcel(){
$("form[@name='mainForm']").submit();
	/**var devicetype = $("input[@name='devicetype']").val(); 
	 var cityId = $("input[@name='cityId']").val();     
	 var url = '<s:url value='/gwms/resource/countDeviceACT!toExcel.action'/>'; 
		$.post(url,{
			devicetype:devicetype,
			cityId:cityId
		},function(ajax){
		  
		});
	*/
  //alert("excel");
		//var mainForm = document.getElementById("mainForm");
		//mainForm.action="<s:url value='/itms/service/bssSheetServ!getBssSheetServInfoExcel.action'/>"
	///	mainForm.submit();
	
}

function DetailDevice(device_id){
	
	var strpage = "../../Resource/DeviceShow.jsp?device_id=" + device_id;
	window.open(strpage,"","left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
}

</script>

<table class="listtable">
	<caption>
		设备列表
	</caption>
	<thead>
		<tr>
			<th>
				设备厂商
			</th>
			<th>
				设备型号
			</th>
			<th>
				软件版本
			</th>
			<th>
				设备属地
			</th>
			<th>
				设备序列号
			</th>
			<th>
				操作
			</th>
			
		</tr>
	</thead>
	<tbody>
		<s:if test="detailDataList.size()>0">
			<s:iterator value="detailDataList">
				<tr>
					<td>
						<s:property value="vendor_add" />
					</td>
					<td>
						<s:property value="device_model" />
					</td>
					<td>
						<s:property value="softwareversion" />
					</td>
					
					<td>
						<s:property value="city_name" />
					</td>
					<td>
						<s:property value="device_serialnumber" />
					</td>
					<td>
				<a href="javascript:DetailDevice('<s:property value="device_id" />')">详细信息</a>
					</td>
					
					
					
				</tr>
			</s:iterator>
		</s:if>
		<s:else>
			<tr>
				<td>
					系统没有相关的设备信息!
				</td>
			</tr>
		</s:else>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="6">
				<span style="float: right;"> <lk:pages
						url="/gwms/resource/countDeviceACT!queryDetailAll.action" styleClass=""
						showType="" isGoTo="true" changeNum="true" /> </span>
			</td>
		</tr>
		<tr>
			<td colspan="6">
				<IMG SRC="/itms/images/excel.gif" BORDER='0' ALT='导出列表'
					style='cursor: hand'
					onclick="ToExcel()">
			</td>
		</tr>
	</tfoot>

	<tr STYLE="display: none">
		<td colspan="5">
			<iframe id="childFrm" src=""></iframe>
		</td>
	</tr>
</table>
<FORM NAME="mainForm" id="mainForm" METHOD="post" 
			ACTION="<s:url value="/gwms/resource/countDeviceACT!toExcelAll.action"/>"      >
		<table width="100%" height="30" border="0" align="center"
			cellpadding="0" cellspacing="0">
			<tr>
				<td>
				<INPUT TYPE="hidden" NAME="cityId" value="<s:property value="cityId"/>">
				</td>
				<td><INPUT TYPE="hidden" NAME="filterDevicetype" value="<s:property value="filterDevicetype"/>"></td>
			</tr>
		</table>
		</FORM>

<%@ include file="/foot.jsp"%>
