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
	var url = '/o2o/local/changelocalpwd';
	let alert = $('.alert');
	$.get(
		'/o2o/local/getUserName',
		{},
		function (data) {
			if(data.userName){
				$('#userName').val(data.userName);
			}
		},
		'json'
	)
	$('#submit').click(function() {
		var userName = $('#userName').val();
		var password = $('#newpsw').val();
		var newPassword = $('#newpswcheck').val();
		if(password == '' || newPassword == '' || userName == ''){
			alert.html('表单未输入完整');
			alert.attr('hidden',false);
			return;
		}
		if(newPassword != password){
			alert.html('密码不一致，请重新输入！');
			alert.attr('hidden',false);
			return;
		}
		var formData = new FormData();
		formData.append('userName', userName);
		formData.append('password', password);
		formData.append('newPassword', newPassword);
		var verifyCodeActual = $('#j_captcha').val();
		if (!verifyCodeActual) {
			alert.html('请输入验证码！');
			alert.attr('hidden',false);
			return;
		}

		formData.append("verifyCodeActual", verifyCodeActual);
		$.post(
			url,
			formData,
			function(data) {
				if (data.success) {
					alert.html('提交成功！');
					alert.attr('hidden',false);
					window.location.href = '/o2o/local/login';
				} else {
					alert.html('提交失败！');
					alert.attr('hidden',false);
					$('#captcha_img').click();
				}
			},
			'json');
	});

	$('#back').click(function() {
		window.location.href = window.history.back() ?
			window.history.back() : '/o2o/local/login';
	});
});
