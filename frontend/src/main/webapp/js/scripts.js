const URL = "https://192.168.75.56/api/v3";
let token;
let tkn;
let login;

window.onload = function () {
    document.getElementById('toggle-btn').addEventListener('click', () => {
        $('#navigation').slideToggle();
    });

    $('#navigation li a').on('click', function () {
        $($(this).attr('href')).addClass('active').siblings().removeClass('active');
    });

    buildIndex();
}

function showSection() {
    $($(this).attr('href')).addClass('active').siblings().removeClass('active');
}

function buildIndex() {
    $.ajax({
        type: "GET",
        url: URL + `/election/resultats`,
        dataType: "json",
    })
        .done((res, textStatus, request) => {
            buildTemplate(`#index-template`, res, '#index', 'ul');
        });
}

function getCandidats(hash, type) {
    $.ajax({
        type: "GET",
        url: URL + `/election/candidats`,
        dataType: "json",
    })
        .done((data) => {
            let candidats = [];
            for (let i = 0; i < data.length; i++) {
                candidat = {
                    nom: data[i].replace('election/candidats/', '')
                };
                candidats.push(candidat);
            }
            buildTemplate(`${hash}-template`, candidats, hash, type);
        });
}

setTimeout("buildIndex();", 5000);

let templates = ["#index-template", "#monCompte-template", "#candidats-template", "#vote-template"];
window.addEventListener('hashchange', () => {
    let hash = window.location.hash;
    let target = hash.replace('#', '').toString();
    if (templates.indexOf(`${hash}-template`) >= 0) {
        if (target === "index") {
            buildIndex();
        } else if (target === "monCompte") {
            console.log("monCompte")
            $.ajax({
                type: "GET",
                url: URL + `/users`,
                headers: {"Authorization": `${tkn}`, "Accept": "application/json"},
                dataType: "json"
            })
                .done((res, textStatus, request) => {
                    buildTemplate(`${hash}-template`, res, hash);
                });
        } else if (target === "candidats") {
            getCandidats(hash, 'ul');
        } else if (target === "vote") {
            getCandidats(hash, 'select');
        }
    }
    console.log("hash : " + hash);
    show(hash);
});

/**
 * Connexion
 */
$('#login-form').on('submit', function (e) {
    e.preventDefault();
    console.log("hellooo1");
    let loginForm = document.forms.namedItem("login-form");
    let formData = new FormData(loginForm);
    console.log(loginForm)
    formData.append('login', '')
    formData.append('nom', '');
    formData.append('admin', false);
    let data = JSON.stringify(Object.fromEntries(formData));
    console.log(data)
    document.getElementById('login-form').innerText = "test";
    $.ajax({
         type: "POST",
         url: URL + "/users/login",
         contentType: "application/json",
         data: data,
         headers: {"Content-Type": "application/json", "Authorization": `${tkn}`},
         dataType: "json",

     })
         .done((data, textStatus, request) => {
             tkn = request.getResponseHeader("authorization");
             token = tkn.replace("Bearer ", "");
             login = $("#user-login").val();
             window.location.assign(window.location.origin + "/#monCompte");
             $("#login-form").hide();
         });
});

/**
 * Déconnexion
 */
$('#deco').on('submit', function (e) {
    e.preventDefault();
    $.ajax({
        type: "POST",
        url: URL + "/users/logout",
        contentType: "application/json",
        headers: {"Content-Type": "application/json", "Authorization": `${tkn}`},
        dataType: "json",
    })
        .done(() => {
            window.location.assign(window.location.origin + "/#index");
            login = null;
            token = null;
            tkn = null
            $("#login-form").show();
        });
});

function buildTemplate(script, data, elt, type) {
    let template = $(script).html();
    Mustache.parse(template);
    let rendered = Mustache.render(template, data);
    $(`${elt} ${type}`).html(rendered);
}

function show(hash) {
    $(hash)
        .addClass('active').siblings().removeClass('active');
}

function validation() {
    console.log("hellooo");
    return false;
}

$('input').attr("contentEditable", "true");