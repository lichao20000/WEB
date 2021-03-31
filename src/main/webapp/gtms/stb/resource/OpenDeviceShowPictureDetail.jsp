<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
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
					document.write("��");
					break;
				case 1:
					document.write("��");
					break;
				default:
					document.write("");
			}
		}
		function downloadHotel(taskId){
			var url =  "<s:url value='/gtms/stb/resource/openDeviceShowPic!exportHotelUser.action'/>?taskId="+taskId;
			document.all("childFrm").src=url;
		}
		</script>
	</head>
	<body>
		<TABLE width="98%" align="center" class="querytable">
			<TR>
				<TD>
					<IMG height=20 src="<s:url value='/images/bite_2.gif'/>" width=24>
					����ǰ��λ�ã���������������ϸ��Ϣ
				</TD>
			</TR>
		</TABLE>
		<br>
		<table class="querytable" width="98%" align="center">
			<tr>
				<td colspan="4" class="title_1">
					��������������ϸ��Ϣ
				</td>
			</tr>
			<tr>
				<TD class="title_2" align="center" width="15%">
					��������
				</td>
				<TD width="85%"  >
					<s:property value="taskDetailMap.task_name" />
				</td>
			</tr>	
			<tr>
				<TD class="title_2" align="center" width="15%">
					����
				</td>
				<TD width="85%"  >
					<s:property value="taskDetailMap.cityName" />
				</td>
			</tr>	
			<tr>
				<TD class="title_2" align="center" width="15%">
					����
				</td>
				<TD width="85%"  >
					<s:property value="taskDetailMap.vendorName" />
				</td>
			</tr>
			<tr>
				<TD class="title_2" align="center" width="15%">
					��ѡ���ͺ�
				</td>
				<TD width="85%"  >
					<s:property value="taskDetailMap.deviceModelName" />
				</td>
			</tr>
			<tr>
				<TD class="title_2" align="center" width="15%">
					��ѡ��汾
				</td>
				<TD width="85%"  >
					<s:property value="taskDetailMap.deviceTypeName" />
				</td>
			</tr>
			<tr>
				<TD class="title_2" align="center" width="15%">
					������
				</td>
				<TD width="85%"  >
					<s:property value="taskDetailMap.acc_loginname" />
				</td>
			</tr>
			<tr>
				<TD class="title_2" align="center" width="15%">
					����ʱ��
				</td>
				<TD width="85%"  >
					<s:property value="taskDetailMap.add_time" />
				</td>
			</tr>
			<ms:inArea areaCode="sd_lt" notInMode="true">
			<tr>
				<TD class="title_2" align="center" width="15%">
					����ip��ַ��
				</td>
				<TD width="85%"  >
					<script type="text/javascript">resultConvert(<s:property value='taskDetailMap.check_ip'/>)</script>	
				</td>
			</tr>
			<s:if test="ipList!=null">
                <s:if test="ipList.size()>0">
                        <tbody>
                                <s:iterator value="ipList">
                                        <tr>
                                               <TD class="title_2" align="center" width="15%">
													��ʼIP ~ ����IP
												</td>	
												<td width="35%"><s:property value="start_ip" /> &nbsp;&nbsp; ~ &nbsp;&nbsp; <s:property value="end_ip" /></td>
                                        </tr>
                                </s:iterator>
                        </tbody>
                </s:if>
            </s:if>
            <tr>
				<TD class="title_2" align="center" width="15%">
					����MAC��ַ��
				</td>
				<TD width="85%"  >
					<script type="text/javascript">resultConvert(<s:property value='taskDetailMap.check_mac'/>)</script>	
				</td>
			</tr>
			</ms:inArea>
            <s:if test="macList!=null">
                <s:if test="macList.size()>0">
                        <tbody>
                                <s:iterator value="macList">
                                        <tr>
                                               <TD class="title_2" align="center" width="15%">
													��ʼmac~����mac
												</td>	
												<td width="35%"><s:property value="start_mac" /> &nbsp;&nbsp; ~  &nbsp;&nbsp;<s:property value="end_mac" /></td>
                                        </tr>
                                </s:iterator>
                        </tbody>
                </s:if>
            </s:if>
            <tr>
	            <TD class="title_2" align="center" width="15%">
						����ҵ���ʺ��б�
					</td>
					<TD width="85%"  >
					<s:if test='taskDetailMap.file_path=="��"'>
						��
					</s:if><s:else>
							<a  href='<s:property value="taskDetailMap.file_path" />'>ҵ���ʺ��б�</a>
					</s:else>
					</td>
            </tr>
            <tr>
				<TD class="title_2" align="center" width="15%">
					����ͼƬ
				</td>
				<TD width="85%"  >
					<s:property value="taskDetailMap.sd_kj_pic_url" />
				</td>
			</tr>
			 <tr>
				<TD class="title_2" align="center" width="15%">
					����ͼƬ
				</td>
				<TD width="85%"  >
					<s:property value="taskDetailMap.sd_qd_pic_url" />
				</td>
			</tr>
			 <tr>
				<TD class="title_2" align="center" width="15%">
					��֤ͼƬ
				</td>
				<TD width="85%"  >
					<s:property value="taskDetailMap.sd_rz_pic_url" />
				</td>
			</tr>
			<tr STYLE="display: none">
					<td colspan="4">
						<iframe id="childFrm" src=""></iframe>
					</td>
		   </tr>	
		</table>
		<br>
		<br>
	</body>