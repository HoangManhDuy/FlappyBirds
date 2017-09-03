/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flappybirds;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import pkg2dgamesframework.AFrameOnImage;
import pkg2dgamesframework.Animation;
import pkg2dgamesframework.GameScreen;

/**
 *
 * @author Hoangduy
 */
public class FlappyBirds extends GameScreen {
    
    private BufferedImage birds;
    private Animation bird_anim;
    
    private Bird bird;
    private Ground ground;
    private ChimneyMuti chimneymuti;
    
    private int GAMEBEGIN_SCREEN = 0;
    private int GAMEPLAY_SCREEN = 1;
    private int GAMEOVER_SCREEN = 2;
    
    private int CurrentScreen;
    
    public static float g = 0.1f;
    
    
   

    public FlappyBirds(){
        super(800,600);
        try{
            birds = ImageIO.read(new File("assets/bird_sprite.png"));
        }catch(IOException ex){}
        
        bird_anim = new Animation(70);     // minisecond
        
        AFrameOnImage f;
        f = new AFrameOnImage(0,0,60,60);
        bird_anim.AddFrame(f);
        f = new AFrameOnImage(60,0,60,60);
        bird_anim.AddFrame(f);
        f = new AFrameOnImage(120,0,60,60);
        bird_anim.AddFrame(f);
        f = new AFrameOnImage(60,0,60,60);
        bird_anim.AddFrame(f);
        
        bird = new Bird(350, 300, 50, 50);
        ground = new Ground();
        
        chimneymuti = new ChimneyMuti();
                
        BeginGame();
    }
    
    public static void main(String[] args) {
        
        new FlappyBirds();
        
    }
    
    public void resetGame(){
        bird.setPos(350, 250);
        bird.setVt(0);
    }
    @Override
    public void GAME_UPDATE(long deltaTime) {
        if(CurrentScreen == GAMEBEGIN_SCREEN){
            resetGame();
        }
        else if(CurrentScreen == GAMEPLAY_SCREEN){
            bird_anim.Update_Me(deltaTime);
            bird.update(deltaTime);
            ground.update();
            
            chimneymuti.update();
            
            for(int i = 0; i< ChimneyMuti.SIZE; i++){
                if(bird.getRect().intersects(chimneymuti.getChimney(i).getRect())){
                    bird.setLive(false);
                }
            }
            
            if(bird.getPosY() + bird.getH() > ground.setYGround()){
                CurrentScreen = GAMEOVER_SCREEN;
                resetGame();
            }
        }
        else if(CurrentScreen == GAMEOVER_SCREEN){
        
        }
           
    }

    @Override
    public void GAME_PAINT(Graphics2D g2) {
        
        chimneymuti.paint(g2);
        ground.Paint(g2);
        
        if(bird.getIsFlying())
            bird_anim.PaintAnims((int) bird.getPosX(), (int) bird.getPosY(), birds, g2, 0, -1);
        else
            bird_anim.PaintAnims((int) bird.getPosX(), (int) bird.getPosY(), birds, g2, 0, 1);
       
        if(CurrentScreen == GAMEBEGIN_SCREEN){
            g2.setColor(Color.red);
            g2.drawString("Press space to play game", 200, 300);
        }
        else if(CurrentScreen == GAMEOVER_SCREEN){
            g2.setColor(Color.red);
            g2.drawString("Press space back play game", 200, 300);
        }
    }

    @Override
    public void KEY_ACTION(KeyEvent e, int Event) {
        if(Event == KEY_PRESSED){
            
            if(CurrentScreen == GAMEBEGIN_SCREEN){
                CurrentScreen = GAMEPLAY_SCREEN;   
            }
            else if(CurrentScreen == GAMEPLAY_SCREEN){
                bird.fly();
            }
            else if(CurrentScreen == GAMEOVER_SCREEN){
                CurrentScreen = GAMEBEGIN_SCREEN;
            }
        }
        
    }
    
    
    
}
