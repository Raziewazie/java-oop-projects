

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Geo extends JFrame {
    private JPanel drawingPanel;
    private Shape[] shapes;
    private Shape selectedShape;
    private int offsetX, offsetY;

    public Geo() {
		// Skapar en fönster till programmet
        setTitle("Geo");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
		
		
		// Här inne ska formerna ritas
        drawingPanel = new DrawingPanel();
        add(drawingPanel, BorderLayout.CENTER);
		
		
		// Arrayen som innehåller de tre former samt startkoordinater och storleken på formen.
        shapes = new Shape[3];
        shapes[0] = new Triangle(50, 50, 100, 100); // 50, 50  är koordinater
        shapes[1] = new Square(200, 200, 100); // 100 (px) är storleken på sidolängden
        shapes[2] = new Circle(350, 350, 50); // 50 (px) är radien
		
		
		// Mouse listener som möjliggör förflyttning av formerna
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                for (int i = shapes.length - 1; i >= 0; i--) {
                    if (shapes[i].contains(e.getX(), e.getY())) {
                        selectedShape = shapes[i];
                        offsetX = e.getX() - selectedShape.getX();
                        offsetY = e.getY() - selectedShape.getY();
                        // Den formen som klickas skickas till sista positionen i arrayen
                        for (int j = i; j < shapes.length - 1; j++) {
                            shapes[j] = shapes[j + 1];
                        }
                        shapes[shapes.length - 1] = selectedShape;
                        break;
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                selectedShape = null;
            }
        });

		
		// Mouse motion listener till aktuella förflyttningen av formerna
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                if (selectedShape != null) {
                    selectedShape.move(e.getX() - offsetX, e.getY() - offsetY);
                    repaint();
                }
            }
        });
    }


	// Klassen ansvarig för ritning av formerna
    private class DrawingPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            for (Shape shape : shapes) {
                shape.draw(g);
            }
        }
    }
	
	
	// Interface som tillhör formerna
    private interface Shape {
        void draw(Graphics g);
        boolean contains(int x, int y);
        void move(int x, int y);
        int getX();
        int getY();
    }
	
	
	// Implementation av triangeln
	private class Triangle implements Shape {
		private int x, y, width, height;
		

		public Triangle(int x, int y, int width, int height) {
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
			
		}

		
		// Tanken var att göra triangeln likbent men inte liksidig som i instruktionerna
		@Override
		public void draw(Graphics g) {
			g.setColor(Color.BLUE);
			int[] xPoints = {x, x + (width / 2), x + width}; 
			int[] yPoints = {y + height, y, y + height}; 
			g.fillPolygon(xPoints, yPoints, 3);
		}
		
		
		// Hitboxen med finjusteringar så att man kan klicka var som helst på formen för att flytta den
		// Det gör processen mer intuitivt. Problemet var att om man ritade en identisk triangel under den som ritades var att de aldrig satt i linje med varandra.
		@Override
		public boolean contains(int x, int y) {
			// Beräknar exakta koordinater av hitboxen
			int[] hitboxX = {this.x + 5, this.x + (width / 2) + 8, this.x + width + 10}; 
			int[] hitboxY = {this.y + height + 33, this.y + 25, this.y + height + 33};

			// Här skapas en polygon som då representerar hitboxen
			Polygon hitbox = new Polygon(hitboxX, hitboxY, 3);

			// Kontroll om punkter (x, y) ligger inom hitboxen
			return hitbox.contains(x, y);
		}


		@Override
		public void move(int x, int y) {
			this.x = x;
			this.y = y;
		}
		

		@Override
		public int getX() {
			return x;
		}
		

		@Override
		public int getY() {
			return y;
		}
	}


	// Implementation av kvadraten
    private class Square implements Shape {
        private int x, y, size;
		

        public Square(int x, int y, int size) {
            this.x = x;
            this.y = y;
            this.size = size;
        }
		

        @Override
        public void draw(Graphics g) {
            g.setColor(Color.YELLOW);
            g.fillRect(x, y, size, size);
        }
		
		
		// Bygger en hitbox som passar ritade formen, dvs samma sak som med triangeln
        @Override
		public boolean contains(int x, int y) { 
		
		
			// Definiera hitboxens storlek som en förskjutning från rektangelns gränser
			int hitboxSq = 10; // Anpassa storleken efter behov, storleken är i pixlar
			

			// Beräkna hitboxens gränser
			int hitboxLeft = this.x - hitboxSq;
			int hitboxRight = this.x + this.size + hitboxSq;
			int hitboxTop = this.y - hitboxSq;
			int hitboxBottom = this.y + this.size + hitboxSq;
			
			
			// Finjustering av hitboxen
			hitboxBottom += 20;
			hitboxTop += 40;
			hitboxLeft += 17.5;
			hitboxRight -= 3;

			// Kontrollera om punkterna (x, y) ligger inom hitboxen, dvs kollisionsdetektering
			return x >= hitboxLeft && x <= hitboxRight && y >= hitboxTop && y <= hitboxBottom;
		}


        @Override
        public void move(int x, int y) {
            this.x = x;
            this.y = y;
        }


        @Override
        public int getX() {
            return x;
        }


        @Override
        public int getY() {
            return y;
        }
    } 


	// Implementation av cirkeln
    private class Circle implements Shape {
        private int x, y, radius;
		

        public Circle(int x, int y, int radius) {
            this.x = x;
            this.y = y;
            this.radius = radius;
        }


        @Override
        public void draw(Graphics g) {
            g.setColor(Color.RED);
            g.fillOval(x - radius, y - radius, 2 * radius, 2 * radius);
        }


		@Override
		public boolean contains(int x, int y) {
			// Beräkna avståndet mellan punkten (x, y) och cirkelns centrum (x, y)
			int distanceX = x - this.x;
			int distanceY = y - this.y;

			// Beräkna avståndet till punkten (x, y) genom att använda Pythagoras sats
			double distanceToCenter = Math.sqrt(distanceX * distanceX + distanceY * distanceY);

			// Om avståndet till punkten (x, y) är mindre än eller lika med cirkelns radie, ligger punkten inuti cirkeln
			return distanceToCenter <= radius * 1.43 // Ökar hitboxens storlek
			
				// Om vi tittar på mittpunkten på cirkeln, "this.y - 19" justerar hitboxens storlek längs y-axeln och ovanför mittpunkten.
				// "this.y + radius * 7" justerar hitboxens storlek längs y-axeln och under mittpunkten. OBS att originella radien är 50 px.
				&& y >= this.y - 19 && y <= this.y + radius * 7
				
				// "this.x - 40" ansvarar för hitboxen längs x-axeln till vänster om mittpunkten
				// Då är "this.x + 54" naturligtvis ansvarig för justering av hitboxen till höger om mittpunkten längs x-axeln
				&& x >= this.x - 40 && x <= this.x + 54; 
		}


        @Override
        public void move(int x, int y) {
            this.x = x;
            this.y = y;
        }


        @Override
        public int getX() {
            return x;
        }


        @Override
        public int getY() {
            return y;
        }


        public int getRadius() {
            return radius;
        }
    }

	// Main metod för start av programmet
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Geo geo = new Geo();
            geo.setVisible(true);
        });
    }
}
