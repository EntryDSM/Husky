package kr.hs.entrydsm.husky.domain.user.dto;

import kr.hs.entrydsm.husky.entities.users.Status;
import kr.hs.entrydsm.husky.entities.users.User;
import kr.hs.entrydsm.husky.entities.users.enums.Sex;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserStatusResponse {

    private String name;
    private Sex sex;
    private boolean isFinalSubmit;
    private boolean isPaid;
    private boolean isPrintedApplicationArrived;
    private boolean isPassedFirstApply;
    private boolean isPassedInterview;

    public static UserStatusResponse response(User user, Status status) {
        return UserStatusResponse.builder()
                .name(user.getName())
                .sex(user.getSex())
                .isFinalSubmit(status.isFinalSubmit())
                .isPaid(status.isPaid())
                .isPrintedApplicationArrived(status.isPrintedApplicationArrived())
                .isPassedFirstApply(status.isPassedFirstApply())
                .isPassedInterview(status.isPassedInterview())
                .build();
    }

}
