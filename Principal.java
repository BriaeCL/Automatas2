package proyecto1;

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

public class Principal extends JFrame {//como abrir una ventana en java
	public JLabel lblTitulo, lblMensaje;
	JPanel panel0, panel1, panel2;
	JTextField jtfRuta;
	JButton btnAbrir, btnLexico, btnReiniciar, btnSalir;
	JTextArea txtAreaProg;
	JScrollPane jspTokens;
	String [] hTokens={"Índice","No. Token","Valor"};
	Object [][] cTokens;
	JTable tablaTokens;
	Archivo file;
	String ruta, cadenaProg="";
	Scanner scn;
	
	public Principal() {
		setTitle("Proyecto de Lenguajes y Autómatas 1 Brianda");
		setBounds(314,96,940,700); setResizable(false); 
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		lblTitulo=new JLabel("Analizador Léxico (Scanner) de MiniJava");
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitulo.setFont(new Font("Consolas",Font.BOLD,20));
		add(lblTitulo,BorderLayout.NORTH);
		panel0=new JPanel();
		panel0.setLayout(null);
		add(panel0,BorderLayout.CENTER);
		panel1=new JPanel();
		panel1.setLayout(null);
		panel1.setBounds(33,0,496,575);
		panel1.setBorder(BorderFactory.createLineBorder(Color.RED));
		panel0.add(panel1);
		JLabel lblRuta=new JLabel("Ruta del Programa:");
		lblRuta.setFont(new Font("Arial",Font.BOLD,12));
		lblRuta.setBounds(5,15,140,20);
		panel1.add(lblRuta);
		jtfRuta=new JTextField();
		jtfRuta.setBounds(120,10,250,30);
		panel1.add(jtfRuta);
		jtfRuta.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent evt) {
				//No hay acciones aquí...
			}
			
			public void keyReleased(KeyEvent evt) { 
				if(!jtfRuta.getText().isEmpty())
					btnAbrir.setEnabled(true);
			}
	
			public void keyTyped(KeyEvent e) {	
				if(jtfRuta.getText().isEmpty())
					btnAbrir.setEnabled(false);	
			}
		});
		btnAbrir=new JButton("Abrir");
		btnAbrir.setFont(new Font("Arial",Font.BOLD,12));
		btnAbrir.setBounds(380,10,100,30);
		btnAbrir.setEnabled(false);
		panel1.add(btnAbrir);
		btnAbrir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				ruta=jtfRuta.getText();
				file=new Archivo();
				file.leerTxt(ruta,txtAreaProg);
				cadenaProg=file.fileText;
				btnAbrir.setEnabled(false);
				btnLexico.setEnabled(true);
				btnReiniciar.setEnabled(true);
			}
		});
		JLabel lblProg=new JLabel("Programa Fuente:");
		lblProg.setFont(new Font("Arial",Font.BOLD,12));
		lblProg.setBounds(5,72,130,21);
		panel1.add(lblProg);
		txtAreaProg=new JTextArea();
		txtAreaProg.setEditable(false);
		txtAreaProg.setLineWrap(true);
		txtAreaProg.setWrapStyleWord(true);
		JScrollPane jsptxta=new JScrollPane(txtAreaProg);
		jsptxta.setBounds(5,100,486,422);
		panel1.add(jsptxta);
		btnLexico=new JButton("Analizador Léxico");
		btnLexico.setFont(new Font("Arial",Font.BOLD,12));
		btnLexico.setBounds(75,530,135,30);
		btnLexico.setEnabled(false);
		panel1.add(btnLexico);
		btnLexico.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if(!txtAreaProg.getText().isEmpty()) {
					scn=new Scanner();
					scn.separaTokens(cadenaProg,tablaTokens,hTokens);
					lblMensaje.setText("Mensaje: Léxico de programa fuente correcto!!!");	
					btnLexico.setEnabled(false); return;
				}
				JOptionPane.showMessageDialog(null,"Programa Vacío.","Error",JOptionPane.ERROR_MESSAGE);
			}
		});		
		btnReiniciar=new JButton("Reiniciar");
		btnReiniciar.setFont(new Font("Arial",Font.BOLD,12));
		btnReiniciar.setBounds(224,530,85,30);
		btnReiniciar.setEnabled(false);
		panel1.add(btnReiniciar);
		btnReiniciar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				txtAreaProg.setText("");
				ruta=""; cadenaProg="";
				file=new Archivo();
				scn=new Scanner();
				tablaTokens.setModel(new DefaultTableModel(cTokens,hTokens));
				btnAbrir.setEnabled(true);
				btnLexico.setEnabled(false);
				btnReiniciar.setEnabled(false);
				lblMensaje.setText("Mensaje: Interfaz Principal");
			}
		});
		btnSalir=new JButton("Salir");
		btnSalir.setFont(new Font("Arial",Font.BOLD,12));
		btnSalir.setBounds(329,530,80,30);
		btnSalir.setEnabled(true);
		panel1.add(btnSalir);
		btnSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				System.exit(1000);
			}
		});
		panel2=new JPanel();
		panel2.setLayout(null);
		panel2.setBounds(542,0,355,575);
		panel2.setBorder(BorderFactory.createLineBorder(Color.RED));
		panel0.add(panel2);
		JLabel lblTokens=new JLabel("Generación de Tokens");
		lblTokens.setFont(new Font("Arial",Font.BOLD,14));
		lblTokens.setBounds(100,3,178,30);
		panel2.add(lblTokens);
		jspTokens=new JScrollPane();
		jspTokens.setBounds(10,30,340,540);
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
		lblMensaje=new JLabel("Mensaje: Interfaz Principal");
		lblMensaje.setHorizontalAlignment(SwingConstants.CENTER);
		lblMensaje.setFont(new Font("Consolas",Font.BOLD,20));
		add(lblMensaje,BorderLayout.SOUTH);
	}
	
	public static void main(String[] args) {
		Principal marco=new  Principal();
		marco.setVisible(true);
	}
}


