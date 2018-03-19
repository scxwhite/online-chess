var Socket = function () {
    var oSocket = new Object();
    var socket;
    oSocket.send = function (x) {
        socket.send(x);
    };
    oSocket.connect = function (url) {
        if ('WebSocket' in window) {
            socket = new WebSocket(url);
        } else {
            console.log("Sorry, Not Support Socket");
        }
        socket.onerror = function () {
            console.log("error");
        };
        socket.onopen = function (event) {
            console.log("open")
        };
        socket.onmessage = function (event) {
            console.log("收到的消息：" + event)
        };
        socket.onclose = function () {
            console.log("close")
        };
        window.onbeforeunload = function () {
            socket.close();
        };
    };
    oSocket.close = function () {
        socket.close();
    }
    return oSocket;
};