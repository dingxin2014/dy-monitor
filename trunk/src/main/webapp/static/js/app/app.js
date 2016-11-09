

var eReceipt = angular.module('eReceipt', ['dui.component.validate', 'dui.directive','dui.component.autocomplete','dui.component.pagination', 'dui.component.searchlist', 'dui.component.datepicker','dui.directive','dui.component.upload']);


eReceipt.run(['$anchorScroll', function($anchorScroll) {
    $anchorScroll.yOffset = 0;      // always scroll by 50 extra pixels
}])
eReceipt.run(function ($rootScope, $location, DuiValidateService) {
	$rootScope.searchQuery={pageNo:1,pageSize:20};
	$rootScope.$on('$routeChangeSuccess', function (event, current, previous) {
		$rootScope.searchQuery={pageNo:1,pageSize:20};
        //路由变更
    	if(previous && previous != undefined && previous != "undefined"){
    		$rootScope.title = current.$$route.title;
    	}
    });

    //添加自定义规则
	DuiValidateService.addRule('number10', {
		validate: function(val){
			if(/^(-)?\d+(\.\d{1,2})?$/.test(val)){
				return true;
			}
			return false;
		},
		message: '必须为数字且最多只有两位小数'
	});
	


});