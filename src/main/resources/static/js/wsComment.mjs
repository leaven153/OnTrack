let commentSocket = new SockJS(`http://localhost:8080/ws/noticeComment`);

// window.onload = function(){
//     connectCommentWs();
// }
function connectCommentWs() {

    const sock = new SockJS(`http://ontrack-env.eba-mpbdgazx.ap-northeast-2.elasticbeanstalk.com/ws/noticeComment`);
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


export { commentSocket, connectCommentWs };