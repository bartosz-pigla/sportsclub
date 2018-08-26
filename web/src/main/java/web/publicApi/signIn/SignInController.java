package web.publicApi.signIn;

import static org.springframework.http.ResponseEntity.ok;
import static web.common.RequestMappings.PUBLIC_API_SIGN_IN;
import static web.common.user.dto.UserDtoFactory.create;

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
import query.model.user.repository.UserEntityRepository;
import web.publicApi.signIn.dto.JwtAuthenticationResponse;
import web.publicApi.signIn.dto.SignInWebCommand;
import web.publicApi.signIn.dto.UserPrincipal;
import web.publicApi.signIn.service.JwtTokenProvider;

@RestController
@Setter(onMethod_ = { @Autowired })
final class SignInController {

    private UserEntityRepository userRepository;
    private AuthenticationManager authenticationManager;
    private JwtTokenProvider tokenProvider;

    @PostMapping(PUBLIC_API_SIGN_IN)
    ResponseEntity<?> authenticateUser(@RequestBody SignInWebCommand command) {
        String username = command.getUsername();
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        username,
                        command.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        return ok(new JwtAuthenticationResponse(jwt, create(principal.getUser())));
    }
}
