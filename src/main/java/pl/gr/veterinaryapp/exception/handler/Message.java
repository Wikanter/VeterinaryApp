package pl.gr.veterinaryapp.exception.handler;


import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
@Setter
public class Message {

    private final HttpStatus httpStatus;
    private final String message;
    private final String path;
    private final LocalDateTime localDateTime;
}
