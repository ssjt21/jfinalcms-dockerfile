$(document).ready(function() {
	$(".c2_list ul li:nth-child(4n)").css("margin-right","0px")
	$(".c4_slide_phone .bd ul li:nth-child(2n)").css("padding-left","0px")
	$(".kc_list1 ul li:nth-child(2n)").css("margin-right","0px")
	$(".kc_list2 ul li:nth-child(3n)").css("margin-right","0px")
	$(".kcpage_main_bottom ul li:nth-child(5n)").css("margin-right","0px")
	//var m_st, m_po = 60;
//	$(window).scroll(function(){
//		m_st = Math.max(document.body.scrollTop || document.documentElement.scrollTop);
//		if (m_st > m_po)
//		{
//		}else{
//		}
//	});
	//服务详情
	//文本框焦点
	// $(document).ready(function() {
	// 	$(".ipt1").val("真实姓名");
	// 	textFill( $('.ipt1') );
	// 	$(".ipt2").val("地址");
	// 	textFill( $('.ipt2') );
	// 	$(".ipt3").val("年龄");
	// 	textFill( $('.ipt3') );
	// 	$(".ipt4").val("联系电话");
	// 	textFill( $('.ipt4') );
	// 	$(".ipt5").val("微信号");
	// 	textFill( $('.ipt5') );
	// 	$(".ipt6").val("Email");
	// 	textFill( $('.ipt6') );
	// 	$(".zxbm_textarea").val("个人简介");
	// 	textFill( $('.zxbm_textarea') );
	// });
	function textFill(input){ //input focus text function
		var originalvalue = input.val();
		input.focus( function(){
			if( $.trim(input.val()) == originalvalue ){
				input.val('');
			}
		}).blur( function(){
			if( $.trim(input.val()) == '' ){
				input.val(originalvalue);
			}
		});
	}
	//导航
	$(".menu").click(function(){
		$(".nav_bj").show();
		$(".navs").animate({top:'0'},600)
	})
	$(".close").click(function(){
		$(".nav_bj").hide();
		$(".navs").animate({top:'-4000'},600)
	})
	$(".nav_bj").click(function(){
		$(this).hide();
		$(".navs").animate({right:'-4000'},600)
	})
	$(".xian2").height($(".lc_con").height());
	$(".kcpage_main_bottom ul li").hover(function(){
		$(this).children(".c4_img_links").show();
	},function(){
		$(this).children(".c4_img_links").hide();
	})
	$(".zp_page_bottom_list ul li").hover(function(){
		$(this).children(".c4_img_links").show();
	},function(){
		$(this).children(".c4_img_links").hide();
	})
});
$(window).resize(function(){
	$(".xian2").height($(".lc_con").height());
	$(".banner_bj").height($(".banner").height());
})

