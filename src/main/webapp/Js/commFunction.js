/*
 * ����Ȩ�޲�����¼��־���÷���
 * ����ajax����������Ȩ�޲�����־���ݼ�¼��t_log_authoper����;
 * ����jquery ajax��������jquery.js
 * 
 * @param authCode ����Ȩ�ޱ��룬����Ϊ��
 * @param operDesc ��¼��־���ݣ����Ϊ�գ���ʹ��Ĭ����־����
 */
function superAuthLog(authCode, operDesc) {
	var url = "/itms/itms/resource/logSuperPowerManage!addSuperAuthLog.action";
	$.post(url, {
		authCode : authCode,
		// ���û�д���operDesc��������ֵΪundefined,����Ϊ���ַ���
		operDesc : operDesc ? operDesc : ''
	});
}

/*-----------------------------------------------------

 //������:	IsNull

 //����  :	strValue-->��ֵ,strMsg-->��ʾ��Ϣ

 //����  :	�жϲ���Ϊ��

 //����ֵ:	�ɹ�-->True,��֮-->False

 //˵��  :	����ϵͳ����FORM������Ϊfrm

 //����  :	Create 2002-9-19 of By Dolphin

 ------------------------------------------------------*/

function IsNull(strValue, strMsg) {

	if (Trim(strValue).length > 0)
		return true;

	else {

		alert(strMsg + '����Ϊ��');

		return false;

	}

}

// ----------------------��������-----------------------

/*-----------------------------------------------------

 //������:	IsNumber

 //����  :	strValue-->��ֵ,strMsg-->��ʾ��Ϣ

 //����  :	�ж��Ƿ�Ϊ����

 //����ֵ:	�ɹ�-->True,��֮-->False

 //˵��  :	����ϵͳ����FORM������Ϊfrm

 //����  :	Create 2002-9-19 of By Dolphin

 ------------------------------------------------------*/

function IsNumber(strValue, strMsg) {

	var bz = true;

	if (IsNull(strValue, strMsg)) {

		for ( var i = 0; i < strValue.length; i++) {

			var ch = strValue.substring(i, i + 1);

			if (ch < '0' || ch > '9') {

				alert(strMsg + 'ӦΪ����');

				bz = false;

				break;

			}

		}

	}

	else {

		bz = false;

	}

	if (bz) {
		return true;
	}

	else {
		return false;
	}

}

function IsNumber2(strValue, strMsg) {

	var bz = true;

	for ( var i = 0; i < strValue.length; i++) {

		var ch = strValue.substring(i, i + 1);

		if (ch < '0' || ch > '9') {

			alert(strMsg + 'ӦΪ����');

			bz = false;

			break;

		}

	}

	if (bz) {
		return true;
	}

	else {
		return false;
	}

}

// ----------------------��������-----------------------

/*-----------------------------------------------------

 //������:	IsMail

 //����  :	strValue-->��ֵ,strMsg-->��ʾ��Ϣ

 //����  :	�ж��Ƿ�ΪE-Mail��ʽ

 //˵��  :	����ϵͳ����

 //����ֵ:	�ɹ�-->True,��֮-->False

 //����  :	Create 2002-9-19 of By Dolphin

 ------------------------------------------------------*/

function IsMail(strValue, strMsg) {

	if (IsNull(strValue, strMsg)) {

		var ln = strValue.length;

		var sign_1_pos = strValue.indexOf('@');

		var sign_2_pos = strValue.indexOf('.');

		if (ln > 4 && sign_1_pos > 0 && sign_2_pos != -1
				&& sign_2_pos > sign_1_pos && sign_2_pos < ln - 1)

			return true;

		else {

			alert(strMsg + '��ʽ����ȷ');

			return false;

		}

	}

	else

		return false;

}

// ----------------------��������-----------------------

/*-----------------------------------------------------

 //������:	Trim

 //����  :	strValue-->�ַ���ֵ

 //����  :	ȥ���ַ���ֵ���ߵĿո�

 //����ֵ:	ȥ�����ߵĿո��ַ���ֵ

 //˵��  :	����ϵͳ����

 //����  :	Create 2002-9-19 of By Dolphin

 ------------------------------------------------------*/

function Trim(strValue) {

	var v = strValue;

	var i = 0;

	while (i < v.length) {

		if (v.substring(i, i + 1) != ' ') {

			v = v.substring(i, v.length);

			break;

		}

		i = i + 1;

	}

	i = v.length;

	while (i > 0) {

		if (v.substring(i - 1, i) != ' ') {

			v = v.substring(0, i);

			break;

		}

		i = i - 1;

	}

	return v;

}

// ----------------------��������-----------------------

/*-----------------------------------------------------

 //������:	IsDate

 //����  :	strValue-->��ֵ

 //����  :	�ж����ڸ�ʽ

 //����ֵ:	�ɹ�-->True,��֮-->False

 //˵��  :	����ϵͳ����FORM������Ϊfrm

 //����  :	Create 2002-9-19 of By Dolphin

 ------------------------------------------------------*/

function IsDate(strValue) {

	var v = strValue;

	if (!IsNull(v, "����"))
		return false;

	if (isNaN(Date.parse(v))) {

		alert("���ڸ�ʽ�����ϣ���ȷ��ʽ�ǣ���-��-��");

		return false;

	}

	var pos = v.indexOf("-");

	if (pos == -1) {

		alert("���ڸ�ʽ�����ϣ���ȷ��ʽ�ǣ���-��-��");

		return false;

	}

	tmpStr1 = v.substring(0, pos);

	if (tmpStr1.length > 2 || parseInt(tmpStr1) > 12) {

		alert("���ڸ�ʽ�����ϣ���ȷ��ʽ�ǣ���-��-��");

		return false;

	}

	v = v.substring(pos + 1, v.length);

	pos = v.indexOf("-");

	if (pos == -1) {

		alert("���ڸ�ʽ�����ϣ���ȷ��ʽ�ǣ���-��-��");

		return false;

	}

	tmpStr2 = v.substring(0, pos);

	if (tmpStr2.length > 2 || parseInt(tmpStr2) > 31) {

		alert("���ڸ�ʽ�����ϣ���ȷ��ʽ�ǣ���-��-��");

		return false;

	}

	return true;

}

// ----------------------��������-----------------------

/*-----------------------------------------------------

 //������:	IsChose

 //����  :	strValue-->��ֵ strMsg-->��ʾ��Ϣ

 //����  :	�ж��Ƿ�ѡ��

 //����ֵ:	�ɹ�-->True,��֮-->False

 //˵��  :	����ϵͳ����FORM������Ϊfrm

 //����  :	Create 2002-9-25 of By Dolphin

 ------------------------------------------------------*/

function IsChose(strValue, strMsg) {

	if (parseInt(strValue) > 0)
		return true;

	else {

		alert(strMsg + "����ѡ��");

		return false;

	}

}

function IsChose1(strValue, strMsg) {

	if (strValue != 0)
		return true;

	else {

		alert(strMsg + "����ѡ��");

		return false;

	}

}

// ----------------------��������-----------------------

function IsMask(mask) {

	var msg = "����";

	if (IsIPAddr(mask)) {

		arrMask = mask.split(".");

		var binMask = "";

		for ( var i = 0; i < arrMask.length; i++) {

			binMask += DecmToBin(arrMask[i]);

		}

	}

	else {

		return false;

	}

	var IsZero = false;

	for ( var i = 0; i < binMask.length; i++) {

		ch = binMask.substring(i, i + 1);

		if (parseInt(ch) == 0)

			IsZero = true;

		else

		if (IsZero == true) {

			alert(msg + "��ʽ������");

			return false;

		}

	}

	return true;

}

function DecmToBin(v) {

	num = parseInt(v);

	var s = "";

	while (num > 0) {

		modnum = num % 2;

		num = parseInt(num / 2);

		s = modnum + s;

	}

	var result = "";

	if (s.length < 4) {

		for ( var i = 0; i < 4 - s.length; i++) {

			result += "0";

		}

	}

	return result + s;

}

/*-----------------------------------------------------

 //������:	DateToDes

 //����  :	v-->��ֵ type-->������Ϣ

 //����  :	���1970������������

 //����ֵ:	�ɹ�-->ʮλ��,��֮-->0

 //˵��  :	����ϵͳ����FORM������Ϊfrm

 //����  :	Create 2002-9-25 of By Dolphin

 ------------------------------------------------------*/

function DateToDes(v, type) {

	if (v != "") {

		pos = v.indexOf("-");

		if (pos != -1 && pos == 4) {

			y = parseInt(v.substring(0, pos));

			v = v.substring(pos + 1, v.length);

		}

		pos = v.indexOf("-");

		if (pos != -1) {

			m = parseInt(v.substring(0, pos));

			v = v.substring(pos + 1, v.length);

		}

		if (v.length > 0)

			d = parseInt(v);

		if (type == "start")

			dt = new Date(m + "/" + d + "/" + y);

		else

			dt = new Date(m + "/" + d + "/" + y);

		var s = dt.getTime();

		return s / 1000;

	}

	else

		return 0;

}

// ----------------------��������-----------------------

/*-----------------------------------------------------

 //������:	strLen

 //����  :	s-->��ֵ 

 //����  :	�����ַ������ȣ���Ӣ�ģ������s����

 //����ֵ:	�ɹ�-->ʮλ��

 //˵��  :	����ϵͳ����FORM������Ϊfrm

 //����  :	Create 2002-9-25 of By Dolphin

 ------------------------------------------------------*/

function strLen(s) {

	var i, str1, str2, str3, nLen;

	str1 = s.value;

	nLen = 0;

	for (i = 1; i <= str1.length; i++) {

		str2 = str1.substring(i - 1, i);

		str3 = escape(str2);

		if (str3.length > 3) {

			nLen = nLen + 2;

		} else {

			nLen = nLen + 1;

		}

	}

	return nLen;

}

// ----------------------��������-----------------------

/*-----------------------------------------------------

 //������:	Replace

 //����  :	s1-->��ֵ s2-->���滻 s3-->�滻

 //����  :	�滻�ַ���

 //����ֵ:	�ɹ�-->����滻���ַ���

 //˵��  :	����ϵͳ����FORM������Ϊfrm

 //����  :	Create 2002-9-25 of By Dolphin

 ------------------------------------------------------*/

function Replace(s1, s2, s3) {

	var len1, len2, i;

	var str1, str2;

	str1 = s1;

	len1 = str1.length;

	len2 = s2.length;

	if (len2 > len1)

		return s1;

	for (i = 1; i <= len1 - len2 + 1; i++) {

		str2 = str1.substring(i - 1, i - 1 + len2);

		if (str2 == s2) {

			str1 = str1.substring(0, i - 1) + s3
					+ str1.substring(i + len2 - 1, len1);

			i = 0;

			len1 = str1.length;

		}

	}

	return str1;

}

// ----------------------��������-----------------------

/*-----------------------------------------------------

 //������:	Pos

 //����  :	s1-->��ֵ s2-->����λ�ַ�

 //����  :	��λs2�ַ���s1�е�λ��

 //����ֵ:	�ɹ�-->�����λλ��

 //˵��  :	����ϵͳ����FORM������Ϊfrm

 //����  :	Create 2002-9-25 of By Dolphin

 ------------------------------------------------------*/

function Pos(s1, s2) {

	var len1, len2, i, flag;

	var str1, str2;

	str1 = s1;

	len1 = str1.length;

	len2 = s2.length;

	if (len2 == 0 || len1 == 0)

		return 0;

	flag = false;

	if (len2 > len1)

		return s1;

	for (i = 1; i <= len1 - len2 + 1; i++) {

		str2 = str1.substring(i - 1, i - 1 + len2);

		if (str2 == s2) {

			flag = true;

			break;

		}

	}

	if (flag)

		return i;

	else

		return 0;

}

// ----------------------��������-----------------------

