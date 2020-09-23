package kr.hs.entrydsm.husky.global.slack;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;


@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SlackMessageRequest {
    private List<Attachment> attachments;
}

@Builder
@NoArgsConstructor
@AllArgsConstructor
class Attachment {
    private String color;
    private String pretext;
    private String authorName;
    private String title;
    private String text;
    private String footer;
    private Long ts;
    private List<Field> fields;
}

@Builder
@NoArgsConstructor
@AllArgsConstructor
class Field {
    private String title;
    private String value;
}
