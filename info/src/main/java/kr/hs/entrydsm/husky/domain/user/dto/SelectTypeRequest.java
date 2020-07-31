package kr.hs.entrydsm.husky.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SelectTypeRequest {

    private String grade_type;
    private String apply_type;
    private String additional_type;
    private boolean is_daejeon;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate graduated_date;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate ged_pass_date;

}
