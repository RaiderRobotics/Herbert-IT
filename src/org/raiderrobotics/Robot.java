package org.raiderrobotics;

import org.raiderrobotics.sensors.ArmControl;
import org.raiderrobotics.sensors.QuickTurnExecutor;

import edu.wpi.first.wpilibj.*;

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
	
	ArmControl arm;
	
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
		arm = new ArmControl(xbox);
		arm.debug = true;
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
		arm.reset();		
	}

	// drive the robot normally
	private void normalDrive() {
		driveTrain1.arcadeDrive(logitech, true); //use squared inputs
				
		gyro.check();
		arm.tick();
	}

	/* This function is called periodically during test mode */
	public void testPeriodic() {
		
	}
}