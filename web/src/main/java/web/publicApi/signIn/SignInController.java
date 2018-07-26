package web.publicApi.signIn;

import static web.common.RequestMappings.SIGN_IN;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import web.publicApi.signIn.dto.JwtAuthenticationResponse;
import web.publicApi.signIn.dto.SignInWebCommand;
import web.publicApi.signIn.service.JwtTokenProvider;

@RestController
@Setter(onMethod_ = { @Autowired })
final class SignInController {

    private AuthenticationManager authenticationManager;
    private JwtTokenProvider tokenProvider;

    @PostMapping(SIGN_IN)
    ResponseEntity<?> authenticateUser(@RequestBody SignInWebCommand command) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        command.getUsername(),
                        command.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }

}
