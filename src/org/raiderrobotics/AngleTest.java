package org.raiderrobotics;

public class AngleTest {

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
	
	//So far, this only handles Y-Axis ( turning to 0 or 180 degrees)
	public static void main(String[] args) {
		int targetAngle = 0;
		int currentAngle = 34; //get from Gyro;
		int quadrant = 1;  	//  4 | 1		North=0,   East=90
							//  3 | 2 		South=180, West=270
		Direction turnDirection = Direction.CW;
		//use cAngle for this instead of currentAngle;
		
		System.out.println("Positive angles" + turnDirection.dir);
		for (int cAngle = 0; cAngle <= 1000; cAngle += 10) {


			int rem = Math.abs(cAngle % 360);
			if (rem > 0 && rem <= 90) quadrant=1;
			if (rem > 90 && rem < 180) quadrant=2;
			if (rem > 180 && rem <= 270) quadrant=3;
			if (rem > 270 && rem < 360) quadrant=4;
			if (cAngle%180 ==0) {
				System.out.printf("Angle=%d  %n", cAngle);
				continue;
			}
				
			//set direction and target angle
			switch(quadrant) {				
			case 1:
				turnDirection = Direction.CCW;
				targetAngle = 0 + 360*(cAngle/360);
				break;
			case 2:
				turnDirection = Direction.CW;
				targetAngle = 180 + 360*(cAngle/360);
				break;
			case 3:
				turnDirection = Direction.CCW;
				targetAngle = 180 + 360*(cAngle/360);
				break;
			case 4:
				turnDirection = Direction.CW;
				targetAngle = 360 + 360*(cAngle/360);
				break;	
			default:
				System.out.println("****** ERROR - no quadrant selected!");
			}
			System.out.printf("Angle=%d /180 = %d, /360 = %d, Q=%d target=%d ", cAngle, cAngle%180, cAngle%360, quadrant, targetAngle);
			System.out.println(turnDirection == Direction.CW ? "+" : "-");
		}
		

		System.out.println("\n Negative angles");
		//we could also just add 360, 720, ... to the negative angle to make it positive.
		//However, then we need to figure out the target angle differently. (it must be negative)
		
		for (int cAngle = 0; cAngle >= -1100; cAngle -= 10) {
			int rem = cAngle % 360;
			if (rem >= -90 && rem < 0) quadrant=4;
			if (rem > -180 && rem < -90) quadrant=3;
			if (rem >= -270 && rem < -180) quadrant=2;
			if (rem > -360 && rem < -270) quadrant=1;
			
			if (cAngle%180 ==0) {
				System.out.printf("Angle=%d  %n", cAngle);
				continue;
			}
			
			//set direction and target angle
			switch(quadrant) {				
			case 1:
				turnDirection = Direction.CCW;
				targetAngle = -360 + 360*(cAngle/360);
				break;
			case 2:
				turnDirection = Direction.CW;
				targetAngle = -180 + 360*(cAngle/360);
				break;
			case 3:
				turnDirection = Direction.CCW;
				targetAngle =  -180 + 360*(cAngle/360) ;
				break;
			case 4:
				turnDirection = Direction.CW;
				targetAngle =  0 + 360*(cAngle/360);
				break;	
			default:
				System.out.println("****** ERROR - no quadrant selected!");
			}
			System.out.printf("Angle=%d /180 = %d, /360 = %d, Q=%d target=%d ", cAngle, cAngle%180, cAngle%360, quadrant, targetAngle);
			System.out.println(turnDirection == Direction.CW ? "+" : "-");
			
//			rem = angle % 360;
//			if (rem < 0) rem +=360;
//			if (rem > 0 && rem < 90) quadrant=1;
//			if (rem > 90 && rem < 180) quadrant=2;
//			if (rem > 180 && rem < 270) quadrant=3;
//			if (rem > 270 && rem < 360) quadrant=4;
//			System.out.printf("Q=%d %n", quadrant);
			
		}
	}

}
