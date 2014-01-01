/**
 * 消息弹出框组件
 * 曾小斌
 * 2014-01-01 00:54:31
 */
define(["messageCss"],function(require,exports,module){
	var util=require("util");
	var message={
		self:null,
		color:"green",	//字体颜色
		small:true,	//是否为小图标模式
		//创建消息框
		create:function(){
			if(this.self==null){
				var msg='<div class="message_box">';
				msg+='<div class="message_content"><span class="message_icon"></span><span class="message_text"></span></div></div>';
				this.self=$(msg).appendTo("body");
			}
			return this.self;
		},
		setOffset:function(){
			var documentWidth = $(document).width();
			var documentHeight = $(document).height();
			var width=this.self.outerWidth();
			var height=this.self.outerHeight();
			var left=(documentWidth-width)/2;
			var top=(documentHeight-height)/2;
			this.self.css({"top":top,"left":left});
			return this.self;
		},
		setContent:function(text,icon){
			this.create();
			if($(".message_content").hasClass("message_content_wait")){
				$(".message_content_wait").removeClass("message_content_wait");
			}
			$(".message_icon").removeAttr("class").addClass("message_icon icon_message_"+icon);
			$(".message_text").text(text).css("color",this.color);
			this.setOffset();
			if(this.small==true){
				this.self.removeAttr("class").addClass("message_box_small");
			}else{
				this.self.removeAttr("class").addClass("message_box");
			}
			return this.self;
		},
		show:function(text,icon,timeout){
			message.setContent(text,icon).show();
			util.processor.process(function(){
				message.hide();
			},timeout||2000);
		},
		hide:function(){
			message.self.fadeOut("slow");
		},
		ok:function(text,timeout){
			this.color="green";
			this.show(text,"ok",timeout);
		},
		error:function(text,timeout){
			this.color="red";
			this.show(text,"error",timeout);
		},
		warn:function(text,timeout){
			this.color="#9E9E29";
			this.show(text,"warn",timeout);
		},
		stop:function(text,timeout){
			this.color="#8f0000";
			this.show(text,"stop",timeout);
		},
		info:function(text,timeout){
			this.color="#3250D3";
			this.show(text,"info",timeout);
		},
		wait:function(text,timeout){
			this.color="#D86F29";
			this.show(text,"wait",timeout||30000);
			$(".message_content").addClass("message_content_wait");
		}
	};
	module.exports={
		show:message.show,
		hide:message.hide,
		ok:message.ok,
		error:message.error,
		warn:message.warn,
		stop:message.stop,
		info:message.info,
		wait:message.wait
	};
})

