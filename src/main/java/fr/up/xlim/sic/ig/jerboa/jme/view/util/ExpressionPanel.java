package fr.up.xlim.sic.ig.jerboa.jme.view.util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import javax.swing.undo.UndoManager;

import fr.up.xlim.sic.ig.jerboa.jme.export.JerboaLanguageGlue;
import fr.up.xlim.sic.ig.jerboa.jme.forms.JerboaModelerEditor;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEEmbeddingInfo;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEModeler;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMENodeExpression;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMERule;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.generator.Translator;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.verification.JSError;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.verification.JSErrorEnumType;
import fr.up.xlim.sic.ig.jerboa.jme.verif.JMEError;
import fr.up.xlim.sic.ig.jerboa.jme.view.JMEElementView;
import fr.up.xlim.sic.ig.jerboa.jme.view.ruletree.RuleTreeViewerRenderer;
import up.jerboa.core.util.Pair;

/**
 *
 * @author Valentin
 *
 *         TODO: Faire une coloration sur les paramï¿½tre quand ils sont
 *         appellï¿½ : \@rule<blabla>(Parametre => valeur) il faudrait
 *         vï¿½rifier qu'il existent dans la rï¿½gle courante
 */
public class ExpressionPanel extends JPanel implements KeyListener, MouseWheelListener, CaretListener, MouseListener {
	/**
	 *
	 */
	private static final long serialVersionUID = -6471405044797773268L;

	private enum documentState {
		Transient, Normal
	};

	private documentState state;

	protected UndoManager undoManager;

	protected Collection<String> autoCompleteFinalWords;

	private int fontSize = 16;
	private int fontSizeDelta = 0;

	private JerboaLanguageGlue languageGlue;

	private AutoCompletePanel autoCompletePanel;

	private javax.swing.text.Style normal;
	private javax.swing.text.Style lineStyle;
	private javax.swing.text.Style infoStyle;
	private javax.swing.text.Style warningStyle;
	private javax.swing.text.Style groupStyle;

	private javax.swing.text.Style errorStyle;

	private ArrayList<javax.swing.text.Style> stylesList;

	private ArrayList<Pair<Character, Character>> charGroupToApair;

	private ArrayList<String> words;
	private ArrayList<javax.swing.text.Style> styles;
	private ArrayList<Boolean> removeFirstLast;

	private ArrayList<String> wordsModeler;
	private ArrayList<javax.swing.text.Style> stylesModeler;
	private ArrayList<Boolean> removeFirstLastModeler;

	private StyledDocument document;
	private StyledDocument documentLines;
	private StyleContext sc;
	private JScrollPane scrolP;

	private JTextPane content;
	private JPanel lineCount;
	private JLabel posCaret;

	private ArrayList<JSError> errorList;

	private JMEModeler modeler;

	private FindAndReplacePanel findRepPanel;

	private int caretPosIndex;

	private ExpressionTraductionViewer expTradView = null;

	private Character previousChar;
	private Point previousCharPos;

	private Style currentLineStyle;

	public ExpressionPanel(String _exprName, JMEModeler _modeler, JerboaLanguageGlue glue) {
		super();
		state = documentState.Normal;
		fontSizeDelta = 0;
		languageGlue = glue;
		modeler = _modeler;
		undoManager = new UndoManager();
		findRepPanel = new FindAndReplacePanel(this);

		previousChar = null;
		previousCharPos = new Point(0,0);
		autoCompleteFinalWords = new ArrayList<>();

		stylesList = new ArrayList<Style>();
		errorList = new ArrayList<JSError>();

		charGroupToApair = new ArrayList<>();
		words = new ArrayList<String>();
		styles = new ArrayList<javax.swing.text.Style>();
		removeFirstLast = new ArrayList<Boolean>();

		wordsModeler = new ArrayList<String>();
		stylesModeler = new ArrayList<javax.swing.text.Style>();
		removeFirstLastModeler = new ArrayList<Boolean>();

		sc = new StyleContext();

		document = new DefaultStyledDocument(sc);
		documentLines = new DefaultStyledDocument(sc);

		content = new JTextPane(document);
		autoCompletePanel = new AutoCompletePanel(content);
		initSyntax();
		content.addKeyListener(this);

		// content.setPreferredSize(new Dimension(400, 200));
		// document.addUndoableEditListener(undoManager);
		document.addUndoableEditListener(new UndoableEditListener() {
			@Override
			public void undoableEditHappened(UndoableEditEvent e) {

				if (e.getEdit().isSignificant() && !e.getEdit().getPresentationName().contains("style")
						&& state == documentState.Normal) {
					undoManager.addEdit(e.getEdit());
				}
			}
		});
		content.addMouseWheelListener(this);
		content.addMouseListener(this);
		content.addCaretListener(this);
		content.setFont(new Font(getFont().getName(), getFont().getStyle(), fontSize));

		//
		// lineCount = new JTextPane(documentLines);
		// lineCount.addMouseWheelListener(this);
		// lineCount.setFocusable(false);
		// lineCount.setBackground(new Color(41, 49, 51));
		//
		// SimpleAttributeSet attribs = new SimpleAttributeSet();
		// StyleConstants.setAlignment(attribs, StyleConstants.ALIGN_RIGHT);
		// lineCount.setParagraphAttributes(attribs, true);

		lineCount = new JPanel();
		lineCount.setLayout(new GridBagLayout());

		normal = sc.addStyle("normal", null);
		normal.addAttribute(StyleConstants.FontSize, content.getFont().getSize());
		document.setLogicalStyle(0, normal);

		lineStyle = sc.addStyle("lineStyle", null);
		lineStyle.addAttribute(StyleConstants.FontSize, content.getFont().getSize());
		lineStyle.addAttribute(StyleConstants.Foreground, new Color(150, 150, 150));
		documentLines.setLogicalStyle(0, lineStyle);

		infoStyle = sc.addStyle("infoStyle", null);
		infoStyle.addAttribute(StyleConstants.FontSize, content.getFont().getSize());
		infoStyle.addAttribute(StyleConstants.Foreground, new Color(255, 255, 255));
		infoStyle.addAttribute(StyleConstants.Background, new Color(100, 100, 255));

		warningStyle = sc.addStyle("warningStyle", null);
		warningStyle.addAttribute(StyleConstants.FontSize, content.getFont().getSize());
		warningStyle.addAttribute(StyleConstants.Foreground, new Color(0, 0, 0));
		warningStyle.addAttribute(StyleConstants.Background, new Color(100, 255, 100));

		errorStyle = sc.addStyle("errorStyle", null);
		errorStyle.addAttribute(StyleConstants.FontSize, content.getFont().getSize());
		errorStyle.addAttribute(StyleConstants.Foreground, new Color(255, 255, 0));
		errorStyle.addAttribute(StyleConstants.Background, new Color(255, 100, 100));

		stylesList.add(normal);
		stylesList.add(lineStyle);

		content.setFont(document.getFont(normal));

		JPanel pan = new JPanel(new BorderLayout());
		pan.add(lineCount, BorderLayout.WEST);
		pan.add(content, BorderLayout.CENTER);
		pan.addMouseWheelListener(this);

		scrolP = new JScrollPane(pan);

		setLayout(new BorderLayout());
		add(scrolP, BorderLayout.CENTER);
		scrolP.getVerticalScrollBar().setAutoscrolls(false);

		JLabel labCaret = new JLabel("Position : ");
		labCaret.setEnabled(false);
		labCaret.setText("");
		Font fontCar = new Font(content.getFont().getName(), content.getFont().getStyle(), content.getFont().getSize());
		labCaret.setFont(fontCar);
		labCaret.setBackground(new Color(0, 0, 0, 0));
		labCaret.setForeground(new Color(0, 0, 0));

		posCaret = new JLabel();
		posCaret.setEnabled(false);

		posCaret = new JLabel("");
		posCaret.setEnabled(false);
		posCaret.setText("Position : ");
		posCaret.setFont(fontCar);
		posCaret.setBackground(new Color(0, 0, 0, 0));
		posCaret.setForeground(new Color(0, 0, 0));

		Box boxCaret = Box.createHorizontalBox();
		boxCaret.add(labCaret);
		boxCaret.add(posCaret);
		boxCaret.add(Box.createHorizontalGlue());

		final Icon zoomIn = new ImageIcon(new ImageIcon(RuleTreeViewerRenderer.class.getResource("/image/zoomIn.png"))
				.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
		JButton b_zoomIn = new JButton(zoomIn);
		b_zoomIn.setContentAreaFilled(false);
		b_zoomIn.setMinimumSize(new Dimension(20, 20));

		final Icon zoomDefault = new ImageIcon(
				new ImageIcon(RuleTreeViewerRenderer.class.getResource("/image/zoomDefault.png")).getImage()
				.getScaledInstance(20, 20, Image.SCALE_SMOOTH));
		JButton b_defaultZoom = new JButton(zoomDefault);
		b_defaultZoom.setMinimumSize(new Dimension(20, 20));
		b_defaultZoom.setContentAreaFilled(false);

		final Icon zoomOut = new ImageIcon(new ImageIcon(RuleTreeViewerRenderer.class.getResource("/image/zoomOut.png"))
				.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
		JButton b_zoomOut = new JButton(zoomOut);
		b_zoomOut.setContentAreaFilled(false);
		b_zoomOut.setMinimumSize(new Dimension(20, 20));

		b_zoomIn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				zoom(2);
			}
		});
		b_defaultZoom.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				zoom(-fontSizeDelta);
			}
		});
		b_zoomOut.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				zoom(-2);
			}
		});
		JButton butVerif = new JButton("Check");
		butVerif.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				verif();
			}
		});
		boxCaret.add(butVerif);
		boxCaret.add(Box.createHorizontalGlue());
		boxCaret.add(b_zoomOut);
		boxCaret.add(b_defaultZoom);
		boxCaret.add(b_zoomIn);

		JButton butPreviewTraduction = new JButton("Preview translation");
		butPreviewTraduction.setMaximumSize(new Dimension(50, 20));
		butPreviewTraduction.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				expTradView = new ExpressionTraductionViewer(content.getText(), languageGlue, modeler);
				expTradView.setVisible(true);
				// System.err.println("exprTrad Preview");
			}
		});
		boxCaret.add(butPreviewTraduction);

		zoom(0);

		add(boxCaret, BorderLayout.SOUTH);
		document.setLogicalStyle(0, normal);

	}

	@Override
	public void addKeyListener(KeyListener l) {
		content.addKeyListener(l);
	}
	
	
	public Document getDocument() {
		return content.getDocument();
	}

	/**
	 * TODO: inclure la possibilitÃ© par l'utilisateur de changer le style.
	 */
	private void initSyntax() {

		Style redStyle = content.addStyle("redStyle", null);
		redStyle.addAttribute(StyleConstants.FontSize, content.getFont().getSize());
		redStyle.addAttribute(StyleConstants.Foreground, new Color(220, 0, 0));
		redStyle.addAttribute(StyleConstants.Bold, true);
		stylesList.add(redStyle);

		Style blueStyle = content.addStyle("blueStyle", null);
		blueStyle.addAttribute(StyleConstants.FontSize, content.getFont().getSize());
		blueStyle.addAttribute(StyleConstants.Foreground, new Color(0, 0, 180));
		blueStyle.addAttribute(StyleConstants.Bold, true);
		stylesList.add(blueStyle);

		Style greenStyle = content.addStyle("greenStyle", null);
		greenStyle.addAttribute(StyleConstants.FontSize, content.getFont().getSize());
		greenStyle.addAttribute(StyleConstants.Foreground, new Color(0, 150, 0));
		stylesList.add(greenStyle);

		Style greenBOLDStyle = content.addStyle("greenBOLDStyle", null);
		greenBOLDStyle.addAttribute(StyleConstants.FontSize, content.getFont().getSize());
		greenBOLDStyle.addAttribute(StyleConstants.Foreground, new Color(0, 150, 0));
		greenBOLDStyle.addAttribute(StyleConstants.Bold, true);
		stylesList.add(greenBOLDStyle);

		Style skyBlue = content.addStyle("skyBlue", null);
		skyBlue.addAttribute(StyleConstants.FontSize, content.getFont().getSize());
		skyBlue.addAttribute(StyleConstants.Foreground, new Color(100, 100, 255));
		stylesList.add(skyBlue);

		Style lightGreen = content.addStyle("lightGreen", null);
		lightGreen.addAttribute(StyleConstants.FontSize, content.getFont().getSize());
		lightGreen.addAttribute(StyleConstants.Foreground, new Color(100, 255, 100));
		stylesList.add(lightGreen);

		Style orange = content.addStyle("orange", null);
		orange.addAttribute(StyleConstants.FontSize, content.getFont().getSize());
		orange.addAttribute(StyleConstants.Foreground, new Color(180, 90, 0));
		stylesList.add(orange);

		Style yellow = content.addStyle("yellow", null);
		yellow.addAttribute(StyleConstants.FontSize, content.getFont().getSize());
		yellow.addAttribute(StyleConstants.Foreground, new Color(150, 150, 0));
		stylesList.add(yellow);

		Style purple = content.addStyle("purple", null);
		purple.addAttribute(StyleConstants.FontSize, content.getFont().getSize());
		purple.addAttribute(StyleConstants.Foreground, new Color(76, 0, 153));
		stylesList.add(purple);

		Style purpleBOLD = content.addStyle("purpleBOLD", null);
		purpleBOLD.addAttribute(StyleConstants.FontSize, content.getFont().getSize());
		purpleBOLD.addAttribute(StyleConstants.Foreground, new Color(76, 0, 153));
		purpleBOLD.addAttribute(StyleConstants.Bold, true);
		stylesList.add(purpleBOLD);

		// type primitifs
		words.add("(?<![\\w\\d])int(?![\\w\\d])");
		styles.add(yellow);
		removeFirstLast.add(false);
		autoCompleteFinalWords.add("int");

		words.add("(?<![\\w\\d])[bB]ool(ean)?(?![\\w\\d])");
		styles.add(yellow);
		removeFirstLast.add(false);
		autoCompleteFinalWords.add("boolean");

		words.add("(?<![\\w\\d])float(?![\\w\\d])");
		styles.add(yellow);
		removeFirstLast.add(false);
		autoCompleteFinalWords.add("double");
		words.add("(?<![\\w\\d])double(?![\\w\\d])");
		styles.add(yellow);
		removeFirstLast.add(false);
		autoCompleteFinalWords.add("double");

		words.add("(?<![\\w\\d])[sS]tring(?![\\w\\d])");
		styles.add(yellow);
		removeFirstLast.add(false);
		autoCompleteFinalWords.add("string");
		words.add("(?<![\\w\\d])JerboaRuleResult(?![\\w\\d])");
		styles.add(orange);
		removeFirstLast.add(false);
		autoCompleteFinalWords.add("JerboaRuleResult");
		words.add("(?<![\\w\\d])JerboaDart(?![\\w\\d])");
		styles.add(orange);
		removeFirstLast.add(false);
		autoCompleteFinalWords.add("JerboaDart");
		words.add("(?<![\\w\\d])JerboaDarts(?![\\w\\d])");
		styles.add(orange);
		removeFirstLast.add(false);
		autoCompleteFinalWords.add("JerboaHookList");
		words.add("(?<![\\w\\d])JerboaHookList(?![\\w\\d])");
		styles.add(orange);
		removeFirstLast.add(false);
		autoCompleteFinalWords.add("JerboaHooks");
		words.add("(?<![\\w\\d])JerboaHooks(?![\\w\\d])");
		styles.add(orange);
		removeFirstLast.add(false);
		autoCompleteFinalWords.add("JerboaDarts");
		words.add("(?<![\\w\\d])JerboaRule(?![\\w\\d])");
		styles.add(orange);
		removeFirstLast.add(false);
		autoCompleteFinalWords.add("JerboaOrbit");
		words.add("(?<![\\w\\d])JerboaRule(?![\\w\\d])");
		styles.add(orange);
		removeFirstLast.add(false);
		autoCompleteFinalWords.add("JerboaRule");
		words.add("(?<![\\w\\d])JerboaMark(?![\\w\\d])");
		styles.add(orange);
		removeFirstLast.add(false);
		autoCompleteFinalWords.add("JerboaMark");
		words.add("(?<![\\w\\d])[jJ][eE][rR][bB][oO][aA][Ll][iI][sS][tT](?![\\w\\d])");
		styles.add(orange);
		removeFirstLast.add(false);
		autoCompleteFinalWords.add("JerboaList");

		// mots clefs
		words.add("(?<![\\w\\d])new(?![\\w\\d])");
		styles.add(blueStyle);
		removeFirstLast.add(false);
		words.add("(?<![\\w\\d])for(?![\\w\\d])");
		styles.add(blueStyle);
		removeFirstLast.add(false);
		words.add("(?<![\\w\\d])if(?![\\w\\d])");
		styles.add(blueStyle);
		removeFirstLast.add(false);
		words.add("(?<![\\w\\d])else(?![\\w\\d])");
		styles.add(blueStyle);
		removeFirstLast.add(false);
		words.add("(?<![\\w\\d])in(?![\\w\\d])");
		styles.add(blueStyle);
		removeFirstLast.add(false);
		words.add("(?<![\\w\\d])for(?![\\w\\d])");
		styles.add(blueStyle);
		removeFirstLast.add(false);
		words.add("(?<![\\w\\d])while(?![\\w\\d])");
		styles.add(blueStyle);
		removeFirstLast.add(false);
		words.add("(?<![\\w\\d])do(?![\\w\\d])");
		styles.add(blueStyle);
		removeFirstLast.add(false);
		words.add("(?<![\\w\\d])foreach(?![\\w\\d])");
		styles.add(blueStyle);
		removeFirstLast.add(false);
		words.add("(?<![\\w\\d])step(?![\\w\\d])");
		styles.add(blueStyle);
		removeFirstLast.add(false);
		words.add("(?<![\\w\\d])not(?![\\w\\d])");
		styles.add(blueStyle);
		removeFirstLast.add(false);
		autoCompleteFinalWords.add("not");
		words.add("(?<![\\.])\\.\\.(?![\\.])");
		styles.add(blueStyle);
		removeFirstLast.add(false);
		words.add("(?<![\\w\\d])return(?![\\w\\d])");
		styles.add(blueStyle);
		removeFirstLast.add(false);
		autoCompleteFinalWords.add("return");
		words.add("(?<![\\w\\d])delete(?![\\w\\d])");
		styles.add(blueStyle);
		removeFirstLast.add(false);
		autoCompleteFinalWords.add("delete");

		words.add("(?<![\\w\\d])try(?![\\w\\d])");
		styles.add(blueStyle);
		removeFirstLast.add(false);
		words.add("(?<![\\w\\d])catch(?![\\w\\d])");
		styles.add(blueStyle);
		removeFirstLast.add(false);
		words.add("(?<![\\w\\d])finally(?![\\w\\d])");
		styles.add(blueStyle);
		removeFirstLast.add(false);

		words.add("(?<![\\w\\d])[Bb][Rr][Ee][Aa][Kk](?![\\w\\d])");
		styles.add(purpleBOLD);
		removeFirstLast.add(false);
		autoCompleteFinalWords.add("break");

		words.add("(?<![\\w\\d])[Tt][Rr][Uu][Ee](?![\\w\\d])");
		styles.add(purpleBOLD);
		removeFirstLast.add(false);
		autoCompleteFinalWords.add("true");

		words.add("(?<![\\w\\d])[Ff][Aa][Ll][Ss][Ee](?![\\w\\d])");
		styles.add(purpleBOLD);
		removeFirstLast.add(false);
		autoCompleteFinalWords.add("false");

		words.add("&");
		styles.add(purple);
		removeFirstLast.add(false);

		words.add("#");
		styles.add(redStyle);
		removeFirstLast.add(false);
		words.add("@print");
		styles.add(redStyle);
		removeFirstLast.add(false);
		words.add("=>");
		styles.add(redStyle);
		removeFirstLast.add(false);

		// operateurs specifiques
		words.add("\\|");
		styles.add(blueStyle);
		removeFirstLast.add(false);

		words.add("\\&\\&");
		styles.add(blueStyle);
		removeFirstLast.add(false);

		words.add("(?<![\\w\\d])and(?![\\w\\d])");
		styles.add(blueStyle);
		removeFirstLast.add(false);

		words.add("(?<![\\w\\d])or(?![\\w\\d])");
		styles.add(blueStyle);
		removeFirstLast.add(false);

		words.add("@");
		styles.add(redStyle);
		removeFirstLast.add(false);

		words.add("@gmap");
		styles.add(redStyle);
		removeFirstLast.add(false);
		autoCompleteFinalWords.add("@gmap");

		words.add("@leftPattern");
		styles.add(redStyle);
		removeFirstLast.add(false);
		autoCompleteFinalWords.add("@leftPattern");

		words.add("@rule");
		styles.add(redStyle);
		removeFirstLast.add(false);
		autoCompleteFinalWords.add("@rule");

		words.add("@header");
		styles.add(greenBOLDStyle);
		removeFirstLast.add(false);
		autoCompleteFinalWords.add("@header");

		words.add("@lang");
		styles.add(greenBOLDStyle);
		removeFirstLast.add(false);
		autoCompleteFinalWords.add("@lang");

		words.add("@op");
		styles.add(redStyle);
		removeFirstLast.add(false);
		autoCompleteFinalWords.add("@op");

		words.add("@collect");
		styles.add(redStyle);
		autoCompleteFinalWords.add("@collect");
		removeFirstLast.add(false);

		words.add("@dimension");
		styles.add(redStyle);
		autoCompleteFinalWords.add("@dimension");
		removeFirstLast.add(false);

		words.add("@modeler");
		styles.add(redStyle);
		autoCompleteFinalWords.add("@modeler");
		removeFirstLast.add(false);

		words.add("@hook");
		styles.add(redStyle);
		removeFirstLast.add(false);

		words.add("@ebd");
		styles.add(redStyle);
		autoCompleteFinalWords.add("@ebd");
		removeFirstLast.add(false);

		words.add("@node");
		styles.add(redStyle);
		autoCompleteFinalWords.add("@node");
		removeFirstLast.add(false);

		words.add("@mark");
		styles.add(redStyle);
		autoCompleteFinalWords.add("@mark");
		removeFirstLast.add(false);

		words.add("@unmark");
		styles.add(redStyle);
		autoCompleteFinalWords.add("@unmark");
		removeFirstLast.add(false);

		words.add("@isNotMarked");
		styles.add(redStyle);
		autoCompleteFinalWords.add("@isNotMarked");
		removeFirstLast.add(false);

		words.add("@isMarked");
		styles.add(redStyle);
		autoCompleteFinalWords.add("@isMarked");
		removeFirstLast.add(false);

		words.add("\".+?\""); // chaÃ®ne de caracteres
		styles.add(skyBlue);
		removeFirstLast.add(false);

		// les commentaires
		words.add("//.*");
		styles.add(greenStyle);
		removeFirstLast.add(false);
		words.add("(?s)/\\*.+?\\*/");
		styles.add(greenStyle);
		removeFirstLast.add(false);

		autoCompletePanel.updateWords(autoCompleteFinalWords);


		// Les groupes

		groupStyle = content.addStyle("groupStyle", null);
		groupStyle.addAttribute(StyleConstants.FontSize, (int) (content.getFont().getSize()*1.5) );
		groupStyle.addAttribute(StyleConstants.Background, new Color(120, 250, 120));
		groupStyle.addAttribute(StyleConstants.Foreground, new Color(76, 0, 153));
		groupStyle.addAttribute(StyleConstants.Bold, true);

		charGroupToApair.add(new Pair<Character, Character>( '(' , ')' ));
		charGroupToApair.add(new Pair<Character, Character>( '{' , '}' ));
		charGroupToApair.add(new Pair<Character, Character>( '[' , ']' ));
		charGroupToApair.add(new Pair<Character, Character>( '<' , '>' ));


		// Les groupes
		currentLineStyle = content.addStyle("currentLineStyle", null);
		currentLineStyle.addAttribute(StyleConstants.Background, new Color(200, 250, 250));

	}

	/**
	 * Mets a jour la colorisation en fonction des noms des regles et des noms
	 * des plongements ainsi que l'autocompletion
	 */
	public void updateModelerDataColorization() {
		wordsModeler.clear();
		stylesModeler.clear();
		removeFirstLastModeler.clear();

		autoCompletePanel.updateWords(autoCompleteFinalWords);

		Style purple = content.addStyle("orange", null);
		purple.addAttribute(StyleConstants.FontSize, content.getFont().getSize());
		purple.addAttribute(StyleConstants.Foreground, new Color(127, 0, 255));

		Style lightGreen = content.addStyle("lightGreen", null);
		lightGreen.addAttribute(StyleConstants.FontSize, content.getFont().getSize());
		lightGreen.addAttribute(StyleConstants.Foreground, new Color(100, 150, 100));
		if(modeler!=null) {
			for (JMERule r : modeler.getRules()) {
				wordsModeler.add("<" + r.getName() + ">");
				stylesModeler.add(purple);
				removeFirstLastModeler.add(true);
				autoCompletePanel.addWord(r.getName());
			}

			for (JMEEmbeddingInfo ei : modeler.getEmbeddings()) {
				wordsModeler.add("_" + ei.getName() + "\\(");
				stylesModeler.add(lightGreen);
				removeFirstLastModeler.add(true);

				wordsModeler.add("." + ei.getName());
				stylesModeler.add(lightGreen);
				removeFirstLastModeler.add(false);

				wordsModeler.add("<" + ei.getName() + ">");
				stylesModeler.add(lightGreen);
				removeFirstLastModeler.add(true);

				autoCompletePanel.addWord(ei.getName());
			}
		}
	}

	private void syntaxeColorization() {			
		state = documentState.Transient;
		updateModelerDataColorization();
		updateLineCount();

		if (content.getText().length() <= 0)
			return;
		String str = content.getText();
		try {
			// Document d = content.getDocument();
			str = document.getText(0, document.getLength());
		} catch (BadLocationException e) {
			e.printStackTrace();
			return;
		}

		// on met tout le document en style normal
		try {
			document.setCharacterAttributes(0, str.length()-1, normal, true);
		}catch(java.lang.IllegalStateException e) {
			//e.printStackTrace();
			return;
		}




		// on change la couleur de tous les mots identifiÃ©s
		for (int i = 0; i < words.size(); i++) {
			Pattern pattern = Pattern.compile(words.get(i), Pattern.MULTILINE);
			Matcher matcher = pattern.matcher(str);
			while (matcher.find()) {
				int beg = matcher.start();
				int end = matcher.end() - matcher.start();
				if (removeFirstLast.get(i))
					document.setCharacterAttributes(beg + 1, end - 2, styles.get(i), true);
				else
					document.setCharacterAttributes(beg, end, styles.get(i), true);
			}
		}

		// Meme chose pour les informations de modeleur
		for (int i = 0; i < wordsModeler.size(); i++) {
			Pattern pattern = Pattern.compile(wordsModeler.get(i), Pattern.MULTILINE);
			Matcher matcher = pattern.matcher(str);
			while (matcher.find()) {
				int beg = matcher.start();
				int end = matcher.end() - matcher.start();
				if (removeFirstLastModeler.get(i))
					document.setCharacterAttributes(beg + 1, end - 2, stylesModeler.get(i), true);
				else
					document.setCharacterAttributes(beg, end, stylesModeler.get(i), true);
			}
		}

		// test if lastChar is a parenthesis or an brace
		if(previousChar != null) {
			//				System.err.println("PASSE " + previousChar + " - " + previousCharPos);
			for(Pair<Character,Character> group : charGroupToApair) {
				boolean searchForFirstChar = false;
				if( (searchForFirstChar = (previousChar.compareTo(group.l())==0))
						|| previousChar.compareTo(group.r())==0 ) {
					int charCount = 0;
					int lineCount = 1;
					int colCount = 0;
					int groupID = -1;
					ArrayList<Integer> listOfFirstCharPos = new ArrayList<>();

					Character lastChar = ' ';
					boolean isInLongCommentaryZone = false;
					boolean isInShortCommentaryZone = false;

					for (Character c : str.toCharArray()) {
						if(lastChar.compareTo('/') == 0 && c.compareTo('*') == 0) {
							isInLongCommentaryZone = true;
						}else if(lastChar.compareTo('*') == 0 && c.compareTo('/') == 0) {
							isInLongCommentaryZone = false;
						}else if(lastChar.compareTo('/') == 0 && c.compareTo('/') == 0) {
							isInShortCommentaryZone = true;
						}
						if(!isInLongCommentaryZone && !isInShortCommentaryZone) {
							if( c.compareTo(group.l()) == 0 || c.compareTo(group.r()) == 0 ) { // si on est sur le bon char
								if(lineCount==previousCharPos.getX() && colCount==previousCharPos.getY()) { // si on est sur ce qu'on recherche
									if (searchForFirstChar) {
										groupID = listOfFirstCharPos.size()+1;
									} else {
										if(listOfFirstCharPos.size() > 0) {
											document.setCharacterAttributes(listOfFirstCharPos.get(listOfFirstCharPos.size()-1), 1, groupStyle, true);
										}
										document.setCharacterAttributes(charCount, 1, groupStyle, true);
										break;
									}
								}else if(searchForFirstChar && listOfFirstCharPos.size() == groupID && c.compareTo(group.r()) == 0) {
									if(listOfFirstCharPos.size() > 0) {
										document.setCharacterAttributes(listOfFirstCharPos.get(listOfFirstCharPos.size()-1), 1, groupStyle, true);
									}else {
										System.err.println("ERROR");
									}
									document.setCharacterAttributes(charCount, 1, groupStyle, true);
									break;
								}
							}

							if( c.compareTo(group.l()) == 0 ) {
								listOfFirstCharPos.add(charCount);
							}else if( c.compareTo(group.r()) == 0 && listOfFirstCharPos.size() > 0 ) {
								listOfFirstCharPos.remove(listOfFirstCharPos.size()-1);
							}
						}

						charCount++;
						colCount++;
						if(c.compareTo('\n')==0) {
							lineCount++;
							colCount = 0;
							isInShortCommentaryZone = false;
						}
						lastChar = c;
					}
				}
			}
		}
		// FIN colorizeGroup

		updateCurrentLineColor();
		updateErrors();
		state = documentState.Normal;

	}

	public void updateCurrentLineColor() {
//		if (content.getText().length() <= 0)
//			return;
//		final String str = content.getText();
//		int lineCount = 1;
//		// 	coloration de la ligne selectionnée
//		for (int ci=0;ci<str.length();ci++) {
//			String cut = str.substring(ci, str.length());
//			int nextLinePos = cut.indexOf('\n');
//			if(nextLinePos==-1) { // si pas trouvé on met a la taille
//				nextLinePos = cut.length();
//			}
//			if(previousCharPos.getX()==lineCount) {
//				try {
//					document.setCharacterAttributes(ci, nextLinePos, currentLineStyle, true);
//				}catch(java.lang.IllegalStateException e) {
//					//e.printStackTrace();
//					return;
//				}
//			} else {
//				try {
//					document.setCharacterAttributes(ci, nextLinePos, normal, true);
//				}catch(java.lang.IllegalStateException e) {
//					//e.printStackTrace();
//					return;
//				}
//			}
//			ci += nextLinePos-1;
//			if(str.charAt(ci)=='\n') {
//				lineCount++;
//			}
//		}
	}

	private void updateLineCount() {
		int nbLine = (content.getText() + "a").split("\n").length;

		int colWidth[] = new int[nbLine];
		for (int i = 0; i < nbLine; i++) {
			colWidth[i] = 0;
		}

		int colHeight[] = new int[nbLine];
		for (int i = 0; i < nbLine; i++) {
			colHeight[i] = 0;
		}

		double colWidthD[] = new double[nbLine + 1];
		for (int i = 0; i < nbLine; i++) {
			colWidthD[i] = 0.0;
		}
		colWidthD[nbLine] = Double.MIN_VALUE;

		lineCount.removeAll();
		GridBagLayout gbl_panel = (GridBagLayout) lineCount.getLayout();
		gbl_panel.columnWidths = new int[] { 1, 0 };
		gbl_panel.rowHeights = colHeight;
		gbl_panel.columnWeights = new double[] { 0.0, -1.0 };
		gbl_panel.rowWeights = colWidthD;

		String nblineString = nbLine + "";

		for (int i = 1; i <= nbLine; i++) {
			// on prÃ©pare l'alignement des nombres sur la droite :
			String labContent = i + "";
			final int sizeLabContent = labContent.length();
			for (int slci = 0; slci < nblineString.length() - sizeLabContent; slci++) {
				labContent = " " + labContent;
			}
			// fin de l'alignement des nombres sur la droite.
			final JLabel lab = new JLabel(labContent);
			lab.setFont(
					new Font(content.getFont().getName(), content.getFont().getStyle(), content.getFont().getSize()));
			lab.setAlignmentY(CENTER_ALIGNMENT);

			GridBagConstraints gbc_line = new GridBagConstraints();
			gbc_line.gridx = 1;
			gbc_line.gridy = i - 1;

			lineCount.add(lab, gbc_line);
			lab.addMouseListener(new MouseListener() {
				@Override
				public void mouseReleased(MouseEvent e) {
				}

				@Override
				public void mousePressed(MouseEvent e) {
				}

				@Override
				public void mouseExited(MouseEvent e) {
				}

				@Override
				public void mouseEntered(MouseEvent e) {
				}

				@Override
				public void mouseClicked(MouseEvent e) {
					lab.setForeground(Color.BLUE);
				}
			});

		}
		GridBagConstraints gbc_line = new GridBagConstraints();
		gbc_line.gridx = 2;
		gbc_line.gridy = nbLine;
		lineCount.add(new JLabel(""), gbc_line);

		lineCount.setBorder(content.getBorder());
		lineCount.repaint();
	}


	private void updateErrors() {
		int nbLine = (content.getText() + "a").split("\n").length;
		errorList.sort(new Comparator<JSError>() {

			@Override
			public int compare(JSError e1, JSError e2) {
				return e2.getCriticality().ordinal()-e1.getCriticality().ordinal();
			}
		});
		for (JSError err : errorList) {
			final JButton buterror = new JButton("E");
			buterror.setToolTipText(err.toString());
			buterror.setBorder(null);
			buterror.setBorderPainted(false);
			buterror.setFocusPainted(false);
			buterror.setContentAreaFilled(false);
			buterror.setOpaque(true);
			buterror.setFont(
					new Font(content.getFont().getName(), content.getFont().getStyle(), content.getFont().getSize()));

			if (err.getCriticality() == JSErrorEnumType.CRITICAL) {
				buterror.setBackground(Color.RED);
			} else if (err.getCriticality() == JSErrorEnumType.DEADCODE) {
				buterror.setBackground(Color.DARK_GRAY);
			} else if (err.getCriticality() == JSErrorEnumType.WARNING) {
				buterror.setBackground(Color.YELLOW);
			} else if (err.getCriticality() == JSErrorEnumType.INFO) {
				buterror.setBackground(Color.GREEN);
			}
			GridBagConstraints gbc_line = new GridBagConstraints();
			gbc_line.gridx = 0;
			gbc_line.gridy = err.getLine() - 1;
			lineCount.add(buterror, gbc_line);
		}

		GridBagConstraints gbc_line = new GridBagConstraints();
		gbc_line.gridx = 0;
		gbc_line.gridy = nbLine;
		lineCount.add(new JLabel(""), gbc_line);
		lineCount.repaint();
	}

	public void setText(String t) {
		int posCar = content.getCaretPosition();
		// boolean resetManager = false;
		// if ((content.getText().replaceAll("\\s*", "")).length() == 0 &&
		// undoManager.canUndo() == false) {
		// resetManager = true;
		// } // eleve le bug du undo qui rend le text vide car tiens compe de
		// // l'init du composant
		content.setText(t);
		// if (resetManager)
		// undoManager = new UndoManager();
		syntaxeColorization();
		content.setCaretPosition(posCar);
	};

	public String getText() {
		return content.getText();
	}

	public void verif() {
		if (languageGlue != null) {
			errorList = new ArrayList<JSError>();
			if(languageGlue.getOwner() instanceof JMERule) {
				((JMERule) languageGlue.getOwner()).clearErrors();
			}else if(languageGlue.getOwner() instanceof JMEModeler) {
				((JMEModeler) languageGlue.getOwner()).clearErrors();
			}

			ArrayList<JSError> listErr = Translator.verif(content.getText(), languageGlue, modeler, true);

			for (JSError er : listErr) {
				errorList.add(new JSError(er));
				// TODO: listener Pour les erreurs
				switch (languageGlue.getLangageType()) {
				case RULE:
					((JMERule) languageGlue.getOwner())
					.addError(new JMEError(er, languageGlue, languageGlue.getOwner()));
					break;
				case SCRIPT:
					((JMERule) languageGlue.getOwner())
					.addError(new JMEError(er, languageGlue, languageGlue.getOwner()));
					break;
				case MODELER:
					((JMEModeler) languageGlue.getOwner())
					.addError(new JMEError(er, languageGlue, languageGlue.getOwner()));
					break;
				case EMBEDDING:
					((JMENodeExpression) languageGlue.getOwner()).getNode().getRule()
					.addError(new JMEError(er, languageGlue, languageGlue.getOwner()));
					break;
				default:
					break;
				}
			}

			syntaxeColorization();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// System.out.println(e.getKeyCode() + " : " + e.getKeyChar());
		if (!e.isShiftDown() && e.isControlDown() && e.getKeyCode() == 90) {
			// crtl + z
//			if (undoManager.canUndo())
//				undoManager.undo();
		} else if (e.isShiftDown() && e.isControlDown() && e.getKeyCode() == 90) {
//			if (undoManager.canRedo())
//				undoManager.redo();
		} else if (!(e.isControlDown() && e.isShiftDown())
				&& (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_UP)) {
			if (autoCompletePanel.isVisible()) {
				autoCompletePanel.requestFocus();
				System.err.println("arrow");
			}
		} else if (e.isAltDown() && e.getKeyCode() == 86) {
			// ALT + V
			verif();
		} else if (e.getKeyCode() == 10) {
			// ENTER
			String line = getCurrentLine();
			try {
				String space = "\n" + line.replaceAll("(\\s*).+(.*\\s*)*", "$1");
				if (line.replaceAll("\\s", "").endsWith("{") || line.replaceAll("\\s", "").endsWith(")")) {
					space += "    ";
				}
				document.insertString(content.getCaretPosition(), space, null);
				e.consume();
			} catch (BadLocationException e1) {
				e1.printStackTrace();
			}
		} else if (e.getKeyCode() == 32 && e.isControlDown()) {
			// SPACE
			// printCurrentWord();
			autoCompletePanel.showProposition();
			System.err.println("autocomplete");
		} else if (e.isAltDown() && e.getKeyCode() == 73) {
			// ALT + i
			int carpos = content.getCaretPosition();
			content.setText(reindent(content.getText()));
			content.setCaretPosition(carpos);
		} else if (e.isControlDown() && e.isShiftDown() && e.getKeyCode() == KeyEvent.VK_DOWN) {
			// System.err.println("##");
			// displaceCurrentLine(+1);
		} else if (e.isControlDown() && e.isShiftDown() && e.getKeyCode() == KeyEvent.VK_UP) {
			// System.err.println("##");
			// displaceCurrentLine(-1);
		} else if (e.isShiftDown() && e.isControlDown() && e.getKeyCode() == 513) {
			// ctrl + shift + / : comment line
			int posInText = 0;
			boolean first = true;
			for (String line : content.getText().split("[\n]")) {
				if (line.length() > 0)
					if (posInText + line.length() + 1 >= content.getSelectionStart() && first) {
						first = false;
						// System.out.println("line : " + line);
						// System.out.println(line.replaceFirst("^\\s+", ""));
						try {
							if (line.replaceFirst("^\\s+", "").startsWith("//")) {
								document.remove(posInText + line.indexOf("//"), 2);
								posInText -= 2;
							} else {
								document.insertString(posInText, "//", null);
								posInText += 2;
							}
						} catch (BadLocationException e1) {
							e1.printStackTrace();
						}
					} else if (posInText >= content.getSelectionStart() && posInText <= content.getSelectionEnd()) {
						try {
							if (line.replaceFirst("^\\s+", "").startsWith("//")) {
								document.remove(posInText + line.indexOf("//"), 2);
								posInText -= 2;
							} else {
								document.insertString(posInText, "//", null);
								posInText += 2;
							}
						} catch (BadLocationException e1) {
							e1.printStackTrace();
						}
					}
				posInText += line.length() + 1;

			}
		} else if (e.isControlDown() && e.getKeyCode() == 76) {
			// System.out.println("passÃ© ctrl + l");
			int buf = 0;
			for (String line : (content.getText() + "a").split("[\n]")) {
				if (content.getSelectionStart() == buf
						&& (content.getSelectionEnd() - content.getSelectionStart()) == line.length()) {
					content.setSelectionStart(content.getSelectionStart());
					content.setSelectionEnd(content.getSelectionStart());
					break;
				} else if (content.getSelectionStart() >= buf
						&& content.getSelectionStart() < buf + line.length() + 1) {
					content.setSelectionStart(buf);
					content.setSelectionEnd(buf + line.length());
					break;
				} else
					buf += line.length() + 1; // +1 for \n
			}
		} else if (!e.isControlDown() && e.getKeyCode() == 9) {
			e.consume();
			// TAB pressed
			// area.remove(area.getCaretPosition());
			if (content.getSelectedText() == null || content.getSelectedText() == "") {
				if (!e.isShiftDown())
					try {
						document.insertString(content.getCaretPosition(), "    ", null);
					} catch (BadLocationException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
			}
		} else if ((e.getKeyCode() < 65 || e.getKeyCode() > 90) && e.getKeyCode() != 10
				&& e.getKeyCode() != 8 & e.getKeyCode() != 127 && e.getKeyCode() != 32
				&& (e.getKeyCode() < 96 || e.getKeyCode() > 105)) {
			// System.out.println(e.getKeyCode());
			return;
		}

		syntaxeColorization();
	}

	public int getCurLineNum() {
		final int carPos = content.getCaretPosition();
		int linecpt = 1;
		int cptCar = 0;
		String line = content.getText();
		while (line.length() > 0) {
			int endLine = line.length();
			if (line.indexOf("\n") != -1)
				endLine = line.indexOf("\n");

			if (carPos >= cptCar && carPos <= cptCar + endLine) {
				break;
			}
			if (line.indexOf("\n") != -1) {
				cptCar += line.indexOf("\n") + 1;
			}
			if (line.contains("\n"))
				line = line.substring(line.indexOf("\n") + 1, line.length());
			else
				line = "";
			linecpt++;
		}
		return linecpt;
	}

	/**
	 * ne marche pas pour l'instant
	 *
	 * @param l
	 */
	public void displaceCurrentLine(final int l) {
		final int curLineNumber = getCurLineNum();
		if (l < 0 && curLineNumber <= 1)
			return;
		final String currentLine = getCurrentLine();
		final int carPos = content.getCaretPosition();
		int linecpt = 1;
		int cptCar = 0;
		String line = content.getText();
		while (line.length() > 0) {
			if (linecpt == curLineNumber + l) {
				try {
					document.insertString(cptCar, currentLine + "\n", null);
				} catch (BadLocationException e) {
				}

				if (l > 0) {
					content.setCaretPosition(carPos + currentLine.length());
					break;
				} else
					content.setCaretPosition(carPos - currentLine.length());
			} else if (linecpt == curLineNumber) {
				try {
					if (l > 0)
						document.remove(cptCar, currentLine.length());
					else
						document.remove(cptCar + currentLine.length(), currentLine.length());
				} catch (BadLocationException e) {
				}
				if (l < 0)
					break;
			}
			if (line.indexOf("\n") != -1) {
				cptCar += line.indexOf("\n") + 1;
			}
			if (line.contains("\n"))
				line = line.substring(line.indexOf("\n") + 1, line.length());
			else
				line = "";
			linecpt++;
		}
	}

	public String getCurrentLine() {
		if (getCurLineNum() > 0)
			return (content.getText() + "a").replaceAll("\\n", " \n").split("[\\n]")[getCurLineNum() - 1];
		return "";
	}

	private String reindent(String in) {
		String res = "";
		int indentFact = 0;
		int indentPar = 0;
		boolean lineJumped = false;
		boolean inShortComment = false;
		boolean inLongComment = false;
		boolean inString = false;
		Character lastChar = ' ';
		for (Character c : in.toCharArray()) {

			if (!inString && !inLongComment && !inShortComment && c.compareTo('}') == 0) {
				indentFact--;
			} else if (!inString && !inLongComment && !inShortComment && c.compareTo('{') == 0) {
				indentFact++;
			}

			if (lineJumped && c.compareTo(' ') != 0 && c.compareTo('\t') != 0 && c.compareTo('\n') != 0) {
				// res += "/*" + indentFact + ";" + indentPar + "*/";
				for (int i = 1; i <= indentFact * 4 + indentPar * 2; i++) {
					res += " ";
				}
				lineJumped = false;
				res += c;
			} else if (!lineJumped) {
				if (inShortComment && (c.compareTo(' ') == 0 || c.compareTo('\t') == 0)
						&& lastChar.compareTo('/') == 0) {
					continue;
				}
				res += c;
			}

			if (!inString && !inLongComment && !inShortComment && c.compareTo(')') == 0) {
				indentPar--;
			} else if (!inString && !inLongComment && !inShortComment && c.compareTo('(') == 0) {
				indentPar++;
			} else if (!inLongComment && c.compareTo('\n') == 0) {
				if (lineJumped)
					res += c;
				lineJumped = true;
				inShortComment = false;
			} else if (c.compareTo('/') == 0 && lastChar.compareTo('/') == 0) {
				res += " ";
				inShortComment = true;
			} else if (c.compareTo('*') == 0 && lastChar.compareTo('/') == 0) {
				inLongComment = true;
			} else if (c.compareTo('/') == 0 && lastChar.compareTo('*') == 0) {
				inLongComment = false;
			} else if (c.compareTo('"') == 0 && lastChar != '\\') {
				inString = !inString;
			}
			lastChar = c;

		}
		return res;
	}

	@Override
	public void setMinimumSize(java.awt.Dimension minimumSize) {
		// content.setMinimumSize(minimumSize);
	};

	@Override
	public void setPreferredSize(java.awt.Dimension preferredSize) {
		// content.setPreferredSize(new Dimension(preferredSize.width - 30,
		// preferredSize.height - 40));
		super.setPreferredSize(preferredSize);
	};

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_F || e.getKeyCode() == KeyEvent.VK_H)) {
			if (findRepPanel == null)
				findRepPanel = new FindAndReplacePanel(this);
			if (content.getSelectedText() != null && (content.getSelectedText().replaceAll("\\s*", "")).length() > 0) {
				findRepPanel.setTextToFind(content.getSelectedText());
			}
			findRepPanel.setVisible(true);
		}
		syntaxeColorization();
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent arg0) {
		if (arg0.isControlDown()) {
			if (arg0.getWheelRotation() > 0) {
				zoom(-1);
			} else {
				zoom(1);
			}
		} else {
			scrolP.getVerticalScrollBar().setValue((int) (scrolP.getVerticalScrollBar().getValue()
					+ content.getFont().getSize() * 1.2 * arg0.getWheelRotation()));
		}
	}

	public void zoom(int factor) {
		fontSizeDelta += factor;
		if (fontSize + fontSizeDelta < 0)
			fontSizeDelta = -fontSize;
		for (Style st : stylesList) {
			st.addAttribute(StyleConstants.FontSize, fontSize + fontSizeDelta);
		}
		content.setFont(document.getFont(normal));
		syntaxeColorization();
		revalidate();
		repaint();
	}

	@Override
	public void caretUpdate(CaretEvent ce) {
		int linecpt = 1;
		int cptCar = 0;
		String line = content.getText();
		caretPosIndex = ce.getDot();
		previousChar = null;
		while (line.length() > 0) {
			int endLine = line.length();
			if (line.indexOf("\n") != -1)
				endLine = line.indexOf("\n");

			if (ce.getDot() >= cptCar && ce.getDot() <= cptCar + endLine) {
				int carretColPos = ce.getDot() - cptCar;
				if(carretColPos >= 1) {
					previousChar = line.charAt(carretColPos - 1);
					previousCharPos = new Point(linecpt, carretColPos - 1);
					for(Pair<Character,Character> group : charGroupToApair) {
						if(previousChar.compareTo(group.l())==0 || previousChar.compareTo(group.r())==0) {
							syntaxeColorization();
						}
					}
				}
				posCaret.setText("L " + linecpt + " : C " + (carretColPos));				
				break;
			}
			if (line.indexOf("\n") != -1) {
				cptCar += line.indexOf("\n") + 1;
			}
			if (line.contains("\n"))
				line = line.substring(line.indexOf("\n") + 1, line.length());
			else
				line = "";
			linecpt++;
		}
		updateCurrentLineColor();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON2) { // le 'coller' de linux
			syntaxeColorization();
		}
		if (e.isControlDown()) { // le ctrl + click ouvre la rÃ¨gle sous le
			// curseur
			String clickedWord = getCurrentWord();
			for (JMERule r : modeler.getRules()) {
				if (r.getName().compareTo(clickedWord) == 0) {
					final Set<JMEElementView> modelerView = new HashSet<>();
					for (final JMEElementView el : modeler.getViews()) {
						modelerView.add(el);
					}
					for (final JMEElementView el : modelerView) {
						if (el instanceof JerboaModelerEditor) {
							((JerboaModelerEditor) el).openRule(r);
						}
					}
				}
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	public String getCurrentWord() {
		int carPos = content.getCaretPosition();
		String reversedStringBefore = new StringBuilder(content.getText().substring(0, carPos)).reverse().toString();
		String stringAfter = new StringBuilder(content.getText().substring(carPos)).toString();

		String before = reversedStringBefore.replaceFirst("([\\w]+)[\\W](.*\\s*\\)*)*", "$1");
		String after = stringAfter.replaceFirst("([\\w]+)[\\W](.*\\s*\\)*)*", "$1");

		return new StringBuilder(before).reverse() + after;
	}

	public int getCaretPos() {
		return caretPosIndex;
	}

	public JTextPane getContentPanel() {
		return content;
	}

	// public static void main(String[] args) {
	// JFrame f = new JFrame("Test Expression pannel");
	// f.add(new ExpressionPanel("", new JMEModeler("", "", 3)));
	// f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	// f.setMinimumSize(new Dimension(10, 300));
	// f.setLocationRelativeTo(null);
	// f.setVisible(true);
	// }

}
