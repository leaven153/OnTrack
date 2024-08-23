import * as fnc from './fnc.mjs'
let binSocket = new SockJS(`http://localhost:8080/ws/taskDeletion`);

// window.onload = function(){
//     connectBinWs();
// }
function connectBinWs() {

    const sock = new SockJS(`http://localhost:8080/ws/taskDeletion`);
    // socket = sock;

    sock.onopen = function () {
        console.log(`Bin socket connected`);
    }

    sock.onmessage = function (e) {
        console.log(e.data);

        // nav.html에 icon이 빈 휴지통이라면 채워진 휴지통으로 바꾼다.
        if(fnc.elExists(document.querySelector("[class^='icon-bin-']"))){
            if(document.querySelector(".icon-bin-full").classList.contains("hide")){
                document.querySelector(".icon-bin-empty").classList.add("hide");
                document.querySelector(".icon-bin-full").classList.remove("hide");
            }
        }

        // tip을 띄운다 (3초 뒤에 사라진다.)
        if(fnc.elExists(document.querySelector("span.alarm-bin"))){
            document.querySelector("span.alarm-bin").classList.remove("img-hidden");
            setTimeout(()=>{
                document.querySelector("span.alarm-bin").classList.add("img-hidden");
            }, 3000);
        }
        // tip의 '확인'버튼을 눌러도 사라진다.
        if(elExists(document.querySelector("mark.btn-close-alarm"))){
            document.querySelector("mark.btn-close-alarm").addEventListener("click", ()=>{
                console.log(`확인 버튼 clicked`);
                document.querySelector("span.alarm-bin").classList.add("img-hidden");
            });
        }

        // 만약, 유저가 휴지통에 접속해 있다면, 해당 task row를 생성해서 붙여준다.
        const currUrl = decodeURIComponent(new URL(location.href).pathname).split("/");
        console.log(`접속 중인 유저의 현재 페이지: ${currUrl}`); // 접속 중인 유저의 현재 페이지: ,project,11
        console.log(`접속 중인 유저의 현재 페이지: ${currUrl[1]}`); //
        if(currUrl[1].includes("bin")){
            fetch(``, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(e.data)
            }).then(response => {
                if(response.ok){
                    response.json().then(task => {
                        console.log(task);
                    });
                } else {
                    const err = response.json();
                    err.then(warning => {
                        alert(warning["message"]);
                    });

                }
            });
        }
        // 필요정보: deletedAt, taskTitle, projectName, deletedBy, taskId
        // 단, 삭제한 사람이 휴지통에 바로 있을 수는 없기 때문에 영구삭제 버튼은 띄우지 않도록 한다.

    }

    sock.close = function(){
        console.log('bin connect close');
    }

    sock.onerror = function(err){
        console.log(`error occured: ${err}`);
    }
}


export { binSocket, connectBinWs };