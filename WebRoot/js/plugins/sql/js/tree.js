/**
 * 数据表组件
 * 曾小斌
 * 2014-01-01 00:00:23
 */

define(function(require,exports,module){
	var sql=require("sql");
	var message=require("message");
	var $document=$(document);
	var editIndex = undefined; //当前编辑的行索引
	var editRows={},delRows={};//修改的行、删除的行
	
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
				sql.selectTable($(".show_table_list"),node.text);
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
			sql.selectTable($(".show_table_list"),table);
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
					handler:appendRow
				},'-',{
					text:'删除',
					handler:deleteRow
				}
			],
			buttons:[{
				text:'预览Sql',
				handler:preview
			},{
				text:'保存',
				handler:function(){}
			},{
				text:'取消',
				handler:function(){}
			}]
		});
		
		//重写扩展输入框-默认值
	    $.extend($.fn.datagrid.defaults.editors, {
	    	textDefault: {
		        init: function(container, options){
			        var input = $('<input type="text" class="datagrid-editable-input"/>').width("100%").css("border-color","#805C1E").appendTo(container);
			        return input.wrap("<div class=\"datagrid-editable-input-div\"></div>").validatebox(options);
		        },
		        destroy: function(target){
		        	$(target).remove();
		        },
		        getValue: function(target){
		        	var value=$(target).val();
		        	return $.isEmptyObject(value)?"":value;
		        },
		        setValue: function(target, value){
		        	$(target).val($.isEmptyObject(value)?"":value);
		        },
		        resize: function(target, width){
			        $(target)._outerWidth(width);
			    }
	        }
	    });
		
		//格式化工具
		var formatUtil={
			//是否自增
			Extra:function(val){
				return val=="auto_increment"?"<i class=\"icon-ok\"></i>":val;
			},
			//是否为空
			Null:function(val){
				return val=="YES"?"<i class=\"icon-ok\"></i>":(val=="NO"?"":val);
			},
			//键约束
			Key:function(val){
				return val=="PRI"?"主键":(val=="UNI"?"唯一":val);
			},
			//格式化空对象
			formatEmpty:function(value){
				if($.isEmptyObject(value)){
					return "";
				}else{
					return value;
				}
			}
		};
		
		//编辑器工具
		var editorUtil={
			//字段名
			Field:{
				type:'textDefault',
                options:{
                	required:true,
                	missingMessage:"请输入字段名称"
                }
			},
			//字段类型
			Type:{
				type:'combobox',
                options:{
                    valueField: 'value',
            		textField: 'text',
            		height:24,
             		panelHeight:"auto",
            		data: [
            		    {value: 'int',text: 'int'},
            		    {value: 'float',text: 'float'},
            		    {value: 'double',text: 'double'},
            		    {value: 'bit',text: 'bit'},
            		    {value: 'varchar()',text: 'varchar()'},
            		    {value: 'char()',text: 'char()'},
            		    {value: 'text',text: 'text'},
            		    {value: 'blob',text: 'blob'},
            		    {value: 'date',text: 'date'},
            		    {value: 'datetime',text: 'datetime'},
            		],
                    required:true,
                    missingMessage:"请输入字段类型"
                }
			},
			//键约束
			Key:{
				type:'combobox',
                options:{
                    valueField: 'key',
            		textField: 'text',
            		height:24,
            		panelHeight:"auto",
            		data: [
            		    {value: '',text: '自定义'},
            		    {value: 'PRI',text: '主键'},
            		    {value: 'UNI',text: '唯一'}
            		]
                }
			},
			//复选框
			checkbox:{
				type:'checkbox',    
				options:{on: "<i class=\"icon-ok\"></i>",off:""}
			},
			//允许为null
			comboboxNull:{
				type:'combobox',
                options:{
                    valueField: 'value',
            		textField: 'text',
            		height:24,
            		panelHeight:"auto",
            		data: [
            		    {value: '',text: '自定义'},
            		    {value: 'null',text: 'null'}
            		]
                }
			},
		};
		
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
				onClickRow: editRow,
		        columns:[[
			        //{checkbox:true,width:32},
			        {field:'Field',title:'字段名',width:150,editor:editorUtil.Field},
			        {field:'Type',title:'字段类型',width:110,align:"center",editor:editorUtil.Type},
			        {field:'Extra',title:'自增',width:100,align:"center",formatter:formatUtil.Extra,editor:editorUtil.checkbox},
			        {field:'Null',title:'可空',width:40,align:"center",formatter:formatUtil.Null,editor:editorUtil.checkbox},
			        {field:'Collation',title:'Collation',width:100,align:"center",hidden:true},
			        {field:'Key',title:'键约束',width:65,align:"center",formatter:formatUtil.Key,editor:editorUtil.Key},
			        {field:'Privileges',title:'权限',width:190,align:"center",hidden:true},
			        {field:'Default',title:'默认值',width:100,align:"center",formatter:formatUtil.formatEmpty,editor:'textDefault'},
			        {field:'Comment',title:'注释',width:108,editor:'textDefault'}
		        ]],
		        onAfterEdit:function(rowIndex, rowData, changes){
		        	editRows[rowData.Field]=changes;
		        }
		    }).datagrid('loadData', data);
			$("#window_edit_table").window("open");
		});
	}
	
	//结束编辑状态
	function endEditing(){
		if (editIndex == undefined){return true}
		if (datagrid.datagrid('validateRow', editIndex)){
			datagrid.datagrid('endEdit', editIndex);
			editIndex = undefined;
			return true;
		} else {
			return false;
		}
	}
	
	//修改行
	function editRow(rowIndex){
		if(endEditing()){
			datagrid.datagrid('endEdit', editIndex);
			datagrid.datagrid('selectRow', rowIndex).datagrid('beginEdit', rowIndex);
			editIndex=rowIndex;
		}else{
			datagrid.datagrid('selectRow', editIndex);
		}
	}
	
	//增加行
	function appendRow(rowIndex){
		if(endEditing()){
			var rowCount = datagrid.datagrid('getRows').length;
			datagrid.datagrid('endEdit', editIndex);
			datagrid.datagrid("appendRow", {Field:"",Type:"",Extra:"",Null:"",Collation:"",Key:"",Privileges:"",Default:"",Comment:""});
			datagrid.datagrid('selectRow', rowCount).datagrid('beginEdit', rowCount);
			editIndex=rowCount;
		}
	}
	
	//删除行
	function deleteRow(){
		var rowData=datagrid.datagrid('getSelected');
		if(rowData!=null){
			datagrid.datagrid('deleteRow', datagrid.datagrid('getRowIndex',rowData));
			delRows[rowData.Field]=rowData;
		}else{
			message.error("请先选中要删除的行");
		}
	}
	
	//预览SQL
	function preview(){
		if(endEditing()){
			datagrid.datagrid('unselectAll');
			//var rows = datagrid.datagrid('getChanges');
			console.log(editRows,delRows);
			message.error("暂未开发，开发难度太高了");
		}
	}
	
});