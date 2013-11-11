
var $document=$(document);
var editor={
	panel:{
		faces:function(){
			var div='';
			div+='<div class="tool_icon_qqfaces">';
			div+='<div class="tool_icon_focus"><img/></div>';
			for(var i=0;i<9;i++){
				div+='<ul class="tool_icon_qqfaces_ul">';
				for(var j=0;j<15;j++){
					div+='<li><div class="qqface" style="background-position:'+(i*15+j)*-24+'px 0;"></div></li>';
				}
				div+='</ul>';
			}
			div+='</div>';
			return div;
		}
	}
};

$(function(){
	$(".chat_warp").append(editor.panel.faces());
});

//点击QQ表情
$document.on("click","#tool_icon_qqfaces",function(){
	$(".tool_icon_qqfaces").toggleClass("qqfaces_offset");
});

//悬浮QQ表情上面
$document.on("mouseover",".tool_icon_qqfaces li",function(){
	var $this=$(this);
	var x=$this.index();
	var y=$this.parent("ul").index(".tool_icon_qqfaces ul");
	var index=y*15+x;
	
	var pos={top:0};
	if(x<8){
		pos.right=0;
	}else{
		pos.left=0;
	}
	$(".tool_icon_focus").removeAttr("style").show().css(pos);
	$(".tool_icon_focus img").attr("src","images/qqfaces/"+index+".gif");
});

//鼠标移出QQ表情面板
$document.on("mouseleave",".tool_icon_qqfaces",function(){
	$(".tool_icon_focus").hide();
});

//点击QQ表情上面
$document.on("click",".tool_icon_qqfaces li",function(){
	var img=$(".tool_icon_focus").html();
	$(".tool_icon_qqfaces").removeClass("qqfaces_offset");
	$("#content").append(img);
});