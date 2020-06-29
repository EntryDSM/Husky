package kr.hs.entrydsm.husky.security;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceAsync;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceAsyncClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsSesConfig {

    @Value("${auth.email.AWS_ACCESS_KEY_ID}")
    private String AWS_ACCESS_KEY_ID;

    @Value("${auth.email.AWS_SECRET_KEY}")
    private String AWS_SECRET_KEY;

    @Bean
    public AmazonSimpleEmailServiceAsync amazonSimpleEmailService() {
        BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(AWS_ACCESS_KEY_ID, AWS_SECRET_KEY);

        return AmazonSimpleEmailServiceAsyncClient.asyncBuilder()
                .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
                .withRegion(Regions.US_EAST_1)
                .build();
    }
}
