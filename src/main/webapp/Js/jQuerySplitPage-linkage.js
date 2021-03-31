//作者： 陈仲民5243  jQuery的翻页功能
jQuery.extend({
/**
*作者：陈仲民
*初始化查询列表的状态栏，显示翻页按钮以及必要的一些信息
*@param actionUrl  查询数据的action
*@param target  显示html内容的位置，默认为一个div，内容为可以标示jquery对象的字符串，例如："#toolbar"
*@param curPage  当前的页码
*@param num 每页的纪录数
*@param maxPage  查询数据的最大页码
*@param paramList 查询参数字符串
*return 
*/
initPage:function(actionUrl,target,curPage,num,maxPage,paramList){
	
	//页面跳转的方法
	var first = "$.firstPage(\"" + actionUrl + "\")";
	var last = "$.lastPage(\"" + actionUrl + "\")";
	var next = "$.nextPage(\"" + actionUrl + "\")";
	var pre = "$.prePage(\"" + actionUrl + "\")";
	var gotos = "$.gotoPage(\"" + actionUrl + "\")";
	
	var obj = $(target);
	
	var html = "";
	
	//输出form
	html += "<form name='splitToolbarFrom' action='' method='post'>";
	
	html += "&nbsp;<input type='hidden' name='curPage_splitPage' value='" + curPage + "'>&nbsp;";
	html += "&nbsp;<input type='hidden' name='num_splitPage' value='" + num + "'>&nbsp;";
	html += "&nbsp;<input type='hidden' name='maxPage_splitPage' value='" + maxPage + "'>&nbsp;";
	html += "&nbsp;<input type='hidden' name='paramList_splitPage' value='" + paramList + "'>&nbsp;";
	
	//显示页面信息
	html += "当前为第" + curPage + "页，总共" + maxPage + "页  ";
	
	//按指定的页码进行跳转
	html += "&nbsp;<input type='text' name='pageIndex_splitPage' value='" + curPage + "' style='width:50px'><a href='#' onclick='" + gotos + "'>&nbsp;跳转&nbsp;</a>&nbsp;";
		
	//至少要有一页才显示“首页”
	if (maxPage > 0){
		html += "&nbsp;<a href='#' onclick='" + first + "'>首页</a>&nbsp;";
	}
	else{
		html += "&nbsp;<a disabled>首页</a>&nbsp;";
	}
	
	//第一页不显示“上一页”按钮
	if (curPage != 1){
		html += "&nbsp;<a href='#' onclick='" + pre + "'>上一页</a>&nbsp;";
	}
	else{
		html += "&nbsp;<a disabled>上一页</a>&nbsp;";
	}
	
	//最后一页不显示“下一页”按钮
	if (curPage != maxPage){
		html += "&nbsp;<a href='#' onclick='" + next + "'>下一页</a>&nbsp;";
	}
	else{
		html += "&nbsp;<a disabled>下一页</a>&nbsp;";
	}
	
	//至少要有一页才显示“尾页”
	if (maxPage > 0){
		html += "&nbsp;<a href='#' onclick='" + last + "'>尾页</a>&nbsp;";
	}
	else{
		html += "&nbsp;<a disabled>尾页</a>&nbsp;";
	}
	
	html += "</form>";
	obj.append(html);
	
},
/**
*作者：陈仲民
*跳转到下一页（一般不允许手工调用）
*@param url  查询数据的action
*@param curPage  当前现实的页码
*@param maxPage  查询数据的最大页码
*@param frm  显示html内容的所在的from，内容为可以标示jquery对象的字符串，例如："form[@name=frm1]"
*return 
*/
nextPage:function(url){
	
	//若当前页数大于最大值则取最大值
	var curPage = $("input[@name='curPage_splitPage']").val();
	var maxPage = $("input[@name='maxPage_splitPage']").val();
	
	curPage = parseInt(curPage) + 1;
	if (curPage > maxPage){
		curPage = maxPage;
	}
	
	//用参数生成url
	url = url + "?curPage_splitPage=" + curPage;
	document.splitToolbarFrom.action = url;
	
	document.splitToolbarFrom.submit();
},
/**
*作者：陈仲民
*跳转到上一页（一般不允许手工调用）
*@param url  查询数据的action
*@param curPage  当前现实的页码
*@param maxPage  查询数据的最大页码
*@param frm  显示html内容的所在的from，内容为可以标示jquery对象的字符串，例如："form[@name=frm1]"
*return 
*/
prePage:function(url){
	
	//若当前页数小于1则取1
	var curPage = $("input[@name='curPage_splitPage']").val();
	var maxPage = $("input[@name='maxPage_splitPage']").val();
	
	curPage = curPage - 1;
	if (curPage < 1){
		curPage = 1;
	}
	
	//用参数生成url
	url = url + "?curPage_splitPage=" + curPage;
	document.splitToolbarFrom.action = url;
	
	document.splitToolbarFrom.submit();
},
/**
*作者：陈仲民
*跳转到首页（一般不允许手工调用）
*@param url  查询数据的action
*@param curPage  当前现实的页码
*@param maxPage  查询数据的最大页码
*@param frm  显示html内容的所在的from，内容为可以标示jquery对象的字符串，例如："form[@name=frm1]"
*return 
*/
firstPage:function(url){

	var maxPage = $("input[@name='maxPage_splitPage']").val();
	
	//用参数生成url
	url = url + "?curPage_splitPage=1";
	document.splitToolbarFrom.action = url;
	
	document.splitToolbarFrom.submit();
},
/**
*作者：陈仲民
*跳转到尾页（一般不允许手工调用）
*@param url  查询数据的action
*@param curPage  当前现实的页码
*@param maxPage  查询数据的最大页码
*@param frm  显示html内容的所在的from，内容为可以标示jquery对象的字符串，例如："form[@name=frm1]"
*return 
*/
lastPage:function(url){

	var maxPage = $("input[@name='maxPage_splitPage']").val();
	
	//用参数生成url
	url = url + "?curPage_splitPage=" + maxPage;
	document.splitToolbarFrom.action = url;
	
	document.splitToolbarFrom.submit();
},
/**
*作者：陈仲民
*跳转到指定页数（一般不允许手工调用）
*@param url  查询数据的action
*@param maxPage  查询数据的最大页码
*@param frm  显示html内容的所在的from，内容为可以标示jquery对象的字符串，例如："form[@name=frm1]"
*return 
*/
gotoPage:function(url){
	
	var maxPage = $("input[@name='maxPage_splitPage']").val();
	var pageIndex = $("input[@name='pageIndex_splitPage']").val();
	
	var reg=/^\d+(\.\d+)?$/;
	
	if (pageIndex == ''){
		alert("请输入指定的页码！");
		return false;
	}
	if (!reg.test(pageIndex)){
		alert("请输入一个整数！");
		return false;
	}
	if ((pageIndex < 1) || (pageIndex > maxPage)){
		alert("您输入的页码超过范围，请重新输入！");
		return false;
	}
	
	//用参数生成url
	url = url + "?curPage_splitPage=" + pageIndex;
	document.splitToolbarFrom.action = url;
	
	document.splitToolbarFrom.submit();
},
/**
*作者：陈仲民
*解析paramList，并生成参数字符串
*@param list  页面传递的参数paramList
*return action后面的需要提交的参数串，例如：“&user=1”
*/
splitParam:function(list){
	
	//分割符
	var code1 = String.fromCharCode(1);
	var code2 = String.fromCharCode(2);
	
	//临时字符串
	var tmp = "";
	
	//解析参数并组成字符串
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