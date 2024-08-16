// import * as SockJs from 'https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js';
// import * as sj from 'sockjs';
// import * as sockJS from 'https://cdnjs.cloudflare.com/ajax/libs/web-socket-js/1.0.0/web_socket.min.js';
// import * as SockJS from 'sockjs-client';

let socket = new SockJS(`http://localhost:8080/ws/noticeComment`);

window.onload = function(){
    connectWs();
}
function connectWs() {

    const sock = new SockJS(`http://localhost:8080/ws/noticeComment`);
    // socket = sock;

    sock.onopen = function () {
        console.log(`socket connected`);
    }

    sock.onmessage = function (e) {
        console.log(e.data);
    }

    sock.close = function(){
        console.log('connect close');
    }

    sock.onerror = function(err){
        console.log(`error occured: ${err}`);
    }
}


export { socket, connectWs };