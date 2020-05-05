package br.com.megahack.api.services;

import br.com.megahack.api.repositories.abstracts.AbstractUserRepository;
import br.com.megahack.api.model.persistence.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TokenAuthenticationService {

    private static long expirationTime;
    private static String secret;
    static final String TOKEN_PREFIX = "Bearer";
    static final String HEADER_STRING = "Authorization";
    static final String AUTHORITIES_KEY = "Authorities";

    @Autowired
    protected AbstractUserRepository userRepository;

    // Methods used to inject configuration values into static class
    @Value("${jwt.expiration_time}")
    public void setExpirationTime(long time){
        expirationTime = time;
    }

    @Value("${jwt.secret}")
    public void setSecret(String secret){
        TokenAuthenticationService.secret = secret;
    }

    public void addAuthentication(HttpServletResponse response, Authentication auth){
        //We need to add our users authorities to their token so we can restrict certain API's
        String authorities = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        String jwt = Jwts.builder()
                .setSubject(auth.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();

        response.addHeader(HEADER_STRING, TOKEN_PREFIX + " " + jwt);
    }

    public Authentication getAuthentication(HttpServletRequest request){
        String token = request.getHeader(HEADER_STRING);
        if(token != null){

            Jws<Claims> claims = Jwts.parser()
                            .setSigningKey(secret)
                            .parseClaimsJws(token.replace(TOKEN_PREFIX, ""));
            String user = claims.getBody().getSubject();
            User u = userRepository.findByUsername(user);
            String authorities = claims.getBody().get(AUTHORITIES_KEY, String.class);
            String credential = u != null? u.getUuid():null;
            if (user != null) {
                List<GrantedAuthority> authorityList = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);
                return new UsernamePasswordAuthenticationToken(user, credential, authorityList);
            }

        }
        return null;
    }

}
