package kr.hs.entrydsm.husky.domain.school.service;

import kr.hs.entrydsm.husky.domain.school.domain.School;
import kr.hs.entrydsm.husky.domain.school.domain.repositories.SchoolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SchoolServiceImpl implements SchoolService {

    private final SchoolRepository schoolRepository;

    @Override
    public Page<School> search(String name, Pageable pageable) {
        return schoolRepository.findBySchoolFullNameContains(name, pageable);
    }

}
