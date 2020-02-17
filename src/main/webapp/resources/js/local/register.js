$().ready(
	$('#myform').validate({
		rules:{
			username: {
				required: true,
				minlength: 4
			},
			newpsw:{
				required: true,
				minlength:	4
			},
			newpswcheck: {
				required: true,
				minlength:	4,
				equalTo: "#newpsw"
			},
			j_captcha:{
				required: $('#j_captcha').attr('hidden') == false ? false : true
			}

		},
		messages:{
			newpswcheck: {
				equalTo: "两次输入的密码不一致，请重新输入"
			}
		}
	}))
$(function() {
	var bindUrl = '/o2o/local/registeruser';
	let alert = $('.alert');
	$('#submit').click(function() {
		var userName = $('#username').val();
		var password = $('#newpsw').val();
		var userType = $('input[name = userType]:radio:checked').val();
		var password_again = $('#newpswcheck').val();
		var verifyCodeActual = $('#j_captcha').val();
		if(password == '' || password_again == '' || userName == ''){
			alert.html('表单未输入完整');
			alert.attr('hidden',false);
			return;
		}
		if(password != password_again){
			alert.html('密码不一致，请重新输入');
			alert.attr('hidden',false);
			return;
		}
		if (!verifyCodeActual) {
			alert.html('请输入验证码');
			alert.attr('hidden',false);
			return;
		}


		$.post(
			bindUrl,
			{
				userName : userName,
				password : password,
				userType : userType,
				verifyCodeActual : verifyCodeActual
			},
			function(data) {
				if (data.success) {
					alert.html('注册成功！');
					alert.attr('hidden',false);
					if(data.userType == 1){
						window.location.href = '/o2o/frontend/index';
					}else if(data.userType == 2){
						window.location.href = '/o2o/shopadmin/shoplist';
					}else{
						alert.html('注册失败！数据错误！');
						alert.attr('hidden',false);
					}

				} else {
					alert.html('注册失败！'+data.errMsg);
					alert.attr('hidden',false);
					$('#captcha_img').click();
				}
			},
			'json'
		);
	});
});