import * as fnc from './fnc.mjs'



/*---- ▼ 휴지통: 카테고리 목록 열고 닫기 시작 ▼ ----*/
    const btnBinList = document.querySelectorAll(".btn-bin-list");
    btnBinList.forEach(function(btn){
        btn.addEventListener("click", ()=>{
            console.log("카테고리 목록 클릭");
            if(fnc.parents(btn, ".bin-category")[0].querySelector(".bin-list").classList.contains("hide")){
                fnc.parents(btn, ".bin-category")[0].querySelector(".bin-list").classList.remove("hide");
                if(fnc.parents(btn, ".bin-category")[0].querySelector("img").classList.contains("img-angle180")){
                    fnc.parents(btn, ".bin-category")[0].querySelector("img").classList.remove("img-angle180");
                }
            } else {
                fnc.parents(btn, ".bin-category")[0].querySelector(".bin-list").classList.add("hide");
                if(!fnc.parents(btn, ".bin-category")[0].querySelector("img").classList.contains("img-angle180")){
                    fnc.parents(btn, ".bin-category")[0].querySelector("img").classList.add("img-angle180");
                }
                fnc.parents(btn, ".bin-category")[0].querySelectorAll(".bin-disposal-box").forEach(function(btnBox){
                    btnBox.classList.add("hide");
                });
            }

        });
    });
    /*---- ▲ 휴지통: 카테고리 목록 열고 닫기 끝 ▲ ----*/


    /*---- ▼ 휴지통: 체크박스 시작 ▼ ----*/
    const binCheckBoxTop = document.querySelector('input[data-id="binChkAll"]');
    const binCheckBoxes = document.querySelectorAll(`input[data-input-type="binChkbox"]`);

    binCheckBoxTop.removeAttribute("checked");
    binCheckBoxTop.checked = false;
    binCheckBoxes.forEach(function(eachOne){
        eachOne.removeAttribute("checked");
        eachOne.checked = false;
    });

    let cntClickedCheckBoxTop = 0;
    binCheckBoxTop.addEventListener("click", ()=>{
        cntClickedCheckBoxTop++;
        binCheckBoxes.forEach(function(eachOne){
            if ((cntClickedCheckBoxTop % 2) === 1 && !eachOne.checked) {
                eachOne.checked = true;
            } else if ((cntClickedCheckBoxTop % 2) === 0) {
                eachOne.checked = false;
            }
        });
    });
    /*---- ▲ 휴지통: 체크박스 끝 ▲ ----*/

    /*---- ▼ row 끝 설정버튼(영구삭제/복원하기)  ▼ ----*/
    fnc.onEvtListener(document, "click", ".btn-bin-disposal", function(){
        if(this.querySelector(".bin-disposal-box").classList.contains("hide")){
            // 다른 열려있던 모든 박스 닫고,
            document.querySelectorAll(".bin-disposal-box").forEach(function(every){
                every.classList.add("hide");
            });
            // 선택된 박스만 연다.
            this.querySelector(".bin-disposal-box").classList.remove("hide");
        } else {
            this.querySelector(".bin-disposal-box").classList.add("hide");
        }
    });
    /*
    const btnBinDelOrBack = document.querySelectorAll(".btn-bin-disposal");

    btnBinDelOrBack.forEach(function(chosenBtn){

        // const box = [...chosenBtn.childNodes].filter((child) => child.classList !== undefined && child.classList.contains("bin-disposal-box"))[0];
        // const btnBinDelPerm = box.querySelector("div.btn-bin-del-permanent");
        chosenBtn.addEventListener("click", ()=>{
            console.log("클릭하지 않았는데 열리는 이유?");
            if(chosenBtn.querySelector(".bin-disposal-box").classList.contains("hide")){
                // 다른 열려있던 모든 박스 닫고,
                document.querySelectorAll(".bin-disposal-box").forEach(function(every){
                    every.classList.add("hide");
                });
                chosenBtn.querySelector(".bin-disposal-box").classList.remove("hide");
            } else {
                chosenBtn.querySelector(".bin-disposal-box").classList.add("hide");
            }



        });
    });*/
    /*---- ▲ row 끝 설정버튼 끝 ▲ ----*/


/*---- ▼ 휴지통: (개별 row) 영구삭제 버튼 클릭 이벤트 ▼ ----*/
// onEvt로 바꿀...까?
document.querySelectorAll(".btn-bin-delete").forEach(function(btn){
    btn.addEventListener("click", (e)=>{
        console.log("영구 삭제 누름");
        // 영구 삭제가 바로 가능하게 할 것인가? 담당자가 여러 명일 경우, 영구삭제는 불가능하도록 할까?
        e.stopPropagation();

    });
});
/*---- ▲ 휴지통: 영구삭제 버튼 클릭 이벤트 끝 ▲ ----*/


/*---- ▼ 휴지통: (개별 row) 복원하기 버튼 클릭 이벤트 ▼ ----*/
document.querySelectorAll(".btn-bin-restore").forEach(function(btn){
    btn.addEventListener("click", (e)=>{
        console.log("복원하기 누름");
        e.stopPropagation(); // 이게 있어야, disposal-box의 hide toggle 작동 멈춤
        const data = btn.dataset;

        // 복원할 아이템: 프로젝트/할 일
        // 프로젝트: 프로젝트 아이디만 있으면 됨?
        // 할 일: 히스토리 필요, 프로젝트 아이디 필요, 여러 개/단일 인지 구분 필요
        const taskHistory = {
            projectId: data["projectid"],
            taskId: data["taskid"],
            modItem: "할 일",
            modType: "복원",
            modContent: "휴지통에서 프로젝트로",
            updatedBy: data["restoredby"]
        };

    });
});
/*---- ▲ 휴지통: 복원하기 버튼 클릭 이벤트 끝 ▲ ----*/
// 웹소켓에 의해 출력된 tip의 '확인'버튼 클릭이벤트
if(fnc.elExists(document.querySelector("mark.btn-close-alarm"))){
    document.querySelector("mark.btn-close-alarm").addEventListener("click", ()=>{
        console.log(`확인 버튼 clicked`);
        document.querySelector("span.alarm-bin").classList.add("img-hidden");
    });
}


/*---- ▼  ▼ ----*/
/*---- ▲  ▲ ----*/