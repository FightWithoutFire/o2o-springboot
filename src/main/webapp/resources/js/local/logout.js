$(function(){
	$('#log-out').click(function(){
		$.post(
			'/o2o/local/logout',
			function(data){
				if(data.success){
					window.location.href='/o2o/local/login';
				}
			},
			'json')
	})
})