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
public class SetUserInfoRequest {

    @NotEmpty @NotBlank
    private String name;
    @NotEmpty @NotBlank
    private String sex;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
    private String studentNumber;
    private String schoolCode;
    private String schoolTel;
    @NotEmpty @NotBlank
    private String parentName;
    @NotEmpty @NotBlank
    private String parentTel;
    @NotEmpty @NotBlank
    private String applicantTel;
    @NotEmpty @NotBlank
    private String address;
    @NotEmpty @NotBlank
    private String detailAddress;
    @NotEmpty @NotBlank
    private String postCode;
    @NotEmpty @NotBlank
    private String photo;

}
