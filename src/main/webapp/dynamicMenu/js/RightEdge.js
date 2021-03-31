/*******************************************\
  游戏人对联广告类(2006-06-14)
  This JavaScript was writen by Dron.
  @2003-2008 Ucren.com All rights reserved.
\*******************************************/

if (!VJBox.Class.webUI) VJBox.Class.webUI = {};

VJBox.Class.webUI.RightEdge = function (uid)
{
	this.obj = $(uid);
	this.oLeft = this.obj.offsetLeft + 212;
	this.oTop = this.obj.offsetTop + 25;
	this.start = function ()
	{
		var timeout,oldLeft,newLeft,leftC,oldTop,newTop,topC,obj,m;
		timeout = 500;
		
		oldLeft = parseInt(this.obj.style.left,10);
		newLeft = document.body.scrollLeft + this.oLeft;
		leftC = Math.ceil(Math.abs(newLeft-oldLeft)/20);
		if (newLeft != oldLeft)
		{
			this.obj.style.left = oldLeft + leftC * (newLeft>oldLeft?1:-1);
			timeout = 10;
		}

		oldTop = parseInt(this.obj.style.top,10);
		newTop = document.body.scrollTop + this.oTop;
		topC = Math.ceil(Math.abs(newTop-oldTop)/20);
		if(newTop != oldTop)
		{
			this.obj.style.top = oldTop + topC * (newTop>oldTop?1:-1);
			timeout = 10;
		}
		m = this;
		setTimeout(function(){m.start()},this.timeout);
	}
	this.start();
}