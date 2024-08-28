import * as commentWs from './wsComment.mjs'
import * as binWs from './wsBin.mjs'
import * as fnc from './fnc.mjs'

window.onload = function(){
    console.log(`nav.mjs`);
    commentWs.connectCommentWs();
    binWs.connectBinWs();

    // 웹소켓에 의해 출력된 tip의 '확인'버튼 클릭이벤트
    if(fnc.elExists(document.querySelector("mark.btn-close-alarm"))){
        document.querySelector("mark.btn-close-alarm").addEventListener("click", ()=>{
            console.log(`확인 버튼 clicked`);
            document.querySelector("span.alarm-bin").classList.add("img-hidden");
        });
    }

    // const navShowBtn = document.querySelector(".navBtn");
    // const navCloseBtn = document.querySelector(".closeBtn");
    // const navSideMini = document.querySelector(".nav-vertical-mini");
    // const mainWrapper = document.querySelector(".mainWrapper");
    // const navIconBtn = document.querySelectorAll(".nav-vertical-mini > i");
    //
    // navShowBtn.addEventListener("click", ()=>{
    //     // console.log("I'm btn");
    //     navSideMini.style.width = "100px";
    //     mainWrapper.style.marginLeft = "100px";
    //     for(let i = 0; i < navIconBtn.length; i++){
    //         navIconBtn[i].style.display = "block";
    //     }
    // });
    //
    // navCloseBtn.addEventListener("click", ()=>{
    //     navSideMini.style.width = "0";
    //     mainWrapper.style.marginLeft = "0";
    //     for(let i = 0; i < navIconBtn.length; i++){
    //         navIconBtn[i].style.display = "";
    //     }
    //
    // });


};