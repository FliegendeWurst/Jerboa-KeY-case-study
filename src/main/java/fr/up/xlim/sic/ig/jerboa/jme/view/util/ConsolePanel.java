package fr.up.xlim.sic.ig.jerboa.jme.view.util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashSet;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.BevelBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import fr.up.xlim.sic.ig.jerboa.jme.model.JMEElement;
import fr.up.xlim.sic.ig.jerboa.jme.windowsmanager.DockablePanel;
import fr.up.xlim.sic.ig.jerboa.jme.windowsmanager.WindowContainerInterface;

public class ConsolePanel extends JPanel implements DockablePanel {

	private static final long serialVersionUID = 2527196416034825632L;
	private JTextPane editorPane;
	private JTextField input;
	private WindowContainerInterface windowContainerDialog;
	private int sizeX;
	private int sizeY;
	private int posX;
	private int posY;
	private boolean ismaximized;
	private Style defaut;
	private Style stylerr;
	private Style stylein;
	private StyledDocument document;

	private PrintStream oldOut;
	private PrintStream oldErr;
	private PrintStream consoleOut;
	private PrintStream consoleErr;

	private boolean autoScroll;

	private HashSet<ConsoleInputListener> listeners;
	private HashSet<ClosedListener> closedListeners;

	public ConsolePanel() {
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(500, 300));
		sizeX = 500;
		sizeY = 300;
		ismaximized = true;
		autoScroll = true;
		listeners = new HashSet<>();
		closedListeners = new HashSet<>();

		editorPane = new JTextPane();
		editorPane.setEditable(false);
		editorPane.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));

		JScrollPane scrollpane = new JScrollPane(editorPane);

		add(scrollpane, BorderLayout.CENTER);

		document = editorPane.getStyledDocument();

		defaut = editorPane.getStyle("default");
		stylerr = editorPane.addStyle("stylerr", defaut);
		StyleConstants.setForeground(stylerr, Color.RED);
		stylein = editorPane.addStyle("stylein", defaut);
		StyleConstants.setForeground(stylein, Color.GREEN);

		oldOut = System.out;
		oldErr = System.err;
		consoleOut = new PrintStream(new PrintToConsole(System.out, document, defaut));
		consoleErr = new PrintStream(new PrintToConsole(System.err, document, stylerr));

		JPanel panel_2 = new JPanel();
		add(panel_2, BorderLayout.NORTH);
		panel_2.setLayout(new BorderLayout(0, 0));

		JLabel lblConsole = new JLabel("Console:");
		panel_2.add(lblConsole, BorderLayout.WEST);

		JPanel panel_3 = new JPanel();
		add(panel_3, BorderLayout.SOUTH);
		panel_3.setLayout(new BorderLayout(0, 0));

		JLabel label = new JLabel("$ ");
		panel_3.add(label, BorderLayout.WEST);

		input = new JTextField();
		input.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER)
					valideEntry();
			}
		});
		label.setLabelFor(input);
		panel_3.add(input, BorderLayout.CENTER);
		input.setColumns(10);

		JButton btnEnter = new JButton("Enter");
		btnEnter.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				valideEntry();
			}
		});
		panel_3.add(btnEnter, BorderLayout.EAST);

	}

	class PrintToConsole extends OutputStream {
		private Style style;
		private Document document;
		private StringBuilder sb;
		private OutputStream oldout;

		public PrintToConsole(OutputStream out, Document document, Style style) {
			this.oldout = out;
			this.style = style;
			this.document = document;
			sb = new StringBuilder();
		}

		@Override
		public void write(int b) throws IOException {
			if (b == '\n') {
				try {
					sb.append((char) b);
					final int position = document.getEndPosition().getOffset();
					document.insertString(position-1, sb.toString(), style);
					if (autoScroll)
						editorPane.setCaretPosition(position);
					sb = new StringBuilder();
				} catch (BadLocationException e) {
					e.printStackTrace(oldErr);
				}
			} else
				sb.append((char) b); // on bufferise
			if (oldout != null)
				oldout.write(b);
		}

		@Override
		public void flush() throws IOException {
			try {
				final int position = document.getEndPosition().getOffset();
				document.insertString(position, sb.toString(), style);
				if (autoScroll)
					editorPane.setCaretPosition(position - 1);
				sb = new StringBuilder();
			} catch (BadLocationException e) {
				e.printStackTrace(oldErr);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (oldout != null)
				oldout.flush();

		}
	}

	public PrintStream getOut() {
		return consoleOut;
	}

	public PrintStream getErr() {
		return consoleErr;
	}

	protected void valideEntry() {
		for (ConsoleInputListener listener : listeners) {
			listener.inputCommand(input.getText());
		}
		String msg = input.getText() + "\n";
		input.setText("");
		final int position = document.getEndPosition().getOffset();
		try {
			document.insertString(position, msg, stylein);
		} catch (BadLocationException e) {
			e.printStackTrace(oldErr);
		}
		if (autoScroll)
			editorPane.setCaretPosition(position);
	}

	@Override
	public void reload() {
	}

	@Override
	public void unlink() {

	}

	@Override
	public Component getRootComponent() {
		return this;
	}

	@Override
	public String getTitle() {
		return "Console";
	}

	@Override
	public void setWindowContainer(WindowContainerInterface windowContainerDialog) {
		this.windowContainerDialog = windowContainerDialog;
	}

	@Override
	public WindowContainerInterface getWindowContainer() {
		return windowContainerDialog;
	}

	@Override
	public int getSizeX() {
		return sizeX;
	}

	@Override
	public int getSizeY() {
		return sizeY;
	}

	@Override
	public boolean isMaximized() {
		return ismaximized;
	}

	@Override
	public void OnResize(int width, int height) {
		this.sizeX = width;
		this.sizeY = height;
	}

	@Override
	public int getPosX() {
		return posX;
	}

	@Override
	public int getPosY() {
		return posY;
	}

	@Override
	public void OnMove(int x, int y) {
		posX = x;
		posY = y;
	}

	@Override
	public void OnMaximize() {
		ismaximized = true;
	}

	@Override
	public void OnUnMaximize() {
		ismaximized = true;
	}

	public void addConsoleInputListener(ConsoleInputListener listener) {
		this.listeners.add(listener);
	}

	public void removeConsoleInputListener(ConsoleInputListener listener) {
		this.listeners.remove(listener);
	}

	@Override
	public void OnClose() {
		for (ClosedListener closedListener : closedListeners) {
			closedListener.closeActionPerformed(this);
		}
	}

	public void addClosedListener(ClosedListener listener) {
		closedListeners.add(listener);
	}

	public void removeClosedListener(ClosedListener listener) {
		closedListeners.remove(listener);
	}

	@Override
	public void check() {
		System.out.println("Rien a checker si vous voyez ce message ce n'est pas normal: 0x324645162");
	}

	@Override
	public void reloadTitle() {
		
	}

	@Override
	public void OnFocus(boolean temporary) {
		
	}

	@Override
	public void OnFocusLost(boolean temporary) {
		
	}

	@Override
	public JMEElement getSourceElement() {
		System.err.println("Normalement, ce message n'est pas normal, contactez les dev SVP avec le code: 0x8746321");
		return null;
	}
}
