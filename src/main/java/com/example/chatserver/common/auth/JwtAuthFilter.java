package com.example.chatserver.common.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtAuthFilter extends GenericFilterBean {

    // application.yml 또는 properties 파일에서 secretKey 주입받기
    @Value("${jwt.secretKey}")
    private String secretKey;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        // JWT 검증
        /*
            1. request 로부터 토큰을 꺼내서, 검증
            2. 검증 시도
            - 실패 -> 예외 발생 후 종료
                     에러 반환 (만약 JWT가 없거나, 잘못됐거나, 만료됐다면) -> 이 경우, 뒤쪽으로 필터 체인이 넘어가지 않고, 에러 응답
            - 성공 → 사용자 정보를 담은 Authentication 생성

                > 토큰 검증이 정상적으로 완료되면, 다음 필터나 컨트롤러로 넘어가도록 filterChain.doFilter()를 호출
                - Authentication 객체 정상 생성
                - Authentication 객체 X
                   - 예외적인 상황(토큰이 없는데도 요청이 허용되는 URL (/login, /public, /health 같은 경로), 필터 체인에서 토큰이 없지만 처리되도록 허용해둔 경우)
                   - 검증 대상 (에러가 발생)
            3. doFilter를 통해  다음 필터/컨트롤러로 이동
            4. 컨트롤러에서는 SecurityContextHolder.getContext().getAuthentication()을 통해 로그인한 사용자 정보를 꺼낼 수 있음
         */

        // HttpServletRequest, HttpServletResponse로 형변환 (ServletRequest는 상위 타입)
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        // 요청 헤더에서 Authorization 값(토큰)을 꺼냄
        String token = httpServletRequest.getHeader("Authorization");

        try{
            // Authorization 헤더가 존재할 경우
            if(token != null){
                // 잘못된 Bearer 형식 검사
                if (!token.startsWith("Bearer ")) {
                    throw new AuthenticationServiceException("Bearer 형식이 아닙니다.");
                }
                // "Bearer " 접두사 제거 → 실제 원본 JWT만 추출
                String jwtToken = token.substring(7);

                // JWT 토큰을 파싱해서 payload(claims) 추출
                // jwtToken 의 signature 부분을 확인해서 우리 서버에서 만든 토큰인 지 검증해야 함.
                Claims payloadValue = Jwts.parserBuilder()
                        .setSigningKey(secretKey)   // 서명 검증을 위한 키 설정
                        .build()
                        .parseClaimsJws(jwtToken)   // 서명 포함된 JWT 파싱
                        .getBody();                 // 페이로드(claims)만 추출

                // 사용자의 권한 정보를 담을 리스트 생성
                List<GrantedAuthority> authorities = new ArrayList<>();

                // TODO. 권한 설정
                // 클레임에서 role 값을 꺼내서 ROLE_ 접두사 붙여 권한 부여
                authorities.add(new SimpleGrantedAuthority(("ROLE_" + payloadValue.get("role"))));

                // 사용자 식별값 (subject)과 권한을 기반으로 UserDetails 생성
                UserDetails userDetails = new User(payloadValue.getSubject(), "", authorities);

                // UserDetails를 기반으로 인증 객체(Authentication) 생성
                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, "", authorities);

                // 생성한 Authentication 객체를 SecurityContext에 등록 → 이후 컨트롤러에서 사용 가능
                SecurityContextHolder.getContext().setAuthentication(authentication);
            /*
                Authentication 객체는 SecurityContext > SecurityContextHolder에 저장되어 있음
             */

            }
            // 다음 필터 또는 서블릿으로 요청을 전달 (필수)
            // 다시 원래 체인으로 돌아가라.
            filterChain.doFilter(request, response);

        }catch(Exception e){
            // 예외 발생 시 로그 출력
            e.printStackTrace();

            // 클라이언트에 401 Unauthorized 응답 반환
            httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            httpServletResponse.setContentType("application/json");
            httpServletResponse.getWriter().write("invalid token");
        }



    }


}


/*
    SecurityConfig ->  jwtAuthFilter -> 다시 SecurityConfig 로 돌아와서 -> Controller로 요청

filterChain : 다시 필터 체인으로 돌아가기 위한 매개변수


Bearer 토큰 인증 방식
관례적으로 토큰 앞에 Bearaer 를 붙여서 보낸다.
 */

/*
 ✅ 동작 흐름 요약:
 - 클라이언트가 Authorization 헤더에 JWT 포함하여 요청
 - 해당 필터에서 JWT 꺼내고 서명 검증 → 유효하면 SecurityContextHolder에 인증 객체 저장
 - 이후 컨트롤러에서 @AuthenticationPrincipal 등으로 사용자 정보 접근 가능
 - 실패 시, 인증 실패 응답(401) 반환
 */

