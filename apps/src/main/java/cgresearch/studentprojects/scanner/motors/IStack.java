package cgresearch.studentprojects.scanner.motors;

public interface IStack{
	public IStepperMotor getMotor1();
	public IStepperMotor getMotor2();
	public void connect();
	public void disconnect();
	public boolean isConnected();
}
