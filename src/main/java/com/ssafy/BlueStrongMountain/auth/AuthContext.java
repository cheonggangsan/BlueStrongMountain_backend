package com.ssafy.BlueStrongMountain.auth;


public class AuthContext {
    private static final ThreadLocal<Long> USER_ID =
            new ThreadLocal<>();
    private AuthContext(){}
    public static void setUserId(Long userId){
        USER_ID.set(userId);
    }
    public static Long getUserId(){
        return USER_ID.get();
    }
    public static void clear(){
        USER_ID.remove();
    }
}
