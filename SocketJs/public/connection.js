
var socket=io.connect('192.168.1.4:8080');

	var handle=document.getElementById('handle');
	var message=document.getElementById('message');
	var btn=document.getElementById('send');
	var output=document.getElementById('output');
	var feedback=document.getElementById('feedback');


btn.addEventListener("click",function(){
       socket.emit('chat-message', {message:message.value,handle:handle.value});
	});
    
	// btn.addEventListener("click",function(){
 //       socket.emit('chat',{
 //           message:message.value,
 //           handle:handle.value
 //       });
	// });
    message.addEventListener('keypress',function(){
      socket.emit('typing',handle.value);
    });
    socket.on('chat-message',function(data){
    	feedback.innerHTML='';
    	message.innerHTML='';
    	output.innerHTML+='<p>'+data.handle+':'+data.message +'</p>';
    })

	socket.on('chat',function(data){
		feedback.innerHTML='';
		message.innerHTML='';
		output.innerHTML+='<img src=../Image_assets/default_user.png width=20 height=20>'+'<p>'+ data.handle+ ': '+ data.message +'</p>';
	});

	socket.on('typing',function(data){
      feedback.innerHTML='<p><strong>'+data.person+' is typing... </strong></p>';
	});

