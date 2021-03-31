
(function ($) {
/*
*author:������ (5243) 2007-12-12
*�����������ݽ������ƣ�Ĭ�����������������ĸ�����֣�����������������ţ����Ҳ����������ģ���ֹ����ճ��
*param p:json����
*���磺 {allow:"."}:����������  {ichars:"."}:������������  {nocaps:true}:�������д��ĸ   {allcaps:true}:������Сд��ĸ  
*/
	$.fn.alphanumeric = function (p) {
		//Ĭ��ȡֵ������������������
		p = $.extend({ichars:"!@#$%^&*()+=[]\\';,/{}|\":<>?~`.- ", nchars:"", allow:""}, p);
		return this.each(function () {
			
			//����ֹ�ķ��źϳ�����
			if (p.nocaps) {
				p.nchars += "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
			}
			if (p.allcaps) {
				p.nchars += "abcdefghijklmnopqrstuvwxyz";
			}
			s = p.allow.split("");
			for (i = 0; i < s.length; i++) {
				if (p.ichars.indexOf(s[i]) != -1) {
					s[i] = "\\" + s[i];
				}
			}
			p.allow = s.join("|");
			var reg = new RegExp(p.allow, "gi");
			var ch = p.ichars + p.nchars;
			ch = ch.replace(reg, "");
			
			//���尴���������¼����жϰ����Ƿ��������룻���ƵĿ�ݼ�Ҳ����
			$(this).keypress(function (e) {
				if (!e.charCode) {
					k = String.fromCharCode(e.which);
				} else {
					k = String.fromCharCode(e.charCode);
				}
				if (ch.indexOf(k) != -1) {
					e.preventDefault();
				}
				if (e.ctrlKey && k == "v") {
					e.preventDefault();
				}
			});
			
			//��ֹ�Ҽ��˵�
			$(this).bind("contextmenu", function () {
				return false;
			});
			
			//������������
			$(this).css("ime-mode","disabled");
		});
	};
/*
*author:������ (5243) 2007-12-12
*�����������ݽ������ƣ�Ĭ������������������֣�������������ĸ��������ţ����Ҳ�����������
*param p:json����
*���磺 {allow:"."}:���������� 
*/
	$.fn.numeric = function (p) {
		var az = "abcdefghijklmnopqrstuvwxyz";
		az += az.toUpperCase();
		p = $.extend({nchars:az}, p);
		return this.each(function () {
			$(this).alphanumeric(p);
		});
	};
/*
*author:������ (5243) 2007-12-12
*�����������ݽ������ƣ�Ĭ�����������������ĸ���������������ֺ�������ţ����Ҳ�����������
*param p:json����  
*���磺{allow:"."}:���������� 
*/
	$.fn.alpha = function (p) {
		var nm = "1234567890";
		p = $.extend({nchars:nm}, p);
		return this.each(function () {
			$(this).alphanumeric(p);
		});
	};
/*
*author:������ (5243) 2007-12-12
*�����������ݽ������ƣ�Ĭ������������������ֺ͵�ţ�������ipʹ��
*/
	$.fn.ipInput = function () {
		$(this).numeric({allow:"."});
	};
/*
*author:������ (5243) 2007-12-12
*�����������ݽ������ƣ�Ĭ������������������ֺ͵�ţ�������ipʹ��
*/
	$.fn.emailInput = function () {
		$(this).alphanumeric({allow:".@"});
	};
})(jQuery);

