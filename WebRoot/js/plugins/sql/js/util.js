/**
 * 工具类组件
 * 曾小斌
 * 2013-12-31 15:39:15
 */

define(function(require,exports,module){
     //延时加载
     module.exports.processor={
     	timeoutId:null,
     	perProcessing:function(){
     		
     	},
     	process:function(callback,timeout){
     		clearTimeout(this.timeoutId);
     		var that=this;
     		this.timeoutId=setTimeout(function(){
     			that.perProcessing();//执行真正的方法  这里为空方法
     			if(typeof callback=="function"){
     				callback();
     			}
     		},timeout||100);
     	}
     };
});
