package proyecto1;

import java.io.BufferedReader;
import java.io.FileReader;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

public class Archivo {
	public String fileText;
	public void leerTxt(String ruta, JTextArea txtAreaProg) {
		try {
			BufferedReader BF=new BufferedReader(new FileReader(ruta));
			String tmpText="";
			String BFRead;
			while((BFRead=BF.readLine())!=null) {//Lee una línea de texto hasta que encuentra un carácter de salto de línea (\n) y retorno de carro (\r).
				tmpText=tmpText+BFRead;
				txtAreaProg.append(BFRead+"\n");
			}
			fileText=tmpText;//contructor compatible
		}
		catch(Exception e) {
			JOptionPane.showMessageDialog(null,"RUTA INCORRECTA.","Error",JOptionPane.ERROR_MESSAGE);
		}
		}
}