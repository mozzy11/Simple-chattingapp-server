import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class server extends JFrame{
    private JTextField UserText;
      private JTextArea ChatWindow;//dispalys the chat
      private ObjectOutputStream OutPut;
      private ObjectInputStream Input;
      private ServerSocket server;
      private Socket connection;
      public server (){
      
          super ("MOZZY MESSANGER");
          UserText = new JTextField();
           UserText.setEditable (false);//it will be uneditable until we are aconnected
           UserText.addActionListener(
           new ActionListener(){
           public void actionPerformed(ActionEvent event){
             sendMessage(event.getActionCommand());// send the string typed in the field
              UserText.setText(""); //empties the textarea after sending the message
           }
        }
           );
           add(UserText, BorderLayout.NORTH);
           ChatWindow = new JTextArea ();
           add(new JScrollPane(ChatWindow), BorderLayout.CENTER);
           setSize(300,400);
           setVisible(true);
      }
      //SETTING UP THE SERVER
      public void startrunning (){
        try {
        server= new ServerSocket(6789, 100);// (portnumber ie any number, number of people waiting in queue )
        try{// CONNECT AND HAVE CONVERSATION
            waitForConnection(); //our mrthod to connect
            setUpStreams();//method to define in and out streams
            whileChatting();//method to help send and recieving
         
        }catch (EOFException eofExcepion){
        showMessage("\n server ended the connection");
        } finally {
        closeCrap();// this closes the streams
        }
        } catch(IOException ioException){
        ioException.printStackTrace(); 
        }
      
      }
      // WAIT FOR A CONNECTION. then display connection info
       private void waitForConnection() throws IOException{
        showMessage("waiting for someone to connect.....\n");
       connection = server.accept();//accepts for one to plug into our connection socket
       showMessage("now connected to\n" + connection.getInetAddress().getHostName());
       }
        // SETING UP STREAMS
       private void setUpStreams()throws IOException{
       OutPut = new ObjectOutputStream(connection.getOutputStream());//creating a pathway to another comp
       OutPut.flush(); // cleans up the remainuing data in the strems
       Input = new ObjectInputStream (connection.getInputStream());
       showMessage("in streams are now set up");
       }
       //SETTING UP THE CHAT CONVERSATION
      private void  whileChatting()throws IOException{
      String message = "you are niw connected";
      sendMessage (message);
      ableToType (true);
       do{ 
           try{
       message = (String)Input.readObject();
       showMessage(  message);
       } catch(ClassNotFoundException  classNotFoundException)
       {
           showMessage ("\n wat ahell is that");
       }
       
       }while(!message.equals("CLIENT - END"));//the chat continues as long as the user doesnt type "end"
      
              
      }
       // CLOSING STREAMS AND SOCKETS AFTER YOU DONE CHATTING
      private void closeCrap(){
       showMessage ("\n closing connection");
      ableToType (false);
      try {
          OutPut.close();// closing the output stream
           Input.close();//closing the input stream
           connection.close();// closes the socket
      } catch (IOException ioException){
      ioException.printStackTrace();
       }
      }
       // SENDING A MESSAGE TO CLIENT
      private void sendMessage(String message){
       try{
        OutPut.writeObject(" SERVER -  " + message);// the "writeobject" sends the message thru the streams
        OutPut.flush();
        showMessage ("\n SERVER - " + message +"\n");// displays the messgae on the screen
       } catch(IOException ioException){
       ChatWindow.append("\n man i cant send that messo");// append means stick it to the chat area
        }
      
      }
        //DISPLAYING THE CHAT IN THE CHAT WINDOW
      private void  showMessage (final String text){
       SwingUtilities.invokeLater(    // this updates the GUI(PORTION) without making anew GUI everytime
       new Runnable (){// introducing athread
       public void run(){
           ChatWindow.append( text );// this dds stuff to the chatwindow and updates the chat window with a new message
            }
           }
         );
       
      }
      // Alowing the USER TO TYPE
      private void ableToType (final boolean tof ){
      
      SwingUtilities.invokeLater(    // this updates the GUI(PORTION) without making anew GUI everytime
       new Runnable (){// introducing athread
       public void run(){
         UserText.setEditable(tof);
         
            }
           }
         );
      }
}   

