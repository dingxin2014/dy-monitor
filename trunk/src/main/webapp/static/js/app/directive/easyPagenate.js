
/*
 * 翻页指令
 *
 */
eReceipt.directive('easyPagenate', function($timeout, $http){
    return {
        restrict: 'EA',
        scope:{
            pageSize: '@',
            partItem: '='
        },
        link: function(scope, el, attrs){

            scope.$parent.testResult.then(function(){

                // 初始数据
                scope.currentPage = 1;
//                scope.pageCount = Math.ceil((scope.partItem.earnBranchCount+ scope.partItem.lossBranchCount)/scope.pageSize);
                scope.pageCount = Math.ceil(scope.partItem.pageCount/scope.pageSize);
                scope.loading = false;
                // 获得父Controller已有搜索参数
                var params = angular.copy(scope.$parent.profitParams);

                // 作用域方法
                scope.nextPage = function(){
                    if(scope.currentPage < scope.pageCount && scope.validatePageNum()){
                        scope.currentPage ++;
                        partRefresh();
                    }
                }
                scope.prevPage = function(){
                    if(scope.currentPage > 1 && scope.validatePageNum()){
                        scope.currentPage --;
                        partRefresh();
                    }
                }
                scope.validatePageNum = function(){
                    return /^\d+$/.test(scope.currentPage) && scope.currentPage <= scope.pageCount;
                }
                // 按Enter翻页
                el.find(".pagenums input").bind("keyup", function (event){
                    if(event.which === 13 && scope.validatePageNum()) {
                        partRefresh();
                        event.preventDefault();
                    }
                });

                // 间隔300毫秒发送请求并局部刷新
                var delayTimeout;
                function partRefresh(){
                    if(delayTimeout){
                        $timeout.cancel(delayTimeout);
                    }
                    delayTimeout = $timeout(function(){
                        scope.loading = true;
                        angular.extend(params, {
                            pageNo: scope.currentPage,
                            roleName: scope.partItem.positionName
                        });
                        $http.get('/branchProfit/info', {params: params}).success(function(data){
                            scope.loading = false;
                            scope.partItem.branchList = angular.copy(data.position[0].branchList);
                            //angular.extend(scope.partItem.branchList, data.position[0].branchList);
                        }).error(function(){
                            console.log('error');
                        });
                    }, 300)
                }
            });
        }
    };

});