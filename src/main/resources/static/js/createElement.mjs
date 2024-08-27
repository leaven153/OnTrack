function binRow(taskInfo, userId){

    // 0. row 컨테이너
    const divContainer = document.createElement("div")
    divContainer.classList.add("my-bin-col", "flex-row-justify-start-align-center");

    // (체크박스)
    const divChkBox = document.createElement("div");
    divChkBox.classList.add("content-chkbox");
    const inputChkBox = document.createElement("input");
    inputChkBox.setAttribute("type", "checkbox");
    divChkBox.appendChild(inputChkBox);

    // 1. 삭제일시
    const divDeletedDate = document.createElement("div");
    const spanDeletedDate = document.createElement("span");
    spanDeletedDate.classList.add("altivo-regular")
    spanDeletedDate.innerText = taskInfo["deletedAt"].slice(0, 10) + " " + taskInfo["deletedAt"].slice(11, 16);
    divDeletedDate.appendChild(spanDeletedDate);

    // 2. 할 일 명
    const divTaskTitle = document.createElement("div");
    const spanTaskTitle = document.createElement("span");
    spanTaskTitle.classList.add("pd-l5");
    spanTaskTitle.innerText = taskInfo["taskTitle"];
    divTaskTitle.appendChild(spanTaskTitle);

    // 3. 위치(프로젝트명)
    const divProjectName = document.createElement("div");
    const spanProjectName = document.createElement("span");
    spanProjectName.innerText = taskInfo["projectName"];
    divProjectName.appendChild(spanProjectName);

    // 4. 삭제한 사람
    const divDeletedBy = document.createElement("div");
    const spanDeletedBy = document.createElement("span");
    spanDeletedBy.innerText = taskInfo["deleterName"];
    divDeletedBy.appendChild(spanDeletedBy);

    // 5. more 컨테이너
    const divMoreContainer = document.createElement("div");
    divMoreContainer.classList.add("flex-row-justify-start-align-center", "btn-bin-disposal");

    // 5-1. more 버튼
    const iconBtnMore = document.createElement("img");
    iconBtnMore.src = "/imgs/icon_3dot(h).png";
    // iconBtnMore.setAttribute("th:src", `@{/imgs/icon_3dot(h).png}`);
    iconBtnMore.classList.add("img-20");

    // 5-2. more box
    const divMoreBox = document.createElement("div");
    divMoreBox.classList.add("bin-disposal-box", "hide");

    // 영구삭제... 휴지통을 보고 있던 유저가 관리자일 수도 있다.
    // 영구삭제를 바로 가능하게 할 것인가? 말 것인가...
    // ☞ 관리자라도, 담당자가 2명 이상인 할 일의 즉시 영구삭제는 안된다.
    // 담당자가 여러 명이라면 어느 정도 후에 영구삭제 실행이 가능하게 할 것인가?
    // const btnDelete = document.createElement("button");
    // btnDelete.classList.add("flex-row-center-center-nowrap", "mb-5");
    //
    // const iconBtnDelete = document.createElement("img");
    // const spanBtnDelete = document.createElement("span");

    // 5-2-1. 복원하기 버튼
    const btnRestore = document.createElement("button");
    btnRestore.classList.add("flex-row-center-center-nowrap");
    btnRestore.setAttribute("onclick", `location.href='/task/restore?pId=${taskInfo["projectId"]}&tId=${taskInfo["taskId"]}&uId=${userId}'`);

    const iconBtnRestore = document.createElement("img");
    iconBtnRestore.src = "/imgs/icon_history(boldBrown).png";
    // iconBtnRestore.setAttribute("th:src", `@{/imgs/icon_history(boldBrown).png}`);
    iconBtnRestore.classList.add("img-15");

    const spanBtnRestore = document.createElement("span");
    spanBtnRestore.innerText = "복원하기";

    btnRestore.appendChild(iconBtnRestore);
    btnRestore.appendChild(spanBtnRestore);
    divMoreBox.appendChild(btnRestore);

    divMoreContainer.appendChild(iconBtnMore);
    divMoreContainer.appendChild(divMoreBox);

    divContainer.appendChild(divChkBox);
    divContainer.appendChild(divDeletedDate);
    divContainer.appendChild(divTaskTitle);
    divContainer.appendChild(divProjectName);
    divContainer.appendChild(divDeletedBy);
    divContainer.appendChild(divMoreContainer);

    return divContainer;
}

export { binRow }