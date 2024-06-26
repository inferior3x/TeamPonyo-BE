package com.econovation.teamponyo.common.enums;

public enum AccountType {
    PERSONAL(),
    TEAM(),
    ADMIN();
    public static AccountType findByName(String accountTypeName){
        for (AccountType accountType : AccountType.values()){
            if (accountType.name().equalsIgnoreCase(accountTypeName))
                return accountType;
        }
        throw new IllegalStateException("등록되지 않은 계정 타입");
    }
}
