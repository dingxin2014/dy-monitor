/**
 * Created by Watcher on 16/6/22.
 */

eReceipt.filter('bankCodeFilter', function(){
	return function(value) {
		var text = '';
		switch (value) {
			case 'BOC_NET':
				text = '中国银行';
				break;
			case 'ICBC_CMP':
				text = '工商银行';
				break;
			default:
				text = '中国银行';
		}
		return text;
	};
}).filter('dateFormat', function() {
	//返回类型：yyyy-MM-dd，输入类型：yyyy-MM-dd HH:mm:ss
	return function(input) {
		var str = $.trim(input) ;
		if( str !== ''){
			return str.substr(0,10);
		}
		return str;
	};
}).filter('status', function() {
	return function(value) {
		var text = '';
		switch (value) {
			case 1:
				text = '连接中';
				break;
			case 0:
				text = '未连接';
				break;
			default:
				text = '未连接';
		}
		return text;
	};
}).filter('suspendFlag', function() {
	return function(value) {
		var text = '';
		switch (value) {
			case 1:
				text = '中止';
				break;
			case 0:
				text = '未中止';
				break;
			default:
				text = '未中止';
		}
		return text;
	};
});
