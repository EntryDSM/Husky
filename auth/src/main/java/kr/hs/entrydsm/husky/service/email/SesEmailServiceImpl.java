package kr.hs.entrydsm.husky.service.email;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceAsync;
import com.amazonaws.services.simpleemail.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SesEmailServiceImpl implements EmailService {

    private final AmazonSimpleEmailServiceAsync amazonSimpleEmailServiceAsync;

    private static final String UTF_8_ENCODED_SOURCE_NAME = "=?utf-8?B?7J6F7ZWZ7KCE7ZiV7Iuc7Iqk7YWc?=";

    @Override
    public void sendEmail(String receiveEmail, String code) {

        SendTemplatedEmailRequest request = new SendTemplatedEmailRequest()
                .withDestination(new Destination().withToAddresses(receiveEmail))
                .withTemplate("EntryEmailConfirmTemplate")
                .withSource(UTF_8_ENCODED_SOURCE_NAME + " <noreply@entrydsm.hs.kr>")
                .withTemplateData("{\"code\": \""+code+"\", \"email\": \""+receiveEmail+"\"}");

        amazonSimpleEmailServiceAsync.sendTemplatedEmailAsync(request);
    }
}
