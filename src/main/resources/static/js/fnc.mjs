function parents(el, selector) {
    const parents = [];
    while((el = el.parentNode) && el !== document){
        if(!selector || el.matches(selector)) parents.push(el);
    }
    return parents;
}

function elExists(el){
    return el !== undefined && el !== null;
}

function dateYYMMDD(date){
    const YY = date.getFullYear().toString().substring(2);
    let MM = date.getMonth()+1;
    if(MM < 10) { MM = `0${MM}`}
    let DD = date.getDate();
    if(DD < 10) { DD = `0${DD}`; }

    return `${YY}.${MM}.${DD} `;
}

function timeHHMM(date){
    let hh = date.getHours();
    if(hh < 10) { hh = `0${hh}`; }
    let mm = date.getMinutes();
    if(mm < 10) { mm = `0${mm}`; }
    return `${hh}:${mm}`;
}

/*---------- 052 ------------*/
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

/*---------- 028 ------------*/
// 파일 유효성검사(파일명 공백, 특수문자 금지, 사이즈 5MB 이하, 첨부불가 유형 여부)
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

/*---------- 025 ------------*/
// create task or notice
// 모달(할 일): 첨부 파일 정보담을 div 동적 생성
function createAndAttachFileBox(name, type, size) {
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
    btnDel.className = "btn-del-file";

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

export { parents, elExists, dateYYMMDD, timeHHMM, onEvtListener, validateFile, createAndAttachFileBox };

