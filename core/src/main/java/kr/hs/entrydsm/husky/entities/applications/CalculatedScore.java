package kr.hs.entrydsm.husky.entities.applications;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class CalculatedScore extends Application implements Serializable {

    private BigDecimal volunteerScore;

    private Integer attendanceScore;

    private BigDecimal conversionScore;

    private BigDecimal finalScore;

}
