package org.raiderrobotics;

//import edu.wpi.first.wpilibj.Joystick;

/* This class is a list of constants used in the main program. All must be static.
 * Should this class be abstract too?
*/
public final class RobotMap {

	/* FINAL constants */
	//final static int ARCADE = 1;
	//final static int TANK = 2;
	final static int NORMSPEED = 70;	//% speed of normal driving
	final static int MAXSPEED = 95;		//% speed of maximum motor output for fast driving
										//drive team said that 100% is too fast for manual driving
	/* joysticks and buttons */
	//static Joystick Xbox360;
	//static Joystick Logitech;
	final static int XBOX_BUMPER_L = 5;
	final static int XBOX_BUMPER_R = 6;
	final static int LOGITECH_TRIGGER = 1;
	final static int LOGITECH_BTN2 = 2;
	final static int LOGITECH_BTN3 = 3;
	
	//global variables that are NOT final
	
}

/*Raw Axis Index for Xbox are as follows :
1 - LeftX
2 - LeftY
3 - Triggers (Each trigger = 0 to 1, axis value = right - left)
4 - RightX
5 - RightY
6 - DPad Left/Right

EXAMPLE: driveTrain1.arcadeDrive(xbox.getRawAxis(2),xbox.getRawAxis(1),true);

However, if there is xbox control, the arcade drives are made using raw double values instead of a joystick class. 
These values are gotten using get raw axis on the left stick.
*/