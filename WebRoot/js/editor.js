/**
 * 编辑器-QQ表情组件
 * 曾小斌
 */

$.fn.editor=function(options){
	var defaults={
		parent:".chat_warp",
		content:"#content",
		handle:".icon_qqfaces"
	};
	var option=$.extend({},defaults,options);
	var $document=$(document);
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
		$(".panel_qqfaces img").attr("src","");
		var $this=$(this);
		var x=$this.index();
		var y=$this.parent("ul").index(".panel_qqfaces ul");
		var index=y*15+x;
		
		var pos={top:y*26-1,left:x*28-1};
		$(".panel_qqfaces img").attr("src","images/qqfaces/"+index+".gif?t="+new Date().getTime());
		$(".panel_qqfaces_focus").removeAttr("style").show().css(pos);
	});
	
	//鼠标移出QQ表情面板
	$document.on("mouseleave",".panel_qqfaces",function(){
		$(".panel_qqfaces_focus").removeAttr("src");
		$(".panel_qqfaces").hide();
	});
	
	//点击QQ表情上面
	$document.on("click",".panel_qqfaces_focus",function(){
		var img=$(this).find("img").attr("src");
		$(".panel_qqfaces").removeClass("qqfaces_offset");
		document.execCommand("InsertImage", false, img); 
		$(option.content).focus();
		//$(option.content).append(img);
	});
	
}