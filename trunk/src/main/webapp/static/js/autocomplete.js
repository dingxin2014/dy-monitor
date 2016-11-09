(function($){
	//默认实现：根据返回数据， 构建html
	//列表中的a标记没有具体的链接地址， 若要对a标记的链接做进一步的处理， 可以在外部传递回调函数：buildResultHtmlCallback
	function buildAutoComp(data, $autoCompCtrl, options){
		var listItem, itemKey=options.itemTextKey;
		if(data && data.length>0){
			$.each(data, function(){
				listItem = $('<a href="#">'+this[itemKey]+'</a>');
				listItem.data('itemData', this);
				$autoCompCtrl.append(listItem);
			});
		}else{
			if(options.noDataCallback && $.isFunction(options.noDataCallback)){
				$autoCompCtrl.append(options.noDataCallback());
			}else{
				$autoCompCtrl.append('<p class="pd_6 grey999">没有搜索到数据</p>');
			}
		}
		
		if(options.appendCallback && $.isFunction(options.appendCallback)){
			$autoCompCtrl.append(options.appendCallback(data));
		}
	}
	
	
	//构建并显示结果集
	function buildQueryResult(resultData, $keywordCtrl, $autoCompCtrl, options){
		//根据数据构建结果集
		options.buildAutoCompCallback ? options.buildAutoCompCallback(resultData, $autoCompCtrl, options) : buildAutoComp(resultData, $autoCompCtrl, options);
		
		if($autoCompCtrl.children().length == 0){		//div没有子元素， 则不显示弹层
			return ;
		}
		
		fnShowLayer($keywordCtrl, $autoCompCtrl, 'right');//显示自动完成提示层
		if($autoCompCtrl.height() >= 256){//重置下拉框高度
			$autoCompCtrl.css({'max-height':'256px', 'overflow':'hidden', 'overflow-y':'auto'});
		}
		
		if(options.hasMask){
			showIE6Mask($autoCompCtrl);
		}
	}
	
	//隐藏自动完成弹层
	function hideAutoCompDiv(layer, options){
		//layer.css({'height':'auto', 'overflow':'hidden', 'overflow-y':'hidden'});//恢复下拉框高度
		if(options.hasMask){
			removeIE6Mask();
		}
		layer.hide();
	}
	
	//请求数据:数据可来自本地或请求服务器(可供外部调用)
	function postRequest($keywordCtrl, $autoCompCtrl, options){
		//查询关键字， 不同情况下的取值
		var isUrl = typeof options.urlOrData == "string";
		if( isUrl ){
			//处理响应
			var processResponse = function(responseData){
				hideAutoCompDiv($autoCompCtrl, options);
				$autoCompCtrl.empty();
				if( responseData && responseData.length>=0 ){			//返回数据为最终结果
					buildQueryResult(responseData, $keywordCtrl, $autoCompCtrl, options);
				}else if( responseData && responseData[options.dataKey] && responseData[options.dataKey].length>=0){		//返回数据放在json对象中，存放的默认key为data
					buildQueryResult(responseData[options.dataKey], $keywordCtrl, $autoCompCtrl, options);
				}
			};
			
			//组装请求参数
			var requestParams = {};
			if( options.setRequestParams && typeof options.setRequestParams === 'function'){		//设置动态请求参数
				options.setRequestParams(requestParams, $keywordCtrl);
			}
			if( requestParams==null || $.isEmptyObject(requestParams) ) return;
			
			if(options.crossDomain){
				$.ajax({
					type: 'GET',
					url: options.urlOrData,
					data: requestParams,
					jsonp: 'jsoncallback',
	                dataType: 'jsonp',
	                success: processResponse
				});
			}else{
				$.getJSON(options.urlOrData, requestParams, processResponse);
			}
		}else{
			var resultData = [];
			var keyword = $keywordCtrl.val();
			$autoCompCtrl.empty();
			$.each(options.urlOrData, function(){
				if( this.keyword.indexOf( keyword ) != -1  ){
					resultData.push(this);
				}
			});
			if(resultData.length > 0){
				buildQueryResult(resultData, $keywordCtrl, $autoCompCtrl, options);
			}
		}
	}
	
	$.fn.extend({
		autocomplete:function(options){
			return this.each(function(){
				var index = -1, self = $(this);
				var resultOptions = {};
				$.extend(resultOptions, $.fn.autocomplete.defaultOptions, options || {});
				var layer = $(this).attr('popdiv') ? $('#'+$(this).attr('popdiv')) : ($(this).next('.autoComplate').length==0 ? $(this).next('.js_autocompleteResult') : $(this).next('.autoComplate'));
				//var layer = $('#'+$(this).attr('popdiv')).length>0 ? $('#'+$(this).attr('popdiv')) : $('.'+$(this).attr('popdiv')).eq(0);
				$(document.body).click(function(){
					hideAutoCompDiv(layer, resultOptions);
				});
				var timer;
				$(this).bind('keyup', function(event){
					var last = layer.find("a").length-1;
					switch(event.keyCode){
						case 13: //回车
								if( index != -1 ){
									$(this).val(layer.find("a").eq(index).text());
									if( resultOptions.selectCallback ){
										resultOptions.selectCallback(layer.find("a").eq(index).data('itemData'), $(this));
									}
								}
								hideAutoCompDiv(layer, resultOptions);
								break;
						case 38: //上箭头
								 layer.find("a").eq(index).removeClass("current");//当前项去掉高亮
								 if(index==0){//如果无前一项则最后一项加上高亮
									 layer.find("a:last-child").addClass("current");
									 index = last;
								 }
								 else{//如果有前一项加前一项上高亮
									 layer.find("a").eq(index-1).addClass("current");
									 index -= 1;
								 }
								 break;
						case 40://下箭头
								 layer.find("a").eq(index).removeClass("current");//当前项去掉高亮
								 if(index==-1 || index==last){//如果无后一项则第一项加上高亮
									 layer.find("a:first-child").addClass("current");
									 index = 0;
								 }
								 else{//如果有后一项加后一项加上高亮
									 layer.find("a").eq(index+1).addClass("current");
									 index += 1;
								 }
								 break;
						default: 
								var self = $(this);
								//if($.trim(self.val()) == '') return;
								timer = window.setTimeout(function(){
									index = -1;
									postRequest(self, layer, resultOptions);
								}, resultOptions.lazyTime);
								break;
					}
				}).click(function(e){
					hideAutoCompDiv($("div.autoComplate"), resultOptions);
					if( resultOptions.autocompleteOnfocus ){
						index = -1;
						postRequest($(this), layer, resultOptions);
					}
					stopBubble(e);
				}).keydown(function(e){
					clearTimeout(timer);
					stopBubble(e);
				});
				
				//给自动提示层绑定mouseover事件， 高亮显示选中项
				layer.delegate('a', "mouseover", function(){
					$(this).siblings().removeClass('current').end().addClass('current');
					index = layer.find("a").index($(this));
					return false;
				});
			
				//给自动提示层绑定click事件，选择选项
				layer.delegate('a', 'click', function(){
					self.val($(this).text());
					self.change();
					self.removeClass('grey999');
					if( resultOptions.selectCallback ){		//点击选项， 用户可自定义回调函数
						resultOptions.selectCallback($(this).data('itemData'), $(this));
					}
					hideAutoCompDiv(layer, resultOptions);
					return false;
				});
			});
		}
	});
	
	//默认配置项
	$.fn.autocomplete.defaultOptions = {
		itemTextKey: 'itemText',			//自动完成列表的值， 取自响应数据的itemTextKey指定的值
		autocompleteOnfocus: true,			//获得焦点时， 显示自动完成
		dataKey: 'data',
		hasMask: true,
		lazyTime: 100,
		crossDomain: true
	};
})(jQuery);