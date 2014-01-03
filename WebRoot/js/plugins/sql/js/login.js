/**
 * 连接数据库组件
 * 曾小斌
 * 2013-12-31 15:39:15
 */

define("login",[],function(require,exports,module){
	var tree=require("tree");
	var message=require("message");
	var dialog=$('#window_create_connection');
	var $document=$(document);
	module.exports.init=function(callback){
		
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
	    
	};
	
	//测试连接
	$document.on("click","#bt_login_test",function(e){
		loginDatabase(function(result){
			if(result.recode==1){
				message.ok("连接成功！");
			}else{
				message.error("连接失败！");
			}
		});
	});
	
	//点击登录
	$document.on("click","#bt_login_in",function(e){
		loginDatabase(function(){
			tree.init();
			dialog.window('close');
		});
	});
	
	var loginInfo={
			"parameter.driver":"com.mysql.jdbc.Driver",
			"parameter.ip":$("#input_con_ip").val(),
			"parameter.port":$("#input_con_port").val(),
			"parameter.charset":$("#input_con_charset").val(),
			"parameter.user":$("#input_con_user").val(),
			"parameter.password":$("#input_con_password").val(),
			"parameter.database":$("#input_con_database").val()
	};
	
	//登录系统
	function loginDatabase(callback){
		message.wait("正在连接");
		$.post("sql/testCon.action",loginInfo,function(result){
			if(typeof result=="object"){
				callback&&callback(result);
			}else{
				console.error("连接失败！系统错误！");
			}
		});
	}
	
	//点击取消按钮
	$document.on("click","#bt_login_cancel",function(e){
		dialog.window('close');
	});
	
	//打开登录提示框
	module.exports.open=function(){
		$('#window_create_connection').window('open');
	};
	//对外接口
	module.exports.target=dialog;
	module.exports.loginInfo=loginInfo;
	module.exports.loginDatabase=loginDatabase;
});