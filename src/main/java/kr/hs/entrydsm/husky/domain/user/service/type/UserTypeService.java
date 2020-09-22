package kr.hs.entrydsm.husky.domain.user.service.type;

import kr.hs.entrydsm.husky.domain.user.dto.SelectTypeRequest;
import kr.hs.entrydsm.husky.domain.user.dto.UserTypeResponse;

public interface UserTypeService {

    void updateUserType(SelectTypeRequest dto);
    UserTypeResponse getUserType();

}
