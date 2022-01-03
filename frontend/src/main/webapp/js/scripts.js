const URL = "https://192.168.75.56/api/v3";
let token;
let login;
let ballotId;
let templates = ["#index-template", "#monCompte-template", "#candidats-template", "#vote-template"];

window.onload = function () {
    for (const template of templates) {
        Handlebars.compile($(template).html());
    }

    $('#toggle-btn').click(() => {
        $('#navigation').slideToggle();
    });

    $('#navigation li a').on('click', function () {
        $($(this).attr('href')).addClass('active').siblings().removeClass('active');
    });

    hideConnectSections();

    buildIndex();
    $("#vote-form").hide();
    $("#vote-div").html("Vous devez être connecté pour voter.");
}

/**
 * Affiche la section courante et et cache toute les autres.
 */
function showSection() {
    $($(this).attr('href')).addClass('active').siblings().removeClass('active');
}

/**
 * Affiche le menu incomplet pour les utilisateurs non connectés.
 */
function hideConnectSections() {
    $('a[href$="monCompte"]').hide();
    $('a[href$="vote"]').hide();
    $('a[href$="ballot"]').hide();
    $('a[href$="deco"]').hide();
}

/**
 * Affiche le menu complet pour les utilisateurs connectés.
 */
function showConnectSections() {
    $('a[href$="monCompte"]').show();
    $('a[href$="vote"]').show();
    $('a[href$="ballot"]').show();
    $('a[href$="deco"]').show();
}

/**
 * Récupère les données nécessaire à l'affichage de l'index.
 */
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

setTimeout("buildIndex();", 5000);

/**
 * Fonction générique qui permet de fetch les candidats.
 *
 * @param hash la section.
 * @param type la balise.
 */
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
                        nom: decodeURI(data[i].replace('election/candidats/', '')).replaceAll('+', ' ')
                    };
                } else {
                    candidat = {
                        nom: decodeURI(data[i].replace('election/candidats/', '')).replaceAll('+', ' ')
                    };
                }
                candidats.push(candidat);
            }
            buildTemplate(`${hash}-template`, candidats, hash, type);
        });
}

/**
 * Fonction de routage.
 */
window.addEventListener('hashchange', () => {
    let hash = window.location.hash;
    let target = hash.replace('#', '').toString();
    if (templates.indexOf(`${hash}-template`) >= 0 || hash.includes("candidats/") || hash.includes("ballot")) {
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
                    $("#supp-vote-input").prop('disabled', false);
                    $("#preuve-vote").html("Votre vote a bien été enregistré.");
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    $("#supp-vote-input").prop('disabled', true);
                    $("#preuve-vote").html("Vous n'avez pas encore voté.");
                }
            })
        } else { // candidat
            hash = "#candidat"
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
    show(hash);
});

/**
 * Connexion
 */
$('#login-form').on('submit', function (e) {
    e.preventDefault();
    if (validate()) {
        $("#loginInput").removeClass('error');
        $("#nomInput").removeClass('error');
        let loginForm = document.forms.namedItem("login-form");
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
                token = request.getResponseHeader("authorization").replace("Bearer ", "");
                login = formData.get('login');
                $("#decoSpan").html("Cliquez ici pour vous déconnecter :");
                $("#decoBouton").show();
                $("#login-form").hide();
                $("#login-div").html("Vous êtes déjà connecté.");
                $("#vote-div").html("");
                $("#vote-form").show();
                showConnectSections();
            });
    }
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
        dataType: "json",
        success: function (res) {
            console.log("tst", nom)
            $('#nom').html(nom);
        }
    })
});

/**
 * Vote
 */
$('#vote-form').on('submit', function (e) {
    e.preventDefault();
    let voteForm = document.forms.namedItem("vote-form");
    let formData = new FormData(voteForm);
    let data = JSON.stringify(Object.fromEntries(formData));
    $.ajax({
        type: "POST",
        url: URL + "/election/ballots",
        contentType: "application/json",
        data: data,
        headers: {"Content-Type": "application/json", "Authorization": `${token}`},
        success: function (res) {
            $("#vote-form").hide();
            $("#vote-div").html("Vous avez déjà voté");
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert("Une erreur est survenue: " + textStatus + ": " + errorThrown);
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
            $("#supp-vote-input").prop('disabled', true);
            $("#preuve-vote").html("Vous n'avez pas encore voté.");
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
        headers: {"Content-Type": "application/json", "Authorization": `${token}`},
        dataType: "json",
    })
        .done(() => {
            login = null;
            token = null;
            $("#decoSpan").html("Vous êtes déconnecté.");
            $("#decoBouton").hide();
            $("#login-div").html("");
            $("#login-form").show();
            $("#vote-form").hide();
            $("#vote-div").html("Vous devez être connecté pour voter.");
            hideConnectSections();
        });
});

/**
 * Render les template.
 *
 * @param script l'id du template
 * @param data les données fetch ou mockées
 * @param elt la section
 * @param type la balise
 */
function buildTemplate(script, data, elt, type) {
    let template = $(script).html();
    Mustache.parse(template);
    let rendered = Mustache.render(template, data);
    $(`${elt} ${type}`).html(rendered);
}

/**
 * Affiche les sections.
 *
 * @param hash le nom de la section
 */
function show(hash) {
    $(hash)
        .addClass('active').siblings().removeClass('active');
}

/**
 * Valide le formulaire de connexion.
 *
 * @returns {boolean}
 */
function validate() {
    let login = document.forms["login-form"]["login"].value;
    let nom = document.forms["login-form"]["nom"].value;
    if (login == "" || nom == "") {
        $("#loginInput").addClass('error');
        $("#nomInput").addClass('error');
        alert("Le login et le nom doivent être renseignés");
        return false;
    }
    return true;
}

function validation() {
    return false;
}

//$('input').attr("contentEditable", "true"); on retire pour pouvoir disable des boutons.
