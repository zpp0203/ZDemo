package com.volley;

/**
 * Created by Ric on 2017/3/12.
 */

public enum HttpCode {

    NeedUpdate(20, "需要更新"),
    NoNeedUpdate(21, "不需要更新"),

    //系统级别错误码
    Success(1, "执行成功"),
    NotData(0, "没有数据或记录"),
    Error(-1, "系统错误"),
    NotLogin(-2, "用户未登录"),
    MissingParams(-3, "参数为空或不完整"),
    InvalidSign(-4, "签名错误或已在其他机器上登录"),
    CallLimit(-5, "超过调用次数"),
    Unavailable(-6, "服务不可用"),
    InvalidFormat(-7, "非法的数据格式"),
    NotPermissions(-8, "用户权限不足"),
    Stop(-9, "API已停用"),
    IsExist(-10, "已存在相同的记录"),
    Forbidden(-11, "请求被禁止"),
    NotEqual(-12, "数据不一致"),
    SmsIdleLimit(-13, "号码或ip距离上次发送的消息的时间太短"),
    SmsPhoneDayLimit(-14, "该号码今天的发送记录已经超过最大次数"),
    SmsIpDayLimit(-15, "该ip今天的发送记录已经超过最大次数"),
    SmsIllegal(-16, "验证码错误"),
    ApparaNotExist(-17, "设备不存在"),
    ApparaNameExist(-18, "设备名称已存在"),
    UserNotBindHouse(-19, "用户没有绑定茶庄"),
    UserNotExist(-20, "用户不存在"),
    TeaNotExist(-21, "茶叶不存在"),
    IllegalPsw(-22, "密码错误"),
    TeaNameExist(-23, "茶叶已存在"),
    ApparaExist(-24, "设备已存在"),
    UnValidateTime(-25, "日期非法"),
    ApparaNotBindHouse(-26, "设备没有绑定茶庄"),
    ApparaNotMine(-27, "设备已经被其它茶庄绑定"),

    AuthMissingParams(-29, "授权参数为空或不完整"),
    PhoneIsExist(-32, "手机号已存在"),
    NickNameIsExist(-34, "用户昵称已存在"),
    EmailInvalid(-41, "邮箱格式错误"),
    PhoneInvalid(-42, "手机号码格式错误"),

    TypeError(-43, "类型错误"),
    CheckCodeError(-44, "校验码错误"),
    //业务级别错误码
    ErrorUserOrPwd(10001, "用户名或密码不正确"),

    NotFind(404, "请求不存在"),
    ServerUNow(400, "服务器不接受请求"),//服务器不理解请求的语法
    LongUrl(414, "请求的URI过长"),//服务器不理解请求的语法
    ServerError(500, "服务器内部错误"),
    HttpError(502, "错误网关"),//服务器作为网关或代理，从上游服务器收到无效响应。
    LongTime(503, "服务器超时"),
    HttpLongTime(504, "网关超时"),
    ServerNotHttp(505, "HTTP版本不受支持"),//服务器不支持请求中所用的HTTP协议版本
    //OtherError("未知错误");//服务器不支持请求中所用的HTTP协议版本
    OtherError("服务器未响应");//服务器不支持请求中所用的HTTP协议版本

    private int code;

    private String msg;

    HttpCode(String msg) {
        this.msg = msg;
    }

    HttpCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public static String getMsg(int code) {
        HttpCode[] httpCodes = HttpCode.values();
        for(HttpCode httpCode:httpCodes){
            if(httpCode.code==code){
                return httpCode.msg;
            }
        }
        return OtherError.msg;
    }
}
