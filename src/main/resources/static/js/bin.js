window.onload = function(){    
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

    /*---- ▼ row 끝 설정버튼(영구삭제/복원하기) 시작 ▼ ----*/
    const btnBinDelOrBack = document.querySelectorAll(".bin-btn-box-del-back");
    let cntEvent = 0;
    btnBinDelOrBack.forEach(function(chosenBtn){
        const box = [...chosenBtn.childNodes].filter((child) => child.classList !== undefined && child.classList.contains("bin-del-back-box"))[0];
        const btnBinDelPerm = box.querySelector("div.btn-bin-del-permanent");
        chosenBtn.addEventListener("click", ()=>{
            cntEvent++;
            console.log(`3dots clicked: ${cntEvent}`);
            box.classList.toggle("hide");

            btnBinDelPerm.addEventListener("click", (e)=>{
                cntEvent++;
                console.log(`btn del perm clicked: ${cntEvent}`);
                e.stopPropagation();
                e.preventDefault();
                
            });

            // box.querySelector(".btn-bin-restore").addEventListener("click", (e)=>{
            //     e.stopPropagation();
                
            // });
           
        });
    });
    /*---- ▲ row 끝 설정버튼 끝 ▲ ----*/

} // window.onload 끝