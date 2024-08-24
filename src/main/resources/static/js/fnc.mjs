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

export { parents, elExists, dateYYMMDD, timeHHMM, onEvtListener };

