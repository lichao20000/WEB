<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../../head.jsp"%>
<%@ include file="../../toolbar.jsp"%>
<%
	String gw_type = request.getParameter("gw_type");
	if(null == gw_type ||  "".equals(gw_type)){
		gw_type="1";  
	}
 %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<title>������ͨ·��</title>
<script type="text/javascript" src="<s:url value='/Js/jquery.js'/>"></script>
<link rel="stylesheet" href="<s:url value='/css/css_green.css'/>"
	type="text/css">
<script type="text/javascript">
	function do_query(){
		var	width=800;    
		var height=450;
		var gwShare_fileName = $("input[@name='gwShare_fileName']").val();
		if("" == gwShare_fileName){
			alert("�����ϴ��ļ���");
			return;
		}
		var gwShare_queryResultType = $("input[@name='gwShare_queryResultType']").val();
		var url="<s:url value='/gtms/config/wirelessConfigAction!queryDeviceList.action'/>?"
			+ "fileName=" + gwShare_fileName
			+ "&gwShare_queryResultType=" + gwShare_queryResultType;
		
		url = url +"&refresh=" + new Date().getTime();
		url=url+"&gw_type="+<%=gw_type %>;
		var returnVal = window.showModalDialog(url ,'','dialogWidth='+width+'px;dialogHeight='+ height+'px;resizable=no;help=no;center=yes;status=no;scroll=yes;edge=raised');    
		if(typeof(returnVal)=='undefined'){
			return;
		}else{
			usnernameResult(returnVal);
		}
	}
	
	var username = "";
	var netNum = "";
	//��ѯ���豸
	function usnernameResult(returnVal){
		$("#doButton").attr("disabled",false);
		$("table[@id='tr_strategybs']").css("display","");
		$("#resultDIV").html("");
		var totalNum = returnVal[0];
		if(returnVal[0]==0){
			totalNum = returnVal[2].length;
			var usernameArray = returnVal[2];
			for(var i = 0;i < usernameArray.length;i++){
				//����������username
				username +=  usernameArray[i][0] + ",";
				netNum += $.trim(usernameArray[i][1]) + ",";
			}
			var endIndex = username.lastIndexOf(",");
			username = username.substring(0,endIndex);
			endIndex = netNum.lastIndexOf(",");
			netNum = netNum.substring(0,endIndex);
			if(totalNum > 2000){
				alert("�����������2000����Ӱ�쵽��������");
				$("#doButton1").attr("disabled",true);
				$("#doButton1").attr("disabled",true);
				return;
			}
			$("div[@id='selectedDev']").html("<font size=2>����" + totalNum + "������ʺ�</font>");
		}else{
			//������ѯ
			deviceId=  deviceId = 	returnVal[2][0][0] ;
			if(deviceId==""){
				$("#selectedDev").html("���û������ڻ�δ���նˣ�");
			}
		}
	}
	
	function doExecute(){
		url = "<s:url value='/gtms/config/wirelessConfigAction!doConfigAll.action'/>";
		$("div[@id='QueryData']").html("����ˢ�£����Ե�....");
		$("#doButton").attr("disabled",true);
		$.post(url,{
            usenmames : username,
            gw_type: <%=gw_type%>,
            netNum : netNum
         },function(ajax){
			$("div[@id='QueryData']").html("");
			username = "";
			netNum = "";
			var array = ajax.split("|||");
	   		if(array.length > 2){
	   			$("#resultDIV").html("���ؽ����" + array[2]);
	   		}else{
	   			$("#resultDIV").html("���ؽ�����������ͳɹ�");
	   		}
         });
	} 
</script>
<table border="0" cellspacing="0" cellpadding="0" width="98%" align="center">
	<tr>
		<td height="20">
			&nbsp;
		</td>
	</tr>
	<tr>
		<td>
			<table width="100%" height="30" border="0" cellspacing="0"
							cellpadding="0" class="green_gargtd">
				<tr>
					<td id="titleTd" width="162" align="center" class="title_bigwhite">
						������ͨ·��
					</td>
					<td>
						<img src="../../images/attention_2.gif" width="15" height="12">
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td colspan="4">
			<table border="1" cellspacing="0" cellpadding="0" width="100%" align="center" class="green_gargtd">
				<tr>
					<th colspan="4">���� �� ѯ</th>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td align="right" width="15%">�ύ�ļ�</td>
					<td colspan="3" width="85%">
						<div id="importUsername">
							<iframe name="gwShare_loadForm" FRAMEBORDER=0 SCROLLING=NO src="<s:url value="/gwms/share/FileUpload.jsp"/>" height="20" width="100%">
							</iframe>
							<input type="hidden" name="gwShare_fileName" value=""/>
						</div>
					</td>
				</tr>
				<tr>
					<td class="green_foot" align="right">ע������</td>
					<td colspan="3" CLASS="green_foot">
						1����Ҫ������ļ���ʽ����Excel���ı��ļ�����xls��txt��ʽ ��
						 <br>
						2���ļ��ĵ�һ��Ϊ�����У�������ʺš��ն�����������
						 <br>
						3���ļ�ֻ��һ�С�
						 <br>
						4��ÿ���ϴ��ļ��������ó���2000�С�
					</td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td colspan="4" align="right" class="green_foot" width="100%">
						<input type="button" onclick="do_query()" class=jianbian 
						name="gwShare_queryButton" value="�����ļ�" />
						<input type="button" class=jianbian onclick="revalue()" 
						name="reButton" value=" �� �� " />
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td height="20">
			&nbsp;
		</td>
	</tr>
	<tr>
		<th colspan="4" align="center" >
			������ͨ·��
		</th>
	</tr>
	<tr>
		<td>
			<form name="frm" method="post" action="" onsubmit="return CheckForm()">
				<input type="hidden" name="gwShare_queryResultType" value="checkbox" />
				<table width="100%" border="1" cellspacing="0" cellpadding="0" align="center">
					<tr bgcolor="#FFFFFF">
						<td colspan="6">
							<div id="selectedDev">
								���ѯ����ʺţ�
							</div>
						</td>
					</tr>
					<tr>
						<td>
							<table border="1" cellspacing="0" cellpadding="0" width="102%" align="center" class="title_bigwhite">
								<tr>
									<td height="20">
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr bgcolor="#FFFFFF" >
						<td colspan="2" align="right" class="green_foot" width="100%">
							<input type="button" id="doButton" name="doButton" onclick="doExecute()" value=" ִ�� " disabled="disabled" class=btn>
						</td>
					</tr>
					<tr bgcolor="#FFFFFF">
						<td colspan="6" align="left" class="green_foot">
							<div id="resultDIV"></div>
						</td>
					</tr>
				</table>
				<table>
					<tr>
						<td height="20">
						</td>
					</tr>
					<tr id="trData" style="display: none">
						<td class="colum">
							<div id="QueryData" style="width: 100%; z-index: 1; top: 100px">
								���ڿ�ͨ�����Ե�....
							</div>
						</td>
					</tr>
					<tr>
						<td height="20">
						</td>
					</tr>
				</table>
			</form>
		</td>
	</tr>
</table>
<%@ include file="../../foot.jsp"%>