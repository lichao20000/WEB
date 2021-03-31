/*-----------------------------------------------------
//������:	IsNull
//����  :	strValue-->��ֵ,strMsg-->��ʾ��Ϣ
//����  :	�жϲ���Ϊ��
//����ֵ:	�ɹ�-->True,��֮-->False
//˵��  :	����ϵͳ����FORM������Ϊfrm
//����  :	Create 2002-9-19 of By Dolphin
------------------------------------------------------*/
function IsNull(strValue,strMsg){
	if(Trim(strValue).length>0) return true;
	else{
		alert(strMsg+'����Ϊ��');
		return false;
	}
}
//----------------------��������-----------------------
/*-----------------------------------------------------
//������:	IsIPAddr
//����  :	strValue-->��ֵ
//����  :	�жϲ���Ϊ��
//����ֵ:	�ɹ�-->True,��֮-->False
//˵��  :	����ϵͳ����FORM������Ϊfrm
//����  :	Create 2002-9-19 of By Dolphin
------------------------------------------------------*/
function IsIPAddr(strValue){
	var msg = "IP��ַ";
	if(IsNull(strValue,msg)){
		var pos;
		var tmpStr;
		var v = strValue;
		var i=0;
		var bz=true;
		while(bz){
			pos = v.indexOf(".");
			if(i != 3 && pos == -1){
				alert(msg + "��ʽ������");
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
			alert(msg + "��ʽ������");
			return false;			
		}
		/*do{
		    if(i>=4)
			break;
			if(i != 3 && pos == -1){
				alert(msg + "��ʽ������");
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
		//alert(msg + "��ʽ������");
		//return false;*/
	}
}

function chkIPArea(strIP)
{
	if(!IsNumber(strIP,"IP��ַ")) return false;
	if(parseInt(strIP)>255){
		alert("IP��ַ��" + strIP + "����255");
		return false;
	}
	return true;
}
//----------------------��������-----------------------
/*-----------------------------------------------------
//������:	IsAccount
//����  :	strValue-->��ֵ,strMsg-->��ʾ��Ϣ
//����  :	�ж��˺�
//����ֵ:	�ɹ�-->True,��֮-->False
//˵��  :	����ϵͳ����FORM������Ϊfrm
//����  :	Create 2002-9-19 of By Dolphin
------------------------------------------------------*/
function IsAccount(strValue,strMsg){
	var bz = true;
	if(IsNull(strValue,strMsg)){
		for(var i=0;i<strValue.length;i++){
			var ch=strValue.substring(i,i+1);
			if((ch<'0'||ch>'9')&&(ch<'a'||ch>'z')&&(ch<'A'||ch>'Z')){
				alert(strMsg+'ӦΪ��ĸ������')
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
//----------------------��������-----------------------
/*-----------------------------------------------------
//������:	IsNumber
//����  :	strValue-->��ֵ,strMsg-->��ʾ��Ϣ
//����  :	�ж��Ƿ�Ϊ����
//����ֵ:	�ɹ�-->True,��֮-->False
//˵��  :	����ϵͳ����FORM������Ϊfrm
//����  :	Create 2002-9-19 of By Dolphin
------------------------------------------------------*/
function IsNumber(strValue,strMsg){
	var bz = true;
	if(IsNull(strValue,strMsg)){
		for(var i=0;i<strValue.length;i++){
			var ch=strValue.substring(i,i+1);
			if(ch<'0'||ch>'9'){
				alert(strMsg+'ӦΪ����')
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
			alert(strMsg+'ӦΪ����')
			bz = false;
			break;
		}
	}

	if(bz){return true;}
	else{return false;}
}
//----------------------��������-----------------------
/*-----------------------------------------------------
//������:	IsMail
//����  :	strValue-->��ֵ,strMsg-->��ʾ��Ϣ
//����  :	�ж��Ƿ�ΪE-Mail��ʽ
//˵��  :	����ϵͳ����
//����ֵ:	�ɹ�-->True,��֮-->False
//����  :	Create 2002-9-19 of By Dolphin
------------------------------------------------------*/
function IsMail(strValue,strMsg){
	if(IsNull(strValue,strMsg)){
		var ln = strValue.length;
		var sign_1_pos = strValue.indexOf('@');
		var sign_2_pos = strValue.indexOf('.');
		if(ln>4&&sign_1_pos>0&&sign_2_pos!=-1&&sign_2_pos>sign_1_pos&&sign_2_pos<ln-1)
			return true;
		else{
			alert(strMsg+'��ʽ����ȷ');
			return false;
		}
	}
	else
		return false;
}
//----------------------��������-----------------------
/*-----------------------------------------------------
//������:	Trim
//����  :	strValue-->�ַ���ֵ
//����  :	ȥ���ַ���ֵ���ߵĿո�
//����ֵ:	ȥ�����ߵĿո��ַ���ֵ
//˵��  :	����ϵͳ����
//����  :	Create 2002-9-19 of By Dolphin
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
//----------------------��������-----------------------
/*-----------------------------------------------------
//������:	IsDate
//����  :	strValue-->��ֵ
//����  :	�ж����ڸ�ʽ
//����ֵ:	�ɹ�-->True,��֮-->False
//˵��  :	����ϵͳ����FORM������Ϊfrm
//����  :	Create 2002-9-19 of By Dolphin
------------------------------------------------------*/
function IsDate(strValue){
	var v = strValue;
	if(!IsNull(v,"����")) return false;
	if(isNaN(Date.parse(v))){
		alert("���ڸ�ʽ�����ϣ���ȷ��ʽ�ǣ���-��-��");
		return false;		
	}
	var pos = v.indexOf("-");
	if(pos == -1){
		alert("���ڸ�ʽ�����ϣ���ȷ��ʽ�ǣ���-��-��");
		return false;		
	}
	tmpStr1 = v.substring(0,pos);
	if(tmpStr1.length >2 || parseInt(tmpStr1) > 12){
		alert("���ڸ�ʽ�����ϣ���ȷ��ʽ�ǣ���-��-��");
		return false;	
	}
	v = v.substring(pos+1,v.length);
	pos = v.indexOf("-");
	if(pos == -1){
		alert("���ڸ�ʽ�����ϣ���ȷ��ʽ�ǣ���-��-��");
		return false;		
	}
	tmpStr2 = v.substring(0,pos);
	if(tmpStr2.length >2 || parseInt(tmpStr2) > 31){
		alert("���ڸ�ʽ�����ϣ���ȷ��ʽ�ǣ���-��-��");
		return false;	
	}
	return true;
}
//----------------------��������-----------------------
/*-----------------------------------------------------
//������:	IsChose
//����  :	strValue-->��ֵ strMsg-->��ʾ��Ϣ
//����  :	�ж��Ƿ�ѡ��
//����ֵ:	�ɹ�-->True,��֮-->False
//˵��  :	����ϵͳ����FORM������Ϊfrm
//����  :	Create 2002-9-25 of By Dolphin
------------------------------------------------------*/
function IsChose(strValue,strMsg){
	if(strValue != "0") return true;
	else{
		alert(strMsg + "����ѡ��");
		return false;
	}
}

function IsChose1(strValue,strMsg){
	if(strValue.length>1) return true;
	else{
		alert(strMsg + "����ѡ��");
		return false;
	}
}
//----------------------��������-----------------------

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
	var msg = "����";
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
				alert(msg + "��ʽ������")
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
