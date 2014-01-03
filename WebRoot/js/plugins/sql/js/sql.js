/**
 * 运行sql组件
 * 曾小斌
 * 2013-12-31 15:39:15
 */

define(function(require,exports,module){
	var message=require("message");
	var $document=$(document);
	var createTable=function(){
		var table = ''+
		'<table class="table_datagrid" title="数据结果" '+
		'	data-options=" rownumbers:true,singleSelect:true,autoRowHeight:false,pagination:true">'+
		'	<thead><tr class="table_datagrid_head">'+
		'	</tr></thead>'+
		'</table>';
		return $(table);
	};
	
	var getRangeById=function(id){ 
		var word='';
		if (document.selection){
			var obj=document.selection.createRange();
			if(obj.text.length>0){
				word=obj.text;
			}
		}else{ 
			var obj=document.getElementById(id); 
			var start=obj.selectionStart,end=obj.selectionEnd; 
			if (start||start=='0'){
				if(start!=end){
					word=obj.value.substring(start,end);
				}
				else{
					word=obj.value;
				}
			}
		} 
		return word;
	};
	
	module.exports.init=function(){
		//运行Sql
		$document.on("click","#btn_run",function(e){
			var sql=getRangeById("sql_text")||$("#sql_text").val();
			if(sql==""){
				return message.error("系统提示：请输入sql语句");
			}
			module.exports.exeSql($(".exe_result_list"),sql,1,100);
		});
	};
	
	//打开表自动生成Sql调用的
	module.exports.selectTable=function(that,sql,callback){
		if(that.find(".table_datagrid").size()==0){
			that.find(".result_table").html(createTable());
		}
		var datagrid=that.find(".table_datagrid");
		var treeTable=$('.dataBaseTree').tree("getSelected");
		datagrid.datagrid({
	        url:"sql/findTableData.action",
	        queryParams: {"sql":sql},
	        columns:[treeTable.columns],
	        height:$(that).height(),
	        remoteSort:false,
	        loadMsg:"正在加载，请稍后...",
	        pageSize:20,
	        pageList: [20,50,100,200],
	        loadFilter: function(result){
	    		if (result.data){
	    			return result.data;
	    		} else {
	    			return result;
	    		}
	    	},
	        onLoadSuccess: function(result){
	        	$(".conn_content").tabs("select","列表展示");
	        	var layout=['list','sep','first','prev','sep',"links",'sep','next','last','sep','refresh','sep','manual'];
	        	
	        	if(result.total==0){
	        		layout=['refresh'];
	        		that.find(".result_table .datagrid-view2 .datagrid-body").html("<div class=\"no-result\">表中没有数据！</div>");
	        	}
	        	else if(result.total<=20){
	        		//that.find(".datagrid-pager").hide();
	        		layout=['list','sep','refresh'];
	        	}else{
	        		//that.find(".datagrid-pager").show();
	        	}
	        	
	        	datagrid.datagrid('getPager').pagination({
	        		layout:layout,
	        		beforePageText:'跳转：第',
	        		afterPageText:'页',
	        		displayMsg:"当前第[{from}-{to}]条 共[{total}]条"
	        	});
	    	}
	    });
	};
	
	//执行SQL
	module.exports.exeSql=function(that,sql,pageIndex,pageSize,callback){
		message.wait("正在运行sql，请稍后...");
		var sqlType=$("#selectTableType").combobox("getValue");
		$.post("sql/"+sqlType+".action",{"sql":sql,"page":pageIndex||"1","rows":pageSize},function(result){
			if(typeof result=="object" && typeof result.data=="object"){
				that.find(".msg_tip").html("");
				that.find(".result_table").empty().show();
				message.hide();
				if(that.find(".table_datagrid").size()==0){
					that.find(".result_table").html(createTable());
				}
				
				if(result.recode==1){
					if(result.data.total==0){
						return that.find(".msg_tip").html("<div class=\"no-result\">表中没有数据！</div>");
					}
				}else{
					return that.find(".msg_tip").html("<div class=\"no-result\">"+result.message+"</div>");
				}
				var fields=[];
				console.log(result.data);
				for(var field in result.data.rows[0]){
					fields.push({field:field,title:field,width:100});
				}
				var datagrid=that.find(".table_datagrid");
				datagrid.datagrid({
			        columns:[fields],
			        height:$(that).height(),
			        remoteSort:false,
			        loadMsg:"正在加载，请稍后...",
			        pageNumber:pageIndex,
			        pageSize:pageSize,
			        pageList: [pageSize],
			        loadFilter:function(data){
			    		var pager = datagrid.datagrid('getPager');
			    		pager.pagination({
			    			onSelectPage:function(pageNum, pageSize){
			    				module.exports.exeSql(that,sql,pageNum,pageSize);
			    			}
			    		});
			    		return data;
			    	},
			        onLoadSuccess: function(result){
			        	if(result.total==0){
			        		return that.find(".result_table").html("<div class=\"no-result\">表中没有数据！</div>");
			        	}
			        	if(result.total<=100){
			        		that.find(".datagrid-pager").hide();
			        	}else{
			        		that.find(".datagrid-pager").show();
			        	}
			    	}
			    }).datagrid('loadData', result.data||result);
				datagrid.datagrid('getPager').pagination({
					layout:['sep','first','prev','sep',"links",'sep','next','last','sep','refresh'],
					displayMsg:"因浏览器性能限制，若查询语句未加分页，默认每100条数据进行分页；当前第[{from}-{to}]条 共[{total}]条"
				});
				callback&&callback(result.data||result);
			}else{
		    	message.stop("系统出现错误！");
				return that.find(".msg_tip").show().html("<div class=\"no-result\">系统出现错误，"+result.message+"</div>");
		    }
		});
	}
});