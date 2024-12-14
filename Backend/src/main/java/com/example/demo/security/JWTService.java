package com.example.demo.security;

import com.example.demo.repo.UsersRepo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTService {

    private final Key secretKey;

    @Autowired
    private final UsersRepo usersRepo;

    public JWTService(UsersRepo usersRepo) {
        this.usersRepo = usersRepo;
        try {

            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
            secretKey = keyGen.generateKey();

            System.out.println("SecretKey for JWT signiture generated: " + secretKey.getEncoded());

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public String generateToken(String username) {

        Map<String, Object> claims = new HashMap<String, Object>();
        claims.put("role", usersRepo.findByUsername(username).getRole());

        String stri = Jwts.builder()
                .claims()
                .add(claims)
                .subject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()
                        +
                    30 * 60 * 1000) // 30 min
                )
                .and()
                .signWith(secretKey)
                .compact();

        System.out.println("In the JWTService the JWT token is: " + stri);

        return stri;
    }



    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token, org.springframework.security.core.userdetails.UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

}
