function elExists(el){
    return el !== undefined && el !== null;
}
console.log(`myprojects.js 인식됨`);

window.onload = function(){
    /*---------- 0 ------------*/
    /* 새 프로젝트 추가 모달 */
    if(elExists(document.querySelector(".btn-open-modal-create-project"))){
        console.log(`새프로젝트 추가 버튼 인식`);
        // 모달 컨테이너 (add/remove hide)
        const modalCreateProject = document.querySelector("#modal-create-project");

        // form 초기화
        document.querySelector("form#form-create-project").reset();

        // 생성하기 버튼(submit)
        const submitCreateProject = document.querySelector(".btn-submit-create-project");
        
        /* 새 프로젝트 추가 모달 열기 */
        document.querySelectorAll(".btn-open-modal-create-project").forEach(function(chosenBtn){
            chosenBtn.addEventListener("click", ()=>{
                console.log(`새프로젝트 추가 버튼 클릭`);
                modalCreateProject.classList.remove("hide");
                // 생성하기 버튼(submit) 비활성화
                submitCreateProject.disabled = true;
                submitCreateProject.classList.remove("bg533");
                submitCreateProject.classList.add("btn-blur");
                submitCreateProject.classList.add("cursorNot");
            });
        });


        // 생성하기 버튼 활성화 조건 test
        // 프로젝트 유형인 radio버튼이 선택되기 전에 프로젝트 이름이 입력될 때마다 form을 인식하므로
        // 아래처럼 newProjectType변수를 별도로 만들어 넘기지 않으면 계속 null 에러가 발생한다.
        let newProjectType = "";
        document.querySelectorAll("input[type=radio].new-project-type").forEach(function(eachOne){
            eachOne.addEventListener("click", ()=>{
                newProjectType = document.querySelector("input[type=radio].new-project-type:checked").value;
                // console.log(`newProjectType: ${newProjectType}`);
            });
        });

        // form에 필수 요소 입력 되면 생성하기 버튼 활성화
        document.querySelector("form#form-create-project").addEventListener("input", ()=>{

            // 프로젝트 이름은 유효성 검사 안해도 됩니까? 
            // 20글자 이하! + ?
            if (document.getElementsByName("newProjectName")[0].value !== "" && newProjectType !== "") {
                submitCreateProject.disabled = false;
                submitCreateProject.classList.add("bg533");
                submitCreateProject.classList.remove("btn-blur");
                submitCreateProject.classList.remove("cursorNot");
            } else {
                submitCreateProject.disabled = true;
                submitCreateProject.classList.remove("bg533");
                submitCreateProject.classList.add("btn-blur");
                submitCreateProject.classList.add("cursorNot");
            }
        });

        // submit 버튼 클릭 시 data 확인 (추후 새 프로젝트 추가의 위치, a태그로 바꿀지 말지 결정한 후 수정요망)
        /*
        document.querySelector(".btn-submit-create-project").addEventListener("click", (e)=>{
            // e.preventDefault();
            console.log(document.getElementsByName("newProjectName")[0].value);
            console.log(document.querySelector("input[type=radio].new-project-type:checked").value);
            console.log(document.getElementsByName("newProjectDueDate")[0].value);
        });*/
        /* 새 프로젝트 추가 모달 닫기 */
        document.querySelector("#btn-close-modal-create-project").addEventListener("click", ()=>{
            document.querySelector("form#form-create-project").reset();
            modalCreateProject.classList.add("hide");
        });
    }
   
    /*---- ▼ 프로젝트 탭전환 시작 ▼ ----*/
    
    if(elExists(document.querySelectorAll(".projects-tab"))) {
        const btnProjectTabs = document.querySelectorAll(".projects-tab");
        btnProjectTabs.forEach(function(chosenBtn){
            
            chosenBtn.addEventListener("click", ()=>{
                // console.log(chosenBtn.dataset.tabType); // a 태그로 갈 때 dataset 어떻게 가니? 응?
                [...chosenBtn.parentNode.children].filter((child) => child !== chosenBtn).forEach(function(sibling){
                    sibling.classList.remove("projects-tab-choosen");
                });
                chosenBtn.classList.add("projects-tab-choosen");
            });
        });
    } // document.querySelectorAll(".projects-tab") 끝
       

    /*---- ▲ 프로젝트 탭전환 끝 ▲ ----*/


    /*---- ▼  시작 ▼ ----*/
    /*---- ▲  끝 ▲ ----*/
} // window.onload = function(){} 끝