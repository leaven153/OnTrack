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


export { parents, elExists };

