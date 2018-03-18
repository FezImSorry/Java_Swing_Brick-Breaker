import java.awt.*;
import java.util.Random;

public class Block {
    int x, y, size, speed, vspeed;
    int dirX, dirY;
    int appletWdt, appletHgt;
    boolean grounded, solid, firstHit;

    int launch;

    public Block( int _x, int _y, int _size, int _hspeed, int _vspeed, int w, int h, boolean ground )
    {
        x = _x;		// (x, y) coordinates of ball
        y = _y;
        size = _size;	// width and height are same size
        speed = _hspeed;	// speed is number of pixels to move each time
        dirX = 1;		// direction is either +1 go right or -1 go left
        dirY = 1;		// direction is either +1 go down or -1 go up
        appletWdt = w;	// applet width
        appletHgt = h; 	// applet height
        grounded = ground;
        solid = true;
        vspeed = _vspeed;
        launch = 0;
    }

    public void paint( Graphics g )			// called from applet
    {
        g.setColor( Color.BLUE );
        g.fillRect( x, y, size, size );
    }

    public void paintRed( Graphics g )			// called from applet
    {
        g.setColor( Color.RED );
        g.fillRect( x, y, size, size );
    }

    public void move()
    {
    	if (launch == 2)
    	{
    		if (size < 15)
        		moveHigh();
    		else
    			moveMed();
    	}
    	else if (launch == 1)
    	{
    		if (size < 15)
        		moveMed();
    		else
    			moveLow();
    	}
    	else if (launch == 0)
    	{
    		if (size < 15)
        		moveLow();
    		else
    			moveSuperLow();
    	}
    	else
    	{
        	x = x + speed * dirX;
            y = y + vspeed * dirY;
    	}

        if ( x < 0 )		  // hit left boundary, change direction
        {
        	dirX = 1;
        	solid = true;
        }
        else if ( x + size > appletWdt )  // hit right boundary, change direction
        {
        	dirX = -1;
        	solid = true;
        }
        if ( y < 0 )		  // hit top boundary, change direction
        {
        	dirY = 1;
        	solid = true;
        }
        else if ( y > appletHgt )	  // hit bottom boundary, change direction
        {
        	dirY = -1;
        	solid = false;
        }
    }

    public void moveHigh()
    {
    	x = x + speed * dirX;
        y = y + vspeed * dirY;

        if (y < 250)
        	vspeed = 7;

        if (y < 200)
        	vspeed = 5;

        if (y < 150)
        	vspeed = 4;

        if (y < 110)
        	vspeed = 3;

        if (y < 90 )
        	vspeed = 2;

        if (y < 80)
        	vspeed = 1;

        if (y < 70)
        {
        	dirY = 1;
            solid = true;
        }
    }

    public void moveMed()
    {
    	x = x + speed * dirX;
        y = y + vspeed * dirY;

        if (y < 250)
        	vspeed = 6;

        if (y < 225)
        	vspeed = 5;

        if (y < 195)
        	vspeed = 4;

        if (y < 165)
        	vspeed = 3;

        if (y < 125)
        	vspeed = 2;

        if (y < 110)
        	vspeed = 1;

        if (y < 100)
        {
        	dirY = 1;
            solid = true;
        }
    }

    public void moveLow()
    {
    	x = x + speed * dirX;
        y = y + vspeed * dirY;

        if (y < 280)
        	vspeed = 5;

        if (y < 250)
        	vspeed = 4;

        if (y < 230)
        	vspeed = 3;

        if (y < 200)
        	vspeed = 2;

        if (y < 175)
        	vspeed = 1;

        if (y < 160)
        {
        	dirY = 1;
            solid = true;
        }
    }

    public void moveSuperLow()
    {
    	x = x + speed * dirX;
        y = y + vspeed * dirY;

        if (y < 300)
        	vspeed = 5;

        if (y < 250)
        	vspeed = 4;

        if (y < 200)
        	vspeed = 3;

        if (y < 160)
        	vspeed = 2;

        if (y < 140)
        	vspeed = 1;

        if (y < 130)
        {
        	dirY = 1;
            solid = true;
        }
    }

    public void launchHigh()
    {
    	if (firstHit)
    	{
    		dirY = -1 * dirY;
    	}
        grounded = false;
    	vspeed = 8;
        launch = 2;
    	speed = 2;

        move();
    }

    public void launchMed()
    {
    	if (firstHit)
    	{
    		dirY = -1 * dirY;
    	}
        grounded = false;
    	vspeed = 7;
        launch = 1;
    	speed = 1;

        move();
    }

    public void launchLow()
    {
    	if (firstHit)
    	{
    		dirY = -1 * dirY;
    	}
        grounded = false;
    	vspeed = 6;
    	launch = 0;
    	speed = 1;

        move();
    }

    public void stop(Rectangle swing, boolean leftUp )
    {					 // move ball based on speed and direction
    	grounded = true;

    	if (!leftUp)
        {
        	x = swing.x;
            y = swing.y + swing.height - size - 15;
        }
        else
        {
        	x = swing.x + swing.width - size;
        	y = swing.y + swing.height - size - 15;
        }
    }

    public void respawn()
    {
    	x = appletWdt/2;
    	y = 120;
        dirX = 1;
        dirY = 1;		// direction is either +1 go down or -1 go up
    	solid = true;
        grounded = false;
        vspeed = 3;
        speed = 0;
        launch = -1;

        move();
    }
}
