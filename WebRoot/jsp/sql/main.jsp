<%@page language="java" contentType="text/html; charset=UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>连接中心</title>
	<%-- <%@include file="/jsp/common/easyui.jsp"%> --%>
</head>
<body class="easyui-layout">
	<!-- <div region="south" split="true" style="height:0px;background:#cbcbcb;"></div> -->
	<div region="west" split="true" style="width:260px;border-top-width:0;"><ul class="dataBaseTree" class="easyui-tree" ></ul></div>
	<div region="north" class="menu_tool">
	    <a href="javascript:void(0)" id="menu_top_file" class="easyui-menubutton" data-options="menu:'#menu_down_file'">文件</a>
	    <a href="javascript:void(0)" id="menu_top_tool" class="easyui-menubutton">工具</a>
	    <div id="menu_down_file" style="width:150px;">
		    <div data-options="iconCls:'icon-add'" onclick="$('#window_create_connection').window('open');fillValue();">新建连接</div>
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
						<div class="cmd_toolBar"><a title="因为浏览器性能限制，若查询语句未加分页，默认每100条数据进行分页" position="right" class="easyui-linkbutton easyui-tooltip" plain="true" iconCls="icon-play" id="btn_run">&nbsp;运行</a></div>
			    		<textarea id="sql_text" class="sql_text" rows="0" cols="0">select * from um_usercourse</textarea>
			    	</div>
					<div region="center" border="false" class="exe_result_list">
						<div class="msg_tip"></div>
						<div class="result_table"></div>
					</div>
				</div>
		    </div>  
		</div>
	</div>
	
	<div id="base_data" class="hidden">
	    <!-- 表节点上面点击右键菜单 -->
		<div id="context_table" class="easyui-menu">
	        <div iconCls="icon-search" id="context_table_open">打开表</div>
	        <div iconCls="icon-edit" id="context_table_edit">修改表</div>
	        <div class="menu-sep"></div>
	        <div iconCls="icon-reload" class="context_table_reload">刷新</div>
	    </div>
	    
		<!-- 菜单-文件 -创建新连接按钮 -->
		<div id="window_create_connection" class="window_create_connection easyui-window" title="创建新连接" 
			 data-options="iconCls:'icon-save',closed:true,collapsible:false,maximizable:false,minimizable:false,resizable:false" >
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
		
		<!-- 菜单-文件 -修改表弹出框 -->
		<div id="window_edit_table" class="window_edit_table">
	 	    <span id="datagrid_edit_table" class="datagrid"></span>
		</div>
	</div>
	
	<div class="body_loading"></div>
</body>
</html>
<script src="../js/sea-debug.js"></script>
<script type="text/javascript">

var service={
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
	runSql:function(that,sql,callback){
		if(that.find(".table_datagrid").size()==0){
			that.find(".result_table").html(service.createTable());
		}
		var datagrid=that.find(".table_datagrid");
		var treeTable=$('.dataBaseTree').tree("getSelected");
		datagrid.datagrid({
	        url:"sql/findTableData.action",
	        queryParams: {"parameter.sql":sql},
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
	        	if(result.total==0){
	        		that.find(".result_table").html("<div class=\"no-result\">表中没有数据！</div>");
	        	}
	        	
	        	if(result.total<=10){
	        		that.find(".datagrid-pager").hide();
	        	}else{
	        		that.find(".datagrid-pager").show();
	        	}
	        	$(".conn_content").tabs("select","列表展示");
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
		message.wait("正在运行sql，请稍后...");
		$.post("sql/selectQuerySql.action",{"parameter.sql":sql,"page":pageIndex||"1","rows":pageSize},function(result){
			if(typeof result=="object" && typeof result.data=="object"){
				that.find(".msg_tip").html("");
				that.find(".result_table").empty().show();
				message.hide();
				if(that.find(".table_datagrid").size()==0){
					that.find(".result_table").html(service.createTable());
				}
				
				if(result.recode==1){
					if(result.data.total==0){
						return that.find(".msg_tip").html("<div class=\"no-result\">表中没有数据！</div>");
					}
				}else{
					return that.find(".msg_tip").html("<div class=\"no-result\">"+result.message+"</div>");
				}
				
				var fields=[];
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
			    		var opts = datagrid.datagrid('options');
			    		var pager = datagrid.datagrid('getPager');
			    		pager.pagination({
			    			onSelectPage:function(pageNum, pageSize){
			    				opts.pageNumber = pageNum;
			    				opts.pageSize = pageSize;
			    				pager.pagination('refresh',{
			    					pageNumber:pageNum,
			    					pageSize:pageSize
			    				});
			    				message.wait("正在加载，请稍后...");
			    				service.exeSql(that,sql,pageNum,pageSize,function(result){
			    					message.hide();
			    					datagrid.datagrid('loadData',result);
			    				});
			    			}
			    		});
			    		return data;
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
			    }).datagrid('loadData', result.data||result);
				datagrid.datagrid('getPager').pagination({
					layout:['sep','first','prev','sep',"links",'sep','next','last','sep','refresh'],
					displayMsg:"当前第[{from}-{to}]条 共[{total}]条"
				});
				callback&&callback(result.data||result);
			}else{
		    	message.stop("系统出现错误！");
				return that.find(".msg_tip").show().html("<div class=\"no-result\">系统出现错误，"+result.message+"</div>");
		    }
		});
	},
	bindDom:function(){
		var $document=$(document);
		//登录、测试连接
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
			message.wait("正在测试");
			$.post("sql/testCon.action",data,function(result){
				if(typeof result=="object"){
					if(id=="bt_login_in"){
						service.bindTree();
						$('#window_create_connection').window('close');
					}else{
						if(result.recode==1){
							message.ok("连接成功！");
						}else{
							message.error("连接失败！");
						}
					}
				}else{
					console.error("连接失败！");
				}
			});
		});
		
		//运行Sql
		$document.on("click","#btn_run",function(e){
			var sql=service.getRangeById("sql_text")||$("#sql_text").val();
			if(sql==""){
				return message.error("系统提示：请输入sql语句");
			}
			service.exeSql($(".exe_result_list"),sql,1,100);
		});
		
		//右键-修改表
		$document.on("click","#context_table_edit",function(e){
			var datagrid=$("#datagrid_edit_table").empty();
			var treeTable=$('.dataBaseTree').tree("getSelected");
			var data=[];
			for(var i in treeTable.children){
				data.push(treeTable.children[i].attributes);
			}
			datagrid.datagrid({
				width:"100%",
				rownumbers:true,
				singleSelect:true,
		        columns:[[
			        {field:'Field',title:'字段名',width:150},
			        {field:'Type',title:'字段类型',width:90,align:"center"},
			        {field:'Extra',title:'自增',width:100,align:"center",formatter:function(val){return val=="auto_increment"?"<i class=\"icon-ok\"></i>":val;}},
			        {field:'Null',title:'可空',width:40,align:"center",formatter:function(val){return val=="YES"?"<i class=\"icon-ok\"></i>":(val=="NO"?"":val);}},
			        {field:'Collation',title:'Collation',width:100,align:"center",formatter:formatNull,hidden:true},
			        {field:'Key',title:'键约束',width:50,align:"center"},
			        {field:'Privileges',title:'权限',width:190,align:"center",hidden:true},
			        {field:'Default',title:'默认值',width:100,align:"center",formatter:formatNull},
			        {field:'Comment',title:'注释',width:108},
			        {field:'action',title:'操作',width:70,align:'center',    
		                formatter:function(value,row,index){    
		                    if (row.editing){    
		                        var s = '<a href="#" onclick="saverow('+index+')">Save</a> ';    
		                        var c = '<a href="#" onclick="cancelrow('+index+')">Cancel</a>';    
		                        return s+c;    
		                    } else {    
		                        var e = '<a href="#" onclick="editrow('+index+')">Edit</a> ';
		                        var d = '<a href="#" onclick="deleterow('+index+')">Delete</a>';
		                        return e+d;
		                    }    
		                }    
		            }
		        ]]
		    }).datagrid('loadData', data);
			$("#window_edit_table").window("open");
		});
		
		//点击运行按钮
		$document.on("click","#context_table_open",function(e){
			var table=$($('.dataBaseTree').tree("getSelected").target).find(".tree-title").text();
			var sql="select * from "+table;
			service.runSql($(".show_table_list"),sql);
		});
	},
	init:function(){
		message.small=true;//使用小图标提示
		service.bindDom();
		
		$('#window_edit_table').dialog({
		    title: '修改表结构',
		    cache: false,
		    modal: true,
		    iconCls:'icon-edit',
		    closed:true,
		    collapsible:false,
		    maximizable:false,
		    minimizable:false,
		    resizable:false,
		    width:'1000',
		    height:$("body").height()-30,
		    toolbar:[{
					text:'增加',
					handler:function(){
						var datagrid=$("#datagrid_edit_table");
						var input="<input class=\"datagrid_input\" type=\"text\"/>";
						//var column_type="<select class=\"datagrid_input\">"+$("#table_column_type").html()+"</select>";
						$("#datagrid_edit_table").datagrid("appendRow", {Field:input,Type:input,Extra:input,Null:input,Collation:input,Key:input,Privileges:input,Default:input,Comment:input});
					}
				},'-',{
					text:'修改',
					handler:function(){alert('cut')}
				},'-',{
					text:'删除',
					handler:function(){alert('save')}
				}
			]
		});
		
		$("<div id=\"base_database_type\"></div>").appendTo("#base_data").load("jsp/sql/base_database_type.html");
	}
};


//格式化空对象
function formatNull(value){
	if($.isEmptyObject(value)){
		return "<i>null</i>";
	}else{
		return value;
	}
}

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


seajs.config({
  base: "../js",
  paths: {
	 'plugins': 'plugins',
	 'sql': 'plugins/sql'
  },
  alias: {
    "jquery": "jquery.min.js",
    "bootstrapCss": "plugins/bootstrap/css/bootstrap.min.css",
    "bootstrap": "plugins/bootstrap/js/bootstrap.min.js",
    "font-awesome": "plugins/bootstrap/css/font-awesome.css",
    "easyuiCss-black": "plugins/jquery-easyui/themes/black/easyui.css",
    "easyui": "plugins/jquery-easyui/jquery.easyui.min.js",
    "message": "plugins/message/message.js",
    "messageCss": "plugins/message/message.css",
    "sqlCss": "plugins/sql/css/sql.css",
    "sql": "plugins/sql/js/sql.js",
    "table": "plugins/sql/js/table.js"
  }
});

// 加载入口模块
seajs.use(["jquery","sql"],function(a,b){
	console.log(a,b);
});
</script>