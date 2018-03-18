/*
* Make the paddle usable by the user, arrow keys
 * Ball bounces off paddle collisions
 * still has flicker
 */
import java.awt.*;
import javax.swing.*;
import javax.swing.Timer;
import java.util.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
public class test extends JApplet  implements Runnable, KeyListener, ActionListener
{
    ArrayList blocks;
    int width, height;      // dimensions of applet
    int numBlocks;          // number of blocks horizontally
    int numRows;            // number of rows - 1 least difficult, 5 more difficult
    int ctrBlue, ctrRed, ctrTime;
    int lives;
    long i;
    // Part III
    //Rectangle paddle;
    int speed;
    // Part II
    Thread thread;
    Timer timer;
    BufferedImage buffer;

	Rectangle swing, high, med, low, floor;
	boolean leftUp;
	Block blueBlock, redBlock;
	Image bge;

    public void init( )
    {
        resize(600, 400 );
        numBlocks = 10;
        numRows = 3;
        blocks = new ArrayList( );
        width = getWidth( );
        height = getHeight( );
        buildBlocks( );
        leftUp = false;
        lives = 5;
        i = 2000L;
        // Part III
		swing = new Rectangle((width/2)-70, height-45, 120, 40);
		high = new Rectangle((swing.x+swing.width)-20, swing.y, 15, 40);
		med = new Rectangle((swing.x+swing.width)-40, swing.y+7, 15, 40-7);
		low = new Rectangle((swing.x+swing.width)-60, swing.y+13, 15, 40-13);
        floor = new Rectangle(0, height - 3, width, height);

        addKeyListener( this );
        speed = 10;
        setFocusable(true);

        // PART II
        blueBlock = new Block(50, 120, 10, 1, 3, width, height, false);
        redBlock = new Block(swing.x, swing.y + swing.height - 15, 20, 1, 5, width, height, true);

        thread = new Thread(this);
        thread.start( );
        ctrRed = 0;
        ctrBlue = 0;
        ctrTime = 0;
        timer = new Timer(1000, this);
        timer.start();
        buffer = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        bge = getImage( getCodeBase( ), "background.gif");

    }

    public void buildBlocks( )
    {
        int sizeOfBlock = width / numBlocks;
        int heightOfBlock = 15;
        for( int rows=0; rows<width; rows += sizeOfBlock )
            for( int cols=0; cols<numRows*heightOfBlock; cols += heightOfBlock )
            {
                Rectangle r = new Rectangle( rows,80+cols, sizeOfBlock-2, heightOfBlock-2 );
                blocks.add(r);
            }
    }

    public void update( Graphics g )
    {
    	paint(g);
    }

    public void paint( Graphics g )
    {


    	Graphics bg = buffer.getGraphics( );
		bg.fillRect(0,0,width,height);
    	//bg.drawImage(bge, 0, 0, null);
    	//bg.drawImage(bge, 200, 0, null);
    	//bg.drawImage(bge, 0, 203, null);

		blueBlock.paint(bg);
		redBlock.paintRed(bg);

		bg.setColor( Color.RED );
		bg.drawString("Red ball hit " + ctrRed + " bricks.", 10, 20);

		bg.setColor( Color.BLUE );
		//bg.drawString("Blue ball hit " + ctrBlue + " bricks.", 130, 20);
		bg.drawString("Lives: " + lives, 130, 20);

		bg.setColor( Color.BLACK );
		if (ctrTime <= 30)
		    bg.drawString("Time elapsed in seconds: " + ctrTime, 10, 40);
		else
			bg.drawString( "You exceeded time period! But you may continue the game", 10, 40 );


		     bg.setColor( Color.RED );

		    for (int i=0; i<blocks.size( ); i++ )       // color remaining blocks
		{
		    Rectangle r = (Rectangle )blocks.get(i);
		    bg.fillRect( r.x, r.y, r.width, r.height );
		}

		Polygon p = new Polygon();

		//bg.drawRect(swing.x, swing.y, swing.width, swing.height);
		//bg.drawRect(high.x, high.y, 15, 6);
		//bg.drawRect(med.x, med.y, 15, 6);
		//bg.drawRect(low.x, low.y, 15, 6);


		if(leftUp == false)
		{
			bg.setColor(Color.BLACK);
			p.addPoint(swing.x, swing.y + swing.height -7);
			p.addPoint(swing.x + 2, swing.y + swing.height);
			p.addPoint(swing.x + swing.width, swing.y + 7);
			p.addPoint(swing.x + swing.width -2, swing.y);
			bg.fillPolygon(p);
			bg.setColor(Color.BLUE);
			bg.fillOval((swing.x+(swing.width)/2)-5, (swing.y+(swing.height)/2)+3, swing.height/2-3, swing.height/2-3);
		}
		else
		{
			bg.setColor(Color.BLACK);
			p.addPoint(swing.x, swing.y + 7);
			p.addPoint(swing.x + 2, swing.y);
			p.addPoint(swing.x + swing.width, swing.y + swing.height - 7);
			p.addPoint(swing.x + swing.width -2, swing.y + swing.height);
			bg.fillPolygon(p);
			bg.setColor(Color.BLUE);
			bg.fillOval((swing.x+(swing.width)/2)-9, (swing.y+(swing.height)/2)+3, swing.height/2-3, swing.height/2-3);
		}


		g.drawImage(buffer, 0, 0,this);

    }

    public void run( )
    {
       while( true )
        {
            if(blueBlock.grounded)
            {
            	blueBlock.stop(swing, leftUp);
            	redBlock.move();
            }
            else if (redBlock.grounded)
            {
            	redBlock.stop(swing, leftUp);
            	blueBlock.move();
            }

            checkForCollision( );
            repaint( );

            if (blocks.size() == 0 || lives == 0)
            	thread.stop();

            try {
                Thread.sleep(15);

            }catch( Exception ex ) { }
        }
    }

    public void checkForCollision()
    {

    	Rectangle ballR = new Rectangle( blueBlock.x, blueBlock.y, blueBlock.size, blueBlock.size);
        Rectangle redBallR = new Rectangle( redBlock.x, redBlock.y, redBlock.size, redBlock.size);
        for( int i=0; i<blocks.size( ); i++ )
        {
            Rectangle r = (Rectangle )blocks.get(i);
            if ( r.intersects( ballR ) )
            {
                blocks.remove(r);
                getGraphics( ).clearRect(r.x, r.y, r.width, r.height);

                blueBlock.dirX = -1 * blueBlock.dirX;
                blueBlock.dirY = -1 * blueBlock.dirY;
                blueBlock.solid = true;

                ctrBlue++;

                return;
            }

            if ( r.intersects( redBallR ) )
            {
                blocks.remove(r);
                getGraphics( ).clearRect(r.x, r.y, r.width, r.height);

                //redBlock.dirX = -1 * redBlock.dirX;
                //redBlock.dirY = -1 * redBlock.dirY;
                redBlock.solid = true;

                ctrRed++;

                return;
            }
        }
        // check for paddle collision

        if (blueBlock.solid && ballR.intersects(high))
        {
            blueBlock.solid = false;
            blueBlock.firstHit = true;
        	flipSwing();
            redBlock.launchHigh();
            blueBlock.stop(swing, leftUp);

        }

        if (blueBlock.solid && ballR.intersects(med))
        {
            blueBlock.solid = false;
            blueBlock.firstHit = true;
        	flipSwing();
            redBlock.launchMed();
            blueBlock.stop(swing, leftUp);

        }

        if (blueBlock.solid && ballR.intersects(low))
        {
            blueBlock.solid = false;
            blueBlock.firstHit = true;
        	flipSwing();
            redBlock.launchLow();
            blueBlock.stop(swing, leftUp);

        }

        if (redBlock.solid && redBallR.intersects(high))
        {
            redBlock.solid = false;
            redBlock.firstHit = true;
        	flipSwing();
            blueBlock.launchHigh();
            redBlock.stop(swing, leftUp);
        }

        if (redBlock.solid && redBallR.intersects(med))
        {
            redBlock.solid = false;
            redBlock.firstHit = true;
        	flipSwing();
            blueBlock.launchMed();
            redBlock.stop(swing, leftUp);
        }

        if (redBlock.solid && redBallR.intersects(low))
        {
            redBlock.solid = false;
            redBlock.firstHit = true;
        	flipSwing();
            blueBlock.launchLow();
            redBlock.stop(swing, leftUp);
        }

        if (ballR.intersects(floor))
        {
        	lives--;
        	blueBlock.respawn();
        }

        if (redBallR.intersects(floor))
        {
        	lives--;
        	redBlock.respawn();
        }
    }

    public void flipSwing()
    {
		if(leftUp == false)
		{
			high.x = swing.x;
			med.x = swing.x + 20;
			low.x = swing.x + 40;
		}
		else
		{
			high.x = swing.x + swing.width - 20;
			med.x = swing.x + swing.width - 40;
			low.x = swing.x + swing.width - 60;
		}

		leftUp = !leftUp;
    }

    public void actionPerformed(ActionEvent ae)
    {
    	Object src = ae.getSource();
    	if ( src == timer )
    	{
    	   ctrTime++;
    	}
    }

    public void keyReleased( KeyEvent ke ) {}
    public void keyTyped( KeyEvent ke ) {}

    public void keyPressed( KeyEvent ke )
    {
        int code = ke.getKeyCode( );
        //getGraphics( ).clearRect( swing.x, swing.y, swing.width, swing.height);
        if( code == KeyEvent.VK_LEFT )
        {
        	if (swing.x > 10)
        	{
        		int x = swing.x;
                x -= speed;
                swing.x = x;

                if(leftUp == false)
                {
                	high.x = swing.x+swing.width - 20;
        			med.x = swing.x+swing.width - 40;
        			low.x = swing.x+swing.width - 60;
                }
                else
                {
        			high.x = swing.x;
        			med.x = swing.x + 20;
        			low.x = swing.x + 40;
                }
        	}
        }
        else if( code == KeyEvent.VK_RIGHT )
        {
        	if (swing.x + swing.width < width - 10)
        	{
        		int x = swing.x;
                x += speed;
                swing.x = x;

                if(leftUp == false)
                {
                	high.x = swing.x + swing.width - 20;
        			med.x = swing.x + swing.width - 40;
        			low.x = swing.x + swing.width - 60;
                }
                else
                {
        			high.x = swing.x;
        			med.x = swing.x + 20;
        			low.x = swing.x + 40;
                }
        	}
        }

        if( code == KeyEvent.VK_SPACE )
        {
    		flipSwing();
        }
    }
}
