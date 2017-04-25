package org.jasig.cas.authentication.utils;

/**
 * 
 * Title: BaseJsonRsp
 * Description: 基础响应类
 * @Copyright: Copyright (c) 2017 FFCS All Rights Reserved 
 * @Company: 北京福富软件有限公司  
 * @author youss
 * @date 2017年4月25日
 * @time 下午7:05:37
 */
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