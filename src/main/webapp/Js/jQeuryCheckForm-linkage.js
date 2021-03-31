//作者： 陈仲民5243  jQuery表单验证的方法
jQuery.extend({
/**
 *author:benyp(5260) 2009-1-8
 *检查是否是手机号码【13开头，158，159，189段】
*/
checkMobile:function(number){
	return (/^(?:13\d|18\d|15[89])-?\d{5}(\d{3}|\*{3})$/.test(number));
},
/**
 *author:benyp(5260) 2009-1-8
 *检查是否是电话号码
*/
checkPhone:function(number){
	//"兼容格式: 国家代码(2到3位)-区号(2到3位)-电话号码(7到8位)-分机号(3位)"
    return (/^(([0\+]\d{2,3}-)?(0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/.test(number));
},
/**
*author:benyp (5260) 2007-11-22
*ip地址检查函数（模糊匹配）
*param ip 需要检查的ip地址（可以为空或一部分）
*return 检查通过则为true，否则false
*/
checkIllIP:function(ip){
	if(ip.split(".").length>4){
		return false;
	}else{
		var tem=ip.split(".");
		for(var i=0;i<tem.length;i++){
			if(tem[i].search(/^[0-9]+$/g) == -1 || tem[i]>255){
				return false;
			}
		}
		return true;
	}	
},
/*
*author:benyp (5260) 2007-11-22
*判断是否为整数
*param type:int:整型
*param num return true:是整数 false：不是整数
*/
checkNum:function(num,type){
	var reg;
	if(type=="int"){
		reg=/^[0-9]+$/g;
	}else if(type=="float"){
		reg=/^-?(0|\d+)(\.\d+)?$/;
	}
	return num.search(reg)==-1?false:true;
},
/**
*author:benyp (5260) 2007-11-23
*判断是否为Email格式
*param email
*return true：格式正确；false：格式不正确
*/
checkEmail:function(email){
	var reg=/\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*/g;
	return email.search(reg)==-1?false:true;
},
/**
*作者：王志猛
*ip地址检查的函数
*@param ip 需要检查的ip地址
*return 检查通过则为true，否则false
*/
checkIP:function(ip)
{
	var reg = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5]).(\d{1,2}|1\d\d|2[0-4]\d|25[0-5]).(\d{1,2}|1\d\d|2[0-4]\d|25[0-5]).(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/g;
	if((ip==null)||(ip.search(reg)==-1))
	{
	return false;
	}
	else
	{
	return true;
	}
},
/**
*作者：陈仲民
*检查是否为数字的函数，包括整数和小数
*@param num 需要检查的数字
*return 检查通过则为true，否则false
*/
isNum:function(num)
{
	var reg=/^\d+(\.\d+)?$/;
	
	if (!reg.test(num)){
		return false;
	}
	else{
		return true;
	}
},
/**
*作者：陈仲民
*检查时间格式是否正确的函数  标准格式为：YYYY-MM-DD HH:MI:SS
*@param time 需要检查的时间字符串
*return 检查通过则为true，否则false
*/
isDate:function(time)
{
	var reg = /^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2}) (\d{1,2}):(\d{1,2}):(\d{1,2})$/;
	
	if (!reg.test(time)){
		return false;
	}
	else{
		return true;
	}
},
/**
*作者：陈仲民
*检查字符串是否符合指定的正则表达式
*@param str 需要检查的字符串
*@param reg 用于验证的正则表达式
*return 检查通过则为true，否则false
*/
isMatch:function(str,reg)
{
	if (!reg.test(str)){
		return false;
	}
	else{
		return true;
	}
},
/**
*作者：陈仲民
*检查字符串是否为空或空格
*@param str 需要检查的字符串
*return 为空则为true，否则false
*/
isNull:function(str)
{
	str = str.replace(/(^\s*)|(\s*$)/g,'');
	
	if (str != null && str != ''){
		return false;
	}
	else{
		return true;
	}
}
});