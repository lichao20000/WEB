<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<script type="text/javascript" src="<s:url value="/Js/jsDate/WdatePicker.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/edittable.js"/>"></SCRIPT>
<script type="text/javascript">

//�����ն��굥 
function ListToExcel() {
	var page="<s:url value='/gwms/report/v6OpenStat!toExcel.action'/>";
	document.all("childFrm").src=page;
}
	
 
</script>
</head>
<body>
	<table border=0 cellspacing=0 cellpadding=0 width="90%" align="center" style="margin-top: 20px">
	 <TR>
         <TD>
                <TABLE width="100%" height="30" border="0" cellspacing="0"
                       cellpadding="0" class="green_gargtd">
                    <TR>
                        <TD width="164" align="center" class="title_bigwhite">IPV6��ͨ</TD>
                        <td>&nbsp; <img src="<s:url value="/images/attention_2.gif"/>"
                                        width="15" height="12"> &nbsp;IPV6��ͨ���ͳ�Ʊ���
                        </td>
                    </TR>
                </TABLE>
            </TD>
    </TR>
 <tr>
  <td >
  <form name="frm" action="" method='POST'>
  	 <table width="100%" border="0" cellspacing="1" cellpadding="2" bgcolor="#999999">
  	 </table>
  	 </form>
  </td>
 </tr>
 <!-- end -->
		<tr>
			<td>
				<table class="listtable">
					 <caption>
						IPV6��ͨ���ͳ�Ʊ���
					</caption>
					<thead>
						<tr>
						    <th>����</th>
							<th>�ͺ�</th>
							<th>��������</th>
							<th>�Ƿ�֧��IPV6</th>
							<th>������</th>
							<th>�ɹ�����</th>
							<th>ʧ������</th>
							<th>���һ��δ������</th>
						</tr>
					</thead>
					<tbody>
						<s:if test="statList.size()>0">
							<s:iterator value="statList">
								<tr>
								    <td>
										<s:property value="vendorName" />
									</td>
									<td>
										<s:property value="deviceModel" />
									</td>
									<td>
										<s:property value="specType" />
									</td>
									<td>
										<s:property value="isIpv6" />
									</td>
									<td>
										<s:property value="bindNum" />
									</td>
									<td>
										<s:property value="succNum" />
									</td>
									<td>
										<s:property value="failNum" />
									</td>
									<td>
										<s:property value="notOnlineNum" />
									</td>
								</tr>
							</s:iterator>
						</s:if>
						<s:else>
							<tr>
								<td colspan="8">
									ϵͳû����ص��豸��Ϣ!
								</td>
							</tr>
						</s:else>
					</tbody>
					<tfoot>
						<tr>
							<td colspan="8">
								<span style="float: right;"> <lk:pages
										url="/gwms/report/v6OpenStat!init.action" styleClass=""
										showType="" isGoTo="true" changeNum="true" /> </span>
									<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='�����б�'
										style='cursor: hand'
										onclick="ListToExcel()">
							</td>
						</tr>
					</tfoot>
					<tr STYLE="display: none">
						<td colspan="8">
							<iframe id="childFrm" src=""></iframe>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</body>
</html>
