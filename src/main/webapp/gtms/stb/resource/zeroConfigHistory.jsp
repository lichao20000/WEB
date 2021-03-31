<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
	<head>
	    <link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
		<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">		
		<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
		<script type="text/javascript" src="<s:url value="/Js/date/WdatePicker.js"/>"></script>
		<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/CheckForm.js"/>"></SCRIPT>
		
		<lk:res />
		<script language="JavaScript">
			function doQuery(){
				var starttime = $.trim($("input[@name='starttime']").val());
			    var endtime = $.trim($("input[@name='endtime']").val());
			    if(validateOneMonth(starttime,endtime)){
			    	alert("ʱ���Ȳ��ܳ���һ����");
			    	return false;
			    }
			    
			    var servAccount = $.trim($("input[@name='servAccount']").val());
			    var deviceSerialnumber = $.trim($("input[@name='deviceSerialnumber']").val());
			    if(servAccount == "" && deviceSerialnumber == ""){
			    	alert("ҵ���˺ź��豸���к�������һ���Ϊ�գ�");
			    	return false;
			    }
			    
				$("#resultFrame").hide("");
				$("#tip_loading").show();
				var url = "<s:url value='/gtms/stb/resource/zeroConfigHistory!doZeroHistoryQuery.action'/>";
				$("#queryForm").attr("action", url);
				$("#queryForm").attr("target", "resultFrame");
				$("#queryForm").submit();
			}

			function doExcel() {
			    var starttime = $.trim($("input[@name='starttime']").val());
			    var endtime = $.trim($("input[@name='endtime']").val());		    
			    if(validateOneMonth(starttime,endtime)){
			    	alert("ʱ���Ȳ��ܳ���һ����");
			    	return false;
			    }
			    
			    var servAccount = $.trim($("input[@name='servAccount']").val());
			    var deviceSerialnumber = $.trim($("input[@name='deviceSerialnumber']").val());
			    if(servAccount == "" && deviceSerialnumber == ""){
			    	alert("ҵ���˺ź��豸���к�������һ���Ϊ�գ�");
			    	return false;
			    }
			    
				var page="<s:url value='/gtms/stb/resource/zeroConfigHistory!doZeroHistoryExcel.action'/>?"
					+ "starttime=" + starttime + "&endtime=" + endtime
					+ "&servAccount=" + servAccount + "&deviceSerialnumber=" + deviceSerialnumber;
				document.all("childFrm").src=page;
			}	
			
			function setDataSize(dataHeight) {
				$("#resultFrame").height(dataHeight);
			}

			function showIframe() {
				$("#tip_loading").hide();
				$("#resultFrame").show();
			}
		</script>	
	</head>
	<body>	
	<TABLE class="querytable" style="width: 98%!important;" align="center">
		<TR>
			<TD >
				<IMG height=20 src="<s:url value='/images/bite_2.gif'/>" width=24>
	            	����ǰ��λ�ã������û�������ʷ���ò�ѯ
			</TD>
		</TR>
	</TABLE>
	<TABLE style="width: 98%!important;" align="center" cellpadding="0" cellspacing="0">
		<tr>
		<td>
			<form name="queryForm" id="queryForm">
			<table class="querytable">
				<tr> <th colspan=4>�����û�������ʷ���ò�ѯ</th> </tr>
				<tr bgcolor=#ffffff>
					<td class=column align="center" width="15%">
						ҵ���˺�
					</td>
					<td width="35%">
						<input type="text" id="servAccount" name="servAccount" class='bk' value="<s:property value='servAccount'/>"/>&nbsp;<font color="red">*</font>			
					</td>
					<td class=column align="center" width="15%">
						�豸���к�
					</td>
					<td width="35%">
						<input type="text" id="deviceSerialnumber" name="deviceSerialnumber" class='bk' value="<s:property value='deviceSerialnumber'/>"/>&nbsp;<font color="red">*</font>
					</td>
				</tr>
				<tr bgcolor=#ffffff>
					<td class=column align="center" width="15%">
						��ʼʱ��
					</td>
					<td width="35%">
						<lk:date id="starttime" name="starttime"
							defaultDate="%{starttime}" />
					</td>
					<td class=column align="center" width="15%">
						����ʱ��
					</td>
					<td width="35%">
						<lk:date id="endtime" name="endtime"
							defaultDate="%{endtime}" />
					</td>
				</tr>
				<tr bgcolor=#ffffff>
					<td class=foot colspan=4 align=right>
						<button id="btn" onclick="doQuery();">&nbsp;��ѯ&nbsp;</button>
						<button id="btn" onclick="doExcel();">&nbsp;����&nbsp;</button>
					</td>
				</tr>
			</table>
			<iframe id="resultFrame" name="resultFrame" width="100%"
				frameborder="0" scrolling="no" align="center"></iframe>
		    <div style="width: 100%; display: none; text-align: center;"
				id="tip_loading">
				���ڼ�������,�����ĵȴ�......
			</div>
			</form>
		</td>
		</tr>
		<tr STYLE="display: none">
			<td colspan="12">
				<iframe id="childFrm" src=""></iframe>
			</td>
		</tr>		
	</TABLE>
	
	</body>
	<%@ include file="/foot.jsp"%>
</html>