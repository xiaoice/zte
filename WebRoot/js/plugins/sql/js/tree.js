/**
 * 数据表组件
 * 曾小斌
 * 2014-01-01 00:00:23
 */

define(function(require,exports,module){
	var sql=require("sql");
	var message=require("message");
	var $document=$(document);
	
	module.exports.target=$('.dataBaseTree');
	var datagrid=$("#datagrid_edit_table");
	module.exports.init=function(callback){
		bindDomEvent();
	};
	
	//绑定事件
	function bindDomEvent(){
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
				sql.runSql($(".show_table_list"),node.text);
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
				sql.init();
			}
		});
		
		//右键-打开表
		$document.on("click","#context_table_open",function(e){
			var table=$($('.dataBaseTree').tree("getSelected").target).find(".tree-title").text();
			sql.runSql($(".show_table_list"),table);
		});
		
		//初始化表结构弹出框
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
			],
			buttons:[{
				text:'预览Sql',
				handler:function(){}
			},{
				text:'保存',
				handler:function(){}
			},{
				text:'取消',
				handler:function(){}
			}]
		});
		
		var formatUtil={
			//是否自增
			Extra:function(val){
				return val=="auto_increment"?"<i class=\"icon-ok\"></i>":val;
			},
			//是否为空
			Null:function(val){
				return val=="YES"?"<i class=\"icon-ok\"></i>":(val=="NO"?"":val);
			},
			//复选框
			checkbox:{
				type:'checkbox',    
				options:{on: "<i class=\"icon-ok\"></i>",off:""}
			}
		}
		
		//右键-修改表
		$document.on("click","#context_table_edit",function(e){
			datagrid.empty();
			var treeTable=$('.dataBaseTree').tree("getSelected");
			var data=[];
			for(var i in treeTable.children){
				data.push(treeTable.children[i].attributes);
			}
			datagrid.datagrid({
				width:"100%",
				rownumbers:true,
				singleSelect:true,
				onClickCell: onClickCell,
		        columns:[[
			        //{checkbox:true,width:32},
			        {field:'Field',title:'字段名',width:150,editor:'text'},
			        {field:'Type',title:'字段类型',width:90,align:"center",editor:'text'},
			        {field:'Extra',title:'自增',width:100,align:"center",formatter:formatUtil.Extra,editor:formatUtil.checkbox},
			        {field:'Null',title:'可空',width:40,align:"center",formatter:formatUtil.Null,editor:formatUtil.checkbox},
			        {field:'Collation',title:'Collation',width:100,align:"center",formatter:formatNull,hidden:true},
			        {field:'Key',title:'键约束',width:50,align:"center",editor:'text'},
			        {field:'Privileges',title:'权限',width:190,align:"center",hidden:true},
			        {field:'Default',title:'默认值',width:100,align:"center",formatter:formatNull,editor:'text'},
			        {field:'Comment',title:'注释',width:108,editor:'text'},
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
	}
	
	//格式化空对象
	function formatNull(value){
		if($.isEmptyObject(value)){
			return "<i>null</i>";
		}else{
			return value;
		}
	};
	
	 
	 function endEditing(){
		if (editIndex == undefined){return true}
		if (datagrid.datagrid('validateRow', editIndex)){
			console.log(editIndex);
			var ed = datagrid.datagrid('getEditor', {index:editIndex,field:'productid'});
			//var productname = $(ed.target).combobox('getText');
			//datagrid.datagrid('getRows')[editIndex]['productname'] = productname;
			datagrid.datagrid('endEdit', editIndex);
			editIndex = undefined;
			return true;
		 } else {
			return false;
		 }
	 }
	 
	//双击datagrid单元行
	function onClickRow(rowIndex, rowData){
		/* if (editIndex != index){
			 if (endEditing()){
				 datagrid.datagrid('selectRow', index).datagrid('beginEdit', index);
				 editIndex = index;
			 } else {
				 datagrid.datagrid('selectRow', editIndex);
			 }
		 }*/
		datagrid.datagrid('endEdit', rowIndex);
		datagrid.datagrid('selectRow', rowIndex).datagrid('beginEdit', rowIndex);
		var ed = $(this).datagrid('getEditor', {index:rowIndex,field:Field});
		$(ed.target).focus();
	};
	
	$.extend($.fn.datagrid.defaults.editors, {    
	    checkbox1: {    
	        init: function(container, options){    
	            var input = $('<input type="text">').appendTo(container);    
	            return input.numberspinner(options);    
	        },    
	        destroy: function(target){    
	            $(target).numberspinner('destroy');    
	        },    
	        getValue: function(target){    
	            return $(target).numberspinner('getValue');    
	        },    
	        setValue: function(target, value){    
	            $(target).numberspinner('setValue',value);    
	        },    
	        resize: function(target, width){    
	            $(target).numberspinner('resize',width);    
	        }    
	    }
	}); 
	
	var editIndex = undefined;
	function onClickCell(rowIndex, field, value){
		datagrid.datagrid('endEdit', editIndex);
		$("#datagrid_edit_table").datagrid('selectRow', rowIndex).datagrid('beginEdit', rowIndex);
		editIndex=rowIndex;
		//var ed = $(this).datagrid('getEditor', {index:rowIndex,field:field});
		//$(ed.target).focus();
	}
});