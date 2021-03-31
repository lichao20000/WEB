<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<link href="../../css/inmp/css/css_green.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="<s:url value="../../css/inmp/css3/c_table.css"/>" 
	type="text/css"> 
 <link rel="stylesheet" href="<s:url value="../../css/inmp/css3/global.css"/>" 
 	type="text/css"> 
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="../../Js/inmp/jquery.js"/>"></SCRIPT> 
 <script type="text/javascript" 
			src="../../Js/My97DatePicker/WdatePicker.js"></script> 
<script language="JavaScript">


function doQuery(){
	var cityId = $.trim($("select[@name='cityId']").val());
    var starttime = $.trim($("input[@name='starttime']").val());
    var endtime = $.trim($("input[@name='endtime']").val());
    var usertype = $.trim($("select[@name='usertype']").val());
    var is_active= "<s:property value ='is_active'/>";
    var is_act="";
    if(is_active=="true"){
    	is_act=$.trim($("select[@name='is_act']").val());
    }
    if(cityId == "-1"){
         alert("��ѡ������");
         return false;
    }
    $("tr[@id='trData']").show();
    $("div[@id='QueryData']").html("����ͳ�ƣ����Ե�....");
    $("button[@name='button']").attr("disabled", true);
    var url = '<s:url value='/inmp/report/bindWay!countAll.action'/>'; 
	$.post(url,{
		cityId:cityId,
		usertype:usertype,
		starttime:starttime,
		endtime:endtime,
		is_active:is_act
	},function(ajax){	
	    $("div[@id='QueryData']").html("");
		$("div[@id='QueryData']").append(ajax);
		 $("button[@name='button']").attr("disabled", false);
	});
	
}

function countBycityId(cityId,starttime,endtime,usertype,is_active){

    $("tr[@id='trData']").show();
    $("div[@id='QueryData']").html("����ͳ�ƣ����Ե�....");
    var url = '<s:url value='/inmp/report/bindWay!countAll.action'/>'; 
	$.post(url,{
		cityId:cityId,
		usertype:usertype,
		starttime:starttime,
		endtime:endtime,
		is_active:is_active
	},function(ajax){	
	    $("div[@id='QueryData']").html("");
		$("div[@id='QueryData']").append(ajax);
	});
}

function ToExcel(cityId,starttime1,endtime1,usertype,is_active) {
	var page="<s:url value='/inmp/report/bindWay!getAllBindWayExcel.action'/>?"
		+ "&starttime1=" + starttime1
		+ "&endtime1=" + endtime1
		+ "&usertype=" + usertype
		+ "&cityId=" + cityId
		+ "&is_active=" + is_active;
	document.all("childFrm").src=page;
}

function openHgw(cityId,starttime1,endtime1,userTypeId,userline,isChkBind,usertype,is_active){
	if(usertype=="1" && ""==userTypeId){ 
		userTypeId = 2 ;  
	}
	var page="<s:url value='/inmp/report/bindWay!getHgw2.action'/>?"
		+ "cityId=" + cityId 
		+ "&usertype=" +usertype
		+ "&starttime1=" +starttime1
		+ "&endtime1=" +endtime1
		+ "&userTypeId=" +userTypeId
		+ "&userline=" +userline
		+ "&isChkBind=" +isChkBind
		+ "&is_active=" +is_active;
	window.open(page,"","left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
}

</script>

<br>
<TABLE>
	<tr>
		<td>
			<table width="100%" height="30" border="0" cellspacing="0"
							cellpadding="0" class="green_gargtd">
				<tr>
					<th width="162" align="center" class="title_bigwhite" nowrap>
						<s:if test="is_active=='true'">
						��Ծ�û��󶨷�ʽͳ��
						</s:if>
						<s:else>
							�û��󶨷�ʽͳ��
							</s:else>
					</th>
					<td nowrap>
						<img src="<s:url value="../../images/inmp/attention_2.gif"/>" width="15"
							height="12">
							��ѯʱ�䣺e8b�û�����Ծ�û�ͳ�ƣ�e8c�û�������ʱ��ͳ�ƣ����û��Ƕ�ͳ�ơ��Զ����� = (����SN + �Ž��˺� + �߼�SN + ·���˺�) / �ܿ�����
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
						<s:if test="is_active=='true'">
						��Ծ�û��󶨷�ʽͳ��
						</s:if>
						<s:else>
							�û��󶨷�ʽͳ��
							</s:else>
						</th>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=column align=center width="15%">
							��ʼʱ��
						</td>
						<td>
							<input type="text" name="starttime" class='bk' readonly
								value="<s:property value='starttime'/>">
							<img name="shortDateimg"
								onClick="WdatePicker({el:document.frm.starttime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../images/inmp/dateButton.png" width="15" height="12"
								border="0" alt="ѡ��" />
						</td>
						<td class=column align=center width="15%">
							����ʱ��
						</td>
						<td>
							<input type="text" name="endtime" class='bk' readonly
								value="<s:property value='endtime'/>">
							<img name="shortDateimg"
								onClick="WdatePicker({el:document.frm.endtime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../images/inmp/dateButton.png" width="15" height="12"
								border="0" alt="ѡ��" />
						</td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=column align=center width="15%">
							�� ��
						</td>
						<td>
							<s:select list="cityList" name="cityId" headerKey="-1"
								headerValue="��ѡ������" listKey="city_id" listValue="city_name"
								value="cityId" cssClass="bk"></s:select>
						</td>
						<td class=column width="15%" align='right'>
								�û�����
						</td>
						<td width="35%">
							<s:if test="telecom == 'CUC'">
								<SELECT name="usertype">
									<option value="1">HGU</option>
									<!-- <option value="0">ȫ��</option> -->
								</SELECT>
							</s:if>
							<s:else>
								<SELECT name="usertype">
									<option selected value="2">E8-C</option>
									<option value="1">E8-B</option>
									<!-- <option value="0">ȫ��</option> -->
								</SELECT>
							</s:else>
						</td>
					</tr>
					<s:if test="is_active=='true'">
					<tr bgcolor=#ffffff>
						<td class=column width="15%" align='right'>
								�Ƿ��Ծ
						</td>
						<td width="35%">
							
								<SELECT name="is_act">
									<option value="1">��</option>
									<option value="0">��</option>
								</SELECT>
							
						</td>
					</tr>
					</s:if>
					<tr bgcolor=#ffffff>
						
						<td align="right" colspan="4"  class="green_foot" width="100%">
							<input type="button" class=jianbian onclick="doQuery()" name="button" value="ͳ ��" style="margin-left: 1000" />
								
						</td>
					</tr>
				</table>
			</form>
		</td>
	</tr>

	<tr id="trData" style="display: none">
		<td class="colum">
			<div id="QueryData" style="width: 100%; z-index: 1; top: 100px">
				����ͳ�ƣ����Ե�....
			</div>
		</td>
	</tr>
	<tr>
		<td height="20">
		</td>
	</tr>
</TABLE>

<%@ include file="../foot.jsp"%>
