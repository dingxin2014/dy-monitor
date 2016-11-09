
eReceipt.run(function($rootScope, $location, $http,$q,$timeout){
	$rootScope.showMenu = false;
	$rootScope.arealist = null;
	$rootScope.privileges = privileges;

	$rootScope.$on('$routeChangeStart', function (event, next, current) {
		var isDev = "development" == headerParameters.env ;
		if(!isDev){
			if(next.$$route.perm && next.$$route.perm != "ano" &&!$rootScope.privileges[next.$$route.perm]){
				$location.path('/no_privilege');
			}
		}
		//根据浏览器地址匹配
		var path=window.location.href;
		var searchPath=path.substring(path.indexOf("#"),path.length);
		var lsMenu=false;
		$(".navTab1th").find("a").removeClass("current");
		for(i=0;i<$(".navTab1th").find("a").length;i++){
			if($(".navTab1th").find("a").get(i).hash==searchPath){
				$(".navTab1th").find("a").eq(i).addClass("current");
				lsMenu=true;
				break;
			}
		}
		//未能匹配,默认选中第一个
		if(!lsMenu) {
			$(".navTab1th").find("a").first().addClass("current");
		}
	});

	$rootScope.havePrivilege = function(pri){
    	if($rootScope.privileges[pri] == true){
    		return true;
    	}else{
    		return false;
    	}
    }
    
    /*http方法:get、post
     * 1、successBack--返回成功的回调函数,必传参数
     * 2、params--参数{},必传参数
     */
    $rootScope.isSending = false;
    var defaultFinalBack = function(){
    	$rootScope.isSending = false;
    }
    $rootScope.httpGet = function(url,params,successBack,errorBack,finalBack){
    	httpGetPost("get",url,successBack,{params: params},errorBack,finalBack);
    }
    $rootScope.httpGetCache = function(url,params,successBack,errorBack,finalBack){
    	httpGetPost("get",url,successBack,{params: params,cache: true},errorBack,finalBack);
    }
    $rootScope.httpPost = function(url,params,successBack,errorBack,finalBack){
    	httpGetPost("Post",url,successBack,params,errorBack,finalBack);
    }
    var httpGetPost = function(mode,url,successBack,params,errorBack,finalBack){
    	var result = null;
    	if(mode == "get"){
    		result = $http.get(url, params);
    	}else{
    		result = $http.post(url, params);
    	} 
        result.success(function(data){
        	successBack(data);        	
        	if(finalBack != null){
        		finalBack();
        	}else{
        		defaultFinalBack();
        	}
        });
        result.error(function(){
        	if(errorBack == null){
        		alert("服务器异常，请联系管理员处理。");
        	}else{
        		errorBack();
        	}
        	if(finalBack != null){
        		finalBack();
        	}else{
        		defaultFinalBack();
        	}
        });
    }
});