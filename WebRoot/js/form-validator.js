/*
 * name:表达验证插件
 * auth:曾小斌
 * version:1.0.0
 * createdate:2013-11-15
 */

$.fn.formValidator=function(options){
	//缓存当前DOM对象
	var $document=$(document),$this=$(this),$target=$this.parents("td").next(),selector=$this.selector;
	//临时全局变量
	var temp={};
	
	//图标类型
	var icon={
		info:"icon-info-sign text-info",
		warn:"icon-warning-sign text-warning",
		ok:"icon-ok-sign text-success",
		error:"icon-remove-sign text-danger",
		wait:"icon-spinner icon-spin text-warning"
	};
	var option={
		show:"",
		focus:"",
		success:"",
		validator:{min:0,max:1000,error:""},
		compare:{des:null,regexp:null,error:""},
		ajax:{url:null,wait:"",error:""}
	};
	option=$.extend({},option,options);
	
	//封装html文字
	var html=function(icon,text){
		var html_icon='<i class="'+icon+'"></i>';
		var html_text='<span>'+text+'</span>';
		$target.html(html_icon+html_text);
	};
	
	//获取焦点事件
	$document.on("focus",selector,function(){
		if(stringUtil.isNotNull(option.focus)){
			html(icon.info,option.focus);
		}
	});
	
	//获取
	$document.on("blur",selector,function(){
		var value=$this.val(),len=value.length;
		console.log(len,option.validator,typeof option.validator.min,len<option.validator.min);
		if(len<option.validator.min || len>option.validator.max){
			return html(icon.error,option.validator.error);
		}
		else if(stringUtil.isNotNull(option.compare.des)){
			alert(1);
		}else if(stringUtil.isNotNull(option.ajax.url)){
			$.get("",function(data){
				
			});
			return html(icon.wait,option.ajax.wait);
		}else{
			return html(icon.ok,option.success);
		}
	});
	
};

//工具类
var stringUtil={
	isNull:function(val){
		return val==undefined||val=="null"||val==null;
	},
	isNotNull:function(val){
		return !stringUtil.isNull(val);
	}
};