
var express=require('express');
var socket=require('socket.io');

var app=express();
var server=app.listen(8080,function(){
    console.log('listening to ports 8080');
});

app.use(express.static('public'));

var io=socket(server);

io.on('connection',function(socket){
  console.log(' a new socket is created'+socket.id);

  socket.on('connected',function(user){
    console.log(user.id+ 'has connected');
  })
  
  socket.on('chat',function(data){
     io.sockets.emit('chat',data);
  });
  socket.on('chat-message',function(data){
     console.log(data);
     io.sockets.emit('chat-message',{message:data.message,handle:data.handle});
  });
   socket.on('login', function(data){
    console.log('a user ' + data.userId + ' connected');
  });

  socket.on('typing',function(data){
  	console.log(data);
    socket.broadcast.emit('typing',{person:data});
  });
  socket.on('disconnect',function(data){
  	console.log('one user is disconnected '+socket.id);
  	io.sockets.emit('leave',{person:data});
  });

  socket.on('leave',function(data){
     console.log(data+ ' has leaved the group');
  });

   
});
