package kr.hs.entrydsm.husky.domain.application.domain;

import kr.hs.entrydsm.husky.domain.user.domain.User;
import kr.hs.entrydsm.husky.domain.user.dto.SelectTypeRequest;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Entity(name = "ged_application")
@NoArgsConstructor
public class GEDApplication extends BaseTimeEntity {

    @Id
    @Column(name = "user_receipt_code")
    private Integer receiptCode;

    @OneToOne
    @PrimaryKeyJoinColumn
    private User user;

    @Digits(integer = 3, fraction = 2)
    private BigDecimal gedAverageScore;

    @Column
    private LocalDate gedPassDate;

    public void updateGedAverageScore(BigDecimal gedAverageScore) {
        this.gedAverageScore = gedAverageScore;
    }

    public GEDApplication update(SelectTypeRequest dto) {
        if (dto.getGedPassDate() != null)
            this.gedPassDate = dto.getGedPassDate().atDay(1);
        return this;
    }

    public GEDApplication(Integer receiptCode) {
        this.receiptCode = receiptCode;
    }

    @Builder(builderMethodName = "gedApplicationBuilder")
    public GEDApplication(Integer receiptCode, BigDecimal gedAverageScore, LocalDate gedPassDate) {
        this.receiptCode = receiptCode;
        this.gedAverageScore = gedAverageScore;
        this.gedPassDate = gedPassDate;
    }

}
