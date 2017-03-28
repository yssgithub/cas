package org.jasig.cas.authentication.utils;

public class BaseJsonRsp {
    private String resultCode;
    private String resultDesc;

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultDesc() {
        return resultDesc;
    }

    public void setResultDesc(String resultDesc) {
        this.resultDesc = resultDesc;
    }

    public BaseJsonRsp(){

    }
    public BaseJsonRsp(ResultCode rc){
        this.resultCode = rc.resultCode();
        this.resultDesc = rc.resultDesc();

    }
    public void setResult(ResultCode rc){
        this.resultCode = rc.resultCode();
        this.resultDesc = rc.resultDesc();

    }
}