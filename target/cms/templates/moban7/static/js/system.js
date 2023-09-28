/**********
 * 通用默认变量
 **********/
if(typeof(siteIndex) == "undefined"){ var siteIndex = "/" };
if(typeof(searchVa) == "undefined"){ var searchVa = "在此输入关键词" };
if(typeof(tel400) == "undefined"){ var tel400 = "4000731777" };
if(typeof(jingVa) == "undefined"){ var jingVa = [true, 1, 50, ["1000000"], [1, "http://c.hnjing.com/code/code.gif", "扫一扫我"]] };

/**********
 * 通用函数
 **********/
// minBanner
function minBanner(){
	if(!$("#minBanner").length || $("#minBanner li").length <= 1){
		return false;
	}
	var _this = $("#minBanner"),
		me = $("#minBanner ul"),
		tip = $("#minBanner .tip"),
		t,
		interval = 10000,
		speed = 1000,
		speed2 = 700,
		n = 0,
		N = me.children("li").length < 3 ? me.children("li").length : 3;
		
	if($("#minBanner .tip").length){
		var htmlTip = "";
		for(var i=0; i<N; i++){
			if(i==0){ htmlTip += "<span class='cur'></span>"; }else{ htmlTip += "<span></span>"; }
		}
		tip.html(htmlTip);
	}
	var func = function(){
		if(n >= N-1){ n = 0; }else{	n ++; }
		me.children("li").eq(n).css({"z-index":2}).stop().fadeIn(speed).siblings("li").css({"z-index":1}).stop().fadeOut(speed2);
		if($("#minBanner .tip").length){
			tip.children("span").eq(n).addClass("cur").siblings("span").removeClass("cur");
		}
	}
	tip.children("span").click(function(){
		clearInterval(t); n = $(this).index()-1; func(); t = setInterval(func, interval); 
	});
	_this.hover(
		function(){ tip.stop().animate({"bottom":10}, 100); },
		function(){ tip.stop().animate({"bottom":-10}, 100); }
	)
	t = setInterval(func, interval);
}


// 改变字体大小
function setFont(){
	$("#setFont a").click(function(){
		$(this).addClass("cur").siblings().removeClass("cur");
		var size = $(this).attr("rel");
		$(this).parents(".mark").siblings(".info").css({"font-size":size+"px"});
	});
}
// 搜索效果
function searcher(){
    var isFocus = false;  // 是否由关键词框获取焦点
	$("#SearchTxt").val(searchVa).focus(function(){
        isFocus = true;
		$(this).val("");
	}).blur(function(){
        isFocus = false;
		if($(this).val() == ""){
			$(this).val(searchVa);
		}
	});
    $("#SearchSubmit").click(function(){
		if($("#SearchTxt").val() == "" || $("#SearchTxt").val() == searchVa){
			alert(searchVa);
			return false;
		}
        search();
    });
    $(document).keydown(function(event){
		event = event ? event : ( window.event ? window.event : null );
		if(event.keyCode == 13 && isFocus == true){
			$("#SearchSubmit").trigger("click");
		}
	});
}
// 百度地图
function baiduMap(){
  if($("#baiduMap").length){
    var func = function(){
      if($("#baiduMap .BMap_bubble_title a").length < 1){
        setTimeout(func, 100);
      }else{
        $("#baiduMap .BMap_bubble_title a").attr({"target":"_blank"});
      }
    }
    func();  // 执行函数
  }
}
// 客服系统
function jingKf(bool){
	if(bool == true){
		return false;
	}else{
		if(!$("#jingKf").length){
			// 判断是手机还是400电话
			var telLength = tel400.length;
			if(telLength == 11){  // 手机号码 OR 座机号码
				var firstNum = tel400.substr(0,1);
				if(firstNum == 0){
					var tel1 = tel400.substr(0, 4);
					var tel2 = tel400.substr(4, 7);
					tel400 = tel1+ "-" + tel2;
				}else{
					var tel1 = tel400.substr(0, 3);
					var tel2 = tel400.substr(3, 4);
					var tel3 = tel400.substr(7, 4);
					tel400 = tel1+ "-" + tel2 + "-" + tel3;
				}
			}else if(telLength == 12){
				var tel1 = tel400.substr(0, 4);
				var tel2 = tel400.substr(4, 8);
				tel400 = tel1+ "-" + tel2;
			}else if(telLength == 10){
				var tel1 = tel400.substr(0, 7);
				var tel2 = tel400.substr(7, 3);
				
				tel400 = tel1+ "-" + tel2 ;
			}
			if($("#tel400").length){ $("#tel400").html(tel400); }
			if($(".format400").length){ $(".format400").html(tel400); }
			// 计算循环体代码
			var htm = "";
			for(var index in jingVa[3]){
				htm += "<li class='b'><a href='tencent://message/?uin="+ jingVa[3][index] +"&Menu=yes'><i class='online'></i></a></li>";
			}
			// 输出页面代码
			var html = "<!-- kf start --><div class='jing_kf jing_kf_"+ jingVa[1] +"' id='jingKf'>";
			html += "<div class='jing_top pngfix'></div>";
			html += "<div class='jing_main pngfix'><ul class='t'>";
			html += "<li class='e'><strong>咨询热线</strong></li><li class='e lin'><strong>"+ tel400 +"</strong></li>";
			html += htm;  // 联系方式循环体
			html += "</ul>";
			if(jingVa[4][0] == 1){
				var tags = "扫一扫我";
				if(jingVa[4][2] != "" && jingVa[4][2] != undefined){
					tags = jingVa[4][2];
				}
				html += "<div class='mobileCode'><i class='dimCode'><img src='"+ jingVa[4][1] +"' alt='二维码' /></i><span>"+ tags +"</span></div>";
			}else{
				html += "<div class='mobileCode'><a href='"+ siteIndex +"'>网站首页</a></div>";
			}
			html += "<div class='returnTop'><a href='javascript:;' class='pngfix' title='返回顶部'></a></div>";
			html += "</div>";
			html += "<div class='jing_pointer'></div>";
			html += "</div><!-- kf end -->";
			$(html).appendTo("body");
		}else{
			var telLength = tel400.length;
			if(telLength == 11){  // 手机号码 OR 座机号码
				var firstNum = tel400.substr(0,1);
				if(firstNum == 0){
					var tel1 = tel400.substr(0, 4);
					var tel2 = tel400.substr(4, 7);
					tel400 = tel1+ "-" + tel2;
				}else{
					var tel1 = tel400.substr(0, 3);
					var tel2 = tel400.substr(3, 4);
					var tel3 = tel400.substr(7, 4);
					tel400 = tel1+ "-" + tel2 + "-" + tel3;
				}
			}else if(telLength == 12){
				var tel1 = tel400.substr(0, 4);
				var tel2 = tel400.substr(4, 8);
				tel400 = tel1+ "-" + tel2;
			}else if(telLength == 10){
				var tel1 = tel400.substr(0, 3);
				var tel2 = tel400.substr(3, 4);
				var tel3 = tel400.substr(7, 3);
				tel400 = tel1+ "-" + tel2 + "-" + tel3;
			}
			if($("#tel400").length){ $("#tel400").html(tel400); }
			if($(".format400").length){ $(".format400").html(tel400); }
		}
		$(window).scroll(function(){
			var scrollTop = $(document).scrollTop();
			$("#jingKf").stop().animate({"top":(scrollTop+jingVa[2])}, 550);
		});
		$("#jingKf .returnTop a").mouseenter(function(){
			$(this).stop().animate({"height":55});
		}).mouseleave(function(){
			$(this).stop().animate({"height":50});
		}).click(function(){
			$("body, html").stop().animate({"scrollTop":0});
		});
		$("#jingKf .jing_pointer").bind('click', function(){
			if($(this).hasClass('active')){
				$(this).removeClass('active').siblings().show();
			}else{
				$(this).addClass('active').siblings().hide();
			}
		})
	}
}
// 检查数据
function checkForm(){
    if($("#formPost").length){  // 初始化表单输入框内容
       $("#formPost tr.code input").val("");
    }
	$("#formPost input[type='submit']").click(function(){
		// 开始验证.遍历tr.required 必填字段
		var bool = true;
		$("#formPost tr.required").each(function(){
			var a, b, c;  // a:值 b:检查类型 c:项目名称
			$(this).find(".tip").html("");  // 初始化提示语。规避判断错误。
			if($(this).find("input").length){
				a = $(this).find("input").val().replace(/\s+/gm,' ');
				b = $(this).find("input").attr("model");
				c = $(this).find("label").text();
				c = c.substr(0, c.length-1);
			}else{
				a = $(this).find("textarea").val().replace(/\s+/gm,' ');
				b = "text";  // 文本框默认是任意文本
				c = $(this).find("label").text();
				c = c.substr(0, c.length-1);
			}
			if(a == "" || a == " "){
			    $(this).find(".tip").html("<span class='err'>"+ c +" 不能为空！</span>");
				bool = false;
			}else{
				// 验证格式
				switch (b){
					case "name":  // 验证姓名： 汉字或者英文
					if(!a.match(/^[A-Za-z]+$/) && !a.match(/^[\u4e00-\u9fa5]{0,}$/)){
						$(this).find(".tip").html("<span class='err'>"+ c +"格式不正确：只能是汉字或英文</span>");
						bool = false;
					}else{
						$(this).find(".tip").html("");
					}
					break;
					case "phone":  // 验证手机： 纯数字没有限制数量
					if(!a.match(/^[0-9]*$/)){
						$(this).find(".tip").html("<span class='err'>"+ c +"格式不正确：只能是纯数字</span>");
						bool = false;
					}else{
						$(this).find(".tip").html("");
					}
					break;
					case "email":  // 验证邮箱： a@b.com
					if(!a.match(/^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/)){
						$(this).find(".tip").html("<span class='err'>"+ c +"格式不正确：例如 Ex@163.com</span>");
						bool = false;
					}else{
						$(this).find(".tip").html("");
					}
					break;
				}
			}
        });
		// 验证完成。决定是否跳转
		return bool;
	});
}
// 集成JS效果
function tools(){
	// 设为首页
	$("#setHome").click(function(){
        SetHome(this, location.href);
	});
	// 加入收藏
	$("#addFavo").click(function(){
		var fm = $("title").html();
		AddFavorite(fm, location.href, '');
	});
    // 返回网站顶部
    $("#returnTop").click(function(){
        $("body, html").animate({"scrollTop":0});
    });
	// 去掉A的超级链接
	$("a").focus(function(){ $(this).blur(); });
    // 面包屑导航
	if($("#sitepath").length){
		var txt = $("#sitepath a:last-child").text();
		$("#sitepath a:last-child").remove();
		$("#sitepath").append("<span>"+ txt +"</span>");
	}
	// 详情页预定
    $("#book").click(function(){
    	var a = $("#bookTip").offset().top;
        $("body, html").stop().animate({"scrollTop":a}, 500);
    });
}
// 设置主页
function SetHome(obj,url){
	try{
		obj.style.behavior='url(#default#homepage)';
		obj.setHomePage(url);
   }catch(e){
	   if(window.netscape){
		  try{
			  netscape.security.PrivilegeManager.enablePrivilege("UniversalXPConnect");
		 }catch(e){
			  alert("抱歉，此操作被浏览器拒绝！\n\n请在浏览器地址栏输入“about:config”并回车然后将[signed.applets.codebase_principal_support]设置为'true'");
		  }
	   }else{
		alert("抱歉，您所使用的浏览器无法完成此操作。\n\n您需要手动将【"+url+"】设置为首页。");
	   }
  }
}
//收藏本站
function AddFavorite(title, url) {
  try {
	  window.external.addFavorite(url, title);
  }
catch (e) {
	 try {
	   window.sidebar.addPanel(title, url, "");
	}
	 catch (e) {
		 alert("抱歉，您所使用的浏览器无法完成此操作。\n\n加入收藏失败，请使用Ctrl+D进行添加");
	 }
  }
}

/**********
 * 初始化函数
 *********/
setFont();           // 改变字体大小
searcher();          // 搜索效果
baiduMap();          // 百度地图跳转模式
checkForm();         // 表单验证系统
tools();             // 基本工具
jingKf(jingVa[0]);   // 客服
minBanner();         // 迷你banner