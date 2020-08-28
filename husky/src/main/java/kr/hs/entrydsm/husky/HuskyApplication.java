package kr.hs.entrydsm.husky;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"kr.hs.entrydsm.husky"})
public class HuskyApplication {

    public static void main(String[] args) {
        SpringApplication.run(HuskyApplication.class, args);
    }
}
