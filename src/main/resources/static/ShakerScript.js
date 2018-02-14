const player = {
    playerName: "player",
    texture:    "knight",
    playerId:   "-1"
};

let ready = false;

function run() {
    if (window.DeviceMotionEvent != undefined) {
        let ax = 0, ay = 0;

        window.ondevicemotion = function () {
            ax = event.acceleration.x;
            ay = event.acceleration.y;

            sendData(ax, ay);
        };
    } else
        alert("Датчики положения не поддерживаются =( ");
}

function sendData(ax, ay) {
    let post = new XMLHttpRequest();
    post.open("POST", "/play", true);
    post.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');

    let params = 'id=' + player.playerId + '&ax=' + ax + '&ay=' + ay;
    post.send(params);

    post.onreadystatechange = function () {
        if (post.readyState == 4) {
            if (post.responseText == "finish") {
                document.getElementById("wait_page").style.visibility = 'hidden';
                document.getElementById("play_page").style.visibility = 'hidden';
                document.getElementById("finish_page").style.visibility = 'visible';
            }
        }
    }
}

function signUp(name, tex) {
    if (name === "") {
        alert("Ошибка! Введено пустое имя!");
        return false
    } else {
        document.getElementById("sign_up_form").style.visibility = 'hidden';
        document.getElementById("wait_page").style.visibility = 'visible';

        player.playerName = name;
        player.texture = tex;

        let post = new XMLHttpRequest();
        post.open("POST", "/sign_in", true);
        post.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');

        let params = 'name=' + player.playerName + '&texture=' + player.texture;
        post.send(params);

        post.onreadystatechange = function () {
            if (post.readyState == 4) {
                let tempId = post.responseText;
                if (tempId != "-1") {
                    player.playerId = tempId;
                    document.getElementById("wait_page").style.visibility = 'hidden';
                    document.getElementById("play_page").style.visibility = 'visible';
                    ready = true;
                    run();
                } else alert("Ошибка! Игра уже началась!")
            }
        }
    }
}
