package sistemaNotas;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class DialogResultadoNota
        extends JDialog {

    public DialogResultadoNota(
            JFrame proprietario,
            ResultadoNota resultado) {

        super(
            proprietario,
            "Resultado da Consulta",
            true
        );

        setSize(450, 300);
        setLocationRelativeTo(proprietario);

        JPanel painel = new JPanel(
                new GridLayout(5, 1, 5, 10));

        painel.setBorder(
                BorderFactory.createEmptyBorder(
                        20, 30, 20, 30));

        JLabel lblAluno =
                new JLabel(
                    "Aluno: "
                    + resultado.getAluno());

        JLabel lblRa =
                new JLabel(
                    "RA: "
                    + resultado.getRa());

        JLabel lblDisciplina =
                new JLabel(
                    "Disciplina: "
                    + resultado.getDisciplina());

        JLabel lblProva =
                new JLabel(
                    "Prova: "
                    + resultado.getProva());

        JLabel lblNota =
                new JLabel(
                    "Nota: "
                    + String.format(
                            "%.2f",
                            resultado
                                .getNota()
                                .doubleValue()
                    )
                );

        lblNota.setFont(
                lblNota.getFont()
                       .deriveFont(
                            Font.BOLD,
                            18f
                       )
        );

        painel.add(lblAluno);
        painel.add(lblRa);
        painel.add(lblDisciplina);
        painel.add(lblProva);
        painel.add(lblNota);

        JButton btnFechar =
                new JButton("Fechar");

        btnFechar.addActionListener(
                e -> dispose()
        );

        add(painel, BorderLayout.CENTER);
        add(btnFechar, BorderLayout.SOUTH);
    }
}