<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<script type="text/javascript" src="../../Js/My97DatePicker/WdatePicker.js"></script>
<script language="JavaScript">
// ��ѯ
function doQuery(){
	
	var cityId = $.trim($("select[@name='cityId']").val());                // �豸����
    var starttime = $.trim($("input[@name='starttime']").val());           // ��ʼʱ��(����ʱ��)
    var endtime = $.trim($("input[@name='endtime']").val());               // ����ʱ��(����ʱ��)
    
    // ���У��
    if(!checkQuery(cityId, starttime, endtime)){
    	return;
    }
    
    $("#dataList").html("");
    $("tr[@id='trData']").show();
    $("div[@id='QueryData']").html("���ڲ�ѯ�����Ե�....");
    var url = '<s:url value='/itms/report/userOpenBindACT!userOpenBindList.action'/>'; 
	$.post(url,{
		cityId:cityId,
		starttime:starttime,
		endtime:endtime
	},function(ajax){
	    $("div[@id='QueryData']").html("");
		$("div[@id='QueryData']").append(ajax);
	});
}

//���У��
function checkQuery(cityId, starttime, endtime){
	if(cityId == "-1"){
        alert("��ѡ������");
        return false;
    }
	
	var startTime = strToTimestamp(starttime);
	var endTime = strToTimestamp(endtime);
	if(startTime > endTime){
		alert("��ʼʱ�䲻�ܴ��ڽ���ʱ��");
        return false;
	}
	if((endTime - startTime) > 60 * 60 * 24 * 31 * 3){
		alert("��ʼ������ʱ�䷶Χ���ܳ���3����");
        return false;
	}
	
	return true;
}

// ����Excel
function toExcel( cityId, starttime, endtime ) {
	var page="<s:url value='/itms/report/userOpenBindACT!userOpenBindListToExcel.action'/>?"
		+ "cityId=" + cityId 
		+ "&starttime=" +starttime
		+ "&endtime=" +endtime;
	document.all("childFrm").src=page;
}


// ʱ���ַ���ת��Ϊʱ���
function strToTimestamp(date){
	date = date.substring(0,19);    
	date = date.replace(/-/g,'/');
	return new Date(date).getTime() / 1000;
}


</script>

<br>
<TABLE>
	<tr>
		<td>
			<table class="green_gargtd">
				<tr>
					<th>
						�¿��û����ն����
					</th>
					<td>
						<img src="<s:url value="/images/attention_2.gif"/>" width="15" height="12">
						ʱ��Ϊ����ʱ��
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<form name=frm>
				<table class="querytable">
					<tr>
						<th colspan=4>
							�¿��û����ն����
						</th>
					</tr>
					<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
						<td class="column" align="right" width="15%">
							�豸����
						</td>
						<td width="35%">
							<s:select list="cityList" name="cityId" headerKey="-1"
								headerValue="��ѡ������" listKey="city_id" listValue="city_name"
								value="cityId" cssClass="bk"></s:select>
							&nbsp;<font style="color:red">*</font>
						</td>
						<td class="column" width='15%' align="right"></td>
                      	<td width='35%' align="left"></td>
					</TR>
					<tr bgcolor=#ffffff>
						<td class=column align=center width="15%">
							��ʼʱ��
						</td>
						<td>
							<input type="text" name="starttime" class='bk' readonly value="<s:property value="starttime"/>">
							<img name="shortDateimg"
								onClick="WdatePicker({el:document.frm.starttime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../images/dateButton.png" width="15" height="12"
								border="0" alt="ѡ��" />
						</td>
						<td class=column align=center width="15%">
							����ʱ��
						</td>
						<td>
							<input type="text" name="endtime" class='bk' readonly value="<s:property value="endtime"/>">
							<img name="shortDateimg"
								onClick="WdatePicker({el:document.frm.endtime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../images/dateButton.png" width="15" height="12"
								border="0" alt="ѡ��" />
						</td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=foot colspan=4 align=right>
							<button onclick="doQuery()">
								&nbsp;�� ѯ&nbsp;
							</button>
						</td>
					</tr>
				</table>
			</form>
		</td>
	</tr>

	<tr id="trData" style="display: none">
		<td class="colum">
			<div id="QueryData" style="width: 100%; z-index: 1; top: 100px">
				���ڲ�ѯ�����Ե�....
			</div>
		</td>
	</tr>
	<tr id="dataList" style="display: none">
		<td class="colum">
			<table class="listtable">
				<caption>
					�û����ն�����б�
				</caption>
				<thead>
					<tr>
						<th>LOID</th>
						<th>����˺�</th>
						<th>����</th>
						<th>����</th>
						<th>�ͺ�</th>
						<th>ҵ��ͨʱ��</th>
						<th>��ʱ��</th>
						<th>�������ʱ��</th>
					</tr>
				</thead>
				<tbody>
					<s:if test="data.size()>0">
						<s:iterator value="data">
							<tr>
								<td><s:property value="loid" /></td>
								<td><s:property value="broadband_account" /></td>
								<td><s:property value="city_name" /></td>
								<td><s:property value="vendor_name" /></td>
								<td><s:property value="device_model" /></td>
								<td><s:property value="opendate" /></td>
								<td><s:property value="binddate" /></td>
								<td><s:property value="last_time" /></td>
							</tr>
						</s:iterator>
					</s:if>
					<s:else>
						<tr>
							<td colspan=9>
								ϵͳû����ص��û���Ϣ!
							</td>
						</tr>
					</s:else>
				</tbody>
				<tfoot>
					<tr>
						<td colspan="9">
							<span style="float: right;"> 
								<lk:pages url="/itms/report/userOpenBindACT!userOpenBindListForPage.action" 
									styleClass="" showType="" isGoTo="true" changeNum="true" /> 
							</span>
							<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='�����б�' style='cursor: hand'
								onclick="toExcel('<s:property value="cityId"/>', '<s:property value="starttime"/>',
								'<s:property value="endtime"/>')">
						</td>
					</tr>
				</tfoot>
			
				<tr STYLE="display: none">
					<td colspan="9">
						<iframe id="childFrm" src=""></iframe>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td height="20">
		</td>
	</tr>
</TABLE>

<%@ include file="/foot.jsp"%>
<script language="JavaScript">
	// ����ǵ��ҳ����ת�����Ļ�������
	var starttime1 = '<s:property value="starttime1"/>';
	if(starttime1){
		$("#dataList").show();
	}
</script>
