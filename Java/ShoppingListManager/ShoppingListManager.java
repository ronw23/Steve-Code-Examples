/*
 * ShoppingListManager.java
 *
 * The Week 5 Programming Assignment.
 * A shopping list application.
 *
 * Copyright 2011 Stephen Sloan.
 * Feel free to use this under the terms of the LGPL v3 (or later if you wish).
 *
 * CS434XP70: Assignment 6, 4/22/11
 */

import java.util.ArrayList;
import javax.swing.SwingUtilities;
import javax.swing.table.TableModel;
import javax.swing.event.TableModelListener;
import javax.swing.event.TableModelEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * The Shopping List application
 * Stores a shopping list of items and desired quantity in an <code>ArrayList</code>
 *
 * @author Stephen Sloan
 * @version 1.0
 */
public class ShoppingListManager {

	public final static String DEFAULT_FILE_NAME = "ShoppingList.txt";
	
	private ArrayList<Item> items = new ArrayList<Item>();
	private String fileName = DEFAULT_FILE_NAME;

	/**
	 * Constructor that uses the default file name
	 */
	public ShoppingListManager() {}
	
	/**
	 * Constructor that takes a file name
	 */
	public ShoppingListManager(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * Add an <code>Item</code> to the shopping list
	 *
	 * @param Item newItem (being added)
	 */
	public void addItem(Item newItem) {
		items.add(newItem);
	}
	
	/**
	 * Delete an item from the list given an <code>Item</code>
	 *
	 * @param Item remItem (to remove)
	 */
	public void delItem(Item remItem) {
		items.remove(remItem);
	}
	
	/**
	 * Delete an item from the list given it's index
	 *
	 * @param int indx
	 */
	public void delItem(int indx) {
		items.remove(indx);	
	}
	
	/**
	 * Update an item in the list (given an <code>Item</code> with the same item name.
	 *
	 * @param Item upItem (The updated Item)
	 */
	public void updateItem(Item upItem) {
		int size = items.size();
		int rowIndx;
		Item test;
		String upName, testName;
		boolean foundItem = false;

		upName = upItem.getName();
		for (rowIndx=0; rowIndx < size; rowIndx++) {
			test = items.get(rowIndx);
			testName = test.getName();
			if (upName.equals(testName)) {
				foundItem = true;
				break;	
			}
		}
		if (foundItem) {
			items.set(rowIndx, upItem);	
		}
	}
	
	/**
	 * Get an <code>Item</code> from the list
	 * Will return null if not found
	 *
	 * @param String itemName (Item name to search on)
	 * @return Item (The result)
	 */
	public Item getItem(String itemName) {
		Item result = null;
		Item test;
		int size = items.size();
		for (int indx=0; indx < size; indx++) {
			test = items.get(indx);
			if (test.getName().equals(itemName)) {
				result = test;
				break;
			}
		}
		return result;
	}
	
	/**
	 * Get an <code>Item</code> from the list given it's index
	 * 
	 * @param int indx (The index)
	 * @return Item (The result)
	 */
	public Item getItem(int indx) {
		return items.get(indx);
	}
	
	/**
	 * Write the shopping list to a file
	 *
	 * @throws IOException (for file IO woes)
	 */
	public void writeShoppingList() throws IOException {
		FileWriter fstream = new FileWriter(fileName, false);
		BufferedWriter out = new BufferedWriter(fstream);
		out.write("Shopping List:");
		out.newLine();
		for (Item it : items) {
			out.write(it.getName() + "\t" + it.getQuantity());
			out.newLine();
		}
		out.close();
		fstream.close();
	}
	
	/**
	 * Get the ArrayList<Item> used to store the list of <code>Items</code>
	 *
	 * @return ArrayList<Item> (The item list)
	 */
	public ArrayList<Item> getItems() {
		return items;
	}

	/**
	 * The main routine.  Launches the GUI to create the shoppping list then
	 * creates the shopping list file when the GUI is closed.
	 * There is one command line option, the path to the output file.
	 *
	 * @params String[] args (command line arguments)
	 */
	public static void main(String[] args) {
		int argsLen = args.length;
		String fileName;
		
		// Determine the output file name
		if (argsLen > 0) {
			fileName = args[0];	
		}
		else
		{
			fileName = ShoppingListManager.DEFAULT_FILE_NAME;
		}
		
		// Create a ShoppingListManager instance
		final ShoppingListManager slm = new ShoppingListManager(fileName);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				// Create the ShoppingListManager GUI
				final ShoppingListManagerView view = new ShoppingListManagerView();
				
				// Handle GUI item adds and deletes in the GUI
				view.addTableModelListener(new TableModelListener() {
					public void tableChanged(TableModelEvent tme) {
						String itemName, quantity;
						Item newItem;
						int changeType, rowIndx;

						changeType = tme.getType();
						rowIndx = tme.getFirstRow();
						if (changeType == TableModelEvent.DELETE) {
							slm.delItem(rowIndx);
						} else {
							itemName = view.getValueAt(rowIndx, 0);
							quantity = view.getValueAt(rowIndx, 1);
							newItem = new Item(itemName, quantity);
							slm.addItem(newItem);
						}
					}
				});
				
				// Add a WindowListener to write the shopping list on close
				view.addWindowListener(new WindowAdapter() {
					public void windowClosed(WindowEvent we) {
						try {
							slm.writeShoppingList();
						} catch(IOException e) {
							System.out.println("Could not write to file");
						}
					}
				});
				
				// Display the GUI
				view.showDisplay();
			}
		});
		
	}
}


/**
 * The Item class which stores the item name and quantity for the shopping list
 *
 * @author Stephen Sloan
 * @version 1.0
 */
class Item {
	private String name;
	private String quantity;
	
	/**
	 * Constructor that requires the item name and quantity
	 *
	 * @param String name (Item name)
	 * @param String quantity (How much of the item is needed)
	 */
	Item (String name, String quantity)
	{
		this.name = name;
		this.quantity = quantity;
	}
	
	/**
	 * Get the item name
	 *
	 * @return String (the item name)
	 */
	String getName() {
		return name;
	}
	
	/**
	 * Set the item name
	 *
	 * @param String name (the new name)
	 */
	void setName(String name) {
		this.name = name;	
	}
	
	/**
	 * Get the quantity
	 *
	 * @param String (the quantity)
	 */
	String getQuantity() {
		return quantity;
	}
	
	/**
	 * Set the quantity
	 *
	 * @param String quantity (the new quantity)
	 */
	void setQuantity(String quantity) {
		this.quantity = quantity;
	}
}
