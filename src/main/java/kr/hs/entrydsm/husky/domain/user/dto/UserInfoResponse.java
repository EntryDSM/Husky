package kr.hs.entrydsm.husky.domain.user.dto;

import kr.hs.entrydsm.husky.domain.user.domain.User;
import kr.hs.entrydsm.husky.domain.user.domain.enums.GradeType;
import kr.hs.entrydsm.husky.domain.user.domain.enums.Sex;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponse {

    private GradeType gradeType;
    private String name;
    private Sex sex;
    private String birthDate;
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

    public static UserInfoResponse response(User user, String studentNumber, String schoolCode, String schoolTel) {
        return UserInfoResponse.builder()
                .gradeType(user.getGradeType())
                .name(user.getName())
                .sex(user.getSex())
                .birthDate(user.getBirthDate().toString())
                .parentName(user.getParentName())
                .parentTel(user.getParentTel())
                .applicantTel(user.getApplicantTel())
                .address(user.getAddress())
                .detailAddress(user.getDetailAddress())
                .postCode(user.getPostCode())
                .photo(user.getUserPhoto())
                .studentNumber(studentNumber)
                .schoolCode(schoolCode)
                .schoolTel(schoolTel)
                .build();
    }

    public static UserInfoResponse nullResponse() {
        return UserInfoResponse.builder().build();
    }

}
