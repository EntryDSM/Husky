package kr.hs.entrydsm.husky.entities.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@Entity(name = "user")
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @Column(length = 100, nullable = false)
    private String email;

    @Column(length = 100, nullable = false)
    private String password;

    @Column(nullable = false)
    private Integer receiptNumber;

    @Enumerated(EnumType.STRING)
    private ApplyType applyType;

    @Enumerated(EnumType.STRING)
    private AdditionalType additionalType;

    @Enumerated(EnumType.STRING)
    private GradeType gradeType;

    @Column
    private boolean isDaejeon;

    @Column(length = 15)
    private String name;

    @Enumerated(EnumType.STRING)
    private Sex sex;

    @Column(columnDefinition = "DATE")
    private Date birthDate;

    @Column(length = 15)
    private String parentName;

    @Column(length = 20)
    private String parentTel;

    @Column(length = 20)
    private String applicantTel;

    @Column(length = 500)
    private String address;

    @Column(length = 5)
    private String postCode;

    @Column(length = 45)
    private String userPhoto;

    @Column(length = 45)
    private String homeTel;

    @Column(length = 1600)
    private String selfIntroduction;

    @Column(length = 1600)
    private String studyPlan;

    @Column(nullable = false)
    private LocalDateTime createdAt;

}