package proyecto1;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Scanner {
	ArrayList<Token> lsTokens;
	String [] alfabeto;
	public Scanner() {
		lsTokens=new ArrayList<Token>();
		alfabeto=new String [35];
		alfabeto[0]="class";
		alfabeto[1]="{";
		alfabeto[2]="public";
		alfabeto[3]="static";
		alfabeto[4]="void"; 
		alfabeto[5]="main";  
		alfabeto[6]="(";
		alfabeto[7]="String";
		alfabeto[8]="[";
		alfabeto[9]="]";
		alfabeto[10]=")";
		alfabeto[11]="}";
		alfabeto[12]="extends";
		alfabeto[13]="identifier";;
		alfabeto[14]=";";
		alfabeto[15]=",";
		alfabeto[16]="return";
		alfabeto[17]="int";
		alfabeto[18]="boolean";
		alfabeto[19]="if";
		alfabeto[20]="else";
		alfabeto[21]="while";
		alfabeto[22]="System.out.println";
		alfabeto[23]="=";
		alfabeto[24]="&&";
		alfabeto[25]="<";
		alfabeto[26]="+";
		alfabeto[27]="*";
		alfabeto[28]=".";
		alfabeto[29]="true";
		alfabeto[30]="false";
		alfabeto[31]="this";
		alfabeto[32]="new";
		alfabeto[33]="!";
		alfabeto[34]="boolean";
	}
	
	public void separaTokens(String cadenaTxt, JTable tablaTokens, String [] header) {
		String charact="", Tok="";
		String [][] tableContent;
		int NoTok=0;
		for(int j=0; j<cadenaTxt.length(); j++) {
			charact=String.valueOf(cadenaTxt.charAt(j));
			if((charact.equals(" ") || charact.equals("	")) || (j==(cadenaTxt.length()-1))) {
				if(j==(cadenaTxt.length()-1)) Tok+=charact;
				if(Tok!="") {
					if(VerificaToken(Tok)) {
						if(!EstaEnAlfabeto(Tok)) {
							if(IsIdentifier(Tok))
								NoTok=13;
							else
								NoTok=34;
						}
						else
							NoTok=GetTokenPosition(Tok);
						lsTokens.add(new Token(NoTok,Tok));
					}
					else {
						//Los cuadros de dialogo (JOptionPane) son “herramientas” muy útiles al momento de ingresar datos y mostrar información; digo útil ya que con estas no necesitamos crear objetos de tipo BufferedReader y “tirar” más código para poder usar correctamente los datos que se ingresan como lo expliqué brevemente cuando explique sobre como ingresar datos en Java por consola.
						JOptionPane.showMessageDialog(null,"LA ESTÁS REGANDO EN"+ " '"+Tok+"'.","Error",JOptionPane.ERROR_MESSAGE);
						System.exit(1000);
					}
					Tok="";
				}
			}
			else
				Tok+=charact;
		}
		tableContent=new String [lsTokens.size()][3];
		for(int i=0; i<lsTokens.size(); i++) {
			tableContent[i][0]=String.valueOf((i+1));
			tableContent[i][1]=String.valueOf(lsTokens.get(i).NoToken);
			tableContent[i][2]=lsTokens.get(i).Nombre;
		}
		tablaTokens.setModel(new DefaultTableModel(tableContent,header));
	}
	
	public boolean VerificaToken(String tok) {
		if(!IsIdentifier(tok) && !IsInteger(tok) && !EstaEnAlfabeto(tok))
			return false;
		return true;
	}
	
	public boolean IsIdentifier(String token) {
		if(Character.isLetter(token.charAt(0))) {
			for(int j=0; j<token.length(); j++) {
				if(!Character.isLetter(token.charAt(j)) && !Character.isDigit(token.charAt(j)))
					return false;
			}
			return true;
		}
		return false;
	}
	
	public boolean IsInteger(String token) {
		for(int j=0; j<token.length(); j++){
			if(!Character.isDigit(token.charAt(j)))
				return false;
		}
		return true;
	}
	
	public boolean EstaEnAlfabeto(String token) {
		for(int i=0; i<alfabeto.length; i++){
			if(alfabeto[i].equals(token))
				return true;
		}
		return false;
	}
	
	public int GetTokenPosition(String token) {
		for(int i=0; i<alfabeto.length; i++) {
			if(alfabeto[i].equals(token))
				return i;
		}
		return 0;
	}
}


