package kr.hs.entrydsm.husky.domain.pdf.processor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class TemplateProcessor {

    private final TemplateEngine templateEngine;

    public String process(String template, Map<String, Object> data) {
        Context context = new Context();
        context.setVariables(data);
        return templateEngine.process(template, context);
    }

}
