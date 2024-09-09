import * as fnc from './fnc.mjs'
import {binRow} from "./createElement.mjs";

let binSocket = new SockJS(`http://ontrack-env.eba-mpbdgazx.ap-northeast-2.elasticbeanstalk.com/ws/taskDeletion`);
// let justDeletedTaskId;

// window.onload = function(){
//     connectBinWs();
// }
function connectBinWs() {

    const sock = new SockJS(`http://ontrack-env.eba-mpbdgazx.ap-northeast-2.elasticbeanstalk.com/ws/taskDeletion`);
    // socket = sock;

    sock.onopen = function () {
        console.log(`Bin socket connected`);
    }

    sock.onmessage = function (e) {
        console.log(e.data);
        // justDeletedTaskId = e.data;

        // nav.html에 icon이 빈 휴지통이라면 채워진 휴지통으로 바꾼다.
        if(fnc.elExists(document.querySelector("[class^='icon-bin-']"))){
            if(document.querySelector(".icon-bin-full").classList.contains("hide")){
                document.querySelector(".icon-bin-empty").classList.add("hide");
                document.querySelector(".icon-bin-full").classList.remove("hide");
            }
        }


        // tip을 띄운다 (n초 뒤에 사라질까..말까?)
        if(fnc.elExists(document.querySelector("span.alarm-bin"))){
            document.querySelector("span.alarm-bin").classList.remove("img-hidden");
        }

        // console.log(`웹소켓 메시지 받고 여기까지 안오나?`);
        const currUrl = decodeURIComponent(new URL(location.href).pathname).split("/");
        // console.log(`접속 중인 유저의 현재 페이지: ${currUrl}`); // 접속 중인 유저의 현재 페이지: ,project,11
        // console.log(`접속 중인 유저의 현재 페이지: ${currUrl[2]}`); //
        if(currUrl[2].includes("bin")){

            fetch(`http://ontrack-env.eba-mpbdgazx.ap-northeast-2.elasticbeanstalk.com/mypage/removedTask?taskId=${e.data}`, {
                method: 'GET',
                // headers: {
                //     'Content-Type': 'application/json'
                // },
                // body: JSON.stringify(e.data)
            }).then(response => {
                if(response.ok){
                    response.json().then(task => {
                        // 필요정보: deletedAt, taskTitle, projectName, deletedBy, taskId + 복원버튼-projectId, 지금 내 member id,
                        // 단, 삭제한 사람이 휴지통에 바로 있을 수는 없기 때문에 영구삭제 버튼은 띄우지 않도록 한다.

                        // 1. Object 형태(특히, 날짜)
                        console.log(task);
                        // {projectId: null, projectName: 'By your side', taskId: null, memberId: null, taskTitle: '크러쉬와 송혜교만',…}
                        /*
                        * authorMid: null
                        * authorized: null
                        * deletedAt: "2024-08-24T18:14:00"
                        * deletedBy: null
                        * deleterName: null
                        * memberId: null
                        * projectId: null
                        * projectName: "By your side"
                        * taskId: null
                        * taskTitle: "크러쉬와 송혜교만"*/
                        // console.log(task["deletedAt"]); // 2024-08-24T18:14:00
                        // console.log(task["deletedAt"].slice(0, 10));
                        // console.log(task["deletedAt"].slice(11, 16));
                        let userId;
                        if(fnc.elExists(document.querySelector("span.userId"))){
                            userId = document.querySelector("span.userId").dataset.userid;
                        }

                        if(fnc.elExists(document.querySelector(".bin-task-list"))){
                            document.querySelector(".bin-task-list").prepend(binRow(task, userId));
                        }

                        // 2. 여러 개 할 일이 왔을 때

                    });
                } else {
                    const err = response.json();
                    err.then(warning => {
                        alert(warning["message"]);
                    });

                }
            });
        }

        // setTimeout(function(){
        //     document.querySelector("span.alarm-bin").classList.add("img-hidden");
        // }, 5000);
    }

    sock.close = function(){
        console.log('bin connect close');
    }

    sock.onerror = function(err){
        console.log(`error occured: ${err}`);
    }
}


export { binSocket, connectBinWs };