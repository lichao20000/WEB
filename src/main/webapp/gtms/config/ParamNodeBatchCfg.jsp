<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../../head.jsp"%>
<%
	String gwType = request.getParameter("gw_type");
	String do_type = request.getParameter("do_type");
 %>
 <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<%if("1".equals(request.getParameter("do_type"))){%><title>LOID�����·�</title><%}else{ %><title>�ر�ǿ��</title><%}%>
		<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
		<link href="../../css/listview.css" rel="stylesheet" type="text/css">
		<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.js"></SCRIPT>
		<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckForm.js"></SCRIPT>
	</head>
	<body>
		<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
			<TR>
				<TD HEIGHT=20>
					&nbsp;
				</TD>
			</TR>
			<TR>
				<TD>

					<table width="98%" border="0" align="center" cellpadding="0"
						cellspacing="0" class="text">
						<tr>
							<td>
								<table width="100%" height="30" border="0" cellspacing="0"
									cellpadding="0" class="green_gargtd">
									<tr>
											<%if("1".equals(do_type)){%>
											<td width="162" align="center" class="title_bigwhite" nowrap>
												LOID�����·�
											</td>
											<%}else{%>
											<td width="162" align="center" class="title_bigwhite" nowrap>
												�ر�ǿ��
											</td>
											<%}%>
											<td>
												<img src="../../images/attention_2.gif" width="15" height="12">
												ҳ�汸ע˵��
											</td>
										
										<!-- <td align="right">
											<button type="button" id="doAddButton"
													onclick="doAdd();" class=btn>
															&nbsp;��&nbsp;�� &nbsp;
											</button>
										</td> -->
									</tr>
								</table>
							</td>
						</tr>

						<TR bgcolor="#FFFFFF">
							<td colspan="4">
								<%@ include file="/gwms/share/gwShareDeviceQuery_paramNode.jsp"%>
							</td>
						</TR>
						<tr bgcolor="#FFFFFF">
							<td>
									<input type="hidden" name="param" value="" />
									<TABLE id="table_showConfig" width="100%" border=0
										cellspacing=0 cellpadding=0 align="center" class="querytable">
										<!-- <tr>
											<th  align="center" width="100%">
												����    ����
											</th>
												
										</tr> -->
										<tr bgcolor="#FFFFFF">
															<td colspan="4">
																<div id="selectedDev">
																	���ѯ�豸��
																</div>
															</td>
														</tr>
										<TR>
											<TD bgcolor=#999999>
											 <div id="fatherDiv">
												<div id="paramAddDiv0">
													<table border=0 cellspacing=1 cellpadding=1 width="100%">
														<%if("1".equals(do_type)){%>
															<TR id= "gwShare_thTitle" height="50px">
																<th align="right" class=column width="35%" style="font-size: 19px;text-align: right;font-weight: normal;">LOID</th>
																<th class=column width="65%"  style="text-align: left;">
																	<input type="text" name="username" style="height: 25px; font-size: 17px; text-align: left">
																</th>
															</TR>
														<% }else{%>
														<TR id= "gwShare_thTitle" height="50px;">
															<th align="right" class=column width="35%" style="font-size: 20px;text-align: right;font-weight: normal;">Status</th>
															<th style="font-size: 19px;text-align: left;font-weight: normal;">0</th>
														</TR>
														<TR bgcolor="#FFFFFF">
															<th align="right" class=column width="35%" style="font-size: 20px;text-align: right;font-weight: normal;">Result</th>
															<th style="font-size: 19px;text-align: left;font-weight: normal;">1</th>
														</TR>
														<%} %>
																<%-- <span >
																	<a href="javascript:doDelete()">
																		ɾ��    |
																	 </a>
																</span> --%>
																<!-- <td width="70%" colspan="3" >
																	<input type="text" id="paramNodePath0" name="paramNodePath0" style="width:800px;"> 
																</td> -->
																<%-- <td  align="right" class=column width="20%">
																����ֵ
															</td>
															<td  width="30%" >
																<input type="text" id="paramValue0" name="paramValue0" value=""/>
															</td>
															<td  align="right" class=column width="30%">
																��������
															</td>
															<td  width="70%" >
																<select name="paramType0" id="paramType0" cssClass="bk">
																 	<option value="-1">==��ѡ��==</option>
																 	<option value="1">string</option>
																 	<option value="2">int</option>
																 	<option value="3">unsignedInt</option>
																 	<option value="4">boolean</option>
																 </select>
															</td> --%>
															
														</table>
														</div>
													</div>
													<table  border=0 cellspacing=1 cellpadding=2 width="100%">
															<TR bgcolor="#FFFFFF">
																<TD colspan="4" align="right" class="green_foot">
																	<button type="button" id="exeButton" name="exeButton"
																		onclick="doExecute();" class=btn>
																				&nbsp;ִ&nbsp;�� &nbsp;
																	</button>
																</TD>
															</TR>
															<TR bgcolor="#FFFFFF">
																<TD colspan="4" align="left" class="green_foot">
																	<div id="resultDIV"></div>
																</TD>
															</TR>
													</table>
											</TD>
										</TR>
									</TABLE>
							</td>
						</tr>
					</table>
				</TD>
			</TR>
		</TABLE>
	</body>
<script type="text/javascript">
	var deviceIds = "";
	var param = "";
	var do_type = "<%=do_type%>";
	$(function(){
		if(do_type == 1){
			$("input[@name='gwShare_queryResultType']").val("radio");
		}else{
			$("input[@name='gwShare_queryResultType']").val("checkbox");
		}
	});
	
	function deviceResult(returnVal){

		$("#exeButton").attr("disabled",false);
		deviceIds ="";
		$("table[@id='table_showConfig']").css("display","");
		$("#resultDIV").html("");
		var totalNum = returnVal[0];
		if(returnVal[0]==0){
			totalNum = returnVal[2].length;
			$("div[@id='selectedDev']").html("<font size=3><strong>��ѡ���豸:"+returnVal[2].length+"</strong></font>");
			for(var i=0;i<returnVal[2].length;i++){
				deviceIds = deviceIds + returnVal[2][i][0]+",";			
			}
		}else{
			$("div[@id='selectedDev']").html("<font size=2><strong>��ѡ���豸:"+returnVal[0]+"</strong></font>");
			deviceIds = "0";
			param=returnVal[1];
		}
		// �ж�δ�����Ե�����
		var url = "<s:url value='/gtms/config/paramNodeBatchConfigAction!queryUndoNum.action'/>"; 
		var maxNum = 50000;
		$.post(url,{} ,function(ajax){
	          var num = parseInt(ajax);
	           if(num > maxNum)
	           {
	           		alert("�����������Ѵﵽ���ޣ������������ã�");
	           		$("#exeButton").attr("disabled",true);
		            return;
	           }
	    });
	}
	
	//������ӵ�ʱ������һ��
	var flag = 0;
    function doAdd(){
    	flag++ ;
    	//����paramAddDiv0���Ԫ��
		$("#fatherDiv").append($("#paramAddDiv0").clone(true).attr("id","paramAddDiv"+flag));
		$("#paramAddDiv"+flag).each(function(){
			//����¡�����������ȫ���ÿ�
			$(this).find("input[type='text']").val("");
			$(this).find("select[name='paramType0']").val("");
			//���Ŀ�¡����div�ĸ���������id
			$(this).find("input[name='paramNodePath0']").attr("id","paramNodePath"+flag).attr("name","paramNodePath"+flag);
			$(this).find("input[name='paramValue0']").attr("id","paramValue"+flag).attr("name","paramValue"+flag);
			$(this).find("input[name='paramType0']").attr("id","paramType"+flag).attr("name","paramType"+flag);
		});
	}
	//��̬��ɾ��ɾ����һ��,����ֻɾ�����һ��
	function doDelete(){
		   if($("#fatherDiv div").size()>1){
		   		$("#fatherDiv div:last-child").remove();
		   }else{
		   		alert("�������һ�У��޷���ɾ��");
		   		return;
		   }
		    flag--;
		}
    function checkForm(){
       $("#fatherDiv div").each(function(i){
			//��������	
			var tempPath = $("#paramNodePath"+i).val();
			var tempValue = $("#paramValue"+i).val();
			var tempType = $("#paramType"+i).val();
			if(tempPath==""){
				alert("��"+(i+1)+"�������ڵ�·��Ϊ�գ�");
				return false;
			}
			if(tempValue==""){
				alert("��"+(i+1)+"������ֵΪ�գ�")
				return false;
			}
			if(tempType==""){
				alert("��"+(i+1)+"����������Ϊ�գ�")
				return false;
			}
        });
        return true;
    }
	//ִ������
	function doExecute(){
	
	   var url = "<s:url value='/gtms/config/paramNodeBatchConfigAction!doConfigAll.action'/>"; 
	  // $("#doButton").attr("disabled",true);
	   var len = $("#fatherDiv div").size();
	   var gwType =<%=gwType%>;
	   var dpi=false;
	   var do_type = <%=do_type%>;
	   if(do_type == 1){
		   var paramNodePath = ",InternetGatewayDevice.X_CT-COM_UserInfo.UserName";
		   var paramValue =  "," + $("input[@name='username']").val();
		   var paramType = ",1";
	   }else{
		   var paramNodePath = ",InternetGatewayDevice.X_CT-COM_UserInfo.Status,InternetGatewayDevice.X_CT-COM_UserInfo.Result"; 
		   var paramValue = ",0,1";
		   var paramType = ",1,1";
	   }
	   
        if(checkForm()){
	        $.post(
	                url,{
	                deviceIds : deviceIds,
	                param:param,
	                paramNodePath : paramNodePath,
	                paramValue : paramValue,
	                paramType : paramType,
	                dpi : dpi,
	                gw_type : gwType
	         } ,function(ajax){
	                $("#resultDIV").html("");
	                $("#doButton").attr("disabled",false);
	                if("1"==ajax){
	                      $("#resultDIV").append("��ִ̨�гɹ�");
	                 }else{
	                     $("#resultDIV").append("��ִ̨��ʧ��");
	                 }
	        });
        }
	}
	 	
	</script>
</html>
<%@ include file="../../foot.jsp"%>