package org.jasig.cas.authentication.utils;

public enum ResultCode {
	SUCCESS("0", "成功"),
    ERROR_PARAM("1", "参数出错"),
	UNKNWON_ERROR("-1", "未知异常");
    
    private String resultCode = null; // 结果响应代码
    private String resultDesc = null; // 响应信息描述
    /**
     * @description: 私有构造方法,增加描述信息
     * @param resultDesc
     *            对象描述信息
     */
    private ResultCode(String resultCode, String resultDesc) {
        this.resultCode = resultCode;
        this.resultDesc = resultDesc;
    }
    /**
     * @description: 返回对象描述
     * @return String 对象描述信息
     */
    public String resultDesc() {
        return this.resultDesc;
    }

    /**
     * @description: 返回错误编码
     * @return String 返回错误编码
     */
    public String resultCode() {
        return this.resultCode;
    }
}
