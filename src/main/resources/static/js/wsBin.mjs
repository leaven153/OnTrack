let binSocket = new SockJS(`http://localhost:8080/ws/taskDeletion`);

window.onload = function(){
    connectBinWs();
}
function connectBinWs() {

    const sock = new SockJS(`http://localhost:8080/ws/taskDeletion`);
    // socket = sock;

    sock.onopen = function () {
        console.log(`Bin socket connected`);
        if(document.querySelector("[class^='icon-bin-']") !== undefined && document.querySelector("[class^='icon-bin-']") !== null){
            document.querySelectorAll("[class^='icon-bin-']").forEach(function(icon){
                if(icon.classList.contains("hide")){
                    const fileName = icon.src;
                    console.log(fileName);
                }
            });
        }
    }

    sock.onmessage = function (e) {
        console.log(e.data);

    }

    sock.close = function(){
        console.log('bin connect close');
    }

    sock.onerror = function(err){
        console.log(`error occured: ${err}`);
    }
}


export { binSocket, connectBinWs };