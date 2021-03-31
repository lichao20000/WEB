<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
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
		<script type="text/javascript" src="../../../Js/My97DatePicker/WdatePicker.js"></script>
		<lk:res />
		<SCRIPT LANGUAGE="JavaScript">

	var gw_type = "<%=request.getParameter("gw_type")%>";

	$(function(){
		gwShare_setGaoji();
		
	});
	function deviceResult(returnVal){
		for(var i=0;i<returnVal[2].length;i++){
			$("input[@name='deviceId']").val(returnVal[2][i][0]);
			$("tr[@id='trDeviceResult']").css("display","");
			$("td[@id='tdDeviceSn']").html(returnVal[2][i][1]);
			$("td[@id='tdDeviceSn']").append("-");
			$("td[@id='tdDeviceSn']").append(returnVal[2][i][2]);
			$("td[@id='tdDeviceCityName']").html(returnVal[2][i][5]);
		}
	}
	
  	function ExecMod(){ 
  		
  		if($.trim($("#taskName").val()).length==0){
  			alert("��������������");
  			return false;
  		}  		
  		if($.trim($("#start_time").val()).length==0){
  			alert("��ѡ������ʱ��");
  			return false;
  		}
  		var taskType = $("input[@name='taskType']").val();
  		if("1"==taskType){
  			var cityIDSStr ="";
  			$("input[@name='cityIDS'][@checked]").each(function(){ 
  				cityIDSStr += $(this).val()+",";
  		    });
  			if($.trim(cityIDSStr).length==0){
  				alert("��ѡ������");
  	  			return false;
  			}
  			if(cityIDSStr!=""){
  				cityIDSStr=cityIDSStr.substring(0,cityIDSStr.length-1);
  			}
  			$("input[@name='cityIds']").val(cityIDSStr);
  		}else if("2"==taskType){
  			var isImportCustomer = importCustomer();
			if(!isImportCustomer){
				return false;
			}
  		}
  		
  		$("form[@name='batchexform']").attr("action","<%=request.getContextPath()%>/gtms/stb/diagnostic/stbDeviceBatchReboot!importConfig.action");
		$("form[@name='batchexform']").submit();
    }

	
	 /**
	**�������ػ�ȡ�¼����أ����Ը�ѡ�����ʽ���ֳ���
	**/ 
  function getNextCity(){
		var cityId = $("select[@name='cityId']").val();
		var url = "<s:url value='/gtms/stb/diagnostic/stbDeviceBatchReboot!getNextCity.action'/>";
		//if(cityId!="-1" && cityId != "00"){
		if(cityId!="-1"){	
			$("div[@id='adaptNextCity']").html("");
			$.post(url,{
				cityId:cityId
			},function(ajax){
				if(ajax!=""){
					var lineData = ajax.split("#");
					if(typeof(lineData)&&typeof(lineData.length)){
						for(var i=0;i<lineData.length;i++){
							var oneElement = lineData[i].split("$");
							var xValue = oneElement[0];
							var xText = oneElement[1];
							
							if(cityId != "00"){
								var checkboxtxt = "&nbsp;&nbsp;&nbsp;<input type='checkbox' name='cityIDS' value='"+xValue+"'>"+xText+"  ";
								$("div[@id='adaptNextCity']").append(checkboxtxt);
							}else if(cityId == "00"){
								if(xValue.length<=3){
									var checkboxtxt = "&nbsp;&nbsp;&nbsp;<input type='checkbox' name='cityIDS' value='"+xValue+"'>"+xText+"  ";
									$("div[@id='adaptNextCity']").append(checkboxtxt);
								}
							}
						}
					}else{
						$("div[@id='adaptNextCity']").append("������û���¼����أ�");
					}
				}else{
					$("div[@id='adaptNextCity']").append("������û���¼����أ�");
				}
			});
		}else if(cityId == "00"){
			$("div[@id='adaptNextCity']").html("");
		}else{
			$("div[@id='adaptNextCity']").html("��ѡ������");
		}
	}
		
	function ExportMod(){
		var buttonVal = $("#doButtonExport").val();
		if("���붨��"==buttonVal){
			$("#areaId").css("display","none");
			$("#areaIds").css("display","none");
			$("#importCustomer").css("display","");
			$("#doButtonExport").val("���ض���");
			$("input[@name='taskType']").val("2");// 1:���ض��ƣ�2:���붨�ƣ�
		}else{
			$("#areaId").css("display","");
			$("#areaIds").css("display","");
			$("#importCustomer").css("display","none");
			$("#doButtonExport").val("���붨��");
			$("input[@name='taskType']").val("1");// 1:���ض��ƣ�2:���붨�ƣ�
		}
	}
	
	//�����ʺ���Ϣ
    function importCustomer(){
    	var myFileCustomer = $("input[@name='uploadCustomer']").val();
		if(""==myFileCustomer){	
			alert("��ѡ���ļ�!");
			return false;
		}
		var filetCustomer = myFileCustomer.split(".");
		if(filetCustomer.length<2)
		{
			alert("��ѡ���ļ�!");
			return false;
		}
		if("xls" == filetCustomer[filetCustomer.length-1] || "txt" == filetCustomer[filetCustomer.length-1])
		{
			var file2 = myFileCustomer.split("\\");
			var fileName2 = file2[file2.length-1];
			$("input[@name='uploadFileName4Customer']").attr("value",fileName2);
			return true;
		}else
		{
			alert("��֧�ֺ�׺Ϊxls��txt���ļ�");
			return false;
		}
    }
	
    function queryTask()
	{
		var taskNameQ = $("input[name=taskNameQ]").val();
		var url = "<s:url value='/gtms/stb/diagnostic/stbDeviceBatchReboot!initBatchConfig.action'/>?taskNameQ=" + taskNameQ;
	    window.location.href=url;
	}
    
    function viewdetailTask(taskId)
	{
		var page = "<s:url value='/gtms/stb/diagnostic/stbDeviceBatchReboot!queryRestartDev.action'/>?taskIdQ="+taskId;
		window.open(page,"","left=20,top=20,width=800,height=400,resizable=no,scrollbars=yes");
	}
    
    function deleteTask(taskIdD){
    	if(confirm("�Ƿ�ȷ��ɾ��������")){
    		var url = "<s:url value='/gtms/stb/diagnostic/stbDeviceBatchReboot!deleteTask.action'/>?taskIdD=" + taskIdD+"&delete=yes";
    	    window.location.href=url;
    	}
	}
	
</SCRIPT></head>
<TABLE width="98%" align="center" class="querytable">
	<TR>
		<TD>
			<IMG height=20 src="<s:url value='/images/bite_2.gif'/>" width=24>
			����ǰ��λ�ã������豸����
		</TD>
	</TR>
</TABLE>
<s:form action="stbDeviceBatchReboot!importConfig.action" method="post" enctype="multipart/form-data" name="batchexform">
<input type="hidden" name="taskType" value="1"/>
<input type="hidden" name="uploadFileName4Customer" value=""/>
<input type="hidden" name="cityIds" value=""/>
<table class="querytable" width="98%" align="center">
				<tr>
					<td colspan="4" class="title_1">
						��������������������
					</td>
				</tr>
				<TR>
					<TD class="title_2" align="center" width="15%">��������</TD>
					<TD colspan="3"> <input type="text" id="taskName" name="taskName" width="500"> </TD>
				</TR>
				<TR>
					<TD class="title_2" align="center" width="15%">����ִ��ʱ��</TD>
					<TD colspan="3">
					<input type="text" name="startTime" id="start_time" class='bk' readonly
						value="<s:property value='startTime'/>">
					<img name="shortDateimg"
						onClick="WdatePicker({el:document.batchexform.startTime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
						src="../../../images/dateButton.png" width="15" height="12"
						border="0" alt="ѡ��" />
					</TD>
				</TR>
				<TR id="areaId">
					<TD class="title_2" align="center" width="15%">
						����
					</TD>
			   <TD width="85%" colspan="3">
						<s:select list="cityList" name="cityId" headerKey="-1"
							headerValue="��ѡ������" listKey="city_id" listValue="city_name"
							value="cityId" cssClass="bk" theme="simple" onchange="getNextCity();"></s:select>
					</TD>
			    </TR>
			    <TR id="areaIds">
					<TD class="title_2" align="center" width="15%">
						���ط�Χ����
					</TD>
					<TD width="85%" colspan="3">
						<div id="adaptNextCity">
							 ��ѡ������
						</div>
					</TD>
				</TR>
				
				<tr id="importCustomer" style="display: none;">
					<td class="title_2" align="center" width="15%">
						��ѡ���ļ�
					</td>
					<td width="85%" colspan="3">
						<s:file label="�ϴ�" theme="simple" name="uploadCustomer"></s:file><font color="red">*</font>
						xls,txt��ʽ�ĵ�
					</td>
				</tr>
				
				<TR>
					<td colspan="4" class="foot" align="right">
						<div class="right">
							<button id="doButtonExport" onclick="ExportMod()">
								���붨��
							</button>&nbsp;
							<button id="doButton" onclick="ExecMod()">
								����
							</button>
						</div>
					</td>
				</TR>
			</table>
</s:form>


		<br>
		<br>
		<table width="98%" class="querytable" align="center">
		<TR>
			<TD class="title_2" align="center" width="15%">��������</TD>
			<TD colspan="3"> <input type="text" id="taskNameQ" name="taskNameQ" width="500">&nbsp;
			<button onclick="queryTask()">��ѯ</button>
			</TD>
			</tr>
		</table>
		<table width="98%" class="listtable" align="center">
			<thead>
				<tr>
					<th align="center" width="13%">
						������
					</th>
					<th align="center" width="15%">
						��������
					</th>
					<th align="center" width="25%">
						����
					</th>
					<th align="center" width="15%">
						����ʱ��
					</th>
					<th align="center" width="15%">
						���
					</th>
					<th align="center" width="17%">
						�鿴����
					</th>
				</tr>
			</thead>
			<s:if test="tasklist!=null">
				<s:if test="tasklist.size()>0">
					<tbody>
						<s:iterator value="tasklist">
							<tr>
								<td align="center">
									<s:property value="taskNameR" />
								</td>
								<td align="center">
									<s:property value="taskTypeR" />
								</td>
								<td align="center">
									<s:property value="cityNameR" />
								</td>
								<td align="center">
									<s:property value="taskTimeR" />
								</td>
								<td align="center">
									<s:property value="taskStatusR" />
								</td>
								<td align="center">
									<button name="cancerButton"
											onclick="javascript:deleteTask('<s:property value="taskIdR"/>')">
											ɾ��
									</button>&nbsp;&nbsp;&nbsp;&nbsp;
									<button name="viewDetailButton"
											onclick="javascript:viewdetailTask('<s:property value="taskIdR"/>')">
											�鿴��ϸ
									</button>
								</td>
							</tr>
						</s:iterator>
					</tbody>
					<tfoot>
						<tr bgcolor="#FFFFFF">
							<td colspan="6" align="right">
								<lk:pages url="/gtms/stb/diagnostic/stbDeviceBatchReboot!initBatchConfig.action"
									styleClass="" showType="" isGoTo="true" changeNum="false" />
							</td>
						</tr>
					</tfoot>
				</s:if>
				<s:else>
					<tbody>
						<tr>
							<td colspan="6">
								<font color="red">û�ж��Ƶ�����</font>
							</td>
						</tr>
					</tbody>
				</s:else>
			</s:if>
			<s:else>
				<tbody>
					<tr>
						<td colspan="6">
							<font color="red">û�ж��Ƶ�����</font>
						</td>
					</tr>
				</tbody>
			</s:else>


		</table>
			
		
			
