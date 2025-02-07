package solace.test.ReplayQueueBrowsing.controller;

import com.solacesystems.jcsmp.JCSMPException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import solace.test.ReplayQueueBrowsing.dto.BrowsingRequestDto;
import solace.test.ReplayQueueBrowsing.dto.ReplayRequestDto;
import solace.test.ReplayQueueBrowsing.service.ReplayService;


@RestController
@RequestMapping("/api/replay")
public class ReplayViewController {

    private final ReplayService replayService;

    @Autowired
    public ReplayViewController(ReplayService replayService) {
        this.replayService = replayService;
    }

    @PostMapping("/replayCommand")
    public String replay(@RequestBody ReplayRequestDto replayRequest) throws Exception {
        System.out.println("REPLAY COMMAND : Received  Request Body: " + replayRequest); // DTO 전체 확인
        System.out.println("REPLAY COMMAND : Received Queue Name: " + replayRequest.getQueueName()); // 디버깅용
        return replayService.Replay(replayRequest.getQueueName());
    }

    @PostMapping("/browsingQueue")
    public void browsingQueue(@RequestBody BrowsingRequestDto browsingRequestDto) throws JCSMPException {
        System.out.println("BROWSING QUEUE : Received Request Body: " + browsingRequestDto); // DTO 전체 확인
        System.out.println("BROWSING QUEUE : Queue Name: " + browsingRequestDto.getQueueName()); // 특정 필드 확인
        replayService.browwsingQueue(browsingRequestDto.getQueueName());
    }
}
