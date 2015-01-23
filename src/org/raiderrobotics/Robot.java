package org.raiderrobotics;

//import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.*;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
  
    //constants
	final static int ARCADE = 1;
	final static int TANK = 2;
	final static int NORMSPEED = 70;	//% speed of normal driving
	final static int MAXSPEED = 95;		//% speed of maximum motor output for fast driving
										//drive team said that 100% is too fast for manual driving
	//global variables
	//private int driveState = ARCADE;
	
	//create object references
	Joystick leftStick, rightStick;
	public RobotDrive driveTrain1;
	Talon talon1, talon2;


	/*This function is run when the robot is first started up and should be used for any initialization code. 
	* Create global objects here.
	*/
	
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

		leftStick = new Joystick(0);
		rightStick = new Joystick(1);
	}

	/* This function is called periodically during operator control.
	* Called at 50Hz (every 20ms). This method must not take more than 20ms to complete! 
	*/
	public void teleopPeriodic() {
		
		normalDrive();

	}

	// Drive the robot normally
	private void normalDrive() {
		double stickX = leftStick.getX();
		double stickY = leftStick.getY();
		
		double xnorm = stickX * (NORMSPEED/100.0);
		double ynorm = stickY * (NORMSPEED/100.0);
		double xmax = stickX * (MAXSPEED/100.0);
		double ymax = stickY * (MAXSPEED/100.0);
		//use 6 for XBOX; 1 for Logitech
		if (leftStick.getRawButton(6)) {
			driveTrain1.arcadeDrive(ymax, xmax); //use squared inputs
		} else {
			driveTrain1.arcadeDrive(ynorm, xnorm);
		}
	}

  /* This function is called periodically during autonomous */
	public void autonomousPeriodic() { }

	/* This function is called periodically during test mode */
	public void testPeriodic() { }

}
