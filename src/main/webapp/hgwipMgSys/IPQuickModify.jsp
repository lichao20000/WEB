<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<title>IP��ַ��������޸�</title>
		<%
			 /**
			 * IP��ַ��������޸�
			 *
			 * @author ������(5260) tel:13770734313
			 * @version 1.0
			 * @since 2007-11-13
			 * @category ipmg
			 */
		%>
		<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet" type="text/css">
		<script src="<s:url value="/Js/Calendar.js"/>"></script>
		<script src="<s:url value="/Js/CheckForm.js"/>"></script>
		<script src="<s:url value="/Js/jquery.js"/>"></script>
		<script src="<s:url value="/Js/jquery.tablesorter.pager_linkage.js"/>"></script>
		<script src="<s:url value="/Js/jquery.tablesorter_LINKAGE.js"/>"></script>
		<SCRIPT LANGUAGE="JavaScript">
		<!--
		//��ʼ��ʱ��
			$(function(){
			    var t=new Date();
				var year=t.getFullYear();
				var month=t.getMonth()+1;
				var day=t.getDate();
				var date=year+"-"+month+"-"+day;
				$("input[@name=allot]").val(date);
			});
			$(function(){
				//ȫѡ
				$("input[@name=chkall]").bind("click",function(){
					var ck=this.checked;
					$("input[@name=chk]").each(function(){
	                  this.checked=ck;
	  				});
				});
				//�����е�ѡ����
				$("select[@name=cityid]").bind("change",function(){
				    $.ajax({
				       type:"post",
				       url:"<s:url value="/hgwipMgSys/getChildCity.action"/>",
				       data:"cityid="+this.value,
				       success:function(data){
				      	  data="<option value='-1'>===��ѡ��===</option>"+data;
				          $("select[@name=country]").html(data);
				       }
				    });
				});
				//�޸��е�ѡ����
				$("select[@name=modcityid]").bind("change",function(){
				    $.ajax({
				       type:"post",
				       url:"<s:url value="/hgwipMgSys/getChildCity.action"/>",
				       data:"cityid="+this.value,
				       success:function(data){
				          data="<option value='-1'>===��ѡ��===</option>"+data;
				          $("select[@name=modcountry]").html(data);
				       }
				    });
				});
				//bind save
				$("input[@name=save]").bind("click",function(){
				   //����Ƿ�ѡ���豸
				   var chk=$("input[@name='chk']").filter("[@checked]").length;
				   if(chk==0){
				     alert("����ѡ��һ��IP����ѡ��");
				     $("input[@name=chk]").focus();
				     return false;
				   }
				   //ȡ��ѡ�е��豸
				   var tmp="";
				   $("input[@name=chk]").each(function(){
					   if(this.checked==true){
					      tmp+=this.value+",";
					   }
					});
				   $.ajax({
				   		type:"post",
				   		url:"<s:url value="/hgwipMgSys/batchModifyIP.action"/>",
				   		data:{modcityid:$("select[@name=modcityid]").val(),
				   		      modcountry:encodeURIComponent($("select[@name=modcountry]").val()),
				   		      allot:$("input[@name=allot]").val(),
				   		      modu1:encodeURIComponent($("select[@name=modu1]").val()),
				   		      modu2:encodeURIComponent($("select[@name=modu2]").val()),
				   		      modu3:encodeURIComponent($("select[@name=modu3]").val()),
				   		      remark:encodeURIComponent($("textarea").val()),
				   		      chk:tmp
				   		},
				   		success:function(data){
				   		  var temp=data.split("-");
				   		  var result="";
				   		  for(var i=0;i<temp.length;i++){
				   		     result+=temp[i]+"\n";
				   		  }
				   		   alert(result);
				   		   $("form").submit();
				   		}
				   });
				});
			});
			//����ҳ����
			$(function(){
				$("#data").tablesorter({widthFixed: false,headers: { 8: { sorter: false}}}).tablesorterPager({
				   container: $("#pager"),
				   positionFixed:false

				 });
			});
			//��ʾ��ϸ��Ϣ
			function showPage(para){
			   window.open(para,'','toolbar=no,scrollbar=yes,top=200,left=300,width=600,height=400,status=no');
			}
		//-->
		</SCRIPT>
	</head>
	<body>
		<form action="ipQuickModify.action">
			<br>
			<table width="98%" border="0" align="center" cellpadding="5"
				cellspacing="0">
				<tr>
					<td class=text>
						<table width="100%" height="30" border="0" align="center"cellpadding="0" cellspacing="0" class="green_gargtd">
							<tr>
								<td width="162" align="center" class="title_bigwhite">
									IP��ַ����
								</td>
								<td align="left">
									<img src="<s:url value="/images/attention_2.gif"/>" width="15"
										height="12">
									<span class="style1">ע��</span>�������ѯ������
								</td>
							</tr>
						</table>
						<table width="100%" border=0 align="center" cellpadding="2"	cellspacing="1" bgcolor=#999999>
						    <TR>
				               <TH  colspan="4" align="center"><B>IP��ַ��������޸�</B></TH>
			               </TR>
							<tr bgcolor="#FFFFFF">
								<td  width="10%" align=right>
									������IP��ַ��
								</td>
								<td width="30%" align=left>
									<input name="IpAdd" type="text" size="16">
									<img src="<s:url value="/images/attention_2.gif"/>" width="15"
										height="12">
									��ֻ��ͷ��λ
								</td>
								<td width="10%" align=right>
									�������У�
								</td>
								<td width="50%" align=left>
									<select name="cityid">
										<option value="-1">
												====��ѡ��====
										</option>
										<s:iterator value="getCityList" var="GCL">
											<option value="<s:property value="#GCL.city_id"/>">
												<s:property value="#GCL.city_name" />
											</option>
										</s:iterator>
									</select>
									&nbsp;&nbsp; ������:
									<select name="country">
										<option value="-1">
												====��ѡ��====
										</option>
										<s:iterator value="getCList" var="gcc">
											<option value="<s:property value="#gcc.city_id"/>">
												<s:property value="#gcc.city_name" />
											</option>
										</s:iterator>
									</select>
								</td>
							</tr>
							<tr bgcolor="#FFFFFF">
								<td width="10%" align=right>
									<div align="center">
										��;1��
									</div>
								</td>
								<td width="30%" align=left>
									<select name="used1">
										<option value="-1">
													====��ѡ��====
										</option>
										<s:iterator value="UsedList1" var="ul1">
											<option value="<s:property value="#ul1.value"/>">
												<s:property value="#ul1.value" />
											</option>
										</s:iterator>
									</select>
								</td>
								<td width="10%" align=right>
									��;2��
								</td>
								<td width="50%" align=left>
									<select name="used2">
										<option value="-1">
													====��ѡ��====
										</option>
										<s:iterator var="ul2" value="UsedList2">
											<option value="<s:property value="#ul2.value"/>">
												<s:property value="#ul2.value" />
											</option>
										</s:iterator>
									</select>
								</td>
							</tr>
							<tr bgcolor="#FFFFFF" class=text
								onmouseout="className='blue_trOut'">
								<td width="10%" align=right>
									<div align="center">
										��;3��
									</div>
								</td>
								<td colspan="3" width="10%" align=left>
									<select name="used3">
									   	<option value="-1">
												====��ѡ��====
										</option>
										<s:iterator var="ul3" value="UsedList3">
											<option value="<s:property value="#ul3.value"/>">
												<s:property value="#ul3.value" />
											</option>
										</s:iterator>
									</select>
								</td>
							</tr>
							<tr class="green_foot">
							<td colspan=4 align= right>
							  <input name="search" type="submit" class="green_foot" value="�� ѯ">
							</td>
							</tr>
						</table>
						<br>
						<table id="data" width="100%" border=0 align="center" cellpadding="2"	cellspacing="1"  bgcolor=#999999>
							<thead>
							<tr class="green_title">
								<td class=text>
									IP��ַ
								</td>
								<td class=text>
									��������
								</td>
								<td class=text>
									����״̬
								</td>
								<td class=text>
									����
								</td>
								<td class=text>
									��
								</td>
								<td class=text>
									��;1
								</td>
								<td class=text>
									��;2
								</td>
								<td class=text>
									��;3
								</td>
								<td class=text>
									<input type="checkbox" name="chkall">
								</td>
							</tr>
							</thead>
							<tbody>
							<s:if test="getIPList.size()==0">
								<tr bgcolor="#FFFFFF">
								  <td  class=text colspan="9">��ѯ������</td>
								</tr>
							</s:if>
							<s:else>
							<s:iterator value="getIPList" var="getip">
								<s:url var="url" value="/hgwipMgSys/getSubDetail.action">
							   		<s:param name="attr" value="#getip.showpara" />
							   </s:url>
								<tr bgcolor="#FFFFFF">
									<td class=text>
										<s:property value="#getip.subnet" />
									</td>
									<td class=text>
										<s:property value="#getip.netmask" />
									</td>
									<td class=text>
									    <s:if test="#getip.assign==1">
											<s:a href="javascript://" onclick="showPage('%{url}')"><img src="<s:url value="/hgwipMgSys/images/ip_ico4.gif"/>" width="13" height="13" border="0"> </s:a> �ѷ��������
										</s:if>
										<s:elseif test="#getip.assign==3">
											<s:a href="javascript://" onclick="showPage('%{url}')"><img src="<s:url value="/hgwipMgSys/images/ip_ico4.gif"/>" width="13" height="13" border="0"> </s:a> ������û�
										</s:elseif>
										<s:elseif test="#getip.assign==4">
											<s:a href="javascript://" onclick="showPage('%{url}')"><img src="<s:url value="/hgwipMgSys/images/ip_ico4.gif"/>" width="13" height="13" border="0"> </s:a> ���������
										</s:elseif>
										<s:elseif test="#getip.assign==5">
											<s:a href="javascript://" onclick="showPage('%{url}')"><img src="<s:url value="/hgwipMgSys/images/ip_ico4.gif"/>" width="13" height="13" border="0"> </s:a> �ȴ�����
										</s:elseif>
									</td>
									<td class=text>
										<s:property value="#getip.city_name" />
									</td>
									<td class=text>
										<s:property value="#getip.country" />
									</td>
									<td class=text>
										<s:property value="#getip.purpose1" />
									</td>
									<td class=text>
										<s:property value="#getip.purpose2" />
									</td>
									<td class=text>
										<s:property value="#getip.purpose3" />
									</td>
									<td class=text>
										<div align="center">
											<input type="checkbox" name="chk"
												value="<s:property value="#getip.onlyip"/>">
										</div>
									</td>
								</tr>
							</s:iterator>
							</s:else>
							</tbody>
							<tfoot>
							<tr bgcolor="#FFFFFF" class=text
								onMouseOut="className='blue_trOut'">
								<td colspan="9" class="green_foot">
									<table width="100%" border="0" cellspacing="0" cellpadding="0">
										<tr>
											<td class=text>
												�ܹ��У�
												<s:property value="total" />
												��IP��ַ
											</td>
											<td class=text align="right">
													<div id="pager" class="pager">
													        <a href="java://" class="first">��ҳ</a>
													        <a href="java://" class="prev">ǰһҳ</a>
													        <input id="page" type="text" class="pagedisplay"/>
													        <a href="java://" class="next">��һҳ</a>
													        <a href="java://" class="last">βҳ</a>
													        ����ÿҳ��ʾ��������
															<select class="pagesize">
																<option selected="selected"  value="10">10</option>
																<option value="20">20</option>
																<option value="30">30</option>
																<option  value="40">40</option>
															</select>
													</div>
												</td>
										</tr>
									</table>
								</td>
							</tr>
							</tfoot>
						</table>
						<br>
						<table width="100%" border=0 align="center" cellpadding="2"	cellspacing="1"  bgcolor=#999999>
							<TR>
								<TH colspan="2">
									�༭��ϸ��Ϣ
								</TH>
							</TR>
							<tr bgcolor="#FFFFFF" class=text
								onmouseout="className='blue_trOut'">
								<td  width="30%" align=right>
									�������У�
								</td>
								<td width="70%" align=left>
								  <select name="modcityid">
									<s:iterator value="getCityList" var="GCL">
										<option value="<s:property value="#GCL.city_id"/>">
											<s:property value="#GCL.city_name" />
										</option>
									</s:iterator>
								</select>
								&nbsp;&nbsp; ������:
								<select name="modcountry">
									<option value="">
												====��ѡ��====
										</option>
									<s:iterator value="getCList" var="gcc">
										<option value="<s:property value="#gcc.city_id"/>">
											<s:property value="#gcc.city_name" />
										</option>
									</s:iterator>
								</select>
								</td>
							</tr>
							<tr bgcolor="#FFFFFF" class=text
								onmouseout="className='blue_trOut'">
								<td width="30%" align=right>
									����ʱ�䣺
								</td>
								<td width="70%" align=left>
									<input type="text" name="allot">
									<input type="button" class="jianbian"
										onClick="showCalendar('day',event)" value="��">
								</td>
							</tr>
							<tr bgcolor="#FFFFFF" class=text
								onmouseout="className='blue_trOut'">
								<td width="30%" align=right>
									��;1��
								</td>
								<td width="70%" align=left>
									<select name="modu1">
									  <s:iterator value="UsedList1" var="ul1">
									       <option value="<s:property value="#ul1.value"/>">
									       		<s:property value="#ul1.value"/>
									       	</option>
									  </s:iterator>
									</select>
								</td>
							</tr>
							<tr bgcolor="#FFFFFF" class=text
								onmouseout="className='blue_trOut'">
								<td width="30%" align=right>
									��;2��
								</td>
								<td width="70%" align=left>
									<select name="modu2">
										<s:iterator var="ul2" value="UsedList2">
										   <option value="<s:property value="#ul2.value"/>">
										   		<s:property value="#ul2.value"/>
										   	</option>
										</s:iterator>
									</select>
								</td>
							</tr>
							<tr bgcolor="#FFFFFF" class=text onmouseout="className='blue_trOut'">
								<td width="30%" align=right>
									��;3��
								</td>
								<td width="70%" align=left>
									<select name="modu3">
										<s:iterator var="ul3" value="UsedList3">
											<option value="<s:property value="#ul3.value"/>">
												<s:property value="#ul3.value"/>
											</option>
										</s:iterator>
									</select>
								</td>
							</tr>
							<tr bgcolor="#FFFFFF" class=text onmouseout="className='blue_trOut'">
								<td width="30%" align=right>
									��ע:
								</td>
								<td width="70%" align=left>
									<textarea name="remark" cols="35" rows="8"></textarea>
								</td>
							</tr>
							<tr class="green_foot">
							<td colspan=2 align=right>
							  <input name="save" type="button" class="green_foot" value="�� ��">
									&nbsp;&nbsp;
							  <input name="cancel" type="reset" class="green_foot" value="ȡ ��">
							</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</form>
	</body>
</html>
<br>
<%@ include file="../foot.jsp"%>
