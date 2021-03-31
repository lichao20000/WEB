
(function ($) {
/*
*author:陈仲民 (5243) 2007-12-12
*对输入框的内容进行限制，默认情况下允许输入字母和数字，不允许输入特殊符号；并且不许输入中文；禁止复制粘贴
*param p:json对象
*例如： {allow:"."}:允许输入点号  {ichars:"."}:不允许输入点号  {nocaps:true}:不允许大写字母   {allcaps:true}:不允许小写字母  
*/
	$.fn.alphanumeric = function (p) {
		//默认取值，不允许各种特殊符号
		p = $.extend({ichars:"!@#$%^&*()+=[]\\';,/{}|\":<>?~`.- ", nchars:"", allow:""}, p);
		return this.each(function () {
			
			//将禁止的符号合成数组
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
			
			//定义按键触发的事件，判断按键是否允许输入；复制的快捷键也屏蔽
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
			
			//禁止右键菜单
			$(this).bind("contextmenu", function () {
				return false;
			});
			
			//限制中文输入
			$(this).css("ime-mode","disabled");
		});
	};
/*
*author:陈仲民 (5243) 2007-12-12
*对输入框的内容进行限制，默认情况下允许输入数字，不允许输入字母和特殊符号；并且不许输入中文
*param p:json对象
*例如： {allow:"."}:允许输入点号 
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
*author:陈仲民 (5243) 2007-12-12
*对输入框的内容进行限制，默认情况下允许输入字母，不允许输入数字和特殊符号；并且不许输入中文
*param p:json对象  
*例如：{allow:"."}:允许输入点号 
*/
	$.fn.alpha = function (p) {
		var nm = "1234567890";
		p = $.extend({nchars:nm}, p);
		return this.each(function () {
			$(this).alphanumeric(p);
		});
	};
/*
*author:陈仲民 (5243) 2007-12-12
*对输入框的内容进行限制，默认情况下允许输入数字和点号，供输入ip使用
*/
	$.fn.ipInput = function () {
		$(this).numeric({allow:"."});
	};
/*
*author:陈仲民 (5243) 2007-12-12
*对输入框的内容进行限制，默认情况下允许输入数字和点号，供输入ip使用
*/
	$.fn.emailInput = function () {
		$(this).alphanumeric({allow:".@"});
	};
})(jQuery);

