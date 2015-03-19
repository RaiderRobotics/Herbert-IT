package org.raiderrobotics.sensors;

import org.raiderrobotics.Robot;

import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Joystick;

public class GyroExecutor {
    Joystick joystick;
    Robot main;
    Gyro gyro;

    double turnSpeed = 0.5;
    
    double aimingAngle; //the variable to keep track on the destination angle 
    
    double[][] buttonMap = new double[][]{
            //Button number  |||  0 - turn x degrees; 1 - turn TO x degrees  |||  Turn degree
            {5, 0, -45}, //45 left
            {6, 0, 45}, //45 right
            {7, 0, 180}, //180
            {1, 1, 0}, //y+ (aka 0 degree)
            {2, 1, 90}, //x- (aka 90 degree)
            {3, 1, 180}, //y+ (aka 180 degree)
            {4, 1, 270} //x- (aka 270 degree)
    };
    
    public boolean complete = true;

    public GyroExecutor(Robot robotMain, Joystick joystick, Gyro gyro) {
        this.main = robotMain;
        this.joystick = joystick;
        this.gyro = gyro;
        this.gyro.reset(); //just in case
        gyro.setSensitivity(0.007); // "Our gyro is ADRSX622 , with a sensitivity of 7 mV/degree/sec" --Mr.Harwood
    }
    
    

    public void check() {
    	
    	//debug code starts
    		//System.out.println("Current angle: "+gyro.getAngle()+", reduced angle: "+gyro.getAngle()%360);
    	//debug code over
    	
    		
    	if(complete){
    		
    		for(int i=0; i<buttonMap.length; i++){ //Loop through all buttons
    			
    			int id = (int) buttonMap[i][0];
    			if (joystick.getRawButton(id)) { //Find one that's pressed
            	
    				if(buttonMap[i][1]==0){ //refer back to buttonMap[][] declaration 
    			        //since gyro doesnt stop at 360 degrees, gyro.getAngle()%360 gives us the angle within 360 degrees, cheers math!
    					aimingAngle = ((gyro.getAngle()%360) + buttonMap[i][2])%360;
    				}
    				
    				if(buttonMap[i][1]==1){
    					aimingAngle = buttonMap[i][2];
    				}
    				complete = false;
    				turn(aimingAngle);
    			}
    		}
    	}else{
    		turn(aimingAngle); //still doing the last turn
    	}
    }
   
    
    
    public void turn(double destinationAngle){  //please try to use it in the range of 0-360, for the sake of debug
    	double currentAngle = gyro.getAngle();
    	double offset = CircularOperation.offsetCostume(currentAngle, destinationAngle);
    	//turnSpeed = (-joystick.getThrottle() + 1) / 2; //The throttle goes from -1 to 1, so we need to make it go from 0 to 1
    	
    	if(Math.abs(offset) < 1){
    		main.driveTrain1.drive(0, 0);
        	complete = true; 
    	}else{
    		 if(offset > 0){
    			 main.driveTrain1.drive(-1*turnSpeed, 1); //turn right (aka clockwise)
    		 }else{
    			 main.driveTrain1.drive(1*turnSpeed, 1); //turn left (aka counter clockwise)
    		 }
    	}
    	
    	/*
    	
    	double currentAngle = gyro.getAngle()%360; //make sure everything is in the range of 0-360
    	destinationAngle = destinationAngle%360; //make sure everything is in the range of 0-360
    	
        //Check for the throttle
        turnSpeed = (-joystick.getThrottle() + 1) / 2; //The throttle goes from -1 to 1, so we need to make it go from 0 to 1
        
        if(Math.abs(destinationAngle - currentAngle) <= 1){ //stop the turn if the angle reaches 1 degree close to the destinationAngle
        	main.driveTrain1.drive(0, 0);
        	complete = true; 
        	return;
        }
        
        //90 $ 1 = 89 clockwise
        //90 $ 359 = 91 clockwise
        //270 $ 0 = 270 counterclockwise
        
        
        if( ( (destinationAngle - currentAngle) % 360 ) <= 180){  //turn clockwise if true, because the destinationAngle is under(or equal to) 180 degrees to the right (aka clockwise)
        	main.driveTrain1.drive(-0.5*turnSpeed, 1); //turn right (aka clockwise)
        }else{
        	main.driveTrain1.drive(0.5*turnSpeed, 1); //turn left (aka counter clockwise)
        }*/
    }

}
