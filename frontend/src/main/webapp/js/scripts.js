const URL = "https://192.168.75.56/api/v3";
let token;
let tkn;
let login;
let ballotId;

window.onload = function () {
    document.getElementById('toggle-btn').addEventListener('click', () => {
    });

    $('#navigation li a').on('click', function () {
        $($(this).attr('href')).addClass('active').siblings().removeClass('active');
    });

    hideConnectSections();

    buildIndex();
    $("#vote-form").hide();
    $("#vote-div").html("Vous devez être connecté pour voter.");
}

function showSection() {
    $($(this).attr('href')).addClass('active').siblings().removeClass('active');
}

function hideConnectSections() {
    $('a[href$="monCompte"]').hide();
    $('a[href$="vote"]').hide();
    $('a[href$="ballot"]').hide();
    $('a[href$="deco"]').hide();
}

function showConnectSections() {
    $('a[href$="monCompte"]').show();
    $('a[href$="vote"]').show();
    $('a[href$="ballot"]').show();
    $('a[href$="deco"]').show();
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
                if (login != null) {
                    candidat = {
                        connecte: true,
                        nom: data[i].replace('election/candidats/', '')
                    };
                } else {
                    candidat = {
                        nom: data[i].replace('election/candidats/', '')
                    };
                }
                candidats.push(candidat);
            }
            buildTemplate(`${hash}-template`, candidats, hash, type);
        });
}

setTimeout("buildIndex();", 5000);

let templates = ["#index-template", "#monCompte-template", "#candidats-template", "#vote-template", "#ballot-template", "#candidat-connecte-template"];
window.addEventListener('hashchange', () => {
    let hash = window.location.hash;
    let target = hash.replace('#', '').toString();
    if (templates.indexOf(`${hash}-template`) >= 0 || hash.includes("candidats/")) {
        if (target === "index") {
            buildIndex();
        } else if (target === "monCompte") {
            $.ajax({
                type: "GET",
                url: URL + `/users/` + login,
                headers: {"Authorization": `${token}`, "Accept": "application/json"},
                dataType: "json"
            })
                .done((res, textStatus, request) => {
                    buildTemplate(`${hash}-template`, res, hash, 'ul');
                });
        } else if (target === "candidats") {
            getCandidats(hash, 'ul');
        } else if (target === "vote") {
            getCandidats(hash, 'select');
        } else if (target == "ballot") {
            $.ajax({
                type: "GET",
                url: URL + `/election/ballots/byUser/` + login,
                headers: {"Authorization": `${token}`, "Accept": "application/json"},
                dataType: "json",
                success: function (res) {
                    ballotId = res.id;
                    document.getElementById("supp-vote-input").disabled = false;
                    $("#preuve-vote").html("Votre vote a bien été enregistré.");
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    document.getElementById("supp-vote-input").disabled = true;
                    $("#preuve-vote").html("Vous n'avez pas encore voté.");
                }
            })
        } else { // candidat
            hash = "#candidat";
            $.ajax({
                type: "GET",
                url: URL + `/election/` + target,
                headers: {"Authorization": `${token}`, "Accept": "application/json"},
                dataType: "json",
                success: function (res) {
                    buildTemplate(`${hash}-template`, res, hash, 'p');
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    alert("Une erreur est survenue: " + textStatus);
                }
            })
        }
    }
    console.log("hash : " + hash);
    show(hash);
});

/**
 * Connexion
 */
let loginForm = document.forms.namedItem("login-form");
$('#login-form').on('submit', function (e) {
    e.preventDefault();
    loginForm = document.forms.namedItem("login-form");
    let formData = new FormData(loginForm);
    formData.append('admin', !!formData.get('admin'));
    let data = JSON.stringify(Object.fromEntries(formData));
    $.ajax({
        type: "POST",
        url: URL + "/users/login",
        contentType: "application/json",
        data: data,
        headers: {"Content-Type": "application/json", "Authorization": `${token}`},
        dataType: "json"
    })
        .done((data, textStatus, request) => {
            tkn = request.getResponseHeader("authorization");
            token = tkn.replace("Bearer ", "");
            login = formData.get('login');
            window.location.assign(window.location.origin + "/#monCompte");
            $("#login-form").hide();
            $("#login-div").html("Vous êtes déjà connecté.");
            $("#vote-div").html("");
            $("#vote-form").show();
            showConnectSections();
        });
});
/**
 * Mon Compte
 */
$('#change-name-form').on('submit', function (e) {
    e.preventDefault();
    let changeNameForm = document.forms.namedItem("change-name-form");
    let formData = new FormData(changeNameForm);
    let data = JSON.stringify(Object.fromEntries(formData));
    let nom = document.forms["change-name-form"]["nom"].value;
    $.ajax({
        type: "PUT",
        url: URL + "/users/" + login + "/nom",
        contentType: "application/json",
        data: data,
        headers: {"Content-Type": "application/json", "Authorization": `${token}`},
        dataType: "json"
    })
        .done((data, textStatus, request) => {
            $('#nom').html(nom);
        });
});

/**
 * Vote
 */
let voteForm = document.forms.namedItem("vote-form");
$('#vote-form').on('submit', function (e) {
    e.preventDefault();
    let formData = new FormData(voteForm);
    let data = JSON.stringify(Object.fromEntries(formData));
    $.ajax({
        type: "POST",
        url: URL + "/election/ballots",
        contentType: "application/json",
        data: data,
        headers: {"Content-Type": "application/json", "Authorization": `${token}`},
        success: function (res) {
            window.location.assign(window.location.origin + "/#ballot");
            $("#vote-form").hide();
            $("#vote-div").html("Vous avez déjà voté");
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert("Une erreur est survenue: " + textStatus);
        }
    })
});
/**
 * Suppression vote
 */
$('#supp-vote').on('submit', function (e) {
    e.preventDefault();
    $.ajax({
        type: "DELETE",
        url: URL + "/" + ballotId,
        headers: {"Authorization": `${token}`}
    })
        .done(() => {
            ballotId = null;
            window.location.assign(window.location.origin + "/#vote");
            $("#vote-div").html("");
            $("#vote-form").show();
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
            $("#login-div").html("");
            $("#login-form").show();
            $("#vote-form").hide();
            $("#vote-div").html("Vous devez être connecté pour voter.");
            hideConnectSections();
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
    return false;
}

//$('input').attr("contentEditable", "true"); on retire pour pouvoir disable des boutons.