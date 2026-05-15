package in.org.rebit.util;

import in.org.rebit.exception.CustomException;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.time.Instant;

public class KavachCommonUtility {

    private static Logger logger = LoggerFactory.getLogger(KavachCommonUtility.class);

    public static void validateJWTExpiry(String token) {

        try {
            String[] split_string = token.split("\\.");

            if (split_string.length != 3) {
                throw new CustomException(ExceptionConstants.INVALID_JWT_TOKEN_ERROR_DESC,
                        ExceptionConstants.INVALID_JWT_TOKEN_ERROR_CODE,
                        ExceptionConstants.ERROR_TYPE_TECHNICAL);
            }

            String base64EncodedBody = split_string[1];

            Base64 base64Url = new Base64(true);
            String body = new String(base64Url.decode(base64EncodedBody), StandardCharsets.UTF_8);

            // Parse JSON and check expiration
            JSONObject jsonBody = new JSONObject(body);

            if (!jsonBody.has("exp")) {
                throw new CustomException(ExceptionConstants.JWT_TOKEN_EXPIRY_DATE_ABSENT_ERROR_DESC,
                        ExceptionConstants.JWT_TOKEN_EXPIRY_DATE_ABSENT_ERROR_CODE,
                        ExceptionConstants.ERROR_TYPE_TECHNICAL);
            }

            long exp = jsonBody.getLong("exp");

            Instant expiryInstant = Instant.ofEpochSecond(exp);
            Instant nowInstant = Instant.now();

            if (expiryInstant.isBefore(nowInstant)) {

                logger.info("JWT Expiry Time: " + expiryInstant);
                logger.info("Current Time: " + nowInstant);

                throw new CustomException(ExceptionConstants.JWT_TOKEN_EXPIRED_ERROR_DESC,
                        ExceptionConstants.JWT_TOKEN_EXPIRED_ERROR_CODE,
                        ExceptionConstants.ERROR_TYPE_TECHNICAL);
            }
        } catch (CustomException customException) {
            throw customException;

        } catch (Throwable throwable) {
            throw new CustomException(ExceptionConstants.INVALID_JWT_TOKEN_ERROR_DESC, ExceptionConstants.INVALID_JWT_TOKEN_ERROR_CODE, ExceptionConstants.ERROR_TYPE_TECHNICAL);
        }
    }
}
