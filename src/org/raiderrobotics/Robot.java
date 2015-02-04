package org.raiderrobotics;

import edu.wpi.first.wpilibj.*;

/****
TEST OF MOTOR DRIVING DIRECTION

RESULTS: 
>>> buttons 2, 4, 5: all drove forwards
>>> setting the Talons directly made the right hand one go backwards
>>> The drive() function was by far the fastest!!

***/ 
public class Robot extends IterativeRobot {
  
	//create object references
	Joystick leftStick;
	public RobotDrive driveTrain1;
	Talon talon1, talon2;

	public void robotInit() {
		talon1 = new Talon(1);
		talon2 = new Talon(2);
		leftStick = new Joystick(0);
		driveTrain1 = new RobotDrive(talon1, talon2);
	}

	public void teleopPeriodic() {
		
		if (leftStick.getRawButton(2)) {
			driveTrain1.arcadeDrive(0.5, 0);
		} else if (leftStick.getRawButton(3))  {
			talon1.set(0.5);	//set talon speeds directly
			talon2.set(0.5);
		} else if (leftStick.getRawButton(4)) {
			driveTrain1.drive(0.5, 0);
		} else if (leftStick.getRawButton(5)) {
			driveTrain1.tankDrive(0.5, 0.5);
		} else { //stop motors
			talon1.set(0.0);
			talon2.set(0.0);
		}
	}

}
