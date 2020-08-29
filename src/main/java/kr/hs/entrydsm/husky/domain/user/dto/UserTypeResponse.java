package kr.hs.entrydsm.husky.domain.user.dto;

import kr.hs.entrydsm.husky.domain.user.domain.User;
import kr.hs.entrydsm.husky.domain.user.domain.enums.AdditionalType;
import kr.hs.entrydsm.husky.domain.user.domain.enums.ApplyType;
import kr.hs.entrydsm.husky.domain.user.domain.enums.GradeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor@AllArgsConstructor
public class UserTypeResponse {

    private GradeType gradeType;
    private ApplyType applyType;
    private AdditionalType additionalType;
    private boolean isDaejeon;
    private LocalDate graduatedDate;
    private LocalDate gedPassDate;

    public static UserTypeResponse response(User user, LocalDate graduatedDate, LocalDate gedPassDate) {
        return UserTypeResponse.builder()
                .applyType(user.getApplyType())
                .additionalType(user.getAdditionalType())
                .gradeType(user.getGradeType())
                .isDaejeon(user.isDaejeon())
                .graduatedDate(graduatedDate)
                .gedPassDate(gedPassDate)
                .build();
    }
}
