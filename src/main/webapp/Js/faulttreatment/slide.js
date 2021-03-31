// JavaScript Document
$(document).ready(function(){
	//绑定
//	$('#btn_bd').click(function(){
//		$('#bd_content').show();
//	})
	$("#btn_msbd").click(function(){
		$("#bd_content").hide();
	});
	/*$('.sta_close').click(function(){
		$(this).parents('.stainfo').hide();
	})*/
	$(".sta_close").click(function(){
		$(this).parents(".stainfo").hide();
		$(".sta_close").hide();
	});
	//业务下发
	$(".give_list").hide();
	$(".give_btn").mouseover(function(){
			$(".give_list").show();
	});
	$(".it_table").mouseout(function(){
			$(".give_list").hide();
	});
});




