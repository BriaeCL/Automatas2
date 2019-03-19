package Principal;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import Arbol.*;
public class AnalizadorSintactico {
	AnalizadorLexico scn; 
	Arbol<String> ArbolSintactico;
	Arbol<String> ArbolAuxiliar1;
	Arbol<String> ArbolAuxiliar2;
	Arbol<String> ArbolWhile;
	ArrayList<Arbol<String>> WhileSons;
	int whileloop;
	int nuevoID;
	JLabel mensaje;
	
	public AnalizadorSintactico() { }
	
	public AnalizadorSintactico(AnalizadorLexico scn, JLabel mensaje) {
		this.scn=scn;
		this.mensaje=mensaje;
		ArbolSintactico=new Arbol<String>();
		ArbolAuxiliar1=new Arbol<String>();
		ArbolAuxiliar2=new Arbol<String>();
		ArbolWhile=new Arbol<String>();
		WhileSons=new ArrayList<Arbol<String>>();
		whileloop=0;
		nuevoID=0;
	}
	
	public void VerificaGramatica() {
		scn.GetToken(); ProgramP();
	}
	
	public void Advance() {
		if(scn.indiceToken<scn.lsTokens.size())
			scn.GetToken();
	}
	
	public void Eat(int NoToken) {
		if(NoToken==scn.numeroToken)
			Advance();
		else {
			JOptionPane.showMessageDialog(null,"ERROR SINTÁCTICO EN EL TOKEN "+scn.indiceToken+","
			+ ""+"SE ESPERABA EL TOKEN '"+scn.alfabeto[NoToken]+"'.","Error",JOptionPane.ERROR_MESSAGE);
			System.exit(1000);
		}
	}

	public void ProgramP() {
		Eat(0);
		ArbolSintactico.Insertar(scn.lsTokens.get(scn.indiceToken-2).Nombre,0); 
		Eat(1); 
		ArbolSintactico.Insertar(scn.lsTokens.get(scn.indiceToken-2).Nombre,1);
		VarDeclarationVD();
		Eat(4);
		StatementS();
		Eat(5);
		mensaje.setText("Mensaje: ¡Sintaxis de Programa Correcta!");
	}

	public void VarDeclarationVD() {
		ArbolAuxiliar1=new Arbol<String>();
		if(Tipo()) {
			Eat(1);
			ArbolAuxiliar1.Insertar(scn.lsTokens.get(scn.indiceToken-2).Nombre,1); 
			ArbolSintactico.raiz.hijos.add(ArbolAuxiliar1.raiz); 
			Eat(17);
			VarDeclarationVD();
		}
		return;
	}
	
	public boolean Tipo() {
		if(scn.numeroToken==2) { //"boolean"
			Eat(2);
			ArbolAuxiliar1.Insertar(scn.lsTokens.get(scn.indiceToken-2).Nombre,0);
			return true;
		}
		else if(scn.numeroToken==3) { //"int"
			Eat(3); 
			ArbolAuxiliar1.Insertar(scn.lsTokens.get(scn.indiceToken-2).Nombre,0);
			return true;
		}
		else
			return false;
	}
	
	public void StatementS() {
		ArbolAuxiliar1=new Arbol<String>(); 
		ArbolAuxiliar2=new Arbol<String>();
		int op=0;
		switch(scn.numeroToken) {
			case 5: //while end.
				if(whileloop==1) {
					Eat(5);
					for(int i=0; i<WhileSons.size(); i++)
						ArbolWhile.raiz.hijos.add(WhileSons.get(i).raiz);
					ArbolSintactico.raiz.hijos.add(ArbolWhile.raiz);
					ArbolWhile=new Arbol<String>();
					WhileSons=new ArrayList<Arbol<String>>();	
				}
				else
					return;
				break; 
			case 6: //"while" "(" E ")" Statement
				Eat(6);
				ArbolAuxiliar1.Insertar(scn.lsTokens.get(scn.indiceToken-2).Nombre,0);
				Eat(7);
				op=scn.lsTokens.get(scn.indiceToken).NoToken;
				if(op==10 || op==11 || op==12 || op==13) {
					ExpressionE(2); Eat(8);
					ArbolAuxiliar1.raiz.hijos.add(ArbolAuxiliar2.raiz);
				}
				else {
					ExpressionE(1); Eat(8); 
				}
				ArbolWhile.raiz=ArbolAuxiliar1.raiz;
				Eat(4);
				whileloop=1;
				StatementS();
				whileloop=0;
				StatementS(); 
				break;
			case 16: //"SOP" "(" E ")" ";"
				Eat(16); 
				ArbolAuxiliar1.Insertar(scn.lsTokens.get(scn.indiceToken-2).Nombre,0);
				Eat(7);
				op=scn.lsTokens.get(scn.indiceToken).NoToken;
				if(op==10 || op==11 || op==12 || op==13) {
					ExpressionE(2);	Eat(8); Eat(17);
					ArbolAuxiliar1.raiz.hijos.add(ArbolAuxiliar2.raiz);
				}
				else {
					ExpressionE(1);	Eat(8); Eat(17);
				}
				if(whileloop==1)
					WhileSons.add(ArbolAuxiliar1);
				else
					ArbolSintactico.raiz.hijos.add(ArbolAuxiliar1.raiz);
				StatementS(); 
				break;
			case 1: //id "=" E ";"
				Eat(1);	Eat(9);
				op=scn.lsTokens.get(scn.indiceToken).NoToken;
				if(op==10 || op==11 || op==12 || op==13) {
					ArbolAuxiliar1.Insertar(scn.lsTokens.get(scn.indiceToken-2).Nombre,0);
					ArbolAuxiliar1.Insertar(scn.lsTokens.get(scn.indiceToken-3).Nombre,1);
					ExpressionE(2); Eat(17);
					ArbolAuxiliar1.raiz.hijos.add(ArbolAuxiliar2.raiz);
				}
				else {
					ArbolAuxiliar1.Insertar(scn.lsTokens.get(scn.indiceToken-2).Nombre,0);
					ArbolAuxiliar1.Insertar(scn.lsTokens.get(scn.indiceToken-3).Nombre,1);
					ExpressionE(1); Eat(17);
				}
				if(whileloop==1)
					WhileSons.add(ArbolAuxiliar1);
				else	
					ArbolSintactico.raiz.hijos.add(ArbolAuxiliar1.raiz);
				StatementS(); 
				break;
			default: 
				JOptionPane.showMessageDialog(null,"\"ERROR SINTÁCTICO (No. Token: "
				+scn.indiceToken+").\"","Error",JOptionPane.ERROR_MESSAGE);
				System.exit(1000);
		}
	}

	public void ExpressionE(int caso) {
		switch(scn.numeroToken) {
			case 14: //"true"
				Eat(14); 
				ArbolAuxiliar1.Insertar(scn.lsTokens.get(scn.indiceToken-2).Nombre,1);
				break;
			case 15: //"false"
				Eat(15); 
				ArbolAuxiliar1.Insertar(scn.lsTokens.get(scn.indiceToken-2).Nombre,1);
				break;
			case 1: //"id"
				Eat(1);
				if(caso==1) 
					ArbolAuxiliar1.Insertar(scn.lsTokens.get(scn.indiceToken-2).Nombre,1);
				if(scn.numeroToken==10 || scn.numeroToken==11 || scn.numeroToken==12 || scn.numeroToken==13) { 
					Eat(scn.numeroToken); 
					if(caso==1) 
						ArbolAuxiliar1.Insertar(scn.lsTokens.get(scn.indiceToken-2).Nombre,1);
					else {
						ArbolAuxiliar2.Insertar(scn.lsTokens.get(scn.indiceToken-2).Nombre,0);
						ArbolAuxiliar2.Insertar(scn.lsTokens.get(scn.indiceToken-3).Nombre,1);
					}
					Eat(1);
					if(caso==1)
						ArbolAuxiliar1.Insertar(scn.lsTokens.get(scn.indiceToken-2).Nombre,1);
					else
						ArbolAuxiliar2.Insertar(scn.lsTokens.get(scn.indiceToken-2).Nombre,1);
				}
				break;
			case 18: //"integer"
				Eat(18);
				ArbolAuxiliar1.Insertar(scn.lsTokens.get(scn.indiceToken-2).Nombre,1);
				break;
			default:
				JOptionPane.showMessageDialog(null,"\"ERROR SINTÁCTICO (No. Token: "
				+scn.indiceToken+").\"","Error",JOptionPane.ERROR_MESSAGE);
				System.exit(1000);
		}
	}

	public void ActualizaNodosID(Arbol<String> arbol) {
		if(arbol.raiz.hijos.isEmpty()){
			arbol.altura=1; return;
		}
		nuevoID=2; ActualizaNodosID(arbol.raiz);
	}

	public void ActualizaNodosID(NodoArbol<String> R) {
		for(int i=0; i<R.hijos.size(); i++) {
			R.hijos.get(i).id=nuevoID; nuevoID++;
			ActualizaNodosID(R.hijos.get(i));
		}
	}
}