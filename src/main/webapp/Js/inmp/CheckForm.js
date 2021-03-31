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
	var i=0;
	var j=0;
	if(IsNull(strValue,strMsg)){
		 for(var i=0;i<strValue.length;i++){
			 var ch=strValue.substring(i,i+1);
			 if (i == 0 && ch == '_')
			 {
				 alert(strMsg+"�������»��߿�ͷ");
				 bz = false;
				 break;
			 }
		     if((ch<'0'||ch>'9')&&(ch<'a'||ch>'z')&&(ch<'A'||ch>'Z')&&(ch!='_')){
			  	alert(strMsg+'ӦΪ��ĸ(A-Z��a-z)������(0-9)���»������')
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
//������:	IsPassword
//����  :	strValue-->����ֵ,strName--> �û�����
//          strMsg-->��ʾ��Ϣ
//����  :	�ж�����
//����ֵ:	�ɹ�-->True,��֮-->False
//˵��  :	����ϵͳ����FORM������Ϊfrm
//����  :	Create 2006-6-27 of By WangFeng
------------------------------------------------------*/
function IsPassword(strValue,strMsg){
	var bz = true;
	var j=0;
	var k=0;
	if(IsNull(strValue,strMsg)){
		if(strValue.length<6){
			//alert(strMsg+'���Ȳ�������6λ�ַ�')
			//bz = false;
		}else{
	    for(var i=0;i<strValue.length;i++){
		    var ch=strValue.substring(i,i+1);
		    
		    if((ch<'0'||ch>'9')&&(ch<'a'||ch>'z')&&(ch<'A'||ch>'Z')){
		  		alert(strMsg+'ӦΪ��ĸ(A-Z��a-z)������(0-9)')
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
 	        alert(strMsg+'�����ֲ�������2λ')
 	        bz = false;
       	}else{
   	    	if(j<3){
   	   	    alert(strMsg+'����ĸ��������3λ')
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
				alert(strMsg+'ӦΪ��С��0������');
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

//����������ź�С����
//2005-10-10 ��Ծ
function isFloatNumber(strValue,strMsg){
	var bz = true;
	//����λ��
	var index = strValue.lastIndexOf("-");
	if(index>0)
		bz = false;
	else{
		//С����λ��
		index = strValue.indexOf(".");
		var index2 = strValue.lastIndexOf(".");
		//�Ƿ���һ�����ϵ�С����
		if(index2>index)
			bz = false;
		else{
			//�Ƿ��зǷ��ַ�
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
		alert(strMsg+"ӦΪ��ֵ");
	return bz;
}
//----------------------��������-----------------------
/*-----------------------------------------------------
//������:	IsMail
//����  :	strValue-->��ֵ,strMsg-->��ʾ��Ϣ
//����  :	�ж��Ƿ�ΪE-Mail��ʽ
//˵��  :	����ϵͳ����
//����ֵ:	�ɹ�-->True,��֮-->False
//����  :	Create 2002-9-19 of By Dolphin
			Modified by Chen Yue on 2005-10-8
------------------------------------------------------*/
function IsMail(strValue,strMsg){
	if(IsNull(strValue,strMsg)){
		var ln = strValue.length;
		var sign_1_pos = strValue.indexOf('@');
		//var sign_2_pos = strValue.indexOf('.');
		var sign_2_pos = strValue.lastIndexOf('.');
		//if(ln>4&&sign_1_pos>0&&sign_2_pos!=-1&&sign_2_pos>sign_1_pos&&sign_2_pos<ln-1)
		//Email��ַ��a@b.c��liu.dong@hp.com
		if(ln>5&&sign_1_pos>0 && sign_2_pos>sign_1_pos+1 && sign_2_pos<ln-1)
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
	if(parseInt(strValue) > -1) return true;
	else{
		alert(strMsg + "����ѡ��");
		return false;
	}
}

function IsChose1(strValue,strMsg){
	if(strValue != -1) return true;
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



function KillSpace(x){
	while((x.length > 0) && (x.charAt(0) == ' '))
		x = x.substring(1,x.length)
	while((x.length>0) && (x.charAt(x.length-1) ==' '))
		x = x.substring(0,x.length-1)
	return x
}

/*-----------------------------------------------------
//������:	IsTime
//����  :	strValue-->��ֵ
//����  :	�ж����ڸ�ʽ
//����ֵ:	�ɹ�-->True,��֮-->False
//˵��  :	����ϵͳ����FORM������Ϊfrm
//����  :	Create 2004-7-17 of By smf
------------------------------------------------------*/
function IsTime(strValue){
	var v = strValue;
	
	if(!IsNull(v,"ʱ��")) return false;
    /*
	if(isNaN(Date.parse(v))){
		alert("���ڸ�ʽ�����ϣ���ȷ��ʽ�ǣ���-��-��");
		return false;		
	}
	*/

	var pos = v.indexOf(":");
	if(pos == -1){
		alert("ʱ���ʽ�����ϣ���ȷ��ʽ�磺��:��:��(10:11:12)");
		return false;		
	}
	
	tmpStr1 = v.substring(0,pos);
	if(tmpStr1.length >2 || parseInt(tmpStr1) > 23){
		alert("ʱ���ʽ�����ϣ��������ܳ���24��");
		return false;	
		}
	v = v.substring(pos+1,v.length);
		
	pos = v.indexOf(":");
	if(pos == -1){
		alert("ʱ���ʽ�����ϣ���ȷ��ʽ�磺��:��:��(10:11:12)");
		return false;		
	}
	tmpStr2 = v.substring(0,pos);
	
	if(tmpStr2.length >2 || parseInt(tmpStr2) > 59){
		alert("ʱ���ʽ�����ϣ����������ܳ���59��");
		return false;		
	}
    tmpStr3 = v.substring(pos+1,v.length)
    if(tmpStr3.length >2 || parseInt(tmpStr3) > 59){
		alert("ʱ���ʽ�����ϣ��������ܳ���59��");
		return false;	
	}
	return true;
}


//----------------------��������-----------------------

/*-----------------------------------------------------
//������:	IsTime
//����  :	strValue-->��ֵ
//����  :	�ж����ڸ�ʽ
//����ֵ:	�ɹ�-->True,��֮-->False
//˵��  :	����ϵͳ����FORM������Ϊfrm
//����  :	Create 2004-7-17 of By smf
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
  //˵�����ַ���������
   return false;
  }
 }
 //˵��������
 return true;
}
/*-----------------------------------------------------
//������:	checkAll
//����  :	name-->checkbox��
//����  :	�ж϶�ѡ���Ƿ��б�ѡ�е�
//����ֵ:	�ɹ�-->True,��֮-->False
//˵��  :	����ϵͳ����FORM������Ϊfrm
//����  :	Create 2004-7-17 of By smf
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
 * �ϸ��޶�����Ϊ
 * + 1,- 1,+0000��-0000��+0000.��0000��
 * 00000.1��0001.��0.��001.��100.��09.9 
 * �����벻�Ϸ�
 * ���������Ӹ��������ȿ��ǣ�0.00000�ǺϷ���
 */
/*------------------------------------------------------*/
function checkNum(str) {
	// ȥ����β�ո�	
	str = str.trim();
	
	if(str.indexOf(".") >= 0 && str.split(".").length == 2) {
		// 1��С���㣬���ܸ�����
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
		// ��������
		// ������ʽ
		var regExpInvalidZero1 = /^(\+|-)0{1,}$/;
		var regExpInvalidZero2 = /^0{2,}$/gi;
		var regExpInvalidNoneZero = /^(\+|-)?0\d+$/;
		var regExp = /^(\+|-)?\d+$/;
		return (!regExpInvalidZero1.test(str) 
		      && !regExpInvalidZero2.test(str)
		      && !regExpInvalidNoneZero.test(str)
					&& regExp.test(str));	
	
	} else {
		// ����������С����
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
    alert  ("��Ϊ"+ fieldName +"����һ��Ч����(yyyy/m/d/)��"); 
    return false;     
    }
    return true; 
} 

//��2004-10-20������ʱ��ת����long��
function DateParse(strValue)
{
	strValue=strValue.substring(5)+"-"+strValue.substring(0,4);	
	return Date.parse(strValue);
}




//----------------------��������-----------------------
