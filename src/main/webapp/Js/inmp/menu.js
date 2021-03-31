// JavaScript Document

// 树状菜单
$(document).ready(function(){
$(".l1").toggle(function(){
$(".slist").animate({height: 'toggle', opacity: 'hide'}, "slow");
$(this).next(".slist").animate({height: 'toggle', opacity: 'toggle'}, "slow");
},function(){
$(".slist").animate({height: 'toggle', opacity: 'hide'}, "slow");
$(this).next(".slist").animate({height: 'toggle', opacity: 'toggle'}, "slow");
});
$(".l2").toggle(function(){
$(".sslist").animate({height: 'toggle', opacity: 'hide'}, "slow");
$(this).next(".sslist").animate({height: 'toggle', opacity: 'toggle'}, "slow");
},function(){
$(".sslist").animate({height: 'toggle', opacity: 'hide'}, "slow");
$(this).next(".sslist").animate({height: 'toggle', opacity: 'toggle'}, "slow");
});
$(".l3").toggle(function(){
$(".ssslist").animate({height: 'toggle', opacity: 'hide'}, "slow");
$(this).next(".ssslist").animate({height: 'toggle', opacity: 'toggle'}, "slow");
},function(){
$(".ssslist").animate({height: 'toggle', opacity: 'hide'}, "slow");
$(this).next(".ssslist").animate({height: 'toggle', opacity: 'toggle'}, "slow");
});
$(".l4").toggle(function(){
$(".sssslist").animate({height: 'toggle', opacity: 'hide'}, "slow");
$(this).next(".sssslist").animate({height: 'toggle', opacity: 'toggle'}, "slow");
},function(){
$(".sssslist").animate({height: 'toggle', opacity: 'hide'}, "slow");
$(this).next(".sssslist").animate({height: 'toggle', opacity: 'toggle'}, "slow");
});
$(".l1").click(function(){
$(".l1").removeClass("currentl1");
$(this).addClass("currentl1");
});
$(".l2").click(function(){
$(".l2").removeClass("currentl2");
$(this).addClass("currentl2");
});
$(".secondMenulist").click(function(){
$(".secondMenulist").removeClass("secondMenulist_current");
$(this).addClass("secondMenulist_current");
});
$(".l3").click(function(){
$(".l3").removeClass("currentl3");
$(this).addClass("currentl3");
});
$(".l4").click(function(){
$(".l4").removeClass("currentl4");
$(this).addClass("currentl4");
});
$(".l5").click(function(){
$(".l5").removeClass("currentl5");
$(this).addClass("currentl5");
});
$(".listsl3").click(function(){
$(".listsl3").removeClass("listsl3_current");
$(this).addClass("listsl3_current");
});
$(".listsl4").click(function(){
$(".listsl4").removeClass("listsl4_current");
$(this).addClass("listsl4_current");
});
// 菜单隐藏与显示
$("#menuButtton").click(function(){
	var menut=$(".menu");
	if(menut.is(":hidden")==true)
		{
			menut.show();
			$(this).addClass("menuShow")
			$("#main").addClass("main_open");
		}
		else
		{
			menut.hide();
			$(this).removeClass("menuShow");
			$("#main").removeClass("main_open");
		}		
});
});