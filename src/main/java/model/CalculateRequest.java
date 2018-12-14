package model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CalculateRequest {
    private String method;
    private Object a;
    private Object b;
}