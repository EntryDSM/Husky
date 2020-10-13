package kr.hs.entrydsm.husky.domain.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignInRequest {

    @Email
    @NotEmpty
    private String email;

    @NotEmpty
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&_~])[A-Za-z\\d@$!%*?&_~]{8,}$")
    private String password;

    public UsernamePasswordAuthenticationToken getAuthToken(int receiptCode) {
        return new UsernamePasswordAuthenticationToken(receiptCode, password);
    }

}
