/**
 * Created by Watcher on 16/6/22.
 */
(function () {
    angular.module('dui.component.pagination', []).directive('duiPagination', function () {
        return {
            restrict: 'E',
            replace: true,
            scope: {
                currentPage: '=',
                pageSize: '=',
                totalPage: '=',
                displayNum: '@',
                clickCallback: '&'
            },
            template: '<div class="txtRight ">\
            	<div class="pageNew in_block">\
            每页显示<select id="selectItem" ng-model="pageItem" style="margin:4px 5px 4px 5px;height:22px;line-height:22px;" ng-change="setPageSize()">\
            <option  ng-repeat="s in seles">{{s}}</option>\
            </select>条</div>\
									<div class="pageNew in_block" style="margin-top:{{!isSimple && 15 || 0}}px;margin-bottom:{{!isSimple && 15 || 0}}px">\
										<a href="javascript:;" ng-hide="isFirst()" class="in_block btn prev" ng-click="prev()"></a>\
										<span class="in_block prev" ng-show="isFirst()"></span>\
										<span class="simple" ng-show="isSimple"><span class="bold" ng-bind="currentPage"></span>/<span class="mr_5" ng-bind="totalPage"></span></span>\
										<a href="javascript:;" ng-hide="isSimple" class="in_block mr_5" ng-class="{disabled: page==currentPage, ellips: page==-1}" ng-repeat="page in pages" ng-click="setCurrentPage(page,pageSize)">{{page == -1 && \'. . .\' || page}}</a>\
										<a href="javascript:;" ng-hide="isLast()" class="in_block btn next" ng-click="next()"></a>\
										<span class="in_block next" ng-show="isLast()"></span>\
									</div>\
									<div class="in_block" ng-class="{mt_15:!isSimple, mb_15:!isSimple}" ng-show="showJump">\
										<p class="in_block ml_5 lineH180">共<i ng-bind="totalPage" stype="background-color:"></i>页</p>\
										<p class="in_block ml_5 lineH180">\
											到第 <input type="number" class="txtNew t50 disable_num_arrow js_inputPage" ng-model="jumpedPage" maxlength="6""> 页  \
											<a href="javascript:;" class="btnOpH24 h24Silver in_block ml_5" ng-click="jump()">确定</a>\
										</p>\
									</div>\
							</div>',
            link: function (scope, el, attrs) {
            	scope.seles=[5,10,15,20,50,100,200,500];
                var edgeNum = 2;
                scope.pageItem = scope.pageSize;
                //是否显示输入页码跳转
                scope.showJump = attrs.showJump === 'true';
                scope.isSimple = attrs.isSimple === 'true';
                //设置当前页
                scope.setCurrentPage = function (page,pageSize) {
                    pageSize = pageSize ? pageSize : scope.pageSize;
                    if (!(scope.isChangePage(page) || scope.isChangePageSize(pageSize)) ) return;
                    var oldTotalCount = scope.totalPage * scope.pageSize;
                    var newPageCount = oldTotalCount % pageSize == 0 ? oldTotalCount / pageSize : Math.floor( (oldTotalCount / pageSize +1));
                    if(newPageCount < page ){
                    	page = newPageCount;
                    }
                    scope.currentPage = page;
                    scope.pageSize = pageSize;
                    scope.jumpedPage = page;
                    if (attrs.clickCallback) {
                        scope.clickCallback(scope.currentPage,scope.pageSize);
                    }
                };
                
                scope.isChangePage = function(page){
                	return !(isNaN(page) || page < 1 || page > scope.totalPage || page === scope.currentPage);
                }
                
                scope.isChangePageSize = function(pageSize){
                	return !(isNaN(pageSize) || pageSize < 1 || pageSize === scope.pageSize) ;
                }

                scope.isFirst = function () {
                    return scope.currentPage <= 1;
                };

                scope.prev = function () {
                    if (scope.isFirst()) return;
                    scope.setCurrentPage(scope.currentPage - 1);
                };


                scope.next = function () {
                    if (scope.isLast()) return;
                    scope.setCurrentPage(scope.currentPage + 1);
                };

                scope.isLast = function () {
                    return scope.currentPage >= scope.totalPage;
                };

                el[0].querySelector('.js_inputPage').addEventListener('keyup', function (e) {
                    if (e.keyCode == 13) {
                        scope.$apply(function () {
                            scope.jump();
                        });
                    }
                });

                scope.jump = function () {
                	if(scope.jumpedPage > scope.totalPage){
                		scope.jumpedPage = scope.totalPage;
                	}
                	if(scope.jumpedPage < 1){
                		scope.jumpedPage  = 1;
                	}
                    scope.setCurrentPage(scope.jumpedPage,scope.pageSize);
                   // scope.jumpedPage = undefined;
                };

                scope.setPageSize = function () {
                    scope.setCurrentPage(scope.currentPage,scope.pageItem);
                }

                scope.$watch('currentPage + totalPage', function (val) {
                    scope.displayNum = scope.displayNum || 6;
                    if (!scope.currentPage || isNaN(scope.currentPage)) {
                        return;
                    }

                    if(scope.currentPage>5){
                        scope.displayNum =5;
                    }else{
                        scope.displayNum =6;
                    }

                    var pages = [],
                        range = buildPageRange(scope.totalPage, scope.currentPage, scope.displayNum);

                    // Generate starting points
                    if (range.start > 0) {
                        addPageRange(0, Math.min(edgeNum, range.start), pages);
                        addEllipse(edgeNum < range.start, pages);
                    }

                    // Generate interval links
                    addPageRange(range.start, range.end, pages);

                    // Generate ending points
                    if (range.end < scope.totalPage) {
                        addEllipse(scope.totalPage - edgeNum > range.end, pages);
                        addPageRange(Math.max(scope.totalPage - edgeNum, range.end), scope.totalPage, pages);
                    }

                    scope.pages = pages;
                }, true);

                //构建中间区块的页码
                function buildPageRange(totalPage, currentPage, displayNum) {
                    var half = Math.ceil(displayNum / 2),
                        upper_limit = totalPage - displayNum,
                        start = currentPage > half ? Math.max(Math.min(currentPage - half, upper_limit), 0) : 0,
                        end = currentPage > half ? Math.min(currentPage + half, totalPage) : Math.min(displayNum, totalPage);

                    return {
                        start: start,
                        end: end
                    };
                }

                function addPageRange(start, end, target) {
                    for (var page = start; page < end; page++) {
                        var actualPage = (page < 0 ? 0 : (page < scope.totalPage ? page : scope.totalPage - 1)) + 1; // Normalize page id to sane value
                        target.push(actualPage);
                    }
                }

                function addEllipse(isAddEllipse, target) {
                    if (isAddEllipse) {
                        target.push(-1);
                    }
                }
            }
        };
    });
}());