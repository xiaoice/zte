/**
 * 编辑器-QQ表情组件
 * 曾小斌
 */

$.fn.editor=function(options){
	var pos,node;
	var defaults={
		parent:".chat_warp",
		content:"#content",
		handle:".icon_qqfaces"
	};
	var option=$.extend({},defaults,options);
	var $document=$(document);
	
	var $iframe=$("#content").contents(),iframe=$iframe[0];
    iframe.designMode = 'on';
    iframe.open();
   	iframe.write('<link href="css/editor.css" type="text/css" rel="stylesheet" />');
    iframe.close();
    
    //发送消息
    $iframe.on("keydown","body",function(e){
		var keyCode = window.event ? e.keyCode : e.which;
		var value=$(".send_panel .icon-ok").attr("data-value");
		if(value=="return"&&keyCode==13 && !e.ctrlKey){
			service.sendMsg();
			return false;
		}else if(value=="return"&&e.keyCode==13 && e.ctrlKey){
			$("#content").focus();
			iframe.execCommand('InsertHtml', "", "<p></p>");
			return false;
		}else if(value=="Ctrl+return"&&e.keyCode==13 && e.ctrlKey){
			service.sendMsg();
			return false;
		}else if(value=="Ctrl+return"&&e.keyCode==13 && !e.ctrlKey){
			$("#content").focus();
			iframe.execCommand('InsertHtml', "", "<p></p>");
			return false;
		}else{
		}
    });
    
	var editor={
			panel:{
				faces:function(){
					var div='';
					div+='<div class="panel_qqfaces">';
					div+='<div class="panel_qqfaces_focus"><img/></div>';
					for(var i=0;i<9;i++){
						div+='<ul class="panel_qqfaces_ul">';
						for(var j=0;j<15;j++){
							div+='<li><div class="qqface" style="background-position:'+(i*15+j)*-24+'px 0;"></div></li>';
						}
						div+='</ul>';
					}
					div+='</div>';
					return div;
				}
			},
			icon:{
				
			},
			service:{
				
			}
	};
	$(option.parent).append(editor.panel.faces());
	
	//点击QQ表情
	$document.on("click",option.handle,function(){
		$(".panel_qqfaces").toggleClass("qqfaces_offset");
		//点击空白地方，隐藏表情面板
		$document.one("click",function(e){
			$(option.handle)[0]!=e.target&&$(".panel_qqfaces").removeClass("qqfaces_offset");
		});
	});
	
	//悬浮QQ表情上面
	$document.on("mouseover",".panel_qqfaces li",function(){
		var $this=$(this);
		var x=$this.index();
		var y=$this.parent("ul").index(".panel_qqfaces ul");
		var index=y*15+x,url="images/qqfaces/"+index+".gif?t="+new Date().getTime();
		var y_position=/[567]/.test(y)?"1px":0;
		var pos={top:y*26-1,left:x*28-1,background:"url("+url+") no-repeat scroll 2px "+y_position+" #fff"};
		$(".panel_qqfaces_focus").css(pos).attr("url",url).show();
	});
	
	//鼠标移出QQ表情面板
	$document.on("mouseleave",".panel_qqfaces",function(){
		$(".panel_qqfaces_focus").removeAttr("src").hide();
	});
	
	//点击QQ表情上面
	$document.on("click",".panel_qqfaces_focus",function(){
		var img=$(this).attr("url");
		$(".panel_qqfaces").removeClass("qqfaces_offset");
		iframe.execCommand('InsertImage', false, img);
		$("#content").focus();
	});
	
	//获取光标
	function getCursortPosition (ctrl,d) {
		var CaretPos = 0;	// IE Support
		if (d.selection) {
			ctrl.focus ();
			var Sel = d.selection.createRange ();
			Sel.moveStart ('character', -ctrl.value.length);
			CaretPos = Sel.text.length;
		}
		// Firefox support
		else if (ctrl.selectionStart || ctrl.selectionStart == '0')
			CaretPos = ctrl.selectionStart;
		return (CaretPos);
	}
	
	//设置光标
	function setCaretPosition(ctrl, pos){
		if(ctrl.setSelectionRange)
		{
			ctrl.focus();
			ctrl.setSelectionRange(pos,pos);
		}
		else if (ctrl.createTextRange) {
			var range = ctrl.createTextRange();
			range.collapse(true);
			range.moveEnd('character', pos);
			range.moveStart('character', pos);
			range.select();
		}
	}
	
}