package Principal;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import Arbol.*;

public class VentanaPrincipal extends JFrame implements ActionListener, KeyListener {
	public JLabel lblTitulo, lblMensaje;
	JPanel panel0, panel1, panel2, panel3;
	JTextField jtfRuta;
	JButton btnAbrir, btnLexico, btnSintactico,
	btnSemantico,btnReiniciar, btnSalir;
	JTextArea txtAreaProg;
	JScrollPane jspTokens;
	String [] hTokens={"Índice","No. Token","Valor"};
	Object [][] cTokens;
	JTable tablaTokens;
	JScrollPane jspTSimbolos;
	String [] hSimbolos={"Número","Nombre","Tipo"};
	Object [][] cSimbolos;
	JTable tablaSimbolos;
	Archivo file;
	String ruta, cadenaProg="";
	AnalizadorLexico scn;
	AnalizadorSintactico par;
	AnalizadorSemantico sem;
	
	public VentanaPrincipal() {
		HazInterfaz();
		HazEscuchas();
	}
	
	public void HazInterfaz() {
		setTitle("Proyecto de Lenguajes y Autómatas 2");
		setBounds(64,113,1290,630); setResizable(false); 
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
		lblTitulo=new JLabel("Compilador de MicroJava (Analizador"
		+ "Léxico/Sintáctico/Semántico):");
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitulo.setFont(new Font("Consolas",Font.BOLD,20));
		add(lblTitulo,BorderLayout.NORTH);
		
		panel0=new JPanel();
		panel0.setLayout(null);
		add(panel0,BorderLayout.CENTER);
		
		panel1=new JPanel();
		panel1.setLayout(null);
		panel1.setBounds(33,0,496,530);
		panel1.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		panel0.add(panel1);
		
		JLabel lblRuta=new JLabel("Ruta del Programa:");
		lblRuta.setFont(new Font("Arial",Font.BOLD,12));
		lblRuta.setBounds(5,15,140,20);
		panel1.add(lblRuta);
		
		jtfRuta=new JTextField();
		jtfRuta.setBounds(120,10,250,30);
		panel1.add(jtfRuta);
		
		btnAbrir=new JButton("Abrir");
		btnAbrir.setFont(new Font("Arial",Font.BOLD,18));
		btnAbrir.setBounds(380,10,100,30);
		btnAbrir.setEnabled(false);
		panel1.add(btnAbrir);
		
		JLabel lblProg=new JLabel("Programa:");
		lblProg.setFont(new Font("Arial",Font.BOLD,12));
		lblProg.setBounds(5,72,112,21);
		panel1.add(lblProg);
		
		txtAreaProg=new JTextArea();
		txtAreaProg.setEditable(false);
		txtAreaProg.setLineWrap(true);
		txtAreaProg.setWrapStyleWord(true);
		JScrollPane jsptxta=new JScrollPane(txtAreaProg);
		jsptxta.setBounds(5,100,483,338);
		panel1.add(jsptxta);
		
		btnLexico=new JButton("Analizador Léxico");
		btnLexico.setFont(new Font("Arial",Font.BOLD,12));
		btnLexico.setBounds(5,450,135,30);
		btnLexico.setEnabled(false);
		panel1.add(btnLexico);
		
		btnSintactico=new JButton("Analizador Sintáctico");
		btnSintactico.setFont(new Font("Arial",Font.BOLD,12));
		btnSintactico.setBounds(145,450,157,30);
		btnSintactico.setEnabled(false);
		panel1.add(btnSintactico);
				
		btnSemantico=new JButton("Analizador Semántico");
		btnSemantico.setFont(new Font("Arial",Font.BOLD,12));
		btnSemantico.setBounds(308,450,175,30);
		btnSemantico.setEnabled(false);
		panel1.add(btnSemantico);
		
		btnReiniciar=new JButton("Reiniciar");
		btnReiniciar.setFont(new Font("Arial",Font.BOLD,12));
		btnReiniciar.setBounds(158,490,85,30);
		btnReiniciar.setEnabled(false);
		panel1.add(btnReiniciar);
		
		btnSalir=new JButton("Salir");
		btnSalir.setFont(new Font("Arial",Font.BOLD,12));
		btnSalir.setBounds(252,490,80,30);
		btnSalir.setEnabled(true);
		panel1.add(btnSalir);
		
		panel2=new JPanel();
		panel2.setLayout(null);
		panel2.setBounds(542,0,355,530);
		panel2.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		panel0.add(panel2);
		
		JLabel lblTokens=new JLabel("Generación de Tokens:");
		lblTokens.setFont(new Font("Arial",Font.BOLD,14));
		lblTokens.setBounds(100,3,178,30);
		panel2.add(lblTokens);
		
		jspTokens=new JScrollPane();
		jspTokens.setBounds(10,30,340,490);
		panel2.add(jspTokens);		
		
		cTokens=new Object[100][3];
		for(int i=0; i<cTokens.length; i++) {
			for(int j=0; j<cTokens[i].length; j++)
				cTokens[i][j]=null;
		}
		
		tablaTokens=new JTable();
		tablaTokens.setModel(new DefaultTableModel(cTokens,hTokens));
		tablaTokens.setEnabled(false);
		jspTokens.setViewportView(tablaTokens);
		
		panel3=new JPanel();
		panel3.setLayout(null);
		panel3.setBounds(908,0,360,530);
		panel3.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		panel0.add(panel3);
		
		JLabel lblSimbolos=new JLabel("Tabla de Símbolos:");
		lblSimbolos.setFont(new Font("Arial",Font.BOLD,14));
		lblSimbolos.setBounds(110,3,150,30);
		panel3.add(lblSimbolos);
		
		jspTSimbolos=new JScrollPane();
		jspTSimbolos.setBounds(10,30,340,490);
		panel3.add(jspTSimbolos);		
		
		cSimbolos=new Object[35][3];
		for(int i=0; i<cSimbolos.length; i++) {
			for(int j=0; j<cSimbolos[i].length; j++)
				cSimbolos[i][j]=null;
		}
		
		tablaSimbolos=new JTable();
		tablaSimbolos.setModel(new DefaultTableModel(cSimbolos,hSimbolos));
		tablaSimbolos.setEnabled(false);
		jspTSimbolos.setViewportView(tablaSimbolos);
		
		lblMensaje=new JLabel("Mensaje: ¡Bienvenido al compilador de MicroJava!");
		lblMensaje.setHorizontalAlignment(SwingConstants.CENTER);
		lblMensaje.setFont(new Font("Consolas",Font.BOLD,20));
		add(lblMensaje,BorderLayout.SOUTH);
		
		setVisible(true);
	}
	
	public void HazEscuchas() {
		jtfRuta.addKeyListener(this);
		btnAbrir.addActionListener(this);
		btnLexico.addActionListener(this);
		btnSintactico.addActionListener(this);
		btnSemantico.addActionListener(this);
		btnReiniciar.addActionListener(this);
		btnSalir.addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent evt) {
		if(evt.getSource()==btnAbrir) {
			ruta=jtfRuta.getText();
			file=new Archivo();
			file.leerTxt(ruta,txtAreaProg);
			cadenaProg=file.fileText;
			btnAbrir.setEnabled(false);
			btnLexico.setEnabled(true);
			btnReiniciar.setEnabled(true);
		}
		else if(evt.getSource()==btnLexico) {
			if(!txtAreaProg.getText().isEmpty()) {
				scn=new AnalizadorLexico();
				scn.separaTokens(cadenaProg,tablaTokens,hTokens);
				lblMensaje.setText("Mensaje: Léxico de programa fuente correcto!!!");	
				btnLexico.setEnabled(false); btnSintactico.setEnabled(true); return;
			}
			JOptionPane.showMessageDialog(null,"Programa Vacío.","Error",JOptionPane.ERROR_MESSAGE);
		}
		else if(evt.getSource()==btnSintactico) {
			par=new AnalizadorSintactico(scn,lblMensaje);
			par.VerificaGramatica();
			btnSintactico.setEnabled(false);
			btnSemantico.setEnabled(true);
		}
		else if(evt.getSource()==btnSemantico) {
			sem=new AnalizadorSemantico(scn,par,par.ArbolSintactico,tablaSimbolos,hSimbolos,lblMensaje);	
			sem.ConstruyeTablaSimbolos();
			sem.VerificaSemantica();
			btnSemantico.setEnabled(false);
		}
		else if(evt.getSource()==btnReiniciar) {
			txtAreaProg.setText("");
			ruta=""; cadenaProg="";
			file=new Archivo();
			scn=new AnalizadorLexico();
			par=new AnalizadorSintactico();
			sem=new AnalizadorSemantico();
			tablaTokens.setModel(new DefaultTableModel(cTokens,hTokens));
			tablaSimbolos.setModel(new DefaultTableModel(cSimbolos,hSimbolos));
			btnAbrir.setEnabled(true);
			btnLexico.setEnabled(false);
			btnSintactico.setEnabled(false);
			btnSemantico.setEnabled(false);
			btnReiniciar.setEnabled(false);
			lblMensaje.setText("Mensaje: ¡Bienvenido al compilador de MicroJava!");
		}
		else if(evt.getSource()==btnSalir) {
			System.exit(1000);
		}	
	}
	
	public void keyPressed(KeyEvent evt) {

	}
	
	public void keyReleased(KeyEvent evt) { 
		if(!jtfRuta.getText().isEmpty())
			btnAbrir.setEnabled(true);
	}
	
	public void keyTyped(KeyEvent e) {	
		if(jtfRuta.getText().isEmpty())
			btnAbrir.setEnabled(false);	
	}
	
	public static void main(String[] args) {
		new VentanaPrincipal();
	}
}