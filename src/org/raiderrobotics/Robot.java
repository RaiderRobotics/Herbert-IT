package org.raiderrobotics;

//import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.*;

import static org.raiderrobotics.RobotMap.*;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	//Create object references
	Joystick logitech, xbox360;
	public RobotDrive driveTrain1;
	Talon talon1, talon2;
	Encoder encoder1;

	/*This function is run when the robot is first started up and should be used for any initialization code.
	* Create global objects here.
	*/
	public void robotInit() {
		talon1 = new Talon(TALON_1_PORT);
		talon2 = new Talon(TALON_2_PORT);
		
		//encoder1 = new Encoder(final int aChannel, final int bChannel, boolean reverseDirection ); //Not sure of parameter contents
		//encoder1.setDistancePerPulse(); //Not sure parameter contents
		
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
	public void autonomousPeriodic() {
		  	
		while(encoder.getDistance() < AUTODISTANCE){
			driveTrain1.drive(0.7, 0.0);
			encoder.getDistance();
		}
		talon1.stopMotor();
		talon2.stopMotor();
	}

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
}
