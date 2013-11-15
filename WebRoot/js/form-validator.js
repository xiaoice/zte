/*
 * name:表达验证插件
 * auth:曾小斌
 * version:1.0.0
 * createdate:2013-11-15
 */

$.fn.formValidator=function(options){
	//缓存当前DOM对象
	var $document=$(document),$this=$(this),$target=$this.parents("td").next(),selector=$this.selector;
	var isSubmit=false;
	//临时全局变量
	var temp={ajaxHandle:null};
	
	//图标类型
	var icon={
		info:"icon-info-sign text-info",
		warn:"icon-warning-sign text-warning",
		ok:"icon-ok-sign text-success",
		error:"icon-remove-sign text-danger",
		wait:"icon-spinner icon-spin text-warning"
	};
	var defaults={
		show:"",
		focus:"",
		success:"",
		validator:{type:"string",spinner:false,min:0,max:1000,error:""},
		compare:{target:null,regexp:null,error:""},
		concateCompare:null,
		ajax:{url:null,wait:"",error:""}
	};
	var option=$.extend({},defaults,options);
	option.validator=$.extend({},defaults.validator,options.validator);
	option.compare=$.extend({},defaults.compare,options.compare);
	option.ajax=$.extend({},defaults.ajax,options.ajax);
	//封装html文字
	var html=function(icon,text,$that){
		var html_icon='<i class="'+icon+'"></i>';
		var html_text='<span>'+text+'</span>';
		$that||$target.html(html_icon+html_text);
	};
	
	//执行验证
	var validator=function(){
		var value=$this.val(),len=value.length;
		isSubmit=false;
		$this.removeClass("input_success").removeClass("input_wait").addClass("input_error");
		if(option.validator.type=="string"&&(stringUtil.isNull(value)||len<option.validator.min || len>option.validator.max)){
			return html(icon.error,option.validator.error);
		}
		if(option.validator.type=="number"&&(stringUtil.isNull(value)||value<option.validator.min || value>option.validator.max)){
			return html(icon.error,option.validator.error);
		}
		else if(stringUtil.isNotNull(option.compare.target)&&option.compare.regexp=="=" && $(option.compare.target).val()!=value){
			return html(icon.error,option.compare.error);
		}else if(stringUtil.isNotNull(option.ajax.url)){
			$this.removeClass("input_error").addClass("input_wait");
			//取消掉上次等待中的AJAX请求
			temp.ajaxHandle&&temp.ajaxHandle.abort();
			temp.ajaxHandle=$.get(option.ajax.url,function(data){
				
			});
			return html(icon.wait,option.ajax.wait);
		}else{
			if(stringUtil.isNotNull(option.concateCompare)&&stringUtil.isNotNull($(option.concateCompare).val())){
				$(option.concateCompare).blur();
			}
			isSubmit=true;
			$this.removeClass("input_error").addClass("input_success");
			return html(icon.ok,option.success);
		}
	};
	
	//拦截form提交
	$this.parents("form").submit(function(){
		if(!isSubmit){
			$this.blur();
			return false;
		}
	});
	
	//获取焦点事件
	$document.on("focus",selector,function(){
		$this.removeClass("input_success").removeClass("input_wait").removeClass("input_error");
		if(stringUtil.isNotNull(option.focus)){
			html(icon.info,option.focus);
		}
	});
	
	//失去焦点时候触发事件
	$document.on("blur",selector,function(){
		validator();
	});
	
	//如果是数字类型，转换为数字控件-只能输入数字
	if(option.validator.type=="number"){
		$document.on("keydown",selector,function(e){
			var key = window.event ? e.keyCode : e.which;
			//大键盘，小键盘，退格、制表、方向、delete、home、end、F5、回车键
			if ((key > 95 && key < 106) || (key > 47 && key < 60)||/^[89]$|^3[5679]$|^46$|^116$|^13$/.test(key)){
				return true;
			}else if(option.validator.spinner&&key==38){
				var val=$(this).val();
				$(this).val(val<option.validator.max?++val:option.validator.max);
			}else if(option.validator.spinner&&key==40){
				var val=$(this).val();
				$(this).val(val>option.validator.min?--val:0);
			}
			else{
				e.returnValue = false;
				e.preventDefault();
				return false;
			}
		});
	}
	
};

//工具类
var stringUtil={
	isNull:function(val){
		return val==undefined||val=="null"||val==null||val=="";
	},
	isNotNull:function(val){
		return !stringUtil.isNull(val);
	}
};