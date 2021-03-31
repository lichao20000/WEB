<%@ include file="../timelater.jsp"%>
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
		<lk:res />
		<SCRIPT LANGUAGE="JavaScript">
	
	function edit(ipId,cityName,startIp,endIp)
	{
		var page = "<s:url value='/hgwipMgSys/itvipMgSysEdit.jsp'/>?ipId="+ipId + "&city_name=" + cityName + "&start_ip=" + startIp + "&end_ip=" + endIp;
		window.open(page,"","left=20,top=20,width=800,height=600,resizable=no,scrollbars=yes");
	}
	
	function add(ipId,cityName,startIp,endIp)
	{
		var page = "<s:url value='/hgwipMgSys/ItvIpMg!addInit.action'/>";
		window.open(page,"","left=20,top=20,width=800,height=600,resizable=no,scrollbars=yes");
	}
	
	function deleteIp(ipId){
		if(confirm("ɾ���ǲ��ɻָ��ģ�ȷ��Ҫɾ����") == true){     //����û�������ȷ����ť 
			var url = "<s:url value='/hgwipMgSys/ItvIpMg!delete.action'/>";
			$.post(url,{
				ipId : ipId
			},function(ajax){
				 alert(ajax);
				 query();
			});   
		}
	}
	
    function CloseDetail(){
		$("div[@id='divDetail']").hide();
	}
    
   	function query()
	{
		var startTime = $("#start_time").val();
		var endTime = $("#end_time").val();
		var url = "<s:url value='/hgwipMgSys/ItvIpMg.action'/>?start_time="+startTime+"&end_time="+endTime;
	    window.location.href=url;
	}
   	
	function initQuery()
	{
		var startT = '<s:property value="startTime"/>';
		var endT = '<s:property value="endTime"/>';
		if('' != startT)
		{
			$("#start_time").val(startT);
			$("#end_time").val(endT);
		}
	}
</SCRIPT>

	</head>
	<body>
		<TABLE width="98%" align="center" class="querytable">
			<TR>
				<TD>
					<IMG height=20 src="<s:url value='/images/bite_2.gif'/>" width=24>
					����ǰ��λ�ã�IP��ַ����
				</TD>
			</TR>
		</TABLE>
		<br>
		<s:form action="hgwipMgSys/ItvIpMg.action" method="post" enctype="multipart/form-data" name="batchexform">
			<table width="98%" class="querytable" align="center">
			<tr>
			<td class="title_1" width="15%">
						��ʼʱ��
				</td>
				<td width="35%" >
					<span id="startTime"><lk:date id="start_time" name="startTime" type="day" defaultDate="" maxDateOffset="0" dateOffset="-15" /></span>
				</td>
				<td class="title_1" width="15%">
					����ʱ��
				</td>
				<td width="35%">
					<span id="endTime"><lk:date id="end_time" name="endTime" dateOffset="0" defaultDate="" type="day" maxDateOffset="0" /></span>
				</td>
			</tr>
			<tr>
					<td colspan="4" class="foot" align="right">
						<div class="right">
							<button onclick="add()">
								���
							</button>
							<button onclick="query()">
								��ѯ
							</button>
						</div>
					</td>
				</tr>
		</table>
		<table width="98%" class="listtable" align="center">
			<thead>
				<tr>
					<th align="center" width="20%">
						����
					</th>
					<th align="center" width="20%">
						��ʼIP
					</th>
					<th align="center" width="20%">
						��ֹIP
					</th>
					<th align="center" width="20%">
						������
					</th>
					<th align="center" width="20%">
						����ʱ��
					</th>
					<th align="center" width="20%">
						����
					</th>
				</tr>
			</thead>
			<s:if test="ipList!=null">
                <s:if test="ipList.size()>0">
                        <tbody>
                                <s:iterator value="ipList">
                                        <tr>
                                            <td align="center"><s:property value="city_name" /></td>
                                            <td align="center"><s:property value="start_ip" /></td>
                                            <td align="center"><s:property value="end_ip" /></td>
                                            <td align="center"><s:property value="acc_loginname" /></td>
                                            <td align="center"><s:property value="updatetime" /></td>
                                            <td>&nbsp;&nbsp;
                                              <button name="viewButton" 
                                                      onclick="javascript:edit('<s:property value="ip_id"/>','<s:property value="city_name"/>','<s:property value="start_ip"/>','<s:property value="end_ip"/>')">
                                              		�༭
                                              	</button>&nbsp;&nbsp;
                                          	   <button  onclick="javascript:deleteIp('<s:property value="ip_id"/>')">
                                          	   ɾ��</button>
                                             </td>
                                        </tr>
                                </s:iterator>
                        </tbody>
                        <tfoot>
                                <tr bgcolor="#FFFFFF">
                                        <td colspan="6" align="right"><lk:pages
                                                url="/gtms/stb/resource/openDeviceShowPic!initImport.action" styleClass=""
                                                showType="" isGoTo="true" changeNum="false" /></td>
                                </tr>
                        </tfoot>
                </s:if>
			<s:else>
				<tbody>
					<tr>
						<td colspan="6">
							<font color="red">��û�й���ip</font>
						</td>
					</tr>
				</tbody>
			</s:else>

</s:if>
		</table>
		<div id="divDetail"
			style="position: absolute; z-index: 255; top: 200px; left: 350px; width: 40%; border-width: 1; border-style: ridge; background-color: #eeeeee; padding-top: 10px; display: none"></div>
</s:form>
</body>
