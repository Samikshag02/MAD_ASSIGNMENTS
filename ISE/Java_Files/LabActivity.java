package com.example.ise;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

public class LabActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab);

        setupLabClickListeners();
    }

    private void setupLabClickListeners() {
        int[] cardIds = {
            R.id.cardLab1, R.id.cardLab2, R.id.cardLab3, R.id.cardLab4,
            R.id.cardLab5, R.id.cardLab6, R.id.cardLab7, R.id.cardLab8,
            R.id.cardLab9, R.id.cardLab10, R.id.cardLab11, R.id.cardLab12
        };

        for (int i = 0; i < cardIds.length; i++) {
            final int labNumber = i + 1;
            MaterialCardView card = findViewById(cardIds[i]);
            if (card != null) {
                card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showCustomLabDialog(labNumber);
                    }
                });
            }
        }
    }

    private void showCustomLabDialog(int labNumber) {
        String title;
        String info;

        switch (labNumber) {
            case 1:
                title = "Java Programming Lab";
                info = "The JAVA Programming lab is used to perform practicals of Advance C Programming, Data Structures, Information Security, Domain specific mini project and Compiler Construction. The software's that are installed includes Ubuntu OS, Open JDK and GCC packages.\n\nKey Features & Facilities\n• Dell OptiPlex 3020 Machines\n• Microprocessor Anshuman Kit\n• IOT Kit\n• HP Laser Jet 1020 Printer\n• 6 KVA Rudra UPS\n• Courses: Java Programming, JavaScript, Web Development";
                break;
            case 2:
                title = "Advanced Database System Lab";
                info = "The Advance Database System Programming lab is used to perform practicals of Project I, Project II, Advance C Programming, System Programming and Database Engineering. The software's that are installed includes Ubuntu OS, MySQL, Atom editor, Brackets editor, GCC, g++ and Open JDK packages.\n\nKey Features & Facilities\n• Dell OptiPlex 5090 & 390 Machines\n• 6 KVA Renutron UPS\n• HP Laser Jet P1008 Printer";
                break;
            case 3:
                title = "Database Engineering Lab";
                info = "The Database Engineering lab is used to perform practicals of Database Engineering, Web Technology, Java Programming, Domain specific mini project and Computer Network Laboratory. The software's that are installed includes Ubuntu OS, MySQL, MongoDB, GCC, g++ and Open JDK packages.\n\nKey Features & Facilities\n• Dell OptiPlex 3020 Machines\n• 6 KVA Rudra UPS";
                break;
            case 4:
                title = "R Programming Lab";
                info = "The R Programming lab is used to perform practicals of Data Structures, Web Technology, Java Programming and CNN. The software's that are installed includes Ubuntu OS, Rstudio, GCC, g++ and Open JDK packages.\n\nKey Features & Facilities\n• Dell OptiPlex 5090 Machines\n• 6 KVA Renutron UPS";
                break;
            case 5:
                title = "Project Lab";
                info = "The Project lab is used to perform practicals of Project I, Project II, Advance C Programming, System Programming and Database Engineering. The software's that are installed includes Ubuntu OS, MySQL, Atom editor, Brackets editor, GCC, g++ and OpenJDK packages.\n\nKey Features & Facilities\n• Dell OptiPlex 5090 & 390 Machines\n• 6 KVA Renutron UPS\n• HP Laser Jet P1008 Printer";
                break;
            case 6:
                title = "Research Lab";
                info = "The Research Lab is used to perform practicals of Project I/II, Data Structures, Java Programming, Operating System and Domain specific mini project. The software's that are installed includes Ubuntu OS, Atom editor, Brackets editor, PyCharm editor, MySQL, MongoDB, GCC, and Open JDK packages.\n\nKey Features & Facilities\n• Dell OptiPlex 390 Machines\n• 6 KVA Emerson UPS\n• HP 1020 LaserJet Printer";
                break;
            case 7:
                title = "Web Technology Lab";
                info = "The Web Technology lab is used to perform practicals of Web Technology, Advance C Programming, Java Programming, CNN and Domain specific mini project. The software's that are installed includes Ubuntu and Window OS, XAMPP server, Wampserver, Atom editor, Brackets editor, GCC, and Open JDK packages.\n\nKey Features & Facilities\n• Dell OptiPlex 5090 Machines\n• HP Laser Jet M1522 Printer\n• 6 KVA Renutron UPS";
                break;
            case 8:
                title = "Python Programming Lab";
                info = "The Python Programming lab is used to perform practicals of Compiler Construction, BDA, Operating System, Database Engineering and Compiler Construction. The software's that are installed includes Ubuntu OS, PyCharm editor, Atom editor, Brackets editor, GCC, g++ and Open JDK packages.\n\nKey Features & Facilities\n• Dell OptiPlex 3020 Machines\n• 6 KVA Renutron UPS\n• Canon Image Class LBC6030w Printer";
                break;
            case 9:
                title = "C Programming Lab";
                info = "The C Programming lab is used to perform practicals of C language. The software's that are installed includes Ubuntu OS, GCC, g++.\n\nKey Features & Facilities\n• HP 280 GT Machines\n• 6 KVA Rudra UPS";
                break;
            case 10:
                title = "System Programming Lab";
                info = "Laboratory for system programming and low-level software development.\n\nKey Features & Facilities\n• Dell OptiPlex 7000 i7 Machines\n• Courses: Database Engineering, Advanced Database Systems";
                break;
            case 11:
                title = "Mobile Application Development Lab";
                info = "The Mobile Application Development lab is used to perform practical's of Mobile Application development, Data Structures, Java Programming, Information Security and Domain specific mini project. The software's that are installed includes Ubuntu and Window OS, Android studio, Atom editor, Brackets editor, GCC, and Open JDK packages.\n\nKey Features & Facilities\n• Dell OptiPlex 5090 Machines\n• 6 KVA Rudra UPS\n• HP Laser Jet P2055dn printer";
                break;
            case 12:
                title = "C++ Programming Lab";
                info = "The C++ Programming lab is used to perform practicals of C++ language. The software's that are installed includes Ubuntu OS, GCC, g++.\n\nKey Features & Facilities\n• Dell OptiPlex 3020 Machines\n• 6 KVA Renutron UPS.";
                break;
            default:
                title = "Lab Information";
                info = "No specific details available.";
                break;
        }

        // Inflate the custom layout
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_lab_info, null);
        
        TextView txtTitle = dialogView.findViewById(R.id.dialogTitle);
        TextView txtInfo = dialogView.findViewById(R.id.dialogInfo);
        MaterialButton btnClose = dialogView.findViewById(R.id.btnClose);

        txtTitle.setText(title);
        txtInfo.setText(info);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .create();

        // Make the background transparent so the card corners are visible
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
