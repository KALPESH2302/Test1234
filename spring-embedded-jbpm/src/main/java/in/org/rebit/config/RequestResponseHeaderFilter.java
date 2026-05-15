package in.org.rebit.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import in.org.rebit.dto.ErrorTypeEnum;
import in.org.rebit.exception.CustomException;
import in.org.rebit.exception.ExceptionDetails;
import in.org.rebit.util.ExceptionConstants;
import in.org.rebit.util.KavachCommonUtility;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Configuration
public class RequestResponseHeaderFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (request.getMethod().equals("GET") || request.getMethod().equals("POST") || request.getMethod().equals("OPTIONS")) {

            try {
                checkJWTValidity(request, response);
            } catch (CustomException customException) {
                return;
            }

            response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
            response.addHeader("X-Frame-Options", "sameorigin");
            response.addHeader("X-XSS-Protection", "1; mode=block");
            response.addHeader("X-Content-Type-Options", "nosniff");
            response.addHeader("x-Download-options", "noopen");
            response.addHeader("X-Permitted-Cross-Domain-Policies", "none");
            response.addHeader("Referrer-Policy", "strict-origin-when-cross-origin");
            response.addHeader("Content-Security-Policy", "script-src 'self'");
            response.addHeader("Feature-Policy", "microphone 'none'; " + "geolocation 'none'; " + "vibrate 'none'; "
                    + "xr-spatial-trackingExperimental 'none'; " + "publickey-credentials-getExperimental 'none'; "
                    + "legacy-image-formatsExperimental 'none'; " + "picture-in-picture 'none'");
            response.addHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            response.addHeader("pragma", "no-cache");
            response.addHeader("Strict-Transport-Security", "max-age=63072000; includeSubDomains; preload");

            filterChain.doFilter(request, response);

        } else {
            response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        }

    }

    private void checkJWTValidity(HttpServletRequest request, HttpServletResponse response) throws IOException {

        try {
            KavachCommonUtility.validateJWTExpiry(request.getHeader("Authorization"));

        } catch (CustomException customException) {

            logger.error("Exception occurred in " + customException);
            logger.error(customException.getMessage(), customException);

            ExceptionDetails error = new ExceptionDetails(
                    new Date(),
                    ErrorTypeEnum.fromString(ExceptionConstants.ERROR_TYPE_TECHNICAL),
                    ExceptionConstants.RESPONSE_STATUS_CODE,
                    customException.getErrorCode(),
                    customException.getMessage()
            );

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
            response.setCharacterEncoding(StandardCharsets.UTF_8.toString());

            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            String errorJson = mapper.writeValueAsString(error);
            response.getWriter().write(errorJson);
            throw customException;
        }
    }
}
