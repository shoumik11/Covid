
package gui;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ConfirmPopup extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();

	/**
	 * Create the dialog.
	 */
	public ConfirmPopup(String text, Color c) {
		setModalityType(ModalityType.APPLICATION_MODAL);
		setResizable(false);
		setBounds(100, 100, 265, 147);
		getContentPane().setLayout(null);
		contentPanel.setBounds(0, 0, 259, 118);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel);
		contentPanel.setLayout(null);
		
		JButton btnNewButton = new JButton("Okay");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ConfirmPopup.this.dispose();
			}
		});
		btnNewButton.setBounds(86, 82, 89, 23);
		contentPanel.add(btnNewButton);
		
		JLabel lblNewLabel = new JLabel(text);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(10, 39, 239, 14);
		lblNewLabel.setForeground(c);
		contentPanel.add(lblNewLabel);
		this.setLocationRelativeTo(null);
	}
}
