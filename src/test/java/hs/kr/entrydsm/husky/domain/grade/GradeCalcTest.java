package hs.kr.entrydsm.husky.domain.grade;

import kr.hs.entrydsm.husky.infra.redis.EmbeddedRedisConfig;
import kr.hs.entrydsm.husky.HuskyApplication;
import kr.hs.entrydsm.husky.domain.application.domain.CalculatedScore;
import kr.hs.entrydsm.husky.domain.application.domain.GEDApplication;
import kr.hs.entrydsm.husky.domain.application.domain.GraduatedApplication;
import kr.hs.entrydsm.husky.domain.application.domain.UnGraduatedApplication;
import kr.hs.entrydsm.husky.domain.application.domain.repositories.CalculatedScoreRepository;
import kr.hs.entrydsm.husky.domain.application.domain.repositories.GEDApplicationRepository;
import kr.hs.entrydsm.husky.domain.application.domain.repositories.GraduatedApplicationRepository;
import kr.hs.entrydsm.husky.domain.application.domain.repositories.UnGraduatedApplicationRepository;
import kr.hs.entrydsm.husky.domain.grade.service.GradeCalcService;
import kr.hs.entrydsm.husky.domain.user.domain.User;
import kr.hs.entrydsm.husky.domain.user.domain.enums.AdditionalType;
import kr.hs.entrydsm.husky.domain.user.domain.enums.ApplyType;
import kr.hs.entrydsm.husky.domain.user.domain.enums.GradeType;
import kr.hs.entrydsm.husky.domain.user.domain.enums.Sex;
import kr.hs.entrydsm.husky.domain.user.domain.repositories.UserRepository;
import org.junit.After;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {HuskyApplication.class, EmbeddedRedisConfig.class})
class GradeCalcTest {

    @Autowired
    private GradeCalcService gradeCalcService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GEDApplicationRepository gedApplicationRepository;

    @Autowired
    private GraduatedApplicationRepository graduatedApplicationRepository;

    @Autowired
    private UnGraduatedApplicationRepository unGraduatedApplicationRepository;

    @Autowired
    private CalculatedScoreRepository calcScoreRepository;

    @After
    public void tearDown() {
        graduatedApplicationRepository.deleteAll();
        calcScoreRepository.deleteAll();
        userRepository.deleteAll();
    }
    
    @Test
    public void commonUnGraduatedApplication() {
        User user = this.getDefaultUser()
                .applyType(ApplyType.COMMON)
                .gradeType(GradeType.UNGRADUATED)
                .build();
        user = userRepository.save(user);

        UnGraduatedApplication unGraduatedApplication = this.getDefaultUnGraduatedApplication(user).build();
        unGraduatedApplicationRepository.save(unGraduatedApplication);

        gradeCalcService.calcStudentGrade(user);
        CalculatedScore calculatedScore = calcScoreRepository.findById(user.getReceiptCode())
                .orElseThrow();

        assertEquals(15, calculatedScore.getAttendanceScore());
        assertEquals(BigDecimal.valueOf(13), calculatedScore.getVolunteerScore().stripTrailingZeros());
        assertEquals(BigDecimal.valueOf(143.678), calculatedScore.getConversionScore().stripTrailingZeros());
        assertEquals(BigDecimal.valueOf(171.678), calculatedScore.getFinalScore().stripTrailingZeros());
    }

    @Test
    public void commonGraduatedApplication() {
        User user = this.getDefaultUser()
                .applyType(ApplyType.COMMON)
                .gradeType(GradeType.GRADUATED)
                .build();
        user = userRepository.save(user);

        GraduatedApplication graduatedApplication = this.getDefaultGraduatedApplication(user)
                .korean("ABBAAA")
                .social("AAXAAB")
                .history("AAAAAC")
                .math("XABCAD")
                .science("AABBAE")
                .techAndHome("BABAAA")
                .english("AAAAAB")
                .build();
        graduatedApplicationRepository.save(graduatedApplication);

        gradeCalcService.calcStudentGrade(user);
        CalculatedScore calculatedScore = calcScoreRepository.findById(user.getReceiptCode())
                .orElseThrow();

        assertEquals(15, calculatedScore.getAttendanceScore());
        assertEquals(BigDecimal.valueOf(13), calculatedScore.getVolunteerScore().stripTrailingZeros());
        assertEquals(BigDecimal.valueOf(43.607), calculatedScore.getFirstGradeScore().stripTrailingZeros());
        assertEquals(BigDecimal.valueOf(40.071), calculatedScore.getSecondGradeScore().stripTrailingZeros());
        assertEquals(BigDecimal.valueOf(50.571), calculatedScore.getThirdGradeScore().stripTrailingZeros());
        assertEquals(BigDecimal.valueOf(134.249), calculatedScore.getConversionScore().stripTrailingZeros());
        assertEquals(BigDecimal.valueOf(162.249), calculatedScore.getFinalScore().stripTrailingZeros());
    }

    @Test
    public void commonGEDApplication() {
        User user = this.getDefaultUser()
                .applyType(ApplyType.COMMON)
                .gradeType(GradeType.GED)
                .build();
        user = userRepository.save(user);

        GEDApplication gedApplication = this.getDefaultGEDApplication(user)
                .gedAverageScore(BigDecimal.valueOf(89))
                .build();
        gedApplicationRepository.save(gedApplication);

        gradeCalcService.calcStudentGrade(user);
        CalculatedScore calculatedScore = calcScoreRepository.findById(user.getReceiptCode())
                .orElseThrow();

        assertEquals(15, calculatedScore.getAttendanceScore());
        assertEquals(BigDecimal.valueOf(12.8), calculatedScore.getVolunteerScore().stripTrailingZeros());
        assertEquals(BigDecimal.valueOf(117), calculatedScore.getConversionScore().stripTrailingZeros());
        assertEquals(BigDecimal.valueOf(144.8), calculatedScore.getFinalScore().stripTrailingZeros());
    }

    @Test
    public void meisterUnGraduatedApplication() {
        User user = this.getDefaultUser()
                .applyType(ApplyType.MEISTER)
                .gradeType(GradeType.UNGRADUATED)
                .build();
        user = userRepository.save(user);

        UnGraduatedApplication unGraduatedApplication = this.getDefaultUnGraduatedApplication(user).build();
        unGraduatedApplicationRepository.save(unGraduatedApplication);

        gradeCalcService.calcStudentGrade(user);
        CalculatedScore calculatedScore = calcScoreRepository.findById(user.getReceiptCode()).orElseThrow();

        assertEquals(15, calculatedScore.getAttendanceScore());
        assertEquals(BigDecimal.valueOf(13), calculatedScore.getVolunteerScore().stripTrailingZeros());
        assertEquals(BigDecimal.valueOf(86.207), calculatedScore.getConversionScore().stripTrailingZeros());
        assertEquals(BigDecimal.valueOf(114.207), calculatedScore.getFinalScore().stripTrailingZeros());
    }

    @Test
    public void meisterGraduatedApplication() {
        User user = this.getDefaultUser()
                .applyType(ApplyType.MEISTER)
                .gradeType(GradeType.GRADUATED)
                .build();
        user = userRepository.save(user);

        GraduatedApplication graduatedApplication = this.getDefaultGraduatedApplication(user).build();
        graduatedApplicationRepository.save(graduatedApplication);

        gradeCalcService.calcStudentGrade(user);
        CalculatedScore calculatedScore = calcScoreRepository.findById(user.getReceiptCode()).orElseThrow();

        assertEquals(15, calculatedScore.getAttendanceScore());
        assertEquals(BigDecimal.valueOf(13), calculatedScore.getVolunteerScore().stripTrailingZeros());
        assertEquals(BigDecimal.valueOf(82.607), calculatedScore.getConversionScore().stripTrailingZeros());
        assertEquals(BigDecimal.valueOf(110.607), calculatedScore.getFinalScore().stripTrailingZeros());
    }

    @Test
    public void meisterGEDApplication() {
        User user = this.getDefaultUser()
                .applyType(ApplyType.MEISTER)
                .gradeType(GradeType.GED)
                .build();
        user = userRepository.save(user);

        GEDApplication gedApplication = this.getDefaultGEDApplication(user)
                .gedAverageScore(BigDecimal.valueOf(89))
                .build();
        gedApplicationRepository.save(gedApplication);

        gradeCalcService.calcStudentGrade(user);
        CalculatedScore calculatedScore = calcScoreRepository.findById(user.getReceiptCode())
                .orElseThrow();

        assertEquals(15, calculatedScore.getAttendanceScore());
        assertEquals(BigDecimal.valueOf(12.8), calculatedScore.getVolunteerScore().stripTrailingZeros());
        assertEquals(BigDecimal.valueOf(70.2), calculatedScore.getConversionScore().stripTrailingZeros());
        assertEquals(BigDecimal.valueOf(98), calculatedScore.getFinalScore().stripTrailingZeros());
    }

    @Test
    public void socialUnGraduatedApplication() {
        Arrays.stream(ApplyType.values())
                .filter(type -> type != ApplyType.COMMON && type != ApplyType.MEISTER)
                .forEach(this::calcSocialUnGraduatedApplication);
    }

    private void calcSocialUnGraduatedApplication(ApplyType applyType) {
        User user = this.getDefaultUser()
                .applyType(applyType)
                .gradeType(GradeType.UNGRADUATED)
                .build();
        user = userRepository.save(user);

        UnGraduatedApplication unGraduatedApplication = this.getDefaultUnGraduatedApplication(user).build();
        unGraduatedApplicationRepository.save(unGraduatedApplication);

        gradeCalcService.calcStudentGrade(user);
        CalculatedScore calculatedScore = calcScoreRepository.findById(user.getReceiptCode())
                .orElseThrow();

        assertEquals(15, calculatedScore.getAttendanceScore());
        assertEquals(BigDecimal.valueOf(13), calculatedScore.getVolunteerScore().stripTrailingZeros());
        assertEquals(BigDecimal.valueOf(86.207), calculatedScore.getConversionScore().stripTrailingZeros());
        assertEquals(BigDecimal.valueOf(114.207), calculatedScore.getFinalScore().stripTrailingZeros());
    }

    @Test
    public void socialGraduatedApplication() {
        Arrays.stream(ApplyType.values())
                .filter(type -> type != ApplyType.COMMON && type != ApplyType.MEISTER)
                .forEach(this::calcSocialGraduatedApplication);
    }

    private void calcSocialGraduatedApplication(ApplyType applyType) {
        User user = this.getDefaultUser()
                .applyType(applyType)
                .gradeType(GradeType.GRADUATED)
                .build();
        user = userRepository.save(user);

        GraduatedApplication graduatedApplication = this.getDefaultGraduatedApplication(user).build();
        graduatedApplicationRepository.save(graduatedApplication);

        gradeCalcService.calcStudentGrade(user);
        CalculatedScore calculatedScore = calcScoreRepository.findById(user.getReceiptCode()).orElseThrow();

        assertEquals(15, calculatedScore.getAttendanceScore());
        assertEquals(BigDecimal.valueOf(13), calculatedScore.getVolunteerScore().stripTrailingZeros());
        assertEquals(BigDecimal.valueOf(82.607), calculatedScore.getConversionScore().stripTrailingZeros());
        assertEquals(BigDecimal.valueOf(110.607), calculatedScore.getFinalScore().stripTrailingZeros());
    }

    @Test
    public void socialGEDApplication() {
        Arrays.stream(ApplyType.values())
                .filter(type -> type != ApplyType.COMMON && type != ApplyType.MEISTER)
                .forEach(this::calcSocialGEDApplication);
    }

    private void calcSocialGEDApplication(ApplyType applyType) {
        User user = this.getDefaultUser()
                .applyType(applyType)
                .gradeType(GradeType.GED)
                .build();
        user = userRepository.save(user);

        GEDApplication gedApplication = this.getDefaultGEDApplication(user)
                .gedAverageScore(BigDecimal.valueOf(89))
                .build();
        gedApplicationRepository.save(gedApplication);

        gradeCalcService.calcStudentGrade(user);
        CalculatedScore calculatedScore = calcScoreRepository.findById(user.getReceiptCode())
                .orElseThrow();

        assertEquals(15, calculatedScore.getAttendanceScore());
        assertEquals(BigDecimal.valueOf(12.8), calculatedScore.getVolunteerScore().stripTrailingZeros());
        assertEquals(BigDecimal.valueOf(70.2), calculatedScore.getConversionScore().stripTrailingZeros());
        assertEquals(BigDecimal.valueOf(98), calculatedScore.getFinalScore().stripTrailingZeros());
    }

    @Test
    public void commonGraduatedApplicationWithout1stGradeScore() {
        User user = getDefaultUser()
                .gradeType(GradeType.GRADUATED)
                .applyType(ApplyType.COMMON)
                .build();
        userRepository.save(user);

        GraduatedApplication graduatedApplication = getDefaultGraduatedApplication(user)
                .lateCount(1)
                .earlyLeaveCount(1)
                .volunteerTime(45)
                .korean("XXBAAA")
                .social("XXXABB")
                .history("XXAAAC")
                .math("XXBCAB")
                .science("XXBBAA")
                .techAndHome("XXBAAB")
                .english("XXAAAC")
                .build();
        graduatedApplicationRepository.save(graduatedApplication);

        gradeCalcService.calcStudentGrade(user);
        CalculatedScore calculatedScore = calcScoreRepository.findById(user.getReceiptCode())
                .orElseThrow();

        assertEquals(15, calculatedScore.getAttendanceScore());
        assertEquals(BigDecimal.valueOf(15), calculatedScore.getVolunteerScore().stripTrailingZeros());
        assertEquals(BigDecimal.valueOf(133.178), calculatedScore.getConversionScore().stripTrailingZeros());
        assertEquals(BigDecimal.valueOf(163.178), calculatedScore.getFinalScore().stripTrailingZeros());
    }

    @Test
    public void meisterUnGraduatedApplicationWithout2ndGradeScore() {
        User user = getDefaultUser()
                .gradeType(GradeType.UNGRADUATED)
                .applyType(ApplyType.MEISTER)
                .build();
        userRepository.save(user);

        UnGraduatedApplication ungraduatedApplication = getDefaultUnGraduatedApplication(user)
                .lateCount(4)
                .fullCutCount(3)
                .earlyLeaveCount(2)
                .volunteerTime(15)
                .korean("ABXXAX")
                .social("BCXXBX")
                .history("CAXXAX")
                .math("CEXXAX")
                .science("CDXXAX")
                .techAndHome("BDXXAX")
                .english("ACXXAX")
                .build();
        unGraduatedApplicationRepository.save(ungraduatedApplication);

        gradeCalcService.calcStudentGrade(user);
        CalculatedScore calculatedScore = calcScoreRepository.findById(user.getReceiptCode())
                .orElseThrow();

        assertEquals(10, calculatedScore.getAttendanceScore());
        assertEquals(BigDecimal.valueOf(5), calculatedScore.getVolunteerScore().stripTrailingZeros());
        assertEquals(BigDecimal.valueOf(18.129), calculatedScore.getFirstGradeScore().stripTrailingZeros());
        assertEquals(BigDecimal.valueOf(22.179), calculatedScore.getSecondGradeScore().stripTrailingZeros());
        assertEquals(BigDecimal.valueOf(34.971), calculatedScore.getThirdGradeScore().stripTrailingZeros());
        assertEquals(BigDecimal.valueOf(75.279), calculatedScore.getConversionScore().stripTrailingZeros());
        assertEquals(BigDecimal.valueOf(90.279), calculatedScore.getFinalScore().stripTrailingZeros());
    }

    @Test
    public void socialUnGraduatedApplicationWithOnly3rdGradeScore() {
        User user = getDefaultUser()
                .gradeType(GradeType.UNGRADUATED)
                .applyType(ApplyType.SOCIAL_FROM_NORTH)
                .build();
        userRepository.save(user);

        UnGraduatedApplication ungraduatedApplication = getDefaultUnGraduatedApplication(user)
                .lateCount(4)
                .fullCutCount(3)
                .earlyLeaveCount(2)
                .volunteerTime(44)
                .korean("XXXXAX")
                .social("XXXXBX")
                .history("XXXXAX")
                .math("XXXXAX")
                .science("XXXXAX")
                .techAndHome("XXXXAX")
                .english("XXXXAX")
                .build();
        unGraduatedApplicationRepository.save(ungraduatedApplication);

        gradeCalcService.calcStudentGrade(user);
        CalculatedScore calculatedScore = calcScoreRepository.findById(user.getReceiptCode())
                .orElseThrow();

        assertEquals(10, calculatedScore.getAttendanceScore());
        assertEquals(BigDecimal.valueOf(14.667), calculatedScore.getVolunteerScore().stripTrailingZeros());
        assertEquals(BigDecimal.valueOf(26.229), calculatedScore.getFirstGradeScore().stripTrailingZeros());
        assertEquals(BigDecimal.valueOf(26.229), calculatedScore.getSecondGradeScore().stripTrailingZeros());
        assertEquals(BigDecimal.valueOf(34.971), calculatedScore.getThirdGradeScore().stripTrailingZeros());
        assertEquals(BigDecimal.valueOf(87.429), calculatedScore.getConversionScore().stripTrailingZeros());
        assertEquals(BigDecimal.valueOf(112.096), calculatedScore.getFinalScore().stripTrailingZeros());
    }

    private User.UserBuilder getDefaultUser() {
        return User.builder()
                .receiptCode(null)
                .email("email")
                .password("password")
                .additionalType(AdditionalType.NOT_APPLICABLE)
                .isDaejeon(true)
                .name("name")
                .sex(Sex.MALE);
    }

    private UnGraduatedApplication.UnGraduatedApplicationBuilder getDefaultUnGraduatedApplication(User user) {
        return UnGraduatedApplication.unGraduatedApplicationBuilder()
                .receiptCode(user.getReceiptCode())
                .volunteerTime(39)
                .fullCutCount(0)
                .periodCutCount(0)
                .lateCount(1)
                .earlyLeaveCount(0)
                .korean("ABBAAX")
                .social("AAXAAX")
                .history("AAAAAX")
                .math("XABCAX")
                .science("AABBAX")
                .techAndHome("BABAAX")
                .english("AAAAAX");
    }

    private GraduatedApplication.GraduatedApplicationBuilder getDefaultGraduatedApplication(User user) {
        return GraduatedApplication.graduatedApplicationBuilder()
                .receiptCode(user.getReceiptCode())
                .volunteerTime(39)
                .fullCutCount(0)
                .periodCutCount(0)
                .lateCount(1)
                .earlyLeaveCount(0)
                .korean("ABBAAA")
                .social("AAXAAB")
                .history("AAAAAC")
                .math("XABCAB")
                .science("AABBAA")
                .techAndHome("BABAAB")
                .english("AAAAAC");
    }

    private GEDApplication.GEDApplicationBuilder getDefaultGEDApplication(User user) {
        return GEDApplication.gedApplicationBuilder()
                .receiptCode(user.getReceiptCode());
    }

}
