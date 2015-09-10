package cgresearch.studentprojects.scanner.motors;

public class SimulatorStack implements IStack {

	private IVerticalMotor motor1;
	private IRotationMotor motor2;
	private boolean isConnected;
	
	public SimulatorStack() {
		motor1 = new SimulatorMotor();
		motor2 = new SimulatorMotor();
		isConnected = false;
	}
	
	@Override
	public IStepperMotor getMotor1() {
		return motor1;
	}

	@Override
	public IStepperMotor getMotor2() {
		return motor2;
	}

	@Override
	public void connect() {
		isConnected = true;
	}

	@Override
	public void disconnect() {
		isConnected = false;
	}

	@Override
	public boolean isConnected() {
		return isConnected;
	}

}
