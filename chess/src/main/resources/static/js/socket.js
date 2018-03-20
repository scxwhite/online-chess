var Socket = function () {
    var oSocket = new Object();
    var socket, isConnect = false, canStart = false;
    oSocket.send = function (x) {
        socket.send(JSON.stringify(x));
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
            isConnect = true;
        };
        /**
         * 收到消息的回调方法
         * @param event
         */
        socket.onmessage = function (event) {
           var data = JSON.parse(event.data);
           if (data.type === "start") {
               canStart = true;
               console.log("可以开始了")
           }
           console.log(event)
           console.log(data)
        };

        socket.onclose = function () {
            console.log("close")
            isConnect = false;
        };

        window.onbeforeunload = function () {
            socket.close();
        };
    };
    oSocket.connected = function () {
        return isConnect;
    };
    oSocket.close = function () {
        socket.close();
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