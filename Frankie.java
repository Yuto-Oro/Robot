package TestBot;
import robocode.*;
import robocode.util.*;
import java.awt.*;
import static robocode.util.Utils.normalRelativeAngleDegrees;

public class Frankie extends AdvancedRobot
{
	static final double distanciaF = Double.POSITIVE_INFINITY;
	static double fuerzaX;
	static double fuerzaY;
	int dist = 40;	

	public void run() 
	{
		setBodyColor(Color.black);
		setGunColor(Color.red);
		setRadarColor(Color.white);
		setScanColor(Color.pink);
		setBulletColor(Color.green);
		setAdjustGunForRobotTurn(true);
    	turnRadarRightRadians(distanciaF);
		while(true) 
		{
			ahead(100);
			turnGunRight(360);
			back(75);
			turnGunRight(360);
		}
	}
	public void onScannedRobot(ScannedRobotEvent e) 
	{
		double absoluteBearing = getHeadingRadians() + e.getBearingRadians();
    	fuerzaX = fuerzaX * 0.9 - Math.sin(absoluteBearing) / e.getDistance() * Math.random();
    	fuerzaY = fuerzaY * 0.9 - Math.cos(absoluteBearing) / e.getDistance() / ((double)getTime() * 0.13);
	    setMaxVelocity(400.0 / getTurnRemaining());
	    if (e.getDistance() < 300.0 || getOthers() == 1) 
		{
	        setTurnRadarLeftRadians(getRadarTurnRemaining());
	    }
	    setTurnGunRightRadians(Utils.normalRelativeAngle((double)(absoluteBearing - getGunHeadingRadians() + e.getVelocity() * Math.sin(e.getHeadingRadians() - absoluteBearing) / 13.0)));
	    setAhead(distanciaF);
	    setTurnRightRadians(Utils.normalRelativeAngle((double)(Math.atan2(fuerzaX + 1.0 / getX() - 1.0 / (getBattleFieldWidth() - getX()), fuerzaY + 1.0 / getY() - 1.0 / (getBattleFieldHeight() - getY())) - getHeadingRadians())));
	    fire(2.5 - Math.max(0.0, (30.0 - getEnergy()) / 16.0));
	}
	
	public void onHitByBullet(HitByBulletEvent e) 
	{
		double energia = getEnergy();
		double bearing = e.getBearing();
		if(energia < 100)
		{
			turnRight(normalRelativeAngleDegrees(90 - (getHeading() - e.getHeading())));
			ahead(dist);
			dist *= -1;
			scan();
			turnRight(-bearing);
			ahead(100);
		}else
			turnRight(360);
		back(10);
	}
	public void onHitWall(HitWallEvent e) 
	{
		double wall = e.getBearing();
		turnRight(-wall);
		ahead(100);
	}	
}
