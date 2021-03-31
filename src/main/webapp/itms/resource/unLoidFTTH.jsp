<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<title>e8-cҵ���ѯ</title>
		<%
			 /**
			 * e8-cҵ���ѯ
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
				��ѯ���
			</caption>
			<thead>
		
				<tr>
					<th>
						��������
					</th>
					<th>
						�豸����
					</th>
					<th>
						�豸�ͺ�
					</th>
					
					<th>
						�豸���к�
					</th>
					<th>
						LOID
					</th>
					<th>
						��������IP
					</th>
					<th>
						�ϱ�ʱ��
					</th>
					
				    <th>
						����
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
									SRC="/itms/images/view.gif" BORDER="0" ALT="��ϸ��Ϣ"
									onclick="detailDevice('<s:property value="device_id" />')"
									style="cursor: hand">
								</td>
						
							</tr>
						</s:iterator>
					</s:if>
					<s:else>
						<tr>
							<td colspan=9>
								û�в�ѯ������豸��
							</td>
						</tr>
					</s:else>
				</s:if>
				<s:else>
					<tr>
						<td colspan=9>
							û�в�ѯ������豸��
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
				<IMG SRC="/itms/images/excel.gif" BORDER='0' ALT='�����б�'
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