package org.freitas.vendas.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 31/08/2023
 * {@code @project} spring-vendas
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StandardError implements Serializable {
    @Serial
    private static final long serialVersionUID = -6868184536352257798L;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private ZonedDateTime timestamp;
    private Integer status;
    private String error;
    private String message;
    private String path;
}