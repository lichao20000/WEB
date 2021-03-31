 <%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>


<%--
	/**
	 * �淶�汾��ѯҳ��
	 *
	 * @author ����(����) Tel:�绰
	 * @version 1.0
	 * @since ${date} ${time}
	 * 
	 * <br>��Ȩ���Ͼ������Ƽ� ���ܿƼ���
	 * 
	 */
 --%>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<title>����FTTH�û��б�</title>
		
		<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
		<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"	type="text/css">
		<script language="JavaScript" src="<s:url value="/Js/jquery.js"/>"></script>
		<script language="JavaScript" src="<s:url value="/Js/edittable.js"/>"></script>
		<script type="text/javascript" src="<s:url value="/Js/jsDate/WdatePicker.js"/>"></script>
		
		<script type="text/javascript">
			function ListToExcel(bindFlag,cityId,starttime1,endtime1) {
				var page="<s:url value='/gtms/report/nxFtthBindReport!getUserExcel.action'/>?"
					+ "bindFlag=" + bindFlag
					+ "&cityId=" + cityId 
					+ "&starttime1=" +starttime1
					+ "&endtime1=" +endtime1;
				document.all("childFrm").src=page;
			}
			
			//�鿴itms�û���ص���Ϣ
			function itmsUserInfo(user_id){
				var strpage="<s:url value='/Resource/HGWUserRelatedInfo.jsp'/>?user_id=" + user_id;
				window.open(strpage,"","left=20,top=20,width=600,height=450,resizable=yes,scrollbars=yes");
			}
		</script>
	</head>
	
	<body>
		<table class="listtable">
			<caption>�û��б�</caption>
			<thead>
				<tr>
					<th>�û��ʺ�</th>
					<th>�豸����</th>
					<th>�豸�ͺ�</th>
					<th>�� ��</th>
					<th>�û���Դ</th>
					<th>���豸</th>
					<th>����ʱ��</th>
					<th>�󶨷�ʽ</th>
					<th>����</th>
				</tr>
			</thead>
			<tbody>
				<s:if test="FtthUserList.size()>0">
					<s:iterator value="FtthUserList">
						<tr>
							<td><s:property value="username" /></td>
							<td><s:property value="vendor_add" /></td>
							<td><s:property value="device_model" /></td>
							<td><s:property value="city_name" /></td>
							<td><s:property value="user_type" /></td>
							<td><s:property value="device" /></td>
							<td><s:property value="opendate" /></td>
							<td><s:property value="bindtype" /></td>
							<td>
								<IMG SRC="<s:url value="/images/view.gif"/>" BORDER="0" ALT="��ϸ��Ϣ"
									onclick="itmsUserInfo('<s:property value="user_id"/>')"
									style="cursor: hand">
							</td>
						</tr>
					</s:iterator>
				</s:if>
				<s:else>
					<tr>
						<td colspan=9>ϵͳû����ص��û���Ϣ!</td>
					</tr>
				</s:else>
			</tbody>
			<tfoot>
				<tr>
					<td colspan="9">
						<span style="float: right;"> 
							<lk:pages url="/gtms/report/nxFtthBindReport!getUserList.action" styleClass=""
								showType="" isGoTo="true" changeNum="true" />
						</span>
						<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='�����б�'
							style='cursor: hand'
							onclick="ListToExcel('<s:property value="bindFlag"/>','<s:property value="cityId"/>','<s:property value="starttime1"/>','<s:property value="endtime1"/>')">
						
						<s:if test='#session.isReport=="1"'>
							
						</s:if>
					</td>
				</tr>
		
				<TR>
					<TD align="center" colspan="9">
						<button onclick="javascript:window.close();">&nbsp;�� ��&nbsp;</button>
					</TD>
				</TR>
			</tfoot>
		
			<tr STYLE="display: none">
				<td colspan="9"><iframe id="childFrm" src=""></iframe></td>
			</tr>
		</table>
	</body>
	<%@ include file="/foot.jsp"%>
</html>