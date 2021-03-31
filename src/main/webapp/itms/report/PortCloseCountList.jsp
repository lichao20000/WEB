<%@  page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<ms:inArea areaCode="cq_dx" notInMode="false">

	<%--
	/**
	 * �淶�汾��ѯ�б�ҳ��
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
<title>�ر�21��23�˿�ͳ��</title>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

<script type="text/javascript">

	 function deviceDetail(type,countNum,city_id){
			var url = "<s:url value='/itms/report/portCloseCount!queryDevList.action?countNum='/>"+countNum+"&&type="+type+"&&city_id="+city_id;
			window.open(url ,"","left=140,top=80,width=1000,height=600,resizable=yes,scrollbars=yes");
	}
 
 	function ToExcel() {
		var page="<s:url value='/itms/report/portCloseCount!reportCountExcel.action'/>";
		document.all("childFrm").src=page;
	}
</script>

</head>

<body>
	</FORM>
	<TABLE>
		<tr>
			<td>
				<table class="green_gargtd">
					<tr>
						<th>�ر�21��23�˿�ͳ��</th>
						<td><img src="<s:url value="/images/attention_2.gif"/>"
							width="15" height="12"></td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td>
				<table class="listtable">
					<caption style="background-color: #BCE9FE">�ر�21��23�˿�ͳ��</caption>
					<caption>��ѯ���</caption>
					<thead>
						<tr>
							<th>����</th>
							<th>��������</th>
							<th>�ɹ�</th>
							<th>δ����</th>
							<th>ʧ��</th>
						</tr>
					</thead>
					<tbody>
						<s:if test="countList!=null">
							<s:if test="countList.size()>0">
								<s:iterator value="countList">
									<tr>
										<td align="center"><s:property value="city_name" /></td>
										<td align="center"><a class='green_link'
											href="javascript:deviceDetail('total','<s:property value="totalNum" />','<s:property value="city_id"/>');">
												<s:property value="totalNum" />
										</a></td>
										<td align="center"><a class='green_link'
											href="javascript:deviceDetail('succ','<s:property value="succNum" />','<s:property value="city_id"/>');">
												<s:property value="succNum" />
										</a></td>
										<td align="center"><a class='green_link'
											href="javascript:deviceDetail('unDone','<s:property value="unDoneNum" />','<s:property value="city_id"/>');">
												<s:property value="unDoneNum" />
										</a></td>
										<td align="center"><a class='green_link'
											href="javascript:deviceDetail('fail','<s:property value="failNum" />','<s:property value="city_id"/>');">
												<s:property value="failNum" />
										</a></td>
									</tr>
								</s:iterator>
								<tr style="background-color: #CCECFF">
									<td align="center"><s:property
											value="countList.get(countList.size()-1).get('all')" /></td>
									<td align="center"><a class='green_link'
										href="javascript:deviceDetail('total','<s:property value="countList.get(countList.size()-1).get('allTotalNum')" />','');">
											<s:property
												value="countList.get(countList.size()-1).get('allTotalNum')" />
									</a></td>
									<td align="center"><a class='green_link'
										href="javascript:deviceDetail('succ','<s:property value="countList.get(countList.size()-1).get('allSuccNum')" />','');">
											<s:property
												value="countList.get(countList.size()-1).get('allSuccNum')" />
									</a></td>
									<td align="center"><a class='green_link'
										href="javascript:deviceDetail('unDone','<s:property value="countList.get(countList.size()-1).get('allUnDoneNum')" />','');">
											<s:property
												value="countList.get(countList.size()-1).get('allUnDoneNum')" />
									</a></td>
									<td align="center"><a class='green_link'
										href="javascript:deviceDetail('fail','<s:property value="countList.get(countList.size()-1).get('allFailNum')" />','');">
											<s:property
												value="countList.get(countList.size()-1).get('allFailNum')" />
									</a></td>
								</tr>
							</s:if>
							<s:else>
								<tr>
									<td colspan=5>û�в�ѯ�������Ϣ��</td>
								</tr>
							</s:else>
						</s:if>
						<s:else>
							<tr>
								<td colspan=5>û�в�ѯ�������Ϣ��</td>
							</tr>
						</s:else>
					<tfoot>
						<tr>
							<td colspan="10" align="left">
								<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='�����б�'
								style='cursor: hand' onclick="ToExcel()">����
							</td>
						</tr>
						<tr STYLE="display: none">
							<td>
								<iframe id="childFrm" src=""></iframe>
							</td>
						</tr>
					</tfoot>
					</tbody>
				</table>
			</td>
		</tr>
	</TABLE>
</body>
	</html>