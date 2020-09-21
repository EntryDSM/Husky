package kr.hs.entrydsm.husky.domain.application.domain.repositories.async;

import kr.hs.entrydsm.husky.domain.application.domain.adapter.GeneralApplicationAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static kr.hs.entrydsm.husky.domain.user.domain.enums.GradeType.GRADUATED;
import static kr.hs.entrydsm.husky.domain.user.domain.enums.GradeType.UNGRADUATED;

@Repository
@RequiredArgsConstructor
public class GeneralApplicationAsyncRepositoryImpl implements GeneralApplicationAsyncRepository {

    private final ApplicationAsyncRepository applicationAsyncRepository;

    @Override
    public void save(GeneralApplicationAdapter adapter) {
        switch (adapter.getGradeType()) {
            case UNGRADUATED:
                applicationAsyncRepository.save(adapter.getUnGraduatedApplication());
                break;

            case GRADUATED:
                applicationAsyncRepository.save(adapter.getGraduatedApplication());
        }
    }

}
