import javax.swing.JFrame;

public class ClientMain {
	public static void main(String[] args) {
		ClientFrame frame = new ClientFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
}