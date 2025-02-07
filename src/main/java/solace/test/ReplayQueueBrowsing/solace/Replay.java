package solace.test.ReplayQueueBrowsing.solace;

import com.solacesystems.jcsmp.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@Component
public class Replay implements XMLMessageListener {

    private JCSMPSession session;
    private Consumer consumer;
    private ConsumerFlowProperties consumerFlowProps;
    private volatile int replayErrorResponseSubcode = JCSMPErrorResponseSubcodeEx.UNKNOWN; // Replay 이벤트 핸들링용

    @Autowired
    public Replay(JCSMPSession session) {
        this.session = session;
    }

    /**
     * Message Replay 가능 여부 확인
     */
    public void checkCapability(final CapabilityType cap) {
        System.out.printf("Checking for capability %s... ", cap);
        if (session.isCapable(cap)) {
            System.out.println("OK");
        } else {
            System.out.println("FAILED - exiting.");
            throw new UnsupportedOperationException("Message replay capability not supported.");
        }
    }

    /**
     * Replay를 시작하는 Flow 생성 및 설정
     */
    public void initiateReplay(String queueName, String dateStr) throws Exception {
        // ReplayStartLocation 설정
        ReplayStartLocation replayStart;
        if (dateStr != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = simpleDateFormat.parse(dateStr);
            replayStart = JCSMPFactory.onlyInstance().createReplayStartLocationDate(date);
        } else {
            replayStart = JCSMPFactory.onlyInstance().createReplayStartLocationBeginning();
        }

        // ConsumerFlowProperties 생성
        consumerFlowProps = new ConsumerFlowProperties();
        Queue queue = JCSMPFactory.onlyInstance().createQueue(queueName); // Queue 이름 설정
        consumerFlowProps.setEndpoint(queue);
        consumerFlowProps.setReplayStartLocation(replayStart);

        // Consumer Event 핸들러 설정
        ReplayFlowEventHandler eventHandler = new ReplayFlowEventHandler();

        // Flow 생성 및 시작
        consumer = session.createFlow(this, consumerFlowProps, null, eventHandler);
        consumer.start();
        System.out.println("Flow (" + consumer + ") created");
    }

    /**
     * Replay 이벤트 핸들링
     */
    class ReplayFlowEventHandler implements FlowEventHandler {
        @Override
        public void handleEvent(Object source, FlowEventArgs event) {
            System.out.println("Consumer received flow event: " + event);
            if (event.getEvent() == FlowEvent.FLOW_DOWN) {
                if (event.getException() instanceof JCSMPErrorResponseException) {
                    JCSMPErrorResponseException ex = (JCSMPErrorResponseException) event.getException();
                    replayErrorResponseSubcode = ex.getSubcodeEx(); // Subcode 저장
                    switch (replayErrorResponseSubcode) {
                        case JCSMPErrorResponseSubcodeEx.REPLAY_STARTED:
                            System.out.println("Replay started by broker.");
                            break;
                        case JCSMPErrorResponseSubcodeEx.REPLAY_FAILED:
                            System.out.println("Replay failed unexpectedly.");
                            break;
                        case JCSMPErrorResponseSubcodeEx.REPLAY_START_TIME_NOT_AVAILABLE:
                            System.out.println("Replay start time is unavailable.");
                            break;
                        default:
                            System.out.println("Other replay flow event: " + replayErrorResponseSubcode);
                            break;
                    }
                }
            }
        }
    }

    /**
     * Replay 관련 Exception 핸들링
     */
    @Override
    public void onException(JCSMPException exception) {
        if (exception instanceof JCSMPFlowTransportUnsolicitedUnbindException) {
            try {
                switch (replayErrorResponseSubcode) {
                    case JCSMPErrorResponseSubcodeEx.REPLAY_STARTED:
                        System.out.println("Recreating flow after REPLAY_STARTED.");
                        if (consumerFlowProps.getReplayStartLocation() != null) {
                            consumerFlowProps.setReplayStartLocation(null); // Live 메시지만 수신
                        }
                        consumer = session.createFlow(this, consumerFlowProps, null, new ReplayFlowEventHandler());
                        consumer.start();
                        break;
                    case JCSMPErrorResponseSubcodeEx.REPLAY_START_TIME_NOT_AVAILABLE:
                        System.out.println("Adjusting replay to start from beginning.");
                        consumerFlowProps.setReplayStartLocation(JCSMPFactory.onlyInstance().createReplayStartLocationBeginning());
                        consumer = session.createFlow(this, consumerFlowProps, null, new ReplayFlowEventHandler());
                        consumer.start();
                        break;
                    default:
                        System.err.println("Unhandled replay exception.");
                        break;
                }
            } catch (JCSMPException e) {
                e.printStackTrace();
            }
            replayErrorResponseSubcode = JCSMPErrorResponseSubcodeEx.UNKNOWN; // Subcode 초기화
        } else {
            exception.printStackTrace();
        }
    }

    /**
     * 새로운 메시지 수신 시 호출되는 메소드
     */
    @Override
    public void onReceive(BytesXMLMessage message) {
        System.out.println("Received message: " + message.dump());
    }
}