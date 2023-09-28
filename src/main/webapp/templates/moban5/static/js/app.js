(function(){

	$('#inews').scroller({
		'offset': 36,
		'number': 4
	})

    
}());
function application(){
	var _self = this;
	this.searchval = '请输入搜索关键词';
	this.api = '4DD845D1BB619BEEFB641EC49A7D8735';
	this.phone = /((\d{11})|^((\d{7,8})|(\d{4}|\d{3})-(\d{7,8})|(\d{4}|\d{3})-(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1})|(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1}))$)/;
    this.email = /^(\w)+(\.\w+)*@(\w)+((\.\w+)+)$/;
	this.toolAuto = true;

//banner

  $(function(){
    var banner=$("#banner"),ul=banner.find("ul"),oLi=banner.find("li").length,oTip=banner.find(".tip"),oS=oTip.children("span").length;
	
  		oTip.children().slice(3,oS).remove();
		ul.children().slice(3,oLi).remove();
})


  //拆分标题
  $('.headx h2 em').each(function(index, element){
    var iproh1= $.trim($(this).text());
    	var iproh1a= iproh1.substr(0, 2);
 	 var iproh1b = iproh1.substr(2);
     iproh1="<b>"+ iproh1a + "</b>"+ iproh1b;
    $(this).html(iproh1);
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
  $("#setHome").click(function(){
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
     //悬浮
        $(".fixclick").click(function(){
          $("#brTxt").width("140px");
        })
        $(".fixclose").click(function(){
              $("#brTxt").width("80px");
        })
	$("#close").click(function(){
      $(this).parents(".txt").hide().css({"width":"60px"}).siblings(".fixclick").fadeIn();
	})
	$(".fixclick").click(function(){
		$(this).siblings(".txt").show();
		$(this).hide()
	})
	// API验证
	if(typeof(_self.api) == 'undefined' || _self.api.substr(13,4) != 'BEEF'){
		return false;
	}
	 if(typeof(searchval) == "undefined"){ var searchval = "请输入搜索关键词" };
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
			size = $('#inewsView .listx dd').length,
			step = $('#inewsView .listx dd').width(),
			index = 0,
			speed = 300,
			interval = 5000,
			t;
		$list.append($list.html()).width(2*size*step);
		var _html = '';
		for(var i = 0; i <= size - 1; i++){
			i == 0 ? _html += '<span class="cur"></span>' : _html += '<span></span>';
		}
		$tip.html(_html);
		function func(){
			if(index > size){
				$list.css({'left':0});
				index = 1;
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
			i == 0 ? _html += '<em class="cur"></em>' : _html += '<em></em>';
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
   $('.nav li:last').css({"background":"none"});
   $('.subnav a:last').css({"background":"none"});
	$('.nav li').hover(function() {
      var index=$(this).index(),direction=(index>5)?'right':'left';
       $(this).find('.subNav').css(direction,'3px');
      var leng=$(this).find('a').length-1;
      $(this).children('a').addClass('hover');
      if(leng>7){
        $(this).find('.pai').children('a').eq(Math.ceil(leng/2)-1).addClass('no_bg');
       $(this).find('.pai').children('a').eq(leng-1).addClass('no_bg');
		$(this).children('.subNav').slideDown(100);
      $(this).find('.pai').css({"width":Math.ceil(leng/2)*104});
      }else{
      $(this).find('.pai').children('a').eq(Math.ceil(leng)-1).addClass('no_bg');
       $(this).find('.pai').children('a').eq(leng-1).addClass('no_bg');
		$(this).children('.subNav').slideDown(100);
      $(this).find('.pai').css({"width":Math.ceil(leng)*104});
      }
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
		$postform.find('[type="submit"]').bind('click', function () {
			var $name = $postform.find('[name="Name"]'),
				$phone = $postform.find('[name="Phone"]'),
				$email = $postform.find('[name="Email"]'),
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
			if (!$phone.val().match(app.config.phone)) {
				app.jLog($phone.attr('error'), $phone.offset().left, $phone.offset().top);
				$phone.focus();
				return false;
			}
			// 电子邮箱
			if ($email.val() != $email.attr('placeholder') && !$email.val().match(app.config.email)) {
				app.jLog($email.attr('error'), $email.offset().left, $email.offset().top);
				$email.focus();
				return false;
			}
			// 验证码
			if ($code.length && ($code.val() == '' || $code.val() == $code.attr('placeholder'))) {
				app.jLog($code.attr('empty'), $code.offset().left, $code.offset().top);
				$code.focus();
				return false;
			}
		})
	},
	toolbar: function(){
		$("#side .code").hover(
		function(){
			$(this).find(".wd").stop().slideDown(200,"swing");
		},
		function(){
			$(this).find(".wd").stop().slideUp(200,"swing");
		}
	);

  $("#gotop").click(function(){
			$("body, html").stop().animate({"scrollTop":0});
   });
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
		// 首页banner
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
					_index = 0,
					_size = $list.children('*').length;
				
				if($tip.length){
					var _html = '';
					for(var i=0; i<_size; i++){
						if(i==0){
							_html += '<li class="current"> </li>';
						}else{
							_html += '<li> </li>';
						}
					}
					$tip.html(_html);
				}
				
				$list.children('*').eq(0).addClass('active');
				
				function _prev(){
					if(_index < 0){
						_index = _size - 1;
					}
					$list.children('*').eq(_index).addClass('active').stop(true, true).fadeIn(_speed).siblings('*').removeClass('active').stop(false, false).fadeOut(_speed);
					$tip.children('*').eq(_index).addClass('current').siblings().removeClass('current');
				}
				
				function _next(){
					if(_index >= _size){
						_index = 0;
					}
					$list.children('*').eq(_index).addClass('active').stop(true, true).fadeIn(_speed).siblings('*').removeClass('active').stop(false, false).fadeOut(_speed);
					$tip.children('*').eq(_index).addClass('current').siblings().removeClass('current');
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
   // 首页产品 - 左右可触发滚动
		if($('#icase .list li').length > 7){
			!function(){
				var $id = $('#icase'),
					$cont = $id.find('.list'),
					$list = $id.find('.list'),
					$prev = $('.arrow').find('.arrow_r'),
					$next = $('.arrow').find('.arrow_l'),
					_index = 0,
					_width = 281,
					_sped = 300,
					_size = $id.find('li').length;
				// clone数据
				$cont.width((_width)*_size);
				var clone = $cont.html();
        		// $cont.append(clone);
              //$cont.find("li").eq(_size/2-1).after(clone);
				$id.find('.list').width(_size * _width);
              var _size1=_size-4;
				function fun(_size){
					if(_index > _size1){
						$cont.css({'margin-left': 0});
						_index = 1;
					}
                  else if(_index < 0){
						$cont.css({'margin-left': -_size1 *_width});
						_index = _size1 - 1;
					}
					$cont.stop().animate({'margin-left': -_index*_width}, _sped);
				}
               $('.icase .tod ul li').hover(function() {
			$(this).find('.cover').animate({'height': '348px'}, 100,'swing');
                 
				}, function() {
				$(this).find('.cover').animate({'height': '271px'}, 100,'swing');
				})
    
				$prev.bind('click', function(){
					_index --;
					fun();
				})
				$next.bind('click', function(){
					_index ++;
					fun();
				}) 
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
		var _offsetX = 0,
			_offsetY = 44;
		if(i == null){
			i = '必填字段，请输入正确的内容';
		}
        if ($('#jLog').length) {
            $('#jLog').html(i + '<i></i>').show().css({ 'left': (l + _offsetX), 'top': (t + _offsetY) });
        } else {
            $('<div class="dialog-log" id="jLog">' + i + '<i></i></div>').appendTo('body').css({ 'left': (l + _offsetX), 'top': (t + _offsetY) });
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

$(function(){
    newsest();
});
// 最新动态
function newsest(){
	if(!$("#newsest").length){ return false; }
	var t, a = $("#newsest ul"), time = 2000, step = 200, c = a.height(), d = a.find("li").height();
	// 克隆数据,实现无间断滚动
	var clone = a.html();
	a.append(clone);
	var func = function(){
		var b = parseInt(a.css("margin-top"));
		if(c <= -b){
			a.css({"margin-top":0});
			func();
		}else{
			a.animate({"margin-top":(b-d)}, 300);
		}
	}
	t = setInterval(func, time);
	// 鼠标触发
	a.find("li").hover(
		function(){ clearInterval(t); },
		function(){ t = setInterval(func, time); }
	);
}
