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


    // '계속' 버튼.
    // step1에서만 보여야 한다.
    // step 2에서는 가려야 한다.
    if(elExists(document.querySelector(".btn-signup-continue"))) {
        signUpStep1.push(document.querySelector(".btn-signup-continue"));
        
        // 인증링크를 통해 들어왔을 경우, 아래 시행(추후 if문으로 변경요망)
        // signUpStep3.push(document.querySelector(".btn-signup-continue"));
    }

    /*---- ▼  회원가입 정보 유효성 검사 시작 ▼ ----*/
    // 이메일, 비밀번호
    if(elExists(document.querySelector("input.signup-email")) && elExists(document.querySelector(".btn-signup-continue"))){
        const userInputSignupEmail = document.querySelector("input.signup-email");
        const userInputSignUpPw = document.querySelector("input[name=signup-password]");
        const validPwGuide = document.querySelectorAll(".valid-pw-guide");
        userInputSignupEmail.value="";

        // 이메일 형식
        const regEmail = /^([a-z0-9_.-]+)@([\da-z.-]+)\.([a-z.]{2,6})$/;
        // 9자 이상
        const regPwLetterCount = /[\W\w]{9,}/;
        // 위험: 연속된 3글자
        const regPwLetterInRows = /(\w)\1\1/;


        // 이메일 주소 입력 후
        // '계속' 버튼 클릭했을 때(유효성 검사)
        document.querySelector(".btn-signup-continue").addEventListener("click", ()=>{

            const signUpEmail= userInputSignupEmail.value;

            // 비밀번호 입력 안 했을 경우, 경고 (이메일을 입력하지 않았을 경우는 '유효하지 않은 이메일 형식'으로 취급한다.)
            if(userInputSignUpPw.value === ""){
                document.querySelectorAll(".valid-pw-required").forEach(function(eachOne){
                    eachOne.classList.remove("hide");
                    eachOne.classList.add("cRed");
                });
            }

            // 허술한 비밀번호(9자 미만 or 연속 3글자)에 대한 경고
            if(userInputSignUpPw.value !== "" && (regPwLetterInRows.test(userInputSignUpPw.value)|| !regPwLetterCount.test(userInputSignUpPw.value))){
                console.log("dangerous");
                validPwGuide[0].innerText = "위험"
                validPwGuide[1].classList.remove("hide");
            }

            // 이메일 형식이 맞고, 비밀번호가 9자 이상이며 연속의 3글자를 포함하지 않을 경우,
            if(regEmail.test(signUpEmail) && regPwLetterCount.test(userInputSignUpPw.value) && !regPwLetterInRows.test(userInputSignUpPw.value)){

                const newUser = {
                    userEmail: signUpEmail,
                    password: userInputSignUpPw.value,
                    userName: signUpEmail.slice(0, signUpEmail.indexOf("@"))
                };

                signUpStep1.forEach(function(item){
                    item.classList.add("hide");
                });

                document.querySelector("span.signup-email").innerText = signUpEmail;
                document.querySelector(".signup-step-num").innerText = "2";


                // 컨트롤러 통해서 메일 전송,
                fetch(`http://localhost:8080/signup/step1`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(newUser)
                }).then(response => {
                    if(response.ok){
                        signUpStep2.forEach(function(item){
                            item.classList.remove("hide");
                            if(item.tagName === "INPUT") {
                                item.value = "";
                            }
                        });
                    } else { // 이미 가입된 이메일일 경우(중복된 이메일)
                        const data = response.text();
                        data.then(value => {
                            document.querySelector(".signup-step-out").classList.remove("hide");
                            document.querySelector("span.signup-step-out").innerText = value;

                        });
                    }
                });

            } else if (signUpEmail === "" || !regEmail.test(signUpEmail)) {
                // 이메일 입력하지 않거나 유효하지 않은 형식으로 입력 했을 때, 
                document.querySelectorAll(".valid-email-guide").forEach(function(item){
                    item.classList.remove("hide");
                    item.classList.add("cRed");
                });
            }
        }); // 이메일과 비밀번호 유효성 검사와 btn-signup-continue에 대한 클릭이벤트 끝

        // 이메일 입력할 때는 경고문구("이메일이 유효하지 않습니다") 사라지도록 한다.
        userInputSignupEmail.addEventListener("focus", ()=>{
            document.querySelectorAll(".valid-email-guide")[1].classList.add("hide");
            document.querySelectorAll(".valid-email-guide")[0].classList.remove("cRed");
        });

        // 비밀번호를 입력할 때는 경고문구("비밀번호를 입력해주시기 바랍니다") 사라지도록 한다.
        userInputSignUpPw.addEventListener("focus", ()=>{
            document.querySelectorAll(".valid-pw-required")[1].classList.add("hide");
            document.querySelectorAll(".valid-pw-required")[0].classList.remove("cRed");
            document.querySelectorAll(".valid-pw-guide")[0].innerText = "";
            document.querySelectorAll(".valid-pw-guide")[1].classList.add("hide");
        });


    } // 

    /*---- ▲  회원가입 정보 유효성 검사 끝 ▲ ----*/

    // 인증 완료 후 로그인
    if(elExists(document.querySelector(".btn-signup-complete"))){
        const btnFirstLogin = document.querySelector(".btn-signup-complete");
        const firstLoginId = document.querySelector("input#first-login-id");
        const inputFirstLoginPw = document.querySelector("input#first-login-pw");
        btnFirstLogin.addEventListener("click", ()=>{
            console.log(`그냥 버튼은 submit과 다른 거 맞지용?`);
            console.log(`firstLoginId: ${firstLoginId.value}`);
            console.log(inputFirstLoginPw.value);
        });
    }
} // window.onload = function(){} 끝

/*---- ▼  시작 ▼ ----*/
/*---- ▲  끝 ▲ ----*/