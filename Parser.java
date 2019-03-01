package proyectoccnia;

import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
public class Parser {
	Scanner scn;
	JLabel mensaje;
	public Parser(Scanner scn, JLabel mensaje) {
		this.scn=scn; this.mensaje=mensaje;
	}
	
	public void VerificaGramatica() {
		scn.GetToken(); ProgramP();
	}
	
	public void Advance() {
		if(scn.indiceToken<scn.lsTokens.size())
			scn.GetToken();
	}
	
	public void Eat(int NoToken) {
		if(NoToken==scn.numeroToken) Advance();
		else {
			JOptionPane.showMessageDialog(null,"ERROR SINTÁCTICO EN EL TOKEN "+scn.indiceToken+","
			+ ""+"EXPECTED TOKEN '"+scn.alfabeto[NoToken]+"'.","Error",JOptionPane.ERROR_MESSAGE);
			System.exit(1000);
		}
	}

	public void ProgramP() {
		MainClassMC();
		ClassDeclarationCD();
		VarDeclarationVD();
		mensaje.setText("Mensaje: ¡Sintaxis de Programa Correcta!");
	}
	
	public void MainClassMC() {
		Eat(0); Eat(13); Eat(1); Eat(2); Eat(3); Eat(4); 
		Eat(5); Eat(6); Eat(7); Eat(8); Eat(9); Eat(13);
		Eat(10); Eat(1); StatementS(); Eat(11); Eat(11);
	}

	public void ClassDeclarationCD() {
		if(scn.numeroToken==0) {
			Eat(0); Eat(13); Eat(12); Eat(13); Eat(1);
			VarDeclarationVD();
			MethodDeclarationMD();
			Eat(11); ClassDeclarationCD();
		}
	}
	
	public void VarDeclarationVD() {
		if(Tipo()) {
			Eat(13); Eat(14); VarDeclarationVD();
		}
	}
	
	public boolean Tipo() {
		if(scn.numeroToken==17) { //"int"
			Eat(17); return true;
		}
		else if(scn.numeroToken==18) { //"boolean"
			Eat(18); return true;
		}
		return false;
	}
	
	public void MethodDeclarationMD() {
		if(scn.numeroToken==2) {
			Eat(2); Type(); Eat(13); Eat(6); Type(); 
			Eat(13); Parameters(); Eat(10); Eat(1);
			VarDeclarationVD(); StatementS(); Eat(16);
			ExpressionE(); Eat(14); Eat(11);
			MethodDeclarationMD();
		}
	}
	
	public void Type() {
		switch(scn.numeroToken) {
		case 17: //"int"
			Eat(17);
			if(scn.numeroToken==8) { //"["
				Eat(8); Eat(9);
			}
			break;  
		case 18: //"boolean"
			Eat(18); 
		}
	}
	
	public void Parameters() {
		if(scn.numeroToken==15) { //","
			Eat(15); Type(); Eat(13);
			Parameters();
		}
	}

	public void StatementS() {
		switch(scn.numeroToken) {
			case 19: //"if"
				Eat(19); Eat(6); ExpressionE(); Eat(10);
				StatementS(); Eat(20); StatementS(); StatementS();
				break;
			case 21: //"while"
				Eat(21); Eat(6); ExpressionE(); Eat(10);
				Eat(1); StatementS(); Eat(11); StatementS();
				break;
			case 22: //"SOP"
				Eat(22); Eat(6); ExpressionE(); Eat(10); Eat(14);
				StatementS(); 
				break;
			case 13: //"id"
				Eat(13); //"["
				if(scn.numeroToken==8) { //
					Eat(8); ExpressionE(); Eat(9);
					Eat(23); ExpressionE(); Eat(14);
					StatementS();
				}
				else if(scn.numeroToken==23) { //"="
					Eat(23); ExpressionE(); Eat(14);
					StatementS(); 
				}
				break;
			default: return;
		}
	}

	public void ExpressionE() {
		switch(scn.numeroToken) {
			case 24: //"&&"
				Eat(24); ExpressionE();
				break;
			case 25: //"<"
				Eat(25); ExpressionE();
				break;
			case 26: //"+"
				Eat(26); ExpressionE();
				break;
			case 35: //"-"
				Eat(35); ExpressionE();
				break;
			case 27: //"*"
				Eat(27); ExpressionE();
				break;
			case 8: //"["
				Eat(8); ExpressionE(); Eat(9);
				break;
			case 28: //"."
				Eat(8);
				if(scn.numeroToken==36) { //"length"
					Eat(36);
				}
				else if(scn.numeroToken==13) { //Identifier
					Eat(13); Eat(6); Eat(10);
				}
			case 34: //<INTEGER_LITERAL>
				Eat(34); ExpressionE();
				break;
			case 29: //"true"
				Eat(29); ExpressionE();
				break;
			case 30: //"false"
				Eat(30); ExpressionE();
				break;
			case 13: //"identifier"
				Eat(13); ExpressionE();
				break;
			case 31: //"this"
				Eat(31); ExpressionE();
				break;
			case 32: //"new"
				Eat(32); 
				if(scn.numeroToken==17) { //"int"
					Eat(17); Eat(8); ExpressionE(); Eat(9);
				}
				else if(scn.numeroToken==13) { //Identifier
					Eat(13); Eat(6); Eat(10); 
				}
				ExpressionE();
				break;
			case 33: //"!"
				Eat(33); ExpressionE(); ExpressionE();
				break;
			case 6: //"("
				Eat(6); ExpressionE(); Eat(10); ExpressionE();
				break;
			default: return;
		}
	}
}
