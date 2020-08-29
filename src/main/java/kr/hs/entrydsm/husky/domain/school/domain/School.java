package kr.hs.entrydsm.husky.domain.school.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class School {

    @Id
    @Column(length = 10, nullable = false)
    private String schoolCode;

    @Column(length = 45, nullable = false)
    private String schoolName;

    @Column(length = 45, nullable = false)
    private String schoolFullName;

    @Column(length = 100, nullable = false)
    private String schoolAddress;

}
