package kr.hs.entrydsm.husky.domain.user.dto;

import kr.hs.entrydsm.husky.entities.users.enums.GradeType;
import kr.hs.entrydsm.husky.entities.users.enums.Sex;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoResponse {

    private GradeType grade_type;
    private String name;
    private Sex sex;
    private String birth_date;
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

    public void setSchoolInfo(String student_number, String school_code, String school_tel) {
        this.student_number = student_number;
        this.school_code = school_code;
        this.school_tel = school_tel;
    }

}
