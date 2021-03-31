function barBuss(year2,month2,flag){
	 $.ajax({  
            type:"POST",  
            url:"../itms/inmp/homePageData!getBussBarData.action" + "?year=" + year2 + "&month=" + month2 + "&sort=" + flag,  
            dataType:"json",
            success:function(data){  
            	var bussLow = 0;
	        	var bussHigh = 0;
	        	
            	if(data.cities.length > 0){
            		var myChartsBar2 = echarts.init(document.getElementById('columechat2'));
            		var cities = data.cities;
            		var avg = data.total;
            		var value = data.value;
            		var rateHigh = [];
					var rateLow = [];
					for ( var i = 0; i < avg.length; i++) {
						var tmp = avg[i];
						if (tmp >= 90) {
							bussHigh++;
							rateLow.push("-");
							rateHigh.push(tmp);
						} else {
							bussLow++;
							rateLow.push(tmp);
							rateHigh.push('-');
						}
					}
            		var optionBar2 = {
            				grid :{
    		        			width:900
    		        		},
            				color: ['#70c5f1','#0095e7','#ff7f50','#60d22e','#ff7f50','#87cefa','#da70d6','#32cd32','#6495ed',
            			            '#ff69b4','#ba55d3','#cd5c5c','#ffa500','#40e0d0',
            			            '#1e90ff','#ff6347','#7b68ee','#00fa9a','#ffd700',
            			            '#6699FF','#ff6666','#3cb371','#b8860b','#30e0e0'],
            				title : {
            					text : '',//'业务下发成功率',
            					x : 'center',
            					y : 'top'
            				},
            				tooltip : {
            					trigger : 'item',
            					formatter : function(params) {
            						var index;
            						for(var j = 0; j < cities.length; j++){
    		        					if(params[1] == cities[j]){
    		        						index = j;
    		        					}
    		        				}
    		        				
    		        				if (index > -1) {

            							return "宽带一次下发成功率: " + value[index][0] + "% <br>" + "IPTV一次下发成功率: " + value[index][1] + "% <br>" + "VOIP一次下发成功率:" + value[index][2] + "%";
            						} else {
            							return params[1] + ": " + params[2] + " %";
            						}

            					}
            				},
            				legend : {
            					x : 'center',
            					y : 'bottom',
            					data : [ '<90%', '>90%' ]
            				},

            				xAxis : [ {
            					type : 'category',
            					data : cities
            				} ],
            				yAxis : [ {
            					type : 'value',
            					axisLabel : {
            						show : true,
            						formatter : '{value} %'
            					}
//	            					,
//	            					max : 100
            				} ],
            				series : [

  								{
  									name : '>90%',
  									type : 'bar',
  									stack : '业务下发成功率',
  									barWidth : 30,
  									itemStyle : {
  										normal : {
  											label : {
  												show : true,
  												position : 'top',
  												formatter : '{c} %'
  											}
  										}
  									},
  									data : rateHigh
  								}, {
  									name : '<90%',
  									type : 'bar',
  									stack : '业务下发成功率',
  									barWidth : 30,
  									itemStyle : {
  										normal : {
  											label : {
  												show : true,
  												position : 'top',
  												formatter : '{c} %'
  											}
  										}
  									},
  									data : rateLow
  								}

  							]
            			};
            		myChartsBar2.setOption(optionBar2);
            	}
            	
            	$("#bussLow").html(bussLow);
	        	$("#bussHigh").html(bussHigh);
                
            },
            error:function (XMLHttpRequest, textStatus, errorThrown) {
                // 通常 textStatus 和 errorThrown 之中
                // 只有一个会包含信息
//               alert(textStatus); // 调用本次AJAX请求时传递的options参数
//               alert(errorThrown);
            }
        });  
}