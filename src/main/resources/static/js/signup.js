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
    // const signUpStep3 = [];
    
    if(elExists(document.querySelectorAll(".signup-step-1"))) {
        document.querySelectorAll(".signup-step-1").forEach(function(item){
            signUpStep1.push(item);
        });        
        // console.log(signUpStep1);
    }

    if(elExists(document.querySelectorAll(".signup-step-2"))) {
        document.querySelectorAll(".signup-step-2").forEach(function(item){
            signUpStep2.push(item);
        });
        // console.log(signUpStep2);
    }

    // if(elExists(document.querySelector(".signup-step-3"))){
    //     signUpStep3.push(document.querySelector(".signup-step-3"));
    // }

    // '계속' 버튼.
    // step1에서만 보여야 한다.
    // step 2에서는 가려야 한다.
    if(elExists(document.querySelector(".btn-signup-continue"))) {
        signUpStep1.push(document.querySelector(".btn-signup-continue"));
        
        // 인증링크를 통해 들어왔을 경우, 아래 시행(추후 if문으로 변경요망)
        // signUpStep3.push(document.querySelector(".btn-signup-continue"));
    }

    /*---- ▼  회원가입 정보 유효성 검사 시작 ▼ ----*/
    // 1. 이메일
    if(elExists(document.querySelector("input.input-signup-email")) && elExists(document.querySelector(".btn-signup-continue"))){
        const userInputSignupEmail = document.querySelector("input.input-signup-email");
        userInputSignupEmail.value="";

        // 이메일 주소 입력 후
        // '계속' 버튼 클릭했을 때(유효성 검사)
        document.querySelector(".btn-signup-continue").addEventListener("click", ()=>{
            const signUpEmail= userInputSignupEmail.value;
            const regEmail = /^([a-z0-9_.-]+)@([\da-z.-]+)\.([a-z.]{2,6})$/;

            if((regEmail.test(signUpEmail))){

                signUpStep1.forEach(function(item){
                    item.classList.add("hide");
                });

                // signUpStep3.forEach(function(item){
                //     item.classList.add("hide");
                // });

                document.querySelector(".signup-email").innerText = signUpEmail;
                document.querySelector(".signup-step-num").innerText = "2";

                signUpStep2.forEach(function(item){
                    item.classList.remove("hide");
                    if(item.tagName === "INPUT") {
                        item.value = "";
                    }
                });

                // 컨트롤러 통해서 메일 전송,
                /*
                fetch(`http://localhost:8080/signup/step1`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(signUpEmail)
                }).then(response => {
                    if(response.ok){
                        signUpStep2.forEach(function(item){
                            item.classList.remove("hide");
                            if(item.tagName === "INPUT") {
                            item.value = "";
                    }
                        });
                    } else {
                        console.log(`something's wrong.. try again plz..`);
                    }
                });*/
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

    // 2. 비밀번호
    if(elExists(document.querySelector(".btn-signup-complete")) ){
        const userInputSignUpPw = document.querySelector("input[name=signup-password]");
        // 안전: 9자 이상의 대·소문자+숫자+특수문자 조합
        const regPwStrong = /^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[~!@#$%^&*()+=_/,.<>-])[A-Za-z\d~!@#$%^&*()+=_/,.<>-]{10,}$/;

        // 9자 이상
        const regPwLetterCount = /[\W\w]{9,}/;

        // 위험: 연속된 3글자
        const regPwLetterInRows = /(\w)\1\1/;

        document.querySelector(".btn-signup-complete").addEventListener("click", ()=>{
            const validPwGuide = document.querySelectorAll(".valid-pw-guide");
            
            console.log(userInputSignUpPw.value);
            if(userInputSignUpPw.value === ""){
                document.querySelectorAll(".valid-pw-required").forEach(function(eachOne){
                    eachOne.classList.remove("hide");
                    eachOne.classList.add("cRed");
                });
            };
            // 안전: 10자 이상, 대소문자숫자특수문자조합
            if(regPwStrong.test(userInputSignUpPw.value)){
                console.log("안전");
            };
            // 보통: 9자 이상, not 연속 3글자
            if(regPwLetterCount.test(userInputSignUpPw.value) && !regPwLetterInRows.test(userInputSignUpPw.value)){
                console.log("보통");
            };
            // 위험: 9자 미만, 연속 3글자
            if(regPwLetterInRows.test(userInputSignUpPw.value)|| !regPwLetterCount.test(userInputSignUpPw.value)){
                console.log("dangerous");
                validPwGuide[0].innerText = "위험"
                validPwGuide[1].classList.remove("hide");
            };

        }); // 인증 버튼 클릭했을 때 이벤트 끝

        // 비밀번호를 입력할 때는 경고문구("비밀번호를 입력해주시기 바랍니다") 사라지도록 한다.
        userInputSignUpPw.addEventListener("focus", ()=>{
            document.querySelectorAll(".valid-pw-required")[1].classList.add("hide");
            document.querySelectorAll(".valid-pw-required")[0].classList.remove("cRed");
            document.querySelectorAll(".valid-pw-guide")[0].innerText = "";
            document.querySelectorAll(".valid-pw-guide")[1].classList.add("hide");
        });
    }

    /*---- ▲  이메일 유효성 검사 끝 ▲ ----*/
    
} // window.onload = function(){} 끝

/*---- ▼  시작 ▼ ----*/
/*---- ▲  끝 ▲ ----*/