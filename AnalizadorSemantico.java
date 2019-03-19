package Principal;

import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import Arbol.*;

public class AnalizadorSemantico {
	AnalizadorLexico scn;
	AnalizadorSintactico par; 
	Arbol<String> ArbolSintactico;
	ArrayList<Arbol<String>> subArboles;
	Arbol<String> subArbol;
	JTable tablaSimbolos;
	String [] hSimbolos;
	JLabel lblMensaje;
	
	public AnalizadorSemantico() {	}
	
	public AnalizadorSemantico(AnalizadorLexico scn, AnalizadorSintactico par, Arbol<String> ArbolSintactico,
	JTable tablaSimbolos, String [] hSimbolos, JLabel lblMensaje) {
		this.scn=scn; this.par=par; this.ArbolSintactico=ArbolSintactico;
		subArboles=new ArrayList<Arbol<String>>(); subArbol=new Arbol<String>();
		this.hSimbolos=new String[4]; this.hSimbolos=hSimbolos;
		this.tablaSimbolos=new JTable(); this.tablaSimbolos=tablaSimbolos;
		this.lblMensaje=new JLabel(); this.lblMensaje=lblMensaje;
	}
	
	//CONSTRUYE LA TABLA SIMBÓLICA DE LOS IDENTIFICADORES:
	public void ConstruyeTablaSimbolos() {
		String [][] tableContent;
		GetIdentifiers(ArbolSintactico.raiz); //DETERMINA LOS NODOS QUE ENGLOBAN (EN GENERAL) LOS TIPOS DE DATOS (int/bool):
		tableContent=new String[subArboles.size()][3];
		for(int i=0; i<subArboles.size(); i++) {
			String token=subArboles.get(i).raiz.info;
			if(TipoVariable(token)) {
				tableContent[i][0]=String.valueOf((i+1)); //'NÚMERO' DEL IDENTIFICADOR.
				tableContent[i][1]=subArboles.get(i).raiz.hijos.get(0).info; //'NOMBRE' DEL IDENTIFICADOR.
				tableContent[i][2]=subArboles.get(i).raiz.info; //'TIPO' DEL IDENTIFICADOR.
			}
			else {
				tableContent[i][0]=String.valueOf((i+1)); //'NÚMERO' DE IDENTIFICADOR=1 (DEFAULT).
				tableContent[i][1]=subArboles.get(i).raiz.info; //'NOMBRE' DE LA CLASE PROGRAMÁTICA (DEFAULT).
				tableContent[i][2]="class"; //'TIPO' DE INDENTIFICADOR DE CLASE (DEFAULT).
			}
		}
		tablaSimbolos.setModel(new DefaultTableModel(tableContent,hSimbolos));
		VerificaDatosDuplicados(tableContent);
	}
	
	//OBTIENE TODOS LOS TIPOS DE IDENTIFICADORES Y LOS METE 
	//A LA LISTA DE NODOS ARBORALES LLAMADA 'subArboles':
	public void GetIdentifiers(NodoArbol<String> R) {
		String token="";
		for(int i=0; i<R.hijos.size(); i++) {
			token=R.hijos.get(i).info;
			subArbol=new Arbol<String>();
			if(scn.IsIdentifier(token) && !scn.EstaEnAlfabeto(token)){
				subArbol.raiz=R.hijos.get(i); subArboles.add(subArbol);
			}
			else if(TipoVariable(R.hijos.get(i).info)) {
				subArbol.raiz=R.hijos.get(i); subArboles.add(subArbol);
			}
		}
	}
	
	//VERIFICA LA DUPLICACIÓN DE IDENTIFICADORES PROGRAMÁTICOS:
	public void VerificaDatosDuplicados(String[][] tableContent) {
		for(int i=0; i<tableContent.length; i++) {
			String dato=tableContent[i][1];
			if(Duplicado(i,tableContent,dato)) {
				JOptionPane.showMessageDialog(null,"ERROR SEMÁNTICO, DATO '" //NOTIFICACIÓN............
				+dato+"' DUPLICADO.","Error",JOptionPane.ERROR_MESSAGE);	//SEMÁNTICA (DUPLICACIÓN).
				System.exit(1000);
			}
		}
		lblMensaje.setText("Mensaje: Tabla de Símbolos Correcta!!!"); //NOTIFICACIÓN SEMÁNTICA (DECLARACIONES).
	}
	
	//VERIFICA SI EL IDENTIFICADOR 'dato' SE REPITE EN EL HARREGLO SIMBÓLICO 'tableContent'
	//DESDE i<=j<tableContent.length PARA EL AHORRO DE RECORRIDO DE DICHO HARREGLO........:
	public boolean Duplicado(int i, String[][] tableContent, String dato) {
		for(int j=i; j<tableContent.length; j++) {
			if(j!=i && tableContent[j][1].equals(dato))
				return true;
		}
		return false;
	}

	//DETERMINA SI UN TOKEN (IDENTIFIER) ES UN TIPO DE VARIABLE (int ó boolean):
	public boolean TipoVariable(String token) {
		if(!token.equals("int") && !token.equals("boolean"))
			return false;
		return true;
	}
	
	//VERIFICA LA SEMÁNTICA PROGRAMÁTICA;
	public void VerificaSemantica() {
		VerificaSemantica(ArbolSintactico.raiz); //LLAMA AL MÉTODO DE VERIFICACIÓN DE SEMÁNTICA PROGRAMÁTICA.......
		lblMensaje.setText("Mensaje: ¡Semántica de Programa Correcta!"); //NOTIFICACIÓN SEMÁNTICA (RESULTADO FINAL).
	}
	
	//VERIFICA LA SEMÁNTICA PROGRAMÁTICA RECORRIENDO EL ÁRBOL SINTÁCTICO DE LA FASE ANTERIOR (ANÁLISIS SINTÁCTICO):
	public void VerificaSemantica(NodoArbol<String> R) {
		String token=""; int tamano=0;
		for(int i=1; i<R.hijos.size(); i++) {
			token=R.hijos.get(i).info;
			if(token.equals("while") || token.equals("SOP") || token.equals("=")) {
				subArbol=new Arbol<String>();
				subArbol.raiz=R.hijos.get(i);
				subArbol.raiz.id=1;
				par.ActualizaNodosID(subArbol);
				subArbol.DeterminaAltura();
				tamano=subArbol.GetNoNodos();				
				if(token.equals("while")) //VERIFICA EL SUB-ÁRBOL MEDIANTE EL CASO 'WHILE' [CICLO].
					VerificaBucle(subArbol);
				else if(token.equals("SOP")) //VERIFICA EL SUB-ÁRBOL MEDIANTE EL CASO 'SOP' [IMPRESIÓN].
					VerificaSOP1(subArbol); 
				else if(token.equals("=")) //VERIFICA EL SUB-ÁRBOL MEDIANTE EL CASO '=' [ASIGNACIÓN].
					VerificaAsignacion(subArbol);
			}
		}
	}

	//VERIFICA LA SEMÁNTICA DE UN BUCLE (CICLO WHILE):
	public void VerificaBucle(Arbol<String> ArbolWhile) {
		String token="", tipo=""; int tamano=0;
		subArbol=new Arbol<String>();
		subArbol.raiz=ArbolWhile.raiz.hijos.get(0);
		subArbol.raiz.id=1;
		par.ActualizaNodosID(subArbol);
		subArbol.DeterminaAltura();
		tamano=subArbol.GetNoNodos();				
		if(subArbol.altura==1) { //CASO 1: while(value).
			token=subArbol.raiz.info;
			if(scn.IsIdentifier(token) && !scn.EstaEnAlfabeto(token)) {
				tipo=TipoDato(token);
				if(tipo.isEmpty()) {
					JOptionPane.showMessageDialog(null,"ERROR EXISTENCIAL, EL DATO '"	//NOTIFICACIÓN............
					+token+"' NO FUE DECLARADO","Error",JOptionPane.ERROR_MESSAGE); 	//SEMÁNTICA (EXISTENCIAL).
					System.exit(1000);
				}
			}
		}
		else if(subArbol.altura==2) //CASO 2: while(Identifier <operator> Identifier).
			VerificaAsignacion(subArbol);
		VerificaSemantica(ArbolWhile.raiz); //VERIFICA LA SEMÁNTICA DEL SUB-ÁRBOL WHILE ACTUAL.
	}
	
	//VERIFICA LA SEMÁNTICA DE UNA IMPRESION (SOP):
	public void VerificaSOP1(Arbol<String> ArbolSop){
		String token="", tipo="";
		if(ArbolSop.altura==3) //CASO 1: SOP(Identifier <operator> Identifier).
			VerificaSOP2(ArbolSop);
		else if(ArbolSop.altura==2) { //CASO 2: SOP(Identifier).
			token=ArbolSop.raiz.hijos.get(0).info;
			if(scn.IsIdentifier(token) && !scn.EstaEnAlfabeto(token)) {
				tipo=TipoDato(token);
				if(tipo.isEmpty()) {
					JOptionPane.showMessageDialog(null,"ERROR EXISTENCIAL, EL DATO '"	//NOTIFICACIÓN............
					+token+"' NO FUE DECLARADO","Error",JOptionPane.ERROR_MESSAGE);		//SEMÁNTICA (EXISTENCIAL).
					System.exit(1000);
				}
			}
		}
	}
	
	//VERIFICA LA SEMÁNTICA DE UNA IMPRESION: SOP(Identifier <operator> Identifier)
	public void VerificaSOP2(Arbol<String> ArbolSop) {
		ArrayList<String> identifiers=new ArrayList<String>();
		identifiers=ArbolSop.GetNodeLSInfo(ArbolSop.GetNodosPorNivel(3));
		String id1=identifiers.get(0), id2=identifiers.get(1);
		String tipo1=TipoDato(id1), tipo2=TipoDato(id2);
		if(tipo1.isEmpty()) {
			JOptionPane.showMessageDialog(null,"ERROR EXISTENCIAL, EL DATO '"	//NOTIFICACIÓN............
			+id1+"' NO FUE DECLARADO","Error",JOptionPane.ERROR_MESSAGE);		//SEMÁNTICA (EXISTENCIAL).
			System.exit(1000);
		}
		else if(tipo2.isEmpty()) {
			JOptionPane.showMessageDialog(null,"ERROR EXISTENCIAL, EL DATO '"	//NOTIFICACIÓN............
			+id2+"' NO FUE DECLARADO","Error",JOptionPane.ERROR_MESSAGE);		//SEMÁNTICA (EXISTENCIAL).
			System.exit(1000);
		}
		else if(!tipo1.equals("int") || !tipo2.equals("int")) {
			JOptionPane.showMessageDialog(null,"INCOMPATIBILIDAD DE TIPOS "					//NOTIFICACIÓN...............
			+"ENTRE LOS DATOS '"+id1+"' y '"+id2+"'","Error",JOptionPane.ERROR_MESSAGE);	//SEMÁNTICA (COMPATIBILIDAD).
			System.exit(1000);
		}
	}

	//VERIFICA LA SEMÁNTICA DE UNA ASIGNACIÓN:
	public void VerificaAsignacion(Arbol<String> ArbolAsig){
		if(ArbolAsig.altura==3) //CASO 1: Identifier = Identifier <operator> Identifier.
			VerificaAsignacion(ArbolAsig, ArbolAsig.raiz);
		else if(ArbolAsig.altura==2) { //CASO 2: Identifier <operator> Identifier
			ArrayList<String> identifiers=new ArrayList<String>();
			identifiers=ArbolAsig.GetNodeLSInfo(ArbolAsig.GetNodosPorNivel(2));
			String id1=identifiers.get(0), tok=identifiers.get(1);
			String tipo1=TipoDato(id1), tipo2=TipoDato(tok);
			if(tipo1.isEmpty()) {
				JOptionPane.showMessageDialog(null,"ERROR EXISTENCIAL, EL DATO '"	//NOTIFICACIÓN............
				+id1+"' NO FUE DECLARADO","Error",JOptionPane.ERROR_MESSAGE);		//SEMÁNTICA (EXISTENCIAL).
				System.exit(1000);
			}
			else if(tipo2.isEmpty()) {
				if(!scn.IsInteger(tok) && !tok.equals("false") && !tok.equals("true")) {
					JOptionPane.showMessageDialog(null,"ERROR EXISTENCIAL, EL DATO '"	//NOTIFICACIÓN............
					+tok+"' NO FUE DECLARADO","Error",JOptionPane.ERROR_MESSAGE);		//SEMÁNTICA (EXISTENCIAL).
					System.exit(1000);
				}
				if((tipo1.equals("int") && (tok.equals("false") || tok.equals("true")))
				||(tipo1.equals("boolean") && (!tok.equals("false") && !tok.equals("true"))) ) {
					JOptionPane.showMessageDialog(null,"INCOMPATIBILIDAD DE TIPOS, ASIGNACIÓN"			//NOTIFICACIÓN............
					+ " INCORRECTA ENTRE '"+id1+"' y '"+tok+"'","Error",JOptionPane.ERROR_MESSAGE);		//SEMÁNTICA (COMPATIBILIDAD).
					System.exit(1000);
				}
			}
			else if(!tipo1.equals("int") || !tipo2.equals("int")) {
				JOptionPane.showMessageDialog(null,"INCOMPATIBILIDAD DE TIPOS "					//NOTIFICACIÓN...............
				+"ENTRE LOS DATOS '"+id1+"' y '"+tok+"'","Error",JOptionPane.ERROR_MESSAGE);	//SEMÁNTICA (COMPATIBILIDAD).
				System.exit(1000);
			}
		}
	}
	
	//VERIFICA LA SEMÁNTICA DE UNA ASIGNACIÓN (Identifier = Identifier <operator> Identifier):
	public void VerificaAsignacion(Arbol<String> ArbolAsig, NodoArbol<String> R) {
		ArrayList<String> identifiers=new ArrayList<String>();
		identifiers=ArbolAsig.GetNodeLSInfo(ArbolAsig.GetNodosPorNivel(3));
		String id1=R.hijos.get(0).info, id2=identifiers.get(0), id3=identifiers.get(1);
		String tipo1=TipoDato(id1), tipo2=TipoDato(id2), tipo3=TipoDato(id3);
		if(tipo1.isEmpty()) {
			JOptionPane.showMessageDialog(null,"ERROR EXISTENCIAL, EL DATO '"	//NOTIFICACIÓN............
			+id1+"' NO FUE DECLARADO","Error",JOptionPane.ERROR_MESSAGE);		//SEMÁNTICA (EXISTENCIAL).
			System.exit(1000);
		}
		else if(tipo2.isEmpty()) {
			JOptionPane.showMessageDialog(null,"ERROR EXISTENCIAL, EL DATO '"	//NOTIFICACIÓN............
			+id2+"' NO FUE DECLARADO","Error",JOptionPane.ERROR_MESSAGE);		//SEMÁNTICA (EXISTENCIAL).
			System.exit(1000);
		}
		else if(tipo3.isEmpty()) {
			JOptionPane.showMessageDialog(null,"ERROR EXISTENCIAL, EL DATO '"	//NOTIFICACIÓN............
			+id3+"' NO FUE DECLARADO","Error",JOptionPane.ERROR_MESSAGE);		//SEMÁNTICA (EXISTENCIAL).
			System.exit(1000);
		}
		else if(!tipo1.equals("int") || !tipo2.equals("int") || !tipo3.equals("int")) {		
			JOptionPane.showMessageDialog(null,"INCOMPATIBILIDAD DE TIPOS ENTRE LOS "		//NOTIFICACIÓN...............
			+ "DATOS '"+id1+"', '"+id2+"' y '"+id3+"'","Error",JOptionPane.ERROR_MESSAGE);	//SEMÁNTICA (COMPATIBILIDAD).
			System.exit(1000);
		}
	}
	
	//DADO UN IDENTIFICADOR, REGRESA EL TIPO DE DATO DEL MISMO:
	public String TipoDato(String dato) {
		String tipo="";
		for(int i=0; i<tablaSimbolos.getRowCount(); i++) {
			if(tablaSimbolos.getValueAt(i,1).equals(dato)) {
				tipo=(String)tablaSimbolos.getValueAt(i,2);
				break;
			}
		}
		return tipo;
	}
}