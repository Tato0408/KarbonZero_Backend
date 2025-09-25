package project.karbonzero.Utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JWTUtils {
    /// Obtenemos los valores de nuestro AppProperties

    @Value("${security.jwt.secret}")
    private String jwtSecreto;                  // 32 caracteres por seguridad
    @Value("${security.jwt.issuer}")
    private String issuer;                      // Firma del token
    @Value("${security.jwt.expiration}")
    private long expiracionMs;                  // Tiempo de expiración

    ///Creamos un Logger para obtener mensajes especificos sobre EL JWT
    private final Logger log = LoggerFactory.getLogger(JWTUtils.class);

    /**
     * Metodo para crea JWT
     * @param id
     * @param email
     * @return
     */
    public String create(String id, String email, int status){
        ///Decodifica el secreto Base64 y crea una clave HMAC-SHA segura
        SecretKey signingKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecreto));

        /// Creamos una variable con la fecha actual y posteriormente la expiracion se calcula con la expiracionMs que obtenemos
        /// Del app properties, Fecha actual + expiracion
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expiracionMs);

        ///Construye el token con sus componentes
        return Jwts.builder()
                .setId(id)                                              /// ID DEL TOKEN
                .setIssuedAt(now)                                       /// FECHA DE EMISION
                .setSubject(email)                                     /// EMAIL DEL USUARIO
                .claim("id", id)
                .claim("status", status)                            /// STATUS, EN nuestro caso es 0,1,2
                .setIssuer(issuer)                                      /// Emisor del token
                .setExpiration(expiracionMs >= 0 ? expiration : null)   /// Expiración (si es >= 0)
                .signWith(signingKey, SignatureAlgorithm.HS256)         /// Firma con algoritmo HS256
                .compact();                                             /// Convierte a String compacto
    }

    /// Extraemos el status de nuestro token 0,1,2,3,4,5 los que existan, ahorita solo 0,1,2
    public Integer extractStatus(String token){
        Claims claims = parseToken(token);
        /// Aca decimos, obtenemos a la clave que tiene como valor "status" y su valor en integrer
        return claims.get("status", Integer.class);
    }
    /**
     * Obtiene el subject (email) del JWT
     * @param jwt Token JWT como String
     * @return String con el subject del token
     */
    public String getSubject(String jwt){
        //Parsea los claims y devuelve el subject
        Claims claims = parseClaims(jwt);
        return claims.getSubject();
    }

    /**
     * Obtine el ID del JWT
     * @param jwt
     * @return
     */
    public String getKey(String jwt){
        // Parsea los claims y devuelve el ID
        Claims claims = parseClaims(jwt);
        return claims.getId();
    }

    /**
     * Parsea y valida el token
     * @param jwt
     * @return
     * @throws ExpiredJwtException
     * @throws MalformedJwtException
     */
    public Claims parseToken(String jwt) throws ExpiredJwtException, MalformedJwtException {
        return parseClaims(jwt);
    }

    /**
     * Validación del token
     * @param token
     * @return
     */
    public boolean validate(String token){
        try{
            parseClaims(token);
            return true;
        }catch (JwtException | IllegalArgumentException e){
            log.warn("Token inválido: {}", e.getMessage());
            return false;
        }
    }

    //######################## METODOS COMPLEMENTARIOS ########################

    /**
     * Metodo privado para parsear los claims de un JWT
     * @param jwt Token a parsear
     * @return Claims del token
     */
    private Claims parseClaims(String jwt) {
        ///Configura el parse con la clave de firma y parsea el token
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecreto)))  ///Clave de firma
                .build()
                .parseClaimsJws(jwt)
                .getBody();

    }
}
