function monitor($scope, $http, $timeout, $rootScope, $location,DuiValidateService) {
	$rootScope.currentType = "monitor";
	$rootScope.showMenu = true;
	$scope.init = {
		totalCount: 0 //总页码数
	};
	
	
	$scope.searchQuery = {
			pageNo: 1,
			pageSize: 20
		};

	$scope.listData = function () {
		$http.get("/monitor/paginateMonitorList",
			{params: $scope.searchQuery}
		).success(function (re) {
			if (re.status ==1) {
				$scope.list = re.data.pageList;
				$scope.init.totalCount = re.data.totalCount;
				$scope.paginate = re.data;;
			} else {
				alert(re.message);
			}
		}).error(function () {
			alert('获取信息时服务端报错，请重试！');
		});
	};

	
		$scope.heartBeatListData = function () {
			$http.get("/monitor/paginateMonitorList",
				{params: $scope.searchQuery}
			).success(function (re) {
				if (re.status ==1) {
					$scope.list = re.data.pageList;
					$scope.init.totalCount = re.data.totalCount;
					$scope.paginate = re.data;
					$timeout(function(){
						$scope.heartBeatListData();
					}, 10000);
				} else {
					alert(re.message);
				}
			}).error(function () {
				alert('获取信息时服务端报错，请重试！');
			});
		};
		$scope.heartBeatListData();

	$scope.search = function () {
		$scope.searchQuery.pageNo = 1;
		$scope.listData();
	};


	//监控页码变化执行查询
	$scope.$watch('searchQuery.pageNo + searchQuery.pageSize', function (newVal, oldVal) {
		if (oldVal != newVal) {
			$scope.listData();//执行查询
		}
	});

	//修改或者新增
	$scope.updateItem = function(id){
		$scope.isUpdate=false;
		$scope.id = id;
		$scope.opType = "编辑";
		$scope.monitor = angular.copy($scope.list[id]);
		$scope.showPop = true;
	};

	//确定编辑
	$scope.confirmEdit = function(id){
		if($scope.monitorValidator.validateForm()){
			$http.get("/monitor/ModifyJob",
				{ params: {
					id:$scope.monitor.id,
					appCode:$scope.monitor.appCode,
					appName:$scope.monitor.appName,
					ip:$scope.monitor.ip,
					method:$scope.monitor.method,
					cron:$scope.monitor.cron,
					suspendFlag:$scope.monitor.suspendFlag,
					remark:$scope.monitor.remark}}
			).success(function(data){
				if(data.status == 1){
					$scope.listData();
				}else{
					alert(data.message);
				}
			}).error(function(){
				alert('获取信息时服务端报错，请重试！');
			});
			$scope.showPop = false;
		}
	};

	$scope.deleteItem = function(idx,id,appName,ip,method){
		if(confirm('确定删除ip为'+ip+'方法为'+method+"的作业吗?")){
			$http.get("/monitor/DeleteJob",
				{ params: {id:id}}
			).success(function(data){
				if(data.status == 1){
					$scope.listData();
				}else{
					alert(data.message);
				}
			}).error(function(){
				alert('获取信息时服务端报错，请重试！');
			});

		}
	};

	//显示或隐藏弹层
	$scope.$watch('showPop', function(value){
		if(value){
			showPopupDiv($('#editMonitorInfo'));
		}else{
			$scope.monitorValidator.clearErrors();
			hidePopupDiv($('#editMonitorInfo'));
		}
	});
	
}