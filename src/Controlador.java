import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Vector;

// Controlador.java
// Componente Controlador del enfoque Modelo - Vista - Controlador

public class Controlador {
	private Modelo miModelo;
	private Vista miVista;
	
	private Modelo.Figura fig;
	private Point2D puntoInicial;
	private Point2D puntoFinal;
	
	// Variables a modificar con las opciones del menú
	Modelo.Tipo tipoFigura = Modelo.Tipo.Linea;
	Color colorFigura = Color.BLACK;
	boolean rellenoFigura = false;
	
	public Controlador( Modelo mod, Vista vis )
	{
		miModelo = mod;
		miVista = vis;
	}
	
	public void iniciarVista()
	{
		//Oyente de ventana
		OyenteVentana ov = new OyenteVentana();
		miVista.miVentana.addWindowListener(ov);

		// Oyentes de ratón
		OyenteDeRaton odr = new OyenteDeRaton();
		miVista.miPanel.addMouseListener(odr);
		miVista.miPanel.addMouseMotionListener(odr);

		//Oyentes de barra de menú
		OyenteDeBarra odb = new OyenteDeBarra();
		miVista.mniNuevo.addActionListener(odb);
		miVista.mniAbrir.addActionListener(odb);
		miVista.mniGuardar.addActionListener(odb);
		miVista.mniImprimir.addActionListener(odb);
		miVista.mniSalir.addActionListener(odb);
		miVista.mniLinea.addActionListener(odb);
		miVista.mniRectangulo.addActionListener(odb);
		miVista.mniElipse.addActionListener(odb);
		miVista.mniRelleno.addActionListener(odb);
		miVista.mniColor.addActionListener(odb);
		miVista.mniAcercaDe.addActionListener(odb);
		miVista.cmbTipo.addActionListener(odb);
		miVista.chkRelleno.addActionListener(odb);
		miVista.btnColorChooser.addActionListener(odb);
		
		miVista.miVentana.setVisible(true);
	}
	
	private Modelo.Figura crearFigura()
	{
		Modelo.Figura resultado;
		Shape temporal;
		
		// Determinar si es una linea
		if ( tipoFigura == Modelo.Tipo.Linea )
		{
			temporal = new Line2D.Double( puntoInicial, puntoFinal );
			resultado = miModelo.new Figura( temporal, colorFigura, false );
		}
		else
		{
			double x = Math.min( puntoInicial.getX(), puntoFinal.getX());
			double y = Math.min( puntoInicial.getY(), puntoFinal.getY());
			double ancho = Math.abs( puntoInicial.getX() - puntoFinal.getX());
			double alto = Math.abs( puntoInicial.getY() - puntoFinal.getY());
			
			if ( tipoFigura == Modelo.Tipo.Rectangulo )
				temporal = new Rectangle2D.Double(x, y, ancho, alto);
			else
				temporal = new Ellipse2D.Double(x, y, ancho, alto);
			
			resultado = miModelo.new Figura( temporal, colorFigura, rellenoFigura );
		}
		
		return resultado;
	}

	//Clase interna para oyente de barra de menú
	class OyenteDeBarra implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			if (e.getSource() == miVista.mniNuevo)
			{
				int continuar = JOptionPane.showConfirmDialog(null, "¿Deseas guardar el dibujo actual?", "Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if ( continuar == JOptionPane.YES_OPTION)
				{
					if(miModelo.guardar())
					{
						JOptionPane.showMessageDialog(null, "Dibujo guardado con éxito");
					}
					else
					{
						JOptionPane.showMessageDialog(null, "Hubo un error al guardar el archivo.");
					}
				}
				Modelo.figuras = new Vector<>();
				miVista.miPanel.repaint();
				miVista.mniNuevo.setEnabled(false);
			}
			else if (e.getSource() == miVista.mniAbrir)
			{
				if (Modelo.figuras.size() > 0)
				{
					int continuar = JOptionPane.showConfirmDialog(null, "¿Deseas guardar el dibujo actual?", "Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					if ( continuar == JOptionPane.YES_OPTION)
					{
						if(miModelo.guardar())
						{
							JOptionPane.showMessageDialog(null, "Dibujo guardado con éxito");
						}
						else
						{
							JOptionPane.showMessageDialog(null, "Hubo un error al guardar el archivo.");
						}
					}
				}
				if (miModelo.abrir())
				{
					miVista.miPanel.repaint();
					miVista.mniNuevo.setEnabled(true);
				}
				else
					JOptionPane.showMessageDialog(null, "Hubo un error al recuperar el dibujo");
			}
			else if (e.getSource() == miVista.mniGuardar)
			{
				if(miModelo.guardar())
				{
					JOptionPane.showMessageDialog(null, "Dibujo guardado con éxito");
				}
				else
				{
					JOptionPane.showMessageDialog(null, "Hubo un error al guardar el archivo.");
				}
			}
			else if (e.getSource() == miVista.mniImprimir)
			{
				if (Modelo.figuras.size() == 0)
				{
					JOptionPane.showMessageDialog(null, "No ha dibujado sobre el lienzo", "Error de usuario", JOptionPane.ERROR_MESSAGE);
					return;
				}
				miVista.miPanel.print();
			}
			else if (e.getSource() == miVista.mniSalir)
			{
				if (Modelo.figuras.size() > 0)
				{
					int continuar = JOptionPane.showConfirmDialog(null, "¿Deseas guardar el dibujo antes de salir?", "Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					if (continuar == JOptionPane.YES_OPTION)
					{
						if (miModelo.guardar())
						{
							JOptionPane.showMessageDialog(null, "Dibujo guardado con éxito");
						}
						else
						{
							JOptionPane.showMessageDialog(null, "Hubo un error al guardar el archivo.");
						}
					}
					miVista.miVentana.dispose();
					return;
				}
				int continuar = JOptionPane.showConfirmDialog(null, "¿Deseas salir?", "Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (continuar == JOptionPane.YES_OPTION)
				{
					miVista.miVentana.dispose();
				}
			}
			else if (e.getSource() == miVista.mniLinea)
			{
				tipoFigura = Modelo.Tipo.Linea;
				miVista.cmbTipo.setSelectedIndex(0);
				miVista.mniRelleno.setEnabled(false);
				miVista.chkRelleno.setEnabled(false);
			}
			else if (e.getSource() == miVista.mniRectangulo)
			{
				tipoFigura = Modelo.Tipo.Rectangulo;
				miVista.cmbTipo.setSelectedIndex(1);
				miVista.chkRelleno.setEnabled(true);
				miVista.mniRelleno.setEnabled(true);
			}
			else if (e.getSource() == miVista.mniElipse)
			{
				tipoFigura = Modelo.Tipo.Elipse;
				miVista.cmbTipo.setSelectedIndex(2);
				miVista.chkRelleno.setEnabled(true);
				miVista.mniRelleno.setEnabled(true);
			}
			else if (e.getSource() == miVista.mniRelleno)
			{
				miVista.chkRelleno.setSelected(!miVista.chkRelleno.isSelected());
				rellenoFigura = !rellenoFigura;
			}
			else if (e.getSource() == miVista.mniColor || e.getSource() == miVista.btnColorChooser)
			{
				colorFigura = JColorChooser.showDialog(null, "Seleccione el color de la figura:", Color.BLACK);
			}
			else if (e.getSource() == miVista.mniAcercaDe)
			{
				JOptionPane.showMessageDialog(null, "Programa desarrollado por:\nLeonardo Solón Sánchez Flores\nNo de control: 19170672");
			}
			else if (e.getSource() == miVista.cmbTipo)
			{
				switch (miVista.cmbTipo.getSelectedIndex())
				{
					case 0 -> {
						tipoFigura = Modelo.Tipo.Linea;
						miVista.chkRelleno.setEnabled(false);
						miVista.mniRelleno.setEnabled(false);
						miVista.mniLinea.setSelected(true);
					}
					case 1 -> {
						tipoFigura = Modelo.Tipo.Rectangulo;
						miVista.chkRelleno.setEnabled(true);
						miVista.mniRelleno.setEnabled(true);
						miVista.mniRectangulo.setSelected(true);
					}
					default -> {
						tipoFigura = Modelo.Tipo.Elipse;
						miVista.chkRelleno.setEnabled(true);
						miVista.mniRelleno.setEnabled(true);
						miVista.mniElipse.setSelected(true);
					}
				}
			}
			else
			{
				miVista.mniRelleno.setSelected(!miVista.mniRelleno.isSelected());
				rellenoFigura = !rellenoFigura;
			}
		}
	}

	//Clase oyente para el cierre de la ventana
	class OyenteVentana extends WindowAdapter
	{
		public void windowClosing( WindowEvent e )
		{
			if (Modelo.figuras.size() > 0)
			{
				int continuar = JOptionPane.showConfirmDialog(null, "¿Deseas guardar el dibujo antes de salir?", "Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (continuar == JOptionPane.YES_OPTION)
				{
					if (miModelo.guardar())
					{
						JOptionPane.showMessageDialog(null, "Dibujo guardado con éxito");
					}
					else
					{
						JOptionPane.showMessageDialog(null, "Hubo un error al guardar el archivo.");
					}
				}
				miVista.miVentana.dispose();
				return;
			}
			int continuar = JOptionPane.showConfirmDialog(null, "¿Deseas salir?", "Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (continuar == JOptionPane.YES_OPTION)
			{
				miVista.miVentana.dispose();
			}
		}
	}
	
	// Clase interna para oyente de ratón
	class OyenteDeRaton extends MouseAdapter
	{
		public void mousePressed( MouseEvent e )
		{
			miVista.miPanel.setCursor( Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
			puntoInicial = e.getPoint();
			fig = null;
		}
		
		public void mouseReleased( MouseEvent e )
		{
			miVista.miPanel.setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			puntoFinal = e.getPoint();
			fig = crearFigura();
			Modelo.figuras.add(fig);
			miVista.miPanel.repaint();
			miVista.mniNuevo.setEnabled(true);
		}
		
		public void mouseDragged( MouseEvent e )
		{
			puntoFinal = e.getPoint();
			if ( fig != null )
			{
				// Borrar la figura anterior
				Graphics2D g2D = (Graphics2D) miVista.miPanel.getGraphics();
				g2D.setXORMode( miVista.miPanel.getBackground() );
				fig.dibujar(g2D);
			}
			// Dibujar la nueva figura
			fig = crearFigura();
			Graphics2D g2D = (Graphics2D) miVista.miPanel.getGraphics();
			g2D.setXORMode( miVista.miPanel.getBackground() );
			fig.dibujar(g2D);
		}
	}


}













