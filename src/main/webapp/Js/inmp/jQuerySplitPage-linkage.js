//���ߣ� ������5243  jQuery�ķ�ҳ����
jQuery.extend({
/**
*���ߣ�������
*��ʼ����ѯ�б��״̬������ʾ��ҳ��ť�Լ���Ҫ��һЩ��Ϣ
*@param actionUrl  ��ѯ���ݵ�action
*@param target  ��ʾhtml���ݵ�λ�ã�Ĭ��Ϊһ��div������Ϊ���Ա�ʾjquery������ַ��������磺"#toolbar"
*@param curPage  ��ǰ��ҳ��
*@param num ÿҳ�ļ�¼��
*@param maxPage  ��ѯ���ݵ����ҳ��
*@param paramList ��ѯ�����ַ���
*return 
*/
initPage:function(actionUrl,target,curPage,num,maxPage,paramList){
	
	//ҳ����ת�ķ���
	var first = "$.firstPage(\"" + actionUrl + "\")";
	var last = "$.lastPage(\"" + actionUrl + "\")";
	var next = "$.nextPage(\"" + actionUrl + "\")";
	var pre = "$.prePage(\"" + actionUrl + "\")";
	var gotos = "$.gotoPage(\"" + actionUrl + "\")";
	
	var obj = $(target);
	
	var html = "";
	
	//���form
	html += "<form name='splitToolbarFrom' action='' method='post'>";
	
	html += "&nbsp;<input type='hidden' name='curPage_splitPage' value='" + curPage + "'>&nbsp;";
	html += "&nbsp;<input type='hidden' name='num_splitPage' value='" + num + "'>&nbsp;";
	html += "&nbsp;<input type='hidden' name='maxPage_splitPage' value='" + maxPage + "'>&nbsp;";
	html += "&nbsp;<input type='hidden' name='paramList_splitPage' value='" + paramList + "'>&nbsp;";
	
	//��ʾҳ����Ϣ
	html += "��ǰΪ��" + curPage + "ҳ���ܹ�" + maxPage + "ҳ  ";
	
	//��ָ����ҳ�������ת
	html += "&nbsp;<input type='text' name='pageIndex_splitPage' value='" + curPage + "' style='width:50px'><a href='#' onclick='" + gotos + "'>&nbsp;��ת&nbsp;</a>&nbsp;";
		
	//����Ҫ��һҳ����ʾ����ҳ��
	if (maxPage > 0){
		html += "&nbsp;<a href='#' onclick='" + first + "'>��ҳ</a>&nbsp;";
	}
	else{
		html += "&nbsp;<a disabled>��ҳ</a>&nbsp;";
	}
	
	//��һҳ����ʾ����һҳ����ť
	if (curPage != 1){
		html += "&nbsp;<a href='#' onclick='" + pre + "'>��һҳ</a>&nbsp;";
	}
	else{
		html += "&nbsp;<a disabled>��һҳ</a>&nbsp;";
	}
	
	//���һҳ����ʾ����һҳ����ť
	if (curPage != maxPage){
		html += "&nbsp;<a href='#' onclick='" + next + "'>��һҳ</a>&nbsp;";
	}
	else{
		html += "&nbsp;<a disabled>��һҳ</a>&nbsp;";
	}
	
	//����Ҫ��һҳ����ʾ��βҳ��
	if (maxPage > 0){
		html += "&nbsp;<a href='#' onclick='" + last + "'>βҳ</a>&nbsp;";
	}
	else{
		html += "&nbsp;<a disabled>βҳ</a>&nbsp;";
	}
	
	html += "</form>";
	obj.append(html);
	
},
/**
*���ߣ�������
*��ת����һҳ��һ�㲻�����ֹ����ã�
*@param url  ��ѯ���ݵ�action
*@param curPage  ��ǰ��ʵ��ҳ��
*@param maxPage  ��ѯ���ݵ����ҳ��
*@param frm  ��ʾhtml���ݵ����ڵ�from������Ϊ���Ա�ʾjquery������ַ��������磺"form[@name=frm1]"
*return 
*/
nextPage:function(url){
	
	//����ǰҳ���������ֵ��ȡ���ֵ
	var curPage = $("input[@name='curPage_splitPage']").val();
	var maxPage = $("input[@name='maxPage_splitPage']").val();
	
	curPage = parseInt(curPage) + 1;
	if (curPage > maxPage){
		curPage = maxPage;
	}
	
	//�ò�������url
	url = url + "?curPage_splitPage=" + curPage;
	document.splitToolbarFrom.action = url;
	
	document.splitToolbarFrom.submit();
},
/**
*���ߣ�������
*��ת����һҳ��һ�㲻�����ֹ����ã�
*@param url  ��ѯ���ݵ�action
*@param curPage  ��ǰ��ʵ��ҳ��
*@param maxPage  ��ѯ���ݵ����ҳ��
*@param frm  ��ʾhtml���ݵ����ڵ�from������Ϊ���Ա�ʾjquery������ַ��������磺"form[@name=frm1]"
*return 
*/
prePage:function(url){
	
	//����ǰҳ��С��1��ȡ1
	var curPage = $("input[@name='curPage_splitPage']").val();
	var maxPage = $("input[@name='maxPage_splitPage']").val();
	
	curPage = curPage - 1;
	if (curPage < 1){
		curPage = 1;
	}
	
	//�ò�������url
	url = url + "?curPage_splitPage=" + curPage;
	document.splitToolbarFrom.action = url;
	
	document.splitToolbarFrom.submit();
},
/**
*���ߣ�������
*��ת����ҳ��һ�㲻�����ֹ����ã�
*@param url  ��ѯ���ݵ�action
*@param curPage  ��ǰ��ʵ��ҳ��
*@param maxPage  ��ѯ���ݵ����ҳ��
*@param frm  ��ʾhtml���ݵ����ڵ�from������Ϊ���Ա�ʾjquery������ַ��������磺"form[@name=frm1]"
*return 
*/
firstPage:function(url){

	var maxPage = $("input[@name='maxPage_splitPage']").val();
	
	//�ò�������url
	url = url + "?curPage_splitPage=1";
	document.splitToolbarFrom.action = url;
	
	document.splitToolbarFrom.submit();
},
/**
*���ߣ�������
*��ת��βҳ��һ�㲻�����ֹ����ã�
*@param url  ��ѯ���ݵ�action
*@param curPage  ��ǰ��ʵ��ҳ��
*@param maxPage  ��ѯ���ݵ����ҳ��
*@param frm  ��ʾhtml���ݵ����ڵ�from������Ϊ���Ա�ʾjquery������ַ��������磺"form[@name=frm1]"
*return 
*/
lastPage:function(url){

	var maxPage = $("input[@name='maxPage_splitPage']").val();
	
	//�ò�������url
	url = url + "?curPage_splitPage=" + maxPage;
	document.splitToolbarFrom.action = url;
	
	document.splitToolbarFrom.submit();
},
/**
*���ߣ�������
*��ת��ָ��ҳ����һ�㲻�����ֹ����ã�
*@param url  ��ѯ���ݵ�action
*@param maxPage  ��ѯ���ݵ����ҳ��
*@param frm  ��ʾhtml���ݵ����ڵ�from������Ϊ���Ա�ʾjquery������ַ��������磺"form[@name=frm1]"
*return 
*/
gotoPage:function(url){
	
	var maxPage = $("input[@name='maxPage_splitPage']").val();
	var pageIndex = $("input[@name='pageIndex_splitPage']").val();
	
	var reg=/^\d+(\.\d+)?$/;
	
	if (pageIndex == ''){
		alert("������ָ����ҳ�룡");
		return false;
	}
	if (!reg.test(pageIndex)){
		alert("������һ��������");
		return false;
	}
	if ((pageIndex < 1) || (pageIndex > maxPage)){
		alert("�������ҳ�볬����Χ�����������룡");
		return false;
	}
	
	//�ò�������url
	url = url + "?curPage_splitPage=" + pageIndex;
	document.splitToolbarFrom.action = url;
	
	document.splitToolbarFrom.submit();
},
/**
*���ߣ�������
*����paramList�������ɲ����ַ���
*@param list  ҳ�洫�ݵĲ���paramList
*return action�������Ҫ�ύ�Ĳ����������磺��&user=1��
*/
splitParam:function(list){
	
	//�ָ��
	var code1 = String.fromCharCode(1);
	var code2 = String.fromCharCode(2);
	
	//��ʱ�ַ���
	var tmp = "";
	
	//��������������ַ���
	if (list != null && list != ''){
		var arr1 = list.split(code1);
		var len1 = arr1.length;
		
		if (arr1 != null && len1 > 0){
			
			for (var i=0;i<len1;i++){
				
				if (arr1[i] != null && arr1[i] != ''){
					var arr2 = arr1[i].split(code2);
					var len2 = arr2.length;
					
					if (arr2 != null && len2 > 1){
						tmp += "&" + arr2[0] + "=" + arr2[1];
					}
				}
			}
		}
		
	}
	
	return tmp;
}
});