import * as fnc from './fnc.mjs';

console.log(`modalAddTask.js가 인식이 되고 있는가?`);

/*---------- 021 ------------*/
const addTaskForm = document.querySelector("#form-create-task");
/*---- ▼ 할 일 추가 모달: 담당자 배정 ▼ ----*/
const assigneeBeforeChoose  = document.querySelector("#assignee-before-choose");
const assignIndication = document.querySelector("#assign-indication");
const btnShowAssigneeList = document.querySelector("#show-assignee-list");

const assigneeList = document.querySelector("#assignee-list-box"); // div
if(fnc.elExists(document.querySelectorAll("#assignee-list-box input"))) {
    // console.log(`thymeleaf로 생성한 input 있음`);
    // console.log(document.querySelectorAll("#assignee-list-box input")[0].value);
}
const assigneesName = document.querySelectorAll("#assignee-list-box > input");

const chosensBoxes = document.querySelector("#chosens-boxes");

const chosenAssigneeList = new Set(); // 중복 선택 방지를 위한 Set

// 프로젝트 멤버 목록 나타내기
btnShowAssigneeList.addEventListener("click", ()=>{

    console.log(`할 일 추가: 프로젝트 멤버 목록 나타내기 버튼 클릭됨`);
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
fnc.onEvtListener(document, "click", ".btn-assignee-del", function(){

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
            const result = fnc.validateFile(files[i].name, parseInt(files[i].size));
            if (result === 'okay') {
                console.log(`파일 형식, 이름 검사 후`);
                createTasknofile.classList.remove("hide");
                createTaskFiles.appendChild(fnc.createAndAttachFileBox(name, type, returnFileSize(parseInt(files[i].size))));
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

            if(fnc.elExists(document.querySelector(".alert-not-attach-container"))){

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
// 모달(할 일): 첨부 파일 정보담을 div 동적 생성

/*---------- 026 ------------*/
// 모달(할 일): 서버 전송 전, 첨부했던 파일 삭제
// createAndAttachFileBox 안의 btnDel을 클릭했을 때
fnc.onEvtListener(document, "click", ".btn-del-file", function(){

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
// function validateFile(fullfilename, filesize)는 fnc.js에서 import


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
//addTaskForm.elements.assigneeMids
    document.querySelectorAll("input[name='assigneeMids']").forEach(function(eachOne){
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
        document.querySelectorAll("input[name='assigneeMids']").forEach(function(eachOne){
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
        if(fnc.elExists(document.querySelectorAll(".assignee-chosen-box"))){
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
            console.log("왜 화면 reload가 이렇게 되는거지?");
            console.log(location);
            location.reload();
        }
    });

});
/*---- ▲ Modal(Create task;할 일 추가): submit 끝 ▲ ----*/

/*---------- 030 ------------*/
// 할 일 추가 모달 닫는 기능
// function afterAddTaskSubmit(){
//
// }

/*---------- 031 ------------*/
/* 할 일 추가 모달 닫기 버튼 */
/*--- 닫기 전에 묻는 창 추가 요망? ---*/
const btnCloseModalCreateTask = document.querySelector(".btn-close-modal-createTask");

btnCloseModalCreateTask.addEventListener("click", ()=>{
    // afterAddTaskSubmit();
    addTaskForm.reset(); // 파일첨부: 콘솔에는 length 0으로 찍힘. 담당자: 콘솔과 화면 모두 reset 필요

    console.log(addTaskForm); // <form id="form-create-task" action="/task" method="POST" enctype="multipart/form-data">
    // console.log(addTaskForm.elements); // HTMLFormControlsCollection { 0: input#projectId,
    // 1: input#authorMid, 2: input, 3: input.modal-add-task,
    // 4: input#vip.input-radio.hide, 5: input#ip.input-radio.hide,
    // 6: input#norm.input-radio.hide, 7: input.altivo-light, 8: input#34.hide,
    // 9: input#create-task-attach-file.opacity0.wh0, … }


    // console.log(Array.of(addTaskForm.elements.assigneeMids));
    // console.log(Array.of(addTaskForm.elements));
    // <input id="34" class="hide" type="checkbox" name="assigneeMids" value="34" data-nickname="busmoja">

    // 선택했던 담당자 input 모두 해제
    document.querySelectorAll("input[name='assigneeMids']").forEach(function(eachOne){
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
    if(fnc.elExists(document.querySelectorAll(".assignee-chosen-box"))){
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