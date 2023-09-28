// JavaScript Document
$(document).ready(function() {
 bannershow();
	nav();
    banner();
    sidenav();
  listNews();
  empty();
  changebig();
  newscroll();
  control();
  news()
});

function bannershow(){
	var banner=$("#banner"),ul=banner.find("ul"),oLi=banner.find("li").length;
  		ul.width(banner.width()*3);
		ul.children().slice(3,oLi).remove();

}


function news(){
		var $li=$("#news ul li"),		//获取页面可用元素保存为变量
			$ul=$("#news ul"),
			width , n , timer , speed,index=0,w;		//定义变量
		n=$li.length;					//获取li的个数
		width=$li.eq(0).width();		//获取任意li的宽度
		$ul.width(width*n);
		for(var i=0;i<n;i++){			//通过li的个数判断添加的按钮数
          if(i==0){
          $("<em class='cur'></em>").appendTo($("#tip"));
          }else{
			$("<em></em>").appendTo($("#tip"));
          }
			}
		function fn(){
          if(index == n){
          index=0;
          }else{
				w=-index*width;
				$ul.stop().animate({"left":w});
          }
			$("#tip em").eq(index).addClass("cur").siblings().removeClass("cur");
			}
		timer=setInterval(function(){
			index++;
			fn();
			},2000)
		$("#btn-l").click(function(){		//右边按钮点击时，关闭滚动定时器，让index递减，使产品向右滚动一次，再开启滚动定时器
			clearInterval(timer);
			index--;
			if(index<0){
				index=n-1;
				}
			fn();
			timer=setInterval(function(){
			index++;
			fn();
			},2000)
			return false;
			})
		$("#btn-r").click(function(){			////左边按钮点击时，关闭滚动定时器，让index递增，使产品向左滚动一次，再开启滚动定时器
			clearInterval(timer);
			index++;
			if(index==n){
				index=0;
				}
			fn();
			timer=setInterval(function(){
			index++;
			fn();
			},2000)
			return false;
			})
		
		$("#tip em").click(function(){			//按钮点击时，通过判断按钮的序列，改变index的值，使产品滚动到相应的地方
			index=$("#tip em").index($(this));
			fn();
			})
		$(".tip em,#news ul").hover(function(){			//鼠标移上时，关闭滚动定时器，鼠标移开开启
			clearInterval(timer);
			},function(){
			timer=setInterval(function(){
			index++;
			fn();
			},2000)
			})
		fn();
		}



function control(){
	var n=0,m=$("#scroll li").length,w=$("#scroll li").width()+2,t,t1=3000,t2=500;
  if(m<=4){return false}
  $("#scroll ul").width(m*w+"px");
  function cut(){
    n++;
    if( n >=m-4){
    n=0;
    }
    $("#scroll ul").stop().animate({"left":-n*w+"px"},t2);
  }
  $("#scroll ul,#left,#right").hover(function(){
  clearInterval(t);
  },function(){
   t=setInterval(cut,t1);
  })
    t=setInterval(cut,t1);
  $("#left").click(function(){
  cut();
  })
  $("#right").click(function(){
    if(n == 0){
    n=m-4;
    }else{
    n--;
    }
    $("#scroll ul").stop().animate({"left":-n*w+"px"},t2);
  })
}


function newscroll(){
	var height, n ,timer,bool=1,time=2000,
	$li=$("#newscroll li");
	n=$li.length;
	height=$li.eq(0).height();
	if(n<= 1){return false};
	$li.clone().appendTo($("#newscroll"));						//复制内容放到后面，以实现无缝滚动
	$("#newscroll").height(2*n*height+"px");
	function up(){
		var f=parseFloat($("#newscroll").css("top"));			//整体向上运动，当滚完一半，直接跳到第一行，循环下去就实现了无缝滚动
		if(f <= (-n*height)){
			$("#newscroll").css("top","0px")
			up();
			}else{
			$("#newscroll").animate({"top":"-=24px"});
			}
		}
			
		$("#newscroll").hover(function(){		//鼠标移上时，取消滚动定时器，鼠标离开时，通过bool值判断开启哪个滚动定时器；
			clearInterval(timer);
			},function(){
			timer=setInterval(up,time);
			})
		
		timer=setInterval(up,time);
	}



function changebig(){
	var width=$("#changebig a.img img").width(),
        height=$("#changebig a.img img").height(),
        zwidth,zheight;
  zwidth=Math.floor(width/10);
  zheight=Math.floor(height/10);
  //alert(zheight)
  $("#changebig li").hover(function(){
    $(this).find("img").stop().animate({"width":width+2*zwidth,"height":height+2*zheight+"px","left":-zwidth+"px","top":-zheight+"px"});
  },function(){
  $(this).find("img").stop().animate({"width":width,"height":height,"left":"0px","top":"0px"});
  })
  
}


function empty(){
  if($("#empty").html() == 0 ){
  $(".page .right .wrap").addClass("nodata");
  }else{
  
  }
}


function listNews(){
               $(".skeyTag").remove();
                $("#listNews li.item a").bind("mouseover",function(){
                  $(this).stop().animate({borderLeftWidth:"20px"},200);
                })
                 $("#listNews li.item a").bind("mouseout",function(){
                  $(this).stop().animate({borderLeftWidth:"0px"},200)
                })
              }



function nav(){
  var n,m,w,time=5000,s,l;
  if($("#nav .cur").length > 1){
  $("#nav li").first().removeClass("cur");
  }
  m=$("#nav li").index($("#nav .cur"));
  
	$("#nav li").hover(function(){
      n=$("#nav li").index($(this));
      $(this).addClass("cur").siblings().removeClass("cur");
      if($(this).children(".drop").children("dd").length != 0 ){ 
		$(this).children(".drop").stop().slideDown(200);
       
      }else{
      $(this).children(".drop").remove();
      }},function(){
      $(this).removeClass("cur")
		$(this).children(".drop").stop().slideUp(200);
      $("#nav li").eq(m).addClass("cur");
		});
	}

// banner图片
function banner(){
	if($("#banner").length){
		var t, time = 4000, tim = 500, n = 1;
		// 克隆第一个元素‘
		var clones = $("#banner ul.list li").eq(0).clone(true);
		$("#banner ul.list").append(clones);
		var N = $("#banner ul.list li").length;
		// ul宽度
		var prinW = $("#banner").width();
		$("#banner ul.list").css({"width":N*prinW});
        $("#banner ul.list li").css({"width":prinW});
		// 自动生成tip标签
		var htmlTip = "";
		for(var i=1; i<N; i++){
			if(i==1){
				htmlTip += "<em class='cur'></em>";
			}else{
				htmlTip += "<em></em>";
			}
		}
		$("#banner").append("<div class='tip'>" + htmlTip + "</div>");
		// 执行函数
		var func = function(){
			if(n == N){
				$("#banner ul.list").css({"margin-left":0});
				n = 1;
				setTimeout(function(){
					func();
				}, 0)
			}else{
				n++;
				$("#banner ul.list").stop().animate({"margin-left":(-prinW*(n-1))}, tim);
				$("#banner .tip em").eq(n-1).addClass("cur").siblings().removeClass("cur");
				if(n == N){
					$("#banner .tip em").eq(0).addClass("cur").siblings().removeClass("cur");
				}
			}
		}
		//  自动执行
		t = setInterval(function(){
			func();
		}, time);
		// 点击执行
		$("#banner .tip em").click(function(){
			$(this).addClass("cur").siblings().removeClass("cur");
			var index = $(this).index();
			$("#banner ul.list").stop().animate({"margin-left":(-prinW*index)}, tim);
			n = index;
		});
        $(window).resize(function(){
			clearInterval(t);
			prinW = $("#banner").width();
            $("#banner ul.list").css({"width":N*prinW, "margin-left":(-prinW*(n-1))});
            $("#banner ul.list li").css({"width":prinW});
			t = setInterval(func, time);
		});
	}
}


function sidenav(){
  $("#sidenav li").hover(function(){
    if( $(this).find("dd").length !=0 ){
      $(this).children(".drop").stop().slideDown(100)}
    else{
    $(this).children(".drop").remove();
    }
  },function(){
    $(this).children(".drop").stop().slideUp(200);
  })
}


