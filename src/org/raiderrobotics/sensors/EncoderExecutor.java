package org.raiderrobotics.sensors;

import org.raiderrobotics.Robot;
import edu.wpi.first.wpilibj.Encoder;

public class EncoderExecutor {
	Robot main;
	Encoder encoder1;
	Encoder encoder2;
	
	public EncoderExecutor(Robot main, Encoder encoder1){
		this.main = main;
		this.encoder1 = encoder1;
		this.encoder1.setDistancePerPulse(0.18); 
		//0.0391 is what was calculated, 
		//but 0.18 seems to be more accurate
		//(I don't remember how I got this number)		
	}
	
	//Ticking method
	public void check(){
		System.out.println("Distance: " + encoder1.getDistance());
		System.out.println("Raw: " + encoder1.getRaw()); //Returns current pulse index (with direction preserved) 
	} 	
}
