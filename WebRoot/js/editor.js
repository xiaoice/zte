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
				getCurrentRange : function () {
					var sel = window.getSelection();
					if (sel.getRangeAt && sel.rangeCount) {
						return sel.getRangeAt(0);
					}
				},
				insertText:function(obj,str) {
					if (document.selection) {
						var sel = document.selection.createRange();
						sel.text = str;
					} else if (typeof obj.selectionStart === 'number' && typeof obj.selectionEnd === 'number') {
						var startPos = obj.selectionStart,
						endPos = obj.selectionEnd,
						cursorPos = startPos,
						tmpStr = obj.value;
						obj.value = tmpStr.substring(0, startPos) + str + tmpStr.substring(endPos, tmpStr.length);
						cursorPos += str.length;
						obj.selectionStart = obj.selectionEnd = cursorPos;
					} else {
						obj.value += str;
					}
				},
				moveEnd:function(obj){
					obj.focus();
					var val = obj.value||obj.innerHTML;
					var len=val.length;
					if (document.selection) {
						var sel = obj.createTextRange();
						sel.moveStart('character',len);
						sel.collapse();
						sel.select();
					} else if (typeof obj.selectionStart == 'number' && typeof obj.selectionEnd == 'number') {
						obj.selectionStart = obj.selectionEnd = len;
					}
				} 
			}
	};
	$(option.parent).append(editor.panel.faces());
	
	//点击QQ表情
	$document.on("click",option.handle,function(){
		$(".panel_qqfaces").toggleClass("qqfaces_offset");
		
		//点击空白地方，隐藏表情面板
		$document.one("click",function(e){
			$(".panel_qqfaces").removeClass("qqfaces_offset");
		})
	});
	
	//悬浮QQ表情上面
	$document.on("mouseover",".panel_qqfaces li",function(){
		var $this=$(this);
		var x=$this.index();
		var y=$this.parent("ul").index(".panel_qqfaces ul");
		var index=y*15+x;
		
		var pos={top:0};
		if(x<8){
			pos.right=0;
		}else{
			pos.left=0;
		}
		$(".panel_qqfaces_focus").removeAttr("style").show().css(pos);
		$(".panel_qqfaces img").attr("src","images/qqfaces/"+index+".gif");
	});
	
	//鼠标移出QQ表情面板
	$document.on("mouseleave",".panel_qqfaces",function(){
		$(".panel_qqfaces").hide();
	});
	
	//点击QQ表情上面
	$document.on("click",".panel_qqfaces li",function(){
		var img=$(".panel_qqfaces_focus").html();
		$(".panel_qqfaces").removeClass("qqfaces_offset");
		$(option.content).append(img);
	});
	
}