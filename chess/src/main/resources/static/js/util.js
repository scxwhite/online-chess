function getCookie(name)
{
    var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)"); //正则匹配
    if(arr = document.cookie.match(reg)){
        return unescape(arr[2]);
    }
    else {
        return null;
    }
}