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

        // nav에 알람표시를 출력한다.
        if(document.querySelector("#icon-myTasks") !== undefined && document.querySelector("#icon-myTasks") !== null){
            if(document.querySelector("#icon-myTasks").classList.contains("hide")){
                document.querySelector("#icon-myTasks").classList.remove("hide");
            }
        }

        // 나의 일 list에서 해당 task에 알람 표시를 출력한다.
        // 나의 일 모아보기에서 detail 열릴 수 있게 할 것인가 말 것인가

        // 프로젝트 list에도 해당 task에 알람 표시를 출력한다...

        // 여러 개가 등록되었을 경우?


    }

    sock.close = function(){
        console.log('connect close');
    }

    sock.onerror = function(err){
        console.log(`error occured: ${err}`);
    }
}


export { socket, connectWs };