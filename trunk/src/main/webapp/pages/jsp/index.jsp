<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="at" uri="http://www.dooioo.com/tags/eReceipt" %>
<%
    String urlSuffix = "${config.urlSuffix}";
%>
<!DOCTYPE html>
<html ng-app="eReceipt">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=8">
    <meta http-equiv="Expires" content="0">
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv="Cache-control" content="no-cache">
    <meta http-equiv="Cache" content="no-cache">
    <base href="">
    <title ng-bind="title">首页-电子产品-链家</title>
    <link rel="shortcut icon" href="http://dui.dooioo.${config.urlSuffix}/public/images/favicon.ico"
          type="image/x-icon">
    <link rel="bookmark" href="http://dui.dooioo.${config.urlSuffix}/public/images/favicon.ico" type="image/x-icon">
    <link type="text/css" rel="stylesheet" href="http://dui.dooioo.${config.urlSuffix}/public/css/main.css"/>
	<link type="text/css" rel="stylesheet" href="http://dui.dooioo.${config.urlSuffix}/public/css/header.css"/>
    <link type="text/css" rel="stylesheet" href="/css/common.css">
    <script src="/js/jquery1.6.4.js"></script>
    <script type="text/javascript">
        var headerParameters =
        {
            empNo: '${sessionScope.eReceipt_session_user.userCode}',
            empName: '${sessionScope.eReceipt_session_user.userName}',
            companyId: '${sessionScope.eReceipt_session_user.companyId}',
            appName: '${config.appCode}',
            env: '${config.env}'
        };
        var ctxPath = "${pageContext.request.contextPath}";
        ctxPath = ctxPath == "" ? "/" : ctxPath;
        var version = "${config.version}";
        var pdfUrl = "${config.pdfUrl}";
    </script>
    <script type="text/javascript">

	    //报表服务器连接地址
		//要跳转到的链接
		
		var default_url = '/bill';
			
		var privileges,privilegesUrl=[];
        $.ajax({
            type:'GET',
            url: "/loadPrivileges",
            async:false,
            dataType:'json',
            success: function (res) {
                if(res.status == 1){
                  privileges = res.data;
                }
            }
        });
        </script>
    <style type="text/css">
    </style>
</head>

<body>

<!--头部开始-->
<div id="new_header"></div>

<!--头部结束-->


<div class="container clearfix" style="margin-bottom:50px;">
	<!-- 一级导航 begin -->
			<div style="display:none;" ng-show="showMenu">
				<at:menu/>
			</div>
	<!-- 一级导航 end -->

    <!-- 页面体部 -->
    <div ng-view></div>
</div>
<div class="footer clearfix">&copy:2016--version:${config.sysVersion } </div>

</body>
<!-- 公用组件 -->
<script src="http://dui.dooioo.${config.urlSuffix}/public/js/header.js"></script>
<script src="http://dui.dooioo.${config.urlSuffix}/public/js/fns.js"></script>
<script src="http://dui.dooioo.${config.urlSuffix}/public/js/lib/angular.min.js?v=${config.sysVersion}"></script>
<script src="http://dui.dooioo.${config.urlSuffix}/public/json/dooioo/lianjiaAll.js?v=${config.sysVersion}"></script>
<script src="http://dui.dooioo.${config.urlSuffix}/public/js/angular/config/config.js?v=1"></script>
<script src="http://dui.dooioo.${config.urlSuffix}/public/js/plugs/jquery-tree-1.0.js?v=1"></script>
<script src="http://dui.dooioo.${config.urlSuffix}/public/json/dooioo/dooiooAll.js?v=1"></script>
<script type="text/javascript" src="http://dui.dooioo.${config.urlSuffix}/public/js/angular/directive/angular-validate-1.0.js"></script>
<script type="text/javascript" src="http://dui.dooioo.${config.urlSuffix}/public/js/paginate.js"></script>
<script type="text/javascript" src="http://dui.dooioo.${config.urlSuffix}/public/js/plugs/jquery-upload-1.0.js"></script>
<script type="text/javascript" src="http://dui.dooioo.${config.urlSuffix}/public/js/angular/directive/directive.js"></script>
<script type="text/javascript" src="http://dui.dooioo.${config.urlSuffix}/public/js/angular/directive/angular-upload-3.0.js"></script>
<script type="text/javascript" src="http://dui.dooioo.${config.urlSuffix}/public/js/tipsNotice.js"></script>
<script type="text/javascript" src="http://dui.dooioo.${config.urlSuffix}/public/js/WdatePicker.js"></script>
<script type="text/javascript" src="http://dui.dooioo.${config.urlSuffix}/public/js/angular/directive/angular-autocomplete-1.0.js"></script>
<script type="text/javascript" src="http://dui.dooioo.${config.urlSuffix}/public/js/angular/directive/angular-pagination-1.0.js"></script>
<script type="text/javascript" src="http://dui.dooioo.${config.urlSuffix}/public/js/angular/directive/angular-searchlist-1.0.js"></script>
<script type="text/javascript" src="http://dui.dooioo.${config.urlSuffix}/public/js/angular/directive/angular-datepicker-1.0.js"></script>
<script type="text/javascript" src="http://dui.dooioo.${config.urlSuffix}/public/js/plugs/jquery-watermark-1.0.js?v=${config.sysVersion}"></script>
<script type="text/javascript" src="http://dui.dooioo.${config.urlSuffix}/public/js/angular/filter/filter.js?v=${config.sysVersion}"></script>
<script type="text/javascript" src="http://dui.dooioo.${config.urlSuffix}/public/js/validation.js"></script>
<!-- 公用组件 end -->

<%--自定义指令--%>

<!-- application resource -->
<script src="/js/app/app.js?v=${config.version}"></script>
<script src="/js/app/run.js?v=${config.version}"></script>
<script src="/js/app/route.js?v=${config.version}"></script>
<script src="/js/app/filter/filters.js?v=${config.version}"></script>
<script src="/js/app/directive/easyPagenate.js?v=${config.version}"></script>
<script src="/js/app/directive/subMenu.js?v=${config.version}"></script>
<script src="/js/app/directive/pagination.js?v=${config.version}"></script>
<script src="/js/app/common/initbackground.js?v=${config.version}"></script>
<script src="/js/app/common/fun.js?v=${config.version}"></script>
<script src="/js/download.js?v=${config.version}"></script>
<script type="text/javascript" src="/js/autocomplete.js?v=${config.version}"></script>

<%--基础数据--%>
<script type="text/javascript" src="/js/app/bill/bill.js?v=${config.version}"></script>
<script type="text/javascript" src="/js/app/register/register.js?v=${config.version}"></script>
<script type="text/javascript" src="/js/app/monitor/monitor.js?v=${config.version}"></script>

<!-- application resource end -->

<script type="text/javascript">
	//菜单当前状态选中
	$(function(){
		//菜单页单击后事件
		$(".navTab1th").find("a").bind("click",function(){
			$(".navTab1th").find("a").removeClass("current");
			$(this).addClass("current");
		});
	})
	//消息通知
	var _tipNotice = new __tipNotice(headerParameters.empNo);
	_tipNotice.run(true);
	if(headerParameters.env == "production"){
	   	var ubt_js = "<script src='http://logment.dooioo.com/ubt.js' type='text/javascript'><\/script>";
	   	document.write(ubt_js);
	}
</script>




</html>
