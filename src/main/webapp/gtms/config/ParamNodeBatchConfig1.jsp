<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	String gwType = request.getParameter("gw_type");
	String tempdpi = request.getParameter("dpi");
	String isShowGJ = request.getParameter("isShowGJ");
%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=GBK">
	<%if("true".equals(request.getParameter("dpi"))){%><title>dpi�����·�</title><%}else{ %><title>�ն�ʱ��ͬ������</title><%}%>
	<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
	<link href="../../css/listview.css" rel="stylesheet" type="text/css">
	<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.js"></SCRIPT>
	<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckForm.js"></SCRIPT>
</head>
<body>
	<input type="hidden" name="instArea" value="<s:property value="instArea"/>" />
	<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
		<TR><TD HEIGHT=20>&nbsp;</TD></TR>
		<TR>
			<TD>
				<table width="98%" border="0" align="center" cellpadding="0" cellspacing="0" class="text">
					<tr>
						<td>
							<table width="100%" height="30" border="0" cellspacing="0"
								cellpadding="0" class="green_gargtd">
								<tr>
									<td width="162" align="center" class="title_bigwhite" nowrap>
										<%if("true".equals(request.getParameter("dpi"))){%>dpi�����·�<%}else{ %>�ն�ʱ��ͬ������<%}%>
									</td>
									<td align="right">
										
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<TR bgcolor="#FFFFFF">
						<td colspan="4">
							<%@ include file="/gwms/share/gwShareDeviceQuery.jsp"%>
						</td>
					</TR>
					<tr bgcolor="#FFFFFF">
						<td>
							<input type="hidden" name="param" value="" />
							<TABLE id="table_showConfig" width="100%" border=0
								cellspacing=0 cellpadding=0 align="center" class="querytable">
								<tr>
									<th align="center" width="100%">����    ����</th>
								</tr>
								<tr bgcolor="#FFFFFF">
									<td colspan="4"><div id="selectedDev">���ѯ�豸��</div></td>
								</tr>
								<TR>
									<TD bgcolor=#999999>
										 <div id="fatherDiv">
											<div id="paramAddDiv0">
												<table border=0 cellspacing=1 cellpadding=2 width="100%">
													<TR bgcolor="#FFFFFF">
														<td nowrap align="right" class=column width="20%">
															<span ><a href="javascript:doDelete()">ɾ��    |</a></span>�����ڵ�·��
														</td>
														<td width="70%" colspan="3" >
															<input type="text" id="paramNodePath0" name="paramNodePath0" style="width:800px;" 
																value="InternetGatewayDevice.Time.X_CT-COM_NTPServerType" > 
														</td>
													</TR>
													<TR bgcolor="#FFFFFF">
														<td align="right" class=column width="20%">����ֵ</td>
														<td width="30%" >
															<input type="text" id="paramValue0" name="paramValue0" value="2"/>
														</td>
														<td align="right" class=column width="30%">��������</td>
														<td width="70%" >
															<select name="paramType0" id="paramType0" cssClass="bk">
															 	<option value="-1">==��ѡ��==</option>
															 	<option value="1">string</option>
															 	<option value="2">int</option>
															 	<option value="3" selected>unsignedInt</option>
															 	<option value="4">boolean</option>
															 </select>
														</td>
													</TR>
												</table>
												</div>
												<div id="paramAddDiv1">
												<table border=0 cellspacing=1 cellpadding=2 width="100%">
													<TR bgcolor="#FFFFFF">
														<td nowrap align="right" class=column width="20%">
															<span ><a href="javascript:doDelete()">ɾ��    |</a></span>�����ڵ�·��
														</td>
														<td width="70%" colspan="3" >
															<input type="text" id="paramNodePath1" name="paramNodePath1" style="width:800px;" 
																value="InternetGatewayDevice.Time.Enable" > 
														</td>
													</TR>
													<TR bgcolor="#FFFFFF">
														<td align="right" class=column width="20%">����ֵ</td>
														<td width="30%" >
															<input type="text" id="paramValue1" name="paramValue1" value="1"/>
														</td>
														<td align="right" class=column width="30%">��������</td>
														<td width="70%" >
															<select name="paramType1" id="paramType1" cssClass="bk">
															 	<option value="-1">==��ѡ��==</option>
															 	<option value="1">string</option>
															 	<option value="2">int</option>
															 	<option value="3" selected>unsignedInt</option>
															 	<option value="4">boolean</option>
															 </select>
														</td>
													</TR>
												</table>
											</div>
											
										</div>
										<table border=0 cellspacing=1 cellpadding=2 width="100%">
												<TR bgcolor="#FFFFFF">
													<TD colspan="4" align="right" class="green_foot">
														<button type="button" id="exeButton" name="exeButton"
															onclick="doExecute();" class=btn>
																	&nbsp;ִ&nbsp;�� &nbsp;
														</button>
													</TD>
												</TR>
												<TR bgcolor="#FFFFFF">
													<TD colspan="4" align="left" class="green_foot"><div id="resultDIV" /></TD>
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
	var isShowGJ = "<%=isShowGJ%>";
	$(function(){
		if("1"==isShowGJ){
			gwShare_setGaoji();
			setIsGJ(isShowGJ);
		}
		//gwShare_setImport();
		$("input[@name='gwShare_queryResultType']").val("checkbox");
	});
	
	function deviceResult(returnVal){
		$("#exeButton").attr("disabled",false);
		deviceIds ="";
		$("table[@id='table_showConfig']").css("display","");
		$("#resultDIV").html("");
		var totalNum = returnVal[0];
		if(returnVal[0]==0){
			totalNum = returnVal[2].length;
			if(1000<totalNum && "nx_dx"!=returnVal[3]){
				alert("���������豸��ӦС��1000̨��");
	       		$("#exeButton").attr("disabled",true);
	            return;
			}
			$("div[@id='selectedDev']").html("<font size=2><strong>�������豸��Ŀ:"+returnVal[2].length+"</strong></font>");
			for(var i=0;i<returnVal[2].length;i++){
				deviceIds = deviceIds + returnVal[2][i][0]+",";			
			}
		}else{
			$("div[@id='selectedDev']").html("<font size=2><strong>�������豸��Ŀ:"+returnVal[0]+"</strong></font>");
			deviceIds = "0";
			param=returnVal[1];
		}

		if(1000<returnVal[0] && "nx_dx"!=returnVal[3]){
			alert("���������豸��ӦС��1000̨��");
       		$("#exeButton").attr("disabled",true);
            return;
		}
		
		if("nx_dx"!=returnVal[3]){
			// �ж�δ�����Ե�����
			var url = "<s:url value='/gtms/config/paramNodeBatchConfigAction!queryUndoNum.action'/>"; 
			var maxNum = 50000;
			$.post(url,{} ,function(ajax){
		          var num = parseInt(ajax);
		           if(num > maxNum){
		           		alert("�����������Ѵﵽ���ޣ������������ã�");
		           		$("#exeButton").attr("disabled",true);
			            return;
		           }
		    });
		}
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
	   var dpi=<%=tempdpi%>;
	   var paramNodePath = ""; 
	   var paramValue = "";
	   var paramType = "";
		$("#fatherDiv div").each(function(i){
			//��������	
			paramNodePath += "," + $("#paramNodePath"+i).val();
			paramValue    += "," + $("#paramValue"+i).val();
			paramType     += "," + $("#paramType"+i).val();
        });

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