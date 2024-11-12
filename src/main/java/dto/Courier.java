package dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;


@Data
@AllArgsConstructor
public class Courier {
        private String login;
        private String password;
        private String firstName;
}


