package exam.web.jwt;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@AllArgsConstructor
public class JWTUtil {

    public static final String USER_ID = "userId";

    public static final String USERNAME_NAME = "userName";

    public static final String ROLE = "role";

    public static final String ATTR = "attr";

    private JWTConfig jwtConfig;


    public String generateToken(JWTParam param) {

        //过期时间和加密算法设置
        Date date = DateUtil.date(System.currentTimeMillis() + jwtConfig.getExpire() * 1000);
        Algorithm algorithm = Algorithm.HMAC256(jwtConfig.getSecret());

        //头部信息
        Map<String, Object> header = new HashMap<>(2);
        header.put("typ", "JWT");
        header.put("alg", "HS256");

        return JWT.create()
                .withHeader(header)
                .withJWTId(param.getJti())
                .withClaim(USER_ID, param.getId())
                .withClaim(USERNAME_NAME, param.getUsername())
                .withClaim(ROLE,param.getRole())
                .withClaim(ATTR, param.getAttrMap())
                .withExpiresAt(date)
                .sign(algorithm);
    }

    public JWTParam decodedToken(String token) {
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(jwtConfig.getSecret())).build().verify(token);
        JWTParam param = new JWTParam()
                .setId(decodedJWT.getClaim(USER_ID).asLong())
                .setUsername(decodedJWT.getClaim(USERNAME_NAME).asString())
                .setRole(decodedJWT.getClaim(ROLE).asString())
                .setJti(decodedJWT.getId());
        Claim claim = decodedJWT.getClaim(ATTR);
        param.setAttrMap(claim == null ? MapUtil.empty() : claim.asMap());
        return param;
    }

}
