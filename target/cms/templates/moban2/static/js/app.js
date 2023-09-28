/*********************************************
 * 个性化
 ********************************************/
(function(){
	$('#inews .main').scroller({
		
	});
	
	$('#icase .main').scroller({
		number: 4,
		offset: 15*2+15
	});
	
})();

(function(){
    // 导航
    $('#nav li').each(function(){
        if(!$(this).find('.drop dd').length){
			$(this).find('.drop').remove();
        }
    })

  $(".jing_pointer").bind('click', function(){
	if($(this).hasClass('active')){
		$(this).removeClass('active').siblings().show();
	}else{
		$(this).addClass('active').siblings().hide();
	}
})
    
	$('#nav li').bind({
		'mouseenter': function(){
			$(this).addClass('active').find('.drop').stop().slideDown(300);
		},
		'mouseleave': function(){
			$(this).removeClass('active').find('.drop').stop().slideUp(300);
		}
	})
	
	// 菜单
    $('#menu dd').each(function(){
        if(!$(this).find('.drop li').length){
			$(this).find('.drop').remove();
        }
    })
    
	$('#menu dd').hover(
		function(){
			$(this).addClass('active');
			$(this).children('.drop').stop().slideDown(200);
		},
		function(){
			$(this).removeClass('active');
			$(this).children('.drop').stop().slideUp(200);
		}
	)
    
    // 百度分享-浮窗+图标
    window._bd_share_config={
        "common": {
            "bdSnsKey":{},
            "bdText":"",
            "bdMini":"2",
            "bdMiniList":false,
            "bdPic":"",
            "bdStyle":"0",
            "bdSize":"24"
        },
        "slide": {
            "type":"slide",
            "bdImg":"2",
            "bdPos":"left",
            "bdTop":"160.5"
        },
        "share": {}
    };
    with(document)0[(getElementsByTagName('head')[0]||body) .appendChild(createElement('script')) .src='http://bdimg.share.baidu.com/static/api/js/share.js?v=89860593.js?cdnversion='+~(-new Date()/36e5)];
})();

(function(){
	var $toolbar = $('#toolbar');
	
	$toolbar.find('.itop').bind('click', function(){
		$('html, body').stop().animate({'scrollTop': 0}, 200);
	})
	$toolbar.find('.code a').bind({
		'mouseenter': function(){
			$(this).next().stop().show(200);
		},
		'mouseleave': function(){
			$(this).next().stop().hide(200);
		}
	})
	$toolbar.find('.tel a').bind({
		'mouseenter': function(){
			$(this).next().stop().animate({'width': 162}, 200);
		},
		'mouseleave': function(){
			$(this).next().stop().animate({'width': 0}, 200);
		}
	})
	
	$toolbar.find('.close').bind({
		'click': function(){
			$toolbar.children('.pointer').show(300).next().hide();
		}
	})
	
	$toolbar.find('.pointer').bind({
		'click': function(){
			$toolbar.children('.inner').show(300).prev().hide();
		}
	})
}());