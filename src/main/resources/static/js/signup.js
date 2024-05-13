function elExists(el){
    return el !== undefined && el !== null;
}



window.onload = function(){
    /*************
     * 
     * signup.html는
     * A) 메인페이지에서 넘어오거나
     * B) 인증링크를 통해 넘어온다
     * 컨트롤러를 통해 넘어오는 결과값으로 A와 B를 구별하여 출력해야 한다.
     * *****/ 
    
    const signUpStep1 = [];
    const signUpStep2 = [];
    const signUpStep3 = [];
    
    if(elExists(document.querySelectorAll(".signup-step-1"))) {
        document.querySelectorAll(".signup-step-1").forEach(function(item){
            signUpStep1.push(item);
        });        
        console.log(signUpStep1);
    }

    if(elExists(document.querySelectorAll(".signup-step-2"))) {
        document.querySelectorAll(".signup-step-2").forEach(function(item){
            signUpStep2.push(item);
        });
        console.log(signUpStep2);
    }

    if(elExists(document.querySelector(".signup-step-3"))){
        signUpStep3.push(document.querySelector(".signup-step-3"));
    }

    // '계속' 버튼. 
    // step 2로 넘어갈 때는 가려야 하고
    // step1과 step3에서는 보여야 한다.
    if(elExists(document.querySelector(".btn-signup-continue"))) {
        signUpStep1.push(document.querySelector(".btn-signup-continue"));
        
        // 인증링크를 통해 들어왔을 경우, 아래 시행(추후 if문으로 변경요망)
        // signUpStep3.push(document.querySelector(".btn-signup-continue"));
    }

    /*---- ▼  이메일 유효성 검사 시작 ▼ ----*/
    if(elExists(document.querySelector("input.input-signup-email")) && elExists(document.querySelector(".btn-signup-continue"))){
        const userInputSignupEmail = document.querySelector("input.input-signup-email");
        userInputSignupEmail.value="";

        // 이메일 주소 입력 후 (입력하지 않고 눌러도)
        // '계속' 버튼 클릭했을 때(유효성 검사)
        document.querySelector(".btn-signup-continue").addEventListener("click", ()=>{
            const signUpEmail= userInputSignupEmail.value;
            const regEmail = /^([a-z0-9_.-]+)@([\da-z.-]+)\.([a-z.]{2,6})$/;
            if((regEmail.test(signUpEmail))){
                console.log(true);
                console.log(signUpStep1);

                // 인증링크(메일) 전송 성공시 추후 컨트롤러 반응 아래로 이동 요망
                signUpStep1.forEach(function(item){
                    item.classList.add("hide");
                });

                signUpStep3.forEach(function(item){
                    item.classList.add("hide");
                });

                signUpStep2.forEach(function(item){
                    item.classList.remove("hide");
                });
                document.querySelector(".signup-email").innerText = signUpEmail;
                document.querySelector(".signup-step-num").innerText = "2";
                
                // 컨트롤러 통해서 메일 전송, 
                /* const response = await fetch("/url", {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringfy(userInputSignupEmail.value)
                }); */
                /* if (response.ok) {
                    signUpStep1.classList.add("hide");
                    signUpStep2.classList.remove("hide");
                } else {
                    메일 전송 실패 안내 with btn (다시 보내기 or 메일 주소 다시 입력하기)
                }*/
                // await response.text();
                

            } else {
                document.querySelectorAll(".valid-email-guide").forEach(function(item){
                    item.classList.remove("hide");
                    item.classList.add("cRed");
                });
            }
        }); // 계속 버튼 눌렀을 때 이벤트 끝

        // 이메일 입력할 때는 경고문구("이메일이 유효하지 않습니다") 사라지도록 한다.
        userInputSignupEmail.addEventListener("focus", ()=>{
            document.querySelectorAll(".valid-email-guide")[1].classList.add("hide");
            document.querySelectorAll(".valid-email-guide")[0].classList.remove("cRed");
        });

    } // input.input-signup-email과 btn-signup-continue에 대한 이벤트 끝

    /*---- ▲  이메일 유효성 검사 끝 ▲ ----*/
    
} // window.onload = function(){} 끝

/*---- ▼  시작 ▼ ----*/
/*---- ▲  끝 ▲ ----*/