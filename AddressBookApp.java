import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

public class AddressBookApp {
    private ArrayList<Contact> contacts;
    private JFrame frame;
    private JTextField firstNameField, lastNameField, contactNumberField, emailField;
    private JTextArea displayArea;

    public AddressBookApp() {
        contacts = new ArrayList<>();
        frame = new JFrame("Address Book");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("First Name:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        firstNameField = new JTextField(20);
        inputPanel.add(firstNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(new JLabel("Last Name:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        lastNameField = new JTextField(20);
        inputPanel.add(lastNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(new JLabel("Contact Number:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        contactNumberField = new JTextField(20);
        inputPanel.add(contactNumberField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        inputPanel.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        emailField = new JTextField(20);
        inputPanel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton addButton = new JButton("Add Contact");
        inputPanel.add(addButton, gbc);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String firstName = firstNameField.getText();
                String lastName = lastNameField.getText();
                String contactNumber = contactNumberField.getText();
                String email = emailField.getText();
                if (!firstName.isEmpty() && !lastName.isEmpty() && !contactNumber.isEmpty() && !email.isEmpty()) {
                    Contact contact = new Contact(firstName, lastName, contactNumber, email);
                    contacts.add(contact);
                    displayContacts();
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(frame, "All fields are required.");
                }
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JButton deleteButton = new JButton("Delete Contact");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String emailToDelete = emailField.getText();
                if (!emailToDelete.isEmpty()) {
                    contacts.removeIf(contact -> contact.getEmail().equalsIgnoreCase(emailToDelete));
                    displayContacts();
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(frame, "Enter an email to delete.");
                }
            }
        });

        JButton updateButton = new JButton("Update Contact");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String emailToUpdate = emailField.getText();
                String newFirstName = firstNameField.getText();
                String newLastName = lastNameField.getText();
                String newContactNumber = contactNumberField.getText();
                if (!emailToUpdate.isEmpty() && !newFirstName.isEmpty() && !newLastName.isEmpty() && !newContactNumber.isEmpty()) {
                    for (Contact contact : contacts) {
                        if (contact.getEmail().equalsIgnoreCase(emailToUpdate)) {
                            contact.setFirstName(newFirstName);
                            contact.setLastName(newLastName);
                            contact.setContactNumber(newContactNumber);
                        }
                    }
                    displayContacts();
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(frame, "All fields are required.");
                }
            }
        });

        JButton searchButton = new JButton("Search Contact");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String emailToSearch = emailField.getText();
                if (!emailToSearch.isEmpty()) {
                    displayArea.setText("");
                    for (Contact contact : contacts) {
                        if (contact.getEmail().equalsIgnoreCase(emailToSearch)) {
                            displayArea.append(contact.toString() + "\n");
                        }
                    }
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(frame, "Enter an email to search.");
                }
            }
        });

        JButton viewAllButton = new JButton("View All Contacts");
        viewAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayContacts();
            }
        });

        buttonPanel.add(deleteButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(viewAllButton);

        displayArea = new JTextArea(10, 40);
        displayArea.setEditable(false);

        frame.setLayout(new BorderLayout());
        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(new JScrollPane(displayArea), BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);

        // Load contacts from CSV file if available
        loadContactsFromCSV("contacts.csv");
        displayContacts();
    }

    private void displayContacts() {
        displayArea.setText("");
        for (Contact contact : contacts) {
            displayArea.append(contact.toString() + "\n");
        }
        // Save contacts to the CSV file
        saveContactsToCSV("contacts.csv");
    }

    private void loadContactsFromCSV(String filename) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String firstName = parts[0].trim();
                    String lastName = parts[1].trim();
                    String contactNumber = parts[2].trim();
                    String email = parts[3].trim();
                    Contact contact = new Contact(firstName, lastName, contactNumber, email);
                    contacts.add(contact);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveContactsToCSV(String filename) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
            for (Contact contact : contacts) {
                writer.write(contact.getFirstName() + "," + contact.getLastName() + "," + contact.getContactNumber() + "," + contact.getEmail() + "\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void clearFields() {
        firstNameField.setText("");
        lastNameField.setText("");
        contactNumberField.setText("");
        emailField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new AddressBookApp();
            }
        });
    }
}

class Contact {
    private String firstName;
    private String lastName;
    private String contactNumber;
    private String email;

    public Contact(String firstName, String lastName, String contactNumber, String email)
{
    this.firstName = firstName;
    this.lastName = lastName;
    this.contactNumber = contactNumber;
    this.email = email;
}

public String getFirstName() {
    return firstName;
}

public void setFirstName(String firstName) {
    this.firstName = firstName;
}

public String getLastName() {
    return lastName;
}

public void setLastName(String lastName) {
    this.lastName = lastName;
}

public String getContactNumber() {
    return contactNumber;
}

public void setContactNumber(String contactNumber) {
    this.contactNumber = contactNumber;
}

public String getEmail() {
    return email;
}

public void setEmail(String email) {
    this.email = email;
}

@Override
public String toString() {
    return "First Name: " + firstName + ", Last Name: " + lastName + ", Contact Number: " + contactNumber + ", Email: " + email;
}
}
