let sideBar = null;
let isDisplayed = true;

window.onload = function () {
    document.getElementById('toggle-btn').addEventListener('click', show);
    sideBar = document.getElementById('side-bar');

    $('#navigation li a').on('click', function () {
        $($(this).attr('href')).addClass('active').siblings().removeClass('active');
    });
}

function show() {
    if (isDisplayed) {
        sideBar.classList.remove('active');
    } else {
        sideBar.classList.toggle('active');
    }
    isDisplayed = !isDisplayed;
}

function traiteXML(XMLDoc, id) {
    console.log(XMLDoc.documentElement.textContent);
    console.log(XMLDoc.getElementsByTagName("h")[0].childNodes[0].nodeValue);
    document.getElementById(id).innerHTML = XMLDoc.documentElement.t;
    document.getElementById(id).innerHTML = XMLDoc.documentElement.textContent;
}
