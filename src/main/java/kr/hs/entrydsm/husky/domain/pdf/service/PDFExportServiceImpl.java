package kr.hs.entrydsm.husky.domain.pdf.service;

import kr.hs.entrydsm.husky.domain.application.domain.CalculatedScore;
import kr.hs.entrydsm.husky.domain.grade.exception.UserNotFoundException;
import kr.hs.entrydsm.husky.domain.grade.service.GradeCalcService;
import kr.hs.entrydsm.husky.domain.pdf.constant.TemplateFileName;
import kr.hs.entrydsm.husky.domain.pdf.converter.ApplicationInfoConverter;
import kr.hs.entrydsm.husky.domain.pdf.converter.HtmlConverter;
import kr.hs.entrydsm.husky.domain.pdf.exception.FinalSubmitRequiredException;
import kr.hs.entrydsm.husky.domain.pdf.exception.UnprocessableEntityException;
import kr.hs.entrydsm.husky.domain.pdf.processor.TemplateProcessor;
import kr.hs.entrydsm.husky.domain.pdf.util.PdfMergeUtil;
import kr.hs.entrydsm.husky.domain.user.domain.Status;
import kr.hs.entrydsm.husky.domain.user.domain.User;
import kr.hs.entrydsm.husky.domain.user.domain.repositories.StatusRepository;
import kr.hs.entrydsm.husky.domain.user.domain.repositories.UserRepository;
import kr.hs.entrydsm.husky.global.config.security.AuthenticationFacade;
import kr.hs.entrydsm.husky.global.slack.SlackSenderManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PDFExportServiceImpl implements PDFExportService {

    private final AuthenticationFacade authFacade;
    private final TemplateProcessor templateProcessor;
    private final SlackSenderManager slackSenderManager;
    private final ApplicationInfoConverter applicationInfoConverter;

    private final UserRepository userRepository;
    private final StatusRepository statusRepository;

    private final GradeCalcService gradeCalcService;

    @Override
    public byte[] getPDFApplicationPreview() {
        return userRepository.findById(authFacade.getReceiptCode())
                .map(this::generatePDFApplication)
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public byte[] getFinalPDFApplication() {
        return userRepository.findById(authFacade.getReceiptCode())
                .map(user -> {
                    statusRepository.findById(user.getReceiptCode())
                            .filter(Status::isFinalSubmitRequired)
                            .ifPresent(status -> {
                                throw new FinalSubmitRequiredException();
                            });
                    return generatePDFApplication(user);
                })
                .orElseThrow(UserNotFoundException::new);
    }

    private byte[] generatePDFApplication(User user) {
        try {
            CalculatedScore calculatedScore = gradeCalcService.calcStudentGrade(user);
            Map<String, Object> data = applicationInfoConverter.applicationToInfo(user, calculatedScore);

            List<String> templates = new LinkedList<>(List.of(
                    TemplateFileName.APPLICATION_FOR_ADMISSION,
                    TemplateFileName.INTRODUCTION,
                    TemplateFileName.NON_SMOKING,
                    TemplateFileName.ADMISSION_AGREEMENT));

            if (!user.isGED() && !user.isCommonApplyType())
                templates.add(2, TemplateFileName.RECOMMENDATION);

            ByteArrayOutputStream result = templates.parallelStream()
                    .map(template -> templateProcessor.process(template, data))
                    .map(HtmlConverter::convertHtmlToPdf)
                    .reduce(PdfMergeUtil::concatPdf)
                    .orElseGet(() -> (ByteArrayOutputStream) ByteArrayOutputStream.nullOutputStream());

            return result.toByteArray();

        } catch (Exception e) {
            slackSenderManager.send(e);
            e.printStackTrace();
            throw new UnprocessableEntityException();
        }
    }

}
