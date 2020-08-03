package kr.hs.entrydsm.husky.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SelectTypeRequest {

    @NotEmpty @NotBlank
    private String gradeType;
    @NotEmpty @NotBlank
    private String applyType;
    @NotEmpty @NotBlank
    private String additionalType;
    private boolean isDaejeon;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate graduatedDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate gedPassDate;

}
