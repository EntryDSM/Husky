package kr.hs.entrydsm.husky.service.email;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceAsync;
import com.amazonaws.services.simpleemail.model.*;
import kr.hs.entrydsm.husky.exceptions.VerifyEmailGenerateException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
@RequiredArgsConstructor
public class SesEmailServiceImpl implements EmailService {

    private final AmazonSimpleEmailServiceAsync amazonSimpleEmailServiceAsync;

    @Override
    public void sendEmail(String receiveEmail, String code) {

        SendTemplatedEmailRequest request = new SendTemplatedEmailRequest()
                .withDestination(new Destination().withToAddresses(receiveEmail))
                .withTemplate("EntryEmailConfirmTemplate")
                .withSource("=?utf-8?B?7J6F7ZWZ7KCE7ZiV7Iuc7Iqk7YWc?= <noreply@entrydsm.hs.kr>")
                .withTemplateData("{\"code\": \""+code+"\"}");

        amazonSimpleEmailServiceAsync.sendTemplatedEmailAsync(request);
    }

}
