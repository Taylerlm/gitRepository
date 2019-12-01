package com.leyou.lmhitysu.common.Exception;

public enum MyException {
    MY_EXCEPTION_NULL("MSG_EXCEPTION_0001","空指针异常"),
    MY_EXCEPTION_INSERT("MSG_EXCEPTION_0002","插入异常");

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    private String code;
    private String desc;
    private MyException(String code,String desc){
        this.code = code;
        this.desc = desc;
    }
}
