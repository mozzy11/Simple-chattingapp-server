
import javax.swing.JFrame;


public class mozzy {

  
    public static void main(String[] args) {
      server OBJ =  new server ();
      OBJ.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      OBJ.startrunning ();// SINCE ITS THE METHOD THAT refers to all the other methods
    }
    
}
