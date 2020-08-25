package kr.hs.entrydsm.husky.domain.user.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.AnonymousAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import io.findify.s3mock.S3Mock;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class S3MockConfig {

    String region = "ap-northeast-2";
    String bucket = "test";

    @Bean
    public S3Mock s3Mock() {
        return new S3Mock.Builder().withPort(8081).withInMemoryBackend().build();
    }

    @Bean
    @Primary
    public AmazonS3 amazonS3(S3Mock s3Mock) {
        s3Mock.start();
        AwsClientBuilder.EndpointConfiguration endpoint =
                new AwsClientBuilder.EndpointConfiguration("http://localhost:8081", region);
        AmazonS3 clinet = AmazonS3ClientBuilder
                .standard()
                .withPathStyleAccessEnabled(true)
                .withEndpointConfiguration(endpoint)
                .withCredentials(new AWSStaticCredentialsProvider(
                        new AnonymousAWSCredentials()
                ))
                .build();
        clinet.createBucket(bucket);

        return clinet;
    }
}
