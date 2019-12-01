package com.leyou.lmhitysu.common.Exception;

public class LyException extends RuntimeException{
    private String code;
    private String msg;
    public LyException(MyException myException){
        super(myException.getDesc());
    }
    public LyException(String message){
        super(message);
    }
    public LyException(String code,String msg){
        super(code+msg);
        this.code = code;
        this.msg = msg;

    }
}
