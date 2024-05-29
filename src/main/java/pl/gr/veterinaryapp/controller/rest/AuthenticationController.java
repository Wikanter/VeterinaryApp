package pl.gr.veterinaryapp.controller.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.gr.veterinaryapp.common.AuthToken;
import pl.gr.veterinaryapp.model.dto.LoginUser;
import pl.gr.veterinaryapp.service.impl.TokenServiceImpl;

import javax.servlet.http.HttpServletRequest;

import static pl.gr.veterinaryapp.common.TokenConstants.AUTH_HEADER_NAME;

@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
public class AuthenticationController {

    private final TokenServiceImpl tokenService;
    private final ObjectMapper objectMapper;

    @PostMapping("/login")
    public AuthToken register(@RequestBody LoginUser loginUser) throws AuthenticationException, JsonProcessingException {
        log.info("received request on POST /login with body: {}", objectMapper.writeValueAsString(loginUser));
        return tokenService.register(loginUser);
    }

    @PostMapping("/log-out")
    public void logout(HttpServletRequest req) {
        String tokenHeader = req.getHeader(AUTH_HEADER_NAME);
        tokenService.logout(tokenHeader);
    }
}
