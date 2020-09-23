package kr.hs.entrydsm.husky.global.slack;

import kr.hs.entrydsm.husky.global.slack.dto.Attachment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SlackMessageRequest {
    private List<Attachment> attachments;
}
