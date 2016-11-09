<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>警告</title>
<style type="text/css">
/**************
				reset
			***************/
body, div, p {
	margin: 0;
	padding: 0
}



img {
	border: 0;
	vertical-align: top
}

h1 {
	margin: 0;
	padding: 0;
	font-size: 100%;
	font-weight: normal
}

body {
	font-size: 14px;
	color: #333;
	font-family: "Hiragino Sans GB", "Microsoft Yahei UI", "Microsoft Yahei",sans-serif;
	text-rendering: optimizeLegibility;
	-ms-text-size-adjust: 100%;
	-webkit-text-size-adjust: 100%
}

/**************
				header
			***************/

/* .mtl { */
/* 	margin-top: 20px */
/* } */

.fl {
	float: left
}

.wrapper {
	width: 1000px;
	margin: 0 auto
}

.p {
	position: relative;
	display: inline-block;
	*display: inline;
	zoom: 1
}

.ml_20 {
	margin-left: 20px
}

/*******************************
				error page (404/500 ect.)
			********************************/
.errorPage .errorWrap {
	height: 380px;
	width: 600px;
	margin: 0 auto;
	background: url("/images/errorPageIcon.png") no-repeat center bottom
}

.errorPage .errorWrap h1 {
	font-size: 72px;
}

.errorPage .errorMessageInfo {
	font-size: 16px;
	color: #333;
	margin-top: 20px;
	margin-bottom: 20px;
	text-align: center;
	color: #39ac6a
}

.errorPage a.btn_goHome, a.btn_goHome:hover {
	background-color: #39ac6a;
	height: 30px;
	line-height: 30px;
	font-size: 16px;
	color: #fff;
	padding: 0 24px;
	display: inline-block
}

</style>

</head>
<body>
	<div class="errorPage">
		<div class="errorWrap">
			<h1></h1>
		</div>
		<p class="errorMessageInfo">
			<c:if test="${errorType==0}">您没有相应权限,如有疑问,请联系相关负责人。</c:if>
			<c:if test="${errorType==1}">此功能模块未开放,请联系管理员</c:if>
			<a href="/" class="btn_goHome ml_20">首页</a>
		</p>
	</div>




</body>
</html>