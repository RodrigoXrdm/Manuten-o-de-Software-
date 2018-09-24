
$(document).ready(function(){
	//checkboxes
	$('.periograma-icon').html("check_box_outline_blank");
	$('.periograma-icon').attr('checked', 0);
	$('.periograma-icon').on('click', function(){
		if($(this).html() == "check_box_outline_blank"){
			//checked style
			
			$(this).html("check_box");

			if ($(this).hasClass("supragengival")){
				$(this).addClass("orange-text text-lighten-3");
			}
			if($(this).hasClass("placa")){
				$(this).addClass("light-blue-text");
			}
			else{
				$(this).addClass("red-text");
			}
		}
		else{
			//unchecked style
			$(this).html("check_box_outline_blank");
			$(this).removeClass("red-text");
			$(this).removeClass("light-blue-text");
			$(this).removeClass("orange-text text-lighten-3");
		}
	});
	//furca
	$('.furca').attr('src', '/img/periograma/furca-3.png');
	$('.furca').addClass("almost-hidden");
	$('.furca').attr("value", 0);
	$('.furca').on('click', function(){
		$value = parseInt($(this).attr("value"));
		$new_value = ($value+1)%4;
//		console.log($new_value);
		//incrementa o value
		$(this).attr("value", $new_value);
		if(($new_value)===0){
			$(this).addClass("almost-hidden");
		}
		else{
			$(this).removeClass("almost-hidden");
			$(this).attr('src', '/img/periograma/furca-'+$(this).attr("value")+'.png');
//			console.log($(this).attr('src'));
		}
	});
	//disabled
	$('.disabled').removeClass("almost-hidden");
	$('.disabled').prop('onclick',null).off('click');
  });
