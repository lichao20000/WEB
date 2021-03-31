/*
 * 超级权限操作记录日志公用方法
 * 采用ajax方法将超级权限操作日志内容记录到t_log_authoper表中;
 * 该用jquery ajax，故依赖jquery.js
 * 
 * @param authCode 超级权限编码，不能为空
 * @param operDesc 记录日志内容，如果为空，则使用默认日志内容
 */
function superAuthLog(authCode, operDesc) {
	var url = "/itms/itms/resource/logSuperPowerManage!addSuperAuthLog.action";
	$.post(url, {
		authCode : authCode,
		// 如果没有传入operDesc参数，该值为undefined,重置为空字符串
		operDesc : operDesc ? operDesc : ''
	});
}

/*-----------------------------------------------------

 //函数名:	IsNull

 //参数  :	strValue-->表单值,strMsg-->提示信息

 //功能  :	判断不能为空

 //返回值:	成功-->True,反之-->False

 //说明  :	公用系统函数FORM的名称为frm

 //描述  :	Create 2002-9-19 of By Dolphin

 ------------------------------------------------------*/

function IsNull(strValue, strMsg) {

	if (Trim(strValue).length > 0)
		return true;

	else {

		alert(strMsg + '不能为空');

		return false;

	}

}

// ----------------------函数结束-----------------------

/*-----------------------------------------------------

 //函数名:	IsNumber

 //参数  :	strValue-->表单值,strMsg-->提示信息

 //功能  :	判断是否为数字

 //返回值:	成功-->True,反之-->False

 //说明  :	公用系统函数FORM的名称为frm

 //描述  :	Create 2002-9-19 of By Dolphin

 ------------------------------------------------------*/

function IsNumber(strValue, strMsg) {

	var bz = true;

	if (IsNull(strValue, strMsg)) {

		for ( var i = 0; i < strValue.length; i++) {

			var ch = strValue.substring(i, i + 1);

			if (ch < '0' || ch > '9') {

				alert(strMsg + '应为数字');

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

			alert(strMsg + '应为数字');

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

// ----------------------函数结束-----------------------

/*-----------------------------------------------------

 //函数名:	IsMail

 //参数  :	strValue-->表单值,strMsg-->提示信息

 //功能  :	判断是否为E-Mail格式

 //说明  :	公用系统函数

 //返回值:	成功-->True,反之-->False

 //描述  :	Create 2002-9-19 of By Dolphin

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

			alert(strMsg + '格式不正确');

			return false;

		}

	}

	else

		return false;

}

// ----------------------函数结束-----------------------

/*-----------------------------------------------------

 //函数名:	Trim

 //参数  :	strValue-->字符串值

 //功能  :	去除字符串值两边的空格

 //返回值:	去除两边的空格字符串值

 //说明  :	公用系统函数

 //描述  :	Create 2002-9-19 of By Dolphin

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

// ----------------------函数结束-----------------------

/*-----------------------------------------------------

 //函数名:	IsDate

 //参数  :	strValue-->表单值

 //功能  :	判断日期格式

 //返回值:	成功-->True,反之-->False

 //说明  :	公用系统函数FORM的名称为frm

 //描述  :	Create 2002-9-19 of By Dolphin

 ------------------------------------------------------*/

function IsDate(strValue) {

	var v = strValue;

	if (!IsNull(v, "日期"))
		return false;

	if (isNaN(Date.parse(v))) {

		alert("日期格式不符合，正确格式是：月-日-年");

		return false;

	}

	var pos = v.indexOf("-");

	if (pos == -1) {

		alert("日期格式不符合，正确格式是：月-日-年");

		return false;

	}

	tmpStr1 = v.substring(0, pos);

	if (tmpStr1.length > 2 || parseInt(tmpStr1) > 12) {

		alert("日期格式不符合，正确格式是：月-日-年");

		return false;

	}

	v = v.substring(pos + 1, v.length);

	pos = v.indexOf("-");

	if (pos == -1) {

		alert("日期格式不符合，正确格式是：月-日-年");

		return false;

	}

	tmpStr2 = v.substring(0, pos);

	if (tmpStr2.length > 2 || parseInt(tmpStr2) > 31) {

		alert("日期格式不符合，正确格式是：月-日-年");

		return false;

	}

	return true;

}

// ----------------------函数结束-----------------------

/*-----------------------------------------------------

 //函数名:	IsChose

 //参数  :	strValue-->表单值 strMsg-->提示信息

 //功能  :	判断是否选择

 //返回值:	成功-->True,反之-->False

 //说明  :	公用系统函数FORM的名称为frm

 //描述  :	Create 2002-9-25 of By Dolphin

 ------------------------------------------------------*/

function IsChose(strValue, strMsg) {

	if (parseInt(strValue) > 0)
		return true;

	else {

		alert(strMsg + "必须选择");

		return false;

	}

}

function IsChose1(strValue, strMsg) {

	if (strValue != 0)
		return true;

	else {

		alert(strMsg + "必须选择");

		return false;

	}

}

// ----------------------函数结束-----------------------

function IsMask(mask) {

	var msg = "掩码";

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

			alert(msg + "格式不符合");

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

 //函数名:	DateToDes

 //参数  :	v-->表单值 type-->类型信息

 //功能  :	输出1970年以来的秒数

 //返回值:	成功-->十位数,反之-->0

 //说明  :	公用系统函数FORM的名称为frm

 //描述  :	Create 2002-9-25 of By Dolphin

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

// ----------------------函数结束-----------------------

/*-----------------------------------------------------

 //函数名:	strLen

 //参数  :	s-->表单值 

 //功能  :	测试字符串长度（中英文），输出s长度

 //返回值:	成功-->十位数

 //说明  :	公用系统函数FORM的名称为frm

 //描述  :	Create 2002-9-25 of By Dolphin

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

// ----------------------函数结束-----------------------

/*-----------------------------------------------------

 //函数名:	Replace

 //参数  :	s1-->表单值 s2-->待替换 s3-->替换

 //功能  :	替换字符串

 //返回值:	成功-->输出替换后字符串

 //说明  :	公用系统函数FORM的名称为frm

 //描述  :	Create 2002-9-25 of By Dolphin

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

// ----------------------函数结束-----------------------

/*-----------------------------------------------------

 //函数名:	Pos

 //参数  :	s1-->表单值 s2-->待定位字符

 //功能  :	定位s2字符在s1中的位置

 //返回值:	成功-->输出定位位置

 //说明  :	公用系统函数FORM的名称为frm

 //描述  :	Create 2002-9-25 of By Dolphin

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

// ----------------------函数结束-----------------------

