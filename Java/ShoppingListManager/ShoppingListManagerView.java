/*
 * ShoppingListManagerView.java
 *
 * The Week 7 Programming Assignment.
 * A shopping list application's view (user interface).
 *
 * Copyright 2011 Stephen Sloan.
 * Feel free to use this under the terms of the LGPL v3 (or later if you wish).
 *
 * CS434XP70: Assignment 8, 4/27/11
 */

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.io.File;
import javax.swing.JFileChooser;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.filechooser.FileFilter;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

/**
 * The main UI for the shopping list, implemented as a JFrame
 *
 * @author Stephen Sloan
 * @version 3.0
 */
public class ShoppingListManagerView extends JFrame {

	private final static String frameNameStr = "Shopping List Manager";
	private final static String itemNameStr = "Item Name";
	private final static String quantNameStr = "Quantity";
	private final static String quantLblStr = quantNameStr + ": ";
	private final static String itemLblStr = itemNameStr + ": ";
	private final static String addBtnStr = "Add";
	private final static String delBtnStr = "Delete";
	private final static String clsBtnStr = "Close";
	private final static String readBtnStr = "Read Items";
	private final static String writeBtnStr = "Write Items";
	
	private JLabel topLabel, itemLabel, quantLabel;
	private JTable table;
	private JScrollPane scroll;
	private JButton addButton, delButton, closeButton, readButton, writeButton;
	private ShoppingListAddView addView;
	private ShoppingListTableModel tableModel;
	
	/**
	 * Custructor the the <code>ShoppingListManagerView</code>
	 */
	public ShoppingListManagerView () {
		super(frameNameStr);
		initDisplay();
	}

	/**
	 * Intialize the user interface
	 * Adds components to this frame.
	 */
	private void initDisplay() {
		GridBagConstraints gbc;
		
		// Set up the frame
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new GridBagLayout());

		// Add top label
		topLabel = new JLabel(frameNameStr);
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		gbc.insets = new Insets(5, 0, 10, 0);
		add(topLabel, gbc);

		// Add table
		tableModel = new ShoppingListTableModel();
		table = new JTable(tableModel);
		//table.setTableHeader((Object) headings);
		table.setPreferredScrollableViewportSize(
			new Dimension(320, 288));
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scroll = new JScrollPane(table);
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add(scroll, gbc);

		// Add item label
		itemLabel = new JLabel(itemLblStr);
		itemLabel.setEnabled(false);
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(5, 2, 5, 2);
		add(itemLabel, gbc);
		
		// Add quantity label
		quantLabel = new JLabel(quantLblStr);
		quantLabel.setEnabled(false);
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(5, 2, 5, 2);
		add(quantLabel, gbc);
		
		// Add apply button
		addButton = new JButton(addBtnStr);
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.weightx = 0.5;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add(addButton, gbc);
		
		// Add delete button
		delButton = new JButton(delBtnStr);
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 4;
		gbc.weightx = 0.5;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add(delButton, gbc);
		
		// Add read button
		readButton = new JButton(readBtnStr);
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.weightx = 0.5;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add(readButton, gbc);
		
		// Add write button
		writeButton = new JButton(writeBtnStr);
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 5;
		gbc.weightx = 0.5;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add(writeButton, gbc);
		
		// Add close button
		closeButton = new JButton(clsBtnStr);
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 6;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add(closeButton, gbc);

		// Initialize the add item view
		addView = new ShoppingListAddView();
		
		// Set variables used by inner classes
		final ShoppingListManagerView that = this;
		
		// Add table row selection listener
		ListSelectionModel lsm = table.getSelectionModel();
		lsm.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent le) {
				int rowIndx = table.getSelectedRow();
				String itemLblStr, quantLblStr, itemTest, quantTest;
				if (rowIndx >= 0) {
					itemLabel.setEnabled(true);
					quantLabel.setEnabled(true);
					itemTest = (String) table.getValueAt(rowIndx, 0);
					quantTest = (String) table.getValueAt(rowIndx, 1);
					itemLblStr = (itemTest != null) ? that.itemLblStr + itemTest : that.itemLblStr;
					quantLblStr = (quantTest != null) ? that.quantLblStr + quantTest : that.quantLblStr;
				} else {
					itemLabel.setEnabled(false);
					quantLabel.setEnabled(false);
					itemLblStr = that.itemLblStr;
					quantLblStr = that.quantLblStr;
				}
				itemLabel.setText(itemLblStr);
				quantLabel.setText(quantLblStr);
			}
		});
		
		// Add "Add" button listener
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				addView.showDisplay();
			}
		});
		
		// Add a listener for the save button on the add view
		addView.registerSaveButtonListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				String item = addView.getItemName();
				String quant = addView.getItemQuantity();
				ShoppingListTableModel model = (ShoppingListTableModel) table.getModel();
				int indx = model.appendRow(item, quant);
				addView.clear();
			}
		});

		// Add Delete button listener
		delButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				ShoppingListTableModel model = (ShoppingListTableModel) table.getModel();
				int rowIndx = table.getSelectedRow();
				if (rowIndx >= 0) {
					model.removeRow(rowIndx);
				}
			}
		});
		
		// Add Write button listener
		writeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				JFileChooser fc = new JFileChooser();
				fc.setApproveButtonText("Write");
				fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int option = fc.showOpenDialog(that);
				if (option == JFileChooser.APPROVE_OPTION) {
					File f = fc.getSelectedFile();
					ArrayList<ArrayList<String>> data = tableModel.getData();
					try {
						FileMgr.writeData(data, f);
					} catch(Exception e) {
						System.out.println(e);
					}
				}
			}
		});
		
		// Add Read button listener
		readButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				JFileChooser fc = new JFileChooser();
				fc.setApproveButtonText("Read");
				fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int option = fc.showOpenDialog(that);
				if (option == JFileChooser.APPROVE_OPTION) {
					File f = fc.getSelectedFile();
					try {
						ArrayList<ArrayList<String>> data = FileMgr.readData(f);
						int size = data.size();
						for (int i=0; i < size; i++) {
							ArrayList<String> row = data.get(i);
							String item = row.get(0);
							String quant = row.get(1);
							tableModel.appendRow(item, quant);
						}
					} catch(Exception e) {
						System.out.println(e);
					}
				}
			}
		});

		// Add Close button listener
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				addView.dispose();
				that.dispose();
			}
		});	
	}

	/**
	 * Intialize the "Add New Item" view
	 * This is to initialize the child view, <code>ShoppingListAddView</code>
	 */
	private void initAddView() {
		addView = new ShoppingListAddView();
		addView.initDisplay();
	}
	
	/**
	 * Make this frame visible
	 */
	public void showDisplay() {
		pack();
		setVisible(true);
	}
	
	/**
	 * Make this frame invisible
	 */
	public void hideDisplay() {
		setVisible(false);	
	}
	
	/**
	 * Gets a value from the table given a row and column index
	 *
	 * @param int rowIndex (The Row index to get at)
	 * @param int columnIndex (The column index to get at)
	 * @return String (The value at the specified cell)
	 */
	public String getValueAt(int rowIndex, int columnIndex) {
		return tableModel.getValueAt(rowIndex, columnIndex).toString();
	}
	
	/**
	 * Add a <code>TableModelListener</code> to the internal table model
	 * used by this view.
	 *
	 * @param TableModelListener listener (lister to add to the table)
	 */
	public void addTableModelListener(TableModelListener listener) {
		tableModel.addTableModelListener(listener);
	}
}

/**
 * Custom model for the <code>JTable</code> used in <code>ShoppingListManagerView</code>
 * Uses an ArrayList of ArrayList<String> to store data.
 * This extends AbstractTableModel to implement the custom model for the table.
 *
 * @author Stephen Sloan
 * @version 1.0
 */
class ShoppingListTableModel extends AbstractTableModel {

	private final static String itemNameStr = "Item Name";
	private final static String quantNameStr = "Quantity";
	private final static String[] headings = {itemNameStr, quantNameStr};
	private ArrayList<ArrayList<String>> data; 
	
	/**
	 * Constructor for <code>ShoppingListTableModel</code>
	 */
	ShoppingListTableModel() {
		super();
		data = new ArrayList<ArrayList<String>>();
	}
	
	/**
	 * Get a column name
	 *
	 * @param int c (The index of the column)
	 * @return String (The name of the column)
	 */
	public String getColumnName(int c) {
		return headings[c];	
	}

	/**
	 * Get the value of the cell at rowIndex and columnIndex
	 *
	 * @param int rowIndex (The row index of the desired value)
	 * @param int columnIndex (The column index of the desired value)
	 * @return Object (The value as an <code>Object</code>)
	 */
	public Object getValueAt(int rowIndex, int columnIndex) {
		ArrayList<String> row = data.get(rowIndex);
		Object val = row.get(columnIndex);
		return val;
	}
	
	/**
	 * Set the value of the cell at rowIndex and columnIndex
	 * Warning: If the rowIndex is out of range, the set is ignored.
	 *          Check the size with getRowCount() first to be safe.
	 * 
	 * @param Object aValue (The new value being set)
	 * @param int rowIndex (The row index of the desired value)
	 * @param int columnIndex (The column index of the desired value)
	 */
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		int dataSize = data.size();
		if (rowIndex < dataSize) {
			ArrayList<String> cols = data.get(rowIndex);
			cols.set(columnIndex, (String) aValue);
			data.set(rowIndex, cols);
			fireTableCellUpdated(rowIndex, columnIndex);
			//fireTableRowsUpdated(rowIndex, rowIndex);
		}
	}
	
	/**
	 * Append a row to the table
	 *
	 * @param String itemName (The first column value)
	 * @param String itemQuantity (The second column value)
	 * @return int (The index of the new row)
	 */
	public int appendRow(String itemName, String itemQuantity) {
		int sizeOf = data.size();
		ArrayList<String> newRow = new ArrayList<String>();
		newRow.add(itemName);
		newRow.add(itemQuantity);
		data.add(newRow);
		fireTableRowsInserted(sizeOf, sizeOf);
		return sizeOf;
	}
	
	/**
	 * Remove a row from the table
	 *
	 * @param int rowIndex (Index of the row to remove)
	 */
	public void removeRow(int rowIndex) {
		data.remove(rowIndex);
		fireTableRowsDeleted(rowIndex, rowIndex);
	}
	
	/**
	 * Get the number of rows in the table
	 *
	 * @return int (Number of rows in the table)
	 */
	public int getRowCount() {
		return data.size();
	}
	
	/**
	 * Get the number of columns in the table
	 *
	 * @return int (Number of columns in the table)
	 */
	public int getColumnCount() {
		return 2;	
	}
	
	public ArrayList<ArrayList<String>> getData() {
		return data;	
	}
}


/**
 * Represents the "Add New Item" view
 * Implemented by extending a <code>JFrame</code>
 *
 * @author Stephen Sloan
 * @version 1.0
 */
class ShoppingListAddView extends JFrame {
	
	private final static String frameNameStr = "Add New ITem";
	private final static String nameLblStr = "Item Name:";
	private final static String quantLblStr = "Quantity:";
	private final static String savButStr = "Save";
	private final static String cnclButStr = "Cancel";
	
	private final static int txtFieldCols = 25;
	
	private JLabel nameLabel, quantLabel;
	private JTextField nameField, quantField;
	private JButton saveBut, cnclBut;
	
	/**
	 * Constructor for <code>ShoppingListAddView</code>
	 */
	public ShoppingListAddView() {
		super(frameNameStr);
		initDisplay();
	}
	
	/**
	 * Intialize the GUI.  Starts of invisible.
	 */
	public void initDisplay() {
		GridBagConstraints gbc;
		
		// Set up the frame
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new GridBagLayout());
		
		// Add Name Label
		nameLabel = new JLabel(nameLblStr);
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(5, 5, 5, 5);
		add(nameLabel, gbc);
		
		// Add Name Field
		nameField = new JTextField(txtFieldCols);
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		nameLabel.setLabelFor(nameField);
		gbc.insets = new Insets(5, 5, 5, 5);
		add(nameField, gbc);
		
		// Add Quantity Label
		quantLabel = new JLabel(quantLblStr);
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(5, 5, 5, 5);
		add(quantLabel, gbc);
		
		// Add Quantity Field
		quantField = new JTextField(txtFieldCols);
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(5, 5, 5, 5);
		quantLabel.setLabelFor(quantField);
		add(quantField, gbc);
		
		// Add Save button
		saveBut = new JButton(savButStr);
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.weightx = 0.5;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add(saveBut, gbc);
		
		// Add Cancel button
		cnclBut = new JButton(cnclButStr);
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 4;
		gbc.weightx = 0.5;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add(cnclBut, gbc);
		
		// Set variables used by inner classes
		final ShoppingListAddView that = this;
		final ActionListener hideAL = new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				that.hideDisplay();
			}	
		};
		
		// Add save button listener
		saveBut.addActionListener(hideAL);
		
		// Add cancel button listener
		cnclBut.addActionListener(hideAL);
		
		// Add item name field listener
		nameField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				quantField.requestFocusInWindow();
			}
		});
		
		// Add quantity field listener
		quantField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				saveBut.doClick();	
			}
		});
	}
	
	/**
	 * Make this view visible
	 */
	void showDisplay() {
		pack();
		setVisible(true);
		nameField.requestFocusInWindow();
	}
	
	/**
	 * Make this view invisible
	 */
	void hideDisplay() {
		setVisible(false);
	}
	
	/**
	 * Get the contents of the item name field
	 *
	 * @return String (The item name)
	 */
	String getItemName() {
		return nameField.getText();
	}
	
	/**
	 * Get the contents of the quantity field
	 *
	 * @return String (The quantity)
	 */
	String getItemQuantity() {
		return quantField.getText();
	}
	
	/**
	 * Empty the item name and quantity fields
	 */
	void clear() {
		nameField.setText("");
		quantField.setText("");
	}
	
	/**
	 * Add an <code>ActionListener</code> to the save button
	 */
	void registerSaveButtonListener(ActionListener al) {
		saveBut.addActionListener(al);	
	}
	
	/**
	 * Add an <code>ActionListener</code> to the cancel button
	 */
	void registerCancelButtonListener(ActionListener al) {
		cnclBut.addActionListener(al);
	}
}


/**
 * For the week 7 FileIO programming assignment: This class adds methods
 * for reading and writing program data to files.  These features are 
 * implemented using two static methods, writeData() and readData().
 * Note that readData() does an unchecked cast.  As far as I can tell, there
 * isn't a great alternative to this without changing the data model used.
 * See the comments in the code for more info.
 *
 * @author Stephen Sloan
 * @version 1.0
 */
class FileMgr {

	/**
	 * Write data to a file
	 *
	 * @param ArrayList<ArrayList<String>> data (The data)
	 * @param File outFile (Where to write)
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void writeData (final ArrayList<ArrayList<String>> data, final File outFile) throws FileNotFoundException, IOException {
		final FileOutputStream fStream = new FileOutputStream(outFile);
		final ObjectOutputStream out = new ObjectOutputStream(fStream);
		out.writeObject(data);
		out.close();
		fStream.close();
	}

	/**
	 * Read data from a file
	 *
	 * @param File inFile (Where to read from)
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @return ArrayList<ArrayList<String>> data (The data)
	 */
	@SuppressWarnings({"unchecked"}) // See comment below
	public static ArrayList<ArrayList<String>> readData(final File inFile) throws FileNotFoundException, IOException {
		final FileInputStream fStream = new FileInputStream(inFile);
		final ObjectInputStream in = new ObjectInputStream(fStream);
		ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
		try {
			// This is an unchecked cast!
			// See http://stackoverflow.com/questions/509076/how-do-i-address-unchecked-cast-warnings/
			// for details, but there doesn't seem to be much value in going to great lengths to solve
			// this problem.  No matter what, if it doesn't work, there will be a runtime error.
			data = (ArrayList<ArrayList<String>>) in.readObject();
		} catch(Exception e) {
			System.out.println(e);
		} finally {
			in.close();
			fStream.close();
		}
		return data;
	}
}
