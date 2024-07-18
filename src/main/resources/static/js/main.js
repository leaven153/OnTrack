/*로컬에서 file:// 프로토콜을 사용해 웹페이지를 열면 import, export 지시자가 동작하지 않습니다.*/
// import { elExists } from "./myprojects";
function elExists(el){
    return el !== undefined && el !== null;
}

window.onload = function(){
    /*---- ▼ 열고닫기.. 시작 ▼ ----*/

    /*---------- 001 ------------*/
    /*** 프로젝트 나가기 ***/
    if(elExists(document.querySelector(".btn-project-out"))){
        document.querySelector(".btn-project-out").addEventListener("click", ()=>{
            document.querySelector("div#modal-leave-project").classList.remove("hide");
        });

        document.querySelector(".btn-project-leave").addEventListener("click", ()=>{
            console.log("진짜로 나가겠음");
        });

        document.querySelector(".btn-close-modal-leave-project").addEventListener("click", ()=>{
            document.querySelector("div#modal-leave-project").classList.add("hide");
        });

    }

    /*---------- 002 ------------*/
    /*** 프로젝트 內 별명 설정 ***/
    if(elExists(document.querySelector("div.nickname")) && document.querySelector("span.nickname")){
        const btnChkNickname = document.querySelector(".btn-chk-nickname-double");
        document.querySelector("div.nickname").addEventListener("click", ()=>{
            document.querySelector("input.modal-configure-nickname").value = "";
            document.querySelector(".nickname-ok").classList.add("hide");
            document.querySelector(".nickname-no").classList.add("hide");
            document.querySelector("div#modal-configure-nickname").classList.remove("hide");
            btnChkNickname.disabled = true;
            btnChkNickname.classList.add("font-blur");
            btnChkNickname.classList.add("cursorNot");
        });
        document.querySelector(".btn-close-modal-confiture-nickname").addEventListener("click", ()=>{
            document.querySelector("div#modal-configure-nickname").classList.add("hide");
        });

        // 특수기호 불용(여백 허용)
        const regNonWord = /[{}\[\]()\/?,.'";:`~<>!@#$%^&*=-_+|]/g;

        let newnickname = "";
        
        document.querySelector("input.modal-configure-nickname").addEventListener("input", ()=>{
            newnickname = document.querySelector("input.modal-configure-nickname").value;
            // 입력된 별칭의 길이 및 특수문자 포함여부 확인
            if(newnickname.length >= 1 && newnickname.length < 10){
                // console.log(newnickname.match(regNonWord));
                if(newnickname.match(regNonWord) || newnickname.length === 1) {
                    document.querySelector(".nickname-no").classList.remove("hide");
                    btnChkNickname.disabled = true;
                    btnChkNickname.classList.add("font-blur");
                    btnChkNickname.classList.add("cursorNot");
                } else {
                    document.querySelector(".nickname-no").classList.add("hide");
                    btnChkNickname.disabled = false;
                    btnChkNickname.classList.remove("font-blur");
                    btnChkNickname.classList.remove("cursorNot");
                }
            } 
        });

        btnChkNickname.addEventListener("click", ()=>{      
            // 한 번 더 확인..?      
            if(newnickname.length > 1 && newnickname.length < 10 && !newnickname.match(regNonWord)){
                console.log(`nickname: ${newnickname}`);
            } else {
                alert(`사용 불가능한 별칭입니다.`);
            }
        });

        document.querySelector(".btn-submit-nickname").addEventListener("click", ()=>{
            // 중복확인 완료 후에 설정 버튼 활성화 요망
            document.querySelector("span.nickname").innerText = newnickname;
        });
    } // if(elExists(document.querySelector("div.nickname"))) 끝


   /*---------- 003 ------------*/
   /*---- ▼  Modal: 프로젝트 설정 탭버튼 시작 ▼ ----*/
   const btnConfigureProject = document.querySelectorAll(".btn-configure-project");
   const configureProjectTabTitle = document.querySelectorAll(".configure-project-tab-title");
   const configureProjectTab = document.querySelectorAll(".configure-project-tab");
   btnConfigureProject.forEach(function(chosenBtn){
    chosenBtn.addEventListener("click", ()=>{
        configureProjectTabTitle.forEach(function(chosenTitle){
            chosenTitle.classList.add("hide");
            if(chosenTitle.dataset.id === chosenBtn.dataset.id) {
                chosenTitle.classList.remove("hide");
            }
        });
        configureProjectTab.forEach(function(chosenTab){
            chosenTab.classList.add("hide");
            if(chosenTab.dataset.id === chosenBtn.dataset.id){
                chosenTab.classList.remove("hide");
            }
        });

    });
   });

   /*---- ▲  Modal: 프로젝트 설정 탭버튼 끝 ▲ ----*/
    

    /*---------- 004 ------------*/
    /* 프로젝트 설정 버튼: 목록 열기(공지등록, 프로젝트설정)  */
    if(elExists(document.querySelector(".btn-open-settings")) && elExists(document.querySelector(".click-proj-setting"))){
        const btnOpenProjSetting = document.querySelector(".btn-open-settings");
        const clickProjSetting = document.querySelector(".click-proj-setting");
        btnOpenProjSetting.addEventListener("click", ()=>{
            // console.log(btnOpenProjSetting.dataset.id); // 추후 서버로 넘길 id
            clickProjSetting.classList.toggle("img-hidden");
        });
    }


    /*---------- 005 ------------*/
    /* 프로젝트 설정 모달 열기 */
    if(elExists(document.querySelector(".btn-open-configure-project")) && elExists(document.querySelector("#configure-project"))){
        const btnOpenModalConfigureProject = document.querySelector(".btn-open-configure-project");
        const modalConfigureProject = document.querySelector("#configure-project");
        btnOpenModalConfigureProject.addEventListener("click", ()=>{
            modalConfigureProject.classList.remove("hide");
            // 언제나 기본설정이 먼저 뜨도록!
            configureProjectTabTitle.forEach(function(chosenTitle){
                chosenTitle.classList.add("hide");
                if(chosenTitle.dataset.id === "configure-project-basic") {
                    chosenTitle.classList.remove("hide");
                }
            });
            configureProjectTab.forEach(function(chosenTab){
                chosenTab.classList.add("hide");
                if(chosenTab.dataset.id === "configure-project-basic"){
                    chosenTab.classList.remove("hide");
                }
            });

        });
    }

    /*---------- 006 ------------*/
    /* 프로젝트 설정 모달 닫기 */
    if(elExists(document.querySelector(".box-close-modal-configure-project"))){
        const btnCloseModalConfigureProject = document.querySelector(".box-close-modal-configure-project");
        btnCloseModalConfigureProject.addEventListener("click", ()=>{
            modalConfigureProject.classList.add("hide");
        });
    }


    /*---------- 007 ------------*/
    /* 할 일 추가 모달 열기 */
    if(elExists(document.querySelector("#btn-add-task")) && elExists(document.querySelector("#container-create-task"))){
        const btnOpenModalAddTask = document.querySelector("#btn-add-task");
        const containerCreateTask = document.querySelector("#container-create-task");

        btnOpenModalAddTask.addEventListener("click", ()=>{
            modalCreateTaskParentTitle.classList.add("img-hidden");
            iconChildTaskTitle.classList.add("hide");
            containerCreateTask.classList.remove("hide");
        });
    }



    /*---------- 008 ------------*/
    /* 할 일 상세 모달 닫기 버튼 */
    if(elExists(document.querySelectorAll(".btn-close-modal-taskDetail"))){
        const btnCloseModalTaskDetail = document.querySelectorAll(".btn-close-modal-taskDetail");
        const containerTaskDetail = document.querySelector("#container-task-detail");
        btnCloseModalTaskDetail.forEach(function(chosenBtn){
            chosenBtn.addEventListener("click", ()=>{
                // containerTaskDetail.classList.add("hide");
                document.querySelector("#container-task-detail .modal-Right").classList.remove("slide-side");
            });
        });
    }

    /*---------- 009 ------------*/
    /* 할 일 제목 수정: span을 눌렀을 때 */
    // fetch의 url이 같아서 인지, img에서 fetch 실행해도
    // span의 response가 또 출력됨..
    // 추후 span과 img를 통합하여 분기처리 해보기
    if(elExists(document.querySelector("span.btn-edit-task-title"))){
        const spanBtnEditTaskTitle = document.querySelectorAll("span.btn-edit-task-title");
        spanBtnEditTaskTitle.forEach(function(chosenOne){
            chosenOne.addEventListener("click", ()=>{

                // 현 url에서 projectId, taskId, memberId(updatedBy) 정보 추출
                let currUrl = decodeURIComponent(new URL(location.href).pathname).split("/");
                console.log(`제목span누름`);

                // 할 일 제목이 담긴 input 출력
                const titleInput = next(chosenOne);
                titleInput.classList.remove("hide");
                titleInput.focus();

                // 기존 할 일 제목이 담긴 span 숨긴다. 추후 바뀐 제목을 담아 출력한다.
                chosenOne.classList.add("hide");

                // 수정 버튼 숨긴다.
                next(titleInput).classList.add("hide");

                // 바뀐 할 일 제목을 서버에 반영할 엔터 모양 버튼(img) 출력
                next(next(titleInput)).classList.remove("hide");

                titleInput.addEventListener("blur", ()=>{
                    console.log('blur 발생');
                    console.log(titleInput.value);
                    const taskHistory = {
                        projectId: currUrl[2],
                        taskId: titleInput.dataset.taskid,
                        modItem: "title",
                        modType: "update",
                        modContent: titleInput.value,
                        updatedBy: titleInput.dataset.mid
                    }

                    fetch('http://localhost:8080/task/editTask?item=title', {
                        method: 'POST',
                        headers: {
                            'Content-type': 'application/json'
                        },
                        body: JSON.stringify(taskHistory)
                    })
                        .then(response => response.text())
                        .then(data => {
                            console.log(`span의 fetch 결과`);
                            console.log(data);

                            // input 숨기고
                            titleInput.classList.add("hide");

                            // enter 버튼 숨기고
                            next(next(titleInput)).classList.add("hide");

                            // span에 새 제목 담고 출력
                            chosenOne.innerHTML = data;
                            chosenOne.classList.remove("hide");

                            // edit 버튼 재 출력
                            next(titleInput).classList.remove("hide");
                        }); // fetch ends

                }); // titleInput.addEventListener ends
            });
        });
    } // edit task title ends (span을 눌렀을 때)

    /* 할 일 제목 수정: img를 눌렀을 때 */
    /*
    if(elExists(document.querySelector("img.btn-edit-task-title"))){
        const btnEditTaskTitle = document.querySelectorAll("img.btn-edit-task-title");
        btnEditTaskTitle.forEach(function(chosenOne){
            chosenOne.addEventListener("click", ()=>{
                console.log(`img 누름`);
                // 할 일 제목이 담긴 input 출력
                const currInput = prev(chosenOne);
                prev(chosenOne).classList.remove("hide");
                currInput.focus();

                // 기존 할 일 제목이 담긴 span 숨긴다. 추후 바뀐 제목을 담아 출력한다.
                prev(currInput).classList.add("hide");

                // 바뀐 할 일 제목을 서버에 반영할 엔터 모양 버튼(img)
                next(chosenOne).classList.remove("hide");

                // 수정 버튼(자기 자신) 숨긴다.
                chosenOne.classList.add("hide");


                currInput.addEventListener("blur", ()=>{
                    console.log('blur 발생');
                    console.log(currInput.value);

                    fetch('http://localhost:8080/task/editTask?item=title', {
                        method: 'POST',
                        headers: {},
                        body: currInput.value
                    })
                    .then(response => response.text())
                    .then(data => {
                        console.log(`img의 fetch 결과`);
                        console.log(data);

                        // input 숨기고
                        currInput.classList.add("hide");

                        // enter 버튼 숨기고
                        next(chosenOne).classList.add("hide");

                        // span에 새 제목 담고 출력
                        prev(currInput).innerHTML = data;
                        prev(currInput).classList.remove("hide");

                        // edit 버튼 재 출력
                        chosenOne.classList.remove("hide");
                    }); // fetch ends

                }); // currInput.addEventListener ends 
            });
        });
    } // edit task title ends (img를 눌렀을 때)
*/
    /*---------- 010 ------------*/
    /* 할 일 담당자 상세보기 및 수정 */

    // 클릭하면 나오는 담당자 상세보기 div 높이 담을 변수
    let boxHeight = 0;
    // 담당자상세보기 div에서 '담당자 추가하기' 누르면 나오는 목록의 Y좌표가 될 변수
    let topValue = 0;
    // 현재 담당자 목록 담을 map
    let currAssignees = new Map();
    // 담당자 수 담을 변수
    let cntAssignee = 0;
    // 현재 미배정 멤버 목록 담을 map (배정할 멤버 검색 시 사용)
    let currUnassignedMembers = new Map();

    // 10-0.해당 버튼 외 다른 곳을 클릭했을 때도 모달이 닫힐 수 있도록 시도중 (blur, tabindex)
    // const testBtnBlur = document.querySelectorAll("[class^=btn]");
    /*
    testBtnBlur.forEach(function(btn){
        btn.addEventListener("click", ()=>{
            console.log(`어떤 버튼이든 click되었을 때`);
        });
    }); */

    // 10-1. 해당 할 일의 담당자 상세보기 열기 (담당자full name목록, 참여하기, 추가하기 포함됨)
    if(elExists(document.querySelector(".btn-more-assignInfo"))){
        const btnOpenAssigneeList = document.querySelectorAll(".btn-more-assignInfo");
        btnOpenAssigneeList.forEach(function(chosenOne){
            chosenOne.addEventListener("click", ()=>{
                // console.log(next(chosenOne)); // <div class="tableView-assignee-list img-hidden">
                // console.log(next(chosenOne).offsetHeight);

                // (이미 담겨있던) 담당자 정보 clear
                currAssignees.clear();

                // 클릭한 task의 현재 담당자 수를 구해서 변수에 담는다.
                // console.log(parents(chosenOne, "[class$=-assignee]")[0].querySelector(".task-assignee-list").children);
                cntAssignee = parseInt(parents(chosenOne, "[class$=-assignee]")[0].querySelector(".task-assignee-list").children.length); // next(chosenOne).children[0].children.length
                // console.log(`현재 담당자 수: ${cntAssignee}`);


                // 현재 배정되어 있는 담당자를 (full name list를 참조하여) map에 담는다. (이 일에서 빼기/빠지기에서 활용된다.)
                for(let i = 0; i < cntAssignee; i++) {
                    currAssignees.set(parents(chosenOne, "[class$=-assignee]")[0].querySelector(".task-assignee-list").children[i].children[0].dataset.mid, parents(chosenOne, "[class$=-assignee]")[0].querySelector(".task-assignee-list").children[i].children[0].innerHTML);
                }

                // 담당자가 없는 경우, 참여하기/담당차추가하기 div의 top-line과 padding값을 뺀다.
                if(cntAssignee === 0 && elExists(next(chosenOne).children[1])) {
                    next(chosenOne).children[1].classList.remove("top-line", "pd-t10");
                }

                // console.log(cntAssignee);
                // 클릭한 담당자 목록 높이 구해서 변수에 담는다. (아래 담당자 배정하기 위치에서 활용)
                boxHeight = next(chosenOne).offsetHeight;

                // 클릭한 task의 담당자 목록 toggle
                if(next(chosenOne).classList.contains("img-hidden")){
                    // 열려있던 다른 box 다 닫아야 한다.
                    const allAssigneeListBox = document.querySelectorAll(".more-assignInfo");
                    allAssigneeListBox.forEach(function(eachOne){
                        eachOne.classList.add("img-hidden");
                    });

                    // 열려있던 '담당자로 추가할 멤버찾기 div'도 다 닫는다
                    const allUnassignedMemberList = document.querySelectorAll("[class$=find-member-to-assign]");
                    allUnassignedMemberList.forEach(function(eachOne){
                        //↓ NodeList(7) [ #text, div.검색.bottom-line.pd-b3, #text, div.배정안된멤버목록.unassigned-member-list.scroll, #text, div.배정안된멤버검색결과, #text ]
                        // console.log(eachOne.childNodes);
                        // [...eachOne.childNodes].filter(el => console.log(el.localName));
                        eachOne.classList.add("img-hidden"); //el.attributes[0].type == "input" ).value = "";
                    });

                    // (검색을 위해)입력 되었던 담당자 이름도 지운다.
                    if(elExists(document.querySelectorAll(".input-findByName-toAssign"))){
                        const inputFindByNameToAssign = document.querySelectorAll(".input-findByName-toAssign");
                        inputFindByNameToAssign.forEach(function(eachInput){
                            eachInput.value = "";
                        });
                    }

                    // 클릭한 task의 담당자 목록만 출력한다.
                    next(chosenOne).classList.remove("img-hidden");

                    // staus view의 경우, 담당자가 없을 땐 열리지 않는다. → 취소. 담당자 추가하기 버튼이 열려야 한다..
                    // if(chosenOne.dataset.view === "table" || (chosenOne.dataset.view === "status" && cntAssignee !== 0)){
                    //     next(chosenOne).classList.remove("img-hidden");
                    // }

                } else {
                    next(chosenOne).classList.add("img-hidden");
                }
                // if(!next(chosenOne).classList.contains("img-hidden"))
                // if(tableViewAssigneeListBox.children[0].classList.contains("img-hidden")){
                //     tableViewAssigneeListBox.children[0].classList.remove("img-hidden");
                // } else {
                //     tableViewAssigneeListBox.children[0].classList.add("img-hidden");
                // }

            });
        });
    } // 10-1. 담당자 목록 보기 ends
    
    // 10-2. 동적으로 생성된 (여러)담당자 상세보기 (btn-more-assignInfo-new) (담당자가 0 혹은 1명이었다가 2명 이상이 되어 해당 버튼이 생긴 경우)
    onEvtListener(document, "click", ".btn-more-assignInfo-new", function(evt){
        //↓ <img className="btn-more-assignInfo ml-3" src="/imgs/down.png" th:src="@{/imgs/down.png}" alt="more assignment info">
        console.log(this);
        //↓ <div class="more-assignInfo tableView-assignee-list">
        console.log(next(this));

        evt.stopPropagation();

        // 버튼을 클릭했을 때 옆 요소(담당자 상세 div)가 열려있다면 닫고
        // 닫혀있다면 열어야 한다.
        if(!next(this).classList.contains("img-hidden")) {
            next(this).classList.add("img-hidden");
        } else {
            // 담당자 정보 clear
            currAssignees.clear();

            // 클릭한 task의 현재 담당자 수
            cntAssignee = next(this).children[0].children.length;

            // 클릭한 task의 현재 담당자 정보(mid, name)를 담는다.
            for(let i = 0; i < cntAssignee; i++){
                // console.log(`기배정담당자의 mid: ${next(this).children[0].children[i].children[0].dataset.mid}`);
                currAssignees.set(next(this).children[0].children[i].children[0].dataset.mid, next(this).children[0].children[i].children[0].innerHTML);
            }
            // 담당자가 없는 상태일 경우
            // 담당자 추가하기 / 참여하기의 div에 top-line을 없앤다.
            if(cntAssignee === 0) {
                next(this).children[1].classList.remove("top-line", "pd-t10");
            }

            // 현재 담당자 수에 따른 담당자 목록 높이를 담아둔다. (멤버목록 출력 시 활용)
            boxHeight = next(this).offsetHeight;

            // 멤버 검색에 입력되어 있던 내용 지운다.
            if(elExists(document.querySelectorAll(".input-findByName-toAssign"))){
                const inputFindByNameToAssign = document.querySelectorAll(".input-findByName-toAssign");
                inputFindByNameToAssign.forEach(function(eachInput){
                    eachInput.value = "";
                });
            }

            // 열려있던 다른 담당자 상세보기 닫는다.
            document.querySelectorAll(".more-assignInfo").forEach(function(eachOne){
                eachOne.classList.add("img-hidden");
            });

            // 열려있던 다른 멤버목록 닫는다.
            document.querySelectorAll("[class$=find-member-to-assign]").forEach(function(eachOne){
                eachOne.classList.add("img-hidden");
            });
            
            // 클릭한 task의 담당자 목록 출력
            next(this).classList.remove("img-hidden");
        }
    });



    // 10-3. 이 일에서 빠지기, 빼기
    // 해당 일의 담당자이거나 작성자인 멤버만, 진행중일때까지만 담당해제 버튼(X)이 노출된다.
    if(elExists(document.querySelector(".btn-dropOut-task"))){
        const btnDropOutTask = document.querySelectorAll(".btn-dropOut-task");
        btnDropOutTask.forEach(function(chosenOne){
            chosenOne.addEventListener("click", (evt)=>{


                // 1) 서버로 정보 넘긴다.
                // RequestParam에 assigneeMid 넘기고
                // task History를 객체로 넘긴다
                const taskHistory = {
                    taskId: chosenOne.dataset.taskid,
                    projectId: chosenOne.dataset.projectid,
                    modItem: "assignee",
                    modType: "delete",
                    modContent: prev(chosenOne).innerText,
                    updatedBy: chosenOne.dataset.executormid,
                }

                // console.log(taskHistory);

                fetch(`http://localhost:8080/task/editAssignee?mid=${chosenOne.dataset.assigneemid}`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(taskHistory)
                }).then(response => {
                    if(response.ok){

                        // 만약 '담당자'가 이 일에서 빠지기를 클릭했다면 reponse 이후 reload 한다.
                        if(chosenOne.dataset.executormid === chosenOne.dataset.assigneemid){
                            location.reload();
                        }

                        // 2) 멤버 목록에 (담당 해제된) 해당 멤버 출력 (append)
                        // parents(parents(chosenOne)[0], ".more-assignInfo")[0].querySelectorAll(`:scope ${".unassigned-member-list"}`)[0].append(unassingedMemberElement(chosenOne.dataset.assigneemid, prev(chosenOne).innerText));
                        parents(chosenOne, ".more-assignInfo")[0].querySelector(".unassigned-member-list").appendChild(unassingedMemberElement(chosenOne.dataset.assigneemid, prev(chosenOne).innerText)); // 해당 div의 높이가 조절되지 않는다?
                        // console.log(parents(parents(chosenOne)[0], ".more-assignInfo")[0].querySelector(".unassigned-member-list"));

                        // 3) 변경된 boxHeight를 멤버목록 top에 반영
                        // status에는 멤버목록이 없기 때문에(검색만 있다) 높이를 반영할 요소가 있는지 확인 후 진행한다.
                        boxHeight = parents(parents(chosenOne)[0], ".more-assignInfo")[0].offsetHeight;
                        if(parents(parents(chosenOne)[0], ".more-assignInfo")[0].querySelectorAll(`:scope ${".find-member-to-assign"}`)[0] !== null && parents(parents(chosenOne)[0], ".more-assignInfo")[0].querySelectorAll(`:scope ${".find-member-to-assign"}`)[0] !== undefined){
                            topValue = boxHeight - 75;
                            console.log(`2-1. 이 일에서 빠지기, 빼기의 topValue: ${topValue}`)
                            parents(parents(chosenOne)[0], ".more-assignInfo")[0].querySelectorAll(`:scope ${".find-member-to-assign"}`)[0].style.top = topValue + 'px';
                        }

                        // 4) 현재 담당자 수에서 1 차감한다.
                        cntAssignee--;

                        // 5) 현재 담당자 map에서 담당자 삭제한다.
                        currAssignees.delete(chosenOne.dataset.assigneemid);

                        // 6) 담당자 요약 목록에서 해당 담당자 이름 제거
                        if(cntAssignee === 0) { // one → none

                            // one-assignee는 remove
                            if(parents(chosenOne, "[class$=-assignee]")[0].children[0].classList.contains("one-assignee")){ // table view
                                parents(chosenOne, "[class$=-assignee]")[0].children[0].remove();
                            }
                            if(chosenOne.dataset.view === "status") {
                                parents(chosenOne, ".status-assignee")[0].querySelector(".one-assignee").remove();
                            }

                            // no-assignee를 prepend
                            parents(chosenOne, "[class$=-assignee]")[0].prepend(assigneeBoxByNum(0));

                            // 담당자 추가하기에 top-line 없애기
                            parents(chosenOne, "[class^=more-]")[0].children[1].classList.remove("top-line", "pd-t10");

                        } else if (cntAssignee === 1) { // many → one

                            // 더보기 버튼(img) remove (status view에는 없기 때문에 요소가 있는지 검사 후 진행한다.)
                            if (elExists([...parents(chosenOne, "[class$=-assignee]")[0].children].filter(child => child.tagName === "IMG")[0])){
                                [...parents(chosenOne, "[class$=-assignee]")[0].children].filter(child => child.tagName === "IMG")[0].remove();
                            }

                            // staus view와 통합 전:
                            // if(parents(chosenOne, "[class$=-assignee]")[0].children[0].className === "many-assignee"){
                            //     // parents(chosenOne, "[class$=-assignee]")[0].children[0].classList.add("hide");
                            //     let manyAssigneeList = [...parents(chosenOne, "[class$=-assignee]")[0].children].filter(child => child.classList.contains("many-assignee"));
                            //     for(let i = 0; i < manyAssigneeList.length; i++) {
                            //         manyAssigneeList[i].remove();
                            //     }
                            // }
                            // div.table-assignee에 남은 담당자(span.one-assignee) prepend
                            // console.log(currAssignees); // status view: Map(0)
                            // console.log(parents(chosenOne, "[class$=-assignee]")[0]);
                            // status view: <div class="status-assignee">
                            // table view: <div class="flex-row-center-center-n…w-column table-assignee">
                            if(chosenOne.dataset.view === "status") {
                                currAssignees.forEach((v, k) => parents(chosenOne, "[class$=-assignee]")[0].firstElementChild.prepend(assigneeBoxByNum(1, k, v)));
                            } else {
                                currAssignees.forEach((v, k) => parents(chosenOne, "[class$=-assignee]")[0].prepend(assigneeBoxByNum(1, k, v)));
                            }


                            // many-assignee remove
                            parents(chosenOne, "[class$=-assignee]")[0].querySelectorAll(".many-assignee").forEach(function(eachOne){
                                eachOne.remove();
                            });

                        } else { // many에서 many 유지 (cntAssignee가 2이상인 경우)

                            // 담당자 요약목록에서 담당해제된 멤버의 mid를 찾아 지운다
                            // full name에는 assigneemid, 요약 목록에는 mid
                            // const assigneeFirstNameList = [...parents(chosenOne, "[class$=-assignee]")[0].children].filter(child => child.classList.contains("many-assignee")); // status view와 통합 전
                            const assigneeFirstNameList = [...parents(chosenOne, "[class$=-assignee]")[0].querySelectorAll(".many-assignee")];
                            for(let i = 0; i < assigneeFirstNameList.length; i++){
                                if(assigneeFirstNameList[i].children[0].dataset.mid === chosenOne.dataset.assigneemid) {
                                    assigneeFirstNameList[i].remove();
                                }
                            }
                        }

                        // 7) 해당 담당자 이름 담긴 box를 (담당자 full name 목록에서) 삭제한다.
                        parents(chosenOne)[0].remove();

                        // 8)

                    } // if(response.ok) ends
                }); // fetch ends
            });
        });
    }

    // 10-4A. 새로 배정했던 담당자 다시 해제(이 일에서 빠지기)
    // 동적으로 생성된 해당 요소의 class 명은 btn-cancel-assign-task로 부여한다. (cf. btn-dropOut-task)
    onEvtListener(document, "click", ".btn-cancel-assign-task", function(){
        console.log(this);
        //↑ <span class="btn-cancel-assign-task ml-3 cursorP" data-assigneemid="14" data-executormid="14" data-authormid="14" data-taskid="8" data-projectid="9">

        // 1) 서버에 전달
        const taskHistory = {
          taskId: this.dataset.taskid,
          projectId: this.dataset.projectid,
          modItem: "assignee",
          modType: "delete",
          modContent: prev(this).innerHTML,
          updatedBy: this.dataset.executormid
        };
        fetch(`http://localhost:8080/task/editAssignee?mid=${this.dataset.assigneemid}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(taskHistory)
        }).then(response => {
            if(response.ok){
                // 2) 멤버 목록에 다시 추가하기
                parents(this, "[class^=more-]")[0].children[2].children[1].appendChild(unassingedMemberElement(this.dataset.assigneemid, prev(this).innerHTML));

                // 3) boxHeight 조정
                boxHeight = parents(this, "[class^=more-]")[0].offsetHeight;
                topValue = boxHeight - 75;
                console.log(`2-2. 새로 배정했던 담당자 다시 해제(이 일에서 빠지기)의 topValue: ${topValue}`)
                if(parents(this, "[class^=more-]")[0].querySelectorAll(`:scope ${".find-member-to-assign"}`)[0] !== null && parents(this, "[class^=more-]")[0].querySelectorAll(`:scope ${".find-member-to-assign"}`)[0] !== undefined){
                    parents(this, "[class^=more-]")[0].querySelectorAll(`:scope ${".find-member-to-assign"}`)[0].style.top = topValue + 'px';
                }
                // 4) 현재 담당자 수에서 1 차감
                cntAssignee--;

                // 5) 현재 담당자목록 map에서 삭제
                currAssignees.delete(this.dataset.assigneemid);

                // 6-1) 담당자 요약 목록에서 빼기
                // one → none
                if(cntAssignee === 0){
                    // one-assignee 제거
                    parents(this, "[class$=-assignee]")[0].children[0].remove();
                    // no-assignee prepend
                    parents(this, "[class$=-assignee]")[0].prepend(assigneeBoxByNum(0));
                    // 담당자 추가하기에 top-line과 padding 없애기
                    parents(this, "[class^=more-]")[0].children[1].classList.remove("top-line", "pd-t10");
                }

                // many → one
                if(cntAssignee === 1){
                    // 더보기 버튼(img) 제거
                    [...parents(this, "[class$=-assignee]")[0].children].filter(child => child.tagName === "IMG")[0].remove();

                    // many-assignee 제거
                    if(parents(this, "[class$=-assignee")[0].children[0].className === "many-assignee"){
                        const manyAssigneeList = [...parents(this, "[class$=-assignee")[0].children].filter(child => child.classList.contains("many-assignee"));
                        for(let i = 0; i < manyAssigneeList.length; i++){
                            manyAssigneeList[i].remove();
                        }
                    }

                    // 남은 담당자(1명) prepend
                    currAssignees.forEach((v, k) => parents(this, "[class$=-assignee]")[0].prepend(assigneeBoxByNum(1, k, v)));
                }

                // many → many
                if(cntAssignee > 1) {
                    const assigneeFirstNameList = [...parents(this, "[class$=-assignee]")[0].children].filter(child => child.classList.contains("many-assignee"));
                    for(let i = 0; i < assigneeFirstNameList.length; i++){
                        if(assigneeFirstNameList[i].children[0].dataset.mid === this.dataset.assigneemid) {
                            assigneeFirstNameList[i].remove();
                        }
                    }
                }
                // 6-2) 담당자 full name div에서 빼기
                parents(this)[0].remove();
            } else {
                console.log(`2-2. 새로 배정했던 담당자 다시 해제(이 일에서 빠지기)의 fetch 결과 확인요망`);
            }
        }); // fetch ends

    }); // 10-4A 새로 배정했던 담당자 다시 해제(btn-cancel-assign-task) Ends.

    // 10-4B. (동적으로 생성된) 참여취소 (status view에서 보류, 시작 안 함, 계획중인 일에 참여하기(btn-bePartOf-task)를 누른 사람에게만 나타나는 버튼.)
    // cf. 이 일에서 빠지기: btn-dropOut-task
    onEvtListener(document, "click", ".btn-cancel-participation", function(){
        console.log("참여취소 눌렀음");
        console.log(this);
        const datum = this.dataset; // project id, task id, mid, nickname, view

        console.log(datum);

        // 서버로 보낼 정보 추출
        const taskHistory = {
            projectId: datum["projectid"],
            taskId: datum["taskid"],
            modItem: "assignee",
            modType: "delete",
            modContent: datum["nickname"],
            updatedBy: datum["mid"]
        };

        // 서버로 전송
        // 담당자 추가 시, taskHistory에는 작성자의 member id가 담긴다. (as executor; updatedBy)
        // 때문에 editAssignee에는 추가/삭제된 담당자의 member id가 파라미터로 담긴 상황
        // modContent에 이름과 id를 묶어서 보내어 끊어 사용하려 해봤으나 (json stringfy로 전달된 객체의 값은 배열이었다...)
        // 예) 심지호(34) ← 괄호의 위치를 찾아 인덱스로 받아 끊어내는 작업을 하느니, 파라미터로 던지는 게 낫다고 판단함.

        fetch(`http://localhost:8080/task/editAssignee?mid=${datum["mid"]}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(taskHistory)
        }).then(response => {
            if(response.ok){
                console.log(`서버로 전송 및 결과 반환 ok`);
                // 이전 정보를 지운다.
                currAssignees.clear();

                // 참여취소 누르기 전의 담당자 수를 저장한다.
                cntAssignee = parents(this, ".status-assignee")[0].querySelector(".task-assignee-list").children.length;
                console.log(cntAssignee);

                // 참여 취소 누르기 전의 담당자 정보를 currAssingees에 담는다.
                [...parents(this, ".status-assignee")[0].querySelectorAll(".assignee-fullname-box p")].forEach(function (eachAssignee){
                    currAssignees.set(eachAssignee.dataset.mid, eachAssignee.innerHTML);
                });
                console.log(currAssignees);

                // 현재 담당자인 멤버 map(currAssignees)에서 제거한다.
                currAssignees.delete(datum["mid"]);

                console.log(currAssignees);

                // 현재 담당자 수에서 제거한다.
                cntAssignee--;
                console.log(cntAssignee);

                // 담당자 풀네임에서 제거한다.
                [...parents(this, ".status-assignee")[0].querySelectorAll(".assignee-fullname-box p")].forEach(function(each){
                    if(each.dataset.mid === datum["mid"]){
                        parents(each)[0].remove();
                    }
                });

                // 배정안된멤버 map(검색을 위한 data)에 참여취소를 누른 멤버의 정보 담는다.
                currUnassignedMembers.set(datum["mid"], datum["nickname"]);
                console.log(currUnassignedMembers)

                // 검색 결과로 출력하기 위해 unassigned-member-list에도 추가한다.
                parents(this, ".status-assignee")[0].querySelector(".unassigned-member-list").append(unassingedMemberElement(datum["mid"], datum["nickname"]));


                // 담당자 요약 목록에서 제거한다.
                if(cntAssignee === 0) { // one to none
                    console.log("one to none");
                    // (status view 구조상, 담당자 이름 요약 요소가 full name div와 같은 부모 아래 있기 때문에,
                    // 담당자 이름 일단 hide 후, no-assignee 요소 prepend 후에 one-assignee 제거
                    parents(this, ".status-assignee")[0].querySelector(".one-assignee").classList.add("hide");
                    parents(this, ".status-assignee")[0].firstElementChild.append(assigneeBoxByNum(0));
                    parents(this, ".status-assignee")[0].querySelector(".one-assignee").remove();
                }

                if(cntAssignee === 1){ // many to one
                    console.log(currAssignees);
                    // 내가 빠지고 남은 멤버의 이름을 one-assignee 요소로 변환하여 화면에 부착한다.
                    currAssignees.forEach((v, k) => parents(this, ".status-assignee")[0].firstElementChild.prepend(assigneeBoxByNum(1, k, v)));

                    // many-assignee 요소 없앤다.
                    parents(this, ".status-assignee")[0].querySelectorAll(".many-assignee").forEach(function(eachOne){
                        eachOne.remove();
                    });
                }

                if(cntAssignee > 1) { // many to many
                    console.log("many to many");
                    // many-assignee '요소'를 받는다.
                    const manyAssigneeFirstNameBoxes = [...parents(this, ".status-assignee")[0].querySelectorAll(".many-assignee")];
                    for(let i = 0; i < manyAssigneeFirstNameBoxes.length; i++) {
                        if(manyAssigneeFirstNameBoxes[i].children[0].dataset.mid === datum["mid"]){
                            manyAssigneeFirstNameBoxes[i].remove();
                        }
                    }
                }

                // hide했던 참여하기 버튼을 다시 보이게 한 후
                parents(this, ".status-assignee")[0].querySelector(".btn-bePartOf-task").classList.remove("hide");

                // 참여취소 버튼 없앤다.
                this.remove();
            }
        });




    });

    // 10-5. 이 일에 참여하기 (해당 일에 이미 배정된 담당자가 아니고, 해당 일의 진행상태가 '계획중'일때까지만 출력된 버튼)
    if(elExists(document.querySelector(".btn-bePartOf-task"))){
        const btnApplySelfTask = document.querySelectorAll(".btn-bePartOf-task");
        btnApplySelfTask.forEach(function(btn){
            btn.addEventListener("click", ()=>{

                const currView = btn.dataset.view;
                if(currView === "table") {
                    const datum = btn.children[1].dataset;
                    const currAssigneeCnt = parents(btn, ".more-assignInfo")[0].querySelector(".task-assignee-list").children.length
                    console.log("table view에서 참여하기 눌렀을 때: ")
                    console.log(datum); // DOMStringMap(4) { mid → "14", nickname → "공지철", projectid → "9", taskid → "11" }
                    console.log(`현재 담당자 수: ${currAssigneeCnt}`); // 현재 담당자 수: 1
                    console.log(currAssignees); // 담당자 상세보기 버튼을 누를 때 담긴다.
                    // 담당자가 6명 미만일 때만 참여하기 가능하다
                    // 담당자 배정 및 해제가 자유로운 상태이기 때문에
                    // 참여하기 버튼 자체는 진행중 이전의 할 일에 담당자가 아닌 멤버에게는 모두 노출된다.
                    // 배정되는 과정에서 6명이상인지 아닌지 서버에서 확인할 필요가 있겠다.

                }

                if(currView === "status") {
                    const datum = btn.dataset;
                    let cntAssignee = parents(btn, ".status-assignee")[0].querySelector(".task-assignee-list").children.length;
                    console.log("status view에서 참여하기 눌렀을 때: ")
                    console.log(datum); // DOMStringMap(4) { mid → "14", nickname → "공지철", projectid → "9", taskid → "21" }
                    console.log(`현재 담당자 수: ${cntAssignee}`);

                    // 0. 이전에 담긴 currAssignees를 먼저 clear
                    // (status view에서는 '참여하기'가 btn-more-assignInfo를 거치지 않고 시작된다.)
                    currAssignees.clear();

                    // 1-1. 현재 담당자를 currAssignees map에 담는다 (∴status는 참여하기 버튼이 btn-moreAssign-info와 별도로 있다)
                    // console.log(parents(btn, ".status-assignee")[0]); // <div class="status-assignee">
                    // 개별담당자div
                    [...parents(btn, ".status-assignee")[0].querySelector(".task-assignee-list").children].forEach(function(eachAssignee){
                        // 개별담당자 div 안의 p
                        currAssignees.set(eachAssignee.children[0].dataset.mid, eachAssignee.children[0].innerHTML);
                    });

                    console.log(currAssignees);

                    // 1-2. 현재 미배정 멤버 목록을 currUnassignedMembers map에 담는다. (검색에 사용한다.)
                    [...parents(btn, ".status-assignee")[0].querySelectorAll(".unassigned-member p")].forEach(function(eachMember){
                        currUnassignedMembers.set(eachMember.dataset.mid, eachMember.innerHTML);
                    });

                    console.log(currUnassignedMembers);

                    // 1-3. 참여하기를 누른 멤버를 currUnassignedMembers에서 제거한다.
                    currUnassignedMembers.delete(datum["mid"]);

                    // console.log(parents(btn, ".status-assignee")[0].querySelector(".task-assignee-list").children.length);
                    // console.log(parents(btn, ".status-assignee")[0].querySelector(".statusView-assignee-list").children[0].children.length);

                    // 2. 기존 배정된 담당자 수가 6명 미만 때만 실행한다.
                    if(cntAssignee < 6) {
                        // 2-1. 추가하기 이전의 담당자 수를 beforeCnt에 담는다. (담당자 요약 박스에 담을 형태를 확인하기 위함)
                        const beforeCnt = cntAssignee;

                        // 2-2. 담당자 수 currAssigneeCnt를 추가한다.
                        cntAssignee++;

                        // 2-3. 참여하기를 누른 자의 member id와 name을 currAssignees map에 담는다
                        // 참여하기 버튼 div 안의 span
                        currAssignees.set(datum["mid"], datum["nickname"]);

                        // 2-4. 서버로 보낼 정보(TaskHistory)를 생성한다.
                        // TaskHistory에 보낼 정보 추출한다
                        const taskHistory = {
                            projectId: datum["projectid"],
                            taskId: datum["taskid"],
                            modItem: "assignee",
                            modType: "register",
                            modContent: datum["nickname"],
                            updatedBy: datum["mid"]
                        };

                        console.log(taskHistory);

                        // 2-5. 서버로 전송
                        fetch(`http://localhost:8080/task/editAssignee?mid=${btn.dataset.mid}`, {
                            method: 'POST',
                            headers: {
                                'Content-Type': 'application/json'
                            },
                            body: JSON.stringify(taskHistory)
                        }).then(response => {
                            if(response.ok){
                                console.log('서버로 전송 및 결과 반환 ok');
                                // 서버 전송 성공시: 담당자 풀네임 박스에 이름담는다
                                parents(btn, ".status-assignee")[0].querySelector(".task-assignee-list").append(newAssigneeBySelf(datum));

                                // 서버 전송 성공시: 담당자 요약 박스에 담는다
                                // ★★★ DB에 저장 전에 현재 담당자가 6명인지 확인하는 로직이 필요하다!!

                                // 담당자요약박스: 담당자없음 → 담당자 1명
                                if (cntAssignee === 1) {
                                    console.log("담당자 없다가 추가되었을 때");
                                    console.log(currAssignees);
                                    console.log(assigneeBoxByNum(1, btn.dataset.mid, btn.dataset.nickname));
                                    // status view는 status-assignee 안에
                                    // no-assignee와 담당자 full name list가 형제이기 때문에 지우면 안된다. 일단 숨기고
                                    parents(btn, ".status-assignee")[0].querySelector(".no-assignee").classList.add("hide");

                                    // one-assignee div 생성해서 '배정된 담당자'div의 첫 자식요소 뒤에 append 한다.
                                    parents(btn, ".status-assignee")[0].firstElementChild.append(assigneeBoxByNum(1, btn.dataset.mid, btn.dataset.nickname));

                                    // no-assignee 요소 지운다.
                                    parents(btn, ".status-assignee")[0].querySelector(".no-assignee").remove();
                                }

                                // 담당자요약박스: 담당자 1명 → 담당자 다수(many-assignee)
                                // table view의 경우 담당자를 추가한 후에 (모달을 열어둔 상태에서) 기존 담당자를 삭제할 수도 있기 때문에
                                // 이전 담당자 수만이 아닌 현재 담당자 수도 확인하고 one to many를 진행해야 하지만
                                // staus view의 참여하기는 참여하기를 누르면 바로 요약 목록에 추가되고
                                // 참여하기를 누른 멤버에게는 참여 취소 버튼만 노출되고
                                // 담당자 삭제는 할 수 없기 때문에 이전 담당자 수가 1이었는지만 확인하면 된다.
                                if (beforeCnt === 1) {
                                    // 담당자 이름 요약 부착 (status view에는 btn-more-assignInfo 버튼을 부착하지 않고, 담당자 추가 버튼을 클릭하면 full name list가 뜨도록 한다)
                                    console.log("one to many");
                                    console.log(currAssignees); // Map { 28 → "스칼렛 요한슨", 14 → "공지철" }

                                    parents(btn, ".status-assignee")[0].querySelector(".one-assignee").classList.add("hide");
                                    for(const entry of currAssignees) {
                                        parents(btn, ".status-assignee")[0].firstElementChild.append(assigneeBoxByNum(2, entry[0], entry[1]));
                                    }
                                    parents(btn, ".status-assignee")[0].querySelector(".one-assignee").remove();
                                }

                                // 담당자요약박스: many → many
                                if(beforeCnt > 1 && cntAssignee > 1){
                                    console.log("many to many");
                                    console.log(currAssignees);
                                    console.log([...parents(btn, ".status-assignee")[0].querySelectorAll(".many-assignee")]);
                                    parents(btn, ".status-assignee")[0].firstElementChild.append(assigneeBoxByNum(2, btn.dataset.mid, btn.dataset.nickname));
                                }

                                // 참여취소 버튼으로 바꾼다.
                                parents(btn, ".status-assignee")[0].append(btnCancelParticipation(datum));

                                // 참여하기 버튼을 동적으로 또 생성하는 것보다는 숨기기했다가 다시 드러내는 게 좋을 듯!
                                btn.classList.add("hide");
                            }
                        });

                    } else {
                        alert(`담당자는 6명을 초과할 수 없습니다.`);
                    }
                }



            });
        });
    }

    // 055. 참여취소 버튼 생성
    function btnCancelParticipation(datum){
        const div = document.createElement("div");
        const span = document.createElement("span");
        div.classList.add("mr-5", "opacity0", "btn-cancel-participation");
        div.setAttribute("data-mid", datum["mid"]);
        div.setAttribute("data-nickname", datum["nickname"]);
        div.setAttribute("data-projectid", datum["projectid"]);
        div.setAttribute("data-taskid", datum["taskid"]);
        span.classList.add("font-11");
        span.innerText = "참여취소";
        div.appendChild(span);
        return div;
    }

    // 10-6A. table view의 담당자 추가하기: 새로운 담당자 배정하기
    //  (∵table view는 멤버목록 div가 담당자div의 우측으로 추가됨. cf. status view는 btn-more-assignInfo를 누르면 바로 담당자 목록이 뜸)
    // - 작성자만이 가능하며, 해당 일의 진행상태가 '검토중'일 때까지만 가능
    // - 검색창에서 새로 배정할 사람을 입력한다. (검색버튼(btn-findByName-member-to-assign)은 next에 의해 지정됨)
    // - 해제한 사람도 다시 배정할 수 있어야 한다.
    if(elExists(document.querySelector(".btn-add-member-to-assignee"))){
        const btnFindMemberToAssign = document.querySelectorAll(".btn-add-member-to-assignee");
        btnFindMemberToAssign.forEach(function(chosenOne){
            chosenOne.addEventListener("click", (evt)=>{
                evt.stopPropagation();

                const executorMid = chosenOne.dataset.executor;
                const thisAuthorMid = chosenOne.dataset.authormid;
                const thisTaskId = chosenOne.dataset.taskid;
                const thisProjectId = chosenOne.dataset.projectid;

                // 1. 해당 프로젝트 멤버 목록 div 화면 노출(출력)
                boxHeight = parents(chosenOne, "[class^=more-]")[0].offsetHeight;
                topValue = boxHeight - 40;
                // status인지, table인지 구분해야할 듯.
                next(chosenOne).style.top = topValue + 'px';
                if(next(chosenOne).classList.contains("img-hidden")){
                    next(chosenOne).classList.remove("img-hidden");
                } else {
                    next(chosenOne).classList.add("img-hidden");
                }

                // 2. 멤버 검색 버튼 클릭했을 때
                // btn-findByName-member-to-assign
                const btnFindByNameMemberToAssign = next(chosenOne).children[0].children[1];
                btnFindByNameMemberToAssign.addEventListener("click", ()=>{
                    // input에 입력된 값 받아온다
                    console.log(prev(btnFindByNameMemberToAssign).value);
                });

                // 3. 담당자(새로 배정): 멤버 목록에서 '멤버 이름' 클릭했을 때
                const unassignedMember = next(chosenOne).children[1].querySelectorAll(".unassigned-member");
                unassignedMember.forEach(function(member){
                    member.addEventListener("click", (evt)=>{
                        evt.stopImmediatePropagation();

                        // many → many 인지 one → many 인지 확인하기 위해 beforeCnt값 필요
                        const beforeAssigneeNum = cntAssignee;

                        // 담당자는 6명을 초과할 수 없다.
                        if(cntAssignee < 6){

                            cntAssignee++;
                            const newAssgineeName = member.children[0].innerText;
                            const newAssigneeMid = member.children[0].dataset.mid;

                            const taskHistory = {
                                taskId: thisTaskId,
                                projectId: thisProjectId,
                                modItem: "assignee",
                                modType: "register",
                                modContent: newAssgineeName,
                                updatedBy: executorMid
                            }
                            // console.log(taskAssignment);
                            
                            // 1)-1 담당자 풀네임(fullname) div에 이름 출력
                            parents(chosenOne, ".more-assignInfo")[0].firstElementChild.append(newAssigneeElement(newAssgineeName, newAssigneeMid, executorMid, thisAuthorMid, thisTaskId, thisProjectId, member.dataset.view));

                            // 1)-2 담당자 div 높이 변경에 따른 멤버 목록 위치(top) 변경
                            topValue = (parents(member, ".more-assignInfo")[0].offsetHeight) - 32;
                            next(chosenOne).style.top = topValue + 'px';
                            console.log(`4A. 담당자 추가하기-멤버클릭했을 때 (table view)의 topValue: ${topValue}`)

                            // 2) 서버에 전달
                            fetch(`http://localhost:8080/task/editAssignee?mid=${newAssigneeMid}`, {
                                method: 'POST',
                                headers: {
                                    'Content-Type': 'application/json'
                                },
                                body: JSON.stringify(taskHistory)
                            }).then(response => {
                                if(response.ok){
                                    // 담당자 축약 목록에 출력
                                    // 멤버 추가 후 담당자가 1명이 된 상태 (이전에는 담당자가 없었던 상태)
                                    if (cntAssignee === 1) {

                                        // no-assignee를 일단 hide한 후에
                                        console.log(`기배정담당자인원: ${parents(member, "[class$=-assignee]")[0].children[0].className}`);
                                        parents(member, "[class$=-assignee]")[0].children[0].classList.add("hide");

                                        // (remove했던) 담당자 추가 div에 top-line과 pd-t10을 붙인다.
                                        // parents(member, "[class$=-assignee]")[0].children[1].children[1]의 classList에 top-line, pd-t10 add
                                        // console.log(`top-line붙여야할 요소 ↓`);
                                        // console.log(parents(member, "[class$=-assignee]")[0].children[1].children[1]);
                                        parents(member, "[class$=-assignee]")[0].children[1].children[1].classList.add("top-line", "pd-t10");

                                        // no-assignee를 삭제
                                        parents(member, "[class$=-assignee]")[0].children[0].remove();

                                        // one-assignee를 parents(member, "[class$=-assignee]")[0]에 prepend (첫번째자식으로 붙는다.)
                                        parents(member, "[class$=-assignee]")[0].prepend(assigneeBoxByNum(1, newAssigneeMid, newAssgineeName));

                                    }

                                    // 멤버 추가 후에 one → many가 된 상태
                                    // 기존 배정된 담당자의 이름도 축약해서 출력해야 한다.★★★
                                    // 멤버 추가한 후에 (모달을 열어둔 상태에서) 기존 담당자를 지울 수도 있기 때문에
                                    // 현재 담당자의 수도 체크한 후에 one to many를 적용해야 한다.
                                    if (beforeAssigneeNum === 1 && cntAssignee > 1) {
                                        // console.log(`div.many-assignee에 기존 담당자 이름첫글자와, 새로 배정된 담당자 이름 첫글자를 span.lastName append`);

                                        // one-assignee는 hide가 아니라 remove해야 btn-more-assignInfo-new버튼이 작동할 수 있음.
                                        // parents(member, "[class$=-assignee]")[0].children[0].classList.add("hide");
                                        parents(member, "[class$=-assignee]")[0].children[0].remove();
                                        // many-assignee의 moreInfo 버튼 prepend
                                        parents(member, "[class$=-assignee]")[0].prepend(btnMoreAssigneeInfo());

                                        // 새 담당자 prepend
                                        parents(member, "[class$=-assignee]")[0].prepend(assigneeBoxByNum(2, newAssigneeMid, newAssgineeName));

                                        // 기존 담당자 many로 변환해서 prepend
                                        for(const entry of currAssignees){
                                            // console.log(entry); // Array [ "26", "송혜교" ]
                                            // console.log(entry[0]); // mid
                                            // console.log(entry[1]) // name
                                            parents(member, "[class$=-assignee]")[0].prepend(assigneeBoxByNum(2, entry[0], entry[1]));
                                        }

                                    }

                                    // many → many 인 상태
                                    if (beforeAssigneeNum > 1) {
                                        // ↓ <div class="flex-row-center-center-n…w-column table-assignee">
                                        // console.log(parents(member, "[class$=-assignee]")[0]);

                                        // console.log(`요소를 찾아요*******************************************`);

                                        // ↓ <img class="btn-more-assignInfo cursorP ml-3" src="/imgs/down.png" alt="more assignment info">
                                        // console.log([...parents(member, "[class$=-assignee]")[0].children].at(-2));

                                        // ↓ <div class="more-assignInfo tableView-assignee-list">
                                        // console.log([...parents(member, "[class$=-assignee]")[0].children].at(-1));

                                        // div.many-assignee의 마지막 요소인 img(btn-more-assignInfo) 앞에 붙여야 함.
                                        [...parents(member, "[class$=-assignee]")[0].children].at(-2).before(assigneeBoxByNum(2,newAssigneeMid, newAssgineeName));
                                    }

                                    // 현재 담당자 Map에 담기
                                    currAssignees.set(newAssigneeMid, newAssgineeName);

                                    // 3) 멤버 목록에서 이름 빼기
                                    // member.remove();
                                    member.classList.add("hide");
                                }
                            });
                        } else {
                            alert(`담당자는 6명을 초과할 수 없습니다.`);
                        }
                    });
                });

            });
        });
    } // 10-6A. table view 담당자 추가하기: 새로운 담당자 배정하기 끝

    // 10-6B. status view 담당자 추가(이미 배정된 담당자의 풀네임 목록과 검색하여 배정하기 창이 뜬다)
    if(elExists(document.querySelector(".btn-add-assignee"))){
        const btnAddAssignees = document.querySelectorAll(".btn-add-assignee");
        btnAddAssignees.forEach(function(btn){
            btn.addEventListener("click", ()=>{

                // 담당자 목록을 열든 닫든 이미 담겨있던 담당자 map은 비운다.
                currAssignees.clear();

                // 클릭한 task의 담당자 full name 목록과 배정 안된 멤버를 검색할 수 있는 모달 창이 닫혀있다면 열고,
                // 열려있다면 닫는다. (toggle)
                if (parents(btn, ".status-assignee")[0].querySelector(".more-assignInfo").classList.contains("img-hidden")) {
                    // 열려있던 (다른) 담당자 정보 모달 모두 닫는다.
                    document.querySelectorAll(".more-assignInfo").forEach(function(otherModal){
                        otherModal.classList.add("img-hidden");
                    });

                    // 검색하기 위해 입력했던 내용 모두 지운다.
                    if(elExists(document.querySelector(".input-findByName-toAssign"))){
                        document.querySelectorAll(".input-findByName-toAssign").forEach(function(eachInput){
                            eachInput.value = "";
                        });
                    }

                    // 클릭한 task의 모달(만) 연다.
                    parents(btn, ".status-assignee")[0].querySelector(".more-assignInfo").classList.remove("img-hidden");


                    // 이미 배정되어 있던 담당자 수를 구한다.
                    cntAssignee = parents(btn, ".status-assignee")[0].querySelector(".task-assignee-list").children.length;

                    // 이미 배정되어 있던 담당자의 member id와 이름을 currAssignees map에 담는다.
                    parents(btn, ".status-assignee")[0].querySelectorAll("div.assignee-fullname-box > p").forEach(function(eachOne){
                        currAssignees.set(eachOne.dataset.mid, eachOne.innerHTML);
                    });

                    // 담당자 추가는 이미 배정된 담당자가 6명 미만일 때만 가능하다.
                    if (cntAssignee < 6){
                        // 서버에 보낼 정보 담는다.

                        // 배정안된멤버 currUnassignedMembers map에서 뺀다.
                        // 배정안된멤버 목록 요소에서 뺀다.

                        // 담당자 수를 추가한다.
                        cntAssignee++;

                        // 담당자 map에 담는다
                        // 담당자 풀네임 박스에 이름을 담는다
                        // 담당자 요약 박스에 이름을 담는다
                    }

                } else {
                    parents(btn, ".status-assignee")[0].querySelector(".more-assignInfo").classList.add("img-hidden");
                }

            });
        });
    } // 10-6B.status view의 담당자 추가 ends

    // 10-7. 해제했던 담당자 다시 배정하기
    // 작성자의 경우, ('완료'이전까지만 )담당자 목록에 이름 추가
    // 담당자의 경우, ('계획중'인 경우까지만 )'참여하기' 버튼 출력
    onEvtListener(document, "click", ".unassigned-member", function(){
        console.log(this);
    });


    // 10-8. 담당자 검색하여 추가하기
    // 이미 받아둔 unassigned-member에서 검색 시행하고
    // unassigned-member 클릭 이벤트와 동일하게 진행하면 될 듯?
    // (status view. ∵ 멤버 목록이 우측으로 나올 경우, 다른 진행상태 div의 overflow를 수정해야 한다... z-index로 해결안됨)
    if(elExists(document.querySelector(".btn-findByName-member-to-assign"))){
        const findMemberByNameToAssign = document.querySelectorAll(".btn-findByName-member-to-assign");
        findMemberByNameToAssign.forEach(function(statusviewSearchMemberToAssign){
            statusviewSearchMemberToAssign.addEventListener("click", ()=>{
                console.log(`status view에서 담당자 추가 위해 멤버 검색 버튼 눌렀을 때`);
                console.log(`${prev(statusviewSearchMemberToAssign).value}`);
                const searchCondTaskId = prev(prev(statusviewSearchMemberToAssign)).dataset.taskid;
                const searchCondProjectId = prev(prev(statusviewSearchMemberToAssign)).dataset.projectid;
                const searchName = prev(statusviewSearchMemberToAssign).value;
                const getMemberName = {
                  projectId: searchCondProjectId,
                  taskId: searchCondTaskId,
                  nickname: searchName
                };
                fetch(`http://localhost:8080/task/search?object=member&purpose=assign`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(getMemberName)
                }).then(response => response.text())
                    .then(data => {
                        console.log(data);
                    });
            });
        });
    } // // 10-8. 담당자 검색하여 추가하기 끝

    /* 10-9. 할 일 진행상태 수정 */
    if(elExists(document.querySelector(".btn-edit-task-status"))){
        const btnEditTaskStatus = document.querySelectorAll(".btn-edit-task-status");
        btnEditTaskStatus.forEach(function(chosenOne){
            chosenOne.addEventListener("click", ()=>{
                console.log(`진행상태 변경을 click`);
                console.log(next(chosenOne));
                // 진행상태 목록 toggle
                if(next(chosenOne).classList.contains("img-hidden")) {
                    // 열려 있던 다른 task의 진행상태 목록들 닫고
                    document.querySelectorAll(".tableView-status-list").forEach(function(everyList){
                        everyList.classList.add("img-hidden");
                    });
                    // 선택된 목록만 연다
                    next(chosenOne).classList.remove("img-hidden")
                } else {
                    next(chosenOne).classList.add("img-hidden")
                }

                // 변경하고자 하는 진행상태를 클릭했을 때
                [...next(chosenOne).children].forEach(function(eachStatus){
                    eachStatus.addEventListener("click", ()=>{
                        console.log(eachStatus);
                        console.log(eachStatus.dataset.projectid); // ontrack_task에서는 task id만으로 쿼리 가능, 단 task_history에 저장하기 위해서 필요!!
                        console.log(eachStatus.dataset.taskid);
                        console.log(eachStatus.dataset.status);
                        console.log(eachStatus.dataset.updatedby);
                        console.log(eachStatus.children[1].innerHTML);

                        const taskHistory = {
                            projectId: eachStatus.dataset.projectid,
                            taskId: eachStatus.dataset.taskid,
                            modItem: "status",
                            modType: "update",
                            modContent: eachStatus.children[1].innerHTML,
                            updatedBy: eachStatus.dataset.updatedby
                        };

                        fetch(`http://localhost:8080/task/editTask?item=status&statusNum=${eachStatus.dataset.status}`, {
                            method: 'POST',
                            headers: {
                                'Content-type': 'application/json'
                            },
                            body: JSON.stringify(taskHistory)
                        })
                            .then(response => response.json())
                            .then(data => {
                                // console.log(data);
                                // console.log(data[0])
                                // console.log(prev(chosenOne)); // span
                                // console.log(prev(prev(chosenOne))); // div.status-sign
                                // console.log(prev(prev(chosenOne)).dataset.status);
                                prev(chosenOne).innerText = data[0];
                                prev(prev(chosenOne)).classList.remove(`${prev(prev(chosenOne)).dataset.status}`);
                                prev(prev(chosenOne)).classList.add(data[1]);

                                // 열려있던 진행상태 목록 닫기
                                next(chosenOne).classList.add("img-hidden")

                            });
                    });
                });
            });
        });
    } // 10-9 할 일 진행상태 수정 끝
    
    /* 10-10. 할 일 마감일 수정 */

    /*---------- 053A ------------*/
    // 추가된 담당자의 full name 요소 (table view: ①목록에서 담당자 추가, ②참여하기로 추가)
    function newAssigneeElement(newAssigneeName, newAssigneeMid, executorMid, authorMid, taskId, projectId, view) {
        const div = document.createElement("div");
        const p = document.createElement("p");
        const btnX = document.createElement("span");

        div.classList.add("assignee-fullname-box", "mb-7");
        p.classList.add("div-inline-block", "mb-5");
        btnX.classList.add("btn-cancel-assign-task", "ml-3");

        p.setAttribute("data-mid", newAssigneeMid);

        btnX.setAttribute("data-assigneemid", newAssigneeMid);
        btnX.setAttribute("data-executormid", executorMid);
        btnX.setAttribute("data-authormid", authorMid);
        btnX.setAttribute("data-taskid", taskId);
        btnX.setAttribute("data-projectid", projectId);
        btnX.setAttribute("data-view", view);

        p.innerText = newAssigneeName;
        btnX.innerHTML = "&times;";
        div.appendChild(p);
        // console.log(executorMid);
        // console.log(newAssigneeMid);
        // console.log(authorMid);
        if(executorMid === newAssigneeMid || authorMid === executorMid) {
            // console.log(`why? ${btnDelX}`);
            div.appendChild(btnX);
        }
        return div;
    }

    /*---------- 053B ------------*/
    // (참여하기로)추가된 담당자의 full name 요소 (status view: datum{} 형태로 넘긴다)
    function newAssigneeBySelf(datum) {
        const div = document.createElement("div");
        const p = document.createElement("p");
        const btnX = document.createElement("span");

        div.classList.add("assignee-fullname-box", "mb-7");
        p.classList.add("div-inline-block", "mb-5");
        btnX.classList.add("btn-cancel-assign-task", "ml-3");

        p.setAttribute("data-mid", datum["mid"]);

        btnX.setAttribute("data-assigneemid", datum["mid"]);
        btnX.setAttribute("data-taskid", datum["taskId"]);
        btnX.setAttribute("data-projectid", datum["projectid"]);
        btnX.setAttribute("data-view", datum["view"]);

        p.innerText = datum["nickname"];
        btnX.innerHTML = "참여취소";
        div.appendChild(p);

        return div;
    }


    /*---------- 056 ------------*/
    function unassingedMemberElement(mId, nickname){
        const div = document.createElement("div");
        const p = document.createElement("p");

        div.classList.add("unassigned-member", "mt-10", "mb-10", "cursorP");
        p.classList.add("div-inline-block");

        p.setAttribute("data-mid", mId);
        p.innerText = nickname;
        div.appendChild(p);

        return div;
    }
    /*---------- 057 ------------*/
    /* 담당자 추가할 때 담당자 이름 (요약) 출력 요소 생성 */
    // ★★★ 새로 생성된 btn-more-assignInfo는 -new를 붙인다. (기능이 겹쳐서 기존 버튼도 안열..림?)
    function assigneeBoxByNum(assigneeNum, assigneeMid, assigneeName){
        const span = document.createElement("span");
        const div = document.createElement("div");

        if (assigneeNum === 0) {
            span.classList.add("font-blur", "no-assignee", "btn-more-assignInfo-new", "font-14");
            span.innerText = "담당자 없음";
            return span;
        } else if (assigneeNum === 1) {
            span.classList.add("one-assignee", "btn-more-assignInfo-new", "font-14");
            span.setAttribute("data-mid", assigneeMid);
            span.innerText = assigneeName;
            return span;
        } else if (assigneeNum === 2) {
            div.classList.add("many-assignee");
            span.classList.add("lastName");
            span.setAttribute("data-mid", assigneeMid);
            span.innerText = assigneeName.slice(0, 1);
            div.appendChild(span);
            return div;
        }
    }

    /*---------- 058 ------------*/
    /* 담당자 추가할 때: many-assignee일 경우, img 추가생성 */
    function btnMoreAssigneeInfo(){
       const img = document.createElement("img");
       img.src = "/imgs/down.png";
       img.setAttribute("th:src", "@{/imgs/down.png}");
       img.alt = "more assignment info";
       img.classList.add("btn-more-assignInfo-new", "ml-3");
       return img;
    }
    /*---------- 011 ------------*/
    /* 할 일 상세 모달 열기 */
    // 컨트롤러에 다녀와서 열리는 버전: 화면이 깜빡거리더라도 slide로 열린다면 좀 낫지 않을까.. ㅠㅠ
    if(elExists(document.querySelector(".z3")) && document.querySelector(".z3").classList.contains("open")) {
        // console.log("does it work?");
        document.querySelector("#container-task-detail .modal-Right").classList.add("slide-side");
        // setTimeout(document.querySelector("#container-task-detail .modal-Right").classList.add("slide-side"), 300)
    }

    /*---------- 012 ------------*/
    /* 할 일 상세 모달 탭버튼 클릭*/
    if(elExists(document.querySelector(".btn-modal-task-tab"))){
        let modalTaskTabChosen;
        const btnTaskTabs = document.querySelectorAll(".btn-modal-task-tab");
        const modalTaskTabs = document.querySelectorAll(".modal-task-tab");
        const modalTaskCommonArea = document.querySelector("#task-detail-common");

        btnTaskTabs.forEach(function(chosenTab){
            chosenTab.addEventListener("click", ()=>{
                // 1. 선택된 탭메뉴 표시
                chosenTab.classList.add("task-tab-chosen");
                modalTaskTabChosen = chosenTab.id;
                // console.log(modalTaskTabChosen);
                [...chosenTab.parentElement.children].filter((child) => child.id !== chosenTab.id).forEach(function(others){
                    others.classList.remove("task-tab-chosen");
                });
                // 2. 선택된 탭 표시
                modalTaskTabs.forEach(function(eachtab){
                    eachtab.classList.add("hide");
                    if(eachtab.id === "task-tab-info"){ // 상세 보기(수정)의 id → 수정요망...
                        eachtab.classList.remove("hide");
                        modalTaskCommonArea.classList.add("hide");
                    } else if (eachtab.classList.contains(modalTaskTabChosen)) {
                        eachtab.classList.remove("hide");
                        document.querySelector("#task-detail-common").classList.remove("hide");
                    }
                });
            });
        }); // 할 일 상세 모달 탭버튼 클릭 끝
    }



    // 아래는 컨트롤러 다녀오지 않는 버전.
    if(elExists(document.querySelectorAll(".btn-task-detail"))){
        const btnOpenTaskDetail = document.querySelectorAll(".btn-task-detail");
        const containerTaskDetail = document.querySelector("#container-task-detail");

        btnOpenTaskDetail.forEach(function(chosenTask){
            chosenTask.addEventListener("click", ()=>{

                console.log("다시 시도");
                // document.querySelector("#container-task-detail .modal-Right").classList.add("slide-side");
                document.querySelector("#container-task-detail .modal-Right").style.transform = "translateX(0)";


                // 어떤 탭 열어야 하는지 확인
                const chosenTab = `task`+`-tab-`+ chosenTask.dataset.tab;
                // console.log(chosenTab);

                // 1) 컨테이너 열고
                containerTaskDetail.classList.remove("hide");

                // 2) 전체 탭버튼에서 선택됨 뺐다가
                // btnTaskTabs.forEach(function(btnTabs){
                //     btnTabs.classList.remove("task-tab-chosen");
                // });

                // 3) 선택된 탭버튼에 '선택됨' 넣고
                // [...btnTaskTabs].filter(btn => btn.id === chosenTab)[0].classList.add("task-tab-chosen");

                // 4) 모든 탭을 숨겼다가
                // modalTaskTabs.forEach(function(everyTabs){
                //     everyTabs.classList.add("hide");
                // });

                // 5) 공통영역과 선택된 탭을 출력한다.
                // modalTaskCommonArea.classList.remove("hide");
                // [...modalTaskTabs].filter(tab => tab.classList.contains(chosenTab)).forEach(function(chosenArea){
                //     chosenArea.classList.remove("hide");
                // });

            });
        });

    }




    /*---------- 013 ------------*/
    /* 할 일 나누기(create child task) - 세부(하위)항목 */
    const btnOpenModalCreateChildTask = document.querySelectorAll(".btn-create-child-task");
    const modalCreateTaskParentTitle = document.querySelector(".modal-create-task-parent-title");
    const iconChildTaskTitle = document.querySelector(".icon-child-task");
    btnOpenModalCreateChildTask.forEach(function(chosenBtn){
        chosenBtn.addEventListener("click", ()=>{
            const id = chosenBtn.parentElement.parentElement.id;
            // console.log(id);
            containerCreateTask.classList.remove("hide");
            modalCreateTaskParentTitle.classList.remove("img-hidden");
            iconChildTaskTitle.classList.remove("hide");
        });
    });


    /*---------- 014 ------------*/
    /* 공지읽기 열기 */
    const btnOpenModalNoticeRead = document.querySelectorAll(".btn-open-notice");
    const modalNotice = document.querySelector("#modal-notice-read");
    btnOpenModalNoticeRead.forEach(function(chosenBtn){
        chosenBtn.addEventListener("click", ()=>{
            
            modalNotice.classList.remove("hide");

            // 추후 서버에서 공지 내용 가져오는 코드 추가요망
            console.log(chosenBtn.dataset.id); // notice01
        });
    });

    /*---------- 015 ------------*/
    /* 공지읽기 닫기 */
    const btnCloseModalNoticeRead = document.querySelectorAll(".btn-close-modal-notice-read");
    btnCloseModalNoticeRead.forEach(function(chosenBtn){
        chosenBtn.addEventListener("click", ()=>{
            modalNotice.classList.add("hide");
        });
    });


    /*---------- 016 ------------*/
    /* 공지쓰기 열기 */
    if(elExists(document.querySelector(".btn-write-notice"))){
        const btnOpenWriteNotice = document.querySelector(".btn-write-notice");
        const modalWriteNotice = document.querySelector("#modal-notice-write");
        btnOpenWriteNotice.addEventListener("click", ()=>{
            modalWriteNotice.classList.remove("hide");
        });
    }


    /*---- ▲ 열고닫기 끝 ▲ ----*/
    /*************************/

    /*---------- 017 ------------*/
    /*---- ▼  프로젝트 view tab 시작 ▼ ----*/
/*
    const btnTabView = document.querySelectorAll(".btn-view");
    const projectView = document.querySelectorAll(".project-view");
    btnTabView.forEach(function(chosenBtn){
        chosenBtn.addEventListener("click", ()=>{
            console.log(projectId.value); // input의 name으로 인식한다!!
            const url = `http://localhost:8080/project/${projectId.value}?view=status`
            console.log(url);

            fetch(url, {
                method:'GET'
            });
                // .then(response => response.text())
                // .then(data => console.log(data));
            btnTabView.forEach(function(allBtn){
                allBtn.classList.remove("btn-view-choosen");
            });
            chosenBtn.classList.add("btn-view-choosen");
            // console.log(chosenBtn.dataset.view);
            projectView.forEach(function(eachView){
                eachView.classList.add("hide");
                if(eachView.id === chosenBtn.dataset.view){
                    eachView.classList.remove("hide");
                }
            });
        });
    }); */
    /*---- ▲  view tab 끝 ▲ ----*/

    /*---------- 018 ------------*/
    /*---- ▼ 공지쓰기: 파일첨부 시작 ▼ ----*/
    /*****************
     * - set Attribute로 data-id되는지 확인!
     * - form으로 넘어오는 data 확인!
     * - 현재 공지쓰기의 파일첨부는 1번에 1개씩만 업로드 가능함...
     **/
    if(elExists(document.querySelector("#notice-file"))){
        const btnModalNoticeFileUpload = document.querySelector("#notice-file"); /* input */
        const modalNoticeFileListContainer = document.querySelector("#modal-notice-write-file-box");
        const createNoticeNoFile = document.querySelector("span#create-notice-no-file");
        let noticeFileCnt = 0;
        btnModalNoticeFileUpload.addEventListener("change", ()=>{

            const files = btnModalNoticeFileUpload.files;
            for(let i = 0; i < files.length; i++) {
                const result = validateFile(files[i].name, parseInt(files[i].size));
                if( result === true){
                    noticeFileCnt++;
                    createNoticeNoFile.classList.add("hide");
                    const name = files[i].name.substring(0, files[i].name.lastIndexOf("."));
                    const type = files[i].name.substring(files[i].name.lastIndexOf(".")+1);
                    modalNoticeFileListContainer.append(createAndAttachFileBox(name,type,returnFileSize(parseInt(files[i].size))));
                    rewriteNoticeFileList.push(files[i]);
                } else if (result === 2 || result === 3 || result === 6) {
                    alert(`첨부가 불가능한 유형의 파일입니다.`);
                } else if (result === 1 || result === 4 || result === 5 || result === 7) {
                    alert(`파일 첨부에 실패했습니다. 다음 사항을 확인해주시기 바랍니다. \n 1. 파일명에는 특수기호와 여백이 포함될 수 없습니다.\n(단, +, _, -, .은 포함가능) \n 2. 5MB이하의 파일만 첨부할 수 있습니다.\n ${files[i].name}의 크기: ${returnFileSize(parseInt(files[i].size))} \n`);
                }
            }

        });
    }

    /*---- ▲ 공지쓰기: 파일첨부 끝 ▲ ----*/

    /*---------- 019 ------------*/
    /*---- ▼ 공지쓰기: submit 시작 ▼ ----*/
    if(elExists(document.querySelector("a#notice-write-submit"))){
        const btnSubmitWriteNotice = document.querySelector("a#notice-write-submit");
        const formWriteNotice = document.querySelector("form#form-write-notice");
        let rewriteNoticeFileList = [];
        let noticeFileDelCnt = 0;
        btnSubmitWriteNotice.addEventListener("click", ()=>{
            // 서버에 보낼 값 확인.
            console.log(formWriteNotice.elements.author.value); // ok
            console.log(formWriteNotice.elements.noticeTitle.value); // ok
            console.log(formWriteNotice.elements.noticeContent.value); // ok (콘솔에는 개행 반영됨. db에는 어떻게 저장되는지 확인요망)
            if(noticeFileDelCnt > 0) {
                console.log(rewriteNoticeFileList);
            } else {
                console.log(formWriteNotice.elements.noticeFile.files);
            }

        });
    }

    /*---- ▲ 공지쓰기: submit 끝 ▲ ----*/

    /*---------- 020 ------------*/
     /* 공지쓰기 창 닫기 */
     const btnCloseModalWriteNotice = document.querySelectorAll(".btn-close-modal-write-notice");
     btnCloseModalWriteNotice.forEach(function(chosenBtn){
         chosenBtn.addEventListener("click", ()=>{
            formWriteNotice.elements.noticeTitle.value = "";
            formWriteNotice.elements.noticeContent.value = "";
            formWriteNotice.elements.noticeFile.value = "";
            modalNoticeFileListContainer.innerHTML = "";
            createNoticeNoFile.classList.remove("hide");
            noticeFileCnt = 0;
            noticeFileDelCnt = 0;
            formWriteNotice.reset(); // 없어도 되는 것 같은데... 
            modalWriteNotice.classList.add("hide");
         });
     });

    /*---------- 021 ------------*/
    const addTaskForm = document.querySelector("#form-create-task");
    /*---- ▼ 할 일 추가 모달: 담당자 배정 ▼ ----*/

    const assigneeBeforeChoose  = document.querySelector("#assignee-before-choose");
    const assignIndication = document.querySelector("#assign-indication");
    const btnShowAssigneeList = document.querySelector("#show-assignee-list");

    const assigneeList = document.querySelector("#assignee-list-box"); // div
    if(elExists(document.querySelectorAll("#assignee-list-box input"))) {
        // console.log(`thymeleaf로 생성한 input 있음`);
        // console.log(document.querySelectorAll("#assignee-list-box input")[0].value);
    }
    const assigneesName = document.querySelectorAll("#assignee-list-box > input");

    const chosensBoxes = document.querySelector("#chosens-boxes");

    const chosenAssigneeList = new Set(); // 중복 선택 방지를 위한 Set

    // 프로젝트 멤버 목록 나타내기
     btnShowAssigneeList.addEventListener("click", ()=>{

        // 담당자가 아직 배정되지 않았다면 '담당자 배정하기' 글씨는 출력,
        // 선택된 담당자 이름이 출력될 div는 미출력 (목록 나타내기 클릭할 때마다 체크)
        if (chosenAssigneeList.size === 0) {
            chosensBoxes.classList.remove("assignee-display");
            chosensBoxes.classList.add("hide");
            assigneeBeforeChoose.classList.add("assignee-display");
            assigneeBeforeChoose.classList.remove("hide");
        }
        // 1. 목록 보이기 버튼을 목록 숨기기(올리기) 버튼으로
        btnShowAssigneeList.classList.toggle("img-angle180");
        // 1. 담당자 목록 출력
        assigneeList.classList.toggle("hide");
        // 1. '담당자 배정하기' 글씨 흐리기
        assignIndication.classList.toggle("font-blur");

    }); // 프로젝트 멤버 목록 나타내기 끝 (btnShowAssigneeList.addEventListener(click) ends)

    // 배정된 담당자 member Id array
    let chosenAssigneesMid = [];
    // 배정된 담당자 nick name array
    let choosenAssigneesName = [];

    // 프로젝트 멤버 목록에서 담당자 선택
    assigneesName.forEach(function(chosenName){
        chosenName.addEventListener("click", ()=>{
            // 해당 input에 선택됨 표시(class="chosen")
            // console.log(this); // html...
            // console.log(chosenName); // <input id="assignee1" class="hide" type="checkbox" name="taskAssignee" value="박종건">
            chosenName.classList.add("chosen");

            // 2. '담당자 배정하기' 글씨 hide
            assigneeBeforeChoose.classList.remove("assignee-display");
            assigneeBeforeChoose.classList.add("hide");

            // 3. 선택된 담당자 이름들이 담길 div의 hide 해제
            chosensBoxes.classList.add("assignee-display");
            chosensBoxes.classList.remove("hide");

            // 하나의 일에 최대 배정되는 담당자 제한(6명), 이미 배정된 담당자는 또 클릭해도 무동작
            if (chosenAssigneeList.size < 6 && !chosenAssigneeList.has(chosenName.dataset.nickname)) {

                // 클릭될 때 마다 div 요소 생성
                chosensBoxes.appendChild(chosenAssigneeBox(chosenName.dataset.nickname));
                console.log(`선택된 담당자: ${chosenName.dataset.nickname}`);

                chosenAssigneeList.add(chosenName.dataset.nickname); // 중복 방지를 위한 set

                console.log("담당자 선택 후 set: ");
                console.log(chosenAssigneeList);
                console.log("담당자 선택 후 담당자 수: ");
                console.log(chosenAssigneeList.size);

            }
        }); // chosenName.addEventListener(click) ends
    });  // 프로젝트 멤버 목록에서 담당자 선택 끝 (assigneesName.map ends)


    /*---------- 022 ------------*/
    // 할 일 추가 모달: 배정했던 담당자 해제(삭제)
    onEvtListener(document, "click", ".btn-assignee-del", function(){

        const deletedAssignee = [...this.parentElement.children].filter((child) => child !== this)[0].innerText;
        // 해당 input에 선택해제
        assigneesName.forEach(function(eachOne){
            if (eachOne.dataset.nickname === deletedAssignee) {
                eachOne.classList.remove("chosen");
                console.log(`배정됐다 해제된 담당자: ${eachOne.value}`);
            }
        });
        // 중복 확인을 위한 set에서 담당자 이름 삭제
        chosenAssigneeList.delete([...this.parentElement.children].filter((child) => child !== this)[0].innerText);
        console.log(`배정했던 담당자 1명 삭제 후 set: `);
        console.log(chosenAssigneeList); // set 확인

        // 배정 해제된 담당자 box 삭제
        this.parentElement.remove();

        // 담당자가 0명이라면 '담당자 배정하기'가 출력되도록 한다.
        if (chosenAssigneeList.size === 0) {
            chosensBoxes.classList.remove("assignee-display");
            chosensBoxes.classList.add("hide");
            assigneeBeforeChoose.classList.add("assignee-display");
            assigneeBeforeChoose.classList.remove("hide");
        }
    });

    /*---------- 023 ------------*/
    // 할 일 추가 모달: 담당자 배정 - 선택된 담당자 div 동적 생성.
    function chosenAssigneeBox(value) {
        let div = document.createElement("div");
        let span1 = document.createElement("span");
        let span2 = document.createElement("span");

        div.className = "assignee-chosen-box";
        span1.className = "assignee-chosen-name";
        span2.className = "cursorP";
        span2.classList.add("ml-5");
        span2.classList.add("btn-assignee-del");
        span2.innerHTML = "&times;";
        span1.innerText = value;
        
        div.appendChild(span1);
        div.appendChild(span2);

        return div;
    }
    /*---- ▲ Modal(Create Task;할일추가) 담당자 배정 끝 ▲ ----*/


    /*---------- 024 ------------*/
    /*---- ▼ 할 일 추가 모달: 파일첨부 ▼ ----*/
    /*******************************
     * 파일 업로드 규칙
     * 용량 - (무료버전) 파일당 5MB 이하 (단, 개수는 무관.)
     * multiple -  가능
     * 타입 - js, cmd, com 등을 포함한 61개 타입 첨부 불가
     * 파일명1(original) - 특수문자, 공백 포함금지(단, +, -, _, .은 가능)
     * 파일명2(new) - uuid
     * 저장위치 - url 접근 불가능하도록 (how?)
     * 중복업로드 허용... (네이버 메일은 파일명이 같다고 내치지 않는다... )
     *******************************/
    
    const btnCreateTaskAttachFile = document.querySelector("#create-task-attach-file"); /*input*/
    const createTaskFiles = document.querySelector("#create-task-files");
    const createTasknofile = document.querySelector("#no-file-placeholder");
    let cntFiles = 0;
    let rewriteCreateTaskFileList = [];

    btnCreateTaskAttachFile.addEventListener("change", ()=>{
        const files = btnCreateTaskAttachFile.files;
        for (let i = 0; i < files.length; i++) {
            const name = files[i].name.substring(0, files[i].name.lastIndexOf("."));
            const type = files[i].name.substring(files[i].name.lastIndexOf(".")+1);
            const result = validateFile(files[i].name, parseInt(files[i].size));
            if (result === true) {
                createTasknofile.classList.remove("hide");
                createTaskFiles.appendChild(createAndAttachFileBox(name, type, returnFileSize(parseInt(files[i].size))));
                cntFiles++;
                rewriteCreateTaskFileList.push(files[i]);
            } else if (result === 2 || result === 3 || result === 6) {
                alert(`첨부가 불가능한 유형의 파일입니다.`);
            } else if (result === 1 || result === 4 || result === 5 || result === 7) {
                alert(`파일 첨부에 실패했습니다. 다음 사항을 확인해주시기 바랍니다. \n 1. 파일명에는 특수기호와 여백이 포함될 수 없습니다.\n(단, +, _, -, .은 포함가능) \n 2. 5MB이하의 파일만 첨부할 수 있습니다.\n ${files[i].name}의 크기: ${returnFileSize(parseInt(files[i].size))} \n`);
            }
        }

        if (cntFiles !== 0) {
            createTasknofile.classList.add("hide");
        }
    });

    /*---------- 025 ------------*/
    // create task or notice
    // 모달(공지, 할 일): 첨부 파일 정보담을 div 동적 생성
    function createAndAttachFileBox(name, type, size) {
        const div1 = document.createElement("div");
        const div2 = document.createElement("div");
        const fileName = document.createElement("span");
        const fileType = document.createElement("span");
        const fileSize = document.createElement("span");
        const btnDel = document.createElement("span");

        div1.className = "flex-row-between-nowrap";
        div1.classList.add("modal-create-and-attach-file");
        div2.className = "flex-row-justify-start-align-center";
        fileName.className = "modal-file-name";
        fileType.classList.add("font-14");
        fileType.classList.add("modal-file-type");
        fileType.classList.add("mr-10");
        fileSize.className = "modal-file-size";
        btnDel.className = "del-file";
        btnDel.classList.add("cursorP");
        
        btnDel.innerHTML = "&times;";
        fileName.innerText = name;
        fileType.innerText = `.${type}`;
        fileSize.innerText = size; /*`${size}`*/
        
        div2.appendChild(fileName);
        div2.appendChild(fileType);
        div2.appendChild(fileSize);

        div1.appendChild(div2);
        div1.appendChild(btnDel);

        return div1;
    } // function attahcedFileBox() 끝


    /*---------- 026 ------------*/
    // 모달(공지, 할 일): 첨부했던 파일 삭제
    // createAndAttachFileBox 안의 btnDel을 클릭했을 때
    onEvtListener(document, "click", ".del-file", function(){

        // notice인지 task인지 식별
        const tellNociteOrTask = this.parentElement.parentElement.id
        
        // notice인지 task인지 구별
        if (tellNociteOrTask === "create-task-files") {
            cntFiles--;
            createTaskFileDelCnt++;
            const idx = [...this.parentNode.parentNode.children].indexOf(this.parentNode);
            rewriteCreateTaskFileList.splice(idx-1, 1);
        } else if (tellNociteOrTask === "modal-notice-write-file-box") {
            noticeFileCnt--;
            noticeFileDelCnt++;           
            const idx2 = [...this.parentNode.parentNode.children].indexOf(this.parentNode);
            rewriteNoticeFileList.splice(idx2, 1);
        } else {
            console.log("you need to check something...");
        }
        
        // 삭제된 파일box 삭제
        this.parentElement.remove();

        // 첨부된 파일이 0개라면 '첨부된 파일이 없습니다' 안내 출력
        if(cntFiles === 0){
            createTasknofile.classList.remove("hide");
        }
        
        if(noticeFileCnt === 0){
            createNoticeNoFile.classList.remove("hide");
        }
    });

    /*---------- 027 ------------*/
    // 파일 사이즈 보기 좋게(KB, MB로 각각 적용)
    function returnFileSize(filesize){
        if (filesize < 1024) {
            return filesize + "byte";
        } else if (filesize >= 1024 && filesize < 1048576) {
            return (filesize/1024).toFixed(1) + "KB";
        } else if (filesize >= 1048576) {
            return (filesize/1048576).toFixed(1) + "MB";
        }
    }

    /*---------- 028 ------------*/
    // 파일 유효성검사(파일명 공백, 특수문자 금지, 사이즈 5MB 이하, 첨부불가 유형 여부)
    function validateFile(fullfilename, filesize){
        // 파일명 안에 특수기호는 +, _, -, .만 허용
        const regFileName = /[{}\[\]()\/?,'";:`~<>!@#$%^&*=|\s]/g;
        const regFileType = /(ade|adp|apk|appx|appxbundle|aspx|bat|bin|cab|cmd|com|cpl|dll|dmg|exe|gadget|hta|inf1|ins|ipa|iso|isp|isu|jar|js|jse|jsp|jsx|lib|lnk|mde|msc|msi|msix|msixbundle|msp|mst|nsh|paf|pif|ps1|reg|rgs|scr|sct|sh|shb|shs|sys|tar|u3p|vb|vbe|vbs|vbscript|vxd|ws|wsc|wsf|wsh)$/i;
        const filename = fullfilename.substring(0, fullfilename.lastIndexOf("."));
        const filetype = fullfilename.substring(fullfilename.lastIndexOf(".")+1);
        
        // 정규표현식에 있는 특수기호/공백/문자와 일치하는 글자가 파일명에 있을 경우 true 반환됨
        const nameResult = regFileName.test(filename)? 1: 0;
        const typeResult = regFileType.test(filetype)? 2: 0;
        const sizeResult = filesize > 5242880 ? 4: 0;


        
        return (nameResult + typeResult + sizeResult === 0) ? true: (nameResult + typeResult + sizeResult);
    }

    /*---- ▲ Modal(Create Task;할일추가) 파일첨부 끝 ▲ ----*/

    /*---------- 029 ------------*/
    /*---- ▼ Modal(Create Task;할 일 추가): submit 시작 ▼ ----*/
    const modalCreateTask = document.querySelector("#container-create-task");
    const btnCreateTaskSubmit = document.querySelector("#create-task-submit"); // button
    let createTaskFileDelCnt = 0;


    btnCreateTaskSubmit.addEventListener("click", (e)=>{
        e.preventDefault();
        const addTaskData = new FormData();
        //
        // // /* form submit 하고 전송되는 값 확인 */
        // // console.log(addTaskForm.elements.projectId.value); // 9
        // // console.log(addTaskForm.elements.taskAuthorMid.value); // 14
        // // console.log(addTaskForm.elements.taskTitle.value); // 할 일 추가중
        // // console.log(addTaskForm.elements.taskPriority.value); // 1
        // // console.log(addTaskForm.elements.taskDueDate.value); // 2024-06-11
        // // console.log(addTaskForm.elements.assigneesMid); // RadioNodeList { 0: input#4.hide.chosen, 1: input#14.hide.chosen, 2: input#26.hide.chosen, 3: input#27.hide, 4: input#28.hide, value: "", length: 5 }
        //
        addTaskForm.elements.assigneesMid.forEach(function(eachOne){
            if(eachOne.classList.contains("chosen")) {
                console.log(`배정된 담당자 id: ${eachOne.id}, 배정된 담당자 nickname: ${eachOne.dataset.nickname}`);
                chosenAssigneesMid.push(eachOne.id);
                choosenAssigneesName.push(eachOne.dataset.nickname);
            }
        });
        // 부분 삭제가 이뤄졌을 때만 새로운 array가 가고 아닐 경우, 기존의 fileList가 전송된다.
        if (createTaskFileDelCnt > 0) {
            console.log(`파일 부분 삭제 후: `);
            console.log(rewriteCreateTaskFileList);
            for(let i = 0; i < rewriteCreateTaskFileList.length; i++) {
                addTaskData.append("taskFile", rewriteCreateTaskFileList[i]);
            }

        } else {
            console.log(addTaskForm.elements.taskFile.files);
            const files = addTaskForm.elements.taskFile.files;
            for(let i = 0; i < files.length; i++){
                addTaskData.append("taskFile", files[i]);
            }
        // //     // FileList [ File ] 줄바꿈
        // //     //   0: File { name: "100자.txt", lastModified: 1712675372978, size: 250, … }
        // //     //      lastModified: 1712675372978
        // //     //      name: "100자.txt"
        // //     //      size: 250
        // //     //      type: "text/plain"
        // //     //      webkitRelativePath: ""
        // //     //   length: 1
        }
        // //
        addTaskData.append("projectId", addTaskForm.elements.projectId.value);
        addTaskData.append("taskAuthorMid", addTaskForm.elements.taskAuthorMid.value);
        addTaskData.append("authorName", addTaskForm.elements.authorName.value);
        addTaskData.append("taskTitle", addTaskForm.elements.taskTitle.value);
        addTaskData.append("taskPriority", addTaskForm.elements.taskPriority.value);
        addTaskData.append("taskDueDate", addTaskForm.elements.taskDueDate.value);
        addTaskData.append("assigneesMid", chosenAssigneesMid);
        addTaskData.append("assigneeNames", choosenAssigneesName);
        //
        console.log(`------- addTaskData -------`);
        console.log(addTaskData);
        fetch('http://localhost:8080/task/addTask', {
            method:'POST',
            headers: {},
            body: addTaskData
        }).then(response => {
            afterAddTaskSubmit();
            const chkTime = new Date();
            console.log(`fetch 후 어떻게 되는가: ${chkTime.getHours()}:${chkTime.getMinutes()}:${chkTime.getSeconds()}:${chkTime.getMilliseconds()}`);
            if (response.ok) {
                location.reload();
            }
        });




    });
    /*---- ▲ Modal(Create task;할 일 추가): submit 끝 ▲ ----*/


    /*---------- 030 ------------*/
    // 할 일 추가 모달 닫는 기능
    function afterAddTaskSubmit(){
        addTaskForm.reset(); // 파일첨부: 콘솔에는 length 0으로 찍힘. 담당자: 콘솔과 화면 모두 reset 필요

        // 선택했던 담당자 input 모두 해제
        addTaskForm.elements.assigneesMid.forEach(function(eachOne){
            eachOne.classList.remove("chosen");
        });

        // 담당자 담은 array clear (data to controller)
        chosenAssigneesMid = [];
        choosenAssigneesName = [];

        // 담당자 담은 set clear (화면)
        chosenAssigneeList.clear();
        console.log(`모달 닫은 후에 set: `);
        console.log(chosenAssigneeList);


        // 선택된 담당자 이름이 출력된 div 삭제
        if(elExists(document.querySelectorAll(".assignee-chosen-box"))){
            const assigneeNamediv = document.querySelectorAll(".assignee-chosen-box");
            for(const el of assigneeNamediv) el.remove();
        }

        // 선택된 담당자들이 담기는 box hide
        chosensBoxes.classList.remove("assignee-display");
        chosensBoxes.classList.add("hide");


        //담당자 배정하기 문구 출력
        assigneeBeforeChoose.classList.add("assignee-display");
        assigneeBeforeChoose.classList.remove("hide");

        // 첨부된 파일 화면(div)에서 삭제
        for(const div of createTaskFiles.querySelectorAll("div")) {
            div.remove();
        }
        createTasknofile.classList.remove("hide");

        // 첨부된 파일 fileList array 비우기
        // addTaskForm.elements.taskFile.files = [];
        createTaskFileDelCnt = 0;
        rewriteCreateTaskFileList = [];

        // 모달 닫기
        modalCreateTask.classList.add("hide");
    }

    /*---------- 031 ------------*/
    /* 할 일 추가 모달 닫기 버튼 */
    /*--- 닫기 전에 묻는 창 추가 요망? ---*/
    const btnCloseModalCreateTask = document.querySelector(".btn-close-modal-createTask");
    
    btnCloseModalCreateTask.addEventListener("click", ()=>{
        afterAddTaskSubmit();
    });


    /*---------- 032 ------------*/
    /*---- ▼ 할일 수정(table view, status view): 담당자 배정·수정 시작 ▼ ----*/
    //
    // const edit_assigneeBeforeChoose  = document.querySelector("#edit-assignee-before-choose");
    // const edit_assignIndication = document.querySelector("#edit-assign-indication");
    // const edit_btnShowAssigneeList = document.querySelector("#edit-show-assignee-list");
    //
    // const edit_assigneeList = document.querySelector("#edit-assignee-list");
    // const edit_assigneesName = document.querySelectorAll("#edit-assignee-list > input");
    //
    // const edit_chosensBoxes = document.querySelector("#edit-chosens-boxes");
    //
    // let edit_cntChoosenAssignee = 0; // 하나의 일에 배정되는 담당자의 수를 세기 위한 변수
    //
    // // 프로젝트 멤버 목록 나타내기
    //  edit_btnShowAssigneeList.addEventListener("click", ()=>{
    //
    //     // 담당자가 아직 배정되지 않았다면 '담당자 배정하기' 글씨는 출력,
    //     // 선택된 담당자 이름이 출력될 div는 미출력 (목록 나타내기 클릭할 때마다 체크)
    //     if (edit_cntChoosenAssignee === 0) {
    //         edit_chosensBoxes.classList.remove("assignee-display");
    //         edit_chosensBoxes.classList.add("hide");
    //         edit_assigneeBeforeChoose.classList.add("assignee-display");
    //         edit_assigneeBeforeChoose.classList.remove("hide");
    //     }
    //     // 1. 목록 보이기 버튼을 목록 숨기기(올리기) 버튼으로
    //     edit_btnShowAssigneeList.classList.toggle("img-angle180");
    //     // 1. 담당자 목록 출력
    //     edit_assigneeList.classList.toggle("hide");
    //     // 1. '담당자 배정하기' 글씨 흐리기
    //     edit_assignIndication.classList.toggle("font-blur");
    //
    // }); // 프로젝트 멤버 목록 나타내기 끝 (btnShowAssigneeList.addEventListener(click) ends)
    //

    /*---------- 033 ------------*/
    //
    // const edit_chosenAssigneeList = new Set(); // 중복 선택 방지를 위한 Set
    //
    // // 프로젝트 멤버 목록에서 담당자 선택
    // edit_assigneesName.forEach(function(edit_chosenName){
    //     edit_chosenName.addEventListener("click", ()=>{
    //
    //         console.log("clicked edit modal list");
    //         // 2. '담당자 배정하기' 글씨 hide
    //         edit_assigneeBeforeChoose.classList.remove("assignee-display");
    //         edit_assigneeBeforeChoose.classList.add("hide");
    //
    //         // 3. 선택된 담당자 이름들이 담길 div의 hide 해제
    //         edit_chosensBoxes.classList.add("assignee-display");
    //         edit_chosensBoxes.classList.remove("hide");
    //
    //         // 하나의 일에 최대 배정되는 담당자 제한(6명), 이미 배정된 담당자는 또 클릭해도 무동작
    //         if (edit_cntChoosenAssignee < 6 && !edit_chosenAssigneeList.has(edit_chosenName.value)) {
    //
    //             // 클릭될 때 마다 div 요소 생성
    //             edit_chosensBoxes.appendChild(edit_chosenAssigneeBox(edit_chosenName.value));
    //
    //             edit_chosenAssigneeList.add(edit_chosenName.value); // 중복 방지를 위한 set
    //
    //             edit_cntChoosenAssignee++;
    //         }
    //     }); // chosenName.addEventListener(click) ends
    // });  // 프로젝트 멤버 목록에서 담당자 선택 끝 (assigneesName.map ends)

    /*---------- 034 ------------*/
    // 할 일 상세에서 담당자 삭제할 때와 할 일 추가(생성)할 때의 담당자 삭제 구분 요망!!!
    // 동적으로 생성된, (배정된)담당자를 삭제(배정해제) 할 때!
    onEvtListener(document, "click", ".btn-assignee-del", function(){
        // 중복 확인을 위한 set에서 담당자 이름 삭제
        edit_chosenAssigneeList.delete([...this.parentElement.children].filter((child) => child !== this)[0].innerText);
        console.log(`할일 상세의 담당자 목록(추후 할일생성과 구분요망!):`); // set 확인
        console.log(edit_chosenAssigneeList);

        // 배정 해제된 담당자 box 삭제
        this.parentElement.remove();

        // 배정된 담당자 수 변경
        edit_cntChoosenAssignee--;

        // 담당자가 0명이라면 '담당자 배정하기'가 출력되도록 한다.
        if (edit_cntChoosenAssignee === 0) {
            edit_chosensBoxes.classList.remove("assignee-display");
            edit_chosensBoxes.classList.add("hide");
            edit_assigneeBeforeChoose.classList.add("assignee-display");
            edit_assigneeBeforeChoose.classList.remove("hide");
        }
    });

    /*---------- 035 ------------*/
    // 선택된 담당자 div 동적 생성. 
    function edit_chosenAssigneeBox(value) {
        let div = document.createElement("div");
        let span1 = document.createElement("span");
        let span2 = document.createElement("span");

        div.className = "assignee-chosen-box";
        span1.className = "assignee-chosen-name";
        span2.className = "cursorP";
        span2.classList.add("ml-5");
        span2.classList.add("btn-assignee-del");
        span2.innerHTML = "&times;";
        span1.innerText = value;
        
        div.appendChild(span1);
        div.appendChild(span2);

        return div;
    }
    /*---- ▲ Modal(Edit Task; 할일 수정(상세): 담당자 배정·수정 끝 ▲ ----*/


    /*---------- 036 ------------*/
    /*---- ▼ Modal(Edit Task; 할일 수정(상세): 세부항목(하위할일;Child Task) 리스트 보기 시작 ▼ ----*/
    // const btnShowChildTaskList = document.querySelector("#btn-show-childTasks");
    // const childTaskList = document.querySelector("#childTask-list");
    // btnShowChildTaskList.addEventListener("click", ()=>{
    //     btnShowChildTaskList.classList.toggle("img-angle180");
    //     childTaskList.classList.toggle("hide");
    // });
    /*---- ▲ Modal(Edit Task; 할일 수정(상세): 세부항목(하위할일;Child Task) 리스트 보기 끝 ▲ ----*/



    /*---------- 037 ------------*/
    /*---- ▼ Modal(Task comment)소통하기 - 글 등록 ▼ ----*/
    if(elExists(document.querySelector("#btn-submit-comment"))) {

        // 모두 확인 요청 시 요소 변화 + task-comment-type: Required Reading
        // 모두 확인 요청 (radio)버튼 클릭 시 css 변경
        const commentNoticeBtn = document.querySelector("#task-comment-notice");
        const commentWriteBox = document.querySelector(".modal-task-comment-write");
        const btnSubmitComment = document.querySelector("#btn-submit-comment");
        let commentType = "normal";
        commentNoticeBtn.addEventListener("change", ()=>{
            if(commentNoticeBtn.checked){
                commentWriteBox.classList.add("task-comment-write-notice");
                btnSubmitComment.style.backgroundColor = "#9d3f3b";
                commentType = "notice";
            } else {
                commentType = "normal";
                commentWriteBox.classList.remove("task-comment-write-notice");
                btnSubmitComment.style.backgroundColor = "#411c02";
            }
        });

        /*---------- 038 ------------*/
        // 글 등록
        const newCommentContent = document.querySelector("textarea#task-comment-write-content");

        btnSubmitComment.addEventListener("click", ()=>{
            console.log(newCommentContent);
            const content = newCommentContent.value;
            const datum = btnSubmitComment.dataset;
            const date = new Date();

            // 서버에 보낼 정보 입력
            // ★★ comment_check에도 작성자는 확인함으로 입력해야 한다.
            // ★★ 작성한 글을 바로 출력할 것이기 때문에 작성한 시간을 javascript에서 보내줘야 한다.
            const taskComment = {
                taskId: datum["taskid"],
                projectId: datum["projectid"],
                authorMid: datum["authormid"],
                authorName: datum["authorname"],
                type: commentType,
                comment: content,
                createdAt: date,
                updatedAt: date
            };

            console.log(taskComment);
            newCommentContent.value = "";
            // Object { taskId: "8", projectId: "9", authorMid: "14", authorName: "공지철",
            // type: "normal", comment: "소통하기 작성",
            // createdAt: Date Fri Jul 19 2024 00:19:33 GMT+0900 (대한민국 표준시),
            // updatedAt: Date Fri Jul 19 2024 00:19:33 GMT+0900 (대한민국 표준시) }


            // 서버에 보내기 (작성자, 글내용, 타입(RR일 경우, 확인cnt=1(작성자id, check True)), 작성일시)

            // 화면에 출력
            const commentArea = document.querySelector("#task-tab-comment-list");
            commentArea.prepend(createCommentBox(datum["authorname"], commentType, content, datum["assigneecnt"], date));
        });
    }


    /*---------- 059 날짜형태반환(YY.MM.DD (요일) hh:mm ------------*/
    function dateYYMMDD(date){
        const YY = date.getFullYear().toString().substring(2);
        let MM = date.getMonth()+1;
        if(MM < 10) { MM = `0${MM}`}
        let DD = date.getDate();
        if(DD < 10) { DD = `0${DD}`; }

        return `${YY}.${MM}.${DD} `;
    }

    function dateDay(date){
        const D = date.getDay();
        switch(D){
            case 0: return `(일) `;
            case 1: return `(월) `;
            case 2: return `(화) `;
            case 3: return `(수) `;
            case 4: return `(목) `;
            case 5: return `(금) `;
            case 6: return `(토) `;
        }
    }

    function dateDayEng(date){
        const D = date.getDay();
        switch(D){
            case 0: return `SUN `;
            case 1: return `MON `;
            case 2: return `TUE `;
            case 3: return `WED `;
            case 4: return `THU `;
            case 5: return `FRI `;
            case 6: return `SAT `;
        }
    }

    function timeHHMM(date){
        let hh = date.getHours();
        if(hh < 10) { hh = `0${hh}`; }
        let mm = date.getMinutes();
        if(mm < 10) { mm = `0${mm}`; }
        return `${hh}:${mm}`;
    }

    /*---------- 039 ------------*/
    // 작성한 소통하기 글 바로 출력할 때 사용할 function (쓰게 될까? 쓰게 되겠지...)
    function createCommentBox(writer, type, content, assigneeCnt, date) {
        // 0. Container
        const commentBox = document.createElement("div");
        commentBox.classList.add("modal-task-comment-read");
        if(type === "notice"){
            commentBox.classList.add("task-comment-notice");
        }
        
        // 1. 작성자+모두확인요청cnt + 작성일시+tool DIV (2개 요소)
        const commentInfoBox = document.createElement("div");
        commentInfoBox.classList.add("modal-task-comment-info-box");

        // 1-1. 작성자+ RR확인cnt (2개 요소 appendChild)
        const commentWriterNNoticeBox = document.createElement("div");
        commentWriterNNoticeBox.classList.add("flex-row-justify-start-align-center");

        // 1-1-1. 작성자
        const commentWriter = document.createElement("span");
        commentWriter.classList.add("commentWriter");
        commentWriter.innerText = writer;

        // 1-1-2. 모두확인요청 글의 확인/미확인 수 (5개 요소 appendChild)
        const commentNoticeChkCntBox = document.createElement("div");
        commentNoticeChkCntBox.classList.add("flex-row-justify-start-align-center");

        const commentNoticeIcon = document.createElement("img");
        commentNoticeIcon.classList.add("img-2520", "mr-5");
        commentNoticeIcon.src = "../../imgs/icon_chatRR.png";
        commentNoticeIcon.setAttribute("th:src", `@{/imgs/commentNoticeIcon}`);

        // 1-1-2-1. 확인span, cnt span
        const commentNoticeChk = document.createElement("span");
        commentNoticeChk.classList.add("font-12");
        commentNoticeChk.innerText = "확인"

        const commentNoticeChkCnt = document.createElement("span");
        commentNoticeChkCnt.classList.add("comment-notice-chk-cnt");
        commentNoticeChkCnt.innerText = "1"; // 작성자는 이미 확인한 것으로 출력한다.

        // 1-1-2-2. 미확인 span, cnt span
        const commentNoticeUnchk = document.createElement("span");
        commentNoticeUnchk.classList.add("font-12");
        commentNoticeUnchk.innerText = "미확인";

        const commentNoticeUnhkCnt = document.createElement("span");
        commentNoticeUnhkCnt.classList.add("altivo-light");
        commentNoticeUnhkCnt.innerText = assigneeCnt - 1;

        // 1-1-2에 appendChild (5개)
        commentNoticeChkCntBox.appendChild(commentNoticeIcon);
        commentNoticeChkCntBox.appendChild(commentNoticeChk);
        commentNoticeChkCntBox.appendChild(commentNoticeChkCnt);
        commentNoticeChkCntBox.appendChild(commentNoticeUnchk);
        commentNoticeChkCntBox.appendChild(commentNoticeUnhkCnt);

        // 1-1에 appendChild (2개)
        commentWriterNNoticeBox.appendChild(commentWriter);
        commentWriterNNoticeBox.appendChild(commentNoticeChkCntBox);


        // 1-2. 작성일시+tool (2개요소 appendChild)
        const commentDateNToolBox = document.createElement("div");
        commentDateNToolBox.classList.add("mr-15", "flex-row-justify-start-align-center");

        // 1-2-1. 작성일시
        const commentDate = document.createElement("span");
        commentDate.classList.add("altivo-regular", "mr-5");
        commentDate.innerText = dateYYMMDD(date);
        commentDate.innerText += dateDayEng(date);
        commentDate.innerText += timeHHMM(date);

        // 1-2-2. tool(수정/삭제) 버튼과 그에 따른 모달 2개 (3개요소 appendChild)
        const commentToolBox = document.createElement("div");
        commentToolBox.classList.add("btn-comment-edit-del");

        // 1-2-2-1. tool 버튼 (이미지; 3dot)
        const commentToolBtn = document.createElement("img");
        commentToolBtn.src = "../../imgs/icon_3dot.png";
        commentToolBtn.setAttribute("th:src", `@{/imgs/icon_3dot.png}`);

        // 1-2-2-2. 수정/삭제 버튼box (2개요소 appendChild)
        const commentEditDelBox = document.createElement("div");
        commentEditDelBox.classList.add("click-comment-edit-del", "img-hidden");

        // 1-2-2-2-1. 수정버튼 (2개요소 appendChild. p > img, span)
        // (∵ button으로 하면 visibility hidden이 적용되지 않음)
        const commentEditBtnP = document.createElement("p");
        commentEditBtnP.classList.add("btn-comment-edit");

        const commentEditIcon = document.createElement("img");
        commentEditIcon.src = "../../imgs/icon_write.png";
        commentEditIcon.setAttribute("th:src", `@{/imgs/icon_write.png}`);
        commentEditIcon.classList.add("img-1718", "mr-3");

        const commentEditSpan = document.createElement("span");
        commentEditSpan.innerText = "수정하기";

        // 수정버튼(p)에 icon과 span 부착
        commentEditBtnP.appendChild(commentEditIcon);
        commentEditBtnP.appendChild(commentEditSpan);

        // 1-2-2-2-2. 삭제버튼 (2개요소 appendChild)
        const commentDelBtnP = document.createElement("p");
        commentDelBtnP.classList.add("btn-comment-del");

        const commentDelIcon = document.createElement("img");
        commentDelIcon.src = "../../imgs/icon_bin98.png";
        commentDelIcon.setAttribute("th:src", `@{/imgs/icon_bin98.png}`);
        commentDelIcon.classList.add("img-1718", "mr-3");

        const commentDelSpan = document.createElement("span");
        commentDelSpan.innerText = "삭제하기";

        // 삭제버튼(p)에 icon과 span 부착
        commentDelBtnP.appendChild(commentDelIcon);
        commentDelBtnP.appendChild(commentDelSpan);

        // 수정/삭제버튼 box(1-2-2-2)에 각 버튼 부착
        commentEditDelBox.appendChild(commentEditBtnP);
        commentEditDelBox.appendChild(commentDelBtnP);


        // 1-2-2-3. 삭제confirm창 (2개요소 appendChild)
        const commentDelConfirmBox = document.createElement("div");
        commentDelConfirmBox.classList.add("popupYN", "img-hidden");

        // 1-2-2-3-1.
        const commentDelConfirm = document.createElement("span");
        commentDelConfirm.innerText = "정말로 삭제하시겠습니까?";

        // 1-2-2-3-2. 확인/취소버튼 box (2개 요소 appendChild)
        const commentDelConfirmBtnBox = document.createElement("div");
        commentDelConfirmBtnBox.classList.add("flex-row-center-center-nowrap", "mt-20");

        const commentDelConfirmY = document.createElement("p");
        commentDelConfirmY.classList.add("btn-confirm-yes");
        commentDelConfirmY.innerText = "확인";

        const commentDelConfirmN = document.createElement("p");
        commentDelConfirmN.classList.add("btn-confirm-no");
        commentDelConfirmN.innerText = "취소";

        // 1-2-2-3-2에 부착 (2개 요소)
        commentDelConfirmBtnBox.appendChild(commentDelConfirmY);
        commentDelConfirmBtnBox.appendChild(commentDelConfirmN);

        // 1-2-2-3에 요소 부착(2개 요소)
        commentDelConfirmBox.appendChild(commentDelConfirm);
        commentDelConfirmBox.appendChild(commentDelConfirmBtnBox);

        // 1-2-2에 요소 부착 (btn(img)+모달,모달)
        commentToolBox.appendChild(commentToolBtn);
        commentToolBox.appendChild(commentEditDelBox);
        commentToolBox.appendChild(commentDelConfirmBox);


        // 1-2에 작성일시+tool 부착(2개요소)
        commentDateNToolBox.appendChild(commentDate);
        commentDateNToolBox.appendChild(commentToolBox);

        // 1. commentInfoBox(①작성자+모두확인요청cnt + ②작성일시+tool)에 2개 요소 부착
        commentInfoBox.appendChild(commentWriterNNoticeBox);
        commentInfoBox.appendChild(commentDateNToolBox);


        // 2. 글 내용
        const commentContentBox = document.createElement("div");
        commentContentBox.classList.add("task-comment-content");

        const commentContent = document.createElement("textarea");
        commentContent.setAttribute("maxlength", "200");
        commentContent.setAttribute("readonly", "readonly");
        commentContent.innerText = content;
        commentContentBox.appendChild(commentContent);


        // 3. 글 하단 버튼(수정 등록/취소) - hide
        // 모두확인요청인 comment의 '내용확인'버튼은 어차피 작성자에게는 노출되지 않도록 한다.
        // 댓글은 내가 작성한 글만 실시간으로 화면에 반영되도록 한다.
        // 타인이 작성한 댓글이 실시간으로 화면으로 나올 필요는 없다. 
        const commentEditApplyBtnBox = document.createElement("div");
        const commentEditSubmitBtn = document.createElement("span");
        const commentEditCancelBtn = document.createElement("span");

        commentEditApplyBtnBox.classList.add("flex-row-end-center-nowrap", "hide");
        commentEditSubmitBtn.classList.add("btn-comment-edit-submit");
        commentEditCancelBtn.classList.add("btn-comment-edit-cancel");

        commentEditApplyBtnBox.appendChild(commentEditSubmitBtn);
        commentEditApplyBtnBox.appendChild(commentEditCancelBtn);

        // 0. container에 부착 (3개요소)
        commentBox.appendChild(commentInfoBox);
        commentBox.appendChild(commentContentBox);
        commentBox.appendChild(commentEditApplyBtnBox);

        return commentBox;

        // 하다 만 나에게 박수...
        // 결국엔 이 function을 사용하게 된 나에게도 박수..
    }

    /*---- ▲ Modal(Task comment)소통하기 - 글 등록 끝 ▲ ----*/

    /*---------- 040 ------------*/
    /*---- ▼ Modal(Task comment)소통하기 - (자신의 글) 수정/삭제 ▼ ----*/

    // 소통하기 - (자신의 글)설정 버튼 눌렀을 때(수정하기/삭제하기 버튼 출력)
    // btncommentTool or btncommentSettings? 이름변경요망...
    // 새로운 글 동적으로 추가했을 때의 수정과 삭제는...
    const btnCommentEditDel = document.querySelectorAll(".btn-comment-edit-del");
    btnCommentEditDel.forEach(function(chosenBtn){
        chosenBtn.addEventListener("click", ()=>{
            // console.log(chosenBtn.children[1]);
            chosenBtn.children[1].classList.toggle("img-hidden");
        });
    });

    /*---------- 041 ------------*/
    // 소통하기 - (자신의 글)수정하기 버튼 눌렀을 때 
    const btnCommentEdit = document.querySelectorAll(".btn-comment-edit");
    btnCommentEdit.forEach(function(chosenBtn){
        chosenBtn.addEventListener("click", ()=>{
            const btn3dot = chosenBtn.parentElement.parentElement;
            btn3dot.classList.add("hide");

            const btnEdits = chosenBtn.parentNode.parentNode.parentNode.parentNode.parentNode.children[2];
            btnEdits.classList.remove("hide");

            const textarea = chosenBtn.parentNode.parentNode.parentNode.parentNode.parentNode.children[1].children[0];
            const beforeEdit = textarea.value;
            textarea.removeAttribute("readonly");
            textarea.focus();
            textarea.classList.add("border-editable");

            // 수정한 내용 등록·취소 버튼 클릭 시
            const btnSubmitEdit = chosenBtn.parentNode.parentNode.parentNode.parentNode.parentNode.children[2].children[0].children[0];
            const btnCancelEdit = chosenBtn.parentNode.parentNode.parentNode.parentNode.parentNode.children[2].children[0].children[1];

            // 수정한 내용 등록
            btnSubmitEdit.addEventListener("click", (chosenBtnthis)=>{
                // 3dot 버튼 나타내기
                btn3dot.classList.remove("hide");
                // 자신이담긴div 숨기기
                btnEdits.classList.add("hide");

                let afterEdit = textarea.value;
                textarea.value = afterEdit;
                textarea.setAttribute("readonly", "readonly");
                textarea.classList.remove("border-editable");
            });

            // 수정 취소
            btnCancelEdit.addEventListener("click", ()=>{
                // 3dot 버튼 나타내기
                btn3dot.classList.remove("hide");
                // 자신이담긴div 숨기기
                btnEdits.classList.add("hide");
                
                textarea.value = beforeEdit;
                textarea.setAttribute("readonly", "readonly");
                textarea.classList.remove("border-editable");
            });
        });
    });  // 소통하기 - (자신의 글)수정하기 버튼 눌렀을 때 끝

    /*---------- 042 ------------*/
    // 소통하기 - (자신의 글)삭제하기 버튼 눌렀을 때
    const btnCommentDel = document.querySelectorAll(".btn-comment-del");
    btnCommentDel.forEach(function(chosenBtn){
        chosenBtn.addEventListener("click", ()=>{
            const confirmBox = chosenBtn.parentElement.parentElement.children[2];
            confirmBox.classList.remove("img-hidden");
            const myComment = chosenBtn.parentElement.parentElement.parentElement.parentElement.parentElement;
            const btnYes = chosenBtn.parentElement.parentElement.children[2].children[1].children[0];
            const btnNo = chosenBtn.parentElement.parentElement.children[2].children[1].children[1];

            btnYes.addEventListener("click", (e)=>{
                e.stopPropagation();
                confirmBox.classList.add("img-hidden");
                myComment.remove();
            });
            btnNo.addEventListener("click", (e)=>{
                e.stopPropagation();
                confirmBox.classList.add("img-hidden");
            });
        });
    }); // 소통하기 - (자신의 글)삭제하기 버튼 눌렀을 때 끝
    /*---- ▲  Modal(Task comment)소통하기 - (자신의 글) 수정/삭제 끝 ▲ ----*/

    /*---------- 043 ------------*/
    /*---- ▼ Modal(File): 파일 시작 ▼ ----*/

    // 이미 생성된 할일의 파일 추가 1/2: input type="file" 
    const modalTaskFileUploadInput = document.querySelector("#upload");
    const modalTaskFileListContainer = document.querySelector(".modal-task-file-list-container");
    modalTaskFileUploadInput.addEventListener("change", ()=>{
        
        const file = modalTaskFileUploadInput.files[0];
        const result = validateFile(file.name, parseInt(file.size));
        if( result === true){
            const name = file.name.substring(0, file.name.lastIndexOf("."));
            const type = file.name.substring(file.name.lastIndexOf(".")+1);
            modalTaskFileListContainer.append(fileHistoryRow("공효진", addFileBox(name, type, returnFileSize(parseInt(file.size)))));
        } else if (result === 2 || result === 3 || result === 6) {
            alert(`첨부가 불가능한 유형의 파일입니다.`);
        } else if (result === 1 || result === 4 || result === 5 || result === 7) {
            alert(`파일 첨부에 실패했습니다. 다음 사항을 확인해주시기 바랍니다. \n 1. 파일명에는 특수기호와 여백이 포함될 수 없습니다.\n(단, +, _, -, .은 포함가능) \n 2. 5MB이하의 파일만 첨부할 수 있습니다.\n ${file.name}의 크기: ${returnFileSize(parseInt(file.size))} \n`);
        }
                
    });

    /*---------- 044 ------------*/
    // 이미 생성된 할일의 파일 추가 2/2: Drag and Drop
    const ModalTaskFileDropZone = document.querySelector("#upload-drop-zone");
    ModalTaskFileDropZone.addEventListener("dragover", (evt)=>{
        evt.preventDefault();
        ModalTaskFileDropZone.classList.add("dragover");
    });
    ModalTaskFileDropZone.addEventListener("dragenter", ()=>{
        if(ModalTaskFileDropZone.classList.contains("dropzone")){
            ModalTaskFileDropZone.classList.add("dragover");
        }
    });
    ModalTaskFileDropZone.addEventListener("dragleave", ()=>{
        if(ModalTaskFileDropZone.classList.contains("dropzone")){
            ModalTaskFileDropZone.classList.remove("dragover");
        }
    });
    ModalTaskFileDropZone.addEventListener("drop", (e)=>{
        e.preventDefault();
        if(ModalTaskFileDropZone.classList.contains("dropzone")){
            ModalTaskFileDropZone.classList.remove("dragover");
            const file = e.dataTransfer.files[0];
            const result = validateFile(file.name, parseInt(file.size));
            if (result === true) {
                const name = file.name.substring(0, file.name.lastIndexOf("."));
                const type = file.name.substring(file.name.lastIndexOf(".")+1);
                modalTaskFileListContainer.append(fileHistoryRow("아이유", addFileBox(name, type, returnFileSize(parseInt(file.size)))));
            } else if (result === 2 || result === 3 || result === 6) {
                alert(`첨부가 불가능한 유형의 파일입니다.`);
            } else if (result === 1 || result === 4 || result === 5 || result === 7) {
                alert(`파일 첨부에 실패했습니다. 다음 사항을 확인해주시기 바랍니다. \n 1. 파일명에는 특수기호와 여백이 포함될 수 없습니다.\n(단, +, _, -, .은 포함가능) \n 2. 5MB이하의 파일만 첨부할 수 있습니다.\n ${file.name}의 크기: ${returnFileSize(parseInt(file.size))} \n`);
            }
        }
    });

    /*---------- 045 ------------*/
    // 이미 생성된 할일의 파일 삭제
    onEvtListener(document, "click", ".modal-task-file-del", function(){
        //console.log(`this: ${this}`); //this: [object HTMLSpanElement]
        //console.log(this); // <span class="hoverBigger20 cursorP modal-task-file-del">
        //console.log(this.parentElement.parentElement); // <div class="파일박스 flex-row-between-nowrap hoverShadow">
        
        
        // 관리자에 의해 삭제된 건지 알려면 userId까지 또 check 해야 하잖아!!
        // 추후 if문 추가 요망: 관리자에 의한 삭제 or 작성자에 의한 삭제
        if(confirm(`정말로 삭제하시겠습니까? \n 파일삭제는 복구가 불가능합니다`) === true) {
            // 작성자 삭제
            this.parentElement.parentElement.parentElement.remove();

            // 관리자 삭제
            // const guide = this.parentNode.parentNode.parentNode.children[3]
            // this.parentElement.parentElement.remove();
            // guide.classList.remove("hide");
        } else {
            return;
        }
    });


    /*---------- 046 ------------*/
    // 업로드일시 생성 - YY.MM.DD (D) 24:00
    function returnDate(){
        const d = new Date();
        // console.log(`date: ${d}`);
        const year = String(d.getFullYear()).substring(2);
        const month1 = d.getMonth()+1;
        const month = month1 < 10 ? "0" + month1: month1;
        const date1 = d.getDate();
        const date = date1 < 10 ? "0" + date1: date1;
        const dayNum = d.getDay();
        const h = d.getHours();
        const m = d.getMinutes();
        const hh = h < 10 ? "0"+h : h;
        const mm = m < 10 ? "0"+m: m;
        let day;
        switch(dayNum) {
            case 0: day = "일"; break;
            case 1: day = "월"; break;
            case 2: day = "화"; break;
            case 3: day = "수"; break;
            case 4: day = "목"; break;
            case 5: day = "금"; break;
            case 6: day = "토"; break;
            default: break;
        }
        return `${year}.${month}.${date} (${day}) ${hh}:${mm}`;
    }

    /*---------- 047 ------------*/
    // 사용자 id로 파일 삭제 버튼 부착여부 판별하는 코드 추가요망
    // 이미 생성된 할일의 파일 추가: 파일내역Row 동적생성
    function fileHistoryRow(uploader, addFileBox){
        const fileRow = document.createElement("div");
        const fileRowDate = document.createElement("span");
        const fileRowUploader = document.createElement("span");
        const deletedByAdminBox = document.createElement("div");

        // 파일이 관리자에 의해 삭제됐을 때 넣을 문구..
        // 파일은 관리자와 작성자에 의해서만 삭제될 수 있다.
        // 작성자가 삭제한 것은 기록에 남지 않으나
        // 타인(관리자)에 의해 삭제된 것은 기록에 남는다.
        const fileDeletedByAdmin = document.createElement("span");

        fileRow.classList.add("modal-task-file-list-col");
        fileRow.classList.add("flex-row-center-center-nowrap");
        fileRow.classList.add("modal-task-file-history-row");

        deletedByAdminBox.classList.add("deletedByAdminBox");
        deletedByAdminBox.classList.add("hide");

        fileDeletedByAdmin.classList.add("font-blur");
        fileDeletedByAdmin.classList.add("font-13");
        fileDeletedByAdmin.classList.add("deletedByAdmin");
        fileDeletedByAdmin.classList.add("hide");
        

        fileRowDate.classList.add("altivo-light");
        fileRowDate.classList.add("font-13");

        fileRowUploader.className = "font-14";

        fileRowDate.innerText = returnDate();
        fileRowUploader.innerText = uploader;
        fileDeletedByAdmin.innerText = "관리자에 의해 삭제되었습니다."

        deletedByAdminBox.append(fileDeletedByAdmin);
        fileRow.append(fileRowDate, fileRowUploader, addFileBox, deletedByAdminBox);

        return fileRow;
    }

    /*---------- 048 ------------*/
    // 사용자 id로 파일 삭제 버튼 부착여부 판별하는 코드 추가요망
    // 이미 생성된 할일의 파일 추가: 첨부된 파일 정보담을 div 동적생성
    function addFileBox(name, type, size) {
        const fileBox = document.createElement("div");

        const fileDiv1 = document.createElement("div");
        const fileIcon = document.createElement("img");
        const fileName = document.createElement("span");
        const fileType = document.createElement("span");

        const fileDiv2 = document.createElement("div");
        const fileSize = document.createElement("span");
        const fileDelBtn = document.createElement("span");

        fileBox.classList.add("flex-row-between-nowrap");
        fileBox.classList.add("hoverShadow");

        fileDiv1.classList.add("flex-row-justify-start-align-center");
        
        fileIcon.src = "../../static/imgs/icon_download4.png";
        fileIcon.alt = "download";
        fileIcon.classList.add("img-15");
        fileIcon.classList.add("mr-5");

        fileName.classList.add("modal-file-name");
        fileType.classList.add("font-14");
        fileType.classList.add("modal-file-type");

        fileDiv2.classList.add("flex-row-justify-start-align-center");
        fileSize.classList.add("modal-task-file-size");
        fileDelBtn.classList.add("modal-task-file-del");

        fileDelBtn.innerHTML = `&times;`;
        fileName.innerText = name;
        fileType.innerText = `.${type}`;
        fileSize.innerText = size;

        fileDelBtn.classList.add("hoverBigger20");
        fileDelBtn.classList.add("cursorP");

        fileDiv1.append(fileIcon, fileName, fileType);
        fileDiv2.append(fileSize, fileDelBtn);
        fileBox.append(fileDiv1, fileDiv2);

        return fileBox;
    }
   
    /*---- ▲ Modal(File): 파일 끝 ▲ ----*/


    /*---------- 049 ------------*/
    /*---- ▼  프로젝트뷰(assignee): 각 담당자의 진행상태별 task 목록 상세 열기 시작 ▼ ----*/
    const btnOpenAssigneeStatusBox = document.querySelectorAll(".btn-open-assignee-status-box");
    const boxAssigneeStatus = document.querySelectorAll(".assignee-status-box");
    btnOpenAssigneeStatusBox.forEach(function(chosenBtn){
        chosenBtn.addEventListener("click", ()=>{
            chosenBtn.classList.toggle("img-angle180");
            boxAssigneeStatus.forEach(function(boxes){
                if(boxes.dataset.id === chosenBtn.dataset.id){
                    boxes.classList.toggle("hide");
                }
            });
        });
    });
    /*---- ▲  프로젝트뷰(assignee): 각 담당자의 진행상태별 task 목록 상세 열기 끝 ▲ ----*/


    /*---------- 050 ------------*/
    // next
    function next(el, selector) {
        const nextEl = el.nextElementSibling;
        if(!selector || (nextEl && nextEl.matches(selector))) {
            return nextEl;
        }
        return null;
    }

    /*---------- 051 ------------*/
    // prev
    function prev(el, selector){
        const prevEl = el.previousElementSibling;
        if(!selector || (prevEl && prevEl.matches(selector))){
            return prevEl;
        }
        return null;
    }

    /*---------- 052 ------------*/
    // jQuery없이 on 사용할 때 ! ★★★
    function onEvtListener(el, eventName, selector, eventHandler){
        if (selector) {
            const wrappedHandler = (e) => {
                if (!e.target) return;
                const el = e.target.closest(selector);
                if (el) {
                    eventHandler.call(el, e);
                }
            }; // wrappedHandler Ends
            el.addEventListener(eventName, wrappedHandler);
            return wrappedHandler;
        } else {
            const wrappedHandler = (e) => {
                eventHandler.call(el, e);
            }; // wrappedHandler Ends
            el.addEventListener(eventName, wrappedHandler);
            return wrappedHandler;
        }

    } // function onEvtListener Ends 

    /*---------- 054 ------------*/
    function parents(el, selector) {
        const parents = [];
        while((el = el.parentNode) && el !== document){
            if(!selector || el.matches(selector)) parents.push(el);
        }
        return parents;
    }
}; // window.onload = function() ends

   /*---- ▼  시작 ▼ ----*/
   /*---- ▲  끝 ▲ ----*/
