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
@NoArgsConstructor
@AllArgsConstructor
public class UserTypeResponse {

    private GradeType gradeType;
    private ApplyType applyType;
    private AdditionalType additionalType;
    private Boolean isDaejeon;
    private String graduatedDate;
    private String gedPassDate;

    public static UserTypeResponse response(User user, LocalDate graduatedDate, LocalDate gedPassDate) {
        String graduated =
                (graduatedDate != null) ? yearMonthFormatter(graduatedDate.getYear(),
                        graduatedDate.getMonthValue()) : null;
        String gedPass =
                (gedPassDate != null) ? yearMonthFormatter(gedPassDate.getYear(), gedPassDate.getMonthValue()) : null;

        return UserTypeResponse.builder()
                .applyType(user.getApplyType())
                .additionalType(user.getAdditionalType())
                .gradeType(user.getGradeType())
                .isDaejeon(user.getIsDaejeon())
                .graduatedDate(graduated)
                .gedPassDate(gedPass)
                .build();
    }

    private static String yearMonthFormatter(int year, int month) {
        return year + "-" + month;
    }
}
