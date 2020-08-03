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
public class SetUserInfoRequest {

    private String name;
    private String sex;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
    private String studentNumber;
    private String schoolCode;
    private String schoolTel;
    private String parentName;
    private String parentTel;
    private String applicantTel;
    private String address;
    private String detailAddress;
    private String postCode;
    private String photo;

}
