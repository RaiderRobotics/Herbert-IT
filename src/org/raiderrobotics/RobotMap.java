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
	final static int MAXSPEED = 95;	    //% speed of maximum motor output for fast driving
						//drive team said that 100% is too fast for manual driving
	final static double AUTODISTANCE = 200.0;  //distance(m) that the robot needs to drive to reach the tote in autonomous
	/* joysticks and buttons */
	//Logitech joystick
	final static int LOGITECH_TRIGGER = 1;
	final static int LOGITECH_BTN2 = 2;
	final static int LOGITECH_BTN3 = 3;
	final static int LOGITECH_BTN4 = 4;
	final static int LOGITECH_BTN5 = 5;
	final static int LOGITECH_BTN6 = 6;
	//xbox 360 controller buttons
	final static int XBOX_BTN_A = 1;
	final static int XBOX_BTN_B = 2;
	final static int XBOX_BTN_X = 3;
	final static int XBOX_BTN_Y = 4;
	final static int XBOX_BUMPER_L = 5;
	final static int XBOX_BUMPER_R = 6;
	final static int XBOX_BTN_BACK = 7;
	final static int XBOX_BTN_START = 8;
	//Xbox 360 controller joysticks
	final static int XBOX_L_XAXIS = 0;
	final static int XBOX_L_YAXIS = 1;
	final static int XBOX_L_TRIGGER = 2;
	final static int XBOX_R_TRIGER = 3;
	final static int XBOX_R_XAXIS = 4;
	final static int XBOX_R_YAXIS = 5;
	
	/* Port allocation */
	//Joystick ports
	final static int LOGITECH_PORT = 0;
	final static int XBOX_PORT = 1;
	//motor controller ports
	final static int TALON_1_PORT = 1;
	final static int TALON_2_PORT = 2;

	//global variables that are NOT final
	
}
