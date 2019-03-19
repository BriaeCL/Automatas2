package Principal;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
public class AnalizadorLexico {
	ArrayList<Token> lsTokens;
	String [] alfabeto;
	int indiceToken;
	int numeroToken;
	public AnalizadorLexico() {
		lsTokens=new ArrayList<Token>();
		alfabeto=new String [19];
		alfabeto[0]="class"; alfabeto[1]="identificador"; alfabeto[2]="boolean";
		alfabeto[3]="int"; alfabeto[4]="{";	alfabeto[5]="}"; alfabeto[6]="while";
		alfabeto[7]="("; alfabeto[8]=")"; alfabeto[9]="=";
		alfabeto[10]="<"; alfabeto[11]="+"; alfabeto[12]="-";
		alfabeto[13]="*"; alfabeto[14]="true"; alfabeto[15]="false";
		alfabeto[16]="SOP"; alfabeto[17]=";"; alfabeto[18]="entero";
		indiceToken=0;  numeroToken=0;
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
								NoTok=1;
							else
								NoTok=18;
						}
						else
							NoTok=GetTokenPos(Tok);
						lsTokens.add(new Token(NoTok,Tok));
					}
					else {
						JOptionPane.showMessageDialog(null,"ERROR LÉXICO EN"
						+ " '"+Tok+"'.","Error",JOptionPane.ERROR_MESSAGE);
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

	public void GetToken() {
		numeroToken=lsTokens.get(indiceToken).NoToken;
		if(indiceToken<lsTokens.size()) 
			indiceToken++;
	}
	
	public int GetTokenPos(String token) {
		for(int i=0; i<alfabeto.length; i++) {
			if(alfabeto[i].equals(token))
				return i;
		}
		return 0;
	}
}