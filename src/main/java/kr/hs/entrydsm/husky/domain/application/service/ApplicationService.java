package kr.hs.entrydsm.husky.domain.application.service;

import kr.hs.entrydsm.husky.domain.application.dto.*;

public interface ApplicationService {

    void setIntro(SetDocsRequest request);
    IntroResponse getIntro();
    void setPlan(SetDocsRequest request);
    PlanResponse getPlan();
    void setGedScore(SetGedScoreRequest request);
    void setScore(SetScoreRequest dto);
    ScoreResponse getScore();

}
