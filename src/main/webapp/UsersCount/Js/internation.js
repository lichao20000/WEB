angular.module("roam", []).controller("inprovince",
		function($scope, $http, $interval, $filter) {


			Array.prototype.max = function() {
				return Math.max.apply({}, this)
			}
			Array.prototype.min = function() {
				return Math.min.apply({}, this)
			}
			Array.prototype.unique = function() {
				var res = [];
				var json = {};
				for (var i = 0; i < this.length; i++) {
					if (!json[this[i]]) {
						res.push(this[i]);
						json[this[i]] = 1;
					}
				}
				return res;
			}
			//日期
			$scope.GetDateStr = function(AddDayCount) {
				var dd = new Date();
				dd.setDate(dd.getDate() + AddDayCount);//获取AddDayCount天后的日期
				var y = dd.getFullYear();
				var m = (dd.getMonth() + 1) < 10
						? "0" + (dd.getMonth() + 1)
						: (dd.getMonth() + 1);//获取当前月份的日期，不足10补0
				var d = dd.getDate() < 10 ? "0" + dd.getDate() : dd.getDate();//获取当前几号，不足10补0
				return y + "/" + m + "/" + d;
			};
			$scope.getQueryString = function(name) {
				var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
				var r = window.location.search.substr(1).match(reg);
				if (r != null)
					return decodeURI(r[2]);
				return null;
			};
			$scope.type = $scope.getQueryString('type');
			//创建table
			$scope.creatTable = function() {
				try {

					for (var index in $scope.cityNames) {
						if (typeof $scope.cityNames[index] != 'string') {
							break;
						}
						var obj = $scope.cityName_date[$scope.cityNames[index]];
						$('#dataBody').append('<tr id="' + 'city' + index
								+ '">' + '<td>' + $scope.cityNames[index]
								+ '</td>' + '</tr>');
						for (var i = 0; i < 10; i++) {
							var td = '';
							if (obj != undefined) {
								if (obj[$scope.dates[i]] != undefined) {
									var countall = obj[$scope.dates[i]].countall;
									var netsucc = obj[$scope.dates[i]].netsucc;
									var voipsucc = obj[$scope.dates[i]].voipsucc;
									var iptvsucc = obj[$scope.dates[i]].iptvsucc;
									if (i == 0) {
										td = '<td  >' + '用户数:'
												+ '<font color="green">'
												+ countall + '</font><br />'
												+ '宽带数:' + '<font color="red">'
												+ netsucc + '</font><br />'
												+ '语音数:'
												+ '<font color="black">'
												+ voipsucc + '</font><br />'
												+ 'ITV数 :'
												+ '<font color="blue">'
												+ iptvsucc + '</font>'
												+ '</td>';
									} else {
										td = '<td  >' + '<font color="green">'
												+ countall + '</font><br />'
												+ '<font color="red">'
												+ netsucc + '</font><br />'
												+ '<font color="black">'
												+ voipsucc + '</font><br />'
												+ '<font color="blue">'
												+ iptvsucc + '</font>'
												+ '</td>';
									}

								} else {
									if (i == 0) {
										td = '<td  >' + '用户数:'
												+ '<font color="green">' + '0'
												+ '</font><br />' + '宽带数:'
												+ '<font color="red">' + '0'
												+ '</font><br />' + '语音数:'
												+ '<font color="black">' + '0'
												+ '</font><br />' + 'ITV数 :'
												+ '<font color="blue">' + '0'
												+ '</font>' + '</td>';
									} else {
										td = '<td  ><font color="green">' + '0'
												+ '</font><br />'
												+ '<font color="red">' + '0'
												+ '</font><br />'
												+ '<font color="black">' + '0'
												+ '</font><br />'
												+ '<font color="blue">' + '0'
												+ '</font>' + '</td>';
									}

								}
							} else {
								if (i == 0) {
									td = '<td  >' + '用户数:'
											+ '<font color="green">' + '0'
											+ '</font><br />' + '宽带数:'
											+ '<font color="red">' + '0'
											+ '</font><br />' + '语音数:'
											+ '<font color="black">' + '0'
											+ '</font><br />' + 'ITV数 :'
											+ '<font color="blue">' + '0'
											+ '</font>' + '</td>';
								} else {
									td = '<td  ><font color="green">' + '0'
											+ '</font><br />'
											+ '<font color="red">' + '0'
											+ '</font><br />'
											+ '<font color="black">' + '0'
											+ '</font><br />'
											+ '<font color="blue">' + '0'
											+ '</font>' + '</td>';
								}
							}
							$('#city' + index).append(td);
						}

					}
				} catch (error) {
					console.log(error)
				}
			}
			
			//取得日期
			$scope.getDates = function() {
				$scope.dates = [];
				for (var i = -1; i >= -10; i--) {
					$scope.dates.push($scope.GetDateStr(i));
				}
			}();
			// ----------------取得属地   ----------------start
			$scope.getCityNames = function() {

				$.ajax({
					url : "/itms/itms/resource/UsersCountSS!getCityNames.action",
					type : 'GET', // GET
					async : true, // 或false,是否异步
					dataType : 'json', // 返回的数据格式：json/xml/html/script/jsonp/text
					beforeSend : function(xhr) {

					},
					success : function(data, textStatus, jqXHR) {
						$scope.cityNames = [];
						for (var index in data) {
							var city_name = data[index].city_name;
							if (typeof city_name == 'string') {
								if (city_name == 'max' || city_name == 'min'
										|| city_name == 'unique' || city_name== '省中心') {
									break;
								} else {
									$scope.cityNames.push(city_name);
								}

							}

						}
						//执行
						if ($scope.type == 'static') {
							$scope.getAllData();
							$('#static').show();
							$('#staticTable').show()
						} else if ($scope.type == 'active') {
							$scope.perTenMinutesData();
							$('#active').show();
							$('#runningChart').show();
							var i = 0;
							setInterval(function() {
										$scope.perTenMinutesData();
										console.log(i++);
									}, 600000);
						} else {
							$scope.getAllData();
							$('#static').show();
							$('#staticTable').show()
						}

					},
					error : function(data, xhr, textStatus) {
						console.log(data);
					},
					complete : function() {

					}
				})
			}();
			// ----------------取得总数据----------------start
			$scope.getAllData = function() {

				$.ajax({
							url : "/itms/itms/resource/UsersCountSS!UsersCount.action",
							type : 'GET', // GET
							async : true, // 或false,是否异步							
							dataType : 'json', // 返回的数据格式：json/xml/html/script/jsonp/text
							beforeSend : function(xhr) {

							},
							success : function(data, textStatus, jqXHR) {

								$scope.data = data;
								if (data.length == 0) {

								} else {
									$scope.handleData(data);
								}

							},
							error : function(data, xhr, textStatus) {
								console.log(data);
							},
							complete : function() {

							}
						})
			};

			// ----------------处理总数据--------------start
			$scope.handleData = function(data) {
				$('#headers').append('<th>' +'属地' + '</th>');
				var cityName_date = {};
				for (var index in data) {
					var city_name = data[index].city_name;
					var date_time = data[index].date_time;
					var netsucc = data[index].netsucc;
					var countall = data[index].countall;
					var voipsucc = data[index].voipsucc;
					var iptvsucc = data[index].iptvsucc;

					if (cityName_date[city_name] != undefined
							&& city_name != undefined) {
						cityName_date[city_name][date_time] = {
							'netsucc' : netsucc,
							'countall' : countall,
							'voipsucc' : voipsucc,
							'iptvsucc' : iptvsucc
						};
					} else if (city_name != undefined) {
						cityName_date[city_name] = new Object();
						cityName_date[city_name][date_time] = {

							'netsucc' : netsucc,
							'countall' : countall,
							'voipsucc' : voipsucc,
							'iptvsucc' : iptvsucc

						};
					}

				}
				$scope.cityName_date = cityName_date;
				//创建日期
				
				for (var index in $scope.dates) {
					var head = $scope.dates[index];
					if (typeof head == 'string') {
						
						$('#headers').append('<th>' + head + '</th>');
					}

				}
				//创建table

				$scope.creatTable();
			};
			// -------------取得每10分钟数据-------------start
			$scope.perTenMinutesData = function() {

				$.ajax({
					url : "/itms/itms/resource/UsersCountSS!UsersCountPerMinutes.action",
					type : 'GET', // GET
					async : true, // 或false,是否异
					cache:false,        //这里
                    ifModified :true , //这里
					dataType : 'json', // 返回的数据格式：json/xml/html/script/jsonp/text
					beforeSend : function(xhr) {
						var date = new Date();
						var hours = date.getHours();
						var mins = date.getMinutes();
						var secs = date.getSeconds();
						$scope.recentTime = hours + ':' + mins + ':' + secs;
					},
					success : function(data, textStatus, jqXHR) {

						$scope.dataPerMin = data;
						$scope.activeChart();
						$scope.handleDaraPerMin($scope.dataPerMin);
						var myChart = echarts.init(document.getElementById('activeChart'));
						myChart.setOption($scope.IPSecBar);
                        console.log('set')
					},
					error : function(data, xhr, textStatus) {

					},
					complete : function() {

					}
				})
			};

			// -------------处理每十分钟数据-------------start

			$scope.handleDaraPerMin = function(dataPerMin) {
				var countallArray = [];
				var netsuccArray = [];
				var voipsuccArray = [];
				var iptvsuccArray = [];
				var OBJ = {};
				var cityNames = $scope.cityNames;
				for (var index in cityNames) {
					var flag = true;
					var city_name = cityNames[index];
					if (typeof city_name != 'string' || city_name == 'max'
							|| city_name == 'min' || city_name == 'unique') {
						break;
					}
					for (var index1 = 0; index1 < dataPerMin.length; index1++) {
						var city_name1 = dataPerMin[index1].city_name;
						if (city_name1 == city_name) {
							var countall = dataPerMin[index1].countall;
							var netsucc = dataPerMin[index1].netsucc;
							var voipsucc = dataPerMin[index1].voipsucc;
							var iptvsucc = dataPerMin[index1].iptvsucc;
							countallArray.push(countall);
							netsuccArray.push(netsucc);
							voipsuccArray.push(voipsucc);
							iptvsuccArray.push(iptvsucc);
							flag = false;
							break;
						} else {
							flag = true
						}
					}
					if (flag) {
						countallArray.push('0');
						netsuccArray.push('0');
						voipsuccArray.push('0');
						iptvsuccArray.push('0');
					}

				}
				OBJ['countallArray'] = countallArray;
				OBJ['netsuccArray'] = netsuccArray;
				OBJ['voipsuccArray'] = voipsuccArray;
				OBJ['iptvsuccArray'] = iptvsuccArray;
				console.log(JSON.stringify(countallArray));
				console.log(JSON.stringify(netsuccArray));
				console.log(JSON.stringify(voipsuccArray));
				console.log(JSON.stringify(iptvsuccArray));
				$scope.IPSecBar.series[0].data = countallArray;
				$scope.IPSecBar.series[1].data = netsuccArray;
				$scope.IPSecBar.series[2].data = voipsuccArray;
				$scope.IPSecBar.series[3].data = iptvsuccArray;
				console.log(JSON.stringify($scope.IPSecBar));
				return OBJ;

			};
			// 动态图表
			$scope.activeChart = function() {

				$scope.IPSecBar = {
					title : {
						text : '10分钟新开用户数(' + $scope.recentTime + ')'
					},
					tooltip : {
						trigger : 'axis'
					},
					legend : {
						data : ['用户数', '宽带数', '语音数', 'ITV数']
					},
					grid : {
						left : '3%',
						right : '4%',
						bottom : '3%',
						containLabel : true
					},

					xAxis : {
						type : 'category',
						boundaryGap : true,
						data : $scope.cityNames
					},
					yAxis : {
						type : 'value'
					},
					series : [{
								name : '用户数',
								type : 'line',
								 itemStyle : { normal: {label : {show: true}}},
								data : []
							}, {
								name : '宽带数',
								type : 'line',
								itemStyle : { normal: {label : {show: true}}},
								data : []
							}, {
								name : '语音数',
								type : 'line',
								itemStyle : { normal: {label : {show: true}}},
								data : []
							}, {
								name : 'ITV数',
								type : 'line',
								itemStyle : { normal: {label : {show: true}}},
								data : []
							}]
				};
			};

		});