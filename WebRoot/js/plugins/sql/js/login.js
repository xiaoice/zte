/**
 * 连接数据库组件
 * 曾小斌
 * 2013-12-31 15:39:15
 */

define("login",[],function(require,exports,module){
	var tree=require("tree");
	var message=require("message");
	module.exports.target=$('#window_create_connection');
	module.exports.init=function(callback){
		var $document=$(document);
		
		//初始化登录弹出框
	    $('#window_create_connection').window({
	        iconCls:'icon-retweet',
	        closed:false,
	        collapsible:false,
	        maximizable:false,
	        minimizable:false,
	        resizable:false,
	        modal:true,
	        onOpen:function(){
	        	callback&&callback();
	        }
	    });
	    
		//点击登录、测试连接
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
			message.wait("正在连接");
			$.post("sql/testCon.action",data,function(result){
				if(typeof result=="object"){
					if(id=="bt_login_in"){
						tree.init();
						module.exports.target.window('close');
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
		
		//点击取消按钮
		$document.on("click","#bt_login_cancel",function(e){
			module.exports.target.window('close');
		});
	};
	
	//打开登录提示框
	module.exports.open=function(){
		$('#window_create_connection').window('open');
	};
});