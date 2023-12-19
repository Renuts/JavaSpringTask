package ru.pflb.newmock.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.pflb.newmock.Model.RequestDTO;
import ru.pflb.newmock.Model.ResponseDTO;

import java.math.BigDecimal;
import java.util.Random;

@RestController

public class MainController {

    private Logger log = LoggerFactory.getLogger(MainController.class);

    ObjectMapper mapper = new ObjectMapper();

    @PostMapping(
            value = "/info/postBalances",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public Object postBalances(@RequestBody RequestDTO requestDTO){
        try {
            String clientId = requestDTO.getClientId();
            char firstDigit = clientId.charAt(0);
            BigDecimal maxLimit;
            String clientRegion;

            if (firstDigit == '8'){
                maxLimit = new BigDecimal(2000);
            } else if (firstDigit == '9'){
                maxLimit = new BigDecimal(1000);
            } else {
                maxLimit = new BigDecimal(50000);
            }

            Random random = new Random();
            BigDecimal randomBalance = new BigDecimal(random.nextInt(maxLimit.intValue() + 1));

            if (firstDigit == '8'){
                clientRegion = "US";
            } else if (firstDigit == '9'){
                clientRegion = "EU";
            } else {
                clientRegion = "RU";
            }

            ResponseDTO responseDTO = new ResponseDTO();

            String rqUID = requestDTO.getRqUID();

            responseDTO.setRqUID(rqUID);
            responseDTO.setClientId(clientId);
            responseDTO.setAccount(requestDTO.getAccount());
            responseDTO.setCurrency (clientRegion);
            responseDTO.setBalance(randomBalance);
            responseDTO.setMaxLimit(maxLimit);

            log.error("********** RequestDTO **********" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(requestDTO));
            log.error("********** ResponseDTO **********" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(responseDTO));

            return  responseDTO;

        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }
}
