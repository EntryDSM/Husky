package kr.hs.entrydsm.husky.domain.school.domain.repositories;

import kr.hs.entrydsm.husky.domain.school.domain.School;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolRepository extends JpaRepository<School, String> {
    Page<School> findBySchoolFullNameContains(String name, Pageable pageable);
}
