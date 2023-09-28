$(function () {
		
	   $('[data-rel="close"]').click(function() {
		 $().offCanvas('close')
		});
	   
	   setInterval(function(){$('#time').html(currentTime)},600);
	
	   function currentTime(){
		var d = new Date(),str = '';
		var x = new Array("星期日", "星期一", "星期二","星期三","星期四", "星期五","星期六");
		 str += d.getFullYear()+'年';
		 str  += d.getMonth() + 1+'月';
		 str  += d.getDate()+'日 &nbsp; ';
		 str += x[d.getDay()];
		return str;
		};
		
		 if ($(window).width() <= 500) {
			$(".centered").css({"right":(1000 - $(window).width()) / 2 });
		};

});
$(function() {
    var id = '#my-offcanvas';
    var $myOc = $(id);
    $('.doc-oc-js').on('click', function() {
      $myOc.offCanvas($(this).data('rel'));
    });  
	
	$('.fclose').click(function() {
		 $(".fload").fadeOut();
		});
});
$(document).ready(function(){
	var $liCur = $(".list-nav ul li.list-active"),
	  curP = $liCur.position().left,
	  curT = $liCur.position().top + $(".list-nav ul li").height() - 3,
	  curW = $liCur.outerWidth(true),
	  $slider = $(".curBg"),
	  $navBox = $(".list-nav");
	 $targetEle = $(".list-nav ul li a"),
	$slider.animate({
	  "left":curP,
	  "top":curT,
	  "width":curW
	});
	$targetEle.mouseenter(function () {
	  var $_parent = $(this).parent(),
		_width = $_parent.outerWidth(true),
		posL = $_parent.position().left;
		posT = $_parent.position().top + $(".list-nav ul li").height() - 3;
	  $slider.stop(true, true).animate({
		"left":posL,
		"top":posT,
		"width":_width
	  }, "fast");
	});
	$navBox.mouseleave(function (cur, wid, toh) {
	  cur = curP;
	  wid = curW;
	  toh = curT;
	  $slider.stop(true, true).animate({
		"left":cur,
		"top":toh,
		"width":wid
	  }, "fast");
	});
	
});

jQuery(".txtScroll-top").slide({mainCell:".bd ul",autoPage:true,effect:"topLoop",autoPlay:true,scroll:4,vis:4,delayTime:700,interTime:4000});