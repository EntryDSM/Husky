package kr.hs.entrydsm.husky.entities.users;

import lombok.*;

import javax.persistence.*;
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

    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private User user;

}
