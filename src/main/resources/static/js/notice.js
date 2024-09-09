import * as fnc from './fnc.mjs'

console.log(`왜 인식안되는가`);
function elExists(el){
    return el !== undefined && el !== null;
}

// jQuery없이 on 사용할 때 ! ★★★
function onEvtListener(el, eventName, selector, eventHandler){
    if (selector) {
        const wrappedHandler = (e) => {
            if (!e.target) return;
            const el = e.target.closest(selector);
            if (el) {
                eventHandler.call(el, e);
            }
        }; // wrappedHandler Ends
        el.addEventListener(eventName, wrappedHandler);
        return wrappedHandler;
    } else {
        const wrappedHandler = (e) => {
            eventHandler.call(el, e);
        }; // wrappedHandler Ends
        el.addEventListener(eventName, wrappedHandler);
        return wrappedHandler;
    }

} // function onEvtListener Ends

function validateFile(fullfilename, filesize){
    // ver.1. 파일명 안에 특수기호는 +, _, -, .만 허용
    const regFileName = /[{}\[\]()\/?,'";:`~<>!@#$%^&*=|\s]/g;
    // ver.2. 윈도우가 허하지 않는 특수기호만 잡는 표현식 (\ / : * ? < > | 9개)
    // const regWinFileName = /[\\/:*?"<>|]/g;
    const regFileType = /(ade|adp|apk|appx|appxbundle|aspx|bat|bin|cab|cmd|com|cpl|dll|dmg|exe|gadget|hta|inf1|ins|ipa|iso|isp|isu|jar|js|jse|jsp|jsx|lib|lnk|mde|msc|msi|msix|msixbundle|msp|mst|nsh|paf|pif|ps1|reg|rgs|scr|sct|sh|shb|shs|sys|tar|u3p|vb|vbe|vbs|vbscript|vxd|ws|wsc|wsf|wsh)$/i;

    const filename = fullfilename.substring(0, fullfilename.lastIndexOf("."));
    const filetype = fullfilename.substring(fullfilename.lastIndexOf(".")+1);

    // 정규표현식에 있는 특수기호/공백/문자와 일치하는 글자가 파일명에 있을 경우 true 반환됨
    const nameResult = regFileName.test(filename)? 'n': 'ok';
    const typeResult = regFileType.test(filetype)? 't': 'a';
    const sizeResult = filesize > 5242880 ? 's': 'y';



    return (nameResult + typeResult + sizeResult);
}

function noticeAttachFileBox(name, type, size) {
    const div1 = document.createElement("div");
    const div2 = document.createElement("div");
    const fileName = document.createElement("span");
    const fileType = document.createElement("span");
    const fileSize = document.createElement("span");
    const btnDel = document.createElement("span");

    div1.className = "flex-row-between-nowrap";
    div1.classList.add("modal-create-and-attach-file");
    div2.className = "flex-row-justify-start-align-center";
    fileName.className = "modal-file-name";
    fileType.classList.add("font-14", "modal-file-type", "mr-10");
    fileSize.className = "modal-file-size";
    btnDel.className = "btn-del-notice-file";

    btnDel.innerHTML = "&times;";
    fileName.innerText = name;
    fileType.innerText = `.${type}`;
    fileSize.innerText = size; /*`${size}`*/

    div2.appendChild(fileName);
    div2.appendChild(fileType);
    div2.appendChild(fileSize);

    div1.appendChild(div2);
    div1.appendChild(btnDel);

    return div1;
} // function attahcedFileBox() 끝

// 파일 사이즈 보기 좋게(KB, MB로 각각 적용)
function returnFileSize(filesize){
    if (filesize < 1024) {
        return filesize + "byte";
    } else if (filesize >= 1024 && filesize < 1048576) {
        return (filesize/1024).toFixed(1) + "KB";
    } else if (filesize >= 1048576) {
        return (filesize/1048576).toFixed(1) + "MB";
    }
}

// 첨부 실패한 파일 설명 div 생성
function notAttachedGuide(notAttachList){

    const contentBox = document.createElement("div");
    contentBox.classList.add("alert-not-attach-content");
    const fileNameBox = document.createElement("div");
    const reasonBox = document.createElement("div");

    const fileNameSpan = document.createElement("span");
    const spanArr = [];
    const reasonSpan1 = document.createElement("span");
    const reasonSpan2 = document.createElement("span");
    const reasonSpan3 = document.createElement("span");
    spanArr.push(reasonSpan1);
    spanArr.push(reasonSpan2);
    spanArr.push(reasonSpan3);

    fileNameSpan.innerText = notAttachList[0];
    fileNameBox.appendChild(fileNameSpan);
    for(let i = 0; i < notAttachList[1].length; i++) {
        spanArr[i].innerHTML = notAttachList[1][i];
        reasonBox.appendChild(spanArr[i]);
    }
    contentBox.appendChild(fileNameBox);
    contentBox.appendChild(reasonBox);

    return contentBox;
}

// window.onload = function(){

    let rewriteNoticeFileList = [];
    let noticeFileCnt = 0;
    const createNoticeNoFile = document.querySelector("span#create-notice-no-file");

    /*---------- 016 ------------*/
    /* 공지쓰기 열기 */
    if(elExists(document.querySelector(".btn-write-notice"))){
        const btnOpenWriteNotice = document.querySelector(".btn-write-notice");
        const modalWriteNotice = document.querySelector("#modal-notice-write");
        btnOpenWriteNotice.addEventListener("click", ()=>{
            modalWriteNotice.classList.remove("hide");
            document.querySelector(".modal-notice-write").classList.remove("hide");
        });
    }


    /*---------- 018 ------------*/
    /*---- ▼ 공지쓰기: 파일첨부 시작 ▼ ----*/
    if(elExists(document.querySelector("#notice-file"))){
        const btnModalNoticeFileUpload = document.querySelector("#notice-file"); /* input */
        const modalNoticeFileListContainer = document.querySelector("#modal-notice-write-file-box");


        btnModalNoticeFileUpload.addEventListener("change", ()=>{

            const files = btnModalNoticeFileUpload.files;

            // 유효성 검사 후 미첨부된 파일 목록
            let invalidFile = new Map();

            // 파일 첨부는 최대 5개로 제한한다.
            if(files.length < 6) {
                for(let i = 0; i < files.length; i++) {
                    // 파일명, 타입, 크기 유효성 검사
                    const result = validateFile(files[i].name, parseInt(files[i].size));
                    const name = files[i].name.substring(0, files[i].name.lastIndexOf("."));
                    const type = files[i].name.substring(files[i].name.lastIndexOf(".")+1);
                    if( result === 'okay'){
                        noticeFileCnt++;
                        createNoticeNoFile.classList.add("hide");
                        modalNoticeFileListContainer.append(noticeAttachFileBox(name,type,returnFileSize(parseInt(files[i].size))));
                        rewriteNoticeFileList.push(files[i]);
                    } else  {
                        invalidFile.set(name, result);
                        // alert(`파일 첨부에 실패했습니다. 다음 사항을 확인해주시기 바랍니다. \n 1. 파일명에는 특수기호와 여백이 포함될 수 없습니다.\n(단, +, _, -, .은 포함가능) \n 2. 5MB이하의 파일만 첨부할 수 있습니다.\n ${files[i].name}의 크기: ${returnFileSize(parseInt(files[i].size))} \n`);
                    }
                }

                // 유효성 검사 결과: 미첨부 파일 있을 경우, 경고창으로 파일명과 사유 안내
                if(invalidFile.size !== 0){
                    let notAttachList = new Map();
                    let warning = [];
                    invalidFile.forEach((v, k) => {
                        if(v.includes('t')){
                            warning.push("첨부가 불가능한 유형의 파일입니다.");
                        }
                        if(v.includes('n')){
                            warning.push(`파일명에는 특수기호와 여백이 포함될 수 없습니다.<br/> (단, +, _, -, .은 포함가능)`);
                        }
                        if(v.includes('s')){
                            warning.push("5MB이하의 파일만 첨부할 수 있습니다.");
                        }
                        notAttachList.set(k, warning);
                        warning = [];
                    });

                    if(elExists(document.querySelector(".alert-not-attach-container"))){

                        const modalNotAttachGuide = document.querySelector(".alert-not-attach-container");
                        modalNotAttachGuide.classList.remove("hide");

                        for(const entry of notAttachList){
                            modalNotAttachGuide.querySelector(".alert-not-attach-scroll").appendChild(notAttachedGuide(entry));
                        }

                        document.querySelector("#btn-alert-not-attach").addEventListener("click", ()=>{
                            // 안에 내용 비운다.
                            [...modalNotAttachGuide.querySelector(".alert-not-attach-scroll").children].forEach(function(item){
                                item.remove();
                            });

                            // 창 닫는다.
                            modalNotAttachGuide.classList.add("hide");
                        });
                    }
                }
            } else {
                alert(`파일 첨부는 5개까지만 가능합니다.`);
            }
        });
    }

    /*---- ▲ 공지쓰기: 파일첨부 끝 ▲ ----*/

    /*---------- 018 ------------*/
    /*---- ▼ 서버 전송 전 첨부했던 파일 삭제 시작 ▼ ----*/
    onEvtListener(document, "click", ".btn-del-notice-file", function(){

        noticeFileCnt--;
        const idx = [...parents(this, "#create-task-files")[0].children].indexOf(parents(this, ".modal-create-and-attach-file")[0]); // [...this.parentNode.parentNode.children].indexOf(this.parentNode);
        rewriteNoticeFileList.splice(idx-1, 1); // p요소(첨부된 파일이 없습니다)로 인해 인덱스가 하나씩 밀려있다!

        // 삭제된 파일box 삭제
        this.parentElement.remove();

        // 첨부된 파일이 0개라면 '첨부된 파일이 없습니다' 문구 출력
        if(noticeFileCnt === 0){
            createNoticeNoFile.classList.remove("hide");
        }

    });
    /*---- ▲ 서버 전송 전 첨부했던 파일 삭제 끝 ▲ ----*/


    /*---------- 019 ------------*/
    /*---- ▼ 공지쓰기: submit 시작 ▼ ----*/

    if(elExists(document.querySelector("a#notice-write-submit"))){
        const btnSubmitWriteNotice = document.querySelector("a#notice-write-submit");
        const formWriteNotice = document.querySelector("form#form-write-notice");

        // let noticeFileDelCnt = 0;
        btnSubmitWriteNotice.addEventListener("click", ()=>{
            // 서버에 보낼 값 확인.
            console.log(formWriteNotice.elements.authorMid.value); // ok
            console.log(formWriteNotice.elements.title.value); // ok
            console.log(formWriteNotice.elements.content.value); // ok (콘솔에는 개행 반영됨. db에는 어떻게 저장되는지 확인요망)
            console.log(rewriteNoticeFileList);


            // formData에 파일 첨부
            for(let i = 0; i < rewriteNoticeFileList.length; i++) {
                formWriteNotice.append("noticeFiles", rewriteNoticeFileList[i]);
            }

            //서버 전송
            fetch(`http://ontrack-env.eba-mpbdgazx.ap-northeast-2.elasticbeanstalk.com/project/notice`, {
                method: 'POST',
                headers: {},
                body: formWriteNotice
            });

        });
    }

    /*---- ▲ 공지쓰기: submit 끝 ▲ ----*/

    /*---------- 020 ------------*/
    /* 공지쓰기 창 닫기: 모달 안에 X와 '닫기' 버튼 2개가 있다. */
    if(elExists(document.querySelector(".btn-close-modal-write-notice"))){
        const btnCloseModalWriteNotice = document.querySelectorAll(".btn-close-modal-write-notice");
        const modalNoticeFileListContainer = document.querySelector("#modal-notice-write-file-box");
        const createNoticeNoFile = document.querySelector("span#create-notice-no-file");
        let noticeFileCnt = 0;
        let noticeFileDelCnt = 0;
        const modalWriteNotice = document.querySelector("#modal-notice-write");
        const formWriteNotice = document.querySelector("form#form-write-notice");

        btnCloseModalWriteNotice.forEach(function(btn){
            btn.addEventListener("click", ()=>{
                formWriteNotice.elements.title.value = "";
                formWriteNotice.elements.content.value = "";
                formWriteNotice.elements.noticeFile.value = "";
                modalNoticeFileListContainer.innerHTML = "";
                createNoticeNoFile.classList.remove("hide");


                formWriteNotice.reset(); // 없어도 되는 것 같은데...
                modalWriteNotice.classList.add("hide");
            });
        });

    }
// }