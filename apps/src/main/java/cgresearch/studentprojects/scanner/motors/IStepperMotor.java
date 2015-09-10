package cgresearch.studentprojects.scanner.motors;

public interface IStepperMotor {
	public void setup();
	public void enable();
	public void disable();
	public void moveBackward();
	public void moveForward();
	public void moveStepsForward(int steps);
	public void waitForStepper();
	public int getStepCounter();
	public void stop();
	public void setStartPosition();
	public void moveCm(float cm);
}
