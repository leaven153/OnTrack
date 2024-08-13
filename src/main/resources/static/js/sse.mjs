// const sseTest = new EventSource(`http://localhost:8080/connect`);
//
// sseTest.addEventListener("connect", (e)=>{
//     const {data: receivedConnectData} = e;
//     console.log(`받은 데이터: ${receivedConnectData}`);
// });

// const chkNoticeComment = async () => {
//     await fetch(`http://localhost:8080/login`, {
//         method: 'POST',
//         headers: {}
//     }).then(response => {
//         console.log(response);
//     });
// }
if(document.querySelector("#userIdforSse") !== undefined && document.querySelector("#userIdforSse") !== null){
    const userId = document.querySelector("#userIdforSse").dataset.userid;
    const sse = await new EventSource(`http://localhost:8080/commentListener/${userId}`);
// sse.onerror =  () => {
//     console.log(`이렇게 하면?`);
// };

    sse.addEventListener("noticeComment", (e)=>{
        const {data: receivedIdMap} = e;

        console.log(userId);
        console.log(e);
        console.log(`notice comment가 등록된 task: ${receivedIdMap}`);
        const mapDataTest = JSON.parse(receivedIdMap);
        console.log(mapDataTest);
        // console.log(mapDataTest["commentId"]);
        /**
         * Array [ {…} ]
         * ​
         * 0: Object { commentId: 24, memberId: 9, userId: 47, … }
         * ​
         * length: 1
         * Object { id: null, commentId: 24, memberId: 9, checked: null }
         * checked: null
         * commentId: 24
         * id: null
         * memberId: 9*/
        // console.log(mapDataTest.size); // undefined
        console.log(mapDataTest[0]); // undefined


        document.querySelectorAll("a").forEach(function(btn){
            btn.addEventListener("click", ()=>{
                sse.close();
            });
        })
    });
}


/*
const testFncSse = async ()=>{
    const sse = await new EventSource(`http://localhost:8080/commentListener`);
// sse.onerror =  () => {
//     console.log(`이렇게 하면?`);
// };

    sse.addEventListener("noticeComment", (e)=>{
        const {data: receivedIdMap} = e;
        const userId = document.querySelector("#userIdforSse");
        console.log(userId);
        console.log(e);
        console.log(`notice comment가 등록된 task: ${receivedIdMap}`);
        const mapDataTest = JSON.parse(receivedIdMap);
        console.log(mapDataTest);
        console.log(mapDataTest["commentId"]);

         // * Object { id: null, commentId: 24, memberId: 9, checked: null }
         // * checked: null
         // * commentId: 24
         // * id: null
         // * memberId: 9
        console.log(mapDataTest.size); // undefined
        console.log(mapDataTest[0]); // undefined


    });

};*/
//
// window.onload = function() {
//     testFncSse();
// }
