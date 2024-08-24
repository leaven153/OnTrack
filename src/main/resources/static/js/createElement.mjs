function binRow(taskInfo){
    const divContainer = document.createElement("div")

    const divDeletedDate = document.createElement("div");
    const spanDeletedDate = document.createElement("span");
    spanDeletedDate.classList.add("altivo-regular")
    spanDeletedDate.innerText = taskInfo["deletedAt"].slice(0, 10) + " " + taskInfo["deletedAt"].slice(11, 16);
    divDeletedDate.appendChild(spanDeletedDate);

    const divTaskTitle = document.createElement("div");
    const spanTaskTitle = document.createElement("span");
    spanTaskTitle.classList.add("pd-l5");
    spanTaskTitle.innerText = taskInfo["taskTitle"];
    divTaskTitle.appendChild(spanTaskTitle);

    const divProjectName = document.createElement("div");
    const spanProjectName = document.createElement("span");
    spanProjectName.innerText = taskInfo["projectName"];
    divProjectName.appendChild(spanProjectName);

    const divDeletedBy = document.createElement("div");
    const spanDeletedBy = document.createElement("span");
    spanDeletedBy.innerText = taskInfo["deleterName"];
    divDeletedBy.appendChild(spanDeletedBy);

    const divMoreContainer = document.createElement("div");
    divMoreContainer.classList.add("flex-row-justify-start-align-center", "btn-bin-disposal");

    const iconBtnMore = document.createElement("img");
    iconBtnMore.src = "../../static/imgs/icon_3dot(h).png";
    iconBtnMore.setAttribute("th:src", `@{/imgs/icon_3dot(h).png}`);
    iconBtnMore.classList.add("img-20");

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

    const btnRestore = document.createElement("button");
    btnRestore.classList.add("flex-row-center-center-nowrap");
    // |location.href='@{|/task/restore?pId=${task.getProjectId()}&tId=${task.getTaskId()}&mId=${task.getMemberId()}|}'|
    // 복원하려면 해당 project에서의 내 member Id를 알아야 한다...
    btnRestore.setAttribute("th:onclick", ``);

    const iconBtnRestore = document.createElement("img");
    const spanBtnRestore = document.createElement("span");


}

export { binRow }