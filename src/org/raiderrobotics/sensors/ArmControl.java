package org.raiderrobotics.sensors;

import com.copperboard.ConfigurationApi.ConfigurationAPI;
import com.copperboard.ConfigurationApi.ConfigurationAPI.ConfigurationSection;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.ControlMode;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;

public class ArmControl {
	//Talon variables
	CANTalon leftTalon;
	CANTalon rightTalon;

	//Hall effect sensors
	DigitalInput rightSwitch;
	DigitalInput leftSwitch;

	double speed = 0.5; //autonomous move speed

	//NOTE: 2 is xBox left throttle
	//      3 is xBox right throttle	
	//Encoder position variables
	boolean pusshedPosition; //If pushed the reset button	
	double balancePosition=0; //Position for the right or left talon to go to
	boolean right; //True if the right talon has to be moved to balance
	
	Joystick xbox;

	public boolean debug;

	//I'll only use it for debugging
	ConfigurationSection config = ConfigurationAPI.load("/home/lvuser/config.yml");

	//Tasks

	/**
	 * Used to move the arms to the rest position
	 */
	public final LucidTask moveToRest = new LucidTask() {
		//Run if not isDOne() every tick.
		@Override
		public void run() {
			//Move one arm down until it hits the sensor
			rightTalon.set(!rightSwitch.get() ? 0 : -speed);
			leftTalon.set(!leftSwitch.get() ? 0 : -speed);
		}

		@Override
		public boolean isDone() {
			return !rightSwitch.get() && !leftSwitch.get();
		}

		@Override
		public LucidTask stop() {
			rightTalon.set(0);
			leftTalon.set(0);

			rightTalon.setPosition(0);
			leftTalon.setPosition(0);
			
			return null;
		}
	};

	public final LucidTask moveToMiddle = new LucidTask() {
		boolean rightDone;
		boolean leftDone;

		@Override
		public void run() {			
			double pos = config.getDouble("pos");
			double dr = pos - getRightEncPos();
			double dl = pos - getLeftEncPos();
			
			//It's going by 10-20
			//Move right
			if(!rightDone){
				if (Math.abs(dr) < 100) {
					rightDone = true;
					rightTalon.set(0);
				} else 
					rightTalon.set(Math.signum(dr) * speed);
			}

			//Move left
			if(!leftDone){
				if (Math.abs(dl) < 100) {
					leftDone = true;
					leftTalon.set(0);
				} else 
					leftTalon.set(Math.signum(dl) * speed);
			}
		}

		@Override
		public boolean isDone() {
			return rightDone && leftDone;
		}

		/**
		 * Stop the running task
		 * @return The next task to run
		 */
		@Override
		public LucidTask stop() {
			rightDone = false;
			leftDone = false;

			rightTalon.set(0);
			leftTalon.set(0);
			
			//The next task would be to balance the arms
			return null;
		}
	};

	private LucidTask currentTask; //The currently running task
	private LucidTask previousTask;

	public ArmControl(Joystick xbox){
		this.xbox = xbox;
		
		leftTalon = new CANTalon(3);
		rightTalon = new CANTalon(4);

		leftSwitch = new DigitalInput(8);
		rightSwitch = new DigitalInput(9);

		leftTalon.enableControl();
		leftTalon.set(0);
		leftTalon.changeControlMode(ControlMode.PercentVbus);
		
		rightTalon.enableControl();
		rightTalon.set(0);
		rightTalon.changeControlMode(ControlMode.PercentVbus);
		
		reset();
	}	
	
	public void tick(){
		if(debug){
			System.out.println("\nRight Pos: "+getRightEncPos());
			System.out.println("Left Pos: "+getLeftEncPos());
		}

		//Move the arms with a push of a button
		if(xbox.getRawButton(4)){
			//If no tasks assigned - 
			if(currentTask == null)
				currentTask = moveToMiddle;
//			else if(currentTask.equals(moveToMiddle))
//				currentTask = moveToRest;

			return;
		}
		
		//TODO used for tests and debug only (should be removed on the actual competition)
		//Individual talons control
		if(xbox.getRawButton(1)){
			leftTalon.set(-0.5);
			return;
		}
		if(xbox.getRawButton(2)){
			rightTalon.set(-0.5);
			return;
		}

		//TODO: Manual control
		//Get the direction of the two triggers (and altitude)
		double move = xbox.getRawAxis(3) - xbox.getRawAxis(2);
		
		//If no buttons pressed (or the delta is less than 0.15)
		if(Math.abs(move) < 0.15){
			
			//if not manually controlled
			//Run the lucid tasks
			if(currentTask != null){
				if(!currentTask.isDone()) 
					currentTask.run();
				else { //If the task is done: stop it and run the next task (which might be null)
					previousTask = currentTask;
					currentTask = currentTask.stop();
				}
				return;
			}
			
			//Stop the talons
			leftTalon.set(0);
			rightTalon.set(0);
			return;
			
		} else {
			if(currentTask != null) 
				currentTask = currentTask.stop();
		}
		
		//Percentage at what the talons will move 
		//when one is going faster than the other one
		double rightCut = 0.95;
		double leftCut = 0.95;
			
		//Determining if one talon is moving faster than the other one 
		double right = Math.abs(getRightEncPos()) > Math.abs(getLeftEncPos()) 
				? rightCut 
				: 1;
		double left = Math.abs(getLeftEncPos()) > Math.abs(getRightEncPos()) 
				? leftCut 
				: 1;
		
		//Move the talons based on their determined speeds
		leftTalon.set(move * left);
		rightTalon.set(move * right);
	}
	
	public void reset(){
		config.reload();
				
		leftTalon.setPosition(0);
		rightTalon.setPosition(0);

		speed = config.getDouble("speed");
		if(speed == 0)
			speed = 0.5;
		
		if(currentTask != null) 
			currentTask = currentTask.stop();
				
		if(config.getBoolean("moveToRest"))
			currentTask = moveToRest;
	}

/*
	//Position methods
	public void move(){
		double pos = config.getDouble("pos");
		double dr = pos - getRightEncPos();
		double dl = pos - getLeftEncPos();
		
		if (dr < 5) {
			rightTalon.set(0);
		} else 
//			rightTalon.set(Math.abs(dr) < 0.5 ? 0 : Math.signum(dr) * speed);
			rightTalon.set(speed);
		
		if (dl < 5) {
			leftTalon.set(0);
		} else
			leftTalon.set(speed);		
	}
*/
	
	boolean balance(){
		//Calibration button
		if(!pusshedPosition){
			pusshedPosition = true;
			if(getRightEncPos() > getLeftEncPos()){
				balancePosition = getRightEncPos();
				right = true;
			} else if(getLeftEncPos() > getRightEncPos()){
				balancePosition = getLeftEncPos();
				right = false;
			}
			
			if(debug)
				System.out.println("Balance position: "+balancePosition+" ; Right: "+right);
		}
		
		if(right){ //If moving the right talon
			if(getRightEncPos() > balancePosition){ //If not in place yet
				rightTalon.set(-0.5);
				return false;
			} else 
				rightTalon.set(0);
			
		} else {
			if(getLeftEncPos() > balancePosition){ //If not in place yet
				leftTalon.set(-0.5); 
				return false;
			} else 
				leftTalon.set(0);
						
		}
		
		//Reset the status variables
		pusshedPosition = false;
		balancePosition = 0;
		right = false;
		return true;
	}
	
	//Utils
	public int getRightEncPos(){
		return -rightTalon.getEncPosition();
	}
	public int getLeftEncPos(){
		return -leftTalon.getEncPosition();
	}

	//ArmRunnable - advanced stuff
	//Used to create discrete runnable task to perform various Arm control tasks
	public interface LucidTask {
		public void run();
		public boolean isDone();
		public LucidTask stop();
	}
}