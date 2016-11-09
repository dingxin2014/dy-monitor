function bill($scope, $http, $timeout, $rootScope, $location,DuiValidateService) {
	$rootScope.currentType = "bill";
	$rootScope.showMenu = true;
	$scope.init = {
		totalCount: 0 //总页码数
	};

	$scope.searchQuery = {
		pageNo: 1,
		pageSize: 20,
		bankCode: '',
		billNumber: '',
		payDateFrom: '',
		payDateTo: '',
		serialNumber: '',
		receiptBankAccountName: '',
		receiptBankAccount: ''
	};

	$scope.listData = function () {
		$http.get("/bill/paginateBankBillList",
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

	//获取电子回单pdf
	$scope.queryFile = function (idx, virtualPath) {
			window.open(pdfUrl+virtualPath);
	}
}

