package ec.edu.ups.icc.fundamentos01.security.filters;

import com.fasterxml.jackson.databind.ObjectMapper;

import ec.edu.ups.icc.fundamentos01.exception.response.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * JwtAuthenticationEntryPoint: Maneja errores de autenticación
 * 
 * PROPÓSITO:
 * - Capturar TODOS los errores de autenticación
 * - Retornar respuesta JSON consistente con formato 401 Unauthorized
 * - Reemplazar el comportamiento por defecto de Spring Security
 * 
 * ¿CUÁNDO SE EJECUTA?
 * - Cuando NO hay token JWT en request a endpoint protegido
 * - Cuando el token JWT es inválido (firma incorrecta, expirado, malformado)
 * - Cuando JwtAuthenticationFilter NO establece autenticación en SecurityContext
 * - Cuando Spring Security detecta falta de autenticación
 * 
 * ¿POR QUÉ NO USAR @RestControllerAdvice?
 * - @RestControllerAdvice captura excepciones DENTRO de controladores
 * - AuthenticationException se lanza ANTES de llegar al controlador
 * - Ocurre en la cadena de FILTROS de seguridad
 * - Por eso necesitamos AuthenticationEntryPoint
 * 
 * DIFERENCIA CON GlobalExceptionHandler:
 * ┌──────────────────────────────────────────────────────────┐
 * │ Request → Filtros → ¿Autenticado? → Controlador → Response│
 * │            ↑                          ↑                   │
 * │     AuthenticationEntryPoint    @RestControllerAdvice    │
 * │     (errores ANTES controlador) (errores EN controlador) │
 * └──────────────────────────────────────────────────────────┘
 * 
 * INTERFAZ AuthenticationEntryPoint:
 * - Parte de Spring Security
 * - Se configura en SecurityConfig con:
 *   .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint))
 * - Método principal: commence() → Se ejecuta cuando falla autenticación
 */
@Component  // Spring lo registra como bean para inyección
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);

    private final ObjectMapper objectMapper;

    public JwtAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        logger.error("Error de autenticación: {}", authException.getMessage());


        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.UNAUTHORIZED,  // Status 401
            "Token de autenticación inválido o no proporcionado. " +
                "Debe incluir un token válido en el header Authorization: Bearer <token>",
            request.getRequestURI()   // Path del endpoint (ej: /api/products)
        );


        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

     
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}