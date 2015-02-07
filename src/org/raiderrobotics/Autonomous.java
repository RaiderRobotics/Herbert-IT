package org.raiderrobotics;
import edu.wpi.first.wpilibj.*;
import static org.raiderrobotics.RobotMap.*;

public class Autonomous {
	Talon talon1, talon2;
	Encoder distEncoder;
	int programUsed;
	
	boolean isInAutoZone = false;
	
	public Autonomous(Talon talonA, Talon talonB,Encoder encoderA, int currAutoProgram){
		talon1 = talonA;
		talon2 = talonB;
		distEncoder = encoderA;
		programUsed = currAutoProgram;
		System.out.println("got to the constructer");
	}
	
	public void init(){
		isInAutoZone = false;
		distEncoder.reset();
	}
	
	public void run(){
		System.out.println("got to the run method");
		if(programUsed == AUTO_RECYCLE){
			autoRecycle();
		}
	}
	
	
	public void autoRecycle(){
		System.out.println("got to he autorecycle method");
		if(isInAutoZone){
			
			if(distEncoder.getDistance() > AUTO_BACKUP_DISTANCE){
				talon1.set(-0.5);
				talon2.set(0.5); 
			}else{
				talon1.stopMotor();
				talon2.stopMotor();	
			}
			
		}else{
			
			if(distEncoder.getDistance() < AUTO_ZONE_DISTANCE){
				talon1.set(0.25);
				talon2.set(-0.25); 
			}else{
				isInAutoZone = true;
				distEncoder.reset();
				talon1.stopMotor();
				talon2.stopMotor();
			}
			
		}
	}
	
	public void rampUpSpeed(int speed){
		
	}
	
}
