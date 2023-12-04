package exam.web.jwt;

import cn.hutool.core.util.IdUtil;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

@Data
@Accessors(chain = true)
public class JWTParam {

    private Long id;

    private String username;

    private String role;

    private String jti = IdUtil.randomUUID();

    private Map<String, Object> attrMap;
}
