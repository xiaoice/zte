<%@page language="java" contentType="text/html; charset=UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>连接中心</title>
	<!-- Bootstrap -->
	<%@include file="/jsp/common/bootstrap.jsp"%>
	
	<script src="${base}js/plugins/jquery-easyui/jquery.easyui.min.js"></script>
	<link href="${base}js/plugins/jquery-easyui/themes/black/easyui.css" rel="stylesheet" type="text/css">
	
	<link rel="stylesheet" href="js/plugins/message/message.css" type="text/css"></link>
	<link type="text/css" href="js/plugins/conn/css/conn.css" rel="stylesheet" />
	<script type="text/javascript" src="js/plugins/message/message.js"></script>
</head>
<body class="easyui-layout">
	<!-- <div region="south" split="true" style="height:0px;background:#cbcbcb;"></div> -->
	<div region="west" split="true" style="width:260px;border-top-width:0;"><ul class="dataBaseTree" class="easyui-tree" ></ul></div>
	<div region="north" class="menu_tool">
		    <a href="javascript:void(0)" id="menu_top_file" class="easyui-menubutton" data-options="menu:'#menu_down_file'">文件</a>
		    <a href="javascript:void(0)" id="menu_top_tool" class="easyui-menubutton">工具</a>
		    <div id="menu_down_file" style="width:150px;">
			    <div data-options="iconCls:'icon-add'" onclick="$('#menu_create_connection_box').window('open');fillValue();">新建连接</div>
			    <div class="menu-sep"></div>
			    <div>导入SQL文件</div>
			    <div>导出Excel</div>
			    <div class="menu-sep"></div>
			    <div>注销</div>
		    </div>
	</div>
	
	<!-- <div region="east" title="数据库" split="true" style="width:180px;"></div> -->
	<div region="center" style="overflow:hidden;border-top-width:0;">
		<div class="easyui-tabs conn_content" border="false" fit="true">  
		    <div class="show_table_list" title="列表展示" style="padding:5px;">
		    	<div class="msg_tip"></div>
				<div class="result_table"></div>
		    </div>  
		    <div title="命令提示行" style="overflow:auto;">  
        		<div class="easyui-layout" fit="true" >
			    	<div region="north" split="true" style="overflow:hidden;height:140px;border-width:0;border-bottom-width: 1px;">
						<div class="cmd_toolBar"><a class="easyui-linkbutton" plain="true" iconCls="icon-play" id="btn_run">&nbsp;运行</a></div>
			    		<textarea id="sql_text" class="sql_text" rows="0" cols="0">select * from um_userinfo</textarea>
			    	</div>
					<div region="center" border="false" class="exe_result_list">
						<div class="msg_tip"></div>
						<div class="result_table"></div>
					</div>
				</div>
		    </div>  
		</div>
	</div>
	
	
	<div id="context_table1" class="menu_tree menu_tree_schema">
        <div iconCls="icon-add" id="menu_create_table" onclick='$("#panel_create_table").dialog("open");'>创建表</div>
        <div class="menu-sep"></div>
        <div iconCls="icon-reload" class="desk_icon_reload">刷新</div>
    </div>
    
    <div id="panel_create_table" class="panel_create_table" title="创建表" resizable="true" iconCls="icon-table" >
		<iframe frameborder="0" width="100%" height="100%" src="${base}jsp/conn/create_table.jsp"></iframe>
	</div>
	
	
	<div class="hidden">
    
    <!-- 表节点上面点击右键菜单 -->
	<div id="context_table" class="easyui-menu">
        <div iconCls="icon-search" id="context_table_open">打开表</div>
        <div iconCls="icon-edit" id="context_table_edit" onclick='message.info("正在开发，敬请期待！");'>修改表</div>
        <div class="menu-sep"></div>
        <div iconCls="icon-reload" class="context_table_reload">刷新</div>
    </div>
    
	<!-- 菜单-文件 -->
	<div id="menu_create_connection" class="panel_create_table" title="创建新连接" resizable="true" iconCls="icon-table" >
		<iframe frameborder="0" width="100%" height="100%" src="${base}jsp/conn/create_table.jsp"></iframe>
	</div>
	
	<!-- 菜单-文件 -创建新连接按钮 -->
	<div id="menu_create_connection_box" class="menu_create_connection_box easyui-window" title="创建新连接" 
		 data-options="iconCls:'icon-save',closed:true,collapsible:false,maximizable:false,minimizable:false" >
		<table class="edit_table">
			<tr>
				<td class="tdLabel">数据库类型:</td>
				<td>MYSQL</td>
			</tr>
			<tr>
				<td class="tdLabel">IP:</td>
				<td><input id="input_con_ip" class="easyui-validatebox" type="text"></input></td>
			</tr>
			<tr>
				<td class="tdLabel">数据库名称:</td>
				<td><input id="input_con_database" class="easyui-validatebox" type="text"></input></td>
			</tr>
			<tr>
				<td class="tdLabel">端口:</td>
				<td><input id="input_con_port" class="easyui-validatebox" type="text"></input></td>
			</tr>
			<tr>
				<td class="tdLabel">登录名:</td>
				<td><input id="input_con_user" class="easyui-validatebox" type="text"></input></td>
			</tr>
			<tr>
				<td class="tdLabel">口令:</td>
				<td><input id="input_con_password" class="easyui-validatebox" type="text"></input></td>
			</tr>
			<tr>
				<td colspan="2" class="text-center">
					<a id="bt_login_in" class="easyui-linkbutton">连接</a>
					<a id="bt_login_cancel" class="easyui-linkbutton">取消</a>
					<a id="bt_login_test" class="easyui-linkbutton">测试连接</a>
				</td>
			</tr>
		</table>
	</div>
	</div>
	
</body>
</html>

<script type="text/javascript">

var service={
	//将快捷菜单绑定到目标上
	menuTree:function(source,target){
		$(source).bind('contextmenu',function(e){
		    e.preventDefault();
		    $(target).menu('show', {  
		    	left: e.pageX,  
		        top: e.pageY
		   	}); 
		});
	},
	bindTree:function(){
		//绑定数据到到左侧树上
		$('.dataBaseTree').tree({
			checkbox: false,
			lines:true,
			url: 'sql/getTable.action',
			onBeforeLoad:function(node){
				message.wait("正在加载数据，请稍后");
			}, 
			loadFilter: function(data){
				if (data.data){
					return data.data;
				} else {
					return data;
				}
			},
			onDblClick:function(node){
				var sql="select * from "+node.text;
				service.runSql($(".show_table_list"),sql);
			},
			onContextMenu:function(e,node){
				$(".dataBaseTree").tree("select", node.target);
				if(node.iconCls=="icon-table"){
					e.preventDefault();
					$('#context_table').menu('show', {
			    		left: e.pageX,
			    		top: e.pageY
			    	});
				}
			},
			onLoadSuccess:function(node, data){
				message.hide();
				if(node==null){
					
				    /* $(data.target).bind('contextmenu',function(e){
				    	e.preventDefault();
				    	$('#context_table').menu('show', {
				    		left: e.pageX,
				    		top: e.pageY
				    	});
				    }); */
				}else if(node.id){
					
				}
				
				/* //颜色控制，图标控制
				if(node==null){
					$(".dataBaseTree .tree-folder").addClass("icon-user-man");
					$(".dataBaseTree .tree-title").addClass("gray");
					service.menuTree(".dataBaseTree .tree-node",".menu_tree_schema");
				}
				else if(node.id){
					var ids=node.id.split('＿');
					if(ids.length==1){
						$(node.target).next().find(".tree-title").addClass("gray");
						service.menuTree($(node.target).next().find(".tree-node"),".menu_tree_table");
					}
					$(node.target).find(".tree-title").removeClass("gray").addClass("black");
				} */
			},
			onSelect:function(node){
				/* if(node==null){
					
				}
				else if(node.id){
					var ids=node.id.split('＿');
					if(ids.length==2){
						var sql="select * from "+ids[0]+"."+ids[1];
						service.runSql(node,sql,$(".show_table_list"));
						$('.conn_content').tabs("select","列表展示");
					}
					else{
						
					}
					$(node.target).find(".tree-title").removeClass("gray").addClass("black");
				} */
			}
		});
	},
	getRangeById:function(id){ 
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
	},
	createTable:function(){
		var table = ''+
		'<table class="table_datagrid" title="数据结果" '+
		'	data-options=" rownumbers:true,singleSelect:true,autoRowHeight:false,pagination:true,pageSize:10">'+
		'	<thead><tr class="table_datagrid_head">'+
		'	</tr></thead>'+
		'</table>';
		return $(table);
	},
	pagerFilter:function(data){
		var dg = $(this);
		var opts = dg.datagrid('options');
		var pager = dg.datagrid('getPager');
		pager.pagination({
			onSelectPage:function(pageNum, pageSize){
				opts.pageNumber = pageNum;
				opts.pageSize = pageSize;
				pager.pagination('refresh',{
					pageNumber:pageNum,
					pageSize:pageSize
				});
				message.wait("正在加载，请稍后...");
				service.exeSql(opts.that,opts.sql,pageNum,pageSize,function(result){
					message.hide();
					dg.datagrid('loadData',result);
				});
			}
		});
		return data;
	},
	runSql:function(that,sql,callback){
		if(that.find(".table_datagrid").size()==0){
			that.find(".result_table").html(service.createTable());
		}
		//var sql=sqlcode||service.getRangeById("sql_text");
		var datagrid=that.find(".table_datagrid");
		var treeTable=$('.dataBaseTree').tree("getSelected");
		datagrid.datagrid({
	        url:"sql/findTableData.action",
	        queryParams: {"parameter.sql":sql},
	        columns:[treeTable.columns],
	        height:$(that).height(),
	        remoteSort:false,
	        loadMsg:"正在加载，请稍后...",
	        showHeader:false,
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
	        	if(result.total==0){
	        		that.find(".result_table").html("<div class=\"no-result\">表中没有数据！</div>");
	        	}
	        	
	        	if(result.total<=10){
	        		that.find(".datagrid-pager").hide();
	        	}else{
	        		that.find(".datagrid-pager").show();
	        	}
	    	}
	    });
		datagrid.datagrid('getPager').pagination({
			layout:['list','sep','first','prev','sep',"links",'sep','next','last','sep','refresh','sep','manual'],
			 beforePageText:'跳转：第',
			 afterPageText:'页'
		});
	},
	
	//执行SQL
	exeSql:function(that,sql,pageIndex,pageSize,callback){
		//var sql=sqlcode||service.getRangeById("sql_text");
		$.post("sql/findTableData.action",{"parameter.sql":sql,"page":pageIndex||"1","rows":pageSize||"20"},function(result){
			if(typeof result=="object" && typeof result.data=="object"){
				if(that.find(".table_datagrid").size()==0){
					that.find(".result_table").html(service.createTable());
				}
				if(typeof result=="string"){
					that.find(".result_table").hide();
					return that.find(".msg_tip").show().html("程序出现错误！");
				}else{
					if(result.recode==0){
						return that.find(".msg_tip").show().html(result.message);
					}else if(result.data.total==0){
						that.find(".msg_tip").show().html("该表为空表，查找不到数据！");
					}else{
						that.find(".msg_tip").html("").hide();
						that.find(".result_table").show();
					}
				}
				
				var fields=[];
				for(var field in result.data.rows[0]){
					fields.push({field:field,title:field,width:100});
				}
				var datagrid=that.find(".table_datagrid");
				var result1=result.data||result;
				datagrid.datagrid({
			        columns:[fields],
			        height:$(that).height(),
			        remoteSort:false,
			        loadMsg:"正在加载，请稍后...",
			        showHeader:true,
			        pageSize:20,
			        pageList: [20,50,100,200],
			        loadFilter:service.pagerFilter,
			        onLoadSuccess: function(result){
			        	if(result.total==0){
			        		that.find(".result_table").html("<div class=\"no-result\">表中没有数据！</div>");
			        	}
			        	if(result.total<=10){
			        		that.find(".datagrid-pager").hide();
			        	}else{
			        		that.find(".datagrid-pager").show();
			        	}
			    	}
			    }).datagrid('loadData', result1);
				datagrid.datagrid('getPager').pagination({
					layout:['list','sep','first','prev','sep',"links",'sep','next','last','sep','refresh','sep','manual'],
					 beforePageText:'跳转：第',
					 afterPageText:'页'
				});

				
				/* table.datagrid({
					//loadFilter:service.pagerFilter,
					height:$(that).height()
				}).datagrid('loadData', result); */
				var opts = datagrid.datagrid('options');
				opts.sql=sql;
				opts.that=that;
				callback&&callback(result1);
			}else{
		    	message.stop("操作异常，程序已经停止");
		    }
		});
	},
	bindDom:function(){
		service.bindTree();
		$('.menu_tree_schema,.menu_tree_table').menu();//初始化右键菜单
		$(document).on("click",".icon-run",service.runSql);
		$('#panel_create_table').dialog({
			width:616,
			height:394,
		    modal:true,
		    closed:true
		}).show();
		
		$(".sql_text").keypress(function(e){
			if(e.ctrlKey && e.which == 13 || e.which == 10) { 
				service.runSql();
			}
		});
		
		$(document).on("click","#desk_icon_select",function(e){
			$('.conn_content').tabs("select","命令提示行");
			var node = $('.dataBaseTree').tree("getSelected");
			if(node!=null){
				var ids=node.id.split('＿');
				if(ids.length==2){
					var sql="select * from "+ids[0]+"."+ids[1];
					$("#sql_text").val(sql);
					service.runSql(e,sql,$(".result_list"));
				}
			}
			else{
				message.stop("请先选中树节点！");
			}
		});
		
		$(document).on("click",".desk_icon_reload",function(e){
			var node=$('.dataBaseTree').tree("getSelected");
			if(node!=null){
				$('.dataBaseTree').tree("reload",node.target);
			}
			else{
				message.stop("请先选中树节点！");
			}
		});

	},
	init:function(){
		message.small=true;//使用小图标提示
		service.bindDom();
	}
};

function close(){
	$('#panel_create_table').dialog("close");
}

$(function(){
	//service.init();
	message.small=true;
});






var $document=$(document);
$document.on("click","#bt_login_test,#bt_login_in",function(e){
	var id=$(this).attr("id");
	var data={
		"parameter.driver":"com.mysql.jdbc.Driver",
		"parameter.ip":$("#input_con_ip").val(),
		"parameter.port":$("#input_con_port").val(),
		"parameter.database":$("#input_con_database").val(),
		"parameter.user":$("#input_con_user").val(),
		"parameter.password":$("#input_con_password").val()
	};
	$.post("sql/testCon.action",data,function(result){
		if(typeof result=="object"){
			if(id=="bt_login_in"){
				service.bindTree();
				$('#menu_create_connection_box').window('close');
			}else{
				message.ok("连接成功！");
			}
		}else{
			console.error("连接失败！");
		}
	});
});

//右键-打开表
$document.on("click","#btn_run",function(e){
	var sql=$("#sql_text").val();
	if(sql==""){
		return message.error("系统提示：请输入sql语句");
	}
	service.exeSql($(".exe_result_list"),sql);
});

//点击运行按钮
$document.on("click","#context_table_open",function(e){
	var table=$($('.dataBaseTree').tree("getSelected").target).find(".tree-title").text();
	var sql="select * from "+table;
	service.runSql($(".show_table_list"),sql);
});

function fillValue1(){
	$("#input_con_ip").val("localhost");
	$("#input_con_port").val("3306");
	$("#input_con_database").val("test");
	$("#input_con_user").val("root");
	$("#input_con_password").val("root");
}

function fillValue(){
	$("#input_con_ip").val("10.17.82.155");
	$("#input_con_port").val("3306");
	$("#input_con_database").val("newest_edu3");
	$("#input_con_user").val("edu_jyg");
	$("#input_con_password").val("Zte@ict2012");
}

</script>