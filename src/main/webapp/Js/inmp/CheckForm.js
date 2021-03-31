/*-----------------------------------------------------
//函数名:	IsNull
//参数  :	strValue-->表单值,strMsg-->提示信息
//功能  :	判断不能为空
//返回值:	成功-->True,反之-->False
//说明  :	公用系统函数FORM的名称为frm
//描述  :	Create 2002-9-19 of By Dolphin
------------------------------------------------------*/
function IsNull(strValue,strMsg){
	if(Trim(strValue).length>0) return true;
	else{
		alert(strMsg+'不能为空');
		return false;
	}
}
//----------------------函数结束-----------------------
/*-----------------------------------------------------
//函数名:	IsIPAddr
//参数  :	strValue-->表单值
//功能  :	判断不能为空
//返回值:	成功-->True,反之-->False
//说明  :	公用系统函数FORM的名称为frm
//描述  :	Create 2002-9-19 of By Dolphin
------------------------------------------------------*/
function IsIPAddr(strValue){
	var msg = "IP地址";
	if(IsNull(strValue,msg)){
		var pos;
		var tmpStr;
		var v = strValue;
		var i=0;
		var bz=true;
		while(bz){
			pos = v.indexOf(".");
			if(i != 3 && pos == -1){
				alert(msg + "格式不符合");
				return false;
			}
			if(pos == -1){pos = v.length;bz=false;}
			tmpStr = v.substring(0,pos);
			if(!chkIPArea(tmpStr)) return false;
			v = v.substring(pos+1,v.length);
			i=i+1;
		}
		if(i=4) return true;
		else{
			alert(msg + "格式不符合");
			return false;			
		}
		/*do{
		    if(i>=4)
			break;
			if(i != 3 && pos == -1){
				alert(msg + "格式不符合");
				return false;
			}
			i=i+1;
			if(pos == -1) pos = v.length;
			tmpStr = v.substring(0,pos);
			if(!chkIPArea(tmpStr)) return false;
			v = v.substring(pos+1,v.length);
			pos = v.indexOf(".");
		}while(pos != -1 || i<=3)
		//if(i==3)  return true;
		//alert(msg + "格式不符合");
		//return false;*/
	}
}

function chkIPArea(strIP)
{
	if(!IsNumber(strIP,"IP地址")) return false;
	if(parseInt(strIP)>255){
		alert("IP地址中" + strIP + "大于255");
		return false;
	}
	return true;
}

//----------------------函数结束-----------------------
/*-----------------------------------------------------
//函数名:	IsAccount
//参数  :	strValue-->表单值,strMsg-->提示信息
//功能  :	判断账号
//返回值:	成功-->True,反之-->False
//说明  :	公用系统函数FORM的名称为frm
//描述  :	Create 2002-9-19 of By Dolphin
------------------------------------------------------*/
function IsAccount(strValue,strMsg){
	var bz = true;
	var i=0;
	var j=0;
	if(IsNull(strValue,strMsg)){
		 for(var i=0;i<strValue.length;i++){
			 var ch=strValue.substring(i,i+1);
			 if (i == 0 && ch == '_')
			 {
				 alert(strMsg+"不能以下划线开头");
				 bz = false;
				 break;
			 }
		     if((ch<'0'||ch>'9')&&(ch<'a'||ch>'z')&&(ch<'A'||ch>'Z')&&(ch!='_')){
			  	alert(strMsg+'应为字母(A-Z或a-z)或数字(0-9)或下划线组成')
			   	bz = false;
		       	break;
		   	}
	   	}	    
	}
	else{
		bz = false;
	}
	if(bz){return true;}
	else{return false;}
}
//----------------------函数结束-----------------------
/*-----------------------------------------------------
//函数名:	IsPassword
//参数  :	strValue-->密码值,strName--> 用户名，
//          strMsg-->提示信息
//功能  :	判断密码
//返回值:	成功-->True,反之-->False
//说明  :	公用系统函数FORM的名称为frm
//描述  :	Create 2006-6-27 of By WangFeng
------------------------------------------------------*/
function IsPassword(strValue,strMsg){
	var bz = true;
	var j=0;
	var k=0;
	if(IsNull(strValue,strMsg)){
		if(strValue.length<6){
			//alert(strMsg+'长度不能少于6位字符')
			//bz = false;
		}else{
	    for(var i=0;i<strValue.length;i++){
		    var ch=strValue.substring(i,i+1);
		    
		    if((ch<'0'||ch>'9')&&(ch<'a'||ch>'z')&&(ch<'A'||ch>'Z')){
		  		alert(strMsg+'应为字母(A-Z或a-z)或数字(0-9)')
		     	bz = false;
	        break;
	      }
	       	
		    if(ch>'0'&&ch<'9'){
	     		k++; 
	     	}else{
	     		j++;
	     	}
      }
      /*
	    if (!((ch<'0'||ch>'9')&&(ch<'a'||ch>'z')&&(ch<'A'||ch>'Z'))){
  	    if(k<2){
 	        alert(strMsg+'中数字不能少于2位')
 	        bz = false;
       	}else{
   	    	if(j<3){
   	   	    alert(strMsg+'中字母不能少于3位')
   	  	    bz = false;
   	      }
        }    	    	
      }
      */       	    
    }      
	}
	else{
		bz = false;
	}

	if(bz){
		return true;
	}else{
		return false;
	}
}
//----------------------函数结束-----------------------
/*-----------------------------------------------------
//函数名:	IsNumber
//参数  :	strValue-->表单值,strMsg-->提示信息
//功能  :	判断是否为数字
//返回值:	成功-->True,反之-->False
//说明  :	公用系统函数FORM的名称为frm
//描述  :	Create 2002-9-19 of By Dolphin
------------------------------------------------------*/
function IsNumber(strValue,strMsg){
	var bz = true;
	if(IsNull(strValue,strMsg)){
		for(var i=0;i<strValue.length;i++){
			var ch=strValue.substring(i,i+1);
			if(ch<'0'||ch>'9'){
				alert(strMsg+'应为不小于0的整数');
				bz = false;
				break;
			}
		}
	}
	else{
		bz = false;
	}
	if(bz){return true;}
	else{return false;}
}

function IsNumber2(strValue,strMsg){
	var bz = true;

	for(var i=0;i<strValue.length;i++){
		var ch=strValue.substring(i,i+1);
		if(ch<'0'||ch>'9'){
			alert(strMsg+'应为数字')
			bz = false;
			break;
		}
	}

	if(bz){return true;}
	else{return false;}
}

//允许包含负号和小数点
//2005-10-10 陈跃
function isFloatNumber(strValue,strMsg){
	var bz = true;
	//负号位置
	var index = strValue.lastIndexOf("-");
	if(index>0)
		bz = false;
	else{
		//小数点位置
		index = strValue.indexOf(".");
		var index2 = strValue.lastIndexOf(".");
		//是否有一个以上的小数点
		if(index2>index)
			bz = false;
		else{
			//是否有非法字符
			for(var i=0;i<strValue.length;i++){
				var ch = strValue.charAt(i);
				if(ch!='-' && ch!='.' && (ch<'0' || ch>'9')){
					bz = false;
					break;
				}
			}
		}
	}
	if(!bz)
		alert(strMsg+"应为数值");
	return bz;
}
//----------------------函数结束-----------------------
/*-----------------------------------------------------
//函数名:	IsMail
//参数  :	strValue-->表单值,strMsg-->提示信息
//功能  :	判断是否为E-Mail格式
//说明  :	公用系统函数
//返回值:	成功-->True,反之-->False
//描述  :	Create 2002-9-19 of By Dolphin
			Modified by Chen Yue on 2005-10-8
------------------------------------------------------*/
function IsMail(strValue,strMsg){
	if(IsNull(strValue,strMsg)){
		var ln = strValue.length;
		var sign_1_pos = strValue.indexOf('@');
		//var sign_2_pos = strValue.indexOf('.');
		var sign_2_pos = strValue.lastIndexOf('.');
		//if(ln>4&&sign_1_pos>0&&sign_2_pos!=-1&&sign_2_pos>sign_1_pos&&sign_2_pos<ln-1)
		//Email地址：a@b.c或liu.dong@hp.com
		if(ln>5&&sign_1_pos>0 && sign_2_pos>sign_1_pos+1 && sign_2_pos<ln-1)
			return true;
		else{
			alert(strMsg+'格式不正确');
			return false;
		}
	}
	else
		return false;
}
//----------------------函数结束-----------------------
/*-----------------------------------------------------
//函数名:	Trim
//参数  :	strValue-->字符串值
//功能  :	去除字符串值两边的空格
//返回值:	去除两边的空格字符串值
//说明  :	公用系统函数
//描述  :	Create 2002-9-19 of By Dolphin
------------------------------------------------------*/
function Trim(strValue){
	var v = strValue;
	var i = 0;
	while(i<v.length){
	  if(v.substring(i,i+1)!=' '){
		v = v.substring(i,v.length) 
		break;
	  }
	  i = i + 1;
	  if(i==v.length){
        v="";
      }
	}

	i = v.length;
	while(i>0){
	  if(v.substring(i-1,i)!=' '){
	    v = v.substring(0,i);
		break;
	  }	
	  i = i - 1;
	}

	return v;
}
//----------------------函数结束-----------------------
/*-----------------------------------------------------
//函数名:	IsDate
//参数  :	strValue-->表单值
//功能  :	判断日期格式
//返回值:	成功-->True,反之-->False
//说明  :	公用系统函数FORM的名称为frm
//描述  :	Create 2002-9-19 of By Dolphin
------------------------------------------------------*/
function IsDate(strValue){
	var v = strValue;
	if(!IsNull(v,"日期")) return false;
	if(isNaN(Date.parse(v))){
		alert("日期格式不符合，正确格式是：月-日-年");
		return false;		
	}
	var pos = v.indexOf("-");
	if(pos == -1){
		alert("日期格式不符合，正确格式是：月-日-年");
		return false;		
	}
	tmpStr1 = v.substring(0,pos);
	if(tmpStr1.length >2 || parseInt(tmpStr1) > 12){
		alert("日期格式不符合，正确格式是：月-日-年");
		return false;	
	}
	v = v.substring(pos+1,v.length);
	pos = v.indexOf("-");
	if(pos == -1){
		alert("日期格式不符合，正确格式是：月-日-年");
		return false;		
	}
	tmpStr2 = v.substring(0,pos);
	if(tmpStr2.length >2 || parseInt(tmpStr2) > 31){
		alert("日期格式不符合，正确格式是：月-日-年");
		return false;	
	}
	return true;
}
//----------------------函数结束-----------------------
/*-----------------------------------------------------
//函数名:	IsChose
//参数  :	strValue-->表单值 strMsg-->提示信息
//功能  :	判断是否选择
//返回值:	成功-->True,反之-->False
//说明  :	公用系统函数FORM的名称为frm
//描述  :	Create 2002-9-25 of By Dolphin
------------------------------------------------------*/
function IsChose(strValue,strMsg){
	if(parseInt(strValue) > -1) return true;
	else{
		alert(strMsg + "必须选择");
		return false;
	}
}

function IsChose1(strValue,strMsg){
	if(strValue != -1) return true;
	else{
		alert(strMsg + "必须选择");
		return false;
	}
}
//----------------------函数结束-----------------------

function IsStaff(strValue){
	var v = strValue;
	for(var i=0; i<strValue.length; i++){
		var ch=strValue.substring(i,i+1);
		if(ch!="0") break;
		v = v.substring(1,v.length);
	}
	return v;
}

function IsMask(mask){
	var msg = "掩码";
	if(IsIPAddr(mask)){
		arrMask = mask.split(".");
		var binMask = "";
		for(var i=0; i<arrMask.length; i++){
			binMask += DecmToBin(arrMask[i]);
		}
	}
	else{
		return false;
	}
	var IsZero = false;
	for(var i=0; i<binMask.length; i++){
		ch = binMask.substring(i,i+1);
		if(parseInt(ch) == 0)
			IsZero = true;
		else
			if(IsZero == true){
				alert(msg + "格式不符合")
				return false;
			}
	}
	return true;
}

function DecmToBin(v){
	num = parseInt(v);
	var s = "";
	while(num>0){
		modnum = num % 2;
		num = parseInt(num / 2);
		s = modnum + s;
	}
	var result = ""; 
	if(s.length<4){
		for(var i=0; i<4-s.length; i++){
			result += "0";
		}
	}
	return result + s;
}



function KillSpace(x){
	while((x.length > 0) && (x.charAt(0) == ' '))
		x = x.substring(1,x.length)
	while((x.length>0) && (x.charAt(x.length-1) ==' '))
		x = x.substring(0,x.length-1)
	return x
}

/*-----------------------------------------------------
//函数名:	IsTime
//参数  :	strValue-->表单值
//功能  :	判断日期格式
//返回值:	成功-->True,反之-->False
//说明  :	公用系统函数FORM的名称为frm
//描述  :	Create 2004-7-17 of By smf
------------------------------------------------------*/
function IsTime(strValue){
	var v = strValue;
	
	if(!IsNull(v,"时间")) return false;
    /*
	if(isNaN(Date.parse(v))){
		alert("日期格式不符合，正确格式是：月-日-年");
		return false;		
	}
	*/

	var pos = v.indexOf(":");
	if(pos == -1){
		alert("时间格式不符合，正确格式如：点:分:秒(10:11:12)");
		return false;		
	}
	
	tmpStr1 = v.substring(0,pos);
	if(tmpStr1.length >2 || parseInt(tmpStr1) > 23){
		alert("时间格式不符合，点数不能超过24点");
		return false;	
		}
	v = v.substring(pos+1,v.length);
		
	pos = v.indexOf(":");
	if(pos == -1){
		alert("时间格式不符合，正确格式如：点:分:秒(10:11:12)");
		return false;		
	}
	tmpStr2 = v.substring(0,pos);
	
	if(tmpStr2.length >2 || parseInt(tmpStr2) > 59){
		alert("时间格式不符合，分钟数不能超过59分");
		return false;		
	}
    tmpStr3 = v.substring(pos+1,v.length)
    if(tmpStr3.length >2 || parseInt(tmpStr3) > 59){
		alert("时间格式不符合，秒数不能超过59秒");
		return false;	
	}
	return true;
}


//----------------------函数结束-----------------------

/*-----------------------------------------------------
//函数名:	IsTime
//参数  :	strValue-->表单值
//功能  :	判断日期格式
//返回值:	成功-->True,反之-->False
//说明  :	公用系统函数FORM的名称为frm
//描述  :	Create 2004-7-17 of By smf
------------------------------------------------------*/
function IsNum(NUM)
{
 var i,j,strTemp;
 strTemp="0123456789-.";
 if ( NUM.length== 0){
  return false;
  }
 for (i=0;i<NUM.length;i++)
 {
  j=strTemp.indexOf(NUM.charAt(i)); 
  if (j==-1)
  {
  //说明有字符不是数字
   return false;
  }
 }
 //说明是数字
 return true;
}
/*-----------------------------------------------------
//函数名:	checkAll
//参数  :	name-->checkbox名
//功能  :	判断多选框是否有被选中的
//返回值:	成功-->True,反之-->False
//说明  :	公用系统函数FORM的名称为frm
//描述  :	Create 2004-7-17 of By smf
------------------------------------------------------*/
function checkAll(name){
	var oChk = document.all(name);
	var flag = false;
	for(var i=0; i<oChk.length; i++){
		if(oChk[i].checked){
			flag = true;
			break;
		}
	}
	
	return flag;
}

String.prototype.trim = function() {
	return this.replace(/(^\s*)|(\s*$)/gi, "");
}
/*-----------------------------------------------------

/**
 * 严格限定，认为
 * + 1,- 1,+0000，-0000，+0000.，0000，
 * 00000.1，0001.，0.，001.，100.，09.9 
 * 等输入不合法
 * 浮点数：从浮点数精度考虑，0.00000是合法的
 */
/*------------------------------------------------------*/
function checkNum(str) {
	// 去掉首尾空格	
	str = str.trim();
	
	if(str.indexOf(".") >= 0 && str.split(".").length == 2) {
		// 1个小数点，可能浮点数
		var regExpInvalid1 = /^(\+|-)?0\d+\.\d*$/;
		var regExpInvalid2 = /^(\+|-)?0{2,}\d*\.\d*$/;
		var regExpInvalid3 = /^(\+|-)?\d*\.$/;		
		var regExpInvalid4 = /^(\+|-)?\.\d*$/;
		var regExp = /^(\+|-)?\d*\.\d*$/gi;
		
		return (!regExpInvalid1.test(str) 
		      && !regExpInvalid2.test(str)
		      && !regExpInvalid3.test(str)
		      && !regExpInvalid4.test(str)
					&& regExp.test(str));		
		
	} else if(str.indexOf(".") < 0){
		// 可能整数
		// 正则表达式
		var regExpInvalidZero1 = /^(\+|-)0{1,}$/;
		var regExpInvalidZero2 = /^0{2,}$/gi;
		var regExpInvalidNoneZero = /^(\+|-)?0\d+$/;
		var regExp = /^(\+|-)?\d+$/;
		return (!regExpInvalidZero1.test(str) 
		      && !regExpInvalidZero2.test(str)
		      && !regExpInvalidNoneZero.test(str)
					&& regExp.test(str));	
	
	} else {
		// 不可能两个小数点
		return false;
	}
}




function checkDate(inString,fieldName)
{
	var dd = document.all(inString).value;alert(dd);
    var tempDate;
    var a=new Date(dd);
    var y=a.getFullYear();
    var m=a.getMonth()+1;
    var d=a.getDate();
    var myday=y + "/" + m + "/" + d;
    if (myday!=dd)
    {
    alert  ("请为"+ fieldName +"输入一有效日期(yyyy/m/d/)。"); 
    return false;     
    }
    return true; 
} 

//将2004-10-20此样的时间转换成long型
function DateParse(strValue)
{
	strValue=strValue.substring(5)+"-"+strValue.substring(0,4);	
	return Date.parse(strValue);
}




//----------------------函数结束-----------------------
