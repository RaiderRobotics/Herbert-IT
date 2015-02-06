package org.raiderrobotics;

import org.raiderrobotics.sensors.QuickTurnExecutor;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.CANTalon.ControlMode;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	final static int ARCADE = 1;
	final static int TANK = 2;

	//create object references
	Joystick xbox, logitech;
	public RobotDrive driveTrain1;
	public Talon talon1, talon2;

	//Gyro variables
	QuickTurnExecutor gyro;
	
	//Talon variables
	CANTalon leftTalon;
	CANTalon rightTalon;
	
	//Arm Encoders
//	Encoder rightArmEncoder;
//	Encoder leftArmEncoder;
	
	/*This function is run when the robot is first started up and should be used for any initialization code. */
	public void robotInit() {
		talon1 = new Talon(1);
		talon2 = new Talon(2);

		//reversing 1,2 and 3,4 will switch front and back in arcade mode.
		driveTrain1 = new RobotDrive(talon1, talon2);

		//this works to fix arcade joystick 
		driveTrain1.setInvertedMotor(RobotDrive.MotorType.kFrontLeft,true);
		driveTrain1.setInvertedMotor(RobotDrive.MotorType.kRearLeft,true);
		driveTrain1.setInvertedMotor(RobotDrive.MotorType.kFrontRight,true);
		driveTrain1.setInvertedMotor(RobotDrive.MotorType.kRearRight,true);

		xbox = new Joystick(1);
		logitech = new Joystick(0);
		
		gyro = new QuickTurnExecutor(this, logitech, new Gyro(new AnalogInput(0)));
		
		leftTalon = new CANTalon(3);
		rightTalon = new CANTalon(4);
		
//		leftArmEncoder = new Encoder(4, 5);
//		rightArmEncoder = new Encoder(6, 7);
		
//		rightArmEncoder.setDistancePerPulse(0.1);
//		leftArmEncoder.setDistancePerPulse(0.1);
	}
	
	/* This function is called periodically during autonomous */
	public void autonomousPeriodic() {

	}

	/* This function is called periodically during operator control */
	// called at 50Hz (every 20ms). This method must not take more than 20ms to complete!
	public void teleopPeriodic() {
		normalDrive();
	}
	
	public void teleopInit(){
		leftTalon.enableControl();
		leftTalon.set(0);
		leftTalon.changeControlMode(ControlMode.PercentVbus);
		
		rightTalon.enableControl();
		rightTalon.set(0);
		rightTalon.changeControlMode(ControlMode.PercentVbus);
		
		leftTalon.setPosition(0);
		rightTalon.setPosition(0);
//		rightArmEncoder.reset();
//		leftArmEncoder.reset();
		
		rightTalon.reverseOutput(true);
		leftTalon.reverseOutput(false);
	}

	// drive the robot normally
	private void normalDrive() {
		driveTrain1.arcadeDrive(logitech, true); //use squared inputs
				
		gyro.check();
		armTalonDrive();
	}
	
	
	//NOTE: 2 is xBox left throttle
	//      3 is xBox right throttle	
	//Encoder position variables
	boolean pusshedPosition;
	
	double position=0;
	boolean right;
	
	void armTalonDrive(){
		//TODO IMPORTANT: This code is not final, major changer will take effect as soon as I get time to do that.
		
		//Test encoder button
		//Calibration button (A temporary solution to the talons getting desynchronized)
		if(xbox.getRawButton(4)){
			if(!pusshedPosition){
				pusshedPosition = true;
				if(rightTalon.getEncPosition() > leftTalon.getEncPosition()){
					position = rightTalon.getEncPosition();
					right = true;
				} else if(leftTalon.getEncPosition() > rightTalon.getEncPosition()){
					position = leftTalon.getEncPosition();
					right = false;
				}
				
				System.out.println("Position: "+position+" ; Right: "+right);
			}
			
			if(right){ //If moving the right talon
				if(rightTalon.getEncPosition() > leftTalon.getEncPosition()){ //If not in place yet
					rightTalon.set(-0.5); 
				} else 
					rightTalon.set(0);
			} else {
				if(leftTalon.getEncPosition() > rightTalon.getEncPosition()){ //If not in place yet
					leftTalon.set(-0.5); 
				} else 
					leftTalon.set(0);
			}
					
			return;
		} else {
			//Reset the status variables
			pusshedPosition = false;
			position = 0;
			right = false;
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
		
		//The actual code that does useful stuff
		
		//Get the direction of the two triggers (and altitude)
		double move = xbox.getRawAxis(3) - xbox.getRawAxis(2);
		
		//If no buttons pressed (or the delta is less than 0.15)
		if(Math.abs(move) < 0.15){
			//Stop the talons
			leftTalon.set(0);
			rightTalon.set(0);
			return;
		}
		
		//Percentage at what the talons will move 
		//when one is going faster than the other one
		double rightCut = 0.95;
		double leftCut = 0.95;
			
		//Determining if one talon is moving faster than the other one 
		double right = Math.abs(rightTalon.getEncPosition()) > Math.abs(leftTalon.getEncPosition()) 
				? rightCut 
				: 1;
		double left = Math.abs(leftTalon.getEncPosition()) > Math.abs(rightTalon.getEncPosition()) 
				? leftCut 
				: 1;
		
		//Move the talons based on their determined speeds
		leftTalon.set(move * left);
		rightTalon.set((move * right));
	}

	/* This function is called periodically during test mode */
	public void testPeriodic() {

	}
}