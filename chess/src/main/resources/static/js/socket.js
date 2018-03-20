var webSocket;
var Socket = function () {
    var oSocket = new Object();
    var isConnect = false, canStart = false;
    oSocket.send = function (x) {
        webSocket.send(JSON.stringify(x));
    };
    oSocket.connect = function (url) {
        if ('WebSocket' in window) {
            webSocket = new WebSocket(url);
        } else {
            console.log("Sorry, Not Support Socket");
        }
        webSocket.onerror = function () {
            console.log("error");
        };

        webSocket.onopen = function (event) {
            console.log("open")
            isConnect = true;
        };

        webSocket.onclose = function () {
            console.log("close")
            isConnect = false;
        };

        window.onbeforeunload = function () {
            webSocket.close();
        };
    };
    oSocket.connected = function () {
        return isConnect;
    };
    oSocket.close = function () {
        webSocket.close();
    };
    oSocket.canStart = function () {
        return canStart;
    };
    return oSocket;
};
var socket = new Socket();
function socketMessage(type, content) {
    this.type = type;
    this.content = content;
}