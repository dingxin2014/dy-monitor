function register($scope, $http, $timeout, $rootScope, $location,DuiValidateService) {
	$rootScope.currentType = "register";
	$rootScope.showMenu = true;
	$scope.init = {
		totalCount: 0 //总页码数
	};

	$scope.searchQuery = {
		pageNo: 1,
		pageSize: 20
	};

	$scope.listData = function () {
		$http.get("/register/paginateRegisterList",
			{params: $scope.searchQuery}
		).success(function (re) {
			if (re.status == 1) {
				$scope.list = re.data.pageList;
				$scope.init.totalCount = re.data.totalCount;
				$scope.paginate = re.data;
			} else {
				alert(re.message);
			}
		}).error(function () {
			alert('获取信息时服务端报错，请重试！');
		});
	};
	$scope.listData();

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
		$scope.id = id;
		$scope.opType = "新增";
		if (id > -1){
			$scope.opType = "编辑";
			$scope.register = angular.copy($scope.list[id]);
		} else {
			$scope.register ={};
		}
		$scope.showPop = true;
	};

	//确定编辑
	$scope.comfirmEdit = function(id){
		if($scope.registerValidator.validateForm()){
			$http.get("/register/registerAddOrUpdate",
				{ params: {ipAddress:$scope.register.ipAddress,
					id:$scope.register.id,
					appCode:$scope.register.appCode,
					appName:$scope.register.appName,
					status:$scope.register.status,
					remark:$scope.register.remark}}
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

	//显示或隐藏弹层
	$scope.$watch('showPop', function(value){
		if(value){
			showPopupDiv($('#editRegisterInfo'));
		}else{
			$scope.registerValidator.clearErrors();
			hidePopupDiv($('#editRegisterInfo'));
		}
	});

}

