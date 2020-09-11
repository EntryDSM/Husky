package kr.hs.entrydsm.husky.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import kr.hs.entrydsm.husky.domain.user.domain.enums.AdditionalType;
import kr.hs.entrydsm.husky.domain.user.domain.enums.ApplyType;
import kr.hs.entrydsm.husky.domain.user.domain.enums.GradeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.YearMonth;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SelectTypeRequest {

    private GradeType gradeType;

    private ApplyType applyType;

    private AdditionalType additionalType;

    private Boolean isDaejeon;

    @JsonFormat(pattern = "yyyy-MM")
    private YearMonth graduatedDate;

    @JsonFormat(pattern = "yyyy-MM")
    private YearMonth gedPassDate;

}
