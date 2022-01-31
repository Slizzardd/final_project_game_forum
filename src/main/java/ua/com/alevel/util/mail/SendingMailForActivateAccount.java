package ua.com.alevel.util.mail;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.mail.SimpleMailMessage;
import ua.com.alevel.properties.StaticProperties;

public final class SendingMailForActivateAccount {

    private static final SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

    private static final String LETTER_SUBJECT = "Авторизация на сайте PrtSc.Game";
    private static final String GET_START_URL = "/active_account/";
    private static final String MESSAGE = "Для подтверждения аккаунта, пожалуйста, передейдите по ссылке: \n";

    private SendingMailForActivateAccount() {
        throw new IllegalStateException("Utility class.");
    }

    public static SimpleMailMessage sendMail(String recipientMail, String charactersToActivate) {
        simpleMailMessage.setTo(recipientMail);
        simpleMailMessage.setSubject(LETTER_SUBJECT);
        simpleMailMessage.setText(MESSAGE + generateUrl(charactersToActivate));
        return simpleMailMessage;
    }

    public static String generationCharToActivationAccount() {
        return RandomStringUtils.randomAlphanumeric(100);
    }

    private static String generateUrl(String charsToActivation) {
        return StaticProperties.SITE_NAME + GET_START_URL + charsToActivation;
    }
}