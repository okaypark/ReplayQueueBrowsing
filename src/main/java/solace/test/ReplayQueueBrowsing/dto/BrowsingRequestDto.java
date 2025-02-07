package solace.test.ReplayQueueBrowsing.dto;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BrowsingRequestDto {
    private String queueName;
}
