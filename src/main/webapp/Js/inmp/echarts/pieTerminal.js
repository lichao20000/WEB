$(document).ready(function(){  
	 $.ajax({  
            type:"POST",  
            url:"../itms/inmp/homePageData!getPieData.action",  
            dataType:"json",
            data:{
            	report_type : 1
            },
            success:function(data){  
            	var calcuable1 = false;
            	var calcuable2 = false;
            	var calcuable3 = false;
            	
            	var title1 = "";
            	var title2 = "";
            	var title3 = "";
            	
            	if(data[0].total == 0){
            		calcuable1 = true;
            		title1 = "sorry，未取到数据！";
            	}
            	if(data[1].total == 0){
            		calcuable2 = true;
            		title2 = "sorry，未取到数据！";
            	}
            	if(data[2].total == 0){
            		calcuable3 = true;
            		title3 = "sorry，未取到数据！";
            	}
            	var myCharts1 = echarts.init(document.getElementById('gateway1'));
            	var myCharts2 = echarts.init(document.getElementById('gateway2'));
            	var myCharts3 = echarts.init(document.getElementById('gateway3'));

            	var option1 = {
        			color: ['#ffac2c','#a8c6f0','#69abe7','#9555b4','#f0960e','#60d22e','#ff7f50','#87cefa','#da70d6','#32cd32','#6495ed',
    			            '#ff69b4','#ba55d3','#cd5c5c','#ffa500','#40e0d0',
    			            '#1e90ff','#ff6347','#7b68ee','#00fa9a','#ffd700',
    			            '#6699FF','#ff6666','#3cb371','#b8860b','#30e0e0'],
        			title: {
    			        text: title1,
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
            			formatter : "{d}%, {c}" + data[0].unit
            		},

            		calculable : calcuable1,
            		series : [ {
            			name : 'E8-C',
            			type : 'pie',
            			radius : '65px',
            			center : [ '50%', '50%' ],
            			roseType : '0px',
            			itemStyle : {
            				normal : {
            					borderWidth: 1,  
            					label : {
            						position : 'inner',
            						formatter : '{b} : \n{c}' + data[0].unit
            					},
            					labelLine : {
            						show : false
            					}
            					
            				},
            				emphasis : {
            					label : {
            						show : true
            					// ,
            					// formatter : "{b}\n{d}%"
            					}
            				}
            			},
            			data : data[0].E8C
            		} ]
            	};

            	var option2 = {
        			color: ['#ffac2c','#a8c6f0','#69abe7','#9555b4','#f0960e','#60d22e','#ff7f50','#87cefa','#da70d6','#32cd32','#6495ed',
    			            '#ff69b4','#ba55d3','#cd5c5c','#ffa500','#40e0d0',
    			            '#1e90ff','#ff6347','#7b68ee','#00fa9a','#ffd700',
    			            '#6699FF','#ff6666','#3cb371','#b8860b','#30e0e0'],
        			title: {
    			        text: title2,
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
            			formatter : "{d}%, {c}" + data[1].unit
            		},

            		calculable : calcuable2,
            		series : [ {
            			name : 'E8-B',
            			type : 'pie',
            			radius : '65px',
            			center : [ '50%', '50%' ],
            			itemStyle : {
            				normal : {
            					label : {
            						position : 'inner',
            						formatter : '{b} : \n{c}' + data[1].unit
            					},
            					labelLine : {
            						show : false
            					}
            				},
            				emphasis : {
            					label : {
            						show : true
            					// ,
            					// formatter : "{b}\n{d}%"
            					}
            				}
            			},
            			data : data[1].E8B
            		} ]
            	};

            	var option3 = {
        			color: ['#ffac2c','#a8c6f0','#69abe7','#9555b4','#f0960e','#60d22e','#ff7f50','#87cefa','#da70d6','#32cd32','#6495ed',
    			            '#ff69b4','#ba55d3','#cd5c5c','#ffa500','#40e0d0',
    			            '#1e90ff','#ff6347','#7b68ee','#00fa9a','#ffd700',
    			            '#6699FF','#ff6666','#3cb371','#b8860b','#30e0e0'],
        			title: {
    			        text: title3,
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
            			formatter : "{d}%, {c}" + data[2].unit
            		},

            		calculable : calcuable3,
            		series : [ {
            			name : '政企',
            			type : 'pie',
            			radius : '65px',
            			center : [ '50%', '50%' ],
            			itemStyle : {
            				normal : {
            					label : {
            						position : 'inner',
            						formatter : '{b} : \n{c}' + data[2].unit
            					},
            					labelLine : {
            						show : false
            					}
            				},
            				emphasis : {
            					label : {
            						show : true
            					// ,
            					// formatter : "{b}\n{d}%"
            					}
            				}
            			},
            			data : data[2].HW
            		} ]
            	};
            	myCharts1.setOption(option1);
            	myCharts2.setOption(option2);
            	myCharts3.setOption(option3);
            	$("#e8cnumgw").html(data[0].total + data[0].unit);
            	
            	$("#e8bnumgw").html(data[1].total + data[1].unit);
            	
            	$("#hwnumgw").html(data[2].total + data[2].unit);
            	total = parseFloat(data[0].total) + parseFloat(data[1].total) + parseFloat(data[2].total);
            	/*alert(data[0].total);
            	alert(data[1].total);
            	alert(data[2].total);
            	alert(total);*/
            	$("#totalPieTerminal").html(total.toFixed(2));
                
            },
            error:function (XMLHttpRequest, textStatus, errorThrown) {
                // 通常 textStatus 和 errorThrown 之中
                // 只有一个会包含信息
               //alert(textStatus); // 调用本次AJAX请求时传递的options参数
               //alert(errorThrown);
            }
        });  
}); 