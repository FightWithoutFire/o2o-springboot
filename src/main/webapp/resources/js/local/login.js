$().ready(
	$('#myform').validate({
	rules:{
		username: {
			required: true,
			minlength: 4
		},
		psw:{
			required: true,
			minlength:	4
		},
		j_captcha:{
			required: $('#j_captcha').attr('hidden') == false ? false : true
		}

	}
}))
$(function() {
	let loginUrl = '/o2o/local/logincheck';
	let loginCount = 0;
	let alert = $('.alert');


	


	$('#submit').click(function() {
		let userName = $('#username').val();
		let password = $('#psw').val();
		let userType = $('input[name=userType]:radio:checked').val();
		let verifyCodeActual = $('#j_captcha').val();
		let needVerify = false;
		if (loginCount >= 3) {
			if (!verifyCodeActual) {
				alert.html('请输入验证码');
				alert.attr('hidden',false);
				return;
			} else {
				needVerify = true;
			}
		}
		if(userName == '' || password == ''){
			alert.html('请输入用户名或密码');
			alert.attr('hidden',false);
			return;
		}


		$.post(
			loginUrl,
			{
				userName : userName,
				password : password,
				userType : userType,
				verifyCodeActual : verifyCodeActual,
				needVerify : needVerify
			},
			function(data) {
				if (data.success) {
					if(data.userType == 1){
						window.location.href = '/o2o/frontend/index';
					}else if(data.userType == 2){
						window.location.href = '/o2o/shopadmin/shopoperation';
					}else {
						alert.html("权限错误，请重新选择");
						alert.attr('hidden',false);
					}
				} else {
					alert.html(data.errMsg);
					alert.attr('hidden',false);

					loginCount++;
					if (loginCount >= 3) {
						$('#verifyPart').attr('hidden', false);

					}
				}
			},
			'json'
		);
	});

	$('#register').click(function() {
		window.location.href = '/o2o/local/register';
	});
});
