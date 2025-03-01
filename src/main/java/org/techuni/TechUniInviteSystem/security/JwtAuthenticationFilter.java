package org.techuni.TechUniInviteSystem.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.techuni.TechUniInviteSystem.error.ErrorCode;
import org.techuni.TechUniInviteSystem.service.MyUserDetailsService;

@Slf4j
@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;
    private final MyUserDetailsService userService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        final var requestJwt = getJwtFromRequest(request);
        if (requestJwt.isEmpty()) { // JWTがない場合は無視
            finish(request, response, filterChain);
            return;
        }

        final var decodedJwt = tokenProvider.decode(requestJwt.get());
        if (decodedJwt.isEmpty()) { // JWT認証失敗時
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, HttpStatus.UNAUTHORIZED.getReasonPhrase());
            return;
        }

        final var username = decodedJwt.get().getSubject();
        try {
            final var userDetails = userService.loadUserByUsername(username);

            if (!userDetails.isEnabled()) {
                throw new DisabledException("User(%s) is disabled.".formatted(username));
            }

            final var authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            finish(request, response, filterChain);
        } catch (AccountStatusException e) {
            log.warn(ErrorCode.LOGIN_USER_DENIED.getMessage(username));
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, HttpStatus.UNAUTHORIZED.getReasonPhrase());
        } catch (AuthenticationException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, HttpStatus.UNAUTHORIZED.getReasonPhrase());
        } catch (Exception e) {
            log.error("Unexpected error in JwtAuthenticationFilter.", e);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, HttpStatus.UNAUTHORIZED.getReasonPhrase());
        }
    }

    private void finish(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        filterChain.doFilter(request, response);
    }

    private Optional<String> getJwtFromRequest(HttpServletRequest request) {
        final var bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            final var token = bearerToken.substring(7);
            if (StringUtils.hasText(token)) {
                return Optional.of(token);
            }
        }
        return Optional.empty();
    }
}
