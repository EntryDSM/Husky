package kr.hs.entrydsm.husky.domain.user.dto;

import kr.hs.entrydsm.husky.domain.user.domain.User;
import kr.hs.entrydsm.husky.domain.user.domain.enums.GradeType;
import kr.hs.entrydsm.husky.domain.user.domain.enums.Sex;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
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
    private String schoolName;
    private String parentName;
    private String parentTel;
    private String applicantTel;
    private String homeTel;
    private String address;
    private String detailAddress;
    private String postCode;
    private String photo;

    @Builder
    public UserInfoResponse(User user, String studentNumber, String schoolCode, String schoolTel, String schoolName,
                            String photo) {
        this.gradeType = user.getGradeType();
        this.name = user.getName();
        this.sex = user.getSex();
        this.birthDate = (user.getBirthDate() != null) ? user.getBirthDate().toString() : null;
        this.studentNumber = studentNumber;
        this.schoolCode = schoolCode;
        this.schoolTel = schoolTel;
        this.schoolName = schoolName;
        this.parentName = user.getParentName();
        this.parentTel = user.getParentTel();
        this.applicantTel = user.getApplicantTel();
        this.homeTel = user.getHomeTel();
        this.address = user.getAddress();
        this.detailAddress = user.getDetailAddress();
        this.postCode = user.getPostCode();
        this.photo = photo;
    }

}
