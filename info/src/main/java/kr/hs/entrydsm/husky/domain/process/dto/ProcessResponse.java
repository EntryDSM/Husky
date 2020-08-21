package kr.hs.entrydsm.husky.domain.process.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessResponse {

    private boolean type;
    private boolean info;
    private boolean score;
    private boolean document;

}
