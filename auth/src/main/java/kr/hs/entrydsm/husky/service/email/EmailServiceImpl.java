package kr.hs.entrydsm.husky.service.email;

import kr.hs.entrydsm.husky.service.redis.RedisServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private RedisServiceImpl redisService;

    @Override
    public void sendEmail(String email) {
        redisService.setData(email, "UnAuthorized", 180000L);
    }

}
