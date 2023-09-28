(function(){
	$('#inews_img').scroller({
		offset: 0
	});
   
})();
function application(){
	var _self = this;
	this.searchval = '请输入关键词';
	this.api = '4DD845D1BB619BEEFB641EC49A7D8735';
	this.phone = /((\d{11})|^((\d{7,8})|(\d{4}|\d{3})-(\d{7,8})|(\d{4}|\d{3})-(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1})|(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1}))$)/;
    this.email = /^(\w)+(\.\w+)*@(\w)+((\.\w+)+)$/;
	this.toolAuto = true;
$('.sso').bind('click', function(){
        if($(this).siblings('.searcher').is(':hidden')){
			$(this).siblings('.searcher').slideDown(300);//  超链接跳转
        }else{
            	$(this).siblings('.searcher').slideUp(300);
        }
    })

//banner

  $(function(){
    var banner=$("#banner"),ul=banner.find("ul"),oLi=banner.find("li").length,oTip=banner.find(".tip"),oS=oTip.children("span").length;
	
  		oTip.children().slice(3,oS).remove();
		ul.children().slice(3,oLi).remove();
})


	// 格式化电话号码
    $('[ig-phone]').each(function(index, element){
        var tel400 = $.trim($(this).text()), telLength = tel400.length;
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
        $(this).html(tel400);
    })
        
     // 菜单
    $('#menu .drop').each(function(index, element){
        if($(this).children('li').length == 0){
			$(this).remove();
        }
    })
    $('#menu dd > a').bind('click', function(e){
        if($(this).siblings('.drop').children('li').length == 0){
			//  超链接跳转
        }else{
            if($(this).siblings('.drop').is(':hidden')){
				$(this).siblings('.drop').slideDown(300).end().parent('dd').siblings('dd').children('.drop').slideUp(300);
            }else{
            	$(this).siblings('.drop').slideUp(300);
            }
          	e.preventDefault();
        }
    })
	 
    // 加入收藏
	$('#addFavo').bind('click', function(){
		app.addFavorite($('title').html(), location.href, '');
	});
	
  	// 设为首页
	$('#setHome').bind('click', function(){
		app.addSetHome(this, location.href);
	});
  
	// 返回顶部
	$('#goTop').bind('click', function(){
		$('body, html').animate({'scrollTop': 0}, 200);
	})
    // 返回顶部
	$('#goTop2').bind('click', function(){
		$('body, html').animate({'scrollTop': 0}, 200);
	})
    //拆分标题
  $('.bn_b .number p').each(function(index, element){
    var iproEn= $.trim($(this).text()),index=iproEn.indexOf("-");
     console.log(index)
    if(index!=-1){
     
    var iproEn1a= iproEn.substr(0, index);
 	 var iproEn1b = iproEn.substr(index);
     iproEn="<b>"+iproEn1a + "</b>"+ iproEn1b;
      $(this).html(iproEn);}
    else{  }
    })
	//fix
	$("#close").click(function(){
		$(this).parents(".txt").hide().siblings(".fixclick").fadeIn();
	})
	$(".fixclick").click(function(){
		$(this).siblings(".txt").show();
		$(this).hide()
	})
    
	// API验证
	if(typeof(_self.api) == 'undefined' || _self.api.substr(13,4) != 'BEEF'){
		return false;
	}
	 if(typeof(searchval) == "undefined"){ var searchval = "在此输入关键词" };
	this.scroller();
	this.searcher();
	this.toolbar();
	this.former();
	this.photo();
    this.album();
    this.bdmap();
    this.nav();
 	this.view();
  this.view1();
}
application.prototype = {
 view: function(){
		var $list = $('#inewsView .listx'),
			$tip = $('#inewsView .tipx'),
			size = $('#inewsView .listx dd').length/4,
			step = 660,
			index = 0,
			speed = 300,
			interval = 5000,
			t;
		//$list.append($list.html()).width(2*size*step);
		var _html = '';
		for(var i = 0; i <= size - 1; i++){
			i == 0 ? _html += '<span class="cur"></span>' : _html += '<span></span>';
		}
		$tip.html(_html);
		function func(){
			if(index >= size){
				$list.css({'left':0});
				index = 0;
			}else if(index < 0){
				$list.css({'left': -step*size});
				index = size - 1;
            }
            if(index == size){
                $tip.children('span').eq(0).addClass('cur').siblings().removeClass('cur');
            }
			$list.stop().animate({'left': -step*index}, speed);
			$tip.children('span').eq(index).addClass('cur').siblings().removeClass('cur');
		}
 		  $tip.children('span').click(function(){
					clearInterval(t);
					index = $(this).index();
					func();
					t = setInterval(func, interval);
				})
		t = setInterval(function(){ func(++index) }, interval);
	},
   view1: function(){
		var $list = $('#inewsView1 .listx'),
			$tip = $('#inewsView1 .tip'),
			size = $('#inewsView1 .listx li').length,
			step = $('#inewsView1 .listx li').width(),
			index = 0,
			speed = 300,
			interval = 5000,
			t;
		$list.append($list.html()).width(2*size*step);
		var _html = '';
		for(var i = 0; i <= size - 1; i++){
			i == 0 ? _html += '<em class="cur">'+(i+1)+'</em>' : _html += '<em>'+(i+1)+'</em>';
		}
		$tip.html(_html);
		function func(){
			if(index > size){
				$list.css({'margin-left':0});
				index = 1;
			}else if(index < 0){
				$list.css({'margin-left': -step*size});
				index = size - 1;
            }
            if(index == size){
                $tip.children('em').eq(0).addClass('cur').siblings().removeClass('cur');
            }
			$list.stop().animate({'margin-left': -step*index}, speed);
			$tip.children('em').eq(index).addClass('cur').siblings().removeClass('cur');
		}
 		  $tip.children('em').click(function(){
					clearInterval(t);
					index = $(this).index();
					func();
					t = setInterval(func, interval);
				})
		t = setInterval(function(){ func(++index) }, interval);
	},
 nav: function(){
	$('.nav .subNav').each(function() {
		if (!$(this).find('a').length) {
			$(this).remove();
		}
	});

	$('.nav li').hover(function() {
      $(this).children('a').addClass('hover');
		$(this).children('.subNav').slideDown(100);
    
	}, function() {
      $(this).children('a').removeClass('hover');
		$(this).children('.subNav').slideUp(100);
	});

	},
    bdmap: function(){
        var func = function(){
        	if($(".BMap_bubble_title a").length < 1){
                setTimeout(func, 100);
              }else{
                $(".BMap_bubble_title a").attr({"target":"_blank"});
              }
            }
        func();  // 执行函数
    },
    album: function(){
		$("#albumList .item").click(function(){
            $("#dialogAlbum").show();
            var windowHeight = $(window).height(), windowWidth = $(window).width();
            create();
            var f = $(this).index()+1;
            funny(f);
        });
        function create(){
			var _html = '';
            $("#albumList .item").each(function(index, element){
				_html +='<li><table cellpadding="0" cellspacing="0" border="0" class="img"><tr><td align="center" valign="middle"><img src="'+ $(element).attr('url') +'" /></td></tr></table></li>'; 
            });
            $("#dialogAlbumContainer ul").html(_html).find("img");
        }
        function funny(f){
            var windowHeight = $(window).height(), windowWidth = $(window).width(), t, interval = 5000, speed = 300, n = f,	clones = $("#dialogAlbumContainer li").eq(0).clone(true),	N = $("#dialogAlbumContainer li").length+1, htmlTip = "", isAuto = false;
            $("#dialogAlbumContainer ul").append(clones);
            $("#dialogAlbumContainer ul").css({"width":N*windowWidth});
            $("#dialogAlbumContainer li").css({"width":windowWidth});
            // 自动生成tip标签
            for(var i=1; i<N; i++){
                if(i==f){
                    htmlTip += "<span class='cur'></span>";
                }else{
                    htmlTip += "<span></span>";
                }
            }
            $("#dialogAlbumTip").html(htmlTip);
            // 点击打开默认位置
            $("#dialogAlbumContainer ul").css({"margin-left":(-windowWidth*(n-1))});
            // 执行函数
            var func = function(){
                if(n >= N){
                    $("#dialogAlbumContainer ul").css({"margin-left":0});
                    n = 1;
                    setTimeout(func, 0)
                }else if(n < 0){
                    n = N-2;
                    $("#dialogAlbumContainer ul").css({"margin-left":(-windowWidth*(n+1))});
                    setTimeout(func, 0);
                }else{
                    n++;
                    $("#dialogAlbumContainer ul").stop().animate({"margin-left":(-windowWidth*(n-1))}, speed);
                    $("#dialogAlbumTip span").eq(n-1).addClass("cur").siblings().removeClass("cur");
                    if(n == N){
                        $("#dialogAlbumTip span").eq(0).addClass("cur").siblings().removeClass("cur");
                    }
                }
            }
            // 点击执行
            $("#dialogAlbumTip span").click(function(){
                $(this).addClass("cur").siblings().removeClass("cur");
                n = $(this).index();
                $("#dialogAlbumContainer ul").animate({"margin-left":(-windowWidth*n)}, speed);

            });
            // 关闭.自动播放.左滑动.右滑动
            $("#dialogAlbum .close").click(function(){ $("#dialogAlbum").hide(); });
            $("#imgPlayAuto").click(function(){
                if(!isAuto){ t = setInterval(func, interval); isAuto=true; $(this).attr({"class":"play"}); }else{ clearInterval(t); isAuto=false; $(this).attr({"class":"plus"}); }
            });
            $("#dialogAlbum .left").click(function(){ n-=2; func(); });
            $("#dialogAlbum .right").click(function(){ func(); });
            $(window).resize(function(){
                windowHeight = $(window).height();
                windowWidth = $(window).width();
                $("#dialogAlbumContainer ul").css({"width":N*windowWidth});
                $("#dialogAlbumContainer li").css({"width":windowWidth});
                $("#dialogAlbumContainer ul").css({"margin-left":(-windowWidth*(n-1))});
            });
        }
    },
	photo: function(){
		var $photo = $('#productPhoto'),
			$original = $('#productPhoto .original img'),
			$prev = $('#productPhoto .prev'),
			$next = $('#productPhoto .next'),
			$thum = $('#productPhoto .thum dl'),
			index = 0,
			_os = 4,
			_size = 0,
			_width = 101;
		
		// 载入结构
        if(typeof(multigraph) != 'undefined'){
            var _html = '';
            $(multigraph).each(function(index, element){
                if(index == 0){
					_html += '<dd class="current"><img src="'+ element.src +'" title="'+ element.title +'" alt="'+ element.title +'" /></dd>';
                  	$original.attr({'src': element.src});
                }else{
					_html += '<dd><img src="'+ element.src +'" title="'+ element.title +'" alt="'+ element.title +'" /></dd>';
                }
            });
            $thum.html(_html);
            _size = multigraph.length;
        	$thum.width(_width*_size);
        };
		
		// 点击显示大图
		$photo.on('click', '.thum dd', function(){
			$(this).addClass('current').siblings().removeClass('current');
			$original.attr({'src': $(this).children('img').attr('src')});
		})
		
		// 左右翻页
		function fun(){
			$thum.animate({'left': -index*_width}, 300, function(){
				$prev.removeClass('nodrop');
				$next.removeClass('nodrop');
				if(index == (_size - _os)){
					$next.addClass('nodrop');
				}else if(index == 0){
					$prev.addClass('nodrop');
				}
			});
		}
		$prev.bind('click', function(){
			if(index > 0){
				index --;
				fun();
			}
		})
		$next.bind('click', function(){
			if(index < _size - _os){
				index ++;
				fun();
			}
		})
	},
	former: function(){
		$postform = $('#formPost');
		$postform.find('.txt, .text, .code').bind({
			'focus': function(){
				$(this).parent().addClass('onfocus');
				if ($(this).val() == $(this).attr('placeholder')) {
					$(this).val('');
				}
			},
			'blur': function(){
				$(this).parent().removeClass('onfocus');
				$('#jLog').hide();
				if ($(this).val() == '') {
					$(this).val($(this).attr('placeholder'));
				}
			},
			'keyup': function(){
				$('#jLog').hide();
			}
		});
      $postform.find('#txtImageCode').bind({
			'focus': function(){
				$(this).siblings('.yanma').text('');
			},
			'blur': function(){
				$(this).siblings('.yanma').text('请输入验证码：');
			},
			'keyup': function(){
				$(this).siblings('.yanma').text('');
			}
		});
		$postform.find('[type="submit"]').bind('click', function () {
			var $name = $postform.find('[name="Name"]'),
				$phone = $postform.find('[name="Phone"]'),
				$email = $postform.find('[name="Email"]'),
                $qq = $postform.find('[name="QQ"]'),
				$code = $postform.find('[name="txtImageCode"]');
			
			// 姓名
			if ($name.val() == '' || $name.val() == $name.attr('placeholder')) {
				app.jLog($name.attr('empty'), $name.offset().left, $name.offset().top);
				$name.focus();
				return false;
			}
			// 联系方式
			if ($phone.val() == '' || $phone.val() == $phone.attr('placeholder')) {
				app.jLog($phone.attr('empty'), $phone.offset().left, $phone.offset().top);
				$phone.focus();
				return false;
			}
			if (!$phone.val().match(app.phone)) {
				app.jLog($phone.attr('error'), $phone.offset().left, $phone.offset().top);
				$phone.focus();
				return false;
			}
          // QQ
			if ($qq.val() == '' || $qq.val() == $qq.attr('placeholder')) {
				app.jLog($qq.attr('empty'), $qq.offset().left, $qq.offset().top);
				$qq.focus();
				return false;
			}
			if (!$qq.val().match(app.phone)) {
				app.jLog($qq.attr('error'), $qq.offset().left, $qq.offset().top);
				$qq.focus();
				return false;
			}
			// 电子邮箱
			if ($email.val() != $email.attr('placeholder') && !$email.val().match(app.email)) {
				app.jLog($email.attr('error'), $email.offset().left, $email.offset().top);
				$email.focus();
				return false;
			}
			// 验证码
			if ($code.val() == '' || $code.val() == $code.attr('placeholder')) {
				app.jLog($code.attr('empty'), $code.offset().left, $code.offset().top);
				$code.focus();
				return false;
			}
		})
	},
	toolbar: function(){
		$('#toolbar dd').bind({
			'mouseenter': function(){
				if($(this).children('.slide').length){
					var _this = $(this).children('.slide');
					_this.stop(true, true).animate({'width': 120}, 200);
				}else if($(this).children('.pop').length){
					var _this = $(this).children('.pop');
					_this.show().animate({'right': 84}, 200);
				}
			},
			'mouseleave': function(){
				if($(this).children('.slide').length){
					var _this = $(this).children('.slide');
					_this.stop(false, false).animate({'width': 0}, 200);
				}else if($(this).children('.pop').length){
					var _this = $(this).children('.pop');
					_this.hide().animate({'right': 100}, 200);
				}
			}
		})
	},
	searcher: function(){
		var bool = true;  // 完善模式
	// 模拟select
	$("#searcher .type span.res").click(function(){
		if(bool == true){
			bool = false;
          var value = $(this).attr("data-value");
          $(this).val(value);
			$(this).next().slideDown(200);
			setTimeout(function(){
				$("body").bind("click", function(){
					$("#searcher .type .drop1").slideUp(200);
					bool = true;
					$("body").unbind("click");
				});
			}, 10);
		}else{
			bool = true;
		}
	});
	
      
      var _self = this,
		isFocus = false;
		$('#SearchTxt').bind({
			'focus': function(){
				isFocus = true;
				$(this).val('');
			},
			'blur': function(){
				isFocus = false;
                if($(this).val() == ''){
                  $(this).val(app.searchval);
                }
			}
		})
		$('#SearchSubmit').bind('click', function(){
			if($('#SearchTxt').val() == '' || $('#SearchTxt').val() == $('#SearchTxt').attr('placeholder')){
             app.jAlert(app.searchval);
				return false;
			}
			search();
		});
		$(document).keydown(function(event){
			event = event ? event : ( window.event ? window.event : null );
			if(event.keyCode == 13 && isFocus == true){
				$('#SearchSubmit').trigger('click');
			}
		});
      // 添加热门关键词
        $('#SearchLink a').each(function(){
          	$(this).attr({'href': $('#Searchtype').val() + '&where=Title:' + $(this).text()});
        })
	},
    addFavorite: function(title, url){
      try {
          window.external.addFavorite(url, title);
      }
      catch (e) {
         try {
           window.sidebar.addPanel(title, url, "");
        }
         catch (e) {
             alert('抱歉，您所使用的浏览器无法完成此操作。\n\n加入收藏失败，请使用Ctrl+D进行添加');
         }
      }
    },
  // 设置主页
  addSetHome:function(obj,url){
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
} ,
	scroller: function(){
		if($('#banner .list li').length > 1){
			!function(){
				var $id = $('#banner'),
					$list = $id.children('.list'),
					$tip = $id.children('.tip'),
					$prev = $id.children('.prev'),
					$next = $id.children('.next'),
					t,
					_interval = 8000,
					_speed = 1000,
					_speed2 = 900,
					_index = 0,
					_size = $list.children('*').length;
				
				if($tip.length){
					var _html = '';
					for(var i=0; i<_size; i++){
						if(i==0){
							_html += '<li class="cur"> </li>';
						}else{
							_html += '<li > </li>';
						}
					}
					$tip.html(_html);
				}
				
				$list.children('*').eq(0).addClass('active');
				
				function _prev(){
					if(_index < 0){
						_index = _size - 1;
					}
					$list.children('*').eq(_index).addClass('active').stop(true, true).fadeIn(_speed).siblings('*').removeClass('active').stop(false, false).fadeOut(_speed2);
					$tip.children('*').eq(_index).addClass('cur').siblings().removeClass('cur');
				}
				
				function _next(){
					if(_index >= _size){
						_index = 0;
					}
					$list.children('*').eq(_index).addClass('active').stop(true, true).fadeIn(_speed).siblings('*').removeClass('active').stop(false, false).fadeOut(_speed2);
					$tip.children('*').eq(_index).addClass('cur').siblings().removeClass('cur');
				}
				
				$tip.children('*').click(function(){
					_index = $(this).index();
					_next();
				})
				
				$prev.bind('click', function(){
					_index --;
					_prev();
				})
				
				$next.bind('click', function(){
					_index ++;
					_next();
				})
				
				$id.hover(
					function(){
						clearInterval(t);
					},
					function(){
						t = setInterval(function(){
							_index ++;
							_next();
						}, _interval);
					}
				)
				
				t = setInterval(function(){
					_index ++;
					_next();
				}, _interval);
			}()
		}
    },
	jAlert: function(info, title, callback){
		var _self = this,
			_html = '';
		
		if(typeof(title) == 'function'){
			callback = title;
			title = '温馨提示';
		}else if(title == null){
			title = '温馨提示';
		}
		
		_self.layout(1);
		
		_html += '<div class="dialog-alert" id="jAlear">';
		_html += '<div class="head">';
		_html += '<h2>'+ title +'</h2>';
		_html += '<a href="javascript:;" class="close"></a>';
		_html += '</div>';
		_html += '<div class="main">';
		_html += '<p>'+ info +'</p>';
		_html += '</div>';
		_html += '<div class="foot">';
		_html += '<a href="javascript:;" class="ok">我知道了</a>';
		_html += '</div>';
		_html += '</div>';		
				
		var $obj = $(_html);
		$obj.appendTo('body').show();
		$obj.find('.close')
			.bind('click', function(){
				_self.layout(0);
				$obj.hide().remove();
				if(callback){
					callback(false);
				}
			});
		$obj.find('.ok')
			.bind('click', function(){
				_self.layout(0);
				$obj.hide().remove();
				if(callback){
					callback(true);
				}
			})
	},
	jConfirm: function(info, title, callback){
		var _self = this,
			_html = '';
		
		if(typeof(title) == 'function'){
			callback = title;
			title = '温馨提示';
		}else if(title == null){
			title = '温馨提示';
		}

		_self.layout(1);		
		
		_html += '<div class="dialog-confirm" id="jConfirm">';
		_html += '<div class="head">';
		_html += '<h2>'+ title +'</h2>';
		_html += '<a href="javascript:;" class="close"></a>';
		_html += '</div>';
		_html += '<div class="main">';
		_html += '<p>'+ info +'</p>';
		_html += '</div>';
		_html += '<div class="foot">';
		_html += '<a href="javascript:;" class="ok">确定</a>';
		_html += '<a href="javascript:;" class="cancel">取消</a>';
		_html += '</div>';
		_html += '</div>';		
				
		var $obj = $(_html);
		$obj.appendTo('body').show();
		$obj.find('.close')
			.bind('click', function(){
				_self.layout(0);
				$obj.hide().remove();
				if(callback){
					callback(false);
				}
			});
		$obj.find('.ok')
			.bind('click', function(){
				_self.layout(0);
				$obj.hide().remove();
				if(callback){
					callback(true);
				}
			})
		$obj.find('.cancel')
			.bind('click', function(){
				_self.layout(0);
				$obj.hide().remove();
				if(callback){
					callback(false);
				}
			})
	},
	jLog: function(i, l, t){
		var _offsetX = -23,
			_offsetY = 44;
		if(i == null){
			i = '必填字段，请输入正确的内容';
		}
        if ($('#jLog').length) {
            $('#jLog').html(i + '<i></i>').show().css({ 'left': (l +25+ _offsetX), 'top': (t + _offsetY) });
        } else {
            $('<div class="dialog-log" id="jLog">' + i + '<i></i></div>').appendTo('body').css({ 'left': (l+25 + _offsetX), 'top': (t + _offsetY) });
        }
    },
	layout: function(u){
		if(u == 0){
			$('#dialogLayout').remove();
		}else{
			if(!$('#dialogLayout').length){
				$('<div class="dialog-layout" id="dialogLayout"></div>').appendTo('body').show();
			}
		}
	}
}
var app = new application();