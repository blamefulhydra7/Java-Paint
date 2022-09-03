import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import javax.swing.*;

// Vista.java
// Componente Vista del enfoque Modelo - Vista - Controlador

public class Vista {
	MiVentana miVentana;
	MiPanel miPanel;
	MiToolBar miToolBar;
	JComboBox<String> cmbTipo;
	JCheckBox chkRelleno;
	JButton btnColorChooser;
	JMenuBar mbBarra;
	JMenu mnArchivo, mnDibujar, mnAyuda;
	JMenuItem mniNuevo, mniGuardar, mniAbrir, mniImprimir, mniSalir, mniLinea, mniRectangulo, mniElipse, mniRelleno, mniColor, mniAcercaDe;

	public Vista()
	{
		miVentana = new MiVentana( "Proyecto MVC Dibujo" );
		miVentana.setDefaultCloseOperation( JFrame.DO_NOTHING_ON_CLOSE );
		miVentana.setSize( 700, 500 );
		miVentana.setLocationRelativeTo(null);
	}
	
	class MiVentana extends JFrame
	{
		public MiVentana( String titulo )
		{
			super( titulo );

			mbBarra = new JMenuBar();
			setJMenuBar(mbBarra);

			mnArchivo = new JMenu("Archivo");
			mnArchivo.setMnemonic('A');

			mniNuevo = new JMenuItem("Nuevo");
			mniNuevo.setMnemonic('N');
			mniNuevo.setEnabled(false);

			mniAbrir = new JMenuItem("Abrir...");
			mniAbrir.setMnemonic('A');

			mniGuardar = new JMenuItem("Guardar como...");
			mniGuardar.setMnemonic('G');

			mniImprimir = new JMenuItem("Imprimir...");
			mniImprimir.setMnemonic('I');

			mniSalir = new JMenuItem("Salir");
			mniSalir.setMnemonic('S');

			mnArchivo.add(mniNuevo);
			mnArchivo.addSeparator();
			mnArchivo.add(mniAbrir);
			mnArchivo.add(mniGuardar);
			mnArchivo.addSeparator();
			mnArchivo.add(mniImprimir);
			mnArchivo.addSeparator();
			mnArchivo.add(mniSalir);

			mbBarra.add(mnArchivo);

			mnDibujar = new JMenu("Dibujar");
			mnDibujar.setMnemonic('D');

			mniLinea = new JMenuItem("Línea");
			mniLinea.setMnemonic('L');

			mniRectangulo = new JMenuItem("Rectángulo");
			mniRectangulo.setMnemonic('R');

			mniElipse = new JMenuItem("Elipse");
			mniElipse.setMnemonic('E');

			mniRelleno = new JMenuItem("Relleno");
			mniRelleno.setMnemonic('n');
			mniRelleno.setEnabled(false);

			mniColor = new JMenuItem("Color...");
			mniColor.setMnemonic('C');

			mnDibujar.add(mniLinea);
			mnDibujar.add(mniRectangulo);
			mnDibujar.add(mniElipse);
			mnDibujar.addSeparator();
			mnDibujar.add(mniRelleno);
			mnDibujar.addSeparator();
			mnDibujar.add(mniColor);

			mbBarra.add(mnDibujar);

			mnAyuda = new JMenu("Ayuda");
			mnAyuda.setMnemonic('u');

			mniAcercaDe = new JMenuItem("Acerca de...");
			mniAcercaDe.setMnemonic('A');

			mnAyuda.add(mniAcercaDe);

			mbBarra.add(mnAyuda);

			miPanel = new MiPanel();
			add( miPanel );

			miToolBar = new MiToolBar("Seleccionar tipo de dibujo");
			add(miToolBar, BorderLayout.PAGE_START);
		}
	}

	class MiToolBar extends JToolBar
	{
		public MiToolBar(String titulo)
		{
			super( titulo );

			setAlignmentX(JToolBar.LEFT);

			add(new JLabel("Tipo de dibujo: "));

			cmbTipo = new JComboBox<>();
			cmbTipo.addItem("Línea");
			cmbTipo.addItem("Rectángulo");
			cmbTipo.addItem("Elipse");
			add(cmbTipo);

			chkRelleno = new JCheckBox("Relleno");
			chkRelleno.setEnabled(false);
			add(chkRelleno);

			btnColorChooser = new JButton("Color...");
			add(btnColorChooser);
		}
	}
	
	class MiPanel extends JPanel
	{
		public MiPanel()
		{
			setOpaque( false );
		}
		
		public void paintComponent( Graphics g )
		{
			super.paintComponent(g);
			// Convertir de Graphics a Graphics2D
			Graphics2D g2D = ( Graphics2D ) g;
			
			for ( Modelo.Figura f : Modelo.figuras )
				f.dibujar(g2D);
			
		}
		public void print(){
			PrinterJob pj = PrinterJob.getPrinterJob();
			pj.setJobName(" Programa de Dibujo ");

			pj.setPrintable (new Printable() {
				public int print(Graphics pg, PageFormat pf, int pageNum){
					if (pageNum > 0){
						return Printable.NO_SUCH_PAGE;
					}

					Graphics2D g2 = (Graphics2D) pg;
					g2.translate(pf.getImageableX(), pf.getImageableY());
					double factorEscalaX = 100.0;
					double factorEscalaY = 100.0;
					g2.scale(factorEscalaX/pf.getImageableWidth(), factorEscalaY/pf.getImageableHeight());
					paint(g2);
					return Printable.PAGE_EXISTS;
				}
			});
			if (pj.printDialog() == false)
				return;

			try {
				pj.print();
			} catch (PrinterException ex) {
				// handle exception
			}
		}
	}
}













