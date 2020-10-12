package kr.hs.entrydsm.husky.domain.user.dto;

import kr.hs.entrydsm.husky.domain.user.domain.Status;
import kr.hs.entrydsm.husky.domain.user.domain.User;
import kr.hs.entrydsm.husky.domain.user.domain.enums.Sex;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserStatusResponse {

    private String name;
    private Sex sex;
    private boolean isFinalSubmit;
    private boolean isPaid;
    private boolean isPrintedApplicationArrived;
    private LocalDateTime submittedAt;

    public static UserStatusResponse response(User user, Status status) {
        return UserStatusResponse.builder()
                .name(user.getName())
                .sex(user.getSex())
                .isFinalSubmit(status.isFinalSubmit())
                .isPaid(status.isPaid())
                .isPrintedApplicationArrived(status.isPrintedApplicationArrived())
                .submittedAt(status.getSubmittedAt())
                .build();
    }

}
