package kr.hs.entrydsm.husky.entities.applications;

import kr.hs.entrydsm.husky.entities.users.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CalculatedScore {

    @Id
    @Column(name = "user_receipt_code")
    private Integer receiptCode;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime modifiedAt;

    @Digits(integer = 3, fraction = 3)
    private BigDecimal volunteerScore;

    private Integer attendanceScore;

    @Digits(integer = 3, fraction = 3)
    private BigDecimal conversionScore;

    @Digits(integer = 3, fraction = 3)
    private BigDecimal finalScore;

    @Digits(integer = 3, fraction = 3)
    private BigDecimal firstGradeScore;

    @Digits(integer = 3, fraction = 3)
    private BigDecimal secondGradeScore;

    @Digits(integer = 3, fraction = 3)
    private BigDecimal thirdGradeScore;

    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private User user;

    @Builder
    public CalculatedScore(User user, BigDecimal volunteerScore, Integer attendanceScore, BigDecimal conversionScore, BigDecimal finalScore) {
        this.user = user;
        this.receiptCode = user.getReceiptCode();
        this.volunteerScore = volunteerScore;
        this.attendanceScore = attendanceScore;
        this.conversionScore = conversionScore;
        this.finalScore = finalScore;
        this.createdAt = LocalDateTime.now();
        this.modifiedAt = LocalDateTime.now();
    }

}
