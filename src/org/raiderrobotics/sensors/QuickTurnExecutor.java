package org.raiderrobotics.sensors;

import org.raiderrobotics.Robot;

import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Joystick;

public class QuickTurnExecutor {
    Joystick joystick;
    Robot main;
    Gyro gyro;

    double turnSpeed = 0.5;
    
    double angleCorrection = 35; //Error correction angle

    //Joystick buttons allocation
    //TODO determine the buttons
    //Joystick buttons matrix
    double[][] buttons = new double[][]{
            //Button number | Turn degree | turn for DriveTrain1 
            {5, -90, 0.5}, //90 Left
            {3, -180, 0.5}, //180 Left
            {6, 90, -0.5}, //90 Right
            {4, 180, -0.5} //180 Right
    };
    
    double[][] standardAxis = new double[][]{
            //Button number | Standard Degree | turn for DriveTrain1
            {7, 0, 0.5}, //x+
            {8, 180, 0.5}, //x-
            {9, 90, -0.5}, //y+
            {10, 270, -0.5} //y-
    };

    //Quick turn dynamic state variables
    //int buttonPressed = 0;
    boolean complete = false;

    public QuickTurnExecutor(Robot robotMain, Joystick joystick, Gyro gyro) {
        this.main = robotMain;
        this.joystick = joystick;
        this.gyro = gyro;
    }

    public void check() {
        for(int i=0; i<buttons.length; i++){ //Loop through all buttons
            int id = (int) buttons[i][0];
            if (joystick.getRawButton(id)) { //Find one that's pressed

                //if(buttonPressed != id) //If it id the first time button was pressed
                //    gyro.reset(); //Reset Gyro for better results

                //buttonPressed = id;
                turn(buttons[i]);
                return;
            }
        }
        
       for(int i=0; i<standardAxis.length; i++){ //Loop through all buttons
            int id = (int) standardAxis[i][0];
            if (joystick.getRawButton(id)) { //Find one that's pressed

                //if(buttonPressed != id) //If it id the first time button was pressed
                //    gyro.reset(); //Reset Gyro for better results

                //buttonPressed = id;
                turnToAxis(standardAxis[i]);
                return;
            }
        }
        
       // buttonPressed = 0;
        complete = false;
    }

    //Continue if a pressed button is found
    private void turn(double[] button){
        //Check for the throttle
        turnSpeed = (-joystick.getThrottle() + 1) / 2; //The throttle goes from -1 to 1, so we need to make it go from 0 to 1
                
        if(complete)
            return;
        
        double angle = button[1];
                
        if((angle > 0 && gyro.getAngle() < angle - (angleCorrection * turnSpeed)) || (angle < 0 && gyro.getAngle() > angle + (angleCorrection * turnSpeed))) //If not in position yet
            main.driveTrain1.drive(button[2]*turnSpeed, 1);
        else { //If the rotation is complete
            complete = true;
            main.driveTrain1.drive(0, 0);
        }
    }
    
    
    private void turnToAxis(double[] axis){
        turnSpeed = (-joystick.getThrottle() + 1) / 2; //The throttle goes from -1 to 1, so we need to make it go from 0 to 1
                
        if(complete)
            return;
        
        double angle = axis[1]-gyro.getAngle(); //the master formula: standrad degree - current degree = turn degree !!
                
        if((angle > 0 && gyro.getAngle() < angle - (angleCorrection * turnSpeed)) || (angle < 0 && gyro.getAngle() > angle + (angleCorrection * turnSpeed))) //If not in position yet
            main.driveTrain1.drive(axis[2]*turnSpeed, 1);
        else { //If the rotation is complete
            complete = true;
            main.driveTrain1.drive(0, 0);
        }
    }
}
