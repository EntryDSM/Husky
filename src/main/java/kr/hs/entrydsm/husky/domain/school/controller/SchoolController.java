package kr.hs.entrydsm.husky.domain.school.controller;

import kr.hs.entrydsm.husky.domain.school.service.SchoolService;
import kr.hs.entrydsm.husky.domain.school.exception.AppError;
import kr.hs.entrydsm.husky.domain.school.domain.School;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;

@RequiredArgsConstructor
@RestController
public class SchoolController {

    private final SchoolService schoolService;

    @GetMapping("/schools")
    public Page<School> search(@RequestParam @NotBlank String name,
                               Pageable pageable) {
        return schoolService.search(name, pageable);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody
    AppError RequestInvalidError(MissingServletRequestParameterException e) {
        AppError appError = new AppError(HttpStatus.BAD_REQUEST, "invalid parameter");
        return appError;
    }

}
