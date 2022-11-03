package com.homework.session.config;

import com.homework.session.dto.JwtDto.TokenResponse;
import com.homework.session.jwt.JwtTokenProvider;
import io.jsonwebtoken.JwtException;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class NoAuthArgumentResolver implements HandlerMethodArgumentResolver {
    private final JwtTokenProvider jwtTokenProvider;

    public NoAuthArgumentResolver(final JwtTokenProvider jwtTokenProvide) {
        this.jwtTokenProvider = jwtTokenProvide;
    }

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.hasParameterAnnotation(NoAuth.class)
                && parameter.getParameterType().equals(Long.class);
    }

    @Override
    public Object resolveArgument(final MethodParameter parameter, final ModelAndViewContainer mavContainer,
                                  final NativeWebRequest webRequest, final WebDataBinderFactory binderFactory) {

        String accessToken = webRequest.getHeader("authorization").split("Bearer ")[1];
        String refreshToken = webRequest.getHeader("refreshToken").split("Bearer")[1];

        TokenResponse tokenResponse = TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        return tokenResponse;
    }
}
