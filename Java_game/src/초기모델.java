import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.ArrayList;
import javax.swing.*; 

public class Main extends JFrame implements Runnable, KeyListener {
       
        private BufferedImage bi = null;
        private ArrayList msList = null;
        private ArrayList enList = null;
        private boolean left = false, right = false, up = false, down = false, fire = false;
        private boolean start = false, end = false;
        private int w = 300, h = 500, x = 130, y = 450, xw = 20, xh = 20;
      



        public Main() {
         bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
         msList = new ArrayList();
         enList = new ArrayList();
         this.addKeyListener(this);
         this.setSize(w, h);
         this.setTitle("Shooting Game");
         this.setResizable(false);
         this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         this.setVisible(true);  
        }
        
        public void run() {
         try {
          int msCnt = 0;
          int enCnt = 0;
          while(true) {
           Thread.sleep(10);
           
           if(start) {
            if(enCnt > 2000) {
             enCreate();
             enCnt = 0;
            }
            if(msCnt >= 100) {
             fireMs();
             msCnt = 0;
            }
            msCnt += 10;
            enCnt += 10;
            keyControl();
            crashChk();
           }
           draw();
          }
         } catch(Exception e) {
          e.printStackTrace();
         }
        }
        public void fireMs() {
         if(fire) {
          if(msList.size() < 100) {
           Ms m = new Ms(this.x, this.y);
           msList.add(m);
          }
         }
        }
        public void enCreate() {
         for(int i = 0; i < 9; i++) {
          double rx = Math.random() * (w - xw);
          double ry = Math.random() * 50;
          Enemy2 en = new Enemy2((int)rx, (int)ry);
          enList.add(en);
         }
        }
        public void crashChk() {
         Graphics g = this.getGraphics();
         Polygon p = null;
         for(int i = 0; i < msList.size(); i++) {
          Ms m = (Ms)msList.get(i);
          for(int j = 0; j < enList.size(); j++) {
           Enemy2 e = (Enemy2)enList.get(j);
           int[] xpoints = {m.x, (m.x + m.w), (m.x + m.w), m.x};
           int[] ypoints = {m.y, m.y, (m.y + m.h), (m.y + m.h)};
           p = new Polygon(xpoints, ypoints, 4);
           if(p.intersects((double)e.x, (double)e.y, (double)e.w, (double)e.h)) {
            msList.remove(i);
            enList.remove(j);
           }
          }
         }
         for(int i = 0; i < enList.size(); i++) {
          Enemy2 e = (Enemy2)enList.get(i);
          int[] xpoints = {x, (x + xw), (x + xw), x};
          int[] ypoints = {y, y, (y + xh), (y + xh)};
          p = new Polygon(xpoints, ypoints, 4);
          if(p.intersects((double)e.x, (double)e.y, (double)e.w, (double)e.h)) {
           enList.remove(i);
           start = false;
           end = true;
          }
         }
        }
        
        public void draw() {
         Graphics gs = bi.getGraphics();
         gs.setColor(Color.white);
         gs.fillRect(0, 0, w, h);
         gs.setColor(Color.black);
         gs.drawString("Enemy 객체수 : " + enList.size(), 180, 50);
         gs.drawString("Ms 객체수 : " + msList.size(), 180, 70);
         gs.drawString("게임시작 : Enter", 180, 90);
         
         if(end) {
          gs.drawString("G A M E     O V E R", 100, 250);
         }
         
         gs.fillRect(x, y, xw, xh);
          
         for(int i = 0; i < msList.size(); i++) {
          Ms m = (Ms)msList.get(i);
          gs.setColor(Color.blue);
          gs.drawOval(m.x, m.y, m.w, m.h);
          if(m.y < 0) msList.remove(i);
          m.moveMs();
         }
         gs.setColor(Color.black);
         for(int i = 0; i < enList.size(); i++) {
          Enemy2 e = (Enemy2)enList.get(i);
          gs.fillRect(e.x, e.y, e.w, e.h);
          if(e.y > h) enList.remove(i);
          e.moveEn();
         }
         
         Graphics ge = this.getGraphics();
         ge.drawImage(bi, 0, 0, w, h, this);
        }
        
        public void keyControl() {
         if(0 < x) {
          if(left) x -= 3;
         }
         if(w > x + xw) {
          if(right) x += 3;
         }
         if(25 < y) {
          if(up) y -= 3;
         }
         if(h > y + xh) {
          if(down) y += 3;
         }
        }
        
        public void keyPressed(KeyEvent ke) {
         switch(ke.getKeyCode()) {
         case KeyEvent.VK_LEFT:
          left = true;
          break;
         case KeyEvent.VK_RIGHT:
          right = true;
          break;
         case KeyEvent.VK_UP:
          up = true;
          break;
         case KeyEvent.VK_DOWN:
          down = true;
          break;
         case KeyEvent.VK_A:
          fire = true;
          break;
         case KeyEvent.VK_ENTER:
          start = true;
          end = false;
          break;
         }
        }
        
        public void keyReleased(KeyEvent ke) {
         switch(ke.getKeyCode()) {
         case KeyEvent.VK_LEFT:
          left = false;
          break;
         case KeyEvent.VK_RIGHT:
          right = false;
          break;
         case KeyEvent.VK_UP:
          up = false;
          break;
         case KeyEvent.VK_DOWN:
          down = false;
          break;
         case KeyEvent.VK_A:
          fire = false;
          break;
         }
        }
        
        public void keyTyped(KeyEvent ke) {}
        
        public static void main(String[] args) {
         Thread t = new Thread(new Main());
         t.start();
        }
       }
       class Ms {
        int x;
        int y;
        int w = 5;
        int h = 5;
        public Ms(int x, int y) {
         this.x = x;
         this.y = y;
        }
        public void moveMs() {
         y--;
        }
       }
       class Enemy2 {
        int x;
        int y;
        int w = 10;
        int h = 10;
        public Enemy2(int x, int y) {
         this.x = x;
         this.y = y;
        }
        public void moveEn() {
         y++;
        } 
       }
