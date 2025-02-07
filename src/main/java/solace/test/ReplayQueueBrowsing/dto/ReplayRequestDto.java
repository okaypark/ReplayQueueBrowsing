package solace.test.ReplayQueueBrowsing.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReplayRequestDto {
    private String queueName;
}
