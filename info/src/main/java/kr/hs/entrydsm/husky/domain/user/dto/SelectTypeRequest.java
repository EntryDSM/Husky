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

    private String gradeType;
    private String applyType;
    private String additionalType;
    private boolean isDaejeon;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate graduatedDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate gedPassDate;

}
