/**
 * 修改表、添加表操作
 * 曾小斌
 * 2013-12-31 15:39:15
 */

define(function(require,exports,module){
	var $document=$(document);
	module.exports.init=function(){
		$document.on("click","#menu_down_connection",function(){
			$('#window_create_connection').window('open');
		});
	};
});