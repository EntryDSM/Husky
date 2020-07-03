package kr.hs.entrydsm.husky.config;

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

    @Value("${aws.credential.access_key_id}")
    private String AWS_ACCESS_KEY_ID;

    @Value("${aws.credential.secret_key}")
    private String AWS_SECRET_KEY;

    @Value("${aws.credential.region}")
    private String region;

    @Bean
    public AmazonSimpleEmailServiceAsync amazonSimpleEmailService() {
        BasicAWSCredentials credentials = new BasicAWSCredentials(AWS_ACCESS_KEY_ID, AWS_SECRET_KEY);

        return AmazonSimpleEmailServiceAsyncClient.asyncBuilder()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.fromName(region))
                .build();
    }

}
