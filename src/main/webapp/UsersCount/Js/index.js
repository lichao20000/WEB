$(function(){
    setWidth();
    // $('.J_marquee_bar').liMarquee({direction:'right',hoverstop: false,scrollamount: 14});
    // $('.J_marquee_arrow').liMarquee({direction:'right',hoverstop: false,scrollamount: 30});
    /*tab*/
    svTab('J_tab_info');
    function svTab(tabObj){
        var tabLi=$("#"+tabObj).find(".sv-tab-nav li");
        tabLi.click(function(){
            var tabIndex=$(this).index();
            $(this).addClass("active").siblings().removeClass("active");
            $(".sv-tab-content").hide();
            $("#"+tabObj).find(".sv-tab-content").eq(tabIndex).fadeIn();
        })
    }

    /*只读input*/
    $(".sv-input[readonly='readonly']").addClass("readonly")
    $("#J_searchtype").click(function(event){
    	event.stopPropagation();
        openDrop(this);
    });
    $("#J_select_jk").click(function(){
        if(!$(this).parent().hasClass("readonly")){
            openDrop(this)
        }else{
            return false
        }

    });
    /*查看详情*/
    $(".sv-table-info tr").each(function(index){
        if($(this).index()>5){
            $(this).hide();
        }else{
            $(this).show();
        }
    })
    $(".sv-detail-link").on("click",function(){
        $(this).toggleClass("open");
        if($(this).hasClass("open")){
            $(this).prev().find("tr").fadeIn();
            $(this).html('收起详情 <span class="iconfont icon-arrowdropdown"></span>')
        }else{
            $(this).html('查看详情 <span class="iconfont icon-arrowdropdown"></span>');
            $(".sv-table-info tr").each(function(index){
        if($(this).index()>5){
            $(this).hide();
        }else{
            $(this).show();
        }
    })
        }
    })
    /*成功弹框*/
    $("#J_success_modal_btn").click(function() {
        // swal("192.168.1.0/24",'',"success")
        swal({
          title: "192.168.1.0/24",
          text: "",
          type: "success",
          showCancelButton: false,
          confirmButtonColor: "#ec6941",
          confirmButtonText: "确定",
          // timer: 2e3
        })
    });
    /*未知弹框*/
    $("#J_confirm_modal_btn").click(function(){
        swal({
            title: "",
            text: "确定执行该操作吗？",
            imageUrl: "images/icon-confirm.png",
            showCancelButton: true,
            confirmButtonColor: "#ec6941",
            confirmButtonText: "确定"
        })
    })
    /*警告弹框*/
    $("#J_warn_modal_btn").click(function(){
        swal({
          title: "",
            text: "警告信息",
            imageUrl: "images/icon-warn.png",
            showCancelButton: true,
            confirmButtonColor: "#ec6941",
            confirmButtonText: "确定"
        })
    })
    /*警告弹框*/
    $("#J_error_modal_btn").click(function(){
        swal({
          title: "",
            text: "错误信息",
            imageUrl: "images/icon-error.png",
            showCancelButton: true,
            confirmButtonColor: "#ec6941",
            confirmButtonText: "确定"
        })
    })



});
/*获取宽度*/
function setWidth(){
    var winW=$(window).width();
    var winH=$(window).height();
    if(winW>=1007){
        $(".sv-container").width(1000);
    }else if(winW>=1583){
        $(".sv-container").width(1300);
    }else if(winW>=1903){
        $(".sv-container").width(1440);
    }
}

/*检测中*/
var sec=0;
var loadingLeft=0;
var loadingWrapperWidth=$(".sv-check-loading-wrapper").width();
var timer;
var curTime;
// var loadingTime=checkConfig.timeGap*(checkConfig.checkList.length);/*加载动画总共时间*/

function showItem(num){
    var sobj=$(".sv-check-item").eq(num);
    setTimeout(function(){
        sobj.slideDown();
        var scrollT=$(".sv-check-item").eq(num).offset().top +60;


    },2000*num);


}
/*下拉选择*/
    function openDrop(clickObj){
        var dropmenu=$(clickObj).next();
        dropmenu.slideToggle();
        dropmenu.parent().toggleClass("open");
        var selTxt;
        dropmenu.find("li").each(function(){
            $(this).click(function(){
                $(this).addClass("active").siblings().removeClass("active");
                selTxt=$(this).text();
                $(clickObj).find(".sv-select-text").text(selTxt).prev().val(selTxt);
                $(this).parent().parent().slideUp();
                dropmenu.parent().removeClass("open");
            })
        });
    }
/*滚动到指定位置*/
function scrollToP(obj){
$("html,body").animate({scrollTop:obj.offset().top},1000)
}
/*重新检测*/
function reCheck(){
   $("#J_check_result").hide();
   $(".sv-check-result-btnarea").hide();
   $(".sv-check-topo-smallNOde-wraper").removeClass("normal danger weizhi");
    $("#J_online_search").addClass("checking");
   AutoCheck.run(checkConfig);
   // $("#J_start_check").show().css({"opacity":"1","left":"50%"});
}