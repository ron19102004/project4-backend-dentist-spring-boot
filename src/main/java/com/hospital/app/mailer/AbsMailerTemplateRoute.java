package com.hospital.app.mailer;

public abstract class AbsMailerTemplateRoute {
    protected static final String ONE_TIME_PASSWORD_TEMPLATE = "mailer/OneTimePassword";
    protected static final String WELCOME_REGISTER_TEMPLATE = "mailer/WelcomeRegister";
    protected static final String RESET_PASSWORD_REQUEST_TEMPLATE = "mailer/ResetPasswordRequest";
    protected static final String RESET_PASSWORD_SUCCESS_TEMPLATE = "mailer/ResetPasswordSuccess";
}
