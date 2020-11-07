import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JFrame;

public class SocketChat extends Frame implements ActionListener, Runnable, ItemListener, KeyListener{
     
	DataInputStream in;
	DataOutputStream out;
	boolean CSclose;
    TextArea  tf2; 
    TextField tf3, tf;
	Label msg;
	Button b1, b2;
	int choose = 0; // 0: none, 1:server, 2:client
	JFrame f = new JFrame ("User name");
	JFrame f2 = new JFrame("Chatting Room");
	String name = "Bruce";
	Choice chFont, chStyle, chSize;
	
    int anchor[] = { GridBagConstraints.WEST,
	         GridBagConstraints.NORTH,
	         GridBagConstraints.SOUTH,
	         GridBagConstraints.CENTER,
	         GridBagConstraints.EAST};

     int fill[] = {  GridBagConstraints.BOTH,
	                  GridBagConstraints.HORIZONTAL,
	                  GridBagConstraints.NONE   
	                                              };  
	  
     int att2[][] = {{0, 0, 1, 1, 3, 1, fill[0], anchor[1]},     // tf2
     		         {0, 1, 1, 1, 0, 0, fill[1], anchor[2]},     // tf3
     		         {3, 2, 1, 1, 0, 0, fill[2], anchor[4]},     // b2
                     {0, 2, 1, 1, 0, 0, fill[2], anchor[4]},     // chfont      
                     {1, 2, 1, 1, 0, 0, fill[2], anchor[4]},     // chstyle     
                     {2, 2, 1, 1, 0, 0, fill[2], anchor[4]}      // chsize };
                                                             }; 
    int att[][] = { 
    	           	{0, 3, 1, 1, 1, 0, fill[2], anchor[3]},      //  msg
                    {0, 4, 1, 1, 1, 0, fill[2], anchor[3]},     // tf
                    {1, 4, 1, 1, 1, 0, fill[2], anchor[3]}    // enter                                    
                                                                  };
    String fontname[] = {"Aria", "Courier New", "Times New Roman", "Arial", "�s������"};
    int fontStyle[] = {Font.PLAIN, Font.BOLD, Font.ITALIC, Font.ITALIC|Font.BOLD};    
    String style[] = {"�з�", "����", "����", "�ʱ���"};
    
	SocketChat(InputStream in, OutputStream out, boolean close ){
	//  super("#1:Server  #2:Client and I am #" + ID);	
	  this.in = new DataInputStream(in);
	  this.out = new DataOutputStream(out);
      this.CSclose = false;
	    
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLayout(new GridBagLayout());        
        tf = new TextField("",25);
        tf.setText(name);
        msg = new Label("Please enter your name:             ");
        b1  = new Button("Enter");
    	b1.setBackground(Color.CYAN);
        b1.setActionCommand("Enter");
        b1.addActionListener(this);
        
        add(f, msg, att[0]);
        add(f, tf, att[1]); 
        add(f, b1, att[2]);
        
        f.pack();
        f.setLocation(200, 300);	      
        f.setSize(400, 200);
		f.setResizable(false);
		f.setVisible(true);
        
		f2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f2.setLayout(new GridBagLayout());
		
		tf2 = new TextArea("", 2, 10, TextArea.SCROLLBARS_VERTICAL_ONLY);
		tf2.setForeground(Color.BLUE);
		tf2.setEditable(false);
		tf3 = new TextField("");
		b2 = new Button("Enter");
		b2.setBackground(Color.magenta);
        b2.setActionCommand("Output");
        b2.addActionListener(this);
        f.addKeyListener(this);
        chFont = new Choice();
        for(int i=0 ; i < fontname.length; i++)
        chFont.add(fontname[i]);
        
        chStyle = new Choice();
        for(int i=0 ; i < style.length; i++)
        chStyle.add(style[i]);
        
        chSize = new Choice();
        for(int i=8 ; i <= 72; i+=2)
        chSize.add(Integer.toString(i));
        
        chFont.addItemListener(this);
        chStyle.addItemListener(this);
        chSize.addItemListener(this);
        
		add(f2, tf2, att2[0]);
		add(f2, tf3, att2[1]);	
		add(f2, chFont, att2[3]);
		add(f2, chStyle, att2[4]);
		add(f2, chSize, att2[5]);
		add(f2, b2, att2[2]);
		
		f2.pack();
		f2.setLocation(200, 300);
		f2.setSize(500, 500);
		f2.setResizable(false);
		f2.setVisible(false);
	  
	  new Thread(this).start();	  

    }

	private static void add(Container con, Component com,  int att[]){
		   GridBagConstraints cons = new GridBagConstraints();	
		
		   cons.gridx = att[0];
		   cons.gridy = att[1];
		   cons.gridwidth = att[2];
		   cons.gridheight = att[3];
		   cons.weightx = att[4];
		   cons.weighty = att[5];
		   cons.fill = att[6];
		   cons.anchor = att[7];
		   con.add(com, cons);
		}
	
	public void Output(){
		 try {
         	String input = "" ;
	            input = tf3.getText();
	            out.writeUTF(name + ": " +input);
	            tf2.append(name + ": " +input+"\n");
	            tf3.setText("");	
	         } catch (IOException e1) {
                  e1.printStackTrace();
	         }
	}
	
	public void keyPressed(KeyEvent e){}
		
	public void keyReleased(KeyEvent e){}
	
	public void keyTyped(KeyEvent e) {
		String str = KeyEvent.getKeyText(e.getModifiers());
		if(str.equals("Enter")){
			 Output();
		}	
	}

    public void actionPerformed(ActionEvent e){
	
    	String cmd = e.getActionCommand();
         	if( cmd.equals("Output")) {
         	      Output();
         	}
         	else if(cmd.equals("Enter") ){
         		f2.setVisible(true);
         		f.dispose();
         		name = tf.getText();
         	}
    }

    public void run(){
    	try{
    		while(true){
    			String content = in.readUTF();
    			tf2.append( content+"\n");
    		}
    	}catch (Exception e){
    		tf2.append("Connection Cancel...");		
    	}
    }
	
    public void itemStateChanged(ItemEvent e)
    {
    	String name = chFont.getSelectedItem();
    	int Style = fontStyle[chStyle.getSelectedIndex()];
    	int size = Integer.parseInt(chSize.getSelectedItem());
    	
    	Font fontshape =new Font(name, Style, size);
    	tf2.setFont(fontshape);
    	pack();
    }
    
    public void windowClosing(WindowEvent e)
	{
		try {
			  in.close();
			  out.close(); 
			  CSclose = true;
		    } catch (IOException e1) {
				e1.printStackTrace();
		}  
		 
	}
             
        //private static final String Seraddr = "127.0.0.1";  //140.115.207.122
    	private static final int port = 57968; 
    	
        public static void main(String[] args){
    	Socket cs = null;
    	//String Seraddr = "127.0.0.1"; 
        boolean close = false;
    	
    	System.out.println("Choose user type: (1.Server, 2. Client)");
		Scanner scanner = new Scanner(System.in);
	    int num = scanner.nextInt();
	    
    	try{	
    		    		
    		if(num==1){	
    	      ServerSocket ss = new ServerSocket(port);
    		  System.out.println("port: " + ss.getLocalPort());
    		  System.out.println("IP address: " + InetAddress.getLocalHost().getHostAddress());
    		  cs = ss.accept();	
    		  
    		}else if (num == 2){
    		  
    		   System.out.println("Please enter server's address:");
    		   Scanner scanner2 = new Scanner(System.in);
    		   String Seraddr = scanner2.next();	 
    		   
    		   InetAddress IPAddress = InetAddress.getByName(Seraddr); 
    		   cs = new Socket( IPAddress, port);	
    		   System.out.println("Connect!" );
    		   
    		}else{
    			System.err.println("Input error!!");   		
    			System.exit(1);
    		}
    		new SocketChat(cs.getInputStream(), cs.getOutputStream(), close );
    		
    		if(close == true){
    			try {  				  
    				  cs.close();
    			    } catch (IOException e1) {
    					e1.printStackTrace();
    			}  
    		}
    		
    	}catch (Exception e){
    		e.printStackTrace();
    	}
     	
    }


}

