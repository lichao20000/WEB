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
	$(function(){
		
	});	
	
	function ToExcel() {
		
		var page="../../gwms/config/setMulticastBatch!getInfoExcelDevice.action?taskId=<s:property value='taskId' />";
		window.open(page);
	}
	
</SCRIPT>

	</head>
	<body>
		<TABLE width="98%" align="center" class="querytable">
			<TR>
				<TD>
					<IMG height=20 src="<s:url value='/images/bite_2.gif'/>" width=24>
					����ǰ��λ�ã������޸��鲥��������������
				</TD>
			</TR>
		</TABLE>
		<br>
		<s:form action="/gwms/config/setMulticastBatch!getShowSetMulticastBatchTaskResult.action?taskId=<s:property value='taskId' />&num_splitPage=10&paramList_splitPage=" method="post" enctype="multipart/form-data" name="batchexform">
		<table width="98%" class="listtable" align="center">
		<thead>
				<tr>
					<th align="center" width="26%">
						�豸���к�
					</th>
					<th align="center" width="16%">
						����
					</th>
					<th align="center" width="20%">
						����ʱ��
					</th>
					<th align="center" width="20%">
						ִ��ʱ��
					</th>
					<th align="center" width="18%">
						�·����
					</th>
				</tr>
			</thead>
			
            <s:if test="taskResultList.size()>0">
                <tbody>
                    <s:iterator value="taskResultList">
                      	<tr>
                          	<td align="center"><s:property value="device_serialnumber" /></td>
                            <td align="center"><s:property value="city_id" /></td>
                            <td align="center"><s:property value="add_time" /></td>
                            <td align="center"><s:property value="update_time" /></td>
                            <td align="center"><s:property value="res" />
                            </td>
                            </tr>
                    </s:iterator>
                </tbody>
            </s:if>
		<s:else>
			<tbody>
				<tr>
					<td colspan="3">û�в�ѯ�����</td>
				</tr>
            </tbody>
		</s:else>
		<tfoot>
		
		<tr bgcolor="#FFFFFF">
			
			<td colspan="1" align="left"><INPUT TYPE="button" value=" �� �� " class=jianbian onclick="ToExcel()"></td>
				
        	<td colspan="5" align="right"><span  class=''>[�� <s:property value='curPage_splitPage' /> ҳ] [��<s:property value='maxPage_splitPage' />  ҳ ]&nbsp;
        	
        	<s:if test="curPage_splitPage>1">
        	<a href='#' onclick="gotoPageIndex('/itms/gwms/config/setMulticastBatch!getShowSetMulticastBatchTaskResult.action?paramList_splitPage=',1, <s:property value='curPage_splitPage-1' />)">��һҳ</a>
        	</s:if>
        	<s:if test="curPage_splitPage<maxPage_splitPage">
        	<a href='#' onclick="gotoPageIndex('/itms/gwms/config/setMulticastBatch!getShowSetMulticastBatchTaskResult.action?paramList_splitPage=',1, <s:property value='curPage_splitPage+1' />)">��һҳ</a>
        	</s:if>
        	
        	<input type='text' name='pageIndex_splitPage' value='1' style='width:30px'>
        	<a href='#' onclick="gotoPageIndex('/itms/gwms/config/setMulticastBatch!getShowSetMulticastBatchTaskResult.action?paramList_splitPage=',1,document.all.pageIndex_splitPage.value)">��ת</a></span><script type="text/javascript">
//��ת��ҳ����
function gotoPageIndex(url,maxPage,pageIndex){
	maxPage = <s:property value='maxPage_splitPage' />;
	var reg=/^[1-9]\d*$/; // add by liuht3 2014.8.29
	if (pageIndex == ''){
		alert("������ָ����ҳ�룡");
		return false;
	}
	if (!reg.test(pageIndex)){
		alert("������һ��������");
		return false;
	}
	if ((pageIndex < 1) || (parseInt(pageIndex,10) > parseInt(maxPage,10))){
		alert("�������ҳ�볬����Χ�����������룡");
		return false;
	}
	url = url + "&curPage_splitPage=" + pageIndex + "&maxPage_splitPage=" + maxPage + "&num_splitPage=10&taskId=<s:property value='taskId' />";
	document.all("hiddenClick").href = url;
	document.all("hiddenClick").click();
}

function changeNumSplit(url,maxPage){
	url = url + "&num_splitPage=" + document.all.num_splitPage_select.value + "&num_splitPage=10&maxPage_splitPage=" + maxPage+ "&taskId=<s:property value='taskId' />";
	document.all("hiddenClick").href = url;
	document.all("hiddenClick").click();
}
</script>
<a id="hiddenClick" href="" style="display:none">hidden</a></td>
                                </tr>
                        </tfoot>
                

		</table>
		<div id="divDetail"
			style="position: absolute; z-index: 255; top: 200px; left: 350px; width: 40%; border-width: 1; border-style: ridge; background-color: #eeeeee; padding-top: 10px; display: none"></div>
</s:form>
</body>
