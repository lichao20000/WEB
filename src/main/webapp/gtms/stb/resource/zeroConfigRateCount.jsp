<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
	<head>
	    <link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
		<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">		
		<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
		<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
		<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/CheckForm.js"/>"></SCRIPT>
		
		<lk:res />
		<script language="JavaScript">
			function doQuery(){
			    var start = $.trim($("input[@name='starttime']").val());
			    var end = $.trim($("input[@name='endtime']").val());			    
			    if(validateOneMonth(start,end)){
			    	alert("ʱ���Ȳ��ܳ���һ����");
			    	return false;
			    }
			    			    
			    $("tr[@id='trData']").show();
			    $("#btn").attr("diabled",true);
			    var url = "<s:url value='/gtms/stb/resource/zeroConfigRateCount!countAll.action'/>"; 
				$.post(url,{
					start : start,
					end : end
				},function(ajax){
					 $("#btn").attr("diabled",false);
				    $("div[@id='QueryData']").html("");
					$("div[@id='QueryData']").append(ajax);
				});
			}

			function ToExcel(start,end) {
				 
			    if(validateOneMonth(start,end)){
			    	alert("ʱ���Ȳ��ܳ���һ����");
			    	return false;
			    }					
				var page="<s:url value='/gtms/stb/resource/zeroConfigRateCount!getZeroConfigCountExcel.action'/>?"
					+ "start=" + start
					+ "&end=" + end;
				document.all("childFrm").src=page;
			}	
		</script>	
	</head>
	<body>	
	<TABLE class="querytable" style="width: 98%!important;" align="center">
		<TR>
			<TD >
				<IMG height=20 src="<s:url value='/images/bite_2.gif'/>" width=24>
	            	����ǰ��λ�ã����������ÿ�ͨͳ��
			</TD>
		</TR>
	</TABLE>
	<TABLE style="width: 98%!important;" align="center" cellpadding="0" cellspacing="0">
		<tr>
		<td>
			<form name=frm>
			<table class="querytable">
				<tr> <th colspan=4> ���������ÿ�ͨͳ��</th> </tr>
				<tr bgcolor=#ffffff>
						<td class=column align="center" width="15%">
							��ʼʱ��
						</td>
						<td width="35%">
							<input type="text" name="starttime" class='bk' readonly value="<s:property value='start'/>">
							<img name="shortDateimg"
								onClick="WdatePicker({el:document.frm.starttime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../../images/dateButton.png" width="15" height="12"
								border="0" alt="ѡ��" />
						</td>
						<td class=column align="center" width="15%">
							����ʱ��
						</td>
						<td width="35%">
							<input type="text" name="endtime" class='bk' readonly value="<s:property value='end'/>">
							<img name="shortDateimg"
								onClick="WdatePicker({el:document.frm.endtime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../../images/dateButton.png" width="15" height="12"
								border="0" alt="ѡ��" />
						</td>
					</tr>
				<tr bgcolor=#ffffff>
					<td class=foot colspan=4 align=right>
						<button id="btn" onclick="doQuery();">&nbsp;ͳ ��&nbsp;</button>
					</td>
				</tr>
			</table>
			</form>
		</td>
		</tr>
	
		<tr id="trData" style="display: none">
			<td class="colum">
				<div id="QueryData" style="width: 100%; z-index: 1; top: 100px">
					����Ŭ��Ϊ��ͳ�ƣ����Ե�....
				</div>
			</td>
		</tr>
		<tr>
			<td height="20"> </td>
		</tr>
	</TABLE>
	</body>
	<%@ include file="/foot.jsp"%>
</html>