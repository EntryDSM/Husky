package kr.hs.entrydsm.husky.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import kr.hs.entrydsm.husky.domain.user.domain.enums.Sex;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SetUserInfoRequest {

    @Size(max = 15)
    private String name;

    private Sex sex;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate birthDate;

    @Size(max = 5)
    private String studentNumber;

    @Size(max = 10)
    private String schoolCode;

    @Size(max = 20)
    private String schoolTel;

    @Size(max = 15)
    private String parentName;

    @Size(max = 20)
    private String parentTel;

    @Size(max = 20)
    private String applicantTel;

    @Size(max = 250)
    private String address;

    @Size(max = 250)
    private String detailAddress;

    @Size(max = 5)
    private String postCode;

    @Size(max = 45)
    private String photo;

}
