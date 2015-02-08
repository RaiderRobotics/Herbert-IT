package org.raiderrobotics.sensors;

import com.copperboard.ConfigurationApi.ConfigurationAPI;
import com.copperboard.ConfigurationApi.ConfigurationAPI.ConfigurationSection;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.ControlMode;
import edu.wpi.first.wpilibj.Joystick;

public class ArmControl {
	//Talon variables
	CANTalon leftTalon;
	CANTalon rightTalon;

	//NOTE: 2 is xBox left throttle
	//      3 is xBox right throttle	
	//Encoder position variables
	boolean pusshedPosition; //If pushed the reset button	
	double balancePosition=0; //Position for the right or left talon to go to
	boolean right; //True if the right talon has to be moved to balance
	
	Joystick xbox;
	
	//Test purposes
	ConfigurationSection config = ConfigurationAPI.load("/home/lvuser/config.yml");
	
	public boolean debug;
	
	public ArmControl(Joystick xbox){
		this.xbox = xbox;
		
		leftTalon = new CANTalon(3);
		rightTalon = new CANTalon(4);
		
		leftTalon.enableControl();
		leftTalon.set(0);
		leftTalon.changeControlMode(ControlMode.PercentVbus);
		
		rightTalon.enableControl();
		rightTalon.set(0);
		rightTalon.changeControlMode(ControlMode.PercentVbus);
		
		rightTalon.reverseOutput(true);
		leftTalon.reverseOutput(false);
		
		reset();
	}	
	
	public void tick(){
		if(debug){
			System.out.println("\nRight Pos: "+rightTalon.getEncPosition());
			System.out.println("Left Pos: "+leftTalon.getEncPosition());
		}
		
		//TODO IMPORTANT: This code is not final, major changer will take effect Soon(tm)

		//Move the arms with a push of a button
		if(xbox.getRawButton(4)){
			move();
			return;
		}
		
		//TODO used for tests and debug only (should be removed on the actual competition)
		//Individual talons control
		if(xbox.getRawButton(1)){
			leftTalon.set(-0.5);
			return;
		}
		if(xbox.getRawButton(2)){
			rightTalon.set(-0.5);
			return;
		}
		
		//The actual code that does useful stuff
		
		//Get the direction of the two triggers (and altitude)
		double move = xbox.getRawAxis(3) - xbox.getRawAxis(2);
		
		//If no buttons pressed (or the delta is less than 0.15)
		if(Math.abs(move) < 0.15){
			//Stop the talons
			leftTalon.set(0);
			rightTalon.set(0);
			return;
		}
		
		//Percentage at what the talons will move 
		//when one is going faster than the other one
		double rightCut = 0.95;
		double leftCut = 0.95;
			
		//Determining if one talon is moving faster than the other one 
		double right = Math.abs(rightTalon.getEncPosition()) > Math.abs(leftTalon.getEncPosition()) 
				? rightCut 
				: 1;
		double left = Math.abs(leftTalon.getEncPosition()) > Math.abs(rightTalon.getEncPosition()) 
				? leftCut 
				: 1;
		
		//Move the talons based on their determined speeds
		leftTalon.set(move * left);
		rightTalon.set(move * right);
	}
	
	public void reset(){
		leftTalon.setPosition(0);
		rightTalon.setPosition(0);
		config.reload();
	}
	
	double speed = 0.5;
	//Position methods
	public void move(){
		double pos = config.getDouble("pos");
		double dr = pos - rightTalon.getEncPosition();
		double dl = pos - leftTalon.getEncPosition();
		
		if (dr < 5) {
			rightTalon.set(0);
		} else 
//			rightTalon.set(Math.abs(dr) < 0.5 ? 0 : Math.signum(dr) * speed);
			rightTalon.set(speed);
		
		if (dl < 5) {
			leftTalon.set(0);
		} else
			leftTalon.set(speed);		
	}
	
	boolean balance(){
		//Calibration button
		if(!pusshedPosition){
			pusshedPosition = true;
			if(rightTalon.getEncPosition() > leftTalon.getEncPosition()){
				balancePosition = rightTalon.getEncPosition();
				right = true;
			} else if(leftTalon.getEncPosition() > rightTalon.getEncPosition()){
				balancePosition = leftTalon.getEncPosition();
				right = false;
			}
			
			if(debug)
				System.out.println("Balance position: "+balancePosition+" ; Right: "+right);
		}
		
		if(right){ //If moving the right talon
			if(rightTalon.getEncPosition() > balancePosition){ //If not in place yet
				rightTalon.set(-0.5);
				return false;
			} else 
				rightTalon.set(0);
			
		} else {
			if(leftTalon.getEncPosition() > balancePosition){ //If not in place yet
				leftTalon.set(-0.5); 
				return false;
			} else 
				leftTalon.set(0);
						
		}
		
		//Reset the status variables
		pusshedPosition = false;
		balancePosition = 0;
		right = false;
		return true;
	}
}
