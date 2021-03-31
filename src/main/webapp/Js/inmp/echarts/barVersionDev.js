function barVersionDev(year4,month4,flag){
	 $.ajax({  
         type:"POST",  
         url:"../itms/inmp/homePageData!getVersionDevBarData.action" + "?year=" + year4 + "&month=" + month4 + "&sort=" + flag,    
         dataType:"json",
         success:function(data){
        	var versionDevLow = 0;
        	var versionDevHigh = 0;
         	if(data.cities.length > 0){
         		var myChartsBar4 = echarts.init(document.getElementById('columechat4'));
         		var cities = data.cities;
         		var stand = data.stand; 
         		var nostand = data.nostand; 
         		var totals = data.total;
         		var rateHigh = [];
				var rateLow = [];
				for ( var i = 0; i < stand.length; i++) {
					var tmp = Math.round(stand[i]
							/ totals[i] * 10000) / 100;
					if(totals[i] == 0){
         				tmp = 0;
         			}
					if (tmp >= 90) {
						versionDevHigh++;
						rateHigh.push(tmp);
						rateLow.push('-');
					} else {
						versionDevLow++;
						rateHigh.push('-');
						rateLow.push(tmp);
					}
				}
         		var optionBar4 = {
         				grid :{
		        			width:900
		        		},
         				color: ['#70c5f1','#0095e7','#ff7f50','#60d22e','#ff7f50','#87cefa','#da70d6','#32cd32','#6495ed',
        			            '#ff69b4','#ba55d3','#cd5c5c','#ffa500','#40e0d0',
        			            '#1e90ff','#ff6347','#7b68ee','#00fa9a','#ffd700',
        			            '#6699FF','#ff6666','#3cb371','#b8860b','#30e0e0'],
         				title : {
         					text : '',//'规范比率',
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
         							return "不规范版本终端数: " + nostand[index] + " <br>"
         										+ "&nbsp;&nbsp;&nbsp;&nbsp;规范版本终端数: " + stand[index] + " <br>"
         										+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;终端总数: " + totals[index] ;
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
//         					,
//         					max : 100
         				} ],
         				series : [
  								{
  									name : '>90%',
  									type : 'bar',
  									stack : '规范比率',
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
  								},
  								/*markLine : {	//平均值
  	         		                data : [
  	         		                    {type : 'average', name : '平均值'}
  	         		                ]
  	         		            },*/
  								{
  									name : '<90%',
  									type : 'bar',
  									stack : '规范比率',
  									barWidth : 30,
  									color : '#1e90ff',
  									itemStyle : {
  										normal : {
  											label : {
  												show : true,
  												color : 'rgba(15,35,43,1)',
  												position : 'top',
  												formatter : '{c} %'
  											}
  										}
  									},
  									data : rateLow
  								}

  						]
         			};
         		myChartsBar4.setOption(optionBar4);
         	}
         	$("#versionDevLow").html(versionDevLow);
        	$("#versionDevHigh").html(versionDevHigh);
             
         },
         error:function (XMLHttpRequest, textStatus, errorThrown) {
             // 通常 textStatus 和 errorThrown 之中
             // 只有一个会包含信息
//            alert(textStatus); // 调用本次AJAX请求时传递的options参数
//            alert(errorThrown);
         }
     });
}
