<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../../timelater.jsp"%>
<%
	String gwType = request.getParameter("gw_type");
	String tempdpi = request.getParameter("dpi");
	String isShowGJ = request.getParameter("isShowGJ");
%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=GBK">
	<%if("true".equals(request.getParameter("dpi"))){%><title>dpi�����·�</title><%}else{ %><title>������������������</title><%}%>
	<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
	<link href="../../css/listview.css" rel="stylesheet" type="text/css">
	<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.js"></SCRIPT>
	<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckForm.js"></SCRIPT>
	<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<%@ include file="../../toolbar.jsp"%>
	<input type="hidden" name="instArea" value="<s:property value="instArea"/>" />
	<TABLE border=0 cellspacing=0 cellpadding=0 width="100%" id="myTable">
		<TR><TD HEIGHT=20>&nbsp;</TD></TR>
		<TR>
			<TD>
				<FORM NAME="mainForm" id="mainForm" METHOD="post" ACTION="" target="dataForm">
				<TABLE width="100%" border=0 cellspacing=0 cellpadding=0
				align="center">
					<tr>
						<td>
							<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
								<tr>
									<td width="162" align="center" class="title_bigwhite" nowrap>
										���������·�
									</td>
									<td align="right">
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<TR bgcolor="#FFFFFF">
						<td colspan="4">
							<%@ include file="/gwms/share/gwShareDeviceQuery_newBatchSet.jsp"%>
						</td>
					</TR>
					<tr>
						<TD bgcolor=#000000>
							<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
								<tr>
									<th align="center" width="100%" colspan="4">���� ģ��</th>
								</tr>
								<tr style="cursor: hand; background-color: #cccccc">
									<TD class="column text" nowrap align="center" width="15%" colspan="4">
										<div id="selectedDev">
											���ѯ�豸��
										</div>
									</td>
								</tr>
								<tr style="cursor: hand; background-color: #cccccc">
									<TD class="column text" nowrap align="center" width="15%">��������</TD>
									<TD colspan="1" class="column text" nowrap width="35%">
										<INPUT TYPE="text" style="font-size: 12px;" id="task_name" name="task_name" size=15>&nbsp;<font color="#FF0000"></font>
									</TD>
									<TD class="column text" nowrap align="center" width="15%">���Դ�����ʽ</TD>
									<TD colspan="1" class="column text" nowrap width="35%">
										<input type="checkbox" name="dotype" class="bk" value="2"><span>�����ϱ�</span>
										<input type="checkbox" name="dotype" class="bk" value="5"><span>��������</span>
										<input type="checkbox" name="dotype" class="bk" value="4"><span>�´����ӵ�ϵͳ</span>
										<input type="checkbox" name="dotype" class="bk" value="6"><span>�����ı�</span>
										<input type="checkbox" name="donow" checked="checked" class="bk" value="1"><span>��������</span>
									</TD>
								</tr>
								<tr style="cursor: hand; background-color: #cccccc">
									<TD class="column text" nowrap align="center" width="15%">ģ������</TD>
									<TD colspan="1" class="column text" nowrap width="35%">
										<INPUT TYPE="text" style="font-size: 12px;" id="name" name="name" size=15 value="<s:property value='name'/>">&nbsp;<font color="#FF0000"></font>
									</TD>
									<TD class="column text" nowrap align="center" width="15%">
										<INPUT TYPE="button" style="font-size: 12px;" size=15 value="����ģ��" onclick="loadSelect()">
									</TD>
									<TD colspan="1" class="column text" nowrap width="35%">
										<select name="template" id="template" style="color: black;font-size: 12px;height:24px;">
										<option value='-1' style="height: 24px;">��ѡ��</option>
										</select>
									</TD>
								</tr>
								<tr style="cursor: hand; background-color: #F4F4FF">
									<td align="center" width="15%">
										������ʼ���ڣ�
									</td>
									<td align="left" width="30%">
										<input type="text"   name="expire_date_start" value="<s:property value='expire_date_start'/>" class=bk >
											<img name="shortDateimg"
						 						onClick="WdatePicker({el:document.mainForm.expire_date_start,dateFmt:'yyyy-MM-dd',skin:'whyGreen'})"
						 							src="../../images/dateButton.png" width="15" height="12"
						 								border="0" alt="ѡ��">
									</td>

									<td align="center" width="15%">
										�����������ڣ�
									</td>
									<td align="left" width="30%">
										<input type="text"  name="expire_date_end" value="<s:property value='expire_date_end'/>" class=bk >
											<img name="shortDateimg"
						 						onClick="WdatePicker({el:document.mainForm.expire_date_end,dateFmt:'yyyy-MM-dd',skin:'whyGreen'})"
						 							src="../../images/dateButton.png" width="15" height="12"
						 								border="0" alt="ѡ��">
									</td>
								</tr>
								<tr style="cursor: hand; background-color: #F4F4FF">
									<td align="center" width="15%">
										������ʼʱ�䣺
									</td>
									<td align="left" width="30%">
										<input type="text"   name="expire_time_start" value="<s:property value='expire_time_start'/>" class=bk >
											<img name="shortDateimg"
						 						onClick="WdatePicker({el:document.mainForm.expire_time_start,dateFmt:'HH:mm:ss',skin:'whyGreen'})"
						 							src="../../images/dateButton.png" width="15" height="12"
						 								border="0" alt="ѡ��">
									</td>

									<td align="center" width="15%">
										��������ʱ�䣺
									</td>
									<td align="left" width="30%">
										<input type="text"  name="expire_time_end" value="<s:property value='expire_time_end'/>" class=bk >
											<img name="shortDateimg"
						 						onClick="WdatePicker({el:document.mainForm.expire_time_end,dateFmt:'HH:mm:ss',skin:'whyGreen'})"
						 							src="../../images/dateButton.png" width="15" height="12"
						 								border="0" alt="ѡ��">
									</td>
								</tr>
								<!-- <tr style="cursor: hand; background-color: #cccccc">
									<TD class="column text" nowrap align="center" width="15%">������������</TD>
									<TD colspan="3" class="column text" nowrap width="35%">
										<input type="checkbox" name="donow" checked="checked" class="bk" value="1">
									</TD>
								</tr> -->
								<TR>
									<TD colspan="4" align="right" class=foot style="vertical-align: middle;"><input TYPE="button" value="����" style="height: 22px;font-size: 13px;" onclick="batchSet('<s:property value="id" />')"> </TD>
								</TR>
							</TABLE>
						</td>
					</tr>
				</table>
				</FORM>
			</TD>
		</TR>
	</TABLE>
</body>

<script type="text/javascript">
	var deviceIds = "";
	var param = "";
	var isShowGJ = "1";
	$(function(){
		if("1"==isShowGJ){
			gwShare_setGaoji();
			setIsGJ(isShowGJ);
		}
		$("input[@name='gwShare_queryResultType']").val("checkbox");
		gwShare_setImport();
		loadSelect();
	});
	
	function deviceResult(returnVal){
		$("#exeButton").attr("disabled",false);
		deviceIds ="";
		$("table[@id='table_showConfig']").css("display","");
		$("#resultDIV").html("");
		var totalNum = returnVal[0];
		device_id = returnVal[3];
		if(returnVal[0]==0){
			totalNum = returnVal[2].length;
			if(5000<totalNum && "nx_dx"!=returnVal[3]){
				alert("���������豸��ӦС��5000̨��");
	       		$("#exeButton").attr("disabled",true);
	            return;
			}
			$("div[@id='selectedDev']").html("<font size=2><strong>�������豸��Ŀ:"+returnVal[2].length+"</strong></font>");
			deviceCount = returnVal[2].length;
			for(var i=0;i<returnVal[2].length;i++){
				deviceIds = deviceIds + returnVal[2][i][0]+",";			
			}
		}else{
			$("div[@id='selectedDev']").html("<font size=2><strong>�������豸��Ŀ:"+returnVal[0]+"</strong></font>");
			deviceCount = returnVal[0];
			deviceIds = "0";
			param=returnVal[1];
		}

		if(5000<returnVal[0] && "nx_dx"!=returnVal[3]){
			alert("���������豸��ӦС��5000̨��");
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
			$(this).find("select[name='paramType0']").attr("id","paramType"+flag).attr("name","paramType"+flag);
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
	
    
    
	//ִ������
	function doExecute(){
		alert(deviceCount+device_id+queryTypeNew+$("#gwShare_queryParam").val());
		return;
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
	
	function loadSelect(){
		$.ajax({
    	    type:"POST",
    	    url:"<s:url value='/gwms/resource/servTemplate!queryTemplateStr.action' />",
    	    data: {"name":$("#name").val()},
    	    success:function (ajax) {
    	    	var template = $("#template");
    			template.html("");
    			option = "<option value='-1' selected style='height: 24px;'>��ѡ��</option>";
    			template.append(option);
    			if(""==ajax){
    				return false;
    			}
    			
    			var lineData = ajax.split("#");
    			if(!typeof(lineData) || !typeof(lineData.length)){
    				return false;
    			}
    			
    			for(var i=0;i<lineData.length;i++){
    				var oneElement = lineData[i].split("$");
    				var xValue = oneElement[0];
    				var xText = oneElement[1];
    				//����ÿ��value��text��ǵ�ֵ����һ��option����
    				option = "<option value='"+xValue+"' style='height: 24px;'>"+xText+"</option>";
    				try{
    					template.append(option);
    				}catch(e){
    					alert("ģ�����ʧ�ܣ�");
    				}
    			}
    	    }
    	});
	}
	
	/*���������ύ*/
	function batchSet(){
		/* var deviceCount = "0";
		var queryTypeNew = "";
		var device_id = ""; */
	    var templateId = $("#template").val();
	    if(templateId=="-1"){
	    	alert("��ѡ��ģ��");
	    	return false;
	    }
	    if('' == $("#task_name").val()){
	    	alert("�������Ʋ���Ϊ��");
	    	return false;
	    }
	    if('0' == deviceCount){
	    	alert("��ѡ���豸");
	    	return false;
	    }
	    
	    var starttime = $("input[@name='expire_time_start']").val();
	    var endtime = $("input[@name='expire_time_end']").val();
	    var startdate = $("input[@name='expire_date_start']").val();
	    var enddate = $("input[@name='expire_date_end']").val();
	    
	    if(enddate<startdate || starttime>endtime){
	    	alert("������ʼ����/ʱ��С�ڴ�����������/ʱ��");
	    	return false;
	    }
	    	
	    	
	    var obj = $("input[@name='donow']");
        var donow = '0';
        for(k in obj){
            if(obj[k].checked)
            	donow = "1";
        }
        
	    obj = $("input[@name='dotype']");
        var check_val = '';
        for(k in obj){
            if(obj[k].checked)
            	check_val = check_val + obj[k].value;
        }
        
	    if(check_val == '')
	    {
	    	alert("��ѡ����Դ�����ʽ");
	    	return false;
	    }
	    else{
	    	var url = "<s:url value='/gtms/config/paramNodeBatchConfigAction!addTemplateTaskStb.action' />?task_name="+$("#task_name").val()+"&template_id="+templateId+"&dotype="+check_val+"&starttime="+startdate+" "+starttime+"&endtime="+enddate+" "+endtime+"&donow="+donow;
	    	if("1"==queryTypeNew){
	    		var gwShare_queryField = $("input[@name='gwShare_queryField'][@checked]").val();
	    		var gwShare_queryParam = $.trim($("input[@name='gwShare_queryParam']").val());
	    		url = url + "&gwShare_queryType=1";
	    		url = url + "&device_id=" + device_id;
	    	}else if("2"==queryTypeNew){
	    		var gwShare_nextCityId = $.trim($("select[@name='gwShare_nextCityId']").val());
	    	 	var gwShare_cityId = $.trim($("select[@name='gwShare_cityId']").val());
	            var gwShare_vendorId = $.trim($("select[@name='gwShare_vendorId']").val());
	            var gwShare_deviceModelId = $.trim($("select[@name='gwShare_deviceModelId']").val());
	            var gwShare_devicetypeId = $.trim($("select[@name='gwShare_devicetypeId']").val());
	            var gwShare_bindType = $.trim($("select[@name='gwShare_bindType']").val());
	            url = url + "&gwShare_cityId=" + gwShare_cityId;
	            url = url + "&gwShare_nextCityId=" + gwShare_nextCityId;
	            url = url + "&gwShare_vendorId=" + gwShare_vendorId;
	            url = url + "&gwShare_deviceModelId=" + gwShare_deviceModelId;
	            url = url + "&gwShare_devicetypeId=" + gwShare_devicetypeId;
	            url = url + "&gwShare_bindType=" + gwShare_bindType;
	            url = url + "&gwShare_queryType=2";
	    	}else {
	    		var gwShare_fileName = $("input[@name='gwShare_fileName']").val();
	    		if(""==gwShare_fileName){
	    			alert("�����ϴ��ļ���");
	    			return;
	    		}
	    		url = url + "&gwShare_fileName=" + gwShare_fileName;
	    		url = url + "&gwShare_queryType=3";
	    	}
	    	
	    	$.ajax({
	    	    type:"POST",
	    	    async:false,
	    	    url:url,
	    	    dataType:"text",
	    	    data: {},
	    	    success:function (data) {
	    	    	if(data==1){
	    	    		alert("���Ƴɹ�");
	    	    		window.location.reload();
	    	    	}
	    	    	else
	    	 		{
	    	 			alert("����ʧ��");
	    	 		}
	    	    }
	    	});
	    }
	}
</script>
</html>
<%@ include file="../../foot.jsp"%>