package com.stockanalytics.configuration;
import com.stockanalytics.accounting.model.UserAccount;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;

@Component
public class UserAccountToStringConverter extends AbstractConverter<UserAccount, String> {
    @Override
    protected String convert(UserAccount source) {
        return source != null ? source.getLogin() : null;
    }
}
