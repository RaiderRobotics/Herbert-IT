package org.raiderrobotics;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.DrawMode;
import com.ni.vision.NIVision.Image;


import com.ni.vision.NIVision.ShapeMode;


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

	int session;
	Image frame;

	//constants
	final static int ARCADE = 1;
	final static int TANK = 2;

	//global variables
	private int driveState = ARCADE;

	//create object references
	Joystick leftStick, rightStick;
	public RobotDrive driveTrain1;
	Talon talon1, talon2;


	/*This function is run when the robot is first started up and should be used for any initialization code. 
	 * Create global objects here.
	 */

	public void robotInit() {

		frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);

		// the camera name (ex "cam0") can be found through the roborio web interface
		session = NIVision.IMAQdxOpenCamera("cam0",
				NIVision.IMAQdxCameraControlMode.CameraControlModeController);
		NIVision.IMAQdxConfigureGrab(session);

		talon1 = new Talon(0);
		talon2 = new Talon(1);

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
		NIVision.IMAQdxStartAcquisition(session);

		
		
		while (isOperatorControl() && isEnabled()) {
			//grabs an image
			NIVision.IMAQdxGrab(session, frame, 1);
			//sends the image to the smart dashboard
			CameraServer.getInstance().setImage(frame);

			normalDrive();

			//check for button press to switch mode. Use two buttons to prevent bounce.
			boolean button2 = leftStick.getRawButton(2);
			boolean button3 = leftStick.getRawButton(3);
			if (button2) driveState = ARCADE;
			if (button3) driveState = TANK;

			Timer.delay(0.005);		// wait for a motor update time
		}
		NIVision.IMAQdxStopAcquisition(session);

	}

	// Drive the robot normally
	private void normalDrive() {
		if (driveState == ARCADE) {
			driveTrain1.arcadeDrive(leftStick, true); //use squared inputs
		} else {
			driveTrain1.tankDrive(leftStick, rightStick);
		}
	}

	/* This function is called periodically during autonomous */
	public void autonomousPeriodic() { }

	/* This function is called periodically during test mode */
	public void testPeriodic() { }

}
