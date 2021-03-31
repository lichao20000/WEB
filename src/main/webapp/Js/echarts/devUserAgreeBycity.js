$(document)
		.ready(
				function() {
					var cityId = $.trim($("select[@name='cityId']").val());
					var userSpecId = $.trim($("select[@name='userSpecId']").val());
					var deviceSpecId = $.trim($("select[@name='deviceSpecId']").val());
					var starttime = $.trim($("input[@name='starttime']").val());
					var endtime = $.trim($("input[@name='endtime']").val());

					$
							.ajax({
								type : 'POST',
								url : "itms/resource/devUserAgreeRate!DevUserAgreeRate.action",
								data : {
									userSpecId : userSpecId,
									deviceSpecId : deviceSpecId,
									cityId : cityId,
									starttime : starttime,
									endtime : endtime
								},
								dataType : "json",
								success : function(data) {
									if (data.cities.length > 0) {
										var myChartsBar = echarts.init(document
												.getElementById('bar'));
										var myChartsPie = echarts.init(document
												.getElementById('Pie'));
										var cities = data.cities;// [
										var total = data.total;
										var agree = data.agree;
										var disAgree = data.disAgree;

										var agreeList = [];
										var disAgreeList = [];
										var agreemp = 0;
										var disAgreemp = 0;
										var agreeRate = 0;
										var disAgreeRate = 0;
										for ( var i = 0; i < cities.length; i++) {
											if (i == 0) {
												if (parseInt(total[0]) == 0) {
													agreeRate = 100;
													disAgreeRate = 0;

												} else {
													agreeRate = Math
															.round((parseInt(agree[i])
																	/ parseInt(total[i])
																	* 10000).toFixed(2)) / 100;
													disAgreeRate = 100 - agreeRate;
												}
											}
											if (parseInt(total[i]) == 0) {
												agreemp = 0;
												disAgreemp = 0;
												agreeList.push(agreemp);
												disAgreeList.push(disAgreemp);
											} else {
												agreemp = Math
														.round((parseInt(agree[i])
																/ parseInt(total[i])
																* 10000).toFixed(2)) / 100;
												disAgreemp = 100 - agreemp;
												agreeList.push(agreemp);
												disAgreeList.push(disAgreemp);
											}

										}

										var title = "";
										var showList = [];
										var agreeshow = null;
										var disAgreeshow = null;
										var labelTop = {
											normal : {
												label : {
													show : true,
													position : 'center',
													formatter : '{b}',

													textStyle : {
														baseline : 'bottom',
														color : '#4985c9',
														fontSize : 12
													}
												},
												labelLine : {
													show : false
												}
											}
										};
										var labelFromatter = {
											normal : {
												label : {
													formatter : function(params) {
														return (100 - params.value)
																.toFixed(2)
																+ '%';
													},
													textStyle : {
														baseline : 'top',
														color : '#4985c9',
														fontSize : 24
													}
												}
											}
										};
										var labelBottom = {
											normal : {
												color : '#ccc',
												label : {
													show : true,
													position : 'center'
												},
												labelLine : {
													show : false
												}
											},
											emphasis : {
												color : 'rgba(0,0,0,0)'
											}
										};

											if (cityId == "00") {
												title = "本省各地市终端和用户规格一致率";
											} else {
												title = "本市各地区终端和用户规格一致率";
											}
											showList = agreeList;
											// showList 柱状图中间显示哪个
											disAgreeshow = labelBottom;
											agreeshow= labelTop;

										// >90%
										var rateHigh = [];
										// <90%
										var rateLow = [];
										for ( var i = 0; i < showList.length; i++) {
											if (showList[i] >= 90) {
												rateHigh.push(showList[i]);
												rateLow.push('-');
											} else {
												rateHigh.push('-');
												rateLow.push(showList[i]);
											}
										}
										var optionBar = {
											color : [ '#FF7F50', '#87CEFA' ],
											title : {
												text : title,
												x : 'center',
												textStyle : {
													fontSize : 18,
													fontWeight : 'normal',
													color : '#4985c9'
												}
											},
											tooltip : {
												trigger : 'item',
												formatter : function(params) {
													var index = -1;
													for ( var j = 0; j < cities.length; j++) {
														if (params[1] == cities[j]) {
															index = j;
														}
													}
													if (index > -1) {
														return "匹配用户数: "
																+ agree[index]
																+ "<br>"
																+ "不匹配用户数: "
																+ disAgree[index]
																+ "<br>"
																+ "总开户数: "
																+ total[index];
													} else {
														return params[1] + ": "
																+ params[2]
																+ ": "
																+ params[3]
																+ ": "
																+ params[4]
																+ ": "
																+ params[5]
																+ " %";
													}

												}
											},
											legend : {
												x : 'center',
												y : 'bottom',
												data : [ '<90%', '>90%' ]
											},
											grid : {
												x : '60',
												y : '60',
												x2 : '150',
												y2 : '70',
												borderColor : '#EFEFEF'
											},
											xAxis : [ {
												type : 'category',
												data : cities,
												axisLabel : {
													interval : 0
												}
											} ],
											yAxis : [ {
												type : 'value',
												splitLine : {
													lineStyle : {
														color : '#EFEFEF'
													}
												},
												axisLabel : {
													show : true,
													formatter : '{value} %'
												},
												splitArea : {
													show : true,
													areaStyle : {
														color : [ '#F2f2f2',
																'#FFFFFF'

														]
													}
												}

											// ,
											// max : 100
											} ],

											series : [ {
												name : '>90%',
												type : 'bar',
												barWidth : 30,
												stack : '终端和用户规格一致率',
												itemStyle : {
													normal : {
														label : {
															show : true,

															formatter : '{c} %'
														}
													}
												},
												data : rateHigh
											}, {
												name : '<90%',
												type : 'bar',
												barWidth : 30,
												stack : '终端和用户规格一致率',
												itemStyle : {
													normal : {
														label : {
															show : true,
															formatter : '{c} %'
														}
													}
												},
												data : rateLow
											}

											]
										};

										var optionPie = {
											color : [ '#5AB1EF' ],
											tooltip : {
												formatter : function(params) {
													return params.name + ' '
															+ params.value
															+ '%';
												}
											},
											series : [ {
												type : 'pie',
												center : [ '30%', '58%' ],
												radius : [ '40%', '55%' ],
												x : '0%',
												itemStyle : labelFromatter,
												data : [ {
													name : '终端和用户规格一致率',
													value : agreeRate,
													itemStyle : agreeshow
												}, {
													name : '终端和用户规格不一致率',
													value : disAgreeRate,
													itemStyle : disAgreeshow
												} ]
											} ]
										};
										myChartsPie.setOption(optionPie);
										myChartsBar.setOption(optionBar);

									}
								},
								error : function(XMLHttpRequest, textStatus,
										errorThrown) {
									// alert(textStatus); //
									// 调用本次AJAX请求时传递的options参数
									// alert(errorThrown);
								}
							});
				});