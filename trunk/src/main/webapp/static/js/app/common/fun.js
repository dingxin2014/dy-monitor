
/** 
 * 公共方法:queryUrl, scope, http,scope中query参数必须有，表示查询参数,
 * 返回的值有两个赋值在scope中，一个为scope.list,表示显示的列表数据，一个为scope.paginate显示的分页信息
 **/
function paginateQuery(queryUrl, scope, http){
	scope.isLoad = false;
	var query = scope.searchQuery ? scope.searchQuery : {};
	http.post(queryUrl, query).success(
		function(rs, status, headers, config){
			scope.isLoad = true;
			if(rs.status == 1){
				scope.searchlist = rs.data;
				scope.searchlist.list = rs.data.pageList;
				scope.list = rs.data.pageList;
	            scope.paginate = rs.data;
			}else{
				//alert(rs.message);
			}
			
		}
	).error(function(){
        alert('获取信息时服务端报错，请重试！');
    });
	
}

//加载数据
function loadData(paramConfig){
	paramConfig.http.get(paramConfig.url, {params: paramConfig.params || {}}).success(
		function(data, status, headers, config){
			paramConfig.root[paramConfig.key] = data;
		}
	);
}

function computeTax(totalMoney){
	var taxMoney = totalMoney - 3500;
	var tax = 0;
	if(taxMoney<=0){
		tax = 0;
	}else if(taxMoney<=1500){
		tax = taxMoney * 0.03;
	}else if(taxMoney<=4500){
		tax = taxMoney * 0.1 - 105;
	}else if(taxMoney<=9000){
		tax = taxMoney * 0.2 - 555;
	}else if(taxMoney<=35000){
		tax = taxMoney * 0.25 - 1005;
	}else if(taxMoney<=55000){
		tax = taxMoney * 0.3 - 2755;
	}else if(taxMoney<=80000){
		tax = taxMoney * 0.35 - 5505;
	}else{
		tax = taxMoney * 0.45 - 13505;
	}
	return tax;
}


/**
 * <p>年终奖</p>
 * @param salary			工资
 * @param beforeTax			提现金额
 * @param yearCommission	年终提成
 * @returns {Number}
 */
function computeDecTax(salary,beforeTax,yearCommission){
	var w = 0;
	var tax = 0;
	if(salary <= 3500){
		w = salary + beforeTax + yearCommission - 3500;
	}else{
		w = beforeTax + yearCommission;
	}
	var taxMoney = w/12;
	if(taxMoney<=0){
		tax = 0;
	}else if(taxMoney<=1500){
		tax = w * 0.03;
	}else if(taxMoney<=4500){
		tax = w * 0.1 - 105;
	}else if(taxMoney<=9000){
		tax = w * 0.2 - 555;
	}else if(taxMoney<=35000){
		tax = w * 0.25 - 1005;
	}else if(taxMoney<=55000){
		tax = w * 0.3 - 2755;
	}else if(taxMoney<=80000){
		tax = w * 0.35 - 5505;
	}else{
		tax = w * 0.45 - 13505;
	}
	return tax;
}


//参数解析对象
function ParamsParser(search){
	this.search = search;
	this.params = this._parseUrlToParams();
}

ParamsParser.prototype = {
	getParameter: function(name){
		return this.params[name] ? this.params[name] : '';
	},
	_parseUrlToParams: function(){
		var queryString = (this.search.length > 0) ? this.search.substring(1) : '',
		params = {},
		items = queryString.length ? queryString.split('&') : [];
		
		for(var i=0, len=items.length; i<len; i++){
			var pair = items[i].split('=');
			var name = decodeURIComponent(pair[0]);
			var value = decodeURIComponent(pair[1]);
			
			if(name.length){
				params[name] = value;
			}
		}
		
		return params;
	}
}