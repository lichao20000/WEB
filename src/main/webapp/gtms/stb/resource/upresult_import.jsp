<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<link href="<s:url value="/css2/global.css"/>" rel="stylesheet"
			type="text/css">
		<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet"
			type="text/css">
		<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
		<script type="text/javascript">
			function resultConvert(res)
			{
				switch(res)
				{
					case 0:
						document.write("δ����");
						break;
					case 1:
						document.write("���·�������ַ�޸ĳɹ�");
						break;
					case 2:
						document.write("��������ɹ�");
						break;
					case -1:
						document.write("���·�������ַ�޸�ʧ��");
						break;
					default:
						document.write("");
				}
				
			}
			function dateConvert(dat)
			{
				var d = new Date(dat*1000);
				document.write(d.getFullYear()+"/"+d.getMonth()+"/"+d.getDay()+" "+d.getHours()+":" + d.getMinutes()+":"+d.getSeconds());
			}
		</script>
	</head>
	<body>
		<TABLE width="98%" align="center" class="querytable">
			<TR>
				<TD>
					<IMG height=20 src="<s:url value='/images/bite_2.gif'/>" width=24>
					����ǰ��λ�ã��������Խ����ѯ
				</TD>
			</TR>
		</TABLE>
		<br>
		<s:form action="softUpgrade!execUpload.action" method="post" enctype="multipart/form-data" name="batchexform">
			<input type="hidden" name="vendorPathId" value=""/>
			<table width="98%" class="listtable" align="center"">
				<thead>
					<tr>
						<th align="center" width="10%">
							����
						</th>
						<th align="center" width="10%">
							Ӳ���ͺ�
						</th>
						<th align="center" width="15%">
							ҵ���ʺ�
						</th>
						<th align="center" width="10%">
							�豸���к�
						</th>
						<th align="center" width="10%">
							������汾
						</th>
						<th align="center" width="10%">
							������汾
						</th>
						<th align="center" width="10%">
							�������
						</th>
						<th align="center" width="15%">
							����ʱ��
						</th>
					</tr>
				</thead>
				<s:if test="upRecordList!=null">
					<s:if test="upRecordList.size()>0">
						<tbody>
							<s:iterator value="upRecordList">
								<tr>
									<td align="center">
										<s:property value="vendor_add" />
									</td>
									<td align="center">
										<s:property value="device_model" />
									</td>
									<td align="center">
										<s:property value="serv_account" />
									</td>
									<td align="center">
										<s:property value="device_serialnumber" />
									</td>
									<td align="center">
										<s:property value="newsoftversion" />
									</td>
									<td align="center">
										<s:property value="oldsoftversion" />
									</td>
									<td align="center">
										<script type="text/javascript">resultConvert(<s:property value="result" />)</script>
									</td>
									<td align="center">
										<s:property value="start_time" />
									</td>	
								</tr>
							</s:iterator>
						</tbody>
						<tfoot>
							<tr>
								<td colspan="8"><div align="right"><lk:pages url='/gtms/stb/resource/stbSoftUpgrade!queryUpRecordByTaskId_import.action' changeNum="true"/></div></td>
							</tr>
						</tfoot>
					</s:if>
				</s:if>
			</table>
		</s:form>
		<br>
		<br>
	</body>