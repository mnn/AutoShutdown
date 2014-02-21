package tk.monnef.autoshutdown;

import externalAS.it.sauronsoftware.cron4j.Task;
import externalAS.it.sauronsoftware.cron4j.TaskExecutionContext;

import static tk.monnef.autoshutdown.TimeShutdownStatus.IDLE;
import static tk.monnef.autoshutdown.TimeShutdownStatus.SAVING;

public class TimeShutdownTask extends Task {
    @Override
    public void execute(TaskExecutionContext context) throws RuntimeException {
        if (AutoShutdown.getTimeStatus() == IDLE) {
            AutoShutdown.setTimeStatus(SAVING);
        }
    }
}
