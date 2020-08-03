package kr.hs.entrydsm.husky.domain.user.dto;

import kr.hs.entrydsm.husky.entities.users.User;
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

    public static UserInfoResponse response(User user, String studentNumber, String schoolCode, String schoolTel) {
        return UserInfoResponse.builder()
                .grade_type(user.getGradeType())
                .name(user.getName())
                .sex(user.getSex())
                .birth_date(user.getBirthDate().toString())
                .parent_name(user.getParentName())
                .parent_tel(user.getParentTel())
                .applicant_tel(user.getApplicantTel())
                .address(user.getAddress())
                .detail_address(user.getDetailAddress())
                .post_code(user.getPostCode())
                .photo(user.getUserPhoto())
                .student_number(studentNumber)
                .school_code(schoolCode)
                .school_tel(schoolTel)
                .build();
    }

}
