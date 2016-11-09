/**
 * 系统所有angularJs的服务路由跳转
 */
var APP_SUFFIX_TITLE = "-电子产品-链家";
eReceipt.config(['$routeProvider', '$locationProvider', '$httpProvider', function ($routeProvider, $locationProvider, $httpProvider) {

    $httpProvider.defaults.transformRequest = function (obj) {
        if(obj == undefined){
            return "";
        }
        if(angular.isString(obj)){
            return obj
        }
        var str = [];
        for (var p in obj) {
            str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
        }
        return str.join("&");
    }

    $httpProvider.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded;charset=utf-8';
    $httpProvider.defaults.headers.put['Content-Type'] = 'application/x-www-form-urlencoded;charset=utf-8';

    $locationProvider.hashPrefix('!');
    $routeProvider.
    when('/bill',
        {title:"单据"+APP_SUFFIX_TITLE,
            templateUrl: '/js/app/bill/bill.html',
            controller: bill,
            perm:'ereceipt_list'}
    ).when('/register',
        {
            title: "App注册" + APP_SUFFIX_TITLE,
            templateUrl: '/js/app/register/register.html',
            controller: register,
            perm: 'appRegister_list'
        }
    ).when('/no_privilege',
        {
            title:"无权限"+APP_SUFFIX_TITLE,
            templateUrl: '/js/app/common/no_privilege.html'
        }
    ).when('/monitor',
        {
        title: "监控" + APP_SUFFIX_TITLE,
        templateUrl: '/js/app/monitor/monitor.html',
        controller: monitor,
        perm: 'jobMonitor_list'
        }
).otherwise({redirectTo: default_url});

}]);