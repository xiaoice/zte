/**
 * 修改表、添加表操作
 * 曾小斌
 * 2013-12-31 15:39:15
 */

define(function(require,exports,module){
	var util=require("util"),message=util.message,windows=require("windows");
	var index_request=null,index_request_stop=false;
	module.exports.init=function(callback){
		bindDrag($(".panel_wrap").removeClass("window_warp_model"));
		findUserListAjax();
		callback&&callback();
	};
	
	//绑定拖动事件
	function bindDrag(target){
		target.resizable({minHeight:500,minWidth:300,handles: "n, e, s, w, ne, se, sw, nw"}).draggable({
			handle:target.find(".panel_head"),
			containment: "document",
			scroll: false,
			start: function(event, ui) {
				console.log(event, ui);
			},
			stop: function(event, ui) {
				console.log(event, ui);
			}
		});
	}
	
	//查找用户列表信息
	function findUserListAjax(callback){
		if(index_request!=null){
			index_request.abort();
		}
		index_request=$.get("../user/findUserListAjax.action",function(result){
			//$("#friendList").html(service.createFriendDiv(result.data.friendList));
			//$("#stranger").html(service.createStrangerDiv(result.data.stranger));
			bindAnt(result.data.friendList,result.data.stranger);
			var COUNT=result.data.count;
			for(var i in COUNT){
				var count=COUNT[i].count,friendId=COUNT[i].friendId;
				var $user=$("#user_"+friendId);
				if($user.size()>0){
					$user.html(count).show();
				}else{
					$user.hide();
				}
			}
			callback&&callback();
			/*if(!index_request_stop){
				setTimeout(function(){
					service.findUserListAjax();
				},5000);
			}*/
		});
	}
	
	//加入ant组件
	function bindAnt(friendList,stranger){
		var ant = new Ant($(".panel_wrap .panel_body_ui")[0], {
			  data: {friendList:friendList},
			  events: {
				  'click li': function(e) {
					  var index=e.currentTarget.$index, name = this.data.friendList[index].user.name,_id="windows_"+index,_$id="#"+_id;
					  //若这个对话框不存在，则创建，确保唯一性
					  if($(_$id).size()==0){
						  var model_html=$('.window_warp_model').clone().removeClass("window_warp_model")[0].outerHTML;
						  var el=new Ant(model_html, {data: {name: name, msgs: [],time:"正在发送"},
							  events: {'click .send_msg': function() {
								  this.data.msgs.push({content: this.data.text});
							  	}
							  }
						  }).el;
						  var $el = $(el).appendTo("body").find(".window_warp").attr("id",_id);
						  windows.bindDrag($el);
						  $el.on("click",".windows_close",function(){
							  $(this).parents(".window_warp").parent("div").remove();
						  });
					  }
				  }
			  }
		});
	}
	
});