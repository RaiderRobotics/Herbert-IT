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

//TEST OF MOTOR DRIVING DIRECTION 
public class Robot extends IterativeRobot {
  
	//create object references
	Joystick leftStick;
	public RobotDrive driveTrain1;
	Talon talon1, talon2;


	/*This function is run when the robot is first started up and should be used for any initialization code. 
	* Create global objects here.
	*/
	
	public void robotInit() {
		talon1 = new Talon(0);
		talon2 = new Talon(1);
		
		//reversing 1,2 and 3,4 will switch front and back in arcade mode.
		driveTrain1 = new RobotDrive(talon1, talon2);

		leftStick = new Joystick(0);
	}

	/* This function is called periodically during operator control.
	* Called at 50Hz (every 20ms). This method must not take more than 20ms to complete! 
	*/
	public void teleopPeriodic() {
		
		if (leftStick.getRawButton(2))  driveTrain1.arcadeDrive(0.5, 0);
		else if (leftStick.getRawButton(3))  {
			talon1.set(0.5);
			talon2.set(0.5);
		}
		else if (leftStick.getRawButton(4))  driveTrain1.drive(0.5, 0);
		else if (leftStick.getRawButton(5))  driveTrain1.tankDrive(0.5, 0.5);
		else {
			talon1.set(0.5);
			talon2.set(0.5);
		}


	}

  /* This function is called periodically during autonomous */
	public void autonomousPeriodic() { }

	/* This function is called periodically during test mode */
	public void testPeriodic() { }

}
