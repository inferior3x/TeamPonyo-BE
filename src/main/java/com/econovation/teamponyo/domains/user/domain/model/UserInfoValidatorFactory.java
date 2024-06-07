package com.econovation.teamponyo.domains.user.domain.model;

import java.util.EnumMap;
import java.util.Map;

public final class UserInfoValidatorFactory {
    private static final Map<AccountType, UserInfoValidator> validators = new EnumMap<>(AccountType.class);
    static{
        validators.put(AccountType.PERSONAL,
                (nickname, phoneNumber, email, introduction) -> {

                }
        );
        validators.put(AccountType.TEAM,
                (nickname, phoneNumber, email, introduction) -> {

                }
        );
        validators.put(AccountType.ADMIN,
                (nickname, phoneNumber, email, introduction) -> {

                }
        );
        if (AccountType.values().length != validators.size())
            throw new IllegalStateException();
    }

    public static UserInfoValidator getValidator(AccountType accountType){
        return validators.get(accountType);
    }
}
