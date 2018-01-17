//Assignment #: Final
//Student Name: Mihir Jetly
//Class: COMSC-256
//Section: 8352

package fin;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.beans.BeanInfo;
import java.beans.Beans;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.lang.reflect.Method;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.Color;

@SuppressWarnings("serial")
public class JFrameExt extends JFrame implements ActionListener {

	private JPanel contentPane;
	private JLabel[] jlbPropNames = new JLabel[10];
	private JTextField[] jtfPropValues = new JTextField[10];
	@SuppressWarnings("rawtypes")
	private Class classObject = null;
	private PropertyDescriptor[] pd = null;
	private JPanel targetBeanObject = null;
	private PropertyEditor[] pe = new PropertyEditor[10];
	@SuppressWarnings("rawtypes")
	private JComboBox[] jcboSelect = new JComboBox[10];

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JFrameExt frame = new JFrameExt();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public JFrameExt() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(1, 2, 0, 0));

		JPanel jpLeft = new JPanel();
		jpLeft.setBackground(Color.YELLOW);
		contentPane.add(jpLeft);

		JPanel jpRight = new JPanel();
		contentPane.add(jpRight);
		jpRight.setLayout(new BorderLayout(0, 0));

		JPanel jpController = new JPanel();
		jpController.setBackground(Color.MAGENTA);
		jpRight.add(jpController, BorderLayout.NORTH);

		JPanel jpInspector = new JPanel();
		jpRight.add(jpInspector, BorderLayout.CENTER);
		jpInspector.setLayout(new GridLayout(0, 2, 0, 0));

		JPanel jpPropNames = new JPanel();
		jpPropNames.setBackground(Color.PINK);
		jpInspector.add(jpPropNames);
		jpPropNames.setLayout(new GridLayout(10, 1, 0, 0));

		JPanel jpPropValues = new JPanel();
		jpInspector.add(jpPropValues);
		jpPropValues.setLayout(new GridLayout(10, 1, 0, 0));

		JComboBox jcboClassName = new JComboBox();
		jcboClassName.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String className = (String) jcboClassName.getSelectedItem();
				if (className.equals("")) {
					return;
				}
				try {
					targetBeanObject = (JPanel) Beans.instantiate(null, className);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				if (targetBeanObject instanceof JPanel) {
					contentPane.remove(0);
					contentPane.add(targetBeanObject, 0);
					contentPane.validate();
				}
				BeanInfo bi = null;
				try {
					classObject = Class.forName(className);
					bi = Introspector.getBeanInfo(classObject, JPanel.class);
				} catch (Exception ex) {
					ex.printStackTrace();
				}

				// get property editor class object from pd and using it instantiate property
				// editor object

				String propName;
				pd = bi.getPropertyDescriptors();

				for (int i = 0; i < 10; i++) {
					jpPropValues.remove(jtfPropValues[i]);
				}

				for (int i = 0; i < pd.length; i++) {
					Class customEditorClass = pd[i].getPropertyEditorClass();
					PropertyEditor customEditor = null;
					if (customEditorClass != null) {
						try {
							customEditor = (PropertyEditor) customEditorClass.newInstance();
							pe[i] = customEditor;
						} catch (IllegalAccessException ex) {
							ex.printStackTrace();
						} catch (InstantiationException ex) {
							ex.printStackTrace();
						}
					}

					propName = pd[i].getName();
					jlbPropNames[i].setText(propName);
					Method mget = pd[i].getReadMethod();
					Object robj = null;
					try {
						robj = mget.invoke(targetBeanObject, null);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					String sobj = robj.toString();
					if (pe[i].getTags() == null) {
						jtfPropValues[i].setText(sobj);
						jpPropValues.add(jtfPropValues[i]);
					} else {
						String[] tags = pe[i].getTags();
						jcboSelect[i].setEditable(true);
						jcboSelect[i].addItem("");
						for (int j = 0; j < tags.length; j++) {
							jcboSelect[i].addItem(tags[j]);
						}
						jpPropValues.add(jcboSelect[i]);
					}
				} // for
			}
		});
		jcboClassName.setEditable(true);
		jcboClassName.addItem("");
		jcboClassName.addItem("fin.Rect");
		jcboClassName.addItem("fin.Circ");
		jcboClassName.addItem("fin.Ticker");
		jpController.add(jcboClassName);

		for (int i = 0; i < 10; i++) {
			jlbPropNames[i] = new JLabel("");
			jpPropNames.add(jlbPropNames[i]);
		}
		for (int i = 0; i < 10; i++) {
			jtfPropValues[i] = new JTextField(10);
			jpPropValues.add(jtfPropValues[i]);
			// register this object with text field
			jtfPropValues[i].addActionListener(this);
		}
		for (int i = 0; i < 10; i++) {
			jcboSelect[i] = new JComboBox();
			jcboSelect[i].addActionListener(this);
		}
	}

	@SuppressWarnings({ "deprecation", "unused", "rawtypes" })
	@Override
	public void actionPerformed(ActionEvent ev) {
		boolean found = false;
		int i;
		@SuppressWarnings("unused")
		String propName = "", propValue = "";
		// Determine the name, value and index of the property that changed.
		for (i = 0; i < jtfPropValues.length; i++) {
			if (ev.getSource() == jtfPropValues[i]) {
				found = true;
				break;
			}
		}

		if (!found) {
			for (i = 0; i < jtfPropValues.length; i++) {
				if (ev.getSource() == jcboSelect[i]) {
					break;
				}
			}
		}
		// Get the property name and the property value from the
		// JLabel and JTextfield corresponding to the i value

		propName = jlbPropNames[i].getText();
		if (found) {
			propValue = jtfPropValues[i].getText();
		} else {
			propValue = (String) jcboSelect[i].getSelectedItem();
		}

		BeanInfo bi = null;

		try {
			bi = Introspector.getBeanInfo(classObject, JPanel.class);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		Class propType = pd[i].getPropertyType();

		// Get the property type as a String

		String propTypeName = propType.getName();

		// Create Object array for storing parameters

		Object[] params = new Object[1];

		// Get the set method object.

		Method mset = pd[i].getWriteMethod();

		// get property editor class object from pd and using it instantiate property
		// editor object

		for (int j = 0; j < pd.length; j++) {
			String propertyName = pd[j].getName();
			Class customEditorClass = pd[j].getPropertyEditorClass();
			PropertyEditor customEditor = null;

			if (customEditorClass != null) {
				try {
					customEditor = (PropertyEditor) customEditorClass.newInstance();
					pe[j] = customEditor;
				} catch (IllegalAccessException ex) {
					ex.printStackTrace();
				} catch (InstantiationException ex) {
					ex.printStackTrace();
				}
			}
		}

		Method mget = pd[i].getReadMethod();
		Object robj = null;
		try {
			robj = mget.invoke(targetBeanObject, null);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		String saveVal = robj.toString();

		try {
			pe[i].setAsText(propValue);

			// Depending upon property name, create correct parameter object.
			if (propTypeName.equals("int")) {
				params[0] = new Integer(Integer.parseInt(propValue));
			} else if (propTypeName.equals("double")) {
				params[0] = new Double(Double.parseDouble(propValue));
			} else if (propTypeName.equals("boolean")) {
				params[0] = new Boolean(propValue);
			} else if (propTypeName.equals("java.lang.String")) {
				params[0] = propValue;
			}
			mset.invoke(targetBeanObject, params);
		} catch (Exception ex) {
			ex.printStackTrace();
			if (found) {
				jtfPropValues[i].setText(saveVal);
			} else {
				jcboSelect[i].setSelectedItem(saveVal);
			}
		}
	}
}