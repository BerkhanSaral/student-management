package com.tpe.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)//null olmayan siniflari/fieldlari  json turunde gorucez digerleri`ni gormicez
public class ResponseMessage<E> {

    private E object;
    private String  message;
    private HttpStatus httpStatus;
}
