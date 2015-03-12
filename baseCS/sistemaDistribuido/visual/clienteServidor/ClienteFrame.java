package sistemaDistribuido.visual.clienteServidor;

import sistemaDistribuido.sistema.clienteServidor.modoMonitor.Nucleo;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.ProcesoCliente;
import sistemaDistribuido.visual.clienteServidor.MicroNucleoFrame;
import sistemaDistribuido.visual.clienteServidor.ProcesoFrame;

import java.awt.Label;
import java.awt.TextField;
import java.awt.Choice;
import java.awt.Button;
import java.awt.Panel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class ClienteFrame extends ProcesoFrame{
	private static final long serialVersionUID=1;
	private final static int HEIGHT = 400;
	private final static int WIDTH  = 1000;
	private ProcesoCliente proc;
	private Choice codigosOperacion;
	private Choice campoMensaje1, campoMensaje2;
	private Button botonSolicitud;
	private String codop1,codop2,codop3,codop4;
	private String opcion1, opcion2;

	public ClienteFrame(MicroNucleoFrame frameNucleo){
		super(frameNucleo,"Cliente de Archivos");
		add("South",construirPanelSolicitud());
		validate();
		proc=new ProcesoCliente(this);
		fijarProceso(proc);
	}

	public Panel construirPanelSolicitud(){
		Panel p=new Panel();
		codigosOperacion=new Choice();
		codop1="Crear";
		codop2="Eliminar";
		codop3="Leer";
		codop4="Escribir";
		codigosOperacion.add(codop1);
		codigosOperacion.add(codop2);
		codigosOperacion.add(codop3);
		codigosOperacion.add(codop4);
		campoMensaje1=new Choice();
		opcion1 = "hola.txt";
		opcion2 = "prueba.txt";
		campoMensaje1.add(opcion1);
		campoMensaje1.add(opcion2);
		campoMensaje2 = new Choice();
		campoMensaje2.setVisible(false);
		botonSolicitud=new Button("Solicitar");
		botonSolicitud.addActionListener(new ManejadorSolicitud());	
		p.add(new Label("Operacion:"));
		p.add(codigosOperacion);
		p.add(new Label("Datos:"));
		p.add(campoMensaje1);
		p.add(campoMensaje2);
		p.add(botonSolicitud);
		
		//Listener Choice.
		codigosOperacion.addItemListener(new ItemListener()
		{
			
			@Override
			public void itemStateChanged(ItemEvent e)
			{
				if(codigosOperacion.getSelectedItem().equals("Escribir"))
				{
					campoMensaje2.setVisible(true);
					campoMensaje2.removeAll();
					campoMensaje2.add("Texto de prueba para archivos.");
					campoMensaje2.add("Otro mensaje para escribir dentro de un archivo.");
					setSize(WIDTH, HEIGHT);
				}
				else if(codigosOperacion.getSelectedItem().equals("Leer"))
				{
					campoMensaje2.setVisible(true);
					campoMensaje2.removeAll();
					campoMensaje2.add("5");
					campoMensaje2.add("10");
					setSize(WIDTH - 200, HEIGHT);
				}
				else
				{
					campoMensaje2.setVisible(false);
				}
			}
		});
		
		return p;
	}

	class ManejadorSolicitud implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			String com=e.getActionCommand();
			if (com.equals("Solicitar")){
				botonSolicitud.setEnabled(false);
				com=codigosOperacion.getSelectedItem();
				imprimeln("Solicitud a enviar: "+com);
				imprimeln("Mensaje a enviar: "+campoMensaje1.getSelectedItem());
				//proc.
				Nucleo.reanudarProceso(proc);
			}
		}
	}
}
