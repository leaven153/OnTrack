/*로컬에서 file:// 프로토콜을 사용해 웹페이지를 열면 import, export 지시자가 동작하지 않습니다.*/
import * as commentWs from './wsComment.mjs'
import * as binWs from './wsBin.mjs'

function elExists(el){
    return el !== undefined && el !== null;
}


window.onload = function(){
    console.log(`here we are, main.js!`);
    commentWs.connectCommentWs();
    binWs.connectBinWs();

    /*---- ▼ 열고닫기.. 시작 ▼ ----*/

    /*---------- 001 ------------*/
    /*** 프로젝트 탈퇴 ***/
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
        const btnChkNickname = document.querySelector(".btn-chk-nickname-taken");
        document.querySelector("div.nickname").addEventListener("click", ()=>{
            document.querySelector("input.modal-configure-nickname").value = "";
            document.querySelector(".nickname-ok").classList.add("hide");
            document.querySelector(".nickname-no").classList.add("hide");
            document.querySelector("div#modal-configure-nickname").classList.remove("hide");
            btnChkNickname.disabled = true;
            btnChkNickname.classList.add("font-blur");
            btnChkNickname.classList.add("cursorNot");
        });
        document.querySelector(".btn-close-modal-configure-nickname").addEventListener("click", ()=>{
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
    if(elExists(document.querySelector(".btn-close-modal-taskDetail"))){
        const btnCloseModalTaskDetail = document.querySelector(".btn-close-modal-taskDetail");
        const containerTaskDetail = document.querySelector("#container-task-detail");
        btnCloseModalTaskDetail.addEventListener("click", ()=>{
            document.querySelector("#container-task-detail .modal-Right").classList.remove("slide-side");
            containerTaskDetail.classList.add("hide");
            location.reload(); // 파일 개수 동적으로 설정하기 대체...
        });

    }

    /*---------- 009 ------------*/
    /* 할 일 명 수정: span을 눌렀을 때 */
    // fetch의 url이 같아서 인지, img에서 fetch 실행해도
    // span의 response가 또 출력됨..
    // 추후 span과 img를 통합하여 분기처리 해보기
    if(elExists(document.querySelector("span.btn-edit-task-title"))){
        const spanBtnEditTaskTitle = document.querySelectorAll("span.btn-edit-task-title");
        spanBtnEditTaskTitle.forEach(function(chosenOne){
            chosenOne.addEventListener("click", ()=>{

                // 현 url에서 projectId 추출
                let currUrl = decodeURIComponent(new URL(location.href).pathname).split("/");
                console.log(`제목span누름`);

                // 기존 할 일 명 담는다.
                const oldTaskTitle = chosenOne.innerHTML;
                console.log(oldTaskTitle);

                // 할 일 명이 담긴 input 출력
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
                    if(titleInput.value !== oldTaskTitle) {
                        const taskHistory = {
                            projectId: currUrl[2],
                            taskId: titleInput.dataset.taskid,
                            modItem: "할 일 명",
                            modType: "변경",
                            modContent: titleInput.value,
                            updatedBy: titleInput.dataset.mid
                        }

                        fetch('http://ontrack-env.eba-mpbdgazx.ap-northeast-2.elasticbeanstalk.com/task/editTask?item=title', {
                            method: 'POST',
                            headers: {
                                'Content-type': 'application/json'
                            },
                            body: JSON.stringify(taskHistory)
                        })
                            .then(response => {
                                if(response.ok) {
                                    response.text().then(data => {
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
                                    });
                                } else {
                                    response.json().then(warning => {
                                        alert(warning["message"]);
                                        if(warning["removed"]){
                                            location.reload();
                                        }
                                    });
                                }
                            }); // fetch ends
                    } else {
                        // input 숨기고
                        titleInput.classList.add("hide");

                        // enter 버튼 숨기고
                        next(next(titleInput)).classList.add("hide");

                        // span 출력
                        chosenOne.classList.remove("hide");

                        // edit 버튼 출력
                        next(titleInput).classList.remove("hide");
                    }
                }); // titleInput.addEventListener ends
            });
        });
    } // edit task title ends (span을 눌렀을 때)

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

    // 10-1. table view: 해당 할 일의 담당자 상세보기(.btn-more-assignInfo) 열기 (담당자full name목록, 참여하기, 추가하기 포함됨)
    // (cf. status view: .btn-add-assignee)
    if(elExists(document.querySelector(".btn-more-assignInfo"))){
        const btnOpenAssigneeList = document.querySelectorAll(".btn-more-assignInfo");
        btnOpenAssigneeList.forEach(function(chosenOne){
            chosenOne.addEventListener("click", ()=>{
                // console.log(next(chosenOne)); // <div class="tableView-assignee-list img-hidden">
                // console.log(next(chosenOne).offsetHeight);

                // (이미 담겨있던) 담당자 정보 clear
                currAssignees.clear();

                // 미배정 멤버 정보 clear
                currUnassignedMembers.clear();

                // 클릭한 task의 현재 담당자 수를 구해서 변수에 담는다.
                // console.log(parents(chosenOne, "[class$=-assignee]")[0].querySelector(".task-assignee-list").children);
                cntAssignee = parseInt(parents(chosenOne, "[class$=-assignee]")[0].querySelector(".task-assignee-list").children.length); // next(chosenOne).children[0].children.length
                // console.log(`현재 담당자 수: ${cntAssignee}`);


                // 현재 배정되어 있는 담당자를 (full name list를 참조하여) map에 담는다. (이 일에서 빼기/빠지기에서 활용된다.)
                for(let i = 0; i < cntAssignee; i++) {
                    currAssignees.set(parents(chosenOne, "[class$=-assignee]")[0].querySelector(".task-assignee-list").children[i].children[0].dataset.mid, parents(chosenOne, "[class$=-assignee]")[0].querySelector(".task-assignee-list").children[i].children[0].innerHTML);
                }

                // 현재 미배정 멤버 목록을 currUnassignedMembers map에 담는다.
                [...parents(chosenOne, ".table-assignee")[0].querySelectorAll(".unassigned-member p")].forEach(function(eachMember){
                    currUnassignedMembers.set(eachMember.dataset.mid, eachMember.innerHTML);
                });

                console.log(cntAssignee);
                console.log(parents(chosenOne, "[class$=-assignee]")[0].children[1]);
                // 담당자가 없는 경우, 참여하기/담당차추가하기 div의 top-line과 padding값을 뺀다.
                if(cntAssignee === 0 && elExists(parents(chosenOne, "[class$=-assignee]")[0].querySelector(".btn-add-member-to-assignee"))) {
                    parents(chosenOne, "[class$=-assignee]")[0].querySelector(".btn-add-member-to-assignee").classList.remove("top-line", "pd-t10");
                }
                if(cntAssignee === 0 && elExists(parents(chosenOne, "[class$=-assignee]")[0].querySelector(".btn-bePartOf-task"))) {
                    parents(chosenOne, "[class$=-assignee]")[0].querySelector(".btn-bePartOf-task").classList.remove("top-line", "pd-t10");
                }

                // console.log(cntAssignee);
                // 클릭한 담당자 목록 높이 구해서 변수에 담는다. (아래 담당자 배정하기 위치에서 활용)
                boxHeight = parents(chosenOne, "[class$=-assignee]")[0].querySelector(".more-assignInfo").offsetHeight;

                // 클릭한 task의 담당자 목록 toggle
                if(parents(chosenOne, "[class$=-assignee]")[0].querySelector(".more-assignInfo").classList.contains("img-hidden")){
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

                    // (검색결과 출력을 위해 hide했던) 미배정 멤버 목록 전체를 출력한다.
                    parents(chosenOne,".table-assignee")[0].querySelectorAll(".unassigned-member").forEach(function(member){
                        member.classList.remove("hide");
                    });

                    // 클릭한 task의 담당자 목록만 출력한다.
                    parents(chosenOne, "[class$=-assignee]")[0].querySelector(".more-assignInfo").classList.remove("img-hidden");

                    // staus view의 경우, 담당자가 없을 땐 열리지 않는다. → 취소. 담당자 추가하기 버튼이 열려야 한다..
                    // if(chosenOne.dataset.view === "table" || (chosenOne.dataset.view === "status" && cntAssignee !== 0)){
                    //     next(chosenOne).classList.remove("img-hidden");
                    // }

                } else {
                    parents(chosenOne, "[class$=-assignee]")[0].querySelector(".more-assignInfo").classList.add("img-hidden");
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
    
    // 10-2. table view: 동적으로 생성된 (여러)담당자 상세보기 (.btn-more-assignInfo-new)
    // (담당자가 0 혹은 1명이었다가 2명 이상이 되어 해당 버튼이 생긴 경우)
    onEvtListener(document, "click", ".btn-more-assignInfo-new", function(evt){
        //↓ <img className="btn-more-assignInfo-new ml-3" src="/imgs/down.png" th:src="@{/imgs/down.png}" alt="more assignment info">
        console.log(this);
        //↓ <div class="more-assignInfo tableView-assignee-list">


        evt.stopPropagation();

        // 버튼을 클릭했을 때 옆 요소(담당자 상세 div)가 열려있다면 닫고
        // 닫혀있다면 열어야 한다.
        if(!parents(this, "[class$=-assignee]")[0].querySelector(".more-assignInfo").classList.contains("img-hidden")) {
            parents(this, "[class$=-assignee]")[0].querySelector(".more-assignInfo").classList.add("img-hidden");
        } else {
            // 담당자 정보 clear
            currAssignees.clear();

            // 미배정 멤버 정보 clear
            currUnassignedMembers.clear();

            // 클릭한 task의 현재 담당자 수
            cntAssignee = parents(this, "[class$=-assignee]")[0].querySelector(".task-assignee-list").children.length;

            // 클릭한 task의 현재 담당자 정보(mid, name)를 담는다.
            [...parents(this, ".table-assignee")[0].querySelectorAll(".assignee-fullname-box p")].forEach(function(eachAssignee){
                currAssignees.set(eachAssignee.dataset.mid, eachAssignee.innerHTML);
            });
            // for(let i = 0; i < cntAssignee; i++){
            //     // console.log(`기배정담당자의 mid: ${next(this).children[0].children[i].children[0].dataset.mid}`);
            //     currAssignees.set(next(this).children[0].children[i].children[0].dataset.mid, next(this).children[0].children[i].children[0].innerHTML);
            // }

            // 클릭한 task의 현재 미배정 멤버 정보(mid, name)를 담는다.
            [...parents(this, ".table-assignee")[0].querySelectorAll(".unassigned-member p")].forEach(function(eachMember){
                currUnassignedMembers.set(eachMember.dataset.mid, eachMember.innerHTML);
            });

            // 담당자가 없는 상태일 경우
            // 담당자 추가하기 / 참여하기의 div에 top-line을 없앤다.
            if(cntAssignee === 0) {
                parents(this, "[class^=more-]")[0].children[1].classList.remove("top-line", "pd-t10");
                // next(this).children[1].classList.remove("top-line", "pd-t10");
            }

            // 현재 담당자 수에 따른 담당자 목록 높이를 담아둔다. (멤버목록 출력 시 활용)
            // boxHeight = parents(this, "[class$=-assignee]")[0].querySelector(".more-assignInfo").offsetHeight;

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
            parents(this, "[class$=-assignee]")[0].querySelector(".more-assignInfo").classList.remove("img-hidden");
        }
    });


    // 10-3. 담당자 해제(.btn-dropOut-task): full name 옆에 있는 X 버튼.
    // table view: 담당자, 작성자에게 출력. .btn-more-assignInfo(-new)를 클릭한 후에 출력되는 버튼이므로, cntAssignee, currAssignees map, currUnAssignees map을 공유한다.
    // status view: 작성자에게 출력. .btn-add-assignee를 클릭한 후에 출력되는 버튼이므로, cntAssignee, currAssignees map, currUnAssignees map을 공유한다.
    onEvtListener(document, "click", ".btn-dropOut-task", function(){
        console.log(this);
        const datum = this.dataset;

        const midNname = [datum["assigneemid"], prev(this).innerText];
        console.log(midNname);

        // 1) 서버로 정보 넘긴다.
        // RequestParam에 assigneeMid 넘기고
        // task History를 객체로 넘긴다
        const taskHistory = {
            taskId: datum["taskid"],
            projectId: datum["projectid"],
            modItem: "담당자",
            modType: "해제",
            modContent: prev(this).innerText,
            updatedBy: datum["executormid"],
        }

        // console.log(taskHistory);

        fetch(`http://ontrack-env.eba-mpbdgazx.ap-northeast-2.elasticbeanstalk.com/task/editAssignee?mid=${datum["assigneemid"]}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(taskHistory)
        }).then(response => {
            if(response.ok){

                // X버튼은 작성자 혹은 담당자 자신에게만 노출된다.
                // 만약 작성자가 아닌 담당자가 이 일에서 빠지기를 클릭했다면 response 이후 reload 한다.
                // ∵ 1) 담당자에게만 주어지는 수정 권한 모두를 동적으로 삭제하지 않고
                // 2) 계획중일때까지는 멤버에게 참여하기 버튼이 나와야 하는데, 이를 체크하고 요소 붙여넣기 하지 않고 그냥 바로 리로드!
                if(datum["executormid"] !== datum["authormid"]){
                    location.reload();
                }

                // 2) 미배정 멤버 목록에 (담당 해제된) 해당 멤버 출력 (appendChild): status view에는 미배정 멤버 목록을 출력하지 않는다. (just 검색만 한다.)
                // 3) 변경된 boxHeight를 멤버목록 top에 반영: status에는 멤버목록이 없기 때문에(검색만 있다) view 확인 후 진행한다.

                // parents(parents(chosenOne)[0], ".more-assignInfo")[0].querySelectorAll(`:scope ${".unassigned-member-list"}`)[0].append(unassignedMemberElement(chosenOne.dataset.assigneemid, prev(chosenOne).innerText));
                if(datum["view"] === "table") {
                    parents(this, "[class$=-assignee]")[0].querySelector(".unassigned-member-list").appendChild(unassignedMemberElement(datum, midNname)); // 해당 div의 높이가 조절되지 않는다?
                    boxHeight = parents(this, "[class$=-assignee]")[0].querySelector(".more-assignInfo").offsetHeight;
                    topValue = (boxHeight-28)*(0.6885);
                    console.log(topValue);
                    parents(this, "[class$=-assignee]")[0].querySelector(".find-member-to-assign").style.top = topValue + 'px';
                }

                // 4) 현재 담당자 수에서 1 차감한다.
                cntAssignee--;

                // 5) 현재 담당자 map에서 담당자 삭제한다.
                currAssignees.delete(datum["assigneemid"]);

                // 6) 미배정 멤버 map에 추가한다.
                currUnassignedMembers.set(datum["assigneemid"], prev(this).innerText);

                // 7) 담당자 요약 목록에서 해당 담당자 이름 제거
                if(cntAssignee === 0) { // one → none
                    parents(this, "[class$=-assignee]")[0].querySelector(".one-assignee").remove();
                    /*
                    // one-assignee는 remove
                    if(parents(this, "[class$=-assignee]")[0].children[0].classList.contains("one-assignee")){ // table view
                        parents(this, "[class$=-assignee]")[0].children[0].remove();
                    }
                    if(datum["view"] === "status") {
                        parents(this, ".status-assignee")[0].querySelector(".one-assignee").remove();
                    }
                     */

                    // no-assignee를 prepend
                    parents(this, "[class$=-assignee]")[0].querySelector(".assignee-brief-box").append(assigneeBoxByNum(0, datum["view"]));

                    // table view: 담당자 추가하기div(.btn-add-member-to-assignee),
                    // status view: 검색(.findByName-member-to-assign)에
                    // top-line 없애기
                    parents(this, "[class^=more-]")[0].children[1].classList.remove("top-line", "pd-t10");

                    // table view의 경우, 멤버목록 top 변경
                    if (datum["view"] === "table") {
                        parents(this, ".more-assignInfo")[0].querySelector(".find-member-to-assign").style.top = 0 + 'px';
                    }

                }

                if (cntAssignee === 1) { // many → one

                    // 더보기 버튼(img) remove (status view에는 없기 때문에 요소가 있는지 검사 후 진행한다.)
                    if (elExists([...parents(this, "[class$=-assignee]")[0].querySelector(".assignee-brief-box").children].filter(child => child.tagName === "IMG")[0])){
                        [...parents(this, "[class$=-assignee]")[0].querySelector(".assignee-brief-box").children].filter(child => child.tagName === "IMG")[0].remove();
                    }


                    if(datum["view"] === "status") {
                        // currAssignees.forEach((v, k) => parents(this, "[class$=-assignee]")[0].firstElementChild.prepend(assigneeBoxByNum(1, k, v)));
                        currAssignees.forEach((v, k) => parents(this, "[class$=-assignee]")[0].querySelector(".assignee-brief-box").append(assigneeBoxByNum(1, datum["view"], k, v)));

                    } else {
                        // currAssignees.forEach((v, k) => parents(this, "[class$=-assignee]")[0].prepend(assigneeBoxByNum(1, k, v)));
                        currAssignees.forEach((v, k) => parents(this, "[class$=-assignee]")[0].querySelector(".assignee-brief-box").append(assigneeBoxByNum(1, datum["view"], k, v)));
                    }


                    // many-assignee remove
                    parents(this, "[class$=-assignee]")[0].querySelectorAll(".many-assignee").forEach(function(eachOne){
                        eachOne.remove();
                    });

                }

                if (cntAssignee > 1) { // many에서 many 유지 (cntAssignee가 2이상인 경우)

                    // 담당자 요약목록에서 담당해제된 멤버의 mid를 찾아 지운다
                    // full name에는 assigneemid, 요약 목록에는 mid
                    // const assigneeFirstNameList = [...parents(chosenOne, "[class$=-assignee]")[0].children].filter(child => child.classList.contains("many-assignee")); // status view와 통합 전
                    const assigneeFirstNameList = [...parents(this, "[class$=-assignee]")[0].querySelectorAll(".many-assignee")];
                    for(let i = 0; i < assigneeFirstNameList.length; i++){
                        if(assigneeFirstNameList[i].children[0].dataset.mid === datum["assigneemid"]) {
                            assigneeFirstNameList[i].remove();
                        }
                    }
                }

                // 8) 해당 담당자 이름 담긴 box를 (담당자 full name 목록에서) 삭제한다.
                parents(this)[0].remove();

            } else {
                response.json().then(warning => {
                    alert(warning["message"]); // 담당자 삭제가 완료되지 않았습니다. or 해당 할 일이 존재하지 않습니다.
                    if(warning["taskRemoved"]){
                        location.reload();
                    }

                });

            } // if(response.ok) ends
        }); // fetch ends
    }); // 10-3. (기배정) 담당자 해제 (onEvtListener(.btn-dropOut-task)) 끝


    // 10-4. 참여취소 (status view에서 보류, 시작 안 함, 계획중인 일에 담당자와 참여하기(btn-bePartOf-task)를 누른 사람에게 출력.)
    // cf. 이 일에서 빠지기: table view .btn-dropOut-task
    onEvtListener(document, "click", ".btn-cancel-participation", function(){
        console.log("참여취소 눌렀음");
        console.log(this);
        const datum = this.dataset; // project id, task id, mid, nickname, view

        console.log(datum);

        // 서버로 보낼 정보 추출
        const taskHistory = {
            projectId: datum["projectid"],
            taskId: datum["taskid"],
            modItem: "담당자",
            modType: "해제",
            modContent: datum["nickname"],
            updatedBy: datum["mid"]
        };

        // 서버로 전송
        // 담당자 추가 시, taskHistory에는 작성자의 member id가 담긴다. (as executor; updatedBy)
        // 때문에 editAssignee에는 추가/삭제된 담당자의 member id가 파라미터로 담긴 상황
        // modContent에 이름과 id를 묶어서 보내어 끊어 사용하려 해봤으나 (json stringfy로 전달된 객체의 값은 배열이었다...)
        // 예) 심지호(34) ← 괄호의 위치를 찾아 인덱스로 받아 끊어내는 작업을 하느니, 파라미터로 던지는 게 낫다고 판단함.

        fetch(`http://ontrack-env.eba-mpbdgazx.ap-northeast-2.elasticbeanstalk.com/task/editAssignee?mid=${datum["mid"]}`, {
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
                currUnassignedMembers.clear();

                // 참여취소가 반영되기 전의 담당자 수를 저장한다.
                cntAssignee = parents(this, ".status-assignee")[0].querySelector(".task-assignee-list").children.length;
                console.log(cntAssignee);

                // 참여 취소가 반영되기 전의 담당자 정보를 currAssignees map에 담는다.
                [...parents(this, ".status-assignee")[0].querySelectorAll(".assignee-fullname-box p")].forEach(function (eachAssignee){
                    currAssignees.set(eachAssignee.dataset.mid, eachAssignee.innerHTML);
                });
                console.log(currAssignees);

                // 참여 취소가 반영되기 전의 미배정 멤버 목록을 currUnAssignees map에 담는다.
                // ☞ 참여하기/참여취소 버튼이 출력되는 존재는 검색하여 멤버 배정 기능을 사용할 수 없다!
                // [...parents(this, ".status-assignee")[0].querySelectorAll(".unassigned-member p")].forEach(function(eachMember){
                //     currUnassignedMembers.set(eachMembet.dataset.mid, eachMember.innerHTML);
                // });

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
                // ☞ 참여하기/참여취소 버튼이 출력되는 존재는 검색하여 멤버 배정 기능을 사용할 수 없다!
                // currUnassignedMembers.set(datum["mid"], datum["nickname"]);
                // console.log(currUnassignedMembers)

                // 검색 결과로 출력하기 위해 unassigned-member-list에도 추가한다.
                // ☞ 참여하기/참여취소를 누른 멤버는 작성자가 아니라 멤버이기 때문에 배정을 위한 미배정멤버 검색이 불가능하다.
                // parents(this, ".status-assignee")[0].querySelector(".unassigned-member-list").append(unassignedMemberElement(datum["mid"], datum["nickname"]));


                // 담당자 요약 목록에서 제거한다.
                if(cntAssignee === 0) { // one to none
                    console.log("one to none");
                    // (status view 구조상, 담당자 이름 요약 요소가 full name div와 같은 부모 아래 있기 때문에,
                    // 담당자 이름 일단 hide 후, no-assignee 요소 prepend 후에 one-assignee 제거
                    // parents(this, ".status-assignee")[0].querySelector(".one-assignee").classList.add("hide");
                    parents(this, ".status-assignee")[0].querySelector(".assignee-brief-box").append(assigneeBoxByNum(0, datum["view"]));
                    parents(this, ".status-assignee")[0].querySelector(".one-assignee").remove();
                }

                if(cntAssignee === 1){ // many to one
                    console.log(currAssignees);
                    // 내가 빠지고 남은 멤버의 이름을 one-assignee 요소로 변환하여 화면에 부착한다.
                //     currAssignees.forEach((v, k) => parents(this, ".status-assignee")[0].firstElementChild.prepend(assigneeBoxByNum(1, k, v)));
                    currAssignees.forEach((v, k) => parents(this, ".status-assignee")[0].querySelector(".assignee-brief-box").append(assigneeBoxByNum(1, datum["view"], k, v)));

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

                // hide했던 참여하기 버튼을 다시 보이게 한다.
                // parents(this, ".status-assignee")[0].querySelector(".btn-bePartOf-task").classList.remove("hide");

                // 계획중인지 확인한 후
                // 참여하기 버튼을 생성하여 부착한다.
                console.log(datum["status"]);
                if(datum["status"] < 3) {
                    parents(this, ".status-assignee")[0].append(btnBePartOftask(datum));
                }

                // 참여취소 버튼 없앤다.
                this.remove();
            } else {
                response.json().then(warning => {
                    alert(warning["message"]);
                    if(warning["taskRemoved"]){
                        location.reload();
                    }
                });
            }
        });
    }); // 10-4B. status view 참여취소 끝

    // 10-5. 이 일에 참여하기
    // 해당 일의 담당자, 작성자가 아닌 멤버에게
    // 해당 일의 진행상태가 '계획중'일때까지만 출력된 버튼
    // .btn-more-assignInfo를 누른 후에 출력되는 버튼이기 때문에
    // cntAssingee, currXXX(map)이 공유된 상태다.
    // cf. status view에서 담당자에게는 '참여취소' 버튼, 작성자(담당자 여부에 상관없이) '담당자 관리' 버튼으로 갈음된다.
    onEvtListener(document, "click", ".btn-bePartOf-task", function(){
        console.log(this);

        const currView = this.dataset.view;
        if(currView === "table") {
            const datum = this.dataset;
            console.log("table view에서 참여하기 눌렀을 때: ")
            console.log(cntAssignee);
            console.log(currAssignees);
            console.log(currUnassignedMembers);
            console.log(datum); // DOMStringMap(4) { mid → "14", nickname → "공지철",
            // projectid → "9", taskid → "11" }
            // 담당자가 6명 미만일 때만 참여하기 가능하다
            if(cntAssignee < 6){
                // 서버에 보낼 정보 추출
                const taskHistory = {
                    projectId: datum["projectid"],
                    taskId: datum["taskid"],
                    modItem: "담당자",
                    modType: "배정",
                    modContent: datum["nickname"],
                    updatedBy: datum["executormid"]
                };

                console.log(taskHistory);

                // fetch

                fetch(`http://ontrack-env.eba-mpbdgazx.ap-northeast-2.elasticbeanstalk.com/task/editAssignee?mid=${datum["executormid"]}`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(taskHistory)
                }).then(response => {
                    if(response.ok) {
                        console.log("ok")
                        // 이전 담당자 수
                        const beforeCnt = cntAssignee;

                        // 담당자 수 추가
                        cntAssignee++;

                        // 미배정 멤버 목록 currUnAssignedMEmbers map에서 삭제
                        currUnassignedMembers.delete(datum["executormid"]);

                        // 담당자 요약 목록에 추가
                        if(cntAssignee === 1) { // none to one
                            // no assignee 지운다
                            parents(this, ".table-assignee")[0].querySelector(".no-assignee").remove();

                            // .assignee-brief-box에 one-assignee append
                            parents(this, ".table-assignee")[0].querySelector(".assignee-brief-box").append(assigneeBoxByNum(1, datum["view"], datum["executormid"], datum["nickname"]));
                        }

                        // table의 참여하기는 담당자 배정(.unassigned-member 클릭)과는 달리
                        // 미배정 멤버였던 자에게 한해 노출되는 버튼이기 때문에
                        // 모달을 열어둔 상태에서 기존 담당자를 지우는 등의 행위를 할 수 없다.
                        // 때문에 현재 cntAssignee만 확인하여 진행해도 가능하지만, 일관성을 위해
                        // 두 변수 모두 확인한다.
                        if(beforeCnt === 1 && cntAssignee > 1) { // one to many
                            // one-assignee 지우고
                            parents(this, ".table-assignee")[0].querySelector(".one-assignee").remove();

                            // 기존 담당자 append
                            for(const entry of currAssignees) {
                                parents(this, ".table-assignee")[0].querySelector(".assignee-brief-box").append(assigneeBoxByNum(2, datum["view"], entry[0], entry[1]));
                            }

                            // 새 담당자 append
                            parents(this, ".table-assignee")[0].querySelector(".assignee-brief-box").append(assigneeBoxByNum(2, datum["view"], datum["executormid"], datum["nickname"]));

                            // 상세보기 버튼 append
                            parents(this, ".table-assignee")[0].querySelector(".assignee-brief-box").append(btnMoreAssigneeInfo());

                        }

                        if(beforeCnt > 1) { // many to many
                            parents(this, ".table-assignee")[0].querySelector(".assignee-brief-box").appendChild(assigneeBoxByNum(2, datum["view"], datum["executormid"], datum["nickname"]));
                        }

                        // 담당자 목록 currAssignees map에 추가
                        currAssignees.set(datum["executormid"], datum["nickname"]);

                        // 담당자 full name 목록에 추가
                        parents(this, ".more-assignInfo")[0].querySelector(".task-assignee-list").append(newAssigneeElement(datum["executormid"], datum["nickname"], datum));

                        // 이 일에 참여하기 버튼 숨기기
                        this.classList.add("hide");
                    } else {
                        response.json().then(warning => {
                            console.log(warning);
                            alert(warning["message"]);
                            if(warning["taskRemoved"]){
                                location.reload();
                            }
                        });
                    }
                });


            } else {
                alert(`담당자는 6명을 초과할 수 없습니다.`);
            } // if 6명 미만일 때만 참여 가능 ends

        } // if table view ends

        if(currView === "status") {
            const datum = this.dataset;
            let cntAssignee = parents(this, ".status-assignee")[0].querySelector(".task-assignee-list").children.length;
            console.log("status view에서 참여하기 눌렀을 때: ")
            console.log(datum); // DOMStringMap(4) { mid → "14", nickname → "공지철", projectid → "9", taskid → "21" }
            console.log(`현재 담당자 수: ${cntAssignee}`);

            // 0. 이전에 담긴 currAssignees를 먼저 clear
            // (status view에서는 '참여하기'가 btn-more-assignInfo를 거치지 않고 시작된다.)
            currAssignees.clear();

            // 1-1. 현재 담당자를 currAssignees map에 담는다 (∴status는 참여하기 버튼이 btn-moreAssign-info와 별도로 있다)
            // console.log(parents(btn, ".status-assignee")[0]); // <div class="status-assignee">
            // 개별담당자div
            [...parents(this, ".status-assignee")[0].querySelector(".task-assignee-list").children].forEach(function(eachAssignee){
                // 개별담당자 div 안의 p
                currAssignees.set(eachAssignee.children[0].dataset.mid, eachAssignee.children[0].innerHTML);
            });

            console.log(currAssignees);

            // 1-2. 현재 미배정 멤버 목록을 currUnassignedMembers map에 담는다. (검색에 사용한다.)
            // ☞ 참여하기/참여취소 버튼이 출력되는 존재는 검색하여 멤버 배정 기능을 사용할 수 없다!
            // [...parents(this, ".status-assignee")[0].querySelectorAll(".unassigned-member p")].forEach(function(eachMember){
            //     currUnassignedMembers.set(eachMember.dataset.mid, eachMember.innerHTML);
            // });

            // console.log(currUnassignedMembers);

            // 1-3. 참여하기를 누른 멤버를 currUnassignedMembers map에서 제거한다.
            // (status view에서는 미배정 멤버 목록이 노출되지 않기 때문에 hide할 요소는 없다)
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
                    modItem: "담당자",
                    modType: "배정",
                    modContent: datum["nickname"],
                    updatedBy: datum["mid"]
                };

                console.log(taskHistory);

                // 2-5. 서버로 전송
                fetch(`http://ontrack-env.eba-mpbdgazx.ap-northeast-2.elasticbeanstalk.com/task/editAssignee?mid=${this.dataset.mid}`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(taskHistory)
                }).then(response => {
                    if(response.ok){
                        console.log('서버로 전송 및 결과 반환 ok');
                        // 서버 전송 성공시: 담당자 풀네임 박스에 이름담는다
                        parents(this, ".status-assignee")[0].querySelector(".task-assignee-list").append(newAssigneeBySelf(datum));

                        // 서버 전송 성공시: 담당자 요약 박스에 담는다
                        // ★★★ DB에 저장 전에 현재 담당자가 6명인지 확인하는 로직이 필요하다!!

                        // 담당자요약박스: 담당자없음 → 담당자 1명
                        if (cntAssignee === 1) {
                            console.log("담당자 없다가 추가되었을 때");
                            console.log(currAssignees);
                            // console.log(assigneeBoxByNum(1, this.dataset.mid, this.dataset.nickname));
                            // status view는 status-assignee 안에
                            // no-assignee와 담당자 full name list가 형제이기 때문에 지우면 안된다. 일단 숨기고
                            // parents(this, ".status-assignee")[0].querySelector(".no-assignee").classList.add("hide");

                            // one-assignee div 생성해서 '배정된 담당자 이름 요약'div(.assignee-brief-box)에 append 한다.
                            parents(this, ".status-assignee")[0].querySelector(".assignee-brief-box").append(assigneeBoxByNum(1, datum["view"], this.dataset.mid, this.dataset.nickname));

                            // no-assignee 요소 지운다.
                            parents(this, ".status-assignee")[0].querySelector(".no-assignee").remove();
                        }

                        // 담당자요약박스: 담당자 1명 → 담당자 다수(many-assignee)
                        // table view의 경우 담당자를 추가한 후에 (모달을 열어둔 상태에서) 기존 담당자를 삭제할 수도 있기 때문에
                        // 이전 담당자 수만이 아닌 현재 담당자 수도 확인하고 one to many를 진행해야 하지만
                        // staus view의 참여하기는 참여하기를 누르면 바로 요약 목록에 추가되고
                        // 참여하기를 누른 멤버에게는 참여 취소 버튼만 노출되고
                        // 담당자 삭제는 할 수 없기 때문에 이전 담당자 수가 1이었는지만 확인하면 된다.
                        if (beforeCnt === 1) { // one → many
                            // 담당자 이름 요약 부착 (status view에는 btn-more-assignInfo 버튼을 부착하지 않고, 담당자 추가 버튼을 클릭하면 full name list가 뜨도록 한다)
                            console.log("one to many");
                            // console.log(currAssignees); // Map { 28 → "스칼렛 요한슨", 14 → "공지철" }

                            parents(this, ".status-assignee")[0].querySelector(".one-assignee").remove();

                            for(const entry of currAssignees) {
                                parents(this, ".status-assignee")[0].querySelector(".assignee-brief-box").append(assigneeBoxByNum(2, datum["view"], entry[0], entry[1]));
                            }
                        }

                        // 담당자요약박스: many → many
                        if(beforeCnt > 1 && cntAssignee > 1){
                            console.log("many to many");
                            // console.log(currAssignees);
                            // console.log([...parents(this, ".status-assignee")[0].querySelectorAll(".many-assignee")]);
                            parents(this, ".status-assignee")[0].querySelector(".assignee-brief-box").append(assigneeBoxByNum(2, datum["view"], this.dataset.mid, this.dataset.nickname));
                        }

                        // 참여취소 버튼으로 바꾼다.
                        parents(this, ".status-assignee")[0].append(btnCancelParticipation(datum));

                        // 참여하기 버튼을 제거한다.
                        this.remove();

                        // 참여하기 버튼을 동적으로 생성하지 않고 숨기기한다.
                        // this.classList.add("hide");
                    } else {
                        response.json().then(warning => {
                            alert(warning["message"]);
                            if(warning["taskRemoved"]){
                                location.reload();
                            }
                        });
                    }
                });

            } else {
                alert(`담당자는 6명을 초과할 수 없습니다.`);
            }
        }
    });

    // 055. 참여취소 버튼 생성
    function btnCancelParticipation(datum){
        const div = document.createElement("div");
        const span = document.createElement("span");
        div.classList.add("mr-5", "opacity0", "btn-cancel-participation");
        div.setAttribute("data-mid", datum["mid"]);
        div.setAttribute("data-nickname", datum["nickname"]);
        div.setAttribute("data-projectid", datum["projectid"]);
        div.setAttribute("data-taskid", datum["taskid"]);
        div.setAttribute("data-status", datum["status"]);
        span.classList.add("font-11");
        span.innerText = "참여취소";
        div.appendChild(span);
        return div;
    }

    // 060. 참여하기 버튼 생성(기배정 담당자가 '참여취소'버튼을 눌렀을 경우)
    function btnBePartOftask(datum) {
        const div = document.createElement("div");
        const span = document.createElement("span");
        div.classList.add("btn-bePartOf-task", "mr-5", "opacity0");
        div.setAttribute("data-mid", datum["mid"]);
        div.setAttribute("data-nickname", datum["nickname"]);
        div.setAttribute("data-projectid", datum["projectid"]);
        div.setAttribute("data-taskid", datum["taskid"]);
        div.setAttribute("data-status", datum["status"]);
        div.setAttribute("data-view", "status");
        span.classList.add("font-11");
        span.innerText = "참여하기";
        div.appendChild(span);
        return div;
        
    }
    // 10-6A. 미배정 담당자 보기(table view, .btn-more-assignInfo): ①검색, ②미배정 멤버 목록 출력
    // (담당자 상세보기 버튼 클릭 후에 출력되므로, cntAssignee, currAssignees, currUnAssignees가 공유된다.)
    //  (∵table view는 멤버목록 div가 담당자div의 우측으로 추가됨. cf. status view는 btn-more-assignInfo를 누르면 바로 담당자 목록이 뜸)
    // - 작성자에게만 출력되며, 해당 일의 진행상태가 '검토중'일 때까지만 가능
    // - 검색창에서 새로 배정할 사람을 입력한다. (검색버튼(btn-findByName-member-to-assign)은 next에 의해 지정됨)
    if(elExists(document.querySelector(".btn-add-member-to-assignee"))){
        const btnFindMemberToAssign = document.querySelectorAll(".btn-add-member-to-assignee");
        btnFindMemberToAssign.forEach(function(chosenOne){
            chosenOne.addEventListener("click", (evt)=>{
                evt.stopPropagation();

                // 1. table view - 해당 프로젝트 멤버 목록 div 화면 노출(출력)
                boxHeight = parents(chosenOne, "[class^=more-]")[0].offsetHeight;
                console.log(boxHeight);
                if(boxHeight < 100) {
                    topValue = boxHeight*(1/2);
                }

                if (boxHeight > 100 && boxHeight < 160) {
                    topValue = boxHeight*(0.685);
                }
                if(boxHeight >= 160) {
                    topValue = boxHeight*(0.76);
                }

                next(chosenOne).style.top = topValue + 'px';
                if(cntAssignee === 0) {
                    next(chosenOne).style.top = 0 + 'px';
                }
                if(next(chosenOne).classList.contains("img-hidden")){
                    next(chosenOne).classList.remove("img-hidden");
                } else {
                    next(chosenOne).classList.add("img-hidden");
                }
            }); // table view: 담당자 추가하기 버튼(btnFindMemberToAssign) click 이벤트 끝
        }); // table view: 담당자 추가하기 (더보기) 버튼 For Each 끝
    } // 10-6A. 미배정 담당자 보기(table view, .btn-more-assignInfo): ①검색, ②미배정 멤버 목록 출력 끝

    // 10-6B. 담당자 상세보기, 미배정 담당자 보기(status view; 검색하여 담당자 추가)
    // 작성자에게만 검토중일 때까지만 출력된다.)
    if(elExists(document.querySelector(".btn-add-assignee"))){
        const btnAddAssignees = document.querySelectorAll(".btn-add-assignee");
        btnAddAssignees.forEach(function(btn){
            btn.addEventListener("click", ()=>{

                // console.log("담당자관리 클릭");
                // 담당자 목록을 열든 닫든 이미 담겨있던 담당자, 미배정멤버 map은 비운다.
                currAssignees.clear();
                currUnassignedMembers.clear();

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

                    // 검색 결과로 출력했던 내용 hide
                    if(document.querySelectorAll(".unassigned-member-list")){
                        document.querySelectorAll(".unassigned-member-list").forEach(function(eachOne){
                            eachOne.classList.add("hide");
                        });
                    }

                    // 클릭한 task의 모달(만) 연다.
                    parents(btn, ".status-assignee")[0].querySelector(".more-assignInfo").classList.remove("img-hidden");

                    // 이미 배정되어 있던 담당자 수를 구한다.
                    cntAssignee = parents(btn, ".status-assignee")[0].querySelector(".task-assignee-list").children.length;
                    // console.log(cntAssignee);
                    // 이미 배정되어 있던 담당자의 member id와 이름을 currAssignees map에 담는다.
                    parents(btn, ".status-assignee")[0].querySelectorAll("div.assignee-fullname-box > p").forEach(function(eachOne){
                        currAssignees.set(eachOne.dataset.mid, eachOne.innerHTML);
                    });
                    // console.log(currAssignees);

                    // 미배정된 담당자의 member id와 이름을 currUnassignedMembers map에 담는다.
                    parents(btn, ".status-assignee")[0].querySelectorAll(".unassigned-member p").forEach(function(eachUnassignedMember){
                        currUnassignedMembers.set(eachUnassignedMember.dataset.mid, eachUnassignedMember.innerHTML);
                    });
                    console.log(currUnassignedMembers);

                } else { // 담당자 관리 모달 toggle
                    parents(btn, ".status-assignee")[0].querySelector(".more-assignInfo").classList.add("img-hidden");
                }

            });
        });
    } // 10-6B. 미배정 담당자 보기(status view; 검색하여 담당자 추가) 모달 여닫기(toggle) ends


    // 10-7. 담당자 추가(.unassigned-member 클릭 이벤트)
    // [A] table view:
    // 담당자 상세보기 버튼(.btn-more-assigneInfo)를 클릭한 후(cntAssignee, currAssignees(map), currUnAssignees(map)이 생성된다)
    // 작성자에게만 출력되는 담당자추가하기 버튼(.btn-add-member-to-assignee) 클릭하면 나오는
    // 미배정목록에서 멤버 이름 클릭했을 때
    // [B] status view:
    // 작성자에게만 출력되는 담당자관리 버튼(.btn-add-assignee)을 클릭한 후
    // cntAssignee, currAssignees(map), currUnAssignees(map)에 값이 저장된 상태에서
    // 검색하기를 누른 후에 검색결과로 출력된 멤버 이름을 클릭했을 때
    onEvtListener(document, "click", ".unassigned-member", function(){

        console.log(this); // <div class="개별멤버box unassigned-member" data-view="table" data-executormid="14" data-authormid="14" data-taskid="8" data-projectid="9">
        const datum = this.dataset;
        const newAssigneeMid = this.querySelector("p").dataset.mid;
        const newAssigneeName = this.querySelector("p").innerHTML;
        console.log(datum); // DOMStringMap(5) { view → "table", executormid → "14", authormid → "14", taskid → "8", projectid → "9" }
        console.log(`현재 담당자 수: ${cntAssignee}`);
        console.log(`현재 담당자 목록(map): ${currAssignees}`);
        console.log(`현재 미배정 멤버 목록(map): ${currUnassignedMembers}`);

        // 현재 담당자 수가 6명 미만일 때만 추가가 가능하다

        if (cntAssignee < 6) {

            // 서버에 전달할 정보
            const taskHistory = {
                projectId: datum["projectid"],
                taskId: datum["taskid"],
                modItem: "담당자",
                modType: "배정",
                modContent: newAssigneeName,
                updatedBy: datum["executormid"]
            };

            console.log(taskHistory);


            fetch(`http://ontrack-env.eba-mpbdgazx.ap-northeast-2.elasticbeanstalk.com/task/editAssignee?mid=${newAssigneeMid}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(taskHistory)
            }).then(response => {
                if(response.ok){
                    // 담당자를 추가하기 전 담당자 수를 담아둔다. (담당자 요약 목록 변화에 반영한다.)
                    const beforeCnt = cntAssignee;

                    // 현재 담당자 수에 추가한다.
                    cntAssignee++;

                    // 현재 미배정 담당자 목록 map에서 삭제한다.
                    currUnassignedMembers.delete(newAssigneeMid);

                    // 담당자 요약 목록에 출력한다.
                    if(cntAssignee === 1) { // none → one
                        console.log("unassigned-memeber click: none to one");
                        // no-assignee hide
                        parents(this, "[class$=-assignee]")[0].querySelector(".no-assignee").classList.add("hide");

                        // one-assignee add
                        parents(this, "[class$=-assignee]")[0].querySelector(".assignee-brief-box").prepend(assigneeBoxByNum(1, datum["view"], newAssigneeMid, newAssigneeName));

                        // table view의 경우 담당자 추가 div(.btn-add-member-to-assignee)에 remove했던 top-line, pd-t10을 붙인다.
                        if(datum["view"] === "table") {
                            parents(this, ".more-assignInfo")[0].querySelector(".btn-add-member-to-assignee").classList.add("top-line", "pd-t10");
                        }

                        // no-assignee remove
                        parents(this, "[class$=-assignee]")[0].querySelector(".no-assignee").remove();

                    }

                    // 멤버 추가한 후에 (모달을 열어둔 상태에서) 기존 담당자를 지울 수도 있기 때문에
                    // 현재 담당자의 수도 체크한 후에 one to many를 적용해야 한다.
                    if(beforeCnt === 1 && cntAssignee > 1) { // one → many
                        console.log("unassigned-memeber click: one to many");
                        // one-assignee 삭제
                        parents(this, "[class$=-assignee]")[0].querySelector(".one-assignee").remove();

                        // table view에는 담당자 상세보기(.btn-more-assignInfo) 버튼(▼) prepend
                        if(datum["view"] === "table") {
                            parents(this, "[class$=-assignee]")[0].querySelector(".assignee-brief-box").prepend(btnMoreAssigneeInfo());
                        }

                        // 새 담당자 prepend
                        parents(this, "[class$=-assignee]")[0].querySelector(".assignee-brief-box").prepend(assigneeBoxByNum(2, datum["view"], newAssigneeMid, newAssigneeName));

                        // 기존 담당자 prepend
                        for(const entry of currAssignees){
                            parents(this, "[class$=-assignee]")[0].querySelector(".assignee-brief-box").prepend(assigneeBoxByNum(2, datum["view"], entry[0], entry[1]));
                        }
                    }

                    if (beforeCnt > 1) { // many → many
                        console.log("unassigned-memeber click: still many");
                        // console.log([...parents(this, "[class$=-assignee]")[0].querySelectorAll(".many-assignee")][beforeCnt-1]);
                        [...parents(this, "[class$=-assignee]")[0].querySelectorAll(".many-assignee")][beforeCnt-1].after(assigneeBoxByNum(2, datum["view"], newAssigneeMid, newAssigneeName));

                    }

                    // 담당자 풀네임 목록(.task-assignee-list)에 출력한다.
                    parents(this, ".more-assignInfo")[0].querySelector(".task-assignee-list").append(newAssigneeElement(newAssigneeMid, newAssigneeName, datum));

                    // table view의 경우, .more-assignInfo의 높이를 구하여
                    // 미배정 멤버 목록 div(.find-member-to-assign)의 top에 반영한다.
                    if (datum["view"] === "table") {
                        boxHeight = parents(this, ".more-assignInfo")[0].offsetHeight;
                        topValue = boxHeight*(0.689);
                        parents(this, ".find-member-to-assign")[0].style.top = topValue + 'px';
                    }

                    // 현재 담당자 목록 map에 추가한다.
                    currAssignees.set(newAssigneeMid, newAssigneeName);

                    // 미배정 멤버 목록 div(.unassigned-member-list)에서 삭제한다.
                    this.remove();

                } else {
                    response.json().then(warning => {
                        alert(warning["message"]);
                        if(warning["taskRemoved"]){
                            location.reload();
                        }
                    });
                }
            });

        } else {
            alert(`담당자는 6명을 초과할 수 없습니다.`);
        }
    });


    // 10-8. 미배정 멤버 '검색' 클릭 이벤트 (<< 담당자 추가하기/담당자관리)
    // table view: 담당자 상세보기 버튼(▼, .btn-more-assignInfo)을 클릭하면
    // status view: 담당자 관리(.btn-add-assignee)를 클릭하면
    // ☞ unassigned-member를 currUnassignedMembers map으로 받아둔다.
    if(elExists(document.querySelector(".btn-findByName-member-to-assign"))){
        const btnFindMemberByNameToAssign = document.querySelectorAll(".btn-findByName-member-to-assign");
        btnFindMemberByNameToAssign.forEach(function(btn){
            btn.addEventListener("click", ()=>{
                console.log(`담당자 추가 위해 멤버 검색 버튼 눌렀을 때`);
                console.log(`${prev(btn).value}`);
                const datum = btn.dataset;
                const searchName = prev(btn).value;

                // input에 값이 있을 때만 검색을 실행한다.
                // input에 아무 값이 들어오지 않았다면
                // status view: 검색을 실행하지 않는다.
                // table view: 미배정 멤버 목록을 출력한다.
                if (searchName !== "" || searchName.length !== 0) {

                    // table view: 미배정 멤버의 목록이 이미 노출되어 있다.
                    // 해당 요소를 찾아서 해당 요소만 출력되도록 한다.
                    if(datum["view"] === "table") {
                        /*
                        // 이전 검색결과 삭제
                        // const prevResult = [...parents(btn, ".more-assignInfo")[0].querySelector(".unassigned-member-list").children].filter(child => !child.classList.contains("hide"));
                        // console.log(prevResult)
                        // prevResult.remove();
                        */
                        // 검색에 사용할 정규표현식 생성
                        let regex = new RegExp(searchName, "gi");
                        console.log(regex);

                        // 1. 미배정 멤버 목록에서 검색한다.
                        let arr = Array.from(currUnassignedMembers, ([mid, name]) => ({mid, name}));
                        console.log(arr);
                        // arr의 결과 ↓
                        // 0: Object { mid: "32", name: "톰 하디" }
                        // 1: Object { mid: "33", name: "제임스 서버" }
                        // 2: Object { mid: "14", name: "공지철" }
                        // 3: Object { mid: "31", name: "서머싯 몸" }

                        const result = arr.filter(value => regex.test(value["name"]));
                        // console.log(result);
                        // console.log(result.length);
                        // 결과값 있을 때
                        // 0: Object { mid: "14", name: "공지철" } >> mid로 요소 찾는다!
                        // length: 1
                        // 결과값 없을 때: length 0

                        // 부모인 .find-member-to-assign의 자식 .unassigned-member-list
                        // ① 모든 멤버 hide
                        [...parents(btn, ".find-member-to-assign")[0].querySelector(".unassigned-member-list").children].forEach(member => member.classList.add("hide"));

                        // ② 검색된 멤버만 hide remove: unassignedMemberElement(datum, searchName);
                        if(result.length >= 1) {
                            for(let i = 0; i < result.length; i++) {
                                [...parents(btn, ".find-member-to-assign")[0].querySelector(".unassigned-member-list").children].filter(member => member.children[0].dataset.mid === result[i]["mid"])[0].classList.remove("hide");
                            }
                        }

                        if(result.length === 0) {
                            alert(`검색결과가 없습니다.`);
                        }

                    } // if table view ends

                    if(datum["view"] === "status") {
                        // 서버에 보낼 정보 생성
                        const searchCond = {
                            projectId: datum["projectid"],
                            taskId: datum["taskid"],
                            nickname: prev(btn).value
                        };

                        fetch(`http://ontrack-env.eba-mpbdgazx.ap-northeast-2.elasticbeanstalk.com/task/search?object=member`, {
                            method: 'POST',
                            headers: {
                                'Content-Type': 'application/json'
                            },
                            body: JSON.stringify(searchCond)
                        }).then(response => response.json())
                            .then(result => {
                                // console.log(result); // 결과가 없을 때: Array []
                                // console.log(result.length); // 결과가 없을 때: 0
                                // console.log(result[0]["id"]);
                                // Object { id: 9, taskTitle: null, authorMid: null, authorName: null, taskPriority: null, taskStatus: null, taskDueDate: null, taskParentId: null, createdAt: null, updatedAt: null, … }
                                // Array [ {…}, {…} ]
                                // 0: Object { id: 9, taskTitle: null, authorMid: null, … }
                                // 1: Object { id: 9, taskTitle: null, authorMid: null, … }
                                // length: 2



                                // 검색결과 출력 전, margin-bottom 준다.
                                if(!parents(btn, ".findByName-member-to-assign")[0].classList.contains("mb-10")){
                                    parents(btn, ".findByName-member-to-assign")[0].classList.add("mb-10");
                                }

                                // 부모인 .more-assignInfo의 자식 .unassigned-member-list
                                // ① hide제거
                                console.log(parents(btn, ".more-assignInfo")[0]);
                                console.log(parents(btn, ".more-assignInfo")[0].querySelector(".unassigned-member-list"));
                                parents(btn, ".more-assignInfo")[0].querySelector(".unassigned-member-list").classList.remove("hide");

                                // ② 다른 미배정 멤버 모두 숨긴다
                                // [...parents(btn, ".more-assignInfo")[0].querySelector(".unassigned-member-list").children].forEach(member => member.classList.add("hide"));

                                // ③ appendChild: unassignedMemberElement(datum, result);
                                // ③-1. 해당 이름이 미배정 멤버에 있다면
                                if(result.length !== 0) {
                                    for(let i = 0; i < result.length; i++) {
                                        parents(btn, ".more-assignInfo")[0].querySelector(".unassigned-member-list").appendChild(unassignedMemberElement(datum, result[i]));
                                    }
                                }
                                // ③-2. 해당 이름이 미배정 멤버에 없다면
                                if(result.length === 0) {
                                    const span = document.createElement("span");
                                    span.classList.add("font-blur", "font-12");
                                    span.innerText = "검색 결과가 없습니다.";
                                    parents(btn, ".more-assignInfo")[0].querySelector(".unassigned-member-list").appendChild(span);
                                }

                            }); // fetch ends
                    } // if status view ends

                } else { // 검색어를 입력하지 않고 '검색'버튼을 눌렀을 때

                    // table view에서는 미배정 멤버 목록을 출력한다.
                    if(datum["view"] === "table") {
                        [...parents(btn, ".find-member-to-assign")[0].querySelector(".unassigned-member-list").children].forEach(member => member.classList.remove("hide"));
                    }

                    // status view에서는 아무 것도 출력하지 않는다.
                    if(datum["view"] === "status") {
                        parents(btn, ".unassigned-member-list")[0].classList.add("hide");
                    }
                } // if (searchName !== "" || searchName !== null) ends




            });
        });
    } // // 10-8. 담당자 검색하기 끝

    /* 10-9. 할 일 진행상태 수정 */
    // 담당자가 없는 할 일은 진행상태를 변경할 수 없다.
    // 변경 버튼은 권한이 있다면 출력되게 하되, 실행과정에서 담당자가 없을 경우, 변경이 안됨을 경고하도록 하자.
    if(elExists(document.querySelector(".btn-edit-task-status"))){
        const btnEditTaskStatus = document.querySelectorAll(".btn-edit-task-status");
        btnEditTaskStatus.forEach(function(btn){
            btn.addEventListener("click", (e)=>{
                e.stopPropagation();
                console.log(`진행상태 변경을 click`);

                // 1) 진행상태 목록 toggle
                if(btn.querySelector(".tableView-status-list").classList.contains("img-hidden")) {
                    // 열려 있던 다른 task의 진행상태 목록들 닫고
                    document.querySelectorAll(".tableView-status-list").forEach(function(everyList){
                        everyList.classList.add("img-hidden");
                    });
                    // 선택된 목록만 연다
                    btn.querySelector(".tableView-status-list").classList.remove("img-hidden")
                } else {
                    btn.querySelector(".tableView-status-list").classList.add("img-hidden")
                }

                // 2) 바꾸려는 진행상태를 클릭했을 때
                btn.querySelectorAll(".status-each").forEach(function(chosenStatus){
                    chosenStatus.addEventListener("click", (e)=>{
                        e.stopImmediatePropagation();
                        // 변경 전 진행상태
                        const beforeStatus = btn.querySelector(".status-sign").dataset.status;
                        const datum = chosenStatus.dataset;
                        console.log(beforeStatus);
                        console.log(datum);
                        // console.log(btn.querySelector(".status-sign").classList[1]);
                        const taskHistory = {
                            projectId: datum["projectid"],
                            taskId: datum["taskid"],
                            modItem: "진행상태",
                            modType: "변경",
                            modContent: chosenStatus.querySelector("p").innerHTML,
                            updatedBy: datum["updatedby"]
                        };
                        console.log(taskHistory);

                        fetch(`http://ontrack-env.eba-mpbdgazx.ap-northeast-2.elasticbeanstalk.com/task/editTask?item=status&statusNum=${datum["status"]}`, {
                            method: 'POST',
                            headers: {
                                'Content-type': 'application/json'
                            },
                            body: JSON.stringify(taskHistory)
                        }).then(response => {
                            if(response.ok){
                                // 완료했던 일을 그 이전 상태로 바꾸거나, 완료중으로 바꿀 경우
                                // 담당자 추가/해제 설정이 바뀌어야 하므로 페이지 리로드 한다.
                                if(beforeStatus === "done" || chosenStatus.dataset.status === "5") {
                                    location.reload();
                                }
                                btn.querySelector(".status-remark").innerText = chosenStatus.querySelector("p").innerHTML;
                                btn.querySelector(".status-sign").classList.remove(beforeStatus);
                                btn.querySelector(".status-sign").dataset.status = chosenStatus.querySelector(".status-sign").classList[1];
                                btn.querySelector(".status-sign").classList.add(chosenStatus.querySelector(".status-sign").classList[1]);

                                // 열려있던 진행상태 목록 닫기
                                btn.querySelector(".tableView-status-list").classList.add("img-hidden")

                            } else {
                                const err = response.json();
                                console.log(err);
                                err.then(warning => {
                                    btn.querySelector(".tableView-status-list").classList.add("img-hidden")
                                    alert(warning["message"]);
                                    if(warning["taskRemoved"]) {
                                        location.reload();
                                    }
                                });
                            }
                        }); // fetch ends
                    });
                });

            }); // chosenOne click evt ends
        }); // btnEditTaskStatus.forEach ends
    } // 10-9 할 일 진행상태 수정 끝
    
    /* 10-10. 할 일 마감일 수정 */
    // 1) 캘린더div 출력
    let beforeDueDate = true; // 이전 마감일이 있는 것으로 기본 설정
    if(elExists(document.querySelector(".btn-edit-task-duedate"))){
        const btnEditTaskDueDate = document.querySelectorAll(".btn-edit-task-duedate");
        btnEditTaskDueDate.forEach(function(btn){
            btn.addEventListener("click", ()=>{
                // e.stopImmediatePropagation();
                console.log(`btn click!`);


                // console.log(btn.querySelector("span:nth-of-type(1)"));
                // 이전 마감일이 없었다면 false 저장
                if (btn.querySelector("span:nth-of-type(1)").classList.contains("font-blur") || btn.querySelector("span:nth-of-type(1)").classList.contains("no-dueDate")){
                    beforeDueDate = false;
                }

                if(btn.querySelector(".edit-task-duedate").classList.contains("hide")) {
                    // 열려 있는 다른 모든 캘린더 닫는다.
                    document.querySelectorAll(".edit-task-duedate").forEach(function(everyCal){
                        everyCal.classList.add("hide");
                        everyCal.classList.add("opacity0")
                    });

                    btn.querySelector(".edit-task-duedate").classList.remove("hide");
                    btn.querySelector(".edit-task-duedate").classList.remove("opacity0");
                    // btn.querySelector(".input-edit-task-duedate").showPicker();
                } else {
                    btn.querySelector(".edit-task-duedate").classList.add("hide");
                    btn.querySelector(".edit-task-duedate").classList.add("opacity0");
                }
            });
        });
    } // 1) 캘린더div 출력 ends

    // 2) 캘린더div와 btn분리 및 반영하기 버튼 이벤트
    if(elExists(document.querySelector(".edit-task-duedate"))){
        const divEditTaskDueDate = document.querySelectorAll(".edit-task-duedate");
        divEditTaskDueDate.forEach(function(div){
           div.addEventListener("click", (e)=>{
               e.stopPropagation();
               console.log(`캘린더 클릭됨`);
           });
        });
    } // 2) 캘린더div와 btn분리 및 반영하기 버튼 이벤트 ends

    // 3) 수정된 날짜 반영하기 버튼(.btn-submit-task-duedate) 클릭 이벤트
    if(elExists(document.querySelector(".btn-submit-task-duedate"))) {
        const btnSubmitTaskDueDate = document.querySelectorAll(".btn-submit-task-duedate");
        btnSubmitTaskDueDate.forEach(function(btn){
            btn.addEventListener("click", (e)=>{
                e.stopPropagation();
                console.log("btn submit (due date): clicked!");
                console.log(prev(btn).value); // 2024-07-24
                const datum = btn.dataset;

                if (!beforeDueDate && (prev(btn).value.length === 0 || prev(btn).value === "")) {
                    // 이전 마감일이 없고, 마감일도 없을 때
                    console.log(`이전 마감일이 없고, 마감일도 없을 때`);
                    return;
                }  // if empty string chk ends


                // 서버로 보낼 정보 생성
                const taskHistory = {
                    projectId: datum["projectid"],
                    taskId: datum["taskid"],
                    modItem: "마감일",
                    modType: "변경",
                    modContent: prev(btn).value,
                    updatedBy: datum["updatedby"]
                };
                console.log(taskHistory);

                // fetch
                fetch(`http://ontrack-env.eba-mpbdgazx.ap-northeast-2.elasticbeanstalk.com/task/editTask?item=dueDate`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(taskHistory)
                }).then(response => {
                    if(response.ok){
                        location.reload();
                    } else {
                        response.json().then(warning => {
                            alert(warning["message"]);
                            if(warning["taskRemoved"]){
                                location.reload();
                            }
                        });
                    }
                });

            });
        });

    } // 3) 수정된 날짜 반영하기 버튼(.btn-submit-task-duedate) 클릭 이벤트 ends


    /*---------- 053A ------------*/
    // 추가된 담당자의 full name 요소 (table view: ①목록에서 담당자 추가, ②참여하기로 추가)
    function newAssigneeElement(newAssigneeMid, newAssigneeName, datum) {
        const div = document.createElement("div");
        const p = document.createElement("p");
        const btnX = document.createElement("span");

        div.classList.add("assignee-fullname-box", "mb-7");
        p.classList.add("div-inline-block", "mb-5");
        btnX.classList.add("btn-dropOut-task", "ml-3"); // btn-cancel-assign-task

        p.setAttribute("data-mid", newAssigneeMid);

        btnX.setAttribute("data-assigneemid", newAssigneeMid);
        btnX.setAttribute("data-executormid", datum["executormid"]); // table view: 작성자가 아닌 담당자가 참여해제할 경우, reload한다.
        btnX.setAttribute("data-authormid", datum["authormid"]); // table view: 작성자가 아닌 담당자가 참여해제할 경우, reload한다.
        btnX.setAttribute("data-taskid", datum["taskid"]);
        btnX.setAttribute("data-projectid", datum["projectid"]);
        btnX.setAttribute("data-view", datum["view"]);

        p.innerText = newAssigneeName;
        btnX.innerHTML = "&times;";
        div.appendChild(p);
        div.appendChild(btnX);
        // console.log(executorMid);
        // console.log(newAssigneeMid);
        // console.log(authorMid);
        // if(executorMid === newAssigneeMid || authorMid === executorMid) {
        //     div.appendChild(btnX);
        // }
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
    function unassignedMemberElement(datum, result){
        const div = document.createElement("div");
        const p = document.createElement("p");

        div.classList.add("unassigned-member");
        p.classList.add("div-inline-block");

        div.setAttribute("data-executormid", datum["executormid"]);
        div.setAttribute("data-authormid", datum["authormid"]);
        div.setAttribute("data-taskid", datum["taskid"]);
        div.setAttribute("data-projectid", datum["projectid"]);


        if(datum["view"] === "status") {
            p.setAttribute("data-mid", result["assigneeMid"]);
            p.innerText = result["assigneeName"];
        }
        if(datum["view"] === "table") {
            p.setAttribute("data-mid", result[0]);
            p.innerText = result[1];
        }
        div.appendChild(p);

        return div;
    }
    /*---------- 057 ------------*/
    /* 담당자 추가할 때 담당자 이름 (요약) 출력 요소 생성 */
    // ★★★ 새로 생성된 btn-more-assignInfo는 -new를 붙인다. (기능이 겹쳐서 기존 버튼도 안열..림?)
    function assigneeBoxByNum(assigneeNum, view, assigneeMid, assigneeName){
        const span = document.createElement("span");
        const div = document.createElement("div");

        if (assigneeNum === 0) {
            span.classList.add("no-assignee", "font-14");
            if (view === "table") span.classList.add("btn-more-assignInfo-new");
            span.innerText = "담당자 없음";
            return span;
        } else if (assigneeNum === 1) {
            span.classList.add("one-assignee", "font-14");
            if (view === "table") span.classList.add("btn-more-assignInfo-new");
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
        document.querySelector("#container-task-detail .modal-Right").classList.add("slide-side");
    }

    /*---------- 067 ------------*/
    /* 삭제된 할 일 상세 열려고 했을 때 출력된 경고창 닫기 */
    if(elExists(document.querySelector(".btn-alert-task-removed"))){
        document.querySelector(".btn-alert-task-removed").addEventListener("click", ()=>{
            document.querySelector(".alert-task-removed").classList.add("hide");
        });
    }
    /*---------- 012 ------------*/
    /* 할 일 상세 모달 탭버튼 클릭*/
    if(elExists(document.querySelector(".btn-modal-task-tab"))){
        let modalTaskTabChosen;
        const btnTaskTabs = document.querySelectorAll(".btn-modal-task-tab");
        const modalTaskTabs = document.querySelectorAll(".modal-task-tab");
        // const modalTaskCommonArea = document.querySelector("#task-detail-common");

        btnTaskTabs.forEach(function(chosenTab){
            chosenTab.addEventListener("click", ()=>{
                // 1. 선택된 탭버튼 표시
                chosenTab.classList.add("task-tab-chosen");
                modalTaskTabChosen = chosenTab.id;
                // console.log(modalTaskTabChosen);
                [...chosenTab.parentElement.children].filter((child) => child.id !== chosenTab.id).forEach(function(others){
                    others.classList.remove("task-tab-chosen");
                });
                // 2. 선택된 탭 표시
                modalTaskTabs.forEach(function(eachtab){
                    eachtab.classList.add("hide");
                    if (eachtab.classList.contains(modalTaskTabChosen)) {
                        eachtab.classList.remove("hide");
                        document.querySelector("#task-detail-common").classList.remove("hide");
                    }
                });
            });
        }); // 할 일 상세 모달 탭버튼 클릭 끝
    }

    /*---------- 013 ------------*/
    /* 할 일 나누기(create child task) - 세부(하위)항목 */
    const btnOpenModalCreateChildTask = document.querySelectorAll(".btn-create-child-task");
    const modalCreateTaskParentTitle = document.querySelector(".modal-create-task-parent-title");
    const iconChildTaskTitle = document.querySelector(".icon-child-task");
    const containerCreateTask = document.querySelector("#container-create-task");
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
            modalNotice.querySelector(".modal-Right-noslide").classList.remove("hide");

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
            modalNotice.querySelector(".modal-Right-noslide").classList.add("hide");
        });
    });




    /*---- ▲ 열고닫기 끝 ▲ ----*/
    /*************************/

    /*---------- 017 ------------*/




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
    let chosenAssigneeMids = [];
    // 배정된 담당자 nick name array
    let choosenAssigneeNames = [];

    // 프로젝트 멤버 목록에서 담당자 선택
    assigneesName.forEach(function(chosenName){
        chosenName.addEventListener("click", ()=>{
            // 해당 input에 선택됨 표시(class="chosen")
            // console.log(this); // html...
            console.log(chosenName); // <input id="assignee1" class="hide" type="checkbox" name="taskAssignee" value="박종건">
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
    
    const createTaskAttachedFiles = document.querySelector("#create-task-attach-file"); /*input*/
    const createTaskFiles = document.querySelector("#create-task-files");
    const createTasknofile = document.querySelector("#no-file-placeholder");
    let cntFiles = 0;
    let rewriteCreateTaskFileList = [];
    // let createTaskFileDelCnt = 0;

    createTaskAttachedFiles.addEventListener("change", ()=>{

        const files = createTaskAttachedFiles.files;
        console.log(`첨부 직후(유효성 검사 전)`);
        console.log(files);
        let invalidFile = new Map();
        if(files.length < 6){
            for (let i = 0; i < files.length; i++) {
                const name = files[i].name.substring(0, files[i].name.lastIndexOf("."));
                const type = files[i].name.substring(files[i].name.lastIndexOf(".")+1);
                const result = validateFile(files[i].name, parseInt(files[i].size));
                if (result === 'okay') {
                    console.log(`파일 형식, 이름 검사 후`);
                    createTasknofile.classList.remove("hide");
                    createTaskFiles.appendChild(createAndAttachFileBox(name, type, returnFileSize(parseInt(files[i].size))));
                    cntFiles++;
                    rewriteCreateTaskFileList.push(files[i]);
                    // console.log(rewriteCreateTaskFileList);
                    // rewriteCreateTaskFileList.forEach(file => {
                    //     console.log(rewriteCreateTaskFileList.indexOf(file));
                    //     console.log(file["name"]); // 100자.txt
                    // });
                } else {
                    invalidFile.set(name, result);
                }
            }

            if(invalidFile.size !== 0){
                let notAttachList = new Map();
                let warning = [];
                invalidFile.forEach((v, k) => {
                    if(v.includes('t')){
                        warning.push("첨부가 불가능한 유형의 파일입니다.");
                    }
                    if(v.includes('n')){
                        warning.push(`파일명에는 특수기호와 여백이 포함될 수 없습니다.<br/> (단, +, _, -, .은 포함가능)`);
                    }
                    if(v.includes('s')){
                        warning.push("5MB이하의 파일만 첨부할 수 있습니다.");
                    }
                    notAttachList.set(k, warning);
                    warning = [];
                });

                if(elExists(document.querySelector(".alert-not-attach-container"))){

                    const modalNotAttachGuide = document.querySelector(".alert-not-attach-container");
                    modalNotAttachGuide.classList.remove("hide");

                    for(const entry of notAttachList){
                        modalNotAttachGuide.querySelector(".alert-not-attach-scroll").appendChild(notAttachedGuide(entry));
                    }

                    document.querySelector("#btn-alert-not-attach").addEventListener("click", ()=>{
                        // 안에 내용 비운다.
                        [...modalNotAttachGuide.querySelector(".alert-not-attach-scroll").children].forEach(function(item){
                            item.remove();
                        });

                        // 창 닫는다.
                        modalNotAttachGuide.classList.add("hide");
                    });
                }
            }

        } else {
            alert(`파일 첨부는 5개 이하로 가능합니다.`);
        }


        if (cntFiles !== 0) {
            createTasknofile.classList.add("hide");
        }
    }); // 할 일 추가 모달: 파일 첨부 끝

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
        fileType.classList.add("font-14", "modal-file-type", "mr-10");
        fileSize.className = "modal-file-size";
        btnDel.className = "btn-del-file";
        
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
    // 모달(할 일): 서버 전송 전, 첨부했던 파일 삭제
    // createAndAttachFileBox 안의 btnDel을 클릭했을 때
    onEvtListener(document, "click", ".btn-del-file", function(){

        cntFiles--;
        const idx = [...parents(this, "#create-task-files")[0].children].indexOf(parents(this, ".modal-create-and-attach-file")[0]); // [...this.parentNode.parentNode.children].indexOf(this.parentNode);
        rewriteCreateTaskFileList.splice(idx-1, 1); // p요소(첨부된 파일이 없습니다)로 인해 인덱스가 하나씩 밀려있다!

        // 삭제된 파일box 삭제
        this.parentElement.remove();

        // 첨부된 파일이 0개라면 '첨부된 파일이 없습니다' 문구 출력
        if(cntFiles === 0){
            createTasknofile.classList.remove("hide");
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
        // ver.1. 파일명 안에 특수기호는 +, _, -, .만 허용
        const regFileName = /[{}\[\]()\/?,'";:`~<>!@#$%^&*=|\s]/g;
        // ver.2. 윈도우가 허하지 않는 특수기호만 잡는 표현식 (\ / : * ? < > | 9개)
        // const regWinFileName = /[\\/:*?"<>|]/g;
        const regFileType = /(ade|adp|apk|appx|appxbundle|aspx|bat|bin|cab|cmd|com|cpl|dll|dmg|exe|gadget|hta|inf1|ins|ipa|iso|isp|isu|jar|js|jse|jsp|jsx|lib|lnk|mde|msc|msi|msix|msixbundle|msp|mst|nsh|paf|pif|ps1|reg|rgs|scr|sct|sh|shb|shs|sys|tar|u3p|vb|vbe|vbs|vbscript|vxd|ws|wsc|wsf|wsh)$/i;

        const filename = fullfilename.substring(0, fullfilename.lastIndexOf("."));
        const filetype = fullfilename.substring(fullfilename.lastIndexOf(".")+1);
        
        // 정규표현식에 있는 특수기호/공백/문자와 일치하는 글자가 파일명에 있을 경우 true 반환됨
        const nameResult = regFileName.test(filename)? 'n': 'ok';
        const typeResult = regFileType.test(filetype)? 't': 'a';
        const sizeResult = filesize > 5242880 ? 's': 'y';


        
        return (nameResult + typeResult + sizeResult);
    }

    /*---- ▲ Modal(Create Task;할일추가) 파일첨부 끝 ▲ ----*/

    /*---------- 029 ------------*/
    /*---- ▼ Modal(Create Task;할 일 추가): submit 시작 ▼ ----*/
    const modalCreateTask = document.querySelector("#container-create-task");
    const btnCreateTaskSubmit = document.querySelector("#create-task-submit"); // button

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
        // // console.log(addTaskForm.elements.assigneesMid); // RadioNodeList { 0: input#4.hide.chosen,
        // 1: input#14.hide.chosen, 2: input#26.hide.chosen, 3: input#27.hide,
        // 4: input#28.hide, value: "", length: 5 }
        //

        addTaskForm.elements.assigneeMids.forEach(function(eachOne){
            if(eachOne.classList.contains("chosen")) {
                console.log(`배정된 담당자 id: ${eachOne.id}, 배정된 담당자 nickname: ${eachOne.dataset.nickname}`);
                chosenAssigneeMids.push(eachOne.id);
                choosenAssigneeNames.push(eachOne.dataset.nickname);
            }
        });
        // 파일 첨부 수정 여부와 상관 없이, rewriteCreateTaskFileList를 사용한다.
        // ∵ 파일 첨부 이후 추가했을 때, 새로 추가된 파일만 formData에 남게 된다.(이전 파일을 덮어버린다..)
        for(let i = 0; i < rewriteCreateTaskFileList.length; i++) {
            addTaskData.append("taskFiles", rewriteCreateTaskFileList[i]);
        }

        // 부분 삭제가 이뤄졌을 때만 새로운 array가 가고 아닐 경우, 기존의 fileList가 전송된다.
        /*
        if (createTaskFileDelCnt > 0) {
            console.log(`파일 부분 삭제 후: `);
            console.log(rewriteCreateTaskFileList);
            for(let i = 0; i < rewriteCreateTaskFileList.length; i++) {
                addTaskData.append("taskFiles", rewriteCreateTaskFileList[i]);
            }

        } else {
            console.log(addTaskForm.elements.taskFile.files);
            const files = addTaskForm.elements.taskFile.files;
            console.log(files);
            for(let i = 0; i < files.length; i++){
                console.log(`addTaskData에 파일 첨부 `);
                console.log(files[i]);
                addTaskData.append("taskFiles", files[i]);
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
        */
        console.log(`==== 파일의 append가 된 후 FormData ====`);
        console.log(addTaskData);

        addTaskData.append("projectId", addTaskForm.elements.projectId.value);
        addTaskData.append("authorMid", addTaskForm.elements.authorMid.value);
        addTaskData.append("authorName", addTaskForm.elements.authorName.value);
        addTaskData.append("taskTitle", addTaskForm.elements.taskTitle.value);
        addTaskData.append("taskPriority", addTaskForm.elements.taskPriority.value);
        addTaskData.append("taskDueDate", addTaskForm.elements.taskDueDate.value);
        addTaskData.append("assigneeMids", chosenAssigneeMids);
        addTaskData.append("assigneeNames", choosenAssigneeNames);

        //
        console.log(`------- addTaskData -------`);
        console.log(addTaskData);


        fetch('http://ontrack-env.eba-mpbdgazx.ap-northeast-2.elasticbeanstalk.com/task', {
            method:'POST',
            headers: {},
            body: addTaskData
        }).then(response => {
            // afterAddTaskSubmit();
            addTaskForm.reset(); // 파일첨부: 콘솔에는 length 0으로 찍힘. 담당자: 콘솔과 화면 모두 reset 필요

            // 선택했던 담당자 input 모두 해제
            [...addTaskForm.elements.assigneeMids].forEach(function(eachOne){
                eachOne.classList.remove("chosen");
            });

            // 담당자 담은 array clear (data to controller)
            chosenAssigneeMids = [];
            choosenAssigneeNames = [];

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
            // createTaskFileDelCnt = 0;
            rewriteCreateTaskFileList = [];

            // 모달 닫기
            modalCreateTask.classList.add("hide");

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
        [...addTaskForm.elements.assigneeMids].forEach(function(eachOne){
            eachOne.classList.remove("chosen");
        });

        // 담당자 담은 array clear (data to controller)
        chosenAssigneeMids = [];
        choosenAssigneeNames = [];

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
        // createTaskFileDelCnt = 0;
        rewriteCreateTaskFileList = [];

        // 모달 닫기
        modalCreateTask.classList.add("hide");
    }

    /*---------- 031 ------------*/
    /* 할 일 추가 모달 닫기 버튼 */
    /*--- 닫기 전에 묻는 창 추가 요망? ---*/
    const btnCloseModalCreateTask = document.querySelector(".btn-close-modal-createTask");
    
    btnCloseModalCreateTask.addEventListener("click", ()=>{
        // afterAddTaskSubmit();
        addTaskForm.reset(); // 파일첨부: 콘솔에는 length 0으로 찍힘. 담당자: 콘솔과 화면 모두 reset 필요

        console.log(addTaskForm); // <form id="form-create-task" action="/task" method="POST" enctype="multipart/form-data">
        console.log(addTaskForm.elements); // HTMLFormControlsCollection { 0: input#projectId,
        // 1: input#authorMid, 2: input, 3: input.modal-add-task,
        // 4: input#vip.input-radio.hide, 5: input#ip.input-radio.hide,
        // 6: input#norm.input-radio.hide, 7: input.altivo-light, 8: input#34.hide,
        // 9: input#create-task-attach-file.opacity0.wh0, … }

        console.log(addTaskForm.elements.assigneeMids);
        console.log(Array.of(addTaskForm.elements.assigneeMids));
        console.log(Array.of(addTaskForm.elements));
        // <input id="34" class="hide" type="checkbox" name="assigneeMids" value="34" data-nickname="busmoja">
        const inputs = addTaskForm.elements.assigneeMids;
        // 선택했던 담당자 input 모두 해제
        Array.of(addTaskForm.elements.assigneeMids).forEach(function(eachOne){
            eachOne.classList.remove("chosen");
        });

        // 담당자 담은 array clear (data to controller)
        chosenAssigneeMids = [];
        choosenAssigneeNames = [];

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
        // createTaskFileDelCnt = 0;
        rewriteCreateTaskFileList = [];

        // 모달 닫기
        modalCreateTask.classList.add("hide");
    });


    /*---------- 032 ------------*/

    /*---------- 033 ------------*/

    /*---------- 034 ------------*/

    /*---------- 035 ------------*/

    /*---------- 036 ------------*/




    /*---------- 037 ------------*/
    /*---- ▼ Modal(Task comment)소통하기 - 글 등록 ▼ ----*/
    if(elExists(document.querySelector("#btn-submit-comment"))) {

        const btnSubmitComment = document.querySelector("#btn-submit-comment");
        let commentType = "normal";
        // 모두 확인 요청 시 요소 변화 + task-comment-type: Required Reading
        // 모두 확인 요청 (radio)버튼 클릭 시 css 변경

        /*
        const commentWriteBox = document.querySelector(".modal-task-comment-write");
        const commentNoticeBtn = document.querySelector("#task-comment-notice");



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
        }); */

        /*---------- 038 ------------*/
        // 글 등록
        const newCommentContent = document.querySelector("textarea#task-comment-write-content");

        btnSubmitComment.addEventListener("click", ()=>{

            console.log(newCommentContent);
            const content = newCommentContent.value;
            const datum = btnSubmitComment.dataset;
            const date = new Date();
            console.log(datum["authorized"]);
            if(datum["authorized"] === "true"){ // 담당자와 작성자만 글을 등록할 수 있다.
                // 서버에 보낼 정보 입력
                // (댓글확인상태는 취소: ★★ comment_check에도 작성자는 확인함으로 입력해야 한다.)
                // ★★ 작성한 글을 바로 출력할 것이기 때문에 작성한 시간을 javascript에서 보내줘야 한다.
                const taskDetailRequest = {
                    taskId: datum["taskid"],
                    projectId: datum["projectid"],
                    authorMid: datum["authormid"],
                    authorName: datum["authorname"],
                    commentType: commentType,
                    comment: content,
                    createdAt: dateYYMMDD(date) + timeHHMM(date),
                };

                console.log(taskDetailRequest);
                newCommentContent.value = "";
                // Object { taskId: "8", projectId: "9", authorMid: "14", authorName: "공지철",
                // type: "normal", comment: "dateDay(date)+timeHHMM(date)",
                // createdAt: "24.07.19 13:43", updatedAt: "24.07.19 13:43" }


                // 서버에 보내기 (작성자, 글내용, 타입(RR일 경우, 확인cnt=1(작성자id, check True)), 작성일시)
                fetch(`http://ontrack-env.eba-mpbdgazx.ap-northeast-2.elasticbeanstalk.com/task/comment?type=add`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(taskDetailRequest)
                }).then(response => {
                    if(response.ok){
                        response.text().then(commentId => {
                            // 등록된 글이 없었을 경우, '등록된 글이 없습니다' 요소 삭제
                            if(elExists(parents(btnSubmitComment, "#container-task-detail")[0].querySelector("p.no-comment"))) {
                                parents(btnSubmitComment, "#container-task-detail")[0].querySelector("p.no-comment").remove();
                            }

                            // 화면에 출력
                            const commentArea = document.querySelector("#task-tab-comment-list");
                            commentArea.prepend(createCommentBox(datum["authorname"], commentType, content, date, commentId));

                            // web socket
                            // commentWs.commentSocket.send(commentId);

                        });
                    } else {
                        response.json().then(warning => {
                            alert(warning["message"]);
                            if(warning["taskRemoved"]){
                                location.reload();
                            }
                        });
                    }
                }) // fetch 끝
            } else {
                newCommentContent.value = "";
                alert(`해당 할 일의 담당자와 작성자만 소통글을 등록할 수 있습니다.`);
            }

        }); // 클릭이벤트 끝
    }


    /*---------- 059 날짜형태로 반환(YY.MM.DD (요일) hh:mm ------------*/
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
    // 작성한 소통하기 글 바로 출력할 때 사용할 function
    function createCommentBox(writer, type, content, date, commentId) {
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


        // 1-1에 appendChild (2개)
        commentWriterNNoticeBox.appendChild(commentWriter);


        // 1-2. 작성일시+tool (2개요소 appendChild)
        const commentDateNToolBox = document.createElement("div");
        commentDateNToolBox.classList.add("mr-15", "flex-row-justify-start-align-center");

        // 1-2-1. 작성일시
        const commentDate = document.createElement("span");
        commentDate.classList.add("altivo-regular", "mr-5");
        commentDate.innerText = dateYYMMDD(date);
        // commentDate.innerText += dateDayEng(date);
        commentDate.innerText += timeHHMM(date);

        // 1-2-2. tool(수정/삭제) 버튼과 그에 따른 모달 2개 (3개요소 appendChild)
        const commentToolBox = document.createElement("div");
        commentToolBox.classList.add("btn-comment-edit-del");
        commentToolBox.setAttribute("data-commentid", commentId);

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
        commentDelConfirmY.setAttribute("data-commentid", commentId);
        commentDelConfirmY.setAttribute("data-deletedby", 'author');
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
        const commentEditTimeNBtnBox = document.createElement("div");
        commentEditTimeNBtnBox.classList.add("flex-row-between-nowrap");

        const commentEditTimeBox = document.createElement("div");
        commentEditTimeBox.classList.add("flex-row-justify-start-align-center", "time-of-edit", "img-hidden");

        const commentEditTimeSpan = document.createElement("span");
        commentEditTimeSpan.classList.add("font-12", "altivo-light", "time-of-edit");
        const commentEditTimeSignSpan = document.createElement("span");
        commentEditTimeSignSpan.classList.add("font-12", "edit-sign");
        commentEditTimeSignSpan.innerText = "(수정됨)";

        commentEditTimeBox.appendChild(commentEditTimeSpan);
        commentEditTimeBox.appendChild(commentEditTimeSignSpan);

        commentEditTimeNBtnBox.appendChild(commentEditTimeBox);

        const commentEditApplyBtnBox = document.createElement("div");
        const commentEditSubmitBtn = document.createElement("span");
        const commentEditCancelBtn = document.createElement("span");

        commentEditApplyBtnBox.classList.add("flex-row-end-center-nowrap", "confirm-edit", "hide");
        commentEditSubmitBtn.classList.add("btn-comment-edit-submit");
        commentEditCancelBtn.classList.add("btn-comment-edit-cancel");

        commentEditSubmitBtn.setAttribute("data-taskid", commentId);

        commentEditSubmitBtn.innerText = "등록";
        commentEditCancelBtn.innerText = "취소";

        commentEditApplyBtnBox.appendChild(commentEditSubmitBtn);
        commentEditApplyBtnBox.appendChild(commentEditCancelBtn);

        commentEditTimeNBtnBox.appendChild(commentEditApplyBtnBox);

        // 0. container에 부착 (3개요소)
        commentBox.appendChild(commentInfoBox);
        commentBox.appendChild(commentContentBox);
        commentBox.appendChild(commentEditTimeNBtnBox);

        return commentBox;

        // 하다 만 나에게 박수...
        // 결국엔 이 function을 사용하게 된 나에게도 박수..
    }

    /*---- ▲ Modal(Task comment)소통하기 - 글 등록 끝 ▲ ----*/

    /*---------- 040 ------------*/
    /*---- ▼ Modal(Task comment)소통하기 - (자신의 글) 수정/삭제 ▼ ----*/

    // 소통하기 - (자신의 글)설정 버튼 눌렀을 때(수정하기/삭제하기 버튼 출력)
    let commentId;
    onEvtListener(document, "click", ".btn-comment-edit-del", function(){
        this.querySelector(".click-comment-edit-del").classList.toggle("img-hidden");
        commentId = this.dataset.commentid;
        // console.log(commentId);
    });

    /*---------- 041 ------------*/
    // 소통하기 - (자신의 글)수정하기 버튼 눌렀을 때
    onEvtListener(document, "click", ".btn-comment-edit", function(){
        const btn3dot = parents(this, ".btn-comment-edit-del")[0]; // .parentElement.parentElement
        btn3dot.classList.add("hide");

        console.log(commentId);

        const btnEdits = parents(this, ".modal-task-comment-read")[0].querySelector(".confirm-edit"); // chosenBtn.parentNode.parentNode.parentNode.parentNode.parentNode.children[2];
        btnEdits.classList.remove("hide");

        const textarea = parents(this, ".modal-task-comment-read")[0].querySelector("textarea"); // chosenBtn.parentNode.parentNode.parentNode.parentNode.parentNode.children[1].children[0];
        const beforeEdit = textarea.value;
        textarea.removeAttribute("readonly");
        textarea.focus();
        textarea.classList.add("border-editable");

        // 수정한 내용 등록·취소 버튼 클릭 시
        const btnSubmitEdit = parents(this, ".modal-task-comment-read")[0].querySelector(".btn-comment-edit-submit"); // chosenBtn.parentNode.parentNode.parentNode.parentNode.parentNode.children[2].children[0].children[0];
        const btnCancelEdit = parents(this, ".modal-task-comment-read")[0].querySelector(".btn-comment-edit-cancel"); // chosenBtn.parentNode.parentNode.parentNode.parentNode.parentNode.children[2].children[0].children[1];

        // 수정한 내용 등록
        btnSubmitEdit.addEventListener("click", ()=>{
            // const datum = btnSubmitEdit.dataset;
            const date = new Date();
            // 서버에 보낼 정보
            const taskDetailRequest = {
                commentId: commentId,
                comment: textarea.value,
                modifiedAt: dateYYMMDD(date) + timeHHMM(date)
            };

            console.log(taskDetailRequest);

            // fetch
            fetch(`http://ontrack-env.eba-mpbdgazx.ap-northeast-2.elasticbeanstalk.com/task/comment?type=edit`, { //${datum["taskid"]}/
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(taskDetailRequest)
            }).then(response => {
                if(response.ok){
                    // 3dot 버튼 나타내기
                    btn3dot.classList.remove("hide");

                    // 수정됨 div 나타내고, 수정 시간 입력
                    parents(this, ".modal-task-comment-read")[0].querySelector("div.time-of-edit").classList.remove("img-hidden");
                    parents(this, ".modal-task-comment-read")[0].querySelector("span.time-of-edit").innerText = dateYYMMDD(date) + timeHHMM(date);
                    parents(this, ".modal-task-comment-read")[0].querySelector(".edit-sign").classList.remove("hide");

                    // 등록/취소 버튼이 담긴div 숨기기
                    btnEdits.classList.add("hide");

                    // let afterEdit = textarea.value;
                    // textarea.value = afterEdit;
                    textarea.setAttribute("readonly", "readonly");
                    textarea.classList.remove("border-editable");
                } else {
                    response.json().then(warning => {
                        alert(warning["message"]);
                        if(warning["taskRemoved"]){
                            location.reload();
                        }
                    });
                }
            });

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
    }); // 소통하기 - (자신의 글)수정하기 버튼 눌렀을 때 끝


    /*---------- 042 ------------*/
    // 소통하기 - (자신의 글)삭제하기 버튼 눌렀을 때
    onEvtListener(document, "click", ".btn-comment-del", function(){
        const confirmBox = parents(this, ".btn-comment-edit-del")[0].querySelector(".popupYN"); //chosenBtn.parentElement.parentElement.children[2];
        confirmBox.classList.remove("img-hidden");
        const myComment = parents(this, ".modal-task-comment-read")[0];// chosenBtn.parentElement.parentElement.parentElement.parentElement.parentElement;
        const btnYes = parents(this, ".modal-task-comment-info-box")[0].querySelector(".btn-confirm-yes");// chosenBtn.parentElement.parentElement.children[2].children[1].children[0];
        const btnNo = parents(this, ".modal-task-comment-info-box")[0].querySelector(".btn-confirm-no");// chosenBtn.parentElement.parentElement.children[2].children[1].children[1];
        // let cntComment = parents(this, ".modal-task-comment-read")[0].children.length;
        let cntComment = parents(this, "#task-tab-comment-list")[0].querySelectorAll(".modal-task-comment-read").length;
        console.log(cntComment);
        btnYes.addEventListener("click", (e)=>{
            e.stopPropagation();
            const data = btnYes.dataset;
            cntComment--;
            console.log(`삭제 후 소통글 개수: ${cntComment}`);
            if(btnYes.dataset.deletedby === 'author'){ // 작성자에 의해 삭제
                console.log("작성자에 의한 소통글 삭제");
                // 서버 요청

                fetch(`http://ontrack-env.eba-mpbdgazx.ap-northeast-2.elasticbeanstalk.com/task/comment?cId=${data["commentid"]}`, {
                    method: 'DELETE'
                }).then(response => {
                    if(response.ok){
                        confirmBox.classList.add("img-hidden");
                        if(cntComment === 0) {
                            const pNoComment = document.createElement("p");
                            pNoComment.classList.add("no-comment");
                            pNoComment.innerText = "등록된 글이 없습니다.";
                            parents(this, "#task-tab-comment-list")[0].prepend(pNoComment);
                        }
                        myComment.remove();
                    } else {
                        alert(`글 삭제가 완료되지 않았습니다.`);
                    }
                });
            } else { // 관리자에 의해 삭제
                console.log("관리자에 의한 소통글 삭제");

                // 서버 요청
                const taskDetailRequest = {
                    commentId: data["commentid"],
                    blockedBy: data["deletedby"]
                }

                fetch(`http://ontrack-env.eba-mpbdgazx.ap-northeast-2.elasticbeanstalk.com/task/comment?type=blocked`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(taskDetailRequest)
                }).then(response => {
                    if(response.ok){
                        confirmBox.classList.add("img-hidden");
                        if(cntComment === 0) {
                            const pNoComment = document.createElement("p");
                            pNoComment.classList.add("no-comment");
                            pNoComment.innerText = "등록된 글이 없습니다.";
                            parents(this, "#task-tab-comment-list")[0].prepend(pNoComment);
                        }
                        myComment.remove();
                    } else {
                        response.json().then(warning => {
                            alert(warning["message"]);
                            if(warning["taskRemoved"]){
                                location.reload();
                            }
                        });
                    }
                });
            }

        });
        btnNo.addEventListener("click", (e)=>{
            e.stopPropagation();
            confirmBox.classList.add("img-hidden");
        });

    });

    /*---- ▲  Modal(Task comment)소통하기 - (자신의 글) 수정/삭제 끝 ▲ ----*/


    /*---------- 066 ------------*/
    // 할 일 상세: 중요 소통하기 '미확인'버튼 클릭 시
    if(elExists(document.querySelector(".notice-chk"))){
        const btnNoticeCommentChk = document.querySelectorAll(".notice-chk");
        btnNoticeCommentChk.forEach(function(chkBtn){
            chkBtn.addEventListener("click", ()=>{
                console.log(`중요소통 미확인 버튼 클릭`);
                const data = chkBtn.dataset;
                console.log(data);
            });
        });
    }


    /*---- ▼ Modal(File): 파일 시작 ▼ ----*/
    /*---------- 043 ------------*/
    // 이미 생성된 할일의 파일 추가 1/2: Drag and Drop
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

        const datum = ModalTaskFileDropZone.dataset;
        console.log(datum);
        // DOMStringMap(4) { taskid → "8", projectid → "9", uploadername → "공지철", uploademid → "14" }

        if(datum["authorized"] === "true"){ // 담당자나 작성자만 파일 업로드가 가능하다.
            if(ModalTaskFileDropZone.classList.contains("dropzone")){
                ModalTaskFileDropZone.classList.remove("dragover");
                console.log(e.dataTransfer.files);
                // FileList(3) [ File, File, File ] length: 3
                // 0: File { name: "backup_0404.txt", lastModified: 1712237338042, size: 4711, … }
                // lastModified: 1712237338042
                // name: "backup_0404.txt"
                // size: 4711
                // type: "text/plain"
                // webkitRelativePath: ""
                // 1: File { name: "backup_0405.txt", lastModified: 1712319431021, size: 2500, … }

                const fileData = new FormData();
                const file = e.dataTransfer.files;
                let invalidateFile = new Map();
                if(e.dataTransfer.files.length < 6){
                    for(let i = 0; i < e.dataTransfer.files.length; i++){

                        // 파일 유형 검사 후 append
                        if(validateFile(file[i].name, parseInt(file[i].size)) === 'okay') {
                            fileData.append("files", file[i]); // e.dataTransfer.files[i]
                        } else {
                            invalidateFile.set(files[i].name, validateFile(file[i].name, parseInt(file[i].size))); // e.dataTransfer.
                        }

                    }
                } else {
                    alert(`파일 첨부는 5개 이하로 가능합니다.`);
                }


                // fetch
                fetch(`http://ontrack-env.eba-mpbdgazx.ap-northeast-2.elasticbeanstalk.com/task/file?pId=${datum["projectid"]}&tId=${datum["taskid"]}&mId=${datum["uploadermid"]}`, { // /upload
                    method: 'POST',
                    headers: {},
                    body: fileData
                }).then(response => {
                    if(response.ok){
                        const serverResult = response.json();
                        serverResult.then(data => {
                            console.log(data)
                            console.log(data.length)
                            console.log(data[0]["id"])
                            //  Array [ {…} ]
                            //
                            /**
                             * 0: Object { id: 2, projectId: 9, memberId: 14, … }
                             * createdAt: "2024-08-04T19:35:06"
                             * fileNewName: null
                             * fileOrigName: "원래파일이름"
                             * filePath: null
                             * fileSize: 24
                             * fileType: "txt"
                             * formattedFileSize: null
                             * id: 2
                             * memberId: 14
                             * projectId: 9
                             * taskId: null
                             * uploaderName: null*/


                            // 만약 '등록된 파일이 없습니다' 문구가 있다면 삭제
                            if(parents(ModalTaskFileDropZone, "#container-task-detail")[0].querySelector("p.no-file")){
                                [...parents(ModalTaskFileDropZone, "#container-task-detail")[0].querySelectorAll("p.no-file")].filter(sign => sign.dataset.taskid === datum["taskid"])[0].remove();
                            }

                            // 화면에 출력
                            for(let i = 0; i < data.length; i++) {
                                modalTaskFileListContainer.append(fileHistoryRow(datum["uploadername"], addFileBox(data[i]["fileOrigName"], data[i]["fileType"], data[i]["formattedFileSize"], data[i]["id"])));
                            }
                            /*
                            for(let i = 0; i < fileData.getAll("files").length; i++){ //
                                const name = fileData.getAll("files")[i]["name"].substring(0, fileData.getAll("files")[i]["name"].lastIndexOf("."));
                                const type = fileData.getAll("files")[i]["name"].substring(fileData.getAll("files")[i]["name"].lastIndexOf(".")+1);
                                modalTaskFileListContainer.append(fileHistoryRow(datum["uploadername"], addFileBox(name, type, returnFileSize(parseInt(fileData.getAll("files")[i]["size"])), fileId)));
                            }*/
                        });
                    } else {
                        const err = response.json();
                        err.then(warning => {
                            alert(warning["message"]);
                            if(warning["taskRemoved"]){
                                location.reload();
                            }
                        });
                    }
                });

                // const result = validateFile(file.name, parseInt(file.size));
                if(invalidateFile.size !== 0){

                    let notAttachList = new Map();
                    let warning = [];
                    invalidateFile.forEach((v, k) => {
                        console.log(k, v); // #$%^.txt nay
                        if(v.includes('t')){
                            warning.push("첨부가 불가능한 유형의 파일입니다.");
                        }
                        if(v.includes('n')){
                            warning.push(`파일명에는 특수기호와 여백이 포함될 수 없습니다.<br/> (단, +, _, -, .은 포함가능)`);
                        }
                        if(v.includes('s')){
                            warning.push("5MB이하의 파일만 첨부할 수 있습니다.");
                        }
                        notAttachList.set(k, warning);
                        warning = [];
                    });

                    if(elExists(document.querySelector(".alert-not-attach-container"))){

                        const modalNotAttachGuide = document.querySelector(".alert-not-attach-container");
                        modalNotAttachGuide.classList.remove("hide");

                        for(const entry of notAttachList){
                            modalNotAttachGuide.querySelector(".alert-not-attach-scroll").appendChild(notAttachedGuide(entry));
                        }

                        document.querySelector("#btn-alert-not-attach").addEventListener("click", ()=>{
                            // 안에 내용 비운다.
                            [...modalNotAttachGuide.querySelector(".alert-not-attach-scroll").children].forEach(function(item){
                                item.remove();
                            });

                            // 창 닫는다.
                            modalNotAttachGuide.classList.add("hide");
                        });
                    }

                    console.log(notAttachList);
                    // console.log(notAttachList.get("login.js").length); // 1
                    // console.log(notAttachList.get("login.js")[0]); // 첨부가 불가능한 유형의 파일입니다.
                    //
                    // notAttachList.forEach((v, k) => {
                    //     console.log(k);
                    //     for (let i = 0; i < v.length; i++){
                    //         console.log(v[i]);
                    //     }
                    // })
                    // Map { "login.js" → (1) […], "#$%^.txt" → (1) […] }
                    // 0: "login.js" → Array [ "첨부가 불가능한 유형의 파일입니다." ]
                    // <key>: "login.js"
                    // <value>: Array [ "첨부가 불가능한 유형의 파일입니다." ]
                    // 0: "첨부가 불가능한 유형의 파일입니다."
                    // length: 1

                    // 1: "#$%^.txt" → Array [ "파일명에는 특수기호와 여백이 포함될 수 없습니다. (단, +, _, -, .은 포함가능)" ]
                }
            }
        } else {
            alert(`해당 할 일의 담당자와 작성자만 파일을 업로드할 수 있습니다.`);
            ModalTaskFileDropZone.classList.remove("dragover");
        }

    }); // 파일 drag and drop 끝

    /*---------- 044 ------------*/
    // 이미 생성된 할일의 파일 추가 2/2: input type="file"
    const modalTaskFileUploadInput = document.querySelector("#upload");
    const modalTaskFileListContainer = document.querySelector(".modal-task-file-list-container");
    modalTaskFileUploadInput.addEventListener("change", ()=>{

        const fileData = new FormData();
        const files = modalTaskFileUploadInput.files;
        const data = parents(modalTaskFileUploadInput)[0].dataset;
        console.log(data);
        let invalidFile = new Map();
        if(data["authorized"] === "true"){ // 할 일의 담당자와 작성자만 파일 업로드가 가능하다.
            if(files.length < 6) {
                for(let i = 0; i < files.length; i++) {
                    const result = validateFile(files[i].name, parseInt(files[i].size));
                    if(result === 'okay'){
                        createTasknofile.classList.remove("hide");
                        // const type = files[i].name.substring(files[i].name.lastIndexOf(".")+1);
                        fileData.append("files", files[i]);
                    } else {
                        const name = files[i].name.substring(0, files[i].name.lastIndexOf("."));
                        invalidFile.set(name, result);
                    }
                }
                // 서버 전송
                fetch(`http://ontrack-env.eba-mpbdgazx.ap-northeast-2.elasticbeanstalk.com/task/file?pId=${data["projectid"]}&tId=${data["taskid"]}&mId=${data["uploadermid"]}`, {
                    method: 'POST',
                    headers: {},
                    body: fileData
                }).then(response => {
                    if(response.ok){
                        response.json().then(file => {
                            // 만약 '등록된 파일이 없습니다' 문구가 있다면 삭제
                            if(modalTaskFileListContainer.querySelector("p.no-file")){
                                modalTaskFileListContainer.querySelector("p.no-file").remove();
                            }

                            // 화면에 출력
                            for(let i = 0; i < file.length; i++) {
                                modalTaskFileListContainer.append(fileHistoryRow(data["uploadername"], addFileBox(file[i]["fileOrigName"], file[i]["fileType"], file[i]["formattedFileSize"], file[i]["id"])));
                            }
                        });

                    } else {

                    }
                }); // fetch ends
                if(invalidFile.size !== 0){

                    let notAttachList = new Map();
                    let warning = [];
                    invalidFile.forEach((v, k) => {
                        console.log(k, v); // #$%^.txt nay
                        if(v.includes('t')){
                            warning.push("첨부가 불가능한 유형의 파일입니다.");
                        }
                        if(v.includes('n')){
                            warning.push(`파일명에는 특수기호와 여백이 포함될 수 없습니다.<br/> (단, +, _, -, .은 포함가능)`);
                        }
                        if(v.includes('s')){
                            warning.push("5MB이하의 파일만 첨부할 수 있습니다.");
                        }
                        notAttachList.set(k, warning);
                        warning = [];
                    });

                    if(elExists(document.querySelector(".alert-not-attach-container"))){

                        const modalNotAttachGuide = document.querySelector(".alert-not-attach-container");
                        modalNotAttachGuide.classList.remove("hide");

                        for(const entry of notAttachList){
                            modalNotAttachGuide.querySelector(".alert-not-attach-scroll").appendChild(notAttachedGuide(entry));
                        }

                        document.querySelector("#btn-alert-not-attach").addEventListener("click", ()=>{
                            // 안에 내용 비운다.
                            [...modalNotAttachGuide.querySelector(".alert-not-attach-scroll").children].forEach(function(item){
                                item.remove();
                            });

                            // 창 닫는다.
                            modalNotAttachGuide.classList.add("hide");
                        });
                    } // 첨부되지 않은 파일들 사유와 함께 출력 ends
                } // 첨부되지 않은 파일이 있다면 실행 코드 ends
            } else {
                alert(`파일 첨부는 5개 이하로 가능합니다.`);
            }
        } else {
            alert(`해당 할 일의 담당자와 작성자만 파일을 업로드할 수 있습니다.`);
            ModalTaskFileDropZone.classList.remove("dragover");
        }




    }); // 이미 생성된 할 일에 파일 추가(input) 끝

    /*---------- 061 ------------*/
    // 첨부 실패한 파일 설명 div 생성
    function notAttachedGuide(notAttachList){

        const contentBox = document.createElement("div");
        contentBox.classList.add("alert-not-attach-content");
        const fileNameBox = document.createElement("div");
        const reasonBox = document.createElement("div");

        const fileNameSpan = document.createElement("span");
        const spanArr = [];
        const reasonSpan1 = document.createElement("span");
        const reasonSpan2 = document.createElement("span");
        const reasonSpan3 = document.createElement("span");
        spanArr.push(reasonSpan1);
        spanArr.push(reasonSpan2);
        spanArr.push(reasonSpan3);

        fileNameSpan.innerText = notAttachList[0];
        fileNameBox.appendChild(fileNameSpan);
        for(let i = 0; i < notAttachList[1].length; i++) {
            spanArr[i].innerHTML = notAttachList[1][i];
            reasonBox.appendChild(spanArr[i]);
        }
        contentBox.appendChild(fileNameBox);
        contentBox.appendChild(reasonBox);

        return contentBox;
    }

    /*---------- 045 ------------*/
    // 할 일 상세에서의 파일 삭제
    onEvtListener(document, "click", ".btn-modal-task-file-del", function(){
        //console.log(`this: ${this}`); //this: [object HTMLSpanElement]
        //console.log(this); // <span class="hoverBigger20 cursorP modal-task-file-del">
        //console.log(this.parentElement.parentElement); // <div class="파일박스 flex-row-between-nowrap hoverShadow">

        const data = this.dataset;
        console.log(data);
        console.log(data["deletedby"]);
        console.log(data["deletedby"] === "uploader"); // false


        // 현재 파일 개수
        let cntFileRow = [...parents(this, ".modal-task-file-list-container")[0].querySelectorAll(".modal-task-file-history-row")].length;

        console.log(cntFileRow);
        // 파일 개수에서 차감한다.
        cntFileRow--;

        // 관리자에 의해 삭제된 건지 알려면 userId까지 또 check 해야 하잖아!!
        // 추후 if문 추가 요망: 관리자에 의한 삭제 or 작성자에 의한 삭제
        if(confirm(`정말로 삭제하시겠습니까? \n 파일삭제는 복구가 불가능합니다`) === true) {

            if(data["deletedby"] === "uploader"){ // 작성자 삭제
                console.log("작성자에 의한 삭제");
                // 서버 요청
                fetch(`http://ontrack-env.eba-mpbdgazx.ap-northeast-2.elasticbeanstalk.com/task/file?fId=${data["fileid"]}`, {
                    method: 'DELETE'
                }).then(response => {
                    if(response.ok){
                        if(cntFileRow === 0) {
                            const noFileP = document.createElement("p");
                            noFileP.classList.add("mt-10", "no-file");
                            noFileP.innerHTML = "등록된 파일이 없습니다.";
                            console.log(parents(this, ".modal-task-file-history-row")[0]);
                            parents(this, ".modal-task-file-history-row")[0].before(noFileP);
                        }
                        parents(this, ".modal-task-file-history-row")[0].remove();
                    } else {
                        const err = response.json();
                        err.then(warning => {
                            alert(warning["message"]);
                            if(warning["taskRemoved"]){
                                location.reload();
                            }
                        });
                    }
                });
            } else { // 관리자 삭제
                console.log("관리자에 의한 삭제");
                // 서버 요청
                fetch(`http://ontrack-env.eba-mpbdgazx.ap-northeast-2.elasticbeanstalk.com/task/file?fId=${data["fileid"]}&executorMid=${data["deletedby"]}`, {
                    method: 'DELETE'
                }).then(response => {
                    if(response.ok){
                        parents(this, ".modal-task-file-history-row")[0].querySelector(".deletedByAdminBox").classList.remove("hide");
                        parents(this, ".hoverShadow")[0].remove();
                    } else {
                        const err = response.json();
                        err.then(warning => {
                            alert(warning["message"]);
                            if(warning["taskRemoved"]){
                                location.reload();
                            }
                        });
                    }
                });
            }
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
    // 이미 생성된 할일의 파일 추가: 파일내역Row 동적생성
    function fileHistoryRow(uploader, addFileBox){
        const fileRow = document.createElement("div");
        const fileRowDateBox = document.createElement("div");
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

        fileRowDateBox.classList.add("flex-row-justify-start-align-center");

        deletedByAdminBox.classList.add("deletedByAdminBox");
        deletedByAdminBox.classList.add("hide");

        fileDeletedByAdmin.classList.add("font-blur");
        fileDeletedByAdmin.classList.add("font-13");
        fileDeletedByAdmin.classList.add("deletedByAdmin"); // 지워도 될 듯?
        fileDeletedByAdmin.classList.add("hide");
        

        fileRowDate.classList.add("altivo-light");
        fileRowDate.classList.add("font-12");

        fileRowUploader.className = "font-14";

        fileRowDate.innerText = returnDate();
        fileRowUploader.innerText = uploader;
        fileDeletedByAdmin.innerText = "관리자에 의해 삭제되었습니다."

        deletedByAdminBox.append(fileDeletedByAdmin);
        fileRowDateBox.appendChild(fileRowDate);

        fileRow.append(fileRowDateBox, fileRowUploader, addFileBox, deletedByAdminBox);

        return fileRow;
    }

    /*---------- 048 ------------*/
    // 이미 생성된 할일의 파일 추가: 첨부된 파일 정보담을 div 동적생성
    function addFileBox(name, type, size, fileId) {
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
        
        fileIcon.src = "/imgs/icon_download4.png";
        fileIcon.setAttribute("th:src", "@{/imgs/icon_download4.png}");
        fileIcon.alt = "download";
        fileIcon.classList.add("img-15");
        fileIcon.classList.add("mr-5");

        fileName.classList.add("modal-file-name");
        fileType.classList.add("font-13");
        fileType.classList.add("modal-file-type");

        fileDiv2.classList.add("flex-row-justify-start-align-center");
        fileSize.classList.add("modal-task-file-size");
        fileDelBtn.classList.add("btn-modal-task-file-del");
        fileDelBtn.setAttribute("data-fileid", fileId);
        fileDelBtn.setAttribute("data-deletedby", "uploader"); // 파일 업로드 후 생성되는 요소이므로, 업로더로 고정

        fileDelBtn.innerHTML = `&times;`;
        fileName.innerText = name;
        fileType.innerText = `.${type}`;
        fileSize.innerText = size;

        fileDelBtn.classList.add("hoverBigger20");
        // fileDelBtn.classList.add("cursorP");

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


    /*---------- 062 할 일 체크박스 클릭(삭제 버튼 출력) ------------*/
    let cntChkBox = 0;
    if(elExists(document.querySelector(".task-checkbox"))){
        document.querySelectorAll(".task-checkbox").forEach(function(chkbox){

            chkbox.addEventListener("change", ()=>{
                console.log(`check box checked!`);
                if(chkbox.checked === true) {
                    console.log(`체크된 박스가 생겼다`);
                    cntChkBox++;
                    console.log(cntChkBox);
                } else {
                    console.log(`체크 해제`);
                    cntChkBox--;
                    console.log(cntChkBox);
                }
                if(cntChkBox > 0) {
                    document.querySelector(".btn-delete-task").classList.remove("hide");
                } else {
                    document.querySelector(".btn-delete-task").classList.add("hide");
                }

                // if(document.querySelector(".btn-delete-task").classList.contains("hide")){
                //     document.querySelector(".btn-delete-task").classList.remove("hide");
                // } else {
                //     document.querySelector(".btn-delete-task").classList.add("hide");
                // }
            });
        });
    }

    // 삭제할 할 일의 task id 담는 array
    let checkedTaskList = [];

    /*---------- 063 (체크박스에 의해 노출된) 할 일 삭제 버튼 클릭 ------------*/
    if(elExists(document.querySelector(".btn-delete-task"))){
        document.querySelector(".btn-delete-task").addEventListener("click", ()=>{
            console.log(`삭제버튼 클릭됨`);
            console.log(cntChkBox);

            document.querySelectorAll(".task-checkbox").forEach(function(chkBox){
                if(chkBox.checked === true) {
                    checkedTaskList.push(chkBox.value);
                }
            });
            console.log(checkedTaskList);

            let currUrl = decodeURIComponent(new URL(location.href).pathname).split("/");
            const executorMid = document.querySelector(".btn-delete-task").dataset.executormid;
            // const projectId = document.querySelector(".btn-delete-task").dataset.projectid;


            const taskBinRequest = {
                projectId: currUrl[2],
                taskIds: checkedTaskList,
                deletedBy: executorMid
            }

            console.log(taskBinRequest);

            fetch(`http://ontrack-env.eba-mpbdgazx.ap-northeast-2.elasticbeanstalk.com/task`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(taskBinRequest)

            }).then(response => {
                if(response.ok){
                    // 모든 체크박스 false 처리
                    document.querySelectorAll(".task-checkbox").forEach(function(chkBox){
                        chkBox.checked = false;
                    });

                    // web socket: 휴지통으로 이동한 할 일들의 task id를 array로 전송
                    binWs.binSocket.send(checkedTaskList);

                    // 삭제할 task id array clear
                    checkedTaskList = [];
                    // 화면 리로드
                    location.reload();
                } else {
                    alert(`할 일 삭제가 완료되지 않았습니다.`);
                }
            });

        });
    } // 체크박스에 의한 할 일 삭제 end(.btn-delete-task)

    /*---------- 064 (개별row의 삭제 버튼에 의한)할 일 삭제 ------------*/
    if(elExists(document.querySelector(".btn-delete-eachTask"))){
        const btnDelTaskEachRow = document.querySelectorAll(".btn-delete-eachTask");
        btnDelTaskEachRow.forEach(function(btn){
            btn.addEventListener("click", ()=>{
                console.log('개별 할 일 삭제 버튼 눌림');
                let currUrl = decodeURIComponent(new URL(location.href).pathname).split("/");
                const data = btn.dataset;
                console.log(data);
                checkedTaskList.push(data["taskid"]);
                const taskBinRequest = {
                    projectId: currUrl[2],
                    taskIds: checkedTaskList,
                    deletedBy: data["executormid"]
                };

                fetch(`http://ontrack-env.eba-mpbdgazx.ap-northeast-2.elasticbeanstalk.com/task`, {
                    method: 'DELETE',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(taskBinRequest)
                }).then(response => {
                    if(response.ok){
                        console.log(`할 일 개별 row에서 삭제됨.`)

                        // web socket: 휴지통으로 이동한 할 일들의 task id를 array로 전송
                        binWs.binSocket.send(checkedTaskList);

                        checkedTaskList = [];
                        location.reload();
                    } else {
                        alert(`할 일 삭제가 완료되지 않았습니다.`);
                    }
                });
            });

        });
    }

    /*---------- 065 할 일 테이블 뷰: 맨 위 체크박스 클릭 ------------*/
    // check-all, task-checkbox
    if(elExists(document.querySelector(".tableview-check-all"))){
        const btnCheckAll = document.querySelector(".tableview-check-all");
        btnCheckAll.addEventListener("click", ()=>{
            if(btnCheckAll.checked === true){
                parents(btnCheckAll, "#table-view")[0].querySelectorAll(".task-checkbox").forEach(function(eachChkBox){
                    eachChkBox.checked = true;
                    document.querySelector(".btn-delete-task").classList.remove("hide");

                });
            } else {
                parents(btnCheckAll, "#table-view")[0].querySelectorAll(".task-checkbox").forEach(function(eachChkBox){
                    eachChkBox.checked = false;
                    document.querySelector(".btn-delete-task").classList.add("hide");
                });
            }
        });
    }
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


/*---------- 000 ------------*/
/*---- ▼   시작 ▼ ----*/
/*---- ▲   끝 ▲ ----*/
