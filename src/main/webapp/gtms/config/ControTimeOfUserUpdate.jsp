<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script language="JavaScript">
//��ʼ��ʱ��
function initDate()
{
	//��ʼ��ʱ��  
	var theday=new Date();
	var day=theday.getDate();
	var month=theday.getMonth()+1;
	var year=theday.getFullYear();
	var hour = theday.getHours();
	var mimute = theday.getMinutes();
	var second = theday.getSeconds();
	var flag ='<s:property value="conTime"/>';
    if(null!=flag &&""!=flag){
    	$("input[@name='conTime']").val('<s:property value="conTime"/>');
    }else{
	    $("input[@name='conTime']").val(year+"-" + month + "-" + day + " " + hour +":"+ mimute +":" + second);
    }
}

function doUpdate(){
	var cuId = $("#cuId").val();
	var typeId = $("select[@name='typeId']").val();
	var conTime = $("input[@name='conTime']").val();
	var url = "<s:url value='/gtms/config/timeSet!update.action'/>"; 
	$.post(url,{
		cuId :cuId,
		typeId : typeId,
		conTime: conTime
	},function(ajax){
		if("1"== ajax){
			alert("�޸ĳɹ�");
		}else{
			alert("�޸�ʧ��");
		}
	});
}
</script>
<br>
<TABLE>
	<tr>
		<td>
			<table class="green_gargtd" >
				<tr>
					<th>
						�޸Ŀ����û�����ʱ��
					</th>
					<td>
						<img src="<s:url value="/images/attention_2.gif"/>" width="15" height="12">
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<form name=frm>
				<intput type="hidden" name="cuId" id="cuId" value='<s:property value="cuId"/>'/>
				<table class="querytable">
					<tr>
						<th colspan=4>
							�޸Ŀ����û�����ʱ��
						</th>
						
					</tr>
					
					<TR bgcolor="#FFFFFF" id="tr_timeCont" STYLE="">
						<td class="column" align="right" width="15%">
							ʱ������
						</td>
						<td width="25%" >
							<SELECT name="typeId">
								<option value="-1">==��ѡ��==</option>
									<option value="1" selected="selected">�û�����ʱ��</option>
							</SELECT>
						</td>
						<td class=column align=center width="15%">
							ʱ��
						</td>
						<td width="45%">
							<input type="text" name="conTime" class='bk' readonly value="<s:property value='conTime'/>">
							<img name="shortDateimg"
								onClick="WdatePicker({el:document.frm.conTime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../images/dateButton.png" width="15" height="12"
								border="0" alt="ѡ��" />
						</td>
					</tr>
					<tr>
						<td height="60" colspan="4">&nbsp;</td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=foot colspan=4 align=right>
							<button onclick="doUpdate()" name="button">
								&nbsp;����&nbsp;
							</button>&nbsp;&nbsp;&nbsp;&nbsp;
							<button onclick="window.close();" name="button">
								&nbsp;�ر�&nbsp;
							</button>
						</td>
					</tr>
				</table>
			</form>
		</td>
	</tr>
</TABLE>


<SCRIPT LANGUAGE="JavaScript">
	initDate();
</SCRIPT>

