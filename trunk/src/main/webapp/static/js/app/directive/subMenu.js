
/*
 * 子菜单指令
 *
 */
eReceipt.directive('subMenu', function($timeout, $http,$templateCache,$compile){
	 /*
     * <div class="navTab2th mt_20 clearfix">
			<a href="kpiManage" class="current">维护KPI</a>
			<a href="monthPositionManage" ng-show="havePrivilege('pfp_monthPosition_manage')">维护区总</a>
			<a href="billManage" ng-show="havePrivilege('pfp_billManage')">账单管理</a>
			<a href="reportManage" ng-show="havePrivilege('pfp_reportservice_list')">报表管理</a>
			<a href="relationRecord" ng-show="havePrivilege('pfp_relationrecord_list')">上区上副</a>
		</div>
     */
	var menuKey = "__menu__key__";
    return {
        restrict: 'E',
        template:'',
        link: function(scope, el, attrs){
        	var appendMenu = function(menus){
            	var s = '<div class="navTab2th mt_20 clearfix">';
            	angular.forEach(menus, function(perm, key) {
            	  if(curr == perm.code){
            		  s += '<a href="'+perm.url+'" code="'+perm.code+'" class="current" >'+perm.name+'</a>';
            	  }else{
            		  s += '<a href="'+perm.url+'" code="'+perm.code+'" >'+perm.name+'</a>';
            	  }
        		});
            	s+='</div>';
            	el.html(s);
            	$compile(el.contents())(scope);
            	$(".navTab1th").find("a").removeClass("current");
            	$(".navTab1th").find("a").each(function(){
            		var c = $(this).attr("code");
            		if(c == attrs["code"]){
            			$(this).addClass("current");
            		}
            	});
        	}	
        	var code = attrs["code"];
        	var params = {code:code};
        	var curr = attrs["current"];
        	 $http.post(attrs["url"], params).success(function(rs){
                if(rs.status == 1){
                	appendMenu(rs.data);
                	//$templateCache.put(menuKey,rs.data);
                }else{
                	console.log(rs.message);
                }
             }).error(function(){
                 console.log('error');
             });
        }
    };

});