package kr.hs.entrydsm.husky.domain.pdf.service;

import kr.hs.entrydsm.husky.domain.application.domain.CalculatedScore;
import kr.hs.entrydsm.husky.domain.grade.exception.UserNotFoundException;
import kr.hs.entrydsm.husky.domain.grade.service.GradeCalcService;
import kr.hs.entrydsm.husky.domain.pdf.exception.FinalSubmitRequiredException;
import kr.hs.entrydsm.husky.domain.pdf.exception.UnprocessableEntityException;
import kr.hs.entrydsm.husky.domain.pdf.util.FontLoader;
import kr.hs.entrydsm.husky.domain.user.domain.User;
import kr.hs.entrydsm.husky.domain.user.domain.repositories.UserRepository;
import kr.hs.entrydsm.husky.global.config.security.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import org.docx4j.Docx4J;
import org.docx4j.convert.out.FOSettings;
import org.docx4j.fonts.Mapper;
import org.docx4j.model.datastorage.migration.VariablePrepare;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import static kr.hs.entrydsm.husky.domain.pdf.converter.ApplicationInfoConverter.toMap;

@RequiredArgsConstructor
@Service
public class PDFExportServiceImpl implements PDFExportService {

    private Mapper fontMapper;

    private final AuthenticationFacade authFacade;
    private final UserRepository userRepository;
    private final GradeCalcService gradeCalcService;

    @Override
    public byte[] getPDFApplicationPreview() {
        return userRepository.findById(authFacade.getReceiptCode())
                .map(user -> generatePDFApplication(user, gradeCalcService.calcStudentGrade(user.getReceiptCode())))
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public byte[] getFinalPDFApplication() {
        return userRepository.findById(authFacade.getReceiptCode())
                .map(user -> {
                    if (user.getStatus() == null || !user.getStatus().isFinalSubmit())
                        throw new FinalSubmitRequiredException();

                    return generatePDFApplication(user, gradeCalcService.calcStudentGrade(user.getReceiptCode()));
                })
                .orElseThrow(UserNotFoundException::new);
    }

    private byte[] generatePDFApplication(User user, CalculatedScore calculatedScore) {
        try {
            InputStream template = new ClassPathResource("templates/application.docx").getInputStream();
            WordprocessingMLPackage mlPackage = WordprocessingMLPackage.load(template);
            MainDocumentPart documentPart = mlPackage.getMainDocumentPart();
            VariablePrepare.prepare(mlPackage);

            documentPart.variableReplace(toMap(user, calculatedScore));
            mlPackage.setFontMapper(fontMapper);

            FOSettings settings = new FOSettings();
            settings.setWmlPackage(mlPackage);

            ByteArrayOutputStream result = new ByteArrayOutputStream();
            Docx4J.toFO(settings, result, Docx4J.FLAG_EXPORT_PREFER_XSL);

            return result.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
            throw new UnprocessableEntityException();
        }
    }

    @PostConstruct
    public void initFonts() {
        FontLoader fontLoader = new FontLoader();
        fontLoader.addPhysicalFonts();
        this.fontMapper = fontLoader.createFontMapperInstance();
    }

}
