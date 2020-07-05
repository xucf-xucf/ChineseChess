package com.ChineseChess.model;

/**
 * 用户模型
 * @author 天理
 *
 */
public class User {
    /**
     * 主键ID
     */
    private String userId;
 
    /**
     * 姓名
     */
    private String userName;
 
    /**
     * 创建时间
     */
    private String createTime;
 
    /**
     * 密码
     */
    private String userPwd;
 
    /**
     * 手机
     */
    private String userPhone;
 
    /**
     * 账号
     */
    private String userAccount;

    /**
     * 权限
     */
    private String roleCode;
 
    /**
     * 本家
     */
    private String local;
    /**
     * 对家
     */
    private String rival;
    
    public String getLocal() {
    	return local;
    }
    
    public void setLocal(String local) {
    	this.local = local;
    }
    
    public String getRival() {
        return rival;
    }
 
    public void setRival(String rival) {
        this.rival = rival;
    }
    
    public String getUserId() {
        return userId;
    }
 
    public void setUserId(String userId) {
        this.userId = userId;
    }
 
    public String getUserName() {
        return userName;
    }
 
    public void setUserName(String userName) {
        this.userName = userName;
    }
 
    public String getCreateTime() {
        return createTime;
    }
 
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
 
    public String getUserPwd() {
        return userPwd;
    }
 
    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }
 
    public String getUserPhone() {
        return userPhone;
    }
 
    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }
 
    public String getUserAccount() {
        return userAccount;
    }
 
    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }
    public String getRoleCode() {
        return roleCode;
    }
 
    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }
}
