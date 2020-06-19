package kr.hs.entrydsm.husky.service.grade;

import kr.hs.entrydsm.husky.entities.users.User;
import kr.hs.entrydsm.husky.entities.users.repositories.UserRepository;
import kr.hs.entrydsm.husky.exceptions.UserNotFoundException;
import kr.hs.entrydsm.husky.service.pdf.PdfServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class GradeServiceImpl implements GradeService {

    private final UserRepository userRepository;

    private final PdfServiceImpl pdfService;

    @Override
    public InputStreamResource getTmpPdf(String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(UserNotFoundException::new);
        pdfService.load();

        pdfService.replace("{userName}", user.getName());
        // 이런식으로 docx 수정

        return new InputStreamResource(pdfService.save());
    }

    @Override
    public InputStreamResource getFinalPdf(String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(UserNotFoundException::new);
        pdfService.load();

        pdfService.replace("{userName}", user.getName());
        // 이런식으로 docx 수정

        return new InputStreamResource(pdfService.save());
    }
}
