package kr.hs.entrydsm.husky.domain.school.service;

import kr.hs.entrydsm.husky.domain.school.domain.School;
import kr.hs.entrydsm.husky.domain.school.domain.repositories.SchoolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SchoolService {

    private final SchoolRepository schoolRepository;

    public Page<School> search(String eduOffice, String name, Pageable pageable) {
        return schoolRepository.findBySchoolFullNameContainsAndSchoolNameContains(eduOffice, name, pageable);
    }

}