function elExists(el){
    return el !== undefined && el !== null;
}



window.onload = function(){

    // form의 내용이 모두 입력되지 않았을 경우, 로그인 버튼 비활성화 되도록 한다.
    // form의 내용이 입력되면 로그인 버튼 활성화 되지만
    // 유효성 검사를 실시간으로 진행하지는 않는다.

    /*---- ▼  로그인 유효성 검사 시작 ▼ ----*/
    /*---- 0. 입력 여부 ----*/
    /*---- 1. 이메일 유효성 ----*/
    if(elExists(document.querySelector("input.input-login-id")) && elExists(document.querySelector(".btn-login-submit")) && elExists(document.querySelector("input.input-login-pw"))){
        // console.log("connected");
        const userInputLoginEmail = document.querySelector("input.input-login-id");
        const userInputLoginPw = document.querySelector("input.input-login-pw");
        userInputLoginEmail.value="";


        // 이메일 주소 입력 후 (입력하지 않고 눌러도)
        // '계속' 버튼 클릭했을 때(유효성 검사)
        document.querySelector(".btn-login-submit").addEventListener("click", (e)=>{
            e.preventDefault();
            
            console.log("login test");
            const loginEmail= userInputLoginEmail.value;
            const regEmail = /^([a-z0-9_.-]+)@([\da-z.-]+)\.([a-z.]{2,6})$/;
            if(regEmail.test(loginEmail) && userInputLoginPw != null){
                // 컨트롤러에 LoginRequest 객체 전달
                console.log("id와 pw모두 정상");
                console.log(`id: ${loginEmail}`);
                console.log(`pw: ${userInputLoginPw.value}`);
                

            } else if (!regEmail.test(loginEmail)) {
                document.querySelectorAll(".valid-email-guide").forEach(function(item){
                    item.classList.remove("hide");
                    item.classList.add("cRed");
                });
            }
        }); // 로그인 버튼 눌렀을 때 이벤트 끝

        // 아이디(이메일) 입력할 때는 경고문구("이메일을 확인해주세요") 사라지도록 한다.
        userInputLoginEmail.addEventListener("keydown", ()=>{
            document.querySelectorAll(".valid-email-guide")[1].classList.add("hide");
            document.querySelectorAll(".valid-email-guide")[0].classList.remove("cRed");
        });

        // 비밀번호 입력할 때는 경고문구("비밀번호를 입력해주세요") 사라지도록 한다.
        userInputLoginPw.addEventListener("keydown", ()=>{
            document.querySelectorAll(".valid-pw-guide")[1].classList.add("hide");
            document.querySelectorAll(".valid-pw-guide")[0].classList.remove("cRed");
        });

    } // input.input-signup-email과 btn-signup-continue에 대한 이벤트 끝

    /*---- ▲  로그인 유효성 검사 끝 ▲ ----*/
    
} // window.onload = function(){} 끝

/*---- ▼  시작 ▼ ----*/
/*---- ▲  끝 ▲ ----*/