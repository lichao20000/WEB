<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@taglib prefix="lk" uri="/linkage"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<link href="<s:url value="/css2/global.css"/>" rel="stylesheet"
			type="text/css">
		<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet"
			type="text/css">
		<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
		<lk:res />
		<SCRIPT LANGUAGE="JavaScript">
	$(function(){
		
	});	
</SCRIPT>

	</head>
	<body>
		<TABLE width="98%" align="center" class="querytable">
			<TR>
				<TD>
					<IMG height=20 src="<s:url value='/images/bite_2.gif'/>" width=24>
					����ǰ��λ�ã������п�������������
				</TD>
			</TR>
		</TABLE>
		<br>
		<s:form action="openDeviceShowPic!batchImportUp.action" method="post" enctype="multipart/form-data" name="batchexform">
		<table width="98%" class="listtable" align="center">
		<thead>
				<tr>
					<th align="center" width="8%">
						����
					</th>
					<th align="center" width="8%">
						����
					</th>
					<th align="center" width="8%">
						�ͺ�
					</th>
					<th align="center" width="8%">
						����汾
					</th>
					<th align="center" width="8%">
						�豸���к�
					</th>
					<th align="center" width="12%">
						ҵ���˺�
					</th>
					<th align="center" width="12%">
						ip��ַ
					</th>
					<th align="center" width="12%">
						mac��ַ
					</th>
					<th align="center" width="12%">
						���ý��
					</th>
					<th align="center" width="12%">
						����ʱ��
					</th>
				</tr>
			</thead>
                <s:if test="taskResultList.size()>0">
                        <tbody>
                                <s:iterator value="taskResultList">
                                        <tr>
                                                <td align="center"><s:property value="cityName" /></td>
                                                <td align="center"><s:property value="vendorName" /></td>
                                                <td align="center"><s:property value="deviceModel" /></td>
                                                <td align="center"><s:property value="deviceTypeName" /></td>
	                                            <td align="center"><s:property value="deviceSerialNumber" /></td>
	                                            <td align="center"><s:property value="servAccount" /></td>
	                                            <td align="center"><s:property value="loopback_ip" /></td>
	                                            <td align="center"><s:property value="cpe_mac" /></td>
	                                            <td align="center"><s:property value="result" /></td>
	                                            <td align="center"><s:property value="update_time" /></td>
                                        </tr>
                                </s:iterator>
                        </tbody>
                </s:if>
		<s:else>
			<tbody>
				<tr>
					<td colspan="10">û�в�ѯ������豸��</td>
				</tr>
            </tbody>
		</s:else>
		<tfoot>
			<tr>
				<td colspan="10" align="right" class=column>
					<lk:pages url="/gtms/stb/resource/OpenDeviceShowPictureNew!getShowPictureConfigResult.action" 
					styleClass="" showType="" isGoTo="true" changeNum="true"/>
				</td>
			</tr>
		</tfoot>
		</table>
		<div id="divDetail"
			style="position: absolute; z-index: 255; top: 200px; left: 350px; width: 40%; border-width: 1; border-style: ridge; background-color: #eeeeee; padding-top: 10px; display: none"></div>
</s:form>
</body>
