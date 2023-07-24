package com.leblon.app.dto;

public class VerificationEmailDTO {
    private String tokenEmail;

    public VerificationEmailDTO(String tokenEmail) {
        this.tokenEmail = tokenEmail;
    }

    public VerificationEmailDTO() {
    }

    public String getTokenEmail() {
        return tokenEmail;
    }

    public void setTokenEmail(String tokenEmail) {
        this.tokenEmail = tokenEmail;
    }
}
