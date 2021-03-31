//���ߣ� ������5243  jQuery����֤�ķ���
jQuery.extend({
/**
 *author:benyp(5260) 2009-1-8
 *����Ƿ����ֻ����롾13��ͷ��158��159��189�Ρ�
*/
checkMobile:function(number){
	return (/^(?:13\d|18\d|15[89])-?\d{5}(\d{3}|\*{3})$/.test(number));
},
/**
 *author:benyp(5260) 2009-1-8
 *����Ƿ��ǵ绰����
*/
checkPhone:function(number){
	//"���ݸ�ʽ: ���Ҵ���(2��3λ)-����(2��3λ)-�绰����(7��8λ)-�ֻ���(3λ)"
    return (/^(([0\+]\d{2,3}-)?(0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/.test(number));
},
/**
*author:benyp (5260) 2007-11-22
*ip��ַ��麯����ģ��ƥ�䣩
*param ip ��Ҫ����ip��ַ������Ϊ�ջ�һ���֣�
*return ���ͨ����Ϊtrue������false
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
*�ж��Ƿ�Ϊ����
*param type:int:����
*param num return true:������ false����������
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
*�ж��Ƿ�ΪEmail��ʽ
*param email
*return true����ʽ��ȷ��false����ʽ����ȷ
*/
checkEmail:function(email){
	var reg=/\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*/g;
	return email.search(reg)==-1?false:true;
},
/**
*���ߣ���־��
*ip��ַ���ĺ���
*@param ip ��Ҫ����ip��ַ
*return ���ͨ����Ϊtrue������false
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
*���ߣ�������
*����Ƿ�Ϊ���ֵĺ���������������С��
*@param num ��Ҫ��������
*return ���ͨ����Ϊtrue������false
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
*���ߣ�������
*���ʱ���ʽ�Ƿ���ȷ�ĺ���  ��׼��ʽΪ��YYYY-MM-DD HH:MI:SS
*@param time ��Ҫ����ʱ���ַ���
*return ���ͨ����Ϊtrue������false
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
*���ߣ�������
*����ַ����Ƿ����ָ����������ʽ
*@param str ��Ҫ�����ַ���
*@param reg ������֤��������ʽ
*return ���ͨ����Ϊtrue������false
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
*���ߣ�������
*����ַ����Ƿ�Ϊ�ջ�ո�
*@param str ��Ҫ�����ַ���
*return Ϊ����Ϊtrue������false
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