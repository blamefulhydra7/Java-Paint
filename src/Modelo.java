import javax.swing.*;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.io.*;
import java.util.Vector;

// Modelo.java
// Componente Modelo del enfoque Modelo - Vista - Controlador
public class Modelo implements Serializable {

	enum Tipo {Linea, Rectangulo, Elipse };
	static Vector<Figura> figuras;
	private JFileChooser chooser;
	
	public Modelo()
	{
		figuras = new Vector<>();
	}
	
	class Figura implements Serializable
	{
		private Shape	figura;
		private	Color	color;
		private boolean	relleno;
		
		public Figura( Shape f, Color c, boolean r )
		{
			figura = f;
			color = c;
			relleno = r;
		}
		
		// Método para dibujar la figura
		public void dibujar( Graphics2D g2D )
		{
			g2D.setColor(color);
			if ( relleno )
				g2D.fill(figura);
			else
				g2D.draw(figura);
		}
	}

	public boolean guardar()
	{
		chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File("."));

		if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION)
		{
			try (FileOutputStream fos = new FileOutputStream(chooser.getSelectedFile() + ".data"))
			{
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				oos.writeObject(figuras);
			}
			catch (IOException e)
			{
				return false;
			}
			return true;
		}
		return false;
	}

	public boolean abrir()
	{
		chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File("."));

		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
		{
			try (FileInputStream fis = new FileInputStream(chooser.getSelectedFile()))
			{
				ObjectInputStream ois = new ObjectInputStream(fis);
				figuras = (Vector<Figura>) ois.readObject();
			}
			catch (Exception e)
			{
				return false;
			}
			return true;
		}
		return false;
	}
}
