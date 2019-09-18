package com.nichetech.smartonsite.benchmark.ResponseClass;

/**
 * Created by kaushal on 13/12/16.
 */

public class ResponseLogin {

    public int error_code = 0;
    public String error_message;
    public data Data;

    public int getError_code() {
        return error_code;
    }

    public String getError_message() {
        return error_message;
    }

    public data getData() {
        return Data;
    }

    public class data {

        public String UserId, Username,RoleId, Rolename, BranchId,BranchName,BranchCode,UserCode,Token,TokenExpireOn;

        public String getUserId() {
            return UserId;
        }

        public String getUsername() {
            return Username;
        }

        public String getRoleId() {
            return RoleId;
        }

        public String getRolename() {
            return Rolename;
        }

        public String getBranchId() {
            return BranchId;
        }

        public String getBranchName() {
            return BranchName;
        }

        public String getBranchCode() {
            return BranchCode;
        }

        public String getUserCode() {
            return UserCode;
        }

        public String getTokenExpireOn() {
            return TokenExpireOn;
        }

        public String getToken() {
            return Token;
        }
    }

}
