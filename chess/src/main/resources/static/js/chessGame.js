var myUser = 'black';
var otherUser = 'red';
var turnMe = true;
/**
 * 坐标对象
 * @param x    x坐标
 * @param y    y坐标
 */
function Coordinate(x, y) {
    this.x = x;
    this.y = y;
}

function changeUser() {
    var tmp = myUser;
    myUser = otherUser;
    otherUser = tmp;
}
/**
 * 设置文字
 * @param msg   文字内容
 * @param type  类型   1：不关闭文字  2：3s后关闭文字
 */
function setText(msg, type) {
    closeText();
    if (type === 1) {
        $('#gameHints').text(msg).css({"display":""}).beatText({isAuth:true,beatHeight:"1em",isRotate:false,upTime:100,downTime:100});;
    }

    if (type === 2) {
        $('#gameHints').text(msg).css({"display":""});
        setTimeout(closeText, 3000);
    }
}

function closeText() {
    $('#gameHints').css({"display" : "none"})
}
function record(id, res) {
    $.ajax({
        url: "/score/update",
        type: "post",
        data: {
            id: id,
            res: res
        },
        success: function (data) {
            console.log(data.message);
        }
    })
}
var ChessGame = function () {
    var oChessGame = new Object();
    var canvas_jquery = $("#canvas");
    var canvas = canvas_jquery[0];
    var canvasWidth = canvas_jquery.attr("width");
    var canvasHeight = canvas_jquery.attr("height");
    var ctx = canvas.getContext("2d");
    //当前象棋状态
    var chessStatus = [];
    //敌方老将的位置
    var enemyGeneral = new Coordinate(4, 0);
    //我方老将的位置
    var myGeneral = new Coordinate(4, 9);
    var signSize = 8;
    var signSpace = 3;
    var chessWidth = 50, chessHeight = 50;
    var fontSize = 40;
    //是否为第一次点击棋子
    var firstClick = true;
    //保存点击的棋子
    var clickChess;
    //棋盘的线个数
    var xNum = 9, yNum = 5 * 2;
    //棋线间隔93
    var space = 60;
    //有效的坐标
    var validCoordinate = [];
    //棋盘外框起始坐标
    var blackRect = new Coordinate(20, 20);
    //棋盘外边框的宽和高
    var blackRectWidth = 490, blackRectHeight = 550;
    //将棋盘视为以左上角 （10，10） 为圆点的坐标轴
    var start = new Coordinate(blackRect.x + 5, blackRect.y + 5);
    var end = new Coordinate(getCoordinateX(xNum - 1), getCoordinateY(yNum - 1));
    //连续将军的次数
    var chessNum = 0;


    //得到相对于棋盘的X坐标位置
    function getCoordinateX(num) {
        return start.x + space * num;
    }

    //得到相对于棋盘的Y坐标位置
    function getCoordinateY(num) {
        return start.y + space * num;
    }
    /**
     * 判断是否过河
     **/
    function crossRiver(isOther, point) {
        if (isOther && point.y > 4) return true;
        if (!isOther && point.y <= 4) return true;
        return false;
    }
    /**
     * 游戏结束
     **/
    function gameOver(msg) {
        setText(msg, 2);
        initData();
        initUI();
    }
    //在 (x,y)处绘制图片image
    function drawImage(image, x, y) {
        ctx.drawImage(image, getCoordinateX(x) - chessWidth / 2, getCoordinateY(y) - chessHeight / 2, chessWidth, chessHeight);
    }
    /**
     * 绘制棋盘线
     * @param x_s    起始坐标x
     * @param y_s    起始坐标y
     * @param x_e    结束坐标x
     * @param y_e    结束坐标y
     */
    function drawLine(x_s, y_s, x_e, y_e) {
        ctx.beginPath();
        ctx.moveTo(x_s, y_s);
        ctx.lineTo(x_e, y_e);
        ctx.stroke();
        ctx.save();
    }

    /**
     * 画*花标志
     * @param x    坐标x
     * @param y    坐标y
     */
    function sign(x, y, type) {
        //右边*花
        if (type != 0) {
            drawLine(x + signSpace, y + signSpace, x + signSpace, y + signSpace + signSize);
            drawLine(x + signSpace, y + signSpace, x + signSpace + signSize, y + signSpace);

            drawLine(x + signSpace, y - signSpace, x + signSpace, y - signSpace - signSize);
            drawLine(x + signSpace, y - signSpace, x + signSpace + signSize, y - signSpace);
        }

        //左边*花
        if (type != 1) {
            drawLine(x - signSpace, y - signSpace, x - signSpace, y - signSpace - signSize);
            drawLine(x - signSpace, y - signSpace, x - signSpace - signSize, y - signSpace);

            drawLine(x - signSpace, y + signSpace, x - signSpace, y + signSpace + signSize);
            drawLine(x - signSpace, y + signSpace, x - signSpace - signSize, y + signSpace);
        }
    }

    //绘制棋盘外边框
    function drawBlackRect() {
        ctx.strokeRect(blackRect.x, blackRect.y, blackRectWidth, blackRectHeight);
    }

    /**
     * 对选择的棋子画边框
     */
    function drawSelectTag(point) {
        //棋盘线条颜色
        ctx.strokeStyle = myUser;
        point = new Coordinate(getCoordinateX(point.x), getCoordinateY(point.y));
        var tagWidth = chessWidth / 2;
        var tagHeight = chessHeight / 2;
        //右下
        drawLine(point.x + tagWidth, point.y + tagHeight, point.x + tagWidth, point.y + tagHeight - signSize);
        drawLine(point.x + tagWidth, point.y + tagHeight, point.x + tagWidth - signSize, point.y + tagHeight);
        //右上
        drawLine(point.x + tagWidth, point.y - tagHeight, point.x + tagWidth - signSize, point.y - tagHeight);
        drawLine(point.x + tagWidth, point.y - tagHeight, point.x + tagWidth, point.y - tagHeight + signSize);
        //左上
        drawLine(point.x - tagWidth, point.y - tagHeight, point.x - tagWidth, point.y - tagHeight + signSize);
        drawLine(point.x - tagWidth, point.y - tagHeight, point.x - tagWidth + signSize, point.y - tagHeight);
        //左下
        drawLine(point.x - tagWidth, point.y + tagHeight, point.x - tagWidth + signSize, point.y + tagHeight);
        drawLine(point.x - tagWidth, point.y + tagHeight, point.x - tagWidth, point.y + tagHeight - signSize);
    }

    //绘制棋子
    function drawChess() {
        var point;
        for (var key in chessStatus) {
            if (key == null || chessStatus[key] == null) {
                continue;
            }
            point = key.split(",");
            drawImage($('#' + chessStatus[key])[0], point[0], point[1]);
        }
    }


    //绘制所有的*花
    function drawAllSign() {
        sign(getCoordinateX(1), getCoordinateY(2), 2);
        sign(getCoordinateX(1), getCoordinateY(7), 2);
        sign(getCoordinateX(7), getCoordinateY(2), 2);
        sign(getCoordinateX(7), getCoordinateY(7), 2);

        sign(getCoordinateX(0), getCoordinateY(3), 1);
        sign(getCoordinateX(2), getCoordinateY(3), 2);
        sign(getCoordinateX(4), getCoordinateY(3), 2);
        sign(getCoordinateX(6), getCoordinateY(3), 2);
        sign(getCoordinateX(8), getCoordinateY(3), 0);

        sign(getCoordinateX(0), getCoordinateY(6), 1);
        sign(getCoordinateX(2), getCoordinateY(6), 2);
        sign(getCoordinateX(4), getCoordinateY(6), 2);
        sign(getCoordinateX(6), getCoordinateY(6), 2);
        sign(getCoordinateX(8), getCoordinateY(6), 0);


    }

    //绘制棋盘
    function drawChessBoard() {
        var cnt_x = 0, cnt_y = 0;
        //横线
        for (var i = start.x; cnt_x < xNum; i = i + space, cnt_x++) {
            //绘制红方横线
            drawLine(i, start.y, i, getCoordinateY(4));
            //绘制黑方横线
            drawLine(i, getCoordinateY(5), i, end.y);
        }
        //竖线
        for (var j = start.y; cnt_y < yNum; j = j + space, cnt_y++) {
            drawLine(start.x, j, end.x, j);
        }
    }

    //绘制四条斜线
    function drawSlash() {
        drawLine(getCoordinateX(3), getCoordinateY(0), getCoordinateX(5), getCoordinateY(2));
        drawLine(getCoordinateX(3), getCoordinateY(7), getCoordinateX(5), getCoordinateY(9));
        drawLine(getCoordinateX(3), getCoordinateY(2), getCoordinateX(5), getCoordinateY(0));
        drawLine(getCoordinateX(3), getCoordinateY(9), getCoordinateX(5), getCoordinateY(7));
    }

    //绘制楚河汉界
    function drawFont() {
        ctx.save();
        ctx.font = fontSize + "px microsoft yahei";
        var size = fontSize / 2;
        ctx.fillText('楚', getCoordinateX(2) - size, getCoordinateY(5) - size);
        ctx.fillText('河', getCoordinateX(3) - size, getCoordinateY(5) - size);
        ctx.fillText('汉', getCoordinateX(5) - size, getCoordinateY(5) - size);
        ctx.fillText('界', getCoordinateX(6) - size, getCoordinateY(5) - size);
    }

    //保存有效的坐标位置
    function saveValidCoordinate() {
        var index;
        for (var i = 0; i < xNum; i++) {
            for (var j = 0; j < yNum - 1; j++) {
                index = i + "," + j;
                chessStatus[index] = null;
                validCoordinate[index] = new Coordinate(getCoordinateX(i), getCoordinateY(j));
            }
        }
    }

    /**
     *     保存初始时的象棋状态
     */
    function saveChessStatus() {
        chessStatus["0,3"] = otherUser + "_army";
        chessStatus["2,3"] = otherUser + "_army";
        chessStatus["4,3"] = otherUser + "_army";
        chessStatus["6,3"] = otherUser + "_army";
        chessStatus["8,3"] = otherUser + "_army";
        chessStatus["0,0"] = otherUser + "_car";
        chessStatus["8,0"] = otherUser + "_car";
        chessStatus["1,0"] = otherUser + "_horse";
        chessStatus["7,0"] = otherUser + "_horse";
        chessStatus["2,0"] = otherUser + "_elephant";
        chessStatus["6,0"] = otherUser + "_elephant";
        chessStatus["3,0"] = otherUser + "_soldier";
        chessStatus["5,0"] = otherUser + "_soldier";
        chessStatus["1,2"] = otherUser + "_cannon";
        chessStatus["7,2"] = otherUser + "_cannon";
        chessStatus["4,0"] = otherUser + "_general";
        chessStatus["0,6"] = myUser + "_army";
        chessStatus["2,6"] = myUser + "_army";
        chessStatus["4,6"] = myUser + "_army";
        chessStatus["6,6"] = myUser + "_army";
        chessStatus["8,6"] = myUser + "_army";
        chessStatus["0,9"] = myUser + "_car";
        chessStatus["8,9"] = myUser + "_car";
        chessStatus["1,9"] = myUser + "_horse";
        chessStatus["7,9"] = myUser + "_horse";
        chessStatus["2,9"] = myUser + "_elephant";
        chessStatus["6,9"] = myUser + "_elephant";
        chessStatus["3,9"] = myUser + "_soldier";
        chessStatus["5,9"] = myUser + "_soldier";
        chessStatus["1,7"] = myUser + "_cannon";
        chessStatus["7,7"] = myUser + "_cannon";
        chessStatus["4,9"] = myUser + "_general";
    }

    /**
     *     在 (x,y)处绘制图片image
     */
    function initUI() {
        //首先清除画布
        ctx.clearRect(0, 0, canvasWidth, canvasHeight);
        //棋盘线条颜色
        ctx.strokeStyle = "black";
        //棋盘外框线宽
        ctx.lineWidth = 4;
        //绘制棋盘外框
        drawBlackRect();
        //棋盘线宽
        ctx.lineWidth = 2;
        //绘制棋盘
        drawChessBoard();
        //绘制四条斜线
        drawSlash();
        //画*花标志
        drawAllSign();
        //绘制楚河汉界
        drawFont();
        //初始化棋盘位置
        drawChess();
    }
    function initData() {
        //保存有效坐标
        saveValidCoordinate();
        //保存当前的象棋的状态
        saveChessStatus();
    };
    oChessGame.start = function () {
        //初始化数据
        initData();
        //初始化界面
        initUI();
        //初始化事件
        initEvent();

        function initEvent() {
            canvas_jquery.on('click', function (e) {
                //得到距离鼠标最近的画布坐标
                var nearest = getNearestCoordinate(getPointOnCanvas(e.clientX, e.clientY));
                var currChess = chessStatus[nearest.x + "," + nearest.y];
                if (currChess === null) {
                    //如果已经选中己方棋子并且第一次点击空空棋盘位置 准备移动
                    if (!firstClick) {
                        firstClick = oChessGame.chessMove(clickChess, nearest, myUser);
                        //如果第一次点击空白区域  不做任何处理
                    }
                    //如果当前点击的棋子是当前用户
                } else if (currChess.split("_")[0] === myUser) {
                    clickChess = nearest;
                    if (firstClick === false) {
                        initUI();
                    }
                    firstClick = false;
                    drawSelectTag(nearest);
                } else if (currChess.split("_")[0] === otherUser) {
                    //尺子
                    if (!firstClick) {
                        oChessGame.chessMove(clickChess, nearest, myUser);
                    } else {
                        setText("只能移动" + myUser + "方棋子哦", 2);
                    }
                }
            });

        }


        /**
         * 四舍五入计算最近的棋盘位置
         * @param point    当前坐标
         **/
        function getNearestCoordinate(point) {
            return new Coordinate(Math.floor((point.x - start.x) / space + 0.5), Math.floor((point.y - start.y) / space + 0.5));
        }

        /**
         * 获得当前鼠标在canvas上的坐标
         *@param x    实际坐标
         *@param y    实际坐标
         *
         **/
        function getPointOnCanvas(x, y) {
            var rect = canvas.getBoundingClientRect();
            return {
                x: Math.round((x - rect.left) * (canvas.width / rect.width)),
                y: Math.round((y - rect.top) * (canvas.height / rect.height))
            };
        }
    };
    /**
     *
     * 准备棋子移动  首先校验棋子的有效性
     * clearRect会自动清除移动之前位置的所有棋子  所以不用判断此次移动是吃子还是移动
     * @param pointX 开始移动的位置
     * @param pointY 结束移动的位置
     * @param user  当前移动的用户
     **/
    oChessGame.chessMove = function (pointX, pointY, user) {
        console.log("我方老将位置" + JSON.stringify(myGeneral));
        console.log("敌方老将位置" + JSON.stringify(enemyGeneral));
        var chessName = chessStatus[pointX.x + "," + pointX.y];
        if (chessName === null || !canMove(chessName, pointX, pointY) || (user === myUser && !turnMe) || (user === otherUser && turnMe)) {
            return false;
        }
        //告知远程执行结果
        if (user === myUser && turnMe) {
            turnMe = false;
            var source = changePoint(pointX);
            var target = changePoint(pointY);
            socket.send(new socketMessage("move", source.x + "," + source.y + ";" + target.x + "," +target.y));
        }
        //切换到自己下子
        if (user === otherUser && !turnMe){
            turnMe = true;
        }

        chessStatus[pointX.x + "," + pointX.y] = null;
        chessStatus[pointY.x + "," + pointY.y] = chessName;
        drawImage($('#' + chessName)[0], pointY.x, pointY.y);
        //重绘棋盘
        initUI();
        ctx.save();
        //记录敌方老将的位置
        if (chessName === otherUser + "_general") {
            enemyGeneral = pointY;
        }
        //记录我方老将的位置
        if (chessName === myUser + "_general") {
            myGeneral = pointY;
        }
        //检验将军状态
        check();
        //判断胜利条件
        checkWin(pointY, user);


        return true;
    };
    function changePoint(point) {
        return new Coordinate(-point.x + xNum - 1, -point.y + yNum - 1);
    }

    /**
     * 是否将军操作检验
     */
    function check() {

        var chessName = "", point, tempGeneral;
        for (var key in chessStatus) {
            chessName = chessStatus[key];
            if (chessName === null) continue;
            point = new Coordinate(parseInt(key.split(",")[0]), parseInt(key.split(",")[1]));
            if (chessName.split("_")[0] === myUser) {
                tempGeneral = enemyGeneral;
            } else {
                tempGeneral = myGeneral;
            }
            if (canMove(chessName, point, tempGeneral)) {
                setText("将军", 2);
                return;
            }
        }
    }
    /**
     * 判断胜利条件
     *
     **/
     function checkWin(pointY, user) {
         var id = getCookie("username");
        //如果是我方移动移动
        if (user === myUser) {
            if (pointY.x === enemyGeneral.x && pointY.y === enemyGeneral.y) {
                gameOver("恭喜你，你赢了");
                record(id, 1);
                return;
            }
            //老将对面 ：先判断移动的是老将 然后判断是否对面
            if (checkGeneralFace()) {
                gameOver("老将对面:抱歉，你输了");
                record(id,0);
            }
        } else { //如果是 敌方移动
            if (pointY.x === myGeneral.x && pointY.y === myGeneral.y) {
                gameOver("抱歉，你输了");
                record(id, 0);
                return;
            }
            //老将对面 ：先判断移动的是老将 然后判断是否对面
            if (checkGeneralFace()) {
                gameOver("老将对面:恭喜你，你赢了");
                record(id, 1);
            }
        }
    }
    /**
     *
     * 判断老将对面
     **/
    function checkGeneralFace() {
        //在同一列
        if (myGeneral.x !== enemyGeneral.x) {
            return false;
        }
        //中间无子相隔
        var minY = Math.min(myGeneral.y, enemyGeneral.y);
        var maxY = Math.max(myGeneral.y, enemyGeneral.y);
        for (var i = minY + 1; i < maxY; i++) {
            if (chessStatus[myGeneral.x + "," + i] != null) {
                return false;
            }
        }
        return true;
    }

    /**
     * 移动规则
     * @param chessName  象棋名称例如:black_car
     * @param pointX    起始坐标 即：需要移动的象棋的坐标
     * @param pointY    终点坐标 即：象棋走向的坐标
     **/
    function canMove(chessName, pointX, pointY) {
        chessName = chessName.split("_");
        var isOther = chessName[0] === otherUser;
        var tmp_x, tmp_y;
        //士
        if (chessName[1] === "soldier") {
            //判断是否在中心位置
            var inCenter = (pointX.x === 4 && pointX.y === 1) || (pointX.x === 4 && pointX.y === 8);
            if (inCenter || (pointX.x === 3 && pointX.y === 0) || (pointX.x === 3 && pointX.y === 7)) {
                //右下方移动
                if (pointX.x + 1 === pointY.x && pointX.y + 1 === pointY.y) return true;
            }
            if (inCenter || (pointX.x === 5 && pointX.y === 0) || (pointX.x === 5 && pointX.y === 7)) {
                //左下方移动
                if (pointX.x - 1 === pointY.x && pointX.y + 1 === pointY.y) return true;
            }
            if (inCenter || (pointX.x === 3 && pointX.y === 2) || (pointX.x === 3 && pointX.y === 9)) {
                //右上方移动
                if (pointX.x + 1 === pointY.x && pointX.y - 1 === pointY.y) return true;
            }
            if (inCenter || (pointX.x === 5 && pointX.y === 2) || (pointX.x === 5 && pointX.y === 9)) {
                //左上方移动
                if (pointX.x - 1 === pointY.x && pointX.y - 1 === pointY.y) return true;
            }
        }
        //相
        if (chessName[1] === "elephant") {
            //不能过河
            if (crossRiver(isOther, pointY)) return false;
            //右下方移动
            tmp_x = pointX.x + 1, tmp_y = pointX.y + 1;
            if (pointX.x + 2 === pointY.x && pointX.y + 2 === pointY.y && chessStatus[tmp_x + "," + tmp_y] == null) return true;
            //左下方
            tmp_x = pointX.x - 1, tmp_y = pointX.y + 1;
            if (pointX.x - 2 === pointY.x && pointX.y + 2 === pointY.y && chessStatus[tmp_x + "," + tmp_y] == null) return true;  //左下方
            //右上方
            tmp_x = pointX.x + 1, tmp_y = pointX.y - 1;
            if (pointX.x + 2 === pointY.x && pointX.y - 2 === pointY.y && chessStatus[tmp_x + "," + tmp_y] == null) return true;
            //左上方
            tmp_x = pointX.x - 1, tmp_y = pointX.y - 1;
            if (pointX.x - 2 === pointY.x && pointX.y - 2 === pointY.y && chessStatus[tmp_x + "," + tmp_y] == null) return true;
        }
        //車
        if (chessName[1] === "car") {
            tmp_y = pointX.y;
            tmp_x = pointX.x;
            //左右移动
            if (tmp_y === pointY.y) {
                //向左移动
                while (--tmp_x > pointY.x) {
                    //如果移动路径上有其它棋子 直接返回
                    if (chessStatus[tmp_x + "," + tmp_y] != null) {
                        return false;
                    }
                }
                //向右移动
                tmp_x = pointX.x;
                while (++tmp_x < pointY.x) {
                    //如果移动路径上有其它棋子 直接返回
                    if (chessStatus[tmp_x + "," + tmp_y] != null) {
                        return false;
                    }
                }
                return true;
            }
            //上下移动
            if (tmp_x === pointY.x) {
                //向上移动
                while (--tmp_y > pointY.y) {
                    //如果移动路径上有其它棋子 直接返回
                    if (chessStatus[tmp_x + "," + tmp_y] != null) {
                        return false;
                    }
                }

                //向下移动
                tmp_y = pointX.y;
                while (++tmp_y < pointY.y) {
                    //如果移动路径上有其它棋子 直接返回
                    if (chessStatus[tmp_x + "," + tmp_y] != null) {
                        return false;
                    }
                }
                return true;
            }
        }
        //兵
        if (chessName[1] === "army") {
            //未过河 只能前进
            if (isOther) {
                //前进
                if (pointX.x === pointY.x && pointX.y + 1 === pointY.y) return true;
            } else {
                if (pointX.x === pointY.x && pointX.y - 1 === pointY.y) return true;
            }
            //过河可以横着走
            if (crossRiver(isOther, pointY)) {
                //横着走
                if (pointX.x + 1 === pointY.x && pointX.y === pointY.y) return true;
                if (pointX.x - 1 === pointY.x && pointX.y === pointY.y) return true;
            }
        }
        //马
        if (chessName[1] === "horse") {
            //左上
            tmp_x = pointX.x;
            tmp_y = pointX.y - 1;
            if (pointX.x - 1 === pointY.x && pointX.y - 2 === pointY.y && chessStatus[tmp_x + "," + tmp_y] == null) return true;
            tmp_x = pointX.x - 1;
            tmp_y = pointX.y;
            if (pointX.x - 2 === pointY.x && pointX.y - 1 === pointY.y && chessStatus[tmp_x + "," + tmp_y] == null) return true;
            //右上
            tmp_x = pointX.x;
            tmp_y = pointX.y - 1;
            if (pointX.x + 1 === pointY.x && pointX.y - 2 === pointY.y && chessStatus[tmp_x + "," + tmp_y] == null) return true;
            tmp_x = pointX.x + 1;
            tmp_y = pointX.y;
            if (pointX.x + 2 === pointY.x && pointX.y - 1 === pointY.y && chessStatus[tmp_x + "," + tmp_y] == null) return true;
            //左下
            tmp_x = pointX.x;
            tmp_y = pointX.y + 1;
            if (pointX.x - 1 === pointY.x && pointX.y + 2 === pointY.y && chessStatus[tmp_x + "," + tmp_y] == null) return true;
            tmp_x = pointX.x - 1;
            tmp_y = pointX.y;
            if (pointX.x - 2 === pointY.x && pointX.y + 1 === pointY.y && chessStatus[tmp_x + "," + tmp_y] == null) return true;
            //右下
            tmp_x = pointX.x;
            tmp_y = pointX.y + 1;
            if (pointX.x + 1 === pointY.x && pointX.y + 2 === pointY.y && chessStatus[tmp_x + "," + tmp_y] == null) return true;
            tmp_x = pointX.x + 1;
            tmp_y = pointX.y;
            if (pointX.x + 2 === pointY.x && pointX.y + 1 === pointY.y && chessStatus[tmp_x + "," + tmp_y] == null) return true;
        }
        //炮
        if (chessName[1] === "cannon") {
            var chessNumOnPath = 0;
            tmp_x = pointX.x;
            tmp_y = pointX.y;
            //横着走
            if (tmp_y === pointY.y) {
                //左
                while (--tmp_x > pointY.x) {
                    if (chessStatus[tmp_x + "," + tmp_y] != null) {
                        chessNumOnPath++;
                    }
                }
                tmp_x = pointX.x;
                //右
                while (++tmp_x < pointY.x) {
                    if (chessStatus[tmp_x + "," + tmp_y] != null) {
                        chessNumOnPath++;
                    }
                }
                //攻击时要隔一个
                if (chessNumOnPath === 1 && chessStatus[pointY.x + "," + pointY.y] != null) return true;
                //不攻击时 一个都不能隔
                if (chessNumOnPath === 0 && chessStatus[pointY.x + "," + pointY.y] == null) return true;
            }
            //竖着走
            if (tmp_x === pointY.x) {
                //上
                while (--tmp_y > pointY.y) {
                    if (chessStatus[tmp_x + "," + tmp_y] != null) {
                        chessNumOnPath++;
                    }
                }


                tmp_y = pointX.y;
                //下
                while (++tmp_y < pointY.y) {
                    if (chessStatus[tmp_x + "," + tmp_y] != null) {
                        chessNumOnPath++;
                    }
                }
                //攻击时要隔一个
                if (chessNumOnPath === 1 && chessStatus[pointY.x + "," + pointY.y] != null) return true;
                //不攻击时 一个都不能隔
                if (chessNumOnPath === 0 && chessStatus[pointY.x + "," + pointY.y] == null) return true;
            }


        }
        //将
        if (chessName[1] === "general") {
            //边界检测
            if (pointY.x < 3 || pointY.x > 5) return false;
            if (isOther && pointY.y > 2) return false;
            if (!isOther && pointY.y < 7) return false;
            //右
            if (pointX.x + 1 === pointY.x && pointX.y === pointY.y) return true;
            //左
            if (pointX.x - 1 === pointY.x && pointX.y === pointY.y) return true;
            //上
            if (pointX.x === pointY.x && pointX.y - 1 === pointY.y) return true;
            //下
            if (pointX.x === pointY.x && pointX.y + 1 === pointY.y) return true;
        }
        return false;
    };
    return oChessGame;
};