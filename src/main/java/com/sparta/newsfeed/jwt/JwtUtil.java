package com.sparta.newsfeed.jwt;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.sparta.newsfeed.entity.User;
import com.sparta.newsfeed.repository.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtUtil {
	public static final String AUTHORIZATION_HEADER = "Authorization";
	public static final String AUTHORIZATION_KEY = "auth";
	public static final String BEARER_PREFIX = "Bearer ";
	private final long ACCESS_TIME = 60 * 60 * 1000L; // 60분
	private final long REFRESH_TIME = 30 * 24 * 60 * 60 * 1000L; // 30일
	private final UserRepository userRepository;
	private final UserDetailsService userDetailsService;

	@Value("${jwt.secret.key}")
	private String secretKey;
	private Key key;
	private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

	public static final Logger logger = LoggerFactory.getLogger("JWT 관련 로그");

	public JwtUtil(UserRepository userRepository, UserDetailsService userDetailsService) {
		this.userRepository = userRepository;
		this.userDetailsService = userDetailsService;
	}

	@PostConstruct
	public void init() {
		byte[] bytes = Base64.getDecoder().decode(secretKey);
		key = Keys.hmacShaKeyFor(bytes);
	}
	// Access토큰과 RefreshToken을 만료시간 설정을 위해 따로 생성함.
	public String createAccessToken(String username) {
		Date date = new Date();

		return BEARER_PREFIX + Jwts.builder()
			.setSubject(username)
			.setExpiration(new Date(date.getTime() + ACCESS_TIME))
			.setIssuedAt(date)
			.signWith(key, signatureAlgorithm)
			.compact();
	}

	public String createRefreshToken(String username) {
		Date date = new Date();

		return BEARER_PREFIX + Jwts.builder()
			.setSubject(username)
			.setExpiration(new Date(date.getTime() + REFRESH_TIME))
			.setIssuedAt(date)
			.signWith(key, signatureAlgorithm)
			.compact();
	}

	// 쿠키 키능은 Postman에서 크게 쓸모 없어서 제외해둠.
	// public void addJwtToCookie(String token, HttpServletResponse res) {
	// 	try {
	// 		token = URLEncoder.encode(token, "utf-8").replaceAll("\\+", "%20");
	//
	// 		Cookie cookie = new Cookie(AUTHORIZATION_HEADER, token);
	// 		cookie.setPath("/");
	//
	// 		res.addCookie(cookie);
	// 	} catch (UnsupportedEncodingException e) {
	// 		logger.error(e.getMessage());
	// 	}
	// }

	public String substringToken(String tokenValue) {
		if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
			return tokenValue.substring(7);
		}
		logger.error("토큰이 없습니다.");
		throw new NullPointerException("토큰이 없습니다.");
	}

	// 토큰 유효성 검사
	// 만료 시에는 refreshToken 할 예정이라 true 처리.
	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
		} catch (ExpiredJwtException e) {
			logger.info("Access 토큰 만료: " + e.getMessage());
			return true;
		} catch (Exception e) {
			logger.error("Access 토큰 검증 실패: " + e.getMessage());
			return false;
		}
	}

	public boolean validateRefreshToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
		} catch (ExpiredJwtException e) {
			logger.error("Refresh 토큰 만료: " + e.getMessage());
			return false;
		} catch (Exception e) {
			logger.error("Refresh 토큰 검증 실패: " + e.getMessage());
			return false;
		}
	}

	// Access 토큰이 만료되었을 때 Refresh 토큰이 살아있다면 Access 토큰을 새로 발급. 그 후 새로운 Access 토큰 return.
	public String refreshToken(String token, HttpServletResponse response) {
		String username = getUserInfoFromToken(token).getSubject();
		token = getRefreshToken(username);
		String newAccessToken = createAccessToken(username);
		response.addHeader(AUTHORIZATION_HEADER, newAccessToken);
		System.out.println("AccessToken Refresh 완료!" + newAccessToken);
		return newAccessToken;
	}

	// Refresh 토큰을 get. Access 토큰의 아이디와 저장된 Refresh 토큰의 아이디가 다르다면 예외처리, Refresh 토큰이 Expired 되었다면 다시 로그인 메세지 출력.
	public String getRefreshToken(String username) {
		User user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("아이디가 존재하지 않습니다."));
		String refreshToken = user.getRefreshToken();
		String token = substringToken(refreshToken);
		if (!getUserInfoFromToken(token).getSubject().equals(user.getUsername())) {
			throw new IllegalArgumentException("아이디가 일치하지 않습니다.");
		}
		if (!validateRefreshToken(token)) {
			throw new IllegalArgumentException("유효하지 않은 Refresh 토큰입니다. 다시 로그인 해주세요.");
		}
		return token;
	}

	// 토큰에서 Info를 Claims형식으로 받아옴. Expired 되었어도 정보만 받아오면 되기에 e.getClaims() 처리함.
	public Claims getUserInfoFromToken(String token) {
		token = token.replace(BEARER_PREFIX, "");
		try {
			// 기존 토큰 유효성 검사 로직
			return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
		} catch (ExpiredJwtException e) {
			return e.getClaims();
		} catch (Exception e) {
			throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
		}
	}

	public Authentication getAuthentication(String token) {
		Claims claims = getUserInfoFromToken(token);
		UserDetails userDetails = userDetailsService.loadUserByUsername(claims.getSubject());
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}

	public String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
			return bearerToken.substring(BEARER_PREFIX.length());
		}
		return null;
	}

}
