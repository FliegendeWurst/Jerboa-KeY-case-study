package fr.up.xlim.sic.ig.jerboa.jme.model.util.preferences;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import fr.up.xlim.sic.ig.jerboa.jme.forms.JerboaModelerEditor;
import net.miginfocom.swing.MigLayout;



public abstract class JMEUIPrefPrototype extends MouseAdapter {

	private ActionPanel[] aps;
	private JPanel[] panels;

	private ArrayList<Field> fields = new ArrayList<>();
	private ArrayList<String> groups = new ArrayList<>();


	protected JMEUIPrefPrototype() {

	}

	private void prepareGroupsAndFields() {
		fields = new ArrayList<>();
		groups = new ArrayList<>();
		for (Field field : getClass().getDeclaredFields()) {
			if(field.isAnnotationPresent(JMEUIPrefItem.class)){
				JMEUIPrefItem item = field.getAnnotation(JMEUIPrefItem.class);
				if(!item.noModification())
					fields.add(field);
			}
		}

		TreeSet<String> set = new TreeSet<>();
		for (Field field : fields) {
			JMEUIPrefItem item = field.getAnnotation(JMEUIPrefItem.class);
			set.add(item.group());
		}
		groups.addAll(set);
	}

	public void save(PrintStream out) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		out.println("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\" ?>");
		out.println("<jmepreference>");
		prepareGroupsAndFields();
		for (Field field : fields) {
			JMEUIPrefItem item = field.getAnnotation(JMEUIPrefItem.class);
			StringBuilder sb = new StringBuilder();
			Method getter;
			getter = getClass().getDeclaredMethod("get"+fieldToMethod(field));
			sb.append("<field name=\"").append(field.getName()).append("\" group=\"").append(item.group()).append("\" subgroup=\"").append(item.subGroup()).append("\">");
			boolean isok = serializeField(sb, field, item, getter);
			sb.append("</field>");

			if(isok) {
				out.println(sb.toString());
			}
		}
		out.println("</jmepreference>");
		out.close();
	}

	private boolean serializeField(StringBuilder sb, Field field, JMEUIPrefItem item, Method getter) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Class<?> type = field.getType();
		if(type == boolean.class || type == int.class || type == double.class || type == long.class) {
			sb.append(getter.invoke(this));
			return true;
		}
		else if(type == Color.class) {
			Color c = (Color)getter.invoke(this);
			sb.append(c.getRGB());
			return true;
		}
		else if(type == String.class) {
			String c = (String)getter.invoke(this);
			sb.append(c);
			return true;
		}

		return false;
	}

	public void load(Document docXML) {
		prepareGroupsAndFields();
		final Element root = docXML.getDocumentElement();
		NodeList nl = root.getChildNodes();
		final int max = nl.getLength();
		for(int i = 0;i < max; i++) {
			Node n = nl.item(i);
			if(n instanceof Element) {
				Element e = (Element)n;
				if("field".equals(e.getNodeName())) {
					String name = e.getAttribute("name");
					String group = e.getAttribute("group");
					String subgroup = e.getAttribute("subgroup");
					String content = e.getTextContent();
					unserializeField(name, group, subgroup, content);
				}
			}
		}
	}


	private void unserializeField(String name, String group, String subgroup, String content) {
		Field field = searchField(name, group, subgroup);
		if(field == null)
			return; // on laisse tomber
		Class<?> type = field.getType();
		/// JMEUIPrefItem item = field.getAnnotation(JMEUIPrefItem.class);
		try {
			if(type == boolean.class) {
				Method setter = getClass().getMethod("set"+fieldToMethod(field), type);
				setter.invoke(this, Boolean.parseBoolean(content));
			}
			else if(type == int.class) {
				Method setter = getClass().getMethod("set"+fieldToMethod(field), type);
				setter.invoke(this, Integer.parseInt(content));
			}
			else if(type == long.class) {
				Method setter = getClass().getMethod("set"+fieldToMethod(field), type);
				setter.invoke(this, Long.parseLong(content));
			}
			else if(type == double.class) {
				Method setter = getClass().getMethod("set"+fieldToMethod(field), type);
				setter.invoke(this, Double.parseDouble(content));
			}
			else if(type == String.class) {
				Method setter = getClass().getMethod("set"+fieldToMethod(field), type);
				setter.invoke(this, content);
			}
			else if(type == Color.class) {
				Method setter = getClass().getMethod("set"+fieldToMethod(field), type);
				int rgb = Integer.parseInt(content);
				setter.invoke(this, new Color(rgb));
			}
		}
		catch(Exception e) {

		}
	}

	private Field searchField(String name, String group, String subgroup) {
		for (Field field : fields) {
			JMEUIPrefItem item = field.getAnnotation(JMEUIPrefItem.class);
			if(field.getName().equals(name) && item.group().equals(group) && item.subGroup().equals(subgroup)) {
				return field;
			}
		}
		return null;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		ActionPanel ap = (ActionPanel) e.getSource();
		if (ap.target.contains(e.getPoint())) {
			ap.toggleSelection();
			togglePanelVisibility(ap);
		}
	}

	private void togglePanelVisibility(ActionPanel ap) {
		int index = getPanelIndex(ap);
		if (panels[index].isShowing()) {
			panels[index].setVisible(false);
		} else {
			panels[index].setVisible(true);
		}
		ap.getParent().validate();
	}

	private int getPanelIndex(ActionPanel ap) {
		for (int j = 0; j < aps.length; j++) {
			if (ap == aps[j]) {
				return j;
			}
		}
		return -1;
	}

	private void assembleActionPanels(List<String> groups) {
		aps = new ActionPanel[groups.size()];
		panels = new JPanel[groups.size()];

		for (int j = 0; j < aps.length; j++) {
			aps[j] = new ActionPanel(groups.get(j), this);
		}
	}


	private JPanel getComponent() {
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(1, 3, 0, 3);
		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		for (int j = 0; j < aps.length; j++) {
			panel.add(aps[j], gbc);
			panel.add(panels[j], gbc);
			panels[j].setVisible(false);
		}
		JLabel padding = new JLabel();
		gbc.weighty = 1.0;
		panel.add(padding, gbc);
		return panel;
	}



	public void display(JerboaModelerEditor parent, String title) {
		aps = null;
		panels = null;
		JDialog f = new JDialog(parent.getWindow(), title, ModalityType.MODELESS);
		// f.setLocationRelativeTo(parent);
		prepareGroupsAndFields();
		assembleActionPanels(groups);
		preparePanelsPerGroup();
		f.getContentPane().add(new JScrollPane(getComponent()));
		f.setSize(600, 600);
		f.setModal(true);
		f.setVisible(true);
		parent.reload();
		save();
	}


	private void preparePanelsPerGroup() {
		for(int i = 0;i < groups.size(); i++) {
			JPanel panel = preparePanelForGroup(groups.get(i), i);
			panels[i] = panel;
		}
	}

	private JPanel preparePanelForGroup(String group, int i) {
		List<Field> fieldOfGroup = filterFields(group);
		HashMap<String, List<Field>> map = new HashMap<>();
		for (Field field : fieldOfGroup) {
			JMEUIPrefItem item = field.getAnnotation(JMEUIPrefItem.class);
			String subgroup  = item.subGroup();
			if(map.containsKey(subgroup)) {
				map.get(subgroup).add(field);
			}
			else {
				ArrayList<Field> lfield = new ArrayList<>();
				lfield.add(field);
				map.put(subgroup, lfield);
			}
		}
		JPanel rootGroup = new JPanel(new GridLayout(0, 1));
		// rootGroup.setBorder(BorderFactory.createTitledBorder(group) );

		for (Entry<String, List<Field>> entry : map.entrySet()) {
			try {
				makeSubGroupPanel(rootGroup, entry.getKey(), entry.getValue());
			} catch (IllegalArgumentException|IllegalAccessException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return rootGroup;
	}

	private void makeSubGroupPanel(JPanel rootGroup, String subgroup, List<Field> fields) throws IllegalArgumentException, IllegalAccessException, NoSuchMethodException, SecurityException, InvocationTargetException {
		JPanel rootSubGroup = new JPanel(new BorderLayout());
		rootSubGroup.setBorder(BorderFactory.createTitledBorder(subgroup));
		rootSubGroup.setLayout(new GridLayout(0, 1));;
		rootGroup.add(rootSubGroup);
		for (Field field : fields) {
			JMEUIPrefItem item = field.getAnnotation(JMEUIPrefItem.class);
			Method getter = getClass().getDeclaredMethod("get"+fieldToMethod(field));
			Method setter;
			try {
				setter = getClass().getMethod("set"+fieldToMethod(field), getter.getReturnType());
			}
			catch(NoSuchMethodError|NoSuchMethodException nsme) { setter = null; }
			rootSubGroup.add(makePanelForField(getter,setter, field, item));
		}
	}

	private Component makePanelForField(Method getter, Method setter, Field field, JMEUIPrefItem item) throws IllegalArgumentException, IllegalAccessException, NoSuchMethodException, SecurityException, InvocationTargetException {
		Class<?> type = field.getType();

		if(setter == null || item.noModification()) {
			JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
			panel.add(new JLabel(item.name()));
			panel.add(new JLabel(getter.invoke(this).toString()));
			panel.add(new JLabel(" (read-only)"));
			if(!item.desc().isEmpty())
				panel.setToolTipText(item.desc());
			return panel;
		}

		if(type == boolean.class) {
			JCheckBox res = new JCheckBox(item.name());
			if(!item.desc().isEmpty())
				res.setToolTipText(item.desc());
			res.setSelected((Boolean)getter.invoke(this));
			res.addActionListener( new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						boolean answer = res.isSelected();
						setter.invoke(JMEUIPrefPrototype.this, answer);
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
						e1.printStackTrace();
					}
				}
			});
			return res;
		}
		else if(type == int.class || type == long.class) { 
			JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
			JSpinner res = new JSpinner(new SpinnerNumberModel((Number) ((Number)getter.invoke(this)).intValue(), new Integer(item.min()) , new Integer(item.max()), new Integer(1)));
			JLabel label = new JLabel(item.name());
			label.setLabelFor(res);
			panel.add(label);
			panel.add(res);
			if(!item.desc().isEmpty()) {
				res.setToolTipText(item.desc());
				label.setToolTipText(item.desc());
				panel.setToolTipText(item.desc());
			}
			res.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					try {
						setter.invoke(JMEUIPrefPrototype.this, res.getValue());
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
						e1.printStackTrace();
					}
				}
			});
			return panel;
		}
		else if(type == double.class) { 
			JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
			JSpinner res = new JSpinner(new SpinnerNumberModel((Number) ((Number)getter.invoke(this)).doubleValue(), new Double(item.dmin()) , new Double(item.dmax()), new Double(item.dstep())));
			JLabel label = new JLabel(item.name());
			label.setLabelFor(res);
			panel.add(label);
			panel.add(res);
			if(!item.desc().isEmpty()) {
				res.setToolTipText(item.desc());
				label.setToolTipText(item.desc());
				panel.setToolTipText(item.desc());
			}
			res.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					try {
						setter.invoke(JMEUIPrefPrototype.this, res.getValue());
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
						e1.printStackTrace();
					}
				}
			});
			return panel;
		}
		else if(type == String.class) {
			JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
			JTextField textfield = new JTextField(item.size());
			textfield.setText((String)getter.invoke(this));
			JButton fix = new JButton("set"); 
			JLabel label = new JLabel(item.name());
			label.setLabelFor(textfield);
			panel.add(label);
			panel.add(textfield);
			panel.add(fix);
			if(!item.desc().isEmpty()) {
				textfield.setToolTipText(item.desc());
				label.setToolTipText(item.desc());
				panel.setToolTipText(item.desc());
			}
			ActionListener al = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						setter.invoke(JMEUIPrefPrototype.this, textfield.getText());
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
						e1.printStackTrace();
					}
				}
			};
			textfield.addActionListener(al);
			fix.addActionListener(al);
			return panel;
		}
		else if(type == Color.class) {
			Color curcol = (Color)getter.invoke(this);
			JLabel label = new JLabel(item.name());

			JSpinner spinnerR = new JSpinner(new SpinnerNumberModel(curcol.getRed(), 0, 255, 1));
			JSpinner spinnerG = new JSpinner(new SpinnerNumberModel(curcol.getGreen(), 0, 255, 1));
			JSpinner spinnerB = new JSpinner(new SpinnerNumberModel(curcol.getBlue(), 0, 255, 1));

			JPanel panelColor = new JPanel();
			panelColor.setBorder(new BevelBorder(BevelBorder.RAISED));
			panelColor.setLayout(new MigLayout("", "[grow,fill]", "[]"));
			panelColor.setBackground(new Color((Integer)spinnerR.getValue(), (Integer)spinnerG.getValue(), (Integer)spinnerB.getValue()));
			Component rigidArea1 = Box.createRigidArea(new Dimension(20, 10));
			rigidArea1.setMaximumSize(new Dimension(60, 10));
			rigidArea1.setPreferredSize(new Dimension(60, 10));
			rigidArea1.setMinimumSize(new Dimension(60, 10));
			panelColor.add(rigidArea1);

			ChangeListener cl = new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					panelColor.setBackground(new Color((Integer)spinnerR.getValue(), (Integer)spinnerG.getValue(), (Integer)spinnerB.getValue()));
					try {
						setter.invoke(JMEUIPrefPrototype.this, panelColor.getBackground());
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
						e1.printStackTrace();
					}
				}
			};
			spinnerR.addChangeListener(cl);
			spinnerG.addChangeListener(cl);
			spinnerB.addChangeListener(cl);

			JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
			panel.add(label);
			panel.add(spinnerR);
			panel.add(spinnerG);
			panel.add(spinnerB);
			panel.add(panelColor, "cell 0 0,growy");

			if(!item.desc().isEmpty()) {
				label.setToolTipText(item.desc());
				panel.setToolTipText(item.desc());
				spinnerR.setToolTipText(item.desc());
				spinnerG.setToolTipText(item.desc());
				spinnerB.setToolTipText(item.desc());
			}
			return panel;
		}
		else {
			JLabel label = new JLabel(item.name() + "   Unsupported: "+type);
			label.setToolTipText(item.desc());
			return label;
		}
	}

	private String fieldToMethod(Field field) {
		String name = field.getName();
		String res = name.substring(0,1).toUpperCase() + name.substring(1);
		return res;
	}

	private List<Field> filterFields(String group) {
		ArrayList<Field> res = new ArrayList<>();
		for (Field field : fields) {
			JMEUIPrefItem item = field.getAnnotation(JMEUIPrefItem.class);
			if(group.equalsIgnoreCase(item.group()))
				res.add(field);
		}
		return res;
	}


	// ---------------------------------------------------------
	/**
	 * @return {@link String} the default directory for preferences file. The
	 *         return is difference in terms of the Operating System of the
	 *         computer. Only MacOS, Linux and Windows are supported.
	 */
	private static String defaultDirectory() {
		String OS = System.getProperty("os.name").toUpperCase();
		if (OS.contains("WIN"))
			return System.getenv("APPDATA") + File.separatorChar;
		else if (OS.contains("MAC"))
			return System.getProperty("user.home") + "/Library/Application "
			+ "Support" + File.separatorChar;
		else if (OS.contains("NUX"))
			return System.getProperty("user.home") + File.separatorChar + ".";
		return System.getProperty("user.dir") + File.separatorChar;
	}

	public String getPreferenceDir() {
		String directory = defaultDirectory() + "JerboaModelerEditor";
		return directory;
	}

	public String getPreferenceFile() {
		return getPreferenceDir() + File.separatorChar + "jmepreferences.option";
	}
	
	public void save() {
		File dir = new File(getPreferenceDir());
		dir.mkdir();

		// if the temp directory is not set it create it
		if (!dir.exists()) {
			try {
				dir.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try(FileOutputStream file = new FileOutputStream(getPreferenceFile())) {
			save(new PrintStream(file));
			// savePreference(new PrintStream(file));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void load() {
		File dir = new File(getPreferenceDir());
		dir.mkdir();

		// if the temp directory is not set it create it
		if (dir.exists()) {
			try (FileInputStream file = new FileInputStream(getPreferenceFile());){
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(file);

				System.out.println("Read preference file : " + getPreferenceFile());
				load(document);
			} catch (final ParserConfigurationException e) {
				e.printStackTrace();
			} catch (final FileNotFoundException e) {
				System.err.println("No preferences found");
				// e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}


class ActionPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private String text;
	private Font font;
	private boolean selected;
	private BufferedImage open, closed;
	public Rectangle target;
	final int OFFSET = 30, PAD = 5;

	ActionPanel(String text, MouseListener ml) {
		this.text = text;
		addMouseListener(ml);
		font = new Font("sans-serif", Font.PLAIN, 12);
		selected = false;
		setBackground(new Color(200, 200, 220));
		setPreferredSize(new Dimension(200, 20));
		setBorder(BorderFactory.createRaisedBevelBorder());
		setPreferredSize(new Dimension(200, 20));
		createImages();
		setRequestFocusEnabled(true);
	}

	public void toggleSelection() {
		selected = !selected;
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		// int w = getWidth();
		int h = getHeight();
		if (selected) {
			g2.drawImage(open, PAD, 0, this);
		} else {
			g2.drawImage(closed, PAD, 0, this);
		}
		g2.setFont(font);
		FontRenderContext frc = g2.getFontRenderContext();
		LineMetrics lm = font.getLineMetrics(text, frc);
		float height = lm.getAscent() + lm.getDescent();
		float x = OFFSET;
		float y = (h + height) / 2 - lm.getDescent();
		g2.drawString(text, x, y);
	}

	private void createImages() {
		int w = 20;
		int h = getPreferredSize().height;
		target = new Rectangle(2, 0, 20, 18);
		open = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = open.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setPaint(getBackground());
		g2.fillRect(0, 0, w, h);
		int[] x = { 2, w / 2, 18 };
		int[] y = { 4, 15, 4 };
		Polygon p = new Polygon(x, y, 3);
		g2.setPaint(Color.green.brighter());
		g2.fill(p);
		g2.setPaint(Color.blue.brighter());
		g2.draw(p);
		g2.dispose();
		closed = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		g2 = closed.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setPaint(getBackground());
		g2.fillRect(0, 0, w, h);
		x = new int[] { 3, 13, 3 };
		y = new int[] { 4, h / 2, 16 };
		p = new Polygon(x, y, 3);
		g2.setPaint(Color.red);
		g2.fill(p);
		g2.setPaint(Color.blue.brighter());
		g2.draw(p);
		g2.dispose();
	}
}