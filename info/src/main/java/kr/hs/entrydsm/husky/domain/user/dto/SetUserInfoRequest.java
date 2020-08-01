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
    private LocalDate birth_date;
    private String student_number;
    private String school_code;
    private String school_tel;
    private String parent_name;
    private String parent_tel;
    private String applicant_tel;
    private String address;
    private String detail_address;
    private String post_code;
    private String photo;

}
