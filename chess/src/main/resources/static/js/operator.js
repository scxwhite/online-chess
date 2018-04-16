var myName , otherName;
window.onload = function () {
    var windowWidth = window.screen.availWidth;
    var windowHeight = window.screen.availHeight;
    var chessGame = new ChessGame();
    $('#canvas').css({"width": windowWidth * 0.4, "height": windowHeight * 0.7});
    $('#chat').css({"width": windowWidth * 0.2, "height": windowHeight * 0.6});
    $('#userInfo').css({"width": windowWidth * 0.2, "height": windowHeight * 0.7});
    chessGame.start();
    if (!socket.connected()) {
        socket.connect("ws://"+ getCookie("address")+":8080/websocket?id=" + getCookie("username"));
    }
    $('#prepare').on("click", function () {
        if (!socket.connected()) {
            setText("服务器连接中。。。", 2);
            return;
        }
        //排队中 动画
        setText("正在匹配中...", 1);
        //请求变为准备状态
        socket.send(new socketMessage("prepare", null));
    });

    $('#confirmSend').on('click', function () {
        var msg = $('#msgText').val();
        if (msg === undefined || msg === null || msg === "") {
            return;
        }
        socket.send(new socketMessage("chat", msg));
        toChat(msg, myName.username);
    });



    /**
     * 收到消息的回调方法
     * @param event
     */
    webSocket.onmessage = function (event) {
        var data = JSON.parse(event.data);
        console.log(data)
        if (data.type === "start") {
            if (data.content === "you first") {
                turnMe = true;
            } else {
                turnMe = false;
            }
            setHeadImage(data.extra)
            startGame();
        } else if (data.type === "move") {
            var content = data.content.split(";");
            var firstPoint = content[0].split(",");
            var secondPoint = content[1].split(",");
            var pointX = new Coordinate(parseInt(firstPoint[0]), parseInt(firstPoint[1]));
            var pointY = new Coordinate(parseInt(secondPoint[0]), parseInt(secondPoint[1]));
            chessGame.chessMove(pointX, pointY, otherUser);
        } else if (data.type === "leave") {
            setText("由于对方离开，恭喜你 ，你赢了", 2);
        } else if (data.type === "chat") {
            toChat(data.content, otherName.username);
        } else if (data.type === "createRoom") {
            loadGame();
            setHeadImage(null);
        } else if(data.type === "enterRoom") {
            loadGame();
            setHeadImage(data.content);
        } else if (data.type === "error") {
            setText(data.content, 2);
        }
    };

    /**
     * 开始游戏
     */
    function startGame() {
        if (turnMe) {
            setText("我方先手", 2);
        } else {
            setText("对方先手", 2);
        }
        chessGame.start();
    }

    $('#changeUser').on('click', function () {
        var tmp = myUser;
        myUser = otherUser;
        otherUser = tmp;
    });
};

/**
 * 设置头像
 * @param id
 */
function setHeadImage(id) {

    if (id != null) {
        //对手信息
        $.ajax({
            url: "/user/headImage",
            data: {
                id: id
            },
            type: "get",
            success: function (data) {
                if (data.success == true) {
                    otherName = data.result;
                    $('#other img:first').attr("src", otherName.imageUrl);
                    $('#other p:first').text("昵称：" + otherName.username);
                    toChat(otherName.username + "加入游戏", "系统提示");
                }
            }
        });
    }


    //我的信息
    $.ajax({
        url: "/user/headImage",
        data: {
            id: getCookie("username")
        },
        type: "get",
        success: function (data) {
            if (data.success == true) {
                myName = data.result;
                $('#myself img:first').attr("src", myName.imageUrl);
                $('#myself p:first').text("昵称：" + myName.username);
            }
        }
    });
}
function toChat(msg, user) {
    $('#allMsg').text($('#allMsg').text() + "\n" + "[" + user + "]:\t" + msg + "\n" + getLocalTime(new Date().getTime()));
}
function getLocalTime(nS) {
    return new Date(parseInt(nS)).toLocaleString().replace(/年|月/g, "-").replace(/日/g, " ");
}
