/*로컬에서 file:// 프로토콜을 사용해 웹페이지를 열면 import, export 지시자가 동작하지 않습니다.*/
// import { elExists } from "./myprojects";
function elExists(el){
    return el !== undefined && el !== null;
}


window.onload = function(){

    /*************************/
    /***********************
     * 1) 할 일 추가(생성): 열기, 담당자배정, 파일첨부, 새 할 일 정보 전송, 닫기,
     * 2) 공지 등록(쓰기): 열기, 파일첨부, 새 공지 정보 전송, 닫기
     * 3) 할 일 상세(읽기): 
     * 4) view tab
     * 그 때의 최선은 지금 여기 없습니다. 최선 씨, 어디갔나요. 여긴 엉망진창이에요.
    */

    // try1. redirect로 페이지 로드 됐을 때 할 일 추가 모달 닫기
    // loadReset();

    /*---- ▼ 열고닫기.. 시작 ▼ ----*/


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
    

    /*---------- 0 ------------*/
    /* 프로젝트 설정 목록(공지등록, 프로젝트설정) 열기 */
    const btnOpenProjSetting = document.querySelector(".btn-open-settings");
    const clickProjSetting = document.querySelector(".click-proj-setting");
    btnOpenProjSetting.addEventListener("click", ()=>{
        // console.log(btnOpenProjSetting.dataset.id); // 추후 서버로 넘길 id
        clickProjSetting.classList.toggle("img-hidden");
    });

    /*---------- 0 ------------*/
    /* 프로젝트 설정 모달 열기 */
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
    /*---------- 0 ------------*/
    /* 프로젝트 설정 모달 닫기 */
    const btnCloseModalConfigureProject = document.querySelector(".box-close-modal-configure-project");
    btnCloseModalConfigureProject.addEventListener("click", ()=>{
        modalConfigureProject.classList.add("hide");
    });


    /*---------- 1) ------------*/
    /* 할 일 추가 모달 열기 버튼 */
    const btnOpenModalAddTask = document.querySelector("#btn-add-task");
    const containerCreateTask = document.querySelector("#container-create-task");

    btnOpenModalAddTask.addEventListener("click", ()=>{
        modalCreateTaskParentTitle.classList.add("img-hidden");
        iconChildTaskTitle.classList.add("hide");
        containerCreateTask.classList.remove("hide");
    });


    /*---------- 3) ------------*/
    /* 할 일 상세 모달 닫기 버튼 */
    const btnCloseModalTaskDetail = document.querySelectorAll(".btn-close-modal-taskDetail");
    const containerTaskDetail = document.querySelector("#container-task-detail");
    btnCloseModalTaskDetail.forEach(function(chosenBtn){
        chosenBtn.addEventListener("click", ()=>{
            containerTaskDetail.classList.add("hide");
        });
    });

    /*---------- 4) ------------*/
    /* 할 일 상세 모달 열기 */
    const btnOpenTaskDetail = document.querySelectorAll(".btn-task-detail");
    const btnTaskTabs = document.querySelectorAll(".btn-modal-task-tab");

    const btnModalTaskDetailTab = document.querySelector("#task-tab-info");
    const modalTaskCommonArea = document.querySelector("#task-detail-common");
    const modalTaskTabs = document.querySelectorAll(".modal-task-tab");
    const modalTaskDetailForm = document.querySelector("form#edit-task");
    btnOpenTaskDetail.forEach(function(chosenTask){
        chosenTask.addEventListener("click", ()=>{
            // console.log(chosenTask.id); // id값 가져옴. 8
            // id값으로 서버에서 해당 task 정보 가져오는 코드 추가 요망


            // success일 경우 아래 실행
            // 1) 컨테이너 열고
            containerTaskDetail.classList.remove("hide");
            // 2) 전체 탭버튼에서 선택됨 뺐다가
            btnTaskTabs.forEach(function(btnTabs){
                btnTabs.classList.remove("task-tab-chosen");
            });
            // 3) 할 일 상세 탭버튼에만 선택됨 넣고
            btnModalTaskDetailTab.classList.add("task-tab-chosen");

            // 4) 모든 탭을 숨겼다가
            modalTaskTabs.forEach(function(everyTabs){
                everyTabs.classList.add("hide");
            });
            // 5) 할 일 상세 탭만 출력한다. (추후 각 컬럼과 일치하는 id를 가진 탭을 출력 코드로 변경요망...? 휴..)
            modalTaskDetailForm.classList.remove("hide");
        });
    });

    /*---------- 5) ------------*/
    /* 할 일 상세 모달 탭버튼 클릭*/
    let modalTaskTabChosen;
    
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
                if(eachtab.id === "task-tab-info"){
                    eachtab.classList.remove("hide");
                    modalTaskCommonArea.classList.add("hide");
                } else if (eachtab.classList.contains(modalTaskTabChosen)) {
                    eachtab.classList.remove("hide");
                    document.querySelector("#task-detail-common").classList.remove("hide");
                }
            });
        });
    }); // 할 일 상세 모달 탭버튼 클릭 끝

    /*---------- 6) ------------*/
    /* 할 일 나누기 (세부항목/하위항목) create child task*/
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


    /*---------- 7) ------------*/
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

    /*---------- 8) ------------*/
    /* 공지읽기 닫기 */
    const btnCloseModalNoticeRead = document.querySelectorAll(".btn-close-modal-notice-read");
    btnCloseModalNoticeRead.forEach(function(chosenBtn){
        chosenBtn.addEventListener("click", ()=>{
            modalNotice.classList.add("hide");
        });
    });


    /*---------- 9) ------------*/
    /* 공지쓰기 열기 */
    
     const btnOpenWriteNotice = document.querySelector(".btn-write-notice");
     const modalWriteNotice = document.querySelector("#modal-notice-write");
     btnOpenWriteNotice.addEventListener("click", ()=>{
        modalWriteNotice.classList.remove("hide");
     });
 
   


    /*---- ▲ 열고닫기 끝 ▲ ----*/
    /*************************/

    /*---- ▼  프로젝트 view tab 시작 ▼ ----*/

    const btnTabView = document.querySelectorAll(".btn-view");
    const projectView = document.querySelectorAll(".project-view");
    btnTabView.forEach(function(chosenBtn){
        chosenBtn.addEventListener("click", ()=>{
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
    });
    /*---- ▲  view tab 끝 ▲ ----*/


    /*---- ▼ 공지쓰기: 파일첨부 시작 ▼ ----*/
    /*****************
     * - set Attribute로 data-id되는지 확인!
     * - form으로 넘어오는 data 확인!
     * - 현재 공지쓰기의 파일첨부는 1번에 1개씩만 업로드 가능함...
     **/
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
    /*---- ▲ 공지쓰기: 파일첨부 끝 ▲ ----*/


    /*---- ▼ 공지쓰기: submit 시작 ▼ ----*/
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
    /*---- ▲ 공지쓰기: submit 끝 ▲ ----*/

    /*---------- 10) ------------*/
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


    /*---- ▼ Modal(Create Task;할일추가) 담당자 배정 시작 ▼ ----*/

    const assigneeBeforeChoose  = document.querySelector("#assignee-before-choose");
    const assignIndication = document.querySelector("#assign-indication");
    const btnShowAssigneeList = document.querySelector("#show-assignee-list");

    const assigneeList = document.querySelector("#assignee-list-box"); // div
    if(elExists(document.querySelectorAll("#assignee-list-box input"))) {
        console.log(`thymeleaf로 생성한 input 있음`);
        console.log(document.querySelectorAll("#assignee-list-box input")[0].value);
    }
    const assigneesName = document.querySelectorAll("#assignee-list-box input");
    
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
                console.log(`선택된 담당자들: ${chosenName.dataset.nickname}`);

                chosenAssigneeList.add(chosenName.dataset.nickname); // 중복 방지를 위한 set

                console.log("담당자 선택 후 set: ");
                console.log(chosenAssigneeList);
                console.log("담당자 선택 후 담당자 수: ");
                console.log(chosenAssigneeList.size);

            }
        }); // chosenName.addEventListener(click) ends
    });  // 프로젝트 멤버 목록에서 담당자 선택 끝 (assigneesName.map ends)

    
    // 동적으로 생성된, (배정된)담당자를 삭제(배정해제) 할 때!
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

    
    // 선택된 담당자 div 동적 생성. 
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

    
    /*---- ▼ Modal(Create Task;할일추가) 파일첨부 시작 ▼ ----*/
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

    // create task or notice
    // 공지쓰기, 할 일 추가(생성): 첨부된 파일 정보담을 div 동적 생성. 
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


    // 공지쓰기, 할일추가(생성): 동적으로 생성된, (첨부된)파일 삭제할 때!
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

    /*---- ▼ Modal(Create Task;할 일 추가): submit 시작 ▼ ----*/
    const modalCreateTask = document.querySelector("#container-create-task");
    const addTaskForm = document.querySelector("#form-create-task");
    const btnCreateTaskSubmit = document.querySelector("#create-task-submit"); // button
    let createTaskFileDelCnt = 0;

    // 배정된 담당자 member Id array
    let chosenAssigneesMid = [];
    // 배정된 담당자 nick name array
    let choosenAssigneesName = [];

    btnCreateTaskSubmit.addEventListener("click", (e)=>{
        e.preventDefault();
        const addTaskData = new FormData();

        /* form submit 하고 전송되는 값 확인 */
        console.log(addTaskForm.elements.projectId.value); // 9
        console.log(addTaskForm.elements.taskAuthorMid.value); // 14
        console.log(addTaskForm.elements.taskTitle.value); // 할 일 추가중
        console.log(addTaskForm.elements.taskPriority.value); // 1
        console.log(addTaskForm.elements.taskDueDate.value); // 2024-06-11
        console.log(addTaskForm.elements.taskAssignee); // RadioNodeList { 0: input#4.hide.chosen, 1: input#14.hide.chosen, 2: input#26.hide.chosen, 3: input#27.hide, 4: input#28.hide, value: "", length: 5 }
        addTaskForm.elements.taskAssignee.forEach(function(eachOne){
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
            // FileList [ File ] 줄바꿈
            //   0: File { name: "100자.txt", lastModified: 1712675372978, size: 250, … }
            //      lastModified: 1712675372978
            //      name: "100자.txt"
            //      size: 250
            //      type: "text/plain"
            //      webkitRelativePath: ""
            //   length: 1
        }

        addTaskData.append("projectId", addTaskForm.elements.projectId.value);
        addTaskData.append("taskAuthorMid", addTaskForm.elements.taskAuthorMid.value);
        addTaskData.append("authorName", addTaskForm.elements.authorName.value);
        addTaskData.append("taskTitle", addTaskForm.elements.taskTitle.value);
        addTaskData.append("taskPriority", addTaskForm.elements.taskPriority.value);
        addTaskData.append("taskDueDate", addTaskForm.elements.taskDueDate.value);
        addTaskData.append("assigneesMid", chosenAssigneesMid);
        addTaskData.append("assigneeNames", choosenAssigneesName);

        console.log(`------- addTaskData -------`);
        console.log(addTaskData);
        fetch('http://localhost:8080/task/addTask', {
            method:'POST',
            headers: {},
            body: addTaskData
        }).then(response => {
            if (response.ok) {
                afterAddTaskSubmit();
                const chkTime = new Date();
                console.log(`fetch 후 어떻게 되는가: ${chkTime.getHours()}:${chkTime.getMinutes()}:${chkTime.getSeconds()}:${chkTime.getMilliseconds()}`);
                // fetch 후 어떻게 되는가: 17:50:39:612 (컨트롤러보다 늦음)
            }
        });


    });
    /*---- ▲ Modal(Create task;할 일 추가): submit 끝 ▲ ----*/


    // 할 일 추가 모달 닫는 기능
    function afterAddTaskSubmit(){
        addTaskForm.reset(); // 파일첨부: 콘솔에는 length 0으로 찍힘. 담당자: 콘솔과 화면 모두 reset 필요

        // 선택했던 담당자 input 모두 해제
        addTaskForm.elements.taskAssignee.forEach(function(eachOne){
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
        createTaskFileDelCnt = 0;
        rewriteCreateTaskFileList = [];

        // 모달 닫기
        modalCreateTask.classList.add("hide");
    }

    /*---------- 2) ------------*/
    /* 할 일 추가 모달 닫기 버튼 */
    /*--- 닫기 전에 묻는 창 추가 요망? ---*/
    const btnCloseModalCreateTask = document.querySelector(".btn-close-modal-createTask");
    
    btnCloseModalCreateTask.addEventListener("click", ()=>{

        afterAddTaskSubmit();
    });



    /*---- ▼ Modal(Edit Task; 할일 수정(상세): 담당자 배정·수정 시작 ▼ ----*/

    const edit_assigneeBeforeChoose  = document.querySelector("#edit-assignee-before-choose");
    const edit_assignIndication = document.querySelector("#edit-assign-indication");
    const edit_btnShowAssigneeList = document.querySelector("#edit-show-assignee-list");

    const edit_assigneeList = document.querySelector("#edit-assignee-list");
    const edit_assigneesName = document.querySelectorAll("#edit-assignee-list > input");
    
    const edit_chosensBoxes = document.querySelector("#edit-chosens-boxes");
    
    let edit_cntChoosenAssignee = 0; // 하나의 일에 배정되는 담당자의 수를 세기 위한 변수

    // 프로젝트 멤버 목록 나타내기
     edit_btnShowAssigneeList.addEventListener("click", ()=>{

        // 담당자가 아직 배정되지 않았다면 '담당자 배정하기' 글씨는 출력, 
        // 선택된 담당자 이름이 출력될 div는 미출력 (목록 나타내기 클릭할 때마다 체크)
        if (edit_cntChoosenAssignee === 0) {
            edit_chosensBoxes.classList.remove("assignee-display");
            edit_chosensBoxes.classList.add("hide");
            edit_assigneeBeforeChoose.classList.add("assignee-display");
            edit_assigneeBeforeChoose.classList.remove("hide");
        }
        // 1. 목록 보이기 버튼을 목록 숨기기(올리기) 버튼으로
        edit_btnShowAssigneeList.classList.toggle("img-angle180");
        // 1. 담당자 목록 출력
        edit_assigneeList.classList.toggle("hide");
        // 1. '담당자 배정하기' 글씨 흐리기
        edit_assignIndication.classList.toggle("font-blur");
        
    }); // 프로젝트 멤버 목록 나타내기 끝 (btnShowAssigneeList.addEventListener(click) ends)


    
    const edit_chosenAssigneeList = new Set(); // 중복 선택 방지를 위한 Set

    // 프로젝트 멤버 목록에서 담당자 선택
    edit_assigneesName.forEach(function(edit_chosenName){
        edit_chosenName.addEventListener("click", ()=>{

            console.log("clicked edit modal list");
            // 2. '담당자 배정하기' 글씨 hide
            edit_assigneeBeforeChoose.classList.remove("assignee-display");
            edit_assigneeBeforeChoose.classList.add("hide");

            // 3. 선택된 담당자 이름들이 담길 div의 hide 해제
            edit_chosensBoxes.classList.add("assignee-display");
            edit_chosensBoxes.classList.remove("hide");

            // 하나의 일에 최대 배정되는 담당자 제한(6명), 이미 배정된 담당자는 또 클릭해도 무동작
            if (edit_cntChoosenAssignee < 6 && !edit_chosenAssigneeList.has(edit_chosenName.value)) {

                // 클릭될 때 마다 div 요소 생성
                edit_chosensBoxes.appendChild(edit_chosenAssigneeBox(edit_chosenName.value));

                edit_chosenAssigneeList.add(edit_chosenName.value); // 중복 방지를 위한 set

                edit_cntChoosenAssignee++;
            }
        }); // chosenName.addEventListener(click) ends
    });  // 프로젝트 멤버 목록에서 담당자 선택 끝 (assigneesName.map ends)

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


    /*---- ▼ Modal(Edit Task; 할일 수정(상세): 세부항목(하위할일;Child Task) 리스트 보기 시작 ▼ ----*/
    const btnShowChildTaskList = document.querySelector("#btn-show-childTasks");
    const childTaskList = document.querySelector("#childTask-list");
    btnShowChildTaskList.addEventListener("click", ()=>{
        btnShowChildTaskList.classList.toggle("img-angle180");
        childTaskList.classList.toggle("hide");
    });
    /*---- ▲ Modal(Edit Task; 할일 수정(상세): 세부항목(하위할일;Child Task) 리스트 보기 끝 ▲ ----*/

    

    /*---- ▼ Modal(Task comment)소통하기 - 글 등록 ▼ ----*/
    // 모두 확인 요청 시 요소 변화 + task-comment-type: Required Reading
    const commentNoticeBtn = document.querySelector("#task-comment-notice");
    const commentWriteBox = document.querySelector(".modal-task-comment-write");
    const btnSubmitComment = document.querySelector("#btn-submit-comment");
    let commentType;
    commentNoticeBtn.addEventListener("change", ()=>{
        if(commentNoticeBtn.checked){
            commentWriteBox.classList.add("task-comment-write-notice");
            btnSubmitComment.style.backgroundColor = "#9d3f3b";
            commentType = "notice";
        } else {
            commentType = "Normal";
            commentWriteBox.classList.remove("task-comment-write-notice");
            btnSubmitComment.style.backgroundColor = "#411c02";
        }
    });

    // 글 등록
    const newCommentContent = document.querySelector("#task-comment-write-content");
    btnSubmitComment.addEventListener("click", ()=>{
        // console.log(newCommentContent.value); // 입력한 글 뜸.

        // 화면 바로 출력 (비동기) 아니야... 그냥 서버 치고 오자...
    
        // 서버에 보내기 (작성자, 글내용, 타입(RR일 경우, 확인cnt=1(작성자id, check True)), 작성일시)
    });

    // 작성한 소통하기 글 바로 출력할 때 사용할 function (쓰게 될까? 쓰게 되겠지...)
    function createCommentBox(writer, type, content, assigneeCnt) {

        // img도 createElement or new Image();
        // .src = URL, .alt로 속성 지정 가능
        // url: js파일 기준 or html 기준?

        // 동적으로 RR을 생성할 때 미확인에 들어갈 숫자를 서버로부터 가져와야 한..다? 노노노
        // 해당 task의 담당자 수가 어딘가에 잠재되어 있어야겠..네? ㅋㅋㅋㅋㅋㅋㅋㅋㅋ 있으면 되지! 

        
        // 0. Container
        const commentBox = document.createElement("div");
        
        // 1. 작성자+모두확인요청cnt+작성일시+tool
        const commentInfoBox = document.createElement("div");
        
        // 1-1. 작성자+모두확인요청cnt
        const commentWriterNNoticeBox = document.createElement("div");
        // 작성자
        const commentWriter = document.createElement("span");
        // 모두확인요청 글의 확인/미확인 수 
        const commentNoticeChkCntBox = document.createElement("div");
        const commentNoticeIcon = document.createElement("img");
        const commentNoticeChk = document.createElement("span");
        const commentNoticeChkCnt = document.createElement("span");
        const commentNoticeUnchk = document.createElement("span");
        const commentNoticeUnhkCnt = document.createElement("span");
        
        //1-2. 작성일시+tool
        const commentDateNToolBox = document.createElement("div");
        // 작성일시
        const commentDate = document.createElement("span");
        // tool(수정/삭제) 버튼과 그에 따른 모달 2개
        const commentToolBox = document.createElement("div");
        // tool 버튼(3dot)
        const commentToolBtn = document.createElement("img");
        // 수정/삭제 버튼box
        const commentEditDelBox = document.createElement("div");
        // 수정버튼 (∵button으로 하면 visibility hidden이 적용되지 않음)
        const commentEditBtn = document.createElement("p");
        const commentEditIcon = document.createElement("img");
        const commentEdit = document.createElement("span");
        // 삭제버튼
        const commentDelBtn = document.createElement("p");
        const commentDelIcon = document.createElement("img");
        const commentDel = document.createElement("span");
        // 삭제confirm창
        const commentDelConfirmBox = document.createElement("div");
        const commentDelConfirm = document.createElement("span");
        const commentDelConfirmBtnBox = document.createElement("div");
        const commentDelConfirmY = document.createElement("p");
        const commentDelConfirmN = document.createElement("p");

        // 2. 글 내용
        const commentContentBox = document.createElement("div");
        const commentContent = document.createElement("textarea");
        
        // 3. 글 하단 버튼(수정 등록/취소) - hide
        // 모두확인요청인 comment의 '내용확인'버튼은 어차피 작성자에게는 노출되지 않도록 한다.
        // 댓글은 내가 작성한 글만 실시간으로 화면에 반영되도록 한다.
        // 타인이 작성한 댓글이 실시간으로 화면으로 나올 필요는 없다. 
        const commentEditApplyBtnBox = document.createElement("div");
        const commentEditSubmitBtn = document.createElement("span");
        const commentEditCancelBtn = document.createElement("span");

    }

    /*---- ▲ Modal(Task comment)소통하기 - 글 등록 끝 ▲ ----*/
    
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

    /*---- ▼ project별 사용자 이름(display name) 시작 ▼ ----*/
    const btnNickName = document.querySelector(".nickname");
    btnNickName.addEventListener("click", ()=>{
        
    });
    /*---- ▲ project별 사용자 이름(display name) 끝 ▲ ----*/

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

}; // window.onload = function() ends

   /*---- ▼  시작 ▼ ----*/
   /*---- ▲  끝 ▲ ----*/