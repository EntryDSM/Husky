package kr.hs.entrydsm.husky;

import kr.hs.entrydsm.husky.domain.grade.service.GradeCalcService;
import kr.hs.entrydsm.husky.entities.applications.CalculatedScore;
import kr.hs.entrydsm.husky.entities.applications.GEDApplication;
import kr.hs.entrydsm.husky.entities.applications.GraduatedApplication;
import kr.hs.entrydsm.husky.entities.applications.UnGraduatedApplication;
import kr.hs.entrydsm.husky.entities.applications.repositories.CalculatedScoreRepository;
import kr.hs.entrydsm.husky.entities.applications.repositories.GEDApplicationRepository;
import kr.hs.entrydsm.husky.entities.applications.repositories.GraduatedApplicationRepository;
import kr.hs.entrydsm.husky.entities.applications.repositories.UnGraduatedApplicationRepository;
import kr.hs.entrydsm.husky.entities.users.User;
import kr.hs.entrydsm.husky.entities.users.enums.AdditionalType;
import kr.hs.entrydsm.husky.entities.users.enums.ApplyType;
import kr.hs.entrydsm.husky.entities.users.enums.GradeType;
import kr.hs.entrydsm.husky.entities.users.enums.Sex;
import kr.hs.entrydsm.husky.entities.users.repositories.UserRepository;
import org.junit.After;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = GradeApplication.class)
@ActiveProfiles("test")
public class GradeCalcTest {

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
    public void calcUnGraduatedApplication() {
        User user = User.builder()
                .receiptCode(null)
                .email("email")
                .password("password")
                .applyType(ApplyType.COMMON)
                .additionalType(AdditionalType.NOT_APPLICABLE)
                .gradeType(GradeType.UNGRADUATED)
                .isDaejeon(true)
                .name("name")
                .sex(Sex.MALE)
                .createdAt(LocalDateTime.now())
                .build();
        user = userRepository.save(user);

        UnGraduatedApplication unGraduatedApplication = UnGraduatedApplication.unGraduatedApplicationBuilder()
                .user(user)
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
                .english("AAAAAX")
                .build();
        unGraduatedApplicationRepository.save(unGraduatedApplication);

        gradeCalcService.calcStudentGrade(user.getReceiptCode());
        CalculatedScore calculatedScore = calcScoreRepository.findById(user.getReceiptCode())
                .orElseThrow();

        assertEquals(15, calculatedScore.getAttendanceScore());
        assertEquals(BigDecimal.valueOf(13), calculatedScore.getVolunteerScore().stripTrailingZeros());
        assertEquals(BigDecimal.valueOf(143.678), calculatedScore.getConversionScore().stripTrailingZeros());
        assertEquals(BigDecimal.valueOf(171.678), calculatedScore.getFinalScore().stripTrailingZeros());
    }

    @Test
    public void calcGraduatedApplication() {
        User user = User.builder()
                .receiptCode(null)
                .email("email")
                .password("password")
                .applyType(ApplyType.COMMON)
                .additionalType(AdditionalType.NOT_APPLICABLE)
                .gradeType(GradeType.GRADUATED)
                .isDaejeon(true)
                .name("name")
                .sex(Sex.MALE)
                .createdAt(LocalDateTime.now())
                .build();
        user = userRepository.save(user);

        GraduatedApplication graduatedApplication = GraduatedApplication.graduatedApplicationBuilder()
                .user(user)
                .volunteerTime(39)
                .fullCutCount(0)
                .periodCutCount(0)
                .lateCount(1)
                .earlyLeaveCount(0)
                .korean("ABBAAA")
                .social("AAXAAB")
                .history("AAAAAC")
                .math("XABCAD")
                .science("AABBAE")
                .techAndHome("BABAAA")
                .english("AAAAAB")
                .build();
        graduatedApplicationRepository.save(graduatedApplication);

        gradeCalcService.calcStudentGrade(user.getReceiptCode());
        CalculatedScore calculatedScore = calcScoreRepository.findById(user.getReceiptCode())
                .orElseThrow();

        assertEquals(15, calculatedScore.getAttendanceScore());
        assertEquals(BigDecimal.valueOf(13), calculatedScore.getVolunteerScore().stripTrailingZeros());
        assertEquals(BigDecimal.valueOf(134.249), calculatedScore.getConversionScore().stripTrailingZeros());
        assertEquals(BigDecimal.valueOf(162.249), calculatedScore.getFinalScore().stripTrailingZeros());
    }

    @Test
    public void calcGEDApplication() {
        User user = User.builder()
                .receiptCode(null)
                .email("email")
                .password("password")
                .applyType(ApplyType.COMMON)
                .additionalType(AdditionalType.NOT_APPLICABLE)
                .gradeType(GradeType.GED)
                .isDaejeon(true)
                .name("name")
                .sex(Sex.MALE)
                .createdAt(LocalDateTime.now())
                .build();
        user = userRepository.save(user);

        GEDApplication gedApplication = GEDApplication.gedApplicationBuilder()
                .user(user)
                .gedAverageScore(89)
                .build();
        gedApplicationRepository.save(gedApplication);

        gradeCalcService.calcStudentGrade(user.getReceiptCode());
        CalculatedScore calculatedScore = calcScoreRepository.findById(user.getReceiptCode())
                .orElseThrow();

        assertEquals(15, calculatedScore.getAttendanceScore());
        assertEquals(BigDecimal.valueOf(12.8), calculatedScore.getVolunteerScore().stripTrailingZeros());
        assertEquals(BigDecimal.valueOf(117), calculatedScore.getConversionScore().stripTrailingZeros());
        assertEquals(BigDecimal.valueOf(144.8), calculatedScore.getFinalScore().stripTrailingZeros());
    }

}
