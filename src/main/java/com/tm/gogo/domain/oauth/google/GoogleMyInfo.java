package com.tm.gogo.domain.oauth.google;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tm.gogo.domain.oauth.OauthProfileResponse;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleMyInfo implements OauthProfileResponse {

    @JsonProperty("emailAddresses")
    private List<EmailAddress> emailAddresses;

    @JsonProperty("nicknames")
    private List<Nickname> nicknames;

    @JsonProperty("names")
    private List<Name> names;

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class EmailAddress {
        private String value;
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class Nickname {
        private String value;
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class Name {
        private String displayName;
    }

    @Override
    public String getEmail() {
        return emailAddresses.get(0).value;
    }

    @Override
    public String getNickName() {
        if (nicknames == null || nicknames.isEmpty()) {
            return names.get(0).displayName;
        }

        return nicknames.get(0).value;
    }
}
