package kr.hs.entrydsm.husky.domain.school.service;

import kr.hs.entrydsm.husky.domain.school.domain.School;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SchoolService {

    Page<School> search(String name, Pageable pageable);

}
