package kr.hs.entrydsm.husky.domain.user.dto;

import kr.hs.entrydsm.husky.entities.users.enums.AdditionalType;
import kr.hs.entrydsm.husky.entities.users.enums.ApplyType;
import kr.hs.entrydsm.husky.entities.users.enums.GradeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserTypeResponse {

    private GradeType grade_type;
    private ApplyType apply_type;
    private AdditionalType additional_type;
    private boolean is_daejeon;
    private LocalDate graduated_date;
    private LocalDate ged_pass_date;

    public void setGraduated_date(LocalDate graduated_date) {
        this.graduated_date = graduated_date;
    }

    public void setGed_pass_date(LocalDate ged_pass_date) {
        this.ged_pass_date = ged_pass_date;
    }
}
