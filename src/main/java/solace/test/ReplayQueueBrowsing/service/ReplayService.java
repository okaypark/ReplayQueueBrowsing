package solace.test.ReplayQueueBrowsing.service;

import com.solacesystems.jcsmp.JCSMPException;
import solace.test.ReplayQueueBrowsing.solace.QueueConsumer;
import solace.test.ReplayQueueBrowsing.solace.SEMPCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReplayService {

    private final SEMPCommand sempCommand;
    private final QueueConsumer queueConsumer;

    @Autowired
    public ReplayService(SEMPCommand sempCommand, QueueConsumer queueConsumer) {
        this.sempCommand = sempCommand;
        this.queueConsumer = queueConsumer;
    }

    public String Replay(String queueName) throws Exception {

        // 재생 시작 시간 설정 (UTC ISO8601 형식: 예 "2023-01-01T00:00:00Z")
        String replayStartTime = null; // null인 경우 시작부터 모든 메시지

        //Replay Command실행 with SEMP
        return sempCommand.replayQueue(queueName, replayStartTime);
    }

    public void browwsingQueue(String qName) throws JCSMPException {
        queueConsumer.browsingQueue(qName);
        //queueConsumer.consumeQueue(qName);
    }
}
