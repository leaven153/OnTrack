function elExists(el){
    return el !== undefined && el !== null;
}



window.onload = function(){
    


    /*---- ▼  이메일 유효성 검사 시작 ▼ ----*/
    if(elExists(document.querySelector("input.input-login-email")) && elExists(document.querySelector(".btn-login-continue"))){
        // console.log("connected");
        const userInputLoginEmail = document.querySelector("input.input-login-email");
        userInputLoginEmail.value="";

        // 이메일 주소 입력 후 (입력하지 않고 눌러도)
        // '계속' 버튼 클릭했을 때(유효성 검사)
        document.querySelector(".btn-login-continue").addEventListener("click", ()=>{
            const loginEmail= userInputLoginEmail.value;
            const regEmail = /^([a-z0-9_.-]+)@([\da-z.-]+)\.([a-z.]{2,6})$/;
            if((regEmail.test(loginEmail))){

                
                
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
        userInputLoginEmail.addEventListener("focus", ()=>{
            document.querySelectorAll(".valid-email-guide")[1].classList.add("hide");
            document.querySelectorAll(".valid-email-guide")[0].classList.remove("cRed");
        });

    } // input.input-signup-email과 btn-signup-continue에 대한 이벤트 끝

    /*---- ▲  이메일 유효성 검사 끝 ▲ ----*/
    
} // window.onload = function(){} 끝

/*---- ▼  시작 ▼ ----*/
/*---- ▲  끝 ▲ ----*/