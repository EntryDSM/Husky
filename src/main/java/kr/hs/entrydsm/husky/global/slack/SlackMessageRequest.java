package kr.hs.entrydsm.husky.global.slack;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class SlackMessageRequest {

    @Getter
    @NoArgsConstructor
    public static class Attachments {
        private List<Attachment> attachments;

        @Builder
        public Attachments(List<Attachment> attachments) {
            this.attachments = attachments;
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    public static class Attachment {
        private String color;
        private String pretext;
        private String authorName;
        private String title;
        private String text;
        private String footer;
        private Long ts;
        private List<Field> fields;

        @Builder
        public Attachment(String color, String pretext, String authorName, String title, String text, String footer, Long ts, List<Field> fields) {
            this.color = color;
            this.pretext = pretext;
            this.authorName = authorName;
            this.title = title;
            this.text = text;
            this.footer = footer;
            this.ts = ts;
            this.fields = fields;
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    public static class Field {
        private String title;
        private String value;

        @Builder
        public Field(String title, String value) {
            this.title = title;
            this.value = value;
        }
    }

}
