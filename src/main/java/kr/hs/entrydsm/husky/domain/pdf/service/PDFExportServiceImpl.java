package kr.hs.entrydsm.husky.domain.pdf.service;

import kr.hs.entrydsm.husky.domain.application.domain.CalculatedScore;
import kr.hs.entrydsm.husky.domain.grade.exception.UserNotFoundException;
import kr.hs.entrydsm.husky.domain.grade.service.GradeCalcService;
import kr.hs.entrydsm.husky.domain.image.service.ImageService;
import kr.hs.entrydsm.husky.domain.pdf.exception.FinalSubmitRequiredException;
import kr.hs.entrydsm.husky.domain.pdf.exception.UnprocessableEntityException;
import kr.hs.entrydsm.husky.domain.pdf.util.FontLoader;
import kr.hs.entrydsm.husky.domain.user.domain.User;
import kr.hs.entrydsm.husky.domain.user.domain.enums.ApplyType;
import kr.hs.entrydsm.husky.domain.user.domain.repositories.UserRepository;
import kr.hs.entrydsm.husky.global.config.security.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import org.docx4j.Docx4J;
import org.docx4j.XmlUtils;
import org.docx4j.convert.out.FOSettings;
import org.docx4j.dml.wordprocessingDrawing.Inline;
import org.docx4j.fonts.Mapper;
import org.docx4j.jaxb.Context;
import org.docx4j.model.datastorage.migration.VariablePrepare;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.toc.TocHelper;
import org.docx4j.wml.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import static kr.hs.entrydsm.husky.domain.pdf.converter.ApplicationInfoConverter.applicationToInfo;

@Service
@RequiredArgsConstructor
public class PDFExportServiceImpl implements PDFExportService {

    private Mapper fontMapper;

    private final AuthenticationFacade authFacade;
    private final UserRepository userRepository;
    private final GradeCalcService gradeCalcService;
    private final ImageService imageService;

    @Override
    public byte[] getPDFApplicationPreview() {
        return userRepository.findById(authFacade.getReceiptCode())
                .map(user -> generatePDFApplication(user, gradeCalcService.calcStudentGrade(user.getReceiptCode()), true))
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public byte[] getFinalPDFApplication() {
        return userRepository.findById(authFacade.getReceiptCode())
                .map(user -> {
                    if (isFinalSubmitRequired(user))
                        throw new FinalSubmitRequiredException();
                    return generatePDFApplication(user, gradeCalcService.calcStudentGrade(user.getReceiptCode()), false);
                })
                .orElseThrow(UserNotFoundException::new);
    }

    private boolean isFinalSubmitRequired(User user) {
        return user.getStatus() == null || !user.getStatus().isFinalSubmit();
    }

    private byte[] generatePDFApplication(User user, CalculatedScore calculatedScore, boolean isPreview) {
        String templateFileName = setTemplateFileName(user, isPreview);

        try {
            InputStream template = new ClassPathResource(templateFileName).getInputStream();
            WordprocessingMLPackage mlPackage = WordprocessingMLPackage.load(template);
            MainDocumentPart documentPart = mlPackage.getMainDocumentPart();
            VariablePrepare.prepare(mlPackage);

            insertUserProfileImage(user, mlPackage);
            Map<String, String> templateVariables = applicationToInfo(user, calculatedScore);
            documentPart.variableReplace(templateVariables);
            mlPackage.setFontMapper(fontMapper);

            FOSettings settings = new FOSettings();
            settings.setWmlPackage(mlPackage);

            ByteArrayOutputStream result = new ByteArrayOutputStream();
            Docx4J.toFO(settings, result, Docx4J.FLAG_EXPORT_PREFER_NONXSL);

            return result.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
            throw new UnprocessableEntityException();
        }
    }

    private String setTemplateFileName(User user, boolean isPreview) {
        String application = "templates/";
        if (isPreview) application += "preview/";

        if (isGED(user)) {
            application += "ged_application.docx";

        } else if (isCommonApply(user)) {
            application += "common_application.docx";

        } else {
            application += "application.docx";
        }

        return application;
    }

    private boolean isGED(User user) {
        return !user.isGradeTypeEmpty() && user.isGED();
    }

    private boolean isCommonApply(User user) {
        return user.getApplyType() != null && user.getApplyType().equals(ApplyType.COMMON);
    }

    private void insertUserProfileImage(User user, WordprocessingMLPackage mlPackage) throws Exception {
        String objectKey = (user.isPhotoEmpty()) ? "default.png" : user.getUserPhoto();
        byte[] image = imageService.getObject(objectKey);
        MainDocumentPart documentPart = mlPackage.getMainDocumentPart();

        Body body = documentPart.getJaxbElement().getBody();
        List<Object> runs = TocHelper.getAllElementsFromObject(body, R.class);
        for (Object o : runs) {
            R run = (R) o;
            String str = XmlUtils.marshaltoString(run);
            if (str.contains("${userPhoto}")) {
                P p = (P) run.getParent();
                p.getContent().remove(run);
                p.getContent().add(this.createImageRun(mlPackage, image));
                break;
            }
        }
    }

    private R createImageRun(WordprocessingMLPackage mlPackage, byte[] image) throws Exception {
        BinaryPartAbstractImage imagePart = BinaryPartAbstractImage.createImagePart(mlPackage, image);
        Inline inline = imagePart.createImageInline(null, "userPhoto", 0, 1, 979200, 1303200, false);
        ObjectFactory objectFactory = Context.getWmlObjectFactory();
        R run = objectFactory.createR();
        Drawing drawing = objectFactory.createDrawing();
        run.getContent().add(drawing);
        drawing.getAnchorOrInline().add(inline);
        return run;
    }

    @PostConstruct
    public void initFonts() {
        FontLoader fontLoader = new FontLoader();
        fontLoader.addPhysicalFonts();
        this.fontMapper = fontLoader.createFontMapperInstance();
    }

}
