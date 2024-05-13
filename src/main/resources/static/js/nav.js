window.onload = function(){
    const navShowBtn = document.querySelector(".navBtn");
    const navCloseBtn = document.querySelector(".closeBtn");
    const navSideMini = document.querySelector(".nav-vertical-mini");
    const mainWrapper = document.querySelector(".mainWrapper");
    const navIconBtn = document.querySelectorAll(".nav-vertical-mini > i");

    navShowBtn.addEventListener("click", ()=>{
        // console.log("I'm btn");
        navSideMini.style.width = "100px";
        mainWrapper.style.marginLeft = "100px";
        for(let i = 0; i < navIconBtn.length; i++){
            navIconBtn[i].style.display = "block";
        }
    });

    navCloseBtn.addEventListener("click", ()=>{
        navSideMini.style.width = "0";
        mainWrapper.style.marginLeft = "0";
        for(let i = 0; i < navIconBtn.length; i++){
            navIconBtn[i].style.display = "";
        }
        
    });


};