package kr.hs.entrydsm.husky.entities.schools;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
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
