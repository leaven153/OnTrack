/*로컬에서 file:// 프로토콜을 사용해 웹페이지를 열면 import, export 지시자가 동작하지 않습니다.*/
// import { elExists } from "./myprojects";
function elExists(el){
    return el !== undefined && el !== null;
}


window.onload = function(){

    /*---------- 998) ------------*/
    /* 할 일 담당자 (더보기) 수정 */
    if(elExists(document.querySelector(".btn-more-assignInfo"))){
        const btnOpenAssigneeList = document.querySelectorAll(".btn-more-assignInfo");
        btnOpenAssigneeList.forEach(function(chosenOne){
            chosenOne.addEventListener("click", ()=>{
                if(next(chosenOne, ".assigneeList").classList.contains("img-hidden")){
                    next(chosenOne, ".assigneeList").classList.remove("img-hidden");
                } else {
                    next(chosenOne, ".assigneeList").classList.add("img-hidden");
                }

            });
        });
    }
    /* 할 일 진행상태 수정 */
    /* 할 일 마감일 수정 */

    const addTaskForm = document.querySelector("#form-create-task");
    /*---- ▼ Modal(Create Task;할일추가) 담당자 배정 시작 ▼ ----*/
    /*
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

        */

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



    /*---- ▼ Modal(Edit Task; 할일 수정(상세): 담당자 배정·수정 시작 ▼ ----*/
/*
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

    */

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


    // next
    function next(el, selector) {
        const nextEl = el.nextElementSibling;
        if(!selector || (nextEl && nextEl.matches(selector))) {
            return nextEl;
        }
        return null;
    }

    // prev
    function prev(el, selector){
        const prevEl = el.previousElementSibling;
        if(!selector || (prevEl && prevEl.matches(selector))){
            return prevEl;
        }
        return null;
    }

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