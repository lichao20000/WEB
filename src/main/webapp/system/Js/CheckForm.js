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
	if(IsNull(strValue,strMsg)){
		for(var i=0;i<strValue.length;i++){
			var ch=strValue.substring(i,i+1);
			if((ch<'0'||ch>'9')&&(ch<'a'||ch>'z')&&(ch<'A'||ch>'Z')){
				alert(strMsg+'应为字母或数字')
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
				alert(strMsg+'应为数字')
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
//----------------------函数结束-----------------------
/*-----------------------------------------------------
//函数名:	IsMail
//参数  :	strValue-->表单值,strMsg-->提示信息
//功能  :	判断是否为E-Mail格式
//说明  :	公用系统函数
//返回值:	成功-->True,反之-->False
//描述  :	Create 2002-9-19 of By Dolphin
------------------------------------------------------*/
function IsMail(strValue,strMsg){
	if(IsNull(strValue,strMsg)){
		var ln = strValue.length;
		var sign_1_pos = strValue.indexOf('@');
		var sign_2_pos = strValue.indexOf('.');
		if(ln>4&&sign_1_pos>0&&sign_2_pos!=-1&&sign_2_pos>sign_1_pos&&sign_2_pos<ln-1)
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
	if(strValue != "0") return true;
	else{
		alert(strMsg + "必须选择");
		return false;
	}
}

function IsChose1(strValue,strMsg){
	if(strValue.length>1) return true;
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
