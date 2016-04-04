import java.io.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.Color;
import java.sql.Timestamp;
import java.util.Date;
import java.io.File;

class TrieNode
{
    char letra;
    TrieNode[] hijos;
    boolean palabraFin;
   
    TrieNode(char letra, boolean palabraFin)
    {
        this.palabraFin = palabraFin;
        hijos = new TrieNode[61];
        this.palabraFin = palabraFin;
    }
}
class TreePrefix{
	static TrieNode crearTree(){
		return (new TrieNode('\0',false));
	}
	static void insertWord(TrieNode root, String word)
    {
        int offset;
        int l = word.length();
        char[] letters = word.toCharArray();
        TrieNode curNode = root;
        for (int i = 0; i < l; i++){
        	if (letters[i] != '/') {
        		if (letters[i] >=65 && letters[i] <= 91) {
				offset = letters[i] - 64;
				}else if (letters[i]>=97 && letters[i] <=123) {
					offset = letters[i]-70;
				}else{
					switch (letters[i]) {
						case 193 :
							offset = 53;
						break;
						case 225 :
							offset = 54;
						break;
						case 233 :
							offset = 55;
						break;
						case 237 :
							offset = 56;
						break;
						case 241 :
							offset = 57;
						break;
						case 243 :
							offset = 58;
						break;
						case 250 :
							offset = 59;
						break;
						default:
							offset = 60;
							break;
					}
				}
	            if (curNode.hijos[offset] == null){
	                curNode.hijos[offset] = new TrieNode(letters[i], i == l-1 ? true : false);
	            }
	            curNode = curNode.hijos[offset];
	        }else{
	        	i = l;
	        }
        }
    }

    static boolean buscar(TrieNode root, String word){
    	int offset;
        int l = word.length();
        char[] letters = word.toCharArray();
        TrieNode curNode = root;
        int i;
        for (i = 0; i < l; i++){
        	if (letters[i] != '/') {
        		if (letters[i] >=65 && letters[i] <= 91) {
				offset = letters[i] - 64;
				}else if (letters[i]>=97 && letters[i] <=123) {
					offset = letters[i]-70;
				}else{
					switch (letters[i]) {
						case 193 :
							offset = 53;
						break;
						case 225 :
							offset = 54;
						break;
						case 233 :
							offset = 55;
						break;
						case 237 :
							offset = 56;
						break;
						case 241 :
							offset = 57;
						break;
						case 243 :
							offset = 58;
						break;
						case 250 :
							offset = 59;
						break;
						default:
							offset = 60;
							break;
					}
				}
	            if (curNode == null) {
	            	return false;
	            }
	            curNode = curNode.hijos[offset];
	        }else{
	        	i = l;
	        }
        }

        if (i == l && curNode == null)
            return false;
       
        if (curNode != null && !curNode.palabraFin)
            return false;
       
        return true;
    }
}

class getOS{
	private static String OS = System.getProperty("os.name").toLowerCase();

	public static boolean isWindows(){ return (OS.indexOf("win") >= 0); }
	public static boolean isMac(){ return (OS.indexOf("mac") >= 0); }
	public static boolean isPOSIX(){ return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0) ;}
}

public class gui extends KeyAdapter implements ActionListener{

	private static JFrame frame = new JFrame();	
	private static Scanner as = new Scanner(System.in);
	private static TrieNode tree = TreePrefix.crearTree();
    private static File archivo = null;
    private static FileReader fr = null;
    private static BufferedReader br = null;
    private static String linea;
    private static JTextArea textArea = new JTextArea(20, 40);
    private static JMenuBar menu = new JMenuBar();
    private static JMenu file = new JMenu("File");
    private static JMenuItem open = new JMenuItem("Open File");
    private static JMenuItem save = new JMenuItem("Save File");
    private static JMenuItem neo = new JMenuItem("New File");
    private static JMenuItem exit = new JMenuItem("Exit");
    private static String title = new String("Untitled");
    private static getOS os = new getOS();

    public void debug(String kind,Object args){
    	Date date = new Date();
    	System.out.println(new Timestamp(date.getTime()) + " [" + kind + "] " + args);
    }


    public static String convertFromUTF8(String s) {
      String out = null;
      try {
          out = new String(s.getBytes("ISO-8859-1"), "UTF-8");
      } catch (java.io.UnsupportedEncodingException e) {
          return null;
      }
      return out;
    }

	public void init(){
		debug("Debug", "OS: " + System.getProperty("os.name"));
		debug("Debug","loading GUI");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle(title);
		frame.setBounds(0, 0, 300, 150);
		textArea.addKeyListener(this);
		JScrollPane scroll = new JScrollPane(textArea);
		menu.add(file);
		open.addActionListener(this);
		save.addActionListener(this);
		neo.addActionListener(this);
		exit.addActionListener(this);
		file.add(open);
		file.add(save);
		file.add(neo);
		file.add(exit);
		frame.setJMenuBar(menu);
		frame.add(scroll);
		frame.pack();
		frame.setVisible(true);
		debug("Debug","GUI Initialized");
	}

	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		gui g = new gui();
		g.init();
        try {
        	g.debug("Debug","Reading Dic");
            archivo = new File ("words.txt");
            fr = new FileReader (archivo);
            br = new BufferedReader(fr);
            long startTime = System.currentTimeMillis();
            while((linea=br.readLine())!=null){
               if (os.isWindows()) {
               		TreePrefix.insertWord(tree, g.convertFromUTF8(linea));
               }else if (os.isMac() || os.isPOSIX()) {
               		TreePrefix.insertWord(tree, linea);
               }
            }
            long endTime = System.currentTimeMillis();
            g.debug("Debug",(endTime - startTime) / 1000.0);
            g.debug("Debug","Dic loaded");
        }catch(Exception e){
            e.printStackTrace();
            g.debug("Error","Exit with code 1");
            System.exit(1);
        }finally{
            try{                   
                if( null != fr ){
                    fr.close();    
                }                 
            }catch (Exception e2){
                e2.printStackTrace();
                g.debug("Error","Exit with code 1");
                System.exit(1);
            }
        }
        long finish = System.currentTimeMillis();
        g.debug("Debug","Everything Ready in " + ((finish - start) / 1000.0) + " Seconds");
	}

	public void keyPressed(KeyEvent evt) {
	    if(evt.getKeyCode() == KeyEvent.VK_ENTER){
	         String[] arr = textArea.getText().split("\\n");
	         debug("Debug",Arrays.toString(arr));
	         ArrayList<String> list = new ArrayList<String>();
	         for (int i = 0; i < arr.length; i++) {
	         	String[] aux = arr[i].split(" ");
	         	for (String g : aux) {
	         		if (!TreePrefix.buscar(tree, g)) {
		         		list.add(g);
		         		debug("Error","No esta: " + g);
		         	}else{
		         		debug("Info","Esta bien: " + g);
		         	}
	         	}
	         }
	         if (list.size() >= 1) {
	         	debug("Debug",Arrays.toString(list.toArray()));
		         String message = "Estas Palabras estan mal escritas: " + Arrays.toString(list.toArray());
		         JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
	         }
	    }
	 }

	 public void open(){
	 	JFileChooser fileChooser = new JFileChooser();
	 	int returnVal = fileChooser.showOpenDialog(null);
	    if (returnVal == JFileChooser.APPROVE_OPTION) {
	        File file = fileChooser.getSelectedFile();
	        try {
	          textArea.read( new FileReader( file.getAbsolutePath() ), null );
	          frame.setTitle(file.getAbsolutePath());
	        } catch (IOException ex) {
	          debug("Error","problem accessing file"+file.getAbsolutePath());
	        }
	    } else {
	        debug("Info","Open Cancelled");
	    }
	 }

	 public void save(){
	 	JFileChooser fileChooser = new JFileChooser();
	    int returnVal = fileChooser.showSaveDialog(null);
	     
	    if (returnVal == JFileChooser.APPROVE_OPTION) {
	        File file = fileChooser.getSelectedFile();
	         
	        try {
	            PrintWriter writer = new PrintWriter(file);
	            writer.write(textArea.getText());
	            writer.close();
	        }
	        catch (FileNotFoundException e) {
	        	debug("Error","File not found");
	        }
	        debug("Info","File saved");
	    }else{
	    	debug("Info","Save Cancelled");
	    }
	 }

	public void actionPerformed(ActionEvent e){
		debug("Debug","Command: " + e.getActionCommand());
		if ("Open File".equals(e.getActionCommand())) {
			debug("Info","open");
			open();
		}else if ("Save File".equals(e.getActionCommand())) {
			debug("Info","save");
			save();
		}else if ("New File".equals(e.getActionCommand())) {
			textArea.setText("");
			frame.setTitle(title);
		}else if ("Exit".equals(e.getActionCommand())) {
			debug("Info","Exit with code 0");
			System.exit(0);
		}
	}
}