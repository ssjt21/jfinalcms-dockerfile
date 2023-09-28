function application(){
	var _self = this;
	this.config = {
		'searchval': '请输入关键词搜索',
		'api': '4DD845D1BB619BEEFB641EC49A7D8735',
		'phone': /((\d{11})|^((\d{7,8})|(\d{4}|\d{3})-(\d{7,8})|(\d{4}|\d{3})-(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1})|(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1}))$)/,
		'email': /^(\w)+(\.\w+)*@(\w)+((\.\w+)+)$/
	}
  
	// 顶部导航
    $('#nav .drop').each(function(index, element){
        if($(this).find('dd').length == 0){
			$(this).remove();
        }
    })
	$('#nav li').bind({
		'mouseenter': function(){
			$(this).children('.drop').stop(true, true).slideDown();
		},
		'mouseleave': function(){
			$(this).children('.drop').stop(false, false).slideUp();
		}
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
    
	// 格式化电话号码
    $('[ig-phone]').each(function(index, element){
        var tel400 = $(this).text(), telLength = tel400.length;
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
    
    // 加入收藏
	$('#addFavo').bind('click', function(){
		app.addFavorite($('title').html(), location.href, '');
	});
	
	// 返回顶部
	$('#top').bind('click', function(){
		$('body, html').animate({'scrollTop': 0}, 200);
	})
	
	// API验证
	if(typeof(_self.config.api) == 'undefined' || _self.config.api.substr(13,4) != 'BEEF'){
		return false;
	}
	
	this.scroller();
	this.searcher();
	this.toolbar();
	this.plugs();
	this.bdmap();
	this.former();
}
application.prototype = {
	plugs: function(){
		// 百度分享
		window._bd_share_config = {
            "common": {
                "bdSnsKey": {},
                "bdText": "",
                "bdMini": "2",
                "bdMiniList": false,
                "bdPic": "",
                "bdStyle": "0",
                "bdSize": "24"
            },
            "share": {}
        };
        with (document) 0[(getElementsByTagName('head')[0] || body).appendChild(createElement('script')).src = 'http://bdimg.share.baidu.com/static/api/js/share.js?v=89860593.js?cdnversion=' + ~(-new Date() / 36e5)];
		
		// 
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
		$("#toolbar .pointer").bind('click', function(){
			var _this = $(this);
			if(_this.hasClass('active')){
				_this.parent().stop().animate({'right': 10}, function(){
					_this.removeClass('active');
				});
			}else{
				_this.parent().stop().animate({'right': -109}, function(){
					_this.addClass('active');
				});
			}
		})
	},
	searcher: function(){
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
					$(this).val(app.config.searchval);
                }
			}
		})
		$('#SearchSubmit').bind('click', function(){
			if($('#SearchTxt').val() == '' || $('#SearchTxt').val() == $('#SearchTxt').attr('placeholder')){
				app.jAlert(app.config.searchval);
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
        try{
            window.external.addFavorite(url, title);
        }
        catch(e){
            try{
                window.sidebar.addPanel(title, url, '');
            }
            catch(e){
                alert('抱歉，您所使用的浏览器无法完成此操作');
           }
        }
    },
	scroller: function(){
		if($('#banner .list li').length > 1){
			!function banner(){
				var $banner = $('#banner'),
					$list = $banner.children('.list'),
					$tip = $banner.children('.tip'),
					$prev = $banner.children('.prev'),
					$next = $banner.children('.next'),
					t,
					_interval = 8000,
					_speed = 1000,
					_speed2 = 700,
					_index = 0,
					_size = $list.children('li').length;
				if($tip.length){
					var html = '';
					for(var i=0; i<_size; i++){
						if(i==0){ html += '<span class="cur"></span>'; }else{ html += '<span></span>'; }
					}
					$tip.html(html);
				}
				
				function _prev(){
					if(_index <= 0){
						_index = _size - 1;
					}else{
						_index --;
					}
					$list.children('li').eq(_index).css({'z-index':2}).stop().fadeIn(_speed).siblings('li').css({'z-index':1}).stop(true,true).fadeOut(_speed2);
					if($tip.length){
						$tip.children('*').eq(_index).addClass('cur').siblings().removeClass('cur');
					}
				}
				function _next(){
					if(_index >= _size - 1){
						_index = 0;
					}else{
						_index ++;
					}
					$list.children('li').eq(_index).css({'z-index':2}).stop().fadeIn(_speed).siblings('li').css({'z-index':1}).stop(true,true).fadeOut(_speed2);
					if($tip.length){
						$tip.children('*').eq(_index).addClass('cur').siblings().removeClass('cur');
					}
				}
				
				$banner.hover(
					function(){
						clearInterval(t);
					},
					function(){
						t = setInterval(_next, _interval);
					}
				)
				$tip.children('*').click(function(){
                  	_indexTowo = $(this).index();
					$list.children('li').eq(_indexTowo).css({'z-index':2}).stop().fadeIn(_speed).siblings('li').css({'z-index':1}).stop(true,true).fadeOut(_speed2);
					if($tip.length){
						$tip.children('*').eq(_indexTowo).addClass('cur').siblings().removeClass('cur');
					}
                  _index = $(this).filter(".cur").index();
				})
				$prev.bind('click', function(){
					_prev();
				})
				$next.bind('click', function(){
					_next();
				})
				
				t = setInterval(_next, _interval);
			}()
		}
        
		// 首页产品
		if($('#ipro').length){
			!function banner(){
				var $id = $('#ipro'),
					$menu = $id.find('.classify .list'),
					$list = $id.find('.main .list'),
					$tip = $id.find('.tip'),
					_size = $id.find('.main .list .mini').length;
				
				var _html = '';
				for(var i=0; i<_size; i++){
					if(i == 0){
						_html +='<span class="current"></span>';
					}else{
						_html +='<span></span>';
					}
				}
				$tip.html(_html);
				
				$tip.children('*').bind('click', function(){
					var _index = $(this).index();
					$(this).addClass('current').siblings().removeClass('current');
					$list.children('dd.online').siblings('dd').hide().eq(_index).fadeIn().next().fadeIn();
				})
				
				$menu.children('li').bind('mouseenter', function(){
					$(this).siblings().children('.drop').hide();
					if($(this).find('.drop dd').length > 1){
						$(this).find('.drop').slideDown(300);
					}
				})
				
			}()
		}
		
		// 首页新闻
		if($('#inews .list dd').length > 3){
			!function(){
				var $id = $('#inews'),
                    $list = $id.find('.list'),
					index = 0,
					_width = 340,
					_speed = 300,
					_size = $list.children('dd').length,
					t,
					_interval = 5000;
				
                $list.append($list.html()).width(_size*_width*2);
				function fun(){
					if(index > _size){
						$id.find('.list').css({'left': 0});
						index = 1;
                        fun();
					}else if(index < 1){
						$id.find('.list').css({'left': -_size*_width});
						index = _size - 1;
                        fun();
                    }else{
                        $list.stop().animate({'left': -index*_width}, _speed);
                    }
				}
				
				$id.bind({
                    'mouseenter': function(){
						clearInterval(t);
                    },
                    'mouseleave': function(){
						t = setInterval(function(){
                            index ++;
                            fun();
                        }, _interval);
                    }
                })
				
				t = setInterval(function(){
					index ++;
					fun();
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