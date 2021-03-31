$(document).ready(function() {
	var mqId = $.trim($("select[@name='mqId']").val());
	var starttime = $.trim($("input[@name='starttime']").val());
	var endtime = $.trim($("input[@name='endtime']").val());
	var topicName = $.trim($("select[@name='topicName']").val());
	var myChartsLine = echarts.init(document.getElementById('line'));
	var legendName = [];
	function getJsonLength(jsonData) {
		var jsonLength = 0;
		for ( var item in jsonData) {
			jsonLength++;
		}
		return jsonLength;
	}
	$.ajax({
		type : 'POST',
		url : "itms/report/queryMq!getMqEchartsData.action",
		data : {
			mqId : mqId,
			topicName : topicName,
			starttime : starttime,
			endtime : endtime
		},
		dataType : "json",
		success : function(jsons) {
			for ( var key in jsons.data) {
				legendName.push(key);
			}
			var option = {
					title : {
						text : 'mq'
					},
					tooltip : {
						trigger : 'axis'
					},
					legend : {
						data : legendName
					},
					toolbox : {
						feature : {
							saveAsImage : {}
						}
					},
					grid : {
						left : '3%',
						right : '4%',
						bottom : '3%',
						containLabel : true
					},
					xAxis : [ {
						type : 'category',
						position : 'auto',
						boundaryGap : false,
						data : jsons.xcontent
					} ],
					yAxis : [ {
						type : 'value'
					} ],
					series : function() {
						var serie = [];
						for ( var key in jsons.data) {
							var item = {
								name : key,
								type : 'line',
								stack : '总量',
								areaStyle : {
									normal : {}
								},
								data : jsons.data[key]
							}
							serie.push(item);
						}
						;
						return serie;
					}()
				};
			myChartsLine.setOption(option);
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
		}
	});
	// 鍒濇鍔犺浇鍥捐〃(鏃犳暟鎹�
	myChartsLine.setOption(option);
});