$(document).ready(function(){ 
	var cityId = $.trim($("select[@name='cityId']").val());
	var repair_vendor_id = $.trim($("select[@name='repair_vendor_id']").val());
    var starttime = $.trim($("input[@name='starttime']").val());
    var endtime = $.trim($("input[@name='endtime']").val());
    var batchNum = $.trim($("select[@name='batchNum']").val());
    var searchType = $.trim($("input[@name='searchType']").val());
	 $.ajax({  
		 	type:'POST',    
            url: "itms/resource/devRepairTestInfo!getUseRatePieData.action", 
            data:{                    
            	repair_vendor_id: repair_vendor_id, 
            	cityId : cityId,
	        	starttime: starttime,  
	        	endtime : endtime,
	        	batchNum : batchNum,
	        	searchType : searchType
	        }, 
            dataType: "json",
            success:function(data){ 
            	var calcuable = false;
            	var title = "";
            	if(data.total == 0){
            		calcuable = true;
            		title = "sorry,未取到数据！";
            	}
            	var myCharts = echarts.init(document.getElementById('useRateCharts'));
            	var option = {
        			color: ['#ffac2c','#a8c6f0','#69abe7','#9555b4','#f0960e','#60d22e','#ff7f50','#87cefa','#da70d6','#32cd32','#6495ed',
    			            '#ff69b4','#ba55d3','#cd5c5c','#ffa500','#40e0d0',
    			            '#1e90ff','#ff6347','#7b68ee','#00fa9a','#ffd700',
    			            '#6699FF','#ff6666','#3cb371','#b8860b','#30e0e0'],
        			title: {
    			        text: title,
    			        x: 'center',
    			        y: 'center',
    			        textStyle : {
    			            color : 'red',//rgba(30,144,255,0.8)
    			            fontFamily : '微软雅黑',
    			            fontSize : 12,
    			            fontWeight : 'bolder'
    			        }
    			    },
            		tooltip : {
            			trigger : 'item',
            			formatter : "{b}:{d}%"
            		},
            		calculable : calcuable,
            		series : [ {
            			name : '使用率',
            			type : 'pie',
            			radius : '65px',
            			center : [ '50%', '50%' ],
            			roseType : '0px',
            			itemStyle : {
            				normal : {
            					label : {
            						position : 'inner',
            						formatter : '{b}\n({d}%)'
            					},
            					labelLine : {
            						show : false
            					}
            				},
            				emphasis : {
            					label : {
            						show : true
            					,
            					 formatter : "{b}\n{d}%"
            					}
            				}
            			},
            			data : data.use
            		} ]
            	};
        		myCharts.setOption(option);
            	$("#useRatePie").html(data.total);
            },
            error:function (XMLHttpRequest, textStatus, errorThrown) {
            	// 通常 textStatus 和 errorThrown 之中
                // 只有一个会包含信息
               //alert(textStatus); // 调用本次AJAX请求时传递的options参数
               //alert(errorThrown);
            }
        });  
}); 