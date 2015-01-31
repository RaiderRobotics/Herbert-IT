package org.raiderrobotics;

//import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.*;

import static org.raiderrobotics.RobotMap.*;

public class Robot extends IterativeRobot {
	
	/* Usage of Direction enum:
	 * Direction turnDirection = Direction.CW;
	 * turnDirection.dir  will return a +1 or -1
	 * This makes sure that no other possibilities are allowed for turnDirection.
	*/
	enum Direction {CW (+1), CCW (-1);
		private final int dir;
		Direction(int dir) {
			this.dir = dir;
		}
	};
	
	//TODO: test angles > 360; test angles < -360
	
	//Create object references
	Joystick logitech, xbox360;
	public RobotDrive driveTrain1;
	Talon talon1, talon2;
	Gyro gyro1;

	boolean isTurning = false;
	Direction turnDirection = Direction.CW;
	int currentAngle = 0;	//should these be doubles?
	int targetAngle = 0;
	double turnSpeed = 0.8;
	/*This function is run when the robot is first started up and should be used for any initialization code.
	* Create global objects here.
	*/
	
	
	public void robotInit() {
		talon1 = new Talon(TALON_1_PORT);
		talon2 = new Talon(TALON_2_PORT);

		//this is supposed to shut off the motors when joystick is at zero to save power.
		//Does it work only on Jaguars?
		talon1.enableDeadbandElimination(true);
		talon2.enableDeadbandElimination(true);

		//reversing 1,2 and 3,4 will switch front and back in arcade mode.
		driveTrain1 = new RobotDrive(talon1, talon2);

		//this works to fix arcade joystick
		driveTrain1.setInvertedMotor(RobotDrive.MotorType.kFrontLeft,true);
		driveTrain1.setInvertedMotor(RobotDrive.MotorType.kRearLeft,true);
		driveTrain1.setInvertedMotor(RobotDrive.MotorType.kFrontRight,true);
		driveTrain1.setInvertedMotor(RobotDrive.MotorType.kRearRight,true);

		logitech = new Joystick(LOGITECH_PORT);
		xbox360 = new Joystick(XBOX_PORT);
		
		gyro1 = new Gyro(0);
		gyro1.reset();
	}

	/* This function is called periodically during operator control.
	* Called at 50Hz (every 20ms). This method must not take more than 20ms to complete!
	*/
	public void teleopPeriodic() {
		normalDrive();
	}

	// Drive the robot normally
	//Temporarily: we are allowing either controller to work
    private void normalDrive() {
        double stick1X = logitech.getX();
        double stick1Y = logitech.getY();
        double stick2X = xbox360.getX(); //this is the same as xbox.getRawAxis(1)
        double stick2Y = xbox360.getY(); //this is the same as xbox.getRawAxis(2)

        //drive using whichever joystick is pushed further from zero in any direction
        double n1 = stick1X * stick1X + stick1Y * stick1Y;
        double n2 = stick2X * stick2X + stick2Y * stick2Y;

	if (xbox360.getRawButton(XBOX_BTN_Y) {
		setUpAngles();
	}
	
	if (isTurning) {
		turnByGyro();
	}
	// NOTE: there is no interrupting this turning yet. The joystick will only mess it up now.
	
        //If using an xBox controller
        if (n1 <= n2) {
            if (xbox360.getRawButton(XBOX_BUMPER_R)) {//high speed mode
                double x2max = stick2X * (MAXSPEED / 100.0);
                double y2max = stick2Y * (MAXSPEED / 100.0);

                driveTrain1.arcadeDrive(y2max, x2max, true); //use squared inputs
            } else {
                double x2norm = stick2X * (NORMSPEED / 100.0);
                double y2norm = stick2Y * (NORMSPEED / 100.0);

                driveTrain1.arcadeDrive(y2norm, x2norm, true);
            }
        }

        //If using a Logitech controller
        else {
            if (logitech.getRawButton(LOGITECH_TRIGGER)) {
                double x1max = stick1X * (MAXSPEED / 100.0);
                double y1max = stick1Y * (MAXSPEED / 100.0);

                driveTrain1.arcadeDrive(y1max, x1max, true); //use squared inputs
            } else {
                double x1norm = stick1X * (NORMSPEED / 100.0);
                double y1norm = stick1Y * (NORMSPEED / 100.0);

                driveTrain1.arcadeDrive(y1norm, x1norm, true);
            }
        }
    }

  /* This function is called periodically during autonomous */
	public void autonomousPeriodic() { }

	/* This function is called periodically during test mode */
	/*** Run only one side of robot drive - based on logitech buttons****/
	public void testPeriodic() {
		if (logitech.getRawButton(LOGITECH_BTN3) ) {
			talon1.set(logitech.getY());
			talon2.stopMotor();
		} else if (logitech.getRawButton(LOGITECH_BTN4) ) {
			talon2.set(logitech.getY());
			talon1.stopMotor();
		} else {
			talon1.stopMotor();
			talon2.stopMotor();
		}
	}
	
	void setUpAngles() {
		int quadrant = 1;
		currentAngle = (int)gyro1.getAngle();
		if (currentAngle > 0) {
			int rem = Math.abs(currentAngle % 360);
			if (rem > 0 && rem <= 90) quadrant=1;
			if (rem > 90 && rem < 180) quadrant=2;
			if (rem > 180 && rem <= 270) quadrant=3;
			if (rem > 270 && rem < 360) quadrant=4;
			
			if (currentAngle%180 ==0) {
				isTurning = false;
				return;
			}
			switch(quadrant) {				
			case 1:
				turnDirection = Direction.CCW;
				targetAngle = 0 + 360*(currentAngle/360);
				break;
			case 2:
				turnDirection = Direction.CW;
				targetAngle = 180 + 360*(currentAngle/360);
				break;
			case 3:
				turnDirection = Direction.CCW;
				targetAngle = 180 + 360*(currentAngle/360);
				break;
			case 4:
				turnDirection = Direction.CW;
				targetAngle = 360 + 360*(currentAngle/360);
				break;	
			default:
				System.out.println("****** ERROR - no quadrant selected!");
			}
		} else {
			int rem = currentAngle % 360;
			if (rem >= -90 && rem < 0) quadrant=4;
			if (rem > -180 && rem < -90) quadrant=3;
			if (rem >= -270 && rem < -180) quadrant=2;
			if (rem > -360 && rem < -270) quadrant=1;
			
			if (currentAngle%180 ==0) {
				isTurning = false;
				return;
			}
			
			//set direction and target angle
			switch(quadrant) {				
			case 1:
				turnDirection = Direction.CCW;
				targetAngle = -360 + 360*(currentAngle/360);
				break;
			case 2:
				turnDirection = Direction.CW;
				targetAngle = -180 + 360*(currentAngle/360);
				break;
			case 3:
				turnDirection = Direction.CCW;
				targetAngle =  -180 + 360*(currentAngle/360) ;
				break;
			case 4:
				turnDirection = Direction.CW;
				targetAngle =  0 + 360*(currentAngle/360);
				break;	
			default:
				System.out.println("****** ERROR - no quadrant selected!");
			}
		}
		isTurning = true;
	} //end of method
	
	//later this should be fixed up with PID controls.
	void turnByGyro() {
		//turn robot
		driveTrain.arcadeDrive(0, turnSpeed * turnDirection.dir);
		
		//update angle
		currentAngle = (int)gyro1.getAngle();
		System.out.println("angle=" + currentAngle);
		//see if we need to stop turning. These also work for negative angles.
		if (turnDirection == Direction.CW) { // current angle is less than target angle
			if (currentAngle >= targetAngle) isTurning = false;
		}
		if (turnDirection == Direction.CCW) { // current angle is greater than target angle
			if (currentAngle <= targetAngle) isTurning = false;
		}
	}
}
