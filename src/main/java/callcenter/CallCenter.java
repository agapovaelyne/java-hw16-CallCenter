package callcenter;

import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;

public class CallCenter {
    private final int CALLS_PER_SECOND = 50;

    private final int CALL_HANDLING_TIME = 2500;
    private final int GENERATION_PAUSE_TIME = 1000;
    private final int ATC_WORKING_TIME = 30000;

    private Queue<Call> calls = new PriorityBlockingQueue<>();

    private final ExecutorService operators = Executors.newFixedThreadPool(5);
    private final Thread atc = new Thread(null, new Runnable() {
        int idCounter = 1;

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                for (int i = 0; i < CALLS_PER_SECOND; i++) {
                    calls.offer(new Call(idCounter));
                    idCounter++;
                }
                try {
                    Thread.sleep(GENERATION_PAUSE_TIME);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }, "Calls Generator");

    public void startWork() throws InterruptedException {
        atc.start();
        operators.submit(() -> {
            try {
                while (!calls.isEmpty()) {
                    acceptCall();
                }
                operators.shutdown();
            } catch (InterruptedException e) {
                operators.shutdown();
            }
        });
        Thread.sleep(ATC_WORKING_TIME);
        atc.interrupt();
    }

    private void acceptCall() throws InterruptedException {
        System.out.printf("%s accepted (%d calls are waiting in queue)\n", calls.poll(), calls.size());
        Thread.sleep(CALL_HANDLING_TIME);
    }

}
