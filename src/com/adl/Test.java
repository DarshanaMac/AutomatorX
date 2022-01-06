package com.adl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.BadLocationException;

public class Test  {

	public class SuggestionPanel {
		private JList list;
		private JPopupMenu popupMenu;
		private String subWord;
		private final int insertionPosition;

		public SuggestionPanel(JTextArea textarea, int position, String subWord, Point location) {
			this.insertionPosition = position;
			this.subWord = subWord;
			popupMenu = new JPopupMenu();
			popupMenu.removeAll();
			popupMenu.setOpaque(false);
			popupMenu.setBorder(null);
			popupMenu.add(list = createSuggestionList(position, subWord), BorderLayout.CENTER);
			popupMenu.show(textarea, location.x, textarea.getBaseline(0, 0) + location.y);

		}

		public void hide() {
			popupMenu.setVisible(false);
			if (suggestion == this) {
				suggestion = null;
			}
		}

		Map<String, List<String>> map = new HashMap<>();

		private JList createSuggestionList(final int position, final String subWord) {
			// Object[] data = new Object[10];
			// for (int i = 0; i < data.length; i++) {
			// data[i] = subWord + i;
			// }
			Object[] data = new Object[10];
			Object[] dataTemp = { "click", "checkElementPresent", "type", "open", "pause", "quit", "close", "mouseover",
					"select", "selectframe", "call" };
			int j = 0;
			for (int i = 0; i < dataTemp.length; i++) {
				String tmp = dataTemp[i].toString();
				// System.out.println("tmp >>> " + subWord);
				if (tmp.contains(subWord)) {
					// System.out.println("contains >>> " + tmp);
					data[j] = tmp;
					j++;
				}
			}

			/*
			 * block
			 */
			List<String> typeList = new ArrayList<>();
			typeList.add("object");
			typeList.add("text");
			map.put("type", typeList);

			List<String> clickList = new ArrayList<>();
			clickList.add("object");
			map.put("click", clickList);
			List<String> openList = new ArrayList<>();
			openList.add("url");
			map.put("open", openList);
			List<String> pauseList = new ArrayList<>();
			pauseList.add("time");
			map.put("pause", pauseList);
			List<String> quitList = new ArrayList<>();
			map.put("quit", quitList);
			List<String> closeList = new ArrayList<>();
			map.put("close", closeList);
			List<String> mouseoverList = new ArrayList<>();
			mouseoverList.add("object");
			map.put("mouseover", mouseoverList);
			List<String> checkElementPresentList = new ArrayList<>();
			checkElementPresentList.add("object");
			map.put("checkElementPresent", checkElementPresentList);
			List<String> selectList = new ArrayList<>();
			selectList.add("object");
			selectList.add("itemName");
			map.put("select", selectList);
			List<String> selecframetList = new ArrayList<>();
			selecframetList.add("object");
			map.put("selectframe", selecframetList);
			List<String> callList = new ArrayList<>();
			callList.add("function");
			map.put("call", callList);

			JList list = new JList(data);
			list.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
			list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			list.setSelectedIndex(0);
			list.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					// System.out.println(">>>>>>>>>>>>>>>>>>>>>");
					if (e.getClickCount() == 2) {
						insertSelection();
					}
				}
			});
			return list;
		}

		public boolean insertSelection() {
			// System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<");
			String suggesionSet = null;

			if (list.getSelectedValue() != null) {
				try {
					final String selectedSuggestion = ((String) list.getSelectedValue()).substring(subWord.length());
					// textarea.getDocument().insertString(insertionPosition,
					// selectedSuggestion, null);
					suggesionSet = list.getSelectedValue().toString();
					for (Map.Entry<String, List<String>> m : map.entrySet()) {
						// System.out.println("m is : " + m.getKey());
						// System.out.println("sugges set : " + suggesionSet);
						if (m.getKey().equalsIgnoreCase(suggesionSet)) {
							List<String> vals = m.getValue();
							suggesionSet = selectedSuggestion + "\n";
							for (String val : vals) {
								suggesionSet = suggesionSet + " " + val + ":" + "\n";
							}
							// suggesionSet = selectedSuggestion + " \n object:
							// \n text:";
							textarea.getDocument().insertString(insertionPosition, suggesionSet, null);
						}
					}

					return true;
				} catch (BadLocationException e1) {
					e1.printStackTrace();
				}
				hideSuggestion();
			}
			return false;
		}

		public void moveUp() {
			int index = Math.min(list.getSelectedIndex() - 1, 0);
			selectIndex(index);
		}

		public void moveDown() {
			int index = Math.min(list.getSelectedIndex() + 1, list.getModel().getSize() - 1);
			selectIndex(index);
		}

		private void selectIndex(int index) {
			final int position = textarea.getCaretPosition();
			list.setSelectedIndex(index);
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					textarea.setCaretPosition(position);
				};
			});
		}
	}

	private SuggestionPanel suggestion;
	private JTextArea textarea;

	protected void showSuggestionLater() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				showSuggestion();
			}

		});
	}

	protected void showSuggestion() {
		hideSuggestion();
		final int position = textarea.getCaretPosition();
		Point location;
		try {
			location = textarea.modelToView(position).getLocation();
		} catch (BadLocationException e2) {
			e2.printStackTrace();
			return;
		}
		String text = textarea.getText();
		int start = Math.max(0, position - 1);
		while (start > 0) {
			if (!Character.isWhitespace(text.charAt(start))) {
				start--;
			} else {
				start++;
				break;
			}
		}
		if (start > position) {
			return;
		}
		final String subWord = text.substring(start, position);
		if (subWord.length() < 2) {
			return;
		}
		suggestion = new SuggestionPanel(textarea, position, subWord, location);
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				textarea.requestFocusInWindow();
			}
		});
	}

	private void hideSuggestion() {
		if (suggestion != null) {
			suggestion.hide();
		}
	}

	protected void initUI() {
		final JFrame frame = new JFrame();
		frame.setTitle("AutomaterX");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = new JPanel(new BorderLayout());

		final javax.swing.JMenu jMenu1;
		final javax.swing.JMenu jMenu2;
		final javax.swing.JMenuBar jMenuBar1;
		final javax.swing.JMenuItem jMenuItem1;
		final javax.swing.JMenuItem jMenuItem2;
		final javax.swing.JMenuItem jMenuItem3;

		final JMenuBar mb = new JMenuBar();
		panel.add(mb, BorderLayout.NORTH);

		jMenuBar1 = new javax.swing.JMenuBar();
		jMenu1 = new javax.swing.JMenu();
		jMenuItem1 = new javax.swing.JMenuItem();
		jMenuItem2 = new javax.swing.JMenuItem();
		jMenu2 = new javax.swing.JMenu();
		jMenuItem3 = new javax.swing.JMenuItem();

		jMenu1.setText("File");
		jMenuItem1.setText("run");
		jMenuItem2.setText("Open");
		jMenuItem3.setText("Save");

		jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jMenuItem1ActionPerformed(evt);
			}

			List<List<String>> finalStringList = new ArrayList<>();
			List<String> stList = null;
			int tempInt = 0;

			private void jMenuItem1ActionPerformed(ActionEvent evt) {				
				for (String line : textarea.getText().split("\\n")) {
					if (tempInt < 0)
						tempInt = 0;
					if (line.equalsIgnoreCase("call")) {
						// System.out.println(">>> from click");

						stList = new ArrayList<>();
						stList.add(line);
						tempInt = 1;
					} else if (line.equalsIgnoreCase("click")) {
						// System.out.println(">>> from click");

						stList = new ArrayList<>();
						stList.add(line);
						tempInt = 1;
					} else if (line.equalsIgnoreCase("type")) {
						// System.out.println(">>> from type");

						stList = new ArrayList<>();
						stList.add(line);
						tempInt = 2;
					} else if (line.equalsIgnoreCase("open")) {
						// System.out.println(">>> from type");

						stList = new ArrayList<>();
						stList.add(line);
						tempInt = 1;
					} else if (line.equalsIgnoreCase("pause")) {
						// System.out.println(">>> from type");

						stList = new ArrayList<>();
						stList.add(line);
						tempInt = 1;
					} else if (line.equalsIgnoreCase("quit")) {

						stList = new ArrayList<>();
						stList.add(line);
						tempInt = 0;
						// System.out.println(">>> from type");
					} else if (line.equalsIgnoreCase("close")) {
						stList = new ArrayList<>();
						stList.add(line);
						tempInt = 0;

					} else if (line.equalsIgnoreCase("mouseover")) {
						stList = new ArrayList<>();
						stList.add(line);
						tempInt = 1;
					} else if (line.equalsIgnoreCase("checkElementPresent")) {
						stList = new ArrayList<>();
						stList.add(line);
						tempInt = 1;
					} else if (line.equalsIgnoreCase("select")) {
						// System.out.println(">>> from type");

						stList = new ArrayList<>();
						stList.add(line);
						tempInt = 2;
					} else if (line.equalsIgnoreCase("selectframe")) {
						// System.out.println(">>> from type");

						stList = new ArrayList<>();
						stList.add(line);
						tempInt = 2;
					}

					else {
						// System.out.println(">>> else part");
						try {
							stList.add(line);
						} catch (NullPointerException nex) {

						} catch (Exception ex) {
							ex.printStackTrace();
						}
						tempInt--;
					}
					if (tempInt == 0) {
						finalStringList.add(stList);
					}
					// System.out.println("temp int >>> " + tempInt);
				}

				TestBase tb = new TestBase();// FROM UI - Project creation
				
				for (List<String> li : finalStringList) {

					// System.out.println("len >>>" + li.size());
					// System.out.println(">>> " + li.get(0));
					if (li.get(0).equalsIgnoreCase("type")) {
						tb.type(li);
					}
					if (li.get(0).equalsIgnoreCase("open")) {
						// System.out.println("Called open");
						tb.open(li);
					}
					if (li.get(0).equalsIgnoreCase("click")) {
						tb.click(li);
					}
					if (li.get(0).equalsIgnoreCase("pause")) {
						tb.pause(li);
					}
					if (li.get(0).equalsIgnoreCase("quit")) {
						tb.quit(li);
					}
					if (li.get(0).equalsIgnoreCase("close")) {
						tb.close(li);
					}
					if (li.get(0).equalsIgnoreCase("mouseover")) {
						tb.mouseover(li);
					}
					if (li.get(0).equalsIgnoreCase("checkElementPresent")) {
						tb.checkElementPresent(li);
					}
					if (li.get(0).equalsIgnoreCase("select")) {
						tb.select(li);
					}
					if (li.get(0).equalsIgnoreCase("selectframe")) {
						tb.select(li);
					}
					if(li.get(0).equalsIgnoreCase("call")) {
						tb.call(li);
					}
					// for(String str : li){
					// System.out.println("str>>> " + str);
					//
					// }
				}
				finalStringList.clear();

			}

		});

		final JFileChooser chooser = new JFileChooser();
		;

		jMenuItem3.addActionListener(new java.awt.event.ActionListener() {

			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jMenuItem3ActionPerformed(evt);
			}

			private void jMenuItem3ActionPerformed(ActionEvent evt) {
				// https://stackoverflow.com/questions/10083447/selecting-folder-destination-in-java
				String choosertitle = "abc";
				chooser.setCurrentDirectory(new java.io.File("."));
				chooser.setDialogTitle(choosertitle);
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				//
				// disable the "All files" option.
				//
				chooser.setAcceptAllFileFilterUsed(false);
				//
				if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					System.out.println("getCurrentDirectory(): " + chooser.getCurrentDirectory());
					System.out.println("getSelectedFile() : " + chooser.getSelectedFile());
					// save goes here

					String str = JOptionPane.showInputDialog(null, "Give a project name");
					File crunchifyFile = new File(chooser.getSelectedFile() + "\\" + str + ".txt");
					// System.out.println(">>>>>>>>>>>>>. " + str);
					if (!crunchifyFile.exists()) {
						try {
							File directory = new File(crunchifyFile.getParent());
							if (!directory.exists()) {
								directory.mkdirs();
							}
							crunchifyFile.createNewFile();
						} catch (IOException e) {
							e.printStackTrace();
						}

						try {
							// Convenience class for writing character files
							FileWriter crunchifyWriter;
							crunchifyWriter = new FileWriter(crunchifyFile.getAbsoluteFile(), true);

							// Writes text to a character-output stream
							BufferedWriter bufferWriter = new BufferedWriter(crunchifyWriter);
							bufferWriter.write(textarea.getText());
							bufferWriter.close();

						} catch (IOException e) {
							e.printStackTrace();
						}
					}

					// save end
				} else {
					System.out.println("No Selection ");
				}
			}

			public Dimension getPreferredSize() {
				return new Dimension(200, 200);
			}

		});
		jMenu1.add(jMenuItem1);

		jMenu1.add(jMenuItem2);
		jMenu1.add(jMenuItem3);
		// jMenu1.add(jMenuItem2);

		jMenuBar1.add(jMenu1);
		mb.add(jMenu1);

		// jMenu2.setText("");

		// jMenuItem3.setText("");
		// jMenu2.add(jMenuItem3);
		//
		// jMenuBar1.add(jMenu2);
		// mb.add(jMenu2);

		frame.add(jMenuBar1);

		textarea = new JTextArea(24, 80);
		textarea.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
		textarea.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar() == KeyEvent.VK_ENTER) {
					if (suggestion != null) {
						if (suggestion.insertSelection()) {
							e.consume();
							final int position = textarea.getCaretPosition();
							SwingUtilities.invokeLater(new Runnable() {
								@Override
								public void run() {
									try {
										textarea.getDocument().remove(position - 1, 1);
									} catch (BadLocationException e) {
										e.printStackTrace();
									}
								}
							});
						}
					}
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_DOWN && suggestion != null) {
					suggestion.moveDown();
				} else if (e.getKeyCode() == KeyEvent.VK_UP && suggestion != null) {
					suggestion.moveUp();
				} else if (Character.isLetterOrDigit(e.getKeyChar())) {
					showSuggestionLater();
				} else if (Character.isWhitespace(e.getKeyChar())) {
					hideSuggestion();
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {

			}
		});
		panel.add(textarea, BorderLayout.CENTER);
		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				new Test().initUI();
			}
		});
	}

}