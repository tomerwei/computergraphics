package cgresearch.studentprojects.scanner.motors;

public class SimulatorMotor implements IStepperMotor, IRotationMotor, IVerticalMotor {

	private static final int STEPS_FOR_CM = 1600;
	
	private int targetStep;
	private int currentStep;
	
	private StepCounter stepCounter;
	
	@Override
	public void setup() {
		targetStep = 0;
		currentStep = 0;
		stepCounter = new StepCounter();
	}

	@Override
	public void enable() {
		if(!stepCounter.isAlive() && !stepCounter.isInterrupted()) stepCounter.start();
	}

	@Override
	public void disable() {
		stepCounter.interrupt();
	}

	@Override
	public void moveBackward() {
		targetStep = Integer.MIN_VALUE;
	}

	@Override
	public void moveForward() {
		targetStep = Integer.MAX_VALUE;
	}

	@Override
	public void moveCm(float cm) {
    	moveStepsForward((int)(cm*STEPS_FOR_CM));
    }
	
	@Override
	public void moveStepsForward(int steps) {
		targetStep = targetStep + steps;
	}

	@Override
	public void waitForStepper() {
		while(currentStep != targetStep) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {}
		}
	}

	@Override
	public int getStepCounter() {
		return currentStep;
	}

	@Override
	public void stop() {
		targetStep = currentStep;
	}

	@Override
	public void setStartPosition() {
		currentStep = targetStep = 0;
	}

	class StepCounter extends Thread {
		@Override
		public void run() {
			while(!isInterrupted()) {
				if(targetStep < currentStep) {
					currentStep--;
				}else if(targetStep > currentStep) {
					currentStep++;
				}
				
				try {
					sleep(1);
				} catch (InterruptedException e) {}
				
				super.run();
			}
		}
	}
}
