package kr.hs.entrydsm.husky.domain.user.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Status {

    @Id
    @Column(name = "user_receipt_code")
    private Integer receiptCode;

    @Column
    private boolean isPaid;                         // 전형료 납부 여부

    @Column
    private boolean isPrintedApplicationArrived;    // 원서 도착 여부

    @Column
    private boolean isPassedFirstApply;             // 1차 전형 통과 여부

    @Column
    private boolean isPassedInterview;              // 2차 전형(면접) 통과 여부

    @Column
    private boolean isFinalSubmit;                  // 최종 제출 여부

    @Column
    private LocalDateTime submittedAt;              // 최종 제출 시각

    @Column(length = 6)
    private String examCode;

    public void finalSubmit() {
        this.isFinalSubmit = true;
        this.submittedAt = LocalDateTime.now();
    }

    public boolean isFinalSubmitRequired() {
        return !isFinalSubmit;
    }

    public Status(int receiptCode) {
        this.receiptCode = receiptCode;
        this.isFinalSubmit = false;
        this.isPaid = false;
        this.isPassedFirstApply = false;
        this.isPassedInterview = false;
        this.isPrintedApplicationArrived = false;
    }
}
