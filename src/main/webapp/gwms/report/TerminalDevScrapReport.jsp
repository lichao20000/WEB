<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet"type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript">
//�����ն�����
function getSrcapDevDetailList(city_id){
	var page="<s:url value='/gwms/report/terminalNoMatchReport!querySrcapDevDetailList.action'/>?"+ "cityId=" + city_id;
	window.open(page,"","left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
}
//��һ��δ���������
function getOldDevDetail(city_id){
	var page="<s:url value='/gwms/report/terminalNoMatchReport!queryOldDevDetailList.action'/>?"+ "cityId=" + city_id;
	window.open(page,"","left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
}
//�������������
function getOldCompleteDevDetail(city_id){
	var page="<s:url value='/gwms/report/terminalNoMatchReport!queryOldCompleteDevDetailList.action'/>?"+
			"cityId=" + city_id;
	window.open(page,"","left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
}

//�����������
function getOldDayCompleteDevDetail(city_id,needCount){
	var page="<s:url value='/gwms/report/terminalNoMatchReport!queryOldDayCompleteDevDetailList.action'/>?"
			+ "cityId=" + city_id+"&needCount="+needCount;
	window.open(page,"","left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
}

//PT921Gδ��������
function getPt921gDetail(city_id){
	var page="<s:url value='/gwms/report/terminalNoMatchReport!queryPt921gDetail.action'/>?"+ "cityId=" + city_id;
	window.open(page,"","left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
}

//������δ���������
function getThrdDetail(city_id){
	var page="<s:url value='/gwms/report/terminalNoMatchReport!queryThrdDetail.action'/>?"+ "cityId=" + city_id;
	window.open(page,"","left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
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
                        <TD width="164" align="center" class="title_bigwhite">�Ͼ��ն˸���</TD>
                        <td>&nbsp; <img src="<s:url value="/images/attention_2.gif"/>"
                                        width="15" height="12"> &nbsp;�Ͼ��ն˱��ϱ���
                        </td>
                    </TR>
                </TABLE>
            </TD>
    </TR>
    <tr>
  <td>
  <form name="frm" action="/gwms/report/terminalNoMatchReport!devMisMatch.action" method='POST'>
  	 <table width="100%" border="0" cellspacing="1" cellpadding="2" bgcolor="#999999" style="margin-top: 5px">
  	 	<%-- <tr>
  	 		<TH colspan="4">�Ͼ��ն˱��ϱ���</TH>
  	 	</tr>
  	    <tr bgcolor=#ffffff>
  	   	  <td class=column align=right>����</td>
  	   	     <s:select list="cityList" name="cityId" headerKey="-1"
										headerValue="��ѡ������" listKey="city_id" listValue="city_name"
										value="cityId" cssClass="bk"></s:select>
  	   	  <td>
		  </td>
		  <td class=column colspan='2' align=right>
		    <input type="button" value=" ͳ �� " name="button" onclick="doQuery()" class=jianbian>
		  </td>
   	    </tr> --%>
  	 </table>
  	 </form>
  </td>
 </tr>
		<tr>
			<td>
				<table border=0 cellspacing=1 cellpadding=1 width="100%" align="center" bgcolor="#999999">
					<tr bgcolor="#FFFFFF">
						<th rowspan="3" width="6%">����</th>
						<th colspan="13" width="74%">2021���Ͼ��ն˸���</th>
                   <!-- <th colspan="2" width="20%">�����ն��ٴ����ߣ�4��30�պ��ֹ���ߣ�</th> -->
                        <th colspan="2" width="20%">�����ն��ٴ����ߣ�2021�꣩</th>
					</tr>
					<tr bgcolor="#FFFFFF">
						<th colspan='7'>��һ���Ͼ��ն�</th>
						<th colspan='3'>PT921G�Ͼ��ն�</th>
						<th colspan='3'>�������Ͼ��ն�(11���ͺ�)</th>
						<th rowspan='2'>����</th>
						<th rowspan='2'>����</th>
					</tr>
					<tr bgcolor="#FFFFFF">
				        <!-- ��һ�� -->
						<th>T0ֵ</th>
						<th>δ�����</th>
						<th>δ������굥</th>
						<th>ÿ�������</th>
						<th>��������굥</th>
						<th>�������</th>
                        <th>��������굥</th>
                        <!-- ��һ��end-->
                        
                        <!-- PT921G -->
                        <th>��������T0</th>
						<th>δ�����</th>
						<th>����</th>
                        <!-- PT921G end-->
                        
                        <!-- �������Ͼ��ն�(11���ͺ�) -->
                        <th>��������T0</th>
						<th>δ�����</th>
						<th>����</th>
                        <!-- �������Ͼ��ն�(11���ͺ�) end-->
					</tr>
					<s:iterator value="data">
						<tr bgcolor="#FFFFFF">
						<td class=column >
								<strong><s:property value="city_name" /></strong>
							</td>
						    <!-- ��һ�� -->
							<td>
                            	<s:property value="first_oldt0" />
                            </td>
                            <td>
                                <s:property value="first_oldnum" />
                            </td>
                            <td>
                                <a href="javascript:getOldDevDetail('<s:property value="city_id" />');">
                                    		��ϸ��Ϣ
                                </a>
                            </td>
                            <td>
                                <s:property value="first_day_complete_num" />
                            </td>
                            <td>
                                <a href="javascript:getOldDayCompleteDevDetail('<s:property value="city_id" />','<s:property value="first_day_complete_num" />');">
                                    		��ϸ��Ϣ
                                </a>
                            </td>
                            <td>
                                <s:property value="first_old_complete_num" />
                            </td>
                            <td>
                                <a href="javascript:getOldCompleteDevDetail('<s:property value="city_id" />');">
                                    		��ϸ��Ϣ
                                </a>
                            </td>
                             <!-- ��һ��end -->
                             
                              <!-- PT921G -->
                              <td>
                            	<s:property value="sec_t0" />
                              </td>
                              <td>
                                <s:property value="sec_nocplt_num" />
                              </td>
                              <td>
                                <a href="javascript:getPt921gDetail('<s:property value="city_id" />');">��ϸ��Ϣ
                                </a>
                              </td>
                              <!-- PT921G end -->
                              
                              <!-- ������ -->
                              <td>
                            	<s:property value="thrd_t0" />
                              </td>
                              <td>
                                <s:property value="thrd_nocplt_num" />
                              </td>
                              <td>
                                <a href="javascript:getThrdDetail('<s:property value="city_id" />');">
                                    		��ϸ��Ϣ
                                </a>
                              </td>
                              <!-- ������end -->
                              <!-- �����ն� -->
							<td>
								<s:property value="scrapdevnum" />
							</td>
							<td>
								<a href="javascript:getSrcapDevDetailList('<s:property value="city_id" />');">
					 				��ϸ��Ϣ
						 		</a>
							</td>
							<!-- �����ն�end -->
						</tr>
					</s:iterator>
				</table>
			</td>
		</tr>
	</table>
</body>
</html>
